package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_MAIL_FOLDER"
 *     
*/
@Entity
@Table(name="T_OA_MAIL_FOLDER",catalog="",schema="")
public class ToaMailFolder implements Serializable {

    /** identifier field */
    private String mailfolderId;

    /** nullable persistent field */
    private String mailfolderName;

    /** nullable persistent field */
    private String mailfolderType;

    /** persistent field */
    private com.strongit.oa.bo.ToaMailBox toaMailBox;

    /** persistent field */
    private Set toaMails;
    
    private String mailNum;

    /** full constructor */
    public ToaMailFolder(String mailfolderId, String mailfolderName, String mailfolderType, com.strongit.oa.bo.ToaMailBox toaMailBox, Set toaMails) {
        this.mailfolderId = mailfolderId;
        this.mailfolderName = mailfolderName;
        this.mailfolderType = mailfolderType;
        this.toaMailBox = toaMailBox;
        this.toaMails = toaMails;
    }

    /** default constructor */
    public ToaMailFolder() {
    }

    /** minimal constructor */
    public ToaMailFolder(String mailfolderId, com.strongit.oa.bo.ToaMailBox toaMailBox, Set toaMails) {
        this.mailfolderId = mailfolderId;
        this.toaMailBox = toaMailBox;
        this.toaMails = toaMails;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MAILFOLDER_ID"
     *         
     */
	@Id
	@Column(name="MAILFOLDER_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getMailfolderId() {
        return this.mailfolderId;
    }

    public void setMailfolderId(String mailfolderId) {
        this.mailfolderId = mailfolderId;
    }

    /** 
     *            @hibernate.property
     *             column="MAILFOLDER_NAME"
     *             length="128"
     *         
     */
    @Column(name="MAILFOLDER_NAME",nullable=true)
    public String getMailfolderName() {
        return this.mailfolderName;
    }

    public void setMailfolderName(String mailfolderName) {
        this.mailfolderName = mailfolderName;
    }

    /** 
     *            @hibernate.property
     *             column="MAILFOLDER_TYPE"
     *             length="1"
     *         
     */
    @Column(name="MAILFOLDER_TYPE",nullable=true)
    public String getMailfolderType() {
        return this.mailfolderType;
    }

    public void setMailfolderType(String mailfolderType) {
        this.mailfolderType = mailfolderType;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="MAILBOX_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="MAILBOX_ID", nullable=false)
    public com.strongit.oa.bo.ToaMailBox getToaMailBox() {
        return this.toaMailBox;
    }

    public void setToaMailBox(com.strongit.oa.bo.ToaMailBox toaMailBox) {
        this.toaMailBox = toaMailBox;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="MAILFOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaMail"
     *         
     */
    //@Transient
    @OneToMany(mappedBy="toaMailFolder",targetEntity=com.strongit.oa.bo.ToaMail.class,cascade=CascadeType.ALL)
    public Set getToaMails() {
        return this.toaMails;
    }

    public void setToaMails(Set toaMails) {
        this.toaMails = toaMails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mailfolderId", getMailfolderId())
            .toString();
    }

	@Transient
	public String getMailNum() {
		return mailNum;
	}
	
	public void setMailNum(String mailNum) {
		this.mailNum = mailNum;
	}
}
