package com.strongit.oa.attachment;


import java.util.List;

import com.strongit.oa.bo.WorkflowAttach;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;

/**
 * 公文附件接口
 * @author 严建
 */
public interface IWorkflowAttachService {
	
	public boolean doneHistoryData() throws DAOException,ServiceException,SystemException;

	public List<WorkflowAttach> getWorkflowAttachsByDocId(String docId,boolean containsAttach);
	
	/**
	 * 删除数据库中的附件记录
	 * @param attachId				附件id
	 * @throws DAOException
	 * @throws ServiceException
	 * @throws SystemException
	 */
	public void delete(String attachId) throws DAOException,ServiceException,SystemException;
	
	/**
	 * @description 保存功能附件
	 * @author 严建
	 * @createTime Aug 29, 2011
	 * @param model 
	 * @ return void 
	 */
	public void saveWorkflowAttach(WorkflowAttach model);
	
	/**
	 * @description 获取附件信息
	 * @author 严建
	 * @createTime Aug 29, 2011
	 * @param id 附件id
	 * @return
	 * @ return WorkflowAttach  公文附件 
	 */
	public WorkflowAttach get(String id);
	
	public byte[] gets(String ATTACH_PATH);
	
	/**
	 * @description 获取不包含内容的附件信息
	 * @author 严建
	 * @createTime Sep 2, 2011
	 * @param id 附件id
	 * @return
	 * @ return WorkflowAttach  公文附件 
	 */
	public WorkflowAttach getWithoutContent(String id);
	/**
	 * @description 根据公文id获取附件列表
	 * @author 严建
	 * @createTime Aug 29, 2011
	 * @param docId 公文id
	 * @return
	 * @ return List<WorkflowAttach> 附件列表
	 */
	public List<WorkflowAttach> getWorkflowAttachsByDocId(String docId);

	public void savetoAttachs(byte[] inputstream2ByteArray);


}
