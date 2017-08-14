package com.strongit.oa.workinspect.worksend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.SystemException;

import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOsManagementSummary;
import com.strongit.oa.bo.TOsTaskAttach;
import com.strongit.oa.bo.TOsWorkReviews;
import com.strongit.oa.bo.TOsWorkType;
import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongit.oa.common.remind.Constants;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.im.IMManager;
import com.strongit.oa.sms.IsmsService;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.MessagesConst;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
public class WorkSendAction extends BaseActionSupport<TOsWorktask> {

	private Page<TOsWorktask> page = new Page<TOsWorktask>(10, true);
	private Page<TOsWorktaskSend> pageSend = new Page<TOsWorktaskSend>(10, true);
	private TOsWorktask model = new TOsWorktask();
	private TOsWorktaskSend send = new TOsWorktaskSend();
	private TOsWorkReviews review = new TOsWorkReviews();

	private List<TOsWorkType> workTypeList = new ArrayList<TOsWorkType>();
	private List<TOsWorktaskSend> workSendList = new ArrayList<TOsWorktaskSend>();
	private List<TOsTaskAttach> attachList = new ArrayList<TOsTaskAttach>();
	private List<TOsTaskAttach> workattachList = new ArrayList<TOsTaskAttach>();
	private List<TOsManagementSummary> summaryList = new ArrayList<TOsManagementSummary>();
	private Map<String, String> taskTypeMap = new HashMap<String, String>();// 办理类型（个人=1，部门=0）
	private Map<String, String> taskStateMap = new HashMap<String, String>();// 办理状态(未签收=0，待办=1，办结=2)

	private String taskId;
	private String taskTitle;
	private String sendTaskId;
	private String attachId;
	private String recvIds;
	private String recvNames;
	private String selectedData;

	private String delAttIds;//删除附件ID
	
	private File[] file; // 附件
	private String[] fileFileName; // 附件名称

	// 查询字段
	private String selectTaskTitle;// 工作标题
	private String selectTaskNo;// 工作编号
	private String selectTaskType;// '1':'按个人查','0':'按部门查'

	private String selectTaskTitle2;// 工作标题
	private String selectRecvOrgName;// 承办单位
	private Date selectTaskBSendTime;// 发送开始时间
	private Date selectTaskESendTime;// 发送结束时间
	private String selectTaskState;// 状态(未签收=0，待办=1，办结=2)

	@Autowired
	private IUserService userService;
	@Autowired
	private IWorkSendService taskService;
	
	private String  remindType;//提醒方式

	@Autowired
	private IsmsService smsService;// 手机短信发送方式

	@Autowired
	private IMManager rtxService;// rtx发送方式
	
	private String inputFrom;//判断是从草稿处新增还是工作发送新增

	@Override
	public String delete() throws Exception {
		String[] ids = taskId.split(",");
//		for (String id : ids) {
//			model = this.taskService.getTaskById(id);
//			if (model != null) {
//				Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
//				workSendList = new ArrayList<TOsWorktaskSend>(sends);
//				this.taskService.deleteRecver(workSendList.get(0).getSendtaskId());
//			}
//			attachList = this.taskService.getAttachListByTaskId(id);
//			this.taskService.deleteWorkSendAtt(attachList);
//		}
		this.taskService.deleteWorkSend(taskId);
		return renderHtml("<script>parent.document.location='" + getRequest().getContextPath()
				+ "/fileNameRedirectAction.action?toPage=/workinspect/worksend/worksend.jsp"
				+ "'; </script>");
	}
	public void getworkType(){
		
	}

	/**
	 * 删除草稿
	 * @author: qibh
	 *@return
	 *@throws Exception
	 * @created: 2013-5-7 上午02:24:15
	 * @version :5.0
	 */
	public String deleteDraft() throws Exception {
		String[] ids = taskId.split(",");
//		for (String id : ids) {
//			model = this.taskService.getTaskById(id);
//			if (model != null) {
//				Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
//				workSendList = new ArrayList<TOsWorktaskSend>(sends);
//				this.taskService.deleteRecver(workSendList.get(0).getSendtaskId());
//			}
//			attachList = this.taskService.getAttachListByTaskId(id);
//			this.taskService.deleteWorkSendAtt(attachList);
//		}
		this.taskService.deleteWorkSend(taskId);
		return renderHtml("<script>location='" + getRequest().getContextPath()
				+ "/workinspect/worksend/workSend!getPersonalDraft.action"
				+ "'; </script>");
	}
	
