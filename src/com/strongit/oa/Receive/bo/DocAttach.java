package com.strongit.oa.Receive.bo;

/**
 * 公文传输 文件附件
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date May 25, 2012 9:07:24 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.Receive.bo.DocAttach
 */
public class DocAttach {
	private long fileId;

	private String fileName;

	private byte[] fileContent;

	private String fileState;

	private String docId;

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileState() {
		return fileState;
	}

	public void setFileState(String fileState) {
		this.fileState = fileState;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

}
