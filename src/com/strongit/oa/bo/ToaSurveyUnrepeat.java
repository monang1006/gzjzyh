package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="t_oa_survey_unrepeat")
public class ToaSurveyUnrepeat implements Serializable {
		
	  @Id
	  @GeneratedValue	
	  private String unrepeatId;
	  private String  unrSurveyId;
	  
	  private String unrUserId;
	  
	  
		public ToaSurveyUnrepeat() {
		}

	public ToaSurveyUnrepeat(String unrSurveyId, String unrUserId) {
		this.unrSurveyId = unrSurveyId;
		this.unrUserId = unrUserId;
	}
	
	
	  @Id
	  @Column(name="UNREPEAT_ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getUnrepeatId() {
		return unrepeatId;
	}

	public void setUnrepeatId(String unrepeatId) {
		this.unrepeatId = unrepeatId;
	}

	@Column(name="UNR_SURVEY_ID", nullable=false)
	public String getUnrSurveyId() {
		return unrSurveyId;
	}

	public void setUnrSurveyId(String unrSurveyId) {
		this.unrSurveyId = unrSurveyId;
	}

	 @Column(name="UNR_USER_ID", nullable=false)
	public String getUnrUserId() {
		return unrUserId;
	}

	public void setUnrUserId(String unrUserId) {
		this.unrUserId = unrUserId;
	}


	  
	  

}
