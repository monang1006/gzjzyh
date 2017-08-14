package com.strongit.workflow.workflowDelegation.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.strongit.oa.common.remind.RemindManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongit.workflow.bo.ToaInfoDelegation;
import com.strongit.workflow.po.UserInfo;
import com.strongit.workflow.uupInterface.UupUtil;
import com.strongit.workflow.workflowInterface.IProcessDefinitionService;
import com.strongit.workflow.workflowInterface.ITaskService;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * @author       喻斌
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 15, 2008  6:09:54 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowDelegation.action.ProcessTypeAction
 * @comment      流程代理处理Action
 */
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "processDelegation.action", type = ServletActionRedirectResult.class),
			@Result(name = "defaultReturn", value = "/WEB-INF/jsp/workflowDelegation/defaultpage/defaultPage.jsp", type = ServletDispatcherResult.class),
			@Result(name = "view", value = "/WEB-INF/jsp/workflowDelegation/action/processDelegation-view.jsp", type = ServletDispatcherResult.class),
			@Result(name = "user", value = "/WEB-INF/jsp/workflowDelegation/action/processDelegation-user.jsp", type = ServletDispatcherResult.class),
			@Result(name = "process", value = "/WEB-INF/jsp/workflowDelegation/action/processDelegation-process.jsp", type = ServletDispatcherResult.class)})
public class ProcessDelegationAction extends BaseActionSupport<ToaInfoDelegation> {
	private Page<ToaInfoDelegation> page = new Page<ToaInfoDelegation>(FlexTableTag.MAX_ROWS,
			true);

	private String deId;
	//input 标识 ：1.添加 2.查看
	private String type;
	
	private String dhDeleProcess;
	
	private String deStartDate;
	
	private String deEndDate;
	
	private List userList;
	
	private List processList;

	private ToaInfoDelegation model = new ToaInfoDelegation();

	private IProcessDefinitionService manager;
	
	private UupUtil uupBroker;
	
	private UserInfo userInfo;
	
	@Autowired RemindManager remindService ;//注入提醒服务类
	@Autowired
	private ITaskService taskService;
	
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	@Autowired
	public void setUupBroker(UupUtil uupBroker){
		this.uupBroker = uupBroker;
	}

	public void setModel(ToaInfoDelegation model) {
		this.model = model;
	}

	public void setPage(Page<ToaInfoDelegation> page) {
		this.page = page;
	}

	/**
	 * @roseuid 493692F5002E
	 */
	public ProcessDelegationAction() {

	}

	/**
	 * Access method for the page property.
	 * 
	 * @return the current value of the page property
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Access method for the model property.
	 * 
	 * @return the current value of the model property
	 */
	public ToaInfoDelegation getModel() {
		return model;
	}

