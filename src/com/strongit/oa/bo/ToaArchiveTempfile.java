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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_TEMPFILE"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_TEMPFILE")
public class ToaArchiveTempfile implements Serializable {

    /** identifier field  文件ID*/
	@Id 
	@GeneratedValue
    private String tempfileId;

    /** nullable persistent field 文件编号*/
    private String tempfileNo;

    /** nullable persistent field 文件作者*/
    private String tempfileAuthor;

    /** nullable persistent field 文件标题*/
    private String tempfileTitle;

    /** nullable persistent field 创建日期，*/
    private Date tempfileDate;

    /** nullable persistent field 文件页数*/
    private Long tempfilePage;

    /** nullable persistent field 文件描述*/
    private String tempfileDesc;

    /** nullable persistent field 所属部门*/
    private String tempfileDepartment;

    /** nullable persistent field 文件类型*/
    private String tempfileDocType;

    /** nullable persistent field */
    private String tempfileDocId;
    /** 文件年份，从外界流程过来文件时，该字段不用赋值，只要文件创建如期不为空就可以了*/
    private String tempfileYear;//年份
    /** 文件月份，从外界流程过来文件时，该字段不用赋值，只要文件创建如期不为空就可以了*/
    private String tempfileMonth;//月份
    /** 件号*/
    private String tempfilePieceNo ;
    /**保管期限ID */
    private String tempfileDeadlineId;
    /**保管期限*/
    private String tempfileDeadline ;
    /**顺序号*/
    private String tempfile_sortorder ;
    /**
     * 地点
     */
    private String tempfilePlace ;
    /**人（物）*/
    private String tempfileFigure ;
    /**事由）*/
    private String tempfileReasons ;
    /**颁奖单位ID*/
    private String tempfileAwardsOrgId  ; 
    /**颁奖单位）*/
    private String tempfileAwardsOrg  ;
    /**获奖内容*/
    private String tempfileAwardsContent  ;
    
    /** 所属机构* 郑志斌 2010-12-28 机构授权开关，添加字段 / */
	private String tempfileOrgCode;
	
	 /** 所属机构* 郑志斌 2010-12-28 机构授权开关，添加字段 / */
	private String tempfileOrgId;
    

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder;

    /** persistent field */
    private Set toaArchiveTfileAppends;
    
    private Set toaArchiveTempfilePrivil;
    
    private int appendsize;//附件数量
    private String tempfileDepartmentName;//部门名称
    /** 流程ID*/
    private String workflow;
    private String tempfileFormId;//表单ID
    @Column(name="TEMPFILE_DEPARTMENTNAME",nullable=true)
    public String getTempfileDepartmentName() {
		return tempfileDepartmentName;
	}

	public void setTempfileDepartmentName(String tempfileDepartmentName) {
		this.tempfileDepartmentName = tempfileDepartmentName;
	}

	@Transient
    public int getAppendsize() {
		return appendsize;
	}

	public void setAppendsize(int appendsize) {
		this.appendsize = appendsize;
	}

	/** full constructor */
    public ToaArchiveTempfile(String tempfileId, String tempfileNo, String tempfileAuthor, String tempfileTitle, Date tempfileDate, Long tempfilePage, String tempfileDesc, String tempfileDepartment, String tempfileDocType,
    			String tempfileDocId, com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder, Set toaArchiveTfileAppends,Set toaArchiveTempfilePrivil,String tempfilePieceNo ) {
        this.tempfileId = tempfileId;
        this.tempfileNo = tempfileNo;
        this.tempfileAuthor = tempfileAuthor;
        this.tempfileTitle = tempfileTitle;
        this.tempfileDate = tempfileDate;
        this.tempfilePage = tempfilePage;
        this.tempfileDesc = tempfileDesc;
        this.tempfileDepartment = tempfileDepartment;
        this.tempfileDocType = tempfileDocType;
        this.tempfileDocId = tempfileDocId;
        this.toaArchiveFolder = toaArchiveFolder;
        this.toaArchiveTfileAppends = toaArchiveTfileAppends;
        this.toaArchiveTempfilePrivil=toaArchiveTempfilePrivil;
        this.tempfilePieceNo=tempfilePieceNo;
    }

    /** default constructor */
    public ToaArchiveTempfile() {
    }

