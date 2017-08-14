package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_TFILE_APPEND"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_TFILE_APPEND")
public class ToaArchiveTfileAppend implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String tempappendId;

    /** nullable persistent field */
    private byte[] tempappendContent;

    /** nullable persistent field */
    private String tempappendType;
    
    /** nullable persistent field */
    private String tempappendName;
    
    /** nullable persistent field */
    private String tempappendSize;

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile;

    
    /** full constructor */
    public ToaArchiveTfileAppend(String tempappendId, byte[] tempappendContent, String tempappendType, com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile) {
        this.tempappendId = tempappendId;
        this.tempappendContent = tempappendContent;
        this.tempappendType = tempappendType;
        this.toaArchiveTempfile = toaArchiveTempfile;
    }

    /** default constructor */
    public ToaArchiveTfileAppend() {
    }

    /** minimal constructor */
    public ToaArchiveTfileAppend(String tempappendId, com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile) {
        this.tempappendId = tempappendId;
        this.toaArchiveTempfile = toaArchiveTempfile;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="TEMPAPPEND_ID"
     *         
     */
    @Id
	@Column(name="TEMPAPPEND_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getTempappendId() {
        return this.tempappendId;
    }

    public void setTempappendId(String tempappendId) {
        this.tempappendId = tempappendId;
    }
      
    
    /** 
     *            @hibernate.property
     *             column="TEMPAPPEND_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name="TEMPAPPEND_CONTENT",nullable=true)
    @Lob
    public byte[] getTempappendContent() {
		return tempappendContent;
	}

	public void setTempappendContent(byte[] tempappendContent) {
		this.tempappendContent = tempappendContent;
	}

	/** 
     *            @hibernate.property
     *             column="TEMPAPPEND_SIZE"
     *             length="10"
     *         
     */
    @Column(name="TEMPAPPEND_SIZE",nullable=true)
	public String getTempappendSize() {
		return tempappendSize;
	}

	public void setTempappendSize(String tempappendSize) {
		this.tempappendSize = tempappendSize;
	}

	/** 
     *            @hibernate.property
     *             column="TEMPAPPEND_NAME"
     *             length="100"
     *         
     */
    @Column(name="TEMPAPPEND_NAME",nullable=true)
    public String getTempappendName() {
        return this.tempappendName;
    }

    public void setTempappendName(String tempappendName) {
        this.tempappendName = tempappendName;
    }

    /** 
     *            @hibernate.property
     *             column="TEMPAPPEND_TYPE"
     *             length="1"
     *         
     */
    @Column(name="TEMPAPPEND_TYPE",nullable=true)
    public String getTempappendType() {
        return this.tempappendType;
    }

    public void setTempappendType(String tempappendType) {
        this.tempappendType = tempappendType;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="TEMPFILE_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="TEMPFILE_ID", nullable=true)
    public com.strongit.oa.bo.ToaArchiveTempfile getToaArchiveTempfile() {
        return this.toaArchiveTempfile;
    }

    public void setToaArchiveTempfile(com.strongit.oa.bo.ToaArchiveTempfile toaArchiveTempfile) {
        this.toaArchiveTempfile = toaArchiveTempfile;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("tempappendId", getTempappendId())
            .toString();
    }

    

}
