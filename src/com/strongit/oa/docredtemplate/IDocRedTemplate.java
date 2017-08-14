/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-27
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板套红对外接口
 */
package com.strongit.oa.docredtemplate;

import java.util.List;

public interface IDocRedTemplate {
	/**
	 * author:lanlc
	 * description:获取所有的公文套红
	 * modifyer:
	 * @return
	 */
	public List getAllDocred();
	
	/**
	 * author:lanlc
	 * description:获取所有的公文套红类别
	 * modifyer:
	 * @return
	 */
	public List getAllDocredType();
	
	/**
	 * author:lanlc
	 * description:获取公文套红类别下的公文套红项
	 * modifyer:
	 * @param redtempGroupId
	 * @return
	 */
	public List getTypeByItem(String redtempGroupId);
}
