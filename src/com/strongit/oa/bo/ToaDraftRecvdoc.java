package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

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
 *         table="T_OARECVDOC"
 *     
*/
@Entity
@Table(name="T_OARECVDOC",catalog="",schema="")
public class ToaDraftRecvdoc implements Serializable {

	private static final long serialVersionUID = -5349617800378452809L;

	/** identifier field */
	@Id
	@GeneratedValue
    private String oarecvdocid;

    /** identifier field */
    private String personConfigFlag;

    /** identifier field */
    private Date personOperateDate;

    /** identifier field */
    private String personOperater;

    /** identifier field */
    private byte[] personDemo;

    /** identifier field */
    private String personFilename;

    /** identifier field */
    private String recvNum;

    /** identifier field */
    private String docTitle;

    /** identifier field */
    private String issueDepartSigned;

    /** identifier field */
    private String docNumber;

    /** identifier field */
    private Date recvTime;

    /** identifier field */
    private String secretLvl;

    /** identifier field */
    private String emergency;

    /** identifier field */
    private String keywords;

    /** identifier field */
    private byte[] content;

    /** identifier field */
    private String printTime;

    /** identifier field */
    private String entryPeople;

    /** identifier field */
    private String draftSuggestion;

    /** identifier field */
    private String officeDirectorSuggestion;

    /** identifier field */
    private String branchSuggestion;

    /** identifier field */
    private String leaderSuggestion;

    /** identifier field */
    private String processResult;

    /** identifier field */
    private String recvState;

    /** identifier field */
    private Object diposalBill;

    /** identifier field */
    private String approvalRecvTime;

    /** identifier field */
    private String recvFormid;

    /** identifier field */
    private String contentName;
    
    private String pageCount;
    
    private String workflowName;
    
    private String workflowState;
    
    private String workflowAuthor;
    
    private String workflowTitle;
    
    private String workflowCode;

	/** full constructor */
    public ToaDraftRecvdoc(String oarecvdocid, String personConfigFlag, Date personOperateDate, String personOperater, byte[] personDemo, String personFilename, String recvNum, String docTitle, String issueDepartSigned, String docNumber, Date recvTime, String secretLvl, String emergency, String keywords, byte[] content, String printTime, String entryPeople, String draftSuggestion, String officeDirectorSuggestion, String branchSuggestion, String leaderSuggestion, String processResult, String recvState, Object diposalBill, String approvalRecvTime, String recvFormid, String contentName) {
        this.oarecvdocid = oarecvdocid;
        this.personConfigFlag = personConfigFlag;
        this.personOperateDate = personOperateDate;
        this.personOperater = personOperater;
        this.personDemo = personDemo;
        this.personFilename = personFilename;
        this.recvNum = recvNum;
        this.docTitle = docTitle;
        this.issueDepartSigned = issueDepartSigned;
        this.docNumber = docNumber;
        this.recvTime = recvTime;
        this.secretLvl = secretLvl;
        this.emergency = emergency;
        this.keywords = keywords;
        this.content = content;
        this.printTime = printTime;
        this.entryPeople = entryPeople;
        this.draftSuggestion = draftSuggestion;
        this.officeDirectorSuggestion = officeDirectorSuggestion;
        this.branchSuggestion = branchSuggestion;
        this.leaderSuggestion = leaderSuggestion;
        this.processResult = processResult;
        this.recvState = recvState;
        this.diposalBill = diposalBill;
        this.approvalRecvTime = approvalRecvTime;
        this.recvFormid = recvFormid;
        this.contentName = contentName;
    }

    /** default constructor */
    public ToaDraftRecvdoc() {
    }

    /** 
     *                @hibernate.property
     *                 column="OARECVDOCID"
     *                 length="32"
     *             
     */
	@Id
	@Column(name="OARECVDOCID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getOarecvdocid() {
        return this.oarecvdocid;
    }

    public void setOarecvdocid(String oarecvdocid) {
        this.oarecvdocid = oarecvdocid;
    }

    /** 
     *                @hibernate.property
     *                 column="PERSON_CONFIG_FLAG"
     *                 length="1"
     *             
     */
    @Column(name="PERSON_CONFIG_FLAG",length=1)
    public String getPersonConfigFlag() {
        return this.personConfigFlag;
    }

    public void setPersonConfigFlag(String personConfigFlag) {
        this.personConfigFlag = personConfigFlag;
    }

    /** 
     *                @hibernate.property
     *                 column="PERSON_OPERATE_DATE"
     *                 length="7"
     *             
     */
    @Column(name="PERSON_OPERATE_DATE",length=7)
    public Date getPersonOperateDate() {
        return this.personOperateDate;
    }

    public void setPersonOperateDate(Date personOperateDate) {
        this.personOperateDate = personOperateDate;
    }

    /** 
     *                @hibernate.property
     *                 column="PERSON_OPERATER"
     *                 length="20"
     *             
     */
    @Column(name="PERSON_OPERATER",length=20)
    public String getPersonOperater() {
        return this.personOperater;
    }

