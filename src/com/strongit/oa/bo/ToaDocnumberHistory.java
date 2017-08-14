package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_DOCNUMBER_HISTORY"
 *     
*/
@Entity
@Table(name="T_OA_DOCNUMBER_HISTORY")
public class ToaDocnumberHistory implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String docnumberHistoryid;

    /** nullable persistent field */
    private String docnumberHistoryword;

    /** nullable persistent field */
    private String docnumberHistoryyear;

    /** nullable persistent field */
    private String docnumberHistorynumber;

    /** nullable persistent field */
    private String docnumberHistoryflag;

    /** full constructor */
    public ToaDocnumberHistory(String docnumberHistoryid, String docnumberHistoryword, String docnumberHistoryyear, String docnumberHistorynumber, String docnumberHistoryflag) {
        this.docnumberHistoryid = docnumberHistoryid;
        this.docnumberHistoryword = docnumberHistoryword;
        this.docnumberHistoryyear = docnumberHistoryyear;
        this.docnumberHistorynumber = docnumberHistorynumber;
        this.docnumberHistoryflag = docnumberHistoryflag;
    }

    /** default constructor */
    public ToaDocnumberHistory() {
    }

    /** minimal constructor */
    public ToaDocnumberHistory(String docnumberHistoryid) {
        this.docnumberHistoryid = docnumberHistoryid;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOCNUMBER_HISTORYID"
     *         
     */
    @Id
	@Column(name="DOCNUMBER_HISTORYID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getDocnumberHistoryid() {
        return this.docnumberHistoryid;
    }

    public void setDocnumberHistoryid(String docnumberHistoryid) {
        this.docnumberHistoryid = docnumberHistoryid;
    }

    /** 
     *            @hibernate.property
     *             column="DOCNUMBER_HISTORYWORD"
     *             length="30"
     *         
     */
    @Column(name="DOCNUMBER_HISTORYWORD",nullable=true)
    public String getDocnumberHistoryword() {
        return this.docnumberHistoryword;
    }

    public void setDocnumberHistoryword(String docnumberHistoryword) {
        this.docnumberHistoryword = docnumberHistoryword;
    }

    /** 
     *            @hibernate.property
     *             column="DOCNUMBER_HISTORYYEAR"
     *             length="6"
     *         
     */
    @Column(name="DOCNUMBER_HISTORYYEAR",nullable=true)
    public String getDocnumberHistoryyear() {
        return this.docnumberHistoryyear;
    }

    public void setDocnumberHistoryyear(String docnumberHistoryyear) {
        this.docnumberHistoryyear = docnumberHistoryyear;
    }

    /** 
     *            @hibernate.property
     *             column="DOCNUMBER_HISTORYNUMBER"
     *             length="15"
     *         
     */
    @Column(name="DOCNUMBER_HISTORYNUMBER",nullable=true)
    public String getDocnumberHistorynumber() {
        return this.docnumberHistorynumber;
    }

    public void setDocnumberHistorynumber(String docnumberHistorynumber) {
        this.docnumberHistorynumber = docnumberHistorynumber;
    }

    /** 
     *            @hibernate.property
     *             column="DOCNUMBER_HISTORYFLAG"
     *             length="4"
     *         
     */
    @Column(name="DOCNUMBER_HISTORYFLAG",nullable=true)
    public String getDocnumberHistoryflag() {
        return this.docnumberHistoryflag;
    }

    public void setDocnumberHistoryflag(String docnumberHistoryflag) {
        this.docnumberHistoryflag = docnumberHistoryflag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("docnumberHistoryid", getDocnumberHistoryid())
            .toString();
    }

}
