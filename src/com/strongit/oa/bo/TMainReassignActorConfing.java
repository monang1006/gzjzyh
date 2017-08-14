package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_MAINA_REASSIGN_CORCONFING")
public class TMainReassignActorConfing {
	private String id;

	private String processInstanceId;

	private String actorId;

	@Id
	@Column(name = "ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PROCESSINSTANCE_ID", nullable = true)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "ACTORID", nullable = true)
	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public TMainReassignActorConfing(String processInstanceId, String actorId) {
		this.processInstanceId = processInstanceId;
		this.actorId = actorId;
	}

	public TMainReassignActorConfing() {
	}
}