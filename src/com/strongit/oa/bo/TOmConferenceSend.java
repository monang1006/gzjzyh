package com.strongit.oa.bo;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * 通知下发表
 * 
 * TOmConferenceSend entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OM_CONFERENCE_SEND")
public class TOmConferenceSend implements java.io.Serializable{

	// Fields

	private String sendconId;
	private TOmConference TOmConference;
	private String conferencecode;
	private String deptCode;
	private String deptName;
	private Date conRecvTime;
	private String conRecvUser;
	private String conRemark;
	private String recvState = "0";//默认是0,1：签收，2：已上报 3：已接受 4：打回重办 
	private String operateIp;
	private String conferenceRest1;
	private String conferenceRest2;
	private String conferenceRest3;
	private Set<TOmDeptreport> TOmDeptreports = new HashSet<TOmDeptreport>(0);

	// Constructors

	/** default constructor */
	public TOmConferenceSend() {
	}

	/** minimal constructor */
	public TOmConferenceSend(String conferencecode) {
		this.conferencecode = conferencecode;
	}

	/** full constructor */
	public TOmConferenceSend(TOmConference TOmConference,
			String conferencecode, String deptCode, String deptName,
			Date conRecvTime, String conRecvUser, String conRemark,
			String recvState, String operateIp, String conferenceRest1,
			String conferenceRest2, String conferenceRest3,
			Set<TOmDeptreport> TOmDeptreports) {
		this.TOmConference = TOmConference;
		this.conferencecode = conferencecode;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.conRecvTime = conRecvTime;
		this.conRecvUser = conRecvUser;
		this.conRemark = conRemark;
		this.recvState = recvState;
		this.operateIp = operateIp;
		this.conferenceRest1 = conferenceRest1;
		this.conferenceRest2 = conferenceRest2;
		this.conferenceRest3 = conferenceRest3;
		this.TOmDeptreports = TOmDeptreports;
	}

	
		 @Id
	@Column(name="SENDCON_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getSendconId() {
		return this.sendconId;
	}

	public void setSendconId(String sendconId) {
		this.sendconId = sendconId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONFERENCE_ID")
	public TOmConference getTOmConference() {
		return this.TOmConference;
	}

	public void setTOmConference(TOmConference TOmConference) {
		this.TOmConference = TOmConference;
	}

	@Column(name = "CONFERENCECODE",  length = 132)
	public String getConferencecode() {
		return this.conferencecode;
	}

	public void setConferencecode(String conferencecode) {
		this.conferencecode = conferencecode;
	}

	@Column(name = "DEPT_CODE", length = 50)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Column(name = "DEPT_NAME", length = 1000)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "CON_RECV_TIME", length = 7)
	public Date getConRecvTime() {
		return this.conRecvTime;
	}

	public void setConRecvTime(Date conRecvTime) {
		this.conRecvTime = conRecvTime;
	}

	@Column(name = "CON_RECV_USER", length = 200)
	public String getConRecvUser() {
		return this.conRecvUser;
	}

	public void setConRecvUser(String conRecvUser) {
		this.conRecvUser = conRecvUser;
	}

	@Column(name = "CON_REMARK", length = 4000)
	public String getConRemark() {
		return this.conRemark;
	}

	public void setConRemark(String conRemark) {
		this.conRemark = conRemark;
	}

	@Column(name = "RECV_STATE", length = 1)
	public String getRecvState() {
		return this.recvState;
	}

	public void setRecvState(String recvState) {
		this.recvState = recvState;
	}

	@Column(name = "OPERATE_IP", length = 15)
	public String getOperateIp() {
		return this.operateIp;
	}

	public void setOperateIp(String operateIp) {
		this.operateIp = operateIp;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOmConferenceSend")
	public Set<TOmDeptreport> getTOmDeptreports() {
		return this.TOmDeptreports;
	}

	public void setTOmDeptreports(Set<TOmDeptreport> TOmDeptreports) {
		this.TOmDeptreports = TOmDeptreports;
	}

}
