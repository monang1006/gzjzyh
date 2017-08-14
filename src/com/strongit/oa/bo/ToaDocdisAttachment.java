package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_DOCDIS_ATTACHMENT"
 *     
*/
@Entity
@Table(name="T_OA_DOCDIS_ATTACHMENT")
public class ToaDocdisAttachment implements Serializable {

	private static final long serialVersionUID = 2145390627185915404L;

	/** identifier field */
	@Id
	@GeneratedValue
    private String docAttachId;

    /** persistent field */
    private String docId;

    /** nullable persistent field */
    private String docAttachName;

    /** nullable persistent field */
    private byte[] docAttachContent;
    
    /** identifier field */
    private String attachPath;

    /** full constructor */
    public ToaDocdisAttachment(String docAttachId, String docId, String docAttachName, byte[] docAttachContent,String attachPath ) {
        this.docAttachId = docAttachId;
        this.docId = docId;
        this.docAttachName = docAttachName;
        this.docAttachContent = docAttachContent;
        this.attachPath = attachPath;

    }

    /** default constructor */
    public ToaDocdisAttachment() {
    }

    /** minimal constructor */
    public ToaDocdisAttachment(String docAttachId, String docId) {
        this.docAttachId = docAttachId;
        this.docId = docId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOC_ATTACH_ID"
     *         
     */
	@Id
	@Column(name="DOC_ATTACH_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getDocAttachId() {
        return this.docAttachId;
    }

    public void setDocAttachId(String docAttachId) {
        this.docAttachId = docAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_ID"
     *             length="32"
     *             not-null="true"
     *         
     */
    @Column(name="DOC_ID",length=32,nullable=false)
    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_ATTACH_NAME"
     *             length="100"
     *         
     */
    @Column(name="DOC_ATTACH_NAME",length=100)
    public String getDocAttachName() {
        return this.docAttachName;
    }

    public void setDocAttachName(String docAttachName) {
        this.docAttachName = docAttachName;
    }
    
    @Column(name = "ATTACH_PATH", length = 255)
	public String getAttachPath() {
		return attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

    /** 
     *            @hibernate.property
     *             column="DOC_ATTACH_CONTENT"
     *             length="4000"
     *         
     */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DOC_ATTACH_CONTENT", columnDefinition = "BLOB")
    public byte[] getDocAttachContent() {
        return this.docAttachContent;
    }

    public void setDocAttachContent(byte[] docAttachContent) {
        this.docAttachContent = docAttachContent;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docAttachId", getDocAttachId())
            .toString();
    }

}
