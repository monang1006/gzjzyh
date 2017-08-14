package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * The persistent class for the T_INFO_BASE_UNSAVE_MAIL database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_UNSAVE_MAIL")
public class TInfoBaseUnsaveMail implements Serializable {
	private static final long serialVersionUID = 1L;

    @Lob()
    //@Type(type="org.springframework.orm.hibernate3.support.ClobStringType")
	private String content;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	private String id;

	@Column(name="MAIL_ADDRESS")
	private String mailAddress;

	@Column(name="MAIL_UID")
	private String mailUid;

	@Column(name="ORG_ID")
	private String orgId;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="SENT_DATE")
	private Date sentDate;

	private String subject;

	@Column(name="USER_ID")
	private String userId;

    public TInfoBaseUnsaveMail() {
    }

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getMailUid() {
		return this.mailUid;
	}

	public void setMailUid(String mailUid) {
		this.mailUid = mailUid;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Date getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}