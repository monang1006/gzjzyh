package com.strongit.oa.docbacktracking.vo;

/**
 * 用于公文补录功能模块存放流程相关信息
 * @author yanjian
 *
 * Oct 9, 2012 12:36:14 PM
 */
public class WorkflowInfoVo {
	private String pfname;

	private String typeName;

	private Integer typeId;

	private Integer mainFormId;

	public Integer getMainFormId() {
		return mainFormId;
	}

	public void setMainFormId(Integer mainFormId) {
		this.mainFormId = mainFormId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getPfname() {
		return pfname;
	}

	public void setPfname(String pfname) {
		this.pfname = pfname;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
