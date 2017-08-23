package com.strongit.gzjzyh.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the T_GZJZYH_APPLICATION database table.
 * 
 */
@Entity
@Table(name = "T_GZJZYH_APPLICATION")
public class TGzjzyhApplication implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "APP_ID")
	private String appId;

	@Column(name = "APP_USERID")
	private String appUserid;

	@Column(name = "CASE_ID")
	private String caseId;

	@Column(name = "APP_BANKUSER")
	private String appBankuser;

	@Column(name = "APP_TYPE")
	private String appType;

	@Column(name = "APP_FILENO")
	private String appFileno;

	@Temporal(TemporalType.TIME)
	@Column(name = "APP_DATE")
	private Date appDate;

	@Column(name = "APP_LAWFILE")
	private String appLawfile;

	@Column(name = "APP_LAWFILE_R")
	private String appLawfileR;

	@Column(name = "APP_ATTACHMENT")
	private String appAttachment;

	@Column(name = "APP_ORG_ACCOUNT")
	private String appOrgAccount;

	@Column(name = "APP_PERSON_ACCOUNT")
	private String appPersonAccount;

	@Column(name = "APP_ORG_DETAIL")
	private String appOrgDetail;

	@Column(name = "APP_PERSON_DETAIL")
	private String appPersonDetail;

	@Column(name = "APP_CHADE_DETAIL")
	private String appChadeDetail;

	@Column(name = "APP_DATE_TYPE")
	private String appDateType;

	@Temporal(TemporalType.TIME)
	@Column(name = "APP_START_DATE")
	private Date appStartDate;

	@Temporal(TemporalType.TIME)
	@Column(name = "APP_END_DATE")
	private Date appEndDate;

	@Column(name = "APP_STATUS")
	private String appStatus;

	@Column(name = "APP_AUDIT_USERID")
	private String appAuditUserId;

	@Column(name = "APP_AUDIT_USER")
	private String appAuditUser;

	@Column(name = "APP_NG_REASON")
	private String appNgReason;

	@Column(name = "APP_RECEIVERID")
	private String appReceiverId;

	@Column(name = "APP_RECEIVER")
	private String appReceiver;

	@Column(name = "APP_RECEIVE_DATE")
	private String appReceivedate;

	@Column(name = "APP_RESPONSEFILE")
	private String appResponsefile;

	@Column(name = "APP_RESPONSERID")
	private String appResponserId;

	@Column(name = "APP_RESPONSER")
	private String appResponser;

	public TGzjzyhApplication() {
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppUserid() {
		return appUserid;
	}

	public void setAppUserid(String appUserid) {
		this.appUserid = appUserid;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getAppBankuser() {
		return appBankuser;
	}

	public void setAppBankuser(String appBankuser) {
		this.appBankuser = appBankuser;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getAppFileno() {
		return appFileno;
	}

	public void setAppFileno(String appFileno) {
		this.appFileno = appFileno;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public String getAppLawfile() {
		return appLawfile;
	}

	public void setAppLawfile(String appLawfile) {
		this.appLawfile = appLawfile;
	}

	public String getAppLawfileR() {
		return appLawfileR;
	}

	public void setAppLawfileR(String appLawfileR) {
		this.appLawfileR = appLawfileR;
	}

	public String getAppAttachment() {
		return appAttachment;
	}

	public void setAppAttachment(String appAttachment) {
		this.appAttachment = appAttachment;
	}

	public String getAppOrgAccount() {
		return appOrgAccount;
	}

	public void setAppOrgAccount(String appOrgAccount) {
		this.appOrgAccount = appOrgAccount;
	}

	public String getAppPersonAccount() {
		return appPersonAccount;
	}

	public void setAppPersonAccount(String appPersonAccount) {
		this.appPersonAccount = appPersonAccount;
	}

	public String getAppOrgDetail() {
		return appOrgDetail;
	}

	public void setAppOrgDetail(String appOrgDetail) {
		this.appOrgDetail = appOrgDetail;
	}

	public String getAppPersonDetail() {
		return appPersonDetail;
	}

	public void setAppPersonDetail(String appPersonDetail) {
		this.appPersonDetail = appPersonDetail;
	}

	public String getAppChadeDetail() {
		return appChadeDetail;
	}

	public void setAppChadeDetail(String appChadeDetail) {
		this.appChadeDetail = appChadeDetail;
	}

	public String getAppDateType() {
		return appDateType;
	}

	public void setAppDateType(String appDateType) {
		this.appDateType = appDateType;
	}

	public Date getAppStartDate() {
		return appStartDate;
	}

	public void setAppStartDate(Date appStartDate) {
		this.appStartDate = appStartDate;
	}

	public Date getAppEndDate() {
		return appEndDate;
	}

	public void setAppEndDate(Date appEndDate) {
		this.appEndDate = appEndDate;
	}

	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getAppAuditUser() {
		return appAuditUser;
	}

	public void setAppAuditUser(String appAuditUser) {
		this.appAuditUser = appAuditUser;
	}

	public String getAppNgReason() {
		return appNgReason;
	}

	public void setAppNgReason(String appNgReason) {
		this.appNgReason = appNgReason;
	}

	public String getAppReceiver() {
		return appReceiver;
	}

	public void setAppReceiver(String appReceiver) {
		this.appReceiver = appReceiver;
	}

	public String getAppReceivedate() {
		return appReceivedate;
	}

	public void setAppReceivedate(String appReceivedate) {
		this.appReceivedate = appReceivedate;
	}

	public String getAppResponsefile() {
		return appResponsefile;
	}

	public void setAppResponsefile(String appResponsefile) {
		this.appResponsefile = appResponsefile;
	}

	public String getAppResponser() {
		return appResponser;
	}

	public void setAppResponser(String appResponser) {
		this.appResponser = appResponser;
	}

	public String getAppAuditUserId() {
		return appAuditUserId;
	}

	public void setAppAuditUserId(String appAuditUserId) {
		this.appAuditUserId = appAuditUserId;
	}

	public String getAppReceiverId() {
		return appReceiverId;
	}

	public void setAppReceiverId(String appReceiverId) {
		this.appReceiverId = appReceiverId;
	}

	public String getAppResponserId() {
		return appResponserId;
	}

	public void setAppResponserId(String appResponserId) {
		this.appResponserId = appResponserId;
	}

}