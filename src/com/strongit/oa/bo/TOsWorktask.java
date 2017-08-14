package com.strongit.oa.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOsWorktask entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_OS_WORKTASK")
public class TOsWorktask implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private String worktaskId;
	private String worktaskTitle;// 工作标题
	private String worktaskType;// 工作类型
	private String worktaskNo;// 工作编号
	private Date worktaskStime;// 办理期限开始日期
	private Date worktaskEtime;// 办理期限结束日期
	private String worktaskUser;// 发起人
	private String worktaskUnit;// 发起单位
	private String worktaskContent;// 工作内容
	private String worktaskPerson;// 录入人
	private Date worktaskEntryTime;// 录入时间
	private String rest1;
	private String rest2;
	private String rest3;
	private String worktaskEmerlevel;// 紧急程度
	private int managetProgress;// 工作进度
	private Date managetTime;// 办理时间
	private String managetState;// 工作状态
	private String managetDemo;// 回复内容
	private String commpleteLevel;// 完成等级
	private String reviewsDemo;// 评语内容
	private Set<TOsWorktaskSend> TOsWorktaskSends = new HashSet<TOsWorktaskSend>(
			0);

	private String worktaskUserName;// 发起人名称
	private String worktaskSender;// 承办情况
	private String worktaskUnitName;// 发起单位名称

	// Constructors

	/** default constructor */
	public TOsWorktask() {
	}

	/** minimal constructor */
	public TOsWorktask(String worktaskTitle) {
		this.worktaskTitle = worktaskTitle;
	}

	/** full constructor */
	public TOsWorktask(String worktaskTitle, String worktaskType,
			String worktaskNo, Date worktaskStime, Date worktaskEtime,
			String worktaskUser, String worktaskUnit, String worktaskContent,
			String worktaskPerson, Date worktaskEntryTime, String rest1,
			String rest2, String rest3, String worktaskEmerlevel,
			int managetProgress, Date managetTime, String managetState,
			String managetDemo, String commpleteLevel, String reviewsDemo,
			Set TOsWorktaskSends) {
		this.worktaskTitle = worktaskTitle;
		this.worktaskType = worktaskType;
		this.worktaskNo = worktaskNo;
		this.worktaskStime = worktaskStime;
		this.worktaskEtime = worktaskEtime;
		this.worktaskUser = worktaskUser;
		this.worktaskUnit = worktaskUnit;
		this.worktaskContent = worktaskContent;
		this.worktaskPerson = worktaskPerson;
		this.worktaskEntryTime = worktaskEntryTime;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
		this.worktaskEmerlevel = worktaskEmerlevel;
		this.managetProgress = managetProgress;
		this.managetTime = managetTime;
		this.managetState = managetState;
		this.managetDemo = managetDemo;
		this.commpleteLevel = commpleteLevel;
		this.reviewsDemo = reviewsDemo;
		this.TOsWorktaskSends = TOsWorktaskSends;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "WORKTASK_ID", unique = true, nullable = false, length = 32)
	public String getWorktaskId() {
		return this.worktaskId;
	}

	public void setWorktaskId(String worktaskId) {
		this.worktaskId = worktaskId;
	}

	@Column(name = "WORKTASK_TITLE", nullable = false, length = 432)
	public String getWorktaskTitle() {
		return this.worktaskTitle;
	}

	public void setWorktaskTitle(String worktaskTitle) {
		this.worktaskTitle = worktaskTitle;
	}

	@Column(name = "WORKTASK_TYPE", length = 132)
	public String getWorktaskType() {
		return this.worktaskType;
	}

	public void setWorktaskType(String worktaskType) {
		this.worktaskType = worktaskType;
	}

	@Column(name = "WORKTASK_NO", length = 32)
	public String getWorktaskNo() {
		return this.worktaskNo;
	}

	public void setWorktaskNo(String worktaskNo) {
		this.worktaskNo = worktaskNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WORKTASK_STIME", length = 7)
	public Date getWorktaskStime() {
		return this.worktaskStime;
	}

	public void setWorktaskStime(Date worktaskStime) {
		this.worktaskStime = worktaskStime;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "WORKTASK_ETIME", length = 7)
	public Date getWorktaskEtime() {
		return this.worktaskEtime;
	}

	public void setWorktaskEtime(Date worktaskEtime) {
		this.worktaskEtime = worktaskEtime;
	}

	@Column(name = "WORKTASK_USER", length = 132)
	public String getWorktaskUser() {
		return this.worktaskUser;
	}

	public void setWorktaskUser(String worktaskUser) {
		this.worktaskUser = worktaskUser;
	}

	@Column(name = "WORKTASK_UNIT", length = 232)
	public String getWorktaskUnit() {
		return this.worktaskUnit;
	}

	public void setWorktaskUnit(String worktaskUnit) {
		this.worktaskUnit = worktaskUnit;
	}

	@Column(name = "WORKTASK_CONTENT")
	public String getWorktaskContent() {
		return this.worktaskContent;
	}

	public void setWorktaskContent(String worktaskContent) {
		this.worktaskContent = worktaskContent;
	}

	@Column(name = "WORKTASK_PERSON", length = 45)
	public String getWorktaskPerson() {
		return this.worktaskPerson;
	}

	public void setWorktaskPerson(String worktaskPerson) {
		this.worktaskPerson = worktaskPerson;
	}

	@Column(name = "WORKTASK_ENTRY_TIME", length = 7)
	public Date getWorktaskEntryTime() {
		return this.worktaskEntryTime;
	}

	public void setWorktaskEntryTime(Date worktaskEntryTime) {
		this.worktaskEntryTime = worktaskEntryTime;
	}

	@Column(name = "REST1", length = 104)
	public String getRest1() {
		return this.rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2", length = 1024)
	public String getRest2() {
		return this.rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "REST3", length = 134)
	public String getRest3() {
		return this.rest3;
	}

	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}

	@Column(name = "WORKTASK_EMERLEVEL", length = 4)
	public String getWorktaskEmerlevel() {
		return this.worktaskEmerlevel;
	}

	public void setWorktaskEmerlevel(String worktaskEmerlevel) {
		this.worktaskEmerlevel = worktaskEmerlevel;
	}

	@Column(name = "MANAGET_PROGRESS")
	public int getManagetProgress() {
		return this.managetProgress;
	}

	public void setManagetProgress(int managetProgress) {
		this.managetProgress = managetProgress;
	}

	@Column(name = "MANAGET_TIME", length = 7)
	public Date getManagetTime() {
		return this.managetTime;
	}

	public void setManagetTime(Date managetTime) {
		this.managetTime = managetTime;
	}

	@Column(name = "MANAGET_STATE", length = 4)
	public String getManagetState() {
		return this.managetState;
	}

	public void setManagetState(String managetState) {
		this.managetState = managetState;
	}

	@Column(name = "MANAGET_DEMO", length = 4000)
	public String getManagetDemo() {
		return this.managetDemo;
	}

	public void setManagetDemo(String managetDemo) {
		this.managetDemo = managetDemo;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "TOsWorktask")
	public Set<TOsWorktaskSend> getTOsWorktaskSends() {
		return this.TOsWorktaskSends;
	}

	public void setTOsWorktaskSends(Set<TOsWorktaskSend> TOsWorktaskSends) {
		this.TOsWorktaskSends = TOsWorktaskSends;
	}

	@Transient
	public String getWorktaskUserName() {
		return worktaskUserName;
	}

	public void setWorktaskUserName(String worktaskUserName) {
		this.worktaskUserName = worktaskUserName;
	}

	@Transient
	public String getWorktaskSender() {
		return worktaskSender;
	}

	public void setWorktaskSender(String worktaskSender) {
		this.worktaskSender = worktaskSender;
	}

	@Transient
	public String getWorktaskUnitName() {
		return worktaskUnitName;
	}

	public void setWorktaskUnitName(String worktaskUnitName) {
		this.worktaskUnitName = worktaskUnitName;
	}

}
