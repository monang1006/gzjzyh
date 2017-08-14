/*package com.strongit.oa.noticeconference;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.ToaSeatsetAttendMember;
import com.strongit.oa.bo.ToaSeatsetDep;
import com.strongit.oa.bo.ToaSeatsetPerson;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

*//*******************************************************************************
 * 参会成员上报管理类
 * 
 * @Description: MemberReportManager.java
 * @Date:Apr 23, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 *//*

@Service
@Transactional
public class MemberReportManager  implements IMemberReportManager {
	private GenericDAOHibernate<ToaSeatsetDep, java.lang.String> seatDeptDao; // 领导名册
																				// 单位机构
	private GenericDAOHibernate<ToaSeatsetPerson, java.lang.String> baseMenberDao; // 领导名册
	private GenericDAOHibernate<ToaSeatsetAttendMember, java.lang.String> memberReportDao;// 参会人员上报
	private GenericDAOHibernate<TOmConferenceSend, java.lang.String> conferenceSendDao; // 会议通知

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {

		seatDeptDao = new GenericDAOHibernate<ToaSeatsetDep, String>(
				sessionFactory, ToaSeatsetDep.class);
		memberReportDao = new GenericDAOHibernate<ToaSeatsetAttendMember, String>(
				sessionFactory, ToaSeatsetAttendMember.class);
		baseMenberDao = new GenericDAOHibernate<ToaSeatsetPerson, String>(
				sessionFactory, ToaSeatsetPerson.class);
		conferenceSendDao = new GenericDAOHibernate<TOmConferenceSend, String>(
				sessionFactory, TOmConferenceSend.class);

	}

	*//***************************************************************************
	 * 
	 * 方法简要描述: 获取领导名册用户信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 *//*

	public List<ToaSeatsetPerson> findAll() {
		return this.baseMenberDao.findAll();
	}

	*//***************************************************************************
	 * 
	 * 方法简要描述：保存上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 9, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 *//*
	public void save(ToaSeatsetAttendMember entity) {
		Assert.notNull(entity);
		this.memberReportDao.save(entity);
	}

	*//***************************************************************************
	 * 
	 * 方法简要描述:新增单位上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param sendId
	 * @param entity
	 * @version 1.0
	 * @see
	 *//*
	public void saveToReport(String sendId, ToaSeatsetPerson entity) {
		Assert.notNull(sendId);
		Assert.notNull(entity);
		// 查找领导名册中是否存在用户信息
		List<ToaSeatsetPerson> list = this.find(sendId, entity);
		if (StringUtil.isNotEmpty(list)) {
			entity = list.get(0);
		} else {
			this.baseMenberDao.save(entity);
		}
		ToaSeatsetAttendMember model = new ToaSeatsetAttendMember();
		model.setDepId(sendId);
		model.setDepName(entity.getBaseDep().getDepFullName());
		model.setIsParty(entity.getPersonIsParty());
		model.setMemberId(entity.getPersonId());// 成员id
		model.setMemberLevel(entity.getPersonLevel());
		model.setMemberName(entity.getPersonName());
		model.setMemberNum(entity.getPersonNum());
		model.setMemberPost(entity.getPersonPost());
		model.setState(Constants.DEPT_REPORT_WAIT_STATE);
		this.memberReportDao.save(model);
	}

	*//**
	 * 
	 * 方法简要描述：批量保存单位用户上报信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param sendId
	 * @param list
	 * @version 1.0
	 * @see
	 *//*
	public void saveToReport(String sendId, List<ToaSeatsetPerson> list) {

		Assert.notNull(sendId);
		Assert.notNull(list);
		// 查找单位已上报人员信息
		List<ToaSeatsetAttendMember> oldReportLst = this.findDeptreport(sendId,
				null);

		// 更新上报单位状态
		TOmConferenceSend sendEntity = this.conferenceSendDao.get(sendId);
		sendEntity.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_REPORTED);// 已上报状态
		this.conferenceSendDao.update(sendEntity);

		List<ToaSeatsetAttendMember> reportLst = new ArrayList();

		for (ToaSeatsetPerson entity : list) {
			// 检查领导名册表中用户信息是否存在，不存在，添加用户名册/
			List<ToaSeatsetPerson> plist = find(sendId, entity);
			if (StringUtil.isNotEmpty(plist)) {
				entity = plist.get(0);
			} else {
				this.baseMenberDao.save(entity);
			}

			// 检查
			ToaSeatsetAttendMember model = getAttendMember(sendId, entity
					.getPersonId());
			if (StringUtil.isNotEmpty(model)) {
				// 过滤删除项
				if (StringUtil.isNotEmpty(oldReportLst)) {
					for (ToaSeatsetAttendMember report : oldReportLst) {
						if (report.getAttendMemberId().equals(
								model.getAttendMemberId())) {
							oldReportLst.remove(report);
							break;
						}
					}
				}
				model.setState(Constants.DEPT_REPORT_WAIT_STATE);
				this.memberReportDao.update(model);
			} else {
				model = new ToaSeatsetAttendMember();
				model.setDepId(sendId);
				model.setDepName(entity.getBaseDep().getDepFullName());
				model.setIsParty(entity.getPersonIsParty());
				model.setMemberId(entity.getPersonId());// 成员id
				model.setMemberLevel(entity.getPersonLevel());
				model.setMemberName(entity.getPersonName());
				model.setMemberNum(entity.getPersonNum());
				model.setMemberPost(entity.getPersonPost());
				model.setState(Constants.DEPT_REPORT_WAIT_STATE);
				reportLst.add(model);
			}
		}
		if (StringUtil.isNotEmpty(reportLst)) {
			this.memberReportDao.save(reportLst);
		}
		try {
			if (StringUtil.isNotEmpty(oldReportLst)) {
				this.memberReportDao.delete(oldReportLst);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	*//***************************************************************************
	 * 
	 * 方法简要描述：根据下发单位编号he
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param conSendId
	 * @param personId
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	public ToaSeatsetAttendMember getAttendMember(String deptId,
			String seatPersonId) {
		Assert.notNull(deptId);
		Assert.notNull(seatPersonId);

		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from ToaSeatsetAttendMember t where 1=1 ");
		sql.append(" and t.memberId = ? ");
		paramLst.add(seatPersonId);

		sql.append(" and t.depId = ? ");
		paramLst.add(deptId);

		List<ToaSeatsetAttendMember> list = this.memberReportDao.find(sql
				.toString(), paramLst.toArray());
		if (StringUtil.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	 
	*//***************************************************************************
	 * 
	 * 方法简要描述：删除指定单位下待上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @version 1.0
	 * @see
	 *//*
	public void deleteMembersReport(String deptId) {
		List<ToaSeatsetAttendMember> list = this
				.findWaitStateMembersByDeptId(deptId);
		if (StringUtil.isNotEmpty(list)) {
			this.memberReportDao.delete(list);
		}
	}

	*//***************************************************************************
	 * 
	 * 方法简要描述：查找单位待上报的人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	public List<ToaSeatsetAttendMember> findWaitStateMembersByDeptId(
			String deptId) {
		Assert.notNull(deptId);
		return findDeptreport(deptId, Constants.DEPT_REPORT_WAIT_STATE);
	}

	*//***************************************************************************
	 * 
	 * 方法简要描述：查找人员上报信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param sendId
	 * @param state
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	public List<ToaSeatsetAttendMember> findDeptreport(String sendId,
			String state) {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" from ToaSeatsetAttendMember t where 1=1 ");
		if (StringUtil.isNotEmpty(sendId)) {
			sql.append(" and t.depId = ? ");
			paramLst.add(sendId);
		}
		if (StringUtil.isNotEmpty(state)) {
			sql.append(" and t.state=? ");
			paramLst.add(state);
		}
		return this.memberReportDao.find(sql.toString(), paramLst.toArray());

	}

 

	*//***************************************************************************
	 * 
	 * 方法简要描述：批量保存上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 9, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 *//*
	public void save(List<ToaSeatsetPerson> list) {
		Assert.notNull(list);
		this.baseMenberDao.save(list);
	}

 

	*//***************************************************************************
	 * 
	 * 方法简要描述:查找单位下上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 10, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	public List<ToaSeatsetPerson> findByDeptid(String deptId) {
		// 测试环境 无需过滤单位数据
		Assert.notNull(deptId);
		return find(deptId, null);
	}

 

	*//***************************************************************************
	 * 
	 * 方法简要描述:根据单位编号，人员姓名，职位及性别，判断领导名册中是否存在相同的记录 <br>
	 * true:表示存在 false：表示不存在 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 11, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @param name
	 * @param personPost
	 * @param sax
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	public boolean hasPerson(String deptId, String personName,
			String personPost, String personSax) {
		Assert.notNull(deptId);
		Assert.notNull(personName);
		Assert.notNull(personPost);
		Assert.notNull(personSax);

		ToaSeatsetPerson entity = new ToaSeatsetPerson();
		 
		entity.setPersonName(personName);
		entity.setPersonPost(personPost);
		entity.setPersonSax(personSax);
		List<ToaSeatsetPerson> list = this.find(deptId, entity);
		if (StringUtil.isNotEmpty(list)) {
			return true;
		}
		return false;
	}

	*//***************************************************************************
	 * 
	 * 方法简要描述：根据指定条件查找上报人员信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 10, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	private List<ToaSeatsetPerson> find(String deptId, ToaSeatsetPerson entity) {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from ToaSeatsetPerson t where 1=1 ");
		if (StringUtil.isNotEmpty(deptId)) {
			sql.append(" and t.baseDep.depId = ?");
			paramLst.add(deptId);
		}
		if (StringUtil.isNotEmpty(entity)) {

			if (StringUtil.isNotEmpty(entity.getPersonName())) {
				sql.append(" and t.personName= ?");
				paramLst.add(entity.getPersonName());
			}

			if (StringUtil.isNotEmpty(entity.getPersonPost())) {
				sql.append(" and t.personPost= ?");
				paramLst.add(entity.getPersonPost());
			}

			if (StringUtil.isNotEmpty(entity.getPersonSax())) {
				sql.append(" and t.personSax= ?");
				paramLst.add(entity.getPersonSax());
			}
		}
		return this.baseMenberDao.find(sql.toString(), paramLst.toArray());
	}

	*//***************************************************************************
	 * 
	 * 方法简要描述：获得领导名册组织机构实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 23, 2013
	 * @Author 万俊龙
	 * @param deptId
	 * @return
	 * @version 1.0
	 * @see
	 *//*
	public ToaSeatsetDep getSeatSetDept(String deptId) {
		Assert.notNull(deptId);
		return this.seatDeptDao.get(deptId);
	}
	
	*//****
	 * 
	* 方法简要描述：根据领导名册用户信息编号主键，获取实体对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 24, 2013
	* @Author 万俊龙
	* @param personId
	* @return
	* @version	1.0
	* @see
	 *//*
	public ToaSeatsetPerson getSeatSetPerson(String personId)
	{
		Assert.notNull(personId);
		return this.baseMenberDao.get(personId);
	}
	
	*//***
	 * 
	* 方法简要描述：更新单位上报人员信息表
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 24, 2013
	* @Author 万俊龙
	* @param entity
	* @version	1.0
	* @see
	 *//*
	public void updateAttenMember(ToaSeatsetAttendMember entity){
		Assert.notNull(entity);
		this.memberReportDao.update(entity);
		
	}
}
*/