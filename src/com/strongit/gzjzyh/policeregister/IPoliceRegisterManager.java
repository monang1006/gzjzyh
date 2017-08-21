package com.strongit.gzjzyh.policeregister;

import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;

public interface IPoliceRegisterManager {
	
	public TGzjzyhUserExtension getUserExtensionById(String ueId) throws SystemException;
	
	public void save(TGzjzyhUserExtension userExtension) throws SystemException;
	
}
