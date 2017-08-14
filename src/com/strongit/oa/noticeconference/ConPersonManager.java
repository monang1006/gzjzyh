package com.strongit.oa.noticeconference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.TOmConPerson;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmDeptreport;
import com.strongit.oa.noticeconference.util.Constants;
import com.strongit.oa.noticeconference.util.StringUtil;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 
 * 
 * @Description: ConPersonManager.java
 * @Date:Apr 7, 2013
 * @Author 万俊龙
 * @Version: V1.0
 * @Copyright: Jiang Xi Strong Co. Ltd. All right reserved.
 */
@Service
@Transactional
public class ConPersonManager implements IConPersonManager {

	private GenericDAOHibernate<TOmConPerson, java.lang.String> personDao;


	private GenericDAOHibernate<TOmDeptreport, java.lang.String> memberReportDao;// 单位上报表TOmConferenceSend
	private GenericDAOHibernate<TOmConferenceSend, java.lang.String> conferenceSendDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		personDao = new GenericDAOHibernate<TOmConPerson, String>(
				sessionFactory, TOmConPerson.class);
		memberReportDao = new GenericDAOHibernate<TOmDeptreport, String>(
				sessionFactory, TOmDeptreport.class);
		conferenceSendDao = new GenericDAOHibernate<TOmConferenceSend, String>(
				sessionFactory, TOmConferenceSend.class);

	}

 
	/***************************************************************************
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
	 */
	public void save(TOmConPerson entity) {
		Assert.notNull(entity);
		this.personDao.save(entity);
	}

	/***************************************************************************
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
	 */
	public void saveToReport(String sendId, TOmConPerson entity) {
		Assert.notNull(sendId);
		Assert.notNull(entity);

		// 判断人员基础表中是否存在
		List<TOmConPerson> list = find(entity);
		if (StringUtil.isNotEmpty(list)) {
			entity = list.get(0);
		} else {
			this.personDao.save(entity);
		}

		TOmConferenceSend sendEntity = this.conferenceSendDao.get(sendId);
		TOmDeptreport model = new TOmDeptreport();
		model.setConferee(entity);
		model.setTOmConferenceSend(sendEntity);
		model.setReportUser(entity.getPersonName());
		model.setReportTime(new Date());
		model.setState(Constants.DEPT_REPORT_WAIT_STATE);
		this.memberReportDao.save(model);
	}

	/**
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
	 */
	public void saveToReport(String sendId, List<TOmConPerson> list) {

		Assert.notNull(sendId);
		Assert.notNull(list);
		List<TOmDeptreport> oldReportLst = this.findDeptreport(sendId, null);
		//this.memberReportDao.delete(oldReportLst);
	//	if (false) {
			TOmConferenceSend sendEntity = this.conferenceSendDao.get(sendId);
			sendEntity
					.setRecvState(Constants.CONFERENCE_SEND_RECVSTATE_REPORTED);// 已上报状态
			this.conferenceSendDao.update(sendEntity);
			List<TOmDeptreport> reportLst = new ArrayList();
			for (TOmConPerson entity : list) {
				// 判断人员基础表中是否存在
				List<TOmConPerson> plist = find(entity);
				if (StringUtil.isNotEmpty(plist)) {
					entity = plist.get(0);
				} else {
					this.personDao.save(entity);
				}
				TOmDeptreport model = getDeptreportModel(sendEntity, entity);
				if (StringUtil.isNotEmpty(model)) {
					// 过滤删除项
					if (StringUtil.isNotEmpty(oldReportLst)) {
						for (TOmDeptreport report : oldReportLst) {
							if (report.getReportingId().equals(
									model.getReportingId())) {
								oldReportLst.remove(report);
								break;
							}
						}
					}

					model.setState(Constants.DEPT_REPORT_WAIT_STATE);
					this.memberReportDao.update(model);
				} else {
					model = new TOmDeptreport();
					model.setConferee(entity);
					model.setTOmConferenceSend(sendEntity);
					model.setReportUser(entity.getPersonName());
					model.setReportTime(new Date());
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
					/*
					 * for (TOmDeptreport entity : oldReportLst) { TOmDeptreport
					 * temp = this.getDeptreportModel(entity
					 * .getTOmConferenceSend(), entity.getConferee());
					 * this.memberReportDao.delete(temp); }
					 */
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		//}

	}

	/***************************************************************************
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
	 */
	public TOmDeptreport getDeptreportModel(TOmConferenceSend sendEntity,
			TOmConPerson personEntity) {
		Assert.notNull(sendEntity);
		Assert.notNull(personEntity);

		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" from TOmDeptreport t where 1=1 ");
		if (StringUtil.isNotEmpty(sendEntity)) {
			sql.append(" and t.TOmConferenceSend.sendconId=? ");
			paramLst.add(sendEntity.getSendconId());
		}
		if (StringUtil.isNotEmpty(personEntity)) {
			sql.append(" and t.conferee.personId=? ");
			paramLst.add(personEntity.getPersonId());
		}
		List<TOmDeptreport> list = this.memberReportDao.find(sql.toString(),
				paramLst.toArray());
		if (StringUtil.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	/***************************************************************************
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
	 */
	public void deleteMembersReport(String deptId) {
		List<TOmDeptreport> list = this.findWaitStateMembersByDeptId(deptId);
		if (StringUtil.isNotEmpty(list)) {
			this.memberReportDao.delete(list);
		}
	}

	/***************************************************************************
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
	 */
	public List<TOmDeptreport> findWaitStateMembersByDeptId(String deptId) {
		Assert.notNull(deptId);
		return findDeptreport(deptId, Constants.DEPT_REPORT_WAIT_STATE);
	}

	/***************************************************************************
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
	 */
	public List<TOmDeptreport> findDeptreport(String sendId, String state) {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(" from TOmDeptreport t where 1=1 ");
		if (StringUtil.isNotEmpty(sendId)) {
			sql.append(" and t.TOmConferenceSend.sendconId=? ");
			paramLst.add(sendId);
		}
		if (StringUtil.isNotEmpty(state)) {
			sql.append(" and t.state=? ");
			paramLst.add(state);
		}
		return this.memberReportDao.find(sql.toString(), paramLst.toArray());

	}

	/***************************************************************************
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
	 */
	public void saveToReport(String sendId, String personId) {
		Assert.notNull(sendId);
		Assert.notNull(personId);
		TOmConferenceSend sendEntity = this.conferenceSendDao.get(sendId);
		TOmConPerson personEntity = this.personDao.get(personId);
		TOmDeptreport model = new TOmDeptreport();
		model.setConferee(personEntity);
		model.setTOmConferenceSend(sendEntity);
		model.setReportUser(personEntity.getPersonName());
		model.setReportTime(new Date());
		model.setState(Constants.DEPT_REPORT_WAIT_STATE);
		this.memberReportDao.save(model);
	}

	/***************************************************************************
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
	 */
	public void save(List<TOmConPerson> list) {
		Assert.notNull(list);
		this.personDao.save(list);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：删除上报人员信息
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
	 */
	public void delete(TOmConPerson entity) {
		Assert.notNull(entity);
		this.personDao.delete(entity);
	}

	/***************************************************************************
	 * 
	 * 方法简要描述：批量删除上报人员信息
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
	 */
	public void delete(List<TOmConPerson> list) {
		Assert.notNull(list);
		this.personDao.delete(list);
	}

	/***************************************************************************
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
	 */
	public List<TOmConPerson> findByDeptid(String deptId) {
		// 测试环境 无需过滤单位数据
		// Assert.notNull(deptId);

		TOmConPerson entity = new TOmConPerson();
		entity.setDepid(deptId);
		return find(entity);
	}

	/***************************************************************************
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
	 */
	private List<TOmConPerson> find(TOmConPerson entity) {
		List paramLst = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("from TOmConPerson t where 1=1 ");
		if (StringUtil.isNotEmpty(entity)) {
			if (StringUtil.isNotEmpty(entity.getDepid())) {
				sql.append(" and t.depid= ?");
				paramLst.add(entity.getDepid());
			}

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
		return this.personDao.find(sql.toString(), paramLst.toArray());
	}

	/***************************************************************************
	 * 
	 * 方法简要描述:根据单位编号，人员姓名，职位及性别，判断用户公共表中是否存在相同的记录 <br>
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
	 */
	public boolean hasPerson(String deptId, String personName,
			String personPost, String personSax) {
		Assert.notNull(deptId);
		Assert.notNull(personName);
		Assert.notNull(personPost);
		Assert.notNull(personSax);

		TOmConPerson entity = new TOmConPerson();
		entity.setDepid(deptId);
		entity.setPersonName(personName);
		entity.setPersonPost(personPost);
		entity.setPersonSax(personSax);
		List<TOmConPerson> list = this.find(entity);
		if (StringUtil.isNotEmpty(list)) {
			return true;
		}
		return false;
	}

}
