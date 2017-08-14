package com.strongit.oa.archive.filesearch;

import java.util.Date;

public class SearchTree {
	private String treeid;
	private String parentId;
	private String treetype;
	private String treeName;
	private String fileNo;//文件编码
	private String fileTitle;//
	private String fileAuthor;
	private String fileDate;
	private String fileFolder;
	private String filepage;
	private String fileType;
	private String orgId;
	private String disLogo;
	public String getDisLogo() {
		return disLogo;
	}
	public void setDisLogo(String disLogo) {
		this.disLogo = disLogo;
	}
	public String getFileNo() {
		return fileNo;
	}
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getFileAuthor() {
		return fileAuthor;
	}
	public void setFileAuthor(String fileAuthor) {
		this.fileAuthor = fileAuthor;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getFileFolder() {
		return fileFolder;
	}
	public void setFileFolder(String fileFolder) {
		this.fileFolder = fileFolder;
	}
	public String getFilepage() {
		return filepage;
	}
	public void setFilepage(String filepage) {
		this.filepage = filepage;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getTreeName() {
		return treeName;
	}
	public void setTreeName(String treeName) {
		this.treeName = treeName;
	}
	public String getTreetype() {
		return treetype;
	}
	public void setTreetype(String treetype) {
		this.treetype = treetype;
	}
	public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
