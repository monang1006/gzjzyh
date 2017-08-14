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
 *         table="T_OA_SMSCONTROL"
 *     
*/
@Entity
@Table(name="T_OA_SMSCONTROL")
public class ToaSmscontrol implements Serializable {

    /** 权限ID */
    private String smscontrolId;

    /** 用户ID */
    private String smscontrolUserid;
    
    /** 用户ID */
    private String smscontrolUserName;
    
    /** 用户所属部门 */
    private String smscontrolDepartment;
    
    /** 用户权限状态*/
    private String smsSendRight;
 
    public static final String SMS_NORIGHT= "0";	//禁用
    public static final String SMS_RIGHT = "1";		//开启
    
    /** full constructor */
    public ToaSmscontrol(String smscontrolId, String smscontrolUserid, String smsSendRight,String smscontrolUserName) {
        this.smscontrolId = smscontrolId;
        this.smscontrolUserid = smscontrolUserid;
        this.smsSendRight = smsSendRight;
        this.smscontrolUserName = smscontrolUserName;
    }

    /** default constructor */
    public ToaSmscontrol() {
    }

    /** minimal constructor */
    public ToaSmscontrol(String smscontrolId) {
        this.smscontrolId = smscontrolId;
    }

    /** 
     *            @hibernate.id
     *            generator-class="assigned"
     *             type="java.lang.String"
     *             column="SMSCONTROL_ID"
     *         
     */
    @Id
	@Column(name="SMSCONTROL_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSmscontrolId() {
        return this.smscontrolId;
    }

    public void setSmscontrolId(String smscontrolId) {
        this.smscontrolId = smscontrolId;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCONTROL_USERID"
     *             length="32"
     *         
     */
    @Column(name="SMSCONTROL_USERID",nullable=true)
    public String getSmscontrolUserid() {
        return this.smscontrolUserid;
    }

    public void setSmscontrolUserid(String smscontrolUserid) {
        this.smscontrolUserid = smscontrolUserid;
    }

    /** 
     *            @hibernate.property
     *             column="SMS_SEND_RIGHT"
     *             length="1"
     *         
     */
    @Column(name="SMS_SEND_RIGHT",nullable=true)
    public String getSmsSendRight() {
        return this.smsSendRight;
    }

    public void setSmsSendRight(String smsSendRight) {
        this.smsSendRight = smsSendRight;
    }

    /** 
     *            @hibernate.property
     *             column="SMSCONTROL_USERNAME"
     *             length="100"
     *         
     */
    @Column(name="SMSCONTROL_USERNAME",nullable=true)
	public String getSmscontrolUserName() {
		return smscontrolUserName;
	}

	public void setSmscontrolUserName(String smscontrolUserName) {
		this.smscontrolUserName = smscontrolUserName;
	}

	/** 
     *            @hibernate.property
     *             column="SMSCONTROL_DEPARTMENT"
     *             length="100"
     *         
     */
    @Column(name="SMSCONTROL_DEPARTMENT",nullable=true)
	public String getSmscontrolDepartment() {
		return smscontrolDepartment;
	}

	public void setSmscontrolDepartment(String smscontrolDepartment) {
		this.smscontrolDepartment = smscontrolDepartment;
	}
	
	public String toString() {
		return new ToStringBuilder(this)
		.append("smscontrolId", getSmscontrolId())
		.toString();
	}

	
}
