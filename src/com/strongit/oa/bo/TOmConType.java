package com.strongit.oa.bo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 会议类型表（数据字典表）
 * TOmConType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OM_CON_TYPE")
public class TOmConType implements java.io.Serializable {

	// Fields

	private String contypeId;
	private String contypeValue;
	private String contypeName;
	private String contypeDemo;
	private Integer contypeSequence;
	private String rest1;
	private String rest2;
	private String rest3;

	// Constructors

	/** default constructor */
	public TOmConType() {
	}

	/** full constructor */
	public TOmConType(String contypeValue, String contypeName,
			String contypeDemo, int contypeSequence, String rest1,
			String rest2, String rest3) {
		this.contypeValue = contypeValue;
		this.contypeName = contypeName;
		this.contypeDemo = contypeDemo;
		this.contypeSequence = contypeSequence;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
	}

	
	 @Id
	@Column(name="CONTYPE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getContypeId() {
		return this.contypeId;
	}

	public void setContypeId(String contypeId) {
		this.contypeId = contypeId;
	}

	@Column(name = "CONTYPE_VALUE", length = 20)
	public String getContypeValue() {
		return this.contypeValue;
	}

	public void setContypeValue(String contypeValue) {
		this.contypeValue = contypeValue;
	}

	@Column(name = "CONTYPE_NAME", length = 128)
	public String getContypeName() {
		return this.contypeName;
	}

	public void setContypeName(String contypeName) {
		this.contypeName = contypeName;
	}

	@Column(name = "CONTYPE__DEMO", length = 1132)
	public String getContypeDemo() {
		return this.contypeDemo;
	}

	public void setContypeDemo(String contypeDemo) {
		this.contypeDemo = contypeDemo;
	}

	@Column(name = "CONTYPE_SEQUENCE")
	public Integer getContypeSequence() {
		return this.contypeSequence;
	}

	public void setContypeSequence(Integer contypeSequence) {
		this.contypeSequence = contypeSequence;
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
