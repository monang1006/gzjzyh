package com.strongit.oa.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_BASE_PERSON"
 *     
*/
@Entity
@Table(name = "T_OA_BASE_PERSON", catalog = "", schema = "")
public class ToaBasePerson implements Serializable {

    /** 人员ID identifier field */
    private String personid;

    /** 是否删除 nullable persistent field */
    private String personIsdel;
    
    /** 编制ID nullable persistent field*/
    private String personStructId;
    
    /** 人员姓名*/
    private String personName;
    
    /** 照片*/
    private byte[] personPhoto;
    
    /** 性别*/
    private String personSax;
    
    /** 出生日期*/
    private String personBorn;
    
    /** 籍贯*/
    private String personNativePlace;
    
    /** 名族*/
    private String  personNation;

    /** 职务 nullable persistent field */
    private String personPset;

    /** 参加工作时间nullable persistent field */
    private String personWorkTime;


    /** 学历 nullable persistent field */
    private String personEducation;
    
    /** 个人身份*/
    private String person_status;
    
    /** 转入时间*/
    private String personZhuanru;

    /** 入职时间*/
    private String personComeDate;
    
    private String personPersonKind;//人员类别
    
    private String  personIsOutLimit;//是否超编人员
    
    private String  uums_person_id;//存入统一用户人员ID
    
	private com.strongit.oa.bo.ToaBaseOrg baseOrg;

    /** full constructor */
    public ToaBasePerson(String personid , String personPset, String personWorkTime ,String personEducation) {
        this.personid = personid;
        this.personPset = personPset;
        this.personWorkTime = personWorkTime;
        this.personEducation = personEducation;
    }

    /** default constructor */
    public ToaBasePerson() {
    }

    /** minimal constructor */
    public ToaBasePerson(String personid) {
        this.personid = personid;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PERSONID"
     *         
     */
    @Id
	@Column(name = "PERSONID", nullable = false,length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPersonid() {
        return this.personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }


    /** 
     *            @hibernate.property
     *             column="PERSON_PSET"
     *             length="100"
     *         
     */
    @Column(name = "PERSON_PSET")
    public String getPersonPset() {
        return this.personPset;
    }

    public void setPersonPset(String personPset) {
        this.personPset = personPset;
    }


    /** 
     *            @hibernate.property
     *             column="PERSON_WORK_TIME"
     *             length="10"
     *         
     */
    @Column(name = "PERSON_WORK_TIME")
    public String getPersonWorkTime() {
        return this.personWorkTime;
    }

    public void setPersonWorkTime(String personWorkTime) {
        this.personWorkTime = personWorkTime;
    }

    
    /** 
     *            @hibernate.property
     *             column="PERSON_EDUCATION"
     *             length="100"
     *         
     */
    @Column(name = "PERSON_EDUCATION")
    public String getPersonEducation() {
        return this.personEducation;
    }

    public void setPersonEducation(String personEducation) {
        this.personEducation = personEducation;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("personid", getPersonid())
            .toString();
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
	public com.strongit.oa.bo.ToaBaseOrg getBaseOrg() {
		return baseOrg;
	}

	public void setBaseOrg(com.strongit.oa.bo.ToaBaseOrg baseOrg) {
		this.baseOrg = baseOrg;
	}

	@Column(name = "PERSON_ISDEL")
	public String getPersonIsdel() {
		return personIsdel;
	}

	public void setPersonIsdel(String personIsdel) {
		this.personIsdel = personIsdel;
	}
	
	@Column(name = "PERSON_BORN")
	public String getPersonBorn() {
		return personBorn;
	}

	public void setPersonBorn(String personBorn) {
		this.personBorn = personBorn;
	}
	
	@Column(name = "PERSON_NAME")
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@Column(name = "PERSON_NATION")
	public String getPersonNation() {
		return personNation;
	}

	public void setPersonNation(String personNation) {
		this.personNation = personNation;
	}
	
	@Column(name = "PERSON_NATIVEPLACE")
	public String getPersonNativePlace() {
		return personNativePlace;
	}

	public void setPersonNativePlace(String personNativePlace) {
		this.personNativePlace = personNativePlace;
	}
	
	@Column(name = "PERSON_PHOTO")
	@Lob
	public byte[] getPersonPhoto() {
		return personPhoto;
	}

	public void setPersonPhoto(byte[] personPhoto) {
		this.personPhoto = personPhoto;
	}
	
	@Column(name = "PERSON_SAX")
	public String getPersonSax() {
		return personSax;
	}

	public void setPersonSax(String personSax) {
		this.personSax = personSax;
	}
	
	@Column(name = "PERSON_STATUS")
	public String getPerson_status() {
		return person_status;
	}

	public void setPerson_status(String person_status) {
		this.person_status = person_status;
	}
	
	@Column(name="STRUC_ID")
	public String getPersonStructId() {
		return personStructId;
	}

	public void setPersonStructId(String personStructId) {
		this.personStructId = personStructId;
	}
	
	@Column(name="PERSON_ZHUANRU")
	public String getPersonZhuanru() {
		return personZhuanru;
	}

	public void setPersonZhuanru(String personZhuanru) {
		this.personZhuanru = personZhuanru;
	}
	
	@Column(name="PERSON_COMEDATE")
	public String getPersonComeDate() {
		return personComeDate;
	}

	public void setPersonComeDate(String prsonComeDate) {
		this.personComeDate = prsonComeDate;
	}
	
	@Column(name = "PERSON_PERSON_KIND")
	public String getPersonPersonKind() {
		return personPersonKind;
	}

	public void setPersonPersonKind(String personPersonKind) {
		this.personPersonKind = personPersonKind;
	}
	
	@Column(name = "PERSON_IS_OUT_LIMIT")
	public String getPersonIsOutLimit() {
		return personIsOutLimit;
	}

	public void setPersonIsOutLimit(String personIsOutLimit) {
		this.personIsOutLimit = personIsOutLimit;
	}
	
	@Column(name = "UUMS_PERSON_ID") 
	public String getUums_person_id() {
		return uums_person_id;
	}

	public void setUums_person_id(String uums_person_id) {
		this.uums_person_id = uums_person_id;
	}

}
