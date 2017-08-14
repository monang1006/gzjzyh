package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_WORK_LOG"
 * 
 */
@Entity
@Table(name = "T_OA_WORK_LOG")
public class ToaWorkLog implements Serializable {

	/** identifier field */
	private String workLogId;

	/** 标题 */
	private String wlogTitle;

	/** 日程内容 */
	private byte[] wlogCon;
	
	/**
	 * 文本编辑内容
	 */
	private String wlogTxtCon;

	/** 开始时间 */
	private Date wlogStartTime;

	/** 结束时间 */
	private Date wlogEndTime;

	/** 用户ID */
	private String userId;

	/** 创建者 */
	private String wlogUserName;
	
	/** 更新时间 */
	private Date updateTime;

	/** 工作日志关联附件记录 */
	private Set toaWorkLogAttaches;
	
	private String appendsize;	//附件数量

	public ToaWorkLog() {
		
	}

	public ToaWorkLog(String workLogId, String wlogTitle,Date wlogStartTime, Date wlogEndTime, String userId, String wlogUserName, Date updateTime, Set toaWorkLogAttaches ,String wlogTxtCon) {
		super();
		this.workLogId = workLogId;
		this.wlogTitle = wlogTitle;
		this.wlogStartTime = wlogStartTime;
		this.wlogEndTime = wlogEndTime;
		this.userId = userId;
		this.wlogUserName = wlogUserName;
		this.updateTime = updateTime;
		this.toaWorkLogAttaches = toaWorkLogAttaches;
		this.wlogTxtCon=wlogTxtCon;
	}



	@Id
	@Column(name = "Work_Log_ID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getWorkLogId() {
		return this.workLogId;
	}

	public void setWorkLogId(String workLogId) {
		this.workLogId = workLogId;
	}

	@Column(name = "WLOG_TITLE")
	public String getWlogTitle() {
		return wlogTitle;
	}

	public void setWlogTitle(String logTitle) {
		wlogTitle = logTitle;
	}

	@Column(name = "WLOG_START_TIME", nullable = true)
	public Date getWlogStartTime() {
		return this.wlogStartTime;
	}

	public void setWlogStartTime(Date wlogStartTime) {
		this.wlogStartTime = wlogStartTime;
	}

	@Column(name = "WLOG_END_TIME", nullable = true)
	public Date getWlogEndTime() {
		return this.wlogEndTime;
	}

	public void setWlogEndTime(Date wlogEndTime) {
		this.wlogEndTime = wlogEndTime;
	}

	@Column(name = "USER_ID", nullable = true)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "WLOG_USER_NAME", nullable = true)
	public String getWlogUserName() {
		return this.wlogUserName;
	}

	public void setWlogUserName(String wlogUserName) {
		this.wlogUserName = wlogUserName;
	}

	@OneToMany(mappedBy = "toaWorkLog", fetch = FetchType.LAZY, cascade = {
			CascadeType.REFRESH, CascadeType.REMOVE }, targetEntity = com.strongit.oa.bo.ToaWorkLogAttach.class)
	public Set getToaWorkLogAttaches() {
		return this.toaWorkLogAttaches;
	}

	public void setToaWorkLogAttaches(Set toaWorkLogAttaches) {
		this.toaWorkLogAttaches = toaWorkLogAttaches;
	}

	public String toString() {
		return new ToStringBuilder(this).append("workLogId", getWorkLogId()).toString();
	}

	@Column(name = "UPDATED_TIME", nullable = true)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "WLOG_CON", columnDefinition = "BLOB")
	public byte[] getWlogCon() {
		return wlogCon;
	}

	public void setWlogCon(byte[] wlogCon) {
		this.wlogCon = wlogCon;
	}

	@Transient
	public String getAppendsize() {
		return appendsize;
	}

	public void setAppendsize(String appendsize) {
		this.appendsize = appendsize;
	}
	@Column(name = "WLOG_TXTCON", nullable=true)  
	public String getWlogTxtCon() {
		return wlogTxtCon;
	}

	public void setWlogTxtCon(String wlogTxtCon) {
		this.wlogTxtCon = wlogTxtCon;
	}

}
