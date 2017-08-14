package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TSwAttach entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_docattach", schema = "")
public class GWYJAttachs implements java.io.Serializable {
//	 Fields

	private String attachId;

	private String docid;//DOC_ID

	private String accachName;//ATTACH_NAME

	private String attachPath;//ATTACH_PATH



	// Constructors

	/** default constructor */
	public GWYJAttachs() {
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


	@Column(name = "DOC_ID", length = 150)
	public String getDocid() {
		return docid;
	}



	public void setDocid(String docid) {
		this.docid = docid;
	}


	@Column(name = "ATTACH_NAME", length = 150)
	public String getAccachName() {
		return accachName;
	}



	public void setAccachName(String accachName) {
		this.accachName = accachName;
	}


	@Column(name = "ATTACH_PATH", length = 150)
	public String getAttachPath() {
		return attachPath;
	}



	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}


	




	
}
