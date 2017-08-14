package com.strongit.oa.bo;

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


/** 
 *        @hibernate.class
 *         table="T_OA_ARCHIVE_DESTROY_FILE"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_DESTROY_FILE")
public class ToaArchiveDestroyFile implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String destroyFileId;

    /** nullable persistent field */
    private String destroyFileNo;

    /** nullable persistent field */
    private String destroyFileAuthor;

    /** nullable persistent field */
    private String destroyFileName;

    /** nullable persistent field */
    private Date destroyFileDate;

    /** nullable persistent field */
    private Long destroyFilePage;

    /** nullable persistent field */
    private String destroyFileDesc;

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveDestroy toaArchiveDestroy;

    /** full constructor */
    public ToaArchiveDestroyFile(String destroyFileId, String destroyFileNo, String destroyFileAuthor, String destroyFileName, Date destroyFileDate, Long destroyFilePage, String destroyFileDesc, com.strongit.oa.bo.ToaArchiveDestroy toaArchiveDestroy) {
        this.destroyFileId = destroyFileId;
        this.destroyFileNo = destroyFileNo;
        this.destroyFileAuthor = destroyFileAuthor;
        this.destroyFileName = destroyFileName;
        this.destroyFileDate = destroyFileDate;
        this.destroyFilePage = destroyFilePage;
        this.destroyFileDesc = destroyFileDesc;
        this.toaArchiveDestroy = toaArchiveDestroy;
    }

    /** default constructor */
    public ToaArchiveDestroyFile() {
    }

    /** minimal constructor */
    public ToaArchiveDestroyFile(String destroyFileId, com.strongit.oa.bo.ToaArchiveDestroy toaArchiveDestroy) {
        this.destroyFileId = destroyFileId;
        this.toaArchiveDestroy = toaArchiveDestroy;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DESTROY_FILE_ID"
     *         
     */
    @Id
	@Column(name="DESTROY_FILE_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDestroyFileId() {
        return this.destroyFileId;
    }

    public void setDestroyFileId(String destroyFileId) {
        this.destroyFileId = destroyFileId;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FILE_NO"
     *             length="25"
     *         
     */
    @Column(name="DESTROY_FILE_NO",nullable=true)
    public String getDestroyFileNo() {
        return this.destroyFileNo;
    }

    public void setDestroyFileNo(String destroyFileNo) {
        this.destroyFileNo = destroyFileNo;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FILE_AUTHOR"
     *             length="25"
     *         
     */
    @Column(name="DESTROY_FILE_AUTHOR",nullable=true)
    public String getDestroyFileAuthor() {
        return this.destroyFileAuthor;
    }

    public void setDestroyFileAuthor(String destroyFileAuthor) {
        this.destroyFileAuthor = destroyFileAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FILE_NAME"
     *             length="500"
     *         
     */
    @Column(name="DESTROY_FILE_NAME",nullable=true)
    public String getDestroyFileName() {
        return this.destroyFileName;
    }

    public void setDestroyFileName(String destroyFileName) {
        this.destroyFileName = destroyFileName;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FILE_DATE"
     *             length="7"
     *         
     */
    @Column(name="DESTROY_FILE_DATE",nullable=true)
    public Date getDestroyFileDate() {
        return this.destroyFileDate;
    }

    public void setDestroyFileDate(Date destroyFileDate) {
        this.destroyFileDate = destroyFileDate;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FILE_PAGE"
     *             length="10"
     *         
     */
    @Column(name="DESTROY_FILE_PAGE",nullable=true)
    public Long getDestroyFilePage() {
        return this.destroyFilePage;
    }

    public void setDestroyFilePage(Long destroyFilePage) {
        this.destroyFilePage = destroyFilePage;
    }

    /** 
     *            @hibernate.property
     *             column="DESTROY_FILE_DESC"
     *             length="4000"
     *         
     */
    @Column(name="DESTROY_FILE_DESC",nullable=true)
    public String getDestroyFileDesc() {
        return this.destroyFileDesc;
    }

    public void setDestroyFileDesc(String destroyFileDesc) {
        this.destroyFileDesc = destroyFileDesc;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DESTROY_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="DESTROY_ID", nullable=true)
    public com.strongit.oa.bo.ToaArchiveDestroy getToaArchiveDestroy() {
        return this.toaArchiveDestroy;
    }

    public void setToaArchiveDestroy(com.strongit.oa.bo.ToaArchiveDestroy toaArchiveDestroy) {
        this.toaArchiveDestroy = toaArchiveDestroy;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("destroyFileId", getDestroyFileId())
            .toString();
    }

}
