/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2008-12-27
 * Autour: lanlc
 * Version: V1.0
 * Description：公文套红对外接口实现类
 */
package com.strongit.oa.docredtemplate;

import java.util.List;

import com.strongit.oa.docredtemplate.docreditem.DocRedItemManager;
import com.strongit.oa.docredtemplate.docredtype.DocRedTypeManager;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public class DocRedTemplate implements IDocRedTemplate {
	private DocRedItemManager itemmanager;
	private DocRedTypeManager typemanager;
	
	public DocRedItemManager getItemmanager() {
		return itemmanager;
	}
	public void setItemmanager(DocRedItemManager itemmanager) {
		this.itemmanager = itemmanager;
	}
	public DocRedTypeManager getTypemanager() {
		return typemanager;
	}
	public void setTypemanager(DocRedTypeManager typemanager) {
		this.typemanager = typemanager;
	}
	
	/**
	 * author:lanlc
	 * description:构造方法
	 * modifyer:
	 * @return
	 */
	public DocRedTemplate(){};
	
	/**
	 * author:lanlc
	 * description:获取所有的公文套红
	 * modifyer:
	 * @return
	 */
	public List getAllDocred() throws SystemException,ServiceException{
		try{
			List allDocred = itemmanager.getAllDocred();
			return allDocred;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取套红类别集"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取所有的公文套红类别
	 * modifyer:
	 * @return
	 */
	public List getAllDocredType() throws SystemException,ServiceException{
		try{
			List allDocredType = typemanager.getAllDocredType();
			return allDocredType;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取套红类别集"});
		}
	}
	
	/**
	 * author:lanlc
	 * description:获取公文套红类别下的公文套红项
	 * modifyer:
	 * @param redtempGroupId
	 * @return
	 */
	public List getTypeByItem(String redtempGroupId)throws SystemException,ServiceException{
		try{
			List typeByItem = itemmanager.getTypeByItem(redtempGroupId);
			return typeByItem;
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获取套红类别集"});
		}
	}
	

}
