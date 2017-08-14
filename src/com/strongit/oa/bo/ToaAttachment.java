package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_ATTACHMENT"
 * 
 */
@Entity
@Table(name = "T_OA_ATTACHMENT")
public class ToaAttachment implements Serializable,Cloneable {

	@Override
	public ToaAttachment clone() throws CloneNotSupportedException {
		return (ToaAttachment)super.clone();
	}

	private static final long serialVersionUID = -7861746791836891043L;

	/** identifier field */
	@Id
	@GeneratedValue
	private String attachId;

	/** nullable persistent field */
	private String attachName;

	/** nullable persistent field */
	private String attachType;

	/** nullable persistent field */
	private byte[] attachCon;

	/** nullable persistent field */
	private String isPrivacy;

	/** nullable persistent field */
	private Date attachTime;

	/** nullable persistent field */
	private String attachSize;

	/** nullable persistent field */
	private String attachRemark;

	/** nullable persistent field */
	private String userId;

	/** full constructor */
	public ToaAttachment(String attachId, String attachName, String attachType,
			byte[] attachCon, String isPrivacy, Date attachTime,
			String attachSize, String attachRemark, String userId) {
		this.attachId = attachId;
		this.attachName = attachName;
		this.attachType = attachType;
		this.attachCon = attachCon;
		this.isPrivacy = isPrivacy;
		this.attachTime = attachTime;
		this.attachSize = attachSize;
		this.attachRemark = attachRemark;
		this.userId = userId;
	}

	/** default constructor */
	public ToaAttachment() {
	}

	/** minimal constructor */
	public ToaAttachment(String attachId) {
		this.attachId = attachId;
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="ATTACH_ID"
	 * 
	 */
	@Id
	@Column(name = "ATTACH_ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getAttachId() {
		return this.attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	/**
	 * @hibernate.property column="ATTACH_NAME" length="50"
	 * 
	 */
	@Column(name = "ATTACH_NAME", length = 50)
	public String getAttachName() {
		return this.attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	/**
	 * @hibernate.property column="ATTACH_TYPE" length="5"
	 * 
	 */
	@Column(name = "ATTACH_TYPE", length = 5)
	public String getAttachType() {
		return this.attachType;
	}

	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

	/**
	 * @hibernate.property column="ATTACH_CON" length="1024"
	 * 
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "ATTACH_CON", columnDefinition = "BLOB")
	public byte[] getAttachCon() {
		return this.attachCon;
	}

	public void setAttachCon(byte[] attachCon) {
		this.attachCon = attachCon;
	}

	/**
	 * @hibernate.property column="IS_PRIVACY" length="1"
	 * 
	 */
	@Column(name = "IS_PRIVACY", length = 1)
	public String getIsPrivacy() {
		return this.isPrivacy;
	}

	public void setIsPrivacy(String isPrivacy) {
		this.isPrivacy = isPrivacy;
	}

	/**
	 * @hibernate.property column="ATTACH_TIME" length="7"
	 * 
	 */
	@Column(name = "ATTACH_TIME", length = 7)
	public Date getAttachTime() {
		return this.attachTime;
	}

	public void setAttachTime(Date attachTime) {
		this.attachTime = attachTime;
	}

	/**
	 * @hibernate.property column="ATTACH_SIZE" length="10"
	 * 
	 */
	@Column(name = "ATTACH_SIZE", length = 10)
	public String getAttachSize() {
		return this.attachSize;
	}

	public void setAttachSize(String attachSize) {
		this.attachSize = attachSize;
	}

	/**
	 * @hibernate.property column="ATTACH_REMARK" length="4000"
	 * 
	 */
	@Column(name = "ATTACH_REMARK", length = 4000)
	public String getAttachRemark() {
		return this.attachRemark;
	}

	public void setAttachRemark(String attachRemark) {
		this.attachRemark = attachRemark;
	}

	/**
	 * @hibernate.property column="USER_ID" length="32"
	 * 
	 */
	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public String toString() {
		return new ToStringBuilder(this).append("attachId", getAttachId())
				.toString();
	}

}
