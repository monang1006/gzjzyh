package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_PROPERTYPE"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_PROPERTYPE")
public class ToaSysmanagePropertype implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String propertypeId;

    /** nullable persistent field */
    private String propertypeName;

    /** persistent field */
    private com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure;

    /** full constructor */
    public ToaSysmanagePropertype(String propertypeId, String propertypeName, com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure) {
        this.propertypeId = propertypeId;
        this.propertypeName = propertypeName;
        this.toaSysmanageStructure = toaSysmanageStructure;
    }

    /** default constructor */
    public ToaSysmanagePropertype() {
    }

    /** minimal constructor */
    public ToaSysmanagePropertype(String propertypeId, com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure) {
        this.propertypeId = propertypeId;
        this.toaSysmanageStructure = toaSysmanageStructure;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PROPERTYPE_ID"
     *         
     */
    @Id
	@Column(name="PROPERTYPE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPropertypeId() {
        return this.propertypeId;
    }

    public void setPropertypeId(String propertypeId) {
        this.propertypeId = propertypeId;
    }

    /** 
     *            @hibernate.property
     *             column="PROPERTYPE_NAME"
     *             length="50"
     *         
     */
    @Column(name="PROPERTYPE_NAME",nullable=true)
    public String getPropertypeName() {
        return this.propertypeName;
    }

    public void setPropertypeName(String propertypeName) {
        this.propertypeName = propertypeName;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="INFO_SET_CODE"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="INFO_SET_CODE", nullable=false)
    public com.strongit.oa.bo.ToaSysmanageStructure getToaSysmanageStructure() {
        return this.toaSysmanageStructure;
    }

    public void setToaSysmanageStructure(com.strongit.oa.bo.ToaSysmanageStructure toaSysmanageStructure) {
        this.toaSysmanageStructure = toaSysmanageStructure;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("propertypeId", getPropertypeId())
            .toString();
    }

}
