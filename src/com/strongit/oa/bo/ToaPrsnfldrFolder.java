package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_PRSNFLDR_FOLDER"
 *     
*/
@Entity
@Table(name="T_OA_PRSNFLDR_FOLDER",catalog="",schema="")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="FOLDER_TYPE")
public class ToaPrsnfldrFolder implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2124926965873076033L;

	/** identifier field */
    private String folderId;

    /** nullable persistent field */
    private String folderName;

    /** nullable persistent field */
    private String folderSort;

    /** nullable persistent field */
    private String folderParentId;

    /** nullable persistent field */
    private Date folderCreateDatetime;
    
    private String folderCreatePerson;

    private String userId;
    
    /** orgId */
    private String orgId;
    
    /** orgCode */
    private String orgCode;

    /** persistent field */
    private Set<ToaPrsnfldrFile> toaPrsnfldrFiles;

    /** persistent field */
    private Set<ToaPrsnfldrShare> toaPrsnfldrShares;

    /** full constructor */
    public ToaPrsnfldrFolder(String folderId, String folderName, String folderSort, String folderParentId, Date folderCreateDatetime, Set<ToaPrsnfldrFile> toaPrsnfldrFiles, Set<ToaPrsnfldrShare> toaPrsnfldrShares) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.folderSort = folderSort;
        this.folderParentId = folderParentId;
        this.folderCreateDatetime = folderCreateDatetime;
        this.toaPrsnfldrFiles = toaPrsnfldrFiles;
        this.toaPrsnfldrShares = toaPrsnfldrShares;
    }

    /** default constructor */
    public ToaPrsnfldrFolder() {
    }

    /** minimal constructor */
    public ToaPrsnfldrFolder(String folderId, Set<ToaPrsnfldrFile> toaPrsnfldrFiles, Set<ToaPrsnfldrShare> toaPrsnfldrShares) {
        this.folderId = folderId;
        this.toaPrsnfldrFiles = toaPrsnfldrFiles;
        this.toaPrsnfldrShares = toaPrsnfldrShares;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="FOLDER_ID"
     *         
     */
    @Id
    @Column(name="FOLDER_ID",nullable=false,length=32)
    @GeneratedValue(generator="hibernate-uuid")
    @GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_NAME"
     *             length="128"
     *         
     */
    @Column(name="FOLDER_NAME",nullable=false,length=128)
    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_SORT"
     *             length="32"
     *         
     */
    @Column(name="FOLDER_SORT",nullable=true, length=32)
    public String getFolderSort() {
        return this.folderSort;
    }

    public void setFolderSort(String folderSort) {
        this.folderSort = folderSort;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_PARENT_ID"
     *             length="32"
     *         
     */
    @Column(name="FOLDER_PARENT_ID",length=32)
    public String getFolderParentId() {
        return this.folderParentId;
    }

    public void setFolderParentId(String folderParentId) {
        this.folderParentId = folderParentId;
    }

    /** 
     *            @hibernate.property
     *             column="FOLDER_CREATE_DATETIME"
     *             length="7"
     *         
     */
    @Column(name="FOLDER_CREATE_DATETIME",nullable=true)
    public Date getFolderCreateDatetime() {
        return this.folderCreateDatetime;
    }

    public void setFolderCreateDatetime(Date folderCreateDatetime) {
        this.folderCreateDatetime = folderCreateDatetime;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPrsnfldrFile"
     *         
     */
    @OneToMany(mappedBy="toaPrsnfldrFolder",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
    public Set<ToaPrsnfldrFile> getToaPrsnfldrFiles() {
        return this.toaPrsnfldrFiles;
    }

    public void setToaPrsnfldrFiles(Set<ToaPrsnfldrFile> toaPrsnfldrFiles) {
        this.toaPrsnfldrFiles = toaPrsnfldrFiles;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="FOLDER_ID"
     *            @hibernate.collection-one-to-many
     *             class="com.strongit.oa.bo.ToaPrsnfldrShare"
     *         
     */
    @OneToMany(mappedBy="toaPrsnfldrFolder",fetch=FetchType.LAZY,cascade={CascadeType.REFRESH,CascadeType.REMOVE})
    public Set<ToaPrsnfldrShare> getToaPrsnfldrShares() {
        return this.toaPrsnfldrShares;
    }

    public void setToaPrsnfldrShares(Set<ToaPrsnfldrShare> toaPrsnfldrShares) {
        this.toaPrsnfldrShares = toaPrsnfldrShares;
    }

    @Column(name="FOLDER_CREATE_PERSON",nullable=true)
	public String getFolderCreatePerson() {
		return folderCreatePerson;
	}

	public void setFolderCreatePerson(String folderCreatePerson) {
		this.folderCreatePerson = folderCreatePerson;
	}
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("folderId", getFolderId())
            .toString();
    }

    @Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="ORG_ID")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name="ORG_CODE")
	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	

}