	@Override
	public String input() throws Exception {
		workTypeList = this.taskService.getWorkTypeList();
		model = new TOsWorktask();
		// 生成工作编号
		Date d = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String taskNo = sf.format(d);
		this.model.setWorktaskNo("TASK" + taskNo);
		User u = userService.getCurrentUser();
		model.setWorktaskUserName(u.getUserName());
		model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(u
				.getOrgId()));
		return INPUT;
	}
	
//	/草稿转办理
	public String inputDraft() throws Exception {
		workTypeList = this.taskService.getWorkTypeList();
		//编辑办理标识
		this.selectedData = "";
		model = this.taskService.getTaskById(taskId);
		Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
		recvIds = "";
		recvNames = "";
		if(sends != null && !sends.isEmpty()){
			//getTaskRecvTime
			List<TOsWorktaskSend> sendss = new ArrayList<TOsWorktaskSend>(sends);
			Collections.sort(sendss, new Comparator<TOsWorktaskSend>(){

				public int compare(TOsWorktaskSend o1, TOsWorktaskSend o2) {
					// TODO Auto-generated method stub
					return o1.getTaskSendTime().compareTo(o2.getTaskSendTime()) ;
				}
				
			}
			);
			for (TOsWorktaskSend sender : sendss) {
				TUumsBaseUser recvU = userService.getUserInfoByUserId(sender.getTaskRecvId());
				/*4|402882043e4e5c2b013e4e6cec360277
				 * ,4|402882043e4e5c2b013e4e6cec270275
				 * ,4|402882043e4e5c2b013e4e6cec270273
				 * */
				recvIds += (",4|"+recvU.getUserId());
				/*肖久光,熊 斌,应 勇*/
				recvNames += (","+recvU.getUserName());
				/*u402882043e4e5c2b013e4e6cec270273$297eb7473e4ab390013e4ab485fa0003,应  勇
				|u402882043e4e5c2b013e4e6cec270275$297eb7473e4ab390013e4ab485fa0003,熊  斌*/
				selectedData += "|u"+recvU.getUserId()+"$" + recvU.getOrgId()+","+recvU.getUserName() ;
			}
			if (!"".equals(recvIds)) {
				recvIds = recvIds.substring(1, recvIds.length());
				recvNames = recvNames.substring(1, recvNames.length());
				selectedData = selectedData.substring(1,
						selectedData.length());
			}
		}

			// 是否有附件
			attachList = this.taskService.getAttachListByTaskId(taskId);
		
		
//		model = new TOsWorktask();
//		// 生成工作编号
//		Date d = new Date();
//		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//		String taskNo = sf.format(d);
//		this.model.setWorktaskNo("TASK" + taskNo);
		User u = userService.getCurrentUser();
		model.setWorktaskUserName(u.getUserName());
		model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(u
				.getOrgId()));
			
		String temp = getRequest().getParameter("temp");
		if("1".equals(temp)){
			inputFrom="1";
			return "Draft";
		}
		return "inputDraft";
	}

	public String taskReview() throws Exception {
		model = this.taskService.getTaskById(taskId);
		return "taskReview";
	}

	public String sendReview() throws Exception {
		send = this.taskService.getSendTaskById(sendTaskId);
		this.taskTitle = this.taskService.getTaskById(
				send.getTOsWorktask().getWorktaskId()).getWorktaskTitle();

		// 通过send找review
		review = this.taskService.getReviewBySendId(sendTaskId);
		if (review == null) {
			review = new TOsWorkReviews();
			review.setWorktaskId(send.getTOsWorktask().getWorktaskId());
			review.setSendtaskId(sendTaskId);
		}
		return "sendReview";
	}

	/**
	 * 保存工作任务的评语
	 */
	public String saveTaskReview() throws Exception {
		taskService.saveTaskReview(this.model);
		return renderHtml("<script>window.close();</script>");
	}

	/**
	 * 保存工作下发的评语
	 */
	public String saveSendReview() throws Exception {
		if ("".equals(review.getReviewsid())) {
			review.setReviewsid(null);
		}
		taskService.saveSendReview(review);
		return renderHtml("<script>window.close();</script>");
	}

	/**
	 * 保存工作任务的纪要
	 */
	public String saveTaskSummary() throws Exception {
		TOsWorktask task = this.taskService.getTaskById(model.getWorktaskId());
		task.setManagetDemo(model.getManagetDemo());
		task.setManagetProgress(model.getManagetProgress());
		task.setManagetState(model.getManagetState());
		task.setManagetTime(new Date());
		if(null!=delAttIds&&!"".equals(delAttIds)){
//			delAttIds = delAttIds.substring(0,delAttIds.length()-1);
			String[] ids = delAttIds.split(",");
			for(int i=0;i<ids.length;i++){
				taskService.delAttach(ids[i]);
			}
		}
		taskService.saveTaskSummary(task, file, fileFileName);
		return renderHtml("<script>window.close();</script>");
	}

	public String edit() throws Exception {
		workTypeList = this.taskService.getWorkTypeList();
		this.selectedData = "";
		if (taskId == null || "".equals(taskId)) {
			model = new TOsWorktask();
			// 生成工作编号
			Date d = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
			String taskNo = sf.format(d);
			this.model.setWorktaskNo("TASK" + taskNo);
			User u = userService.getCurrentUser();
			model.setWorktaskUserName(u.getUserName());
			model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(u
					.getOrgId()));

		} else {
			model = this.taskService.getTaskById(taskId);

			Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
			TUumsBaseUser u = userService.getUserInfoByUserId(model
					.getWorktaskUser());
			model.setWorktaskUserName(u.getUserName());
			model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(u
					.getOrgId()));
			workSendList = new ArrayList<TOsWorktaskSend>(sends);
			taskTypeMap.put("1", "个人");
			taskTypeMap.put("0", "部门");
			taskStateMap.put("0", "<font color=red>待签收</font>");
			taskStateMap.put("1", "待办");
			taskStateMap.put("2", "办结");
			recvIds = "";
			recvNames = "";
			for (TOsWorktaskSend send : sends) {
				if ("1".equals(send.getTaskType())) {
					// 个人承办
					TUumsBaseUser recvU = userService.getUserInfoByUserId(send
							.getTaskRecvId());
					recvIds += recvU.getUserId() + ",";
					recvNames += recvU.getUserName() + ",";
					send.setTaskRecvName(recvU.getUserName());
					TUumsBaseOrg recvOrg = userService.getOrgInfoByOrgId(send
							.getTaskRecvOrgid());
					if (recvOrg == null) {
						recvOrg = userService.getOrgInfoByUserId(recvU
								.getUserId());
					}
					send.setTaskRecvOrgName(recvOrg.getOrgName());

					this.selectedData += "4|" + recvU.getUserId() + ",";
				} else {
					// 单位承办
					TUumsBaseOrg org = userService.getOrgInfoByOrgId(send
							.getTaskRecvId());
					recvIds += org.getOrgId() + ",";
					recvNames += org.getOrgName() + ",";
					send.setTaskRecvName(org.getOrgName());
					send.setTaskRecvOrgName(org.getOrgName());
					this.selectedData += "1|" + org.getOrgId() + ",";
				}
			}
			if (!"".equals(recvNames)) {
				recvIds = recvIds.substring(0, recvIds.length() - 1);
				recvNames = recvNames.substring(0, recvNames.length() - 1);
				selectedData = selectedData.substring(0,
						selectedData.length() - 1);
			}

			// 是否有附件
			attachList = this.taskService.getAttachListByTaskId(taskId);
		}
		return "edit";
	}

	@Override
	public String list() throws Exception {

		return null;
	}

	public String getPersonal() throws Exception {
		model.setWorktaskTitle(selectTaskTitle);
		model.setWorktaskNo(selectTaskNo);
		page = this.taskService.getTaskList(page, model, selectTaskType);
		return "personal";
	}
	/**
	 * 获取某个工作的承办者ID
	 * @author: qibh
	 *@throws Exception
	 * @created: 2013-5-7 上午09:27:30
	 * @version :5.0
	 */
	public String getTaskReceiver()throws Exception {
		List<TOsWorktaskSend> list = this.taskService.getTaskReceiver(model);
		String receiveIds = "";
		if(list!=null&&list.size()>0){
			for(TOsWorktaskSend taskSend:list){
				if(receiveIds!=""&&!receiveIds.equals("")){
					receiveIds = receiveIds+","+taskSend.getTaskRecvId();
				}else{
					receiveIds = taskSend.getTaskRecvId();
				}
			}
		}
		renderText(receiveIds);
		return null;
	}
	public String getPersonalDraft() throws Exception {
		model.setWorktaskTitle(selectTaskTitle);
		model.setWorktaskNo(selectTaskNo);
		page = this.taskService.getTaskListDraft(page, model, selectTaskType);
		return "personalDraft";
	}

	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		if(null!=delAttIds&&!"".equals(delAttIds)){
//			delAttIds = delAttIds.substring(0,delAttIds.length()-1);
			String[] ids = delAttIds.split(",");
			for(int i=0;i<ids.length;i++){
				taskService.delAttach(ids[i]);
			}
		}
		taskService.save(this.model, this.recvIds, file, fileFileName);
		// addActionMessage("保存成功");
		// return INPUT;
		//if (this.model.getWorktaskId() != null
		//		&& !"".equals(this.model.getWorktaskId())&&!"null".equals(this.model.getWorktaskId())) {
		//	return "input";
		//} else {
		//	StringBuffer returnhtml = new StringBuffer("<script>");
		//	returnhtml.append(" alert('操作成功'); window.location='").append(
		//			getRequest().getContextPath()).append(
		//			"/workinspect/worksend/workSend!input.action';</script>");
		//	return renderHtml(returnhtml.toString());
		//}
		if(remindType!=null&&!"".equals(remindType)){
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String s[] = remindType.split(",");
				String con = "工作开始时间:" + df.format(model.getWorktaskStime()) + "---"
				+ df.format(model.getWorktaskEtime()) + "   工作内容："
				+ model.getWorktaskContent();
				String title = model.getWorktaskTitle();
				for (int k = 0; k < s.length; k++) {
					String mth = s[k];
					final List<String> users = new ArrayList<String>();
					String[] userids = recvIds.split(",");
					String userId = "";
					for (int i = 0; i < userids.length; i++) {
						String[] kv = userids[i].split("\\|");
						String taskRecvId = kv[1];// 接收单位id/个人id
						users.add(taskRecvId);
						if(userId!=""&&!"".equals(userId)){
							userId = userId+","+taskRecvId;
						}else{
							userId = taskRecvId;
						}
					}
					if (Constants.SMS.equals(mth)) {
						String smsCon = "您收到来自办公系统待办工作的消息：(工作标题：" + title + "，" + con + ")";
						smsService.sendSms("", users, smsCon,
								GlobalBaseData.SMSCODE_DCDB);
					}
					if (Constants.RTX.equals(mth)) {
						StringBuffer mess = new StringBuffer();
						mess.append("待办工作通知：");
						mess.append(title + " \n");
						mess.append(con + "\n");
						String rtxMess = mess.toString();
						rtxService.sendIM(rtxMess, userId,null);
					}
				}
				
			} catch (Exception e) {
				System.out.print(e);
				throw new ServiceException(MessagesConst.save_error,
						new Object[] { "待办工作提醒" });
			}
		}
		if(inputFrom!=null&&inputFrom.equals("1")){
			return renderHtml("<script> window.returnValue='OK';window.close(); </script>");
		}else{
			String drafsend= getRequest().getParameter("drafsend");
			if (!"1".equals(drafsend)) {
				return renderHtml("<script> alert('发送成功');window.location='"
						+ getRequest().getContextPath()
						+ "/workinspect/worksend/workSend!input.action"
						+ "'; </script>");
			}else{
				return renderHtml("<script> alert('发送成功');window.location='"
						+ getRequest().getContextPath()
						+ "/workinspect/worksend/workSend!getPersonalDraft.action"
						+ "'; </script>");
			}
		}
	}
	public String saveDraft() throws Exception {
		// 草稿标识
		model.setRest1("0");
	//	this.recvIds=null;
		if(null!=delAttIds&&!"".equals(delAttIds)){
//			delAttIds = delAttIds.substring(0,delAttIds.length()-1);
			String[] ids = delAttIds.split(",");
			for(int i=0;i<ids.length;i++){
				taskService.delAttach(ids[i]);
			}
		}
		taskService.saveDraft(this.model, this.recvIds, file, fileFileName);
		// addActionMessage("保存成功");
		// return INPUT;
//		if (this.model.getWorktaskId() != null
//				&& !"".equals(this.model.getWorktaskId())) {
//			return "init";
//		} else {
//			StringBuffer returnhtml = new StringBuffer("<script>");
//			returnhtml.append(" alert('操作成功'); window.location='").append(
//					getRequest().getContextPath()).append(
//					"/workinspect/worksend/workSend!input.action';</script>");
//			return renderHtml(returnhtml.toString());
//		}
		if(inputFrom!=null&&inputFrom.equals("1")){
			return renderHtml("<script> alert('成功保存至任务草稿');window.returnValue='OK';window.close(); </script>");
		}else{
			return renderHtml("<script> alert('成功保存至任务草稿');window.location='" + getRequest().getContextPath()
					+ "/workinspect/worksend/workSend!input.action"
					+ "'; </script>");
		}
	}
	public String viewDraft() throws Exception {
		this.selectedData = "";
		model = this.taskService.getTaskById(taskId);
		User u = userService.getCurrentUser();
		model.setWorktaskUserName(u.getUserName());
		model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(u
				.getOrgId()));
		Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
		recvIds = "";
		recvNames = "";
		if(sends != null && !sends.isEmpty()){
			//getTaskRecvTime
			List<TOsWorktaskSend> sendss = new ArrayList<TOsWorktaskSend>(sends);
			Collections.sort(sendss, new Comparator<TOsWorktaskSend>(){

				public int compare(TOsWorktaskSend o1, TOsWorktaskSend o2) {
					// TODO Auto-generated method stub
					return o1.getTaskSendTime().compareTo(o2.getTaskSendTime()) ;
				}
				
			}
			);
			for (TOsWorktaskSend sender : sendss) {
				TUumsBaseUser recvU = userService.getUserInfoByUserId(sender.getTaskRecvId());
				/*4|402882043e4e5c2b013e4e6cec360277
				 * ,4|402882043e4e5c2b013e4e6cec270275
				 * ,4|402882043e4e5c2b013e4e6cec270273
				 * */
				recvIds += (",4|"+recvU.getUserId());
				/*肖久光,熊 斌,应 勇*/
				recvNames += (","+recvU.getUserName());
				/*u402882043e4e5c2b013e4e6cec270273$297eb7473e4ab390013e4ab485fa0003,应  勇
				|u402882043e4e5c2b013e4e6cec270275$297eb7473e4ab390013e4ab485fa0003,熊  斌*/
				selectedData += "|u"+recvU.getUserId()+"$" + recvU.getOrgId()+","+recvU.getUserName() ;
			}
			if (!"".equals(recvIds)) {
				recvIds = recvIds.substring(1, recvIds.length());
				recvNames = recvNames.substring(1, recvNames.length());
				selectedData = selectedData.substring(1,
						selectedData.length());
			}
		}
