package com.strongit.oa.common.business.jbpmtaskextend;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TJbpmTaskExtend;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 
 * 流程任务扩展管理类
 * 
 * @author yanjian
 *
 * Nov 23, 2012 9:33:50 PM
 */
@Service
@Transactional
@OALogger
public class JbpmTaskExtendManager implements IJbpmTaskExtendService{
	GenericDAOHibernate<TJbpmTaskExtend, java.lang.String> dao;
	/**
	 * 注入SessionFactory
	 * 
	 * @param session
	 *            会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		dao = new GenericDAOHibernate<TJbpmTaskExtend, java.lang.String>(
				session, TJbpmTaskExtend.class);
	}
	
	/**
	 * 保存数据
	 * 
	 * @author yanjian
	 * @param model
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 23, 2012 9:39:31 PM
	 */
	public void saveModel(TJbpmTaskExtend model) throws ServiceException,DAOException, SystemException{
		try {
			if(model.getTaskInstanceId() == null || model.getTaskInstanceId()<1){
				throw new IllegalArgumentException("model.taskInstanceId is not null or less than 1 ");
			}
			if(model.getFromUserId() == null || "".equals(model.getFromUserId())){
				throw new IllegalArgumentException("model.fromUserId is not null or Empty String ");
			}
			if(model.getFromUserName() == null || "".equals(model.getFromUserName())){
				throw new IllegalArgumentException("model.fromUserName is not null or less than 1 ");
			}
			if(!TJbpmTaskExtend.matchActionType(model.getAction())){
				throw new IllegalArgumentException(TJbpmTaskExtend.getMatchActionTypeErrorMessage());
			}
			/**
			 * 默认设置为当前时间
			 * */
			if(model.getCreateDate() == null){
				model.setCreateDate(new Date());
			}
			dao.save(model);
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
	 * 
	 * 获取数据
	 * 
	 * @author yanjian
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 23, 2012 9:54:25 PM
	 */
	public TJbpmTaskExtend getModelById(String id) throws ServiceException,DAOException, SystemException{
		try {
			if(id == null || "".equals(id)){
				throw new IllegalArgumentException("id is not null or Empty String ");
			}
			TJbpmTaskExtend model = dao.get(id);
			dao.evict(model);
			return model;
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
	 * 根据一组任务实例id，获取相应的扩展数据
	 * 
	 * 
	 * @author yanjian
	 * @param taskInstanceIds
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 23, 2012 10:01:14 PM
	 */
	@SuppressWarnings("unchecked")
	public List<TJbpmTaskExtend> getInfoForListByTaskInstanceId(Long[] taskInstanceIds) throws ServiceException,DAOException, SystemException{
		try {
			if(taskInstanceIds == null || taskInstanceIds.length == 0){
				throw new IllegalArgumentException("taskInstanceIds is not null or Empty Array ");
			}
			Object[] objs = new Object[taskInstanceIds.length];
			StringBuilder HQL = new StringBuilder(" from TJbpmTaskExtend t where t.taskInstanceId in ( ");
			for(int i=0;i<taskInstanceIds.length;i++){
				HQL.append("?,");
				objs[i] = taskInstanceIds[i];
			}
			HQL.deleteCharAt(HQL.length()-1);
			HQL.append(" ) order by t.createDate ");
			return dao.find(HQL.toString(), objs);
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
	
}
