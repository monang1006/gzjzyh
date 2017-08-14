/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：短信猫引擎接口
 */
package com.strongit.oa.sms;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.strongit.oa.bo.ToaSms;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IsmsEngineService {
	
	/**
	 * author:luosy
	 * description: 扫描端口
	 * modifyer:
	 * description:
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws ServiceException 
	 * @throws SystemException 
	 */
	public String CommTest() throws SystemException, ServiceException;
	
	/**
	 * author:luosy
	 * description: 发送单条短信
	 * modifyer:
	 * description:
	 * @param num 短信接受号码
	 * @param content	短信内容
	 * @return 发送状态
	 * @throws Exception 
	 */
	public String sendSingleSms(ToaSms sms) throws Exception,ServiceException;
	
	/**
	 * author:luosy
	 * description:读取短信
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public void readSIMsms()throws Exception;
	
	/**
	 * author:luosy
	 * description:删除短信
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public void delSIMsms()throws Exception;
	
	/**
	 * author:luosy
	 * description: 发送单条短信 根据短信猫配置的手机号码
	 * modifyer:
	 * description:
	 * @param smsNum 接受号码
	 * @param smsText 短信内容
	 * @param simNum	短信猫配置的手机号码
	 * @return 发送状态
	 * @throws Exception 
	 */
	public String sendSingleSmsBySimNum(String smsNum, String smsText,String simNum) throws Exception,ServiceException;
	

}
