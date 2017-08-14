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
 *         table="T_OA_MODULE_STATE_MEAN"
 *     
*/
@Entity
@Table(name="T_OA_MODULE_STATE_MEAN",catalog="",schema="")
public class ToaModuleStateMean implements Serializable {

    /** identifier field */
    private String moduleStateMeanId;

    /** nullable persistent field */
    private String moduleStateFlag;

    /** nullable persistent field */
    private String moduleStateMean;

    /** persistent field */
    private com.strongit.oa.bo.ToaBussinessModulePara toaBussinessModulePara;

    /** full constructor */
    public ToaModuleStateMean(String moduleStateMeanId, String moduleStateFlag, String moduleStateMean, com.strongit.oa.bo.ToaBussinessModulePara toaBussinessModulePara) {
        this.moduleStateMeanId = moduleStateMeanId;
        this.moduleStateFlag = moduleStateFlag;
        this.moduleStateMean = moduleStateMean;
        this.toaBussinessModulePara = toaBussinessModulePara;
    }

    /** default constructor */
    public ToaModuleStateMean() {
    }

    /** minimal constructor */
    public ToaModuleStateMean(String moduleStateMeanId, com.strongit.oa.bo.ToaBussinessModulePara toaBussinessModulePara) {
        this.moduleStateMeanId = moduleStateMeanId;
        this.toaBussinessModulePara = toaBussinessModulePara;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MODULE_STATE_MEAN_ID"
     *         
     */
	@Id
	@Column(name="MODULE_STATE_MEAN_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getModuleStateMeanId() {
        return this.moduleStateMeanId;
    }

    public void setModuleStateMeanId(String moduleStateMeanId) {
        this.moduleStateMeanId = moduleStateMeanId;
    }

    /** 
     *            @hibernate.property
     *             column="MODULE_STATE_FLAG"
     *             length="1"
     *         
     */
    @Column(name="MODULE_STATE_FLAG",nullable=true)
    public String getModuleStateFlag() {
        return this.moduleStateFlag;
    }

    public void setModuleStateFlag(String moduleStateFlag) {
        this.moduleStateFlag = moduleStateFlag;
    }

    /** 
     *            @hibernate.property
     *             column="MODULE_STATE_MEAN"
     *             length="50"
     *         
     */
    @Column(name="MODULE_STATE_MEAN",nullable=true)
    public String getModuleStateMean() {
        return this.moduleStateMean;
    }

    public void setModuleStateMean(String moduleStateMean) {
        this.moduleStateMean = moduleStateMean;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="BUSSINESS_MODULE_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="BUSSINESS_MODULE_ID", nullable=false)
    public com.strongit.oa.bo.ToaBussinessModulePara getToaBussinessModulePara() {
        return this.toaBussinessModulePara;
    }

    public void setToaBussinessModulePara(com.strongit.oa.bo.ToaBussinessModulePara toaBussinessModulePara) {
        this.toaBussinessModulePara = toaBussinessModulePara;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("moduleStateMeanId", getModuleStateMeanId())
            .toString();
    }

}
