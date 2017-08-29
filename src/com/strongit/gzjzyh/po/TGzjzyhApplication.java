package com.strongit.gzjzyh.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.strongit.gzjzyh.appConstants;

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

	@Temporal(TemporalType.TIMESTAMP)
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
	private String appDateType = "0";

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_START_DATE")
	private Date appStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_END_DATE")
	private Date appEndDate;

	@Column(name = "APP_STATUS")
	private String appStatus;

	@Column(name = "APP_AUDIT_USERID")
	private String appAuditUserId;

	@Column(name = "APP_AUDIT_USER")
	private String appAuditUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_AUDIT_DATE")
	private Date appAuditDate;

	@Column(name = "APP_NG_REASON")
	private String appNgReason;

	@Column(name = "APP_RECEIVERID")
	private String appReceiverId;

	@Column(name = "APP_RECEIVER")
	private String appReceiver;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_RECEIVE_DATE")
	private Date appReceiveDate;

	@Column(name = "APP_RESPONSEFILE")
	private String appResponsefile;

	@Column(name = "APP_RESPONSERID")
	private String appResponserId;

	@Column(name = "APP_RESPONSER")
	private String appResponser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_RESPONSE_DATE")
	private Date appResponseDate;

	@Column(name = "APP_MAIN_NAME")
	private String appMainName;

	@Column(name = "APP_MAIN_ID")
	private String appMainId;

	@Column(name = "APP_MAIN_NO")
	private String appMainNo;

	@Column(name = "APP_MAIN_MOBILE")
	private String appMainMobile;

	@Column(name = "APP_MAIN_NO1")
	private String appMainNo1;

	@Column(name = "APP_MAIN_NO2")
	private String appMainNo2;

	@Column(name = "APP_MAIN_ID1")
	private String appMainId1;

	@Column(name = "APP_MAIN_ID2")
	private String appMainId2;

	@Column(name = "APP_HELP_NAME")
	private String appHelpName;

	@Column(name = "APP_HELP_ID")
	private String appHelpId;

	@Column(name = "APP_HELP_NO")
	private String appHelpNo;

	@Column(name = "APP_HELP_MOBILE")
	private String appHelpMobile;

	@Column(name = "APP_HELP_NO1")
	private String appHelpNo1;

	@Column(name = "APP_HELP_NO2")
	private String appHelpNo2;

	@Column(name = "APP_HELP_ID1")
	private String appHelpId1;

	@Column(name = "APP_HELP_ID2")
	private String appHelpId2;
	
	@Column(name = "APP_CASE_CODE")
	private String appCaseCode;
	
	@Column(name = "APP_ORGID")
	private String appOrgId;

	@Column(name = "APP_ORG")
	private String appOrg;

	@Column(name = "APP_CASE_NAME")
	private String appCaseName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_CASE_CONFIRMTIME")
	private Date appCaseConfirmTime;
	
	@Column(name = "APP_ADDRESS")
	private String appAddress;

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

	public Date getAppReceiveDate() {
		return appReceiveDate;
	}

	public void setAppReceiveDate(Date appReceiveDate) {
		this.appReceiveDate = appReceiveDate;
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

	public Date getAppAuditDate() {
		return appAuditDate;
	}

	public void setAppAuditDate(Date appAuditDate) {
		this.appAuditDate = appAuditDate;
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

	public Date getAppResponseDate() {
		return appResponseDate;
	}

	public void setAppResponseDate(Date appResponseDate) {
		this.appResponseDate = appResponseDate;
	}

	public String getAppMainName() {
		return appMainName;
	}

	public void setAppMainName(String appMainName) {
		this.appMainName = appMainName;
	}

	public String getAppMainId() {
		return appMainId;
	}

	public void setAppMainId(String appMainId) {
		this.appMainId = appMainId;
	}

	public String getAppMainNo() {
		return appMainNo;
	}

	public void setAppMainNo(String appMainNo) {
		this.appMainNo = appMainNo;
	}

	public String getAppMainMobile() {
		return appMainMobile;
	}

	public void setAppMainMobile(String appMainMobile) {
		this.appMainMobile = appMainMobile;
	}

	public String getAppMainNo1() {
		return appMainNo1;
	}

	public void setAppMainNo1(String appMainNo1) {
		this.appMainNo1 = appMainNo1;
	}

	public String getAppMainNo2() {
		return appMainNo2;
	}

	public void setAppMainNo2(String appMainNo2) {
		this.appMainNo2 = appMainNo2;
	}

	public String getAppMainId1() {
		return appMainId1;
	}

	public void setAppMainId1(String appMainId1) {
		this.appMainId1 = appMainId1;
	}

	public String getAppMainId2() {
		return appMainId2;
	}

	public void setAppMainId2(String appMainId2) {
		this.appMainId2 = appMainId2;
	}

	public String getAppHelpName() {
		return appHelpName;
	}

	public void setAppHelpName(String appHelpName) {
		this.appHelpName = appHelpName;
	}

	public String getAppHelpId() {
		return appHelpId;
	}

	public void setAppHelpId(String appHelpId) {
		this.appHelpId = appHelpId;
	}

	public String getAppHelpNo() {
		return appHelpNo;
	}

	public void setAppHelpNo(String appHelpNo) {
		this.appHelpNo = appHelpNo;
	}

	public String getAppHelpMobile() {
		return appHelpMobile;
	}

	public void setAppHelpMobile(String appHelpMobile) {
		this.appHelpMobile = appHelpMobile;
	}

	public String getAppHelpNo1() {
		return appHelpNo1;
	}

	public void setAppHelpNo1(String appHelpNo1) {
		this.appHelpNo1 = appHelpNo1;
	}

	public String getAppHelpNo2() {
		return appHelpNo2;
	}

	public void setAppHelpNo2(String appHelpNo2) {
		this.appHelpNo2 = appHelpNo2;
	}

	public String getAppHelpId1() {
		return appHelpId1;
	}

	public void setAppHelpId1(String appHelpId1) {
		this.appHelpId1 = appHelpId1;
	}

	public String getAppHelpId2() {
		return appHelpId2;
	}

	public void setAppHelpId2(String appHelpId2) {
		this.appHelpId2 = appHelpId2;
	}

	public String getAppCaseCode() {
		return appCaseCode;
	}

	public void setAppCaseCode(String appCaseCode) {
		this.appCaseCode = appCaseCode;
	}

	public String getAppCaseName() {
		return appCaseName;
	}

	public void setAppCaseName(String appCaseName) {
		this.appCaseName = appCaseName;
	}

	public Date getAppCaseConfirmTime() {
		return appCaseConfirmTime;
	}

	public void setAppCaseConfirmTime(Date appCaseConfirmTime) {
		this.appCaseConfirmTime = appCaseConfirmTime;
	}

	public String getAppOrg() {
		return appOrg;
	}

	public void setAppOrg(String appOrg) {
		this.appOrg = appOrg;
	}

	public String getAppAddress() {
		return appAddress;
	}

	public void setAppAddress(String appAddress) {
		this.appAddress = appAddress;
	}

	public String getAppOrgId() {
		return appOrgId;
	}

	public void setAppOrgId(String appOrgId) {
		this.appOrgId = appOrgId;
	}

}