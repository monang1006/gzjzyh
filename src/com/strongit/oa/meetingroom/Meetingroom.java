package com.strongit.oa.meetingroom;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMeetingroom;
import com.strongit.oa.bo.ToaMeetingroomApply;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.annotation.OALogger;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class Meetingroom {
	private GenericDAOHibernate<ToaMeetingroom, java.lang.String> meetingroomDao;
	private GenericDAOHibernate<ToaMeetingroomApply, java.lang.String> meetingApplyDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		meetingroomDao = new GenericDAOHibernate<ToaMeetingroom, java.lang.String>(
				session, ToaMeetingroom.class);
		meetingApplyDao = new GenericDAOHibernate<ToaMeetingroomApply, java.lang.String>(
				session, ToaMeetingroomApply.class);
	}
	@Transactional(readOnly=true)
	public Page getAllRooms(Page page, ToaMeetingroom model) throws SystemException,ServiceException{
		try{
			StringBuilder hql = new StringBuilder();
			hql.append(" from ToaMeetingroom as t where t.mrState not like "+ToaMeetingroom.ROOM_DEL+" ");
			
//		会议室状态
			hql.append(" and t.mrState = "+ToaMeetingroom.ROOM_OPEN);
			
			 /*TUumsBaseOrg org=user.getSupOrgByUserIdByHa(user.getCurrentUser().getUserId());
				String orgCode=org.getOrgSyscode();
				String orgid=org.getOrgId();
			  if(user.isViewChildOrganizationEnabeld()){
				  hql.append(" and t.topOrgcode like'"
						+ orgCode + "%'");
			}
			  else{
				  hql.append(" and t.departmentId ='"
							+ orgid + "'");
			  }*/
			  
			
			return meetingroomDao.find(page,hql.toString()+" order by t.mrName asc ");
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"会议室列表"});
		}
	}
	//查找该会议是否已被申请使用
	public String listMeetingRoom(String mrId){
		try{
			StringBuffer addhql = new StringBuffer();
			addhql.append("1=1 ");
			if (!"".equals(mrId) && mrId != null) {
				addhql.append(" and t.toaMeetingroom.mrId = '" + mrId + "' ");
			}
			addhql.append(" and t.maState = "+ToaMeetingroomApply.APPLY_ALLOW);
			
			Date m = new Date();
			Date d = new Date();
			d.setHours(23);
			d.setMinutes(59);
			d.setSeconds(59);
			Object[] obj = new Object[6];
			obj[0] = m;
			obj[1] = d;
			obj[2] = m;
			obj[3] = m;
			obj[4] = d;
			obj[5] = d;
			
			List l = meetingApplyDao.find("select t.maId,t.maMeetingdec, t.maCreaterName, t.maAppstarttime,t.maAppendtime,t.maState,t.toaMeetingroom.mrId,t.toaMeetingroom.mrName, t.maSubmittime " +
					"from ToaMeetingroomApply as t where " + addhql.toString()+
					" and ( ( t.maAppstarttime<=? and t.maAppendtime>=? ) or ( t.maAppstarttime<=? and t.maAppendtime>=?) " +
					" or ( t.maAppstarttime<=? and t.maAppendtime>=?) )  ",obj);
			if(l!=null&&l.size()>0){
				return "true";
			}
			return "false";
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据时间段得到对应的会议申请信息列表"});
		}
	}
	
	/**
	 * author:luosy   tj
	 * description:根据时间段得到对应的会议已允许申请信息列表
	 * modifyer:
	 * description:
	 * @param startCal
	 * @param endCal
	 * @param mrId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List getListByTimeAllow(String mrId,String maId)throws SystemException,ServiceException{
		try{
			StringBuffer addhql = new StringBuffer();
			addhql.append("1=1 ");
			if (!"".equals(mrId) && mrId != null) {
				addhql.append(" and t.toaMeetingroom.mrId = '" + mrId + "' ");
			}
			if (!"".equals(maId) && maId != null) {
				addhql.append(" and t.maId not like '" + maId + "' ");
			}
			addhql.append(" and t.maState = "+ToaMeetingroomApply.APPLY_ALLOW);
			Date m = new Date();
			Date d = new Date();
			d.setHours(23);
			d.setMinutes(59);
			d.setSeconds(59);
			Object[] obj = new Object[6];
			obj[0] = m;
			obj[1] = d;
			obj[2] = m;
			obj[3] = d;
			obj[4] = m;
			obj[5] = d;
			
			return meetingApplyDao.find("select t.maId,t.maMeetingdec, t.maCreaterName, t.maAppstarttime,t.maAppendtime,t.maState,t.toaMeetingroom.mrId,t.toaMeetingroom.mrName, t.maSubmittime " +
					"from ToaMeetingroomApply as t where " + addhql.toString()+
					"  and ( ( t.maAppendtime>=? and t.maAppendtime<=? ) or ( t.maAppendtime>=? and t.maAppendtime<=?) " +
					" or ( t.maAppendtime>=? and t.maAppendtime<=?) )  ",obj);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据时间段得到对应的会议申请信息列表"});
		}
	}
}
