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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MESSAGE"
 *     
*/
@Entity
@Table(name="T_OA_MESSAGE")
public class ToaMessage implements Serializable {

    /** identifier field */
    private String msgId;

    /** nullable persistent field */
    private String msgSender;

    /** nullable persistent field */
    private String msgTitle;

    /** nullable persistent field */
    private Date msgDate;

    /** nullable persistent field */
    private String msgSize;

    /** nullable persistent field */
    private String msgReceiver;

    /** nullable persistent field */
    private String msgContent;

    /** nullable persistent field */
    private String isSendBack;

    /** nullable persistent field */
    private String isReturnBack;

    /** nullable persistent field */
    private String isReply;

    /** nullable persistent field */
    private String isRead;
    
    /** nullable persistent field */
    private String isHasAttr;

    /** nullable persistent field */
    private String msgPri;

    /** nullable persistent field */
    private String parentMsgId;

    /** nullable persistent field */
    private String msgCc;
    
    /** 自增编码 */
    private String messageCode;
    
    /** 模块ID */
    private String moduleCode;
    
    /** 短信逻辑删除 */
    private String msgIsdel;
    
    /** 删除状态*/
    public static final String MSG_NOTDEL = "0";	//未删除
    public static final String MSG_DEL = "1";	//已删除
    
    /**  消息类型 */
    private String msgType;
    
    /** persistent field */
    private com.strongit.oa.bo.ToaMessageFolder toaMessageFolder;

    /** persistent field */
    private Set toaMessageAttaches;
    
    private Set toaMsgStateMeans;

    /** full constructor */
    public ToaMessage(String msgId, String msgSender, String msgTitle, Date msgDate, String msgSize, String msgReceiver, String msgContent, String isSendBack, String isReturnBack, String isReply, String isRead, String msgPri, String parentMsgId, String msgIsdel, String msgType, String msgCc, String messageCode, String moduleCode, com.strongit.oa.bo.ToaMessageFolder toaMessageFolder, Set toaMessageAttaches, Set toaMsgStateMeans) {
        this.msgId = msgId;
        this.msgSender = msgSender;
        this.msgTitle = msgTitle;
        this.msgDate = msgDate;
        this.msgSize = msgSize;
        this.msgReceiver = msgReceiver;
        this.msgContent = msgContent;
        this.isSendBack = isSendBack;
        this.isReturnBack = isReturnBack;
        this.isReply = isReply;
        this.isRead = isRead;
        this.msgPri = msgPri;
        this.parentMsgId = parentMsgId;
        this.msgIsdel = msgIsdel;
        this.msgType = msgType;
        this.msgCc = msgCc;
        this.messageCode = messageCode;
        this.moduleCode = moduleCode;
        this.toaMessageFolder = toaMessageFolder;
        this.toaMessageAttaches = toaMessageAttaches;
        this.toaMsgStateMeans = toaMsgStateMeans;
    }

    /** default constructor */
    public ToaMessage() {
    }

