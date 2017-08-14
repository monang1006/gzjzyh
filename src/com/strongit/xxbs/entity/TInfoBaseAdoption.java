package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the T_INFO_BASE_ADOPTION database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_ADOPTION")
public class TInfoBaseAdoption implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="PUB_ID")
	private String pubId;

    @Temporal( TemporalType.DATE)
	@Column(name="ADO_DATE")
	private Date adoDate;

	@Column(name="ADO_INSTRUCTION_CONTENT")
	private String adoInstructionContent;

	@Column(name="ADO_INSTRUCTION_SCORE")
	private BigDecimal adoInstructionScore;

	@Column(name="ADO_INSTRUCTOR")
	private String adoInstructor;

	@Column(name="ADO_USE_CONTENT")
	private String adoUseContent;

	@Column(name="ADO_USE_SCORE")
	private BigDecimal adoUseScore;

	@OneToOne
	@JoinColumn(name="COL_ID")
	private TInfoBaseColumn TInfoBaseColumn;

	@Column(name="JV_ID")
	private String jvId;

    public TInfoBaseAdoption() {
    }

	public Date getAdoDate() {
		return this.adoDate;
	}

	public void setAdoDate(Date adoDate) {
		this.adoDate = adoDate;
	}

	public String getAdoInstructionContent() {
		return this.adoInstructionContent;
	}

	public void setAdoInstructionContent(String adoInstructionContent) {
		this.adoInstructionContent = adoInstructionContent;
	}

	public BigDecimal getAdoInstructionScore() {
		return this.adoInstructionScore;
	}

	public void setAdoInstructionScore(BigDecimal adoInstructionScore) {
		this.adoInstructionScore = adoInstructionScore;
	}

	public String getAdoInstructor() {
		return this.adoInstructor;
	}

	public void setAdoInstructor(String adoInstructor) {
		this.adoInstructor = adoInstructor;
	}

	public String getAdoUseContent() {
		return this.adoUseContent;
	}

	public void setAdoUseContent(String adoUseContent) {
		this.adoUseContent = adoUseContent;
	}

	public BigDecimal getAdoUseScore() {
		return this.adoUseScore;
	}

	public void setAdoUseScore(BigDecimal adoUseScore) {
		this.adoUseScore = adoUseScore;
	}

	public String getJvId() {
		return this.jvId;
	}

	public void setJvId(String jvId) {
		this.jvId = jvId;
	}

	public TInfoBaseColumn getTInfoBaseColumn()
	{
		return TInfoBaseColumn;
	}

	public void setTInfoBaseColumn(TInfoBaseColumn tInfoBaseColumn)
	{
		TInfoBaseColumn = tInfoBaseColumn;
	}

	public String getPubId()
	{
		return pubId;
	}

	public void setPubId(String pubId)
	{
		this.pubId = pubId;
	}

}