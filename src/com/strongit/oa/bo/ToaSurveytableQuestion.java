package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_SURVEYTABLE_QUESTION")
public class ToaSurveytableQuestion implements Serializable {
	
	 @Id
	  @GeneratedValue	
	   
	   private String   questionId ;
	   
	   private String   questionName;
	   
	   private String   questionTrue;
	   
	   private String   questionType;
	   
	   private int   questionNumber;
	   
	   private String   questionSurveyId;
	   
	
	   
	   private ToaSurveytableSurvey  toaSurveytableSurvey;
	   
	   private Set  toaSurveytableAnswer;



	public ToaSurveytableQuestion(String questionId, String questionName,
			String questionTrue, String questionType, int questionNumber,
			String questionSurveyId, ToaSurveytableSurvey toaSurveytableSurvey,
			Set toaSurveytableAnswer) {
		super();
		this.questionId = questionId;
		this.questionName = questionName;
		this.questionTrue = questionTrue;
		this.questionType = questionType;
		this.questionNumber = questionNumber;
		this.questionSurveyId = questionSurveyId;
		this.toaSurveytableSurvey = toaSurveytableSurvey;
		this.toaSurveytableAnswer = toaSurveytableAnswer;
	}

	public ToaSurveytableQuestion() {
		
	}

	  @Id
	  @Column(name="QUESTION_ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	 @Column(name="QUESTION_NAME", nullable=true)
	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	@Column(name="QUESTION_TRUE", nullable=true)
	public String getQuestionTrue() {
		return questionTrue;
	}

	public void setQuestionTrue(String questionTrue) {
		this.questionTrue = questionTrue;
	}

	@Column(name="QUESTION_TYPE", nullable=true)
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	@Column(name="QUESTION_NUMBER", nullable=true)
	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	  @ManyToOne
	  @JoinColumn(name="SURVEY_ID", nullable=true)
	public ToaSurveytableSurvey getToaSurveytableSurvey() {
		return toaSurveytableSurvey;
	}

	public void setToaSurveytableSurvey(ToaSurveytableSurvey toaSurveytableSurvey) {
		this.toaSurveytableSurvey = toaSurveytableSurvey;
	}
	   
	
	@Column(name="QUESTION_SURVEY_ID", nullable=true)   
	public String getQuestionSurveyId() {
		return questionSurveyId;
	}

	public void setQuestionSurveyId(String questionSurveyId) {
		this.questionSurveyId = questionSurveyId;
	}
	
	
	
	
 @OneToMany(mappedBy="toaSurveytableQuestion", targetEntity=ToaSurveytableAnswer.class, cascade={javax.persistence.CascadeType.ALL})
	public Set getToaSurveytableAnswer() {
		return toaSurveytableAnswer;
	}

	public void setToaSurveytableAnswer(Set toaSurveytableAnswer) {
		this.toaSurveytableAnswer = toaSurveytableAnswer;
	}

	public String toString() {
	    return new ToStringBuilder(this).append("questionEd", getQuestionId()).toString();
	  }
	
	   
	   

}
