package com.strongit.oa.bo;

import java.util.Date;

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
 * @author piyu Jun 18, 2010
 * 
 */
@Entity
@Table(name = "T_OA_VOTE_LOG")
public class TOaVoteLog {
	@Id
	@GeneratedValue	
	private String logid;
	private String userid;
	private String username;
	private String IP;
	private String mobile;
	private Date vote_date;
	private TOaVote vote;
	@Id
	@Column(name="LOGID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getLogid() {
		return logid;
	}

	public void setLogid(String logid) {
		this.logid = logid;
	}
	@Column(name="USERID", nullable=true)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Column(name="USERNAME", nullable=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="IP", nullable=true)
	public String getIP() {
		return IP;
	}

	public void setIP(String ip) {
		IP = ip;
	}
	@Column(name="MOBILE", nullable=true)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column(name="VOTE_DATE", nullable=true)
	public Date getVote_date() {
		return vote_date;
	}

	public void setVote_date(Date vote_date) {
		this.vote_date = vote_date;
	}
	@ManyToOne(optional=false, fetch = FetchType.LAZY)
	@JoinColumn(name="vid", nullable=true)
	public TOaVote getVote() {
		return vote;
	}

	public void setVote(TOaVote vote) {
		this.vote = vote;
	}

}
