/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 知识收藏BO
 */
package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;



/** 
 *        @hibernate.class
 *         table="T_OA_KNOWLEDGE_MYKM"
 *     
*/
@Entity
@Table(name="T_OA_KNOWLEDGE_MYKM",catalog="",schema="")
public class ToaKnowledgeMykm implements Serializable {

    /** identifier field */
    private String mykmId;

    /** nullable persistent field */
    private String mykmName;

    /** nullable persistent field */
    private String mykmUrl;

    /** nullable persistent field */
    private String mykmDesc;

    /** nullable persistent field */
    private String mykmSource;

    /** nullable persistent field */
    private String mykmIssortId;

    /** nullable persistent field */
    private Date mykmDate;

    /** nullable persistent field */
    private String mykmIsshare;

    /** nullable persistent field */
    private String mykmRedirecturl;

    /** nullable persistent field */
    private byte[] mykmContent;
    
    
    
    private String mykmUser;
    
    private String mykmOrder;
    
    private String mykmAuthor;

    private String mykmIsshow;
    

   

   
	/** full constructor */
    public ToaKnowledgeMykm(String mykmId, String mykmName, String mykmUrl, String mykmDesc, String mykmSource, String mykmIssortId, Date mykmDate, String mykmIsshare, String mykmRedirecturl, byte[] mykmContent,String mykmUser,String mykmOrder) {
        this.mykmId = mykmId;
        this.mykmName = mykmName;
        this.mykmUrl = mykmUrl;
        this.mykmDesc = mykmDesc;
        this.mykmSource = mykmSource;
        this.mykmIssortId = mykmIssortId;
        this.mykmDate = mykmDate;
        this.mykmIsshare = mykmIsshare;
        this.mykmRedirecturl = mykmRedirecturl;
        this.mykmContent = mykmContent;
       
        this.mykmUser=mykmUser;
        this.mykmOrder=mykmOrder;
    }

    /** default constructor */
    public ToaKnowledgeMykm() {
    }

    /** minimal constructor */
    public ToaKnowledgeMykm(String mykmId) {
        this.mykmId = mykmId;
        
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MYKM_ID"
     *         
     */
    @Id
    @Column(name="MYKM_ID")
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getMykmId() {
        return this.mykmId;
    }

    public void setMykmId(String mykmId) {
        this.mykmId = mykmId;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_NAME"
     *             length="400"
     *         
     */
    @Column(name="MYKM_NAME",nullable=false)
    public String getMykmName() {
        return this.mykmName;
    }

    public void setMykmName(String mykmName) {
        this.mykmName = mykmName;
    }

    /**
     * @hibernate.property
     * column="MYKM_ORDER"
     * @return
     */
    @Column(name="MYKM_ORDER",nullable=true)
    public String getMykmOrder() {
		return mykmOrder;
	}

	public void setMykmOrder(String mykmOrder) {
		this.mykmOrder = mykmOrder;
	}
    /** 
     *            @hibernate.property
     *             column="MYKM_URL"
     *             length="400"
     *         
     */
    @Column(name="MYKM_URL",nullable=true)
    public String getMykmUrl() {
        return this.mykmUrl;
    }

    public void setMykmUrl(String mykmUrl) {
        this.mykmUrl = mykmUrl;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_DESC"
     *             length="400"
     *         
     */
    @Column(name="MYKM_DESC",nullable=true)
    public String getMykmDesc() {
        return this.mykmDesc;
    }

    public void setMykmDesc(String mykmDesc) {
        this.mykmDesc = mykmDesc;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_SOURCE"
     *             length="100"
     *         
     */
    @Column(name="MYKM_SOURCE",nullable=true)
    public String getMykmSource() {
        return this.mykmSource;
    }

    public void setMykmSource(String mykmSource) {
        this.mykmSource = mykmSource;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_ISSORT_ID"
     *             length="32"
     *         
     */
    @Column(name="MYKM_ISSORT_ID",nullable=true)
    public String getMykmIssortId() {
        return this.mykmIssortId;
    }

    public void setMykmIssortId(String mykmIssortId) {
        this.mykmIssortId = mykmIssortId;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_DATE"
     *             length="7"
     *         
     */
    @Column(name="MYKM_DATE",nullable=true)
    public Date getMykmDate() {
        return this.mykmDate;
    }

    public void setMykmDate(Date mykmDate) {
        this.mykmDate = mykmDate;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_ISSHARE"
     *             length="1"
     *         
     */
    @Column(name="MYKM_ISSHARE",nullable=true)
    public String getMykmIsshare() {
        return this.mykmIsshare;
    }

    public void setMykmIsshare(String mykmIsshare) {
        this.mykmIsshare = mykmIsshare;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_REDIRECTURL"
     *             
     *         
     */
    @Column(name="MYKM_REDIRECTURL",nullable=true)
    public String getMykmRedirecturl() {
        return this.mykmRedirecturl;
    }

    public void setMykmRedirecturl(String mykmRedirecturl) {
        this.mykmRedirecturl = mykmRedirecturl;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_CONTENT"
     *         
     */
    @Column(name="MYKM_CONTENT",nullable=true)
    public byte[] getMykmContent() {
        return this.mykmContent;
    }

    public void setMykmContent(byte[] mykmContent) {
        this.mykmContent = mykmContent;
    }

   

    public String toString() {
        return new ToStringBuilder(this)
            .append("mykmId", getMykmId())
            .toString();
    }
    
/**
 * @hibernate.property 
 * not-null="true"
 * @hibernate.column name="MYKM_USER"
 */
    @Column(name="MYKM_USER",nullable=true)
	public String getMykmUser() {
		return mykmUser;
	}

	public void setMykmUser(String mykmUser) {
		this.mykmUser = mykmUser;
	}
	
	 @Column(name="MYKM_AUTHOR",nullable=true)
		public String getMykmAuthor() {
			return mykmAuthor;
		}

		public void setMykmAuthor(String mykmAuthor) {
			this.mykmAuthor = mykmAuthor;
	    }

		@Column(name="MYKM_ISSHOW",nullable=true)
		public String getMykmIsshow() {
			return mykmIsshow;
		}

		public void setMykmIsshow(String mykmIsshow) {
			this.mykmIsshow = mykmIsshow;
		}


}
