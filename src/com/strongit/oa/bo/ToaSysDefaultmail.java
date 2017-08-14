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
 *         table="T_OA_SYS_DEFAULTMAIL"
 *     
*/
@Entity
@Table(name="T_OA_SYS_DEFAULTMAIL")
public class ToaSysDefaultmail implements Serializable {

    /** identifier field */
    private String defaultMailId;

    /** nullable persistent field */
    private String defaultMail;

    /** nullable persistent field */
    private String defaultMailSys;

    /** nullable persistent field */
    private String defaultMailUser;

    /** nullable persistent field */
    private String defaultMailPsw;

    /** nullable persistent field */
    private String defaultMailPort;

    /** nullable persistent field */
    private String defaultMailSsl;
    
    /** 邮箱模块是否启用 */
    private String defaultMailUseable;
    
    /** 邮箱模块启动*/
    public static final String USEABLE_TURE = "1";	//启动

    /** 邮箱模块关闭*/
    public static final String USEABLE_FALSE = "0";	//关闭 

    /** full constructor */
    public ToaSysDefaultmail(String defaultMailId, String defaultMail, String defaultMailSys, String defaultMailUser, String defaultMailPsw, String defaultMailPort, String defaultMailSsl, String defaultMailUseable) {
        this.defaultMailId = defaultMailId;
        this.defaultMail = defaultMail;
        this.defaultMailSys = defaultMailSys;
        this.defaultMailUser = defaultMailUser;
        this.defaultMailPsw = defaultMailPsw;
        this.defaultMailPort = defaultMailPort;
        this.defaultMailSsl = defaultMailSsl;
        this.defaultMailUseable = defaultMailUseable;
    }

    /** default constructor */
    public ToaSysDefaultmail() {
    }

    /** minimal constructor */
    public ToaSysDefaultmail(String defaultMailId) {
        this.defaultMailId = defaultMailId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DEFAULT_MAIL_ID"
     *         
     */
    @Id
	@Column(name="DEFAULT_MAIL_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDefaultMailId() {
        return this.defaultMailId;
    }

    public void setDefaultMailId(String defaultMailId) {
        this.defaultMailId = defaultMailId;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL"
     *             length="256"
     *         
     */
    @Column(name="DEFAULT_MAIL",nullable=true)
    public String getDefaultMail() {
        return this.defaultMail;
    }

    public void setDefaultMail(String defaultMail) {
        this.defaultMail = defaultMail;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL_SYS"
     *             length="128"
     *         
     */
    @Column(name="DEFAULT_MAIL_SYS",nullable=true)
    public String getDefaultMailSys() {
        return this.defaultMailSys;
    }

    public void setDefaultMailSys(String defaultMailSys) {
        this.defaultMailSys = defaultMailSys;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL_USER"
     *             length="25"
     *         
     */
    @Column(name="DEFAULT_MAIL_USER",nullable=true)
    public String getDefaultMailUser() {
        return this.defaultMailUser;
    }

    public void setDefaultMailUser(String defaultMailUser) {
        this.defaultMailUser = defaultMailUser;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL_PSW"
     *             length="128"
     *         
     */
    @Column(name="DEFAULT_MAIL_PSW",nullable=true)
    public String getDefaultMailPsw() {
        return this.defaultMailPsw;
    }

    public void setDefaultMailPsw(String defaultMailPsw) {
        this.defaultMailPsw = defaultMailPsw;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL_PORT"
     *             length="128"
     *         
     */
    @Column(name="DEFAULT_MAIL_PORT",nullable=true)
    public String getDefaultMailPort() {
        return this.defaultMailPort;
    }

    public void setDefaultMailPort(String defaultMailPort) {
        this.defaultMailPort = defaultMailPort;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL_SSL"
     *             length="1"
     *         
     */
    @Column(name="DEFAULT_MAIL_SSL",nullable=true)
    public String getDefaultMailSsl() {
        return this.defaultMailSsl;
    }

    public void setDefaultMailSsl(String defaultMailSsl) {
        this.defaultMailSsl = defaultMailSsl;
    }

    /** 
     *            @hibernate.property
     *             column="DEFAULT_MAIL_USEABLE"
     *             length="1"
     *         
     */
    @Column(name="DEFAULT_MAIL_USEABLE",nullable=true)
    public String getDefaultMailUseable() {
    	return defaultMailUseable;
    }
    
    public void setDefaultMailUseable(String defaultMailUseable) {
    	this.defaultMailUseable = defaultMailUseable;
    }

    
    public String toString() {
        return new ToStringBuilder(this)
            .append("defaultMailId", getDefaultMailId())
            .toString();
    }


}
