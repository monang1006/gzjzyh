package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 附件 TOmConAttach entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OM_CON_ATTACH")
public class TOmConAttach implements java.io.Serializable{

	// Fields

	private String attachId;

	private String conferenceId;

	private String attachRepId;

	private String fileServer;

	private String attachFilePath;

	private String attachFileName;

	private String attachFileType;

	private Date attachFileData;

	private String attachRest1;// 使用该预留字段标识附件是否为正文[0:不是|1:是]

	private String attachRest2;

	private String attachRest3;

	private byte[] attachContent;

	// Constructors

	/** default constructor */
	public TOmConAttach() {
	}

	/** minimal constructor */
	public TOmConAttach(String fileServer, String attachFilePath,
			String attachFileName, String attachFileType) {
		this.fileServer = fileServer;
		this.attachFilePath = attachFilePath;
		this.attachFileName = attachFileName;
		this.attachFileType = attachFileType;
	}

	/** full constructor */
	public TOmConAttach(String attachConId, String attachRepId,
			String fileServer, String attachFilePath, String attachFileName,
			String attachFileType, Date attachFileData, String attachRest1,
			String attachRest2, String attachRest3) {
		this.conferenceId = attachConId;
		this.attachRepId = attachRepId;
		this.fileServer = fileServer;
		this.attachFilePath = attachFilePath;
		this.attachFileName = attachFileName;
		this.attachFileType = attachFileType;
		this.attachFileData = attachFileData;
		this.attachRest1 = attachRest1;
		this.attachRest2 = attachRest2;
		this.attachRest3 = attachRest3;
	}

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

	@Column(name = "ATTACH_CON_ID", length = 32)
	public String getConferenceId() {
		return this.conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}

	@Column(name = "ATTACH_REP_ID", length = 32)
	public String getAttachRepId() {
		return this.attachRepId;
	}

	public void setAttachRepId(String attachRepId) {
		this.attachRepId = attachRepId;
	}

	@Column(name = "FILE_SERVER", nullable = false, length = 100)
	public String getFileServer() {
		return this.fileServer;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	@Column(name = "ATTACH_FILE_PATH", nullable = false, length = 200)
	public String getAttachFilePath() {
		return this.attachFilePath;
	}

	public void setAttachFilePath(String attachFilePath) {
		this.attachFilePath = attachFilePath;
	}

	@Column(name = "ATTACH_FILE_NAME", nullable = false, length = 500)
	public String getAttachFileName() {
		return this.attachFileName;
	}

	public void setAttachFileName(String attachFileName) {
		this.attachFileName = attachFileName;
	}

	@Column(name = "ATTACH_FILE_TYPE", nullable = false, length = 5)
	public String getAttachFileType() {
		return this.attachFileType;
	}

	public void setAttachFileType(String attachFileType) {
		this.attachFileType = attachFileType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ATTACH_FILE_DATA", length = 7)
	public Date getAttachFileData() {
		return this.attachFileData;
	}

	public void setAttachFileData(Date attachFileData) {
		this.attachFileData = attachFileData;
	}

	@Column(name = "ATTACH_REST1", length = 132)
	public String getAttachRest1() {
		return this.attachRest1;
	}

	public void setAttachRest1(String attachRest1) {
		this.attachRest1 = attachRest1;
	}

	@Column(name = "ATTACH_REST2", length = 1024)
	public String getAttachRest2() {
		return this.attachRest2;
	}

	public void setAttachRest2(String attachRest2) {
		this.attachRest2 = attachRest2;
	}

	@Column(name = "ATTACH_REST3", length = 132)
	public String getAttachRest3() {
		return this.attachRest3;
	}

	public void setAttachRest3(String attachRest3) {
		this.attachRest3 = attachRest3;
	}

	@Transient
	public byte[] getAttachContent() {
		return attachContent;
	}

	public void setAttachContent(byte[] attachContent) {
		this.attachContent = attachContent;
	}
}
