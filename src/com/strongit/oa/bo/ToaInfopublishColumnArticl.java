package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;


/** 
 *        @hibernate.class
 *         table="T_OA_INFOPUBLISH_COLUMN_ARTICL"
 *     
*/
@Entity
@Table(name = "T_OA_INFOPUBLISH_COLUMN_ARTICL")
public class ToaInfopublishColumnArticl implements Serializable {

    /** identifier field */
	@Id
	@GeneratedValue
    private String columnArticleId;

    /** nullable persistent field */
    private String columnArticleState;

    /** nullable persistent field */
    private String columnArticleIsstandtop;

    /** nullable persistent field */
    private String columnArticleGuidetype;

    /** nullable persistent field */
    private String columnArticleSequence;

    /** nullable persistent field */
    private Date columnArticleAddtime;

    /** nullable persistent field */
    private String columnArticleAdduser;

    /** nullable persistent field */
    private Date columnArticleRemovetime;

    /** nullable persistent field */
    private String columnArticleRemoveuser;

    /** nullable persistent field */
    private String columnArticleOldaticlestate;

    /** nullable persistent field */
    private Date columnArticleLatestchangtime;

    /** nullable persistent field */
    private String columnArticleLatestuser;
    
    /** nullable persistent field */
    private String processInstanceId;

    /** persistent field */
    private com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle;

    /** persistent field */
    private com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn;
    
    private int countComment;//评论数

    /** full constructor */
    public ToaInfopublishColumnArticl(String columnArticleId, String columnArticleState, String columnArticleIsstandtop, String columnArticleGuidetype, String columnArticleSequence, Date columnArticleAddtime, String columnArticleAdduser, Date columnArticleRemovetime, String columnArticleRemoveuser, String columnArticleOldaticlestate, Date columnArticleLatestchangtime, String columnArticleLatestuser,String processInstanceId, com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle, com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn) {
        this.columnArticleId = columnArticleId;
        this.columnArticleState = columnArticleState;
        this.columnArticleIsstandtop = columnArticleIsstandtop;
        this.columnArticleGuidetype = columnArticleGuidetype;
        this.columnArticleSequence = columnArticleSequence;
        this.columnArticleAddtime = columnArticleAddtime;
        this.columnArticleAdduser = columnArticleAdduser;
        this.columnArticleRemovetime = columnArticleRemovetime;
        this.columnArticleRemoveuser = columnArticleRemoveuser;
        this.columnArticleOldaticlestate = columnArticleOldaticlestate;
        this.columnArticleLatestchangtime = columnArticleLatestchangtime;
        this.columnArticleLatestuser = columnArticleLatestuser;
        this.processInstanceId = processInstanceId;
        this.toaInfopublishArticle = toaInfopublishArticle;
        this.toaInfopublishColumn = toaInfopublishColumn;
    }

    /** default constructor */
    public ToaInfopublishColumnArticl() {
    }

