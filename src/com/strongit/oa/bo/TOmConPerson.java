package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 会议人员实体类
* 类功能说明 
* 类修改者 修改日期 
* 修改说明 
* <p>Title: TSwConPerson.java</p> 
* <p>Description:省委会务系统</p> 
* <p>Copyright: Copyright (c) 2006</p> 
* <p>Company:思创数码科技股份有限公司</p> 
* @author Jianggb 
* @date 2013-3-21 下午05:16:13 
* @version V1.0
 */
@Entity
@Table(name="T_OM_CONPERSON")
public class TOmConPerson implements Serializable{
	/**
	 * 主键ID
	 */
	private String personId;

	/**
	 * 姓名
	 */
	private String personName;
	/**
	 * 职务
	 */
	private String personPost;

	/**
	 * 办公电话
	 */
	private String personPhone;
	
	/**
	 * EMAIL
	 */
	//private String personEmail;//PERSON_EMAIL
	
	
	private String personSax;//性别 PERSON_SAX


	private String personNation;//民族 PERSON_NATION


	private String personMobile;//手机1 PERSON_MOBILE1
	
	private String rest1;//备用字段
	private String rest2;
	private String rest3;
	private String rest4;

	
	private String depid;
	
	
	public TOmConPerson() {}
	
	
	@Column(name = "DEP_ID")
	public String getDepid() {
		return depid;
	}
	public void setDepid(String depid) {
		this.depid = depid;
	}
	@Id
	@Column(name = "PERSON_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")		
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}


		
	@Column(name = "PERSON_NAME")
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@Column(name = "PERSON_POST")	
	public String getPersonPost() {
		return personPost;
	}
	public void setPersonPost(String personPost) {
		this.personPost = personPost;
	}
	
	
	@Column(name = "PERSON_PHONE")
	public String getPersonPhone() {
		return personPhone;
	}
	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}
	
	
	@Column(name="REST1")
	public String getRest1() {
		return rest1;
	}
	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}
	@Column(name="REST2")
	public String getRest2() {
		return rest2;
	}
	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}
	@Column(name="REST3")
	public String getRest3() {
		return rest3;
	}
	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
	@Column(name="REST4")
	public String getRest4() {
		return rest4;
	}
	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

	@Column(name="PERSON_MOBILE")
	public String getPersonMobile() {
		return personMobile;
	}

	public void setPersonMobile(String personMobile) {
		this.personMobile = personMobile;
	}
	
	@Column(name="PERSON_NATION")
	public String getPersonNation() {
		return personNation;
	}

	public void setPersonNation(String personNation) {
		this.personNation = personNation;
	}
	
	@Column(name="PERSON_SAX")
	public String getPersonSax() {
		return personSax;
	}

	public void setPersonSax(String personSax) {
		this.personSax = personSax;
	}

	
 
	
}
