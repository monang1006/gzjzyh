package com.strongit.oa.leave;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmDeptreport;
import com.strongit.oa.bo.TSwConfersort;
import com.strongit.oa.bo.ToaLog;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.mylog.MyLogManager;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.utils.StringUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/*******************************************************************************
 * 会议通知管理
 * 
 * @Description: NoticeConferenceManager.java
 * @Date:2013-04-11
 * @Author 胡海亮
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */

@Service
@Transactional
public class LeaveManager implements ILeaveManager {
	
	private GenericDAOHibernate<TOmConference, java.lang.String> conDao;
	
	private GenericDAOHibernate<TSwConfersort, java.lang.String> consortDao;

	private GenericDAOHibernate<TOmConType, java.lang.String> conTypeDao;
	
	private GenericDAOHibernate<TOmConPerson, java.lang.String> conPersoneDao;
	
	private GenericDAOHibernate<TOmDeptreport, java.lang.String> depRDao;
	
	private GenericDAOHibernate<TOmConferenceSend, java.lang.String> acceptDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		conDao = new GenericDAOHibernate<TOmConference, String>(
				sessionFactory, TOmConference.class);
		
		consortDao = new GenericDAOHibernate<TSwConfersort, String>(
				sessionFactory, TSwConfersort.class);

		conTypeDao = new GenericDAOHibernate<TOmConType, String>(
				sessionFactory, TOmConType.class);
		
		conPersoneDao = new GenericDAOHibernate<TOmConPerson, String>(
				sessionFactory, TOmConPerson.class);
		
		depRDao = new GenericDAOHibernate<TOmDeptreport, String>(
				sessionFactory, TOmDeptreport.class);

