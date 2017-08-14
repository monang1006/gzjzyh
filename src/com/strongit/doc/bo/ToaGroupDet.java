package com.strongit.doc.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_GROUP_DETS"
 *     
*/
@Entity
@Table(name="T_OA_GROUP_DETS",catalog="",schema="")
public class ToaGroupDet implements Serializable {

    /** identifier field */
    private String groupDetsId;

    /** persistent field */
    private String orgId; 

    /** persistent field */
    private com.strongit.doc.bo.ToaGroupAgency toaGroupAgency;

    /** full constructor */
    public ToaGroupDet(String groupDetsId, String orgId, com.strongit.doc.bo.ToaGroupAgency toaGroupAgency) {
        this.groupDetsId = groupDetsId;
        this.orgId = orgId; 
        this.toaGroupAgency = toaGroupAgency;
    }

    /** default constructor */
    public ToaGroupDet() {
    } 

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="GROUP_DETS_ID"
     *         
     */
    @Id
    @Column(name="GROUP_DETS_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getGroupDetsId() {
        return this.groupDetsId;
    }

    public void setGroupDetsId(String groupDetsId) {
        this.groupDetsId = groupDetsId;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *             not-null="true"
     *         
     */
    @Column(name="ORG_ID")
    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    } 

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="GROUP_AGENCY_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="GROUP_AGENCY_ID")
    public com.strongit.doc.bo.ToaGroupAgency getToaGroupAgency() {
        return this.toaGroupAgency;
    }

    public void setToaGroupAgency(com.strongit.doc.bo.ToaGroupAgency toaGroupAgency) {
        this.toaGroupAgency = toaGroupAgency;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("groupDetsId", getGroupDetsId())
            .toString();
    }

}
