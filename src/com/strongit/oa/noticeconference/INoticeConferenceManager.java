package com.strongit.oa.noticeconference;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmConType;
import com.strongmvc.orm.hibernate.Page;

public interface INoticeConferenceManager {
	
	/***
	 * 
	* 方法简要描述：已办结会议通知
	*	 备注：只要会议通知下其中之一的单位被接收报名处理，该会议通知状态为已办结状态。
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 18, 2013
	* @Author 万俊龙
	* @param page
	* @param entity
	* @param beginTime
	* @param endTime
	* @param regendtime
	* @return
	* @version	1.0
	* @see
	 */
	public  Page<TOmConference> queryHandleList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime, Date regendtime) throws ParseException;
	/****
	 * 
	* 方法简要描述:保存会议下派单位
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param conId
	* @param deptIds
	* @version	1.0
	* @see
	 */
	public void saveForConsend(String conId,String deptIds);
	/***
	 * 
	* 方法简要描述：查找会议下下派的单位
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 9, 2013
	* @Author 万俊龙
	* @param conId
	* @return
	* @version	1.0
	* @see
	 */
	public List<TOmConferenceSend> findConferenceSendListByConId(String conId);
	/***
	 * 
	* 方法简要描述：下发会议通知
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 7, 2013
	* @Author 万俊龙
	* @version	1.0
	* @see
	 */
	public void sendConference(String conId);
	/***
	 * 
	* 方法简要描述：判断会议通知是否存在下发对象
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 7, 2013
	* @Author 万俊龙
	* @param conId
	* @return
	* @version	1.0
	* @see
	 */
	public boolean hasConferenceSend(String conId);
	/****
	 * 
	* 方法简要描述：删除草稿状态的会议通知，并将所属的会议附件及下派单位级联删除
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 3, 2013
	* @Author 万俊龙
	* @param conNoticeId
	* @version	1.0
	* @see
	 */
	public void deleteConNotice(String conNoticeId) ;
	/***************************************************************************
	 * 
	 * 方法简要描述:分页查询通知会议草稿
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmConference> queryDraftList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime,
			Date regendtime) throws ParseException ;
	/***************************************************************************
	 * 
	 * 方法简要描述：分页查询已发通知会议
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmConference> queryIssiedList(Page<TOmConference> page,
			TOmConference entity, Date beginTime, Date endTime,
			Date regendtime) throws ParseException ;
	/***************************************************************************
	 * 
	 * 方法简要描述：根据附件编号，获取附件实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param attachId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConAttach getConAttachByAttachId(String attachId);
	/***************************************************************************
	 * 
	 * 方法简要描述:构造附件展现html浮层，在前台页面需设定 function deldbobj()方法
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param request
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getConAttachHtmlByConId(HttpServletRequest request,
			String conNoticeId);
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议编号Id，获取所有下发单位主键信息 数据格式：【13333333,12222】
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getAcceptOrgIdsByConid(String conNoticeId, StringBuffer deptNames) ;
	/***************************************************************************
	 * 
	 * 方法简要描述:保存会议通知对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param model
	 * @param attachsFileName
	 * @param attachs
	 * @version 1.0
	 * @see
	 */
	public void saveConferInfo(TOmConference model, String depIds,
			String[] attachsFileName, File[] attachs) ;
	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议通知对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	public void saveConNotice(TOmConference entity) throws Exception;
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议通知主键，获取会议通知实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConference getConNoticeByConId(String conNoticeId);
	/***************************************************************************
	 * 
	 * 方法简要描述：保存会议类型; 保存会议类型之前，判断会员类型表中是否存在相同名称的会议类型，如果存在，不在保存。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void saveConType(TOmConType entity);
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议类型名称，查找会议类型对象。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param name
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConType getContypeByName(String name) ;
	
	/***************************************************************************
	 * 
	 * 方法简要描述：判断是否存在指定名称的会议类型; <br>
	 * false:表示不存在 true:表示存在
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @param name
	 * @return
	 * @version 1.0
	 * @see
	 */
	public boolean hasConType(String name);
	/**
	 * 
	 * 方法简要描述:获取所有的会议通知类型。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 1, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<TOmConType> findAll() ;
	/***************************************************************************
	 * 
	 * 方法简要描述：根据类型主键，获取类型实体对象。
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 2, 2013
	 * @Author 万俊龙
	 * @param sortId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConType getConferSortById(String sortId);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：得到所有单位及其下人员信息列表
	 * 
	 * @return List<Object[]> 信息列表<br>
	 *         <p>
	 *         信息列表数据结构：<br>
	 *         <p>
	 *         Object[]{组织机构Id, 父级组织机构Id, 组织机构名称, 组织机构下人员信息}<br>
	 *         <p>
	 *         最顶级组织机构父级Id为”0“<br>
	 *         <p>
	 *         人员信息数据机构：<br>
	 *         <p>
	 *         userList<String[]{人员Id, 人员名称}> 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * @Date:Apr 7, 2013
	 * @Author 万俊龙
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<Object[]> getAllOrgUserList();
	
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议编号Id，获取所有下发单位主键信息 数据格式：【13333333,12222】
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 3, 2013
	 * @Author 万俊龙
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getAcceptOrgIdsByConid(String conNoticeId);
}