	/**
	 * Sets the value of the manager property.
	 * 
	 * @param aManager
	 *            the new value of the manager property
	 */
	@Autowired
	public void setManager(IProcessDefinitionService aManager) {
		manager = aManager;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936902101B5
	 * 
	 * 邓志城 2010年6月30日16:45:20
	 * 调整为只查询未超时的任务.
	 */
	@Override
	public String list() {
		//page = manager.getDelegationList(page);
		UserInfo userInfo = (UserInfo) uupBroker.getCurrentUserInfo();
		page = manager.getDelegationList(page, userInfo.getUserId(), null, null, null, null, null, null, "0");
		return SUCCESS;
	}

	/**
	 * @roseuid 49369077005D
	 */
	@Override
	public void prepareModel() {
		if (deId != null && !"".equals(deId)) {
			model = manager.getDelegationInfoById(deId);
		} else {
			model = new ToaInfoDelegation();
		}
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936910B034B
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String save() throws Exception {
		if (new Long(0).equals(model.getDeId())) {
			model.setDeId(null);
		}
		UserInfo userInfo = (UserInfo) uupBroker.getCurrentUserInfo();
		/**
		 * 将businessId设置成String
		 */
		model.setDeUserId(userInfo.getUserId());
		if(model.getDeIsStart()==null&&model.getDeIsStart().equals("")){
			model.setDeIsStart("0");
		}
		model.setDeIsOuttime("0");
		
		String[] processes = this.dhDeleProcess.split("\\|");
		
		manager.addDelegationDetail(model, processes);
		/**
		 * 立即生效
		 * 解决勾选立即生效后当前用户的待办事宜没有一起到委托人的待办事宜列表
		 */
	    taskService.assignTasks(model.getDeUserId(), 
	          "all", String.valueOf(model.getDeId()));
		
		ActionContext cxt = ActionContext.getContext();
		cxt.getSession().put("remindType", type);
		StringBuilder processName = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(processes != null){
			for(int i=0;i<processes.length;i++){
				String[] processInfo = processes[i].split(",");
				processName.append(processInfo[1]).append(",");
			}
			if(processName.length() > 0){
				processName.deleteCharAt(processName.length() - 1);
			}
		}
		if("1".equals(model.getDeRest1())){	//永不过期
			cxt.getSession().put("handlerMes",new StringBuilder(userInfo.getUserName())
						.append("将流程《").append(processName.toString())
						.append("》委派给您办理").toString());
		}else{
			cxt.getSession().put("handlerMes", getText(GlobalBaseData.WORKFLOW_ASSIGN_REMIND, 
					new String[]{userInfo.getUserName(),
					processName.toString(),
					model.getDeStartDate() == null ? "" : sdf.format(model.getDeStartDate()),
							model.getDeEndDate() == null ? "" : sdf.format(model.getDeEndDate())}));
		}
		cxt.getSession().put("moduleType", "");//调用消息模块的类型
		List<String> list = new ArrayList<String>();
		list.add(model.getDeDeleId());
		remindService.sendRemind(list,null);//发送提醒
		addActionMessage("信息保存成功");
		return "defaultReturn";
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911402EE
	 */
	@Override
	public String delete() throws Exception {
		try {
			String ids = getRequest().getParameter("ids");
			manager.cancelDelegation(ids);
			addActionMessage("信息删除成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addActionMessage(e.getMessage());
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return RELOAD;
	}

	/**
	 * @return java.lang.String
	 * @roseuid 4936911F033C
	 */
	@Override
	public String input() {
		//取当前登录用户信息
		userInfo = (UserInfo) uupBroker.getCurrentUserInfo();
		if("add".equals(type)){
			return INPUT;
		}else{
			return "view";
		}
	}

	/**
	 * 校验当前用户要委派的任务是否别人已经委派给当前用户了.
	 * @author:邓志城
	 * @date:2010-7-1 上午09:16:10
	 * @return
	 * <p>
	 * 	1）0:未找到委派记录
	 * 	2）-1：出现异常
	 * 	3）描述信息
	 * </p>
	 * @throws Exception
	 */
	public String checkTaskIsDelegated() throws Exception {
		String ret = "0";
		try{
			String[] processes = this.dhDeleProcess.split("\\|");//得到设置的所有代理事项
			userInfo = (UserInfo) uupBroker.getCurrentUserInfo();
			for(String process : processes){
				String[] info = process.split(",");
				String id = info[0];
				String name = info[1];
				//查询委派人和委派的流程名称
				StringBuilder hql = new StringBuilder("select t2.deUserId, t.ddProName from ToaInfoDeledetail t,ToaInfoDelegation t2 where t.toaInfoDelegation.deId=t2.deId ");
				hql.append(" and t2.deDeleId=?");//代理人是当前用户
				hql.append(" and t2.deIsOuttime='0'");//未超时的任务
				hql.append(" and t.ddProId=?");//流程id
				List result = manager.getDataByHql(hql.toString(), new Object[]{userInfo.getUserId(),new Long(id)});
				if(result != null && result.size() > 0){//找到记录
					Object[] objs = (Object[])result.get(0);
					String userName = uupBroker.getUsernameById(objs[0].toString());
					String message = this.getText(GlobalBaseData.WORKFLOW_RESSIGN_FINDRECORD, new String[]{userName,name});
					return this.renderText(message);
				}
				
			}
		}catch(Exception e){
			logger.error("校验当前用户要委派的任务是否别人已经委派给当前用户了", e);
			ret = "-1";
		}
		return this.renderText(ret);
	}
	
	/**
	 * 设置代理人
	 * @author  喻斌
	 * @date    Dec 15, 2008  7:23:56 PM
	 * @return
	 * @throws Exception
	 */
	public String user() throws Exception{
		userList = uupBroker.getAllOrgUserList();
		return "user";
	}
	
	/**
	 * 设置代理流程
	 * @author  喻斌
	 * @date    Dec 15, 2008  7:25:42 PM
	 * @return
	 * @throws Exception
	 */
	public String process() throws Exception{
		processList = manager.getSelectProcessList();
		return "process";
	}

	public String getDeId() {
		return deId;
	}

	public void setDeId(String deId) {
		this.deId = deId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List getProcessList() {
		return processList;
	}

	public void setProcessList(List processList) {
		this.processList = processList;
	}

	public List getUserList() {
		return userList;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public String getDhDeleProcess() {
		return dhDeleProcess;
	}

	public void setDhDeleProcess(String dhDeleProcess) {
		this.dhDeleProcess = dhDeleProcess;
	}

	public String getDeEndDate() {
		return deEndDate;
	}

	public void setDeEndDate(String deEndDate) {
		this.deEndDate = deEndDate;
	}

	public String getDeStartDate() {
		return deStartDate;
	}

	public void setDeStartDate(String deStartDate) {
		this.deStartDate = deStartDate;
	}
}