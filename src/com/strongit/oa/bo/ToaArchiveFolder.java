package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_FOLDER"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_FOLDER")
public class ToaArchiveFolder implements Serializable {

    /** identifier field 案卷ID*/
	@Id 
	@GeneratedValue
    private String folderId;

    /** nullable persistent field 案卷名称*/
    private String folderName;

    /** nullable persistent field 案卷序号*/
    private String folderArrayNo;

    /** nullable persistent field 案卷编号*/
    private String folderNo;

    /** nullable persistent field 创建日期*/
    private Date folderDate;

    /** nullable persistent field 所属全宗字典ID*/
    private String folderOrgId;

    /** nullable persistent field 所属全宗名称*/
    private String folderOrgName;

    /** nullable persistent field 保管期限字典ID*/
    private String folderLimitId;

    /** nullable persistent field 保管期限名称 */
    private String folderLimitName;

    /** nullable persistent field 文件数目*/
    private Long folderFileNum;

    /** nullable persistent field 卷内件数*/
    private Long folderPage;

    /** nullable persistent field 年度开始*/
    private String folderFromDate;

    /** nullable persistent field年度结束 */
    private String folderToDate;

    /** nullable persistent field 归档号*/
    private String folderArchiveNo;

    /** nullable persistent field 部门ID*/
    private String folderDepartment;
    
    private String folderDepartmentName;//部门名称

    /** nullable persistent field 创建者ID */
    private String folderCreaterId;

    /** nullable persistent field 创建者名称*/
    private String folderCreaterName;

    /** nullable persistent field 归档申请时间*/
    private Date folderArchiveTime;

    /** nullable persistent field 归档状态*/
    private String folderAuditing;

    /** nullable persistent field 归档审核人ID*/
    private String folderAuditingId;

    /** nullable persistent field 审核人*/
    private String folderAuditingName;
    
    /** nullable persistent field审核意见 */
    private String folderAuditingContent;

    /** nullable persistent field 审核时间*/
    private Date folderAuditingTime;
    /**年度 */
    private String folderYear;

    /** nullable persistent field 案卷描述*/
    private String folderDesc;

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveSort toaArchiveSort;

    /** persistent field */
    private Set toaArchiveFiles;

    /** persistent field */
    private Set toaArchiveTempfiles;
    
    /** 案卷全宗号*/
    private String folderDictNo ;
    /** 案卷盒号*/
    private String folderBoxNo  ;
    /** 案卷书名*/
    private String folderFormName  ;
    
    /** 机构CODE]
     * 郑志斌 2010-12-20 添加机构授权开关*/
    private String folderOrgcode  ;
    
    
    /** 机构ID]
     * 郑志斌 2010-12-20 添加机构授权开关*/
    private String orgId  ;
    

    /** full constructor */
    public ToaArchiveFolder(String folderId, String folderName, String folderArrayNo, String folderNo, Date folderDate, String folderOrgId, String folderOrgName, String folderLimitId, String folderLimitName, Long folderFileNum, Long folderPage, String folderFromDate, String folderToDate, String folderArchiveNo, String folderDepartment, String folderCreaterId, String folderCreaterName, Date folderArchiveTime, String folderAuditing, String folderAuditingId, String folderAuditingName, Date folderAuditingTime, String folderDesc, com.strongit.oa.bo.ToaArchiveSort toaArchiveSort, Set toaArchiveFiles, Set toaArchiveTempfiles) {
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
        this.folderArchiveTime = folderArchiveTime;
        this.folderAuditing = folderAuditing;
        this.folderAuditingId = folderAuditingId;
        this.folderAuditingName = folderAuditingName;
        this.folderAuditingTime = folderAuditingTime;
        this.folderDesc = folderDesc;
        this.toaArchiveSort = toaArchiveSort;
        this.toaArchiveFiles = toaArchiveFiles;
        this.toaArchiveTempfiles = toaArchiveTempfiles;
    }

    /** default constructor */
    public ToaArchiveFolder() {
    }

