package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_MEETING_CONFERENCE"
 * 
 */
@Entity
@Table(name = "T_OA_MEETING_CONFERENCE", catalog = "", schema = "")
public class ToaMeetingConference implements Serializable {
	/** identifier field */
	private String conferenceId;

	private String conferenceCode;

	private String conferenceName;

	private String conferenceContent;

	private Date conferenceStime;

	private String conferenceAddr;

	private Date conferenceEndtime;

	private String conferenceDemo;

	private String conferenceStatus;

	private String rest1;

	private String rest2;

	/** full constructor */
	public ToaMeetingConference(String conferenceId, String conferenceCode,
			String conferenceName, String conferenceContent,
			Date conferenceStime, String conferenceAddr,
			Date conferenceEndtime, String conferenceDemo,
			String conferenceStatus, String rest1, String rest2) {
		super();
		this.conferenceId = conferenceId;
		this.conferenceCode = conferenceCode;
		this.conferenceName = conferenceName;
		this.conferenceContent = conferenceContent;
		this.conferenceStime = conferenceStime;
		this.conferenceAddr = conferenceAddr;
		this.conferenceEndtime = conferenceEndtime;
		this.conferenceDemo = conferenceDemo;
		this.conferenceStatus = conferenceStatus;
		this.rest1 = rest1;
		this.rest2 = rest2;
	}

	/** default constructor */
	public ToaMeetingConference() {

	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="CONFERENCE_ID"
	 * 
	 */
	@Id
	@Column(name = "CONFERENCE_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}

	/**
	 * @hibernate.property column="CONFERENCE_CODE" length="20"
	 * 
	 */
	@Column(name = "CONFERENCE_CODE")
	public String getConferenceCode() {
		return conferenceCode;
	}

	public void setConferenceCode(String conferenceCode) {
		this.conferenceCode = conferenceCode;
	}

	@Column(name = "CONFERENCE_NAME")
	public String getConferenceName() {
		return conferenceName;
	}

	public void setConferenceName(String conferenceName) {
		this.conferenceName = conferenceName;
	}

	@Column(name = "CONFERENCE_CONTENT")
	public String getConferenceContent() {
		return conferenceContent;
	}

	public void setConferenceContent(String conferenceContent) {
		this.conferenceContent = conferenceContent;
	}

	@Column(name = "CONFERENCE_STIME")
	public Date getConferenceStime() {
		return conferenceStime;
	}

	public void setConferenceStime(Date conferenceStime) {
		this.conferenceStime = conferenceStime;
	}

	@Column(name = "CONFERENCE_ADDR")
	public String getConferenceAddr() {
		return conferenceAddr;
	}

	public void setConferenceAddr(String conferenceAddr) {
		this.conferenceAddr = conferenceAddr;
	}

	@Column(name = "CONFERENCE_ENDTIME")
	public Date getConferenceEndtime() {
		return conferenceEndtime;
	}

	public void setConferenceEndtime(Date conferenceEndtime) {
		this.conferenceEndtime = conferenceEndtime;
	}

	@Column(name = "CONFERENCE_DEMO")
	public String getConferenceDemo() {
		return conferenceDemo;
	}

	public void setConferenceDemo(String conferenceDemo) {
		this.conferenceDemo = conferenceDemo;
	}

	@Column(name = "CONFERENCE_STATUS")
	public String getConferenceStatus() {
		return conferenceStatus;
	}

	public void setConferenceStatus(String conferenceStatus) {
		this.conferenceStatus = conferenceStatus;
	}

	@Column(name = "REST1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

}
