package com.strongit.oa.bo;

import java.io.Serializable;
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
 *         table="T_OA_ARCHIVE_SORT"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_SORT")
public class ToaArchiveSort implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String sortId;

    /** nullable persistent field */
    private String sortName;

    /** nullable persistent field */
    private String sortNo;

    /** nullable persistent field */
    private String sortParentNo;

    /** nullable persistent field */
    private String sortArrayNo;

    /** nullable persistent field */
    private String sortDesc;

    /** persistent field */
    private Set toaArchiveFolders;
    private String orgId;//部门ID
    
    /**机构授权，添加机构ID和CODE字段  */
    private String sortOrgId;		
    
    private String sortOrgCode;
    
    @Transient
    public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/** full constructor */
    public ToaArchiveSort(String sortId, String sortName, String sortNo, String sortParentNo, String sortArrayNo, String sortDesc, Set toaArchiveFolders) {
        this.sortId = sortId;
        this.sortName = sortName;
        this.sortNo = sortNo;
        this.sortParentNo = sortParentNo;
        this.sortArrayNo = sortArrayNo;
        this.sortDesc = sortDesc;
        this.toaArchiveFolders = toaArchiveFolders;
    }

    /** default constructor */
    public ToaArchiveSort() {
    }

    /** minimal constructor */
    public ToaArchiveSort(String sortId, Set toaArchiveFolders) {
        this.sortId = sortId;
        this.toaArchiveFolders = toaArchiveFolders;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SORT_ID"
     *         
     */
    @Id
	@Column(name="SORT_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSortId() {
        return this.sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    /** 
     *            @hibernate.property
     *             column="SORT_NAME"
     *             length="40"
     *         
     */
    @Column(name="SORT_NAME",nullable=true)
    public String getSortName() {
        return this.sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    /** 
     *            @hibernate.property
     *             column="SORT_NO"
     *             length="30"
     *         
     */
    @Column(name="SORT_NO",nullable=true)
    public String getSortNo() {
        return this.sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    /** 
     *            @hibernate.property
     *             column="SORT_PARENT_NO"
     *             length="32"
     *         
     */
    @Column(name="SORT_PARENT_NO",nullable=true)
    public String getSortParentNo() {
        return this.sortParentNo;
    }

    public void setSortParentNo(String sortParentNo) {
        this.sortParentNo = sortParentNo;
    }

    /** 
     *            @hibernate.property
     *             column="SORT_ARRAY_NO"
     *             length="40"
     *         
     */
    @Column(name="SORT_ARRAY_NO",nullable=true)
    public String getSortArrayNo() {
        return this.sortArrayNo;
    }

    public void setSortArrayNo(String sortArrayNo) {
        this.sortArrayNo = sortArrayNo;
    }

    /** 
     *            @hibernate.property
     *             column="SORT_DESC"
     *             length="400"
     *         
     */
    @Column(name="SORT_DESC",nullable=true)
    public String getSortDesc() {
        return this.sortDesc;
    }

    public void setSortDesc(String sortDesc) {
        this.sortDesc = sortDesc;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="SORT_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaArchiveFolder"
     *         
     */
    @OneToMany(mappedBy="toaArchiveSort",targetEntity=com.strongit.oa.bo.ToaArchiveFolder.class,cascade=CascadeType.ALL)
    public Set getToaArchiveFolders() {
        return this.toaArchiveFolders;
    }

    public void setToaArchiveFolders(Set toaArchiveFolders) {
        this.toaArchiveFolders = toaArchiveFolders;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("sortId", getSortId())
            .toString();
    }

    @Column(name="SORT_ORGCODE",nullable=true)
	public String getSortOrgCode() {
		return sortOrgCode;
	}

	public void setSortOrgCode(String sortOrgCode) {
		this.sortOrgCode = sortOrgCode;
	}

	 @Column(name="SORT_ORGID",nullable=true)
	public String getSortOrgId() {
		return sortOrgId;
	}

	public void setSortOrgId(String sortOrgId) {
		this.sortOrgId = sortOrgId;
	}

}
