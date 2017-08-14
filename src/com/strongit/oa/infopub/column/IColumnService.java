package com.strongit.oa.infopub.column;

import java.util.List;

import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-17 上午10:16:09
 * Autour: lanlc
 * Version: V1.0
 * Description：
 */

public interface IColumnService {
	
	/**
	 * author:lanlc
	 * description:获得我有权限的栏目集
	 * modifyer:
	 */
	public List getMyColumn() throws SystemException,ServiceException;
	
	/**
	  * author:lanlc
	  * description:获取指定ID的栏目
	  * modifyer:
	  * @param clumnId  栏目ID
	  * @return
	  */
	public ToaInfopublishColumn getColumn(String clumnId);
}
