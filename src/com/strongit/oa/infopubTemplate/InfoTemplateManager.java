/**
 * 
 */
package com.strongit.oa.infopubTemplate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.bo.ToaInfopublishTemplate;
import com.strongit.oa.bo.ToaMeetingAttach;
import com.strongit.oa.bo.ToaMeetingConference;
import com.strongit.oa.bo.ToaMeetingTopic;
import com.strongit.oa.common.service.BaseManager;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * @description 类描述
 * @className TemplateManager
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2011-11-23 下午07:57:38
 * @version 3.0
 */
@Service
@Transactional
public class InfoTemplateManager extends BaseManager{
	
	private GenericDAOHibernate<ToaInfopublishTemplate, String> templateDao = null;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		templateDao = new GenericDAOHibernate<ToaInfopublishTemplate, String>(
				sessionFactory, ToaInfopublishTemplate.class);

	}

	/**
	* @method getOneTemplate
	* @author 申仪玲
	* @created 2011-11-23 下午08:04:10
	* @description 描述
	* @return ToaInfopublishTemplate 返回类型
	*/
	
	public ToaInfopublishTemplate getOneTemplate(String templateId) {
		
		return this.templateDao.get(templateId);
	}

	/**
	* @method saveTemplate
	* @author 申仪玲
	* @created 2011-11-23 下午08:13:48
	* @description 描述
	* @return void 返回类型
	*/
	
	public void saveTemplate(ToaInfopublishTemplate model) {
		
		templateDao.save(model);
		
	}

	/**
	* @method deleteTemplate
	* @author 申仪玲
	* @created 2011-11-23 下午08:15:40
	* @description 描述
	* @return void 返回类型
	*/
	
	public void deleteTemplate(String templateIds) {
		String[] ids = templateIds.split(",");		
		templateDao.delete(ids);
	
	}

	/**
	* @method getTemPages
	* @author 申仪玲
	* @created 2011-11-24 下午06:15:17
	* @description 描述
	* @return Page<ToaInfopublishTemplate> 返回类型
	*/
	
	public Page<ToaInfopublishTemplate> getTemPages(
			Page<ToaInfopublishTemplate> page) {
		String hql= "from ToaInfopublishTemplate as t where 1 = 1";
		return templateDao.find(page, hql);
	}

	/**
	* @method getTemPages
	* @author 申仪玲
	* @created 2011-11-24 下午07:11:34
	* @description 描述
	* @return Page<ToaInfopublishTemplate> 返回类型
	*/
	
	public Page<ToaInfopublishTemplate> getTemPages (
			Page<ToaInfopublishTemplate> page, ToaInfopublishTemplate model) {
		StringBuffer queryStr = new StringBuffer("from ToaInfopublishTemplate t where 1=1");
		if(model.getTemplateName() != null && !"".equals(model.getTemplateName())){
				queryStr.append(" and templateName like '%" + model.getTemplateName()
						+ "%'");
		}
		if(model.getTemplateDesc() != null && !"".equals(model.getTemplateDesc())){			
			queryStr.append(" and templateDesc like '%" + model.getTemplateDesc()
					+ "%'");
		}
		queryStr.append(" order by templateDesc");
		return templateDao.find(page, queryStr.toString());
	}

	/**
	* @method getTemByName
	* @author 申仪玲
	* @created 2011-11-24 下午09:32:15
	* @description 描述
	* @return ToaInfopublishTemplate 返回类型
	*/
	
	@SuppressWarnings("unchecked")
	public ToaInfopublishTemplate getTemByName(String templateName)throws DAOException,ServiceException,SystemException{
		StringBuilder hql = new StringBuilder("from ToaInfopublishTemplate t where t.templateName = ?");
		try{
			List<ToaInfopublishTemplate> list = templateDao.find(hql.toString(),templateName);
			if(list != null && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	/**
	* @method getTemByDesc
	* @author 申仪玲
	* @created 2011-11-24 下午09:32:21
	* @description 描述
	* @return ToaInfopublishTemplate 返回类型
	*/
	
	@SuppressWarnings("unchecked")
	public ToaInfopublishTemplate getTemByDesc(String templateDesc) throws DAOException,ServiceException,SystemException{
		StringBuilder hql = new StringBuilder("from ToaInfopublishTemplate t where t.templateDesc = ?");
		try{
			List<ToaInfopublishTemplate> list = templateDao.find(hql.toString(),templateDesc);
			if(list != null && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

}