    public void setPersonOperater(String personOperater) {
        this.personOperater = personOperater;
    }

    /** 
     *                @hibernate.property
     *                 column="PERSON_DEMO"
     *                 length="4000"
     *             
     */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "PERSON_DEMO", columnDefinition = "BLOB")
    public byte[] getPersonDemo() {
        return this.personDemo;
    }

    public void setPersonDemo(byte[] personDemo) {
        this.personDemo = personDemo;
    }

    /** 
     *                @hibernate.property
     *                 column="PERSON_FILENAME"
     *                 length="255"
     *             
     */
    /*@Column(name="PERSON_FILENAME",length=25)
    public String getPersonFilename() {
        return this.personFilename;
    }*/

    public void setPersonFilename(String personFilename) {
        this.personFilename = personFilename;
    }

    /** 
     *                @hibernate.property
     *                 column="RECV_NUM"
     *                 length="64"
     *             
     */
    @Column(name="RECV_NUM",length=64)
    public String getRecvNum() {
        return this.recvNum;
    }

    public void setRecvNum(String recvNum) {
        this.recvNum = recvNum;
    }

    /** 
     *                @hibernate.property
     *                 column="DOC_TITLE"
     *                 length="400"
     *             
     */
    @Column(name="DOC_TITLE",length=400)
    public String getDocTitle() {
        return this.docTitle;
    }

    public void setDocTitle(String docTitle) {
        this.docTitle = docTitle;
    }

    /** 
     *                @hibernate.property
     *                 column="ISSUE_DEPART_SIGNED"
     *                 length="100"
     *             
     */
    @Column(name="ISSUE_DEPART_SIGNED",length=100)
    public String getIssueDepartSigned() {
        return this.issueDepartSigned;
    }

    public void setIssueDepartSigned(String issueDepartSigned) {
        this.issueDepartSigned = issueDepartSigned;
    }

    /** 
     *                @hibernate.property
     *                 column="DOC_NUMBER"
     *                 length="64"
     *             
     */
    @Column(name="DOC_NUMBER",length=64)
    public String getDocNumber() {
        return this.docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    /** 
     *                @hibernate.property
     *                 column="RECV_TIME"
     *                 length="7"
     *             
     */
    @Column(name="RECV_TIME",length=7)
    public Date getRecvTime() {
        return this.recvTime;
    }

    public void setRecvTime(Date recvTime) {
        this.recvTime = recvTime;
    }

    /** 
     *                @hibernate.property
     *                 column="SECRET_LVL"
     *                 length="20"
     *             
     */
    @Column(name="SECRET_LVL",length=20)
    public String getSecretLvl() {
        return this.secretLvl;
    }

    public void setSecretLvl(String secretLvl) {
        this.secretLvl = secretLvl;
    }

    /** 
     *                @hibernate.property
     *                 column="EMERGENCY"
     *                 length="20"
     *             
     */
    @Column(name="EMERGENCY",length=20)
    public String getEmergency() {
        return this.emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    /** 
     *                @hibernate.property
     *                 column="KEYWORDS"
     *                 length="2000"
     *             
     */
    @Column(name="KEYWORDS",length=2000)
    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /** 
     *                @hibernate.property
     *                 column="CONTENT"
     *                 length="4000"
     *             
     */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", columnDefinition = "BLOB")
    public byte[] getContent() {
        return this.content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    /** 
     *                @hibernate.property
     *                 column="PRINT_TIME"
     *                 length="10"
     *             
     */
    @Column(name="PRINT_TIME",length=10)
    public String getPrintTime() {
        return this.printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    /** 
     *                @hibernate.property
     *                 column="ENTRY_PEOPLE"
     *                 length="40"
     *             
     */
    @Column(name="ENTRY_PEOPLE",length=40)
    public String getEntryPeople() {
        return this.entryPeople;
    }

    public void setEntryPeople(String entryPeople) {
        this.entryPeople = entryPeople;
    }

    /** 
     *                @hibernate.property
     *                 column="DRAFT_SUGGESTION"
     *                 length="4000"
     *             
     */
    @Column(name="DRAFT_SUGGESTION",length=4000)
    public String getDraftSuggestion() {
        return this.draftSuggestion;
    }

    public void setDraftSuggestion(String draftSuggestion) {
        this.draftSuggestion = draftSuggestion;
    }

    /** 
     *                @hibernate.property
     *                 column="OFFICE_DIRECTOR_SUGGESTION"
     *                 length="4000"
     *             
     */
    @Column(name="OFFICE_DIRECTOR_SUGGESTION",length=4000)
    public String getOfficeDirectorSuggestion() {
        return this.officeDirectorSuggestion;
    }

    public void setOfficeDirectorSuggestion(String officeDirectorSuggestion) {
        this.officeDirectorSuggestion = officeDirectorSuggestion;
    }

    /** 
     *                @hibernate.property
     *                 column="BRANCH_SUGGESTION"
     *                 length="4000"
     *             
     */
    @Column(name="BRANCH_SUGGESTION",length=4000)
    public String getBranchSuggestion() {
        return this.branchSuggestion;
    }

    public void setBranchSuggestion(String branchSuggestion) {
        this.branchSuggestion = branchSuggestion;
    }

    /** 
     *                @hibernate.property
     *                 column="LEADER_SUGGESTION"
     *                 length="4000"
     *             
     */
    @Column(name="LEADER_SUGGESTION",length=4000)
    public String getLeaderSuggestion() {
        return this.leaderSuggestion;
    }

    public void setLeaderSuggestion(String leaderSuggestion) {
        this.leaderSuggestion = leaderSuggestion;
    }

    /** 
     *                @hibernate.property
     *                 column="PROCESS_RESULT"
     *                 length="4000"
     *             
     */
    @Column(name="PROCESS_RESULT",length=4000)
    public String getProcessResult() {
        return this.processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    /** 
     *                @hibernate.property
     *                 column="RECV_STATE"
     *                 length="20"
     *             
     */
    @Column(name="RECV_STATE",length=20)
    public String getRecvState() {
        return this.recvState;
    }

    public void setRecvState(String recvState) {
        this.recvState = recvState;
    }

    /** 
     *                @hibernate.property
     *                 column="DIPOSAL_BILL"
     *                 length="4000"
     *             
     */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DIPOSAL_BILL", columnDefinition = "BLOB")
    public Object getDiposalBill() {
        return this.diposalBill;
    }

    public void setDiposalBill(Object diposalBill) {
        this.diposalBill = diposalBill;
    }

    /** 
     *                @hibernate.property
     *                 column="APPROVAL_RECV_TIME"
     *                 length="10"
     *             
     */
    @Column(name="APPROVAL_RECV_TIME",length=10)
    public String getApprovalRecvTime() {
        return this.approvalRecvTime;
    }

    public void setApprovalRecvTime(String approvalRecvTime) {
        this.approvalRecvTime = approvalRecvTime;
    }

    /** 
     *                @hibernate.property
     *                 column="RECV_FORMID"
     *                 length="32"
     *             
     */
    @Column(name="RECV_FORMID",length=32)
    public String getRecvFormid() {
        return this.recvFormid;
    }

    public void setRecvFormid(String recvFormid) {
        this.recvFormid = recvFormid;
    }

    /** 
     *                @hibernate.property
     *                 column="CONTENT_NAME"
     *                 length="25"
     *             
     */
    @Column(name="CONTENT_NAME",length=25)
    public String getContentName() {
        return this.contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("oarecvdocid", getOarecvdocid())
            .append("personConfigFlag", getPersonConfigFlag())
            .append("personOperateDate", getPersonOperateDate())
            .append("personOperater", getPersonOperater())
            //.append("personDemo", getPersonDemo())
            //.append("personFilename", getPersonFilename())
            .append("recvNum", getRecvNum())
            .append("docTitle", getDocTitle())
            .append("issueDepartSigned", getIssueDepartSigned())
            .append("docNumber", getDocNumber())
            .append("recvTime", getRecvTime())
            .append("secretLvl", getSecretLvl())
            .append("emergency", getEmergency())
            .append("keywords", getKeywords())
            .append("content", getContent())
            .append("printTime", getPrintTime())
            .append("entryPeople", getEntryPeople())
            .append("draftSuggestion", getDraftSuggestion())
            .append("officeDirectorSuggestion", getOfficeDirectorSuggestion())
            .append("branchSuggestion", getBranchSuggestion())
            .append("leaderSuggestion", getLeaderSuggestion())
            .append("processResult", getProcessResult())
            .append("recvState", getRecvState())
            .append("diposalBill", getDiposalBill())
            .append("approvalRecvTime", getApprovalRecvTime())
            .append("recvFormid", getRecvFormid())
            .append("contentName", getContentName())
            .toString();
    }

    @Column(name="PAGE_COUNT",length=12)
	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	@Column(name="WORKFLOWNAME",length=255)
	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}
	
	@Column(name="WORKFLOWSTATE",length=2)
	public String getWorkflowState() {
		return workflowState;
	}

	public void setWorkflowState(String workflowState) {
		this.workflowState = workflowState;
	}
	
	@Column(name="WORKFLOWAUTHOR",length=100)
    public String getWorkflowAuthor() {
		return workflowAuthor;
	}

	public void setWorkflowAuthor(String workflowAuthor) {
		this.workflowAuthor = workflowAuthor;
	}

	@Column(name="WORKFLOWTITLE",length=500)
	public String getWorkflowTitle() {
		return workflowTitle;
	}

	public void setWorkflowTitle(String workflowTitle) {
		this.workflowTitle = workflowTitle;
	}

	@Column(name="WORKFLOWCODE",length=255)
	public String getWorkflowCode() {
		return workflowCode;
	}

	public void setWorkflowCode(String workflowCode) {
		this.workflowCode = workflowCode;
	}

}
