/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：处理消息接口实现类
 */
package com.strongit.oa.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

@Service
public class MessageService implements IMessageService {
	private MessageManager manager;

	@Autowired
	public void setManager(MessageManager manager) {
		this.manager = manager;
	}
	
	/**
	 * author:luosy
	 * description:发送消息外部接口实现
	 * modifyer:
	 * description:
	 * @param flag 发送标识 系统system/人员person
	 * @param receiver 接收者 List
	 * @param title	 消息标题
	 * @param content 消息内容
	 */
	public void sendMsg(String flag,List receiver,String title,String content,String msgType)throws SystemException,ServiceException{
		try{
			if(null!=receiver){
				if(receiver.size()>0){
					content = HtmlUtils.htmlEscape(content);
					manager.sendMsg(flag, receiver, title, content, msgType);
				}
			}
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.send_error,               
					new Object[] {"消息接口实现类"});
		}
	}
}
