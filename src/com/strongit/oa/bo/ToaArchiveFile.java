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
 *         table="T_OA_ARCHIVE_FILE"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_FILE")
public class ToaArchiveFile implements Serializable {

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
    
    private String fileYear;
    private String fileMonth;

    private int appendsize;//附件个数
    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder;

    /** persistent field */
    private Set toaArchiveBorrows;

    /** persistent field */
    private Set toaArchiveFileAppends;

    private String fileDepartmentName;
    /** 流程ID*/
    private String workflow;
    private String fileFormId;//表单ID
    /** 件号*/
    private String filePieceNo ;
    /** 保管期限ID */
    private String fileDeadlineId; 
    /**保管期限*/
    private String fileDeadline ;
    /**顺序号*/
    private String file_sortorder ;
    /**地点*/
    private String filePlace;
    /**人（物）*/
    private String fileFigure;
    /**事由*/
    private String fileReasons ;
    /**颁奖单位ID */
    private String fileAwardsOrgId;
    /**颁奖单位*/
    private String fileAwardsOrg;
    /**获奖*/
    private String fileAwardsContent ;
    
    /** 机构CODE]
     * 郑志斌 2010-12-20 添加机构授权开关*/
    private String fileOrgcode;
    
    /** 机构CODE]
     * 郑志斌 2010-12-20 添加机构授权开关*/
    private String fileOrgId;
    
