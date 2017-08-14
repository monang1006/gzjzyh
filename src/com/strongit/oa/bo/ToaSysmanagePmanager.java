package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_PMANAGER"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_PMANAGER")
public class ToaSysmanagePmanager implements Serializable {

    /** identifier field */
    private String infoManagerCode;

    /** nullable persistent field */
    private String infoItemId;

    /** nullable persistent field */
    private String infoBusinessId;

    /** full constructor */
    public ToaSysmanagePmanager(String infoManagerCode, String infoItemId, String infoBusinessId) {
        this.infoManagerCode = infoManagerCode;
        this.infoItemId = infoItemId;
        this.infoBusinessId = infoBusinessId;
    }

    /** default constructor */
    public ToaSysmanagePmanager() {
    }

    /** minimal constructor */
    public ToaSysmanagePmanager(String infoManagerCode) {
        this.infoManagerCode = infoManagerCode;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="INFO_MANAGER_CODE"
     *         
     */
    @Id
	@Column(name="INFO_MANAGER_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getInfoManagerCode() {
        return this.infoManagerCode;
    }

    public void setInfoManagerCode(String infoManagerCode) {
        this.infoManagerCode = infoManagerCode;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_ITEM_ID"
     *             length="32"
     *         
     */
    @Column(name="INFO_ITEM_ID",nullable=true)
    public String getInfoItemId() {
        return this.infoItemId;
    }

    public void setInfoItemId(String infoItemId) {
        this.infoItemId = infoItemId;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_BUSINESS_ID"
     *             length="32"
     *         
     */
    @Column(name="INFO_BUSINESS_ID",nullable=true)
    public String getInfoBusinessId() {
        return this.infoBusinessId;
    }

    public void setInfoBusinessId(String infoBusinessId) {
        this.infoBusinessId = infoBusinessId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("infoManagerCode", getInfoManagerCode())
            .toString();
    }

}
