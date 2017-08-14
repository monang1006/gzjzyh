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

import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OARECVDOC"
 *     
*/
@Entity
@Table(name="T_OARECVDOC")
public class Recivedoc implements Serializable {
	
	 /** identifier field */
	@Id 
	@GeneratedValue
    private String recvdocid;// OARECVDOCID

    /** nullable persistent field */
    private String recvnum;//RECV_NUM

    /** nullable persistent field */
    private String docTitle;// DOC_TITLE

    /** nullable persistent field */
    private String docNumber; //DOC_NUMBER

    /** nullable persistent field */
    private String departsigned; //ISSUE_DEPART_SIGNED

    /** nullable persistent field */
    private String secretLvl;//SECRET_LVL

    /** nullable persistent field */
    private String emergency; //EMERGENCY

    /** nullable persistent field */
    private String keywords;//KEYWORDS

    /** nullable persistent field */
    private String printTime;//PRINT_TIME

    /** nullable persistent field */
    private String entryPeople;//ENTRY_PEOPLE

    /** nullable persistent field */
    private String draftSuggestion; //DRAFT_SUGGESTION

    /** nullable persistent field */
    private String officeDireSuggestion;//OFFICE_DIRECTOR_SUGGESTION

    /** nullable persistent field */
    private String branchSuggestion; //BRANCH_SUGGESTION

    /** nullable persistent field */
    private String leaderSuggestion;//LEADER_SUGGESTION

    /** nullable persistent field */
    private String processResult;//PROCESS_RESULT

    /** nullable persistent field */
    private String recvState;//RECV_STATE

    /** nullable persistent field */
    private String approvalRecvTime;//APPROVAL_RECV_TIME

    /** nullable persistent field */
    private byte[] content; //CONTENT
    
    private byte[] diposal_bill; //DIPOSAL_BILL

    /** nullable persistent field */
    private String recvFormId;//RECV_FORMID

    /** nullable persistent field */
    private String contentName; //CONTENT_NAME

    /** nullable persistent field */
    private String notes;//NOTES

    /** nullable persistent field */
    private String banjiefangshi;//BANJIEFANGSHI

    /** nullable persistent field */
    private Date recvTime;  //RECV_TIME

    /** nullable persistent field */
    private String recvCause;//RECV_CAUSE

    /** nullable persistent field */
    private String contractDepart;//CONTRACT_DEPART

    /** nullable persistent field */
    private String combineHandle;//COMBINE_HANDLE

    /** nullable persistent field */
    private String tingofficeSuggestion;//TINGOFFICE_SUGGESTION

    /** nullable persistent field */
    private String pageCount;//PAGE_COUNT

    /** nullable persistent field */
    private Date docType;//DOC_TYPE
    
     private String workflowTitle;   //  WORKFLOWTITLE  WORKFLOWSTATE
     
     private String workflowState; //WORKFLOWSTATE
    
    public Recivedoc(){
    	
    }
    
    @Id
	@Column(name="OARECVDOCID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRecvdocid() {
		return recvdocid;
	}

	public void setRecvdocid(String recvdocid) {
		this.recvdocid = recvdocid;
	}
	

 @Column(name="APPROVAL_RECV_TIME",nullable=true)
	public String getApprovalRecvTime() {
		return approvalRecvTime;
	}

	public void setApprovalRecvTime(String approvalRecvTime) {
		this.approvalRecvTime = approvalRecvTime;
	}
 @Column(name="BANJIEFANGSHI",nullable=true)
	public String getBanjiefangshi() {
		return banjiefangshi;
	}

	public void setBanjiefangshi(String banjiefangshi) {
		this.banjiefangshi = banjiefangshi;
	}

	  @Column(name="BRANCH_SUGGESTION",nullable=true)
	public String getBranchSuggestion() {
		return branchSuggestion;
	}

	public void setBranchSuggestion(String branchSuggestion) {
		this.branchSuggestion = branchSuggestion;
	}

	  @Column(name="COMBINE_HANDLE",nullable=true)
	public String getCombineHandle() {
		return combineHandle;
	}

	public void setCombineHandle(String combineHandle) {
		this.combineHandle = combineHandle;
	}

	 @Lob
		@Basic(fetch = FetchType.LAZY)
		@Column(name = "CONTENT", columnDefinition = "BLOB",nullable=true)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	  @Column(name="CONTENT_NAME",nullable=true)
	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	  @Column(name="CONTRACT_DEPART",nullable=true)
	public String getContractDepart() {
		return contractDepart;
	}

