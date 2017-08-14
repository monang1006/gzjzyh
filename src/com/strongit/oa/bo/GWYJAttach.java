package com.strongit.oa.bo;

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
 * TSwAttach entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_YJATTACH", schema = "")
public class GWYJAttach implements java.io.Serializable {
//	 Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String attachId;

	private String docid;//DOC_ID

	private String accachName;//ATTACH_NAME

	private String attachPath;//ATTACH_PATH
	
	/** identifier field */
	private byte[] attachContent;
	
	/** identifier field */
	private String personConfigFlag;

	/** identifier field */
	private Date personOperateDate;

	/** identifier field */
	private String personOperater;


	// Constructors

	/** default constructor */
	public GWYJAttach() {
	}

	public GWYJAttach(String attachId, String docid, String accachName, String attachPath, String personConfigFlag, Date personOperateDate, String personOperater) {
		this.attachId = attachId;
		this.docid = docid;
		this.accachName = accachName;
		this.attachPath = attachPath;
		this.personConfigFlag = personConfigFlag;
		this.personOperateDate = personOperateDate;
		this.personOperater = personOperater;
	}

	@Id
	@Column(name = "DOCATTACHID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getAttachId() {
		return this.attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}


	@Column(name = "DOC_ID", length = 32)
	public String getDocid() {
		return docid;
	}



	public void setDocid(String docid) {
		this.docid = docid;
	}


	@Column(name = "ATTACH_NAME", length = 100)
	public String getAccachName() {
		return accachName;
	}

	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "ATTACH_CONTENT", columnDefinition = "BLOB")
	public byte[] getAttachContent() {
		return this.attachContent;
	}

	public void setAttachContent(byte[] attachContent) {
		this.attachContent = attachContent;
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

	public void setAccachName(String accachName) {
		this.accachName = accachName;
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
				.append("attachId", getAttachId()).append("docid",
						getDocid()).append("personConfigFlag",
						getPersonConfigFlag()).append("personOperateDate",
						getPersonOperateDate()).append("personOperater",
						getPersonOperater()).append("accachName",
						getAccachName()).append("attachContent",
						getAttachContent()).append("attachPath",
						getAttachPath()).toString();
	}




	
}
