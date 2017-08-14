package com.strongit.oa.viewmodel;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaPagemodel;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * <p>Title: ViewModelPageManager.java</p>
 * <p>Description: 页面模型Manager</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-06-06
 * @version  1.0
 */
@Service
@Transactional
public class ViewModelPageManager {
	
	private static final Logger log = Logger.getLogger(ViewModelPageManager.class);
	
	private GenericDAOHibernate<ToaPagemodel, String> viewModelPageDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		viewModelPageDao = new GenericDAOHibernate<ToaPagemodel, String>(sessionFactory,ToaPagemodel.class);
	}
	
	/**
	 * @author:于宏洲
	 * @des   :根据ID查询页面模型对象
	 * @param id
	 * @return
	 * @date  :2010-06-06
	 */
	@Transactional(readOnly=true)
	public ToaPagemodel getObjById(String id){
		return viewModelPageDao.get(id);
	}
	
	/**
	 * @author:于宏洲
	 * @des   :判断同一个级别中的
	 * @param parentId
	 * @param id
	 * @return
	 * @date  :2010-06-17
	 */
	public boolean chargeValSame(String modelId,String parentId,String valName,String id){
		List<ToaPagemodel>  list =null;
		if(id!=null&&!"".equals(id)&&!"null".equals(id)){
			String[] obj = {modelId,parentId,valName,id};
			list = viewModelPageDao.find("from ToaPagemodel t where t.toaForamula.foramulaId=? and t.pagemodelParentid=? and t.pagemodelValname=? and t.pagemodelId <> ?", obj);
		}else{
			String[] obj = {modelId,parentId,valName};
			list = viewModelPageDao.find("from ToaPagemodel t where t.toaForamula.foramulaId=? and t.pagemodelParentid=? and t.pagemodelValname=?", obj);
		}
		
		if(list.size()==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * @author:于宏洲
	 * @des   :根据视图模型ID获取视图所有页面模型
	 * @param modelId
	 * @return
	 * @date  :2010-06-18
	 */
	public List<ToaPagemodel> getPageModelList(String modelId){
		List<ToaPagemodel> list = null;
		list = viewModelPageDao.find("from ToaPagemodel t where t.toaForamula.foramulaId=? ", modelId);
		return list;
	}
	
	/**
	 * @author:于宏洲
	 * @des   :进行模板对象保存
	 * @param obj
	 * @return
	 * @date  :2010-06-07
	 */
	public boolean saveObj(ToaPagemodel obj){
		try{
			viewModelPageDao.save(obj);
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	public boolean delObj(ToaPagemodel obj){
		try{
			viewModelPageDao.delete(obj);
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	public boolean delObj(String id){
		try{
			viewModelPageDao.delete(id);
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	@Transactional(readOnly=true)
	public List<ToaPagemodel> getModelRootPage(String modelId){
		String obj[]={"0",modelId};
		return viewModelPageDao.find("from ToaPagemodel t where t.pagemodelParentid=? and t.toaForamula.foramulaId=?",obj);
	}
	
	public List<ToaPagemodel> getPageListByFather(String parentId,String modelId){
		String obj[] = {parentId,modelId};
		return viewModelPageDao.find("from ToaPagemodel t where t.pagemodelParentid=? and t.toaForamula.foramulaId=?",obj);
	}
	
	@Transactional(readOnly=true)
	public boolean chargeLeaf(String parentId,String modelId){
		String obj[]={parentId,modelId};
		List list = viewModelPageDao.find("from ToaPagemodel t where t.pagemodelParentid=? and t.toaForamula.foramulaId=?",obj);
		if(list==null||list.size()==0){
			return true;
		}else{
			return false;
		}
	}

}
