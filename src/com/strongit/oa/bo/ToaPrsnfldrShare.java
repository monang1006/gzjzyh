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
 *         table="T_OA_PRSNFLDR_SHARE"
 *     
*/
@Entity
@Table(name="T_OA_PRSNFLDR_SHARE",catalog="",schema="")
public class ToaPrsnfldrShare implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3261364158932399277L;

	/** identifier field */
    private String shareId;

    /** nullable persistent field */
    private String sharePrivilege;

    /** persistent field */
    private com.strongit.oa.bo.ToaPrivatePrsnfldrFolder toaPrsnfldrFolder;

    /** persistent field */
    private String userId;

    /** full constructor */
    public ToaPrsnfldrShare(String shareId, String sharePrivilege, com.strongit.oa.bo.ToaPrivatePrsnfldrFolder toaPrsnfldrFolder) {
        this.shareId = shareId;
        this.sharePrivilege = sharePrivilege;
        this.toaPrsnfldrFolder = toaPrsnfldrFolder;
    }

    /** default constructor */
    public ToaPrsnfldrShare() {
    }

    /** minimal constructor */
    public ToaPrsnfldrShare(String shareId, com.strongit.oa.bo.ToaPrivatePrsnfldrFolder toaPrsnfldrFolder) {
        this.shareId = shareId;
        this.toaPrsnfldrFolder = toaPrsnfldrFolder;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SHARE_ID"
     *         
     */
    @Id
    @Column(name="SHARE_ID",nullable=false,length=32)
    @GeneratedValue(generator="hibernate-uuid")
    @GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getShareId() {
        return this.shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    /** 
     *            @hibernate.property
     *             column="SHARE_PRIVILEGE"
     *             length="16"
     *         
     */
    @Column(name="SHARE_PRIVILEGE",length=16)
    public String getSharePrivilege() {
        return this.sharePrivilege;
    }

    public void setSharePrivilege(String sharePrivilege) {
        this.sharePrivilege = sharePrivilege;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="FOLDER_ID"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="FOLDER_ID",nullable=false)
    public com.strongit.oa.bo.ToaPrivatePrsnfldrFolder getToaPrsnfldrFolder() {
        return this.toaPrsnfldrFolder;
    }

    public void setToaPrsnfldrFolder(com.strongit.oa.bo.ToaPrivatePrsnfldrFolder toaPrsnfldrFolder) {
        this.toaPrsnfldrFolder = toaPrsnfldrFolder;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="USER_ID"         
     *         @ManyToOne
    @JoinColumn(name="USER_ID",nullable=false)
     */
    @Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("shareId", getShareId())
            .toString();
    }



}
