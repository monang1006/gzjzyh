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
 *         table="T_OA_MESSAGE_ACCOUNT"
 *     
*/
@Entity
@Table(name="T_OA_MESSAGE_ACCOUNT")
public class ToaMessageAccount implements Serializable {

    /** identifier field */
    private String replyMessageId;

    /** nullable persistent field */
    private String replyNumber;

    /** nullable persistent field */
    private String senderNumber;

    /** nullable persistent field */
    private String replyContent;

    /** nullable persistent field */
    private Date replyTime;

    /** full constructor */
    public ToaMessageAccount(String replyMessageId, String replyNumber, String senderNumber, String replyContent, Date replyTime) {
        this.replyMessageId = replyMessageId;
        this.replyNumber = replyNumber;
        this.senderNumber = senderNumber;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
    }

    /** default constructor */
    public ToaMessageAccount() {
    }

    /** minimal constructor */
    public ToaMessageAccount(String replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="REPLY_MESSAGE_ID"
     *         
     */
    @Id
	@Column(name="REPLY_MESSAGE_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getReplyMessageId() {
        return this.replyMessageId;
    }

    public void setReplyMessageId(String replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    /** 
     *            @hibernate.property
     *             column="REPLY_NUMBER"
     *             length="20"
     *         
     */
    @Column(name="REPLY_NUMBER",nullable=true)
    public String getReplyNumber() {
        return this.replyNumber;
    }

    public void setReplyNumber(String replyNumber) {
        this.replyNumber = replyNumber;
    }

    /** 
     *            @hibernate.property
     *             column="SENDER_NUMBER"
     *             length="20"
     *         
     */
    @Column(name="SENDER_NUMBER",nullable=true)
    public String getSenderNumber() {
        return this.senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    /** 
     *            @hibernate.property
     *             column="REPLY_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name="REPLY_CONTENT",nullable=true)
    public String getReplyContent() {
        return this.replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    /** 
     *            @hibernate.property
     *             column="REPLY_TIME"
     *             length="7"
     *         
     */
    @Column(name="REPLY_TIME",nullable=true)
    public Date getReplyTime() {
        return this.replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("replyMessageId", getReplyMessageId())
            .toString();
    }

}
