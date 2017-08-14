package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the T_INFO_BASE_TOTAL_SCORE database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_TOTAL_SCORE")
public class TInfoBaseTotalScore implements Serializable {
	private static final long serialVersionUID = 1L;

    @Temporal( TemporalType.DATE)
	@Column(name="PUB_DATE")
	private Date pubDate;

    @Id
	@Column(name="PUB_ID")
	private String pubId;

	private BigDecimal score;
	
	@Column(name="ORG_ID")
	private String orgId;

    public TInfoBaseTotalScore() {
    }

	public Date getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getPubId() {
		return this.pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getOrgId()
	{
		return orgId;
	}

	public void setOrgId(String orgId)
	{
		this.orgId = orgId;
	}

}