/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-12
 * Autour: luosy
 * Version: V1.0
 * Description：站内消息接口
 */
package com.strongit.oa.message;

import java.util.List;

public interface IMessageService {

	/**
	 * author:luosy
	 * description:发送消息外部接口实现
	 * modifyer:
	 * description:
	 * @param flag 发送标识 系统system/人员person
	 * @param receiver 接收者 List
	 * @param title	 消息标题
	 * @param content 消息内容
	 * @param msgType 消息类型
	 */
	public void sendMsg(String flag,List<String> receiver,String title,String content, String msgType);
}
