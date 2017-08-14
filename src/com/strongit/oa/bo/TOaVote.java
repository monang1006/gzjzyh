package com.strongit.oa.bo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOaVote entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="T_OA_VOTE")
public class TOaVote implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue	
	private String vid;
	private String styleId;
	private String title;
	private String memo;
	private Date startDate;
	private Date endDate;
	private String isPrivate;//限制查看结果
	private String state;
	private String isRepeated;//限制重复参与
	private String isRealname;//实名参与
	private String type;
    private Set<TOaVoteQuestion> questions;
	// Constructors

	/** default constructor */
	public TOaVote() {
	}

	/** full constructor */
	public TOaVote(String styleId, String title, String memo,
			Date startDate, Date endDate, String isPrivate, String state,
			String isRepeated, String isRealname, String type) {
		this.styleId = styleId;
		this.title = title;
		this.memo = memo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isPrivate = isPrivate;
		this.state = state;
		this.isRepeated = isRepeated;
		this.isRealname = isRealname;
		this.type = type;
	}

	// Property accessors
	@Id
	@Column(name="VID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getVid() {
		return this.vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	@Column(name="STYLE_ID", nullable=true)
	public String getStyleId() {
		return styleId;
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	@Column(name="TITLE", nullable=true)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name="MEMO", nullable=true)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Column(name="START_DATE", nullable=true)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@Column(name="END_DATE", nullable=true)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name="IS_PRIVATE", nullable=true)
	public String getIsPrivate() {
		return this.isPrivate;
	}

	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}
	@Column(name="STATE", nullable=true)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}
	@Column(name="IS_REPEATED", nullable=true)
	public String getIsRepeated() {
		return this.isRepeated;
	}

	public void setIsRepeated(String isRepeated) {
		this.isRepeated = isRepeated;
	}
	@Column(name="IS_REALNAME", nullable=true)
	public String getIsRealname() {
		return this.isRealname;
	}

	public void setIsRealname(String isRealname) {
		this.isRealname = isRealname;
	}
	@Column(name="TYPE", nullable=true)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@OneToMany(mappedBy="vote", targetEntity=TOaVoteQuestion.class, cascade={javax.persistence.CascadeType.ALL})
	public Set<TOaVoteQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<TOaVoteQuestion> questions) {
		this.questions = questions;
	}

}