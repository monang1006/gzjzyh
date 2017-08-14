package com.strongit.oa.viewmodel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaForamula;
import com.strongit.oa.bo.ToaForamulaRelation;
import com.strongit.oa.bo.ToaPagemodel;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.PathUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * <p>Title: ViewModelManager.java</p>
 * <p>Description: </p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 2010-06-03
 * @version  1.0
 */
@Service
@Transactional
public class ViewModelManager {
	
	private static final Logger log = Logger.getLogger(ViewModelManager.class);
	
	private GenericDAOHibernate<ToaForamula, String> viewModelDao;
	
	@Autowired private ViewModelPageManager viewModelPageManager;
	
	@Autowired private IModelPrivalService  privalService;
    private GenericDAOHibernate<ToaForamulaRelation, String>relationDao;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		viewModelDao = new GenericDAOHibernate<ToaForamula, String>(sessionFactory,ToaForamula.class);
		relationDao=new GenericDAOHibernate<ToaForamulaRelation, String>(sessionFactory,ToaForamulaRelation.class);
	}
	
	/**
	 * @author:于宏洲
	 * @des   :获得列表页面数据
	 * @param page
	 * @return
	 * @date  :2010-06-03
	 */
	@Transactional(readOnly=true)
	public Page<ToaForamula> getPageList(Page<ToaForamula> page){
		
		StringBuffer str = new StringBuffer("from ToaForamula t order by t.foramulaDate desc");
		
		page = viewModelDao.find(page, str.toString(), null);
		
		return page;
		
	}
	/**
	 * 根据视图模块ID删除视图权限信息
	 * @author xush
	 * @desc 
	 * 2011-1-20 下午02:01:58 
	 * @param foramulaId
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByForamulaId (String foramulaId) throws SystemException,ServiceException{
		try {
			
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where t.foramulaId= ?");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString(), foramulaId);
			if(list!=null&&list.size()>0){
				relationDao.delete(list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据门户模块ID删除门户权限信息！"}); 
		}
	}
	
	/**
	 * 根据用户名删除视图权限信息
	 * @desc 
	 * 2014年2月23日11:42:14
	 * @param userName
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void deleteByUserName (String userName) throws SystemException,ServiceException{
		try {
			
			StringBuffer sql = new StringBuffer("");
			sql.append("from ToaForamulaRelation  t  where t.privilsortName= ?");
			List<ToaForamulaRelation> list=relationDao.find(sql.toString(), userName);
			if(list!=null&&list.size()>0){
				relationDao.delete(list);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"根据用户名删除门户权限信息！"}); 
		}
	}
	public boolean cancleDefault(){
		try{
			ToaForamula temp = this.getDefaultToaForamula();
			temp.setForamulaDefault("0");
			viewModelDao.save(temp);
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * @author:于宏洲
	 * @des   :获取默认类型的视图模型
	 * @return
	 * @date  :2010-06-18
	 */
	public ToaForamula getDefaultToaForamula(){
		StringBuffer str = new StringBuffer("from ToaForamula t where t.foramulaDefault = '1'");
		List<ToaForamula> list = viewModelDao.find(str.toString());
		if(list == null){
			return null;
		}else{
			if(list.size()==1){
				return list.get(0);
			}else{
				return null;
			}
		}
		
	}
	
	/**
	 * @author:于宏洲
	 * @des   :对模型对象进行存储
	 * @param obj
	 * @return
	 * @date  :2010-06-03
	 */
	public boolean saveObj(ToaForamula obj){
		
		try{
			if("1".equals(obj.getForamulaDefault())){				//如果当前修改的对象是默认的时候
				if(cancleDefault()){								//将原有默认值进行调整
					viewModelDao.getSession().merge(obj);			//保存当前的值
				}else{
					return false;
				}
			}else{
				viewModelDao.save(obj);
			}
			
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	/**
	 * @author:于宏洲
	 * @des   :视图模型删除方法
	 * @param id
	 * @return
	 * @date  :2010-06-18
	 */
	public boolean deleteByIds(String id){
		String ids[] = id.split(",");
		try{
			for(String delId:ids){
				ToaForamula temp = this.getObjById(delId);
				deleteByForamulaId(delId);
				viewModelDao.delete(temp);
			}
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}
	
	@Transactional(readOnly=true)
	public ToaForamula getObjById(String id){
		return viewModelDao.get(id);
	}
	
	@Transactional(readOnly=true)
	public boolean chargeDefault(String id){
		
		String ids[] = id.split(",");
		
		for(String myid:ids){
			ToaForamula temp = this.getObjById(myid);
			if("1".equals(temp.getForamulaDefault())){
				return true;
			}
		}
		
		return false;
		
	}
	
	public String getDefaultModel(String userId){
		ToaForamula defaultObj = privalService.getForamulaIdByUserId(userId);
		List<ToaPagemodel> list = viewModelPageManager.getModelRootPage(defaultObj.getForamulaId());
		if(list.size()==0){
			return null;
		}else{
			return list.get(0).getPagemodelName();
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 17, 2011 1:55:49 PM
	 */
	public void createRootPath(ToaForamula model,String menu,String top,String work){
		List<ToaPagemodel> list = viewModelPageManager.getPageModelList(model.getForamulaId());
		if(list.size()!=0){
			String windowMenu = getJsFrame(list,menu);
			String windowTop = getJsFrame(list,top);
			String windowWork = getJsFrame(list,work);
			String path=PathUtil.getRootPath()+"WEB-INF/ftls";
			String jspPath = PathUtil.getRootPath()+"common/include";
			Configuration cfg = new Configuration();
			cfg.setDefaultEncoding("utf-8");
			try {
				cfg.setDirectoryForTemplateLoading(new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error("freemarker模板路径错误：");
			}
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			try{
				Template template = cfg.getTemplate("newrootPath.ftl");
				template.setEncoding("utf-8");
				Writer out = null;
				out = new OutputStreamWriter(new FileOutputStream(jspPath+"/"+model.getCreateTime()+".jsp"),"utf-8");   
				try{
					Map<String,String> map = new HashMap<String,String>();
					map.put("windowMenu", windowMenu);
					map.put("windowTop", windowTop);
					map.put("windowWork", windowWork);
					template.process(map, out);
				}catch(Exception e){
					e.printStackTrace();
					System.out.println(e);
				}
				out.flush();
				out.close();
			}catch(Exception e){
				log.error("freemarker生成页面失败，应该生成页面的名称为:"+model.getCreateTime()+".jsp");
			}
		}
	}
	
	private String getJsFrame(List<ToaPagemodel> list,String type){
		String jsFrame="";
		ToaPagemodel temp = null;
		for(int i=0;i<list.size();i++){
			if(type.equals(list.get(i).getPagemodelPagetype())){
				temp = list.get(i);
				break;
			}
		}
		if(temp == null){
			return null;
		}else{
			jsFrame = jsFrameString(list,temp);
			return jsFrame == null?"":jsFrame;
		}
	}
	
	private String jsFrameString(List<ToaPagemodel> list,ToaPagemodel obj){
		String jsFrame=obj.getPagemodelFramename();
		ToaPagemodel temp = obj;
		while(true){
			temp = getModelById(list,temp.getPagemodelParentid());
			if(temp!=null){
				if("0".equals(temp.getPagemodelParentid())){
					jsFrame = "window.top."+jsFrame;
					return jsFrame;
				}else{
					jsFrame = temp.getPagemodelFramename()+"."+jsFrame;
				}
			}else{
				return null;
			}
		}
	}
	
	private ToaPagemodel getModelById(List<ToaPagemodel> list,String id){
		for(int i=0;i<list.size();i++){
			if(id.equals(list.get(i).getPagemodelId())){
				return list.get(i);
			}
		}
		return null;
	}
	
	/*
	 * 
	 * Description:保存
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 17, 2011 2:21:04 PM
	 */
	public boolean saveForamula(ToaForamula obj){
		try{
			viewModelDao.save(obj);
			return true;
		}catch(Exception e){
			log.error(e);
			return false;
		}
	}

}
