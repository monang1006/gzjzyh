package com.strongit.oa.bo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOsTaskAttach entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OS_TASK_ATTACH")
public class TOsTaskAttach implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String attachId;
	private String attachTaskId;// 工作任务id
	private String attachSummaryId;// 办理纪要id
	private String fileServer;// 文件服务器
	private String attachFilePath;// 文件路径
	private String attachFileName;// 文件名
	private String attachFileType;// 文件类型
	private Date attachFileDate;// 上传时间
	private String attachRest1;//是否为工作任务的会议纪要的附件(0=否;1=是)
	private String attachRest2;
	private String attachRest3;

	// Constructors

	/** default constructor */
	public TOsTaskAttach() {
	}

	/** minimal constructor */
	public TOsTaskAttach(String fileServer, String attachFilePath,
			String attachFileName, String attachFileType) {
		this.fileServer = fileServer;
		this.attachFilePath = attachFilePath;
		this.attachFileName = attachFileName;
		this.attachFileType = attachFileType;
	}

	/** full constructor */
	public TOsTaskAttach(String attachTaskId, String attachSummaryId,
			String fileServer, String attachFilePath, String attachFileName,
			String attachFileType, Date attachFileDate, String attachRest1,
			String attachRest2, String attachRest3) {
		this.attachTaskId = attachTaskId;
		this.attachSummaryId = attachSummaryId;
		this.fileServer = fileServer;
		this.attachFilePath = attachFilePath;
		this.attachFileName = attachFileName;
		this.attachFileType = attachFileType;
		this.attachFileDate = attachFileDate;
		this.attachRest1 = attachRest1;
		this.attachRest2 = attachRest2;
		this.attachRest3 = attachRest3;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ATTACH_ID", unique = true, nullable = false, length = 32)
	public String getAttachId() {
		return this.attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	@Column(name = "ATTACH_TASK_ID", length = 32)
	public String getAttachTaskId() {
		return this.attachTaskId;
	}

	public void setAttachTaskId(String attachTaskId) {
		this.attachTaskId = attachTaskId;
	}

	@Column(name = "ATTACH_SUMMARY_ID", length = 32)
	public String getAttachSummaryId() {
		return this.attachSummaryId;
	}

	public void setAttachSummaryId(String attachSummaryId) {
		this.attachSummaryId = attachSummaryId;
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

	@Column(name = "ATTACH_FILE_DATA", length = 7)
	public Date getAttachFileDate() {
		return this.attachFileDate;
	}

	public void setAttachFileDate(Date attachFileDate) {
		this.attachFileDate = attachFileDate;
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
}
