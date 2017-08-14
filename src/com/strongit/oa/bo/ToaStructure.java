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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;



/** 
 *        @hibernate.class
 *         机构编制bo
 *         table="T_OA_STRUCTURE"
 *     
*/
@Entity
@Table(name = "T_OA_STRUCTURE", catalog = "", schema = "")
public class ToaStructure implements Serializable {

    /** identifier field */
    private String strucId;

    /** 编制类型 nullable persistent field */
    private String strucType;

    /** 人数 nullable persistent field */
    private String strucNumber;
    
   /** 编辑时间 nullable persistent field */
    private Date strucEdittime;

    /** 编制状态 nullable persistent field */
    private String strucStatus;

    /** 编制描述 nullable persistent field */
    private String strucDemo;

    /** nullable persistent field */
    private String rest1;

    /** nullable persistent field */
    private String rest2;
    
    /** 编制类型名称*/
    private String strucTypeName;
  
	/** 实际人数*/
    private int realityPerson;
    /** 超编人数*/
    private int outPerson;

    /** persistent field */
    private com.strongit.oa.bo.ToaBaseOrg toaBaseOrg;

    /** full constructor */
    public ToaStructure(String strucId, String strucType, String strucNumber, String strucEdittime, String strucStatus, String strucDemo, String rest1, String rest2, com.strongit.oa.bo.ToaBaseOrg toaBaseOrg) {
        this.strucId = strucId;
        this.strucType = strucType;
        this.strucNumber = strucNumber;
   
        this.strucStatus = strucStatus;
        this.strucDemo = strucDemo;
        this.rest1 = rest1;
        this.rest2 = rest2;
        this.toaBaseOrg = toaBaseOrg;
    }

    /** default constructor */
    public ToaStructure() {
    }

    /** minimal constructor */
    public ToaStructure(String strucId, com.strongit.oa.bo.ToaBaseOrg toaBaseOrg) {
        this.strucId = strucId;
        this.toaBaseOrg = toaBaseOrg;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="STRUC_ID"
     *         
     */
    @Id
	@Column(name = "STRUC_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getStrucId() {
        return this.strucId;
    }

    public void setStrucId(String strucId) {
        this.strucId = strucId;
    }

    /** 
     *            @hibernate.property
     *             column="STRUC_TYPE"
     *             length="132"
     *         
     */
	@Column(name = "STRUC_TYPE")
    public String getStrucType() {
        return this.strucType;
    }

    public void setStrucType(String strucType) {
        this.strucType = strucType;
    }

    /** 
     *            @hibernate.property
     *             column="STRUC_NUMBER"
     *             length="12"
     *         
     */
	@Column(name = "STRUC_NUMBER")
    public String getStrucNumber() {
        return this.strucNumber;
    }

    public void setStrucNumber(String strucNumber) {
        this.strucNumber = strucNumber;
    }

    /** 
     *            @hibernate.property
     *             column="STRUC_EDITTIME"
     *             length="32"
     *         
     */
	@Column(name = "STRUC_EDITTIME")
    public Date getStrucEdittime() {
        return this.strucEdittime;
    }

    public void setStrucEdittime(Date strucEdittime) {
        this.strucEdittime = strucEdittime;
    }

    /** 
     *            @hibernate.property
     *             column="STRUC_STATUS"
     *             length="4"
     *         
     */
	@Column(name = "STRUC_STATUS")
    public String getStrucStatus() {
        return this.strucStatus;
    }

    public void setStrucStatus(String strucStatus) {
        this.strucStatus = strucStatus;
    }

    /** 
     *            @hibernate.property
     *             column="STRUC_DEMO"
     *             length="400"
     *         
     */
	@Column(name = "STRUC_DEMO")
    public String getStrucDemo() {
        return this.strucDemo;
    }

    public void setStrucDemo(String strucDemo) {
        this.strucDemo = strucDemo;
    }

    /** 
     *            @hibernate.property
     *             column="REST1"
     *             length="32"
     *         
     */
	@Column(name = "REST1")
    public String getRest1() {
        return this.rest1;
    }

    public void setRest1(String rest1) {
        this.rest1 = rest1;
    }

    /** 
     *            @hibernate.property
     *             column="REST2"
     *             length="32"
     *         
     */
	@Column(name = "REST2")
    public String getRest2() {
        return this.rest2;
    }

    public void setRest2(String rest2) {
        this.rest2 = rest2;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ORG_ID"         
     *         
     */
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID")
    public com.strongit.oa.bo.ToaBaseOrg getToaBaseOrg() {
        return this.toaBaseOrg;
    }

    public void setToaBaseOrg(com.strongit.oa.bo.ToaBaseOrg toaBaseOrg) {
        this.toaBaseOrg = toaBaseOrg;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("strucId", getStrucId())
            .toString();
    }

	@Column(name = "STRUC_NAME")
	public String getStrucTypeName() {
		return strucTypeName;
	}

	public void setStrucTypeName(String strucTypeName) {
		this.strucTypeName = strucTypeName;
	}

	@Transient
	public int getRealityPerson() {
		return realityPerson;
	}

	public void setRealityPerson(int realityPerson) {
		this.realityPerson = realityPerson;
	}

	@Transient
	public int getOutPerson() {
		return outPerson;
	}

	public void setOutPerson(int outPerson) {
		this.outPerson = outPerson;
	}

}
