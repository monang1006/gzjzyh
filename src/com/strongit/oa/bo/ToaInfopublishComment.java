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


/** 
 *        @hibernate.class
 *         table="T_OA_INFOPUBLISH_COMMENT"
 *     
*/
@Entity
@Table(name = "T_OA_INFOPUBLISH_COMMENT")
public class ToaInfopublishComment implements Serializable {

    /** identifier field */
	@Id
	@GeneratedValue
    private String commentId;

    /** persistent field */
    private String commentTitle;

    /** nullable persistent field */
    private String commentArticleurl;

    /** nullable persistent field */
    private String commentUser;

    /** nullable persistent field */
    private String commentContent;

    /** nullable persistent field */
    private Date commentAddtime;

    /** nullable persistent field */
    private String commentUserip;

    /** nullable persistent field */
    private String commentIsaudit;

    /** nullable persistent field */
    private String commentReplyContent;

    /** nullable persistent field */
    private Date commentReplyTime;

    /** nullable persistent field */
    private String commentIsshow;
    
    private String showTime;

    /** persistent field */
    private com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle;

    /** full constructor */
    public ToaInfopublishComment(String commentId, String commentTitle, String commentArticleurl, String commentUser, String commentContent, Date commentAddtime, String commentUserip, String commentIsaudit, String commentReplyContent, Date commentReplyTime, String commentIsshow, com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle) {
        this.commentId = commentId;
        this.commentTitle = commentTitle;
        this.commentArticleurl = commentArticleurl;
        this.commentUser = commentUser;
        this.commentContent = commentContent;
        this.commentAddtime = commentAddtime;
        this.commentUserip = commentUserip;
        this.commentIsaudit = commentIsaudit;
        this.commentReplyContent = commentReplyContent;
        this.commentReplyTime = commentReplyTime;
        this.commentIsshow = commentIsshow;
        this.toaInfopublishArticle = toaInfopublishArticle;
    }

    /** default constructor */
    public ToaInfopublishComment() {
    }

    /** minimal constructor */
    public ToaInfopublishComment(String commentId, String commentTitle, com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle) {
        this.commentId = commentId;
        this.commentTitle = commentTitle;
        this.toaInfopublishArticle = toaInfopublishArticle;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="COMMENT_ID"
     *         
     */
    @Id
    @Column(name = "COMMENT_ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getCommentId() {
        return this.commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_TITLE"
     *             length="200"
     *             not-null="true"
     *         
     */
    @Column(name = "COMMENT_TITLE", nullable = false)
    public String getCommentTitle() {
        return this.commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_ARTICLEURL"
     *             length="100"
     *         
     */
    @Column(name = "COMMENT_ARTICLEURL", nullable = true)
    public String getCommentArticleurl() {
        return this.commentArticleurl;
    }

    public void setCommentArticleurl(String commentArticleurl) {
        this.commentArticleurl = commentArticleurl;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_USER"
     *             length="25"
     *         
     */
    @Column(name = "COMMENT_USER", nullable = true)
    public String getCommentUser() {
        return this.commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name = "COMMENT_CONTENT", nullable = true)
    public String getCommentContent() {
        return this.commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_ADDTIME"
     *             length="7"
     *         
     */
    @Column(name = "COMMENT_ADDTIME", nullable = true)
    public Date getCommentAddtime() {
        return this.commentAddtime;
    }

    public void setCommentAddtime(Date commentAddtime) {
        this.commentAddtime = commentAddtime;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_USERIP"
     *             length="20"
     *         
     */
    @Column(name = "COMMENT_USERIP", nullable = true)
    public String getCommentUserip() {
        return this.commentUserip;
    }

    public void setCommentUserip(String commentUserip) {
        this.commentUserip = commentUserip;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_ISAUDIT"
     *             length="1"
     *         
     */
    @Column(name = "COMMENT_ISAUDIT", nullable = true)
    public String getCommentIsaudit() {
        return this.commentIsaudit;
    }

    public void setCommentIsaudit(String commentIsaudit) {
        this.commentIsaudit = commentIsaudit;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_REPLY_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name = "COMMENT_REPLY_CONTENT", nullable = true)
    public String getCommentReplyContent() {
        return this.commentReplyContent;
    }

    public void setCommentReplyContent(String commentReplyContent) {
        this.commentReplyContent = commentReplyContent;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_REPLY_TIME"
     *             length="7"
     *         
     */
    @Column(name = "COMMENT_REPLY_TIME", nullable = true)
    public Date getCommentReplyTime() {
        return this.commentReplyTime;
    }

    public void setCommentReplyTime(Date commentReplyTime) {
        this.commentReplyTime = commentReplyTime;
    }

    /** 
     *            @hibernate.property
     *             column="COMMENT_ISSHOW"
     *             length="1"
     *         
     */
    @Column(name = "COMMENT_ISSHOW", nullable = true)
    public String getCommentIsshow() {
        return this.commentIsshow;
    }

    public void setCommentIsshow(String commentIsshow) {
        this.commentIsshow = commentIsshow;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ARTICLES_ID"         
     *         
     */
    @ManyToOne
	@JoinColumn(name = "ARTICLES_ID", nullable = true)
    public com.strongit.oa.bo.ToaInfopublishArticle getToaInfopublishArticle() {
        return this.toaInfopublishArticle;
    }

    public void setToaInfopublishArticle(com.strongit.oa.bo.ToaInfopublishArticle toaInfopublishArticle) {
        this.toaInfopublishArticle = toaInfopublishArticle;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("commentId", getCommentId())
            .toString();
    }

    @Transient
	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

}
