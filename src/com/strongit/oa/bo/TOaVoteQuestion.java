package com.strongit.oa.bo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOaVoteQuestion entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="T_OA_VOTE_QUESTION")
public class TOaVoteQuestion implements java.io.Serializable {

	// Fields

	private String qid;
	private String title;
	private String type;//text,textarea,radio,checkbox,select,table
	private int showno;
	private String isRequired;
	private int maxRow ;
	private String picSize;
	private String tableHeader;
    private String tableisonly;//表格型问题是：单选，多选
	private TOaVote vote ;
    private Set<TOaVoteAnswer> answers;  
	// Constructors

	/** default constructor */
	public TOaVoteQuestion() {
	}


	/** full constructor */
	public TOaVoteQuestion(String title, String type, int showno,
			String isRequired, int maxRow, int maxSelected,
			String picSize, String tableHeader) {
		this.title = title;
		this.type = type;
		this.showno = showno;
		this.isRequired = isRequired;
		this.maxRow = maxRow;
		this.picSize = picSize;
		this.tableHeader = tableHeader;
	}

	// Property accessors
	@Id
	@Column(name="QID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getQid() {
		return this.qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}
	@Column(name="TITLE", nullable=true)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name="TYPE", nullable=true)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name="SHOW_NO", nullable=true)
	public int getShowno() {
		return this.showno;
	}

	public void setShowno(int showno) {
		this.showno = showno;
	}
	@Column(name="IS_REQUIRED", nullable=true)
	public String getIsRequired() {
		return this.isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	@Column(name="MAX_ROW", nullable=true)
	public int getMaxRow() {
		return this.maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
	@Column(name="PIC_SIZE", nullable=true)
	public String getPicSize() {
		return this.picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}
	@Column(name="TABLE_HEADER", nullable=true)
	public String getTableHeader() {
		return this.tableHeader;
	}

	public void setTableHeader(String tableHeader) {
		this.tableHeader = tableHeader;
	}
	@ManyToOne(optional=false, fetch = FetchType.LAZY)
	@JoinColumn(name="vid", nullable=true)
	public TOaVote getVote() {
		return vote;
	}

	public void setVote(TOaVote vote) {
		this.vote = vote;
	}
	@OneToMany(mappedBy="question", targetEntity=TOaVoteAnswer.class, cascade={javax.persistence.CascadeType.ALL})
	public Set<TOaVoteAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<TOaVoteAnswer> answers) {
		this.answers = answers;
	}

	@Column(name="TABLE_IS_ONLY", nullable=true)
	public String getTableisonly() {
		return tableisonly;
	}

	public void setTableisonly(String tableisonly) {
		this.tableisonly = tableisonly;
	}

}