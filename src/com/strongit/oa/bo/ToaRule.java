package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_RULE")
public class ToaRule {
	
	private static final long serialVersionUID = 6847769209111277965L;
	private String id;
	private String ruleName;
	private String rule;
	private String orgId;
	private String orgCode;
	public ToaRule(){
		
	}
	public ToaRule(String id,String ruleName,String rule){
		this.id=id;
		this.ruleName=ruleName;
		this.rule=rule;
	}
	@Id
    @Column(name="ID",nullable=true,length=32)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name="RULENAME")
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	@Column(name="RULE_")
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	@Column(name="ORG_CODE")
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Column(name="ORG_ID")
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	 
}
