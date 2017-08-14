package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INSPECT_FEEDBACKATTACH"
 *     
*/
@Entity
@Table(name="T_OA_INSPECT_FEEDBACKATTACH",schema="",catalog="")
public class ToaInspectFeedbackattach implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2538557805865585981L;

	/** identifier field */
    private String feedbackattachId;

    /** nullable persistent field */
    private byte[] content;

    /** persistent field */
    private com.strongit.oa.bo.ToaInspectFeedback toaInspectFeedback;

    /** full constructor */
    public ToaInspectFeedbackattach(String feedbackattachId, byte[] attachId4, com.strongit.oa.bo.ToaInspectFeedback toaInspectFeedback) {
        this.feedbackattachId = feedbackattachId;
        this.content = attachId4;
        this.toaInspectFeedback = toaInspectFeedback;
    }

    /** default constructor */
    public ToaInspectFeedbackattach() {
    }

    /** minimal constructor */
    public ToaInspectFeedbackattach(String feedbackattachId, com.strongit.oa.bo.ToaInspectFeedback toaInspectFeedback) {
        this.feedbackattachId = feedbackattachId;
        this.toaInspectFeedback = toaInspectFeedback;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FEEDBACKATTACH_ID"
     *         
     */
    @Id
	@Column(name="FEEDBACKATTACH_ID",nullable=false,length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getFeedbackattachId() {
        return this.feedbackattachId;
    }

    public void setFeedbackattachId(String feedbackattachId) {
        this.feedbackattachId = feedbackattachId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FEEDBACK_ID"         
     *         
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="FEEDBACK_ID")
    public com.strongit.oa.bo.ToaInspectFeedback getToaInspectFeedback() {
        return this.toaInspectFeedback;
    }

    public void setToaInspectFeedback(com.strongit.oa.bo.ToaInspectFeedback toaInspectFeedback) {
        this.toaInspectFeedback = toaInspectFeedback;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("feedbackattachId", getFeedbackattachId())
            .toString();
    }

    @Column(name="ATTACH_CONTENT")
    @Lob
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
