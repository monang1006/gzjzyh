package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MAIL_BOX"
 *     
*/
@Entity
@Table(name="T_OA_MAIL_BOX",catalog="",schema="")
public class ToaMailBox implements Serializable {

    /** identifier field */
    private String mailboxId;

    /** nullable persistent field */
    private String mailboxUserName;

    /** nullable persistent field */
    private String mailAddress;

    /** nullable persistent field */
    private String replyAddress;

    /** nullable persistent field */
    private String smtpServer;

    /** nullable persistent field */
    private String pop3Server;
    
    private String getServerType;

    /** nullable persistent field */
    private String pop3Account;

    /** nullable persistent field */
    private String pop3Pwd;

    /** nullable persistent field */
    private String smtpPort;

    /** nullable persistent field */
    private String smtpSsl;

    /** nullable persistent field */
    private String pop3Port;

    /** nullable persistent field */
    private String pop3Ssl;

    /** nullable persistent field */
    private String isBackup;

    /** persistent field */
    private String userId;

    /** persistent field */
    private Set toaMailFolders;

    /** full constructor */
    public ToaMailBox(String mailboxId, String mailboxUserName, String mailAddress, String replyAddress, String smtpServer, String pop3Server, String pop3Account, String pop3Pwd, String smtpPort, String smtpSsl, String pop3Port, String pop3Ssl, String isBackup, String userId, Set toaMailFolders) {
        this.mailboxId = mailboxId;
        this.mailboxUserName = mailboxUserName;
        this.mailAddress = mailAddress;
        this.replyAddress = replyAddress;
        this.smtpServer = smtpServer;
        this.pop3Server = pop3Server;
        this.pop3Account = pop3Account;
        this.pop3Pwd = pop3Pwd;
        this.smtpPort = smtpPort;
        this.smtpSsl = smtpSsl;
        this.pop3Port = pop3Port;
        this.pop3Ssl = pop3Ssl;
        this.isBackup = isBackup;
        this.userId = userId;
        this.toaMailFolders = toaMailFolders;
    }

    /** default constructor */
    public ToaMailBox() {
    }

    /** minimal constructor */
    public ToaMailBox(String mailboxId, String userId, Set toaMailFolders) {
        this.mailboxId = mailboxId;
        this.userId = userId;
        this.toaMailFolders = toaMailFolders;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MAILBOX_ID"
     *         
     */
	@Id
	@Column(name="MAILBOX_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getMailboxId() {
        return this.mailboxId;
    }

    public void setMailboxId(String mailboxId) {
        this.mailboxId = mailboxId;
    }

    /** 
     *            @hibernate.property
     *             column="MAILBOX_USER_NAME"
     *             length="25"
     *         
     */
    @Column(name="MAILBOX_USER_NAME",nullable=true)
    public String getMailboxUserName() {
        return this.mailboxUserName;
    }

    public void setMailboxUserName(String mailboxUserName) {
        this.mailboxUserName = mailboxUserName;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_ADDRESS"
     *             length="128"
     *         
     */
    @Column(name="MAIL_ADDRESS",nullable=true)
    public String getMailAddress() {
        return this.mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    /** 
     *            @hibernate.property
     *             column="REPLY_ADDRESS"
     *             length="128"
     *         
     */
    @Column(name="REPLY_ADDRESS",nullable=true)
    public String getReplyAddress() {
        return this.replyAddress;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

    /** 
     *            @hibernate.property
     *             column="SMTP_SERVER"
     *             length="128"
     *         
     */
    @Column(name="SMTP_SERVER",nullable=true)
    public String getSmtpServer() {
        return this.smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    /** 
     *            @hibernate.property
     *             column="POP3_SERVER"
     *             length="128"
     *         
     */
    @Column(name="POP3_SERVER",nullable=true)
    public String getPop3Server() {
        return this.pop3Server;
    }

    public void setPop3Server(String pop3Server) {
        this.pop3Server = pop3Server;
    }
    
    @Column(name="GET_SERVER_TYPE",nullable=true)
    public String getGetServerType(){
    	return this.getServerType;
    }
    
    public void setGetServerType(String getServerType){
    	this.getServerType=getServerType;
    }

    /** 
     *            @hibernate.property
     *             column="POP3_ACCOUNT"
     *             length="128"
     *         
     */
    @Column(name="POP3_ACCOUNT",nullable=true)
    public String getPop3Account() {
        return this.pop3Account;
    }

    public void setPop3Account(String pop3Account) {
        this.pop3Account = pop3Account;
    }

    /** 
     *            @hibernate.property
     *             column="POP3_PWD"
     *             length="128"
     *         
     */
    @Column(name="POP3_PWD",nullable=true)
    public String getPop3Pwd() {
        return this.pop3Pwd;
    }

    public void setPop3Pwd(String pop3Pwd) {
        this.pop3Pwd = pop3Pwd;
    }

    /** 
     *            @hibernate.property
     *             column="SMTP_PORT"
     *             length="128"
     *         
     */
    @Column(name="SMTP_PORT",nullable=true)
    public String getSmtpPort() {
        return this.smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    /** 
     *            @hibernate.property
     *             column="SMTP_SSL"
     *             length="1"
     *         
     */
    @Column(name="SMTP_SSL",nullable=true)
    public String getSmtpSsl() {
        return this.smtpSsl;
    }

    public void setSmtpSsl(String smtpSsl) {
        this.smtpSsl = smtpSsl;
    }

    /** 
     *            @hibernate.property
     *             column="POP3_PORT"
     *             length="128"
     *         
     */
    @Column(name="POP3_PORT",nullable=true)
    public String getPop3Port() {
        return this.pop3Port;
    }

    public void setPop3Port(String pop3Port) {
        this.pop3Port = pop3Port;
    }

    /** 
     *            @hibernate.property
     *             column="POP3_SSL"
     *             length="1"
     *         
     */
    @Column(name="POP3_SSL",nullable=true)
    public String getPop3Ssl() {
        return this.pop3Ssl;
    }

    public void setPop3Ssl(String pop3Ssl) {
        this.pop3Ssl = pop3Ssl;
    }

    /** 
     *            @hibernate.property
     *             column="IS_BACKUP"
     *             length="1"
     *         
     */
    @Column(name="IS_BACKUP",nullable=true)
    public String getIsBackup() {
        return this.isBackup;
    }

    public void setIsBackup(String isBackup) {
        this.isBackup = isBackup;
    }

    /** 
     *            @hibernate.property
     *             not-null="true"
     *            @hibernate.column name="USER_ID"         
     *         
     */
    @Column(name="USER_ID",nullable=true)
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="MAILBOX_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMailFolder"
     *         
     */
    @OneToMany(mappedBy="toaMailBox",targetEntity=com.strongit.oa.bo.ToaMailFolder.class,cascade=CascadeType.ALL)
    @OrderBy("mailfolderType")
    public Set getToaMailFolders() {
        return this.toaMailFolders;
    }

    public void setToaMailFolders(Set toaMailFolders) {
        this.toaMailFolders = toaMailFolders;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mailboxId", getMailboxId())
            .toString();
    }

	

}
