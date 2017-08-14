package com.strongit.oa.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_APPROVAL_SUGGESTION"
 *     
*/
@Entity
@Table(name="T_OA_APPROVAL_SUGGESTION")
public class ToaApprovalSuggestion implements Serializable {

    /** identifier field */
    private String suggestionCode;

    /** nullable persistent field */
    private String suggestionUserid;

    /** nullable persistent field */
    private String suggestionContent;

    /** nullable persistent field */
    private Date suggestionDate;
    
    private BigDecimal suggestionSeq;

    /** nullable persistent field */
    private String rest1;

    /** nullable persistent field */
    private String rest2;

    /** full constructor */
    public ToaApprovalSuggestion(String suggestionCode, String suggestionUserid, String suggestionContent, Date suggestionDate, String rest1, String rest2) {
        this.suggestionCode = suggestionCode;
        this.suggestionUserid = suggestionUserid;
        this.suggestionContent = suggestionContent;
        this.suggestionDate = suggestionDate;
        this.rest1 = rest1;
        this.rest2 = rest2;
    }

    /** default constructor */
    public ToaApprovalSuggestion() {
    }

    /** minimal constructor */
    public ToaApprovalSuggestion(String suggestionCode) {
        this.suggestionCode = suggestionCode;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SUGGESTION_CODE"
     *         
     */
    @Id
	@Column(name="SUGGESTION_CODE",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getSuggestionCode() {
        return this.suggestionCode;
    }

    public void setSuggestionCode(String suggestionCode) {
        this.suggestionCode = suggestionCode;
    }

    /** 
     *            @hibernate.property
     *             column="SUGGESTION_USERID"
     *             length="32"
     *         
     */
    @Column(name="SUGGESTION_USERID",nullable=true)
    public String getSuggestionUserid() {
        return this.suggestionUserid;
    }

    public void setSuggestionUserid(String suggestionUserid) {
        this.suggestionUserid = suggestionUserid;
    }

    /** 
     *            @hibernate.property
     *             column="SUGGESTION_CONTENT"
     *             length="100"
     *         
     */
    @Column(name="SUGGESTION_CONTENT",nullable=true)
    public String getSuggestionContent() {
        return this.suggestionContent;
    }

    public void setSuggestionContent(String suggestionContent) {
        this.suggestionContent = suggestionContent;
    }

    /** 
     *            @hibernate.property
     *             column="SUGGESTION_DATE"
     *             length="7"
     *         
     */
    @Column(name="SUGGESTION_DATE",nullable=true)
    public Date getSuggestionDate() {
        return this.suggestionDate;
    }

    public void setSuggestionDate(Date suggestionDate) {
        this.suggestionDate = suggestionDate;
    }

    /** 
     *            @hibernate.property
     *             column="REST1"
     *             length="100"
     *         
     */
    @Column(name="REST1",nullable=true)
    public String getRest1() {
        return this.rest1;
    }

    public void setRest1(String rest1) {
        this.rest1 = rest1;
    }

    /** 
     *            @hibernate.property
     *             column="REST2"
     *             length="100"
     *         
     */
    @Column(name="REST2",nullable=true)
    public String getRest2() {
        return this.rest2;
    }

    public void setRest2(String rest2) {
        this.rest2 = rest2;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("suggestionCode", getSuggestionCode())
            .toString();
    }
    
    /** 
     *            @hibernate.property
     *             column="SUGGESTION_SEQ"
     *             length="100"
     *         
     */
    @Column(name="SUGGESTION_SEQ",nullable=true)
	public BigDecimal getSuggestionSeq() {
		return suggestionSeq;
	}

	public void setSuggestionSeq(BigDecimal suggestionSeq) {
		this.suggestionSeq = suggestionSeq;
	}

}