    @Column(name="FILE_FORMID",nullable=true)
    public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}
	 @Column(name="WORKFLOW",nullable=true)
	public String getFileFormId() {
		return fileFormId;
	}

	public void setFileFormId(String fileFormId) {
		this.fileFormId = fileFormId;
	}

	/** full constructor */
    public ToaArchiveFile(String fileId, String fileNo, String fileAuthor, String fileTitle, Date fileDate, Long filePage, String fileDesc, String fileDepartment, String fileDocType, String fileDocId, com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder, Set toaArchiveBorrows, Set toaArchiveFileAppends) {
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
        this.toaArchiveFolder = toaArchiveFolder;
        this.toaArchiveBorrows = toaArchiveBorrows;
        this.toaArchiveFileAppends = toaArchiveFileAppends;
    }

    /** default constructor */
    public ToaArchiveFile() {
    }

    /** minimal constructor */
    public ToaArchiveFile(String fileId, com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder, Set toaArchiveBorrows, Set toaArchiveFileAppends) {
        this.fileId = fileId;
        this.toaArchiveFolder = toaArchiveFolder;
        this.toaArchiveBorrows = toaArchiveBorrows;
        this.toaArchiveFileAppends = toaArchiveFileAppends;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Long"
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
     *            @hibernate.property
     *             column="FILE_DOC_TYPE"
     *             length="1"
     *         
     */
    @Column(name="FILE_DOC_TYPE",nullable=true)
    public String getFileDocType() {
        return this.fileDocType;
    }

    public void setFileDocType(String fileDocType) {
        this.fileDocType = fileDocType;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_DOC_ID"
     *             length="100"
     *         
     */
    @Column(name="FILE_DOC_ID",nullable=true)
    public String getFileDocId() {
        return this.fileDocId;
    }

    public void setFileDocId(String fileDocId) {
        this.fileDocId = fileDocId;
    }
       
    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FOLDER_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="FOLDER_ID", nullable=true)
    public com.strongit.oa.bo.ToaArchiveFolder getToaArchiveFolder() {
        return this.toaArchiveFolder;
    }

    public void setToaArchiveFolder(com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder) {
        this.toaArchiveFolder = toaArchiveFolder;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FILE_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveBorrow"
     *         
     */
    @OneToMany(mappedBy="toaArchiveFile",targetEntity=com.strongit.oa.bo.ToaArchiveBorrow.class,cascade=CascadeType.ALL)
    public Set getToaArchiveBorrows() {
        return this.toaArchiveBorrows;
    }

    public void setToaArchiveBorrows(Set toaArchiveBorrows) {
        this.toaArchiveBorrows = toaArchiveBorrows;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FILE_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveFileAppend"
     *         
     */
    @OneToMany(mappedBy="toaArchiveFile",targetEntity=com.strongit.oa.bo.ToaArchiveFileAppend.class,cascade={CascadeType.REMOVE})
    public Set getToaArchiveFileAppends() {
        return this.toaArchiveFileAppends;
    }

    public void setToaArchiveFileAppends(Set toaArchiveFileAppends) {
        this.toaArchiveFileAppends = toaArchiveFileAppends;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("fileId", getFileId())
            .toString();
    }

    @Column(name="FILE_YEAR",nullable=true)
	public String getFileYear() {
		return fileYear;
	}

	public void setFileYear(String fileYear) {
		this.fileYear = fileYear;
	}

	 @Column(name="FILE_MONTH",nullable=true)
	public String getFileMonth() {
		return fileMonth;
	}

	public void setFileMonth(String fileMonth) {
		this.fileMonth = fileMonth;
	}

	@Transient
	public int getAppendsize() {
		return appendsize;
	}

	public void setAppendsize(int appendsize) {
		this.appendsize = appendsize;
	}

	@Column(name="FILE_DEPARTMENT_NAME",nullable=true)
	public String getFileDepartmentName() {
		return fileDepartmentName;
	}

	public void setFileDepartmentName(String fileDepartmentName) {
		this.fileDepartmentName = fileDepartmentName;
	}
	@Column(name="FILE_PIECENO",nullable=true)
	public String getFilePieceNo() {
		return filePieceNo;
	}

	public void setFilePieceNo(String filePieceNo) {
		this.filePieceNo = filePieceNo;
	}
	@Column(name="FILE_DEADLINE",nullable=true)
	public String getFileDeadline() {
		return fileDeadline;
	}

	public void setFileDeadline(String fileDeadline) {
		this.fileDeadline = fileDeadline;
	}
	@Column(name="FILE_SORTORDER",nullable=true)
	public String getFile_sortorder() {
		return file_sortorder;
	}

	public void setFile_sortorder(String file_sortorder) {
		this.file_sortorder = file_sortorder;
	}
	@Column(name="FILE_AWARDSCONTENT",nullable=true)
	public String getFileAwardsContent() {
		return fileAwardsContent;
	}

	public void setFileAwardsContent(String fileAwardsContent) {
		this.fileAwardsContent = fileAwardsContent;
	}
	@Column(name="FILE_AWARDSORG",nullable=true)
	public String getFileAwardsOrg() {
		return fileAwardsOrg;
	}

	public void setFileAwardsOrg(String fileAwardsOrg) {
		this.fileAwardsOrg = fileAwardsOrg;
	}
	@Column(name="FILE_FIGURE",nullable=true)
	public String getFileFigure() {
		return fileFigure;
	}

	public void setFileFigure(String fileFigure) {
		this.fileFigure = fileFigure;
	}

	@Column(name="FILE_REASONS",nullable=true)
	public String getFileReasons() {
		return fileReasons;
	}

	public void setFileReasons(String fileReasons) {
		this.fileReasons = fileReasons;
	}
	@Column(name="FILE_PLACE",nullable=true)
	public String getFilePlace() {
		return filePlace;
	}

	public void setFilePlace(String filePlace) {
		this.filePlace = filePlace;
	}
	@Column(name="FILE_DEADLINEID",nullable=true)
	public String getFileDeadlineId() {
		return fileDeadlineId;
	}

	public void setFileDeadlineId(String fileDeadlineId) {
		this.fileDeadlineId = fileDeadlineId;
	}
	@Column(name="FILE_AWARDSORGID",nullable=true)
	public String getFileAwardsOrgId() {
		return fileAwardsOrgId;
	}

	public void setFileAwardsOrgId(String fileAwardsOrgId) {
		this.fileAwardsOrgId = fileAwardsOrgId;
	}

	@Column(name="FILE_ORGCODE",nullable=true)
	public String getFileOrgcode() {
		return fileOrgcode;
	}

	public void setFileOrgcode(String fileOrgcode) {
		this.fileOrgcode = fileOrgcode;
	}

	@Column(name="FILE_ORGID",nullable=true)
	public String getFileOrgId() {
		return fileOrgId;
	}

	public void setFileOrgId(String fileOrgId) {
		this.fileOrgId = fileOrgId;
	}
    

}
