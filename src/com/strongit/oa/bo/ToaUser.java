package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_USER"
 *     
*/
@Entity
@Table(name="T_OA_USER")
//@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class ToaUser implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String userId;

    /** persistent field */
    private String orgId;

    /** persistent field */
    private String userSyscode;

    /** persistent field */
    private String userName;

    /** persistent field */
    private String userLoginname;

    /** persistent field */
    private String userPassword;

    /** nullable persistent field */
    private String userUsbkey;

    /** persistent field */
    private String userIsactive;

    /** nullable persistent field */
    private String userPubkey;

    /** nullable persistent field */
    private String userTel;

    /** nullable persistent field */
    private String userAddr;

    /** nullable persistent field */
    private String userEmail;

    /** nullable persistent field */
    private String userDescription;

    /** nullable persistent field */
    private String rest1;

    /** nullable persistent field */
    private String rest2;

    /** nullable persistent field */
    private String rest3;

    /** nullable persistent field */
    private String rest4;

    /** nullable persistent field */
    private String userIsdel;

    /** nullable persistent field */
    private String userSequence;

    /** nullable persistent field */
    private String userIssupmanager;

    /** nullable persistent field */
    private String userRoleType;

    /** persistent field */
    private Set toaAddresses;

    /** persistent field */
    private Set toaMailBoxs;

    /** persistent field */
    private Set toaUserArchiveFiles;

    /** persistent field */
    private Set toaPrsnfldrConfigs;

    /** persistent field */
    private Set toaPrsnfldrShares;

    /** persistent field */
    private Set toaDesktopWholes;

    /** persistent field */
    private Set toaPersonalInfos;

    /** persistent field */
    private Set toaCalendars;

    /** persistent field */
    private Set toaPrsnfldrFolders;

    /** full constructor */
    public ToaUser(String userId, String orgId, String userSyscode, String userName, String userLoginname, String userPassword, String userUsbkey, String userIsactive, String userPubkey, String userTel, String userAddr, String userEmail, String userDescription, String rest1, String rest2, String rest3, String rest4, String userIsdel, String userSequence, String userIssupmanager, String userRoleType, Set toaAddresses, Set toaMailBoxs, Set toaUserArchiveFiles, Set toaPrsnfldrConfigs, Set toaPrsnfldrShares, Set toaDesktopWholes, Set toaPersonalInfos, Set toaCalendars, Set toaPrsnfldrFolders) {
        this.userId = userId;
        this.orgId = orgId;
        this.userSyscode = userSyscode;
        this.userName = userName;
        this.userLoginname = userLoginname;
        this.userPassword = userPassword;
        this.userUsbkey = userUsbkey;
        this.userIsactive = userIsactive;
        this.userPubkey = userPubkey;
        this.userTel = userTel;
        this.userAddr = userAddr;
        this.userEmail = userEmail;
        this.userDescription = userDescription;
        this.rest1 = rest1;
        this.rest2 = rest2;
        this.rest3 = rest3;
        this.rest4 = rest4;
        this.userIsdel = userIsdel;
        this.userSequence = userSequence;
        this.userIssupmanager = userIssupmanager;
        this.userRoleType = userRoleType;
        this.toaAddresses = toaAddresses;
        this.toaMailBoxs = toaMailBoxs;
        this.toaUserArchiveFiles = toaUserArchiveFiles;
        this.toaPrsnfldrConfigs = toaPrsnfldrConfigs;
        this.toaPrsnfldrShares = toaPrsnfldrShares;
        this.toaDesktopWholes = toaDesktopWholes;
        this.toaPersonalInfos = toaPersonalInfos;
        this.toaCalendars = toaCalendars;
        this.toaPrsnfldrFolders = toaPrsnfldrFolders;
    }

    /** default constructor */
    public ToaUser() {
    }

    /** minimal constructor */
    public ToaUser(String userId, String orgId, String userSyscode, String userName, String userLoginname, String userPassword, String userIsactive, Set toaAddresses, Set toaMailBoxs, Set toaUserArchiveFiles, Set toaPrsnfldrConfigs, Set toaPrsnfldrShares, Set toaDesktopWholes, Set toaPersonalInfos, Set toaCalendars, Set toaPrsnfldrFolders) {
        this.userId = userId;
        this.orgId = orgId;
        this.userSyscode = userSyscode;
        this.userName = userName;
        this.userLoginname = userLoginname;
        this.userPassword = userPassword;
        this.userIsactive = userIsactive;
        this.toaAddresses = toaAddresses;
        this.toaMailBoxs = toaMailBoxs;
        this.toaUserArchiveFiles = toaUserArchiveFiles;
        this.toaPrsnfldrConfigs = toaPrsnfldrConfigs;
        this.toaPrsnfldrShares = toaPrsnfldrShares;
        this.toaDesktopWholes = toaDesktopWholes;
        this.toaPersonalInfos = toaPersonalInfos;
        this.toaCalendars = toaCalendars;
        this.toaPrsnfldrFolders = toaPrsnfldrFolders;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="USER_ID"
     *         
     */
    @Id
	@Column(name="USER_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *             not-null="true"
     *         
     */
    @Column(name="ORG_ID",nullable=true)
    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /** 
     *            @hibernate.property
     *             column="USER_SYSCODE"
     *             length="42"
     *             not-null="true"
     *         
     */
    @Column(name="USER_SYSCODE",nullable=true)
    public String getUserSyscode() {
        return this.userSyscode;
    }

    public void setUserSyscode(String userSyscode) {
        this.userSyscode = userSyscode;
    }

    /** 
     *            @hibernate.property
     *             column="USER_NAME"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name="USER_NAME",nullable=true)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** 
     *            @hibernate.property
     *             column="USER_LOGINNAME"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name="USER_LOGINNAME",nullable=true)
    public String getUserLoginname() {
        return this.userLoginname;
    }

    public void setUserLoginname(String userLoginname) {
        this.userLoginname = userLoginname;
    }

    /** 
     *            @hibernate.property
     *             column="USER_PASSWORD"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name="USER_PASSWORD",nullable=true)
    public String getUserPassword() {
        return this.userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /** 
     *            @hibernate.property
     *             column="USER_USBKEY"
     *             length="4000"
     *         
     */
    @Column(name="USER_USBKEY",nullable=true)
    public String getUserUsbkey() {
        return this.userUsbkey;
    }

    public void setUserUsbkey(String userUsbkey) {
        this.userUsbkey = userUsbkey;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ISACTIVE"
     *             length="1"
     *             not-null="true"
     *         
     */
    @Column(name="USER_ISACTIVE",nullable=true)
    public String getUserIsactive() {
        return this.userIsactive;
    }

    public void setUserIsactive(String userIsactive) {
        this.userIsactive = userIsactive;
    }

    /** 
     *            @hibernate.property
     *             column="USER_PUBKEY"
     *             length="4000"
     *         
     */
    @Column(name="USER_PUBKEY",nullable=true)
    public String getUserPubkey() {
        return this.userPubkey;
    }

    public void setUserPubkey(String userPubkey) {
        this.userPubkey = userPubkey;
    }

    /** 
     *            @hibernate.property
     *             column="USER_TEL"
     *             length="20"
     *         
     */
    @Column(name="USER_TEL",nullable=true)
    public String getUserTel() {
        return this.userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ADDR"
     *             length="200"
     *         
     */
    @Column(name="USER_ADDR",nullable=true)
    public String getUserAddr() {
        return this.userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    /** 
     *            @hibernate.property
     *             column="USER_EMAIL"
     *             length="100"
     *         
     */
    @Column(name="USER_EMAIL",nullable=true)
    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /** 
     *            @hibernate.property
     *             column="USER_DESCRIPTION"
     *             length="1000"
     *         
     */
    @Column(name="USER_DESCRIPTION",nullable=true)
    public String getUserDescription() {
        return this.userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    /** 
     *            @hibernate.property
     *             column="REST1"
     *             length="1000"
     *         
     */
    @Column(name="REST1",nullable=true)
    public String getRest1() {
        return this.rest1;
    }

    public void setRest1(String rest1) {
        this.rest1 = rest1;
    }

    /** 
     *            @hibernate.property
     *             column="REST2"
     *             length="1000"
     *         
     */
    @Column(name="REST2",nullable=true)
    public String getRest2() {
        return this.rest2;
    }

    public void setRest2(String rest2) {
        this.rest2 = rest2;
    }

    /** 
     *            @hibernate.property
     *             column="REST3"
     *             length="1000"
     *         
     */
    @Column(name="REST3",nullable=true)
    public String getRest3() {
        return this.rest3;
    }

    public void setRest3(String rest3) {
        this.rest3 = rest3;
    }

    /** 
     *            @hibernate.property
     *             column="REST4"
     *             length="1000"
     *         
     */
    @Column(name="REST4",nullable=true)
    public String getRest4() {
        return this.rest4;
    }

    public void setRest4(String rest4) {
        this.rest4 = rest4;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ISDEL"
     *             length="1"
     *         
     */
    @Column(name="USER_ISDEL",nullable=true)
    public String getUserIsdel() {
        return this.userIsdel;
    }

    public void setUserIsdel(String userIsdel) {
        this.userIsdel = userIsdel;
    }

    /** 
     *            @hibernate.property
     *             column="USER_SEQUENCE"
     *             length="10"
     *         
     */
    @Column(name="USER_SEQUENCE",nullable=true)
    public String getUserSequence() {
        return this.userSequence;
    }

    public void setUserSequence(String userSequence) {
        this.userSequence = userSequence;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ISSUPMANAGER"
     *             length="1"
     *         
     */
    @Column(name="USER_ISSUPMANAGER",nullable=true)
    public String getUserIssupmanager() {
        return this.userIssupmanager;
    }

    public void setUserIssupmanager(String userIssupmanager) {
        this.userIssupmanager = userIssupmanager;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ROLE_TYPE"
     *             length="1"
     *         
     */
    @Column(name="USER_ROLE_TYPE",nullable=true)
    public String getUserRoleType() {
        return this.userRoleType;
    }

    public void setUserRoleType(String userRoleType) {
        this.userRoleType = userRoleType;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAddress"
     *         
     */
    @Transient
    public Set getToaAddresses() {
        return this.toaAddresses;
    }

    public void setToaAddresses(Set toaAddresses) {
        this.toaAddresses = toaAddresses;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMailBox"
     *         
     */
    @Transient
    public Set getToaMailBoxs() {
        return this.toaMailBoxs;
    }

    public void setToaMailBoxs(Set toaMailBoxs) {
        this.toaMailBoxs = toaMailBoxs;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaUserArchiveFile"
     *         
     */
    @Transient
    public Set getToaUserArchiveFiles() {
        return this.toaUserArchiveFiles;
    }

    public void setToaUserArchiveFiles(Set toaUserArchiveFiles) {
        this.toaUserArchiveFiles = toaUserArchiveFiles;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPrsnfldrConfig"
     *         
     */
    @Transient
    public Set getToaPrsnfldrConfigs() {
        return this.toaPrsnfldrConfigs;
    }

    public void setToaPrsnfldrConfigs(Set toaPrsnfldrConfigs) {
        this.toaPrsnfldrConfigs = toaPrsnfldrConfigs;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPrsnfldrShare"
     *         
     */
    @Transient
    public Set getToaPrsnfldrShares() {
        return this.toaPrsnfldrShares;
    }

    public void setToaPrsnfldrShares(Set toaPrsnfldrShares) {
        this.toaPrsnfldrShares = toaPrsnfldrShares;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaDesktopWhole"
     *         
     */
    @Transient
    public Set getToaDesktopWholes() {
        return this.toaDesktopWholes;
    }

    public void setToaDesktopWholes(Set toaDesktopWholes) {
        this.toaDesktopWholes = toaDesktopWholes;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPersonalInfo"
     *         
     */
    @Transient
    public Set getToaPersonalInfos() {
        return this.toaPersonalInfos;
    }

    public void setToaPersonalInfos(Set toaPersonalInfos) {
        this.toaPersonalInfos = toaPersonalInfos;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USE_USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaCalendar"
     *         
     */
    @Transient
    public Set getToaCalendars() {
        return this.toaCalendars;
    }

    public void setToaCalendars(Set toaCalendars) {
        this.toaCalendars = toaCalendars;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="USER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPrsnfldrFolder"
     *         
     */
    @Transient
    public Set getToaPrsnfldrFolders() {
        return this.toaPrsnfldrFolders;
    }

    public void setToaPrsnfldrFolders(Set toaPrsnfldrFolders) {
        this.toaPrsnfldrFolders = toaPrsnfldrFolders;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userId", getUserId())
            .toString();
    }

}
