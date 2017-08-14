/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: 
 * modifyer:	luosy
 * Version: V1.0
 * Description：车辆信息管理
 */
package com.strongit.oa.car;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaCar;
import com.strongit.oa.common.user.IUserService;
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
 * @comment      车辆Manager
 */
@Service
@Transactional
public class CarManager {

	private GenericDAOHibernate<ToaCar, java.lang.String> carDao = null;

//	统一用户接口
	@Autowired private IUserService userService;
	
	public CarManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		carDao = new GenericDAOHibernate<ToaCar, java.lang.String>(
				sessionFactory, ToaCar.class);
	}

	/**
	 * author:
	 * description:得到一页车辆信息列表<br>
	 * modifyer:	luosy
	 * description: 	获取车辆列表分页信息
	 * @param page
	 * @param carno			车牌号
	 * @param cartype		车辆类型
	 * @param carbrand		车辆品牌
	 * @param takenumber	可乘人数
	 * @param driver		司机
	 * @param buydate1		购置日期(开始时间)
	 * @param buydate2		购置日期(结束时间)
	 * @param status		车辆状态
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaCar> queryCars(
			Page<ToaCar> page, String carno, String cartype, String carbrand,String takenumber, String driver,java.util.Date buydate1,java.util.Date buydate2, String status) throws 
			SystemException, ServiceException {
	try {
		Object[] values = new Object[2];
		StringBuffer  queryStr = new StringBuffer("from ToaCar t where 1=1 and t.isdel not like '1' ");
		if(carno!=null && !"".equals(carno)){
			queryStr.append(" and t.carno like '%"+FiltrateContent.getNewContent(carno)+"%'");
		}
		if(cartype!=null && !"".equals(cartype)){
			queryStr.append(" and t.cartype ='"+cartype+"'");
		}
		if(carbrand!=null && !"".equals(carbrand)){
			queryStr.append(" and t.carbrand like '%"+FiltrateContent.getNewContent(carbrand)+"%'");
		}
		if(driver!=null && !"".equals(driver)){
			queryStr.append(" and t.driver like '%"+FiltrateContent.getNewContent(driver)+"%'");
		}
		if(takenumber!=null && !"".equals(takenumber)){
			queryStr.append(" and t.takenumber ='"+takenumber+"'");
		}
		if(buydate1!=null && !"".equals(buydate1)){
			queryStr.append(" and t.buydate>=?");
			values[0] = buydate1;
		} else {
			queryStr.append(" and 1=?");
			values[0] = 1;
		}
		if(buydate2!=null && !"".equals(buydate2)){
			queryStr.append(" and t.buydate<=?");
			values[1] = buydate2;
		} else {
			queryStr.append(" and 1=?");
			values[1] = 1;
		}
		if(status!=null && !"".equals(status)){
			queryStr.append(" and t.status ='"+status+"'");
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
		
		queryStr.append(" order by t.carno");
		return carDao.find(page, queryStr.toString(), values);
	}catch (ServiceException e) {
		throw new ServiceException(MessagesConst.find_error,new Object[] {"获取车辆信息Page"});
	}
 }

	/**
	 * 得到可用车辆LIST对象
	 * 
	 */
	public List<ToaCar> getCars() throws 
			SystemException, ServiceException {
		try {
			StringBuilder queryStr = new StringBuilder();
			queryStr.append("from ToaCar t where t.status=").append(ToaCar.CAR_USEABLE).append(" and  t.isdel not like '1' ");
//			是否允许看到下级机构
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
			
			queryStr.append(" order by t.carno");
			List<ToaCar> carList = carDao.find(queryStr.toString());
			
			if(null!=carList){
				return carList;
			}else{
				return new ArrayList<ToaCar>(); 
			}
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"获取车辆信息List"});
		}
	}

	/**
	 * 根据车辆ID得到指定车辆对象
	 * 
	 */
	public ToaCar getCarInfo(String carId) throws 
			SystemException, ServiceException {
		try {
		  return carDao.get(carId);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"获取指定ID车辆信息"});
		}
	}
	
	/**
	 * 保存车辆
	 */
	@Transactional(readOnly = false)
	public void saveCarInfo(ToaCar model,String editDelImg) throws 
			SystemException, ServiceException {
		try {
			if ("".equals(model.getCarId())||null==model.getCarId()){
				model.setCarId(null);

				//分级信息
				TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userService.getCurrentUser().getUserId());
				model.setOrgCode(org.getSupOrgCode());
				model.setOrgId(org.getOrgId());
				
				carDao.save(model);
			}else{
				ToaCar car=null;
				car=this.getCarInfo(model.getCarId());
				car.setCarno(model.getCarno());
				car.setBuydate(model.getBuydate());
				car.setCarbrand(model.getCarbrand());
				car.setCardescription(model.getCardescription());
				car.setCartype(model.getCartype());
				car.setDriver(model.getDriver());
				car.setIsdel(model.getIsdel());
				car.setStatus(model.getStatus());
				car.setTakenumber(model.getTakenumber());
			    if(null!=model.getImg()){
			 	    car.setImg(model.getImg());
			    }
			    if("1".equalsIgnoreCase(editDelImg)){  
				    car.setImg(null);
			    }
				carDao.save(car);
			}
	
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.save_error,new Object[] {"保存车辆信息"});
		}
	}
	
	/**
	 * 批量删除车辆
	 * 保留方法 暂不使用
	 */
	@Transactional(readOnly = false)
	public void deleteCars(String carIds) throws 
			SystemException, ServiceException {
		try {
		  String[] ids = carIds.split(",");
		  carDao.delete(ids);
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.del_error,new Object[] {"删除车辆信息"});
		}
	}
	
	/**
	 * author:
	 * description:是否存在车牌号
	 * modifyer: 	 luosy
	 * description:  验证是否已存在车牌号
	 * @param carno 车牌号
	 * @param carId 车辆ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Boolean isExistCar(String carno,String carId)throws SystemException,ServiceException{
		try{ 
			List list = null;
			if("".equals(carId)||null==carId){
				list = carDao.find("select t.carId from ToaCar t where t.carno like ? and t.isdel not like '1' ",carno);
			}else{
				list = carDao.find("select t.carId from ToaCar t where t.carno like ? and t.carId not like ? and t.isdel not like '1' ",carno,carId);
			}
			if(null!=list&&list.size()>0){
				return true;
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取文件夹"});
		}
		
	}
	
	
}
