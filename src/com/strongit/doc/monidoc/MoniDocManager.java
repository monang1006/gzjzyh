package com.strongit.doc.monidoc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.doc.bo.TdocStampLog;
import com.strongit.oa.util.FiltrateContent;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.IGenericDAO;
import com.strongmvc.orm.hibernate.Page;

/**
 * 盖章日志服务类.
 * 
 * @author
 * @company Strongit Ltd. (C) copyright
 * @date
 * @version 2.0.2.3
 * @classpath
 * @comment
 * @email
 */
@Service
@Transactional
public class MoniDocManager {

	IGenericDAO<TdocStampLog, java.lang.String> stampLogDao = null; // 定义DAO操作类.

	/**
	 * 注入SESSION工厂
	 * 
	 * @author:
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		stampLogDao = new GenericDAOHibernate<TdocStampLog, java.lang.String>(
				sessionFactory, TdocStampLog.class);
	}

	/**
	 * 得到标签分页列表.
	 * 
	 * @author:
	 * @date:2010-7-7 下午05:37:39
	 * @param page
	 *            分页对象
	 * @param model
	 *            BO对象,用于传输查询参数.
	 * @return 分页对象
	 */
	Page<TdocStampLog> getStampLogList(Page<TdocStampLog> page,
			TdocStampLog model, Date startDate, Date endDate) {
		Page<TdocStampLog> page1;
		ArrayList list = new ArrayList();
		Calendar te = Calendar.getInstance();
		StringBuilder hql = new StringBuilder("from TdocStampLog t where 1=1 ");
		if (model != null) {
			// 单位
			if (model.getDepartName() != null
					&& !"".equals(model.getDepartName())) {
				hql.append(" and t.departName like '%"
						+ FiltrateContent.getNewContent(model.getDepartName()).trim()
						+ "%'"); 
			}
			// 名
			if (model.getStampName() != null
					&& !"".equals(model.getStampName())) {
				hql.append(" and t.stampName like '%"
						+ FiltrateContent.getNewContent(model.getStampName()).trim()
						+ "%'");
			}
			// 持有人
			if (model.getStampUser() != null
					&& !"".equals(model.getStampUser())) {
				hql.append(" and t.stampUser like '%"
						+ FiltrateContent.getNewContent(model.getStampUser()).trim()
						+ "%'");
			}
			// 盖章人
			if (model.getOperateUser() != null
					&& !"".equals(model.getOperateUser())) {
				hql.append(" and t.operateUser like '%"
						+ FiltrateContent.getNewContent(model.getOperateUser()).trim()
						+ "%'");
			}
			// 盖章人
			if (model.getStampType() != null
					&& !"".equals(model.getStampType())) {
				hql.append(" and t.stampType = '"
						+ FiltrateContent.getNewContent(model.getStampType()).trim()
						+ "'");
			}
			// 公文文号
			if (model.getDocCode() != null && !"".equals(model.getDocCode())) {
				hql.append(" and t.docCode like '%"
						+ FiltrateContent.getNewContent(model.getDocCode()).trim()
						+ "%'");
			}
		}
		// 在某个时间段内
		if (startDate != null && !"".equals(startDate)) { 
			te.setTime(startDate);
			list.add(te.getTime());
			hql.append(" and t.operateTime >=? "); 
		}
		if (endDate != null && !"".equals(endDate)) { 
			te.setTime(endDate);
			te.set(Calendar.HOUR_OF_DAY, 23);
			te.set(Calendar.MINUTE, 59);
			list.add(te.getTime());
			hql.append(" and t.operateTime <= ? "); 

		}

		if (list.isEmpty()) {
			page1 = stampLogDao.find(page, hql.append(
					" order by t.operateTime desc").toString());
		} else {
			page1 = stampLogDao.find(page, hql.append(
					" order by t.operateTime desc").toString(), list.toArray());

		}
		return page1;
		// return page=stampLogDao.findAll(page);
	}

	/**
	 * 保存
	 * 
	 * @author:
	 * @date:
	 * @param model
	 */
	public void save(TdocStampLog model) {
		StringBuilder hql = new StringBuilder("select count(*) from TdocStampLog t where 1=1 ");
		List<Object> params = new ArrayList<Object>(3);
		if(model.getOperateTime() != null){
			hql.append(" and t.operateTime = ?");
			params.add(model.getOperateTime());
		}
		if(model.getDocCode() != null) {
			hql.append(" and t.docCode='"+model.getDocCode()+"'");
		}
		if(model.getOperateUser() != null && !"".equals(model.getOperateUser())){
			hql.append(" and t.operateUser = ?");
			params.add(model.getOperateUser());
		}
		if(model.getStampName() != null && !"".equals(model.getStampName())){
			hql.append(" and t.stampName = ?");
			params.add(model.getStampName());
		}
		if(model.getStampType() != null && !"".equals(model.getStampType())){
			hql.append(" and t.stampType = ?");
			params.add(model.getStampType());
		}
		Long count = stampLogDao.findLong(hql.toString(), params.toArray());
		if(count != null && count == 0){
			stampLogDao.save(model);
			System.out.println("opttime***********" + model.getOperateTime()+"---" + model.getStampType()); 
		}
	}

	/**
	 * 校验日志记录是否存在
	 * @author:邓志城
	 * @date:2010-8-30 下午03:01:11
	 * @param model
	 * @return
	 */
	public boolean isLogExist(TdocStampLog model) {
		StringBuilder hql = new StringBuilder("select count(*) from TdocStampLog t where 1=1 ");
		List<Object> params = new ArrayList<Object>(3);
		if(model.getOperateTime() != null){
			hql.append(" and t.operateTime = ?");
			params.add(model.getOperateTime());
		}
		if(model.getOperateUser() != null && !"".equals(model.getOperateUser())){
			hql.append(" and t.operateUser = ?");
			params.add(model.getOperateUser());
		}
		if(model.getStampName() != null && !"".equals(model.getStampName())){
			hql.append(" and t.stampName = ?");
			params.add(model.getStampName());
		}
		/*if(model.getStampType() != null && !"".equals(model.getStampType())){
			hql.append(" and t.stampType = ?");
			params.add(model.getStampType());
		}*/
		Long count = stampLogDao.findLong(hql.toString(), params.toArray());
		if(count != null && count == 0){
			return false;
		}
		return true;
	}
}
