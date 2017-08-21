package com.strongit.gzjzyh.tohandle;

import com.strongmvc.exception.SystemException;

public interface IToHandleManager {
	
	public void createToHandleMsg(Object obj, String operation) throws SystemException;

}
