package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOsWorkType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OS_WORK_TYPE")
public class TOsWorkType implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String worktypeId;
	private String worktypeValue;// 字典值
	private String worktypeName;// 名称
	private String worktypeDemo;// 备注
	private int worktypeSequence=1;// 排序号
	private String rest1;
	private String rest2;
	private String rest3;

	// Constructors

	/** default constructor */
	public TOsWorkType() {
	}

	/** full constructor */
	public TOsWorkType(String worktypeValue, String worktypeName,
			String worktypeDemo, int worktypeSequence, String rest1,
			String rest2, String rest3) {
		this.worktypeValue = worktypeValue;
		this.worktypeName = worktypeName;
		this.worktypeDemo = worktypeDemo;
		this.worktypeSequence = worktypeSequence;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "WORKTYPE_ID", unique = true, nullable = false, length = 32)
	public String getWorktypeId() {
		return this.worktypeId;
	}

	public void setWorktypeId(String worktypeId) {
		this.worktypeId = worktypeId;
	}

	@Column(name = "WORKTYPE_VALUE", length = 20)
	public String getWorktypeValue() {
		return this.worktypeValue;
	}

	public void setWorktypeValue(String worktypeValue) {
		this.worktypeValue = worktypeValue;
	}

	@Column(name = "WORKTYPE_NAME", length = 128)
	public String getWorktypeName() {
		return this.worktypeName;
	}

	public void setWorktypeName(String worktypeName) {
		this.worktypeName = worktypeName;
	}

	@Column(name = "WORKTYPE__DEMO", length = 1132)
	public String getWorktypeDemo() {
		return this.worktypeDemo;
	}

	public void setWorktypeDemo(String worktypeDemo) {
		this.worktypeDemo = worktypeDemo;
	}

	@Column(name = "WORKTYPE_SEQUENCE")
	public int getWorktypeSequence() {
		return this.worktypeSequence;
	}

	public void setWorktypeSequence(int worktypeSequence) {
		this.worktypeSequence = worktypeSequence;
	}

	@Column(name = "REST1", length = 32)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 32)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 32)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
}
