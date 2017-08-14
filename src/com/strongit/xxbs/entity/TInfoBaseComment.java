package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_COLUMN database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_COMMENT")
public class TInfoBaseComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="COM_ID")
	private String comId;

	@Column(name="COM_USER_ID")
	private String comUserId;

	@Column(name="COM_PUBLISH_ID")
	private String comPublishId;

	@Column(name="COM_INFO")
	private String comInfo;
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="COM_DATE")
	private Date comDate;

    public TInfoBaseComment() {
    }

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getComUserId() {
		return comUserId;
	}

	public void setComUserId(String comUserId) {
		this.comUserId = comUserId;
	}

	public String getComPublishId() {
		return comPublishId;
	}

	public void setComPublishId(String comPublishId) {
		this.comPublishId = comPublishId;
	}

	public String getComInfo() {
		return comInfo;
	}

	public void setComInfo(String comInfo) {
		this.comInfo = comInfo;
	}

	public Date getComDate() {
		return comDate;
	}

	public void setComDate(Date comDate) {
		this.comDate = comDate;
	}

	

	
	
	
	
}