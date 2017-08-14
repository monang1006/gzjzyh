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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_MEETING_SUMMARY" 会议记录表
 */

@Entity
@Table(name = "T_OA_MEETING_SUMMARY", catalog = "", schema = "")
public class ToaMeetingSummary implements Serializable {

	/** identifier field */
	private String summaryId;

	/** nullable persistent field */
	private String summaryTitle;

	/** nullable persistent field */
	private String summaryUsers;

	/** nullable persistent field */
	private Date summaryTime;

	/** nullable persistent field */
	private String summaryAddr;

	/** nullable persistent field */
	private String summaryContent;

	private String isGuidang;

	/** persistent field */
	private String noticeId;

	/** persistent field */

	/** full constructor */
	public ToaMeetingSummary(String summaryId, String summaryTitle,
			String summaryUsers, Date summaryTime, 
			String summaryAddr, String summaryContent, String isGuidang,
			String noticeId) {
		super();
		this.summaryId = summaryId;
		this.summaryTitle = summaryTitle;
		this.summaryUsers = summaryUsers;
		this.summaryTime = summaryTime;
		this.summaryAddr = summaryAddr;
		this.summaryContent = summaryContent;
		this.isGuidang = isGuidang;
		this.noticeId = noticeId;
	}

	/** default constructor */
	public ToaMeetingSummary() {
	}

	/** minimal constructor */
	public ToaMeetingSummary(String meetingSummaryId, String noticeId) {
		this.summaryId = meetingSummaryId;
		this.noticeId = noticeId;

	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="MEETING_SUMMARY_ID"
	 * 
	 */
	@Id
	@Column(name = "MEETING_SUMMARY_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}

	@Column(name = "MEETING_SUMMARY_TITLE")
	public String getSummaryTitle() {
		return summaryTitle;
	}

	public void setSummaryTitle(String summaryTitle) {
		this.summaryTitle = summaryTitle;
	}

	@Column(name = "MEETING_SUMMARY_USERS")
	public String getSummaryUsers() {
		return summaryUsers;
	}

	public void setSummaryUsers(String summaryUsers) {
		this.summaryUsers = summaryUsers;
	}

	@Column(name = "NOTICE_ID")
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "MEETING_SUMMARY_CONTENT", columnDefinition = "CLOB", nullable = true)
	public String getSummaryContent() {
		return summaryContent;
	}

	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}

	@Column(name = "MEETING_SUMMARY_ADDR")
	public String getSummaryAddr() {
		return summaryAddr;
	}

	public void setSummaryAddr(String summaryAddr) {
		this.summaryAddr = summaryAddr;
	}

	@Column(name = "MEETING_SUMMARY_TIME")
	public Date getSummaryTime() {
		return summaryTime;
	}

	public void setSummaryTime(Date summaryTime) {
		this.summaryTime = summaryTime;
	}

	@Column(name = "IS_GUIDANG")
	public String getIsGuidang() {
		return isGuidang;
	}

	public void setIsGuidang(String isGuidang) {
		this.isGuidang = isGuidang;
	}

}
