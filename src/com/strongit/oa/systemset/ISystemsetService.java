package com.strongit.oa.systemset;

import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface ISystemsetService {
	
	/**
	 *  公文打印控制调用接口
	 *  @return  {0:'启用总份数打印控制' , 1:'启用每人份数打印控制'}
	 */
	public String gwControl()throws ServiceException, DAOException, SystemException;

}
