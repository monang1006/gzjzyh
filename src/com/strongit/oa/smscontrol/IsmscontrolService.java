/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-9
 * Autour: luosy
 * Version: V1.0
 * Description：用户短信权限
 */
package com.strongit.oa.smscontrol;

public interface IsmscontrolService {

	/**
	 * author:luosy
	 * description:判断是否具有短信发送权限
	 * modifyer:
	 * description:
	 * @param userId   用户ID
	 * @return true/false
	 */
	public boolean hasSendRight(String userId);
}
