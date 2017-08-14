package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
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
 *         table="T_OA_AFFICHE"
 *     
*/
@Entity
@Table(name="T_OA_AFFICHE")
public class ToaAffiche implements Serializable {

    /** identifier field */
    private String afficheId;

    /** nullable persistent field */
    private String afficheTitle;

    /** nullable persistent field */
    private String afficheDesc;

    /** nullable persistent field */
    private String afficheAuthor;

    /** nullable persistent field */
    private String afficheGov;

    /** nullable persistent field */
    private Date afficheTime;

    /** nullable persistent field */
    private String afficheTop;

    /** nullable persistent field */
    private String afficheTitleColour;

    /** nullable persistent field */
    private String afficheTitleBold;

    /** nullable persistent field */
    private Date afficheUsefulLife;

    /** nullable persistent field */
    private String afficheState;

    /** nullable persistent field */
    private String afficheUserid;
    
    /** nullable persistent field */
    private String orgCode;
    
    /** nullable persistent field */
    private String orgId;
    
    /** nullable persistent field */
    private String viewAfterLogin;
    
    /** persistent field */
    private Set toaAfficheAttachs;
    
    private Set toaAfficheReceivers;
    
    /** nullable persistent field */
    public final static String NOTIFY_DRAFT = "0";    //未发布_草稿
    public final static String NOTIFY_SENDED = "1";		//发布_公告栏
    public final static String NOTIFY_OVERDUE = "2";	//过期_
    
    /** full constructor */
    public ToaAffiche(String afficheId, String afficheTitle, String afficheDesc, String afficheAuthor, String afficheGov, Date afficheTime, String afficheTop, String afficheTitleColour, String afficheTitleBold, Date afficheUsefulLife, String afficheState,String afficheUserid,String orgCode,String orgId, Set toaAfficheAttachs,Set toaAfficheReceivers,String viewAfterLogin) {
        this.afficheId = afficheId;
        this.afficheTitle = afficheTitle;
        this.afficheDesc = afficheDesc;
        this.afficheAuthor = afficheAuthor;
        this.afficheGov = afficheGov;
        this.afficheTime = afficheTime;
        this.afficheTop = afficheTop;
        this.afficheTitleColour = afficheTitleColour;
        this.afficheTitleBold = afficheTitleBold;
        this.afficheUsefulLife = afficheUsefulLife;
        this.afficheState = afficheState;
        this.afficheUserid = afficheUserid; 
        this.orgCode = orgCode;
        this.orgId = orgId;
        this.toaAfficheAttachs = toaAfficheAttachs;
        this.toaAfficheReceivers = toaAfficheReceivers;
        this.viewAfterLogin = viewAfterLogin;
    }

    /** default constructor */
    public ToaAffiche() {
    }

