package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 5, 2012 7:52:11 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bo.TMainActorConfing
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_MAINA_CORCONFING")
public class TMainActorConfing {
	private String id;

	private String processInstanceId;

	private String mainActorId;

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

	@Column(name = "MAIN_ACTORID", nullable = true)
	public String getMainActorId() {
		return mainActorId;
	}

	public void setMainActorId(String mainActorId) {
		this.mainActorId = mainActorId;
	}

	@Column(name = "PROCESSINSTANCE_ID", nullable = true)
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public TMainActorConfing(String processInstanceId, String mainActorId) {
		this.processInstanceId = processInstanceId;
		this.mainActorId = mainActorId;
	}

	public TMainActorConfing() {
	}

}