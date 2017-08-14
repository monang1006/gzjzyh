package com.strongit.oa.approveinfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TJbpmTaskExtend;
import com.strongit.oa.bo.ToaApproveinfo;
import com.strongit.oa.common.business.jbpmtaskextend.IJbpmTaskExtendService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.parameter.Parameter;
import com.strongit.workflow.bo.TwfInfoApproveinfo;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.GenericDAOHibernate;

/**
 * OA系统中的意见管理功能,管理在拟稿环节中需要保持意见的问题.
 * 在提交流程后需要更新到工作流中.
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Dec 9, 2011
 * @classpath	com.strongit.oa.approveinfo.ApproveinfoManager
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
@Repository
@Transactional
public class ApproveinfoManager {

	private GenericDAOHibernate<ToaApproveinfo, String> DAO;
	
	@Autowired
	private IUserService userService;
	@Autowired
	private IWorkflowService workflowService;
	@Autowired
	private IJbpmTaskExtendService jbpmTaskExtendService;
	/**
	 * 注入SESSION工厂
	 * @author:邓志城
	 * @date:2010-7-7 下午04:54:30
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		DAO = new GenericDAOHibernate<ToaApproveinfo, String>(sessionFactory,ToaApproveinfo.class);
	}

	/**
	 * 保存意见数据
	 * @param businessName		意见数据：JSON格式
	 * @param businessId		业务数据id
	 */
	public void save(String businessName,String businessId,String userId,String ruserId) {
		User user = userService.getUserInfoByUserId(userId);
		ToaApproveinfo entry = new ToaApproveinfo();
		List<ToaApproveinfo> list = this.findApproveInfoByBid(businessId);
		JSONObject approveData = JSONObject.fromObject(businessName);//意见数据
		String aiControlName = approveData.getString("aiControlName");//控件名称
		String aiContent = approveData.getString("aiContent");//意见内容
		String[] contents = aiContent.split("＜BR＞");//yanjian 处理换行符 2012-02-11 16:41
		String content_bak = null;
		for(int i=0;i<contents.length;i++){
			if(content_bak == null){
				content_bak = "";
			}
			if(i<contents.length -1){
				content_bak += contents[i] +"\r\n";
			}else{
				content_bak +=  contents[i];
			}
		}
		if(content_bak != null){
			aiContent = content_bak;
		}
		if(!list.isEmpty()) {
			entry = list.get(0);
			if(aiContent == null || aiContent.length() == 0) {//存在记录，意见为空时删除原纪录
				this.delete(entry.getAiId());
				return;
			}
		}else{
			if(aiContent == null || aiContent.length() == 0) {//不存在记录，意见为空，返回，不保存
				return ;
			}
		}
		String aiDate = approveData.getString("aiDate");//填写日期
		String atDate = approveData.getString("atDate"); //领导处理日期
		entry.setAiActor(user.getUserName());
		entry.setAiActorId(user.getUserId());
		entry.setAiBusinessId(businessId);
		entry.setAiContent(aiContent);
		entry.setAiControlName(aiControlName);
		if(ruserId != null && !"".equals(ruserId)){//存在代办
			User ruser = userService.getUserInfoByUserId(ruserId);
			entry.setAtoPersonId(ruserId);
			entry.setAtoPersonName(ruser.getUserName());
			entry.setAtAssignType("2");
		}
		try {
			if(aiDate != null && !"".equals(aiDate)){				
				if(aiDate.length() == 10) {
					entry.setAiDate(new SimpleDateFormat("yyyy-MM-dd").parse(aiDate));
				} else {
					entry.setAiDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(aiDate));
				}
			}
			if(atDate != null && !"".equals(atDate)){
				
				if(atDate.length() == 10) {
					entry.setAtDate(new SimpleDateFormat("yyyy-MM-dd").parse(atDate));
				} else {
					entry.setAtDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(atDate));
				}
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			entry.setAiDate(new Date());
		}
		if(!list.isEmpty()) {
			DAO.update(entry);
		}else{
			DAO.save(entry);
			
		}
	}
	
	public void save(ToaApproveinfo entry) {
		DAO.save(entry);
	}

	public void delete(String id) {
		DAO.delete(id);
	}
	
	/**
	 * 根据业务数据和处理人得到意见对象
	 * @param businessId			业务数据id
	 * @param userId				处理人id
	 * @return						意见对象
	 * @throws DAOException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaApproveinfo> findApproveInfo(String businessId,String userId) throws DAOException,SystemException {
		//StringBuilder hql = new StringBuilder("from ToaApproveinfo t where t.aiContent is not null ");
		StringBuilder hql = new StringBuilder("from ToaApproveinfo t where 1=1 ");
		if(businessId != null && businessId.length() > 0) {
			hql.append(" and t.aiBusinessId='"+businessId+"'");
		}
		if(userId != null && userId.length() > 0) {
			hql.append(" and t.aiActorId='"+userId+"' ");
		}
		List list = DAO.find(hql.toString());
		if(list.size() > 1) {
			throw new DAOException("找到多于一条记录");
		}
		return list;
	}
	
	/**
	 * 根据业务数据得到意见对象
	 * @param businessId			业务数据id
	 * @return						意见对象
	 * @throws DAOException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaApproveinfo> findApproveInfoByBid(String businessId) throws DAOException,SystemException {
		StringBuilder hql = new StringBuilder("from ToaApproveinfo t where 1=1 ");
		if(businessId != null && businessId.length() > 0) {
			hql.append(" and t.aiBusinessId='"+businessId+"'");
		}

		List list = DAO.find(hql.toString());
		if(list.size() > 1) {
			throw new DAOException("找到多于一条记录");
		}
		return list;
	}
	
	/**
	 * 根据主键得到意见对象
	 * @param Aid			主键id
	 * @return						意见对象
	 * @throws DAOException
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ToaApproveinfo> findApproveInfoByAid(String aid) throws DAOException,SystemException {
		StringBuilder hql = new StringBuilder("from ToaApproveinfo t where 1=1 ");
		if(aid != null && aid.length() > 0) {
			hql.append(" and t.aiId='"+aid+"'");
		}

		List list = DAO.find(hql.toString());
		if(list.size() > 1) {
			throw new DAOException("找到多于一条记录");
		}
		return list;
	}
	
	/**
	 *  在启动流程或在草稿提交时，更新流程办理意见及日期
	 * 
	 * @description
	 * @author 严建
	 * @param businessId
	 * @param instanceId
	 * @param sugguestion
	 * @createTime Apr 17, 2012 12:48:48 PM
	 */
	public void synchronizedApproveInfo(String businessId,String instanceId,String sugguestion,Parameter parameter) throws ServiceException,DAOException, SystemException{
		try {
		    User user = userService.getCurrentUser();
			List<ToaApproveinfo> approveInfo = findApproveInfo(businessId.split(";")[2], user.getUserId());
			List<TwfInfoApproveinfo> wfApproveInfo = workflowService.getApproveInfosByPIId(instanceId);
			synchronizedApproveInfo(approveInfo, wfApproveInfo, parameter);	
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	/**
	 * 同步临时意见表和流程意见表的数据
	 * 
	 * @description
	 * @author 严建
	 * @param taskId
	 * @createTime Apr 17, 2012 12:45:59 PM
	 */
	@SuppressWarnings("unchecked")
	public void synchronizedApproveInfo(String taskId,Parameter parameter) throws ServiceException,DAOException, SystemException{
		try {
			List<ToaApproveinfo> approveInfo = findApproveInfoByBid(taskId);
	        
        	List<TwfInfoApproveinfo> wfApproveInfo = workflowService.getDataByHql(
        		"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
        		new Object[] { new Long(taskId) });
        	synchronizedApproveInfo(approveInfo, wfApproveInfo, parameter);
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
    }
	
	/**
	 * 同步临时意见表和流程意见表的数据
	 * 
	 * @author yanjian
	 * @param approveInfo		临时意见表数据信息
	 * @param wfApproveInfo		流程意见表数据信息
	 * @param parameter			参数存储对象
	 * @throws ServiceException
	 * @throws DAOException
	 * @throws SystemException
	 * Nov 27, 2012 3:26:03 PM
	 */
	private void synchronizedApproveInfo(List<ToaApproveinfo> approveInfo,
			List<TwfInfoApproveinfo> wfApproveInfo, Parameter parameter)
			throws ServiceException, DAOException, SystemException {
		try {
			if (!wfApproveInfo.isEmpty()) {
				boolean modify = false; //流程意见记录是否发生变更
				TwfInfoApproveinfo wfAInfo = wfApproveInfo.get(0);
				ToaApproveinfo info = null; //临时意见对象
				if (approveInfo != null && !approveInfo.isEmpty()) { //如果approveInfo不为null或者Empty时，给info赋值
					info = approveInfo.get(0);
				}
				List<TJbpmTaskExtend> jbpmTaskExtendList = jbpmTaskExtendService.getInfoForListByTaskInstanceId(new Long[]{wfAInfo.getAiTaskId()});
				if(jbpmTaskExtendList != null && !jbpmTaskExtendList.isEmpty()){
					wfAInfo.setAiAssigntype("1"); //1：指派意见标识
				}
				//任务处理类型：代办
				if (parameter.isDaiBan()) { //如果是代办意见操作
					User user = userService.getCurrentUser();
					String aiOpersonid = wfAInfo.getAiActorId();
					String aiOpersonname = wfAInfo.getAiActor();
					// 意见处理人设为当前用户
					wfAInfo.setAiActorId(user.getUserId());
					wfAInfo.setAiActor(user.getUserName());
					// 意见委托人设为原记录的处理人
					wfAInfo.setAiOpersonid(aiOpersonid);
					wfAInfo.setAiOpersonname(aiOpersonname);
					wfAInfo.setAiAssigntype("2"); //2：代办意见标识
					if (info != null && !parameter.isBack()) { //不是退回操作
						if (info.getAtDate() != null) {
							wfAInfo.setAiFpersonname(info.getAtDate()
									.toString());
						}
						wfAInfo.setAiDate(info.getAiDate());
					}
					modify = true;
				}

				if (info != null) {
					if (!parameter.isBack()) { //不是退回操作
						wfAInfo.setAiContent(info.getAiContent()); //设置处理内容
						modify = true;
					}
					delete(info.getAiId()); //删除草稿中意见
				}else{
					/**
					 * 解决取回，再提交意见不能更新的问题
					 * modify yanjian 2012-11-28 23:21 退回不做处理
					 * */
					if(!parameter.isBack()){
						if(!"".equals(parameter.getSuggestion())&&null != wfAInfo.getAiContent() && parameter.getSuggestion() != null&& !parameter.getSuggestion().equals(wfAInfo.getAiContent())){
							wfAInfo.setAiContent(parameter.getSuggestion()); //设置处理内容
							modify = true;
						}
					}
				}
				if (modify) { //流程意见对象发生变更时，重新保存流程意见对象
					workflowService.saveApproveInfo(wfAInfo);// 更新意见和内容
				}
			}
		} catch (DAOException e) {
			throw e;
		} catch (ServiceException e) {
			throw e;
		} catch (SystemException e) {
			throw e;
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}
	
	/**
	 * 抚州高新区定制签发功能：提交下一步时，更新流程办理意见及日期
	 * from 林业厅
	 * @description
	 * @author 
	 * @param taskId
	 * @createTime sep 20, 2012 11:14:59 AM
	 */
	@SuppressWarnings("unchecked")
	public void qianfaWorkflowNextStepAfterUpdate(String taskId) {
        	List<ToaApproveinfo> approveInfo = findApproveInfoByBid(taskId);
        
        	List<TwfInfoApproveinfo> wfApproveInfo = workflowService.getDataByHql(
        		"from TwfInfoApproveinfo ta where ta.aiTaskId = ?",
        		new Object[] { new Long(taskId) });
        
        	if (true) {
        	    TwfInfoApproveinfo wfAInfo = wfApproveInfo.get(0);
        	    if (!approveInfo.isEmpty()) {
	        		ToaApproveinfo info = approveInfo.get(0);
	        		if (info.getAtoPersonId() != null
	        			&& !"".equals(info.getAtoPersonId())) {
	        		    wfAInfo.setAiActorId(info.getAiActorId());
	        		    wfAInfo.setAiActor(info.getAiActor());
	        		    wfAInfo.setAiOpersonid(info.getAtoPersonId());
	        		    wfAInfo.setAiOpersonname(info.getAtoPersonName());
	        		    wfAInfo.setAiAssigntype("2");//任务处理类型：代办
	        		    if (info.getAtDate() != null) {
	        			wfAInfo.setAiFpersonname(info.getAtDate().toString());
	        		    }
	        		}
	        		wfAInfo.setAiContent("<签发>" + info.getAiContent());
	        		wfAInfo.setAiDate(info.getAiDate());
	        
	        		// 删除草稿中意见
	        		delete(info.getAiId());
        	    } else {
        	    	// 没有填写意见
        	    	User user = userService.getCurrentUser();
        	    	// 意见记录中的意见处理人与当前用户不是同一个人，说明是代办模式
	        		if (!wfAInfo.getAiActorId().equals(user.getUserId())) {
	        		    String aiOpersonid = wfAInfo.getAiActorId();
	        		    String aiOpersonname = wfAInfo.getAiActor();
	        		    // 意见处理人设为当前用户
	        		    wfAInfo.setAiActorId(user.getUserId());
	        		    wfAInfo.setAiActor(user.getUserName());
	        		    // 意见委托人设为原记录的处理人
	        		    wfAInfo.setAiOpersonid(aiOpersonid);
	        		    wfAInfo.setAiOpersonname(aiOpersonname);
	        		    wfAInfo.setAiAssigntype("2");//任务处理类型：代办
	        		}
        	    }
        
        	    workflowService.saveApproveInfo(wfAInfo);// 更新意见和内容
    	}

    }
	
	/**
	 * 根据意见记录id获得生成该意见记录的任务id
	 * @param aiId
	 * @return
	 */
	public String getTaskIdByAiId(String aiId){
		String taskId = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String sql = "select AI_TASK_ID from T_WF_INFO_APPROVEINFO where AI_ID='" + aiId + "'";
		try {
			psmt = DAO.getConnection().prepareStatement(sql);
			rs = psmt.executeQuery();
			if(rs.next()){
				taskId = rs.getString("AI_TASK_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(psmt != null){
				try {
					psmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return taskId;
	}
}