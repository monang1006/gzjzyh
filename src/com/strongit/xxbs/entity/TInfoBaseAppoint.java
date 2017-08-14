package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_APPOINT database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_APPOINT")
public class TInfoBaseAppoint implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="APT_ID")
	private String aptId;

	@Column(name="APT_CONTENT")
	private String aptContent;

    @Temporal( TemporalType.DATE)
	@Column(name="APT_DATE")
	private Date aptDate;

    @Temporal( TemporalType.DATE)
	@Column(name="APT_DUEDATE")
	private Date aptDuedate;

	@Column(name="APT_TITLE")
	private String aptTitle;

	@Column(name="APT_USERID")
	private String aptUserid;
	
	@Column(name="APT_IS_ALL_ORG")
	private String aptIsAllOrg;

    public TInfoBaseAppoint() {
    }

	public String getAptId() {
		return this.aptId;
	}

	public void setAptId(String aptId) {
		this.aptId = aptId;
	}

	public String getAptContent() {
		return this.aptContent;
	}

	public void setAptContent(String aptContent) {
		this.aptContent = aptContent;
	}

	public Date getAptDate() {
		return this.aptDate;
	}

	public void setAptDate(Date aptDate) {
		this.aptDate = aptDate;
	}

	public Date getAptDuedate() {
		return this.aptDuedate;
	}

	public void setAptDuedate(Date aptDuedate) {
		this.aptDuedate = aptDuedate;
	}

	public String getAptTitle() {
		return this.aptTitle;
	}

	public void setAptTitle(String aptTitle) {
		this.aptTitle = aptTitle;
	}

	public String getAptUserid() {
		return this.aptUserid;
	}

	public void setAptUserid(String aptUserid) {
		this.aptUserid = aptUserid;
	}

	public String getAptIsAllOrg()
	{
		return aptIsAllOrg;
	}

	public void setAptIsAllOrg(String aptIsAllOrg)
	{
		this.aptIsAllOrg = aptIsAllOrg;
	}
	
}