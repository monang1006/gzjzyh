/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息业务类
 */
package com.strongit.oa.eformJspManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TEFormTemplate;
import com.strongit.oa.bo.TEJsptemplate;
import com.strongit.oa.bo.ToaInfopublishTemplate;
import com.strongit.oa.common.eform.model.EForm;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.formManager.IFormManagerService;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 表单模板管理类
 * @author 		
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Aug 22, 2011
 * @classpath	com.strongit.oa.eformManager.EformManagerManager
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Service
@Transactional
@OALogger
public class EformJspManagerManager {
	
	private GenericDAOHibernate<TEJsptemplate, java.lang.String> eformJspDao;
	
	
	/**
	 * 注入SessionFactory
	 * @param session		会话对象.
	 */
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		eformJspDao = new GenericDAOHibernate<TEJsptemplate, java.lang.String>(
				session, TEJsptemplate.class);
	}
	
	/**
	 * 得到所有的Jsp表单
	 * @param page
	 * @param model
	 * @return
	 */
	public Page<TEJsptemplate> getAllJSPForm (
			Page<TEJsptemplate> page, TEJsptemplate model) {
		StringBuffer queryStr = new StringBuffer("from TEJsptemplate t where 1=1");
		if(model.getName() != null && !"".equals(model.getName())){
			String name=model.getName();
			if(name.indexOf("%")!=-1){
				name=name.replaceAll("%", "/%");
			}
				queryStr.append(" and t.name like '%" + name
						+ "%' ESCAPE '/'");
		}
		queryStr.append(" order by modifyTime desc ");
		return eformJspDao.find(page, queryStr.toString());
	}
	/**
	 * 判断jsp表单是否被流程所引用。
	 * @return
	 * @throws Exception
	 */
	public List  findRef(Long formId) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuilder hql = new StringBuilder("from TwfBaseProcessfile t");
		hql.append(" where t.pfMainformId=? ");
		if (formId != null && !"".equals(formId)) {
			params.add(formId);
		}
		List list = eformJspDao.find(hql.toString(), params.toArray());
		return list;
		
	}

	/**
	 * 保存表单模板对象
	 * @param model
	 */
	public void save(TEJsptemplate model) {
		eformJspDao.save(model);
	}
	
	/**
	 * 删除表单模板对象
	 * @param model
	 */
	public void delete(TEJsptemplate model) {
		eformJspDao.delete(model);
	}
	
	/**
	 * 获取所有表单的id
	 * 
	 * @description
	 * @author 
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 * @createTime Apr 28, 2012 6:47:13 PM
	 */
	public List getAllTEFormJspTemplateId() throws DAOException, ServiceException,
	    SystemException {
        	StringBuilder hql;
        	try {
        	    hql = new StringBuilder("select t.id from TEJsptemplate t");
        	    return eformJspDao.find(hql.toString());
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
	 * 得到所有jsp列表
	 * @date 2014年1月6日10:58:52
	 * @return
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	
	public List getAllJsp() throws DAOException, ServiceException,
    SystemException {
    	StringBuilder hql;
    	try {
    		List list = new ArrayList();
    	    hql = new StringBuilder("select t from TEJsptemplate t");
    		List<TEJsptemplate> jspTemplate =eformJspDao.find(hql.toString());
    	    if (jspTemplate != null){
    			for(TEJsptemplate jsptem : jspTemplate){
    				Object object = new Object[]{jsptem.getId().toString(),jsptem.getName()};
    				list.add(object);
    			}
    		}
    	    return list;
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
	 * 根据表单模板id得到表单模板对象,不返回模板内容
	 * @param id		模板id
	 * @return			模板对象
	 */
	public TEJsptemplate getFormTemplateInfo(String id) {
		StringBuilder hql = new StringBuilder("select t from TEJsptemplate t where t.id = ?");
		List<TEJsptemplate> jspForms = eformJspDao.find(hql.toString(),new Long(id));
		TEJsptemplate jspForm;
		if(!jspForms.isEmpty()){
			jspForm=jspForms.get(0);
		}else{
			jspForm = null;
		}
		return jspForm;
	}
	
	/**
	 * 根据表单模板id得到表单模板对象
	 * @param id
	 * @return
	 */
	public TEJsptemplate get(String id) throws DAOException,ServiceException,SystemException {
		StringBuilder hql;
		try {
			if(id == null || "".equals(id)){
				throw new SystemException("电子表单模板id不能为" + id);
			}
			if(id.indexOf(",") != -1){
				String[] infos = id.split(",");
				id = infos[infos.length-1];
			}
			hql = new StringBuilder("from TEJsptemplate t where t.id = ?");
			List list = eformJspDao.find(hql.toString(),new Object[]{Long.parseLong(id)});
			if(list != null && !list.isEmpty()) {
				return (TEJsptemplate)list.get(0);
			} else {
				throw new SystemException("找不到模板id为" + id + "的电子表单模板");
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
	* @method getByName
	* @created  2014年1月2日10:39:27
	* @description 根据表单模板title得到表单模板对象
	* @return TEFormTemplate 返回类型
	*/
	@SuppressWarnings("unchecked")
	public TEJsptemplate getByName(String name) throws DAOException,ServiceException,SystemException {
		StringBuilder hql;
		try {
			hql = new StringBuilder("from TEJsptemplate t where t.title = ?");
			List<TEJsptemplate> list = eformJspDao.find(hql.toString(),name);
			if(list != null && !list.isEmpty()) {
				return (TEJsptemplate)list.get(0);
			} else {
				throw new SystemException("找不到模板title为" + name + "的电子表单模板");
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
