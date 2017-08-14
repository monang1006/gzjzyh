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
 *         table="T_OA_DOCTEMPLATE_GROUP"
 *     
*/
@Entity
@Table(name="T_OA_DOCTEMPLATE_GROUP")
public class ToaDoctemplateGroup implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String docgroupId;

    /** nullable persistent field */
    private String docgroupName;

    /** nullable persistent field */
    private String docgroupRemarl;

    /** nullable persistent field */
    private String docgroupParentId;
    
    private String docgroupType;

    /** persistent field */
    private Set toaDoctemplates;
    
    /** 组织机构 添加机构CODE和ID字段 */
    private String docgroupOrgCode;
    
    private String docgroupOrgId;

    /** full constructor */
    public ToaDoctemplateGroup(String docgroupId, String docgroupName, String docgroupRemarl, String docgroupParentId, Set toaDoctemplates,String docgroupType) {
        this.docgroupId = docgroupId;
        this.docgroupName = docgroupName;
        this.docgroupRemarl = docgroupRemarl;
        this.docgroupParentId = docgroupParentId;
        this.toaDoctemplates = toaDoctemplates;
        this.docgroupType=docgroupType;
    }

    /** default constructor */
    public ToaDoctemplateGroup() {
    }

    /** minimal constructor */
    public ToaDoctemplateGroup(String docgroupId, Set toaDoctemplates) {
        this.docgroupId = docgroupId;
        this.toaDoctemplates = toaDoctemplates;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOCGROUP_ID"
     *         
     */
    @Id
	@Column(name="DOCGROUP_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDocgroupId() {
        return this.docgroupId;
    }

    public void setDocgroupId(String docgroupId) {
        this.docgroupId = docgroupId;
    }

    /** 
     *            @hibernate.property
     *             column="DOCGROUP_NAME"
     *             length="50"
     *         
     */
    @Column(name="DOCGROUP_NAME",nullable=true)
    public String getDocgroupName() {
        return this.docgroupName;
    }

    public void setDocgroupName(String docgroupName) {
        this.docgroupName = docgroupName;
    }

    /** 
     *            @hibernate.property
     *             column="DOCGROUP_REMARL"
     *             length="4000"
     *         
     */
    @Column(name="DOCGROUP_REMARL",nullable=true)
    public String getDocgroupRemarl() {
        return this.docgroupRemarl;
    }

    public void setDocgroupRemarl(String docgroupRemarl) {
        this.docgroupRemarl = docgroupRemarl;
    }

    /** 
     *            @hibernate.property
     *             column="DOCGROUP_PARENT_ID"
     *             length="32"
     *         
     */
    @Column(name="DOCGROUP_PARENT_ID",nullable=true)
    public String getDocgroupParentId() {
        return this.docgroupParentId;
    }

    public void setDocgroupParentId(String docgroupParentId) {
        this.docgroupParentId = docgroupParentId;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DOCGROUP_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaDoctemplate"
     *         
     */
    @OneToMany(mappedBy="toaDoctemplateGroup",targetEntity=com.strongit.oa.bo.ToaDoctemplate.class,cascade=CascadeType.ALL)
    public Set getToaDoctemplates() {
        return this.toaDoctemplates;
    }

    public void setToaDoctemplates(Set toaDoctemplates) {
        this.toaDoctemplates = toaDoctemplates;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docgroupId", getDocgroupId())
            .toString();
    }

    @Column(name="DOCGROUP_TYPE",nullable=true)
	public String getDocgroupType() {
		return docgroupType;
	}

	public void setDocgroupType(String docgroupType) {
		this.docgroupType = docgroupType;
	}

	@Column(name="DOCGROUP_ORGCODE",nullable=true)
	public String getDocgroupOrgCode() {
		return docgroupOrgCode;
	}

	public void setDocgroupOrgCode(String docgroupOrgCode) {
		this.docgroupOrgCode = docgroupOrgCode;
	}

	@Column(name="DOCGROUP_ORGID",nullable=true)
	public String getDocgroupOrgId() {
		return docgroupOrgId;
	}

	public void setDocgroupOrgId(String docgroupOrgId) {
		this.docgroupOrgId = docgroupOrgId;
	}

}
