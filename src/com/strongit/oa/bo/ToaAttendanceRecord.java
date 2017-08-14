package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_AttendanceRecord"
 *     
*/
@Entity
@Table(name="T_OA_AttendanceRecord")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ToaAttendanceRecord implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	@Id 
	@GeneratedValue
    private String id;
	
	private String userId;
	
	private String userName;
	
	private Date time;
	
	private String worktime;
	
	private String leavetime;
	
	private int latetime;
	
	private int leaveearly;
	
	private int nowoke;
	
	private int type;
	
	private String sremarks;
	
	private String qremarks;
	
	private String sIP;
	
	private String qIP;

 
    /** default constructor */
    public ToaAttendanceRecord() {
    }


   
	public ToaAttendanceRecord(String id, String userId, String userName,
			Date time, String worktime, String leavetime, int latetime,
			int leaveearly, int nowoke, int type, String sremarks,
			String qremarks, String sIP, String qIP) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.time = time;
		this.worktime = worktime;
		this.leavetime = leavetime;
		this.latetime = latetime;
		this.leaveearly = leaveearly;
		this.nowoke = nowoke;
		this.type = type;
		this.sremarks = sremarks;
		this.qremarks = qremarks;
		this.sIP = sIP;
		this.qIP = qIP;
	}


	@Id
	@Column(name="ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Column(name="TIME",nullable=true)
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name="WORKTIME",nullable=true)
	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	@Column(name="LEAVETIME",nullable=true)
	public String getLeavetime() {
		return leavetime;
	}

	public void setLeavetime(String leavetime) {
		this.leavetime = leavetime;
	}


	@Column(name="USERID",nullable=true)
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	@Column(name="USERNAME",nullable=true)
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Column(name="LATETIME",nullable=true)
	public int getLatetime() {
		return latetime;
	}


	public void setLatetime(int latetime) {
		this.latetime = latetime;
	}


	@Column(name="LEAVEEARLY",nullable=true)
	public int getLeaveearly() {
		return leaveearly;
	}


	public void setLeaveearly(int leaveearly) {
		this.leaveearly = leaveearly;
	}


	@Column(name="NOWOKE",nullable=true)
	public int getNowoke() {
		return nowoke;
	}


	public void setNowoke(int nowoke) {
		this.nowoke = nowoke;
	}



	/**
	 * 0：旷工（默认值）
	   1：迟到
       2：早退
	   3：上班未打卡
	   4：下班未打卡
	   5：迟到并早退
	   6：正常
	 * @return
	 */
	@Column(name="TYPE",nullable=true)
	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	@Column(name="SREMARKS",nullable=true)
	public String getSremarks() {
		return sremarks;
	}


	public void setSremarks(String sremarks) {
		this.sremarks = sremarks;
	}


	@Column(name="QREMARKS",nullable=true)
	public String getQremarks() {
		return qremarks;
	}


	public void setQremarks(String qremarks) {
		this.qremarks = qremarks;
	}


	@Column(name="SIP",nullable=true)
	public String getsIP() {
		return sIP;
	}


	public void setsIP(String sIP) {
		this.sIP = sIP;
	}


	@Column(name="QIP",nullable=true)
	public String getqIP() {
		return qIP;
	}


	public void setqIP(String qIP) {
		this.qIP = qIP;
	}

    

}
