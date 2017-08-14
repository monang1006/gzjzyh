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
 *         table="T_OA_ARCHIVE_BORROW"
 *     
*/
@Entity
@Table(name="T_OA_ARCHIVE_BORROW")
public class ToaArchiveBorrow implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String borrowId;

    /** nullable persistent field */
    private String borrowPersonid;

    /** nullable persistent field */
    private String borrowPersonname;

    /** nullable persistent field */
    private Date borrowFromtime;

    /** nullable persistent field */
    private Date borrowEndtime;

    /** nullable persistent field */
    private String borrowDesc;

    /** nullable persistent field */
    private Date borrowTime;

    /** nullable persistent field */
    private String borrowAuditing;

    /** nullable persistent field */
    private String borrowAuditingName;

    /** nullable persistent field */
    private Date borrowAuditingTime;

    /** nullable persistent field */
    private String borrowAuditingDesc;
    
    /** nullable persistent field */
    private String borrowViewState;

    /** persistent field */
    private com.strongit.oa.bo.ToaArchiveFile toaArchiveFile;

    /** full constructor */
    public ToaArchiveBorrow(String borrowId, String borrowPersonid, String borrowPersonname, Date borrowFromtime, Date borrowEndtime, String borrowDesc, Date borrowTime, String borrowAuditing, String borrowAuditingName, Date borrowAuditingTime, String borrowAuditingDesc, com.strongit.oa.bo.ToaArchiveFile toaArchiveFile) {
        this.borrowId = borrowId;
        this.borrowPersonid = borrowPersonid;
        this.borrowPersonname = borrowPersonname;
        this.borrowFromtime = borrowFromtime;
        this.borrowEndtime = borrowEndtime;
        this.borrowDesc = borrowDesc;
        this.borrowTime = borrowTime;
        this.borrowAuditing = borrowAuditing;
        this.borrowAuditingName = borrowAuditingName;
        this.borrowAuditingTime = borrowAuditingTime;
        this.borrowAuditingDesc = borrowAuditingDesc;
        this.toaArchiveFile = toaArchiveFile;
    }

    /** default constructor */
    public ToaArchiveBorrow() {
    }

    /** minimal constructor */
    public ToaArchiveBorrow(String borrowId, com.strongit.oa.bo.ToaArchiveFile toaArchiveFile) {
        this.borrowId = borrowId;
        this.toaArchiveFile = toaArchiveFile;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="BORROW_ID"
     *         
     */
    @Id
	@Column(name="BORROW_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getBorrowId() {
        return this.borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_PERSONID"
     *             length="32"
     *         
     */
    @Column(name="BORROW_PERSONID",nullable=true)
    public String getBorrowPersonid() {
        return this.borrowPersonid;
    }

    public void setBorrowPersonid(String borrowPersonid) {
        this.borrowPersonid = borrowPersonid;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_PERSONNAME"
     *             length="25"
     *         
     */
    @Column(name="BORROW_PERSONNAME",nullable=true)
    public String getBorrowPersonname() {
        return this.borrowPersonname;
    }

    public void setBorrowPersonname(String borrowPersonname) {
        this.borrowPersonname = borrowPersonname;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_FROMTIME"
     *             length="30"
     *         
     */
    @Column(name="BORROW_FROMTIME",nullable=true)
    public Date getBorrowFromtime() {
        return this.borrowFromtime;
    }

    public void setBorrowFromtime(Date borrowFromtime) {
        this.borrowFromtime = borrowFromtime;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_ENDTIME"
     *             length="30"
     *         
     */
    @Column(name="BORROW_ENDTIME",nullable=true)
    public Date getBorrowEndtime() {
        return this.borrowEndtime;
    }

    public void setBorrowEndtime(Date borrowEndtime) {
        this.borrowEndtime = borrowEndtime;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_DESC"
     *             length="400"
     *         
     */
    @Column(name="BORROW_DESC",nullable=true)
    public String getBorrowDesc() {
        return this.borrowDesc;
    }

    public void setBorrowDesc(String borrowDesc) {
        this.borrowDesc = borrowDesc;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_TIME"
     *             length="7"
     *         
     */
    @Column(name="BORROW_TIME",nullable=true)
    public Date getBorrowTime() {
        return this.borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_AUDITING"
     *             length="1"
     *         
     */
    @Column(name="BORROW_AUDITING",nullable=true)
    public String getBorrowAuditing() {
        return this.borrowAuditing;
    }

    public void setBorrowAuditing(String borrowAuditing) {
        this.borrowAuditing = borrowAuditing;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_AUDITING_NAME"
     *             length="25"
     *         
     */
    @Column(name="BORROW_AUDITING_NAME",nullable=true)
    public String getBorrowAuditingName() {
        return this.borrowAuditingName;
    }

    public void setBorrowAuditingName(String borrowAuditingName) {
        this.borrowAuditingName = borrowAuditingName;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_AUDITING_TIME"
     *             length="30"
     *         
     */
    @Column(name="BORROW_AUDITING_TIME",nullable=true)
    public Date getBorrowAuditingTime() {
        return this.borrowAuditingTime;
    }

    public void setBorrowAuditingTime(Date borrowAuditingTime) {
        this.borrowAuditingTime = borrowAuditingTime;
    }

    /** 
     *            @hibernate.property
     *             column="BORROW_AUDITING_DESC"
     *             length="400"
     *         
     */
    @Column(name="BORROW_AUDITING_DESC",nullable=true)
    public String getBorrowAuditingDesc() {
        return this.borrowAuditingDesc;
    }

    public void setBorrowAuditingDesc(String borrowAuditingDesc) {
        this.borrowAuditingDesc = borrowAuditingDesc;
    }
    
    /** 
     *            @hibernate.property
     *             column="BORROW_VIEW_STATE"
     *             length="1"
     *         
     */
    @Column(name="BORROW_VIEW_STATE",nullable=true)
    public String getBorrowViewState() {
		return borrowViewState;
	}

	public void setBorrowViewState(String borrowViewState) {
		this.borrowViewState = borrowViewState;
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
            .append("borrowId", getBorrowId())
            .toString();
    }

}
