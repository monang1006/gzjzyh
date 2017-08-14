package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_ADDRESS"
 *     
*/
@Entity
@Table(name="T_OA_SYSSINGLELOAD",catalog="",schema="")
public class ToaSyssingleload implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8843559728229143992L;

	/** identifier field */
    private String SYS_ID;

    /** nullable persistent field */
    private String SYS_NAME;

    /** nullable persistent field */
    private String SYS_NUM;

    /** nullable persistent field */
    private String SYS_PASSWORD;

    private String USER_ID;
    
    private String linkId;//第三方系统的id
    @Column(name="LINK_ID")
    public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	/** full constructor */
    public ToaSyssingleload(String SYS_ID, String SYS_NAME, String SYS_NUM, String SYS_PASSWORD,String USER_ID,String linkId) {
        this.SYS_ID = SYS_ID;
        this.SYS_NAME = SYS_NAME;
        this.SYS_NUM = SYS_NUM;
        this.SYS_PASSWORD = SYS_PASSWORD;
        this.USER_ID = USER_ID;
        this.linkId = linkId;
    }

    public ToaSyssingleload() {
    }
    
    
       @Id
    @Column(name="SYS_ID",nullable=false)
    @GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getSYS_ID() {
        return this.SYS_ID;
    }

    public void setSYS_ID(String SYS_ID) {
        this.SYS_ID = SYS_ID;
    }

    /** 
     *            @hibernate.property
     *             column="SYS_NAME"
     *             length="50"
     *         
     */
    @Column(name="SYS_NAME")
    public String getSYS_NAME() {
        return this.SYS_NAME;
    }

    public void setSYS_NAME(String SYS_NAME) {
        this.SYS_NAME = SYS_NAME;
    }

    /** 
     *            @hibernate.property
     *             column="SYS_NUM"
     *             length="12"
     *         
     */
    @Column(name="SYS_NUM")
    public String getSYS_NUM() {
        return this.SYS_NUM;
    }

    public void setSYS_NUM(String SYS_NUM) {
        this.SYS_NUM = SYS_NUM;
    }

    /** 
     *            @hibernate.property
     *             column="SYS_PASSWORD"
     *             length="20"
     *         
     */
    @Column(name="SYS_PASSWORD")
    public String getSYS_PASSWORD() {
        return this.SYS_PASSWORD;
    }
    public void setSYS_PASSWORD(String SYS_PASSWORD) {
        this.SYS_PASSWORD = SYS_PASSWORD;
    }


@Column(name="USER_ID")
public String getUSER_ID() {
    return this.USER_ID;
}

public void setUSER_ID(String USER_ID) {
    this.USER_ID = USER_ID;
}}



   