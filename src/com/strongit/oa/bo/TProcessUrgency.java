package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 催办记录bean
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 18, 2012 11:36:15 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.bo.TProcessUrgency
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "T_PROCESS_URGENCY")
public class TProcessUrgency {
	/**
	 * @field id 主键
	 */
	private String id;

	/**
	 * @field processInstanceId 流程实例id
	 */
	private long processInstanceId;

	/**
	 * @field remindType 催办方式
	 */
	private String remindType;

	/**
	 * @field handlerMes 催办内容
	 */
	private String handlerMes;

	/**
	 * @field urgencyerId 催办者用户id
	 */
	private String urgencyerId;

	/**
	 * @field urgencyederId 被催办者用户id
	 */
	private String urgencyederId;

	/**
	 * @field urgencyDate 催办时间
	 */
	private Date urgencyDate;

	@Column(name = "HANDLERMES", nullable = true)
	public String getHandlerMes() {
		return handlerMes;
	}

	public void setHandlerMes(String handlerMes) {
		this.handlerMes = handlerMes;
	}

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

	@Column(name = "PROCESSINSTANCEID", nullable = true)
	public long getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "REMINDTYPE", nullable = true)
	public String getRemindType() {
		return remindType;
	}

	public void setRemindType(String remindType) {
		this.remindType = remindType;
	}

	@Column(name = "URGENCYDATE", nullable = true)
	public Date getUrgencyDate() {
		return urgencyDate;
	}

	public void setUrgencyDate(Date urgencyDate) {
		this.urgencyDate = urgencyDate;
	}

	@Column(name = "URGENCYEDERID", nullable = true)
	public String getUrgencyederId() {
		return urgencyederId;
	}

	public void setUrgencyederId(String urgencyederId) {
		this.urgencyederId = urgencyederId;
	}

	@Column(name = "URGENCYERID", nullable = true)
	public String getUrgencyerId() {
		return urgencyerId;
	}

	public void setUrgencyerId(String urgencyerId) {
		this.urgencyerId = urgencyerId;
	}

	public TProcessUrgency() {
	}

	public TProcessUrgency(long processInstanceId, String remindType,
			String handlerMes, String urgencyerId, String urgencyederId,
			Date urgencyDate) {
		this.processInstanceId = processInstanceId;
		this.remindType = remindType;
		this.handlerMes = handlerMes;
		this.urgencyerId = urgencyerId;
		this.urgencyederId = urgencyederId;
		this.urgencyDate = urgencyDate;
	}

	public TProcessUrgency(String id, long processInstanceId,
			String remindType, String handlerMes, String urgencyerId,
			String urgencyederId, Date urgencyDate) {
		this.id = id;
		this.processInstanceId = processInstanceId;
		this.remindType = remindType;
		this.handlerMes = handlerMes;
		this.urgencyerId = urgencyerId;
		this.urgencyederId = urgencyederId;
		this.urgencyDate = urgencyDate;
	}
	
}
