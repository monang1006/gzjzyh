package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


/**
 * The persistent class for the T_INFO_BASE_ISSUE database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_ISSUE")
public class TInfoBaseIssue implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="ISS_ID")
	private String issId;

	@Column(name="ISS_IS_PUBLISH")
	private String issIsPublish;

	@Column(name="ISS_MONTH")
	private BigDecimal issMonth;

	@Column(name="ISS_NUMBER")
	private String issNumber;

	@Column(name="ISS_YEAR")
	private BigDecimal issYear;
	
	//期号发布时间
	@Column(name="ISS_TIME")
	private Date issTime;
	
	//期号生成时间
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="ISS_DATE")
	private Date issDate;
	
    @Lob()
    //@Type(type="org.springframework.orm.hibernate3.support.BlobByteArrayType")
	@Column(name="ISS_CONTENT")
	private byte[] issContent;

	//bi-directional many-to-one association to TInfoBaseJournal
    @ManyToOne
	@JoinColumn(name="JOUR_ID")
	private TInfoBaseJournal TInfoBaseJournal;

    public TInfoBaseIssue() {
    }

	public String getIssId() {
		return this.issId;
	}

	public void setIssId(String issId) {
		this.issId = issId;
	}

	public String getIssIsPublish() {
		return this.issIsPublish;
	}

	public void setIssIsPublish(String issIsPublish) {
		this.issIsPublish = issIsPublish;
	}

	public BigDecimal getIssMonth() {
		return this.issMonth;
	}

	public void setIssMonth(BigDecimal issMonth) {
		this.issMonth = issMonth;
	}

	public String getIssNumber() {
		return this.issNumber;
	}

	public void setIssNumber(String issNumber) {
		this.issNumber = issNumber;
	}

	public BigDecimal getIssYear() {
		return this.issYear;
	}

	public void setIssYear(BigDecimal issYear) {
		this.issYear = issYear;
	}

	public TInfoBaseJournal getTInfoBaseJournal() {
		return this.TInfoBaseJournal;
	}

	public void setTInfoBaseJournal(TInfoBaseJournal TInfoBaseJournal) {
		this.TInfoBaseJournal = TInfoBaseJournal;
	}

	public Date getIssDate()
	{
		return issDate;
	}

	public void setIssDate(Date issDate)
	{
		this.issDate = issDate;
	}

	public byte[] getIssContent()
	{
		return issContent;
	}

	public void setIssContent(byte[] issContent)
	{
		this.issContent = issContent;
	}

	public Date getIssTime() {
		return issTime;
	}

	public void setIssTime(Date issTime) {
		this.issTime = issTime;
	}
	
	
}