    /** minimal constructor */
    public ToaMessage(String msgId, com.strongit.oa.bo.ToaMessageFolder toaMessageFolder, Set toaMessageAttaches) {
        this.msgId = msgId;
        this.toaMessageFolder = toaMessageFolder;
        this.toaMessageAttaches = toaMessageAttaches;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MSG_ID"
     *         
     */
    @Id
	@Column(name="MSG_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_SENDER"
     *             length="32"
     *         
     */
    @Column(name="MSG_SENDER",nullable=true)
    public String getMsgSender() {
        return this.msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_TITLE"
     *             length="50"
     *         
     */
    @Column(name="MSG_TITLE",nullable=true)
    public String getMsgTitle() {
        return this.msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_DATE"
     *             length="7"
     *         
     */
    @Column(name="MSG_DATE",nullable=true)
    public Date getMsgDate() {
        return this.msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_SIZE"
     *             length="20"
     *         
     */
    @Column(name="MSG_SIZE",nullable=true)
    public String getMsgSize() {
        return this.msgSize;
    }

    public void setMsgSize(String msgSize) {
        this.msgSize = msgSize;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_RECEIVER"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="MSG_RECEIVER", columnDefinition="CLOB", nullable=true)
    public String getMsgReceiver() {
        return this.msgReceiver;
    }

    public void setMsgReceiver(String msgReceiver) {
        this.msgReceiver = msgReceiver;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_CONTENT"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="MSG_CONTENT", columnDefinition="CLOB", nullable=true)
    public String getMsgContent() {
        return this.msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    /** 
     *            @hibernate.property
     *             column="IS_SEND_BACK"
     *             length="1"
     *         
     */
    @Column(name="IS_SEND_BACK",nullable=true)
    public String getIsSendBack() {
        return this.isSendBack;
    }

    public void setIsSendBack(String isSendBack) {
        this.isSendBack = isSendBack;
    }

    /** 
     *            @hibernate.property
     *             column="IS_RETURN_BACK"
     *             length="1"
     *         
     */
    @Column(name="IS_RETURN_BACK",nullable=true)
    public String getIsReturnBack() {
        return this.isReturnBack;
    }

    public void setIsReturnBack(String isReturnBack) {
        this.isReturnBack = isReturnBack;
    }

    /** 
     *            @hibernate.property
     *             column="IS_REPLY"
     *             length="1"
     *         
     */
    @Column(name="IS_REPLY",nullable=true)
    public String getIsReply() {
        return this.isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }

    /** 
     *            @hibernate.property
     *             column="IS_READ"
     *             length="1"
     *         
     */
    @Column(name="IS_READ",nullable=true)
    public String getIsRead() {
        return this.isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
    
    /** 
     *            @hibernate.property
     *             column="MSG_ISHASATTR"
     *             length="1"
     *         
     */
    @Column(name="MSG_ISHASATTR",nullable=true)
	public String getIsHasAttr() {
		return isHasAttr;
	}
	
	public void setIsHasAttr(String isHasAttr) {
		this.isHasAttr = isHasAttr;
	}

    /** 
     *            @hibernate.property
     *             column="MSG_PRI"
     *             length="1"
     *         
     */
    @Column(name="MSG_PRI",nullable=true)
    public String getMsgPri() {
        return this.msgPri;
    }

    public void setMsgPri(String msgPri) {
        this.msgPri = msgPri;
    }

    /** 
     *            @hibernate.property
     *             column="PARENT_MSG_ID"
     *             length="32"
     *         
     */
    @Column(name="PARENT_MSG_ID",nullable=true)
    public String getParentMsgId() {
        return this.parentMsgId;
    }

    public void setParentMsgId(String parentMsgId) {
        this.parentMsgId = parentMsgId;
    }
    
    /** 
     *            @hibernate.property
     *             column="MSG_ISDEL"
     *             length="1"
     *         
     */
    @Column(name="MSG_ISDEL",nullable=true)
    public String getMsgIsdel() {
		return msgIsdel;
	}

	public void setMsgIsdel(String msgIsdel) {
		this.msgIsdel = msgIsdel;
	}
    
    /** 
     *            @hibernate.property
     *             column="MESSAGE_CODE"
     *             length="32"
     *         
     */
    @Column(name="MESSAGE_CODE",nullable=true)
    public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	/** 
     *            @hibernate.property
     *             column="BUSINESS_MODULE_CODE"
     *             length="2"
     *         
     */
    @Column(name="BUSINESS_MODULE_CODE",nullable=true)
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	
    /** 
     *            @hibernate.property
     *             column="MSG_CC"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="MSG_CC", columnDefinition="CLOB", nullable=true)
    public String getMsgCc() {
        return this.msgCc;
    }

    public void setMsgCc(String msgCc) {
        this.msgCc = msgCc;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_TYPE"
     *             length="50"
     *         
     */
    @Column(name="MSG_TYPE",nullable=true)
	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

    
    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MSG_FOLDER_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MSG_FOLDER_ID")
    public com.strongit.oa.bo.ToaMessageFolder getToaMessageFolder() {
        return this.toaMessageFolder;
    }

    public void setToaMessageFolder(com.strongit.oa.bo.ToaMessageFolder toaMessageFolder) {
        this.toaMessageFolder = toaMessageFolder;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="MSG_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMessageAttach"
     *         
     */
    @OneToMany(mappedBy="toaMessage",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaMessageAttach.class)
    public Set getToaMessageAttaches() {
        return this.toaMessageAttaches;
    }

    public void setToaMessageAttaches(Set toaMessageAttaches) {
        this.toaMessageAttaches = toaMessageAttaches;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="MSG_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMsgStateMean"
     *         
     */
    @OneToMany(mappedBy="toaMessage",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaMsgStateMean.class)
	public Set getToaMsgStateMeans() {
		return toaMsgStateMeans;
	}

	public void setToaMsgStateMeans(Set toaMsgStateMeans) {
		this.toaMsgStateMeans = toaMsgStateMeans;
	}

	public String toString() {
		return new ToStringBuilder(this)
		.append("msgId", getMsgId())
		.toString();
	}


	
}
