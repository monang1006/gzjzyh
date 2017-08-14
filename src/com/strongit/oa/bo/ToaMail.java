package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MAIL"
 *     
*/
@Entity
@Table(name="T_OA_MAIL",catalog="",schema="")
public class ToaMail implements Serializable {

    /** identifier field */
    private String mailId;

    /** nullable persistent field */
    private String mailSender;

    /** nullable persistent field */
    private String mailTitle;

    /** nullable persistent field */
    private Date mailSendDate;

    /** nullable persistent field */
    private String mailSize;

    /** nullable persistent field */
    private String mailReceiver;

    /** nullable persistent field */
    private String mailCon;

    /** nullable persistent field */
    private String mailIsReply;

    /** nullable persistent field */
    private String mailPri;

    /** nullable persistent field */
    private String mailIsRead;
    
    private String mailMessageId;
    
    private String mailIsHasAtt;

    /** persistent field */
    private com.strongit.oa.bo.ToaMailFolder toaMailFolder;

    /** persistent field */
    private Set toaMailAttaches;

    /** full constructor */
    public ToaMail(String mailId, String mailSender, String mailTitle, Date mailSendDate, String mailSize, String mailReceiver, String mailCon, String mailIsReply, String mailPri, String mailIsRead, com.strongit.oa.bo.ToaMailFolder toaMailFolder, Set toaMailAttaches) {
        this.mailId = mailId;
        this.mailSender = mailSender;
        this.mailTitle = mailTitle;
        this.mailSendDate = mailSendDate;
        this.mailSize = mailSize;
        this.mailReceiver = mailReceiver;
        this.mailCon = mailCon;
        this.mailIsReply = mailIsReply;
        this.mailPri = mailPri;
        this.mailIsRead = mailIsRead;
        this.toaMailFolder = toaMailFolder;
        this.toaMailAttaches = toaMailAttaches;
    }

    /** default constructor */
    public ToaMail() {
    }

    /** minimal constructor */
    public ToaMail(String mailId, com.strongit.oa.bo.ToaMailFolder toaMailFolder, Set toaMailAttaches) {
        this.mailId = mailId;
        this.toaMailFolder = toaMailFolder;
        this.toaMailAttaches = toaMailAttaches;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MAIL_ID"
     *         
     */
	@Id
	@Column(name="MAIL_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getMailId() {
        return this.mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_SENDER"
     *             length="128"
     *         
     */
    @Column(name="MAIL_SENDER",nullable=true)
    public String getMailSender() {
        return this.mailSender;
    }

    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_TITLE"
     *             length="128"
     *         
     */
    @Column(name="MAIL_TITLE",nullable=true)
    public String getMailTitle() {
        return this.mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_SEND_DATE"
     *             length="7"
     *         
     */
    @Column(name="MAIL_SEND_DATE",nullable=true)
    public Date getMailSendDate() {
        return this.mailSendDate;
    }

    public void setMailSendDate(Date mailSendDate) {
        this.mailSendDate = mailSendDate;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_SIZE"
     *             length="128"
     *         
     */
    @Column(name="MAIL_SIZE",nullable=true)
    public String getMailSize() {
        return this.mailSize;
    }

    public void setMailSize(String mailSize) {
        this.mailSize = mailSize;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_RECEIVER"
     *             length="128"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="MAIL_RECEIVER", columnDefinition="CLOB", nullable=true)
    public String getMailReceiver() {
        return this.mailReceiver;
    }

    public void setMailReceiver(String mailReceiver) {
        this.mailReceiver = mailReceiver;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_CON"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="MAIL_CON", columnDefinition="CLOB", nullable=true)
    public String getMailCon() {
        return this.mailCon;
    }

    public void setMailCon(String mailCon) {
        this.mailCon = mailCon;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_IS_REPLY"
     *             length="1"
     *         
     */
    @Column(name="MAIL_IS_REPLY",nullable=true)
    public String getMailIsReply() {
        return this.mailIsReply;
    }

    public void setMailIsReply(String mailIsReply) {
        this.mailIsReply = mailIsReply;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_PRI"
     *             length="1"
     *         
     */
    @Column(name="MAIL_PRI",nullable=true)
    public String getMailPri() {
        return this.mailPri;
    }

    public void setMailPri(String mailPri) {
        this.mailPri = mailPri;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_IS_READ"
     *             length="1"
     *         
     */
    @Column(name="MAIL_IS_READ",nullable=true)
    public String getMailIsRead() {
        return this.mailIsRead;
    }

    /** 
     *            @hibernate.property
     *             column="MAIL_MESSAGE_ID"
     *             length="50"
     *         
     */
    public void setMailIsRead(String mailIsRead) {
        this.mailIsRead = mailIsRead;
    }
    
    @Column(name="MAIL_MESSAGE_ID",nullable=true)
    public String getMailMessageId(){
    	return this.mailMessageId;
    }
    
    public void setMailMessageId(String mailMessageId){
    	this.mailMessageId=mailMessageId;
    }
    
    @Column(name="MAIL_IS_HASATT",nullable=true)
	public String getMailIsHasAtt() {
		return mailIsHasAtt;
	}

	public void setMailIsHasAtt(String mailIsHasAtt) {
		this.mailIsHasAtt = mailIsHasAtt;
	}

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MAILFOLDER_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="MAILFOLDER_ID", nullable=false)
    public com.strongit.oa.bo.ToaMailFolder getToaMailFolder() {
        return this.toaMailFolder;
    }

    public void setToaMailFolder(com.strongit.oa.bo.ToaMailFolder toaMailFolder) {
        this.toaMailFolder = toaMailFolder;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="MAIL_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMailAttach"
     *         
     */
    @OneToMany(mappedBy="toaMail",targetEntity=com.strongit.oa.bo.ToaMailAttach.class,cascade=CascadeType.ALL)
    public Set getToaMailAttaches() {
        return this.toaMailAttaches;
    }

    public void setToaMailAttaches(Set toaMailAttaches) {
        this.toaMailAttaches = toaMailAttaches;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mailId", getMailId())
            .toString();
    }
}
