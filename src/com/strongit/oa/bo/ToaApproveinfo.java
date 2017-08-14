package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_APPROVEINFO",catalog="",schema="")
public class ToaApproveinfo {

	private String aiId;
	
	private String aiBusinessId;
	
	private String aiControlName;
	
	private String aiContent;
	
	private String aiActorId;
	
	private String aiActor;
	
	private Date aiDate;

	private String atoPersonId;
	
	private String atoPersonName;
	
	private Date atDate;

	private String atAssignType;


	@Column(name = "AI_ACOTR")
	public String getAiActor() {
		return aiActor;
	}

	public void setAiActor(String aiActor) {
		this.aiActor = aiActor;
	}

	@Column(name="AI_ACTOR_ID")
	public String getAiActorId() {
		return aiActorId;
	}

	public void setAiActorId(String aiActorId) {
		this.aiActorId = aiActorId;
	}

	@Column(name = "AI_BI_ID")
	public String getAiBusinessId() {
		return aiBusinessId;
	}

	public void setAiBusinessId(String aiBusinessId) {
		this.aiBusinessId = aiBusinessId;
	}

	@Column(name = "AI_CONTENT")
	public String getAiContent() {
		return aiContent;
	}

	public void setAiContent(String aiContent) {
		this.aiContent = aiContent;
	}

	@Column(name = "AI_CONTRIL_NAME")
	public String getAiControlName() {
		return aiControlName;
	}

	public void setAiControlName(String aiControlName) {
		this.aiControlName = aiControlName;
	}

	@Column(name = "AI_DATE")
	public Date getAiDate() {
		return aiDate;
	}

	public void setAiDate(Date aiDate) {
		this.aiDate = aiDate;
	}

	@Id
    @Column(name="AI_ID")
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getAiId() {
		return aiId;
	}

	public void setAiId(String aiId) {
		this.aiId = aiId;
	}
	@Column(name = "AT_OPERSON_ID")
	public String getAtoPersonId() {
		return atoPersonId;
	}

	public void setAtoPersonId(String atoPersonId) {
		this.atoPersonId = atoPersonId;
	}
	@Column(name = "AT_OPERSON_NAME")
	public String getAtoPersonName() {
		return atoPersonName;
	}

	public void setAtoPersonName(String atoPersonName) {
		this.atoPersonName = atoPersonName;
	}
	@Column(name = "AT_DATE")
	public Date getAtDate() {
		return atDate;
	}

	public void setAtDate(Date atDate) {
		this.atDate = atDate;
	}
	@Column(name = "AT_ASSIGN_TYPE")
	public String getAtAssignType() {
		return atAssignType;
	}

	public void setAtAssignType(String atAssignType) {
		this.atAssignType = atAssignType;
	}
}
