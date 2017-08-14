package com.strongit.oa.bo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**IPHON  主要记录上一次登入ip
 * TUumsUserandip entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_UUMS_USERANDIP", schema = "")
public class TUumsUserandip implements java.io.Serializable {

	// Fields

	private String id;
	private String userid;
	private String ip;
	private Date logintime;
	/**
	 * 唯一识别设备的标示id
	 */
	private String deviceId;
	/**
	 * 登录状态:0:登陆失败 1:登陆成功
	 */
	private String loginState;
	/**
	 * 登录描述
	 */
	private String loginDesc;
	/**
	 * 设备类型
	 * 设备类型 0:android 1:iphone 2:ipad
	 */
	private String deviceType;
	
	

	// Constructors

	/** default constructor */
	public TUumsUserandip() {
	}
	public TUumsUserandip(String userid, String ip, Date logintime,String deviceId,String loginState,String loginDesc,String deviceType) {
		this.userid = userid;
		this.ip = ip;
		this.logintime = logintime;
		this.deviceId=deviceId;
		this.loginState=loginState;
		this.loginDesc=loginDesc;
		this.deviceType=deviceType;
	}
	/** full constructor */
	public TUumsUserandip(String userid, String ip, Date logintime) {
		this.userid = userid;
		this.ip = ip;
		this.logintime = logintime;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "USERID", length = 32)
	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "IP", length = 32)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGINTIME", length = 7)
	public Date getLogintime() {
		return this.logintime;
	}

	public void setLogintime(Date logintime) {
		this.logintime = logintime;
	}
	@Column(name="DEVICE_ID")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@Column(name="LOGINSTATE")
	public String getLoginState() {
		return loginState;
	}

	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}
	@Column(name="DEVICE_TYPE")
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	@Column(name="LOGINDESC")
	public String getLoginDesc() {
		return loginDesc;
	}

	public void setLoginDesc(String loginDesc) {
		this.loginDesc = loginDesc;
	}

}