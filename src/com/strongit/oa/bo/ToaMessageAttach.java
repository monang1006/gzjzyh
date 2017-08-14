package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MESSAGE_ATTACH"
 *     
*/
@Entity
@Table(name="T_OA_MESSAGE_ATTACH")
public class ToaMessageAttach implements Serializable {

    /** identifier field */
    private String messageAttachId;

    /** nullable persistent field */
    private String attachId7;

    /** persistent field */
    private com.strongit.oa.bo.ToaMessage toaMessage;

    /** full constructor */
    public ToaMessageAttach(String messageAttachId, String attachId7, com.strongit.oa.bo.ToaMessage toaMessage) {
        this.messageAttachId = messageAttachId;
        this.attachId7 = attachId7;
        this.toaMessage = toaMessage;
    }

    /** default constructor */
    public ToaMessageAttach() {
    }

    /** minimal constructor */
    public ToaMessageAttach(String messageAttachId, com.strongit.oa.bo.ToaMessage toaMessage) {
        this.messageAttachId = messageAttachId;
        this.toaMessage = toaMessage;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MESSAGE_ATTACH_ID"
     *         
     */
    @Id
	@Column(name="MESSAGE_ATTACH_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMessageAttachId() {
        return this.messageAttachId;
    }

    public void setMessageAttachId(String messageAttachId) {
        this.messageAttachId = messageAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_ID7"
     *             length="32"
     *         
     */
    @Column(name="ATTACH_ID7",nullable=true)
    public String getAttachId7() {
        return this.attachId7;
    }

    public void setAttachId7(String attachId7) {
        this.attachId7 = attachId7;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MSG_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="MSG_ID")
    public com.strongit.oa.bo.ToaMessage getToaMessage() {
        return this.toaMessage;
    }

    public void setToaMessage(com.strongit.oa.bo.ToaMessage toaMessage) {
        this.toaMessage = toaMessage;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("messageAttachId", getMessageAttachId())
            .toString();
    }

}
