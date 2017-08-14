package com.strongit.doc.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_GROUP_AGENCY"
 *     
*/
@Entity
@Table(name="T_OA_GROUP_AGENCY",catalog="",schema="")
public class ToaGroupAgency implements Serializable {

    /** identifier field */
    private String groupAgencyId;

    /** nullable persistent field */
    private String groupAgencyName;

    /** nullable persistent field */
    private String groupAgencyRemark;

    /** nullable persistent field */
    private String userId;

    /** persistent field */
    private Set toaGroupDets;

    /** full constructor */
    public ToaGroupAgency(String groupAgencyId, String groupAgencyName, String groupAgencyRemark, String userId, Set toaGroupDets) {
        this.groupAgencyId = groupAgencyId;
        this.groupAgencyName = groupAgencyName;
        this.groupAgencyRemark = groupAgencyRemark;
        this.userId = userId;
        this.toaGroupDets = toaGroupDets;
    }

    /** default constructor */
    public ToaGroupAgency() {
    }

    /** minimal constructor */
    public ToaGroupAgency(String groupAgencyId, Set toaGroupDets) {
        this.groupAgencyId = groupAgencyId;
        this.toaGroupDets = toaGroupDets;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="GROUP_AGENCY_ID"
     *         
     */
    @Id
    @Column(name="GROUP_AGENCY_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getGroupAgencyId() {
        return this.groupAgencyId;
    }

    public void setGroupAgencyId(String groupAgencyId) {
        this.groupAgencyId = groupAgencyId;
    }

    /** 
     *            @hibernate.property
     *             column="GROUP_AGENCY_NAME"
     *             length="256"
     *         
     */
    @Column(name="GROUP_AGENCY_NAME")
    public String getGroupAgencyName() {
        return this.groupAgencyName;
    }

    public void setGroupAgencyName(String groupAgencyName) {
        this.groupAgencyName = groupAgencyName;
    }

    /** 
     *            @hibernate.property
     *             column="GROUP_AGENCY_REMARK"
     *             length="512"
     *         
     */
    @Column(name="GROUP_AGENCY_REMARK")
    public String getGroupAgencyRemark() {
        return this.groupAgencyRemark;
    }

    public void setGroupAgencyRemark(String groupAgencyRemark) {
        this.groupAgencyRemark = groupAgencyRemark;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name="USER_ID")
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="GROUP_AGENCY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.doc.bo.ToaGroupDet"
     *         
     */
    @OneToMany(mappedBy="toaGroupAgency",fetch=FetchType.LAZY,cascade=CascadeType.ALL,targetEntity=ToaGroupDet.class)
    public Set getToaGroupDets() {
        return this.toaGroupDets;
    }

    public void setToaGroupDets(Set toaGroupDets) {
        this.toaGroupDets = toaGroupDets;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("groupAgencyId", getGroupAgencyId())
            .toString();
    }

}
