package com.strongit.oa.util;

public class TempPo {

	private String id;
    //是否为委派
	private String typewei;
	
	private String name;

	private String parentId;
	
	private String codeId;//orgId
	
	private String type;//为null标示个人通讯录,否则为系统通讯录

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getTypewei() {
		return typewei;
	}

	public void setTypewei(String typewei) {
		this.typewei = typewei;
	}

}
