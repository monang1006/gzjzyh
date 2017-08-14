package com.strongit.oa.bo;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the T_OA_FREEDOM_WORKFLOW database table.
 * 
 */
@Entity
@Table(name="T_OA_FREEDOM_WORKFLOW")
public class TOaFreedomWorkflow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FW_ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	private String fwId;

	@Column(name="FW_BUSINESS_ID")
	private String fwBusinessId;

	@Column(name="FW_BUSINESS_NAME")
	private String fwBusinessName;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FW_CREAT_TIME")
	private Date fwCreatTime;

	@Column(name="FW_CREATOR")
	private String fwCreator;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="FW_END_TIME")
	private Date fwEndTime;

	@Column(name="FW_FORM_ID")
	private String fwFormId;

	@Column(name="FW_MEMO")
	private String fwMemo;

	@Column(name="FW_STATUS")
	private String fwStatus;

	@Column(name="FW_TITLE")
	private String fwTitle;

	@Column(name="FW_FORM_BIZ_TABLE")
	private String fwFormBizTable;

	@Column(name="FW_FORM_BIZ_PK")
	private String fwFormBizPk;

	@Column(name="FW_FORM_BIZ_ID")
	private String fwFormBizId;

	//bi-directional many-to-one association to TOaFreedomWorkflowTask
	@OneToMany(mappedBy="TOaFreedomWorkflow")
	private Set<TOaFreedomWorkflowTask> TOaFreedomWorkflowTasks;

    public TOaFreedomWorkflow() {
    }

	public String getFwId() {
		return this.fwId;
	}

	public void setFwId(String fwId) {
		this.fwId = fwId;
	}

	public String getFwBusinessId() {
		return this.fwBusinessId;
	}

	public void setFwBusinessId(String fwBusinessId) {
		this.fwBusinessId = fwBusinessId;
	}

	public String getFwBusinessName() {
		return this.fwBusinessName;
	}

	public void setFwBusinessName(String fwBusinessName) {
		this.fwBusinessName = fwBusinessName;
	}

	public Date getFwCreatTime() {
		return this.fwCreatTime;
	}

	public void setFwCreatTime(Date fwCreatTime) {
		this.fwCreatTime = fwCreatTime;
	}

	public String getFwCreator() {
		return this.fwCreator;
	}

	public void setFwCreator(String fwCreator) {
		this.fwCreator = fwCreator;
	}

	public Date getFwEndTime() {
		return this.fwEndTime;
	}

	public void setFwEndTime(Date fwEndTime) {
		this.fwEndTime = fwEndTime;
	}

	public String getFwFormId() {
		return this.fwFormId;
	}

	public void setFwFormId(String fwFormId) {
		this.fwFormId = fwFormId;
	}

	public String getFwMemo() {
		return this.fwMemo;
	}

	public void setFwMemo(String fwMemo) {
		this.fwMemo = fwMemo;
	}

	public String getFwStatus() {
		return this.fwStatus;
	}

	public void setFwStatus(String fwStatus) {
		this.fwStatus = fwStatus;
	}

	public String getFwTitle() {
		return this.fwTitle;
	}

	public void setFwTitle(String fwTitle) {
		this.fwTitle = fwTitle;
	}

	public Set<TOaFreedomWorkflowTask> getTOaFreedomWorkflowTasks() {
		return this.TOaFreedomWorkflowTasks;
	}

	public void setTOaFreedomWorkflowTasks(Set<TOaFreedomWorkflowTask> TOaFreedomWorkflowTasks) {
		this.TOaFreedomWorkflowTasks = TOaFreedomWorkflowTasks;
	}

	public String getFwFormBizTable()
	{
		return fwFormBizTable;
	}

	public void setFwFormBizTable(String fwFormBizTable)
	{
		this.fwFormBizTable = fwFormBizTable;
	}

	public String getFwFormBizPk()
	{
		return fwFormBizPk;
	}

	public void setFwFormBizPk(String fwFormBizPk)
	{
		this.fwFormBizPk = fwFormBizPk;
	}

	public String getFwFormBizId()
	{
		return fwFormBizId;
	}

	public void setFwFormBizId(String fwFormBizId)
	{
		this.fwFormBizId = fwFormBizId;
	}
	
}