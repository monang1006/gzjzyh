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
 *         table="T_OA_REDSTEMPLATE_GROUP"
 *     
*/
@Entity
@Table(name="T_OA_REDSTEMPLATE_GROUP")
public class ToaDocredGroup implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String redtempGroupId;

    /** nullable persistent field */
    private String redtempGroupName;

    /** nullable persistent field */
    private String redtempGroupRemark;

    /** nullable persistent field */
    private String redtempGroupParentId;

    /** persistent field */
    private Set toaDocred;

    /** full constructor */
    public ToaDocredGroup(String redtempGroupId, String redtempGroupName, String redtempGroupRemark, String redtempGroupParentId, Set toaDocred) {
        this.redtempGroupId = redtempGroupId;
        this.redtempGroupName = redtempGroupName;
        this.redtempGroupRemark = redtempGroupRemark;
        this.redtempGroupParentId = redtempGroupParentId;
        this.toaDocred = toaDocred;
    }

    /** default constructor */
    public ToaDocredGroup() {
    }

    /** minimal constructor */
    public ToaDocredGroup(String redtempGroupId, Set toaDocred) {
        this.redtempGroupId = redtempGroupId;
        this.toaDocred = toaDocred;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="REDTEMP_GROUP_ID"
     *         
     */
    @Id
	@Column(name="REDTEMP_GROUP_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRedtempGroupId() {
        return this.redtempGroupId;
    }

    public void setRedtempGroupId(String redtempGroupId) {
        this.redtempGroupId = redtempGroupId;
    }

    /** 
     *            @hibernate.property
     *             column="REDTEMP_GROUP_NAME"
     *             length="50"
     *         
     */
    @Column(name="REDTEMP_GROUP_NAME",nullable=true)
    public String getRedtempGroupName() {
        return this.redtempGroupName;
    }

    public void setRedtempGroupName(String redtempGroupName) {
        this.redtempGroupName = redtempGroupName;
    }

    /** 
     *            @hibernate.property
     *             column="REDTEMP_GROUP_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="REDTEMP_GROUP_REMARK",nullable=true)
    public String getRedtempGroupRemark() {
        return this.redtempGroupRemark;
    }

    public void setRedtempGroupRemark(String redtempGroupRemark) {
        this.redtempGroupRemark = redtempGroupRemark;
    }

    /** 
     *            @hibernate.property
     *             column="REDTEMP_GROUP_PARENT_ID"
     *             length="32"
     *         
     */
    @Column(name="REDTEMP_GROUP_PARENT_ID",nullable=true)
    public String getRedtempGroupParentId() {
        return this.redtempGroupParentId;
    }

    public void setRedtempGroupParentId(String redtempGroupParentId) {
        this.redtempGroupParentId = redtempGroupParentId;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="REDTEMP_GROUP_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaDocred"
     *         
     */
    @OneToMany(mappedBy="toaDocredGroup",targetEntity=com.strongit.oa.bo.ToaDocred.class,cascade=CascadeType.ALL)
    public Set getToaDocred() {
        return this.toaDocred;
    }

    public void setToaDocred(Set toaDocred) {
        this.toaDocred = toaDocred;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("redtempGroupId", getRedtempGroupId())
            .toString();
    }

}
