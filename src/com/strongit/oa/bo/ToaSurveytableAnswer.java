package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_ANSWER")
public class ToaSurveytableAnswer implements Serializable{
	
	 @Id
	  @GeneratedValue	
	  private String answerId;
	  
	  private String answerName;
	  
	  private String answerValue;
	  
	  private int  answerCount;
	  
	  private String   answerNumber;
	  
	  private int     answerQueNumber;
	  
	  private int     answerSortid;
	  
	  private ToaSurveytableQuestion  toaSurveytableQuestion;

	  
	  
	  

	



	public ToaSurveytableAnswer(String answerId, String answerName,
			String answerValue, int answerCount, String answerNumber,
			int answerQueNumber, int answerSortid,
			ToaSurveytableQuestion toaSurveytableQuestion) {
		super();
		this.answerId = answerId;
		this.answerName = answerName;
		this.answerValue = answerValue;
		this.answerCount = answerCount;
		this.answerNumber = answerNumber;
		this.answerQueNumber = answerQueNumber;
		this.answerSortid = answerSortid;
		this.toaSurveytableQuestion = toaSurveytableQuestion;
	}



	public ToaSurveytableAnswer() {
		
	}



	 @Id
	  @Column(name="ANSWER_ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	@Column(name="ANSWER_NAME", nullable=true)
	public String getAnswerName() {
		return answerName;
	}

	public void setAnswerName(String answerName) {
		this.answerName = answerName;
	}

	@Column(name="ANSWER_VALUE", nullable=true)
	public String getAnswerValue() {
		return answerValue;
	}

	public void setAnswerValue(String answerValue) {
		this.answerValue = answerValue;
	}

	@Column(name="ANSWER_COUNT", nullable=true)
	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	

	

	  @ManyToOne
	  @JoinColumn(name="QUESTION_ID", nullable=true)
	public ToaSurveytableQuestion getToaSurveytableQuestion() {
		return toaSurveytableQuestion;
	}

	public void setToaSurveytableQuestion(
			ToaSurveytableQuestion toaSurveytableQuestion) {
		this.toaSurveytableQuestion = toaSurveytableQuestion;
	}


	@Column(name="ANSWER_NUMBER", nullable=true)
	public String getAnswerNumber() {
		return answerNumber;
	}



	public void setAnswerNumber(String answerNumber) {
		this.answerNumber = answerNumber;
	}


	@Column(name="ANSWER_QUE_NUMBER", nullable=true)
	public int getAnswerQueNumber() {
		return answerQueNumber;
	}


	public void setAnswerQueNumber(int answerQueNumber) {
		this.answerQueNumber = answerQueNumber;
	}


	@Column(name="ANSWER_SORTID", nullable=true)
	public int getAnswerSortid() {
		return answerSortid;
	}



	public void setAnswerSortid(int answerSortid) {
		this.answerSortid = answerSortid;
	}


}
