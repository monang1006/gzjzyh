package com.strongit.oa.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaReportDefinition;
import com.strongit.oa.bo.ToaReportPrivilset;
import com.strongit.oa.bo.ToaReportShowitem;
import com.strongit.oa.bo.ToaSysmanageProperty;
import com.strongit.oa.bo.ToaSysmanageStructure;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.infotable.infoset.InfoSetManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.oa.work.util.WorkFlowBean;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ReportDefineManager {

	private GenericDAOHibernate<ToaReportDefinition, java.lang.String> definitionDao;

	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		definitionDao = new GenericDAOHibernate<ToaReportDefinition, String>(
				session, ToaReportDefinition.class);
	}

	private IWorkflowService workflowService;

	private IEFormService eFormService;

	private InfoSetManager infoSetManager;

	@Autowired
	ReportShowitemManager showitemManager; // 显示项manager

	@Autowired
	ReportPrivilsetManager privilsetManager; // 权限manager

	@Autowired
	IUserService userService;

	/*
	 * 查询定义报表分页
	 */
	public Page<ToaReportDefinition> getPage(Page<ToaReportDefinition> page,
			ToaReportDefinition model) throws SystemException, ServiceException {
		try {
			List<String> list = new ArrayList<String>();
			StringBuffer hql = new StringBuffer();
			hql.append(" from ToaReportDefinition t where 1=1");
			if (model != null && model.getDefinitionName() != null
					&& !"".equals(model.getDefinitionName())) { // 定义报表名
				hql.append(" and t.definitionName like ?");
				model.setDefinitionName(model.getDefinitionName().trim());
				list.add("%" + model.getDefinitionName() + "%");
			}

			if (model != null && model.getDefinitionFormname() != null
					&& !"".equals(model.getDefinitionFormname())) { // 表单名
				hql.append(" and t.definitionFormname like ?");
				model.setDefinitionFormname(model.getDefinitionFormname()
						.trim());
				list.add("%" + model.getDefinitionFormname() + "%");
			}
			if (model != null && model.getDefinitionWorkflowname() != null
					&& !"".equals(model.getDefinitionWorkflowname())) { // 流程名
				hql.append(" and t.definitionWorkflowname like ?");
				model.setDefinitionWorkflowname(model
						.getDefinitionWorkflowname().trim());
				list.add("%" + model.getDefinitionWorkflowname() + "%");
			}
			if (model != null && model.getToaReportSort() != null
					&& model.getToaReportSort().getSortId() != null
					&& !"".equals(model.getToaReportSort().getSortId())) { // 表单名
				hql.append(" and t.toaReportSort.sortId =?");
				list.add(model.getToaReportSort().getSortId());
			}
			if (model != null && model.getToaReportSort() != null
					&& model.getToaReportSort().getSortName() != null
					&& !"".equals(model.getToaReportSort().getSortName())) { // 表单名
				hql.append(" and t.toaReportSort.sortName like ?");
				list.add("%" + model.getToaReportSort().getSortName().trim()
						+ "%");
			}

			if (model != null && model.getDefinitionCreateUserId() != null
					&& model.getDefinitionCreateUserId() != null
					&& !"".equals(model.getDefinitionCreateUserId())) { // 当前用户ID
				hql.append(" and t.definitionCreateUserId =?");
				list.add(model.getDefinitionCreateUserId());
			}

			User user = userService.getCurrentUser();
			TUumsBaseOrg org = userService.getSupOrgByUserIdByHa(user
					.getUserId());
			boolean isView = userService.isViewChildOrganizationEnabeld(); // 是否允许看到下级机构
			if (isView) {
				if (org != null) {
					hql.append(" and t.definitionOrgCode like ?");
					list.add(org.getSupOrgCode() + "%");
				}
			} else {
				if (org != null) {
					hql.append(" and t.definitionOrgId =?");
					list.add(org.getOrgId());
				}
			}

			hql.append(" order by t.definitionSequence asc ");
			if (list != null && list.size() > 0) {
				Object[] obj = new Object[list.size()];
				for (int i = 0; i < list.size(); i++) {
					obj[i] = list.get(i);
				}
				page = definitionDao.find(page, hql.toString(), obj);

			} else {
				page = definitionDao.find(page, hql.toString());
			}
			return page;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { " 定义报表分页列表" });
		}
	}

	/**
	 * 原因：因为之前已经将该条记录取到了session缓存中，用update（游离态对象）方法时发生错误，一个游离态对象和一个持久态对象，具有相同OID
	 * ，因此报错。
	 * 
	 * 解决办法用merge（游离态对象），该方法应该是根据游离态对象的OID，执行select语句，将游离态对象转成了持久化对象，之后update（）
	 * 但是我用的是别人的框架持久化层是不能随便修改的..尽量避免去修改内部的定义的接口.. 想了想 是不是自己在action里查了一遍
	 * 然后Hibernate:session里就存在了游离对象，那么最后有拿这个对象更新的话就出错。 然后我用根据传入的对象ID
	 * 查出来赋给另一个对象然后 交替属性后 更新新的对象 竟然就可以了.. 原理不是很懂..但反正是解决了..忽忽
	 * 
	 * 
	 * hibernate2.17中使用insertOrUpdate()方法
	 * hibernate3.0以上使用merge()来合并两个session中的同一对象
	 */

	/**
	 * 使用 hibernate 3 的 merge 方法. session.merge(newDetail)即可，它会在 session
	 * 缓存中找到持久化对象，把新对象的属性赋过去，再保存原session中的持久化对象。
	 * 如果在session或数据库中没有的对象，用merge方法的话，它也能够帮你把记录 insert 到表中，相当于 save 方法。
	 * 
	 * @param adress
	 * @roseuid 49503F7A01A5
	 */
	/*
	 * 保存定义报表
	 */
	public String save(ToaReportDefinition model) throws SystemException,
			ServiceException {
		try {
			if (model.getDefinitionId() != null
					&& !model.getDefinitionId().equals("")) { // 更新
				definitionDao.getSession().merge(model);// .save(model);
			} else { // 添加
				definitionDao.save(model);
			}
			return model.getDefinitionId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { " 保存定义报表" });
		}
	}

	/*
	 * 删除
	 */
	public void delete(String definitionId) throws SystemException,
			ServiceException {
		try {
			List<ToaReportShowitem> showitemList = getShowitemListByDefinitionId(definitionId);
			List<ToaReportPrivilset> privilsetList = getPrivisetListByDefinitionId(definitionId);
			if (showitemList != null && showitemList.size() > 0) {
				showitemManager.deleteList(showitemList);
			}
			if (privilsetList != null && privilsetList.size() > 0) {
				privilsetManager.deletePrivilsetByList(privilsetList);
			}
			definitionDao.delete(definitionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "删除定义报表" });
		}
	}

	/*
	 * 根据ID获取定义报表
	 */
	public ToaReportDefinition getDefinitionById(String definitionId)
			throws SystemException, ServiceException {
		try {
			ToaReportDefinition modelDefinition = definitionDao
					.get(definitionId);
			return modelDefinition;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据ID获取定义报表" });
		}
	}

	/**
	 * 根据ID获取定义报表中的显示项列表
	 * 
	 * @author zhengzb
	 * @desc 2010-12-16 下午02:17:59
	 * @param definitionId
	 *            定义报表ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportShowitem> getShowitemListByDefinitionId(
			String definitionId) throws SystemException, ServiceException {
		try {
			ToaReportDefinition definition = getDefinitionById(definitionId);
			List<ToaReportShowitem> list = new ArrayList<ToaReportShowitem>();
			Set<ToaReportShowitem> set = definition.getToaReportShowitems();
			Iterator<ToaReportShowitem> sItemIterable = set.iterator();
			if (sItemIterable.hasNext()) {
				while (sItemIterable.hasNext()) {
					ToaReportShowitem showitem = sItemIterable.next();
					if (showitem != null && showitem.getShowitemId() != null
							&& !showitem.getShowitemId().equals("")) {
						list.add(showitem);
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据ID获取定义报表中的显示项列表" });
		}
	}

	/**
	 * 根据ID获取定义报表中的权限列表
	 * 
	 * @author zhengzb
	 * @desc 2010-12-16 下午02:23:07
	 * @param definitionId
	 *            定义报表ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportPrivilset> getPrivisetListByDefinitionId(
			String definitionId) throws SystemException, ServiceException {
		try {
			ToaReportDefinition definition = getDefinitionById(definitionId);
			List<ToaReportPrivilset> list = new ArrayList<ToaReportPrivilset>();
			Set<ToaReportPrivilset> set = definition.getToaReportPrivilsets();
			Iterator<ToaReportPrivilset> sItemIterable = set.iterator();
			if (sItemIterable.hasNext()) {
				while (sItemIterable.hasNext()) {
					ToaReportPrivilset privilset = sItemIterable.next();
					if (privilset != null && privilset.getPrivilsetId() != null
							&& !privilset.getPrivilsetId().equals("")) {
						list.add(privilset);
					}
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据ID获取定义报表中的权限列表" });
		}
	}

	/**
	 * 判断定义报表名称是否存在
	 * 
	 * @author zhengzb
	 * @desc 2010-12-17 上午11:46:19
	 * @param definitionName
	 *            定义报表名
	 * @param definitionId
	 *            定义报表ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isHasDefinitionName(String definitionName,
			String definitionId) throws SystemException, ServiceException {
		try {
			List<ToaReportDefinition> list = new ArrayList<ToaReportDefinition>();
			StringBuffer hql = new StringBuffer();
			hql.append(" from ToaReportDefinition t where t.definitionName=?");
			list = definitionDao.find(hql.toString(), definitionName);
			if (list != null && list.size() > 0) {
				ToaReportDefinition definition = list.get(0);
				if (definitionId != null && !definitionId.equals("")
						&& definitionId.equals(definition.getDefinitionId())) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "判断定义报表名称是否存在" });
		}
	}

	/**
	 * 根据表单ID获取工作流
	 * 
	 * @author zhengzb
	 * @desc 2010-12-17 下午05:04:07
	 * @param definitionFormid
	 *            表单id
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<WorkFlowBean> getWorkflowNameByformId(String definitionFormid)
			throws SystemException, ServiceException {
		try {
			List<WorkFlowBean> workFlowBean = new ArrayList<WorkFlowBean>();
			List<Object[]> list = workflowService
					.getFormRelativeWorkflow(definitionFormid);
			for (Object[] obj : list) {
				WorkFlowBean bean = new WorkFlowBean();
				bean.setWorkFlowId(obj[1].toString());
				bean.setWorkFlowName(obj[0].toString());
				bean.setFormId(definitionFormid);

				workFlowBean.add(bean);
			}
			return workFlowBean;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据表单ID获取工作流" });
		}
	}

	/**
	 * 根据表单ID表单的所有字段
	 * 
	 * @author zhengzb
	 * @desc 2010-12-20 下午03:36:21
	 * @param definitionFormid
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportShowitem> showitemListByFormId(String definitionFormid)
			throws SystemException, ServiceException {
		try {
			List<ToaReportShowitem> list = new ArrayList<ToaReportShowitem>();
			List<EFormField> efList = eFormService
					.getFormTemplateFieldList(definitionFormid);
			if (efList != null && !efList.isEmpty()) {
				for (EFormField eFormField : efList) {
					ToaReportShowitem showitem = new ToaReportShowitem();
					showitem.setShowitemName(eFormField.getFieldname());
					if (!eFormField.getType().equals("Blank")&& !eFormField.getType().equals("Strong.Form.Controls.SingleAccessory")) {
						if (eFormField.getTablename()
								.matches("[A-Z]+([_A-Z])*")
								&& eFormField.getFieldname().matches(
										"[A-Z]+([_A-Z])*")) { // 判断当前表单中的字段名是否符全规则

							if (eFormField.getCaption() != null
									&& !eFormField.getCaption().equals("")) { // 显示项名称
								showitem.setShowitemText(eFormField
										.getCaption());
							} else {
								ToaSysmanageStructure structure = infoSetManager
										.getInfosetModel(eFormField
												.getTablename());
								if (structure != null) {
									Set<ToaSysmanageProperty> set = structure
											.getToaSysmanageProperties();
									Iterator<ToaSysmanageProperty> itemIterator = set
											.iterator();
									if (itemIterator.hasNext()) {
										while (itemIterator.hasNext()) {
											ToaSysmanageProperty property = itemIterator
													.next();
											if (property
													.getInfoItemField()
													.equals(
															eFormField
																	.getFieldname())) {
												showitem
														.setShowitemText(property
																.getInfoItemSeconddisplay());
												break;
											}

										}
									}
								} else {
									showitem.setShowitemText(eFormField
											.getFieldname());
								}

							}
							list.add(showitem);
						}
					}

				}

			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据表单ID表单的所有字段" });
		}
	}

	/**
	 * 页面组装的数据拆分成显示项列表
	 * 
	 * @author zhengzb
	 * @desc 2010-12-22 上午11:50:10
	 * @param showitemData
	 *            页面组装传过来的数据
	 * @param definition
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportShowitem> getShowitemListBySplit(String showitemData,
			ToaReportDefinition definition) throws SystemException,
			ServiceException {
		try {
			List<ToaReportShowitem> list = new ArrayList<ToaReportShowitem>();
			JSONArray array = JSONArray.fromObject(showitemData);
			int count = array.size();
			for (int i = 0; i < count; i++) {
				ToaReportShowitem showitem = new ToaReportShowitem();
				JSONObject jsonObj = array.getJSONObject(i);
				String showitemId = jsonObj.getString("showitemId");
				if (showitemId != null && !showitemId.equals("")) {
					showitem.setShowitemId(showitemId);
				} else {
					showitem.setShowitemId(null);
				}
				showitem.setShowitemIstotal(jsonObj
						.getString("showitemIstotal"));
				showitem.setShowitemName(jsonObj.getString("showitemName"));
				showitem.setShowitemOrderby(jsonObj
						.getString("showitemOrderby"));
				String showitemSequence = jsonObj.getString("showitemSequence");
				if (showitemSequence != null && !showitemSequence.equals("")
						&& !showitemSequence.equals("null")) {
					showitem.setShowitemSequence(new Long(showitemSequence));
				}
				showitem.setShowitemText(jsonObj.getString("showitemText"));
				showitem.setToaReportDefinition(definition);
				list.add(showitem);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "页面组装的数据拆分成显示项列表" });
		}
	}

	/**
	 * 根据报表类型查询定义报表
	 * 
	 * @author zhengzb
	 * @desc 2010-12-22 下午08:06:36
	 * @param sortId
	 *            报表类型 ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportDefinition> getDefinitionListBySortId(String sortId)
			throws SystemException, ServiceException {
		try {
			String hql = " from ToaReportDefinition t where t.toaReportSort.sortId = ? order by t.definitionSequence asc ";

			List<ToaReportDefinition> list = definitionDao.find(hql.toString(),
					sortId);

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据报表类型查询定义报表" });
		}
	}

	/**
	 * 获取所有定义报表
	 * 
	 * @author zhengzb
	 * @desc 2010-12-23 下午04:45:31
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaReportDefinition> getAllList() throws SystemException,
			ServiceException {
		try {
			List<ToaReportDefinition> list = definitionDao.findAll();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取所有定义报表" });
		}
	}

	/**
	 * 根据用户ID查询创建定义报表LIST
	 * 
	 * @author zhengzb
	 * @desc 2010-12-27 上午11:46:56
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<Object[]> getListByUserId(String userId)
			throws SystemException, ServiceException {
		try {
			List<Object[]> objList = new ArrayList<Object[]>();
			String hql = " from ToaReportDefinition t where t.definitionCreateUserId = ? order by t.definitionSequence asc ";

			List<ToaReportDefinition> list = definitionDao.find(hql.toString(),
					userId);
			if (list != null && list.size() > 0) {
				for (ToaReportDefinition definition : list) {
					Object[] obj = new Object[4];
					obj[0] = definition.getDefinitionName();
					obj[1] = definition.getToaReportSort().getSortId();
					obj[2] = definition.getToaReportSort().getSortName();
					obj[3] = definition.getDefinitionId();
					objList.add(obj);
				}
			}
			return objList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "根据用户ID查询创建定义报表LIST" });
		}

	}

	public IWorkflowService getWorkflowService() {
		return workflowService;
	}

	@Autowired
	public void setWorkflowService(IWorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	public IEFormService getEFormService() {
		return eFormService;
	}

	@Autowired
	public void setEFormService(IEFormService formService) {
		eFormService = formService;
	}

	public InfoSetManager getInfoSetManager() {
		return infoSetManager;
	}

	@Autowired
	public void setInfoSetManager(InfoSetManager infoSetManager) {
		this.infoSetManager = infoSetManager;
	}
}
