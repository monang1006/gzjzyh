package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_CODEMANAGE"
 *     
*/
@Entity
@Table(name="T_OA_CODEMANAGE",catalog="",schema="")
public class ToaCodemanage implements Serializable {

    /** identifier field */
    private String codeId;

    /** nullable persistent field */
    private String coderuleId;

    /** nullable persistent field */
    private String codeInfo;

    /** nullable persistent field */
    private Date codeCreatetime;

    /** nullable persistent field */
    private String codeUsername;

    /** nullable persistent field */
    private String orgName;

    /** nullable persistent field */
    private String codeStatus;
    
    private String orgId;
    
    private String orgCode;

    /** full constructor */
    public ToaCodemanage(String codeId, String coderuleId, String codeInfo, Date codeCreatetime, String codeUsername, String orgName, String codeStatus) {
        this.codeId = codeId;
        this.coderuleId = coderuleId;
        this.codeInfo = codeInfo;
        this.codeCreatetime = codeCreatetime;
        this.codeUsername = codeUsername;
        this.orgName = orgName;
        this.codeStatus = codeStatus;
    }

    /** default constructor */
    public ToaCodemanage() {
    }

    /** minimal constructor */
    public ToaCodemanage(String codeId) {
        this.codeId = codeId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CODE_ID"
     *         
     */
    @Id
    @Column(name="CODE_ID",nullable=false, length=32)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getCodeId() {
        return this.codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    /** 
     *            @hibernate.property
     *             column="CODERULE_ID"
     *             length="32"
     *         
     */
    @Column(name="CODERULE_ID",nullable=true,length=32)
    public String getCoderuleId() {
        return this.coderuleId;
    }

    public void setCoderuleId(String coderuleId) {
        this.coderuleId = coderuleId;
    }

    /** 
     *            @hibernate.property
     *             column="CODE_INFO"
     *             length="200"
     *         
     */
    @Column(name="CODE_INFO",nullable=true,length=200)
    public String getCodeInfo() {
        return this.codeInfo;
    }

    public void setCodeInfo(String codeInfo) {
        this.codeInfo = codeInfo;
    }

    /** 
     *            @hibernate.property
     *             column="CODE_CREATETIME"
     *             length="7"
     *         
     */
    @Column(name="CODE_CREATETIME",nullable=true,length=7)
    public Date getCodeCreatetime() {
        return this.codeCreatetime;
    }

    public void setCodeCreatetime(Date codeCreatetime) {
        this.codeCreatetime = codeCreatetime;
    }

    /** 
     *            @hibernate.property
     *             column="CODE_USERNAME"
     *             length="100"
     *         
     */
    @Column(name="CODE_USERNAME",nullable=true,length=100)
    public String getCodeUsername() {
        return this.codeUsername;
    }

    public void setCodeUsername(String codeUsername) {
        this.codeUsername = codeUsername;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_NAME"
     *             length="200"
     *         
     */
    @Column(name="ORG_NAME",nullable=true,length=200)
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** 
     *            @hibernate.property
     *             column="CODE_STATUS"
     *             length="1"
     *         
     */
    @Column(name="CODE_STATUS",nullable=true,length=1)
    public String getCodeStatus() {
        return this.codeStatus;
    }

    public void setCodeStatus(String codeStatus) {
        this.codeStatus = codeStatus;
    }
    
    @Column(name="ORG_CODE",nullable=true,length=100)
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name="ORG_ID",nullable=true,length=32)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
    public String toString() {
        return new ToStringBuilder(this)
            .append("codeId", getCodeId())
            .toString();
    }

}
