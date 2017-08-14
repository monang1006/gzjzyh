package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

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
import org.hibernate.annotations.GenericGenerator;

import com.strongit.oa.prsnfldr.util.Round;



/** 
 *        @hibernate.class
 *         table="T_OA_PRSNFLDR_FILE"
 *     
*/
@Entity
@Table(name="T_OA_PRSNFLDR_FILE",catalog="",schema="")
public class ToaPrsnfldrFile implements Serializable,Cloneable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6543450958481497388L;

	/** identifier field */
    private String fileId;

    /** nullable persistent field */
    private String fileName;

    /** nullable persistent field */
    private Date fileCreateTime;

    /** nullable persistent field */
    private String fileSortNo;

    /** nullable persistent field */
    private byte[] fileContent;

    /** nullable persistent field */
    private String fileSize;
    
    private String fileExt;

    private String fileCreatePerson;
    
    private String fileBeiZhu;//文件备注
    
    private String fileTitle;//文件标题
    /**
     * 用于对文件排序，因为文件大小存在MB，k,字节单位大小
     */
    private Double tmpFileSortNo;

    /** persistent field */
    private com.strongit.oa.bo.ToaPrsnfldrFolder toaPrsnfldrFolder;

    /** full constructor */
    public ToaPrsnfldrFile(String fileId, String fileName, Date fileCreateTime, String fileSortNo, byte[] fileContent, String fileSize, com.strongit.oa.bo.ToaPrsnfldrFolder toaPrsnfldrFolder) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileCreateTime = fileCreateTime;
        this.fileSortNo = fileSortNo;
        this.fileContent = fileContent;
        this.fileSize = fileSize;
        this.toaPrsnfldrFolder = toaPrsnfldrFolder;
    }

    public ToaPrsnfldrFile(String fileId, String fileTitle, String fileSize, Date fileCreateTime){
    	this.fileId = fileId;
        this.fileCreateTime = fileCreateTime;
        this.fileTitle = fileTitle;
        this.fileSize = fileSize;
    }
    /** default constructor */
    public ToaPrsnfldrFile() {
    }

    /** minimal constructor */
    public ToaPrsnfldrFile(String fileId, com.strongit.oa.bo.ToaPrsnfldrFolder toaPrsnfldrFolder) {
        this.fileId = fileId;
        this.toaPrsnfldrFolder = toaPrsnfldrFolder;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FILE_ID"
     *         
     */
    @Id
	@Column(name="FILE_ID",nullable=false,length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_NAME"
     *             length="128"
     *         
     */
    @Column(name="FILE_NAME",nullable=false)
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_CREATE_TIME"
     *             length="7"
     *         
     */
    @Column(name="FILE_CREATE_TIME")
    public Date getFileCreateTime() {
        return this.fileCreateTime;
    }

    public void setFileCreateTime(Date fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_SORT_NO"
     *             length="32"
     *         
     */
    @Column(name="FILE_SORT_NO")
    public String getFileSortNo() {
        return this.fileSortNo;
    }

    public void setFileSortNo(String fileSortNo) {
        this.fileSortNo = fileSortNo;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_CONTENT"
     *             length="128"
     *         
     */
    @Column(name="FILE_CONTENT")
    @Lob
    public byte[] getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    /** 
     *            @hibernate.property
     *             column="FILE_SIZE"
     *             length="32"
     *         
     */
    @Column(name="FILE_SIZE")
    public String getFileSize() {
    	return this.fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FOLDER_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FOLDER_ID")
    public com.strongit.oa.bo.ToaPrsnfldrFolder getToaPrsnfldrFolder() {
        return this.toaPrsnfldrFolder;
    }

    public void setToaPrsnfldrFolder(com.strongit.oa.bo.ToaPrsnfldrFolder toaPrsnfldrFolder) {
        this.toaPrsnfldrFolder = toaPrsnfldrFolder;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("fileId", getFileId())
            .toString();
    }

    @Column(name="FILE_EXT",nullable=true)
	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	 public Object clone()  {
		 try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	 }

	@Column(name="FILE_CREATE_PERSON",nullable=true) 
	public String getFileCreatePerson() {
		return fileCreatePerson;
	}

	public void setFileCreatePerson(String fileCreatePerson) {
		this.fileCreatePerson = fileCreatePerson;
	}

	@Column(name="FILE_BEIZHU")
	@Lob
	public String getFileBeiZhu() {
		return fileBeiZhu;
	}

	public void setFileBeiZhu(String fileBeiZhu) {
		this.fileBeiZhu = fileBeiZhu;
	}

	@Column(name="FILE_TITLE")
	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	@Transient
	public Double getTmpFileSortNo() {
		return tmpFileSortNo;
	}

	public void setTmpFileSortNo(Double tmpFileSortNo) {
		this.tmpFileSortNo = tmpFileSortNo;
	}
}
