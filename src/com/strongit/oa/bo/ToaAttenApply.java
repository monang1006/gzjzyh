package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 申请单表
 *        @hibernate.class
 *         table="T_OA_ATTEN_APPLY"
 *     
*/
@Entity
@Table(name = "T_OA_ATTEN_APPLY", catalog = "", schema = "")
public class ToaAttenApply implements Serializable {

    /** identifier field */
    private String applyId;

    /** nullable persistent field */
    private Date applyStime;//申请开始时间

    /** nullable persistent field */
    private Date applyEtime;//申请结束时间

    /** nullable persistent field */
    private String applyTypeId;//申请类型ID

    /** nullable persistent field */
    private String applyTypeName;//申请类型名称

    /** nullable persistent field */
    private String applicants;//申请人
    
    private String applyPersonId;//申请人ID
    
    private String applyFormId;//绑定表单ID


    /** nullable persistent field */
    private Date applyTime;//申请时间

    /** nullable persistent field */
    private String orgId;//部门ID

    /** nullable persistent field */
    private String orgName;//部门名称

    /** nullable persistent field */
    private String applyState;//申请状态。0：未提交；1：审核中；2：已审核

    /** nullable persistent field */
 

    /** nullable persistent field */
  

    /** nullable persistent field */
    private String applyReason;//申请原因
    
    /** persistent field */
    private Set toaAttendCancles;

    /** full constructor */
    public ToaAttenApply(String applyId, Date applyStime, Date applyEtime, String applyTypeId, String applyTypeName, String applicants, Date applyTime, String orgId, String orgName, String applyState, String applyReason) {
        this.applyId = applyId;
        this.applyStime = applyStime;
        this.applyEtime = applyEtime;
        this.applyTypeId = applyTypeId;
        this.applyTypeName = applyTypeName;
        this.applicants = applicants;
        this.applyTime = applyTime;
        this.orgId = orgId;
        this.orgName = orgName;
        this.applyState = applyState;
      
        this.applyReason = applyReason;
      
    }

    /** default constructor */
    public ToaAttenApply() {
    }

    /** minimal constructor */
    public ToaAttenApply(String applyId) {
        this.applyId = applyId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="APPLY_ID"
     *         
     */
    @Id
	@Column(name = "APPLY_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getApplyId() {
        return this.applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    /** 
     *            @hibernate.property
     *             column="APPLY_STIME"
     *             length="7"
     *         
     */
	@Column(name = "APPLY_STIME")
    public Date getApplyStime() {
        return this.applyStime;
    }

    public void setApplyStime(Date applyStime) {
        this.applyStime = applyStime;
    }

    /** 
     *            @hibernate.property
     *             column="APPLY_ETIME"
     *             length="7"
     *         
     */
	@Column(name = "APPLY_ETIME")
    public Date getApplyEtime() {
        return this.applyEtime;
    }

    public void setApplyEtime(Date applyEtime) {
        this.applyEtime = applyEtime;
    }

    /** 
     *            @hibernate.property
     *             column="APPLY_TYPE_ID"
     *             length="32"
     *         
     */
	@Column(name = "APPLY_TYPE_ID")
    public String getApplyTypeId() {
        return this.applyTypeId;
    }

    public void setApplyTypeId(String applyTypeId) {
        this.applyTypeId = applyTypeId;
    }

    /** 
     *            @hibernate.property
     *             column="APPLY_TYPE_NAME"
     *             length="30"
     *         
     */
	@Column(name = "APPLY_TYPE_NAME")
    public String getApplyTypeName() {
        return this.applyTypeName;
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    /** 
     *            @hibernate.property
     *             column="APPLICANTS"
     *             length="25"
     *         
     */
	@Column(name = "APPLICANTS")
    public String getApplicants() {
        return this.applicants;
    }

    public void setApplicants(String applicants) {
        this.applicants = applicants;
    }

    /** 
     *            @hibernate.property
     *             column="APPLY_TIME"
     *             length="7"
     *         
     */
	@Column(name = "APPLY_TIME")
    public Date getApplyTime() {
        return this.applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *         
     */
	@Column(name = "ORG_ID")
    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_NAME"
     *             length="100"
     *         
     */
	@Column(name = "ORG_NAME")
    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /** 
     *            @hibernate.property
     *             column="APPLY_STATE"
     *             length="1"
     *         
     */
	@Column(name = "APPLY_STATE")
    public String getApplyState() {
        return this.applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    /** 
     *            @hibernate.property
     *             column="CANCLE_STATE"
     *             length="1"
     *         
     */


    /** 
     *            @hibernate.property
     *             column="APPLY_REASON"
     *             length="400"
     *         
     */
	@Column(name = "APPLY_REASON")
    public String getApplyReason() {
        return this.applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="APPLY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAttendCancle"
     *         
     */
    @OneToMany(mappedBy="toaAttenApply",targetEntity=com.strongit.oa.bo.ToaAttendCancle.class,cascade=CascadeType.ALL)
    public Set getToaAttendCancles() {
        return this.toaAttendCancles;
    }

    public void setToaAttendCancles(Set toaAttendCancles) {
        this.toaAttendCancles = toaAttendCancles;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("applyId", getApplyId())
            .toString();
    }
   @Column(name ="APPLY_PERSON_ID")
	public String getApplyPersonId() {
		return applyPersonId;
	}

	public void setApplyPersonId(String applyPersonId) {
		this.applyPersonId = applyPersonId;
	}
    @Column(name ="APPLY_FORM_ID")
	public String getApplyFormId() {
		return applyFormId;
	}

	public void setApplyFormId(String applyFormId) {
		this.applyFormId = applyFormId;
	}

}
