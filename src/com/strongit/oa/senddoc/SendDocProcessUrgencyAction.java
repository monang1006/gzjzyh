package com.strongit.oa.senddoc;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TProcessUrgency;
import com.strongit.oa.common.business.processurgency.ProcessUrgencyManager;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.remind.Constants;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.senddoc.bo.TOAProcessUrgency;
import com.strongit.oa.util.StringUtil;
import com.strongit.workflow.util.WorkflowConst;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * 催办记录action
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 18, 2012 2:06:10 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SenddocProcessUrgencyAction
 */
public class SendDocProcessUrgencyAction extends BaseActionSupport {

	/**
	 * @field serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private TProcessUrgency processurgency;

	private @Autowired
	IWorkflowService workflowService;// 注入工作流服务类.

	private @Autowired
	ICustomUserService customUserService;// 注入工作流服务类.

	private @Autowired
	IUserService userService;// 注入工作流服务类.

	private @Autowired
	ProcessUrgencyManager processUrgencyManager;

	private List<TOAProcessUrgency> modelList;

	/**
	 * 判断流程是否结束【0:结束；1:未结束】
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime May 18, 2012 2:01:49 PM
	 */
	public String processIsEnd() {
		int isEnd = 0;
		if (processurgency.getProcessInstanceId() != 0) {
			if (workflowService.getProcessInstanceById(
					processurgency.getProcessInstanceId() + "").getEnd() == null) {
				isEnd = 1;
			}
		}
		return this.renderText(isEnd + "");
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String list() throws Exception {
		// TODO Auto-generated method stub

		if (processurgency.getProcessInstanceId() != 0) {
			List<TProcessUrgency> processUrgencyList = processUrgencyManager
					.getProcessUrgencyListByPid(processurgency
							.getProcessInstanceId()
							+ "");
			SimpleDateFormat dateFm = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			List<String> userIds = new LinkedList<String>();
			for (TProcessUrgency tprocessurgency : processUrgencyList) {
				if (!userIds.contains(tprocessurgency.getUrgencyerId())) {
					userIds.add(tprocessurgency.getUrgencyerId());
				}
				if (tprocessurgency.getUrgencyederId() != null
						&& tprocessurgency.getUrgencyederId().length() != 0) {
					userIds.addAll(StringUtil
							.stringToStringList(tprocessurgency
									.getUrgencyederId()));
				}
			}
			Map<String, Object[]> userInfoMap = null;
			if (userIds != null && !userIds.isEmpty()) {
				userInfoMap = customUserService.getUserInfoMap(userIds);
			}
			for (TProcessUrgency tprocessurgency : processUrgencyList) {
				if (modelList == null) {
					modelList = new LinkedList<TOAProcessUrgency>();
				}
				TOAProcessUrgency toaprocessurgency = new TOAProcessUrgency();
				toaprocessurgency.setShowUrgencyDate(dateFm
						.format(tprocessurgency.getUrgencyDate()));
				if (userInfoMap != null && !userInfoMap.isEmpty()) {
					// 设置催办人姓名
					toaprocessurgency.setUrgencyerName(userInfoMap
							.get(tprocessurgency.getUrgencyerId())[1]
							.toString());
					// 设置被催办人姓名
					if (tprocessurgency.getUrgencyederId() != null
							&& tprocessurgency.getUrgencyederId().length() != 0) {
						List<String> userids = StringUtil
								.stringToStringList(tprocessurgency
										.getUrgencyederId());
						StringBuilder urgencyederName = new StringBuilder();
						for (String userid : userids) {
							urgencyederName.append(",").append(
									userInfoMap.get(userid)[1].toString());
						}
						urgencyederName.deleteCharAt(0);
						toaprocessurgency.setUrgencyederName(urgencyederName
								.toString());
					} else {
						toaprocessurgency.setUrgencyederName("");
					}
				} else {
					// 设置催办人姓名
					toaprocessurgency.setUrgencyerName(userService
							.getUserInfoByUserId(
									tprocessurgency.getUrgencyerId())
							.getUserName());
					// 设置被催办人姓名
					if (tprocessurgency.getUrgencyederId() != null
							&& tprocessurgency.getUrgencyederId().length() != 0) {
						List<String> userids = StringUtil
								.stringToStringList(tprocessurgency
										.getUrgencyederId());
						StringBuilder urgencyederName = new StringBuilder();
						for (String userid : userids) {
							urgencyederName.append(",").append(
									userService.getUserInfoByUserId(userid)
											.getUserName());
						}
						urgencyederName.deleteCharAt(0);
						toaprocessurgency.setUrgencyederName(urgencyederName
								.toString());
					} else {
						toaprocessurgency.setUrgencyederName("");
					}
				}
				toaprocessurgency
						.setHandlerMes(tprocessurgency.getHandlerMes() == null ? ""
								: tprocessurgency.getHandlerMes());
				String notice = tprocessurgency.getRemindType();
				System.out.println("sdas");
				if (notice != null) {
					StringBuffer remindType = new StringBuffer();
					if (notice.equalsIgnoreCase(WorkflowConst.WORKFLOW_NOTICE_TYPE_MAIL)) {// 邮件提醒
						remindType.append(",").append("邮件提醒");
					}
					if (notice
							.equalsIgnoreCase(WorkflowConst.WORKFLOW_NOTICE_TYPE_NOTICE)) {// 内部消息
						remindType.append(",").append("内部消息");
					}
					if (notice.equalsIgnoreCase(WorkflowConst.WORKFLOW_NOTICE_TYPE_RTX)) { // Rtx
						remindType.append(",").append("即时消息");
					}
					if (notice
							.equalsIgnoreCase(WorkflowConst.WORKFLOW_NOTICE_TYPE_MESSAGE)) {// 手机短信提醒
						remindType.append(",").append("手机短信");
					}
					if (notice.equalsIgnoreCase("SMS")) {// 手机短信提醒
						remindType.append(",").append("手机短信");
					}
					if(notice.equalsIgnoreCase(Constants.MSG)){
						remindType.append(",").append("即时消息");
					}
					if(notice.equalsIgnoreCase("RTX,SMS")){
						remindType.append(",").append("即时消息,手机短信");
					}
				
					if (remindType.length() == 0) {
						remindType.append("其他提醒方式");
					} else {
						remindType.deleteCharAt(0);
					}
					toaprocessurgency.setRemindType(remindType.toString());
				} else {
					toaprocessurgency.setRemindType("无");
				}
				modelList.add(toaprocessurgency);
			}
		}

		return SUCCESS;
	}

	@Override
	protected void prepareModel() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public TProcessUrgency getModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TOAProcessUrgency> getModelList() {
		return modelList;
	}

	public void setModelList(List<TOAProcessUrgency> modelList) {
		this.modelList = modelList;
	}

	public TProcessUrgency getProcessurgency() {
		return processurgency;
	}

	public void setProcessurgency(TProcessUrgency processurgency) {
		this.processurgency = processurgency;
	}

}
