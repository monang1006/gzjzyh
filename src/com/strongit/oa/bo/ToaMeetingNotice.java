package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MEETING_NOTICE"
 *     会议通知表
*/
@Entity
@Table(name = "T_OA_MEETING_NOTICE", catalog = "", schema = "")
public class ToaMeetingNotice implements Serializable {

    /** identifier field */
    private String noticeId;

    private String noticeTitle;

    private String noticeUsers;

    private String noticeAddr;

    private Date noticeStime;
    
    private Date noticeEndTime;

    private String noticeIs;

    private String conferenceId;
 
    /** full constructor */
    public ToaMeetingNotice(String noticeId, String noticeTitle,
			String noticeUsers, String noticeAddr, Date noticeStime,
			Date noticeEndTime, String noticeIs, String conferenceId) {
		super();
		this.noticeId = noticeId;
		this.noticeTitle = noticeTitle;
		this.noticeUsers = noticeUsers;
		this.noticeAddr = noticeAddr;
		this.noticeStime = noticeStime;
		this.noticeEndTime = noticeEndTime;
		this.noticeIs = noticeIs;
		this.conferenceId = conferenceId;
	}

    /** default constructor */
    public ToaMeetingNotice() {
    }
    
	/** minimal constructor */
    public ToaMeetingNotice(String meetingNoticeId, String conferenceId) {
        this.noticeId = meetingNoticeId;
        this.conferenceId = conferenceId;
        
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MEETING_NOTICE_ID"
     *         
     */
	@Id
	@Column(name = "MEETING_NOTICE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	@Column(name = "MEETING_NOTICE_IS")
	public String getNoticeIs() {
		return noticeIs;
	}

	public void setNoticeIs(String noticeIs) {
		this.noticeIs = noticeIs;
	}
	@Column(name = "MEETING_NOTICE_STIME")
	public Date getNoticeStime() {
		return noticeStime;
	}

	public void setNoticeStime(Date noticeStime) {
		this.noticeStime = noticeStime;
	}
	
	@Column(name = "MEETING_NOTICE_ENDTIME")
	public Date getNoticeEndTime() {
		return noticeEndTime;
	}

	public void setNoticeEndTime(Date noticeEndTime) {
		this.noticeEndTime = noticeEndTime;
	}
	
	@Column(name = "MEETING_NOTICE_TITLE")
	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	@Column(name = "MEETING_NOTICE_USERS")
	public String getNoticeUsers() {
		return noticeUsers;
	}

	public void setNoticeUsers(String noticeUsers) {
		this.noticeUsers = noticeUsers;
	}

	@Column(name = "CONFERENCE_ID")
	public String getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}
	
	@Column(name = "MEETING_NOTICE_ADDR")
	public String getNoticeAddr() {
		return noticeAddr;
	}

	public void setNoticeAddr(String noticeAddr) {
		this.noticeAddr = noticeAddr;
	}

}