    /** minimal constructor */
    public ToaInfopublishColumnArticl(String columnArticleId, com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle, com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn) {
        this.columnArticleId = columnArticleId;
        this.toaInfopublishArticle = toaInfopublishArticle;
        this.toaInfopublishColumn = toaInfopublishColumn;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="COLUMN_ARTICLE_ID"
     *         
     */
    @Id
    @Column(name = "COLUMN_ARTICLE_ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getColumnArticleId() {
        return this.columnArticleId;
    }

    public void setColumnArticleId(String columnArticleId) {
        this.columnArticleId = columnArticleId;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_STATE"
     *             length="1"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_STATE", nullable = true)
    public String getColumnArticleState() {
        return this.columnArticleState;
    }

    public void setColumnArticleState(String columnArticleState) {
        this.columnArticleState = columnArticleState;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE__ISSTANDTOP"
     *             length="1"
     *         
     */
    @Column(name = "COLUMN_ARTICLE__ISSTANDTOP", nullable = true)
    public String getColumnArticleIsstandtop() {
        return this.columnArticleIsstandtop;
    }

    public void setColumnArticleIsstandtop(String columnArticleIsstandtop) {
        this.columnArticleIsstandtop = columnArticleIsstandtop;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_GUIDETYPE"
     *             length="1"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_GUIDETYPE", nullable = true)
    public String getColumnArticleGuidetype() {
        return this.columnArticleGuidetype;
    }

    public void setColumnArticleGuidetype(String columnArticleGuidetype) {
        this.columnArticleGuidetype = columnArticleGuidetype;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_SEQUENCE"
     *             length="5"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_SEQUENCE", nullable = true)
    public String getColumnArticleSequence() {
        return this.columnArticleSequence;
    }

    public void setColumnArticleSequence(String columnArticleSequence) {
        this.columnArticleSequence = columnArticleSequence;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_ADDTIME"
     *             length="7"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_ADDTIME", nullable = true)
    public Date getColumnArticleAddtime() {
        return this.columnArticleAddtime;
    }

    public void setColumnArticleAddtime(Date columnArticleAddtime) {
        this.columnArticleAddtime = columnArticleAddtime;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_ADDUSER"
     *             length="25"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_ADDUSER", nullable = true)
    public String getColumnArticleAdduser() {
        return this.columnArticleAdduser;
    }

    public void setColumnArticleAdduser(String columnArticleAdduser) {
        this.columnArticleAdduser = columnArticleAdduser;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_REMOVETIME"
     *             length="7"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_REMOVETIME", nullable = true)
    public Date getColumnArticleRemovetime() {
        return this.columnArticleRemovetime;
    }

    public void setColumnArticleRemovetime(Date columnArticleRemovetime) {
        this.columnArticleRemovetime = columnArticleRemovetime;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_REMOVEUSER"
     *             length="25"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_REMOVEUSER", nullable = true)
    public String getColumnArticleRemoveuser() {
        return this.columnArticleRemoveuser;
    }

    public void setColumnArticleRemoveuser(String columnArticleRemoveuser) {
        this.columnArticleRemoveuser = columnArticleRemoveuser;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_OLDATICLESTATE"
     *             length="1"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_OLDATICLESTATE", nullable = true)
    public String getColumnArticleOldaticlestate() {
        return this.columnArticleOldaticlestate;
    }

    public void setColumnArticleOldaticlestate(String columnArticleOldaticlestate) {
        this.columnArticleOldaticlestate = columnArticleOldaticlestate;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_LATESTCHANGTIME"
     *             length="7"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_LATESTCHANGTIME", nullable = true)
    public Date getColumnArticleLatestchangtime() {
        return this.columnArticleLatestchangtime;
    }

    public void setColumnArticleLatestchangtime(Date columnArticleLatestchangtime) {
        this.columnArticleLatestchangtime = columnArticleLatestchangtime;
    }

    /** 
     *            @hibernate.property
     *             column="COLUMN_ARTICLE_LATESTUSER"
     *             length="25"
     *         
     */
    @Column(name = "COLUMN_ARTICLE_LATESTUSER", nullable = true)
    public String getColumnArticleLatestuser() {
        return this.columnArticleLatestuser;
    }

    public void setColumnArticleLatestuser(String columnArticleLatestuser) {
        this.columnArticleLatestuser = columnArticleLatestuser;
    }
    
    /** 
     *            @hibernate.property
     *             column="PROCESS_INSTANCE_ID"
     *             length="32"
     *         
     */
    @Column(name = "PROCESS_INSTANCE_ID", nullable = true)
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ARTICLES_ID"         
     *         
     */

    @ManyToOne()
	@JoinColumn(name = "ARTICLES_ID", nullable = true)
    public com.strongit.oa.bo.ToaInfopublishArticle getToaInfopublishArticle() {
        return this.toaInfopublishArticle;
    }

    public void setToaInfopublishArticle(com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle) {
        this.toaInfopublishArticle = toaInfopublishArticle;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="CLUMN_ID"         
     *         
     */
    @ManyToOne()
	@JoinColumn(name = "CLUMN_ID", nullable = true)
    public com.strongit.oa.bo.ToaInfopublishColumn getToaInfopublishColumn() {
        return this.toaInfopublishColumn;
    }

    public void setToaInfopublishColumn(com.strongit.oa.bo.ToaInfopublishColumn toaInfopublishColumn) {
        this.toaInfopublishColumn = toaInfopublishColumn;
    }

    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("columnArticleId", getColumnArticleId())
            .toString();
    }

    @Transient
	public int getCountComment() {
		return countComment;
	}

	public void setCountComment(int countComment) {
		this.countComment = countComment;
	}

}
