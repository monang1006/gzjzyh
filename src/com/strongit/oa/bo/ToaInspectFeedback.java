package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INSPECT_FEEDBACK"
 *     
*/
@Entity
@Table(name="T_OA_INSPECT_FEEDBACK",schema="",catalog="")
public class ToaInspectFeedback implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3900387148348161566L;

	/** identifier field */
    private String feedbackId;

    /** nullable persistent field */
    private String feedbackBussinessCode;

    /** nullable persistent field */
    private String feedbackDepart;

    /** nullable persistent field */
    private Date feedbackProcDate;

    /** nullable persistent field */
    private String feedbackProcPeriod;

    /** nullable persistent field */
    private String feedbackResult;

    /** persistent field */
    private Set<ToaInspectFeedbackattach> toaInspectFeedbackattaches;

    /** full constructor */
    public ToaInspectFeedback(String feedbackId, String feedbackBussinessCode, String feedbackDepart, Date feedbackProcDate, String feedbackProcPeriod, String feedbackResult, Set<ToaInspectFeedbackattach> toaInspectFeedbackattaches) {
        this.feedbackId = feedbackId;
        this.feedbackBussinessCode = feedbackBussinessCode;
        this.feedbackDepart = feedbackDepart;
        this.feedbackProcDate = feedbackProcDate;
        this.feedbackProcPeriod = feedbackProcPeriod;
        this.feedbackResult = feedbackResult;
        this.toaInspectFeedbackattaches = toaInspectFeedbackattaches;
    }

    /** default constructor */
    public ToaInspectFeedback() {
    }

    /** minimal constructor */
    public ToaInspectFeedback(String feedbackId, Set<ToaInspectFeedbackattach> toaInspectFeedbackattaches) {
        this.feedbackId = feedbackId;
        this.toaInspectFeedbackattaches = toaInspectFeedbackattaches;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FEEDBACK_ID"
     *         
     */
    @Id
	@Column(name="FEEDBACK_ID",nullable=false,length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getFeedbackId() {
        return this.feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    /** 
     *            @hibernate.property
     *             column="FEEDBACK_BUSSINESS_CODE"
     *             length="50"
     *         
     */
    @Column(name="FEEDBACK_BUSSINESS_CODE")
    public String getFeedbackBussinessCode() {
        return this.feedbackBussinessCode;
    }

    public void setFeedbackBussinessCode(String feedbackBussinessCode) {
        this.feedbackBussinessCode = feedbackBussinessCode;
    }

    /** 
     *            @hibernate.property
     *             column="FEEDBACK_DEPART"
     *             length="50"
     *         
     */
    @Column(name="FEEDBACK_DEPART")
    public String getFeedbackDepart() {
        return this.feedbackDepart;
    }

    public void setFeedbackDepart(String feedbackDepart) {
        this.feedbackDepart = feedbackDepart;
    }

    /** 
     *            @hibernate.property
     *             column="FEEDBACK_PROC_DATE"
     *             length="7"
     *         
     */
    @Column(name="FEEDBACK_PROC_DATE")
    public Date getFeedbackProcDate() {
        return this.feedbackProcDate;
    }

    public void setFeedbackProcDate(Date feedbackProcDate) {
        this.feedbackProcDate = feedbackProcDate;
    }

    /** 
     *            @hibernate.property
     *             column="FEEDBACK_PROC_PERIOD"
     *             length="50"
     *         
     */
    @Column(name="FEEDBACK_PROC_PERIOD")
    public String getFeedbackProcPeriod() {
        return this.feedbackProcPeriod;
    }

    public void setFeedbackProcPeriod(String feedbackProcPeriod) {
        this.feedbackProcPeriod = feedbackProcPeriod;
    }

    /** 
     *            @hibernate.property
     *             column="FEEDBACK_RESULT"
     *             length="4000"
     *         
     */
    @Column(name="FEEDBACK_RESULT")
    public String getFeedbackResult() {
        return this.feedbackResult;
    }

    public void setFeedbackResult(String feedbackResult) {
        this.feedbackResult = feedbackResult;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FEEDBACK_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaInspectFeedbackattach"
     *         
     */
    @OneToMany(mappedBy="toaInspectFeedback",fetch=FetchType.LAZY)
    public Set<ToaInspectFeedbackattach> getToaInspectFeedbackattaches() {
        return this.toaInspectFeedbackattaches;
    }

    public void setToaInspectFeedbackattaches(Set<ToaInspectFeedbackattach> toaInspectFeedbackattaches) {
        this.toaInspectFeedbackattaches = toaInspectFeedbackattaches;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("feedbackId", getFeedbackId())
            .toString();
    }

}
