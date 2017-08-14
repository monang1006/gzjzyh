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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_SENDDOC"
 *     
 */
@Entity
@Table(name="T_OA_SENDDOC")
public class ToaSenddoc implements Serializable {

	private static final long serialVersionUID = 5933760753726613514L;

	/** identifier field */
	@Id
	@GeneratedValue
	private String senddocId;

	/** nullable persistent field */
	private String senddocBussinessCode;

	/** nullable persistent field */
	private String senddocTitle;

	/** nullable persistent field */
	private String senddocCode;

	/** nullable persistent field */
	private String senddocSenderDepartCode;

	/** nullable persistent field */
	private String senddocSecretLvl;

	/** nullable persistent field */
	private String senddocProcDeadline;

	/** nullable persistent field */
	private String senddocSubmittoDepart;

	/** nullable persistent field */
	private String senddocCcDepart;

	/** nullable persistent field */
	private String senddocPeriodSecrecy;

	/** nullable persistent field */
	private String senddocEmergency;

	/** nullable persistent field */
	private String senddocIssuer;

	/** nullable persistent field */
	private String senddocIssueDepartSigned;

	/** nullable persistent field */
	private String senddocLegalCode;

	/** nullable persistent field */
	private Date senddocOfficialTime;

	/** nullable persistent field */
	private Date senddocRecvTime;

	/** nullable persistent field */
	private byte[] senddocContent;

	/** nullable persistent field */
	private String senddocRemark;

	/** nullable persistent field */
	private String senddocAttachNum;

	/** nullable persistent field */
	private String senddocKeywords;

	/** nullable persistent field */
	private String senddocPrintDepart;

	/** nullable persistent field */
	private Date senddocPrintTime;

	/** nullable persistent field */
	private String senddocPrintNum;

	/** nullable persistent field */
	private String senddocAuthor;

	/** nullable persistent field */
	private String senddocProofReader;

	/** nullable persistent field */
	private String senddocEntryPeople;

	/** nullable persistent field */
	private String senddocSealPeople;

	/** nullable persistent field */
	private Date senddocSealTime;
	
	private String senddocFormid;
	
	private String senddocState;//发文状态
	
	private String senddocUser;//草拟人
	
	private String senddocHavePrintNum ;//已打印份数
	
	private String senddocContentName;//正文名

	private String WORKFLOWNAME;
	
	private String WORKFLOWAUTHOR;
	
	private String WORKFLOWTITLE;
	
	private String WORKFLOWCODE;
	
	private String WORKFLOWSTATE;
	
	//预留字段
	private String rest1;
	private String rest2;
	private String rest3;
	private String rest4;
	private String rest5;
	private String rest6;
	private String rest7;
	private String rest8;

	/** full constructor */
	public ToaSenddoc(String senddocId, String senddocBussinessCode,
			String senddocTitle, String senddocCode,
			String senddocSenderDepartCode, String senddocSecretLvl,
			String senddocProcDeadline, String senddocSubmittoDepart,
			String senddocCcDepart, String senddocPeriodSecrecy,
			String senddocEmergency, String senddocIssuer,
			String senddocIssueDepartSigned, String senddocLegalCode,
			Date senddocOfficialTime, Date senddocRecvTime,
			byte[] senddocContent, String senddocRemark,
			String senddocAttachNum, String senddocKeywords,
			String senddocPrintDepart, Date senddocPrintTime,
			String senddocPrintNum, String senddocAuthor,
			String senddocProofReader, String senddocEntryPeople,
			String senddocSealPeople, Date senddocSealTime,String senddocFormid
			) {
		this.senddocId = senddocId;
		this.senddocBussinessCode = senddocBussinessCode;
		this.senddocTitle = senddocTitle;
		this.senddocCode = senddocCode;
		this.senddocSenderDepartCode = senddocSenderDepartCode;
		this.senddocSecretLvl = senddocSecretLvl;
		this.senddocProcDeadline = senddocProcDeadline;
		this.senddocSubmittoDepart = senddocSubmittoDepart;
		this.senddocCcDepart = senddocCcDepart;
		this.senddocPeriodSecrecy = senddocPeriodSecrecy;
		this.senddocEmergency = senddocEmergency;
		this.senddocIssuer = senddocIssuer;
		this.senddocIssueDepartSigned = senddocIssueDepartSigned;
		this.senddocLegalCode = senddocLegalCode;
		this.senddocOfficialTime = senddocOfficialTime;
		this.senddocRecvTime = senddocRecvTime;
		this.senddocContent = senddocContent;
		this.senddocRemark = senddocRemark;
		this.senddocAttachNum = senddocAttachNum;
		this.senddocKeywords = senddocKeywords;
		this.senddocPrintDepart = senddocPrintDepart;
		this.senddocPrintTime = senddocPrintTime;
		this.senddocPrintNum = senddocPrintNum;
		this.senddocAuthor = senddocAuthor;
		this.senddocProofReader = senddocProofReader;
		this.senddocEntryPeople = senddocEntryPeople;
		this.senddocSealPeople = senddocSealPeople;
		this.senddocSealTime = senddocSealTime;
		this.senddocFormid = senddocFormid;
	}

