package com.strongit.oa.noticeconference;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/*******************************************************************************
 * 会议通知下发表
 * 
 * @Description: ConAcceptManager.java
 * @Date:Apr 2, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */
@Service
@Transactional
public class ConAcceptManager implements IConAcceptManager {
	private GenericDAOHibernate<TOmConferenceSend, java.lang.String> acceptDao; // 会议下发签收DAO

	private GenericDAOHibernate<TOmConference, java.lang.String> conNoticedao; // 会议通知对象
	
	private  GenericDAOHibernate<TOmConPerson, java.lang.String> conPersondao; // 会议上报人员通讯录表

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		acceptDao = new GenericDAOHibernate<TOmConferenceSend, String>(
				sessionFactory, TOmConferenceSend.class);

		conNoticedao = new GenericDAOHibernate<TOmConference, String>(
				sessionFactory, TOmConference.class);
		
		conPersondao = new GenericDAOHibernate<TOmConPerson, String>(
				sessionFactory, TOmConPerson.class);

	}

	@Autowired
	private IUserService userService;
	

	@Autowired
	private IConNoticeAttachManager conAttachManager;
	
	/***
	 * 
	* 方法简要描述：根据主键，获取上报人员通讯录实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 16, 2013
	* @Author 万俊龙
	* @param personId
	* @return
	* @version	1.0
	* @see
	 */
	public TOmConPerson getPersonById(String personId){
		Assert.notNull(personId);
		return this.conPersondao.get(personId);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：根据主键，获取实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param id
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConferenceSend getModel(String id) {
		Assert.notNull(id);
		return this.acceptDao.get(id);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:更新下发通知实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void update(TOmConferenceSend entity) {
		Assert.notNull(entity);
		Assert.notNull(entity.getSendconId());
		this.acceptDao.update(entity);
	}

	/**
	 * 
	 * 方法简要描述：根据会议下发签收主键ID,删除实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param id
	 * @version 1.0
	 * @see
	 */
	public void delete(String id) {
		Assert.notNull(id);
		this.acceptDao.delete(id);
	}

	/**
	 * 
	 * 方法简要描述：删除会议签发实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void delete(TOmConferenceSend entity) {
		Assert.notNull(entity);
		this.acceptDao.delete(entity);
	}

	/**
	 * 
	 * 方法简要描述：批量删除会议签发实体对象集
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void delete(List<TOmConferenceSend> list) {
		Assert.notNull(list);
		Assert.notEmpty(list);
		this.acceptDao.delete(list);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议下发实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void save(TOmConferenceSend entity) {
		Assert.notNull(entity);
		entity.setSendconId(null);
		this.acceptDao.save(entity);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：判断在指定会议下，签发单位是否签收
	 * 
	 * @return true:表示签发单位已经签发 false:表示签发单位没有签发
	 * 
	 * 
	 * JavaDoc tags,比如
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @version 1.0
	 * @see
	 */
	public boolean checkSeatsetDepIsAccept(String meetid, String depId) {
		Assert.notNull(meetid);
		Assert.notNull(depId);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");
		sql.append(" and t.deptCode= ? ");
		paramLst.add(depId);
		sql.append(" and t.TOmConference.conferenceId=? ");
		paramLst.add(meetid);
		sql.append(" and t.recvState != ?");
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT); // 状态：0=未签收;1=已签收未上报;2=已签收已上报

		List<TOmConferenceSend> list = this.acceptDao.find(sql.toString(),
				paramLst.toArray());
		if (StringUtil.isNotEmpty(list)) {
			return true;
		}
		return false;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：构造会议签发实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param meetId
	 * @param depId
	 * @return
	 * @version 1.0
	 * @see
	 */
	private TOmConferenceSend genalateEntity(String conId, String depId,
			String state) {
		TOmConferenceSend entity = new TOmConferenceSend();

		TOmConference conNotice = this.getConNoticeByConId(conId);
		Assert.notNull(conNotice);
		entity.setTOmConference(conNotice);

		TUumsBaseOrg dep = userService.getOrgInfoByOrgId(depId);
		Assert.notNull(dep);
		entity.setDeptCode(dep.getOrgId());
		entity.setDeptName(dep.getOrgName());

		User userInfo = userService.getCurrentUser();
		Assert.notNull(userInfo);
		// String userId = userInfo.getUserId();
		// String orgId = userInfo.getOrgId();

		entity.setConferencecode(null);

		entity.setConRecvTime(new Date());
		entity.setConRecvUser(userInfo.getUserName());
		entity.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);
		try {
			InetAddress inet = InetAddress.getLocalHost();
			entity.setOperateIp(inet.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:获取指定会议下签发单位集合
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param meetingId
	 * @param fromOrgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<TOmConferenceSend> findByconNoticeId(String conNoticeId) {
		Assert.notNull(conNoticeId);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");

		sql.append(" and t.TOmConference.conferenceId = ? ");
		paramLst.add(conNoticeId);
		
		sql.append(" and t.recvState != ? ");
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_DEL);
		return this.acceptDao.find(sql.toString(), paramLst.toArray());
	}

	/***************************************************************************
	 * 
	 * 方法简要描述: * 保存签发单位 <br>
	 * 在保存签发单位前，根据会议编号删除之前的签发单位。再保存最新的签发单位。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param conId
	 * @param depIds
	 * @version 1.0
	 * @see
	 */
	public void save(String conId, String depIds,String state) {
	 	List<TOmConferenceSend> acceptLst = new ArrayList();
		if (StringUtil.isEmpty(conId) && StringUtil.isEmpty(depIds)) {
			return;
		}
		// 删除之前的签发单位，已最后一次签发单位为准
		List<TOmConferenceSend> olderList = this.findByconNoticeId(conId);
		if (StringUtil.isNotEmpty(olderList)) {
			this.acceptDao.delete(olderList);
		}
		String[] depArrays = depIds.split(",");
		if (StringUtil.isNotEmpty(depArrays)) {
			for (String depid : depArrays) {
				if (StringUtil.isNotEmpty(depid) && !hasBigAccept(conId, depid)) {
					acceptLst.add(this.genalateEntity(conId, depid, state));
				}
			}
		}

		this.acceptDao.save(acceptLst); 
	}

	/***************************************************************************
	 * 
	 * 方法简要描述: 保存签发单位  
	 * <br> 会议通知草稿 状态下 签发单位需保存最新的，需将之前的单位删除。
	 *  已发会议通知状态下， 过时的签发单位需保留，但需将状态设置为 废除状态。
	 *                    新增的签发单位状态为 待签收状态。
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param conId
	 * @param depIds
	 * @version 1.0
	 * @see
	 */
	public void save(String conId, String depIds) {

		List<TOmConferenceSend> acceptLst = new ArrayList();
		if (StringUtil.isEmpty(conId) && StringUtil.isEmpty(depIds)) {
			return;
		}
		TOmConference conference=this.conNoticedao.get(conId);
		Assert.notNull(conference);
		// 删除之前的签发单位，已最后一次签发单位为准
		List<TOmConferenceSend> olderList = this.findByconNoticeId(conId);

		List<String> filterAddDept = new ArrayList();
		if (StringUtil.isNotEmpty(olderList)) {
			for (TOmConferenceSend send : olderList) {
				boolean  delFlag=true;  //标记是否能删除
				
				//通过比对，如果新旧都存在的，放入集合中，如果不存在，删除。
				String[] depArrays = depIds.split(",");
				if (StringUtil.isNotEmpty(depArrays)) {
					for (String depid : depArrays) {
						if (send.getDeptCode().equals(depid)) {
							filterAddDept.add(depid);
							delFlag=false;
							break;
						}
					}

				}
				if(delFlag){
					if(Constants.CONFERENCE_STATE_ISSIED.equals(conference.getConferenceState())){
						send.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_DEL);
						this.acceptDao.update(send);
					}else{
						this.acceptDao.delete(send);
					}
					
				}
			}
		}
 
		String[] depArrays = depIds.split(",");
		if (StringUtil.isNotEmpty(depArrays)) {
			for (String depid : depArrays) {
				if(StringUtil.isNotEmpty(depid) && !filterAddDept.contains(depid)){
					acceptLst.add(this.genalateEntity(conId, depid, "0"));
				}  
			}
		}

		this.acceptDao.save(acceptLst);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：在指定的会议中，是否存在下发单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param meetingId
	 * @param depId
	 * @return
	 * @version 1.0
	 * @see
	 */
	private boolean hasBigAccept(String meetingId, String depId) {
		Assert.notNull(meetingId);
		Assert.notNull(depId);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");
		sql.append(" and t.deptCode= ? ");
		paramLst.add(depId);
		sql.append(" and t.TOmConference.conferenceId=? ");
		paramLst.add(meetingId);
		List<TOmConferenceSend> lst = this.acceptDao.find(sql.toString(),
				paramLst.toArray());
		if (StringUtil.isNotEmpty(lst)) {
			return true;
		}
		return false;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：删除会议下的所有签发单位
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Mar 15, 2013
	 * @Author 万俊龙
	 * @param meetingId
	 * @version 1.0
	 * @see
	 */
	public void deleteByConId(String conId) {
		Assert.notNull(conId);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");
		sql.append(" and t.TOmConference.conferenceId=? ");
		paramLst.add(conId);

		List<TOmConferenceSend> list = this.acceptDao.find(sql.toString(),
				paramLst.toArray());
		if (StringUtil.isNotEmpty(list)) {
			this.acceptDao.delete(list);
		}
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：更加会议通知编号，获取会议通知实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param conId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConference getConNoticeByConId(String conId) {
		Assert.notNull(conId);
		return this.conNoticedao.get(conId);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：获取指定下发单位所有的会议信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param userId
	 * @version 1.0
	 * @see
	 */
	public List<TOmConferenceSend> find(String orgId, String receState) {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");
		if (StringUtil.isNotEmpty(orgId)) {
			sql.append(" t.deptCode = ? ");
			paramLst.add(orgId);
		}
		if (StringUtil.isNotEmpty(orgId)) {
			sql.append(" t.recvState = ? ");
			paramLst.add(receState);
		}
		return this.acceptDao.find(sql.toString(), paramLst.toArray());
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：获取组织机构下待签收的所有会议编号 <br/>数据格式 aaa,bbbbb,cccc 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param orgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String findWaitStateForConIdsByOrgId(String orgId) {
		Assert.notNull(orgId);
		StringBuffer conIds = new StringBuffer();
		List<TOmConferenceSend> list = this.find(orgId,
				Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend entity : list) {
				conIds.append(entity.getTOmConference().getConferenceId())
						.append(",");
			}
			conIds = conIds.deleteCharAt(conIds.lastIndexOf(","));
			return conIds.toString();
		}
		return null;

	}

	/***************************************************************************
	 * 
	 * 方法简要描述：获取组织机构下已签收及重报的所有会议编号 <br/>数据格式 aaa,bbbbb,cccc
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param orgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String findSignStateForConIdsByOrgId(String orgId) {
		Assert.notNull(orgId);

		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");
		if (StringUtil.isNotEmpty(orgId)) {
			sql.append(" t.deptCode = ? ");
			paramLst.add(orgId);
		}
		if (StringUtil.isNotEmpty(orgId)) {
			sql.append(" t.recvState in (?,?) ");
			paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_SIGN);
			paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_REBACK);
		}

		List<TOmConferenceSend> list = this.acceptDao.find(sql.toString(),
				paramLst.toArray());

		// 存放会议编号
		StringBuffer conIds = new StringBuffer();
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend entity : list) {
				conIds.append(entity.getTOmConference().getConferenceId())
						.append(",");
			}
			conIds = conIds.deleteCharAt(conIds.lastIndexOf(","));
			return conIds.toString();
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：获取组织机构下已上报的所有会议编号 <br/>数据格式 aaa,bbbbb,cccc
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param orgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String findReportedStateForConIdsByOrgId(String orgId) {
		Assert.notNull(orgId);
		StringBuffer conIds = new StringBuffer();
		List<TOmConferenceSend> list = this.find(orgId,
				Constants.CONFERENCE_SEND_RECVSTATE_REPORTED);
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConferenceSend entity : list) {
				conIds.append(entity.getTOmConference().getConferenceId())
						.append(",");
			}
			conIds = conIds.deleteCharAt(conIds.lastIndexOf(","));
			return conIds.toString();
		}
		return null;
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：分页查找下发通知表
	 * 
	 * 
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @param state
	 * @param receState
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmConferenceSend> queryForConsend(
			Page<TOmConferenceSend> page, TOmConferenceSend entity,
			Date beginTime, Date endTime, Date regendtime, String state) {

		if (StringUtil.isEmpty(state)) {
			return query(page, entity, beginTime, endTime, regendtime,
					Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);
		} else {
			// 已签收会议
			if ("SIGN".equals(state)) {
				return queryForSignConferences(page, entity, beginTime,
						endTime, regendtime);
			} else if ("REPORTED".equals(state)) {
				// 已上报
				return query(page, entity, beginTime, endTime, regendtime,
						Constants.CONFERENCE_SEND_RECVSTATE_REPORTED);
			} else if ("ACCEPTED".equals(state)) {
				// 已受理
				return query(page, entity, beginTime, endTime, regendtime,
						Constants.CONFERENCE_SEND_RECVSTATE_ACCEPTED);
			}
			// 默认值 取待签收会议
			return query(page, entity, beginTime, endTime, regendtime,
					Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);

		}

	}

	/***************************************************************************
	 * 
	 * 方法简要描述：分页查找 已签收及重发的 下发通知表数据集
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @see
	 */
	private Page<TOmConferenceSend> queryForSignConferences(
			Page<TOmConferenceSend> page, TOmConferenceSend entity,
			Date beginTime, Date endTime, Date regendtime) {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql
				.append("from TOmConferenceSend t where 1=1 and t.TOmConference.conferenceState = ? ");
		paramLst.add(Constants.CONFERENCE_STATE_ISSIED); // 会议状态为已发送

		sql.append(" and t.recvState in (?,?)");
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_SIGN);
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_REBACK);

		if (StringUtil.isNotEmpty(entity)) {
			if (StringUtil.isNotEmpty(entity.getDeptCode())) {
				sql.append(" and t.deptCode = ? ");
				paramLst.add(entity.getDeptCode());
			}
			if(entity.getTOmConference()!=null){
			if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceFromId())) {
				sql.append("  and t.TOmConference.conferenceFromId like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceFromId() + "%");
			}
			}
			if (StringUtil.isNotEmpty(entity.getDeptCode())) {
				sql.append(" and t.deptCode = ? ");
				paramLst.add(entity.getDeptCode());
			}

		}
		if (StringUtil.isNotEmpty(beginTime)) {
			// sql.append(" and to_char(t.conferenceStime,'yyyy-MM-dd') = ?");
			sql.append(" and t.TOmConference.conferenceStime >= ?");
			paramLst.add(beginTime);
		}
		if (StringUtil.isNotEmpty(endTime)) {
			sql.append(" and t.TOmConference.conferenceEtime <= ?");
			paramLst.add(endTime);
		}

		if (StringUtil.isNotEmpty(regendtime)) {
			sql.append(" and t.TOmConference.conferenceRegendtime <= ? ");
			paramLst.add(regendtime);
		}

		return this.acceptDao.find(page, sql.toString(), paramLst.toArray());
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:分页查找通知下发表
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @see
	 */
	private Page<TOmConferenceSend> query(Page<TOmConferenceSend> page,
			TOmConferenceSend entity, Date beginTime, Date endTime,
			Date regendtime, String receState) {
		Assert.notNull(receState);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql
				.append("from TOmConferenceSend t where 1=1 and t.TOmConference.conferenceState = ? ");
		paramLst.add(Constants.CONFERENCE_STATE_ISSIED); // 会议状态为已发送

		sql.append(" and t.recvState = ? ");
		paramLst.add(receState);
		if (StringUtil.isNotEmpty(entity)) {
			if (StringUtil.isNotEmpty(entity.getTOmConference())) {
				if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceTitle())) {
				sql.append(" and  t.TOmConference.conferenceTitle like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceTitle() + "%");
				}
			}

			if (StringUtil.isNotEmpty(entity.getTOmConference())) {
				if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceAddr())) {
				sql.append(" and t.TOmConference.conferenceAddr like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceAddr() + "%");
				}
			}
			if (StringUtil.isNotEmpty(entity.getDeptCode())) {
				sql.append(" and  t.deptCode = ? ");
				paramLst.add(entity.getDeptCode());
			}

			if (StringUtil.isNotEmpty(entity.getDeptName())) {
				sql.append(" and t.deptName like ? ");
				paramLst.add("%" + entity.getDeptName() + "%");
			}
			
			if(entity.getTOmConference()!=null){
			  if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceFromId())) {
				sql.append("  and t.TOmConference.conferenceFromId like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceFromId() + "%");
			  }
			}
		}
		if (StringUtil.isNotEmpty(beginTime)) {
			// sql.append(" and to_char(t.conferenceStime,'yyyy-MM-dd') = ?");
			sql.append(" and t.TOmConference.conferenceRegendtime >= ?");
			paramLst.add(beginTime);
		}
		if (StringUtil.isNotEmpty(endTime)) {
			sql.append(" and t.TOmConference.conferenceRegendtime <= ?");
			paramLst.add(endTime);
		}

		//if (StringUtil.isNotEmpty(regendtime)) {
		//	sql.append(" and t.TOmConference.conferenceRegendtime <= ? ");
		//	paramLst.add(regendtime);
		//}
		sql.append(" order by  t.TOmConference.conferenceRegendtime desc ");
		return this.acceptDao.find(page, sql.toString(), paramLst.toArray());
	}
	
	/***
	 * 
	* 方法简要描述：获取已签收单位列表
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 17, 2013
	* @Author 万俊龙
	* @param page
	* @param entity
	* @param beginTime
	* @param endTime
	* @param regendtime
	* @return
	* @version	1.0
	* @see
	 */
	public Page<TOmConferenceSend> queryForReceived(Page<TOmConferenceSend> page,
			TOmConferenceSend entity, Date beginTime, Date endTime,
			Date regendtime) {
		 
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql
				.append("from TOmConferenceSend t where 1=1 and t.TOmConference.conferenceState = ? ");
		paramLst.add(Constants.CONFERENCE_STATE_ISSIED); // 会议状态为已发送

		sql.append(" and t.recvState not in (?,?) "); //发送单位不为默认状态 和 废除状态
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);
		paramLst.add(Constants.CONFERENCE_SEND_RECVSTATE_DEL);
		
		if (StringUtil.isNotEmpty(entity)) {
			if (StringUtil.isNotEmpty(entity.getTOmConference())) {
				if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceTitle())) {
				sql.append(" and  t.TOmConference.conferenceTitle like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceTitle() + "%");
				}
			}

			if (StringUtil.isNotEmpty(entity.getTOmConference())) {
				if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceAddr())) {
				sql.append(" and t.TOmConference.conferenceAddr like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceAddr() + "%");
				}
			}
			if (StringUtil.isNotEmpty(entity.getDeptCode())) {
				sql.append(" and  t.deptCode = ? ");
				paramLst.add(entity.getDeptCode());
			}

			if (StringUtil.isNotEmpty(entity.getDeptName())) {
				sql.append(" and t.deptName like ? ");
				paramLst.add("%" + entity.getDeptName() + "%");
			}
			if(entity.getTOmConference()!=null){
			if (StringUtil.isNotEmpty(entity.getTOmConference().getConferenceFromId())) {
				sql.append("  and t.TOmConference.conferenceFromId like ? ");
				paramLst.add("%" + entity.getTOmConference().getConferenceFromId() + "%");
			}
			}
		}
		if (StringUtil.isNotEmpty(beginTime)) {
			// sql.append(" and to_char(t.conferenceStime,'yyyy-MM-dd') = ?");
			sql.append(" and t.TOmConference.conferenceRegendtime >= ?");
			paramLst.add(beginTime);
		}
		if (StringUtil.isNotEmpty(endTime)) {
			sql.append(" and t.TOmConference.conferenceRegendtime <= ?");
			paramLst.add(endTime);
		}

		//if (StringUtil.isNotEmpty(regendtime)) {
		//	sql.append(" and t.TOmConference.conferenceRegendtime <= ? ");
		//	paramLst.add(regendtime);
		//}
		sql.append("order by t.TOmConference.conferenceRegendtime desc ");
		return this.acceptDao.find(page, sql.toString(), paramLst.toArray());
	}
	
	/***
	 * 
	* 方法简要描述：获取已发会议通知中所有下派部门信息
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 10, 2013
	* @Author 万俊龙
	* @param conId
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmConferenceSend> findIssureConferenceDepts(String conId){
		Assert.notNull(conId);
		TOmConferenceSend entity=new TOmConferenceSend();
		entity.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_DEFAULT);
		return find(conId,entity);
	}
	
	/****
	 * 
	* 方法简要描述:根据会议编号，查找会议中符合条件的下派单位集
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 10, 2013
	* @Author 万俊龙
	* @param conId
	* @param entity
	* @return
	* @version	1.0
	* @see
	 */
	private List<TOmConferenceSend> find(String conId,TOmConferenceSend entity){
		Assert.notNull(conId);
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConferenceSend t where 1=1 ");
		sql.append(" and t.TOmConference.conferenceId=? ");
		paramLst.add(conId);
		if(StringUtil.isNotEmpty(entity)){
			if(StringUtil.isNotEmpty(entity.getRecvState())){
				sql.append(" and t.recvState= ? ");
				paramLst.add(entity.getRecvState());
			}
			if (StringUtil.isNotEmpty(entity.getDeptCode())) {
				sql.append(" and  t.deptCode = ? ");
				paramLst.add(entity.getDeptCode());
			}

			if (StringUtil.isNotEmpty(entity.getDeptName())) {
				sql.append(" and t.deptName like ? ");
				paramLst.add("%" + entity.getDeptName() + "%");
			}
			if(StringUtil.isNotEmpty(entity.getConRecvUser())){
				sql.append(" and t.conRecvUser like ? ");
				paramLst.add("%" + entity.getConRecvUser() + "%");
			}
		}
		
		return this.acceptDao.find(sql.toString(),paramLst.toArray());
	}
	
	/***************************************************************************
	 * 
	 * 方法简要描述:构造附件展现html浮层，在前台页面需设定 function deldbobj()方法
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param request
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getConAttachHtmlByConId(HttpServletRequest request,
			String conNoticeId) {
		StringBuffer contentsHtml = new StringBuffer();
		List<TOmConAttach> list = this.conAttachManager
				.getAttachsByAttachConId(conNoticeId, false);
		if (StringUtil.isNotEmpty(list)) {
			for (TOmConAttach attach : list) {
				// 使用该预留字段标识附件是否为正文[0:不是|1:是]
				String type = attach.getAttachRest1();
				StringBuilder temp = new StringBuilder();
				temp.append("<div id=div" + attach.getAttachId()
						+ " attachtype=" + type + ">");
			//	temp.append("[<a onclick=\"deldbobj('" + attach.getAttachId()
			//			+ "');\"");
			//	temp.append("href=\"#\">删除</a>]");

				temp.append("<a ");
				temp
						.append("href=\""
								+ request.getContextPath()
								+ "/noticeconference/noticeConference!downloadConAttach.action?conAttachId="
								+ attach.getAttachId()
								+ "\"><font color='red'>");
				temp.append(attach.getAttachFileName());
				temp.append("</font></a>");
				temp.append("</div>");
			//	temp.append("<input value=" + attach.getAttachId() + " id="
			//			+ attach.getAttachId() + " type=\"hidden\" ");
			//	temp.append(" name=\"dbobj\" />");
				contentsHtml.append(temp.toString());
			}
			return contentsHtml.toString();
		}
		return null;

	}
	
	
}
