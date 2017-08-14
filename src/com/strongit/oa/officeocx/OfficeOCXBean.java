package com.strongit.oa.officeocx;

public class OfficeOCXBean {
	//判断是新建:0,还是编辑:1或其它的数据值
	private String opentype;
	//文档保存url
	private String savepath;
	
	private String openpath;
	
	public String getSavepath() {
		return savepath;
	}

	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}

	public String getOpentype() {
		return opentype;
	}

	public void setOpentype(String opentype) {
		this.opentype = opentype;
	}

	public String getOpenpath() {
		return openpath;
	}

	public void setOpenpath(String openpath) {
		this.openpath = openpath;
	}
}
