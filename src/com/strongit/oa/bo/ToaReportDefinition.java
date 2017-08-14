package com.strongit.oa.bo;

import java.io.Serializable;
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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;


/** 
 *        @hibernate.class
 *         table="T_OA_REPORT_DEFINITION"
 *     
*/
@Entity
@Table(name="T_OA_REPORT_DEFINITION")
public class ToaReportDefinition implements Serializable {

    /** identifier field */
    private String definitionId;

    /** nullable persistent field */
    private String definitionName;

    /** nullable persistent field */
    private String definitionFormid;

    /** nullable persistent field */
    private String definitionFormname;

    /** nullable persistent field */
    private String definitionWorkflowname;

    /** nullable persistent field */
    private String definitionWorkflowid;

    /** nullable persistent field */
    private Long definitionSequence;
    
    private String definitionCreateUserId;

    /** persistent field */
    private com.strongit.oa.bo.ToaReportSort toaReportSort;

    /** persistent field */
    private Set toaReportPrivilsets;

    /** persistent field */
    private Set toaReportShowitems;
    
    /** 组织机构，添加机构CODE和 ID字段 */
    private String definitionOrgCode;
    
    private String definitionOrgId;

    /** full constructor */
    public ToaReportDefinition(String definitionId, String definitionName, String definitionFormid, String definitionFormname, String definitionWorkflowname, String definitionWorkflowid, Long definitionSequence,String definitionCreateUserId, com.strongit.oa.bo.ToaReportSort toaReportSort, Set toaReportPrivilsets, Set toaReportShowitems) {
        this.definitionId = definitionId;
        this.definitionName = definitionName;
        this.definitionFormid = definitionFormid;
        this.definitionFormname = definitionFormname;
        this.definitionWorkflowname = definitionWorkflowname;
        this.definitionWorkflowid = definitionWorkflowid;
        this.definitionSequence = definitionSequence;
        this.definitionCreateUserId=definitionCreateUserId;
        this.toaReportSort = toaReportSort;
        this.toaReportPrivilsets = toaReportPrivilsets;
        this.toaReportShowitems = toaReportShowitems;
    }

    /** default constructor */
    public ToaReportDefinition() {
    }

    /** minimal constructor */
    public ToaReportDefinition(String definitionId, com.strongit.oa.bo.ToaReportSort toaReportSort, Set toaReportPrivilsets, Set toaReportShowitems) {
        this.definitionId = definitionId;
        this.toaReportSort = toaReportSort;
        this.toaReportPrivilsets = toaReportPrivilsets;
        this.toaReportShowitems = toaReportShowitems;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DEFINITION_ID"
     *         
     */
    @Id
	@Column(name="DEFINITION_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDefinitionId() {
        return this.definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    /** 
     *            @hibernate.property
     *             column="DEFINITION_NAME"
     *             length="255"
     *         
     */
    @Column(name="DEFINITION_NAME",nullable=true)
    public String getDefinitionName() {
        return this.definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    /** 
     *            @hibernate.property
     *             column="DEFINITION_FORMID"
     *             length="32"
     *         
     */
    @Column(name="DEFINITION_FORMID",nullable=true)
    public String getDefinitionFormid() {
        return this.definitionFormid;
    }

    public void setDefinitionFormid(String definitionFormid) {
        this.definitionFormid = definitionFormid;
    }

    /** 
     *            @hibernate.property
     *             column="DEFINITION_FORMNAME"
     *             length="255"
     *         
     */
    @Column(name="DEFINITION_FORMNAME",nullable=true)
    public String getDefinitionFormname() {
        return this.definitionFormname;
    }

    public void setDefinitionFormname(String definitionFormname) {
        this.definitionFormname = definitionFormname;
    }

    /** 
     *            @hibernate.property
     *             column="DEFINITION_WORKFLOWNAME"
     *             length="255"
     *         
     */
    @Column(name="DEFINITION_WORKFLOWNAME",nullable=true)
    public String getDefinitionWorkflowname() {
        return this.definitionWorkflowname;
    }

    public void setDefinitionWorkflowname(String definitionWorkflowname) {
        this.definitionWorkflowname = definitionWorkflowname;
    }

    /** 
     *            @hibernate.property
     *             column="DEFINITION_WORKFLOWID"
     *             length="32"
     *         
     */
    @Column(name="DEFINITION_WORKFLOWID",nullable=true)
    public String getDefinitionWorkflowid() {
        return this.definitionWorkflowid;
    }

    public void setDefinitionWorkflowid(String definitionWorkflowid) {
        this.definitionWorkflowid = definitionWorkflowid;
    }
    
    /** 
     *            @hibernate.property
     *             column="DEFINITION_SEQUENCE"
     *             length="100"
     *         
     */
    @Column(name="DEFINITION_SEQUENCE",nullable=true)
    public Long getDefinitionSequence() {
        return this.definitionSequence;
    }

    public void setDefinitionSequence(Long definitionSequence) {
        this.definitionSequence = definitionSequence;
    }

    

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="SORT_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="SORT_ID", nullable=true)
    public com.strongit.oa.bo.ToaReportSort getToaReportSort() {
        return this.toaReportSort;
    }

    public void setToaReportSort(com.strongit.oa.bo.ToaReportSort toaReportSort) {
        this.toaReportSort = toaReportSort;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DEFINITION_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaReportPrivilset"
     *         
     */
    @OneToMany(mappedBy="toaReportDefinition",targetEntity=com.strongit.oa.bo.ToaReportPrivilset.class,cascade=CascadeType.ALL)
    public Set getToaReportPrivilsets() {
        return this.toaReportPrivilsets;
    }

    public void setToaReportPrivilsets(Set toaReportPrivilsets) {
        this.toaReportPrivilsets = toaReportPrivilsets;
    }
    
    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DEFINITION_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaReportShowitem"
     *         
     */
    @OneToMany(mappedBy="toaReportDefinition",targetEntity=com.strongit.oa.bo.ToaReportShowitem.class,cascade=CascadeType.ALL)
    public Set getToaReportShowitems() {
        return this.toaReportShowitems;
    }

    public void setToaReportShowitems(Set toaReportShowitems) {
        this.toaReportShowitems = toaReportShowitems;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("definitionId", getDefinitionId())
            .toString();
    }

    /** 
     *            @hibernate.property
     *             column="DEFINITION_CREATEUSERID"
     *             length="32"
     *         
     */
    @Column(name="DEFINITION_CREATEUSERID",nullable=true)
	public String getDefinitionCreateUserId() {
		return definitionCreateUserId;
	}

	public void setDefinitionCreateUserId(String definitionCreateUserId) {
		this.definitionCreateUserId = definitionCreateUserId;
	}

	@Column(name="DEFINITION_ORGCODE",nullable=true)
	public String getDefinitionOrgCode() {
		return definitionOrgCode;
	}

	public void setDefinitionOrgCode(String definitionOrgCode) {
		this.definitionOrgCode = definitionOrgCode;
	}

	@Column(name="DEFINITION_ORGID",nullable=true)
	public String getDefinitionOrgId() {
		return definitionOrgId;
	}

	public void setDefinitionOrgId(String definitionOrgId) {
		this.definitionOrgId = definitionOrgId;
	}

}
