package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_REPORT_SHOWITEM"
 *     
*/
@Entity
@Table(name="T_OA_REPORT_SHOWITEM")
public class ToaReportShowitem implements Serializable {

    /** identifier field */
    private String showitemId;

    /** nullable persistent field */
    private String showitemText;

    /** nullable persistent field */
    private String showitemName;

    /** nullable persistent field */
    private String showitemIstotal;

    /** nullable persistent field */
    private String showitemOrderby;

    /** nullable persistent field */
    private Long showitemSequence;

    /** persistent field */
    private com.strongit.oa.bo.ToaReportDefinition toaReportDefinition;

    /** full constructor */
    public ToaReportShowitem(String showitemId, String showitemText, String showitemName, String showitemIstotal, String showitemOrderby, Long showitemSequence, com.strongit.oa.bo.ToaReportDefinition toaReportDefinition) {
        this.showitemId = showitemId;
        this.showitemText = showitemText;
        this.showitemName = showitemName;
        this.showitemIstotal = showitemIstotal;
        this.showitemOrderby = showitemOrderby;
        this.showitemSequence = showitemSequence;
        this.toaReportDefinition = toaReportDefinition;
    }

    /** default constructor */
    public ToaReportShowitem() {
    }

    /** minimal constructor */
    public ToaReportShowitem(String showitemId, com.strongit.oa.bo.ToaReportDefinition toaReportDefinition) {
        this.showitemId = showitemId;
        this.toaReportDefinition = toaReportDefinition;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SHOWITEM_ID"
     *         
     */
    @Id
	@Column(name="SHOWITEM_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getShowitemId() {
        return this.showitemId;
    }

    public void setShowitemId(String showitemId) {
        this.showitemId = showitemId;
    }

    /** 
     *            @hibernate.property
     *             column="SHOWITEM_TEXT"
     *             length="255"
     *         
     */
    @Column(name="SHOWITEM_TEXT",nullable=true)
    public String getShowitemText() {
        return this.showitemText;
    }

    public void setShowitemText(String showitemText) {
        this.showitemText = showitemText;
    }

    /** 
     *            @hibernate.property
     *             column="SHOWITEM_NAME"
     *             length="255"
     *         
     */
    @Column(name="SHOWITEM_NAME",nullable=true)
    public String getShowitemName() {
        return this.showitemName;
    }

    public void setShowitemName(String showitemName) {
        this.showitemName = showitemName;
    }

    /** 
     *            @hibernate.property
     *             column="SHOWITEM_ISTOTAL"
     *             length="1"
     *         
     */
    @Column(name="SHOWITEM_ISTOTAL",nullable=true)
    public String getShowitemIstotal() {
        return this.showitemIstotal;
    }

    public void setShowitemIstotal(String showitemIstotal) {
        this.showitemIstotal = showitemIstotal;
    }

    /** 
     *            @hibernate.property
     *             column="SHOWITEM_ORDERBY"
     *             length="10"
     *         
     */
    @Column(name="SHOWITEM_ORDERBY",nullable=true)
    public String getShowitemOrderby() {
        return this.showitemOrderby;
    }

    public void setShowitemOrderby(String showitemOrderby) {
        this.showitemOrderby = showitemOrderby;
    }

    /** 
     *            @hibernate.property
     *             column="SHOWITEM_SEQUENCE"
     *             length="10"
     *         
     */
    @Column(name="SHOWITEM_SEQUENCE",nullable=true)
    public Long getShowitemSequence() {
        return this.showitemSequence;
    }

    public void setShowitemSequence(Long showitemSequence) {
        this.showitemSequence = showitemSequence;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="DEFINITION_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="DEFINITION_ID", nullable=true)
    public com.strongit.oa.bo.ToaReportDefinition getToaReportDefinition() {
        return this.toaReportDefinition;
    }

    public void setToaReportDefinition(com.strongit.oa.bo.ToaReportDefinition toaReportDefinition) {
        this.toaReportDefinition = toaReportDefinition;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("showitemId", getShowitemId())
            .toString();
    }

}
