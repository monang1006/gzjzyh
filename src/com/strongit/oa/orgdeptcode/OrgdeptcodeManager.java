package com.strongit.oa.orgdeptcode;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingAttendance;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.bo.ToaOrgRule;
import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseRole;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class OrgdeptcodeManager extends BaseManager {

	private GenericDAOHibernate<ToaOrgRule, String> orgruleDao = null;

	private GenericDAOHibernate<ToaRule, java.lang.String> ruleDao;

	public OrgdeptcodeManager() {

	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		orgruleDao = new GenericDAOHibernate<ToaOrgRule, String>(
				sessionFactory, ToaOrgRule.class);
		ruleDao = new GenericDAOHibernate<ToaRule, java.lang.String>(
				sessionFactory, ToaRule.class);
	}

	public ToaOrgRule getoneToaOrgRule(String tid) throws DAOException,
			SystemException, ServiceException {
		return orgruleDao.get(tid);
	}

	@Transactional(readOnly = false)
	public void saveToaOrgRule(ToaOrgRule toSort) throws DAOException,
			SystemException, ServiceException {
		orgruleDao.save(toSort);
	}
	
	@Transactional(readOnly = false)
	public void deleteToaOrgRules(List<ToaOrgRule> list) throws DAOException,
			SystemException, ServiceException {
		   orgruleDao.delete(list);
	}

	public List<ToaOrgRule> getAllListByOrgId(String orgid) {
		StringBuffer hql = new StringBuffer("from ToaOrgRule t where 1 = 1");
		hql.append(" and t.deptId like '%").append(orgid).append("%'");
		return orgruleDao.find(hql.toString(), null);
	}
	
	public List<ToaOrgRule> getListByOrgRuleId(String orgid,String ruleids) {
		StringBuffer hql = new StringBuffer("from ToaOrgRule t where 1 = 1");
		hql.append(" and t.deptId like '%").append(orgid).append("%'");
		if(ruleids!=null && ruleids!=""){
			hql.append(" and t.ruleId in (").append(ruleids.toString()).append(")");
		}
		return orgruleDao.find(hql.toString(), null);
	}

	public Page<ToaRule> getRuleList(Page<ToaRule> page, String ruleName,
			String orgid) {
		Object[] obj = new Object[1];
		StringBuffer hql = new StringBuffer("from ToaRule t where 1 = 1");

		if (ruleName != null && !ruleName.equals("")) {
			hql.append(" and t.ruleName like ?");
			obj[0] = "%" + ruleName + "%";
		} else {
			hql.append(" and 1=?");
			obj[0] = 1;
		}

		  String ruleids = "''";
			List<ToaOrgRule> orgrule = this.getAllListByOrgId(orgid);
			if (orgrule != null && orgrule.size() > 0) {
				ruleids += ",";
				for (Iterator<ToaOrgRule> it = orgrule.iterator(); it.hasNext();) {
					ToaOrgRule role = it.next();
					ruleids += "'" + role.getRuleId() + "',";
				}	
			}
			StringBuffer ru=new StringBuffer(ruleids);
			if(ruleids.indexOf(",")>0){
			  ru.deleteCharAt(ruleids.lastIndexOf(","));
			}
			hql.append(" and t.id in (").append(ru.toString()).append(")");
     		page = ruleDao.find(page, hql.toString(), obj);
		return page;
	}
	
	public Page<ToaRule> getAllRuleList(Page<ToaRule> page, String ruleName, String extOrgId) {
		Object[] obj = new Object[1];
		StringBuffer hql = new StringBuffer("from ToaRule t where 1 = 1");

		if (ruleName != null && !ruleName.equals("")) {
			hql.append(" and t.ruleName like ?");
			obj[0] = "%" + ruleName + "%";
		} else {
			hql.append(" and 1=?");
			obj[0] = 1;
		}
		//该部门已添加过的规则不再显示在可添加列表中
		if (extOrgId != null && !"".equals(extOrgId)){
			hql.append(" and t.id not in ( SELECT ro.ruleId FROM ToaOrgRule ro WHERE ro.deptId='" + extOrgId +"')");
		}
		page = ruleDao.find(page, hql.toString(), obj);
		return page;
	}
	
	/**
	 * @author:luosy
	 * @description: 获取某ORG下的所有编号规则
	 * @date : 2011-5-27
	 * @modifyer:
	 * @description:
	 * @param orgid
	 * @return
	 */
	public String getAllRuleByOrgId(String orgid) {
		String hql = "select t.ruleId from ToaOrgRule t where 1 = 1";
		hql = hql+" and t.deptId like '%"+orgid+"%'";
		List<String> list = orgruleDao.find(hql.toString(), null);
		if(list==null){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<list.size();i++){
			String rule = list.get(i);
			if(rule!=null&&!"".equals(rule)){
				sb.append("'").append(rule).append("'").append(",");
			}
		}
		if(sb!=null&&sb.length()>1){
			return sb.delete(sb.length()-1, sb.length()).toString();
		}else{
			return "";
		}
	}
}
