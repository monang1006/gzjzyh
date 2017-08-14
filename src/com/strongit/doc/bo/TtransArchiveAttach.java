package com.strongit.doc.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/** 
 *        @hibernate.class
 *         table="T_TRANS_ARCHIVE_ATTACH"
 *     
*/
@Entity
@Table(name="T_TRANS_ARCHIVE_ATTACH")
public class TtransArchiveAttach implements Serializable {

    /** identifier field */
    private String archiveAttachId;

    /** persistent field */
    private String fileServer;

    /** persistent field */
    private String attachFilePath;

    /** persistent field */
    private String attachFileName;

    /** persistent field */
    private String attachFileType;

    /** nullable persistent field */
    private Date attachFileData;

    /** persistent field */
    private com.strongit.doc.bo.TtransArchive ttransArchive;

    /** full constructor */
    public TtransArchiveAttach(String archiveAttachId, String fileServer, String attachFilePath, String attachFileName, String attachFileType, Date attachFileData, com.strongit.doc.bo.TtransArchive ttransArchive) {
        this.archiveAttachId = archiveAttachId;
        this.fileServer = fileServer;
        this.attachFilePath = attachFilePath;
        this.attachFileName = attachFileName;
        this.attachFileType = attachFileType;
        this.attachFileData = attachFileData;
        this.ttransArchive = ttransArchive;
    }

    /** default constructor */
    public TtransArchiveAttach() {
    }

    /** minimal constructor */
    public TtransArchiveAttach(String archiveAttachId, String fileServer, String attachFilePath, String attachFileName, String attachFileType, com.strongit.doc.bo.TtransArchive ttransArchive) {
        this.archiveAttachId = archiveAttachId;
        this.fileServer = fileServer;
        this.attachFilePath = attachFilePath;
        this.attachFileName = attachFileName;
        this.attachFileType = attachFileType;
        this.ttransArchive = ttransArchive;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ARCHIVE_ATTACH_ID"
     *         
     */
    @Id
    @Column(name="ARCHIVE_ATTACH_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getArchiveAttachId() {
        return this.archiveAttachId;
    }

    public void setArchiveAttachId(String archiveAttachId) {
        this.archiveAttachId = archiveAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_SERVER"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name="FILE_SERVER",nullable=true)
    public String getFileServer() {
        return this.fileServer;
    }

    public void setFileServer(String fileServer) {
        this.fileServer = fileServer;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_FILE_PATH"
     *             length="200"
     *             not-null="true"
     *         
     */
    @Column(name="ATTACH_FILE_PATH",nullable=true)
    public String getAttachFilePath() {
        return this.attachFilePath;
    }

    public void setAttachFilePath(String attachFilePath) {
        this.attachFilePath = attachFilePath;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_FILE_NAME"
     *             length="500"
     *             not-null="true"
     *         
     */
    @Column(name="ATTACH_FILE_NAME",nullable=true)
    public String getAttachFileName() {
        return this.attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_FILE_TYPE"
     *             length="5"
     *             not-null="true"
     *         
     */
    @Column(name="ATTACH_FILE_TYPE",nullable=true)
    public String getAttachFileType() {
        return this.attachFileType;
    }

    public void setAttachFileType(String attachFileType) {
        this.attachFileType = attachFileType;
    }

    /** 
     *            @hibernate.property
     *             column="ATTACH_FILE_DATA"
     *             length="7"
     *         
     */
    @Column(name="ATTACH_FILE_DATA",nullable=true)
    public Date getAttachFileData() {
        return this.attachFileData;
    }

    public void setAttachFileData(Date attachFileData) {
        this.attachFileData = attachFileData;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ARCHIVE_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="ARCHIVE_ID", nullable=true,unique=true)
	@NotFound(action=NotFoundAction.IGNORE)
    public com.strongit.doc.bo.TtransArchive getTtransArchive() {
        return this.ttransArchive;
    }

    public void setTtransArchive(com.strongit.doc.bo.TtransArchive ttransArchive) {
        this.ttransArchive = ttransArchive;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("archiveAttachId", getArchiveAttachId())
            .toString();
    }

}
