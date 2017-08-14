package com.strongit.oa.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOsWorktaskSend entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OS_WORKTASK_SEND")
public class TOsWorktaskSend implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String sendtaskId;
	private TOsWorktask tOsWorktask;// 工作任务
	private String taskRecvId;// 接收单位id/个人id
	private String taskRecvOrgid;// 接收单位id/接收人所在单位id
	private Date taskSendTime;// 发送时间
	private Date taskRecvTime;// 接收时间
	private String taskType;// 办理类型（个人=1，部门=0）
	private String taskRemark;// 备注
	private String taskState;// 状态(未签收=0，办理中=1，办结=2)
	private String operateIp;// 操作IP
	private String rest1;
	private String rest2;
	private String rest3;
	private String operateUserid;// 操作人员ID
	private Date taskcompleteTime;// 办结时间
	private Set<TOsManagementSummary> TOsManagementSummaries = new HashSet<TOsManagementSummary>(
			0);

	private String taskRecvName;// 接收单位名称/个人名称
	private String taskRecvOrgName;// 接收单位名称/接收人所在单位名称
	private String operateUserName;// 操作人员名称
	private String restImg;// 状态说明
	private String restNum;// 行号
	private String restMobileImg;// 手机短信图标

	// Constructors

	/** default constructor */
	public TOsWorktaskSend() {
	}

	/** full constructor */
	public TOsWorktaskSend(TOsWorktask tOsWorktask, String taskRecvId,
			String taskRecvOrgid, Date taskSendTime, Date taskRecvTime,
			String taskType, String taskRemark, String taskState,
			String operateIp, String rest1, String rest2, String rest3,
			String operateUserid, Date taskcompleteTime,
			Set<TOsManagementSummary> TOsManagementSummaries) {
		this.tOsWorktask = tOsWorktask;
		this.taskRecvId = taskRecvId;
		this.taskRecvOrgid = taskRecvOrgid;
		this.taskSendTime = taskSendTime;
		this.taskRecvTime = taskRecvTime;
		this.taskType = taskType;
		this.taskRemark = taskRemark;
		this.taskState = taskState;
		this.operateIp = operateIp;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
		this.operateUserid = operateUserid;
		this.taskcompleteTime = taskcompleteTime;
		this.TOsManagementSummaries = TOsManagementSummaries;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "SENDTASK_ID", unique = true, nullable = false, length = 32)
	public String getSendtaskId() {
		return this.sendtaskId;
	}

	public void setSendtaskId(String sendtaskId) {
		this.sendtaskId = sendtaskId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORKTASK_ID")
	public TOsWorktask getTOsWorktask() {
		return tOsWorktask;
	}

	public void setTOsWorktask(TOsWorktask tOsWorktask) {
		this.tOsWorktask = tOsWorktask;
	}

	@Column(name = "TASK_RECV_ID", length = 32)
	public String getTaskRecvId() {
		return this.taskRecvId;
	}

	public void setTaskRecvId(String taskRecvId) {
		this.taskRecvId = taskRecvId;
	}

	@Column(name = "TASK_RECV_ORGID", length = 32)
	public String getTaskRecvOrgid() {
		return this.taskRecvOrgid;
	}

	public void setTaskRecvOrgid(String taskRecvOrgid) {
		this.taskRecvOrgid = taskRecvOrgid;
	}

	@Column(name = "TASK_SEND_TIME", length = 7)
	public Date getTaskSendTime() {
		return this.taskSendTime;
	}

	public void setTaskSendTime(Date taskSendTime) {
		this.taskSendTime = taskSendTime;
	}

	@Column(name = "TASK_RECV_TIME", length = 7)
	public Date getTaskRecvTime() {
		return this.taskRecvTime;
	}

	public void setTaskRecvTime(Date taskRecvTime) {
		this.taskRecvTime = taskRecvTime;
	}

	@Column(name = "TASK_TYPE", length = 4)
	public String getTaskType() {
		return this.taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	@Column(name = "TASK_REMARK", length = 4000)
	public String getTaskRemark() {
		return this.taskRemark;
	}

	public void setTaskRemark(String taskRemark) {
		this.taskRemark = taskRemark;
	}

	@Column(name = "TASK_STATE", length = 2)
	public String getTaskState() {
		return this.taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	@Column(name = "OPERATE_IP", length = 15)
	public String getOperateIp() {
		return this.operateIp;
	}

	public void setOperateIp(String operateIp) {
		this.operateIp = operateIp;
	}

	@Column(name = "REST1", length = 104)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 1024)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 134)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "OPERATE_USERID", length = 34)
	public String getOperateUserid() {
		return this.operateUserid;
	}

	public void setOperateUserid(String operateUserid) {
		this.operateUserid = operateUserid;
	}

	@Column(name = "TASKCOMPLETE_TIME", length = 7)
	public Date getTaskcompleteTime() {
		return this.taskcompleteTime;
	}

	public void setTaskcompleteTime(Date taskcompleteTime) {
		this.taskcompleteTime = taskcompleteTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOsWorktaskSend")
	public Set<TOsManagementSummary> getTOsManagementSummaries() {
		return this.TOsManagementSummaries;
	}

	public void setTOsManagementSummaries(
			Set<TOsManagementSummary> TOsManagementSummaries) {
		this.TOsManagementSummaries = TOsManagementSummaries;
	}

	@Transient
	public String getTaskRecvName() {
		return taskRecvName;
	}

	public void setTaskRecvName(String taskRecvName) {
		this.taskRecvName = taskRecvName;
	}

	@Transient
	public String getTaskRecvOrgName() {
		return taskRecvOrgName;
	}

	public void setTaskRecvOrgName(String taskRecvOrgName) {
		this.taskRecvOrgName = taskRecvOrgName;
	}

	@Transient
	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}

	@Transient
	public String getRestImg() {
		return restImg;
	}

	public void setRestImg(String restImg) {
		this.restImg = restImg;
	}

	@Transient
	public String getRestNum() {
		return restNum;
	}

	public void setRestNum(String restNum) {
		this.restNum = restNum;
	}

	@Transient
	public String getRestMobileImg() {
		return restMobileImg;
	}

	public void setRestMobileImg(String restMobileImg) {
		this.restMobileImg = restMobileImg;
	}

}
