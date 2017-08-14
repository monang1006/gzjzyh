package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the T_INFO_BASE_SCORE database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_SCORE")
public class TInfoBaseScore implements Serializable {
	private static final long serialVersionUID = 1L;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="SC_DATE")
	private Date scDate;

	@Column(name="SC_DETAIL")
	private String scDetail;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="SC_ID")
	private String scId;

	@Column(name="SC_NAME")
	private String scName;

	@Column(name="SC_SCORE")
	private BigDecimal scScore;
	
	@Column(name="SC_STATE")
	private String scState;


    public TInfoBaseScore() {
    }

	public Date getScDate() {
		return this.scDate;
	}

	public void setScDate(Date scDate) {
		this.scDate = scDate;
	}

	public String getScDetail() {
		return this.scDetail;
	}

	public void setScDetail(String scDetail) {
		this.scDetail = scDetail;
	}

	public String getScId() {
		return this.scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getScName() {
		return this.scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public BigDecimal getScScore() {
		return this.scScore;
	}

	public void setScScore(BigDecimal scScore) {
		this.scScore = scScore;
	}

	public String getScState() {
		return scState;
	}

	public void setScState(String scState) {
		this.scState = scState;
	}
	
	
	
}