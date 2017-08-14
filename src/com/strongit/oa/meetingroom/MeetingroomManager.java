package com.strongit.oa.meetingroom;

import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMeetingroom;
import com.strongit.oa.bo.ToaMeetingroomApply;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.util.FiltrateContent;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.OALogInfo;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
@OALogger
public class MeetingroomManager {
	private GenericDAOHibernate<ToaMeetingroom, java.lang.String> meetingroomDao;
	private GenericDAOHibernate<ToaMeetingroomApply, java.lang.String> meetingApplyDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory session) {
		meetingroomDao = new GenericDAOHibernate<ToaMeetingroom, java.lang.String>(
				session, ToaMeetingroom.class);
		meetingApplyDao = new GenericDAOHibernate<ToaMeetingroomApply, java.lang.String>(
				session, ToaMeetingroomApply.class);
	}
	
//	统一用户服务
	private IUserService user;

	public IUserService getUser() {
		return user;
	}
	@Autowired
	public void setUser(IUserService user) {
		this.user = user;
	}
	
	/**
	 * author:luosy
	 * description:	获取所有没有删除的会议室 page对象
	 * modifyer:
	 * description:
	 * @param page  分页对象
	 * @param model	搜索条件
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public Page getAllRoom(Page page, ToaMeetingroom model) throws SystemException,ServiceException{
		try{
			StringBuilder hql = new StringBuilder();
			hql.append(" select t.mrId,t.mrName,t.mrPeople,t.mrLocation,t.mrType,t.mrRemark,t.mrState " +
					"from ToaMeetingroom as t where t.mrState not like "+ToaMeetingroom.ROOM_DEL+" ");
			
//		会议室名称
			String mrName = model.getMrName();
			if (!"".equals(mrName) && mrName != null) {
				hql.append(" and t.mrName like '%" + FiltrateContent.getNewContent(mrName.trim()) + "%' ");
			}
			
//		会议室地点
			String mrLocation = model.getMrLocation();
			if (!"".equals(mrLocation) && mrLocation != null) {
				hql.append(" and t.mrLocation like '%"+FiltrateContent.getNewContent(mrLocation.trim())+"%' ");
			}
			
//		会议室地点
			String mrPeople = model.getMrPeople();
			if (!"".equals(mrPeople) && mrPeople != null) {
				hql.append(" and t.mrPeople like '%"+FiltrateContent.getNewContent(mrPeople.trim())+"%' ");
			}
			
//		会议室类型
			String mrType = model.getMrType();
			if (!"".equals(mrType) && mrType != null) {
				hql.append(" and t.mrType like '%"+FiltrateContent.getNewContent(mrType.trim()) +"%' ");
			}
			
//		会议室说明
			String mrRemark = model.getMrRemark();
			if (!"".equals(mrRemark) && mrRemark != null) {
				hql.append(" and t.mrRemark like '%"+FiltrateContent.getNewContent(mrRemark.trim()) +"%' ");
			}
//		会议室状态
			String mrState = model.getMrState();
			if(!"".equals(mrState) && mrState != null) {
				hql.append(" and t.mrState like '%"+FiltrateContent.getNewContent(mrState.trim()) +"%' ");
			}
			
			 TUumsBaseOrg org=user.getSupOrgByUserIdByHa(user.getCurrentUser().getUserId());
				String orgCode=org.getOrgSyscode();
				String orgid=org.getOrgId();
			  if(user.isViewChildOrganizationEnabeld()){
				  hql.append(" and t.topOrgcode like'"
						+ orgCode + "%'");
			}
			  else{
				  hql.append(" and t.departmentId ='"
							+ orgid + "'");
			  }
			  
			
			return meetingroomDao.find(page,hql.toString()+" order by t.mrName asc ");
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"会议室列表"});
		}
	}

	/**
	 * author:luosy
	 * description:获取所有可以使用的会议室
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List<ToaMeetingroom> getAllRoomList()throws SystemException,ServiceException{
		try{
			return meetingroomDao.find("from ToaMeetingroom as t where t.mrState like "+ToaMeetingroom.ROOM_OPEN+"  order by t.mrName asc ");
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"会议室列表"});
		}
	}
	
	/**
	 * author:luosy
	 * description:获取所有状态的会议室
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public List<ToaMeetingroom> getAllStateRoom()throws SystemException,ServiceException{
		try{
			return meetingroomDao.find("from ToaMeetingroom as t  ");
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"会议室列表"});
		}
		
		
	}
	
	/**
	 * author:luosy
	 * description:	获取会议室申请单
	 * modifyer:
	 * description:
	 * @param roomId
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional(readOnly=true)
	public ToaMeetingroom getRoomInfoByRoomId(String roomId) throws SystemException,ServiceException{
		try{
			return meetingroomDao.get(roomId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取会议室信息"});
		}
	}
	
	/**
	 * author:luosy
	 * description:修改会议室状态
	 * modifyer:
	 * description:
	 * @param state
	 * @param room
	 */
	public void changeMrState(String state,ToaMeetingroom room){
		room = this.getRoomInfoByRoomId(room.getMrId());
		room.setMrState(state);
		meetingroomDao.save(room);
	}
	
	/**
	 * author:luosy
	 * description:删除会议室信息
	 * modifyer:
	 * description:
	 * @param mrIds 会议室id
	 */
	@Transactional
	public void deleRoomByMrId(ToaMeetingroom room, OALogInfo ... loginfos)throws SystemException,ServiceException,AjaxException,Exception{
		try{
			room.setMrState(ToaMeetingroom.ROOM_DEL);
			meetingroomDao.save(room);
//			String[] ids = mrIds.split(",");
//			for(int i=0;i<ids.length;i++){
//				ToaMeetingroom room = this.getRoomInfoByRoomId(ids[i]);
//				room.setMrState(ToaMeetingroom.ROOM_DEL);
//				meetingroomDao.save(room);
//				meetingroomDao.flush();
//			}
		}catch(ServiceException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除会议室"});
		}catch(AjaxException e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除会议室"});
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.del_error,               
					new Object[] {"删除会议室"});
		}
	}
	
	/**
	 * author:luosy
	 * description:保存会议室信息
	 * modifyer:
	 * description:
	 * @param model
	 * @throws SystemException
	 * @throws ServiceException
	 */
	@Transactional
	public void saveRoomInfo(ToaMeetingroom model, OALogInfo ... loginfos)throws SystemException,ServiceException{
		try{
			 TUumsBaseOrg org=user.getSupOrgByUserIdByHa(user.getCurrentUser().getUserId());
				
			if("".equals(model.getMrId())){
				model.setMrId(null);
				model.setTopOrgcode(org.getOrgSyscode());
				model.setDepartmentId(org.getOrgId());
				meetingroomDao.save(model);
			}else{
				ToaMeetingroom room = this.getRoomInfoByRoomId(model.getMrId());
				room.setMrId(model.getMrId());
				room.setMrLocation(model.getMrLocation());
				room.setMrName(model.getMrName());
				room.setMrPeople(model.getMrPeople());
				room.setMrRemark(model.getMrRemark());
				room.setMrType(model.getMrType());
				room.setMrState(model.getMrState());
				if(null!=model.getMrImg()){
					room.setMrImg(model.getMrImg());
				}
				meetingroomDao.save(room);
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"保存会议室信息"});
		}
	}
}
