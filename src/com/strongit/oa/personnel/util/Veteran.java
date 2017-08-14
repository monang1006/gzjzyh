package com.strongit.oa.personnel.util;

import java.io.Serializable;
import java.util.Date;

public class Veteran implements Serializable {
	private String personId;
	private String name;
	private String sex;
	private String labourNO;
	private Date birthDate;
	private String nativePlace;
	private String nation;
	private String health;
	private String type;		//待遇级别
	
	private String strucId;		//编制
	private String orgId;
	private String code;
	private String status;		//个人身份
	private String cardId;
	private String kind;		//人员类别
	private String post;		//职位
	private Date beginWork;
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getLabourNO() {
		return labourNO;
	}
	public void setLabourNO(String labourNO) {
		this.labourNO = labourNO;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getNativePlace() {
		return nativePlace;
	}
	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Veteran(String personId, String name, String sex, String labourNO,
			Date birthDate, String nativePlace, String nation, String health,
			String type) {
		super();
		this.personId = personId;
		this.name = name;
		this.sex = sex;
		this.labourNO = labourNO;
		this.birthDate = birthDate;
		this.nativePlace = nativePlace;
		this.nation = nation;
		this.health = health;
		this.type = type;
	}
	public Veteran() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getStrucId() {
		return strucId;
	}
	public void setStrucId(String strucId) {
		this.strucId = strucId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public Date getBeginWork() {
		return beginWork;
	}
	public void setBeginWork(Date beginWork) {
		this.beginWork = beginWork;
	}
	
}
