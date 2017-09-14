package com.strongit.gzjzyh.bankaccount;

import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.SystemException;

public interface IBankAccountManager {
	
	/**
	 * 保存银行账号信息
	 * 
	 * @param user -银行账号实体类
	 * @throws SystemException
	 */
	public void save(TUumsBaseUser user) throws SystemException;
	
	/**
	 * 删除银行账号信息
	 * 
	 * @param userIds -银行账号Id，多个以,隔开
	 * @throws SystemException
	 */
	public void delete(String userIds) throws SystemException;
	
	/**
	 * 新增银行账号信息而不进行同步
	 * 
	 * @param model -银行账号实体类
	 * @throws SystemException
	 */
	public void insertWithoutSync(TUumsBaseUser model) throws SystemException;
	
	/**
	 * 更新银行账号信息而不同步
	 * 
	 * @param model -银行账号实体类
	 * @throws SystemException
	 */
	public void updateWithoutSync(TUumsBaseUser model) throws SystemException;
	
	/**
	 * 删除银行账号信息而不同步
	 * 
	 * @param userId -银行账号Id
	 * @throws SystemException
	 */
	public void deleteWithoutSync(String userId) throws SystemException;
	
}
