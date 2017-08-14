package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_COLUMN database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_COLUMN")
public class TInfoBaseColumn implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="COL_ID")
	private String colId;

	@Column(name="COL_DETAIL")
	private String colDetail;

	@Column(name="COL_NAME")
	private String colName;
	
	@Column(name="COL_SORT")
	private String colSort;
	
	@Column(name="COL_CODE")
	private Integer colCode;

	//bi-directional many-to-one association to TInfoBaseJournal
    @ManyToOne
	@JoinColumn(name="JOUR_ID")
	private TInfoBaseJournal TInfoBaseJournal;

    public TInfoBaseColumn() {
    }

	public String getColId() {
		return this.colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getColDetail() {
		return this.colDetail;
	}

	public void setColDetail(String colDetail) {
		this.colDetail = colDetail;
	}

	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public TInfoBaseJournal getTInfoBaseJournal() {
		return this.TInfoBaseJournal;
	}

	public void setTInfoBaseJournal(TInfoBaseJournal TInfoBaseJournal) {
		this.TInfoBaseJournal = TInfoBaseJournal;
	}

	public String getColSort() {
		return colSort;
	}

	public void setColSort(String colSort) {
		this.colSort = colSort;
	}

	public Integer getColCode() {
		return colCode;
	}

	public void setColCode(Integer colCode) {
		this.colCode = colCode;
	}
	
	
	
}