package com.strongit.gzjzyh.policeregister;

import java.util.Date;

import com.strongit.gzjzyh.po.TGzjzyhUserExtension;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IPoliceRegisterManager {
	
	public TGzjzyhUserExtension getUserExtensionByUserId(String userId) throws SystemException;
	
	public TGzjzyhUserExtension getUserExtensionById(String ueId) throws SystemException;
	
	public void save(TGzjzyhUserExtension userExtension) throws SystemException;
	
	public Page<TGzjzyhUserExtension> queryApplyPage(
			Page page, String searchLoginName, String searchName,
			Date appStartDate, Date appEndDate) throws SystemException;
	
}
