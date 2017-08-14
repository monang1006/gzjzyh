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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/** 
 *        @hibernate.class
 *         table="T_OA_POST_PROPERTY"
 *     
*/
@Entity
@Table(name="T_OA_POST_PROPERTY")
public class ToaPostProperty implements Serializable {

    /** identifier field */
    private String dataPrivalId;

    /** persistent field */
    private String postId;

    /** persistent field */
    private ToaSysmanageProperty infoItem;

    /** nullable persistent field */
    private String postPropertyReadonly;

    /** nullable persistent field */
    private String postPropertyHide;

    /** nullable persistent field */
    private String postPropertyReadwrite;

    /** nullable persistent field */
    private String infoSetCode;

    /** full constructor */
    public ToaPostProperty(String dataPrivalId, String postId, ToaSysmanageProperty infoItem, String postPropertyReadonly, String postPropertyHide, String postPropertyReadwrite, String infoSetCode) {
        this.dataPrivalId = dataPrivalId;
        this.postId = postId;
        this.infoItem = infoItem;
        this.postPropertyReadonly = postPropertyReadonly;
        this.postPropertyHide = postPropertyHide;
        this.postPropertyReadwrite = postPropertyReadwrite;
        this.infoSetCode = infoSetCode;
    }

    /** default constructor */
    public ToaPostProperty() {
    }

    /** minimal constructor */
    public ToaPostProperty(String dataPrivalId, String postId, ToaSysmanageProperty infoItem) {
        this.dataPrivalId = dataPrivalId;
        this.postId = postId;
        this.infoItem = infoItem;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DATA_PRIVAL_ID"
     *         
     */
    @Id
	@Column(name="DATA_PRIVAL_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getDataPrivalId() {
        return this.dataPrivalId;
    }

    public void setDataPrivalId(String dataPrivalId) {
        this.dataPrivalId = dataPrivalId;
    }

    /** 
     *            @hibernate.property
     *             column="POST_ID"
     *             length="32"
     *             not-null="true"
     *         
     */
    @Column(name="POST_ID",nullable=false)
    public String getPostId() {
        return this.postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    /** 
     *            @hibernate.property
     *             column="POST_PROPERTY_READONLY"
     *             length="1"
     *         
     */
    @Column(name="POST_PROPERTY_READONLY",nullable=true)
    public String getPostPropertyReadonly() {
        return this.postPropertyReadonly;
    }

    public void setPostPropertyReadonly(String postPropertyReadonly) {
        this.postPropertyReadonly = postPropertyReadonly;
    }

    /** 
     *            @hibernate.property
     *             column="POST_PROPERTY_HIDE"
     *             length="1"
     *         
     */
    @Column(name="POST_PROPERTY_HIDE",nullable=true)
    public String getPostPropertyHide() {
        return this.postPropertyHide;
    }

    public void setPostPropertyHide(String postPropertyHide) {
        this.postPropertyHide = postPropertyHide;
    }

    /** 
     *            @hibernate.property
     *             column="POST_PROPERTY_READWRITE"
     *             length="1"
     *         
     */
    @Column(name="POST_PROPERTY_READWRITE",nullable=true)
    public String getPostPropertyReadwrite() {
        return this.postPropertyReadwrite;
    }

    public void setPostPropertyReadwrite(String postPropertyReadwrite) {
        this.postPropertyReadwrite = postPropertyReadwrite;
    }

    /** 
     *            @hibernate.property
     *             column="INFO_SET_CODE"
     *             length="32"
     *         
     */
    @Column(name="INFO_SET_CODE",nullable=true)
    public String getInfoSetCode() {
        return this.infoSetCode;
    }

    public void setInfoSetCode(String infoSetCode) {
        this.infoSetCode = infoSetCode;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("dataPrivalId", getDataPrivalId())
            .toString();
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="false"
     *            @hibernate.column name="INFO_ITEM_CODE"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="INFO_ITEM_CODE",nullable=false)
    @NotFound(action=NotFoundAction.IGNORE)
	public ToaSysmanageProperty getInfoItem() {
		return infoItem;
	}

	public void setInfoItem(ToaSysmanageProperty infoItem) {
		this.infoItem = infoItem;
	}

}
