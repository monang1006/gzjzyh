/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理短信配置manager类
 */
package com.strongit.oa.sms;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.ToaSmsCommConf;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.PropertiesUtil;
import com.strongmvc.exception.AjaxException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * 处理短信配置类
 * @Create Date: 2009-2-13
 * @author luosy
 * @version 1.0
 */
@Service
@Transactional
public class SmsConfManager {
	private GenericDAOHibernate<ToaSmsCommConf, java.lang.String> smsConfDao;

	private IsmsEngineService smsEngine;

	@Autowired
	public void setSmsEngine(IsmsEngineService smsEngine) {
		this.smsEngine = smsEngine;
	}
	
	@Autowired
	public void setSessionFactory(org.hibernate.SessionFactory session) {
		smsConfDao = new GenericDAOHibernate<ToaSmsCommConf, java.lang.String>(
				session, ToaSmsCommConf.class);
	}

	public SmsConfManager() {

	}

	/**
	 * author:luosy
	 * description: 修改保存短信猫的配置参数
	 * modifyer:
	 * description:
	 * @param toaSmsConf
	 */
	@Transactional
	public void saveConf(ToaSmsCommConf toaSmsConf) throws SystemException,ServiceException{
		try{
			smsConfDao.save(toaSmsConf);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.save_error,               
					new Object[] {"短信猫配置"});
		}
	}

	/**
	 * author:luosy
	 * description: 扫描端口
	 * modifyer:
	 * description:
	 * @param toaSmsConf
	 * @return 返回（端口号,传输比率）或 (ERROR)
	 */
	public String testComm(ToaSmsCommConf toaSmsConf) throws SystemException,ServiceException,AjaxException{
		try{
			String response = smsEngine.CommTest();
			return response;
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"扫描短信猫配置"});
		}

	}

	/**
	 * author:luosy
	 * description: 获取短信猫配置信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public ToaSmsCommConf getSmsConf() throws SystemException,ServiceException{
		try{
			List smsConf = smsConfDao.findAll();
			if (smsConf.size() > 0) {
				return (ToaSmsCommConf) smsConf.get(0);
			} else {
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫配置"});
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取短信猫配置信息
	 * modifyer:
	 * description:
	 * @return
	 */
	public ToaSmsCommConf getSmsConfBySimNum(String simNum) throws SystemException,ServiceException{
		try{
			List<ToaSmsCommConf> list = smsConfDao.findByProperty("smscomSimnum", simNum);
			if (list.size() > 0) {
				return (ToaSmsCommConf) list.get(0);
			} else {
				return null;
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫配置"});
		}
	}
	/**
	 * author:luosy
	 * description: 获取短信猫配置列表
	 * modifyer:
	 * description:
	 * @return
	 */
	public List<ToaSmsCommConf> getSmsConfList() throws SystemException,ServiceException{
		try{
			return smsConfDao.findAll();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"短信猫配置"});
		}
	}

	
	/**
	 * author:luosy
	 * description:得到短信轮循时间 间隔
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getSmsSystemRate()throws SystemException,ServiceException{
		try {
			return PropertiesUtil.getProperty("sms.intermission.bySecond");
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"得到短信轮循时间 间隔 "});
		} catch (Exception e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"得到短信轮循时间 间隔 "});
		}
	}
	
	/**
	 * author:luosy
	 * description:保存 短信发送时间间隔设置
	 * modifyer:
	 * description:
	 * @param rate
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveSmsSystemRate(String rate)throws SystemException,ServiceException{
		try {
			Properties pro= PropertiesLoaderUtils.loadProperties(new ClassPathResource("im.properties"));	
			pro.setProperty("sms.intermission.bySecond", rate);
			PropertiesUtil.saveProp(pro);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存轮循时间间隔 "});
		}catch(Exception e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"保存轮循时间间隔 "});
		}
	}
}
