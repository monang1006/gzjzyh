package com.strongit.oa.infopub.column;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaInfopublishColumn;
import com.strongit.oa.util.MessagesConst;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-2-17 上午10:16:34
 * Autour: lanlc
 * Version: V1.0
 * Description：
 */
@Service
public class ColumnServiceImpl implements IColumnService {
	
	private ColumnManager manager;
	
	/**
	 * author:lanlc
	 * description:获得我有权限的栏目集
	 * modifyer:
	 */
	public List getMyColumn() throws SystemException, ServiceException {
		try{
			return manager.getMyColumn();
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"获得我有权限的栏目集"});
		}
	}
	
	/**
	  * author:lanlc
	  * description:获取指定ID的栏目
	  * modifyer:
	  * @param clumnId  栏目ID
	  * @return
	  */
	public ToaInfopublishColumn getColumn(String clumnId)throws SystemException,ServiceException{
		try{
			return manager.getColumn(clumnId);
		}catch(ServiceException e){
			throw new ServiceException(MessagesConst.find_error,               
					new Object[] {"栏目管理对象"});
		}
	}

	@Autowired
	public void setManager(ColumnManager manager) {
		this.manager = manager;
	}
	
}
