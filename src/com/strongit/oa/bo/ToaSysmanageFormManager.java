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
 *         table="T_OA_SYSMANAGE_FORM_MANAGER"
 *     
*/
@Entity
@Table(name="T_OA_SYSMANAGE_FORM_MANAGER")
public class ToaSysmanageFormManager implements Serializable {

    /** identifier field */
    private String formManagerCode;

    /** nullable persistent field */
    private String formId;

    /** nullable persistent field */
    private String formBusinessId;

    /** full constructor */
    public ToaSysmanageFormManager(String formManagerCode, String formId, String formBusinessId) {
        this.formManagerCode = formManagerCode;
        this.formId = formId;
        this.formBusinessId = formBusinessId;
    }

    /** default constructor */
    public ToaSysmanageFormManager() {
    }

    /** minimal constructor */
    public ToaSysmanageFormManager(String formManagerCode) {
        this.formManagerCode = formManagerCode;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FORM_MANAGER_CODE"
     *         
     */
    @Id
	@Column(name="FORM_MANAGER_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getFormManagerCode() {
        return this.formManagerCode;
    }

    public void setFormManagerCode(String formManagerCode) {
        this.formManagerCode = formManagerCode;
    }

    /** 
     *            @hibernate.property
     *             column="FORM_ID"
     *             length="32"
     *         
     */
    @Column(name="FORM_ID",nullable=true)
    public String getFormId() {
        return this.formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    /** 
     *            @hibernate.property
     *             column="FORM_BUSINESS_ID"
     *             length="32"
     *         
     */
    @Column(name="FORM_BUSINESS_ID",nullable=true)
    public String getFormBusinessId() {
        return this.formBusinessId;
    }

    public void setFormBusinessId(String formBusinessId) {
        this.formBusinessId = formBusinessId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("formManagerCode", getFormManagerCode())
            .toString();
    }

}
