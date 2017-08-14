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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_REPORT_SORT"
 *     
*/
@Entity
@Table(name="T_OA_REPORT_SORT")
public class ToaReportSort implements Serializable {

    /** identifier field */
    private String sortId;

    /** nullable persistent field */
    private String sortName;

    /** nullable persistent field */
    private Long sortSequence;

    /** nullable persistent field */
    private String sortDesc;

    /** persistent field */
    private Set toaReportDefinitions;
    
    /**组织机构权限 添加机构CODE 和ID字段 */
    private String sortOrgCode;
    
    private String sortOrgId;

    /** full constructor */
    public ToaReportSort(String sortId, String sortName, Long sortSequence, String sortDesc, Set toaReportDefinitions) {
        this.sortId = sortId;
        this.sortName = sortName;
        this.sortSequence = sortSequence;
        this.sortDesc = sortDesc;
        this.toaReportDefinitions = toaReportDefinitions;
    }

    /** default constructor */
    public ToaReportSort() {
    }

    /** minimal constructor */
    public ToaReportSort(String sortId, Set toaReportDefinitions) {
        this.sortId = sortId;
        this.toaReportDefinitions = toaReportDefinitions;
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
     *             length="255"
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
     *             column="SORT_SEQUENCE"
     *             length="100"
     *         
     */
    @Column(name="SORT_SEQUENCE",nullable=true)
    public Long getSortSequence() {
        return this.sortSequence;
    }

    public void setSortSequence(Long sortSequence) {
        this.sortSequence = sortSequence;
    }
    
    /** 
     *            @hibernate.property
     *             column="SORT_DESC"
     *             length="500"
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
     *             class="com.strongit.oa.bo.ToaReportDefinition"
     *         
     */
    @OneToMany(mappedBy="toaReportSort",targetEntity=com.strongit.oa.bo.ToaReportDefinition.class,cascade=CascadeType.ALL)
    public Set getToaReportDefinitions() {
        return this.toaReportDefinitions;
    }

    public void setToaReportDefinitions(Set toaReportDefinitions) {
        this.toaReportDefinitions = toaReportDefinitions;
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
