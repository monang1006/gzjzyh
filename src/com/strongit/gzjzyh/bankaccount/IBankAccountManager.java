package com.strongit.gzjzyh.bankaccount;

import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;

public interface IBankAccountManager {
	
	public void save(TUumsBaseUser user) throws SystemException;
	
	public void delete(String userIds) throws SystemException;
	
}
