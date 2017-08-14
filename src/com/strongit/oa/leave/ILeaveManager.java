package com.strongit.oa.leave;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.strongit.oa.bo.TOmConAttach;
import com.strongit.oa.bo.TOmConType;
import com.strongit.oa.bo.TOmConference;
import com.strongit.oa.bo.TOmConferenceSend;
import com.strongit.oa.bo.TOmDeptreport;
import com.strongit.oa.bo.TSwConfersort;
import com.strongmvc.orm.hibernate.Page;

public interface ILeaveManager {
	
	/***************************************************************************
	 * 
	 * 方法简要描述：分页查找下发通知表
	 * 
	 * 
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:Apr 7, 2013
	 * @Author 胡海亮
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @param state
	 * @param receState
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmConferenceSend> queryForConsend(Page<TOmConferenceSend> page,
			TOmConferenceSend entity, Date beginTime, Date endTime,
			Date regendtime, String state);
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
	 * @Author 胡海亮
	 * @param page
	 * @param entity
	 * @param beginTime
	 * @param endTime
	 * @param regendtime
	 * @return
	 * @version 1.0
	 * @see
	 */
	public Page<TOmDeptreport> queryLeaveList(Page<TOmDeptreport> page,
			TOmDeptreport entity, Date beginTime, Date endTime,
			Date regendtime) ;
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
	 * @Author 胡海亮
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
			Date regendtime) ;
	/***************************************************************************
	 * 
	 * 方法简要描述：根据附件编号，获取附件实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-11
	 * @Author 胡海亮
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
	 * @Date:2013-04-11
	 * @Author 胡海亮
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
	 * @Date:2013-04-11
	 * @Author 胡海亮
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
	 * @Author 胡海亮
	 * @param model
	 * @param attachsFileName
	 * @param attachs
	 * @version 1.0
	 * @see
	 */
	public void saveLeaveInfo(TOmDeptreport model, String depIds,
			String[] attachsFileName, File[] attachs) ;
	/***************************************************************************
	 * 
	 * 方法简要描述：保存单位请假对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-11
	 * @Author 胡海亮
	 * @param entity
	 * @throws Exception
	 * @version 1.0
	 * @see
	 */
	public void saveLeave(TOmDeptreport entity) throws Exception;
	/***************************************************************************
	 * 
	 * 方法简要描述：根据会议通知主键，获取会议通知实体对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-11
	 * @Author 胡海亮
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TOmConference getLeaveByConId(String conNoticeId);
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
	 * @Author 胡海亮
	 * @param entity
	 * @version 1.0
	 * @see
	 */
	public void saveConType(TSwConfersort entity);
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
	 * @Author 胡海亮
	 * @param name
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TSwConfersort getContypeByName(String name) ;
	
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
	 * @Author 胡海亮
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
	 * @Author 胡海亮
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<TSwConfersort> findAll() ;
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
	 * @Author 胡海亮
	 * @param sortId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public TSwConfersort getConferSortById(String sortId);
	
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
	 * @Author 胡海亮
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
	 * @Date:2013-04-11
	 * @Author 胡海亮
	 * @param conNoticeId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public String getAcceptOrgIdsByConid(String conNoticeId);
	
	/***************************************************************************
	 * 
	 * 方法简要描述：获取所有的会议类型
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-11
	 * @Author 胡海亮
	 * @return
	 * @version 1.0
	 * @throws Exception 
	 * @see
	 */
	public List<TOmConType> getAllConType() throws Exception;
	/***************************************************************************
	 * 
	 * 方法简要描述：获取所有的会议类型
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-11
	 * @Author 胡海亮
	 * @return
	 * @version 1.0
	 * @throws Exception 
	 * @see
	 */
	public List<TSwConfersort> getAllConSort() throws Exception;
	
	/***************************************************************************
	 * 
	 * 方法简要描述:根据DepId,获取指定单位下签发通知集合
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-12
	 * @Author 胡海亮
	 * @param meetingId
	 * @param fromOrgId
	 * @return
	 * @version 1.0
	 * @see
	 */
	public List<TOmConferenceSend> getConsendList(String depId)throws Exception;
	
	/***************************************************************************
	 * 
	 * 方法简要描述:根据Id获取TOmDeptreport对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-15
	 * @Author 胡海亮
	 * 
	 * @param page
	 * @param model
	 * @param state
	 * @param conId
	 * @param depId
	 * 
	 * @return Page<TOmDeptreport>
	 * 
	 */
	public TOmDeptreport getDepRepById(String id) throws Exception;
	
	/***************************************************************************
	 * 
	 * 方法简要描述:根据DepId、ConId、State和Model的相关信息,获取TOmDeptreport对象
	 * 
	 * 方法详细描述
	 * 
	 * 
	 * JavaDoc tags,比如
	 * 
	 * @Date:2013-04-15
	 * @Author 胡海亮
	 * 
	 * @param page
	 * @param model
	 * @param state
	 * @param conId
	 * @param depId
	 * 
	 * @return Page<TOmDeptreport>
	 * 
	 */
	public Page<TOmDeptreport> getConreportPage(Page<TOmDeptreport> page,
			TOmDeptreport model,String state,String conId,String depId) ;
	
	/**
	 * 根据大中型会议ID得到当前会议通知
	* 函数功能说明 
	* Jianggb  2013-4-19 
	* 修改者名字 修改日期 
	* 修改内容 
	* @param @param conId
	* @param @return     
	* @return TOmConference    
	* @throws
	 */
	public TOmConference getBigConference(String conId);
}