	/** default constructor */
	public ToaSenddoc() {
	}

	/** minimal constructor */
	public ToaSenddoc(String senddocId) {
		this.senddocId = senddocId;
	}

	/** 
	 *            @hibernate.id
	 *             generator-class="assigned"
	 *             type="java.lang.String"
	 *             column="SENDDOC_ID"
	 *         
	 */
	@Id
	@Column(name="SENDDOC_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getSenddocId() {
		return this.senddocId;
	}
	
	public void setSenddocId(String senddocId) {
		this.senddocId = senddocId;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_BUSSINESS_CODE"
	 *             length="32"
	 *         
	 */
	@Column(name="SENDDOC_BUSSINESS_CODE", length=32)
	public String getSenddocBussinessCode() {
		return this.senddocBussinessCode;
	}

	public void setSenddocBussinessCode(String senddocBussinessCode) {
		this.senddocBussinessCode = senddocBussinessCode;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_TITLE"
	 *             length="200"
	 *         
	 */
	@Column(name="SENDDOC_TITLE", length=200)
	public String getSenddocTitle() {
		return this.senddocTitle;
	}

	public void setSenddocTitle(String senddocTitle) {
		this.senddocTitle = senddocTitle;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_CODE"
	 *             length="50"
	 *         
	 */
	@Column(name="SENDDOC_CODE", length=50)
	public String getSenddocCode() {
		return this.senddocCode;
	}

	public void setSenddocCode(String senddocCode) {
		this.senddocCode = senddocCode;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_SENDER_DEPART_CODE"
	 *             length="50"
	 *         
	 */
	@Column(name="SENDDOC_SENDER_DEPART_CODE", length=50)
	public String getSenddocSenderDepartCode() {
		return this.senddocSenderDepartCode;
	}

	public void setSenddocSenderDepartCode(String senddocSenderDepartCode) {
		this.senddocSenderDepartCode = senddocSenderDepartCode;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_SECRET_LVL"
	 *             length="10"
	 *         
	 */
	@Column(name="SENDDOC_SECRET_LVL", length=10)
	public String getSenddocSecretLvl() {
		return this.senddocSecretLvl;
	}

	public void setSenddocSecretLvl(String senddocSecretLvl) {
		this.senddocSecretLvl = senddocSecretLvl;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_PROC_DEADLINE"
	 *             length="1"
	 *         
	 */
	@Column(name="SENDDOC_PROC_DEADLINE", length=1)
	public String getSenddocProcDeadline() {
		return this.senddocProcDeadline;
	}

	public void setSenddocProcDeadline(String senddocProcDeadline) {
		this.senddocProcDeadline = senddocProcDeadline;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_SUBMITTO_DEPART"
	 *             length="50"
	 *         
	 */
	@Column(name="SENDDOC_SUBMITTO_DEPART", length=50)
	public String getSenddocSubmittoDepart() {
		return this.senddocSubmittoDepart;
	}

	public void setSenddocSubmittoDepart(String senddocSubmittoDepart) {
		this.senddocSubmittoDepart = senddocSubmittoDepart;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_CC_DEPART"
	 *             length="50"
	 *         
	 */
	@Column(name="SENDDOC_CC_DEPART", length=50)
	public String getSenddocCcDepart() {
		return this.senddocCcDepart;
	}

	public void setSenddocCcDepart(String senddocCcDepart) {
		this.senddocCcDepart = senddocCcDepart;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_PERIOD_SECRECY"
	 *             length="10"
	 *         
	 */
	@Column(name="SENDDOC_PERIOD_SECRECY", length=10)
	public String getSenddocPeriodSecrecy() {
		return this.senddocPeriodSecrecy;
	}

	public void setSenddocPeriodSecrecy(String senddocPeriodSecrecy) {
		this.senddocPeriodSecrecy = senddocPeriodSecrecy;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_EMERGENCY"
	 *             length="10"
	 *         
	 */
	@Column(name="SENDDOC_EMERGENCY", length=10)
	public String getSenddocEmergency() {
		return this.senddocEmergency;
	}

	public void setSenddocEmergency(String senddocEmergency) {
		this.senddocEmergency = senddocEmergency;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_ISSUER"
	 *             length="20"
	 *         
	 */
	@Column(name="SENDDOC_ISSUER", length=20)
	public String getSenddocIssuer() {
		return this.senddocIssuer;
	}

	public void setSenddocIssuer(String senddocIssuer) {
		this.senddocIssuer = senddocIssuer;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_ISSUE_DEPART_SIGNED"
	 *             length="50"
	 *         
	 */
	@Column(name="SENDDOC_ISSUE_DEPART_SIGNED", length=50)
	public String getSenddocIssueDepartSigned() {
		return this.senddocIssueDepartSigned;
	}

	public void setSenddocIssueDepartSigned(String senddocIssueDepartSigned) {
		this.senddocIssueDepartSigned = senddocIssueDepartSigned;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_LEGAL_CODE"
	 *             length="100"
	 *         
	 */
	@Column(name="SENDDOC_LEGAL_CODE", length=100)
	public String getSenddocLegalCode() {
		return this.senddocLegalCode;
	}

	public void setSenddocLegalCode(String senddocLegalCode) {
		this.senddocLegalCode = senddocLegalCode;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_OFFICIAL_TIME"
	 *             length="7"
	 *         
	 */
	@Column(name="SENDDOC_OFFICIAL_TIME", length=7)
	public Date getSenddocOfficialTime() {
		return this.senddocOfficialTime;
	}

	public void setSenddocOfficialTime(Date senddocOfficialTime) {
		this.senddocOfficialTime = senddocOfficialTime;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_RECV_TIME"
	 *             length="7"
	 *         
	 */
	@Column(name="SENDDOC_RECV_TIME", length=7)
	public Date getSenddocRecvTime() {
		return this.senddocRecvTime;
	}

	public void setSenddocRecvTime(Date senddocRecvTime) {
		this.senddocRecvTime = senddocRecvTime;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_CONTENT"
	 *             length="4000"
	 *         
	 */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "SENDDOC_CONTENT", columnDefinition = "BLOB")
	public byte[] getSenddocContent() {
		return this.senddocContent;
	}

	public void setSenddocContent(byte[] senddocContent) {
		this.senddocContent = senddocContent;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_REMARK"
	 *             length="4000"
	 *         
	 */
	@Column(name="SENDDOC_REMARK", length=4000)
	public String getSenddocRemark() {
		return this.senddocRemark;
	}

	public void setSenddocRemark(String senddocRemark) {
		this.senddocRemark = senddocRemark;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_ATTACH_NUM"
	 *             length="4"
	 *         
	 */
	@Column(name="SENDDOC_ATTACH_NUM", length=4)
	public String getSenddocAttachNum() {
		return this.senddocAttachNum;
	}

	public void setSenddocAttachNum(String senddocAttachNum) {
		this.senddocAttachNum = senddocAttachNum;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_KEYWORDS"
	 *             length="100"
	 *         
	 */
	@Column(name="SENDDOC_KEYWORDS", length=100)
	public String getSenddocKeywords() {
		return this.senddocKeywords;
	}

	public void setSenddocKeywords(String senddocKeywords) {
		this.senddocKeywords = senddocKeywords;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_PRINT_DEPART"
	 *             length="50"
	 *         
	 */
	@Column(name="SENDDOC_PRINT_DEPART", length=50)
	public String getSenddocPrintDepart() {
		return this.senddocPrintDepart;
	}

	public void setSenddocPrintDepart(String senddocPrintDepart) {
		this.senddocPrintDepart = senddocPrintDepart;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_PRINT_TIME"
	 *             length="7"
	 *         
	 */
	@Column(name="SENDDOC_PRINT_TIME", length=7)
	public Date getSenddocPrintTime() {
		return this.senddocPrintTime;
	}

	public void setSenddocPrintTime(Date senddocPrintTime) {
		this.senddocPrintTime = senddocPrintTime;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_PRINT_NUM"
	 *             length="4"
	 *         
	 */
	@Column(name="SENDDOC_PRINT_NUM", length=4)
	public String getSenddocPrintNum() {
		return this.senddocPrintNum;
	}

	public void setSenddocPrintNum(String senddocPrintNum) {
		this.senddocPrintNum = senddocPrintNum;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_AUTHOR"
	 *             length="20"
	 *         
	 */
	@Column(name="SENDDOC_AUTHOR", length=20)
	public String getSenddocAuthor() {
		return this.senddocAuthor;
	}

	public void setSenddocAuthor(String senddocAuthor) {
		this.senddocAuthor = senddocAuthor;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_PROOF_READER"
	 *             length="20"
	 *         
	 */
	@Column(name="SENDDOC_PROOF_READER", length=20)
	public String getSenddocProofReader() {
		return this.senddocProofReader;
	}

	public void setSenddocProofReader(String senddocProofReader) {
		this.senddocProofReader = senddocProofReader;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_ENTRY_PEOPLE"
	 *             length="20"
	 *         
	 */
	@Column(name="SENDDOC_ENTRY_PEOPLE", length=20)
	public String getSenddocEntryPeople() {
		return this.senddocEntryPeople;
	}

	public void setSenddocEntryPeople(String senddocEntryPeople) {
		this.senddocEntryPeople = senddocEntryPeople;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_SEAL_PEOPLE"
	 *             length="20"
	 *         
	 */
	@Column(name="SENDDOC_SEAL_PEOPLE", length=20)
	public String getSenddocSealPeople() {
		return this.senddocSealPeople;
	}

	public void setSenddocSealPeople(String senddocSealPeople) {
		this.senddocSealPeople = senddocSealPeople;
	}

	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_SEAL_TIME"
	 *             length="7"
	 *         
	 */
	@Column(name="SENDDOC_SEAL_TIME", length=7)
	public Date getSenddocSealTime() {
		return this.senddocSealTime;
	}

	public void setSenddocSealTime(Date senddocSealTime) {
		this.senddocSealTime = senddocSealTime;
	}
	
	/** 
	 *            @hibernate.property
	 *             column="SENDDOC_FORMID"
	 *             length="7"
	 *         
	 */
	@Column(name="SENDDOC_FORMID", length=32)
	public String getSenddocFormid() {
		return senddocFormid;
	}

	public void setSenddocFormid(String senddocFormid) {
		this.senddocFormid = senddocFormid;
	}

	@Transient
	public String toString() {
		return new ToStringBuilder(this).append("senddocId", getSenddocId())
				.toString();
	}

	@Column(name="SENDDOC_STATE")
	public String getSenddocState() {
		return senddocState;
	}

	public void setSenddocState(String senddocState) {
		this.senddocState = senddocState;
	}

	@Column(name="SENDDOC_USER")
	public String getSenddocUser() {
		return senddocUser;
	}

	public void setSenddocUser(String senddocUser) {
		this.senddocUser = senddocUser;
	}

	@Column(name="SENDDOC_HAVE_PRINT_NUM",nullable=true)
	public String getSenddocHavePrintNum() {
		return senddocHavePrintNum;
	}

	public void setSenddocHavePrintNum(String senddocHavePrintNum) {
		this.senddocHavePrintNum = senddocHavePrintNum;
	}

	@Column(name="SENDDOC_CONTENT_NAME",nullable=true)
	public String getSenddocContentName() {
		return senddocContentName;
	}

	public void setSenddocContentName(String senddocContentName) {
		this.senddocContentName = senddocContentName;
	}

	//预留字段
	@Column(name = "rest1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "rest2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "rest3")
	public String getRest3() {
		return rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "rest4")
	public String getRest4() {
		return rest4;
	}

	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}

	@Column(name = "rest5")
	public String getRest5() {
		return rest5;
	}

	public void setRest5(String rest5) {
		this.rest5 = rest5;
	}

	@Column(name = "rest6")
	public String getRest6() {
		return rest6;
	}

	public void setRest6(String rest6) {
		this.rest6 = rest6;
	}

	@Column(name = "rest7")
	public String getRest7() {
		return rest7;
	}

	public void setRest7(String rest7) {
		this.rest7 = rest7;
	}

	@Column(name = "rest8")
	public String getRest8() {
		return rest8;
	}

	public void setRest8(String rest8) {
		this.rest8 = rest8;
	}

	@Column(name="WORKFLOWNAME")
	public String getWORKFLOWNAME() {
		return WORKFLOWNAME;
	}

	public void setWORKFLOWNAME(String workflowname) {
		WORKFLOWNAME = workflowname;
	}

	@Column(name="WORKFLOWAUTHOR")
	public String getWORKFLOWAUTHOR() {
		return WORKFLOWAUTHOR;
	}

	public void setWORKFLOWAUTHOR(String workflowauthor) {
		WORKFLOWAUTHOR = workflowauthor;
	}

	@Column(name="WORKFLOWTITLE")
	public String getWORKFLOWTITLE() {
		return WORKFLOWTITLE;
	}

	public void setWORKFLOWTITLE(String workflowtitle) {
		WORKFLOWTITLE = workflowtitle;
	}

	@Column(name="WORKFLOWCODE")
	public String getWORKFLOWCODE() {
		return WORKFLOWCODE;
	}

	public void setWORKFLOWCODE(String workflowcode) {
		WORKFLOWCODE = workflowcode;
	}

	@Column(name="WORKFLOWSTATE")
	public String getWORKFLOWSTATE() {
		return WORKFLOWSTATE;
	}

	public void setWORKFLOWSTATE(String workflowstate) {
		WORKFLOWSTATE = workflowstate;
	}

}
