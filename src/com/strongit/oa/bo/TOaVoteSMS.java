package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author piyu Jun 22, 2010
 * 
 */
@Entity
@Table(name = "T_OA_VOTE_SMS")
/**
 * 短信问题使用的编号
 */
public class TOaVoteSMS {
	private String qid;
	@Id
	@GeneratedValue	
	private String code;

	@Id
	@Column(name="code", nullable=false, length=32)
	@GeneratedValue(generator = "myGenerator")     
	@GenericGenerator(name = "myGenerator", strategy = "assigned")   
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column(name="QID", nullable=false, length=32)
	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	

}
