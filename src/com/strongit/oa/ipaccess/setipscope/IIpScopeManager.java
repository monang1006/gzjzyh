/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-30
 * Autour: pengxq
 * Version: V1.0
 * Description：访问ip控制接口
 */
package com.strongit.oa.ipaccess.setipscope;

import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * ����ip���ƽӿ�
 * 
 * @author pengxq
 * @version 1.0
 */
public interface IIpScopeManager {

	/**
	 * @author：pengxq
	 * @time：2009-1-5下午03:25:44
	 * @desc: 根据ip地址和用户判断该用户是否有登入该系统的权限
	 * @param String
	 *            ip ip地址
	 * @param String
	 *            userid 用户id
	 * @return
	 */
	public boolean isRestrOrNot(String ip, String userid)
			throws SystemException, ServiceException;
}
