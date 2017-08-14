package com.strongit.doc.bo;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_TRANS_DOC_ATTACH"
 *     
*/
@Entity
@Table(name = "T_TRANS_DOC_ATTACH", catalog = "", schema = "")
public class TtransDocAttach implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3252119655360540585L;

	/** identifier field */
    private String docAttachId;

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
    
    private InputStream is ;

    /** persistent field */
    private com.strongit.doc.bo.TtransDoc ttransDoc;

    /** full constructor */
    public TtransDocAttach(String docAttachId, String fileServer, String attachFilePath, String attachFileName, String attachFileType, Date attachFileData, com.strongit.doc.bo.TtransDoc ttransDoc) {
        this.docAttachId = docAttachId;
        this.fileServer = fileServer;
        this.attachFilePath = attachFilePath;
        this.attachFileName = attachFileName;
        this.attachFileType = attachFileType;
        this.attachFileData = attachFileData;
        this.ttransDoc = ttransDoc;
    }

    /** default constructor */
    public TtransDocAttach() {
    }

    /** minimal constructor */
    public TtransDocAttach(String docAttachId, String fileServer, String attachFilePath, String attachFileName, String attachFileType, com.strongit.doc.bo.TtransDoc ttransDoc) {
        this.docAttachId = docAttachId;
        this.fileServer = fileServer;
        this.attachFilePath = attachFilePath;
        this.attachFileName = attachFileName;
        this.attachFileType = attachFileType;
        this.ttransDoc = ttransDoc;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOC_ATTACH_ID"
     *         
     */
    @Id
	@Column(name = "DOC_ATTACH_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDocAttachId() {
        return this.docAttachId;
    }

    public void setDocAttachId(String docAttachId) {
        this.docAttachId = docAttachId;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_SERVER"
     *             length="100"
     *             not-null="true"
     *         
     */
    @Column(name = "FILE_SERVER")
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
    @Column(name = "ATTACH_FILE_PATH")
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
    @Column(name = "ATTACH_FILE_NAME")
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
    @Column(name = "ATTACH_FILE_TYPE")
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
    @Column(name = "ATTACH_FILE_DATA")
    public Date getAttachFileData() {
        return this.attachFileData;
    }

    public void setAttachFileData(Date attachFileData) {
        this.attachFileData = attachFileData;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DOC_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="DOC_ID")
    public com.strongit.doc.bo.TtransDoc getTtransDoc() {
        return this.ttransDoc;
    }

    public void setTtransDoc(com.strongit.doc.bo.TtransDoc ttransDoc) {
        this.ttransDoc = ttransDoc;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docAttachId", getDocAttachId())
            .toString();
    }

    @Transient
	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

}
