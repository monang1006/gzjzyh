package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INFOPUBLISH_ARTICLES"
 *     
*/
@Entity
@Table(name = "T_OA_INFOPUBLISH_ARTICLES")
public class ToaInfopublishArticle implements Serializable {

    /** identifier field */
	@Id
	@GeneratedValue
    private String articlesId;

    /** persistent field */
    private String articlesTitle;

    /** nullable persistent field */
    private String articlesSubtitle;

    /** nullable persistent field */
    private String articlesAuthor;

    /** nullable persistent field */
    private String articlesKeyword;

    /** nullable persistent field */
    private String articlesArticlecontent;

    /** nullable persistent field */
    private String articlesTitlecolor;

    /** nullable persistent field */
    private String articlesTitlefont;

    /** nullable persistent field */
    private String articlesPic;

    /** nullable persistent field */
    private String articlesIsredirect;

    /** nullable persistent field */
    private String articlesRedirecturl;

    /** nullable persistent field */
    private String articlesAticlestate;

    /** nullable persistent field */
    private String articlesAticleattribute;

    /** nullable persistent field */
    private String articlesIscancomment;

    /** nullable persistent field */
    private String articlesIsshowcomment;

    /** nullable persistent field */
    private String articlesIsstandtop;

    /** nullable persistent field */
    private String articlesIshead;

    /** nullable persistent field */
    private String articlesIshot;

    /** nullable persistent field */
    private String articlesGuidetype;

    /** nullable persistent field */
    private String articlesSource;

    /** nullable persistent field */
    private String articlesEditor;

    /** nullable persistent field */
    private String articlesDesc;

    /** nullable persistent field */
    private Date articlesCreatedate;

    /** nullable persistent field */
    private String articlesPagination;

    /** nullable persistent field */
    private Integer articlesPaginationnum;

    /** nullable persistent field */
    private Date articlesAutopublishtime;

    /** nullable persistent field */
    private Date articlesAutocancletime;

    /** nullable persistent field */
    private Date articlesStandtopstart;

    /** nullable persistent field */
    private Date articlesStandtopend;

    /** nullable persistent field */
    private Long articlesHits;

    /** nullable persistent field */
    private String articlesLatestuser;

    /** nullable persistent field */
    private Date articlesLatestchangtime;

    /** nullable persistent field */
    private String articlesIsshare;

    /** nullable persistent field */
    private String articlesIsprivate;
    
    /** nullable persistent field */
    private String articlesIssId;
    
    
    /** nullable persistent field */
    private String articlesInstructionContent;

    /** nullable persistent field */
    private Date articlesCommenttime;

    /** persistent field */
    private Set toaInfopublishColumnArticls;

    /** persistent field */
    private Set toaInfopublishComments;

    /** full constructor */
    public ToaInfopublishArticle(String articlesId, String articlesTitle, String articlesSubtitle, String articlesAuthor, String articlesKeyword, String articlesArticlecontent, String articlesTitlecolor, String articlesTitlefont, String articlesPic, String articlesIsredirect, String articlesRedirecturl, String articlesAticlestate, String articlesAticleattribute, String articlesIscancomment, String articlesIsshowcomment, String articlesIsstandtop, String articlesIshead, String articlesIshot, String articlesGuidetype, String articlesSource, String articlesEditor, String articlesDesc, Date articlesCreatedate, String articlesPagination, Integer articlesPaginationnum, Date articlesAutopublishtime, Date articlesAutocancletime, Date articlesStandtopstart, Date articlesStandtopend, Long articlesHits, String articlesLatestuser, Date articlesLatestchangtime, String articlesIsshare, String articlesIsprivate, Date articlesCommenttime, Set toaInfopublishColumnArticls, Set toaInfopublishComments) {
        this.articlesId = articlesId;
        this.articlesTitle = articlesTitle;
        this.articlesSubtitle = articlesSubtitle;
        this.articlesAuthor = articlesAuthor;
        this.articlesKeyword = articlesKeyword;
        this.articlesArticlecontent = articlesArticlecontent;
        this.articlesTitlecolor = articlesTitlecolor;
        this.articlesTitlefont = articlesTitlefont;
        this.articlesPic = articlesPic;
        this.articlesIsredirect = articlesIsredirect;
        this.articlesRedirecturl = articlesRedirecturl;
        this.articlesAticlestate = articlesAticlestate;
        this.articlesAticleattribute = articlesAticleattribute;
        this.articlesIscancomment = articlesIscancomment;
        this.articlesIsshowcomment = articlesIsshowcomment;
        this.articlesIsstandtop = articlesIsstandtop;
        this.articlesIshead = articlesIshead;
        this.articlesIshot = articlesIshot;
        this.articlesGuidetype = articlesGuidetype;
        this.articlesSource = articlesSource;
        this.articlesEditor = articlesEditor;
        this.articlesDesc = articlesDesc;
        this.articlesCreatedate = articlesCreatedate;
        this.articlesPagination = articlesPagination;
        this.articlesPaginationnum = articlesPaginationnum;
        this.articlesAutopublishtime = articlesAutopublishtime;
        this.articlesAutocancletime = articlesAutocancletime;
        this.articlesStandtopstart = articlesStandtopstart;
        this.articlesStandtopend = articlesStandtopend;
        this.articlesHits = articlesHits;
        this.articlesLatestuser = articlesLatestuser;
        this.articlesLatestchangtime = articlesLatestchangtime;
        this.articlesIsshare = articlesIsshare;
        this.articlesIsprivate = articlesIsprivate;
        this.articlesCommenttime = articlesCommenttime;
        this.toaInfopublishColumnArticls = toaInfopublishColumnArticls;
        this.toaInfopublishComments = toaInfopublishComments;
    }

