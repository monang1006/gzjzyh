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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;

/**
 * @hibernate.class table="T_OA_MEETING_TOPIC"
 * 
 */
@Entity
@Table(name = "T_OA_MEETING_TOPIC", catalog = "", schema = "")
public class ToaMeetingTopic implements Serializable {

	/** identifier field */
	private String topicId;

	/** nullable persistent field */
	private String topicCode;

	/** nullable persistent field */
	private String topicSubject;

	/** nullable persistent field */
	private String topicContent;

	/** nullable persistent field */
	private String topicDemo;

	/** nullable persistent field */
	private Date topicEstime;

	/** nullable persistent field */
	private String topicCreater;

	/** nullable persistent field */
	private String topicStatus;

	private String processInstanceId;

	private String departmentId;// DEPARTMENT_ID

	private String topOrgcode;// TOP_ORGCODE

	private String rest1;

	private String rest2;

	/** persistent field */
	private String conferenceId;

	/** persistent field */
	private com.strongit.oa.bo.ToaMeetingTopicsort topicsort;

	/** full constructor */
	public ToaMeetingTopic(String topicId, String topicCode,
			String topicSubject, String topicContent, String topicDemo,
			Date topicEstime, String topicCreater, String topicStatus,
			String processInstanceId, String departmentId, String topOrgcode,
			String rest1, String rest2, String conferenceId,
			ToaMeetingTopicsort topicsort) {
		super();
		this.topicId = topicId;
		this.topicCode = topicCode;
		this.topicSubject = topicSubject;
		this.topicContent = topicContent;
		this.topicDemo = topicDemo;
		this.topicEstime = topicEstime;
		this.topicCreater = topicCreater;
		this.topicStatus = topicStatus;
		this.processInstanceId = processInstanceId;
		this.departmentId = departmentId;
		this.topOrgcode = topOrgcode;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.conferenceId = conferenceId;
		this.topicsort = topicsort;
	}

	/** default constructor */
	public ToaMeetingTopic() {
	}

	/** minimal constructor */
	public ToaMeetingTopic(String topicId,
			com.strongit.oa.bo.ToaMeetingTopicsort toaMeetingTopicsort) {
		this.topicId = topicId;
		this.topicsort = toaMeetingTopicsort;
	}

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String"
	 *               column="TOPIC_ID"
	 * 
	 */
	@Id
	@Column(name = "TOPIC_ID", nullable = false)
	@GeneratedValue(generator = "hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getTopicId() {
		return this.topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	/**
	 * @hibernate.property column="TOPIC_CODE" length="32"
	 * 
	 */
	@Column(name = "TOPIC_CODE")
	public String getTopicCode() {
		return this.topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	/**
	 * @hibernate.property column="TOPIC_SUBJECT" length="50"
	 * 
	 */
	@Column(name = "TOPIC_SUBJECT")
	public String getTopicSubject() {
		return this.topicSubject;
	}

	public void setTopicSubject(String topicSubject) {
		this.topicSubject = topicSubject;
	}

	/**
	 * @hibernate.property column="TOPIC_CONTENT" length="100"
	 * 
	 */

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TOPIC_CONTENT", columnDefinition = "CLOB", nullable = true)
	public String getTopicContent() {
		return this.topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}

	/**
	 * @hibernate.property column="TOPIC_DEMO" length="4000"
	 * 
	 */
	@Column(name = "TOPIC_DEMO")
	public String getTopicDemo() {
		return this.topicDemo;
	}

	public void setTopicDemo(String topicDemo) {
		this.topicDemo = topicDemo;
	}

	/**
	 * @hibernate.property column="TOPIC_ESTIME" length="7"
	 * 
	 */
	@Column(name = "TOPIC_ESTIME")
	public Date getTopicEstime() {
		return this.topicEstime;
	}

	public void setTopicEstime(Date topicEstime) {
		this.topicEstime = topicEstime;
	}

	/**
	 * @hibernate.property column="TOPIC_CREATER" length="20"
	 * 
	 */
	@Column(name = "TOPIC_CREATER")
	public String getTopicCreater() {
		return this.topicCreater;
	}

	public void setTopicCreater(String topicCreater) {
		this.topicCreater = topicCreater;
	}

	/**
	 * @hibernate.property column="TOPIC_STATUS" length="10"
	 * 
	 */
	@Column(name = "TOPIC_STATUS")
	public String getTopicStatus() {
		return this.topicStatus;
	}

	public void setTopicStatus(String topicStatus) {
		this.topicStatus = topicStatus;
	}

	public String toString() {
		return new ToStringBuilder(this).append("topicId", getTopicId())
				.toString();
	}

	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="TOPICSORT_ID"
	 * 
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TOPICSORT_ID")
	public com.strongit.oa.bo.ToaMeetingTopicsort getTopicsort() {
		return topicsort;
	}

	public void setTopicsort(com.strongit.oa.bo.ToaMeetingTopicsort topicsort) {
		this.topicsort = topicsort;
	}

	@Column(name = "PROCESS_INSTANCEID")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	@Column(name = "DEPARTMENT_ID")
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "TOP_ORGCODE")
	public String getTopOrgcode() {
		return topOrgcode;
	}

	public void setTopOrgcode(String topOrgcode) {
		this.topOrgcode = topOrgcode;
	}

	@Column(name = "REST1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "CONFERENCE_ID")
	public String getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}

}
