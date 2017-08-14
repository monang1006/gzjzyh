package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TOaPcCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OA_PC_CATEGORY", schema = "")
public class ToaPcCategory implements java.io.Serializable {

	// Fields
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pccId;
	private String pccName;//类别名称
	private int pccNum;//类别序号
	private String pccOther;//备注

	// Constructors

	/** default constructor */
	public ToaPcCategory() {
	}

	/** full constructor */
	public ToaPcCategory(String pccName, int pccNum, String pccOther) {
		this.pccName = pccName;
		this.pccNum = pccNum;
		this.pccOther = pccOther;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "PCC_ID", unique = true, nullable = false, length = 32)
	public String getPccId() {
		return this.pccId;
	}

	public void setPccId(String pccId) {
		this.pccId = pccId;
	}

	@Column(name = "PCC_NAME", length = 100)
	public String getPccName() {
		return this.pccName;
	}

	public void setPccName(String pccName) {
		this.pccName = pccName;
	}

	@Column(name = "PCC_NUM", length = 32)
	public int getPccNum() {
		return this.pccNum;
	}

	public void setPccNum(int pccNum) {
		this.pccNum = pccNum;
	}

	@Column(name = "PCC_OTHER", length = 1000)
	public String getPccOther() {
		return this.pccOther;
	}

	public void setPccOther(String pccOther) {
		this.pccOther = pccOther;
	}

}