    /** minimal constructor */
    public ToaAffiche(String afficheId,Set toaAfficheAttachs,Set toaAfficheReceivers) {
        this.afficheId = afficheId;
        this.toaAfficheAttachs = toaAfficheAttachs;
        this.toaAfficheReceivers = toaAfficheReceivers;
        
    }

    
    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="AFFICHE_ID"
     */
    @Id
	@Column(name="AFFICHE_ID", nullable=false,length = 32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
    public String getAfficheId() {
        return this.afficheId;
    }

    public void setAfficheId(String afficheId) {
        this.afficheId = afficheId;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_TITLE"
     *             length="100"
     *         
     */
    @Column(name="AFFICHE_TITLE",nullable=true)
    public String getAfficheTitle() {
        return this.afficheTitle;
    }

    public void setAfficheTitle(String afficheTitle) {
        this.afficheTitle = afficheTitle;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_DESC"
     *             length="4000"
     *         
     */
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name="AFFICHE_DESC", columnDefinition="CLOB", nullable=true)    
    public String getAfficheDesc() {
        return this.afficheDesc;
    }

    public void setAfficheDesc(String afficheDesc) {
        this.afficheDesc = afficheDesc;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_AUTHOR"
     *             length="25"
     *         
     */
    @Column(name="AFFICHE_AUTHOR",nullable=true)
    public String getAfficheAuthor() {
        return this.afficheAuthor;
    }

    public void setAfficheAuthor(String afficheAuthor) {
        this.afficheAuthor = afficheAuthor;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_GOV"
     *             length="30"
     *         
     */
    @Column(name="AFFICHE_GOV",nullable=true)
    public String getAfficheGov() {
        return this.afficheGov;
    }

    public void setAfficheGov(String afficheGov) {
        this.afficheGov = afficheGov;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_TIME"
     *             length="7"
     *         
     */
    @Column(name="AFFICHE_TIME",nullable=true)
    public Date getAfficheTime() {
        return this.afficheTime;
    }

    public void setAfficheTime(Date afficheTime) {
        this.afficheTime = afficheTime;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_TOP"
     *             length="1"
     *         
     */
    @Column(name="AFFICHE_TOP",nullable=true)
    public String getAfficheTop() {
        return this.afficheTop;
    }

    public void setAfficheTop(String afficheTop) {
        this.afficheTop = afficheTop;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_TITLE_COLOUR"
     *             length="10"
     *         
     */
    @Column(name="AFFICHE_TITLE_COLOUR",nullable=true)
    public String getAfficheTitleColour() {
        return this.afficheTitleColour;
    }

    public void setAfficheTitleColour(String afficheTitleColour) {
        this.afficheTitleColour = afficheTitleColour;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_TITLE_BOLD"
     *             length="1"
     *         
     */
    @Column(name="AFFICHE_TITLE_BOLD",nullable=true)
    public String getAfficheTitleBold() {
        return this.afficheTitleBold;
    }

    public void setAfficheTitleBold(String afficheTitleBold) {
        this.afficheTitleBold = afficheTitleBold;
    }
    
    /** 
     *            @hibernate.property
     *             column="VIEW_AFTER_LOGIN"
     *             length="1"
     *         
     */
    @Column(name="VIEW_AFTER_LOGIN",nullable=true)
    public String getViewAfterLogin() {
        return this.viewAfterLogin;
    }
    
    
    public void setViewAfterLogin(String viewAfterLogin) {
        this.viewAfterLogin = viewAfterLogin;
    }
    
    /** 
     *            @hibernate.property
     *             column="AFFICHE_USEFUL_LIFE"
     *             length="7"
     *         
     */
    @Column(name="AFFICHE_USEFUL_LIFE",nullable=true)
    public Date getAfficheUsefulLife() {
        return this.afficheUsefulLife;
    }

    public void setAfficheUsefulLife(Date afficheUsefulLife) {
        this.afficheUsefulLife = afficheUsefulLife;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_STATE"
     *             length="1"
     *         
     */
    @Column(name="AFFICHE_STATE",nullable=true)
    public String getAfficheState() {
        return this.afficheState;
    }

    public void setAfficheState(String afficheState) {
        this.afficheState = afficheState;
    }

    /** 
     *            @hibernate.property
     *             column="AFFICHE_USERID"
     *             length="32"
     *         
     */
    @Column(name="AFFICHE_USERID",nullable=true)
    public String getAfficheUserid() {
        return this.afficheUserid;
    }

    public void setAfficheUserid(String afficheUserid) {
        this.afficheUserid = afficheUserid;
    }
    
    /** 
     *            @hibernate.property
     *             column="ORG_CODE"
     *             length="100"
     *         
     */
    @Column(name = "ORG_CODE",nullable=true)
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
    
    /** 
     *            @hibernate.property
     *             column="ORG_ID"
     *             length="32"
     *         
     */
    @Column(name = "ORG_ID",nullable=true)
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ACTIVITY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAfficheAttach"
     *         
     */
    @OneToMany(mappedBy="toaAffiche",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaAfficheAttach.class)
    public Set getToaAfficheAttachs() {
    	return toaAfficheAttachs;
    }
    
    public void setToaAfficheAttachs(Set toaAfficheAttachs) {
    	this.toaAfficheAttachs = toaAfficheAttachs;
    }
    
    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ACTIVITY_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaAfficheReceiver"
     *         
     */
    @OneToMany(mappedBy="toaAffiche",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE},targetEntity=ToaAfficheReceiver.class)
    public Set getToaAfficheReceivers() {
    	return toaAfficheReceivers;
    }
    
    public void setToaAfficheReceivers(Set toaAfficheReceivers) {
    	this.toaAfficheReceivers = toaAfficheReceivers;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("afficheId", getAfficheId())
            .toString();
    }


}
