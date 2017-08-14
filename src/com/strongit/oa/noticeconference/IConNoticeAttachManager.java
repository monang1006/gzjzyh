package com.strongit.oa.noticeconference;

import java.util.List;

import com.strongit.oa.bo.TOmConAttach;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

public interface IConNoticeAttachManager {
	/**
	 * 
	 * 方法简要描述：根据附件主键Id,获取附件实体对象信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conAttachId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConAttach getConAttachByAttachId(String conAttachId);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议通知id获取附件
	 * 
	 * @param containsAttach：
	 *            是否附件文件内容[true:包含|false:不包含] 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachConId
	 * @param containsAttach：
	 *            是否附件文件内容[true:包含|false:不包含]
	 * @return
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	@SuppressWarnings("unchecked")
	public List<TOmConAttach> getAttachsByAttachConId(String attachConId,
			boolean containsAttach) throws ServiceException, DAOException,
			SystemException ;
	
	/***************************************************************************
	 * 
	 * 方法简要描述：删除附件信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param id
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public void deleteConAttach(String id) throws ServiceException,
			DAOException, SystemException ;
	
	/***************************************************************************
	 * 
	 * 方法简要描述：删除附件信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public void deleteConAttach(TOmConAttach entity) throws ServiceException,
			DAOException, SystemException;
	
	/***************************************************************************
	 * 
	 * 方法简要描述：批量删除会议中的附件
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param attachConId
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * @version 1.0
	 * @see
	 */
	public void deleteConAttchByAttachConId(String attachConId)
			throws ServiceException, DAOException, SystemException ;
	
	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议通知附件信息
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param model
	 * @version 1.0
	 * @see
	 */
	public void saveConAttach(TOmConAttach model);
	 
	
}
