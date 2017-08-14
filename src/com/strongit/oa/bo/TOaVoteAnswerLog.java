package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author piyu Jun 23, 2010 用户回答的详情记录
 */
@Entity
@Table(name = "T_OA_VOTE_ANSWER_LOG")
public class TOaVoteAnswerLog {
	@Id
	@GeneratedValue	
	private String logid;
	private String aid;
	private String userid;
	private String mobile;
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
	@Column(name="AID", nullable=false)
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	@Column(name="USERID", nullable=true)
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	@Column(name="MOBILE", nullable=true)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
