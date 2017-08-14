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
 *         table="T_OA_SMS_REPLY"
 *     
*/
@Entity
@Table(name="T_OA_SMS_REPLY")
public class ToaSmsRep implements Serializable {

    /** identifier field */
    private String smsRepId;

    /** nullable persistent field */
    private String smsRepCon;

    /** nullable persistent field */
    private Date smsRepSendTime;

    /** 发送用户ID*/
    private String smsSendUser;
    
    /** 发送用户名 */
    private String smsSenderName;
    
    /** 模块名称 */
    private String modelName; 
    
    /** 模块ID */
    private String modelCode; 
    
    /** 自增编码 */
    private String increaseCode; 

    /** full constructor */
    public ToaSmsRep(String smsRepId, String smsCon, Date smsSendTime, String smsSendUser, String smsSenderName, String modelName,String modelCode,String increaseCode) {
        this.smsRepId = smsRepId;
        this.smsRepCon = smsCon;
        this.smsRepSendTime = smsSendTime;
        this.smsSendUser = smsSendUser;
        this.smsSenderName = smsSenderName;
        this.modelCode = modelCode;
        this.modelName = modelName;
        this.increaseCode = increaseCode;
    }

    /** default constructor */
    public ToaSmsRep() {
    }

    /** minimal constructor */
    public ToaSmsRep(String smsRepId) {
        this.smsRepId = smsRepId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="uuid.string"
     *             type="java.lang.String"
     *             column="SMSREP_ID"
     *         
     */
    @Id
	@Column(name="SMSREP_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSmsRepId() {
        return this.smsRepId;
    }

    public void setSmsRepId(String smsRepId) {
        this.smsRepId = smsRepId;
    }

    /** 
     *            @hibernate.property
     *             column="SMSREP_CON"
     *             length="512"
     *         
     */
    @Column(name="SMSREP_CON",nullable=true)
    public String getSmsRepCon() {
        return this.smsRepCon;
    }

    public void setSmsRepCon(String smsRepCon) {
        this.smsRepCon = smsRepCon;
    }

    /** 
     *            @hibernate.property
     *             column="SMSREP_SEND_TIME"
     *             length="7"
     *         
     */
    @Column(name="SMSREP_SEND_TIME",nullable=true)
    public Date getSmsRepSendTime() {
        return this.smsRepSendTime;
    }

    public void setSmsRepSendTime(Date smsRepSendTime) {
        this.smsRepSendTime = smsRepSendTime;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_SEND_USER"
     *             length="32"
     *         
     */
    @Column(name="SMS_SEND_USER",nullable=true)
    public String getSmsSendUser() {
        return this.smsSendUser;
    }

    public void setSmsSendUser(String smsSendUser) {
        this.smsSendUser = smsSendUser;
    }
    
    /** 
     *            @hibernate.property
     *             column="SMS_SENDER_NAME"
     *             length="100"
     *         
     */
    @Column(name="SMS_SENDER_NAME",nullable=true)
    public String getSmsSenderName() {
		return smsSenderName;
	}

	public void setSmsSenderName(String smsSenderName) {
		this.smsSenderName = smsSenderName;
	}
	
	/** 
     *            @hibernate.property
     *             column="MODEL_NAME"
     *             length="100"
     *         
     */
    @Column(name="MODEL_NAME",nullable=true)
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	

	/** 
     *            @hibernate.property
     *             column="INCREASE_CODE"
     *             length="8"
     *         
     */
    @Column(name="INCREASE_CODE",nullable=true)
	public String getIncreaseCode() {
		return increaseCode;
	}

	public void setIncreaseCode(String increaseCode) {
		this.increaseCode = increaseCode;
	}

	/** 
     *            @hibernate.property
     *             column="MODEL_CODE"
     *             length="3"
     *         
     */
    @Column(name="MODEL_CODE",nullable=true)
	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	
	public String toString() {
		return new ToStringBuilder(this)
		.append("smsId", getSmsRepId())
		.toString();
	}

}
