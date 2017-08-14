package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;


/**
 * The persistent class for the T_INFO_BASE_BULLETIN database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_BULLETIN")
public class TInfoBaseBulletin implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="BL_ID")
	private String blId;

	@Column(name="BL_CONTENT")
	private String blContent;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="BL_DATE")
	private Date blDate;

	@Column(name="BL_TITLE")
	private String blTitle;

    public TInfoBaseBulletin() {
    }

	public String getBlId() {
		return this.blId;
	}

	public void setBlId(String blId) {
		this.blId = blId;
	}

	public String getBlContent() {
		return this.blContent;
	}

	public void setBlContent(String blContent) {
		this.blContent = blContent;
	}

	public Date getBlDate() {
		return this.blDate;
	}

	public void setBlDate(Date blDate) {
		this.blDate = blDate;
	}

	public String getBlTitle() {
		return this.blTitle;
	}

	public void setBlTitle(String blTitle) {
		this.blTitle = blTitle;
	}

}