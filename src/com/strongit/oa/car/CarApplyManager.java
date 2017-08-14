/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: 
 * modifyer:	luosy
 * Version: V1.0
 * Description：车辆申请信息管理
 */
package com.strongit.oa.car;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaCarApplicant;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       吴为胜
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年4月18日10:31:52
 * @version      1.0.0.0
 * @comment      车辆申请Manager
 */
@Service
@Transactional
public class CarApplyManager extends BaseManager{

	private GenericDAOHibernate<ToaCarApplicant, java.lang.String> carApplyDao = null;

//	统一用户接口
	@Autowired private IUserService userService;
	
	public CarApplyManager() {}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		carApplyDao = new GenericDAOHibernate<ToaCarApplicant, java.lang.String>(
				sessionFactory, ToaCarApplicant.class);
	}
	
	/**
	 * author:
	 * description:得到一页申请单分类列表<br>
	 * modifyer:	luosy
	 * description:	获取车辆申请分页列表
	 * @param page
	 * @param applierId		申请人ID
	 * @param applier	申请人姓名
	 * @param caruser	车辆使用者
	 * @param carId		车辆Id
	 * @param carno		车牌号
	 * @param destination	目的地
	 * @param starttime
	 * @param endtime
	 * @param applystatus 申请状态
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaCarApplicant> queryCarApplicants(Page<ToaCarApplicant> page, ToaCarApplicant model,String carId) throws 
			SystemException, ServiceException {
	try {
		Object[] values = new Object[2];
		StringBuffer  queryStr = new StringBuffer("from ToaCarApplicant t where 1=1");
		if(null!=model){
			if(model.getApplierId()!=null&&!"".equals(model.getApplierId())){
				queryStr.append(" and t.applierId ='"+model.getApplierId()+"'");
			}
			if(model.getApplier()!=null && !"".equals(model.getApplier())){
				queryStr.append(" and t.applier like '%"+FiltrateContent.getNewContent(model.getApplier())+"%'");
			}
			if(model.getCaruser()!=null && !"".equals(model.getCaruser())){
				queryStr.append(" and t.caruser like '%"+FiltrateContent.getNewContent(model.getCaruser())+"%'");
			}
			if(model.getDestination()!=null && !"".equals(model.getDestination())){
				queryStr.append(" and t.destination like '%"+FiltrateContent.getNewContent(model.getDestination())+"%'");
			}
			if(model.getStarttime()!=null){
				queryStr.append(" and t.starttime>=?");
				values[0] = model.getStarttime();
			} else {
				queryStr.append(" and 1=?");
				values[0] = 1;
			}
			if(model.getEndtime()!=null && !"".equals(model.getEndtime())){
				queryStr.append(" and t.endtime<=?");
				values[1] = model.getEndtime();
			} else {
				queryStr.append(" and 1=?");
				values[1] = 1;
			}
			if(model.getApplystatus()!=null && !"".equals(model.getApplystatus()) ){
				queryStr.append(" and t.applystatus ='"+model.getApplystatus()+"'");
			}
			if(model.getIsConfirm()!=null && !"".equals(model.getIsConfirm()) ){
				if(model.getIsConfirm().equals(ToaCarApplicant.CARAPPLY_ISCONFIRM)){
					queryStr.append(" and t.isConfirm ='"+model.getIsConfirm()+"'");
				}else{
					queryStr.append(" and (t.isConfirm is null or t.isConfirm = '"+ToaCarApplicant.CARAPPLY_NOTCONFIRM+"')");
				}
			}
			if(carId!=null && !"".equals(carId)){
				queryStr.append(" and t.toaCar.carId ='"+carId+"'");
			}
			if(null!=model.getToaCar()){
				if(model.getToaCar().getCarno()!=null && !"".equals(model.getToaCar().getCarno())){
					queryStr.append(" and t.toaCar.carno like '%"+FiltrateContent.getNewContent(model.getToaCar().getCarno())+"%'");
				}
			}
		}
		
//		是否允许看到下级机构
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());
		if(userService.isViewChildOrganizationEnabeld()){			
			if(org!=null){
				queryStr.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
			}
		}else {
			if(org!=null){
				queryStr.append(" and t.orgId = '").append(org.getOrgId()).append("'");
			}
		}
		
		queryStr.append(" order by t.applytime desc");
		return carApplyDao.find(page, queryStr.toString(), values);
	}catch (ServiceException e) {
		throw new ServiceException(MessagesConst.find_error,new Object[] {"获取申请单Page"});
	}
  }

	/**
	 * 得到申请单LIST对象
	 * 
	 */
	@Deprecated
	public List<ToaCarApplicant> getCarApplicants() throws 
			SystemException, ServiceException {
		try {
			final String hql = "from ToaCarApplicant t order by t.applytime desc";
			return carApplyDao.find(hql);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"获取申请单List"});
		}
	}

	/**
	 * 根据申请单ID得到指定申请单对象
	 * 
	 */
	public ToaCarApplicant getCarApplyInfo(String applicantId) throws 
			SystemException, ServiceException {
		try {
		  return carApplyDao.get(applicantId);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"获取指定ID申请单"});
		}
	}
