package com.strongit.oa.doctemplate.eform;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.strongit.oa.bo.ToaTemplateEForm;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 电子表单 - 模板管理类
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-11-18 下午02:15:06
 * @version  2.0.7
 * @classpath com.strongit.oa.doctemplate.eform.TemplateEFormManager
 * @comment
 * @email dengzc@strongit.com.cn
 */
@Service
@Transactional
public class TemplateEFormManager {

	GenericDAOHibernate<ToaTemplateEForm, String> dao ;

	/**
	 * Spring SESSION工厂
	 * @author:邓志城
	 * @date:2010-11-18 下午02:14:49
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dao = new GenericDAOHibernate<ToaTemplateEForm, String>(
				sessionFactory, ToaTemplateEForm.class);

	}

	/**
	 * 得到和表单挂接的模板，如有多个取排序号最大的模板
	 * @author:邓志城
	 * @date:2010-11-18 下午05:25:13
	 * @param formId
	 * @return
	 */
	public String findLatestTemplate(String formId) {
		StringBuilder hql = new StringBuilder("SELECT t.templateId FROM ToaTemplateEForm t, ToaDoctemplate tt ");
		hql.append(" WHERE t.templateId=tt.doctemplateId AND t.formId = ? order by tt.docNumber desc ");
		List list = dao.find(hql.toString(), formId);
		if ((list != null) && (!(list.isEmpty()))) {
			StringBuilder templateIds = new StringBuilder("");
			for (int i = 0; i < list.size(); ++i) {
				String ret = (String) list.get(i);
				templateIds.append(ret).append(";");
			}
			if ((templateIds == null) || (templateIds.length() < 32)) {
				return null;
			}
			return templateIds.substring(0, templateIds.length() - 1);
		}
		return null;
	}
	
	/**
	 * 保存关联关系
	 * @author:邓志城
	 * @date:2010-11-18 下午04:34:01
	 * @param templateId
	 * @param formId
	 */
	public void save(String templateId,String formId) {
		//deleteByFormId(formId);
		ToaTemplateEForm bean = new ToaTemplateEForm();
		bean.setFormId(formId);
		bean.setTemplateId(templateId);
		dao.save(bean);
	}

	/**
	 * 删除模板关联的表单
	 * @author:邓志城
	 * @date:2010-11-18 下午04:36:42
	 * @param templateId
	 */
	@SuppressWarnings("unchecked")
	public void delete(String templateId) {
		StringBuilder hql = new StringBuilder("FROM ToaTemplateEForm t ");
		hql.append(" WHERE t.templateId = ?");
		dao.delete(dao.find(hql.toString(), templateId));
	}

	/**
	 * 删除表单管理的模板
	 * @param formId
	 */
	public void deleteByFormId(String formId) {
		StringBuilder hql = new StringBuilder("FROM ToaTemplateEForm t where t.formId = ?");
		dao.delete(dao.find(hql.toString(), formId));
	}
	
	/**
	 * 更新表单与模板关系
	 * @author:邓志城
	 * @date:2010-11-18 下午04:39:20
	 * @param templateId
	 * @param formId
	 */
	public void update(String templateId,String formId) {
		if(formId == null || "".equals(formId)) {//删除所有表单
			delete(templateId);
		}else{
			String[] formIds = formId.split(",");
			delete(templateId);
			for(String id : formIds) {
				save(templateId, id);
			}			
		}
	}
	
	/**
	 * 根据模板id找到相关联的表单
	 * @author:邓志城
	 * @date:2010-11-18 下午04:19:07
	 * @param templateId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List findEForm(String templateId) {
		Assert.notNull(templateId, "参数templateId为空！");
		StringBuilder hql = new StringBuilder("SELECT t.formId FROM ToaTemplateEForm t ");
		hql.append(" WHERE t.templateId = ?");
		return dao.find(hql.toString(), templateId);
	}

	
	public List findTemplatesbyId(String templateId)
	  {
	    Assert.notNull(templateId, "参数templateId为空！");
	    StringBuilder hql = new StringBuilder(" FROM ToaDoctemplate t ");
	    hql.append(" WHERE 1=1 and  t.doctemplateId in (");

	    String[] ids = templateId.split(";");
	    for (String id : ids) {
	      hql.append("'").append(id).append("',");
	    }
	    String hqlstr = hql.substring(0, hql.length() - 1);
	    hqlstr = hqlstr + " ) ";
	    List list = this.dao.find(hqlstr, new Object[0]);
	    return list;
	  }
}