    /** minimal constructor */
    public ToaArchiveFolder(String folderId, com.strongit.oa.bo.ToaArchiveSort toaArchiveSort, Set toaArchiveFiles, Set toaArchiveTempfiles) {
        this.folderId = folderId;
        this.toaArchiveSort = toaArchiveSort;
        this.toaArchiveFiles = toaArchiveFiles;
        this.toaArchiveTempfiles = toaArchiveTempfiles;
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
     *             column="FOLDER_ARRAY_NO"
     *             length="40"
     *         
     */
    @Column(name="FOLDER_ARRAY_NO",nullable=true)
    public String getFolderArrayNo() {
        return this.folderArrayNo;
    }

    public void setFolderArrayNo(String folderArrayNo) {
        this.folderArrayNo = folderArrayNo;
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
     *             column="FOLDER_ARCHIVE_TIME"
     *             length="7"
     *         
     */
    @Column(name="FOLDER_ARCHIVE_TIME",nullable=true)
    public Date getFolderArchiveTime() {
        return this.folderArchiveTime;
    }

    public void setFolderArchiveTime(Date folderArchiveTime) {
        this.folderArchiveTime = folderArchiveTime;
    }

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
     *             length="10"
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
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="SORT_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="SORT_ID", nullable=true)
    public com.strongit.oa.bo.ToaArchiveSort getToaArchiveSort() {
        return this.toaArchiveSort;
    }

    public void setToaArchiveSort(com.strongit.oa.bo.ToaArchiveSort toaArchiveSort) {
        this.toaArchiveSort = toaArchiveSort;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveFile"
     *         
     */
    @OneToMany(mappedBy="toaArchiveFolder",targetEntity=com.strongit.oa.bo.ToaArchiveFile.class,cascade=CascadeType.ALL)
    public Set getToaArchiveFiles() {
        return this.toaArchiveFiles;
    }

    public void setToaArchiveFiles(Set toaArchiveFiles) {
        this.toaArchiveFiles = toaArchiveFiles;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveTempfile"
     *         
     */
    @OneToMany(mappedBy="toaArchiveFolder",targetEntity=com.strongit.oa.bo.ToaArchiveTempfile.class,cascade=CascadeType.ALL)
    public Set getToaArchiveTempfiles() {
        return this.toaArchiveTempfiles;
    }
    public void setToaArchiveTempfiles(Set toaArchiveTempfiles) {
        this.toaArchiveTempfiles = toaArchiveTempfiles;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("folderId", getFolderId())
            .toString();
    }

    @Column(name="FOLDER_DEPARTMENTNAME",nullable=true)
	public String getFolderDepartmentName() {
		return folderDepartmentName;
	}

	public void setFolderDepartmentName(String folderDepartmentName) {
		this.folderDepartmentName = folderDepartmentName;
	}
	
	 @Column(name="FOLDER_DICTNO",nullable=true)
	public String getFolderDictNo() {
		return folderDictNo;
	}

	public void setFolderDictNo(String folderDictNo) {
		this.folderDictNo = folderDictNo;
	}
	 @Column(name="FOLDER_BOXNO",nullable=true)
	public String getFolderBoxNo() {
		return folderBoxNo;
	}

	public void setFolderBoxNo(String folderBoxNo) {
		this.folderBoxNo = folderBoxNo;
	}
	 @Column(name="FOLDER_FORMNAME",nullable=true)
	public String getFolderFormName() {
		return folderFormName;
	}

	public void setFolderFormName(String folderFormName) {
		this.folderFormName = folderFormName;
	}
	@Column(name="FOLDER_YEAR",nullable=true)
	public String getFolderYear() {
		return folderYear;
	}

	public void setFolderYear(String folderYear) {
		this.folderYear = folderYear;
	}

	@Column(name="FOLDER_ORGCODE",nullable=true)
	public String getFolderOrgcode() {
		return folderOrgcode;
	}
	
	public void setFolderOrgcode(String folderOrgcode) {
		this.folderOrgcode = folderOrgcode;
	}

	
	@Column(name="ORGID",nullable=true)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	
}
