package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MESSAGE_FOLDER"
 *     
*/
@Entity
@Table(name="T_OA_MESSAGE_FOLDER")
public class ToaMessageFolder implements Serializable {

    /** identifier field */
    private String msgFolderId;

    /** nullable persistent field */
    private String msgFolderName;

    /** 文件夹类型 */
    private String msgFolderType;
    
    /**文件夹类型:基础文件/1:自定义文件夹 */
    public static final String BASE_FOLDER = "0";
    public static final String USER_FOLDER = "1";

    /**文件夹类型:（基础文件夹）  "收件箱"*/
    public static final String FOLDER_RECV = "收件箱";
    /**文件夹类型:（基础文件夹）  "发件箱"*/
    public static final String FOLDER_SEND = "发件箱";
    /**文件夹类型:（基础文件夹）  "草稿箱"*/
    public static final String FOLDER_DRAFT = "草稿箱";
    /**文件夹类型:（基础文件夹）  "垃圾箱"*/
    public static final String FOLDER_RUBBISH = "垃圾箱";
    
    /** nullable persistent field */
    private String userId;

    /** persistent field */
    private Set toaMessages;

    /** full constructor */
    public ToaMessageFolder(String msgFolderId, String msgFolderName, String msgFolderType, String userId, Set toaMessages) {
        this.msgFolderId = msgFolderId;
        this.msgFolderName = msgFolderName;
        this.msgFolderType = msgFolderType;
        this.userId = userId;
        this.toaMessages = toaMessages;
    }

    /** default constructor */
    public ToaMessageFolder() {
    }

    /** minimal constructor */
    public ToaMessageFolder(String msgFolderId, Set toaMessages) {
        this.msgFolderId = msgFolderId;
        this.toaMessages = toaMessages;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MSG_FOLDER_ID"
     *         
     */
    @Id
	@Column(name="MSG_FOLDER_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMsgFolderId() {
        return this.msgFolderId;
    }

    public void setMsgFolderId(String msgFolderId) {
        this.msgFolderId = msgFolderId;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_FOLDER_NAME"
     *             length="25"
     *         
     */
    @Column(name="MSG_FOLDER_NAME",nullable=true)
    public String getMsgFolderName() {
        return this.msgFolderName;
    }

    public void setMsgFolderName(String msgFolderName) {
        this.msgFolderName = msgFolderName;
    }

    /** 
     *            @hibernate.property
     *             column="MSG_FOLDER_TYPE"
     *             length="1"
     *         
     */
    @Column(name="MSG_FOLDER_TYPE",nullable=true)
    public String getMsgFolderType() {
        return this.msgFolderType;
    }

    public void setMsgFolderType(String msgFolderType) {
        this.msgFolderType = msgFolderType;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
     *         
     */
    @Column(name="USER_ID",nullable=true)
    public String getUserId() {
        return this.userId;
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
     *             column="MSG_FOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMessage"
     *         
     */
    @OneToMany(mappedBy="toaMessageFolder",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaMessage.class)
    public Set getToaMessages() {
        return this.toaMessages;
    }

    public void setToaMessages(Set toaMessages) {
        this.toaMessages = toaMessages;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("msgFolderId", getMsgFolderId())
            .toString();
    }

}
