package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_FILE_APPEND"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_FILE_APPEND")
public class ToaArchiveFileAppend implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String appendId;

    /** nullable persistent field */
    private byte[] appendContent;

    /** nullable persistent field */
    private String appendType;
    
    /** nullable persistent field */
    private String appendName;
    
    /** nullable persistent field */
    private String appendSize;
    
    
    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveFile toaArchiveFile;

    /** full constructor */
    public ToaArchiveFileAppend(String appendId, byte[] appendContent, String appendType, com.strongit.oa.bo.ToaArchiveFile toaArchiveFile) {
        this.appendId = appendId;
        this.appendContent = appendContent;
        this.appendType = appendType;
        this.toaArchiveFile = toaArchiveFile;
    }

    /** default constructor */
    public ToaArchiveFileAppend() {
    }

    /** minimal constructor */
    public ToaArchiveFileAppend(String appendId, com.strongit.oa.bo.ToaArchiveFile toaArchiveFile) {
        this.appendId = appendId;
        this.toaArchiveFile = toaArchiveFile;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="APPEND_ID"
     *         
     */
    @Id
	@Column(name="APPEND_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getAppendId() {
        return this.appendId;
    }

    public void setAppendId(String appendId) {
        this.appendId = appendId;
    }

    /** 
     *            @hibernate.property
     *             column="APPEND_CONTENT"
     *             length="4000"
     *         
     */
    
    @Column(name="APPEND_CONTENT",nullable=true)
    public byte[] getAppendContent() {
        return this.appendContent;
    }

    public void setAppendContent(byte[] appendContent) {
        this.appendContent = appendContent;
    }

    /** 
     *            @hibernate.property
     *             column="APPEND_TYPE"
     *             length="1"
     *         
     */
    @Column(name="APPEND_TYPE",nullable=true)
    public String getAppendType() {
        return this.appendType;
    }

    public void setAppendType(String appendType) {
        this.appendType = appendType;
    }
    
    /** 
     *            @hibernate.property
     *             column="APPEND_NAME"
     *             length="100"
     *         
     */
    @Column(name="APPEND_NAME",nullable=true)
    public String getAppendName() {
		return appendName;
	}

	public void setAppendName(String appendName) {
		this.appendName = appendName;
	}

	/** 
     *            @hibernate.property
     *             column="APPEND_SIZE"
     *             length="10"
     *         
     */
    @Column(name="APPEND_SIZE",nullable=true)
	public String getAppendSize() {
		return appendSize;
	}

	public void setAppendSize(String appendSize) {
		this.appendSize = appendSize;
	}


    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FILE_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="FILE_ID", nullable=true)
    public com.strongit.oa.bo.ToaArchiveFile getToaArchiveFile() {
        return this.toaArchiveFile;
    }

    public void setToaArchiveFile(com.strongit.oa.bo.ToaArchiveFile toaArchiveFile) {
        this.toaArchiveFile = toaArchiveFile;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("appendId", getAppendId())
            .toString();
    }


}
