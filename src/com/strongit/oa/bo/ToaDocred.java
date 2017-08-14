package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
 *         table="T_OA_REDSTEMPLATE"
 *     
*/
@Entity
@Table(name="T_OA_REDSTEMPLATE",catalog="",schema="")
public class ToaDocred implements Serializable {

    /** identifier field */
	@Id 
	@GeneratedValue
    private String redstempId;

    /** nullable persistent field */
    private String redstempTitle;

    /** nullable persistent field */
    private byte[] redstempContent;

    /** nullable persistent field */
    private Date redstempCreateTime;

    /** nullable persistent field */
    private String redstempRemark;

    /** persistent field */
    private com.strongit.oa.bo.ToaDocredGroup toaDocredGroup;

    /** full constructor */
    public ToaDocred(String redstempId, String redstempTitle, byte[] redstempContent, Date redstempCreateTime, String redstempRemark, com.strongit.oa.bo.ToaDocredGroup toaDocredGroup) {
        this.redstempId = redstempId;
        this.redstempTitle = redstempTitle;
        this.redstempContent = redstempContent;
        this.redstempCreateTime = redstempCreateTime;
        this.redstempRemark = redstempRemark;
        this.toaDocredGroup = toaDocredGroup;
    }

    /** default constructor */
    public ToaDocred() {
    }

    /** minimal constructor */
    public ToaDocred(String redstempId, com.strongit.oa.bo.ToaDocredGroup toaDocredGroup) {
        this.redstempId = redstempId;
        this.toaDocredGroup = toaDocredGroup;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="REDSTEMP_ID"
     *         
     */
    @Id
	@Column(name="REDSTEMP_ID",nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getRedstempId() {
        return this.redstempId;
    }

    public void setRedstempId(String redstempId) {
        this.redstempId = redstempId;
    }

    /** 
     *            @hibernate.property
     *             column="REDSTEMP_TITLE"
     *             length="100"
     *         
     */
    @Column(name="REDSTEMP_TITLE",nullable=true)
    public String getRedstempTitle() {
        return this.redstempTitle;
    }

    public void setRedstempTitle(String redstempTitle) {
        this.redstempTitle = redstempTitle;
    }

    /** 
     *            @hibernate.property
     *             column="REDSTEMP_CONTENT"
     *             length="4000"
     *         
     */
    @Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "REDSTEMP_CONTENT", columnDefinition = "BLOB")
    public byte[] getRedstempContent() {
        return this.redstempContent;
    }

    public void setRedstempContent(byte[] redstempContent) {
        this.redstempContent = redstempContent;
    }

    /** 
     *            @hibernate.property
     *             column="REDSTEMP_CREATE_TIME"
     *             length="7"
     *         
     */
    @Column(name="REDSTEMP_CREATE_TIME",nullable=true)
    public Date getRedstempCreateTime() {
        return this.redstempCreateTime;
    }

    public void setRedstempCreateTime(Date redstempCreateTime) {
        this.redstempCreateTime = redstempCreateTime;
    }

    /** 
     *            @hibernate.property
     *             column="REDSTEMP_REMARK"
     *             length="4000"
     *         
     */
    @Column(name="REDSTEMP_REMARK",nullable=true)
    public String getRedstempRemark() {
        return this.redstempRemark;
    }

    public void setRedstempRemark(String redstempRemark) {
        this.redstempRemark = redstempRemark;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="REDTEMP_GROUP_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="REDTEMP_GROUP_ID", nullable=false)
    public com.strongit.oa.bo.ToaDocredGroup getToaDocredGroup() {
        return this.toaDocredGroup;
    }

    public void setToaDocredGroup(com.strongit.oa.bo.ToaDocredGroup toaDocredGroup) {
        this.toaDocredGroup = toaDocredGroup;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("redstempId", getRedstempId())
            .toString();
    }

}
