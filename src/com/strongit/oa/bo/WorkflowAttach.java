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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_DOCATTACH"
 * 
 */
@Entity
@Table(name = "T_DOCATTACH")
public class WorkflowAttach implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Override
	public ToaAttachment clone() throws CloneNotSupportedException {
		return (ToaAttachment) super.clone();
	}

	/** identifier field */
	@Id
	@GeneratedValue
	private String docattachid;

	/** identifier field */
	private String docId;

	/** identifier field */
	private String personConfigFlag;

	/** identifier field */
	private Date personOperateDate;

	/** identifier field */
	private String personOperater;

	/** identifier field */
	private String attachName;

	/** identifier field */
	private byte[] attachContent;

	private String attachPath;

	public WorkflowAttach() {
	}

	public WorkflowAttach(String docattachid, String docId,
			String personConfigFlag, Date personOperateDate,
			String personOperater, String attachName, byte[] attachContent,
			String attachPath) {
		this.docattachid = docattachid;
		this.docId = docId;
		this.personConfigFlag = personConfigFlag;
		this.personOperateDate = personOperateDate;
		this.personOperater = personOperater;
		this.attachName = attachName;
		this.attachContent = attachContent;
		this.attachPath = attachPath;
	}
	
	public WorkflowAttach(String docattachid, String docId, String personConfigFlag, Date personOperateDate, String personOperater, String attachName, String attachPath) {
		super();
		this.docattachid = docattachid;
		this.docId = docId;
		this.personConfigFlag = personConfigFlag;
		this.personOperateDate = personOperateDate;
		this.personOperater = personOperater;
		this.attachName = attachName;
		this.attachPath = attachPath;
	}

	/**
	 * @hibernate.property column="DOCATTACHID" length="32"
	 * 
	 */
	@Id
	@Column(name = "DOCATTACHID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getDocattachid() {
		return this.docattachid;
	}

	public void setDocattachid(String docattachid) {
		this.docattachid = docattachid;
	}

	/**
	 * @hibernate.property column="DOC_ID" length="32"
	 * 
	 */
	@Column(name = "DOC_ID", length = 32)
	public String getDocId() {
		return this.docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	/**
	 * @hibernate.property column="PERSON_CONFIG_FLAG" length="1"
	 * 
	 */
	@Column(name = "PERSON_CONFIG_FLAG", length = 1)
	public String getPersonConfigFlag() {
		return this.personConfigFlag;
	}

	public void setPersonConfigFlag(String personConfigFlag) {
		this.personConfigFlag = personConfigFlag;
	}

	/**
	 * @hibernate.property column="PERSON_OPERATE_DATE" length="7"
	 * 
	 */
	@Column(name = "PERSON_OPERATE_DATE", length = 7)
	public Date getPersonOperateDate() {
		return this.personOperateDate;
	}

	public void setPersonOperateDate(Date personOperateDate) {
		this.personOperateDate = personOperateDate;
	}

	/**
	 * @hibernate.property column="PERSON_OPERATER" length="20"
	 * 
	 */
	@Column(name = "PERSON_OPERATER", length = 20)
	public String getPersonOperater() {
		return this.personOperater;
	}

	public void setPersonOperater(String personOperater) {
		this.personOperater = personOperater;
	}

	/**
	 * @hibernate.property column="ATTACH_NAME" length="100"
	 * 
	 */
	@Column(name = "ATTACH_NAME")
	public String getAttachName() {
		return this.attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	/**
	 * @hibernate.property column="ATTACH_CONTENT" length="4000"
	 * 
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "ATTACH_CONTENT", columnDefinition = "BLOB")
	public byte[] getAttachContent() {
		return this.attachContent;
	}

	public void setAttachContent(byte[] attachContent) {
		this.attachContent = attachContent;
	}

	@Column(name = "ATTACH_PATH", length = 255)
	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("docattachid", getDocattachid()).append("docId",
						getDocId()).append("personConfigFlag",
						getPersonConfigFlag()).append("personOperateDate",
						getPersonOperateDate()).append("personOperater",
						getPersonOperater()).append("attachName",
						getAttachName()).append("attachContent",
						getAttachContent()).append("attachPath",
						getAttachPath()).toString();
	}

}
