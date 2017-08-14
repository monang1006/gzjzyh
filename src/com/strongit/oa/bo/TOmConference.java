package com.strongit.oa.bo;


import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 会议通知
 * TOmConference entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OM_CONFERENCE")
public class TOmConference implements java.io.Serializable{

	// Fields

	private String conferenceId;
	private String conferenceTitle;
	private String conferenceSubject;
	private TOmConType tsConfersort;
	private String conferenceAddr;
	private Date conferenceStime;
	private Date conferenceEtime;
	private String conferenceHost;
	private String conferenceAttenleaders;
	private String conferenceUndertaker;
	private String conferenceUser;
	private String conferenceUsertel;
	private Integer conferenceAttenusers;
	private Date conferenceRegendtime;

	private byte[] noticecontent;//通知正文内容
	
	private String  conferenceAttendOrgs;//CONFERENCE_ATTENORGS参会单位
	
	private String conferenceState;//CONFERENCE_STATUS 会议状态
	
	private String conferencePerson;
	private Date conSendTime;
	private Date conEntryTime;
	private String conferenceRest1;
	private String conferenceRest2;
	private String conferenceRest3;
	private Set<TOmConferenceSend> TOmConferenceSends = new HashSet<TOmConferenceSend>(
			0);

	private String conferenceSendDeptNames;	//下发单位名称（存在多个用“,”隔开）
	private String conferenceSendDeptCodes;//下发单位id（存在多个用“,”隔开）
	private String conferenceFromId;//大中型会议办结后传入会议通知的会议id
	// Constructors
	@Transient
	public String getConferenceSendDeptCodes() {
		return conferenceSendDeptCodes;
	}

	public void setConferenceSendDeptCodes(String conferenceSendDeptCodes) {
		this.conferenceSendDeptCodes = conferenceSendDeptCodes;
	}
	@Transient
	public String getConferenceSendDeptNames() {
		return conferenceSendDeptNames;
	}

	public void setConferenceSendDeptNames(String conferenceSendDeptNames) {
		this.conferenceSendDeptNames = conferenceSendDeptNames;
	}

	/** default constructor */
	public TOmConference() {
	}

	/** minimal constructor */
	public TOmConference(String conferenceTitle) {
		this.conferenceTitle = conferenceTitle;
	}

	/** full constructor */
	public TOmConference(String conferenceTitle,
			String conferenceSubject, String conferenceType,
			String conferenceAddr, Date conferenceStime, Date conferenceEtime,
			String conferenceHost, String conferenceAttenleaders,
			String conferenceUndertaker, String conferenceUser,
			String conferenceUsertel, int conferenceAttenusers,
			Date conferenceRegendtime, String conferenceContent,
			String conferencePerson, Date conSendTime, Date conEntryTime,
			String conferenceRest1, String conferenceRest2,
			String conferenceRest3, Set<TOmConferenceSend> TOmConferenceSends) {
		this.conferenceTitle = conferenceTitle;
		this.conferenceSubject = conferenceSubject;
		this.conferenceAddr = conferenceAddr;
		this.conferenceStime = conferenceStime;
		this.conferenceEtime = conferenceEtime;
		this.conferenceHost = conferenceHost;
		this.conferenceAttenleaders = conferenceAttenleaders;
		this.conferenceUndertaker = conferenceUndertaker;
		this.conferenceUser = conferenceUser;
		this.conferenceUsertel = conferenceUsertel;
		this.conferenceAttenusers = conferenceAttenusers;
		this.conferenceRegendtime = conferenceRegendtime;
		this.conferencePerson = conferencePerson;
		this.conSendTime = conSendTime;
		this.conEntryTime = conEntryTime;
		this.conferenceRest1 = conferenceRest1;
		this.conferenceRest2 = conferenceRest2;
		this.conferenceRest3 = conferenceRest3;
		this.TOmConferenceSends = TOmConferenceSends;
	}


	
	 @Id
	@Column(name="CONFERENCE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getConferenceId() {
		return this.conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}

	@Column(name = "CONFERENCE_TITLE", nullable = false, length = 132)
	public String getConferenceTitle() {
		return this.conferenceTitle;
	}

	public void setConferenceTitle(String conferenceTitle) {
		this.conferenceTitle = conferenceTitle;
	}

	@Column(name = "CONFERENCE_SUBJECT", length = 2332)
	public String getConferenceSubject() {
		return this.conferenceSubject;
	}

	public void setConferenceSubject(String conferenceSubject) {
		this.conferenceSubject = conferenceSubject;
	}

	@Column(name = "CONFERENCE_ADDR", length = 232)
	public String getConferenceAddr() {
		return this.conferenceAddr;
	}

	public void setConferenceAddr(String conferenceAddr) {
		this.conferenceAddr = conferenceAddr;
	}

	
	@Column(name = "CONFERENCE_STIME", length = 7)
	public Date getConferenceStime() {
		return this.conferenceStime;
	}

	public void setConferenceStime(Date conferenceStime) {
		this.conferenceStime = conferenceStime;
	}


	@Column(name = "CONFERENCE_ETIME", length = 7)
	public Date getConferenceEtime() {
		return this.conferenceEtime;
	}

