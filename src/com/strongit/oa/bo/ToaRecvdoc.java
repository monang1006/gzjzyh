package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_RECVDOC"
 *     
*/
@Entity
@Table(name="T_OA_RECVDOC")
public class ToaRecvdoc implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String recvDocId;

    /** nullable persistent field */
    private String recvdocBussinessCode;

    /** nullable persistent field */
    private String recvdocTitle;

    /** nullable persistent field */
    private String recvdocCode;

    /** nullable persistent field */
    private String recvdocSenderDepartCode;

    /** nullable persistent field */
    private String recvdocSecretLvl;

    /** nullable persistent field */
    private String recvdocProcDeadline;

    /** nullable persistent field */
    private String recvdocProcType;

    /** nullable persistent field */
    private String recvdocSubmittoDepart;

    /** nullable persistent field */
    private String recvdocCcDepart;

    /** nullable persistent field */
    private String recvdocPeriodSecrecy;

    /** nullable persistent field */
    private String recvdocEmergency;

    /** nullable persistent field */
    private String recvdocIssuer;

    /** nullable persistent field */
    private String recvdocIssueDepartSigned;

    /** nullable persistent field */
    private String recvdocLegalCode;

    /** nullable persistent field */
    private Date recvdocOfficialTime;

    /** nullable persistent field */
    private Date recvdocRecvTime;

    /** nullable persistent field */
    private byte[] recvdocContent;

    /** nullable persistent field */
    private String recvdocRemark;

    /** nullable persistent field */
    private String recvdocAttachNum;

    /** nullable persistent field */
    private String recvdocKeywords;

    /** nullable persistent field */
    private String recvdocPrintDepart;

    /** nullable persistent field */
    private Date recvdocPrintTime;

    /** nullable persistent field */
    private String recvdocPrintNum;

    /** nullable persistent field */
    private String recvdocAuthor;

    /** nullable persistent field */
    private String recvdocProofReader;

    /** nullable persistent field */
    private String recvdocEntryPeople;

    /** nullable persistent field */
    private String recvdocSealPeople;

    /** nullable persistent field */
    private Date recvdocSealTime;

    /** persistent field */
    private Set toaRecvdocAttaches;

    /** full constructor */
    public ToaRecvdoc(String recvDocId, String recvdocBussinessCode, String recvdocTitle, String recvdocCode, String recvdocSenderDepartCode, String recvdocSecretLvl, String recvdocProcDeadline, String recvdocProcType, String recvdocSubmittoDepart, String recvdocCcDepart, String recvdocPeriodSecrecy, String recvdocEmergency, String recvdocIssuer, String recvdocIssueDepartSigned, String recvdocLegalCode, Date recvdocOfficialTime, Date recvdocRecvTime, byte[] recvdocContent, String recvdocRemark, String recvdocAttachNum, String recvdocKeywords, String recvdocPrintDepart, Date recvdocPrintTime, String recvdocPrintNum, String recvdocAuthor, String recvdocProofReader, String recvdocEntryPeople, String recvdocSealPeople, Date recvdocSealTime, Set toaRecvdocAttaches) {
        this.recvDocId = recvDocId;
        this.recvdocBussinessCode = recvdocBussinessCode;
        this.recvdocTitle = recvdocTitle;
        this.recvdocCode = recvdocCode;
        this.recvdocSenderDepartCode = recvdocSenderDepartCode;
        this.recvdocSecretLvl = recvdocSecretLvl;
        this.recvdocProcDeadline = recvdocProcDeadline;
        this.recvdocProcType = recvdocProcType;
        this.recvdocSubmittoDepart = recvdocSubmittoDepart;
        this.recvdocCcDepart = recvdocCcDepart;
        this.recvdocPeriodSecrecy = recvdocPeriodSecrecy;
        this.recvdocEmergency = recvdocEmergency;
        this.recvdocIssuer = recvdocIssuer;
        this.recvdocIssueDepartSigned = recvdocIssueDepartSigned;
        this.recvdocLegalCode = recvdocLegalCode;
        this.recvdocOfficialTime = recvdocOfficialTime;
        this.recvdocRecvTime = recvdocRecvTime;
        this.recvdocContent = recvdocContent;
        this.recvdocRemark = recvdocRemark;
        this.recvdocAttachNum = recvdocAttachNum;
        this.recvdocKeywords = recvdocKeywords;
        this.recvdocPrintDepart = recvdocPrintDepart;
        this.recvdocPrintTime = recvdocPrintTime;
        this.recvdocPrintNum = recvdocPrintNum;
        this.recvdocAuthor = recvdocAuthor;
        this.recvdocProofReader = recvdocProofReader;
        this.recvdocEntryPeople = recvdocEntryPeople;
        this.recvdocSealPeople = recvdocSealPeople;
        this.recvdocSealTime = recvdocSealTime;
        this.toaRecvdocAttaches = toaRecvdocAttaches;
    }

    /** default constructor */
    public ToaRecvdoc() {
    }

    /** minimal constructor */
    public ToaRecvdoc(String recvDocId, Set toaRecvdocAttaches) {
        this.recvDocId = recvDocId;
        this.toaRecvdocAttaches = toaRecvdocAttaches;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="RECV_DOC_ID"
     *         
     */
    @Id
	@Column(name="RECV_DOC_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRecvDocId() {
        return this.recvDocId;
    }

    public void setRecvDocId(String recvDocId) {
        this.recvDocId = recvDocId;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_BUSSINESS_CODE"
     *             length="32"
     *         
     */
    @Column(name="RECVDOC_BUSSINESS_CODE",nullable=true)
    public String getRecvdocBussinessCode() {
        return this.recvdocBussinessCode;
    }

    public void setRecvdocBussinessCode(String recvdocBussinessCode) {
        this.recvdocBussinessCode = recvdocBussinessCode;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_TITLE"
     *             length="200"
     *         
     */
    @Column(name="RECVDOC_TITLE",nullable=true)
    public String getRecvdocTitle() {
        return this.recvdocTitle;
    }

    public void setRecvdocTitle(String recvdocTitle) {
        this.recvdocTitle = recvdocTitle;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_CODE"
     *             length="50"
     *         
     */
    @Column(name="RECVDOC_CODE",nullable=true)
    public String getRecvdocCode() {
        return this.recvdocCode;
    }

    public void setRecvdocCode(String recvdocCode) {
        this.recvdocCode = recvdocCode;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_SENDER_DEPART_CODE"
     *             length="50"
     *         
     */
    @Column(name="RECVDOC_SENDER_DEPART_CODE",nullable=true)
    public String getRecvdocSenderDepartCode() {
        return this.recvdocSenderDepartCode;
    }

    public void setRecvdocSenderDepartCode(String recvdocSenderDepartCode) {
        this.recvdocSenderDepartCode = recvdocSenderDepartCode;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_SECRET_LVL"
     *             length="10"
     *         
     */
    @Column(name="RECVDOC_SECRET_LVL",nullable=true)
    public String getRecvdocSecretLvl() {
        return this.recvdocSecretLvl;
    }

    public void setRecvdocSecretLvl(String recvdocSecretLvl) {
        this.recvdocSecretLvl = recvdocSecretLvl;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PROC_DEADLINE"
     *             length="1"
     *         
     */
    @Column(name="RECVDOC_PROC_DEADLINE",nullable=true)
    public String getRecvdocProcDeadline() {
        return this.recvdocProcDeadline;
    }

    public void setRecvdocProcDeadline(String recvdocProcDeadline) {
        this.recvdocProcDeadline = recvdocProcDeadline;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PROC_TYPE"
     *             length="1"
     *         
     */
    @Column(name="RECVDOC_PROC_TYPE",nullable=true)
    public String getRecvdocProcType() {
        return this.recvdocProcType;
    }

    public void setRecvdocProcType(String recvdocProcType) {
        this.recvdocProcType = recvdocProcType;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_SUBMITTO_DEPART"
     *             length="50"
     *         
     */
    @Column(name="RECVDOC_SUBMITTO_DEPART",nullable=true)
    public String getRecvdocSubmittoDepart() {
        return this.recvdocSubmittoDepart;
    }

    public void setRecvdocSubmittoDepart(String recvdocSubmittoDepart) {
        this.recvdocSubmittoDepart = recvdocSubmittoDepart;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_CC_DEPART"
     *             length="50"
     *         
     */
    @Column(name="RECVDOC_CC_DEPART",nullable=true)
    public String getRecvdocCcDepart() {
        return this.recvdocCcDepart;
    }

    public void setRecvdocCcDepart(String recvdocCcDepart) {
        this.recvdocCcDepart = recvdocCcDepart;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PERIOD_SECRECY"
     *             length="10"
     *         
     */
    @Column(name="RECVDOC_PERIOD_SECRECY",nullable=true)
    public String getRecvdocPeriodSecrecy() {
        return this.recvdocPeriodSecrecy;
    }

    public void setRecvdocPeriodSecrecy(String recvdocPeriodSecrecy) {
        this.recvdocPeriodSecrecy = recvdocPeriodSecrecy;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_EMERGENCY"
     *             length="10"
     *         
     */
    @Column(name="RECVDOC_EMERGENCY",nullable=true)
    public String getRecvdocEmergency() {
        return this.recvdocEmergency;
    }

    public void setRecvdocEmergency(String recvdocEmergency) {
        this.recvdocEmergency = recvdocEmergency;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_ISSUER"
     *             length="20"
     *         
     */
    @Column(name="RECVDOC_ISSUER",nullable=true)
    public String getRecvdocIssuer() {
        return this.recvdocIssuer;
    }

    public void setRecvdocIssuer(String recvdocIssuer) {
        this.recvdocIssuer = recvdocIssuer;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_ISSUE_DEPART_SIGNED"
     *             length="50"
     *         
     */
    @Column(name="RECVDOC_ISSUE_DEPART_SIGNED",nullable=true)
    public String getRecvdocIssueDepartSigned() {
        return this.recvdocIssueDepartSigned;
    }

    public void setRecvdocIssueDepartSigned(String recvdocIssueDepartSigned) {
        this.recvdocIssueDepartSigned = recvdocIssueDepartSigned;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_LEGAL_CODE"
     *             length="100"
     *         
     */
    @Column(name="RECVDOC_LEGAL_CODE",nullable=true)
    public String getRecvdocLegalCode() {
        return this.recvdocLegalCode;
    }

    public void setRecvdocLegalCode(String recvdocLegalCode) {
        this.recvdocLegalCode = recvdocLegalCode;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_OFFICIAL_TIME"
     *             length="7"
     *         
     */
    @Column(name="RECVDOC_OFFICIAL_TIME",nullable=true)
    public Date getRecvdocOfficialTime() {
        return this.recvdocOfficialTime;
    }

    public void setRecvdocOfficialTime(Date recvdocOfficialTime) {
        this.recvdocOfficialTime = recvdocOfficialTime;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_RECV_TIME"
     *             length="7"
     *         
     */
    @Column(name="RECVDOC_RECV_TIME",nullable=true)
    public Date getRecvdocRecvTime() {
        return this.recvdocRecvTime;
    }

    public void setRecvdocRecvTime(Date recvdocRecvTime) {
        this.recvdocRecvTime = recvdocRecvTime;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC__CONTENT"
     *             length="4000"
     *         
     */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "RECVDOC_CONTENT", columnDefinition = "BLOB",nullable=true)
    public byte[] getRecvdocContent() {
        return this.recvdocContent;
    }

    public void setRecvdocContent(byte[] recvdocContent) {
        this.recvdocContent = recvdocContent;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_REMARK"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="RECVDOC_REMARK", columnDefinition="CLOB", nullable=true) 
    public String getRecvdocRemark() {
        return this.recvdocRemark;
    }

    public void setRecvdocRemark(String recvdocRemark) {
        this.recvdocRemark = recvdocRemark;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_ATTACH_NUM"
     *             length="4"
     *         
     */
    @Column(name="RECVDOC_ATTACH_NUM",nullable=true)
    public String getRecvdocAttachNum() {
        return this.recvdocAttachNum;
    }

    public void setRecvdocAttachNum(String recvdocAttachNum) {
        this.recvdocAttachNum = recvdocAttachNum;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_KEYWORDS"
     *             length="100"
     *         
     */
    @Column(name="RECVDOC_KEYWORDS",nullable=true)
    public String getRecvdocKeywords() {
        return this.recvdocKeywords;
    }

    public void setRecvdocKeywords(String recvdocKeywords) {
        this.recvdocKeywords = recvdocKeywords;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PRINT_DEPART"
     *             length="50"
     *         
     */
    @Column(name="RECVDOC_PRINT_DEPART",nullable=true)
    public String getRecvdocPrintDepart() {
        return this.recvdocPrintDepart;
    }

    public void setRecvdocPrintDepart(String recvdocPrintDepart) {
        this.recvdocPrintDepart = recvdocPrintDepart;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PRINT_TIME"
     *             length="7"
     *         
     */
    @Column(name="RECVDOC_PRINT_TIME",nullable=true)
    public Date getRecvdocPrintTime() {
        return this.recvdocPrintTime;
    }

    public void setRecvdocPrintTime(Date recvdocPrintTime) {
        this.recvdocPrintTime = recvdocPrintTime;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PRINT_NUM"
     *             length="4"
     *         
     */
    @Column(name="RECVDOC_PRINT_NUM",nullable=true)
    public String getRecvdocPrintNum() {
        return this.recvdocPrintNum;
    }

    public void setRecvdocPrintNum(String recvdocPrintNum) {
        this.recvdocPrintNum = recvdocPrintNum;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_AUTHOR"
     *             length="20"
     *         
     */
    @Column(name="RECVDOC_AUTHOR",nullable=true)
    public String getRecvdocAuthor() {
        return this.recvdocAuthor;
    }

    public void setRecvdocAuthor(String recvdocAuthor) {
        this.recvdocAuthor = recvdocAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_PROOF_READER"
     *             length="20"
     *         
     */
    @Column(name="RECVDOC_PROOF_READER",nullable=true)
    public String getRecvdocProofReader() {
        return this.recvdocProofReader;
    }

    public void setRecvdocProofReader(String recvdocProofReader) {
        this.recvdocProofReader = recvdocProofReader;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_ENTRY_PEOPLE"
     *             length="20"
     *         
     */
    @Column(name="RECVDOC_ENTRY_PEOPLE",nullable=true)
    public String getRecvdocEntryPeople() {
        return this.recvdocEntryPeople;
    }

    public void setRecvdocEntryPeople(String recvdocEntryPeople) {
        this.recvdocEntryPeople = recvdocEntryPeople;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_SEAL_PEOPLE"
     *             length="20"
     *         
     */
    @Column(name="RECVDOC_SEAL_PEOPLE",nullable=true)
    public String getRecvdocSealPeople() {
        return this.recvdocSealPeople;
    }

    public void setRecvdocSealPeople(String recvdocSealPeople) {
        this.recvdocSealPeople = recvdocSealPeople;
    }

    /** 
     *            @hibernate.property
     *             column="RECVDOC_SEAL_TIME"
     *             length="7"
     *         
     */
    @Column(name="RECVDOC_SEAL_TIME",nullable=true)
    public Date getRecvdocSealTime() {
        return this.recvdocSealTime;
    }

    public void setRecvdocSealTime(Date recvdocSealTime) {
        this.recvdocSealTime = recvdocSealTime;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="RECV_DOC_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaRecvdocAttach"
     *         
     */
    @OneToMany(mappedBy="toaRecvdoc",targetEntity=com.strongit.oa.bo.ToaRecvdocAttach.class,cascade=CascadeType.ALL)
    public Set getToaRecvdocAttaches() {
        return this.toaRecvdocAttaches;
    }

    public void setToaRecvdocAttaches(Set toaRecvdocAttaches) {
        this.toaRecvdocAttaches = toaRecvdocAttaches;
    }
    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("recvDocId", getRecvDocId())
            .toString();
    }

}
