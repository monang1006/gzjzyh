package com.strongit.gzjzyh.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the T_GZJZYH_CASE database table.
 * 
 */
@Entity
@Table(name = "T_GZJZYH_CASE")
public class TGzjzyhCase implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "CASE_ID")
	private String caseId;

	@Column(name = "CASE_CODE")
	private String caseCode;

	@Column(name = "CASE_NAME")
	private String caseName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CASE_CONFIRMTIME")
	private Date caseConfirmTime;

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public Date getCaseConfirmTime() {
		return caseConfirmTime;
	}

	public void setCaseConfirmTime(Date caseConfirmTime) {
		this.caseConfirmTime = caseConfirmTime;
	}

}