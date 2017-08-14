package com.strongit.oa.common.eform.model;

import java.util.Date;

public class EForm {
	/* 表单模板ID */
	private Long id;
	/* 表单模板标题 */
	private String title;
	/* 表单模板创建者 */
	private String creator;
	/**
	 * 表单所属流程类型
	 */
	private String flowType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}
}