//			if (!"".equals(recvIds)&&recvIds==null) {
//				recvIds = recvIds.substring(0, recvIds.length() - 1);
//				recvNames = recvNames.substring(0, recvNames.length() - 1);
//				selectedData = selectedData.substring(0,
//						selectedData.length() - 1);
//			}

			// 是否有附件
			attachList = this.taskService.getAttachListByTaskId(taskId);

		return "viewDraft";
	}
	/**
	 * public String assignUserList() throws Exception { // selectedData =
	 * this.taskService.getRecvDataByTaskId(this.taskId); if (selectedData !=
	 * null && !"".equals(selectedData)) { String[] arr =
	 * selectedData.split(","); selectedData = ""; for (int i = 0; i <
	 * arr.length; i++) { String[] arr2 = arr[i].split("\\|"); if
	 * ("1".equals(arr2[0])) { // 单位 TUumsBaseOrg org = this.uumsService
	 * .getOrgInfoByOrgId(arr2[1]); if (org != null) { selectedData += arr[i] +
	 * "|" + org.getOrgName() + ","; } } else { // 个人 TUumsBaseUser u =
	 * this.uumsService .getUserInfoByUserId(arr2[1]); if (u != null) {
	 * selectedData += arr[i] + "|" + u.getUserName() + ","; } } } }
	 * 
	 * return "assignUserList"; }
	 */
	public String view() throws Exception {
		this.selectedData = "";
		model = this.taskService.getTaskById(taskId);
		if (model != null) {

			Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
			TUumsBaseUser u = userService.getUserInfoByUserId(model
					.getWorktaskUser());
			model.setWorktaskUserName(u.getUserName());
			model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(model
					.getWorktaskUnit()));
			workSendList = new ArrayList<TOsWorktaskSend>(sends);
			taskTypeMap.put("1", "个人办理");
			taskTypeMap.put("0", "部门办理");
			taskStateMap.put("0", "<font color=red>待签收</font>");
			taskStateMap.put("1", "待办");
			taskStateMap.put("2", "办结");
			recvIds = "";
			recvNames = "";

			HttpServletRequest request = this.getRequest();
			String frameroot = (String) request.getSession().getAttribute(
					"frameroot");
			if (frameroot == null || frameroot.equals("")
					|| frameroot.equals("null"))
				frameroot = "/frame/theme_gray";
			frameroot = request.getContextPath() + frameroot;
			String mobileImg = "<img src='" + frameroot
					+ "/images/mobile.gif'>";
			for (TOsWorktaskSend send : sends) {
				send.setRestMobileImg(mobileImg);
				if ("1".equals(send.getTaskType())) {
					// 个人承办
					TUumsBaseUser recvU = userService.getUserInfoByUserId(send
							.getTaskRecvId());
					recvIds += recvU.getUserId() + ",";
					recvNames += recvU.getUserName() + ",";
					send.setTaskRecvName(recvU.getUserName());
					send.setTaskRecvOrgName(this.taskService
							.getRecvOrgNamePath(send.getTaskRecvOrgid()));
					this.selectedData += "4|" + recvU.getUserId() + ",";
				} else {
					// 单位承办
					TUumsBaseOrg org = userService.getOrgInfoByOrgId(send
							.getTaskRecvId());
					recvIds += org.getOrgId() + ",";
					recvNames += org.getOrgName() + ",";
					send.setTaskRecvName(org.getOrgName());
					send.setTaskRecvOrgName(this.taskService
							.getRecvOrgNamePath(org.getOrgId()));
					this.selectedData += "1|" + org.getOrgId() + ",";
				}
			}
			if (!"".equals(recvIds)) {
				recvIds = recvIds.substring(0, recvIds.length() - 1);
				recvNames = recvNames.substring(0, recvNames.length() - 1);
				selectedData = selectedData.substring(0,
						selectedData.length() - 1);
			}

			// 是否有附件
			attachList = this.taskService.getAttachListByTaskId(taskId);
		}

		return "view";
	}
	/**
	 * 获得任务办理状态
	 * @author niwy
	 * @date 2013年7月18日19:11:32
	 * @return
	 * @throws SystemException 
	 * @throws ServiceException 
	 */
	public String  getworkSate() throws ServiceException, SystemException{
		boolean state= false;//默认允许修改
	    TOsWorktask task = new TOsWorktask();
		task = this.taskService.getTaskById(taskId);
		if (task != null) {
			Set<TOsWorktaskSend> sends = task.getTOsWorktaskSends();
			for(TOsWorktaskSend s:sends){
				if(!"0".equals(s.getTaskState())){
					state = true;
					break;
				}
			}
		}
		return this.renderText(state?"1":"0"); //“1”表示已签收，"0"是表示未签收
	}
	

	public String addRecver() throws Exception {
		this.taskService.addRecver(this.taskId, this.recvIds);
		return renderHtml("<script>location='" + getRequest().getContextPath()
				+ "/workinspect/worksend/workSend!view.action?taskId=" + taskId
				+ "'; </script>");
	}

	public String deleteRecver() throws Exception {
		this.taskService.deleteRecver(this.recvIds);
		return renderHtml("<script>location='" + getRequest().getContextPath()
				+ "/workinspect/worksend/workSend!view.action?taskId=" + taskId
				+ "'; </script>");
	}

	/*
	 * 承办者是否可删除
	 */
	public String getRecverState() {
		return null;
	}

	public String summary() throws Exception {
		model = this.taskService.getTaskById(taskId);
		Set<TOsWorktaskSend> sends = model.getTOsWorktaskSends();
		TUumsBaseUser u = userService.getUserInfoByUserId(model
				.getWorktaskUser());
		model.setWorktaskUserName(u.getUserName());
		model.setWorktaskUnitName(this.taskService.getRecvOrgNamePath(model
				.getWorktaskUnit()));
		workSendList = new ArrayList<TOsWorktaskSend>(sends);

		taskTypeMap.put("1", "个人");
		taskTypeMap.put("0", "部门");
		taskStateMap.put("0", "<font color=red>待签收</font>");
		taskStateMap.put("1", "待办");
		taskStateMap.put("2", "办结");

		StringBuffer recvName = new StringBuffer("");
		int n = 0;
		for (TOsWorktaskSend sender : sends) {
			if (++n > 1) {
				recvName.append("<span style='padding-left:10px;'>");
			} else {
				recvName.append("<span>");
			}
			String recvId = sender.getTaskRecvId();
			String recvState = sender.getTaskState();
			String recvStateF = "";
			if ("0".equals(recvState)) {
				recvStateF = "(<font color='red'>待签收</font>)";
			} else if ("1".equals(recvState)) {
				recvStateF = "(办理中)";
			} else if ("2".equals(recvState)) {
				recvStateF = "(已办结)";
			}
			String recvType = sender.getTaskType();
			if ("1".equals(recvType)) {
				// 个人承办
				TUumsBaseUser recvU = userService.getUserInfoByUserId(recvId);
				TUumsBaseOrg org = userService.getOrgInfoByOrgId(recvU
						.getOrgId());
				recvName.append(org.getOrgName()).append("/").append(
						recvU.getUserName()).append("/").append(recvStateF)
						.append("</span><br/>");
				sender.setTaskRecvName(recvU.getUserName());
			} else {
				// 单位承办
				TUumsBaseOrg org = userService.getOrgInfoByOrgId(recvId);
				recvName.append(org.getOrgName()).append("/")
						.append(recvStateF).append("</span><br/>");
				sender.setTaskRecvName(org.getOrgName());
			}
		}
		model.setWorktaskSender(recvName.toString());
		
		//任务本身的附件
		workattachList = this.taskService.getAttachListByTaskId(taskId);

		// 是否有附件
		attachList = this.taskService.getTaskSummaryAttachListByTaskId(taskId);

		// 是否有纪要
		this.summaryList = this.taskService.getSummaryList(taskId);
		return "summary";
	}

	public void download() throws Exception {
		HttpServletResponse response = super.getResponse();
		TOsTaskAttach file = taskService.getAttachById(attachId);
		response.reset();
		response.setContentType("application/x-download"); // windows
		OutputStream output = null;
		int bytesum = 0;
		int byteread = 0;
		try {
			String url = file.getAttachFilePath();
			// URL url = new URL(u);
			// 截取文件地址以获得文件名
			// int count = u.lastIndexOf("/");
			// int countl = u.lastIndexOf(".");
			// String f_name = u.substring((count+1),(countl));
			// String l_name = u.substring((countl+1),u.length());
			// URLConnection conn = url.openConnection();
			// InputStream inStream = conn.getInputStream();
			// response.reset();//写文件头
			File fileObj = new File(url + File.separator + file.getAttachId()
					+ "." + file.getAttachFileType());
			FileInputStream inStream = new FileInputStream(fileObj);
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getAttachFileName().getBytes("gb2312"),
							"iso8859-1"));
			output = response.getOutputStream();
			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				output.write(buffer, 0, byteread);
			}
			inStream.close();
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				output = null;
			}
		}
	}

	public TOsWorktask getModel() {

		return model;
	}

	public Page<TOsWorktask> getPage() {
		return page;
	}

	public void setPage(Page<TOsWorktask> page) {
		this.page = page;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getRecvIds() {
		return recvIds;
	}

	public void setRecvIds(String recvIds) {
		this.recvIds = recvIds;
	}

	public String getRecvNames() {
		return recvNames;
	}

	public void setRecvNames(String recvNames) {
		this.recvNames = recvNames;
	}

	public List<TOsWorkType> getWorkTypeList() {
		return workTypeList;
	}

	public String getSelectTaskTitle() {
		return selectTaskTitle;
	}

	public String getSelectTaskNo() {
		return selectTaskNo;
	}

	public String getSelectTaskType() {
		return selectTaskType;
	}

	public void setSelectTaskTitle(String selectTaskTitle) {
		this.selectTaskTitle = selectTaskTitle;
	}

	public void setSelectTaskNo(String selectTaskNo) {
		this.selectTaskNo = selectTaskNo;
	}

	public void setSelectTaskType(String selectTaskType) {
		this.selectTaskType = selectTaskType;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<TOsWorktaskSend> getWorkSendList() {
		return workSendList;
	}

	public Map<String, String> getTaskTypeMap() {
		return taskTypeMap;
	}

	public Map<String, String> getTaskStateMap() {
		return taskStateMap;
	}

	public List<TOsTaskAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<TOsTaskAttach> attachList) {
		this.attachList = attachList;
	}

	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public TOsWorktaskSend getSend() {
		return send;
	}

	public void setSend(TOsWorktaskSend send) {
		this.send = send;
	}

	public String getSelectRecvOrgName() {
		return selectRecvOrgName;
	}

	public void setSelectRecvOrgName(String selectRecvOrgName) {
		this.selectRecvOrgName = selectRecvOrgName;
	}

	public Date getSelectTaskBSendTime() {
		return selectTaskBSendTime;
	}

	public void setSelectTaskBSendTime(Date selectTaskBSendTime) {
		this.selectTaskBSendTime = selectTaskBSendTime;
	}

	public Date getSelectTaskESendTime() {
		return selectTaskESendTime;
	}

	public void setSelectTaskESendTime(Date selectTaskESendTime) {
		this.selectTaskESendTime = selectTaskESendTime;
	}

	public String getSelectTaskState() {
		return selectTaskState;
	}

	public void setSelectTaskState(String selectTaskState) {
		this.selectTaskState = selectTaskState;
	}

	public Page<TOsWorktaskSend> getPageSend() {
		return pageSend;
	}

	public void setPageSend(Page<TOsWorktaskSend> pageSend) {
		this.pageSend = pageSend;
	}

	public String getSelectTaskTitle2() {
		return selectTaskTitle2;
	}

	public void setSelectTaskTitle2(String selectTaskTitle2) {
		this.selectTaskTitle2 = selectTaskTitle2;
	}

	public String getSendTaskId() {
		return sendTaskId;
	}

	public void setSendTaskId(String sendTaskId) {
		this.sendTaskId = sendTaskId;
	}

	public TOsWorkReviews getReview() {
		return review;
	}

	public void setReview(TOsWorkReviews review) {
		this.review = review;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public List<TOsManagementSummary> getSummaryList() {
		return summaryList;
	}

	public void setSummaryList(List<TOsManagementSummary> summaryList) {
		this.summaryList = summaryList;
	}

	public String getSelectedData() {
		return selectedData;
	}

	public void setSelectedData(String selectedData) {
		this.selectedData = selectedData;
	}

	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	public String getInputFrom() {
		return inputFrom;
	}

	public void setInputFrom(String inputFrom) {
		this.inputFrom = inputFrom;
	}

	public String getDelAttIds() {
		return delAttIds;
	}

	public void setDelAttIds(String delAttIds) {
		this.delAttIds = delAttIds;
	}
	
	public List<TOsTaskAttach> getWorkattachList() {
		return workattachList;
	}
	
	public void setWorkattachList(List<TOsTaskAttach> workattachList) {
		this.workattachList = workattachList;
	}

}
