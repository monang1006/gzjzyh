package com.strongit.oa.bo;

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
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         May 28, 2012 1:26:48 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.bo.RecvDocTrans
 */
@Entity
@Table(name = "T_OARECVDOC")
public class RecvDocTrans {
	/**
	 * @field oaRecvdocId 来文号
	 */
	private String oarecvdocid;

	/**
	 * @field workflowTitle 标题
	 */
	private String workflowtitle;

	/**
	 * @field issue_depart_signed 来文单位
	 */
	private String issue_depart_signed;

	/**
	 * @field person_config_flag 紧急程度
	 */
	private String person_config_flag;

	/**
	 * @field secret_lvl 密级
	 */
	private String secret_lvl;

	/**
	 * @field keywords 主题词
	 */
	private String keywords;

	/**
	 * @field content 正文
	 */
	private byte[] content;

	/**
	 * @field workflowstate 流程状态
	 */
	private String workflowstate = "0";

	/**
	 * @field workflowcode 流水号
	 */
	private String workflowcode;

	/**
	 * @field workflowauthor 拟稿人
	 */
	private String workflowauthor;

	/**
	 * @field workflowname 流程名称
	 */
	private String workflowname;

	/**
	 * @field recv_formid 收文表单ID
	 */
	private String recv_formid;

	/**
	 * @field entry_people 录入人
	 */
	private String entry_people;

	/**
	 * @field doc_number 来文字号
	 */
	private String doc_number;

	/**
	 * @field recv_time 来文日期
	 */
	private String recv_time;
	/**
	 * @field  recv_state	收文状态
	 */
	private String recv_state = "0";
	/**
	 * @field  adobe_pdf_name	PDF正文文件名字（为“”表示没有PDF正文）
	 */
	private String adobe_pdf_name = "";
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "CONTENT", columnDefinition = "BLOB", nullable = true)
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Column(name = "DOC_NUMBER", nullable = true)
	public String getDoc_number() {
		return doc_number;
	}

	public void setDoc_number(String doc_number) {
		this.doc_number = doc_number;
	}

	@Column(name = "ENTRY_PEOPLE", nullable = true)
	public String getEntry_people() {
		return entry_people;
	}

	public void setEntry_people(String entry_people) {
		this.entry_people = entry_people;
	}

	@Column(name = "ISSUE_DEPART_SIGNED", nullable = true)
	public String getIssue_depart_signed() {
		return issue_depart_signed;
	}

	public void setIssue_depart_signed(String issue_depart_signed) {
		this.issue_depart_signed = issue_depart_signed;
	}

	@Column(name = "KEYWORDS", nullable = true)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Id
	@Column(name = "OARECVDOCID", nullable = false, length = 32)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getOarecvdocid() {
		return oarecvdocid;
	}

	public void setOarecvdocid(String oarecvdocid) {
		this.oarecvdocid = oarecvdocid;
	}

	@Column(name = "PERSON_CONFIG_FLAG", nullable = true)
	public String getPerson_config_flag() {
		return person_config_flag;
	}

	public void setPerson_config_flag(String person_config_flag) {
		this.person_config_flag = person_config_flag;
	}

	@Column(name = "RECV_FORMID", nullable = true)
	public String getRecv_formid() {
		return recv_formid;
	}

	public void setRecv_formid(String recv_formid) {
		this.recv_formid = recv_formid;
	}

	@Column(name = "RECV_TIME", nullable = true)
	public String getRecv_time() {
		return recv_time;
	}

	public void setRecv_time(String recv_time) {
		this.recv_time = recv_time;
	}

	@Column(name = "SECRET_LVL", nullable = true)
	public String getSecret_lvl() {
		return secret_lvl;
	}

	public void setSecret_lvl(String secret_lvl) {
		this.secret_lvl = secret_lvl;
	}

	@Column(name = "WORKFLOWAUTHOR", nullable = true)
	public String getWorkflowauthor() {
		return workflowauthor;
	}

	public void setWorkflowauthor(String workflowauthor) {
		this.workflowauthor = workflowauthor;
	}

	@Column(name = "WORKFLOWCODE", nullable = true)
	public String getWorkflowcode() {
		return workflowcode;
	}

	public void setWorkflowcode(String workflowcode) {
		this.workflowcode = workflowcode;
	}

	@Column(name = "WORKFLOWNAME", nullable = true)
	public String getWorkflowname() {
		return workflowname;
	}

	public void setWorkflowname(String workflowname) {
		this.workflowname = workflowname;
	}

	@Column(name = "WORKFLOWSTATE", nullable = true)
	public String getWorkflowstate() {
		return workflowstate;
	}

	public void setWorkflowstate(String workflowstate) {
		this.workflowstate = workflowstate;
	}

	@Column(name = "WORKFLOWTITLE", nullable = true)
	public String getWorkflowtitle() {
		return workflowtitle;
	}

	public void setWorkflowtitle(String workflowtitle) {
		this.workflowtitle = workflowtitle;
	}
	@Column(name = "RECV_STATE", nullable = true)
	public String getRecv_state() {
		return recv_state;
	}

	public void setRecv_state(String recv_state) {
		this.recv_state = recv_state;
	}

	@Column(name = "ADOBE_PDF_NAME", nullable = true)
	public String getAdobe_pdf_name() {
		return adobe_pdf_name;
	}

	public void setAdobe_pdf_name(String adobe_pdf_name) {
		this.adobe_pdf_name = adobe_pdf_name;
	}

}
