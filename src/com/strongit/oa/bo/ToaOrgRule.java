package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class table="T_OA_ORG_RULE"
 * 
 */
@Entity
@Table(name = "T_OA_ORG_RULE", catalog = "", schema = "")
public class ToaOrgRule implements Serializable {
	
	/** identifier field ID */
	private String tid;

	/** nullable persistent field ORG_ID */
	private String orgId;

	/** nullable persistent field DEPT_ID */
	private String deptId;

	/** nullable persistent field  RULE_ID*/
	private String ruleId;

	
	public ToaOrgRule() {
	}

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getTid() {
		return tid;
	}


	public void setTid(String tid) {
		this.tid = tid;
	}
	
	@Column(name = "ORG_ID")
	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "DEPT_ID")
	public String getDeptId() {
		return deptId;
	}


	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "RULE_ID")
	public String getRuleId() {
		return ruleId;
	}


	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}


}
