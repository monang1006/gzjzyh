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
 *         table="T_OA_SMS_COMM_CONF"
 *     
*/
@Entity
@Table(name="T_OA_SMS_COMM_CONF")
public class ToaSmsCommConf implements Serializable {

    /** identifier field 通信配置编号*/
    private String smscomId;

    /** nullable persistent field 设备名称*/
    private String smscomName;

    /** nullable persistent field 端口号*/
    private String smscomPort;

    /** nullable persistent field 发送失败是否重传*/
    private String smscomResend;

    /** nullable persistent field */
    private String smscomOpen;

    /** nullable persistent field */
    private String smscomBps;
    
    private String smscomSimnum;

    /** 短信猫类型*/
    private String smscomtype;
    /** 短信类型*/
    public static final String SMS_MODEM_GMS = "0";// gms短信猫
    public static final String SMS_MODEM_CDMA = "1";//cmda短信猫
    
    
    /** full constructor */
    public ToaSmsCommConf(String smscomId, String smscomName, String smscomPort, String smscomResend, String smscomOpen, String smscomBps ,String smscomSimnum, String smscomtype) {
        this.smscomId = smscomId;
        this.smscomName = smscomName;
        this.smscomPort = smscomPort;
        this.smscomResend = smscomResend;
        this.smscomOpen = smscomOpen;
        this.smscomBps = smscomBps;
        this.smscomSimnum = smscomSimnum;
        this.smscomtype = smscomtype;
    }

    /** default constructor */
    public ToaSmsCommConf() {
    }

    /** minimal constructor */
    public ToaSmsCommConf(String smscomId) {
        this.smscomId = smscomId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SMSCOM_ID"
     *         
     */
    @Id
	@Column(name="SMSCOM_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSmscomId() {
        return this.smscomId;
    }

    public void setSmscomId(String smscomId) {
        this.smscomId = smscomId;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCOM_NAME"
     *             length="256"
     *         
     */
    @Column(name="SMSCOM_NAME",nullable=true)
    public String getSmscomName() {
        return this.smscomName;
    }

    public void setSmscomName(String smscomName) {
        this.smscomName = smscomName;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCOM_PORT"
     *             length="22"
     *         
     */
    @Column(name="SMSCOM_PORT",nullable=true)
    public String getSmscomPort() {
        return this.smscomPort;
    }

    public void setSmscomPort(String smscomPort) {
        this.smscomPort = smscomPort;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCOM_RESEND"
     *             length="22"
     *         
     */
    @Column(name="SMSCOM_RESEND",nullable=true)
    public String getSmscomResend() {
        return this.smscomResend;
    }

    public void setSmscomResend(String smscomResend) {
        this.smscomResend = smscomResend;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCOM_OPEN"
     *             length="1"
     *         
     */
    @Column(name="SMSCOM_OPEN",nullable=true)
    public String getSmscomOpen() {
        return this.smscomOpen;
    }

    public void setSmscomOpen(String smscomOpen) {
        this.smscomOpen = smscomOpen;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCOM_BPS"
     *             length="15"
     *         
     */
    @Column(name="SMSCOM_BPS",nullable=true)
    public String getSmscomBps() {
        return this.smscomBps;
    }

    public void setSmscomBps(String smscomBps) {
        this.smscomBps = smscomBps;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCOM_SIMNUM"
     *             length="15"
     *         
     */
    @Column(name="SMSCOM_SIMNUM",nullable=true)
	public String getSmscomSimnum() {
		return smscomSimnum;
	}

	public void setSmscomSimnum(String smscomSimnum) {
		this.smscomSimnum = smscomSimnum;
	}

	public String toString() {
		return new ToStringBuilder(this)
		.append("smscomId", getSmscomId())
		.toString();
	}

    /** 
     *            @hibernate.property
     *             column="SMSCOM_TYPE"
     *             length="1"
     *         
     */
    @Column(name="SMSCOM_TYPE",nullable=true)
	public String getSmscomtype() {
		return smscomtype;
	}

	public void setSmscomtype(String smscomtype) {
		this.smscomtype = smscomtype;
	}
	
}
