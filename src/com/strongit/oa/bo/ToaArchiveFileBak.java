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
 *         table="T_OA_ARCHIVE_FILE_BAK"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_FILE_BAK")
public class ToaArchiveFileBak implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String fileId;

    /** nullable persistent field */
    private String fileNo;

    /** nullable persistent field */
    private String fileAuthor;

    /** nullable persistent field */
    private String fileTitle;

    /** nullable persistent field */
    private Date fileDate;

    /** nullable persistent field */
    private Long filePage;

    /** nullable persistent field */
    private String fileDesc;

    /** nullable persistent field */
    private String fileDepartment;
    
    /** nullable persistent field */
    private String fileDocType;

    /** nullable persistent field */
    private String fileDocId;

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveFolderBak toaArchiveFolderBak;

    /** persistent field */
    private Set toaArchiveFileAppendBaks;

    /** 所属机构*/
    private String fileOrgcode;
    
    /** 档案时间*/
    private Date archiveDate;
    /** 件号*/
    private String filePieceNo;
    /** 保管期限*/
    private String fileDeadline;
    /** 保管期限名称:不存入数据库*/
    private String fileDeadlineName;
    /** 部门名称*/
    private String fileDepartmentName;
    
    private int appendsize;//附件个数
    
    @Column(name="ARCHIVEDATE",nullable=true)
    public Date getArchiveDate() {
		return archiveDate;
	}

	public void setArchiveDate(Date archiveDate) {
		this.archiveDate = archiveDate;
	}

	 @Column(name="FILEPIECENO",nullable=true)
	public String getFilePieceNo() {
		return filePieceNo;
	}

	public void setFilePieceNo(String filePieceNo) {
		this.filePieceNo = filePieceNo;
	}

	 @Column(name="FILEDEADLINE",nullable=true)
	public String getFileDeadline() {
		return fileDeadline;
	}

	public void setFileDeadline(String fileDeadline) {
		this.fileDeadline = fileDeadline;
	}

	@Transient
	public String getFileDeadlineName() {
		return fileDeadlineName;
	}

	public void setFileDeadlineName(String fileDeadlineName) {
		this.fileDeadlineName = fileDeadlineName;
	}
	 @Column(name="FILE_DEPARTMENT_NAME",nullable=true)
	public String getFileDepartmentName() {
		return fileDepartmentName;
	}

	public void setFileDepartmentName(String fileDepartmentName) {
		this.fileDepartmentName = fileDepartmentName;
	}

	@Transient
	public int getAppendsize() {
		return appendsize;
	}

	public void setAppendsize(int appendsize) {
		this.appendsize = appendsize;
	}

	/** full constructor */
    public ToaArchiveFileBak(String fileId, String fileNo, String fileAuthor, String fileTitle, Date fileDate, Long filePage, String fileDesc, String fileDepartment, String fileDocType, String fileDocId, com.strongit.oa.bo.ToaArchiveFolderBak toaArchiveFolderBak, Set toaArchiveFileAppendBaks) {
        this.fileId = fileId;
        this.fileNo = fileNo;
        this.fileAuthor = fileAuthor;
        this.fileTitle = fileTitle;
        this.fileDate = fileDate;
        this.filePage = filePage;
        this.fileDesc = fileDesc;
        this.fileDepartment = fileDepartment;
        this.fileDocType = fileDocType;
        this.fileDocId = fileDocId;
        this.toaArchiveFolderBak = toaArchiveFolderBak;
        this.toaArchiveFileAppendBaks = toaArchiveFileAppendBaks;
    }

    /** default constructor */
    public ToaArchiveFileBak() {
    }

    /** minimal constructor */
    public ToaArchiveFileBak(String fileId, com.strongit.oa.bo.ToaArchiveFolderBak toaArchiveFolderBak, Set toaArchiveFileAppendBaks) {
        this.fileId = fileId;
        this.toaArchiveFolderBak = toaArchiveFolderBak;
        this.toaArchiveFileAppendBaks = toaArchiveFileAppendBaks;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FILE_ID"
     *         
     */
    @Id
	@Column(name="FILE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_NO"
     *             length="25"
     *         
     */
    @Column(name="FILE_NO",nullable=true)
    public String getFileNo() {
        return this.fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_AUTHOR"
     *             length="50"
     *         
     */
    @Column(name="FILE_AUTHOR",nullable=true)
    public String getFileAuthor() {
        return this.fileAuthor;
    }

    public void setFileAuthor(String fileAuthor) {
        this.fileAuthor = fileAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_TITLE"
     *             length="500"
     *         
     */
    @Column(name="FILE_TITLE",nullable=true)
    public String getFileTitle() {
        return this.fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_DATE"
     *             length="7"
     *         
     */
    @Column(name="FILE_DATE",nullable=true)
    public Date getFileDate() {
        return this.fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_PAGE"
     *             length="10"
     *         
     */
    @Column(name="FILE_PAGE",nullable=true)
    public Long getFilePage() {
        return this.filePage;
    }

    public void setFilePage(Long filePage) {
        this.filePage = filePage;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_DESC"
     *             length="4000"
     *         
     */
    @Column(name="FILE_DESC",nullable=true)
    public String getFileDesc() {
        return this.fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_DEPARTMENT"
     *             length="20"
     *         
     */
    @Column(name="FILE_DEPARTMENT",nullable=true)
    public String getFileDepartment() {
        return this.fileDepartment;
    }

    public void setFileDepartment(String fileDepartment) {
        this.fileDepartment = fileDepartment;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FOLDER_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="FOLDER_ID", nullable=true)
    public com.strongit.oa.bo.ToaArchiveFolderBak getToaArchiveFolderBak() {
        return this.toaArchiveFolderBak;
    }

    public void setToaArchiveFolderBak(com.strongit.oa.bo.ToaArchiveFolderBak toaArchiveFolderBak) {
        this.toaArchiveFolderBak = toaArchiveFolderBak;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FILE_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveFileAppendBak"
     *         
     */
    @OneToMany(mappedBy="toaArchiveFileBak",targetEntity=com.strongit.oa.bo.ToaArchiveFileAppendBak.class,cascade=CascadeType.ALL)
    public Set getToaArchiveFileAppendBaks() {
        return this.toaArchiveFileAppendBaks;
    }

    public void setToaArchiveFileAppendBaks(Set toaArchiveFileAppendBaks) {
        this.toaArchiveFileAppendBaks = toaArchiveFileAppendBaks;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("fileId", getFileId())
            .toString();
    }

    /** 
     *            @hibernate.property
     *             column="FILE_DOC_ID"
     *             length="100"
     *         
     */
    @Column(name="FILE_DOC_ID",nullable=true)
	public String getFileDocId() {
		return fileDocId;
	}

	public void setFileDocId(String fileDocId) {
		this.fileDocId = fileDocId;
	}

	/** 
     *            @hibernate.property
     *             column="FILE_DOC_TYPE"
     *             length="1"
     *         
     */
    @Column(name="FILE_DOC_TYPE",nullable=true)
	public String getFileDocType() {
		return fileDocType;
	}

	public void setFileDocType(String fileDocType) {
		this.fileDocType = fileDocType;
	}

	@Column(name="FILE_ORGCODE",nullable=true)
	public String getFileOrgcode() {
		return fileOrgcode;
	}

	public void setFileOrgcode(String fileOrgcode) {
		this.fileOrgcode = fileOrgcode;
	}

}