	public void setConferenceEtime(Date conferenceEtime) {
		this.conferenceEtime = conferenceEtime;
	}

	@Column(name = "CONFERENCE_HOST", length = 132)
	public String getConferenceHost() {
		return this.conferenceHost;
	}

	public void setConferenceHost(String conferenceHost) {
		this.conferenceHost = conferenceHost;
	}

	@Column(name = "CONFERENCE_ATTENLEADERS", length = 1232)
	public String getConferenceAttenleaders() {
		return this.conferenceAttenleaders;
	}

	public void setConferenceAttenleaders(String conferenceAttenleaders) {
		this.conferenceAttenleaders = conferenceAttenleaders;
	}

	@Column(name = "CONFERENCE_UNDERTAKER", length = 232)
	public String getConferenceUndertaker() {
		return this.conferenceUndertaker;
	}

	public void setConferenceUndertaker(String conferenceUndertaker) {
		this.conferenceUndertaker = conferenceUndertaker;
	}

	@Column(name = "CONFERENCE_USER", length = 32)
	public String getConferenceUser() {
		return this.conferenceUser;
	}

	public void setConferenceUser(String conferenceUser) {
		this.conferenceUser = conferenceUser;
	}

	@Column(name = "CONFERENCE_USERTEL", length = 132)
	public String getConferenceUsertel() {
		return this.conferenceUsertel;
	}

	public void setConferenceUsertel(String conferenceUsertel) {
		this.conferenceUsertel = conferenceUsertel;
	}

	@Column(name = "CONFERENCE_ATTENUSERS")
	public Integer getConferenceAttenusers() {
		return this.conferenceAttenusers;
	}

	public void setConferenceAttenusers(Integer conferenceAttenusers) {
		this.conferenceAttenusers = conferenceAttenusers;
	}


	@Column(name = "CONFERENCE_REGENDTIME", length = 7)
	public Date getConferenceRegendtime() {
		return this.conferenceRegendtime;
	}

	public void setConferenceRegendtime(Date conferenceRegendtime) {
		this.conferenceRegendtime = conferenceRegendtime;
	}


	@Column(name = "CONFERENCE_PERSON", length = 45)
	public String getConferencePerson() {
		return this.conferencePerson;
	}

	public void setConferencePerson(String conferencePerson) {
		this.conferencePerson = conferencePerson;
	}
	
	@Column(name = "CON_SEND_TIME")
	public Date getConSendTime() {
		return this.conSendTime;
	}

	public void setConSendTime(Date conSendTime) {
		this.conSendTime = conSendTime;
	}


	@Column(name = "CON_ENTRY_TIME", length = 7)
	public Date getConEntryTime() {
		return this.conEntryTime;
	}

	public void setConEntryTime(Date conEntryTime) {
		this.conEntryTime = conEntryTime;
	}

	@Column(name = "CONFERENCE_REST1", length = 104)
	public String getConferenceRest1() {
		return this.conferenceRest1;
	}

	public void setConferenceRest1(String conferenceRest1) {
		this.conferenceRest1 = conferenceRest1;
	}

	@Column(name = "CONFERENCE_REST2", length = 1024)
	public String getConferenceRest2() {
		return this.conferenceRest2;
	}

	public void setConferenceRest2(String conferenceRest2) {
		this.conferenceRest2 = conferenceRest2;
	}

	@Column(name = "CONFERENCE_REST3", length = 134)
	public String getConferenceRest3() {
		return this.conferenceRest3;
	}

	public void setConferenceRest3(String conferenceRest3) {
		this.conferenceRest3 = conferenceRest3;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOmConference")
	public Set<TOmConferenceSend> getTOmConferenceSends() {
		return this.TOmConferenceSends;
	}

	public void setTOmConferenceSends(Set<TOmConferenceSend> TOmConferenceSends) {
		this.TOmConferenceSends = TOmConferenceSends;
	}
	@Column(name = "CONFERENCE_ATTENORGS")
	public String getConferenceAttendOrgs() {
		return conferenceAttendOrgs;
	}

	public void setConferenceAttendOrgs(String conferenceAttendOrgs) {
		this.conferenceAttendOrgs = conferenceAttendOrgs;
	}
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "NOTICE_CONTENT", columnDefinition = "BLOB")
	public byte[] getNoticecontent() {
		return noticecontent;
	}

	public void setNoticecontent(byte[] noticecontent) {
		this.noticecontent = noticecontent;
	}
	
	@Column(name = "CONFERENCE_STATUS")
	public String getConferenceState() {
		return conferenceState;
	}

	public void setConferenceState(String conferenceState) {
		this.conferenceState = conferenceState;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONFERENCE_TYPE")
	public TOmConType getTsConfersort() {
		return tsConfersort;
	}

	public void setTsConfersort(TOmConType tsConfersort) {
		this.tsConfersort = tsConfersort;
	}
	@Column(name="CONFERENCE_FROM_ID")
	public String getConferenceFromId() {
		return conferenceFromId;
	}

	public void setConferenceFromId(String conferenceFromId) {
		this.conferenceFromId = conferenceFromId;
	}

}
