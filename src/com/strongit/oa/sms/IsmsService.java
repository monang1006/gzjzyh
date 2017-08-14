/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：手机短信模块对外接口
 */
package com.strongit.oa.sms;

import java.util.List;

import com.strongit.oa.bo.ToaSms;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IsmsService {
	
	/**
	 * author:luosy
	 * description: 提供给工作流的发送接口
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @return 发送结果
	 */
	public void sendSms(String flag,List recvUser,String smsContent);
	
	/**
	 * author:luosy
	 * description: 提供给各模块的发送接口
	 * modifyer:
	 * description:
	 * @param flag 发送标识 system/person
	 * @param recvUser 接受者ID list
	 * @param smscontent 短信内容
	 * @param code 模块编码
	 * @return 发送结果
	 */
	public void sendSms(String flag,List recvUser,String smsContent,String code);

	/**
	 * author:luosy
	 * description: 提供给大蚂蚁的发送接口
	 * modifyer:
	 * description:
	 * @param ownerNum 接受人号码
	 * @param content 短信内容
	 * @param sender 发送人
	 * @param code   模块编码
	 * @return 发送结果
	 */
	public void sendSmsByAnt(String sender,String ownerNum,String content,String code);
	
	/**
	 * author:luosy
	 * description:提供给消息模块的发送接口 
	 * modifyer:
	 * description:
	 * @param ownerNum 自定义接收号码
	 * @param userList	接受用户
	 * @param sms 短信对象
	 */
	public void sendSmsByMessage(String ownerNum, String recvUser, ToaSms sms);
	
	/**
	 * author:luosy
	 * description: 提供给只给号码的发送接口 
	 * modifyer:
	 * description:
	 * @param ownerNum 接受人号码
	 * @param content 内容
	 * @param sender 发送人
	 * @param code 模块ID
	 * @return 发送结果
	 */
	public void sendSmsByNum(String ownerNum, String content,String sender,String code )throws SystemException,ServiceException,Exception;

	/**
	 * author:luosy
	 * description:  该模块是否开启
	 * modifyer:
	 * description:
	 * @param code
	 * @return true : 启用；false ：关闭
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isModuleParaOpen(String code)throws SystemException,ServiceException;
	
	/**
	 * author:luosy
	 * description:  短信模块是否可用(供提醒标签使用)
	 * modifyer:
	 * description:
	 * @param code
	 * @return true : 启用；false ：关闭
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public boolean isSMSOpen(String code)throws SystemException,ServiceException;
	
	/**
	 * author:luosy
	 * description: 短信模块 获取手机卡号码
	 * modifyer:
	 * description:
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public String getSmsModelNum()throws SystemException,ServiceException;
}
