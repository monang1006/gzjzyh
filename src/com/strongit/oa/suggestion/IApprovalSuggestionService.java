package com.strongit.oa.suggestion;

import java.util.Date;
import java.util.List;

import com.strongit.oa.bo.ToaApprovalSuggestion;
import com.strongit.oa.util.OALogInfo;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IApprovalSuggestionService {

	/**
	 * 根据Model对象 保存审批意见
	 * @author zhengzb
	 * @desc 
	 * 2010-11-9 下午08:58:23 
	 * @param approvalSuggestion
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveSuggestion(ToaApprovalSuggestion approvalSuggestion ,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	
	/**
	 * 保存审批意见
	 * @author zhengzb
	 * @desc 
	 * 2010-11-9 下午09:01:17 
	 * @param userId				用户ID
	 * @param content				审批意见内容
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void saveSuggestion(String userId,String content ,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据用户ID，获取当前用户所有审批意见
	 * @author zhengzb
	 * @desc 
	 * 2010-11-9 下午09:04:37 
	 * @param userId
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public List<ToaApprovalSuggestion> getAppSuggestionListByUserId(String userId,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据ID删除审批意见
	 * @author zhengzb
	 * @desc 
	 * 2010-11-9 下午09:05:39 
	 * @param suggestionCode
	 * @param loginfos
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public void delete(String suggestionCode  ,OALogInfo ... loginfos)throws SystemException,ServiceException;
	
	/**
	 * 根据ID，获取审批意见对象
	 * @author zhengzb
	 * @desc 
	 * 2010-11-10 上午09:09:09 
	 * @param suggestionCode
	 * @param loginfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public ToaApprovalSuggestion getModelById(String suggestionCode,OALogInfo ... loginfos)throws SystemException,ServiceException;
	

	/**
	 * 查询分页
	 * @author zhengzb
	 * @desc 
	 * 2010-11-10 上午10:17:13 
	 * @param page					分页
	 * @param userId				当前用户ID
	 * @param model					审批意见对象
	 * @param startDate				开始时间
	 * @param endDate				结束时间
	 * @param model
	 * @param logInfos
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public Page<ToaApprovalSuggestion> getAppSuggestionPage(Page<ToaApprovalSuggestion> page,String userId,Date startDate,Date endDate, ToaApprovalSuggestion model,OALogInfo ...logInfos)throws SystemException,ServiceException;
	
	/**
	 * 判断当前用户，所添加的审批意见是否存在
	 * @author zhengzb
	 * @desc 
	 * 2010-11-10 下午02:06:12 
	 * @param userId				当前用户ID
	 * @param content              审批意见内容
	 * * @param suggestionCode              主键ID
	 * @return
	 * @throws SystemException
	 * @throws ServiceException
	 */
	public int IsHasSuggestion(String userId,String content ,String suggestionCode)throws SystemException,ServiceException;
	
}
