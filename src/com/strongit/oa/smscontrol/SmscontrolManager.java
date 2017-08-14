/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */
package com.strongit.oa.smscontrol;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSms;
import com.strongit.oa.bo.ToaSmsCommConf;
import com.strongit.oa.bo.ToaSmscontrol;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.ListUtils;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.oa.common.user.util.Const;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class SmscontrolManager {
	private GenericDAOHibernate<ToaSmscontrol, java.lang.String> smsControlDao;
	
	private IUserService userService;
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		smsControlDao = new GenericDAOHibernate<ToaSmscontrol, java.lang.String>(
				session, ToaSmscontrol.class);
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
	public SmscontrolManager() {
	}

	
	

	/**
	 * author:luosy
	 * description:保存用户权限
	 * modifyer:
	 * description:
	 * @param userIds
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveRight(String userIds)throws SystemException,ServiceException{
		try{
			if(!"".equals(userIds)&&null!=userIds){
				String[] user = userIds.split(",");
				ToaSmscontrol smscontrol = null;
				for(int i=0;i<user.length;i++){
					smscontrol = getSmscontrolByUserId(user[i]);
					if(null!=smscontrol){
						smscontrol.setSmscontrolUserName(userService.getUserNameByUserId(user[i]));
						smscontrol.setSmscontrolDepartment(userService.getUserDepartmentByUserId(user[i]).getOrgName());
						smscontrol.setSmsSendRight(ToaSmscontrol.SMS_RIGHT);
					}else{
						smscontrol = new ToaSmscontrol();
						
						smscontrol.setSmscontrolUserid(user[i]);
						smscontrol.setSmscontrolUserName(userService.getUserNameByUserId(user[i]));
						smscontrol.setSmscontrolDepartment(userService.getUserDepartmentByUserId(user[i]).getOrgName());
						smscontrol.setSmsSendRight(ToaSmscontrol.SMS_RIGHT);
					}
					smsControlDao.save(smscontrol);
				}
			}
			
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存用户权限"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存用户权限"});
		}
	}
	
	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @param userId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 * @throws AjaxException
	 */
	public ToaSmscontrol getSmscontrolByUserId(String userId)throws SystemException,ServiceException,AjaxException{
		try{
			List list = smsControlDao.find("from ToaSmscontrol as t where t.smscontrolUserid=? ",userId);
			if(null!=list){
				if(list.size()>0){
					return (ToaSmscontrol) list.get(0);
				}else{
					return null;
				}
			}else{
				return null;
			}
			
		} catch(AjaxException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找用户权限"});
		} catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"查找用户权限"});
		}
		
	}
	
	/**
	 * author:luosy
	 * description: 获取权限列表
	 * modifyer:
	 * description:
	 * @param page
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSmscontrol> getControlList(Page<ToaSmscontrol> page) throws SystemException,ServiceException{
		try{
//			page = smsControlDao.find(page, "from ToaSmscontrol t order by t.smsSendRight desc ");
//			return page;
			
			return searchList(page,new ToaSmscontrol());
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信权限列表"});
		}
	}

	/**
	 * author:luosy
	 * description:
	 * modifyer:
	 * description:
	 * @param page
	 * @param model
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaSmscontrol> searchList(Page<ToaSmscontrol> page, ToaSmscontrol model)throws SystemException,ServiceException{
		try{
			StringBuffer hql = new StringBuffer();
			hql.append("from ToaSmscontrol t where 1=?");
			Object[] obj = new Object[4];
			obj[0] = 1;
			int i = 1;
			
//			部门名称
			String userDepartM = model.getSmscontrolDepartment();
			if (!"".equals(userDepartM) && userDepartM != null) {
				hql.append(" and t.smscontrolDepartment like ? ");
				obj[i] = "%" + userDepartM + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
//			 用户姓名
			String userName = model.getSmscontrolUserName();
			if (!"".equals(userName) && userName != null) {
				hql.append(" and t.smscontrolUserName like ? ");
				obj[i] = "%" + userName + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}
			
//			权限状态
			String sendRight = model.getSmsSendRight();
			if (!"".equals(sendRight) && sendRight != null) {
				hql.append(" and t.smsSendRight like ? ");
				obj[i] = "%" + sendRight + "%";
				i++;
			} else {
				hql.append(" and 1= ? ");
				obj[i] = 1;
				i++;
			}	
			List<ToaSmscontrol> sms=smsControlDao.find(hql.toString()+" order by t.smscontrolId asc ", obj);
			for(int j=0;j<sms.size();j++){
				model=sms.get(j);
				TUumsBaseUser tUser=userService.getUserInfoByUserId(model.getSmscontrolUserid());
				if(tUser != null){
					if (Const.IS_YES.equals(tUser.getUserIsdel())) {
						sms.remove(j);
						j--;
					}					
				}
			}
			 page=ListUtils.splitList2Page(page, sms);
			//page = smsControlDao.find(page, hql.toString()+" order by t.smscontrolId asc ",obj);
			return page;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信权限列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:开启用户权限
	 * modifyer:
	 * description:
	 * @param controlId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void openRight(String controlId)throws SystemException,ServiceException{
		try{
			String[] ids = controlId.split(",");
			for(int i=0;i<ids.length;i++){
				ToaSmscontrol smsContr = smsControlDao.get(ids[i]);
				if(null!=smsContr){
					smsContr.setSmscontrolUserName(userService.getUserNameByUserId(smsContr.getSmscontrolUserid()));
					smsContr.setSmscontrolDepartment(userService.getUserDepartmentByUserId(smsContr.getSmscontrolUserid()).getOrgName());
					smsContr.setSmsSendRight(ToaSmscontrol.SMS_RIGHT);
					smsControlDao.save(smsContr);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信权限列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:关闭用户权限 
	 * modifyer:
	 * description:
	 * @param controlId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void closeRight(String controlId)throws SystemException,ServiceException{
		try{
			String[] ids = controlId.split(",");
			for(int i=0;i<ids.length;i++){
				ToaSmscontrol smsContr = smsControlDao.get(ids[i]);
				if(null!=smsContr){
					smsContr.setSmscontrolUserName(userService.getUserNameByUserId(smsContr.getSmscontrolUserid()));
					smsContr.setSmscontrolDepartment(userService.getUserDepartmentByUserId(smsContr.getSmscontrolUserid()).getOrgName());
					smsContr.setSmsSendRight(ToaSmscontrol.SMS_NORIGHT);
					smsControlDao.save(smsContr);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信权限列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 判斷用户是否有发送权限
	 * modifyer:
	 * description:
	 * @param userId 用户ID
	 * @return	true / false
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean hasSendRight(String userId)throws SystemException,ServiceException{
		try{
			List list = smsControlDao.find("from ToaSmscontrol as t where t.smscontrolUserid=? ",userId);
			if(null!=list){
				if(list.size()>0){
					ToaSmscontrol smsControl = (ToaSmscontrol) list.get(0);
					if(smsControl.getSmsSendRight().equals(ToaSmscontrol.SMS_RIGHT)){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"判断用户权限"});
		}
	}
}
