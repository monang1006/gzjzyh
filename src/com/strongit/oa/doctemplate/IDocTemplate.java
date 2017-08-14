/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-26
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板对外接口类
 */
package com.strongit.oa.doctemplate;

import java.util.List;

public interface IDocTemplate {
	
	/**
	 * author:lanlc
	 * description:获取所有的公文模板
	 * modifyer:
	 * @return
	 */
	public List getAllTemplate();
	
	/**
	 * author:lanlc
	 * description:获取所有的公文模板类别
	 * modifyer:
	 * @return
	 */
	public List getAllTypeTemplate();
	
	/**
	 * author:lanlc
	 * description:获取公文模板类别下的公文模板项
	 * modifyer:
	 * @param docgroupId
	 * @return
	 */
	public List getTypeByItem(String docgroupId);
}
