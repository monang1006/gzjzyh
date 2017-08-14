package com.strongit.xxbs.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_JOURNAL database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_JOURNAL")
public class TInfoBaseJournal implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="JOUR_ID")
	private String jourId;

    @Temporal( TemporalType.DATE)
	@Column(name="JOUR_DATE")
	private Date jourDate;

	@Column(name="JOUR_IS_DEFAULT")
	private String jourIsDefault;

	@Column(name="JOUR_DETAIL")
	private String jourDetail;

	@Column(name="JOUR_NAME")
	private String jourName;
	
	@Column(name="JOUR_CODE")
	private Integer jourCode;

	//bi-directional many-to-one association to TInfoBaseIssue
	@OneToMany(mappedBy="TInfoBaseJournal")
	@OrderBy("issNumber desc")
	private Set<TInfoBaseIssue> TInfoBaseIssues;

	//bi-directional many-to-one association to TInfoBaseColumn
	@OneToMany(mappedBy="TInfoBaseJournal")
	@OrderBy("colSort")
	private Set<TInfoBaseColumn> TInfoBaseColumns;
	
	@OneToOne
	@JoinColumn(name="WT_ID")
	private TInfoBaseWordTemplate TInfoBaseWordTemplate;

    public TInfoBaseJournal() {
    }

	public String getJourId() {
		return this.jourId;
	}

	public void setJourId(String jourId) {
		this.jourId = jourId;
	}

	public Date getJourDate() {
		return this.jourDate;
	}

	public void setJourDate(Date jourDate) {
		this.jourDate = jourDate;
	}

	public String getJourDetail() {
		return this.jourDetail;
	}

	public void setJourDetail(String jourDetail) {
		this.jourDetail = jourDetail;
	}

	public String getJourName() {
		return this.jourName;
	}

	public void setJourName(String jourName) {
		this.jourName = jourName;
	}

	public Set<TInfoBaseIssue> getTInfoBaseIssues() {
		return this.TInfoBaseIssues;
	}

	public void setTInfoBaseIssues(Set<TInfoBaseIssue> TInfoBaseIssues) {
		this.TInfoBaseIssues = TInfoBaseIssues;
	}
	
	public Set<TInfoBaseColumn> getTInfoBaseColumns() {
		return this.TInfoBaseColumns;
	}

	public void setTInfoBaseColumns(Set<TInfoBaseColumn> TInfoBaseColumns) {
		this.TInfoBaseColumns = TInfoBaseColumns;
	}

	public String getJourIsDefault()
	{
		return jourIsDefault;
	}

	public void setJourIsDefault(String jourIsDefault)
	{
		this.jourIsDefault = jourIsDefault;
	}

	public TInfoBaseWordTemplate getTInfoBaseWordTemplate()
	{
		return TInfoBaseWordTemplate;
	}

	public void setTInfoBaseWordTemplate(TInfoBaseWordTemplate tInfoBaseWordTemplate)
	{
		TInfoBaseWordTemplate = tInfoBaseWordTemplate;
	}

	public Integer getJourCode() {
		return jourCode;
	}

	public void setJourCode(Integer jourCode) {
		this.jourCode = jourCode;
	}

	
	
	
}