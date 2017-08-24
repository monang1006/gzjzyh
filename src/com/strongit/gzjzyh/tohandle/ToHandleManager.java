package com.strongit.gzjzyh.tohandle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.gzjzyh.vo.Packet;
import com.strongmvc.exception.SystemException;

@Service
@Transactional(readOnly = true)
public class ToHandleManager implements IToHandleManager {

	@Override
	@Transactional(readOnly = false)
	public void handleMsg(Packet requestPacket) throws SystemException {
		
	}

}
