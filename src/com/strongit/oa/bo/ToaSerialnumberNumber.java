package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *   @hibernate.class
 *   table=T_OA_SERIALNUMBER_NUMBER
 * @author Administrator
 *
 */
@Entity
@Table(name="T_OA_SERIALNUMBER_NUMBER",catalog="",schema="")
public class ToaSerialnumberNumber implements Serializable{
	
	/** identifier field */
	private String numberId;
	
	/** nullable persistent field */
	private String regulationId;
	
	/** nullable persistent field*/
	private String numberNo;
	
	/** nullable persistent field*/
	private Date numberTime;
	
    private String userId;
    private String userName;
    private String orgId;
    private String orgName;
    private String numberDict;
    private String numberAbbreviation;
    private String numberYear;
    private String numberState;//文号状态 0:已使用，1：预留 2：回收
    
    @Column(name="NUMBER_STATE" ,nullable=true)
    public String getNumberState() {
		return numberState;
	}

	public void setNumberState(String numberState) {
		this.numberState = numberState;
	}

	@Column(name="NUMBER_YEAR" ,nullable=true)
    public String getNumberYear() {
		return numberYear;
	}

	public void setNumberYear(String numberYear) {
		this.numberYear = numberYear;
	}

	@Column(name="NUMBER_ABBREVIATION",nullable=true)
    public String getNumberAbbreviation() {
		return numberAbbreviation;
	}

	public void setNumberAbbreviation(String numberAbbreviation) {
		this.numberAbbreviation = numberAbbreviation;
	}

	@Column(name="USER_ID",nullable=true)
	 public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="USER_NAME",nullable=true)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="ORG_ID",nullable=true)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name="ORG_NAME",nullable=true)
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	/** full constructor */
	public ToaSerialnumberNumber(String numberId,String regulationId,String numberNo,Date numberTime){
		this.numberId=numberId;
		this.regulationId=regulationId;
		this.numberNo=numberNo;
		this.numberTime=numberTime;
	}
	
	/** defaule constructor*/
	public ToaSerialnumberNumber(){
		
	}
	
	/** minimal constructor*/
	public ToaSerialnumberNumber(String numberId){
		this.numberId=numberId;
	}
	
	 /** 
	    *            @hibernate.id
	    *             generator-class="assigned"
	    *             type="java.lang.String"
	    *             column="NUMBER_ID"
	    *         
	    */
	@Id
	@Column(name="NUMBER_ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getNumberId() {
		return numberId;
	}
	public void setNumberId(String numberId) {
		this.numberId = numberId;
	}
	
	/**
	 *      @hibernate.property
	 *      column="REGULATION_ID"
	 *      length="32"
	 * 
	 */
	@Column(name="REGULATION_ID",nullable=true)
	public String getRegulationId() {
		return regulationId;
	}
	public void setRegulationId(String regulationId) {
		this.regulationId = regulationId;
	}
	
	/**
	 *              @hibernate.property
	 *              column="NUMBER_NO"
	 *              length="50"
	 * 
	 */
	@Column(name="NUMBER_NO",nullable=true)
	public String getNumberNo() {
		return numberNo;
	}
	public void setNumberNo(String numberNo) {
		this.numberNo = numberNo;
	}
	
	/**
	 *               @hibernate.property
	 *               column="NUMBER_TIME"
	 * 
	 */
	@Column(name="NUMBER_TIME",nullable=true)
	public Date getNumberTime() {
		return numberTime;
	}
	public void setNumberTime(Date numberTime) {
		this.numberTime = numberTime;
	}

	@Column(name="NUMBER_DICT",nullable=true)
	public String getNumberDict() {
		return numberDict;
	}

	public void setNumberDict(String numberDict) {
		this.numberDict = numberDict;
	}

}
