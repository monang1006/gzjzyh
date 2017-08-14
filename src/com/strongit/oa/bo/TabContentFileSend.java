package com.strongit.oa.bo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * 书生公文传输整合——发文附件表
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Jun 1, 2012 11:24:51 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bo.TabContentFileSend
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TAB_CONTENT_FILE_SEND")
public class TabContentFileSend {
	private long fileId;

	private long id;

	private String fileName;

	private byte[] fileContent;

	private String fileState;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "FILECONTENT", columnDefinition = "BLOB")
	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	@Id
	@Column(name = "FILEID", nullable = false)
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "native")
	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "FILENAME", nullable = true)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILESTATE", nullable = true)
	public String getFileState() {
		return fileState;
	}

	public void setFileState(String fileState) {
		this.fileState = fileState;
	}

	@Column(name = "ID", nullable = true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}