/**
 * 保存申请单
 */
	@Transactional(readOnly = false)
	public void saveCarApplyInfo(ToaCarApplicant model) throws 
			SystemException, ServiceException {
		try {
			carApplyDao.save(model);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"保存车辆申请单"});
		}
	}
/**
 * 批量删除申请单
 */
	@Transactional(readOnly = false)
	public void deleteCars(String applicantIds) throws 
			SystemException, ServiceException {
		try {
			String[] ids = applicantIds.split(",");
			carApplyDao.delete(ids);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"删除车辆申请单"});
		}
	}
	
	/**
	 * 根据流程类型ID获取对应的流程
	 * @param processTypeId
	 * @return List
	 */
	@Transactional(readOnly = true)
	public List<Object[]> getProcessOwnedByProcessType(String processTypeId) {
		try {
			if(processTypeId == null || "".equals(processTypeId)){
				processTypeId = CarConst.CAR_WORKFLOW_TYPE_ID;
			}
			return workflow.getProcessOwnedByProcessType(processTypeId);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"根据流程类型ID获取对应的流程"});
		}
	}
	
	
	/**
	 * author:wuws
	 * description:更新车辆申请审批状态 
	 * modifyer:
	 * description:
	 * @param applicantId:车辆申请单ID；status：车辆申请审批状态：("0"："待提交")、	("1"："审批中")、	("3"："已批准")、	("4"："已撤销");
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void updateApplyStatus(String applicantId,String status)throws SystemException,ServiceException{
		try{
			ToaCarApplicant model = carApplyDao.get(applicantId);   //根据车辆申请单ID获取对象 
			model.setApplystatus(status);
			carApplyDao.update(model);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.operation_error,          
					new Object[] {"更新车辆申请状态"});
		}
	}
	
	/**
	 * 获取车辆申请单分类List
	 * 
	 */
	public List queryCarApplicantsList( Calendar startCal, Calendar endCal, String carId) throws 
			SystemException, ServiceException {
	 try{
		StringBuffer  queryStr = new StringBuffer("select t.applicantId,t.applier,t.toaCar.carno,t.destination,t.starttime,t.endtime,t.caruser from ToaCarApplicant t where 1=1");
		if(carId!=null && !"".equals(carId)){
			queryStr.append(" and t.toaCar.carId ='"+carId+"'");
		}			
		Object[] obj = new Object[6];
		obj[0] = startCal.getTime();
		obj[1] = endCal.getTime();
		obj[2] = startCal.getTime();
		obj[3] = startCal.getTime();
	    obj[4] = endCal.getTime();
	    obj[5] = endCal.getTime();
		queryStr.append(" and ( ( t.starttime>? and t.endtime>?) or ( t.starttime<? and t.endtime>?) or ( t.starttime<? and t.endtime>?))");  	
		queryStr.append(" and t.applystatus ='3'");

//		是否允许看到下级机构
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());
		if(userService.isViewChildOrganizationEnabeld()){			
			if(org!=null){
				queryStr.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
			}
		}else {
			if(org!=null){
				queryStr.append(" and t.orgId = '").append(org.getOrgId()).append("'");
			}
		}
		
		return carApplyDao.find(queryStr.toString(), obj);
	}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取车辆申请单分类List"});
		}
	}
	
	
	/**
	 * author:luosy
	 * description:将车辆安排日程事务列表转化为jsonArray
	 * modifyer:wuws
	 * description:
	 * @param 
	 * @return 	日程组件的jsonArray
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public JSONArray makeCalListToJSONArray(List calList, boolean canEdit)throws SystemException,ServiceException{
		try{
			JSONArray array = new JSONArray();
			if(null==calList){
				return array;
			}
			for (int i = 0; i < calList.size(); i++) {
				JSONObject jsonObj = new JSONObject();
				JSONObject start = new JSONObject();
				JSONObject finish = new JSONObject();
				jsonObj.put("edit",canEdit);
				Object[] objs = (Object[])calList.get(i);
	
					//设置活动开始结束时间
					Calendar s = Calendar.getInstance(); //calStartTimel
					s.setTime((Date)objs[4]);
					Calendar e = Calendar.getInstance(); //calEndTime
					e.setTime((Date)objs[5]);
					
					start.put("year", s.get(Calendar.YEAR));
					start.put("month", s.get(Calendar.MONTH));
					start.put("day", s.get(Calendar.DATE));
					start.put("hour", s.get(Calendar.HOUR_OF_DAY));
					start.put("min", s.get(Calendar.MINUTE));
					finish.put("year", e.get(Calendar.YEAR));
					finish.put("month", e.get(Calendar.MONTH));
					finish.put("day", e.get(Calendar.DATE));
					finish.put("hour", e.get(Calendar.HOUR_OF_DAY));
					finish.put("min", e.get(Calendar.MINUTE));
					
					jsonObj.put("id", objs[0]);//calendarId
					String calTitle="申请人："+objs[1]+"；车辆："+objs[2]+"；用车人："+objs[6]+"；目的地："+objs[3]+"；申请状态：已批准";
					jsonObj.put("description", calTitle);//calTitle
					jsonObj.put("start", start);
					jsonObj.put("finish", finish);
					array.add(jsonObj);
				}
			return array;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取车辆安排日程列表"});
		}
	}
	
	/**
	 * 得到当前用户相应类别的任务列表,
	   修改成挂起任务及指派委托状态也查找出来，在展现层展现状态 
	   增加查询条件参数,添加流程类型参数processType，以区分发文、收文等
	   @param page 分页对象
	   @param workflowType 流程类型参数
	   		1)大于0的正整数字符串：流程类型Id 
	   		2)0:不需指定流程类型
	   		3)-1:非系统级流程类型
	   @param businessName 业务名称查询条件
	   @param userName 主办人名称
	   @param startDate 任务开始时间
	   @param endDate 任务结束时间
	   @param isBackspace  是否是回退任务
	   		“0”：非回退任务；“1”：回退任务；“2”：全部
	   @return
	   		Object[]{(0)任务实例Id,(1)任务创建时间,(2)任务名称,(3)流程实例Id,(4)流程创建时间,(5)任务是否被挂起, 
				 (6)业务名称,(7)发起人名称,(8)流程类型Id,(9)是否回退任务,(10)任务前一处理人名称,(11)任务转移类型} 
	 */
	@Transactional(readOnly = true)
	public Page getTodoWorks(Page page,String workflowType,String businessName, 
			 				 String userName,Date startDate,
			 				 Date endDate,String isBackSpace) throws SystemException,ServiceException{
		try {
			User curUser = userService.getCurrentUser();//		获取当前用户
			String searchType = "all";
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType, workflowType,businessName,userName,startDate,endDate,isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取待办工作"});
		}
	}
	
}
