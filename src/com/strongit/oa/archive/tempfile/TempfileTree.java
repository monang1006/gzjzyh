package com.strongit.oa.archive.tempfile;

public class TempfileTree {

	private String treeid;
	private String parentId;
	private String treetype;
	private String treeName;
	
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
