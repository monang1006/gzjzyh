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
 *         table="T_OA_SMS"
 *     
*/
@Entity
@Table(name="T_OA_SMS")
public class ToaSms implements Serializable {

    /** identifier field */
    private String smsId;

    /** nullable persistent field */
    private String smsRecver;

    /** nullable persistent field */
    private String smsRecvnum;

    /** nullable persistent field */
    private String smsCon;

    /** nullable persistent field */
    private Date smsSendTime;

    /** 短信发送状态 */
    private String smsState;
    
    /** 短信发送状态*/
    public static final String SMS_UNSEND = "0";//未发送
    public static final String SMS_SEND = "1";//已发送
    
    /** nullable persistent field */
    private String smsServerRet;
    
    /** 服务器返回状态*/
    public static final String SERVICE_RET_UNSEND = "0";	//未发送
    public static final String SERVICE_RET_SUCCESS = "1";	//发送成功
    public static final String SERVICE_RET_FAILURE = "2";	//发送失败
    
    /** 短信逻辑删除 */
    private String smsIsdel;
    
    /** 删除状态*/
    public static final String SMS_NOTDEL = "0";	//未删除
    public static final String SMS_DEL = "1";	//已删除

    /** nullable persistent field */
    private Date smsSendDelay;
    
    /** 发送用户ID*/
    private String smsSendUser;
    
    /** 发送用户名 */
    private String smsSenderName;
    
    /** 接收用户ID*/
    private String smsRecvId;
    
    /** 模块名称 */
    private String modelName; 
    
    /** 模块ID */
    private String modelCode; 
    
    /** 自增编码 */
    private String increaseCode; 

    /** full constructor */
    public ToaSms(String smsId, String smsRecver, String smsRecvnum, String smsCon, Date smsSendTime, String smsState, String smsServerRet, Date smsSendDelay, String smsSendUser, String smsSenderName,String smsRecvId, String smsIsdel, String modelName,String modelCode,String increaseCode) {
        this.smsId = smsId;
        this.smsRecver = smsRecver;
        this.smsRecvnum = smsRecvnum;
        this.smsCon = smsCon;
        this.smsSendTime = smsSendTime;
        this.smsState = smsState;
        this.smsServerRet = smsServerRet;
        this.smsSendDelay = smsSendDelay;
        this.smsSendUser = smsSendUser;
        this.smsSenderName = smsSenderName;
        this.smsRecvId = smsRecvId;
        this.smsIsdel = smsIsdel;
        this.modelCode = modelCode;
        this.modelName = modelName;
        this.increaseCode = increaseCode;
    }

    /** default constructor */
    public ToaSms() {
    }

    /** minimal constructor */
    public ToaSms(String smsId) {
        this.smsId = smsId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SMS_ID"
     *         
     */
    @Id
	@Column(name="SMS_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSmsId() {
        return this.smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_RECVER"
     *             length="25"
     *         
     */
    @Column(name="SMS_RECVER",nullable=true)
    public String getSmsRecver() {
        return this.smsRecver;
    }

    public void setSmsRecver(String smsRecver) {
        this.smsRecver = smsRecver;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_RECVNUM"
     *             length="25"
     *         
     */
    @Column(name="SMS_RECVNUM",nullable=true)
    public String getSmsRecvnum() {
        return this.smsRecvnum;
    }

    public void setSmsRecvnum(String smsRecvnum) {
        this.smsRecvnum = smsRecvnum;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_CON"
     *             length="512"
     *         
     */
    @Column(name="SMS_CON",nullable=true)
    public String getSmsCon() {
        return this.smsCon;
    }

    public void setSmsCon(String smsCon) {
        this.smsCon = smsCon;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_SEND_TIME"
     *             length="7"
     *         
     */
    @Column(name="SMS_SEND_TIME",nullable=true)
    public Date getSmsSendTime() {
        return this.smsSendTime;
    }

    public void setSmsSendTime(Date smsSendTime) {
        this.smsSendTime = smsSendTime;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_STATE"
     *             length="1"
     *         
     */
    @Column(name="SMS_STATE",nullable=true)
    public String getSmsState() {
        return this.smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_SERVER_RET"
     *             length="512"
     *         
     */
    @Column(name="SMS_SERVER_RET",nullable=true)
    public String getSmsServerRet() {
        return this.smsServerRet;
    }

    public void setSmsServerRet(String smsServerRet) {
        this.smsServerRet = smsServerRet;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_SEND_DELAY"
     *             length="7"
     *         
     */
    @Column(name="SMS_SEND_DELAY",nullable=true)
    public Date getSmsSendDelay() {
        return this.smsSendDelay;
    }

    public void setSmsSendDelay(Date smsSendDelay) {
        this.smsSendDelay = smsSendDelay;
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
     *             column="SMS_RECVID"
     *             length="32"
     *         
     */
    @Column(name="SMS_RECVID",nullable=true)
	public String getSmsRecvId() {
		return smsRecvId;
	}

	public void setSmsRecvId(String smsRecvId) {
		this.smsRecvId = smsRecvId;
	}
	
    /** 
     *            @hibernate.property
     *             column="SMS_ISDEL"
     *             length="1"
     *         
     */
    @Column(name="SMS_ISDEL",nullable=true)
    public String getSmsIsdel() {
		return smsIsdel;
	}

	public void setSmsIsdel(String smsIsdel) {
		this.smsIsdel = smsIsdel;
	}

	/** 
     *            @hibernate.property
     *             column="MODEL_CODE_NAME"
     *             length="100"
     *         
     */
    @Column(name="MODEL_CODE_NAME",nullable=true)
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
		.append("smsId", getSmsId())
		.toString();
	}

}