    /** default constructor */
    public ToaInfopublishArticle() {
    }

    /** minimal constructor */
    public ToaInfopublishArticle(String articlesId, String articlesTitle, Set toaInfopublishColumnArticls, Set toaInfopublishComments) {
        this.articlesId = articlesId;
        this.articlesTitle = articlesTitle;
        this.toaInfopublishColumnArticls = toaInfopublishColumnArticls;
        this.toaInfopublishComments = toaInfopublishComments;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="ARTICLES_ID"
     *         
     */
    @Id
    @Column(name = "ARTICLES_ID", nullable = false, length = 32)
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getArticlesId() {
        return this.articlesId;
    }

    public void setArticlesId(String articlesId) {
        this.articlesId = articlesId;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_TITLE"
     *             length="200"
     *             not-null="true"
     *         
     */
    @Column(name = "ARTICLES_TITLE", nullable = false)
    public String getArticlesTitle() {
        return this.articlesTitle;
    }

    public void setArticlesTitle(String articlesTitle) {
        this.articlesTitle = articlesTitle;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_SUBTITLE"
     *             length="100"
     *         
     */
    @Column(name = "ARTICLES_SUBTITLE", nullable = true)
    public String getArticlesSubtitle() {
        return this.articlesSubtitle;
    }

    public void setArticlesSubtitle(String articlesSubtitle) {
        this.articlesSubtitle = articlesSubtitle;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_AUTHOR"
     *             length="25"
     *         
     */
    @Column(name = "ARTICLES_AUTHOR", nullable = true)
    public String getArticlesAuthor() {
        return this.articlesAuthor;
    }

    public void setArticlesAuthor(String articlesAuthor) {
        this.articlesAuthor = articlesAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_KEYWORD"
     *             length="50"
     *         
     */
    @Column(name = "ARTICLES_KEYWORD", nullable = true)
    public String getArticlesKeyword() {
        return this.articlesKeyword;
    }

    public void setArticlesKeyword(String articlesKeyword) {
        this.articlesKeyword = articlesKeyword;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ARTICLECONTENT"
     *             length="4000"
     *         
     */
    @Column(name = "ARTICLES_ARTICLECONTENT", nullable = true)
    @Lob
    public String getArticlesArticlecontent() {
        return this.articlesArticlecontent;
    }

    public void setArticlesArticlecontent(String articlesArticlecontent) {
        this.articlesArticlecontent = articlesArticlecontent;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_TITLECOLOR"
     *             length="20"
     *         
     */
    @Column(name = "ARTICLES_TITLECOLOR", nullable = true)
    public String getArticlesTitlecolor() {
        return this.articlesTitlecolor;
    }

    public void setArticlesTitlecolor(String articlesTitlecolor) {
        this.articlesTitlecolor = articlesTitlecolor;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_TITLEFONT"
     *             length="20"
     *         
     */
    @Column(name = "ARTICLES_TITLEFONT", nullable = true)
    public String getArticlesTitlefont() {
        return this.articlesTitlefont;
    }

    public void setArticlesTitlefont(String articlesTitlefont) {
        this.articlesTitlefont = articlesTitlefont;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_PIC"
     *             length="4000"
     *         
     */
    @Column(name = "ARTICLES_PIC", nullable = true)
    public String getArticlesPic() {
        return this.articlesPic;
    }

    public void setArticlesPic(String articlesPic) {
        this.articlesPic = articlesPic;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISREDIRECT"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISREDIRECT", nullable = true)
    public String getArticlesIsredirect() {
        return this.articlesIsredirect;
    }

    public void setArticlesIsredirect(String articlesIsredirect) {
        this.articlesIsredirect = articlesIsredirect;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_REDIRECTURL"
     *             length="100"
     *         
     */
    @Column(name = "ARTICLES_REDIRECTURL", nullable = true)
    public String getArticlesRedirecturl() {
        return this.articlesRedirecturl;
    }

    public void setArticlesRedirecturl(String articlesRedirecturl) {
        this.articlesRedirecturl = articlesRedirecturl;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ATICLESTATE"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ATICLESTATE", nullable = true)
    public String getArticlesAticlestate() {
        return this.articlesAticlestate;
    }

    public void setArticlesAticlestate(String articlesAticlestate) {
        this.articlesAticlestate = articlesAticlestate;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ATICLEATTRIBUTE"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ATICLEATTRIBUTE", nullable = true)
    public String getArticlesAticleattribute() {
        return this.articlesAticleattribute;
    }

    public void setArticlesAticleattribute(String articlesAticleattribute) {
        this.articlesAticleattribute = articlesAticleattribute;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISCANCOMMENT"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISCANCOMMENT", nullable = true)
    public String getArticlesIscancomment() {
        return this.articlesIscancomment;
    }

    public void setArticlesIscancomment(String articlesIscancomment) {
        this.articlesIscancomment = articlesIscancomment;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISSHOWCOMMENT"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISSHOWCOMMENT", nullable = true)
    public String getArticlesIsshowcomment() {
        return this.articlesIsshowcomment;
    }

    public void setArticlesIsshowcomment(String articlesIsshowcomment) {
        this.articlesIsshowcomment = articlesIsshowcomment;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISSTANDTOP"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISSTANDTOP", nullable = true)
    public String getArticlesIsstandtop() {
        return this.articlesIsstandtop;
    }

    public void setArticlesIsstandtop(String articlesIsstandtop) {
        this.articlesIsstandtop = articlesIsstandtop;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISHEAD"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISHEAD", nullable = true)
    public String getArticlesIshead() {
        return this.articlesIshead;
    }

    public void setArticlesIshead(String articlesIshead) {
        this.articlesIshead = articlesIshead;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISHOT"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISHOT", nullable = true)
    public String getArticlesIshot() {
        return this.articlesIshot;
    }

    public void setArticlesIshot(String articlesIshot) {
        this.articlesIshot = articlesIshot;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_GUIDETYPE"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_GUIDETYPE", nullable = true)
    public String getArticlesGuidetype() {
        return this.articlesGuidetype;
    }

    public void setArticlesGuidetype(String articlesGuidetype) {
        this.articlesGuidetype = articlesGuidetype;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_SOURCE"
     *             length="100"
     *         
     */
    @Column(name = "ARTICLES_SOURCE", nullable = true)
    public String getArticlesSource() {
        return this.articlesSource;
    }

    public void setArticlesSource(String articlesSource) {
        this.articlesSource = articlesSource;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_EDITOR"
     *             length="25"
     *         
     */
    @Column(name = "ARTICLES_EDITOR", nullable = true)
    public String getArticlesEditor() {
        return this.articlesEditor;
    }

    public void setArticlesEditor(String articlesEditor) {
        this.articlesEditor = articlesEditor;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_DESC"
     *             length="150"
     *         
     */
    @Column(name = "ARTICLES_DESC", nullable = true)
    public String getArticlesDesc() {
        return this.articlesDesc;
    }

    public void setArticlesDesc(String articlesDesc) {
        this.articlesDesc = articlesDesc;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_CREATEDATE"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_CREATEDATE", nullable = true)
    public Date getArticlesCreatedate() {
        return this.articlesCreatedate;
    }

    public void setArticlesCreatedate(Date articlesCreatedate) {
        this.articlesCreatedate = articlesCreatedate;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_PAGINATION"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_PAGINATION", nullable = true)
    public String getArticlesPagination() {
        return this.articlesPagination;
    }

    public void setArticlesPagination(String articlesPagination) {
        this.articlesPagination = articlesPagination;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_PAGINATIONNUM"
     *             length="5"
     *         
     */
    @Column(name = "ARTICLES_PAGINATIONNUM", nullable = true)
    public Integer getArticlesPaginationnum() {
        return this.articlesPaginationnum;
    }

    public void setArticlesPaginationnum(Integer articlesPaginationnum) {
        this.articlesPaginationnum = articlesPaginationnum;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_AUTOPUBLISHTIME"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_AUTOPUBLISHTIME", nullable = true)
    public Date getArticlesAutopublishtime() {
        return this.articlesAutopublishtime;
    }

    public void setArticlesAutopublishtime(Date articlesAutopublishtime) {
        this.articlesAutopublishtime = articlesAutopublishtime;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_AUTOCANCLETIME"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_AUTOCANCLETIME", nullable = true)
    public Date getArticlesAutocancletime() {
        return this.articlesAutocancletime;
    }

    public void setArticlesAutocancletime(Date articlesAutocancletime) {
        this.articlesAutocancletime = articlesAutocancletime;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_STANDTOPSTART"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_STANDTOPSTART", nullable = true)
    public Date getArticlesStandtopstart() {
        return this.articlesStandtopstart;
    }

    public void setArticlesStandtopstart(Date articlesStandtopstart) {
        this.articlesStandtopstart = articlesStandtopstart;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_STANDTOPEND"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_STANDTOPEND", nullable = true)
    public Date getArticlesStandtopend() {
        return this.articlesStandtopend;
    }

    public void setArticlesStandtopend(Date articlesStandtopend) {
        this.articlesStandtopend = articlesStandtopend;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_HITS"
     *             length="15"
     *         
     */
    @Column(name = "ARTICLES_HITS", nullable = true)
    public Long getArticlesHits() {
        return this.articlesHits;
    }

    public void setArticlesHits(Long articlesHits) {
        this.articlesHits = articlesHits;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_LATESTUSER"
     *             length="25"
     *         
     */
    @Column(name = "ARTICLES_LATESTUSER", nullable = true)
    public String getArticlesLatestuser() {
        return this.articlesLatestuser;
    }

    public void setArticlesLatestuser(String articlesLatestuser) {
        this.articlesLatestuser = articlesLatestuser;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_LATESTCHANGTIME"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_LATESTCHANGTIME", nullable = true)
    public Date getArticlesLatestchangtime() {
        return this.articlesLatestchangtime;
    }

    public void setArticlesLatestchangtime(Date articlesLatestchangtime) {
        this.articlesLatestchangtime = articlesLatestchangtime;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISSHARE"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISSHARE", nullable = true)
    public String getArticlesIsshare() {
        return this.articlesIsshare;
    }

    public void setArticlesIsshare(String articlesIsshare) {
        this.articlesIsshare = articlesIsshare;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_ISPRIVATE"
     *             length="1"
     *         
     */
    @Column(name = "ARTICLES_ISPRIVATE", nullable = true)
    public String getArticlesIsprivate() {
        return this.articlesIsprivate;
    }

    public void setArticlesIsprivate(String articlesIsprivate) {
        this.articlesIsprivate = articlesIsprivate;
    }

    /** 
     *            @hibernate.property
     *             column="ARTICLES_COMMENTTIME"
     *             length="7"
     *         
     */
    @Column(name = "ARTICLES_COMMENTTIME", nullable = true)
    public Date getArticlesCommenttime() {
        return this.articlesCommenttime;
    }

    public void setArticlesCommenttime(Date articlesCommenttime) {
        this.articlesCommenttime = articlesCommenttime;
    }
    
    
    @Column(name = "ARTICLES_ISSID", nullable = true)
    public String getArticlesIssId() {
		return articlesIssId;
	}
    
	public void setArticlesIssId(String articlesIssId) {
		this.articlesIssId = articlesIssId;
	}

	@Column(name = "ARTICLES_INSTRUCTION_CONTENT", nullable = true)
	public String getArticlesInstructionContent() {
		return articlesInstructionContent;
	}

	public void setArticlesInstructionContent(String articlesInstructionContent) {
		this.articlesInstructionContent = articlesInstructionContent;
	}
	/** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ARTICLES_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaInfopublishColumnArticl"
     *         
     */
    @OneToMany(mappedBy = "toaInfopublishArticle", targetEntity=com.strongit.oa.bo.ToaInfopublishColumnArticl.class, cascade = CascadeType.ALL)
    public Set getToaInfopublishColumnArticls() {
        return this.toaInfopublishColumnArticls;
    }

    public void setToaInfopublishColumnArticls(Set toaInfopublishColumnArticls) {
        this.toaInfopublishColumnArticls = toaInfopublishColumnArticls;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ARTICLES_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaInfopublishComment"
     *         
     */
    @OneToMany(mappedBy = "toaInfopublishArticle",targetEntity=com.strongit.oa.bo.ToaInfopublishComment.class, cascade = CascadeType.ALL)
    public Set getToaInfopublishComments() {
        return this.toaInfopublishComments;
    }

    public void setToaInfopublishComments(Set toaInfopublishComments) {
        this.toaInfopublishComments = toaInfopublishComments;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("articlesId", getArticlesId())
            .toString();
    }

}
