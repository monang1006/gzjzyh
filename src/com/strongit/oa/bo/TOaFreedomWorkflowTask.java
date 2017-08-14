package com.strongit.oa.bo;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;


/**
 * The persistent class for the T_OA_FREEDOM_WORKFLOW_TASK database table.
 * 
 */
@Entity
@Table(name="T_OA_FREEDOM_WORKFLOW_TASK")
public class TOaFreedomWorkflowTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FT_ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	private String ftId;

	@Column(name="FT_CREATOR")
	private String ftCreator;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FT_END_TIME")
	private Date ftEndTime;

	@Column(name="FT_HANDLER")
	private String ftHandler;

	@Column(name="FT_MEMO")
	private String ftMemo;

	@Column(name="FT_SEQUENCE")
	private Long ftSequence;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FT_START_TIME")
	private Date ftStartTime;

	@Column(name="FT_STATUS")
	private String ftStatus;

	@Column(name="FT_TITLE")
	private String ftTitle;

	//bi-directional many-to-one association to TOaFreedomWorkflow
    @ManyToOne
	@JoinColumn(name="FW_ID")
	private TOaFreedomWorkflow TOaFreedomWorkflow;

    public TOaFreedomWorkflowTask() {
    }

	public String getFtId() {
		return this.ftId;
	}

	public void setFtId(String ftId) {
		this.ftId = ftId;
	}

	public String getFtCreator() {
		return this.ftCreator;
	}

	public void setFtCreator(String ftCreator) {
		this.ftCreator = ftCreator;
	}

	public Date getFtEndTime() {
		return this.ftEndTime;
	}

	public void setFtEndTime(Date ftEndTime) {
		this.ftEndTime = ftEndTime;
	}

	public String getFtHandler() {
		return this.ftHandler;
	}

	public void setFtHandler(String ftHandler) {
		this.ftHandler = ftHandler;
	}

	public String getFtMemo() {
		return this.ftMemo;
	}

	public void setFtMemo(String ftMemo) {
		this.ftMemo = ftMemo;
	}

	public Date getFtStartTime() {
		return this.ftStartTime;
	}

	public void setFtStartTime(Date ftStartTime) {
		this.ftStartTime = ftStartTime;
	}

	public String getFtStatus() {
		return this.ftStatus;
	}

	public void setFtStatus(String ftStatus) {
		this.ftStatus = ftStatus;
	}

	public String getFtTitle() {
		return this.ftTitle;
	}

	public void setFtTitle(String ftTitle) {
		this.ftTitle = ftTitle;
	}

	public TOaFreedomWorkflow getTOaFreedomWorkflow() {
		return this.TOaFreedomWorkflow;
	}

	public void setTOaFreedomWorkflow(TOaFreedomWorkflow TOaFreedomWorkflow) {
		this.TOaFreedomWorkflow = TOaFreedomWorkflow;
	}

	public Long getFtSequence()
	{
		return ftSequence;
	}

	public void setFtSequence(Long ftSequence)
	{
		this.ftSequence = ftSequence;
	}
	
}