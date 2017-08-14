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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_INSPECT_NOTICE"
 *     
*/
@Entity
@Table(name="T_OA_INSPECT_NOTICE",schema="",catalog="")
public class ToaInspectNotice implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3664885202398797517L;

	/** identifier field */
    private String noticeId;

    /** nullable persistent field */
    private String noticeBussinessCode;

    /** nullable persistent field */
    private String noticeDepart;

    /** nullable persistent field */
    private Date noticeStartTime;

    /** nullable persistent field */
    private Date noticeEndTime;

    /** nullable persistent field */
    private String noticeContent;
    
    private String noticeFormId;
    
    private String noticeState;//状态
    
    private String noticeUser;//当前用户
    
    private String noticeTitle;//标题
    
    private Date noticeDraftDate;//登记日期
    
    private String  noticeOrigin;//来源

    /** persistent field */
    private Set<ToaInspectNoticeattach> toaInspectNoticeattaches;

    /** full constructor */
    public ToaInspectNotice(String noticeId, String noticeBussinessCode, String noticeDepart, Date noticeStartTime, Date noticeEndTime, String noticeContent, Set<ToaInspectNoticeattach> toaInspectNoticeattaches,String  noticeOrigin) {
        this.noticeId = noticeId;
        this.noticeBussinessCode = noticeBussinessCode;
        this.noticeDepart = noticeDepart;
        this.noticeStartTime = noticeStartTime;
        this.noticeEndTime = noticeEndTime;
        this.noticeContent = noticeContent;
        this.toaInspectNoticeattaches = toaInspectNoticeattaches;
        this.noticeOrigin = noticeOrigin;
    }

    /** default constructor */
    public ToaInspectNotice() {
    }

    /** minimal constructor */
    public ToaInspectNotice(String noticeId, Set<ToaInspectNoticeattach> toaInspectNoticeattaches) {
        this.noticeId = noticeId;
        this.toaInspectNoticeattaches = toaInspectNoticeattaches;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="NOTICE_ID"
     *         
     */
    @Id
    @Column(name="NOTICE_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getNoticeId() {
        return this.noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    /** 
     *            @hibernate.property
     *             column="NOTICE_BUSSINESS_CODE"
     *             length="50"
     *         
     */
    @Column(name="NOTICE_BUSSINESS_CODE")
    public String getNoticeBussinessCode() {
        return this.noticeBussinessCode;
    }

    public void setNoticeBussinessCode(String noticeBussinessCode) {
        this.noticeBussinessCode = noticeBussinessCode;
    }

    /** 
     *            @hibernate.property
     *             column="NOTICE_DEPART"
     *             length="50"
     *         
     */
    @Column(name="NOTICE_DEPART")
    public String getNoticeDepart() {
        return this.noticeDepart;
    }

    public void setNoticeDepart(String noticeDepart) {
        this.noticeDepart = noticeDepart;
    }

    /** 
     *            @hibernate.property
     *             column="NOTICE_START_TIME"
     *             length="7"
     *         
     */
    @Column(name="NOTICE_START_TIME")
    public Date getNoticeStartTime() {
        return this.noticeStartTime;
    }

    public void setNoticeStartTime(Date noticeStartTime) {
        this.noticeStartTime = noticeStartTime;
    }

    /** 
     *            @hibernate.property
     *             column="NOTICE_END_TIME"
     *             length="7"
     *         
     */
    @Column(name="NOTICE_END_TIME")
    public Date getNoticeEndTime() {
        return this.noticeEndTime;
    }

    public void setNoticeEndTime(Date noticeEndTime) {
        this.noticeEndTime = noticeEndTime;
    }

    /** 
     *            @hibernate.property
     *             column="NOTICE_CONTENT"
     *             length="4000"
     *         
     */
    @Column(name="NOTICE_CONTENT")
    public String getNoticeContent() {
        return this.noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="NOTICE_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaInspectNoticeattach"
     *         
     */
    @OneToMany(mappedBy="toaInspectNotice",fetch=FetchType.LAZY,cascade={CascadeType.REMOVE,CascadeType.REFRESH})
    public Set<ToaInspectNoticeattach> getToaInspectNoticeattaches() {
        return this.toaInspectNoticeattaches;
    }

    public void setToaInspectNoticeattaches(Set<ToaInspectNoticeattach> toaInspectNoticeattaches) {
        this.toaInspectNoticeattaches = toaInspectNoticeattaches;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("noticeId", getNoticeId())
            .toString();
    }

    @Column(name="NOTICE_FORMID")
	public String getNoticeFormId() {
		return noticeFormId;
	}

	public void setNoticeFormId(String noticeFormId) {
		this.noticeFormId = noticeFormId;
	}

	@Column(name="NOTICE_STATE")
	public String getNoticeState() {
		return noticeState;
	}

	public void setNoticeState(String noticeState) {
		this.noticeState = noticeState;
	}

	@Column(name="NOTICE_USER")
	public String getNoticeUser() {
		return noticeUser;
	}

	public void setNoticeUser(String noticeUser) {
		this.noticeUser = noticeUser;
	}

	@Column(name="NOTICE_TITLE")
	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	@Column(name="NOTICE_DRAFTDATE")
	public Date getNoticeDraftDate() {
		return noticeDraftDate;
	}

	public void setNoticeDraftDate(Date noticeDraftDate) {
		this.noticeDraftDate = noticeDraftDate;
	}

	@Column(name="NOTICE_ORIGIN")
	public String getNoticeOrigin() {
		return noticeOrigin;
	}

	public void setNoticeOrigin(String noticeOrigin) {
		this.noticeOrigin = noticeOrigin;
	}
	
	
	

}
