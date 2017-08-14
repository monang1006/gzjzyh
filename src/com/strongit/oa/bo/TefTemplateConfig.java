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
 *         table="T_EF_TEMPLATE_CONFIG"
 *     
*/
@Entity
@Table(name="T_EF_TEMPLATE_CONFIG",catalog="",schema="")
public class TefTemplateConfig implements Serializable {

    /** identifier field */
    private String configId;

    /** nullable persistent field */
    private Long formId;

    /** nullable persistent field */
    private String userId;

    /** nullable persistent field */
    private String senquenceNum;
    
    private String formType;

    /** full constructor */
    public TefTemplateConfig(String configId, Long formId, String userId, String senquenceNum) {
        this.configId = configId;
        this.formId = formId;
        this.userId = userId;
        this.senquenceNum = senquenceNum;
    }

    /** default constructor */
    public TefTemplateConfig() {
    }

    /** minimal constructor */
    public TefTemplateConfig(String configId) {
        this.configId = configId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="CONFIG_ID"
     *         
     */
    @Id
    @Column(name="CONFIG_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getConfigId() {
        return this.configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    /** 
     *            @hibernate.property
     *             column="FORM_ID"
     *             length="12"
     *         
     */
    @Column(name="FORM_ID")
    public Long getFormId() {
        return this.formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
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
     *            @hibernate.property
     *             column="SENQUENCE_NUM"
     *             length="3"
     *         
     */
    @Column(name="SENQUENCE_NUM")
    public String getSenquenceNum() {
        return this.senquenceNum;
    }

    public void setSenquenceNum(String senquenceNum) {
        this.senquenceNum = senquenceNum;
    }

    @Column(name="FORM_TYPE")
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("configId", getConfigId())
            .toString();
    }
}
