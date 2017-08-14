package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSTEMFORM_MODEL"
 *     
*/
@Entity
@Table(name="T_OA_SYSTEMFORM_MODEL")
public class ToaSystemformModel implements Serializable {

    /** identifier field */
    private String modelId;

    /** nullable persistent field */
    private String modelName;

    /** nullable persistent field */
    private String modelUrl;

    /** nullable persistent field */
    private String modelDepartment;

    /** nullable persistent field */
    private byte[] modelContent;

    /** nullable persistent field */
    private Date modelDate;
    
    /** nullable persistent field 流程类型 */
    private String modelProcesstype;

    /** full constructor */
    public ToaSystemformModel(String modelId, String modelName, String modelUrl, String modelDepartment, byte[] modelContent, Date modelDate,String modelProcesstype) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelUrl = modelUrl;
        this.modelDepartment = modelDepartment;
        this.modelContent = modelContent;
        this.modelDate = modelDate;
        this.modelProcesstype = modelProcesstype;
    }
    
  

    /** default constructor */
    public ToaSystemformModel() {
    }

    /** minimal constructor */
    public ToaSystemformModel(String modelId) {
        this.modelId = modelId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MODEL_ID"
     *         
     */
    @Id
	@Column(name="MODEL_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getModelId() {
        return this.modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    /** 
     *            @hibernate.property
     *             column="MODEL_NAME"
     *             length="100"
     *         
     */
    @Column(name="MODEL_NAME",nullable=true)
    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /** 
     *            @hibernate.property
     *             column="MODEL_URL"
     *             length="200"
     *         
     */
    @Column(name="MODEL_URL",nullable=true)
    public String getModelUrl() {
        return this.modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    /** 
     *            @hibernate.property
     *             column="MODEL_DEPARTMENT"
     *             length="32"
     *         
     */
    @Column(name="MODEL_DEPARTMENT",nullable=true)
    public String getModelDepartment() {
        return this.modelDepartment;
    }

    public void setModelDepartment(String modelDepartment) {
        this.modelDepartment = modelDepartment;
    }

    /** 
     *            @hibernate.property
     *             column="MODEL_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name="MODEL_CONTENT",nullable=true)
    public byte[] getModelContent() {
        return this.modelContent;
    }

    public void setModelContent(byte[] modelContent) {
        this.modelContent = modelContent;
    }


    /** 
     *            @hibernate.property
     *             column="MODEL_DATE"
     *             length="7"
     *         
     */
    @Column(name="MODEL_DATE",nullable=true)
    public Date getModelDate() {
        return this.modelDate;
    }

    public void setModelDate(Date modelDate) {
        this.modelDate = modelDate;
    }


    /** 
     *            @hibernate.property
     *             column="MODEL_PROCESSTYPE"
     *             length="32"
     *         
     */
    @Column(name="MODEL_PROCESSTYPE",nullable=true)
    public String getModelProcesstype() {
        return this.modelProcesstype;
    }

    public void setModelProcesstype(String modelProcesstype) {
        this.modelProcesstype = modelProcesstype;
    }

    
    public String toString() {
        return new ToStringBuilder(this)
            .append("modelId", getModelId())
            .toString();
    }

}
