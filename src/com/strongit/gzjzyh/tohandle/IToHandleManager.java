package com.strongit.gzjzyh.tohandle;

import com.strongit.gzjzyh.vo.Packet;
import com.strongmvc.exception.SystemException;

public interface IToHandleManager {
	
	public void handleMsg(Packet requestPacket) throws SystemException;

}
