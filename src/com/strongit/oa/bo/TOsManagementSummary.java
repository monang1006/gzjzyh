package com.strongit.oa.bo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOsManagementSummary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OS_MANAGEMENT_SUMMARY")
public class TOsManagementSummary implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String summaryId;
	private TOsWorktaskSend TOsWorktaskSend;// 工作任务下发
	private int managetProgress;// 工作进度
	private Date managetTime;// 办理时间
	private String managetState;// 工作状态(处理中=0，完成=1)
	private String managetDemo;// 回复内容
	private String rest1;
	private String rest2;
	private String rest3;

	private List<TOsTaskAttach> attachList;

	// Constructors

	/** default constructor */
	public TOsManagementSummary() {
	}

	/** full constructor */
	public TOsManagementSummary(TOsWorktaskSend TOsWorktaskSend,
			int managetProgress, Date managetTime, String managetState,
			String managetDemo, String rest1, String rest2, String rest3) {
		this.TOsWorktaskSend = TOsWorktaskSend;
		this.managetProgress = managetProgress;
		this.managetTime = managetTime;
		this.managetState = managetState;
		this.managetDemo = managetDemo;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "SUMMARY_ID", unique = true, nullable = false, length = 32)
	public String getSummaryId() {
		return this.summaryId;
	}

	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDTASK_ID")
	public TOsWorktaskSend getTOsWorktaskSend() {
		return this.TOsWorktaskSend;
	}

	public void setTOsWorktaskSend(TOsWorktaskSend TOsWorktaskSend) {
		this.TOsWorktaskSend = TOsWorktaskSend;
	}

	@Column(name = "MANAGET_PROGRESS")
	public int getManagetProgress() {
		return this.managetProgress;
	}

	public void setManagetProgress(int managetProgress) {
		this.managetProgress = managetProgress;
	}

	@Column(name = "MANAGET_TIME", length = 7)
	public Date getManagetTime() {
		return this.managetTime;
	}

	public void setManagetTime(Date managetTime) {
		this.managetTime = managetTime;
	}

	@Column(name = "MANAGET_STATE", length = 4)
	public String getManagetState() {
		return this.managetState;
	}

	public void setManagetState(String managetState) {
		this.managetState = managetState;
	}

	@Column(name = "MANAGET_DEMO", length = 4000)
	public String getManagetDemo() {
		return this.managetDemo;
	}

	public void setManagetDemo(String managetDemo) {
		this.managetDemo = managetDemo;
	}

	@Column(name = "REST1", length = 32)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 32)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 32)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Transient
	public List<TOsTaskAttach> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<TOsTaskAttach> attachList) {
		this.attachList = attachList;
	}

}
