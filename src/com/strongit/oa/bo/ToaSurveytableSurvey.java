package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
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
@Table(name="T_OA_SURVEYTABLE_SURVEY")
public class ToaSurveytableSurvey implements Serializable {
	
	  @Id
	  @GeneratedValue	
	  private String surveyId;
	  private String surveyName;
	  private Date   surveyStartTime;
	  private Date   surveyEndTime;
	  private String surveyUnRepeat;
	  private String state;
	  private String explain;
	  private String surveyQusSort;
	  private String surveyStyleId;
	  private int    surveyCount;
	  private Set<ToaSurveytableQuestion>    toaSurveytableQuestion;
	  
	   private String   tableTitle;
	   
	   private String   isPublic;
	  

	  



	public ToaSurveytableSurvey(String surveyId, String surveyName,
			Date surveyStartTime, Date surveyEndTime, String surveyUnRepeat,
			String state, String explain, String surveyQusSort, String surveyStyleId,
			int surveyCount,String isPublic,
			Set<ToaSurveytableQuestion> toaSurveytableQuestion) {
		super();
		this.surveyId = surveyId;
		this.surveyName = surveyName;
		this.surveyStartTime = surveyStartTime;
		this.surveyEndTime = surveyEndTime;
		this.surveyUnRepeat = surveyUnRepeat;
		this.state = state;
		this.explain = explain;
		this.surveyQusSort = surveyQusSort;
		this.surveyStyleId = surveyStyleId;
		this.toaSurveytableQuestion = toaSurveytableQuestion;
		this.surveyCount = surveyCount;
		this.isPublic = isPublic;
	}

	public ToaSurveytableSurvey()
	{
		
	}
	
	  @Id
	  @Column(name="SURVEY_ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	
	  @Column(name="SURVEY_NAME", nullable=false)
	public String getSurveyName() {
		return surveyName;
	}
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	
	 @Column(name="SURVEY_STARTTIME", nullable=true)
	public Date getSurveyStartTime() {
		return surveyStartTime;
	}
	public void setSurveyStartTime(Date surveyStartTime) {
		this.surveyStartTime = surveyStartTime;
	}
	 @Column(name="SURVEY_ENDTIME", nullable=true)
	public Date getSurveyEndTime() {
		return surveyEndTime;
	}
	public void setSurveyEndTime(Date surveyEndTime) {
		this.surveyEndTime = surveyEndTime;
	}
	 @Column(name="SURVEY_UNREPEAT", nullable=true)
	public String getSurveyUnRepeat() {
		return surveyUnRepeat;
	}
	public void setSurveyUnRepeat(String surveyUnRepeat) {
		this.surveyUnRepeat = surveyUnRepeat;
	}
	 @Column(name="SURVEY_STATE", nullable=true)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	 @Column(name="SURVEY_EXPLAIN", nullable=true)
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}

	
	
	 @Column(name="SURVEY_QUS_SORT", nullable=true)
	 public String getSurveyQusSort() {
		return surveyQusSort;
	}
	public void setSurveyQusSort(String surveyQusSort) {
		this.surveyQusSort = surveyQusSort;
	}
	

	
	@Column(name="SURVEY_STYLE_ID", nullable=true)
   	public String getSurveyStyleId() {
		return surveyStyleId;
	}

	public void setSurveyStyleId(String surveyStyleId) {
		this.surveyStyleId = surveyStyleId;
	}
	 
	 
	 

	@OneToMany(mappedBy="toaSurveytableSurvey", targetEntity=ToaSurveytableQuestion.class, cascade={javax.persistence.CascadeType.ALL})
	public Set getToaSurveytableQuestion() {
		return toaSurveytableQuestion;
	}
	public void setToaSurveytableQuestion(Set toaSurveytableQuestion) {
		this.toaSurveytableQuestion = toaSurveytableQuestion;
	}
	
	
	
	
	 @Column(name="SURVEY_COUNT", nullable=true)
	public int getSurveyCount() {
		return surveyCount;
	}

	public void setSurveyCount(int surveyCount) {
		this.surveyCount = surveyCount;
	}
	
	@Column(name="TABLE_TITLE", nullable=true) 
	public String getTableTitle() {
		return tableTitle;
	}

	public void setTableTitle(String tableTitle) {
		this.tableTitle = tableTitle;
	}

	public String toString() {
	    return new ToStringBuilder(this).append("surveyId", getSurveyId()).toString();
	  }

	@Column(name="IS_PUBLIC", nullable=true)
	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}


	
	  
	  
	

}
