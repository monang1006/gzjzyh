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

import com.strongit.uums.bo.TUumsBaseUser;

/**
 * 单位回执表
 * TOmDeptreport entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OM_DEPTREPORT")
public class TOmDeptreport implements java.io.Serializable{

	// Fields

	private String reportingId;
	private TOmConferenceSend TOmConferenceSend;
	private TOmConPerson conferee;// 参会人员
	private String state;// 状态：0=待上报;1=已上报;2=已请假;3=销假
	private String leavereason;// 请假原因
	private String  reportUser;// 上报人
	private Date reportTime;// 上报时间
	private Date leaveBegintime;// 请假开始时间
	private Date leaveEndtime;// 请假结束时间
	private String reportingRest1;   //
	private String reportingRest2;
	private String reportingRest3;


	// Constructors

	/** default constructor */
	public TOmDeptreport() {
	}

	/** full constructor */
	public TOmDeptreport(TOmConferenceSend TOmConferenceSend,
			String conferId, String reportingOrg, Date reportingTime,Date leaveBegintime,Date leaveEndtime,
			String reportingTitle,String reportingContent, int reportingNumber,
			String reportingRest1, String reportingRest2,
			String reportingRest3, Set<TOmAttenUsers> TOmAttenUserses) {
		this.TOmConferenceSend = TOmConferenceSend;
		this.reportingRest1 = reportingRest1;
		this.reportingRest2 = reportingRest2;
		this.reportingRest3 = reportingRest3;
	}
	
		
	 @Id
	@Column(name="REPORTING_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getReportingId() {
		return this.reportingId;
	}

	public void setReportingId(String reportingId) {
		this.reportingId = reportingId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDCON_ID")
	public TOmConferenceSend getTOmConferenceSend() {
		return this.TOmConferenceSend;
	}

	public void setTOmConferenceSend(TOmConferenceSend TOmConferenceSend) {
		this.TOmConferenceSend = TOmConferenceSend;
	}

	

	@Column(name = "REPORTING_REST1", length = 32)
	public String getReportingRest1() {
		return this.reportingRest1;
	}

	public void setReportingRest1(String reportingRest1) {
		this.reportingRest1 = reportingRest1;
	}

	@Column(name = "REPORTING_REST2", length = 32)
	public String getReportingRest2() {
		return this.reportingRest2;
	}

	public void setReportingRest2(String reportingRest2) {
		this.reportingRest2 = reportingRest2;
	}

	@Column(name = "REPORTING_REST3", length = 32)
	public String getReportingRest3() {
		return this.reportingRest3;
	}

	public void setReportingRest3(String reportingRest3) {
		this.reportingRest3 = reportingRest3;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public TOmConPerson getConferee() {
		return conferee;
	}

	public void setConferee(TOmConPerson conferee) {
		this.conferee = conferee;
	}

	@Column(name = "STATE")
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "LEAVEREASON")
	public String getLeavereason() {
		return this.leavereason;
	}

	public void setLeavereason(String leavereason) {
		this.leavereason = leavereason;
	}

	@Column(name = "REPORT_TIME")
	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
	@Column(name = "LEAVEBEGINTIME")
	public Date getLeaveBegintime() {
		return leaveBegintime;
	}

	public void setLeaveBegintime(Date leaveBegintime) {
		this.leaveBegintime = leaveBegintime;
	}
	
	@Column(name = "LEAVEENDTIME")
	public Date getLeaveEndtime() {
		return leaveEndtime;
	}

	public void setLeaveEndtime(Date leaveEndtime) {
		this.leaveEndtime = leaveEndtime;
	}
	
	@Column(name = "REPORT_USER")
	public String getReportUser() {
		return reportUser;
	}

	public void setReportUser(String reportUser) {
		this.reportUser = reportUser;
	}


}
