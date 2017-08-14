/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-26
 * Autour: lanlc
 * Version: V1.0
 * Description：公文模板对外接口实现类
 */
package com.strongit.oa.doctemplate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.doctemplate.doctempItem.DocTempItemManager;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public class DocTemplate implements IDocTemplate {
	private DocTempTypeManager typeManager;
	private DocTempItemManager itemManager;
	
	/**
	 * author:lanlc
	 * description:构造方法
	 * modifyer:
	 * @return
	 */
	public DocTemplate(){}
	
	@Autowired
	public void setItemManager(DocTempItemManager itemManager) {
		this.itemManager = itemManager;
	}
	
	@Autowired
	public void setTypeManager(DocTempTypeManager typeManager) {
		this.typeManager = typeManager;
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文模板
	 * modifyer:
	 * @return
	 */
	public List getAllTemplate() throws SystemException,ServiceException{
		try{
			List allTemplate = itemManager.getAllTemplate();
			return allTemplate;	
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取所有的公文模板"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文模板类别
	 * modifyer:
	 * @return
	 */
	public List getAllTypeTemplate()throws SystemException,ServiceException{
		try{
			List allTypeTemplate = typeManager.getAllTypeTemplate();
			return allTypeTemplate;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取所有的公文模板类别"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取公文模板类别下的公文模板项
	 * modifyer:
	 * @param docgroupId
	 * @return
	 */
	public List getTypeByItem(String docgroupId) throws SystemException,ServiceException{
		try{
			List typeByItemList = itemManager.getTypeByItem(docgroupId);
			return typeByItemList;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取公文模板类别下的公文模板项"});
		}
	}	
}
