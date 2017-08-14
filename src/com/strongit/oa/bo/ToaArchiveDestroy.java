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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_DESTROY"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_DESTROY")
public class ToaArchiveDestroy implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String destroyId;

    /** nullable persistent field */
    private String destroyFolderName;

    /** nullable persistent field */
    private String destroyFolderNo;

    /** nullable persistent field */
    private Date destroyFolderDate;

    /** nullable persistent field */
    private String destroyFolderOrgname;

    /** nullable persistent field */
    private String destroyFolderFromtime;

    /** nullable persistent field */
    private String destroyFolderEndtime;

    /** nullable persistent field */
    private String destroyFolderArchiveNo;

    /** nullable persistent field */
    private String destroyFolderDepartment;
    
    /** 所属处室名称*/
    private String departmentName;

    /** nullable persistent field */
    private String destroyFolderCreaterName;

    /** nullable persistent field */
    private Date destroyFolderCreaterTime;

    /** nullable persistent field */
    private String destroyFolderDesc;

    /** nullable persistent field */
    private String destroyApplyName;

    /** nullable persistent field */
    private Date destroyApplyTime;

    /** nullable persistent field */
    private String destroyApplyDesc;

    /** nullable persistent field */
    private String destroyAuditingName;

    /** nullable persistent field */
    private Date destroyAuditingTime;

    /** nullable persistent field */
    private String destroyAuditingType;

    /** nullable persistent field */
    private String destroyAuditingDesc;

    /** persistent field */
    private Set toaArchiveDestroyFiles;

    /** full constructor */
    public ToaArchiveDestroy(String destroyId, String destroyFolderName, String destroyFolderNo, Date destroyFolderDate, String destroyFolderOrgname, String destroyFolderFromtime, String destroyFolderEndtime, String destroyFolderArchiveNo, String destroyFolderDepartment, String destroyFolderCreaterName, Date destroyFolderCreaterTime, String destroyFolderDesc, String destroyApplyName, Date destroyApplyTime, String destroyApplyDesc, String destroyAuditingName, Date destroyAuditingTime, String destroyAuditingType, String destroyAuditingDesc, Set toaArchiveDestroyFiles) {
        this.destroyId = destroyId;
        this.destroyFolderName = destroyFolderName;
        this.destroyFolderNo = destroyFolderNo;
        this.destroyFolderDate = destroyFolderDate;
        this.destroyFolderOrgname = destroyFolderOrgname;
        this.destroyFolderFromtime = destroyFolderFromtime;
        this.destroyFolderEndtime = destroyFolderEndtime;
        this.destroyFolderArchiveNo = destroyFolderArchiveNo;
        this.destroyFolderDepartment = destroyFolderDepartment;
        this.destroyFolderCreaterName = destroyFolderCreaterName;
        this.destroyFolderCreaterTime = destroyFolderCreaterTime;
        this.destroyFolderDesc = destroyFolderDesc;
        this.destroyApplyName = destroyApplyName;
        this.destroyApplyTime = destroyApplyTime;
        this.destroyApplyDesc = destroyApplyDesc;
        this.destroyAuditingName = destroyAuditingName;
        this.destroyAuditingTime = destroyAuditingTime;
        this.destroyAuditingType = destroyAuditingType;
        this.destroyAuditingDesc = destroyAuditingDesc;
        this.toaArchiveDestroyFiles = toaArchiveDestroyFiles;
    }

    /** default constructor */
    public ToaArchiveDestroy() {
    }

    /** minimal constructor */
    public ToaArchiveDestroy(String destroyId, Set toaArchiveDestroyFiles) {
        this.destroyId = destroyId;
        this.toaArchiveDestroyFiles = toaArchiveDestroyFiles;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DESTROY_ID"
     *         
     */
    @Id
	@Column(name="DESTROY_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDestroyId() {
        return this.destroyId;
    }

    public void setDestroyId(String destroyId) {
        this.destroyId = destroyId;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_NAME"
     *             length="100"
     *         
     */
    @Column(name="DESTROY_FOLDER_NAME",nullable=true)
    public String getDestroyFolderName() {
        return this.destroyFolderName;
    }

    public void setDestroyFolderName(String destroyFolderName) {
        this.destroyFolderName = destroyFolderName;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_NO"
     *             length="40"
     *         
     */
    @Column(name="DESTROY_FOLDER_NO",nullable=true)
    public String getDestroyFolderNo() {
        return this.destroyFolderNo;
    }

    public void setDestroyFolderNo(String destroyFolderNo) {
        this.destroyFolderNo = destroyFolderNo;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_DATE"
     *             length="7"
     *         
     */
    @Column(name="DESTROY_FOLDER_DATE",nullable=true)
    public Date getDestroyFolderDate() {
        return this.destroyFolderDate;
    }

    public void setDestroyFolderDate(Date destroyFolderDate) {
        this.destroyFolderDate = destroyFolderDate;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_ORGNAME"
     *             length="100"
     *         
     */
    @Column(name="DESTROY_FOLDER_ORGNAME",nullable=true)
    public String getDestroyFolderOrgname() {
        return this.destroyFolderOrgname;
    }

    public void setDestroyFolderOrgname(String destroyFolderOrgname) {
        this.destroyFolderOrgname = destroyFolderOrgname;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_FROMTIME"
     *             length="30"
     *         
     */
    @Column(name="DESTROY_FOLDER_FROMTIME",nullable=true)
    public String getDestroyFolderFromtime() {
        return this.destroyFolderFromtime;
    }

    public void setDestroyFolderFromtime(String destroyFolderFromtime) {
        this.destroyFolderFromtime = destroyFolderFromtime;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_ENDTIME"
     *             length="30"
     *         
     */
    @Column(name="DESTROY_FOLDER_ENDTIME",nullable=true)
    public String getDestroyFolderEndtime() {
        return this.destroyFolderEndtime;
    }

    public void setDestroyFolderEndtime(String destroyFolderEndtime) {
        this.destroyFolderEndtime = destroyFolderEndtime;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_ARCHIVE_NO"
     *             length="25"
     *         
     */
    @Column(name="DESTROY_FOLDER_ARCHIVE_NO",nullable=true)
    public String getDestroyFolderArchiveNo() {
        return this.destroyFolderArchiveNo;
    }

    public void setDestroyFolderArchiveNo(String destroyFolderArchiveNo) {
        this.destroyFolderArchiveNo = destroyFolderArchiveNo;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_DEPARTMENT"
     *             length="32"
     *         
     */
    @Column(name="DESTROY_FOLDER_DEPARTMENT",nullable=true)
    public String getDestroyFolderDepartment() {
        return this.destroyFolderDepartment;
    }

    public void setDestroyFolderDepartment(String destroyFolderDepartment) {
        this.destroyFolderDepartment = destroyFolderDepartment;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_CREATER_NAME"
     *             length="20"
     *         
     */
    @Column(name="DESTROY_FOLDER_CREATER_NAME",nullable=true)
    public String getDestroyFolderCreaterName() {
        return this.destroyFolderCreaterName;
    }

    public void setDestroyFolderCreaterName(String destroyFolderCreaterName) {
        this.destroyFolderCreaterName = destroyFolderCreaterName;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_CREATER_TIME"
     *             length="7"
     *         
     */
    @Column(name="DESTROY_FOLDER_CREATER_TIME",nullable=true)
    public Date getDestroyFolderCreaterTime() {
        return this.destroyFolderCreaterTime;
    }

    public void setDestroyFolderCreaterTime(Date destroyFolderCreaterTime) {
        this.destroyFolderCreaterTime = destroyFolderCreaterTime;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FOLDER_DESC"
     *             length="4000"
     *         
     */
    @Column(name="DESTROY_FOLDER_DESC",nullable=true)
    public String getDestroyFolderDesc() {
        return this.destroyFolderDesc;
    }

    public void setDestroyFolderDesc(String destroyFolderDesc) {
        this.destroyFolderDesc = destroyFolderDesc;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_APPLY_NAME"
     *             length="25"
     *         
     */
    @Column(name="DESTROY_APPLY_NAME",nullable=true)
    public String getDestroyApplyName() {
        return this.destroyApplyName;
    }

    public void setDestroyApplyName(String destroyApplyName) {
        this.destroyApplyName = destroyApplyName;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_APPLY_TIME"
     *             length="7"
     *         
     */
    @Column(name="DESTROY_APPLY_TIME",nullable=true)
    public Date getDestroyApplyTime() {
        return this.destroyApplyTime;
    }

    public void setDestroyApplyTime(Date destroyApplyTime) {
        this.destroyApplyTime = destroyApplyTime;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_APPLY_DESC"
     *             length="400"
     *         
     */
    @Column(name="DESTROY_APPLY_DESC",nullable=true)
    public String getDestroyApplyDesc() {
        return this.destroyApplyDesc;
    }

    public void setDestroyApplyDesc(String destroyApplyDesc) {
        this.destroyApplyDesc = destroyApplyDesc;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_AUDITING_NAME"
     *             length="25"
     *         
     */
    @Column(name="DESTROY_AUDITING_NAME",nullable=true)
    public String getDestroyAuditingName() {
        return this.destroyAuditingName;
    }

    public void setDestroyAuditingName(String destroyAuditingName) {
        this.destroyAuditingName = destroyAuditingName;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_AUDITING_TIME"
     *             length="7"
     *         
     */
    @Column(name="DESTROY_AUDITING_TIME",nullable=true)
    public Date getDestroyAuditingTime() {
        return this.destroyAuditingTime;
    }

    public void setDestroyAuditingTime(Date destroyAuditingTime) {
        this.destroyAuditingTime = destroyAuditingTime;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_AUDITING_TYPE"
     *             length="1"
     *         
     */
    @Column(name="DESTROY_AUDITING_TYPE",nullable=true)
    public String getDestroyAuditingType() {
        return this.destroyAuditingType;
    }

    public void setDestroyAuditingType(String destroyAuditingType) {
        this.destroyAuditingType = destroyAuditingType;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_AUDITING_DESC"
     *             length="400"
     *         
     */
    @Column(name="DESTROY_AUDITING_DESC",nullable=true)
    public String getDestroyAuditingDesc() {
        return this.destroyAuditingDesc;
    }

    public void setDestroyAuditingDesc(String destroyAuditingDesc) {
        this.destroyAuditingDesc = destroyAuditingDesc;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DESTROY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveDestroyFile"
     *         
     */
    @OneToMany(mappedBy="toaArchiveDestroy",targetEntity=com.strongit.oa.bo.ToaArchiveDestroyFile.class,cascade=CascadeType.ALL)
    public Set getToaArchiveDestroyFiles() {
        return this.toaArchiveDestroyFiles;
    }

    public void setToaArchiveDestroyFiles(Set toaArchiveDestroyFiles) {
        this.toaArchiveDestroyFiles = toaArchiveDestroyFiles;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("destroyId", getDestroyId())
            .toString();
    }

    @Transient
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

}