		acceptDao = new GenericDAOHibernate<TOmConferenceSend, String>(
				sessionFactory, TOmConferenceSend.class);
	}
	
	public List<TSwConfersort> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAcceptOrgIdsByConid(String conNoticeId,
			StringBuffer deptNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAcceptOrgIdsByConid(String conNoticeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Object[]> getAllOrgUserList() {
		// TODO Auto-generated method stub
		return null;
	}

	public TOmConAttach getConAttachByAttachId(String attachId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getConAttachHtmlByConId(HttpServletRequest request,
			String conNoticeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public TSwConfersort getConferSortById(String sortId) {
		// TODO Auto-generated method stub
		return null;
	}

	public TSwConfersort getContypeByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public TOmConference getLeaveByConId(String conNoticeId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasConType(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public Page<TOmConference> queryIssiedList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<TOmDeptreport> queryLeaveList(Page<TOmDeptreport> page,
			TOmDeptreport entity, Date beginTime, Date endTime, Date regendtime) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveConType(TSwConfersort entity) {
		// TODO Auto-generated method stub
		
	}
	
	public void saveLeave(TOmDeptreport entity) throws Exception {
		// TODO Auto-generated method stub
		depRDao.save(entity);
	}

	public void saveLeaveInfo(TOmDeptreport model, String depIds,
			String[] attachsFileName, File[] attachs) {
		// TODO Auto-generated method stub
		
	}

	public Page<TOmConferenceSend> queryForConsend(
			Page<TOmConferenceSend> page, TOmConferenceSend entity,
			Date beginTime, Date endTime, Date regendtime, String state) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public TOmConference getBigConference(String conId){
		List<TOmConference> list = null;
		StringBuilder hql = new StringBuilder();
		hql
				.append("from TOmConference t where t.conferenceFromId = '"
						+ conId + "'  ");
		list = conDao.find(hql.toString());
		if(list!=null && list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}
	public List<TOmConType> getAllConType() throws Exception{
		return conTypeDao.findAll();
	}
	
	public List<TSwConfersort> getAllConSort() throws Exception{
		return consortDao.findAll();
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述:根据DepId、ConId、State和Model的相关信息,获取TOmDeptreport对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-15
	 * @Author 胡海亮
	 * 
	 * @param page
	 * @param model
	 * @param state
	 * @param conId
	 * @param depId
	 * 
	 * @return Page<TOmDeptreport>
	 * 
	 */
	public Page<TOmDeptreport> getConreportPage(Page<TOmDeptreport> page,
			TOmDeptreport model,String state,String conId,String depId) {
		
		if(!"".equals(conId)&&null!=conId){
			
			/**
			 * 
			 * 1、根据conId和DepId得到唯一确定的会议通知下发单
			 * 
			 * 2、根据下发单Id得到单位上报对象
			 * 
			 */
			String sendId="";
			TOmConferenceSend send = null;
			try {
				send = getConsendByConidAnddepid(conId,depId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(send!=null){
				try {
					sendId=getConsendByConidAnddepid(conId,depId).getSendconId();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
			StringBuilder hql = new StringBuilder("from TOmDeptreport t where 1=1 ");
			List<Object> parmList = new ArrayList<Object>();
			
			hql.append(" and t.TOmConferenceSend.sendconId = ? ");
			parmList.add(sendId );
			
			
			if (StringUtil.isNotEmpty(model)) {	
				//名字
				if(model.getConferee().getPersonName()!= null && !"".equals(model.getConferee().getPersonName())){
					hql.append(" and t.conferee.personName like ? ");
					parmList.add("%" +model.getConferee().getPersonName().trim() + "%");
				}
				//手机号
				if(model.getConferee().getPersonMobile() != null && !"".equals(model.getConferee().getPersonMobile())){
					hql.append(" and t.conferee.personMobile like ? ");
					parmList.add("%" +model.getConferee().getPersonMobile().trim() + "%");
				}
				//职务
				if(model.getConferee().getPersonPost() != null && !"".equals(model.getConferee().getPersonPost())){
					hql.append(" and t.conferee.personPost like ? ");
					parmList.add("%" +model.getConferee().getPersonPost().trim() + "%");
				}
				//性别
				if(model.getConferee().getPersonSax() != null && !"".equals(model.getConferee().getPersonSax() )){
					hql.append(" and t.conferee.personSax like ? ");
					parmList.add("%" +model.getConferee().getPersonSax() .trim() + "%");
				}
				//办公电话
				if(model.getConferee().getPersonPhone()!= null && !"".equals(model.getConferee().getPersonPhone())){
					hql.append(" and t.conferee.personPhone like ? ");
					parmList.add("%" +model.getConferee().getPersonPhone().trim() + "%");
				}
				//民族
				if(model.getConferee().getPersonNation()!= null && !"".equals(model.getConferee().getPersonNation())){
					hql.append(" and t.conferee.personNation like ? ");
					parmList.add("%" +model.getConferee().getPersonMobile().trim() + "%");
				}
			}
			//状态
			if(!"".equals(state)&&null!=state){
				hql.append(" and t.state = ? ");
				parmList.add(state.trim());
			}
			//hql.append(" order by t.capacitySequence ASC");
			System.out.println(hql.toString());
			page = depRDao.find(page,hql.toString(),parmList.toArray());
		}
		else{
			page=null;
		}
		
		return page;
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述:根据DepId和ConId,获取通知下发单对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-12
	 * @Author 胡海亮
	 * @param meetingId
	 * @param fromOrgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConferenceSend getConsendByConidAnddepid(String conId,String depId) throws Exception{
		
		/**
		 * 
		 * 1、根据conId和DepId得到唯一确定的会议通知下发单
		 * 
		 * 2、根据下发单Id得到单位上报对象
		 * 
		 */
		
		TOmConferenceSend send=null;
		List<Criterion> criterion = new ArrayList<Criterion>();
			Criterion cr1 = Restrictions.eq("deptCode", depId);
			criterion.add(cr1);
			Criterion cr2 = Restrictions.eq("TOmConference.conferenceId", conId);
			criterion.add(cr2);
			Criterion cr3 = Restrictions.eq("recvState", "2");
			criterion.add(cr3);
			List<TOmConferenceSend> list = acceptDao.findByCriteria(criterion
					.toArray(new Criterion[criterion.size()]));
			if(!list.isEmpty()){
				send=list.get(0);
			}
		return send;
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述:根据DepId,获取指定单位下签发通知集合
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-12
	 * @Author 胡海亮
	 * @param meetingId
	 * @param fromOrgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public List<TOmConferenceSend> getConsendList(String depId) throws  Exception{
		//Assert.notNull(depId);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");

		sql.append(" and t.deptCode = ? ");
		paramLst.add(depId);
		
		sql.append(" and t.TOmConference.conferenceFromId is not null  ");
		//paramLst.add(depId);
		
		sql.append(" and t.recvState = ? ");
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_REPORTED);
		
//		List<Criterion> criterion = new ArrayList<Criterion>();
//		Criterion cr1 = Restrictions.eq("deptCode", depId);
//		criterion.add(cr1);
////		Criterion cr2 = Restrictions.eq("TOmConference.conferenceId", conId);
////		criterion.add(cr2);
//		Criterion cr3 = Restrictions.eq("recvState", "2");
//		criterion.add(cr3);
//		List<TOmConferenceSend> list=null;
//		try {
//			list = acceptDao.findByCriteria(criterion
//					.toArray(new Criterion[criterion.size()]));
//			System.out.println(list.size());
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		
		System.out.println(sql.toString());
		System.out.println(depId);
		return this.acceptDao.find(sql.toString(), paramLst.toArray());
//		return list;
	}

	public TOmDeptreport getDepRepById(String id) throws Exception {
		// TODO Auto-generated method stub
		return depRDao.get(id);
	}
	
}
