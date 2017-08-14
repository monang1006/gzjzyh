package com.strongit.oa.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TOaVoteAnswer entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="T_OA_VOTE_ANSWER")
public class TOaVoteAnswer implements java.io.Serializable {

	// Fields

	private String aid;
	private String content;
	private String picPath;
	private String url;
	private int count;
	private int showno ;
	private TOaVoteQuestion question;
    
	// Constructors

	/** default constructor */
	public TOaVoteAnswer() {
	}

	/** minimal constructor */
	public TOaVoteAnswer(String content) {
		this.content = content;
	}

	/** full constructor */
	public TOaVoteAnswer(String content, String picPath, String url,
			int count) {
		this.content = content;
		this.picPath = picPath;
		this.url = url;
		this.count = count;
	}

	// Property accessors
	@Id
	@Column(name="AID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getAid() {
		return this.aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}
	@Column(name="CONTENT", nullable=true)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	@Column(name="PIC_PATH", nullable=true)
	public String getPicPath() {
		return this.picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	@Column(name="URL", nullable=true)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name="COUNT", nullable=true)
	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	@ManyToOne(optional=false, fetch = FetchType.LAZY)
	@JoinColumn(name="qid", nullable=true)
	public TOaVoteQuestion getQuestion() {
		return question;
	}

	public void setQuestion(TOaVoteQuestion question) {
		this.question = question;
	}
	@Column(name="SHOW_NO", nullable=true)
	public int getShowno() {
		return showno;
	}
	public void setShowno(int showno) {
		this.showno = showno;
	}


}