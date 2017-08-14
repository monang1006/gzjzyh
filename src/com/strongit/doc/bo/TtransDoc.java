package com.strongit.doc.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;



/** 
 *        @hibernate.class
 *         table="T_TRANS_DOC"
 *     
*/
@Entity
@Table(name = "T_TRANS_DOC", catalog = "", schema = "")
public class TtransDoc implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4329664934285419981L;

	/** identifier field */
    private String docId;

    /** nullable persistent field */
    private String docCode;

    /** nullable persistent field */
    private String docTitle;

    /** nullable persistent field */
    private String docIssueDepartSigned;

    /** nullable persistent field */
    private String docKeywords;

    /** nullable persistent field */
    private String docClass;

    /** nullable persistent field */
    private String docSecretLvl;

    /** nullable persistent field */
    private String docEmergency;

    /** nullable persistent field */
    private String docSubmittoDepart;
    
    /** nullable persistent field 主送单位id  ---  实际是主送单位的orgsyscode*/
    private String docSubmittoDepart_id;

    /** nullable persistent field */
    private String docCcDepart;
    
    /** nullable persistent field 抄送单位id  ---  实际是抄送单位的orgsyscode*/
    private String docCcDepart_id;

    /** nullable persistent field */
    private String docIssuer;

    /** nullable persistent field */
    private String docCountersigner;

    /** nullable persistent field */
    private Date docOfficialTime;

    /** nullable persistent field */
    private String docHavePrintSum;

    /** nullable persistent field */
    private String ddocContentName;

    /** nullable persistent field */
    private byte[] docContent;

    /** nullable persistent field */
    private String docContentSum;

    /** nullable persistent field */
    private String docAttachNum;

    /** nullable persistent field */
    private String docSealPeople;

    /** nullable persistent field */
    private Date docSealTime;

    /** nullable persistent field */
    private String docSealIs;

    /** nullable persistent field */
    private String docState;

    /** nullable persistent field */
    private Date docSendTime;

    /** nullable persistent field */
    private String docUser;

    /** nullable persistent field */
    private Date docEntryTime;

    /** nullable persistent field */
    private String ddocEntryPeople;

    /** nullable persistent field */
    private String docRemark;

    /** nullable persistent field */
    private String isdelete;

    /** nullable persistent field */
    private String ddocProcDeadline;

    /** nullable persistent field */
    private String ddocPeriodSecrecy;

    /** nullable persistent field */
    private String ddocPrintDepart;

    /** nullable persistent field */
    private Date ddocPrintTime;

    /** nullable persistent field */
    private String ddocPrintNum;

    /** nullable persistent field */
    private String ddocAuthor;

    /** nullable persistent field */
    private String ddocProofReader;

    /** nullable persistent field */
    private String docIsunite;

    /** nullable persistent field */
    private String docUniteDepart;

    /** nullable persistent field */
    private String rest1;				//用于存储印章是否锁定了正文

    /** nullable persistent field */
    private String rest2;				//用于存放退回意见

    /** nullable persistent field */
    private String rest3;				//用于存放机构ID

    /** nullable persistent field */
    private String rest4;

    /** nullable persistent field */
    private String rest5;

    /** nullable persistent field */
    private String rest6;

    /** nullable persistent field */
    private String rest7;

    /** nullable persistent field */
    private String rest8;

    /** nullable persistent field */
    private String rest9;

    /** nullable persistent field */
    private String rest10;
    
    /** nullable persistent field  印发时间*/
    private Date docprintSend;

    /** persistent field */
    private Set ttransDocAttaches;

    /** persistent field */
    private Set tdocSends;
    
    private String deledAttachId ;//要删除的附件id集合,多个以逗号隔开；
    
    private Object obj ;		//此属性只用于传递参数,无其他含义

    /** full constructor */
    public TtransDoc(String docId, String docCode, String docTitle, String docIssueDepartSigned, String docKeywords, String docClass, String docSecretLvl, String docEmergency, String docSubmittoDepart, String docSubmittoDepart_id, String docCcDepart, String docCcDepart_id, String docIssuer, String docCountersigner, Date docOfficialTime, String docHavePrintSum, String ddocContentName, byte[] docContent, String docContentSum, String docAttachNum, String docSealPeople, Date docSealTime, String docSealIs, String docState, Date docSendTime, String docUser, Date docEntryTime, String ddocEntryPeople, String docRemark, String isdelete, String ddocProcDeadline, String ddocPeriodSecrecy, String ddocPrintDepart, Date ddocPrintTime, String ddocPrintNum, String ddocAuthor, String ddocProofReader, String docIsunite, String docUniteDepart, String rest1, String rest2, String rest3, String rest4, String rest5, String rest6, String rest7, String rest8, String rest9, String rest10, Date docprintSend, Set ttransDocAttaches, Set tdocSends) {
        this.docId = docId;
        this.docCode = docCode;
        this.docTitle = docTitle;
        this.docIssueDepartSigned = docIssueDepartSigned;
        this.docKeywords = docKeywords;
        this.docClass = docClass;
        this.docSecretLvl = docSecretLvl;
        this.docEmergency = docEmergency;
        this.docSubmittoDepart = docSubmittoDepart;
        this.docSubmittoDepart_id = docSubmittoDepart_id;
        this.docCcDepart = docCcDepart;
        this.docCcDepart_id = docCcDepart_id;
        this.docIssuer = docIssuer;
        this.docCountersigner = docCountersigner;
        this.docOfficialTime = docOfficialTime;
        this.docHavePrintSum = docHavePrintSum;
        this.ddocContentName = ddocContentName;
        this.docContent = docContent;
        this.docContentSum = docContentSum;
        this.docAttachNum = docAttachNum;
        this.docSealPeople = docSealPeople;
        this.docSealTime = docSealTime;
        this.docSealIs = docSealIs;
        this.docState = docState;
        this.docSendTime = docSendTime;
        this.docUser = docUser;
        this.docEntryTime = docEntryTime;
        this.ddocEntryPeople = ddocEntryPeople;
        this.docRemark = docRemark;
        this.isdelete = isdelete;
        this.ddocProcDeadline = ddocProcDeadline;
        this.ddocPeriodSecrecy = ddocPeriodSecrecy;
        this.ddocPrintDepart = ddocPrintDepart;
        this.ddocPrintTime = ddocPrintTime;
        this.ddocPrintNum = ddocPrintNum;
        this.ddocAuthor = ddocAuthor;
        this.ddocProofReader = ddocProofReader;
        this.docIsunite = docIsunite;
        this.docUniteDepart = docUniteDepart;
        this.rest1 = rest1;
        this.rest2 = rest2;
        this.rest3 = rest3;
        this.rest4 = rest4;
        this.rest5 = rest5;
        this.rest6 = rest6;
        this.rest7 = rest7;
        this.rest8 = rest8;
        this.rest9 = rest9;
        this.rest10 = rest10;
        this.docprintSend = docprintSend;
        this.ttransDocAttaches = ttransDocAttaches;
        this.tdocSends = tdocSends;
    }

    /** default constructor */
    public TtransDoc() {
    }

    /** minimal constructor */
    public TtransDoc(String docId, Set ttransDocAttaches, Set tdocSends) {
        this.docId = docId;
        this.ttransDocAttaches = ttransDocAttaches;
        this.tdocSends = tdocSends;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOC_ID"
     *         
     */
    @Id
	@Column(name = "DOC_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDocId() {
        return this.docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_CODE"
     *             length="50"
     *         
     */
    @Column(name = "DOC_CODE")
    public String getDocCode() {
        return this.docCode;
    }

    public void setDocCode(String docCode) {
        this.docCode = docCode;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_TITLE"
     *             length="500"
     *         
     */
    @Column(name = "DOC_TITLE")
    public String getDocTitle() {
        return this.docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_ISSUE_DEPART_SIGNED"
     *             length="50"
     *         
     */
    @Column(name = "DOC_ISSUE_DEPART_SIGNED")
    public String getDocIssueDepartSigned() {
        return this.docIssueDepartSigned;
    }

    public void setDocIssueDepartSigned(String docIssueDepartSigned) {
        this.docIssueDepartSigned = docIssueDepartSigned;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_KEYWORDS"
     *             length="100"
     *         
     */
    @Column(name = "DOC_KEYWORDS")
    public String getDocKeywords() {
        return this.docKeywords;
    }

    public void setDocKeywords(String docKeywords) {
        this.docKeywords = docKeywords;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_CLASS"
     *             length="1"
     *         
     */
    @Column(name = "DOC_CLASS")
    public String getDocClass() {
        return this.docClass;
    }

    public void setDocClass(String docClass) {
        this.docClass = docClass;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SECRET_LVL"
     *             length="10"
     *         
     */
    @Column(name = "DOC_SECRET_LVL")
    public String getDocSecretLvl() {
        return this.docSecretLvl;
    }

    public void setDocSecretLvl(String docSecretLvl) {
        this.docSecretLvl = docSecretLvl;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_EMERGENCY"
     *             length="10"
     *         
     */
    @Column(name = "DOC_EMERGENCY")
    public String getDocEmergency() {
        return this.docEmergency;
    }

    public void setDocEmergency(String docEmergency) {
        this.docEmergency = docEmergency;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SUBMITTO_DEPART"
     *             length="1000"
     *         
     */
    @Column(name = "DOC_SUBMITTO_DEPART")
    public String getDocSubmittoDepart() {
        return this.docSubmittoDepart;
    }

    public void setDocSubmittoDepart(String docSubmittoDepart) {
        this.docSubmittoDepart = docSubmittoDepart;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SUBMITTO_DEPART_ID"
     *             length="4000"
     *         
     */
    @Column(name = "DOC_SUBMITTO_DEPART_ID")
    public String getDocSubmittoDepart_id() {
        return this.docSubmittoDepart_id;
    }

    public void setDocSubmittoDepart_id(String docSubmittoDepart_id) {
        this.docSubmittoDepart_id = docSubmittoDepart_id;
    }
    
    /** 
     *            @hibernate.property
     *             column="DOC_CC_DEPART"
     *             length="1000"
     *         
     */
    @Column(name = "DOC_CC_DEPART")
    public String getDocCcDepart() {
        return this.docCcDepart;
    }

    public void setDocCcDepart(String docCcDepart) {
        this.docCcDepart = docCcDepart;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_CC_DEPART_ID"
     *             length="4000"
     *         
     */
    @Column(name = "DOC_CC_DEPART_ID")
    public String getDocCcDepart_id() {
        return this.docCcDepart_id;
    }

    public void setDocCcDepart_id(String docCcDepart_id) {
        this.docCcDepart_id = docCcDepart_id;
    }
    
    /** 
     *            @hibernate.property
     *             column="DOC_ISSUER"
     *             length="200"
     *         
     */
    @Column(name = "DOC_ISSUER")
    public String getDocIssuer() {
        return this.docIssuer;
    }

    public void setDocIssuer(String docIssuer) {
        this.docIssuer = docIssuer;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_COUNTERSIGNER"
     *             length="200"
     *         
     */
    @Column(name = "DOC_COUNTERSIGNER")
    public String getDocCountersigner() {
        return this.docCountersigner;
    }

    public void setDocCountersigner(String docCountersigner) {
        this.docCountersigner = docCountersigner;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_OFFICIAL_TIME"
     *             length="7"
     *         
     */
    @Column(name = "DOC_OFFICIAL_TIME")
    public Date getDocOfficialTime() {
        return this.docOfficialTime;
    }

    public void setDocOfficialTime(Date docOfficialTime) {
        this.docOfficialTime = docOfficialTime;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_HAVE_PRINT_SUM"
     *             length="4"
     *         
     */
    @Column(name = "DOC_HAVE_PRINT_SUM")
    public String getDocHavePrintSum() {
        return this.docHavePrintSum;
    }

    public void setDocHavePrintSum(String docHavePrintSum) {
        this.docHavePrintSum = docHavePrintSum;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_CONTENT_NAME"
     *             length="25"
     *         
     */
    @Column(name = "DDOC_CONTENT_NAME")
    public String getDdocContentName() {
        return this.ddocContentName;
    }

    public void setDdocContentName(String ddocContentName) {
        this.ddocContentName = ddocContentName;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name = "DOC_CONTENT")
    public byte[] getDocContent() {
        return this.docContent;
    }

    public void setDocContent(byte[] docContent) {
        this.docContent = docContent;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_CONTENT_SUM"
     *             length="4"
     *         
     */
    @Column(name = "DOC_CONTENT_SUM")
    public String getDocContentSum() {
        return this.docContentSum;
    }

    public void setDocContentSum(String docContentSum) {
        this.docContentSum = docContentSum;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_ATTACH_NUM"
     *             length="4"
     *         
     */
    @Column(name = "DOC_ATTACH_NUM")
    public String getDocAttachNum() {
        return this.docAttachNum;
    }

    public void setDocAttachNum(String docAttachNum) {
        this.docAttachNum = docAttachNum;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SEAL_PEOPLE"
     *             length="200"
     *         
     */
    @Column(name = "DOC_SEAL_PEOPLE")
    public String getDocSealPeople() {
        return this.docSealPeople;
    }

    public void setDocSealPeople(String docSealPeople) {
        this.docSealPeople = docSealPeople;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SEAL_TIME"
     *             length="7"
     *         
     */
    @Column(name = "DOC_SEAL_TIME")
    public Date getDocSealTime() {
        return this.docSealTime;
    }

    public void setDocSealTime(Date docSealTime) {
        this.docSealTime = docSealTime;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SEAL_IS"
     *             length="1"
     *         
     */
    @Column(name = "DOC_SEAL_IS")
    public String getDocSealIs() {
        return this.docSealIs;
    }

    public void setDocSealIs(String docSealIs) {
        this.docSealIs = docSealIs;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_STATE"
     *             length="1"
     *         
     */
    @Column(name = "DOC_STATE")
    public String getDocState() {
        return this.docState;
    }

    public void setDocState(String docState) {
        this.docState = docState;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_SEND_TIME"
     *             length="7"
     *         
     */
    @Column(name = "DOC_SEND_TIME")
    public Date getDocSendTime() {
        return this.docSendTime;
    }

    public void setDocSendTime(Date docSendTime) {
        this.docSendTime = docSendTime;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_USER"
     *             length="200"
     *         
     */
    @Column(name = "DOC_USER")
    public String getDocUser() {
        return this.docUser;
    }

    public void setDocUser(String docUser) {
        this.docUser = docUser;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_ENTRY_TIME"
     *             length="7"
     *         
     */
    @Column(name = "DOC_ENTRY_TIME")
    public Date getDocEntryTime() {
        return this.docEntryTime;
    }

    public void setDocEntryTime(Date docEntryTime) {
        this.docEntryTime = docEntryTime;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_ENTRY_PEOPLE"
     *             length="200"
     *         
     */
    @Column(name = "DDOC_ENTRY_PEOPLE")
    public String getDdocEntryPeople() {
        return this.ddocEntryPeople;
    }

    public void setDdocEntryPeople(String ddocEntryPeople) {
        this.ddocEntryPeople = ddocEntryPeople;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_REMARK"
     *             length="4000"
     *         
     */
    @Column(name = "DOC_REMARK")
    public String getDocRemark() {
        return this.docRemark;
    }

    public void setDocRemark(String docRemark) {
        this.docRemark = docRemark;
    }

    /** 
     *            @hibernate.property
     *             column="ISDELETE"
     *             length="1"
     *         
     */
    @Column(name = "ISDELETE")
    public String getIsdelete() {
        return this.isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_PROC_DEADLINE"
     *             length="1"
     *         
     */
    @Column(name = "DDOC_PROC_DEADLINE")
    public String getDdocProcDeadline() {
        return this.ddocProcDeadline;
    }

    public void setDdocProcDeadline(String ddocProcDeadline) {
        this.ddocProcDeadline = ddocProcDeadline;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_PERIOD_SECRECY"
     *             length="10"
     *         
     */
    @Column(name = "DDOC_PERIOD_SECRECY")
    public String getDdocPeriodSecrecy() {
        return this.ddocPeriodSecrecy;
    }

    public void setDdocPeriodSecrecy(String ddocPeriodSecrecy) {
        this.ddocPeriodSecrecy = ddocPeriodSecrecy;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_PRINT_DEPART"
     *             length="50"
     *         
     */
    @Column(name = "DDOC_PRINT_DEPART")
    public String getDdocPrintDepart() {
        return this.ddocPrintDepart;
    }

    public void setDdocPrintDepart(String ddocPrintDepart) {
        this.ddocPrintDepart = ddocPrintDepart;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_PRINT_TIME"
     *             length="7"
     *         
     */
    @Column(name = "DDOC_PRINT_TIME")
    public Date getDdocPrintTime() {
        return this.ddocPrintTime;
    }

    public void setDdocPrintTime(Date ddocPrintTime) {
        this.ddocPrintTime = ddocPrintTime;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_PRINT_NUM"
     *             length="4"
     *         
     */
    @Column(name = "DDOC_PRINT_NUM")
    public String getDdocPrintNum() {
        return this.ddocPrintNum;
    }

    public void setDdocPrintNum(String ddocPrintNum) {
        this.ddocPrintNum = ddocPrintNum;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_AUTHOR"
     *             length="200"
     *         
     */
    @Column(name = "DDOC_AUTHOR")
    public String getDdocAuthor() {
        return this.ddocAuthor;
    }

    public void setDdocAuthor(String ddocAuthor) {
        this.ddocAuthor = ddocAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="DDOC_PROOF_READER"
     *             length="200"
     *         
     */
    @Column(name = "DDOC_PROOF_READER")
    public String getDdocProofReader() {
        return this.ddocProofReader;
    }

    public void setDdocProofReader(String ddocProofReader) {
        this.ddocProofReader = ddocProofReader;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_ISUNITE"
     *             length="1"
     *         
     */
    @Column(name = "DOC_ISUNITE")
    public String getDocIsunite() {
        return this.docIsunite;
    }

    public void setDocIsunite(String docIsunite) {
        this.docIsunite = docIsunite;
    }

    /** 
     *            @hibernate.property
     *             column="DOC_UNITE_DEPART"
     *             length="1000"
     *         
     */
    @Column(name = "DOC_UNITE_DEPART")
    public String getDocUniteDepart() {
        return this.docUniteDepart;
    }

    public void setDocUniteDepart(String docUniteDepart) {
        this.docUniteDepart = docUniteDepart;
    }

    /** 
     *            @hibernate.property
     *             column="REST1"
     *             length="1000"
     *         
     */
    @Column(name = "REST1")
    public String getRest1() {
        return this.rest1;
    }

    public void setRest1(String rest1) {
        this.rest1 = rest1;
    }

    /** 
     *            @hibernate.property
     *             column="REST2"
     *             length="1000"
     *         
     */
    @Column(name = "REST2")
    public String getRest2() {
        return this.rest2;
    }

    public void setRest2(String rest2) {
        this.rest2 = rest2;
    }

    /** 
     *            @hibernate.property
     *             column="REST3"
     *             length="1000"
     *         
     */
    @Column(name = "REST3")
    public String getRest3() {
        return this.rest3;
    }

    public void setRest3(String rest3) {
        this.rest3 = rest3;
    }

    /** 
     *            @hibernate.property
     *             column="REST4"
     *             length="1000"
     *         
     */
    @Column(name = "REST4")
    public String getRest4() {
        return this.rest4;
    }

    public void setRest4(String rest4) {
        this.rest4 = rest4;
    }

    /** 
     *            @hibernate.property
     *             column="REST5"
     *             length="1000"
     *         
     */
    @Column(name = "REST5")
    public String getRest5() {
        return this.rest5;
    }

    public void setRest5(String rest5) {
        this.rest5 = rest5;
    }

    /** 
     *            @hibernate.property
     *             column="REST6"
     *             length="1000"
     *         
     */
    @Column(name = "REST6")
    public String getRest6() {
        return this.rest6;
    }

    public void setRest6(String rest6) {
        this.rest6 = rest6;
    }

    /** 
     *            @hibernate.property
     *             column="REST7"
     *             length="1000"
     *         
     */
    @Column(name = "REST7")
    public String getRest7() {
        return this.rest7;
    }

    public void setRest7(String rest7) {
        this.rest7 = rest7;
    }

    /** 
     *            @hibernate.property
     *             column="REST8"
     *             length="1000"
     *         
     */
    @Column(name = "REST8")
    public String getRest8() {
        return this.rest8;
    }

    public void setRest8(String rest8) {
        this.rest8 = rest8;
    }

    /** 
     *            @hibernate.property
     *             column="REST9"
     *             length="1000"
     *         
     */
    @Column(name = "REST9")
    public String getRest9() {
        return this.rest9;
    }

    public void setRest9(String rest9) {
        this.rest9 = rest9;
    }

    /** 
     *            @hibernate.property
     *             column="REST10"
     *             length="1000"
     *         
     */
    @Column(name = "REST10")
    public String getRest10() {
        return this.rest10;
    }

    public void setRest10(String rest10) {
        this.rest10 = rest10;
    }

    /** 
     * 			 印发时间
     *            @hibernate.property
     *             column="DOC_PRINT_SEND"
     *         
     */
    @Column(name = "DOC_PRINT_SEND")
    public Date getDocprintSend() {
        return this.docprintSend;
    }

    public void setDocprintSend(Date docprintSend) {
        this.docprintSend = docprintSend;
    }
    
    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DOC_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.personal.retirement.bo.TtransDocAttach"
     *         
     */
    @OneToMany(mappedBy="ttransDoc",fetch=FetchType.LAZY,targetEntity=TtransDocAttach.class)
    public Set getTtransDocAttaches() {
        return this.ttransDocAttaches;
    }

    public void setTtransDocAttaches(Set ttransDocAttaches) {
        this.ttransDocAttaches = ttransDocAttaches;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="DOC_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.personal.retirement.bo.TdocSend"
     *         
     */
    @OneToMany(mappedBy="ttransDoc",fetch=FetchType.LAZY,cascade=CascadeType.ALL,targetEntity=TdocSend.class)
    public Set getTdocSends() {
        return this.tdocSends;
    }

    public void setTdocSends(Set tdocSends) {
        this.tdocSends = tdocSends;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docId", getDocId())
            .toString();
    }

    @Transient
	public String getDeledAttachId() {
		return deledAttachId;
	}

	public void setDeledAttachId(String deledAttachId) {
		this.deledAttachId = deledAttachId;
	}

	@Transient
	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
