package com.strongit.oa.bo;

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
 * 参会人员
 * TOmAttenUsers entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OM_ATTEN_USERS")
public class TOmAttenUsers implements java.io.Serializable{


	/** default constructor */
	public TOmAttenUsers() {
	}

	/** full constructor */

	private String userid;
	private TOmDeptreport TOmDeptreport;
	private String sendconId;
	private String userName;
	private String userPet;
	private String userTel;
	private String userMobile;
	private String userEmail;
	private String userRest1;
	private String userRest2;
	private String userRest3;

	// Constructors


	/** full constructor */
	public TOmAttenUsers(TOmDeptreport TOmDeptreport, String sendconId,
			String userName, String userPet, String userTel, String userMobile,
			String userEmail, String userRest1, String userRest2,
			String userRest3) {
		this.TOmDeptreport = TOmDeptreport;
		this.sendconId = sendconId;
		this.userName = userName;
		this.userPet = userPet;
		this.userTel = userTel;
		this.userMobile = userMobile;
		this.userEmail = userEmail;
		this.userRest1 = userRest1;
		this.userRest2 = userRest2;
		this.userRest3 = userRest3;
	}

	// Property accessors
	
   @Id
	@Column(name="USERID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPORTING_ID")
	public TOmDeptreport getTOmDeptreport() {
		return this.TOmDeptreport;
	}

	public void setTOmDeptreport(TOmDeptreport TOmDeptreport) {
		this.TOmDeptreport = TOmDeptreport;
	}

	@Column(name = "SENDCON_ID", length = 32)
	public String getSendconId() {
		return this.sendconId;
	}

	public void setSendconId(String sendconId) {
		this.sendconId = sendconId;
	}

	@Column(name = "USER_NAME", length = 132)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USER_PET", length = 132)
	public String getUserPet() {
		return this.userPet;
	}

	public void setUserPet(String userPet) {
		this.userPet = userPet;
	}

	@Column(name = "USER_TEL", length = 32)
	public String getUserTel() {
		return this.userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	@Column(name = "USER_MOBILE", length = 32)
	public String getUserMobile() {
		return this.userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	@Column(name = "USER_EMAIL", length = 40)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "USER_REST1", length = 32)
	public String getUserRest1() {
		return this.userRest1;
	}

	public void setUserRest1(String userRest1) {
		this.userRest1 = userRest1;
	}

	@Column(name = "USER_REST2", length = 32)
	public String getUserRest2() {
		return this.userRest2;
	}

	public void setUserRest2(String userRest2) {
		this.userRest2 = userRest2;
	}

	@Column(name = "USER_REST3", length = 32)
	public String getUserRest3() {
		return this.userRest3;
	}

	public void setUserRest3(String userRest3) {
		this.userRest3 = userRest3;
	}

}
