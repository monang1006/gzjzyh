package com.strongit.oa.meetingmanage.meetingtopicsort;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMeetingTopicsort;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author Administrator蒋国斌 StrongOA2.0 上午11:50:08
 */
@Service
@Transactional
public class MeetingtopicsortManager extends BaseManager{
	private GenericDAOHibernate<ToaMeetingTopicsort, String> topicsortDao = null;
	 //工作流服务
	private IWorkflowService workflow;

	public MeetingtopicsortManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		topicsortDao = new GenericDAOHibernate<ToaMeetingTopicsort, String>(
				sessionFactory, ToaMeetingTopicsort.class);
	}
	@Autowired
	 public void setWorkflow(IWorkflowService workflow) {
		this.workflow = workflow;
	 }
	
	 /**
	  * author:jgb
	  * description:根据流程类型Id得到该类型下用户有权限的流程信息
	  * modifyer:
	  * @param processTypeId -流程类型Id
	  * @return List<Object[]> 流程类型下有权限的流程信息集<br>
	  * 		<p>数据结构：</p>
	  * 		<p>Object[]{流程名称, 流程Id}</p>
	  * @return
	  */
	 public List<Object[]> getProcessByProcessType(String processTypeId){
		 return workflow.getStartWorkflow(processTypeId);
	 }
	

	/**
	 * 得到一页会议分类列表
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-30 上午11:58:55
	 * @param page
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaMeetingTopicsort> queryTopicsort(
			Page<ToaMeetingTopicsort> page,String querysortId,String sortName,String proName,String sortDes,String sortstatus) throws DAOException,
			SystemException, ServiceException {
		//final String hql = "from ToaMeetingTopicsort";
		StringBuffer  queryStr = new StringBuffer("from ToaMeetingTopicsort t where 1=1");
		if(querysortId!=null && !"".equals(querysortId)){
			queryStr.append(" and t.topicsortNo like '%"+querysortId+"%'");
		}
		if(sortName!=null && !"".equals(sortName)){
			queryStr.append(" and t.topicsortName like '%"+sortName+"%'");
		}
		if(proName!=null && !"".equals(proName)){
			queryStr.append(" and t.processName like '%"+proName+"%'");
		}
		if(sortDes!=null && !"".equals(sortDes)){
			queryStr.append(" and t.topicsortDemo like '%"+sortDes+"%'");
			
		}
		if(sortstatus!=null && !"".equals(sortstatus)){
			queryStr.append(" and t.isDisbled like '%"+sortstatus+"%'");
			
		}
		queryStr.append(" order by t.topicsortNo");
		return topicsortDao.find(page, queryStr.toString());
	}

	/**
	 * 得到议题分类LIST对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-31 上午09:24:55
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaMeetingTopicsort> getTopicsorts() throws DAOException,
			SystemException, ServiceException {
		final String hql = "from ToaMeetingTopicsort t where t.isDisbled='1'";
		return topicsortDao.find(hql);
	}

	/**
	 * 根据会议分类ID得到指定会议分类对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-3-30 下午01:47:57
	 * @param sortId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaMeetingTopicsort getTopicsort(String sortId) throws DAOException,
			SystemException, ServiceException {
		return topicsortDao.get(sortId);
	}
/**
 * 保存会议议题分类
 *@author 蒋国斌
 *@date 2009-3-31 上午09:29:12 
 * @param toSort
 * @throws DAOException
 * @throws SystemException
 * @throws ServiceException
 */
	@Transactional(readOnly = false)
	public void saveTopicsort(ToaMeetingTopicsort toSort) throws DAOException,
			SystemException, ServiceException {
		topicsortDao.save(toSort);
	}
	
	@Transactional(readOnly = false)
	public void updateTopicsort(ToaMeetingTopicsort toSort) throws DAOException,
			SystemException, ServiceException {
		topicsortDao.update(toSort);
	}
/**
 * 支持批量删除会议议题分类
 *@author 蒋国斌
 *@date 2009-3-31 上午09:29:41 
 * @param sortIds
 * @throws DAOException
 * @throws SystemException
 * @throws ServiceException
 */
	@Transactional(readOnly = false)
	public void deleteSorts(String sortIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = sortIds.split(",");
		topicsortDao.delete(ids);
	}
}
