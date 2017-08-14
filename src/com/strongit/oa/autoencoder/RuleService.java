package com.strongit.oa.autoencoder;

import java.util.List;

import javax.transaction.SystemException;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaRule;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.orgdeptcode.OrgdeptcodeManager;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

@Service
@Transactional
public class RuleService implements IRuleService {

	private GenericDAOHibernate<ToaRule, java.lang.String> ruleDao;
	
	@Autowired private IUserService userService;
	
	@Autowired private OrgdeptcodeManager orgdeptcodeManager;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		ruleDao = new GenericDAOHibernate<ToaRule, java.lang.String>(
				sessionFactory, ToaRule.class);
	}

	/**
	 * 保存一条规则
	 * 
	 * @see com.strongit.oa.autoencoder.IRuleService#save(com.strongit.oa.bo.ToaRule)
	 */
	public boolean save(ToaRule rule) throws ServiceException, SystemException {
		if (rule.getRule().equals("") || rule.getRuleName().equals("")) {
			return false;
		} else {
			//对相关数据进行保存
			ruleDao.save(rule);
			return true;
		}
	}
	
	public List<ToaRule> getAllList(){
		StringBuffer hql = new StringBuffer("from ToaRule t where 1 = 1");
		String userId = userService.getCurrentUser().getUserId();
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
		if(!userService.isSystemDataManager(userId)){					//如果是非分级授权管理员
			if(userService.isViewChildOrganizationEnabeld()){			//是否允许看到下级机构
				if(org!=null){
					hql.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
				}
			}else {
				if(org!=null){
					hql.append(" and t.orgId = '").append(org.getOrgId()).append("'");
				}
			}
		}
		return ruleDao.find(hql.toString(), null);
	}

	/**
	 * 删除一条规则
	 * 
	 * @see com.strongit.oa.autoencoder.IRuleService#deleteRuleById(java.lang.String)
	 */
	public void deleteRuleById(String id) throws ServiceException,
			SystemException {
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			ruleDao.delete(ids[i]);
		}

	}

	/**
	 * 取得一条规则
	 * 
	 * @see com.strongit.oa.autoencoder.IRuleService#getRuleById(java.lang.String)
	 */
	public ToaRule getRuleById(String id) throws ServiceException,
			SystemException {
		return ruleDao.get(id);
	}

	/**
	 * 更新规则
	 * 
	 * @see com.strongit.oa.autoencoder.IRuleService#update(com.strongit.oa.bo.ToaRule)
	 */
	public void update(ToaRule ToaRule) throws ServiceException,
			SystemException {
		ToaRule r = ruleDao.findById(ToaRule.getId(), true);
		r.setRuleName(ToaRule.getRuleName());
		r.setRule(ToaRule.getRule());
		ruleDao.update(r);
	}

	/**
	 * 取得规则列表(翻页)
	 * 
	 * @see com.strongit.oa.autoencoder.IRuleService#getRuleList(com.strongmvc.orm.hibernate.Page,
	 *      com.strongit.oa.bo.ToaRule)
	 */
	public Page<ToaRule> getRuleList(Page<ToaRule> page, ToaRule model)
			throws ServiceException, SystemException {

		Object[] obj = new Object[1];
		StringBuffer hql = new StringBuffer("from ToaRule t where 1 = 1");
		if (model.getRuleName() != null && !model.getRuleName().equals("")) {
			hql.append(" and t.ruleName like ?");
			obj[0] = "%" + model.getRuleName() + "%";
		}else{
			hql.append(" and 1=?");
			obj[0]=1;
		}
		
		String userId = userService.getCurrentUser().getUserId();
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
		if(!userService.isSystemDataManager(userId)){					//如果是非分级授权管理员
			if(userService.isViewChildOrganizationEnabeld()){			//是否允许看到下级机构
				if(org!=null){
					hql.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
				}
			}else {
				if(org!=null){
					hql.append(" and t.orgId = '").append(org.getOrgId()).append("'");
				}
			}
		}
		page = ruleDao.find(page, hql.toString(), obj);

		return page;
	}
	
	/* 
	* @see com.strongit.oa.autoencoder.IRuleService#getRuleListByUserId(java.lang.String)
	* @method getRuleListByUserId
	* @author 申仪玲
	* @created 2011-10-26 上午11:36:08
	* @description 根据当前用户所在机构取得规则列表(List)
	* @return
	*/
	public List<ToaRule> getRuleListByUserId(String userId)
			throws ServiceException, SystemException {
		
		StringBuffer hql = new StringBuffer("from ToaRule t where 1 = 1");
		
		TUumsBaseOrg org=userService.getSupOrgByUserIdByHa(userId);
		if(!userService.isSystemDataManager(userId)){					//如果是非分级授权管理员
			if(userService.isViewChildOrganizationEnabeld()){			//是否允许看到下级机构
				if(org!=null){
					hql.append(" and t.orgCode like '").append(org.getSupOrgCode()).append("%'");
				}
			}else {
				if(org!=null){
					hql.append(" and t.orgId = '").append(org.getOrgId()).append("'");
				}
			}
		}
		
		List list= ruleDao.find(hql.toString(), null);

		return list;
	}
	
	/**
	 * @author:luosy
	 * @description: 根据规则的ruleIds取得规则列表(List)
	 * @date : 2011-5-27
	 * @modifyer:
	 * @description:
	 * @param ruleIds
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<ToaRule> getRuleListByRuleIds(String ruleIds)
		throws ServiceException, SystemException {

		StringBuffer hql = new StringBuffer("from ToaRule t where ");
		if(ruleIds!=null&&!"".equals(ruleIds)){
			hql.append("  t.id in (");
			String[] ruleId = ruleIds.split(",");
			for(int i=0;i<ruleId.length;i++){
				hql.append("'").append(ruleId[i]).append("',");
			}
			hql.delete(hql.length()-1, hql.length());// = hql.substring(0, hql.length()-1);
			hql.append(")");
		}else{
			hql.append(" 1<>1");
		}
		
		List list= ruleDao.find(hql.toString(), null);

		return list;
	}
}
