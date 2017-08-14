package com.strongit.oa.infopub.column;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.bo.ToaInfopublishColumnArticl;
import com.strongit.oa.bo.ToaInfopublishColumnPrivil;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class ColumnManager {
	private GenericDAOHibernate<ToaInfopublishColumn, String> columnDAO;

	private GenericDAOHibernate<ToaInfopublishColumnArticl, String> articlesColumnDAO;

	private GenericDAOHibernate<ToaInfopublishColumnPrivil, String> columnPrivilDAO;

	//工作流服务
	private IWorkflowService workflow;

	//统一用户服务
	private IUserService user;

	public ColumnManager() {
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		columnDAO = new GenericDAOHibernate<ToaInfopublishColumn, String>(
				sessionFactory, ToaInfopublishColumn.class);
		articlesColumnDAO = new GenericDAOHibernate<ToaInfopublishColumnArticl, String>(
				sessionFactory, ToaInfopublishColumnArticl.class);
		columnPrivilDAO = new GenericDAOHibernate<ToaInfopublishColumnPrivil, String>(
				sessionFactory, ToaInfopublishColumnPrivil.class);
	}

	@Autowired
	public void setWorkflow(IWorkflowService workflow) {
		this.workflow = workflow;
	}

	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}

	/**
	 * author:lanlc
	 * description: 获取用户名称
	 * modifyer:
	 * @param userId 用户ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public String getUserName(String userId ,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			return user.getUserNameByUserId(userId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "当前用户信息" });
		}
	}

	/**
	 * author:lanlc
	 * description:获取所有的栏目
	 * modifyer:
	 * @return
	 */
	public List<ToaInfopublishColumn> getColumns() throws SystemException,
			ServiceException {
		try {
			return columnDAO.findAll();
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "栏目管理数据集" });
		}
	}

	/**
	 * author:lanlc
	 * description:获取指定ID的栏目
	 * modifyer:
	 * @param clumnId  栏目ID
	 * @return
	 */
	public ToaInfopublishColumn getColumn(String clumnId,OALogInfo ... loginfos)
			throws SystemException, ServiceException {
		try {
			return columnDAO.get(clumnId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "栏目管理对象" });
		}
	}

	/**
	 * author:lanlc
	 * description:删除指定ID栏目
	 * modifyer:
	 * @param clumnId 栏目ID
	 */
	public void delColumn(String clumnId,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			columnDAO.delete(clumnId);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,
					new Object[] { "删除栏目" });
		}
	}

	/**
	 * author:lanlc
	 * description:保存栏目
	 * modifyer:
	 * @param model
	 */
	public void saveColumn(ToaInfopublishColumn model,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			columnDAO.save(model);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "保存栏目" });
		}
	}

	/**
	 * author:lanlc
	 * description:保存栏目用户权限
	 * modifyer:
	 * @param model 栏目
	 * @param userId 用户ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveColumnPrivil(ToaInfopublishColumn model, String userId,
			String priviltype,OALogInfo ... loginfos)throws SystemException, ServiceException {
		try {
			String[] id = userId.split(",");

			if (id.length > 0) {
				for (int i = 0; i < id.length; i++) {
					ToaInfopublishColumnPrivil columnPrivil = new ToaInfopublishColumnPrivil();
					columnPrivil.setColumnPrivilUserid(id[i]);
					columnPrivil.setToaInfopublishColumn(model);
					columnPrivil.setColumnPrivilType(priviltype);
					columnPrivilDAO.save(columnPrivil);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "保存栏目权限" });
		}
	}

	/**
	 * author:lanlc
	 * description:删除栏目权限
	 * modifyer:
	 * @param columnId 栏目ID
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delColumnPrivil(String columnId,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			String hql = "from ToaInfopublishColumnPrivil t where t.toaInfopublishColumn.clumnId=?";
			Object[] values = { columnId };
			List columnPrivilList = columnPrivilDAO.find(hql, values);
			if (columnPrivilList.size() > 0) {
				for (int i = 0, n = columnPrivilList.size(); i < n; i++) {
					ToaInfopublishColumnPrivil colPrivel = (ToaInfopublishColumnPrivil) columnPrivilList
							.get(i);
					columnPrivilDAO.delete(colPrivel);
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "删除栏目权限" });
		}

	}

	/**
	 * author:lanlc
	 * description: 获取栏目下的权限用户ID
	 * modifyer:
	 * @param clumnId 栏目ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getColumnPrivil(String clumnId, String type,OALogInfo ... loginfos)
			throws SystemException, ServiceException {
		try {
			String hql = "from ToaInfopublishColumnPrivil t where t.toaInfopublishColumn.clumnId=? and t.columnPrivilType=?";
			Object[] values = { clumnId, type };
			List columnPrivilList = columnPrivilDAO.find(hql, values);
			StringBuffer str = new StringBuffer(500);
			if (columnPrivilList.size() > 0) {
				for (int i = 0, n = columnPrivilList.size(); i < n; i++) {
					ToaInfopublishColumnPrivil columnPrivil = (ToaInfopublishColumnPrivil) columnPrivilList
							.get(i);
					str.append(columnPrivil.getColumnPrivilUserid())
							.append(",");
				}
			}
			String userid = str.toString();
			return userid;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,
					new Object[] { "保存栏目权限" });
		}

	}

	/**
	 * author:lanlc
	 * description:获取所有父节点ID为0的栏目
	 * modifyer:
	 * @return
	 */
	public List getParentColumn() throws SystemException, ServiceException {
		try {
			String hql = "from ToaInfopublishColumn t where t.clumnParent=? order by t.clumnCreattime";
			Object[] values = { "0" }; //设置栏目父节点为0
			List parentColumnList = columnDAO.find(hql, values);
			return parentColumnList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取父节点栏目" });
		}
	}

	/**
	 * author:lanlc
	 * description:获取栏目下的子栏目
	 * modifyer:
	 * @param parentId
	 * @return
	 */
	public List getSubColumn(String parentId,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			User curUser = user.getCurrentUser();
			String hql = "from ToaInfopublishColumn t where t.clumnParent=? and t.clumnIsprivate=?";
			Object[] values = { parentId,"1" };
			List<ToaInfopublishColumn> columnList = columnDAO.find(hql, values);
			String privatehql = "select t1 from ToaInfopublishColumn t,ToaInfopublishColumnPrivil t1  where t.clumnParent=? and t.clumnIsprivate=? and t1.columnPrivilUserid=? and t.clumnId=t1.toaInfopublishColumn.clumnId  ";//查询有管理权限的栏目
			Object[] pvalues = { parentId,"0",curUser.getUserId() };
			List columnPrivateList = columnPrivilDAO.find(privatehql, pvalues);
			for (int j = 0; j < columnPrivateList.size(); j++) {
				ToaInfopublishColumnPrivil privil = (ToaInfopublishColumnPrivil) columnPrivateList
						.get(j);
				ToaInfopublishColumn column = getColumn(privil
						.getToaInfopublishColumn().getClumnId());
					columnList.add(column);
				
			}
			return columnList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取子节点栏目" });
		}
	}
	/**webservice 调用
	 * author:lanlc
	 * description:获取栏目下的子栏目
	 * modifyer:
	 * @param parentId
	 * @return
	 */
	public List getSubColumnByUserId(String parentId,String userId,OALogInfo ... loginfos) throws SystemException,
	ServiceException {
		try {
			String hql = "from ToaInfopublishColumn t where t.clumnParent=? and t.clumnIsprivate=?";
			Object[] values = { parentId,"1" };
			List<ToaInfopublishColumn> columnList = columnDAO.find(hql, values);
			String privatehql = "select t1 from ToaInfopublishColumn t,ToaInfopublishColumnPrivil t1  where t.clumnParent=? and t.clumnIsprivate=? and t1.columnPrivilUserid=? and t.clumnId=t1.toaInfopublishColumn.clumnId  ";//查询有管理权限的栏目
			Object[] pvalues = { parentId,"0",userId };
			List columnPrivateList = columnPrivilDAO.find(privatehql, pvalues);
			for (int j = 0; j < columnPrivateList.size(); j++) {
				ToaInfopublishColumnPrivil privil = (ToaInfopublishColumnPrivil) columnPrivateList
				.get(j);
				ToaInfopublishColumn column = getColumn(privil
						.getToaInfopublishColumn().getClumnId());
				columnList.add(column);
				
			}
			return columnList;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取子节点栏目" });
		}
	}

	/**
	 * author:lanlc
	 * description:获取栏目名称
	 * modifyer:
	 * @param clumnId 栏目ID
	 * @return
	 */
	public String getClumnName(String clumnId,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			ToaInfopublishColumn column = getColumn(clumnId);
			if(column!=null){
			  return column.getClumnName();
			}
			else 
				return null;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取栏目名称" });
		}
	}

	/**
	 * author:lanlc
	 * description: 判断指定栏目下面是否存在文章
	 * modifyer:
	 * @param clumnId 栏目ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean columnArticl(String clumnId,OALogInfo ... loginfos)throws SystemException,
			ServiceException {
		try {
			String hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId=?";
			Object[] values = { clumnId };
			List columnArticlList = articlesColumnDAO.find(hql, values);
			if (columnArticlList.size() > 0) { //栏目下存在文章
				return true;
			} else {
				return false;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "判断栏目下是否有文章" });
		}
	}

	/**
	 * author:lanlc
	 * description: 判断是否成功合并栏目
	 * modifyer:
	 * @param clumnId 栏目ID
	 * @param column  合并到的指定栏目
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean margeColumn(String clumnId, ToaInfopublishColumn column,OALogInfo ... loginfos)
			throws SystemException, ServiceException, AjaxException {
		try {
			String hql = "from ToaInfopublishColumnArticl t where t.toaInfopublishColumn.clumnId=?";
			Object[] values = { clumnId };
			List columnArticlList = articlesColumnDAO.find(hql, values);
			List<ToaInfopublishColumnArticl> colArticlList = new ArrayList<ToaInfopublishColumnArticl>();
			if (columnArticlList.size() > 0) { //存在栏目下有文章
				for (int i = 0; i < columnArticlList.size(); i++) {
					ToaInfopublishColumnArticl columnArticl = (ToaInfopublishColumnArticl) columnArticlList
							.get(i);
					columnArticl.setToaInfopublishColumn(column);
					colArticlList.add(columnArticl);
				}
			}
			if (colArticlList.size() > 0) {
				articlesColumnDAO.save(colArticlList);
			}
			List<ToaInfopublishColumn> columnList=getSubColumn(clumnId);
			if(columnList!=null&&columnList.size()>0){
				for(ToaInfopublishColumn newcolumn:columnList){
					newcolumn.setClumnParent(column.getClumnId());
				}
			}
			columnDAO.save(columnList);
			return true;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "判断合并栏目是否成功" });
		}
	}

	/**
	 * author:lanlc
	 * description:获取用户有权限的栏目
	 * modifyer:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumn> getMyColumn(OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		User curUser = user.getCurrentUser();
		List<ToaInfopublishColumn> columnList = new ArrayList<ToaInfopublishColumn>();
		try {
			//查找所有没有设置权限的栏目
			String columnhql = "from ToaInfopublishColumn t where t.clumnIsprivate=?";
			Object[] values = { "1" };
			List columnWithnotList = new ArrayList();
			columnWithnotList = columnDAO.find(columnhql, values);
			if (columnWithnotList.size() > 0) {
				for (int i = 0, n = columnWithnotList.size(); i < n; i++) {
					columnList.add((ToaInfopublishColumn) columnWithnotList
							.get(i));
				}
			}
			//查询当前用户有权限的栏目
			String privatehql = "from ToaInfopublishColumnPrivil t where t.columnPrivilUserid=?";
			Object[] pvalues = { curUser.getUserId() };
			List columnPrivateList = columnPrivilDAO.find(privatehql, pvalues);
			if (columnPrivateList.size() > 0) {
				for (int j = 0, m = columnPrivateList.size(); j < m; j++) {
					ToaInfopublishColumnPrivil privil = (ToaInfopublishColumnPrivil) columnPrivateList
							.get(j);
					columnList.add(getColumn(privil.getToaInfopublishColumn()
							.getClumnId()));
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取用户有权限的栏目" });
		}
		return columnList;
	}
	
	/**
	 * author:shenyl
	 * description:获取用户有权限的栏目
	 * modifyer:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumn> getMyColumn(String userId) throws SystemException,
			ServiceException {
		List<ToaInfopublishColumn> columnList = new ArrayList<ToaInfopublishColumn>();
		try {
			//查找所有没有设置权限的栏目
			String columnhql = "from ToaInfopublishColumn t where t.clumnIsprivate=?";
			Object[] values = { "1" };
			List columnWithnotList = new ArrayList();
			columnWithnotList = columnDAO.find(columnhql, values);
			if (columnWithnotList.size() > 0) {
				for (int i = 0, n = columnWithnotList.size(); i < n; i++) {
					columnList.add((ToaInfopublishColumn) columnWithnotList
							.get(i));
				}
			}
			//查询当前用户有权限的栏目
			String privatehql = "from ToaInfopublishColumnPrivil t where t.columnPrivilUserid=?";
			Object[] pvalues = { userId };
			List columnPrivateList = columnPrivilDAO.find(privatehql, pvalues);
			if (columnPrivateList.size() > 0) {
				for (int j = 0, m = columnPrivateList.size(); j < m; j++) {
					ToaInfopublishColumnPrivil privil = (ToaInfopublishColumnPrivil) columnPrivateList
							.get(j);
					columnList.add(getColumn(privil.getToaInfopublishColumn()
							.getClumnId()));
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取用户有权限的栏目" });
		}
		return columnList;
	}
	
	/**
	 * author:shenyl
	 * description:获取用户有权限的栏目
	 * modifyer:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaInfopublishColumn> queryMyColumn(Page<ToaInfopublishColumn> page,String userId) throws SystemException,
			ServiceException {
		Object[] values = new Object[2];
		StringBuffer queryStr = new StringBuffer("from ToaInfopublishColumn t where 1=1");	
		queryStr.append(" and ((t.clumnIsprivate=?) or (t.clumnId in (select t2.toaInfopublishColumn.clumnId from ToaInfopublishColumnPrivil t2  where t2.columnPrivilUserid= ? ) ) )");
		values[0] = "1";
		values[1] = userId;		
		queryStr.append(" order by t.clumnCreattime desc");
		return columnDAO.find(page, queryStr.toString(), values);
		
	}
	
	/**
	 * author:qibh
	 * description:获取桌面指定栏目
	 * modifyer:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumn> getChoseColumn(String columnId,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		User curUser = user.getCurrentUser();
		List<ToaInfopublishColumn> columnList = new ArrayList<ToaInfopublishColumn>();
		try {
			//查找所有没有设置权限的栏目
			String columnIds = getchildColumnIdsfortree("",columnId);
			if(columnIds!=null&&!"".equals(columnIds)){
				columnIds =columnIds+","+columnId;
			}else{
				columnIds =columnId;
			}
			if (columnIds!=null&&!"".equals(columnIds)) {
				String[] id = columnIds.split(",");
				for(int i=0;i<id.length;i++){
					ToaInfopublishColumn col = getColumn(id[i]);
					columnList.add(col);
				}
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取用户有权限的栏目" });
		}
		return columnList;
	}
	/***
	 * 获取用户有权限修改的栏目
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaInfopublishColumn> getMyUpdateColumn(OALogInfo ... loginfos)
			throws SystemException, ServiceException {
		User curUser = user.getCurrentUser();
		List<ToaInfopublishColumn> columnList = new ArrayList<ToaInfopublishColumn>();
		List<String> columnIds = new ArrayList<String>();
		try {
			//查找所有没有设置权限的栏目
			String columnhql = "from ToaInfopublishColumn t where t.clumnIsprivate=?  order by t.clumnCreattime";
			Object[] values = { "1" };
			List columnWithnotList = new ArrayList();
			columnWithnotList = columnDAO.find(columnhql, values);
			if (columnWithnotList.size() > 0) {
				for (int i = 0, n = columnWithnotList.size(); i < n; i++) {
					ToaInfopublishColumn pc = (ToaInfopublishColumn) columnWithnotList
							.get(i);
					columnIds.add(pc.getClumnId());
					columnList.add(pc);
				}
			}
			//查询当前用户有权限的栏目
			String privatehql = "from ToaInfopublishColumnPrivil t where t.columnPrivilUserid=?  ";//查询有管理权限的栏目
//			String privatehql2 = "from ToaInfopublishColumnPrivil t where t.columnPrivilUserid=? and t.columnPrivilType='0'";//查询有查看权限的栏目
			Object[] pvalues = { curUser.getUserId() };
			List columnPrivateList = columnPrivilDAO.find(privatehql, pvalues);
//			List columnPrivateList2 = columnPrivilDAO.find(privatehql2, pvalues);
//			if (columnPrivateList2!=null && columnPrivateList2.size()>0) { //判断如果有管理权限也有修改权限的话，则将其保存一个。
//				for (int j = 0; j < columnPrivateList.size(); j++) {
//					ToaInfopublishColumnPrivil t =(ToaInfopublishColumnPrivil) columnPrivateList.get(j);
//					for (int k = 0; k < columnPrivateList2.size(); k++) {
//						ToaInfopublishColumnPrivil t2 =(ToaInfopublishColumnPrivil) columnPrivateList2.get(k);
//						if(t.getToaInfopublishColumn().getClumnId()==t2.getToaInfopublishColumn().getClumnId()){
//							
//						}
//					}
//					if(columnPrivateList2.contains(t)){
//					columnPrivateList2.remove(t);
//						}
//					}
//				}
				for (int j = 0; j < columnPrivateList.size(); j++) {
					ToaInfopublishColumnPrivil privil = (ToaInfopublishColumnPrivil) columnPrivateList
							.get(j);
					
					String clumnId = privil
							.getToaInfopublishColumn().getClumnId();
					if(!columnIds.contains(clumnId)){
						ToaInfopublishColumn column = getColumn(clumnId);
						columnList.add(column);
						columnIds.add(clumnId);
					}
					
				}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取用户有修改权限的栏目" });
		}
		return columnList;
	}

	/**
	 * author:lanlc
	 * description:根据流程类型Id得到该类型下用户有权限的流程信息
	 * modifyer:
	 * @param processTypeId -流程类型Id
	 * @return List<Object[]> 流程类型下有权限的流程信息集<br>
	 * 		<p>数据结构：</p>
	 * 		<p>Object[]{流程名称, 流程Id}</p>
	 * @return
	 */
	public List<Object[]> getProcessOwnedByProcessType(String processTypeId,OALogInfo ... loginfos) {
		return workflow.getProcessOwnedByProcessType(processTypeId);
	}

	/**
	 * author:lanlc
	 * description: 获取栏目下的流程名称
	 * modifyer:
	 * @param clumnId 流程ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getProcessName(String clumnId,OALogInfo ... loginfos) throws SystemException,
			ServiceException {
		try {
			ToaInfopublishColumn column = getColumn(clumnId);
			String processName = column.getProcessName();
			return processName;
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取栏目下的流程" });
		}
	}
	
	/**
	 * @author:luosy
	 * @description:	根据当前栏目id所对应的所有父栏目id
	 * @date : 2012-6-11
	 * @modifyer:
	 * @description:
	 * @param columnId 当前栏目id
	 * @param parentIds	所有父栏目id
	 * @return	String id1,id2,id3,id4,……,idn
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getparentColumnIds(String columnId,String parentIds) throws SystemException,
			ServiceException {
		try {
			String hql = "select t.clumnParent from ToaInfopublishColumn t where t.clumnId=?";
			List parentIdList = columnDAO.find(hql, columnId);
			if(parentIdList!=null&&parentIdList.size()>0){
				columnId = parentIdList.get(0).toString();
				parentIds += parentIdList.get(0)+",";
				return getparentColumnIds(columnId,parentIds);
			}else{
				if(!"".equals(parentIds)&&parentIds!=null&&parentIds.length()>0){
					parentIds = parentIds.substring(0,parentIds.length()-1);
				}else{
					parentIds = "";
				}
				return parentIds;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取栏目下的流程" });
		}
	}
	
	/**
	 * @author:qibh
	 * @description:	根据当前栏目id所对应的所有子栏目id
	 * @date : 2012-6-26
	 * @modifyer:
	 * @description:
	 * @param columnId 当前栏目id
	 * @param childIds	所有子栏目id
	 * @return	String id1,id2,id3,id4,……,idn
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getchildColumnIds(String childIds,String columnId) throws SystemException,
			ServiceException {
		try {
			String hql = "select t.clumnId from ToaInfopublishColumn t where t.clumnParent=?";
			List childList = columnDAO.find(hql, columnId);
			if(childList!=null&&childList.size()>0){
				columnId = childList.get(0).toString();
				childIds += "'"+childList.get(0)+"'"+",";
				return getchildColumnIds(childIds,columnId);
			}else{
				if(!"".equals(childIds)&&childIds!=null&&childIds.length()>0){
					childIds = childIds.substring(0,childIds.length()-1);
				}else{
					childIds = "";
				}
				return childIds;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取栏目下的流程" });
		}
	}
	
	public String getchildColumnIdsfortree(String childIds,String columnId) throws SystemException,
			ServiceException {
		try {
			String hql = "select t.clumnId from ToaInfopublishColumn t where t.clumnParent=?";
			List childList = columnDAO.find(hql, columnId);
			if(childList!=null&&childList.size()>0){
				columnId = childList.get(0).toString();
				childIds += childList.get(0)+",";
				return getchildColumnIdsfortree(childIds,columnId);
			}else{
				if(!"".equals(childIds)&&childIds!=null&&childIds.length()>0){
					childIds = childIds.substring(0,childIds.length()-1);
				}else{
					childIds = "";
				}
				return childIds;
			}
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,
					new Object[] { "获取栏目下的流程" });
		}
}
	
	public String getViewPrivate(String columnId,String privateUserid){
		String result = "1";
		String hql = "select t.columnPrivilType from ToaInfopublishColumnPrivil t  where  t.toaInfopublishColumn.clumnId=? and t.columnPrivilUserid=?";
		List childList = columnPrivilDAO.find(hql, new Object[]{columnId,privateUserid});
		if(childList!=null && childList.size()!=0){
			for(int i=0;i<childList.size();i++){
				if("0".equals(childList.get(i).toString())){
					result = "0";
				}
			}
		}
		return result;
	}
}