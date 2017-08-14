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


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_FOLDER_BAK"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_FOLDER_BAK")
public class ToaArchiveFolderBak implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String folderId;

    /** nullable persistent field */
    private String folderName;
    
    /** nullable persistent field */
    private String folderArrayNo;

    /** nullable persistent field */
    private String folderNo;

    /** nullable persistent field */
    private Date folderDate;

    /** nullable persistent field */
    private String folderOrgId;

    /** nullable persistent field */
    private String folderOrgName;

    /** nullable persistent field */
    private String folderLimitId;

    /** nullable persistent field */
    private String folderLimitName;

    /** nullable persistent field */
    private Long folderFileNum;

    /** nullable persistent field */
    private Long folderPage;

    /** nullable persistent field */
    private String folderFromDate;

    /** nullable persistent field */
    private String folderToDate;

    /** nullable persistent field */
    private String folderArchiveNo;

    /** nullable persistent field */
    private String folderDepartment;

    /** nullable persistent field */
    private String folderCreaterId;

    /** nullable persistent field */
    private String folderCreaterName;

    /** nullable persistent field */
 //   private Date folderCreaterTime;

    /** nullable persistent field */
    private String folderAuditing;

    /** nullable persistent field */
    private String folderAuditingId;

    /** nullable persistent field */
    private String folderAuditingName;
    
    /** nullable persistent field */
    private String folderAuditingContent;

    /** nullable persistent field */
    private Date folderAuditingTime;

    /** nullable persistent field */
    private String folderDesc;

    /** persistent field */
    private Set toaArchiveFileBaks;

    /** full constructor */
    public ToaArchiveFolderBak(String folderId, String folderName, String folderArrayNo, String folderNo, Date folderDate, String folderOrgId, String folderOrgName, String folderLimitId, String folderLimitName, Long folderFileNum, Long folderPage, String folderFromDate, String folderToDate, String folderArchiveNo, String folderDepartment, String folderCreaterId, String folderCreaterName, String folderAuditing, String folderAuditingId, String folderAuditingName, String folderAuditingContent,Date folderAuditingTime, String folderDesc, Set toaArchiveFileBaks) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.folderArrayNo = folderArrayNo;
        this.folderNo = folderNo;
        this.folderDate = folderDate;
        this.folderOrgId = folderOrgId;
        this.folderOrgName = folderOrgName;
        this.folderLimitId = folderLimitId;
        this.folderLimitName = folderLimitName;
        this.folderFileNum = folderFileNum;
        this.folderPage = folderPage;
        this.folderFromDate = folderFromDate;
        this.folderToDate = folderToDate;
        this.folderArchiveNo = folderArchiveNo;
        this.folderDepartment = folderDepartment;
        this.folderCreaterId = folderCreaterId;
        this.folderCreaterName = folderCreaterName;
     //   this.folderCreaterTime = folderCreaterTime;
        this.folderAuditing = folderAuditing;
        this.folderAuditingId = folderAuditingId;
        this.folderAuditingName = folderAuditingName;
        this.folderAuditingContent = folderAuditingContent;
        this.folderAuditingTime = folderAuditingTime;
        this.folderDesc = folderDesc;
        this.toaArchiveFileBaks = toaArchiveFileBaks;
    }

    /** default constructor */
    public ToaArchiveFolderBak() {
    }

    /** minimal constructor */
    public ToaArchiveFolderBak(String folderId, Set toaArchiveFileBaks) {
        this.folderId = folderId;
        this.toaArchiveFileBaks = toaArchiveFileBaks;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FOLDER_ID"
     *         
     */
    @Id
	@Column(name="FOLDER_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_NAME"
     *             length="128"
     *         
     */
    @Column(name="FOLDER_NAME",nullable=true)
    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_NO"
     *             length="40"
     *         
     */
    @Column(name="FOLDER_NO",nullable=true)
    public String getFolderNo() {
        return this.folderNo;
    }

    public void setFolderNo(String folderNo) {
        this.folderNo = folderNo;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_DATE"
     *             length="7"
     *         
     */
    @Column(name="FOLDER_DATE",nullable=true)
    public Date getFolderDate() {
        return this.folderDate;
    }

    public void setFolderDate(Date folderDate) {
        this.folderDate = folderDate;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_ORG_ID"
     *             length="32"
     *         
     */
    @Column(name="FOLDER_ORG_ID",nullable=true)
    public String getFolderOrgId() {
        return this.folderOrgId;
    }

    public void setFolderOrgId(String folderOrgId) {
        this.folderOrgId = folderOrgId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_ORG_NAME"
     *             length="100"
     *         
     */
    @Column(name="FOLDER_ORG_NAME",nullable=true)
    public String getFolderOrgName() {
        return this.folderOrgName;
    }

    public void setFolderOrgName(String folderOrgName) {
        this.folderOrgName = folderOrgName;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_LIMIT_ID"
     *             length="32"
     *         
     */
    @Column(name="FOLDER_LIMIT_ID",nullable=true)
    public String getFolderLimitId() {
        return this.folderLimitId;
    }

    public void setFolderLimitId(String folderLimitId) {
        this.folderLimitId = folderLimitId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_LIMIT_NAME"
     *             length="100"
     *         
     */
    @Column(name="FOLDER_LIMIT_NAME",nullable=true)
    public String getFolderLimitName() {
        return this.folderLimitName;
    }

    public void setFolderLimitName(String folderLimitName) {
        this.folderLimitName = folderLimitName;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_FILE_NUM"
     *             length="10"
     *         
     */
    @Column(name="FOLDER_FILE_NUM",nullable=true)
    public Long getFolderFileNum() {
        return this.folderFileNum;
    }

    public void setFolderFileNum(Long folderFileNum) {
        this.folderFileNum = folderFileNum;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_PAGE"
     *             length="10"
     *         
     */
    @Column(name="FOLDER_PAGE",nullable=true)
    public Long getFolderPage() {
        return this.folderPage;
    }

    public void setFolderPage(Long folderPage) {
        this.folderPage = folderPage;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_FROM_DATE"
     *             length="30"
     *         
     */
    @Column(name="FOLDER_FROM_DATE",nullable=true)
    public String getFolderFromDate() {
        return this.folderFromDate;
    }

    public void setFolderFromDate(String folderFromDate) {
        this.folderFromDate = folderFromDate;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_TO_DATE"
     *             length="30"
     *         
     */
    @Column(name="FOLDER_TO_DATE",nullable=true)
    public String getFolderToDate() {
        return this.folderToDate;
    }

    public void setFolderToDate(String folderToDate) {
        this.folderToDate = folderToDate;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_ARCHIVE_NO"
     *             length="30"
     *         
     */
    @Column(name="FOLDER_ARCHIVE_NO",nullable=true)
    public String getFolderArchiveNo() {
        return this.folderArchiveNo;
    }

    public void setFolderArchiveNo(String folderArchiveNo) {
        this.folderArchiveNo = folderArchiveNo;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_DEPARTMENT"
     *             length="20"
     *         
     */
    @Column(name="FOLDER_DEPARTMENT",nullable=true)
    public String getFolderDepartment() {
        return this.folderDepartment;
    }

    public void setFolderDepartment(String folderDepartment) {
        this.folderDepartment = folderDepartment;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_CREATER_ID"
     *             length="32"
     *         
     */
    @Column(name="FOLDER_CREATER_ID",nullable=true)
    public String getFolderCreaterId() {
        return this.folderCreaterId;
    }

    public void setFolderCreaterId(String folderCreaterId) {
        this.folderCreaterId = folderCreaterId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_CREATER_NAME"
     *             length="20"
     *         
     */
    @Column(name="FOLDER_CREATER_NAME",nullable=true)
    public String getFolderCreaterName() {
        return this.folderCreaterName;
    }

    public void setFolderCreaterName(String folderCreaterName) {
        this.folderCreaterName = folderCreaterName;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_CREATER_TIME"
     *             length="7"
     *         
     */
//    @Column(name="FOLDER_CREATER_TIME",nullable=true)
//    public Date getFolderCreaterTime() {
//        return this.folderCreaterTime;
//    }
//
//    public void setFolderCreaterTime(Date folderCreaterTime) {
//        this.folderCreaterTime = folderCreaterTime;
//    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_AUDITING"
     *             length="1"
     *         
     */
    @Column(name="FOLDER_AUDITING",nullable=true)
    public String getFolderAuditing() {
        return this.folderAuditing;
    }

    public void setFolderAuditing(String folderAuditing) {
        this.folderAuditing = folderAuditing;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_AUDITING_ID"
     *             length="32"
     *         
     */
    @Column(name="FOLDER_AUDITING_ID",nullable=true)
    public String getFolderAuditingId() {
        return this.folderAuditingId;
    }

    public void setFolderAuditingId(String folderAuditingId) {
        this.folderAuditingId = folderAuditingId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_AUDITING_NAME"
     *             length="20"
     *         
     */
    @Column(name="FOLDER_AUDITING_NAME",nullable=true)
    public String getFolderAuditingName() {
        return this.folderAuditingName;
    }

    public void setFolderAuditingName(String folderAuditingName) {
        this.folderAuditingName = folderAuditingName;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_AUDITING_TIME"
     *             length="7"
     *         
     */
    @Column(name="FOLDER_AUDITING_TIME",nullable=true)
    public Date getFolderAuditingTime() {
        return this.folderAuditingTime;
    }

    public void setFolderAuditingTime(Date folderAuditingTime) {
        this.folderAuditingTime = folderAuditingTime;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_DESC"
     *             length="4000"
     *         
     */
    @Column(name="FOLDER_DESC",nullable=true)
    public String getFolderDesc() {
        return this.folderDesc;
    }

    public void setFolderDesc(String folderDesc) {
        this.folderDesc = folderDesc;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveFileBak"
     *         
     */
    @OneToMany(mappedBy="toaArchiveFolderBak",targetEntity=com.strongit.oa.bo.ToaArchiveFileBak.class,cascade=CascadeType.ALL)
    public Set getToaArchiveFileBaks() {
        return this.toaArchiveFileBaks;
    }

    public void setToaArchiveFileBaks(Set toaArchiveFileBaks) {
        this.toaArchiveFileBaks = toaArchiveFileBaks;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("folderId", getFolderId())
            .toString();
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_ARRAY_NO"
     *             length="40"
     *         
     */
    @Column(name="FOLDER_ARRAY_NO",nullable=true)
	public String getFolderArrayNo() {
		return folderArrayNo;
	}

	public void setFolderArrayNo(String folderArrayNo) {
		this.folderArrayNo = folderArrayNo;
	}

	 /** 
     *            @hibernate.property
     *             column="FOLDER_AUDITING_CONTENT"
     *             length="7"
     *         
     */
    @Column(name="FOLDER_AUDITING_CONTENT",nullable=true)
	public String getFolderAuditingContent() {
		return folderAuditingContent;
	}

	public void setFolderAuditingContent(String folderAuditingContent) {
		this.folderAuditingContent = folderAuditingContent;
	}

}
