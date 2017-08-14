/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息业务类
 */
package com.strongit.oa.eformManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.bo.TEJsptemplate;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.formManager.IFormManagerService;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 表单模板管理类
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Aug 22, 2011
 * @classpath	com.strongit.oa.eformManager.EformManagerManager
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Service
@Transactional
@OALogger
public class EformManagerManager {
	
	private GenericDAOHibernate<TEFormTemplate, java.lang.String> eformDao;
	private GenericDAOHibernate<TEJsptemplate, java.lang.String> jspeformDao;
	@Autowired IUserService userService;
	
	@Autowired JdbcTemplate jdbcTemplate;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private IFormManagerService formManagerService;			//郑志斌添加   表单管理接口
	
	@Autowired protected IWorkflowService workflow;	//工作流服务
	
	/**
	 * 注入SessionFactory
	 * @param session		会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		eformDao = new GenericDAOHibernate<TEFormTemplate, java.lang.String>(
				session, TEFormTemplate.class);
		jspeformDao = new GenericDAOHibernate<TEJsptemplate, java.lang.String>(
				session, TEJsptemplate.class);
	}

	@Autowired
	public void setFormManagerService(IFormManagerService formManagerService) {
		this.formManagerService = formManagerService;
	}
	/**
	 * 保存表单模板对象
	 * @param model
	 */
	public void save(TEFormTemplate model) {
		eformDao.save(model);
	}
	
	/**
	 * 删除表单模板对象
	 * @param model
	 */
	public void delete(TEFormTemplate model) {
		eformDao.delete(model);
	}
	
	/**
	 * 获取所有表单的id
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Apr 28, 2012 6:47:13 PM
	 */
	public List getAllTEFormTemplateId() throws DAOException, ServiceException,
	    SystemException {
        	StringBuilder hql;
        	try {
        	    hql = new StringBuilder("select t.id from TEFormTemplate t");
        	    return eformDao.find(hql.toString());
        	} catch (DAOException e) {
        	    throw e;
        	} catch (ServiceException e) {
        	    throw e;
        	} catch (SystemException e) {
        	    throw e;
        	} catch (Exception e) {
        	    throw new SystemException(e);
        	}
    }
	
