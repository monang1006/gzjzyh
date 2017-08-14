package com.strongit.oa.bgt.model;

import java.util.Date;

/**
 * 意见征询Vo对象
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Feb 22, 2012
 * @classpath	com.strongit.oa.bgt.model.ToaYjzx
 * @version  	5.0
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public class ToaYjzx {

	private String id;				//意见征询id
	
	private String title;			//意见征询标题
	
	private String unit;			//意见征询单位
	
	private Date date;				//限时办理时间
	
	private String strDate;			//限时办理时间（Struts2传参时会吧时间丢失）
	
	private String content;			//意见征询内容
	
	private String instanceId;		//需要挂起的收文流程实例id

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
}
