package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_MEETING_ATTACH"
 * 
 */
@Entity
@Table(name = "T_OA_MEETING_ATTACH", catalog = "", schema = "")
public class ToaMeetingAttach implements Serializable {

	/** identifier field */
	private String attachId;

	/** nullable persistent field */
	private String attachName;

	/** nullable persistent field */
	private byte[] attachCon;

	/** persistent field */
	private String meetingNotice;

	/** persistent field */
	private String meetingSummary;

	/** persistent field */
	private String meetingTopic;

	/** persistent field */
	private String meetingConference;

	/** full constructor */
	public ToaMeetingAttach(String meetingAttachId, String meetingAttachName,
			byte[] meetingAttachCon, String toaMeetingNotice,
			String toaMeetingSummary, String toaMeetingTopic,
			String toaMeetingConference) {
		this.attachId = meetingAttachId;
		this.attachName = meetingAttachName;
		this.attachCon = meetingAttachCon;
		this.meetingNotice = toaMeetingNotice;
		this.meetingSummary = toaMeetingSummary;
		this.meetingTopic = toaMeetingTopic;
		this.meetingConference = toaMeetingConference;
	}

	/** default constructor */
	public ToaMeetingAttach() {
	}

	/** minimal constructor */
	public ToaMeetingAttach(String meetingAttachId, String toaMeetingNotice,
			String toaMeetingSummary, String toaMeetingTopic,
			String toaMeetingConference) {
		this.attachId = meetingAttachId;
		this.meetingNotice = toaMeetingNotice;
		this.meetingSummary = toaMeetingSummary;
		this.meetingTopic = toaMeetingTopic;
		this.meetingConference = toaMeetingConference;
	}

	@Id
	@Column(name = "MEETING_ATTACH_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getAttachId() {
		return attachId;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	@Column(name = "MEETING_ATTACH_CON")
	@Lob
	public byte[] getAttachCon() {
		return attachCon;
	}

	public void setAttachCon(byte[] attachCon) {
		this.attachCon = attachCon;
	}

	@Column(name = "MEETING_ATTACH_NAME")
	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	@Column(name = "MEETING_NOTICE_ID", nullable = true)
	public String getMeetingNotice() {
		return meetingNotice;
	}

	public void setMeetingNotice(String meetingNotice) {
		this.meetingNotice = meetingNotice;
	}

	@Column(name = "MEETING_SUMMARY_ID", nullable = true)
	public String getMeetingSummary() {
		return meetingSummary;
	}

	public void setMeetingSummary(String meetingSummary) {
		this.meetingSummary = meetingSummary;
	}

	@Column(name = "TOPIC_ID", nullable = true)
	public String getMeetingTopic() {
		return meetingTopic;
	}

	public void setMeetingTopic(String meetingTopic) {
		this.meetingTopic = meetingTopic;
	}
	
	@Column(name = "CONFERENCE_ID", nullable = true)
	public String getMeetingConference() {
		return meetingConference;
	}

	public void setMeetingConference(String meetingConference) {
		this.meetingConference = meetingConference;
	}

}
