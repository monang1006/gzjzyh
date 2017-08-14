package com.strongit.xxbs.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.strongit.oa.util.OALogInfo;
import com.strongit.xxbs.dto.MailInfo;
import com.strongit.xxbs.dto.ScoreRankDto;
import com.strongit.xxbs.entity.TInfoBaseComment;
import com.strongit.xxbs.entity.TInfoBasePublish;
import com.strongit.xxbs.entity.TInfoBasePublishUser;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

public interface IPublishService
{
	public TInfoBasePublish getPublish(String pubId)
			throws ServiceException,SystemException, DAOException;

	public List<TInfoBasePublish> getPublishs()
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBasePublish> getPublishsByuser(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus,String[]pubId)
			throws ServiceException,SystemException, DAOException;
		
	public Page<TInfoBasePublish> findPublishs(Page<TInfoBasePublish> page,
			String orgId, Map<String, String> search, Boolean isSubmitted)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBasePublish> findPublishs1(Page<TInfoBasePublish> page,
			String orgId, Map<String, String> search, Boolean isSubmitted)
			throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> findPublishsByIssId(String issId)
			throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBasePublish> findPublishsIsUread(Page<TInfoBasePublish> page,int curpage,int unitpage)
	throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> findPublishsByIssId(String issId, String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public Integer numUsed(String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public Integer numSubmitted(String orgId)
			throws ServiceException,SystemException, DAOException;
	
	public Boolean isExistTitle(String title)
			throws ServiceException,SystemException, DAOException;
	
	public void savePublish(TInfoBasePublish publish, OALogInfo... loginfos)
			throws ServiceException,SystemException, DAOException;

	public void savePublish(TInfoBasePublish publish, String[] scIds, OALogInfo... loginfos)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteByBs(String pubId)
			throws ServiceException,SystemException, DAOException;
	
	public String deleteByCb(String pubId, OALogInfo... loginfos)
			throws ServiceException,SystemException, DAOException;
	
	public void setShare(String pubId, String isshare)
			throws ServiceException,SystemException, DAOException;
	
	public void markPreUse(String pubId, Boolean preUse)
			throws ServiceException,SystemException, DAOException;
	
	public void saveMailInfo(MailInfo info)
			throws ServiceException,SystemException, DAOException;

	public void saveComment(String pubId, String comment)
			throws ServiceException,SystemException, DAOException;

	public List<ScoreRankDto> scoreRank()
			throws ServiceException,SystemException, DAOException;
	
	public Boolean isExistByColId(String colId)
			throws ServiceException,SystemException, DAOException;
	
	public Boolean isExistByAptId(String aptId)
			throws ServiceException,SystemException, DAOException;
		
	public TInfoBasePublishUser getPU(String pubId, String userId)
	throws ServiceException,SystemException, DAOException;

	public Boolean isRead(String pubId, String userId)
		throws ServiceException,SystemException, DAOException;
	
	public void savePU(String pubId, String userId)
		throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublishUser> getPUList(String userId)
	throws ServiceException,SystemException, DAOException;
	
	public String deletePU(String pubId, String userId)
		throws ServiceException,SystemException, DAOException;
	
	public List<TInfoBasePublish> findPublishDate()
	throws ServiceException,SystemException, DAOException;

	
	public List<TInfoBasePublish> findPublishsByIssIdAnduserId(String issId, String userId)
	throws ServiceException, SystemException, DAOException;
	
	public Page<TInfoBasePublish> getPublishs1(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus )
			throws ServiceException, SystemException, DAOException;
	
	public Page<TInfoBasePublish> getPublishs2(Page<TInfoBasePublish> page,
			String orgId, String submitStatus, String useStatus )
			throws ServiceException, SystemException, DAOException;
	
	public List<TInfoBaseComment> findComment(String pubId) throws ServiceException, SystemException,
			DAOException;
	
	public void saveComment(TInfoBaseComment comment) throws ServiceException, SystemException,
			DAOException;
	
	public void delectComment(TInfoBaseComment comment) throws ServiceException, SystemException,
			DAOException;
	
	public TInfoBaseComment findComment1(String comId) throws ServiceException, SystemException,
			DAOException;

	
	public List<TInfoBasePublish> findPublishedByDate(Date startDate, Date endDate)
			throws ServiceException,SystemException, DAOException;
	
	public Map<String, BigDecimal> getPlusScore(List<TInfoBasePublish> publishs)
			throws ServiceException,SystemException, DAOException;
	
	public Map<String, BigDecimal> getOrgScore(List<TInfoBasePublish> publishs)
			throws ServiceException,SystemException, DAOException;

	public void updatePublish(TInfoBasePublish pub)
	throws ServiceException, SystemException, DAOException;
	
	public List<TInfoBasePublish> getPublishBymerge(String flag)
	throws ServiceException,SystemException, DAOException;
	
	public Page<TInfoBasePublish> getPublishBymerge1(Page page,String flag)
	throws ServiceException,SystemException, DAOException;
	
	public TInfoBasePublish getPublishBymergeFlag(String flag)
	throws ServiceException,SystemException, DAOException;
	
	public List<?> getPublishOrdersort(String colId,String issId)
	throws ServiceException,SystemException, DAOException;
	
	public List<?> getPublishup(int sort,String colId,String issId)
	throws ServiceException,SystemException, DAOException;
	
	public List<?> getPublishdown(int sort,String colId,String issId)
	throws ServiceException,SystemException, DAOException;
	/*
	 * 获得前一篇文章
	 * 
	 */
	public String getPrePublish(String date)
	throws ServiceException,SystemException, DAOException;
	
	/*
	 * 获得后一篇文章
	 * 
	 */
	public String getNextPublish(String date)
	throws ServiceException,SystemException, DAOException;
	
	public List getPublishId()
	throws ServiceException,SystemException, DAOException;
	
	public List getMerPublishId()
	throws ServiceException,SystemException, DAOException;
}
