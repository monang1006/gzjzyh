package com.strongit.xxbs.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


/**
 * The persistent class for the T_INFO_BASE_PUBLISH database table.
 * 
 */
@Entity
@Table(name="T_INFO_BASE_PUBLISH")
public class TInfoBasePublish implements Serializable {
	private static final long serialVersionUID = 1L;

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name="PUB_ID")
	private String pubId;
	
	@Column(name="MAIL_UID")
	private String mailUid;
	
	@Column(name="PUB_RAW_TITLE")
	private String pubRawTitle;
	
	@Column(name="PUB_ORG_TYPE")
	private String pubOrgType;
	
	@Column(name="PUB_SYNC_STATUS")
	private String pubSyncStatus;

	@Column(name="ORG_ID")
	private String orgId;

	@Column(name="PUB_COMMENT")
	private String pubComment;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="PUB_DATE")
	private Date pubDate;
    
    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="PUB_SUBMIT_DATE")
	private Date pubSubmitDate;

    @Lob()
    //@Type(type="org.springframework.orm.hibernate3.support.ClobStringType")
	@Column(name="PUB_EDIT_CONTENT")
	private String pubEditContent;

    @Lob()
    //@Type(type="org.springframework.orm.hibernate3.support.ClobStringType")
	@Column(name="PUB_RAW_CONTENT")
    private String pubRawContent;

    @Lob()
    //@Type(type="org.springframework.orm.hibernate3.support.BlobByteArrayType")
	@Column(name="PUB_WORD")
	private byte[] pubWord;

	@Column(name="PUB_EDITOR")
	private String pubEditor;

	@Column(name="PUB_INFO_TYPE")
	private String pubInfoType;

	@Column(name="PUB_INSTRUCTION_CONTENT")
	private String pubInstructionContent;

	@Column(name="PUB_REMARK_SCORE")
	private BigDecimal pubRemarkScore;

	@Column(name="PUB_INSTRUCTOR")
	private String pubInstructor;

	@Column(name="PUB_IS_COMMENT")
	private String pubIsComment;

	@Column(name="PUB_IS_INSTRUCTION")
	private String pubIsInstruction;

	@Column(name="PUB_IS_MAIL_INFO")
	private String pubIsMailInfo;

	@Column(name="PUB_IS_SHARE")
	private String pubIsShare;

	@Column(name="PUB_IS_TEXT")
	private String pubIsText;

	@Column(name="PUB_PUBLISHER_ID")
	private String pubPublisherId;

	@Column(name="PUB_SIGNER")
	private String pubSigner;

	@Column(name="PUB_SUBMIT_STATUS")
	private String pubSubmitStatus;

	@Column(name="PUB_TITLE")
	private String pubTitle;

	@Column(name="PUB_USE_SCORE")
	private BigDecimal pubUseScore;

	@Column(name="PUB_USE_STATUS")
	private String pubUseStatus;

	@Column(name="PUB_WORD_NAME")
	private String pubWordName;
	
	@Column(name="PUB_CODE")
	private Integer pubCode;
	
	@Column(name="PUB_ADOPT_CODE")
	private Integer pubAdoptCode;
	
	@Column(name="PUB_ADOPT_ORG_ID")
	private String pubAdoptOrgId;
	
	@Column(name="PUB_ADOPT_USER_ID")
	private String pubAdoptUserId;
	
	@Column(name="PUB_IS_MERGE")
	private String pubIsMerge;

	@Column(name="PUB_MERGE_ORG")
	private String pubMergeOrg;
	
	@Column(name="PUB_MERGE_NAME")
	private String pubMergeName;
	
	@Column(name="PUB_MERGE_FLAG")
	private String pubMergeFlag;
	
	@Column(name="PUB_SORT")
	private Integer pubSort;
	
	@Column(name="PUB_FILE1")
	private String pubFile1;
	
	@Column(name="PUB_FILE2")
	private String pubFile2;
	
	@Column(name="PUB_FILE1NAME")
	private String pubFile1Name;

	@Column(name="PUB_FILE2NAME")
	private String pubFile2Name;
	
	@Column(name="ORG_NAME")
	private String orgName;
	
	@Column(name="IS_OA")
	private String isOA;
	
	@OneToOne(optional = true)
	@JoinColumn(name="APT_ID", nullable=true)
	private TInfoBaseAppoint TInfoBaseAppoint;

	@OneToMany(mappedBy="TInfoBasePublish")
	private Set<TInfoBasePS> TInfoBasePSs;
	
	@ManyToOne
	@JoinColumn(name = "COL_ID")
	private TInfoBaseColumn TInfoBaseColumn;
	
	@ManyToOne
	@JoinColumn(name = "ISS_ID")
	private TInfoBaseIssue TInfoBaseIssue;

    public TInfoBasePublish() {
    }

	public String getPubId() {
		return this.pubId;
	}

	public void setPubId(String pubId) {
		this.pubId = pubId;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPubComment() {
		return this.pubComment;
	}

	public void setPubComment(String pubComment) {
		this.pubComment = pubComment;
	}

	public Date getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getPubEditContent() {
		return this.pubEditContent;
	}

	public void setPubEditContent(String pubEditContent) {
		this.pubEditContent = pubEditContent;
	}

	public String getPubEditor() {
		return this.pubEditor;
	}

	public void setPubEditor(String pubEditor) {
		this.pubEditor = pubEditor;
	}

	public String getPubInfoType() {
		return this.pubInfoType;
	}

	public void setPubInfoType(String pubInfoType) {
		this.pubInfoType = pubInfoType;
	}

	public String getPubInstructionContent() {
		return this.pubInstructionContent;
	}

	public void setPubInstructionContent(String pubInstructionContent) {
		this.pubInstructionContent = pubInstructionContent;
	}

	public String getPubInstructor() {
		return this.pubInstructor;
	}

	public void setPubInstructor(String pubInstructor) {
		this.pubInstructor = pubInstructor;
	}

	public String getPubIsComment() {
		return this.pubIsComment;
	}

	public void setPubIsComment(String pubIsComment) {
		this.pubIsComment = pubIsComment;
	}

	public String getPubIsInstruction() {
		return this.pubIsInstruction;
	}

	public void setPubIsInstruction(String pubIsInstruction) {
		this.pubIsInstruction = pubIsInstruction;
	}

	public String getPubIsMailInfo() {
		return this.pubIsMailInfo;
	}

	public void setPubIsMailInfo(String pubIsMailInfo) {
		this.pubIsMailInfo = pubIsMailInfo;
	}

	public String getPubIsShare() {
		return this.pubIsShare;
	}

	public void setPubIsShare(String pubIsShare) {
		this.pubIsShare = pubIsShare;
	}

	public String getPubIsText() {
		return this.pubIsText;
	}

	public void setPubIsText(String pubIsText) {
		this.pubIsText = pubIsText;
	}

	public String getPubPublisherId() {
		return this.pubPublisherId;
	}

	public void setPubPublisherId(String pubPublisherId) {
		this.pubPublisherId = pubPublisherId;
	}

	public String getPubRawContent() {
		return this.pubRawContent;
	}

	public void setPubRawContent(String pubRawContent) {
		this.pubRawContent = pubRawContent;
	}

	public String getPubSigner() {
		return this.pubSigner;
	}

	public void setPubSigner(String pubSigner) {
		this.pubSigner = pubSigner;
	}

	public String getPubSubmitStatus() {
		return this.pubSubmitStatus;
	}

	public void setPubSubmitStatus(String pubSubmitStatus) {
		this.pubSubmitStatus = pubSubmitStatus;
	}

	public String getPubTitle() {
		return this.pubTitle;
	}

	public void setPubTitle(String pubTitle) {
		this.pubTitle = pubTitle;
	}

	public BigDecimal getPubUseScore() {
		return this.pubUseScore;
	}

	public void setPubUseScore(BigDecimal pubUseScore) {
		this.pubUseScore = pubUseScore;
	}

	public String getPubUseStatus() {
		return this.pubUseStatus;
	}

	public void setPubUseStatus(String pubUseStatus) {
		this.pubUseStatus = pubUseStatus;
	}

	public byte[] getPubWord() {
		return this.pubWord;
	}

	public void setPubWord(byte[] pubWord) {
		this.pubWord = pubWord;
	}

	public String getPubWordName() {
		return this.pubWordName;
	}

	public void setPubWordName(String pubWordName) {
		this.pubWordName = pubWordName;
	}

	public TInfoBaseAppoint getTInfoBaseAppoint()
	{
		return TInfoBaseAppoint;
	}

	public void setTInfoBaseAppoint(TInfoBaseAppoint tInfoBaseAppoint)
	{
		TInfoBaseAppoint = tInfoBaseAppoint;
	}

	public TInfoBaseColumn getTInfoBaseColumn()
	{
		return TInfoBaseColumn;
	}

	public void setTInfoBaseColumn(TInfoBaseColumn tInfoBaseColumn)
	{
		TInfoBaseColumn = tInfoBaseColumn;
	}

	public TInfoBaseIssue getTInfoBaseIssue()
	{
		return TInfoBaseIssue;
	}

	public void setTInfoBaseIssue(TInfoBaseIssue tInfoBaseIssue)
	{
		TInfoBaseIssue = tInfoBaseIssue;
	}

	public BigDecimal getPubRemarkScore()
	{
		return pubRemarkScore;
	}

	public void setPubRemarkScore(BigDecimal pubRemarkScore)
	{
		this.pubRemarkScore = pubRemarkScore;
	}

	public Set<TInfoBasePS> getTInfoBasePSs()
	{
		return TInfoBasePSs;
	}

	public void setTInfoBasePSs(Set<TInfoBasePS> tInfoBasePSs)
	{
		TInfoBasePSs = tInfoBasePSs;
	}

	public String getPubSyncStatus()
	{
		return pubSyncStatus;
	}

	public void setPubSyncStatus(String pubSyncStatus)
	{
		this.pubSyncStatus = pubSyncStatus;
	}

	public Date getPubSubmitDate() {
		return pubSubmitDate;
	}

	public void setPubSubmitDate(Date pubSubmitDate) {
		this.pubSubmitDate = pubSubmitDate;
	}

	public String getPubOrgType()
	{
		return pubOrgType;
	}

	public void setPubOrgType(String pubOrgType)
	{
		this.pubOrgType = pubOrgType;
	}

	public String getPubRawTitle()
	{
		return pubRawTitle;
	}

	public void setPubRawTitle(String pubRawTitle)
	{
		this.pubRawTitle = pubRawTitle;
	}

	public String getMailUid()
	{
		return mailUid;
	}

	public void setMailUid(String mailUid)
	{
		this.mailUid = mailUid;
	}

	
	public Integer getPubCode() {
		return pubCode;
	}

	public void setPubCode(Integer pubCode) {
		this.pubCode = pubCode;
	}

	public Integer getPubAdoptCode() {
		return pubAdoptCode;
	}

	public void setPubAdoptCode(Integer pubAdoptCode) {
		this.pubAdoptCode = pubAdoptCode;
	}

	public String getPubAdoptOrgId() {
		return pubAdoptOrgId;
	}

	public void setPubAdoptOrgId(String pubAdoptOrgId) {
		this.pubAdoptOrgId = pubAdoptOrgId;
	}

	public String getPubAdoptUserId() {
		return pubAdoptUserId;
	}

	public void setPubAdoptUserId(String pubAdoptUserId) {
		this.pubAdoptUserId = pubAdoptUserId;
	}

	public String getPubIsMerge() {
		return pubIsMerge;
	}

	public void setPubIsMerge(String pubIsMerge) {
		this.pubIsMerge = pubIsMerge;
	}

	public String getPubMergeOrg() {
		return pubMergeOrg;
	}

	public void setPubMergeOrg(String pubMergeOrg) {
		this.pubMergeOrg = pubMergeOrg;
	}

	public String getPubMergeFlag() {
		return pubMergeFlag;
	}

	public void setPubMergeFlag(String pubMergeFlag) {
		this.pubMergeFlag = pubMergeFlag;
	}

	public Integer getPubSort() {
		return pubSort;
	}

	public void setPubSort(Integer pubSort) {
		this.pubSort = pubSort;
	}

	public String getPubFile1() {
		return pubFile1;
	}

	public void setPubFile1(String pubFile1) {
		this.pubFile1 = pubFile1;
	}

	public String getPubFile2() {
		return pubFile2;
	}

	public void setPubFile2(String pubFile2) {
		this.pubFile2 = pubFile2;
	}

	public String getPubFile1Name() {
		return pubFile1Name;
	}

	public void setPubFile1Name(String pubFile1Name) {
		this.pubFile1Name = pubFile1Name;
	}

	public String getPubFile2Name() {
		return pubFile2Name;
	}

	public void setPubFile2Name(String pubFile2Name) {
		this.pubFile2Name = pubFile2Name;
	}

	public String getPubMergeName() {
		return pubMergeName;
	}

	public void setPubMergeName(String pubMergeName) {
		this.pubMergeName = pubMergeName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getIsOA() {
		return isOA;
	}

	public void setIsOA(String isOA) {
		this.isOA = isOA;
	}

	
	
}