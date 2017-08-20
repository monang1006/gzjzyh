package com.strongit.gzjzyh.tosync;

import com.strongmvc.exception.SystemException;

public interface IToSyncManager {
	
	public void createToSyncMsg(Object obj, String operation) throws SystemException;

}
