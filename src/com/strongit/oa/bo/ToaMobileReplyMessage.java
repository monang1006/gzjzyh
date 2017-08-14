package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import com.strongit.oa.util.GlobalBaseData;


/** 
 *        @hibernate.class
 *         table="T_OA_MOBILE_REPLY_MESSAGE"
 *     
*/
@Entity
@Table(name="T_OA_MOBILE_REPLY_MESSAGE")
public class ToaMobileReplyMessage implements Serializable {

    /** identifier field */
    private Long replyMessageId;

    /** nullable persistent field */
    private String replyUser;

    /** nullable persistent field */
    private String mobileNumber;

    /** nullable persistent field */
    private String replyContent;

    /** nullable persistent field */
    private Date replyTime;

    /** nullable persistent field */
    private String module;

    /** nullable persistent field */
    private String messageId;
    
    public final static String TYPE_MESSAGE = GlobalBaseData.message_model;   //消息模块

    /** full constructor */
    public ToaMobileReplyMessage(Long replyMessageId, String replyUser, String mobileNumber, String replyContent, Date replyTime, String module, String messageId) {
        this.replyMessageId = replyMessageId;
        this.replyUser = replyUser;
        this.mobileNumber = mobileNumber;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
        this.module = module;
        this.messageId = messageId;
    }

    /** default constructor */
    public ToaMobileReplyMessage() {
    }

    /** minimal constructor */
    public ToaMobileReplyMessage(Long replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.Long"
     *             column="REPLY_MESSAGE_ID"
     *         
     */
    @Id
	@Column(name="REPLY_MESSAGE_ID", nullable=false,length = 32)
	@GeneratedValue(generator="native")
	@GenericGenerator(name = "native", strategy = "native")
    public Long getReplyMessageId() {
        return this.replyMessageId;
    }

    public void setReplyMessageId(Long replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    /** 
     *            @hibernate.property
     *             column="REPLY_USER"
     *             length="32"
     *         
     */
    @Column(name="REPLY_USER",nullable=true)
    public String getReplyUser() {
        return this.replyUser;
    }

    public void setReplyUser(String replyUser) {
        this.replyUser = replyUser;
    }

    /** 
     *            @hibernate.property
     *             column="MOBILE_NUMBER"
     *             length="20"
     *         
     */
    @Column(name="MOBILE_NUMBER",nullable=true)
    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    /** 
     *            @hibernate.property
     *             column="MODULE"
     *             length="2"
     *         
     */
    @Column(name="MODULE",nullable=true)
    public String getModule() {
        return this.module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    /** 
     *            @hibernate.property
     *             column="MESSAGE_ID"
     *             length="40"
     *         
     */
    @Column(name="MESSAGE_ID",nullable=true)
    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("replyMessageId", getReplyMessageId())
            .toString();
    }

}
