package com.strongit.oa.bo;


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
 * The persistent class for the T_OA_RELATIVE_WORKFLOW database table.
 * 
 */
@Entity
@Table(name="T_OA_RELATIVE_WORKFLOW")
public class TOaRelativeWorkflow implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@Column(name = "RW_ID")
	private String rwId;

	@Column(name="PI_ID")
	private Long piId;

	@Column(name="PI_REF_ID")
	private Long piRefId;

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="RW_DATE")
	private Date rwDate;

    public TOaRelativeWorkflow() {
    }

	public String getRwId() {
		return this.rwId;
	}

	public void setRwId(String rwId) {
		this.rwId = rwId;
	}

	public Long getPiId() {
		return this.piId;
	}

	public void setPiId(Long piId) {
		this.piId = piId;
	}

	public Long getPiRefId() {
		return this.piRefId;
	}

	public void setPiRefId(Long piRefId) {
		this.piRefId = piRefId;
	}

	public Date getRwDate() {
		return this.rwDate;
	}

	public void setRwDate(Date rwDate) {
		this.rwDate = rwDate;
	}

}