/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2012-12-18
 * Autour: luosy
 * Version: V1.0
 * Description： 
 */

package com.strongit.oa.smsplatform;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author 		 于宏洲
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年4月8日
 * @version      1.0
 * @comment      短信平台Manager
 */

@Service
@Transactional
public class SmsPlatformManager {
	private GenericDAOHibernate<ToaBussinessModulePara,String> bussinessModulParaDao;
	
	private Log logger = LogFactory.getLog(SmsPlatformManager.class);
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		bussinessModulParaDao = new GenericDAOHibernate<ToaBussinessModulePara,String>(sessionFactory,ToaBussinessModulePara.class);
	}
	
	@Transactional(readOnly=true)
	public List<ToaBussinessModulePara> getAllObj(){
		//return bussinessModulParaDao.findAll();
		return bussinessModulParaDao.find("from ToaBussinessModulePara as a order by a.bussinessModuleCode asc");
	}
	
	/**
	 * @author：于宏洲
	 * @time：Apr 8, 2009 20:30:56 PM
	 * @desc: 保存模块对象
	 * @param obj
	 * @return
	 */
	public boolean saveObj(ToaBussinessModulePara obj){
		try{
			bussinessModulParaDao.save(obj);
			return true;
		}catch(ServiceException e){
			logger.error("模块存储出现问题");
			return false;
		}
	}
	
	public List<ToaBussinessModulePara> getBo(String id){
		try{
			return bussinessModulParaDao.find("from ToaBussinessModulePara as a where a.bussinessModuleId=? for update",id);
		}catch(Exception e){
			logger.error(e);
			return null;
		}
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 10, 200911:32:14 AM
	 * @desc: 根据模块CODE获取模块对象
	 * @param code
	 * @return ToaBussinessModulePara
	 */
	@Transactional(readOnly=true)
	public ToaBussinessModulePara getObjByCode(String code){
		List<ToaBussinessModulePara> list=bussinessModulParaDao.find("from ToaBussinessModulePara as a where a.bussinessModuleCode=?", code);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * @author：于宏洲
	 * @time：Apr 9, 20098:59:19 AM
	 * @desc: 获得当前用户的最大Code值
	 * @return String
	 */
	@Transactional(readOnly=true)
	public String getMaxCode(){
		List list=bussinessModulParaDao.find("select max(s.bussinessModuleCode) from  ToaBussinessModulePara as s", null);
		return (String)list.get(0);
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 10, 200912:02:14 PM
	 * @desc: 获得 自增长位 的编码
	 * @param code
	 * @return String
	 */
	public String getFullCode(String code){
		ToaBussinessModulePara obj=getObjByCode(code);
		String nowMsgCode=String.valueOf(obj.getIncreaseCode());
		int length=Integer.parseInt(obj.getIncreaseLength());
		if(String.valueOf(Long.parseLong(nowMsgCode)+1).length()>length){
			return "toolong";
		}else{
			return codeFactroy(nowMsgCode,length);
		}
	}
	
	/**
	 * @author：于宏洲
	 * @time：Apr 10, 200911:53:19 AM
	 * @desc: 当短信发送完毕后更新当前的msgcode值
	 * @param code 模块编码
	 * @return String
	 */
	public String updateMsgCode(String code){
		ToaBussinessModulePara obj=getObjByCode(code);
		int length=Integer.parseInt(obj.getIncreaseLength());
		Long nowMsgCode=obj.getIncreaseCode();
		if((String.valueOf(nowMsgCode+1)).length()>length){
			return "long";
		}else{
			obj.setIncreaseCode(nowMsgCode+1);
			try{
				bussinessModulParaDao.save(obj);
				return "true";
			}catch(Exception e){
				return "false";
			}
		}
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 9, 20092:53:26 PM
	 * @desc: 根据ID获得对象
	 * @param id
	 * @return ToaBussinessModulePara
	 */
	@Transactional(readOnly=true)
	public ToaBussinessModulePara getObjById(String id){
		return bussinessModulParaDao.get(id);
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 9, 20092:53:49 PM
	 * @desc: 根据所传过来的字符串来修改相应的模块的开启关闭
	 * @param id
	 * @param state
	 * @return boolean
	 */
	public boolean changeState(String id,String state){
		if(id.indexOf(",")!=-1){
			String[] ids=id.split(",");
			try{
				for(int i=0;i<ids.length;i++){
					ToaBussinessModulePara obj=getObjById(ids[i]);
					obj.setIsEnable(state);
					saveObj(obj);
				}
				return true;
			}catch(Exception e){
				return false;
			}
		}else{
			ToaBussinessModulePara obj=getObjById(id);
			obj.setIsEnable(state);
			if(saveObj(obj)){
				return true;
			}else{
				return false;
			}
		}
	}
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 9, 20094:28:31 PM
	 * @desc: 删除
	 * @param id前台传入ID（可能带有逗号）
	 * @return boolean
	 */
	public boolean deleteObj(String id){
		if(id.indexOf(",")!=-1){
			String[] ids=id.split(",");
			try{
				for(int i=0;i<ids.length;i++){
					ToaBussinessModulePara obj=getObjById(id);
					delete(obj);
				}
				return true;
			}catch(Exception e){
				return false;
			}
		}else{
			ToaBussinessModulePara obj=getObjById(id);
			if(delete(obj)){
				return true;
			}else{
				return false;
			}
		}
	}
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 9, 20094:29:23 PM
	 * @desc: 根据对象进行删除
	 * @param obj
	 * @return boolean
	 */
	public boolean delete(ToaBussinessModulePara obj){
		try{
			bussinessModulParaDao.delete(obj);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 
	 * @author：于宏洲
	 * @time：Apr 10, 200912:04:56 PM
	 * @desc: 生成自增长位编码不够位数的则用"0"补齐
	 * @param code
	 * @param length
	 * @return String
	 */
	private String codeFactroy(String code,int length){
		if(code==null){
			return null;
		}else{
			if(code.length()>length){
				return null;
			}else{
				int k=length-code.length();
				for(int i=0;i<k;i++){
					code="0"+code;
				}
				return code;
			}
		}
	}
	
	/**
	 * author:luosy
	 * description:  该模块是否开启
	 * modifyer:
	 * description:
	 * @param code
	 * @return
	 */
	public String isModuleParaOpen(String code){
		List<ToaBussinessModulePara> list=bussinessModulParaDao.find("from ToaBussinessModulePara as a where a.bussinessModuleCode=?", code);
		if(list.size()>0){
			return list.get(0).getIsEnable();
		}else{
			return null;
		}
	}
	
}