	/**
	 * 根据表单模板id得到表单模板对象
	 * @param id
	 * @return
	 */
	public TEFormTemplate get(String id) throws DAOException,ServiceException,SystemException {
		StringBuilder hql;
		try {
			if(id == null || "".equals(id)){
				throw new SystemException("电子表单模板id不能为" + id);
			}
			if(id.indexOf(",") != -1){
				String[] infos = id.split(",");
				id = infos[infos.length-1];
			}
			hql = new StringBuilder("from TEFormTemplate t where t.id = ?");
			List list = eformDao.find(hql.toString(),new Long(id));
			if(list != null && !list.isEmpty()) {
				return (TEFormTemplate)list.get(0);
			} else {
				throw new SystemException("找不到模板id为" + id + "的电子表单模板");
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
	}
	
	/**
	* @method getByTitle
	* @author 申仪玲
	* @created 2011-9-21 上午10:30:54
	* @description 根据表单模板title得到表单模板对象
	* @return TEFormTemplate 返回类型
	*/
	@SuppressWarnings("unchecked")
	public TEFormTemplate getByTitle(String title) throws DAOException,ServiceException,SystemException {
		StringBuilder hql;
		try {
			hql = new StringBuilder("from TEFormTemplate t where t.title = ?");
			List<TEFormTemplate> list = eformDao.find(hql.toString(),title);
			if(list != null && !list.isEmpty()) {
				return (TEFormTemplate)list.get(0);
			} else {
				throw new SystemException("找不到模板title为" + title + "的电子表单模板");
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
		
	}

	/**
	 * 根据表单模板id得到表单模板对象,不返回模板内容
	 * @param id		模板id
	 * @return			模板对象
	 */
	public TEFormTemplate getFormTemplateInfo(String id) {
		StringBuilder hql = new StringBuilder("select new TEFormTemplate(id ,title,createTime,modifyTime,creator, mender, type, orgCode) from TEFormTemplate t where t.id = ?");
		return (TEFormTemplate)eformDao.find(hql.toString(),new Long(id)).get(0);
	}
	
	/**
	 * 得到表单模板分页列表
	 * @param page			分页对象
	 * @param model			表单模板对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page searchEform(Page page,TEFormTemplate model){
		StringBuilder hql = new StringBuilder("select t.id,t.title,t.type,t.createTime,t.creator,t.mender,t.modifyTime ,t.orgCode " +
				"from TEFormTemplate t ");
		List<String> params = new LinkedList<String>();

		if((model.getTitle() == null || "".equals(model.getTitle()))&&(model.getType() == null || "".equals(model.getType()))){
			hql.append(" where t.orgCode = 0 or (1=1 ");
		}else{
			hql.append(" where (1=1 ");
		}
		
		String userId = userService.getCurrentUser().getUserId();
		if(!userService.isSystemDataManager(userId)&&!userService.isSystemManager(userId)
				&&!"1".equals(userService.getCurrentUser().getUserIsSupManager())){//是否是分级授权管理员且不是管理员或超级管理员
			TUumsBaseOrg org = userService.getSupOrgByUserIdByHa(userId);
			if(userService.isViewChildOrganizationEnabeld()){//是否允许看到下级机构
				hql.append(" and (t.orgCode like '").append(org.getSupOrgCode()).append("%'");
			}else {
				hql.append(" and (t.orgCode = '").append(org.getSupOrgCode()).append("'");
			}
			if(model.getTitle() != null && !"".equals(model.getTitle())) {
				hql.append(" and t.title like ? ");
				params.add("%" + model.getTitle() + "%");
			}
			if(model.getType() != null && !"".equals(model.getType())) {
				hql.append(" and t.type = ? ");
				params.add(model.getType());
			}
			hql.append(")");
			System.out.println("userService.isSystemManager(userId):"+userService.isSystemManager(userId));
		}else if(userService.isSystemManager(userId) || "1".equals(userService.getCurrentUser().getUserIsSupManager())) {
			//超级管理员、管理员都可以看到全局表单
			if(model.getTitle() != null && !"".equals(model.getTitle())) {
				hql.append(" and t.title like ? ");
				params.add("%" + model.getTitle() + "%");
			}
			if(model.getType() != null && !"".equals(model.getType())) {
				hql.append(" and t.type = ? ");
				params.add(model.getType());
			}
		}
		hql.append(" ) order by t.createTime desc ");
		page = eformDao.find(page, hql.toString(),params.toArray());
		return page;
	}

	/**
	 * 得到大集中模式下的表单模板列表
	 * @param formType				电子表单模板类型
	 * @return						模板列表
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public List<EForm> getFormTemplateList(String formType) throws DAOException ,ServiceException ,SystemException  {
		try{
			StringBuilder hql = new StringBuilder("select t.id,t.title,t.type,t.creator from TEFormTemplate t where (t.orgCode = 0) or (1=1 ");
			if(formType != null) {
				hql.append(" and t.type = '").append(formType).append("'"); 
			}
			String userId = userService.getCurrentUser().getUserId();
			if(!userService.isSystemDataManager(userId)){//是否是分级授权管理员
				if(userService.isViewSetOpen()) {
					TUumsBaseOrg org = userService.getSupOrgByUserIdByHa(userId);
					if(userService.isViewChildOrganizationEnabeld()){//是否允许看到下级机构
						hql.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
					}else {
						hql.append(" and t.orgCode = '").append(org.getSupOrgCode()).append("'");
					}
				}
			}
			hql.append(" ) order by t.modifyTime,t.createTime desc ");
			List<Object[]> list = eformDao.find(hql.toString());
			List<EForm> eforms = new ArrayList<EForm>();
			if(list != null && !list.isEmpty()) {
				for(Object[] form : list) {
					EForm eform = new EForm();
					eform.setCreator((String)form[3]);
					eform.setFlowType((String)form[2]);
					eform.setId(Long.parseLong(form[0].toString()));
					eform.setTitle((String)form[1]);
					eforms.add(eform);
				}
			}
			return eforms;
		} catch (DAOException ex) {
			throw ex;
		}catch (ServiceException ex) {
			throw ex;
		}catch (SystemException ex) {
			throw ex;
		}catch (Exception ex) {
			throw new SystemException(ex);
		}
	}
	public List<EForm> getJspFormTemplateList(String formType) throws DAOException ,ServiceException ,SystemException  {
		try{
			StringBuilder hql = new StringBuilder("select t.id,t.name,t.type,t.creator from TEJsptemplate t ");
			List<Object[]> list = jspeformDao.find(hql.toString());
			List<EForm> eforms = new ArrayList<EForm>();
			if(list != null && !list.isEmpty()) {
				for(Object[] form : list) {
					EForm eform = new EForm();
					eform.setCreator((String)form[3]);
					eform.setFlowType((String)form[2]);
					eform.setId(Long.parseLong(form[0].toString()));
					eform.setTitle((String)form[1]);
					eforms.add(eform);
				}
			}
			return eforms;
		} catch (DAOException ex) {
			throw ex;
		}catch (ServiceException ex) {
			throw ex;
		}catch (SystemException ex) {
			throw ex;
		}catch (Exception ex) {
			throw new SystemException(ex);
		}
	}
	/**
	 * 根据表单ID，查询工作流中，是否引用了表单
	 * @author zhengzb
	 * @desc 
	 * 2010-11-23 下午02:30:22 
	 * @param formId
	 * @return
	 * 返回： true 表单已引用
	 *        false 表单未引用 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Boolean isHasEFormByworkflow(String formId) throws Exception{
		try {
			int count=formManagerService.isHasEFormUsed(formId);
			if(count>0){
				return true;
			}else {
				Object[] toSelectItems = {"processName","processTypeId","processTypeName","processMainFormId","processMainFormBusinessId"};//查询processMainFormBusinessId用于过滤重复的已办任务
				List sItems = Arrays.asList(toSelectItems);
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				
				paramsMap.put("processMainFormId", userService.getCurrentUser().getUserId());//当前用户办理任务
				
				Map orderMap = new HashMap<Object, Object>();
				orderMap.put("processTypeId", "0");
				List list = workflow.getTaskInfosByConditionForList(sItems, paramsMap, orderMap, null, null, null, null);
				if(list!=null&&list.size()>0){
					return true;
				}else {
					paramsMap.clear();
					paramsMap.put("taskFormId", formId);//取已办结任务
					List list1 = workflow.getTaskInfosByConditionForList(sItems, paramsMap, orderMap, null, null, null, null);
					if(list1!=null&&list1.size()>0){
						return true;
					}else {
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SystemException("查询表单是否被引用",e);
		}
	}
	
	/**
	 * 根据表单ID，查询流程草稿中，是否引用了表单
	 * @author yanjian
	 * 2011-11-06 15:52
	 * @param formId
	 * @return
	 * 返回： true 表单已引用
	 *        false 表单未引用 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean isHasEFormByDraft(String formId) throws Exception{
		try {
			List workflowInfoList = workflow.getDraftWorkflow(null);//获取流程草稿
			if(workflowInfoList != null && !workflowInfoList.isEmpty()) {
				for(int i=0;i<workflowInfoList.size();i++) {
					Object[] workflowInfo = (Object[])workflowInfoList.get(i);
					String formIdTemp = workflowInfo[3].toString();
					if(formId.endsWith(formIdTemp)){
						return true;
					}
				}
			}
			return false;
		}catch (Exception e) {
			throw new SystemException("查询表单是否在流程草稿中被引用",e);
		}
	}

	/**
	 * @author 严建
	 * @createTime Aug 24, 2011
	 * @description 根据id删除表单对象
	 */
	public String deleteById(String id) {
		TEFormTemplate model=this.get(id);
		this.delete(model);

		// 删除表单使用信息表的记录 通过判断使用的业务数据中是否包含eform+表单ID
		StringBuilder deleteInfo = new StringBuilder(
				"delete from T_OA_SYSMANAGE_PMANAGER ");
		deleteInfo.append(" where INFO_BUSINESS_ID like 'eform" + id + "'");
		jdbcTemplate.execute(deleteInfo.toString());
		logger.info(deleteInfo.toString());
		return "succ";
	}
	
	/**
	 * 获取表单模板对象
	 * @param formId 表单ID
	 * @return
	 */
	public TEFormTemplate getForm(Long formId)
	{
		return (TEFormTemplate) eformDao.getSession().get(TEFormTemplate.class, formId);
	}
	
	/**
	 * author:luosy 2014-3-25
	 * description: 设置表单所属机构
	 * modifyer:
	 * description:
	 * @param formId
	 * @return
	 */
	public void setFormOrgCode(String formId,String orgCode) throws Exception{
		try {
			TEFormTemplate teform = getForm(new Long(formId));//获取流程草稿
			teform.setOrgCode(orgCode);
			eformDao.save(teform);
		}catch (Exception e) {
			e.printStackTrace();
			throw new SystemException("查询表单是否在流程草稿中被引用",e);
		}
	}
	
}
