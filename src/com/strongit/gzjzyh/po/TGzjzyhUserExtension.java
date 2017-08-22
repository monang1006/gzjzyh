package com.strongit.gzjzyh.po;

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

import com.strongit.uums.bo.TUumsBaseUser;

@Entity
@Table(name = "T_GZJZYH_USER_EXTENSION")
public class TGzjzyhUserExtension implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ueId;
	private String ueMainName;
	private String ueMainId;
	private String ueMainNo;
	private String ueMainMobile;
	private String ueMainNo1;
	private String ueMainNo2;
	private String ueMainId1;
	private String ueMainId2;
	private String ueHelpName;
	private String ueHelpId;
	private String ueHelpNo;
	private String ueHelpMobile;
	private String ueHelpNo1;
	private String ueHelpNo2;
	private String ueHelpId1;
	private String ueHelpId2;
	private String ueStatus;
	private String ueAuditUser;
	private String ueAuditUserId;
	private String ueNgReason;
	/** persistent field */
	private TUumsBaseUser tuumsBaseUser;

	public TGzjzyhUserExtension() {

	}

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "UE_ID")
	public String getUeId() {
		return ueId;
	}

	public void setUeId(String ueId) {
		this.ueId = ueId;
	}

	@Column(name = "UE_MAIN_NAME")
	public String getUeMainName() {
		return ueMainName;
	}

	public void setUeMainName(String ueMainName) {
		this.ueMainName = ueMainName;
	}

	@Column(name = "UE_MAIN_NO")
	public String getUeMainNo() {
		return ueMainNo;
	}

	public void setUeMainNo(String ueMainNo) {
		this.ueMainNo = ueMainNo;
	}

	@Column(name = "UE_MAIN_MOBILE")
	public String getUeMainMobile() {
		return ueMainMobile;
	}

	public void setUeMainMobile(String ueMainMobile) {
		this.ueMainMobile = ueMainMobile;
	}

	@Column(name = "UE_MAIN_NO1")
	public String getUeMainNo1() {
		return ueMainNo1;
	}

	public void setUeMainNo1(String ueMainNo1) {
		this.ueMainNo1 = ueMainNo1;
	}

	@Column(name = "UE_MAIN_NO2")
	public String getUeMainNo2() {
		return ueMainNo2;
	}

	public void setUeMainNo2(String ueMainNo2) {
		this.ueMainNo2 = ueMainNo2;
	}

	@Column(name = "UE_MAIN_ID1")
	public String getUeMainId1() {
		return ueMainId1;
	}

	public void setUeMainId1(String ueMainId1) {
		this.ueMainId1 = ueMainId1;
	}

	@Column(name = "UE_MAIN_ID2")
	public String getUeMainId2() {
		return ueMainId2;
	}

	public void setUeMainId2(String ueMainId2) {
		this.ueMainId2 = ueMainId2;
	}

	@Column(name = "UE_HELP_NAME")
	public String getUeHelpName() {
		return ueHelpName;
	}

	public void setUeHelpName(String ueHelpName) {
		this.ueHelpName = ueHelpName;
	}

	@Column(name = "UE_HELP_ID")
	public String getUeHelpId() {
		return ueHelpId;
	}

	public void setUeHelpId(String ueHelpId) {
		this.ueHelpId = ueHelpId;
	}

	@Column(name = "UE_HELP_NO")
	public String getUeHelpNo() {
		return ueHelpNo;
	}

	public void setUeHelpNo(String ueHelpNo) {
		this.ueHelpNo = ueHelpNo;
	}

	@Column(name = "UE_HELP_MOBILE")
	public String getUeHelpMobile() {
		return ueHelpMobile;
	}

	public void setUeHelpMobile(String ueHelpMobile) {
		this.ueHelpMobile = ueHelpMobile;
	}

	@Column(name = "UE_HELP_NO1")
	public String getUeHelpNo1() {
		return ueHelpNo1;
	}

	public void setUeHelpNo1(String ueHelpNo1) {
		this.ueHelpNo1 = ueHelpNo1;
	}

	@Column(name = "UE_HELP_NO2")
	public String getUeHelpNo2() {
		return ueHelpNo2;
	}

	public void setUeHelpNo2(String ueHelpNo2) {
		this.ueHelpNo2 = ueHelpNo2;
	}

	@Column(name = "UE_HELP_ID1")
	public String getUeHelpId1() {
		return ueHelpId1;
	}

	public void setUeHelpId1(String ueHelpId1) {
		this.ueHelpId1 = ueHelpId1;
	}

	@Column(name = "UE_HELP_ID2")
	public String getUeHelpId2() {
		return ueHelpId2;
	}

	public void setUeHelpId2(String ueHelpId2) {
		this.ueHelpId2 = ueHelpId2;
	}

	@Column(name = "UE_STATUS")
	public String getUeStatus() {
		return ueStatus;
	}

	public void setUeStatus(String ueStatus) {
		this.ueStatus = ueStatus;
	}

	@Column(name = "UE_AUDIT_USER")
	public String getUeAuditUser() {
		return ueAuditUser;
	}

	public void setUeAuditUser(String ueAuditUser) {
		this.ueAuditUser = ueAuditUser;
	}

	@Column(name = "UE_AUDIT_USERID")
	public String getUeAuditUserId() {
		return ueAuditUserId;
	}

	public void setUeAuditUserId(String ueAuditUserId) {
		this.ueAuditUserId = ueAuditUserId;
	}

	@Column(name = "UE_NG_REASON")
	public String getUeNgReason() {
		return ueNgReason;
	}

	public void setUeNgReason(String ueNgReason) {
		this.ueNgReason = ueNgReason;
	}

	@Column(name = "UE_MAIN_ID")
	public String getUeMainId() {
		return ueMainId;
	}

	public void setUeMainId(String ueMainId) {
		this.ueMainId = ueMainId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="UE_USERID")
	public TUumsBaseUser getTuumsBaseUser() {
		return tuumsBaseUser;
	}

	public void setTuumsBaseUser(TUumsBaseUser tuumsBaseUser) {
		this.tuumsBaseUser = tuumsBaseUser;
	}

}
