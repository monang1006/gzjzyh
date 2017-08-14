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
 *         table="T_OA_PERSONAL_CONFIG"
 *     
*/
@Entity
@Table(name="T_OA_PERSONAL_CONFIG")
public class ToaPersonalConfig implements Serializable {

    /** identifier field */
    private String prsnConfId;

    /** nullable persistent field */
    private String defaultMail;

    /** nullable persistent field */
    private String defaultMobile;

    /** nullable persistent field */
    private String prsnAvatar;

    /** nullable persistent field */
    private String prsnAvatarConf;

    /** nullable persistent field */
    private String loginMode;

    /** nullable persistent field */
    private String msgPopupMode;

    /** nullable persistent field */
    private String pageTheme;

    /** nullable persistent field */
    private String isLoginIm;

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

    /** persistent field */
    private com.strongit.oa.bo.ToaPersonalInfo toaPersonalInfo;

    /** full constructor */
    public ToaPersonalConfig(String prsnConfId, String defaultMail, String defaultMobile, String prsnAvatar, String prsnAvatarConf, String loginMode, String msgPopupMode, String pageTheme, String isLoginIm, String defaultMailSys, String defaultMailUser, String defaultMailPsw, String defaultMailPort, String defaultMailSsl, com.strongit.oa.bo.ToaPersonalInfo toaPersonalInfo) {
        this.prsnConfId = prsnConfId;
        this.defaultMail = defaultMail;
        this.defaultMobile = defaultMobile;
        this.prsnAvatar = prsnAvatar;
        this.prsnAvatarConf = prsnAvatarConf;
        this.loginMode = loginMode;
        this.msgPopupMode = msgPopupMode;
        this.pageTheme = pageTheme;
        this.isLoginIm = isLoginIm;
        this.defaultMailSys = defaultMailSys;
        this.defaultMailUser = defaultMailUser;
        this.defaultMailPsw = defaultMailPsw;
        this.defaultMailPort = defaultMailPort;
        this.defaultMailSsl = defaultMailSsl;
        this.toaPersonalInfo = toaPersonalInfo;
    }

    /** default constructor */
    public ToaPersonalConfig() {
    }

    /** minimal constructor */
    public ToaPersonalConfig(String prsnConfId, com.strongit.oa.bo.ToaPersonalInfo toaPersonalInfo) {
        this.prsnConfId = prsnConfId;
        this.toaPersonalInfo = toaPersonalInfo;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="PRSN_CONF_ID"
     *         
     */
    @Id
	@Column(name="PRSN_CONF_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getPrsnConfId() {
        return this.prsnConfId;
    }

    public void setPrsnConfId(String prsnConfId) {
        this.prsnConfId = prsnConfId;
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
     *             column="DEFAULT_MOBILE"
     *             length="20"
     *         
     */
    @Column(name="DEFAULT_MOBILE",nullable=true)
    public String getDefaultMobile() {
        return this.defaultMobile;
    }

    public void setDefaultMobile(String defaultMobile) {
        this.defaultMobile = defaultMobile;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_AVATAR"
     *             length="128"
     *         
     */
    @Column(name="PRSN_AVATAR",nullable=true)
    public String getPrsnAvatar() {
        return this.prsnAvatar;
    }

    public void setPrsnAvatar(String prsnAvatar) {
        this.prsnAvatar = prsnAvatar;
    }

    /** 
     *            @hibernate.property
     *             column="PRSN_AVATAR_CONF"
     *             length="2048"
     *         
     */
    @Column(name="PRSN_AVATAR_CONF",nullable=true)
    public String getPrsnAvatarConf() {
        return this.prsnAvatarConf;
    }

    public void setPrsnAvatarConf(String prsnAvatarConf) {
        this.prsnAvatarConf = prsnAvatarConf;
    }

    /** 
     *            @hibernate.property
     *             column="LOGIN_MODE"
     *             length="1"
     *         
     */
    @Column(name="LOGIN_MODE",nullable=true)
    public String getLoginMode() {
        return this.loginMode;
    }

    public void setLoginMode(String loginMode) {
        this.loginMode = loginMode;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_POPUP_MODE"
     *             length="1"
     *         
     */
    @Column(name="MSG_POPUP_MODE",nullable=true)
    public String getMsgPopupMode() {
        return this.msgPopupMode;
    }

    public void setMsgPopupMode(String msgPopupMode) {
        this.msgPopupMode = msgPopupMode;
    }

    /** 
     *            @hibernate.property
     *             column="PAGE_THEME"
     *             length="1"
     *         
     */
    @Column(name="PAGE_THEME",nullable=true)
    public String getPageTheme() {
        return this.pageTheme;
    }

    public void setPageTheme(String pageTheme) {
        this.pageTheme = pageTheme;
    }

    /** 
     *            @hibernate.property
     *             column="IS_LOGIN_IM"
     *             length="1"
     *         
     */
    @Column(name="IS_LOGIN_IM",nullable=true)
    public String getIsLoginIm() {
        return this.isLoginIm;
    }

    public void setIsLoginIm(String isLoginIm) {
        this.isLoginIm = isLoginIm;
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
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="PRSN_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="PRSN_ID", nullable=true)
    public com.strongit.oa.bo.ToaPersonalInfo getToaPersonalInfo() {
        return this.toaPersonalInfo;
    }

    public void setToaPersonalInfo(com.strongit.oa.bo.ToaPersonalInfo toaPersonalInfo) {
        this.toaPersonalInfo = toaPersonalInfo;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("prsnConfId", getPrsnConfId())
            .toString();
    }

}
