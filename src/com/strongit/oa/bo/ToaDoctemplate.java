package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_DOCTEMPLATE"
 *     
*/
@Entity
@Table(name="T_OA_DOCTEMPLATE",catalog="",schema="")
public class ToaDoctemplate implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String doctemplateId;

    /** nullable persistent field */
    private String doctemplateTitle;

    /** nullable persistent field */
    private byte[] doctemplateContent;

    /** nullable persistent field */
    private Date doctemplateCreateTime;

    /** nullable persistent field */
    private String doctemplateRemark;
    
    private String logo;		//图片类型
    
    private Long docNumber;		//排序号
    
    private String isHasImage;		//项目工程中是否存在当前图标，不在数据库中设置，
    
    /**组织机构授权，添加 机构 CODE 和ID字段 */
    private String doctemplateOrgCode;
    
    private String doctemplateOrgId;
    
    private String docType;
    
    


	/**文本编辑器内容 */
    private String doctemplateTxtContent;

    /** persistent field */
    private com.strongit.oa.bo.ToaDoctemplateGroup toaDoctemplateGroup;

    /** full constructor */
    public ToaDoctemplate(String doctemplateId, String doctemplateTitle, byte[] doctemplateContent, Date doctemplateCreateTime, String doctemplateRemark,
    		com.strongit.oa.bo.ToaDoctemplateGroup toaDoctemplateGroup,String doctemplateTxtContent,String docType ) {
        this.doctemplateId = doctemplateId;
        this.doctemplateTitle = doctemplateTitle;
        this.doctemplateContent = doctemplateContent;
        this.doctemplateCreateTime = doctemplateCreateTime;
        this.doctemplateRemark = doctemplateRemark;
        this.toaDoctemplateGroup = toaDoctemplateGroup;
        this.doctemplateTxtContent=doctemplateTxtContent;
        this.docType = docType;
    }

    /** default constructor */
    public ToaDoctemplate() {
    }

    /** minimal constructor */
    public ToaDoctemplate(String doctemplateId, com.strongit.oa.bo.ToaDoctemplateGroup toaDoctemplateGroup) {
        this.doctemplateId = doctemplateId;
        this.toaDoctemplateGroup = toaDoctemplateGroup;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOCTEMPLATE_ID"
     *         
     */
    @Id
	@Column(name="DOCTEMPLATE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDoctemplateId() {
        return this.doctemplateId;
    }

    public void setDoctemplateId(String doctemplateId) {
        this.doctemplateId = doctemplateId;
    }

    /** 
     *            @hibernate.property
     *             column="DOCTEMPLATE_TITLE"
     *             length="50"
     *         
     */
    @Column(name="DOCTEMPLATE_TITLE",nullable=true)
    public String getDoctemplateTitle() {
        return this.doctemplateTitle;
    }

    public void setDoctemplateTitle(String doctemplateTitle) {
        this.doctemplateTitle = doctemplateTitle;
    }

    /** 
     *            @hibernate.property
     *             column="DOCTEMPLATE_CONTENT"
     *             length="4000"
     *         
     */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOCTEMPLATE_CONTENT", columnDefinition = "BLOB")
    public byte[] getDoctemplateContent() {
        return this.doctemplateContent;
    }

    public void setDoctemplateContent(byte[] doctemplateContent) {
        this.doctemplateContent = doctemplateContent;
    }

    /** 
     *            @hibernate.property
     *             column="DOCTEMPLATE_CREATE_TIME"
     *             length="7"
     *         
     */
    @Column(name="DOCTEMPLATE_CREATE_TIME",nullable=true)
    public Date getDoctemplateCreateTime() {
        return this.doctemplateCreateTime;
    }

    public void setDoctemplateCreateTime(Date doctemplateCreateTime) {
        this.doctemplateCreateTime = doctemplateCreateTime;
    }

    /** 
     *            @hibernate.property
     *             column="DOCTEMPLATE_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="DOCTEMPLATE_REMARK",nullable=true)
    public String getDoctemplateRemark() {
        return this.doctemplateRemark;
    }

    public void setDoctemplateRemark(String doctemplateRemark) {
        this.doctemplateRemark = doctemplateRemark;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DOCGROUP_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="DOCGROUP_ID", nullable=false)
    public com.strongit.oa.bo.ToaDoctemplateGroup getToaDoctemplateGroup() {
        return this.toaDoctemplateGroup;
    }

    public void setToaDoctemplateGroup(com.strongit.oa.bo.ToaDoctemplateGroup toaDoctemplateGroup) {
        this.toaDoctemplateGroup = toaDoctemplateGroup;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("doctemplateId", getDoctemplateId())
            .toString();
    }

    @Column(name="LOGO",nullable=true)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	@Column(name="DOCTEMPLATE_TXTCONTENT",nullable=true)
	public String getDoctemplateTxtContent() {
		return doctemplateTxtContent;
	}

	public void setDoctemplateTxtContent(String doctemplateTxtContent) {
		this.doctemplateTxtContent = doctemplateTxtContent;
	}

	@Transient
	public String getIsHasImage() {
		return isHasImage;
	}

	public void setIsHasImage(String isHasImage) {
		this.isHasImage = isHasImage;
	}

	@Column(name = "DOCNUMBER")
	public Long getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(Long docNumber) {
		this.docNumber = docNumber;
	}

	@Column(name="DOCTEMPLATE_ORGCODE",nullable=true)
	public String getDoctemplateOrgCode() {
		return doctemplateOrgCode;
	}

	public void setDoctemplateOrgCode(String doctemplateOrgCode) {
		this.doctemplateOrgCode = doctemplateOrgCode;
	}

	@Column(name="DOCTEMPLATE_ORGID",nullable=true)
	public String getDoctemplateOrgId() {
		return doctemplateOrgId;
	}

	public void setDoctemplateOrgId(String doctemplateOrgId) {
		this.doctemplateOrgId = doctemplateOrgId;
	}
	
	@Column(name="DOCTEMPLATE_DOCTYPE",nullable=true)
    public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

}