	public void setContractDepart(String contractDepart) {
		this.contractDepart = contractDepart;
	}

	  @Column(name="ISSUE_DEPART_SIGNED",nullable=true)
	public String getDepartsigned() {
		return departsigned;
	}

	public void setDepartsigned(String departsigned) {
		this.departsigned = departsigned;
	}

 @Lob
@Basic(fetch = FetchType.LAZY)
@Column(name = "DIPOSAL_BILL", columnDefinition = "BLOB",nullable=true)
	public byte[] getDiposal_bill() {
		return diposal_bill;
	}

	public void setDiposal_bill(byte[] diposal_bill) {
		this.diposal_bill = diposal_bill;
	}

	  @Column(name="DOC_NUMBER",nullable=true)
	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	  @Column(name="DOC_TITLE",nullable=true)
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	
	  @Column(name="DOC_TYPE",nullable=true)
	public Date getDocType() {
		return docType;
	}

	public void setDocType(Date docType) {
		this.docType = docType;
	}

	  @Column(name="DRAFT_SUGGESTION",nullable=true)
	public String getDraftSuggestion() {
		return draftSuggestion;
	}

	public void setDraftSuggestion(String draftSuggestion) {
		this.draftSuggestion = draftSuggestion;
	}

	  @Column(name="EMERGENCY",nullable=true)
	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	  @Column(name="ENTRY_PEOPLE",nullable=true)
	public String getEntryPeople() {
		return entryPeople;
	}

	public void setEntryPeople(String entryPeople) {
		this.entryPeople = entryPeople;
	}

	  @Column(name="KEYWORDS",nullable=true)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	  @Column(name="LEADER_SUGGESTION",nullable=true)
	public String getLeaderSuggestion() {
		return leaderSuggestion;
	}

	public void setLeaderSuggestion(String leaderSuggestion) {
		this.leaderSuggestion = leaderSuggestion;
	}

	  @Column(name="NOTES",nullable=true)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	  @Column(name="OFFICE_DIRECTOR_SUGGESTION",nullable=true)
	public String getOfficeDireSuggestion() {
		return officeDireSuggestion;
	}

	public void setOfficeDireSuggestion(String officeDireSuggestion) {
		this.officeDireSuggestion = officeDireSuggestion;
	}

	  @Column(name="PAGE_COUNT",nullable=true)
	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}


@Column(name="PRINT_TIME",nullable=true)
	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

	  @Column(name="PROCESS_RESULT",nullable=true)
	public String getProcessResult() {
		return processResult;
	}

	public void setProcessResult(String processResult) {
		this.processResult = processResult;
	}

	  @Column(name="RECV_CAUSE",nullable=true)
	public String getRecvCause() {
		return recvCause;
	}

	public void setRecvCause(String recvCause) {
		this.recvCause = recvCause;
	}

	

	  @Column(name="RECV_FORMID",nullable=true)
	public String getRecvFormId() {
		return recvFormId;
	}

	public void setRecvFormId(String recvFormId) {
		this.recvFormId = recvFormId;
	}

	  @Column(name="RECV_NUM",nullable=true)
	public String getRecvnum() {
		return recvnum;
	}

	public void setRecvnum(String recvnum) {
		this.recvnum = recvnum;
	}

	  @Column(name="RECV_STATE",nullable=true)
	public String getRecvState() {
		return recvState;
	}

	public void setRecvState(String recvState) {
		this.recvState = recvState;
	}

	  @Column(name="RECV_TIME",nullable=true)
	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	  @Column(name="SECRET_LVL",nullable=true)
	public String getSecretLvl() {
		return secretLvl;
	}

	public void setSecretLvl(String secretLvl) {
		this.secretLvl = secretLvl;
	}

 @Column(name="TINGOFFICE_SUGGESTION",nullable=true)
	public String getTingofficeSuggestion() {
		return tingofficeSuggestion;
	}

	public void setTingofficeSuggestion(String tingofficeSuggestion) {
		this.tingofficeSuggestion = tingofficeSuggestion;
	}

	@Column(name="WORKFLOWTITLE",nullable=true)
	public String getWorkflowTitle() {
		return workflowTitle;
	}

	public void setWorkflowTitle(String workflowTitle) {
		this.workflowTitle = workflowTitle;
	}
	
	@Column(name="WORKFLOWSTATE")
	public String getWorkflowState() {
		return workflowState;
	}

	public void setWorkflowState(String workflowState) {
		this.workflowState = workflowState;
	}

  

}