    /** minimal constructor */
    public ToaArchiveTempfile(String tempfileId, com.strongit.oa.bo.ToaArchiveFolder toaArchiveFolder, Set toaArchiveTfileAppends) {
        this.tempfileId = tempfileId;
        this.toaArchiveFolder = toaArchiveFolder;
        this.toaArchiveTfileAppends = toaArchiveTfileAppends;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="TEMPFILE_ID"
     *         
     */
    @Id
	@Column(name="TEMPFILE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getTempfileId() {
        return this.tempfileId;
    }

    public void setTempfileId(String tempfileId) {
        this.tempfileId = tempfileId;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_NO"
     *             length="25"
     *         
     */
    @Column(name="TEMPFILE_NO",nullable=true)
    public String getTempfileNo() {
        return this.tempfileNo;
    }

    public void setTempfileNo(String tempfileNo) {
        this.tempfileNo = tempfileNo;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_AUTHOR"
     *             length="50"
     *         
     */
    @Column(name="TEMPFILE_AUTHOR",nullable=true)
    public String getTempfileAuthor() {
        return this.tempfileAuthor;
    }

    public void setTempfileAuthor(String tempfileAuthor) {
        this.tempfileAuthor = tempfileAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_TITLE"
     *             length="500"
     *         
     */
    @Column(name="TEMPFILE_TITLE",nullable=true)
    public String getTempfileTitle() {
        return this.tempfileTitle;
    }

    public void setTempfileTitle(String tempfileTitle) {
        this.tempfileTitle = tempfileTitle;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_DATE"
     *             length="7"
     *         
     */
    @Column(name="TEMPFILE_DATE",nullable=true)
    public Date getTempfileDate() {
        return this.tempfileDate;
    }

    public void setTempfileDate(Date tempfileDate) {
        this.tempfileDate = tempfileDate;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_PAGE"
     *             length="10"
     *         
     */
    @Column(name="TEMPFILE_PAGE",nullable=true)
    public Long getTempfilePage() {
        return this.tempfilePage;
    }

    public void setTempfilePage(Long tempfilePage) {
        this.tempfilePage = tempfilePage;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_DESC"
     *             length="4000"
     *         
     */
    @Column(name="TEMPFILE_DESC",nullable=true)
    public String getTempfileDesc() {
        return this.tempfileDesc;
    }

    public void setTempfileDesc(String tempfileDesc) {
        this.tempfileDesc = tempfileDesc;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_DEPARTMENT"
     *             length="20"
     *         
     */
    @Column(name="TEMPFILE_DEPARTMENT",nullable=true)
    public String getTempfileDepartment() {
        return this.tempfileDepartment;
    }

    public void setTempfileDepartment(String tempfileDepartment) {
        this.tempfileDepartment = tempfileDepartment;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPFILE_DOC_TYPE"
     *             length="1"
     *         
     */
    @Column(name="TEMPFILE_DOC_TYPE",nullable=true)
    public String getTempfileDocType() {
        return this.tempfileDocType;
    }

    public void setTempfileDocType(String tempfileDocType) {
        this.tempfileDocType = tempfileDocType;
    }

    
    /** 
     *            @hibernate.property
     *             column="TEMPFILE_DOC_ID"
     *             length="100"
     *         
     */
    @Column(name="TEMPFILE_DOC_ID",nullable=true)
    public String getTempfileDocId() {
        return this.tempfileDocId;
    }

    public void setTempfileDocId(String tempfileDocId) {
        this.tempfileDocId = tempfileDocId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="false"
     *            @hibernate.column name="FOLDER_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="FOLDER_ID", nullable=true,unique=true)
    @NotFound(action=NotFoundAction.IGNORE)
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
     *             column="TEMPFILE_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveTfileAppend"
     *         
     */
    @OneToMany(mappedBy="toaArchiveTempfile",targetEntity=com.strongit.oa.bo.ToaArchiveTfileAppend.class,cascade={CascadeType.REMOVE})
    public Set getToaArchiveTfileAppends() {
        return this.toaArchiveTfileAppends;
    }

    
    public void setToaArchiveTfileAppends(Set toaArchiveTfileAppends) {
        this.toaArchiveTfileAppends = toaArchiveTfileAppends;
    }

	@OneToMany(mappedBy="toaArchiveTempfile",targetEntity=com.strongit.oa.bo.ToaArchiveTempfilePrivil.class,cascade=CascadeType.ALL)
	public Set getToaArchiveTempfilePrivil() {
		return toaArchiveTempfilePrivil;
	}

    public void setToaArchiveTempfilePrivil(Set toaArchiveTempfilePrivil) {
		this.toaArchiveTempfilePrivil = toaArchiveTempfilePrivil;
	}
   
    public String toString() {
        return new ToStringBuilder(this)
            .append("tempfileId", getTempfileId())
            .toString();
    }

    @Column(name="TEMPFILE_YEAR",nullable=true)
	public String getTempfileYear() {
		return tempfileYear;
	}

	public void setTempfileYear(String tempfileYear) {
		this.tempfileYear = tempfileYear;
	}

	@Column(name="TEMPFILE_MONTH" ,nullable=true)
	public String getTempfileMonth() {
		return tempfileMonth;
	}

	public void setTempfileMonth(String tempfileMonth) {
		this.tempfileMonth = tempfileMonth;
	}

	@Column(name="WORKFLOW" ,nullable=true)
	public String getWorkflow() {
		return workflow;
	}

	public void setWorkflow(String workflow) {
		this.workflow = workflow;
	}

	@Column(name="TEMPFILE_FORMID" ,nullable=true)
	public String getTempfileFormId() {
		return tempfileFormId;
	}

	public void setTempfileFormId(String tempfileFormId) {
		this.tempfileFormId = tempfileFormId;
	}
	@Column(name="TEMPFILE_PIECENO" ,nullable=true)
	public String getTempfilePieceNo() {
		return tempfilePieceNo;
	}

	public void setTempfilePieceNo(String tempfilePieceNo) {
		this.tempfilePieceNo = tempfilePieceNo;
	}
	@Column(name="TEMPFILE_DEADLINE" ,nullable=true)
	public String getTempfileDeadline() {
		return tempfileDeadline;
	}

	public void setTempfileDeadline(String tempfileDeadline) {
		this.tempfileDeadline = tempfileDeadline;
	}
	@Column(name="TEMPFILE_SORTORDER" ,nullable=true)
	public String getTempfile_sortorder() {
		return tempfile_sortorder;
	}

	public void setTempfile_sortorder(String tempfile_sortorder) {
		this.tempfile_sortorder = tempfile_sortorder;
	}
	@Column(name="TEMPFILE_AWARDSCONTENT" ,nullable=true)
	public String getTempfileAwardsContent() {
		return tempfileAwardsContent;
	}

	public void setTempfileAwardsContent(String tempfileAwardsContent) {
		this.tempfileAwardsContent = tempfileAwardsContent;
	}
	@Column(name="TEMPFILE_AWARDSORG" ,nullable=true)
	public String getTempfileAwardsOrg() {
		return tempfileAwardsOrg;
	}

	public void setTempfileAwardsOrg(String tempfileAwardsOrg) {
		this.tempfileAwardsOrg = tempfileAwardsOrg;
	}
	@Column(name="TEMPFILE_REASONS" ,nullable=true)
	public String getTempfileReasons() {
		return tempfileReasons;
	}

	public void setTempfileReasons(String tempfileReasons) {
		this.tempfileReasons = tempfileReasons;
	}
	@Column(name="TEMPFILE_FIGURE" ,nullable=true)
	public String getTempfileFigure() {
		return tempfileFigure;
	}

	public void setTempfileFigure(String tempfileFigure) {
		this.tempfileFigure = tempfileFigure;
	}
	@Column(name="TEMPFILE_PLACE" ,nullable=true)
	public String getTempfilePlace() {
		return tempfilePlace;
	}

	public void setTempfilePlace(String tempfilePlace) {
		this.tempfilePlace = tempfilePlace;
	}
	@Column(name="TEMPFILE_DEADLINEID" ,nullable=true)
	public String getTempfileDeadlineId() {
		return tempfileDeadlineId;
	}

	public void setTempfileDeadlineId(String tempfileDeadlineId) {
		this.tempfileDeadlineId = tempfileDeadlineId;
	}
	@Column(name="TEMPFILE_AWARDSORGID" ,nullable=true)
	public String getTempfileAwardsOrgId() {
		return tempfileAwardsOrgId;
	}

	public void setTempfileAwardsOrgId(String tempfileAwardsOrgId) {
		this.tempfileAwardsOrgId = tempfileAwardsOrgId;
	}

	@Column(name="TEMPFILE_ORGCODE",nullable=true)
	public String getTempfileOrgCode() {
		return tempfileOrgCode;
	}

	public void setTempfileOrgCode(String tempfileOrgCode) {
		this.tempfileOrgCode = tempfileOrgCode;
	}

	@Column(name="TEMPFILE_ORGID",nullable=true)
	public String getTempfileOrgId() {
		return tempfileOrgId;
	}

	public void setTempfileOrgId(String tempfileOrgId) {
		this.tempfileOrgId = tempfileOrgId;
	}



}
