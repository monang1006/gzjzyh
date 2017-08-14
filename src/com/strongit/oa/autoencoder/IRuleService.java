package com.strongit.oa.autoencoder;

import java.util.List;

import javax.transaction.SystemException;

import com.strongit.oa.bo.ToaRule;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;

public interface IRuleService {
	public boolean save(ToaRule rule) throws ServiceException, SystemException;

	/**
	 * 编码列表
	 * 
	 * @param page
	 * @param model
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public Page<ToaRule> getRuleList(Page<ToaRule> page, ToaRule model)
			throws ServiceException, SystemException;

	/**
	 * 删除一个编码
	 * 
	 * @param id
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void deleteRuleById(String id) throws ServiceException, SystemException;

	/**
	 * 取得一个编码
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public ToaRule getRuleById(String id) throws ServiceException, SystemException;

	/**
	 * 更新一个编码
	 * @param rule
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void update(ToaRule rule) throws ServiceException, SystemException;
	
	public List<ToaRule> getAllList() throws ServiceException,SystemException;
	
	/* 
	* @see com.strongit.oa.autoencoder.IRuleService#getRuleListByUserId(java.lang.String)
	* @method getRuleListByUserId
	* @author 申仪玲
	* @created 2011-10-26 上午11:36:08
	* @description 根据当前用户所在机构取得规则列表(List)
	* @return
	*/
	public List<ToaRule> getRuleListByUserId(String userId) throws ServiceException, SystemException ;

	/**
	 * @author:luosy
	 * @description: 根据规则的Ids取得规则列表(List)
	 * @date : 2011-5-27
	 * @modifyer:
	 * @description:
	 * @param ruleIds
	 * @return
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public List<ToaRule> getRuleListByRuleIds(String ruleIds) throws ServiceException, SystemException ;
}
