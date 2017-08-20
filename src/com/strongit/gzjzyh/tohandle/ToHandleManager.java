package com.strongit.gzjzyh.tohandle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongmvc.exception.SystemException;

@Service
@Transactional(readOnly = true)
public class ToHandleManager implements IToHandleManager {

	@Override
	@Transactional(readOnly = false)
	public void createToHandleMsg(Object obj, String operation)
			throws SystemException {
		// TODO Auto-generated method stub

	}

}
