package com.strongit.oa.workinspect.worksend;

import java.util.Date;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.bo.TOsWorktask;
import com.strongit.oa.bo.TOsWorktaskSend;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

@ParentPackage("default")
@Results( { @Result(name = "org", value = "/WEB-INF/jsp/workinspect/worksend/workSend-org.jsp", type = ServletDispatcherResult.class) })
public class WorkSenderAction extends BaseActionSupport<TOsWorktaskSend> {

	private Page<TOsWorktaskSend> page = new Page<TOsWorktaskSend>(20, true);
	private TOsWorktaskSend model = new TOsWorktaskSend();
	// 查询字段
	private String selectTaskTitle2;// 工作标题
	private String selectRecvOrgName;// 承办单位
	private Date selectTaskBSendTime;// 发送开始时间
	private Date selectTaskESendTime;// 发送结束时间
	private String selectTaskState;// 状态(未签收=0，待办=1，办结=2)

	@Autowired
	private IWorkSendService taskService;

	@Override
	public String delete() throws Exception {

		return null;
	}

	@Override
	public String input() throws Exception {

		return null;
	}

	@Override
	public String list() throws Exception {

		return null;
	}

	public String getOrg() throws Exception {
		TOsWorktask task = new TOsWorktask();
		task.setWorktaskTitle(selectTaskTitle2);
		model.setTOsWorktask(task);
		model.setTaskRecvOrgName(selectRecvOrgName);
		model.setTaskSendTime(selectTaskBSendTime);
		model.setTaskRecvTime(selectTaskESendTime);
		page = this.taskService.getSendList(page, model, selectTaskState);
		return "org";
	}
	
	@Override
	protected void prepareModel() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}

	public TOsWorktaskSend getModel() {

		return model;
	}

	public Page<TOsWorktaskSend> getPage() {
		return page;
	}

	public void setPage(Page<TOsWorktaskSend> page) {
		this.page = page;
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

	public String getSelectTaskTitle2() {
		return selectTaskTitle2;
	}

	public void setSelectTaskTitle2(String selectTaskTitle2) {
		this.selectTaskTitle2 = selectTaskTitle2;
	}
}
