package com.strongit.oa.attendance.apply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaAttenApply;
import com.strongit.oa.bo.ToaAttendCancle;
import com.strongit.oa.bo.ToaBasePerson;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.util.MessagesConst;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class ApplyManager extends BaseManager {
	private GenericDAOHibernate<ToaAttenApply, String> applyDao = null;
	private GenericDAOHibernate<ToaAttendCancle, String> cancleDao = null;
	
	public ApplyManager() {
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		applyDao = new GenericDAOHibernate<ToaAttenApply, String>(
				sessionFactory, ToaAttenApply.class);
		cancleDao = new GenericDAOHibernate<ToaAttendCancle, String>(
				sessionFactory, ToaAttendCancle.class);
	}

	/**
	 * 根据查询条件得到一页申请表单对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-16 下午04:02:48
	 * @param page
	 * @param applyer
	 * @param applyTime
	 * @param applyType
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @param demo
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaAttenApply> queryAttendApply(Page<ToaAttenApply> page,
			String applyer, Date applyTime, String applyType, Date startDate,
			Date endDate, String status,String userid) throws DAOException,
			SystemException, ServiceException {
		StringBuffer queryStr = new StringBuffer(
				"from ToaAttenApply t where 1=1");
		List<Object> list=new ArrayList<Object>();
		if (applyer != null && !"".equals(applyer)) {
			queryStr.append(" and t.applicants like '%" + applyer + "%'");
		}
		if (applyTime != null) {
			queryStr.append(" and t.applyTime>=?");
			list.add(applyTime);
		}
		if (applyType != null && !"".equals(applyType)) {
			queryStr.append(" and t.applyTypeName like ?");
			list.add("%"+applyType+"%");
		}
		if (startDate != null) {
			queryStr.append(" and t.applyStime>=?");
			list.add(startDate);
		} 
		if (endDate != null) {
			queryStr.append(" and t.applyEtime<=?");
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			list.add(endDate);
		}
		if (status != null && !"".equals(status)) {
			queryStr.append(" and t.applyState like " + status);
		}
		if(userid!=null&&!"".equals(userid)){
			queryStr.append(" and t.applyPersonId='"+userid+"'");
		}
		queryStr.append(" order by t.applyTime desc");
		if(list.size()>0){
			Object[] objs=new Object[list.size()];
			for(int i=0;i<list.size();i++){
				objs[i]=list.get(i);
			}
			return applyDao.find(page, queryStr.toString(), objs);
		}
		return applyDao.find(page, queryStr.toString());
	}
	
	/**
	 * 查询销假申请单列表
	 */
	public Page<ToaAttendCancle> queryAttendcancelApply(Page<ToaAttendCancle> page,
			String applyType, Date startDate, Date endDate, Date cancleStime,
			Date cancleEtime, String status,String applyId,String userid) throws DAOException,
			SystemException, ServiceException {
		StringBuffer queryStr = new StringBuffer(
				"from ToaAttendCancle t where 1=1");
		Object[] values = new Object[4];
		if (applyType != null && !"".equals(applyType)) {
			queryStr.append(" and t.toaAttenApply.applyTypeName like '%" + applyType + "%'");
		}
		if (startDate != null && !"".equals(startDate)) {
			queryStr.append(" and t.toaAttenApply.applyStime>=?");
			values[0] = startDate;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		if (endDate != null && !"".equals(endDate)) {
			queryStr.append(" and t.toaAttenApply.applyEtime<=?");
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			values[1] = endDate;
		} else {
			queryStr.append(" and 1=?");
			values[1] = 1;
		}
		if (cancleStime != null && !"".equals(cancleStime)) {
			queryStr.append(" and t.cancleStime>=?");
			values[2] = cancleStime;
		} else {
			queryStr.append(" and 1=?");
			values[2] = 1;
		}
		if (cancleEtime != null && !"".equals(cancleEtime)) {
			queryStr.append(" and t.cancleEtime<=?");
			cancleEtime.setHours(23);
			cancleEtime.setMinutes(59);
			cancleEtime.setSeconds(59);
			values[3] = cancleEtime;
		} else {
			queryStr.append(" and 1=?");
			values[3] = 1;
		}	
		if (status != null && !"".equals(status)) {
			queryStr.append(" and t.cancleState like '%" + status + "%'");
		}
		if(userid!=null&&!"".equals(userid)){
			queryStr.append(" and t.toaAttenApply.applyPersonId='"+userid+"'");
		}
		if(applyId!=null&&!"".equals(applyId)){
			String[] ids=applyId.split(",");
			queryStr.append(" and t.toaAttenApply.applyId in(");
			for(int i=0;i<ids.length;i++){
				if(i==ids.length-1){
					queryStr.append(" '"+ids[i]+"')");
				}else{
					queryStr.append(" '"+ids[i]+"',");
				}
			}
		}
		queryStr.append(" order by t.cancleTime desc");
		return cancleDao.find(page, queryStr.toString(), values);
	}

	/**
	 * 根据申请单ID得到销假单列表
	 *@author 蒋国斌
	 *@date 2009-11-24 上午09:07:36 
	 * @param appId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttendCancle getAttendCanclebyApp(String appId) throws DAOException,
			SystemException, ServiceException {
		final String hql = "from ToaAttendCancle t where t.toaAttenApply.applyId=?";
		List<ToaAttendCancle> cancle=cancleDao.find(hql,appId);
		if(cancle!=null && cancle.size()!=0){
			return cancle.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 得到一个销假单对象
	 *@author 蒋国斌
	 *@date 2009-11-24 上午09:10:59 
	 * @param cancleId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttendCancle getToaAttendCancle(String cancleId) throws DAOException,
			SystemException, ServiceException {
		return cancleDao.get(cancleId);
	}

	/**
	 * 保存销假单
	 *@author 蒋国斌
	 *@date 2009-11-24 上午09:13:44 
	 * @param cancle
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveAttendCancle(ToaAttendCancle cancle) throws DAOException,
			SystemException, ServiceException {
		cancleDao.save(cancle);
	}

	/**
	 * 更新销假单
	 *@author 蒋国斌
	 *@date 2009-11-24 上午09:14:18 
	 * @param apply
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void updateAttendCancle(ToaAttendCancle apply) throws DAOException,
			SystemException, ServiceException {
		cancleDao.update(apply);
	}

	/**
	 * 支持批量删除销假单
	 *@author 蒋国斌
	 *@date 2009-11-24 上午09:15:04 
	 * @param applyIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteAttendCancle(String applyIds) throws DAOException,
			SystemException, ServiceException {
		String[] ids = applyIds.split(",");
		cancleDao.delete(ids);
	}
	
	
	public List<ToaAttendCancle> getAttenCancles(String appId) throws DAOException,SystemException, ServiceException {
		final String hql = "from ToaAttendCancle t where t.toaAttenApply.applyId=?";
		return cancleDao.find(hql,appId);
	}

	/**
	 * 根据ID得到一个申请表单对象
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-16 下午04:03:50
	 * @param applyId
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttenApply getAttenApply(String applyId) throws DAOException,
	SystemException, ServiceException {
		return applyDao.get(applyId);
	}
	
	/**
	 * 根据销假单得到申请单对象
	 *@author 蒋国斌
	 *@date 2009-11-24 上午09:54:43 
	 * @param cancleid
	 * @return
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaAttenApply getAttenApplybyCan(String cancleid) throws DAOException,
	SystemException, ServiceException {
		ToaAttendCancle can=this.getToaAttendCancle(cancleid);
		if(can!=null){
			return can.getToaAttenApply();
		}else{
			return null;
		}
	}

	/**
	 * 保存申请表单到库
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-16 下午04:04:14
	 * @param apply
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void saveAttenApply(ToaAttenApply apply) throws DAOException,
	SystemException, ServiceException {
		applyDao.save(apply);
	}

	/**
	 * 更新申请表单
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-16 下午04:04:31
	 * @param apply
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void updateAttenApply(ToaAttenApply apply) throws DAOException,
	SystemException, ServiceException {
		applyDao.update(apply);
	}

	/**
	 * 支持批量删除申请表单同时删除其下面的销假表单
	 * 
	 * @author 蒋国斌
	 * @date 2009-11-16 下午04:04:43
	 * @param applyIds
	 * @throws DAOException
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void deleteApplys(String applyIds) throws DAOException,
	SystemException, ServiceException {
		String[] ids = applyIds.split(",");
		for(int i=0;i<ids.length;i++){
			List<ToaAttendCancle> cancle=this.getAttenCancles(ids[i]);
			if(cancle!=null && cancle.size()!=0){
				cancleDao.delete(cancle);
			}
		}
		applyDao.delete(ids);
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 26, 2009 4:48:50 PM
	 * Desc:	获取某人员考勤计算时间内的已审核的申请信息列表
	 * param:
	 */
	public List<ToaAttenApply> getApplyList(ToaBasePerson person,Date startDate,Date endDate)throws SystemException, ServiceException {
		try{
			if(person==null){
				return null;
			}
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			String hql="from ToaAttenApply t where t.applyPersonId=? and t.applyStime<=? and t.applyEtime>=? and t.applyState=?";
			List<ToaAttenApply> list=applyDao.find(hql, person.getPersonid(),endDate,startDate,"2");
			return list;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取某人员考勤计算时间内的已审核的申请信息列表"});
		}	
	}

	/*
	 * 
	 * @author: 彭小青
	 * @date: 	Nov 29, 2009 4:04:51 PM
	 * Desc:	获取对应申请单的已审核的销假申请单
	 * param:
	 */
	public List<ToaAttendCancle> getAuditAttenCancles(String appId){
		try{
			String hql = "from ToaAttendCancle t where t.toaAttenApply.applyId=? and t.cancleState=?";
			return cancleDao.find(hql,appId,"2");
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
