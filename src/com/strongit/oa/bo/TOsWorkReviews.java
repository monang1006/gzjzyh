package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOsWorkReviews entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OS_WORK_REVIEWS")
public class TOsWorkReviews implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String reviewsid;
	private String sendtaskId;// 下发任务id
	private String worktaskId;// 工作任务id
	private String commpleteLevel;// 完成等级
	private String reviewsDemo;// 评语内容
	private String rest1;
	private String rest2;
	private String rest3;

	// Constructors

	/** default constructor */
	public TOsWorkReviews() {
	}

	/** full constructor */
	public TOsWorkReviews(String sendtaskId, String worktaskId,
			String commpleteLevel, String reviewsDemo, String rest1,
			String rest2, String rest3) {
		this.sendtaskId = sendtaskId;
		this.worktaskId = worktaskId;
		this.commpleteLevel = commpleteLevel;
		this.reviewsDemo = reviewsDemo;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "REVIEWSID", unique = true, nullable = false, length = 32)
	public String getReviewsid() {
		return this.reviewsid;
	}

	public void setReviewsid(String reviewsid) {
		this.reviewsid = reviewsid;
	}

	@Column(name = "SENDTASK_ID", length = 32)
	public String getSendtaskId() {
		return this.sendtaskId;
	}

	public void setSendtaskId(String sendtaskId) {
		this.sendtaskId = sendtaskId;
	}

	@Column(name = "WORKTASK_ID", length = 32)
	public String getWorktaskId() {
		return this.worktaskId;
	}

	public void setWorktaskId(String worktaskId) {
		this.worktaskId = worktaskId;
	}

	@Column(name = "COMMPLETE_LEVEL", length = 2)
	public String getCommpleteLevel() {
		return this.commpleteLevel;
	}

	public void setCommpleteLevel(String commpleteLevel) {
		this.commpleteLevel = commpleteLevel;
	}

	@Column(name = "REVIEWS_DEMO", length = 1132)
	public String getReviewsDemo() {
		return this.reviewsDemo;
	}

	public void setReviewsDemo(String reviewsDemo) {
		this.reviewsDemo = reviewsDemo;
	}

	@Column(name = "REST1", length = 32)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 32)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 32)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

}
