package com.strongit.doc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_DOC_STAMP_LOG"
 * 
 */
@Entity
@Table(name = "T_DOC_STAMP_LOG", catalog = "", schema = "")
public class TdocStampLog implements Serializable {

	/** identifier field */
	private String stampLogId;

	/** nullable persistent field */
	private String docCode;

	/** nullable persistent field */
	private String departName;

	/** nullable persistent field */
	private String stampName;

	/** nullable persistent field */
	private String stampUser;

	/** nullable persistent field */
	private String stampType;

	/** nullable persistent field */
	private String operateUser;

	/** nullable persistent field */
	private Date operateTime;

	/** nullable persistent field */
	private String operateIp;

	/** full constructor */
	public TdocStampLog(String stampLogId, String docCode, String departName,
			String stampName, String stampUser, Date operateTime,
			String operateIp, String stampType, String operateUser) {
		this.stampLogId = stampLogId;
		this.docCode = docCode;
		this.departName = departName;
		this.stampName = stampName;
		this.stampUser = stampUser;
		this.operateTime = operateTime;
		this.operateIp = operateIp;
		this.stampType = stampType;
		this.operateUser = operateUser;
	}

	/** default constructor */
	public TdocStampLog() {
	}

	/** minimal constructor */
	public TdocStampLog(String stampLogId) {
		this.stampLogId = stampLogId;
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="STAMP_LOG_ID"
	 * 
	 */
	@Id
	@Column(name = "STAMP_LOG_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getStampLogId() {
		return this.stampLogId;
	}

	public void setStampLogId(String stampLogId) {
		this.stampLogId = stampLogId;
	}

	/**
	 * @hibernate.property column="DOC_CODE" length="50"
	 * 
	 */
	@Column(name = "DOC_CODE")
	public String getDocCode() {
		return this.docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	/**
	 * @hibernate.property column="DEPART_NAME" length="1000"
	 * 
	 */
	@Column(name = "DEPART_NAME")
	public String getDepartName() {
		return this.departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	/**
	 * @hibernate.property column="STAMP_NAME" length="100"
	 * 
	 */
	@Column(name = "STAMP_NAME")
	public String getStampName() {
		return this.stampName;
	}

	public void setStampName(String stampName) {
		this.stampName = stampName;
	}

	/**
	 * @hibernate.property column="STAMP_USER" length="200"
	 * 
	 */
	@Column(name = "STAMP_USER")
	public String getStampUser() {
		return this.stampUser;
	}

	public void setStampUser(String stampUser) {
		this.stampUser = stampUser;
	}

	/**
	 * @hibernate.property column="STAMP_TYPE" length="50"
	 * 
	 */
	@Column(name = "STAMP_TYPE")
	public String getStampType() {
		return this.stampType;
	}

	public void setStampType(String stampType) {
		this.stampType = stampType;
	}

	/**
	 * @hibernate.property column="OPERATE_USER" length="200"
	 * 
	 */
	@Column(name = "OPERATE_USER")
	public String getOperateUser() {
		return this.operateUser;
	}

	public void setOperateUser(String operateUser) {
		this.operateUser = operateUser;
	}

	/**
	 * @hibernate.property column="OPERATE_TIME" length="7"
	 * 
	 */
	@Column(name = "OPERATE_TIME")
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	/**
	 * @hibernate.property column="OPERATE_IP" length="15"
	 * 
	 */
	@Column(name = "OPERATE_IP")
	public String getOperateIp() {
		return this.operateIp;
	}

	public void setOperateIp(String operateIp) {
		this.operateIp = operateIp;
	}

	public String toString() {
		return new ToStringBuilder(this).append("stampLogId", getStampLogId())
				.toString();
	}

}
