package com.strongit.oa.bo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 书生公文传输系统整合——发文表
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Jun 1, 2012 11:25:19 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.bo.TabContentSend
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "TAB_CONTENT_SEND")
public class TabContentSend {
	private long id;

	private String content;

	private String flag;

	private Date createDate;

	private String lockstatus;

	private String unitName;

	private String memo;

	private String senddocId;

	private TabContentFileSend mainContent;

	private List<TabContentFileSend> attachs;

	@Column(name = "CONTENT", nullable = true)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "CREATEDATE", nullable = true)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "FLAG", nullable = true)
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "native")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "LOCKSTATUS", nullable = true)
	public String getLockstatus() {
		return lockstatus;
	}

	public void setLockstatus(String lockstatus) {
		this.lockstatus = lockstatus;
	}

	@Column(name = "MEMO", nullable = true)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "UNITNAME", nullable = true)
	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Transient
	public String getSenddocId() {
		return senddocId;
	}

	public void setSenddocId(String senddocId) {
		this.senddocId = senddocId;
	}

	@Transient
	public TabContentFileSend getMainContent() {
		return mainContent;
	}

	public void setMainContent(TabContentFileSend mainContent) {
		this.mainContent = mainContent;
	}

	@Transient
	public List<TabContentFileSend> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<TabContentFileSend> attachs) {
		this.attachs = attachs;
	}
}