package com.strongit.xxbs.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_APPOINT_TO database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_APPOINT_TO")
public class TInfoBaseAppointTo implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="APTT_ID")
	private String apttId;

	@Column(name="APT_ID")
	private String aptId;

	@Column(name="ORG_ID")
	private String orgId;

    public TInfoBaseAppointTo() {
    }

	public String getApttId() {
		return this.apttId;
	}

	public void setApttId(String apttId) {
		this.apttId = apttId;
	}

	public String getAptId() {
		return this.aptId;
	}

	public void setAptId(String aptId) {
		this.aptId = aptId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

}