package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_BASE_VETERAN"
 *     
*/
@Entity
@Table(name = "T_OA_BASE_VETERAN", catalog = "", schema = "")
public class ToaBaseVeteran implements Serializable {

    /** identifier field */
    private String personId;//人员主键

    /** persistent field */
    private String personCode;//人员编码

    /** nullable persistent field */
    private String personName;//人员姓名

    /** nullable persistent field */
    private String personSax;//人员性别

    /** nullable persistent field */
    private String personLabourno;//人员工号

    /** nullable persistent field */
    private Date personBorn;//人员出生日期

    /** nullable persistent field */
    private String personNativeplace;//人员籍贯

    /** nullable persistent field */
    private String personNation;//民族

    /** nullable persistent field */
    private String personHealthState;//健康状况

    /** nullable persistent field */
    private String personStatus;//个人身份

    /** nullable persistent field */
    private String personCardId;//身份证号

    /** nullable persistent field */
    private String personTreatmentLevel;//享受待遇级别

    /** nullable persistent field */
    private String personPersonKind;//人员类别

    /** nullable persistent field */
    private String personPset;//职位

    /** nullable persistent field */
    private Date personRetireTime;//离退时间
    
	private com.strongit.oa.bo.ToaBaseOrg baseOrg;//所属单位
	
	private String personIsdel;//是否销毁删除

   @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
    public com.strongit.oa.bo.ToaBaseOrg getBaseOrg() {
		return baseOrg;
	}

	public void setBaseOrg(com.strongit.oa.bo.ToaBaseOrg baseOrg) {
		this.baseOrg = baseOrg;
	}

	/** full constructor */
    public ToaBaseVeteran(String personId, String personCode, String personName, String personSax, String personLabourno, Date personBorn, String personNativeplace, String personNation, String personHealthState, String personStatus, String personCardId, String personTreatmentLevel, String personPersonKind, String personPset) {
        this.personId = personId;
        this.personCode = personCode;
        this.personName = personName;
        this.personSax = personSax;
        this.personLabourno = personLabourno;
        this.personBorn = personBorn;
        this.personNativeplace = personNativeplace;
        this.personNation = personNation;
        this.personHealthState = personHealthState;
        this.personStatus = personStatus;
        this.personCardId = personCardId;
        this.personTreatmentLevel = personTreatmentLevel;
        this.personPersonKind = personPersonKind;
        this.personPset = personPset;
    }

    /** default constructor */
    public ToaBaseVeteran() {
    }

    /** minimal constructor */
    public ToaBaseVeteran(String personId, String personCode) {
        this.personId = personId;
        this.personCode = personCode;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PERSON_ID"
     *         
     */
    
	@Id
	@Column(name = "PERSON_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

  
   

    /** 
     *            @hibernate.property
     *             column="PERSON_CODE"
     *             length="20"
     *             not-null="true"
     *         
     */
	@Column(name = "PERSON_CODE")
    public String getPersonCode() {
        return this.personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_NAME"
     *             length="50"
     *         
     */
	@Column(name = "PERSON_NAME")
    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_SAX"
     *             length="20"
     *         
     */
	@Column(name = "PERSON_SAX")
    public String getPersonSax() {
        return this.personSax;
    }

    public void setPersonSax(String personSax) {
        this.personSax = personSax;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_LABOURNO"
     *             length="10"
     *         
     */
	@Column(name = "PERSON_LABOURNO")
    public String getPersonLabourno() {
        return this.personLabourno;
    }

    public void setPersonLabourno(String personLabourno) {
        this.personLabourno = personLabourno;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_BORN"
     *             length="7"
     *         
     */
	@Column(name = "PERSON_BORN")
    public Date getPersonBorn() {
        return this.personBorn;
    }

    public void setPersonBorn(Date personBorn) {
        this.personBorn = personBorn;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_NATIVEPLACE"
     *             length="100"
     *         
     */
	@Column(name = "PERSON_NATIVEPLACE")
    public String getPersonNativeplace() {
        return this.personNativeplace;
    }

    public void setPersonNativeplace(String personNativeplace) {
        this.personNativeplace = personNativeplace;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_NATION"
     *             length="20"
     *         
     */
	@Column(name = "PERSON_NATION")
    public String getPersonNation() {
        return this.personNation;
    }

    public void setPersonNation(String personNation) {
        this.personNation = personNation;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_HEALTH_STATE"
     *             length="20"
     *         
     */
	@Column(name = "PERSON_HEALTH_STATE")
    public String getPersonHealthState() {
        return this.personHealthState;
    }

    public void setPersonHealthState(String personHealthState) {
        this.personHealthState = personHealthState;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_STATUS"
     *             length="20"
     *         
     */
	@Column(name = "PERSON_STATUS")
    public String getPersonStatus() {
        return this.personStatus;
    }

    public void setPersonStatus(String personStatus) {
        this.personStatus = personStatus;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_CARD_ID"
     *             length="18"
     *         
     */
	@Column(name = "PERSON_CARD_ID")
    public String getPersonCardId() {
        return this.personCardId;
    }

    public void setPersonCardId(String personCardId) {
        this.personCardId = personCardId;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_TREATMENT_LEVEL"
     *             length="20"
     *         
     */
	@Column(name = "PERSON_TREATMENT_LEVEL")
    public String getPersonTreatmentLevel() {
        return this.personTreatmentLevel;
    }

    public void setPersonTreatmentLevel(String personTreatmentLevel) {
        this.personTreatmentLevel = personTreatmentLevel;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_PERSON_KIND"
     *             length="20"
     *         
     */
	@Column(name = "PERSON_PERSON_KIND")
    public String getPersonPersonKind() {
        return this.personPersonKind;
    }

    public void setPersonPersonKind(String personPersonKind) {
        this.personPersonKind = personPersonKind;
    }

    /** 
     *            @hibernate.property
     *             column="PERSON_PSET"
     *             length="50"
     *         
     */
	@Column(name = "PERSON_PSET")
    public String getPersonPset() {
        return this.personPset;
    }

    public void setPersonPset(String personPset) {
        this.personPset = personPset;
    }

    
    public String toString() {
        return new ToStringBuilder(this)
            .append("personId", getPersonId())
            .toString();
    }
    @Column(name = "PERSON_ISDEL")
	public String getPersonIsdel() {
		return personIsdel;
	}

	public void setPersonIsdel(String personIsdel) {
		this.personIsdel = personIsdel;
	}
    @Column(name = "PERSON_RETIRE_TIME")
	public Date getPersonRetireTime() {
		return personRetireTime;
	}

	public void setPersonRetireTime(Date personRetireTime) {
		this.personRetireTime = personRetireTime;
	}

	

}
