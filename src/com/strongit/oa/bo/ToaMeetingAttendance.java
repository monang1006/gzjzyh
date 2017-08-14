package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_MEETING_ATTENDANCE"
 * 会议考勤表
 */
@Entity
@Table(name = "T_OA_MEETING_ATTENDANCE", catalog = "", schema = "")
public class ToaMeetingAttendance implements Serializable {

	/** identifier field */
	private String attendanceId;

	/** nullable persistent field */
	private String attendanceUserid;

	/** nullable persistent field */
	private String attendanceUsername;

	/** nullable persistent field */
	private String attendanceFlag;

	/** persistent field */
	private String noticeId;

	/** full constructor */
	public ToaMeetingAttendance(String meetingAttendanceId,
			String meetingAttendanceUserid, String meetingAttendanceUsername,
			String meetingAttendanceFlag,
			String noticeId) {
		this.attendanceId = meetingAttendanceId;
		this.attendanceUserid = meetingAttendanceUserid;
		this.attendanceUsername = meetingAttendanceUsername;
		this.attendanceFlag = meetingAttendanceFlag;
		this.noticeId = noticeId;
	}

	/** default constructor */
	public ToaMeetingAttendance() {
	}

	/** minimal constructor */
	public ToaMeetingAttendance(String meetingAttendanceId,
			String noticeId) {
		this.attendanceId = meetingAttendanceId;
		this.noticeId = noticeId;
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="MEETING_ATTENDANCE_ID"
	 * 
	 */
	@Id
	@Column(name = "MEETING_ATTENDANCE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(String attendanceId) {
		this.attendanceId = attendanceId;
	}

	@Column(name = "MEETING_ATTENDANCE_FLAG")
	public String getAttendanceFlag() {
		return attendanceFlag;
	}

	public void setAttendanceFlag(String attendanceFlag) {
		this.attendanceFlag = attendanceFlag;
	}

	@Column(name = "MEETING_ATTENDANCE_USERID")
	public String getAttendanceUserid() {
		return attendanceUserid;
	}

	public void setAttendanceUserid(String attendanceUserid) {
		this.attendanceUserid = attendanceUserid;
	}

	@Column(name = "MEETING_ATTENDANCE_USERNAME")
	public String getAttendanceUsername() {
		return attendanceUsername;
	}

	public void setAttendanceUsername(String attendanceUsername) {
		this.attendanceUsername = attendanceUsername;
	}


	@Column(name = "NOTICE_ID")
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

}
