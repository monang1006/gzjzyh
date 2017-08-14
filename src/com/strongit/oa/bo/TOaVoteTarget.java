package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author piyu
 * Jun 18, 2010
 *
 */

@Entity
@Table(name="T_OA_VOTE_TARGET")
public class TOaVoteTarget implements java.io.Serializable {
	@Id
	@GeneratedValue	
	private String tid;
	private String vid; //问卷ID
	private String username ;//用户名
	private String userid; //
	private String mobile ; //
	
	@Id
	@Column(name="TID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	@Column(name="VID", nullable=true)
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
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
	@Column(name="USERNAME", nullable=true)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
  
}
