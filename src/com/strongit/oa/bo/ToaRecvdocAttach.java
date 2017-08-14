package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_RECVDOC_ATTACH"
 *     
*/
@Entity
@Table(name="T_OA_RECVDOC_ATTACH",catalog="",schema="")
public class ToaRecvdocAttach implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String recvdocAttachId;

    /** nullable persistent field */
    private String recvdocAttachTitle;

    /** nullable persistent field */
    private byte[] recvdocAttachRemark;

    /** nullable persistent field */
    private String attachId;

    /** persistent field */
    private com.strongit.oa.bo.ToaRecvdoc toaRecvdoc;

    /** full constructor */
    public ToaRecvdocAttach(String recvdocAttachId, String recvdocAttachTitle, byte[] recvdocAttachRemark, String attachId, com.strongit.oa.bo.ToaRecvdoc toaRecvdoc) {
        this.recvdocAttachId = recvdocAttachId;
        this.recvdocAttachTitle = recvdocAttachTitle;
        this.recvdocAttachRemark = recvdocAttachRemark;
        this.attachId = attachId;
        this.toaRecvdoc = toaRecvdoc;
    }

    /** default constructor */
    public ToaRecvdocAttach() {
    }

    /** minimal constructor */
    public ToaRecvdocAttach(String recvdocAttachId, com.strongit.oa.bo.ToaRecvdoc toaRecvdoc) {
        this.recvdocAttachId = recvdocAttachId;
        this.toaRecvdoc = toaRecvdoc;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="RECVDOC_ATTACH_ID"
     *         
     */
    @Id
	@Column(name="RECVDOC_ATTACH_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRecvdocAttachId() {
        return this.recvdocAttachId;
    }

    public void setRecvdocAttachId(String recvdocAttachId) {
        this.recvdocAttachId = recvdocAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_ATTACH_TITLE"
     *             length="100"
     *         
     */
    @Column(name="RECVDOC_ATTACH_TITLE",nullable=true)
    public String getRecvdocAttachTitle() {
        return this.recvdocAttachTitle;
    }

    public void setRecvdocAttachTitle(String recvdocAttachTitle) {
        this.recvdocAttachTitle = recvdocAttachTitle;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_ATTACH_REMARK"
     *             length="4000"
     *         
     */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "RECVDOC_ATTACH_REMARK", columnDefinition = "BLOB",nullable=true)
    public byte[] getRecvdocAttachRemark() {
        return this.recvdocAttachRemark;
    }

    public void setRecvdocAttachRemark(byte[] recvdocAttachRemark) {
        this.recvdocAttachRemark = recvdocAttachRemark;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_ID"
     *             length="32"
     *         
     */
    @Column(name="ATTACH_ID",nullable=true)
    public String getAttachId() {
        return this.attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="RECV_DOC_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="RECV_DOC_ID", nullable=false)
    public com.strongit.oa.bo.ToaRecvdoc getToaRecvdoc() {
        return this.toaRecvdoc;
    }

    public void setToaRecvdoc(com.strongit.oa.bo.ToaRecvdoc toaRecvdoc) {
        this.toaRecvdoc = toaRecvdoc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("recvdocAttachId", getRecvdocAttachId())
            .toString();
    }

}
