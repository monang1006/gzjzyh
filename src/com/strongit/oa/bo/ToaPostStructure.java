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
 *         table="T_OA_POST_STRUCTURE"
 *     
*/
@Entity
@Table(name="T_OA_POST_STRUCTURE")
public class ToaPostStructure implements Serializable {

    /** identifier field */
    private String dataPrivalId;

    /** persistent field */
    private String postId;

    /** persistent field */
    private ToaSysmanageStructure infoSet;

    /** nullable persistent field */
    private String postStructureReadonly;

    /** nullable persistent field */
    private String postStructureHide;

    /** nullable persistent field */
    private String postStructureReadwrite;

    /** full constructor */
    public ToaPostStructure(String dataPrivalId, String postId, ToaSysmanageStructure infoSet, String postStructureReadonly, String postStructureHide, String postStructureReadwrite) {
        this.dataPrivalId = dataPrivalId;
        this.postId = postId;
        this.infoSet = infoSet;
        this.postStructureReadonly = postStructureReadonly;
        this.postStructureHide = postStructureHide;
        this.postStructureReadwrite = postStructureReadwrite;
    }

    /** default constructor */
    public ToaPostStructure() {
    }

    /** minimal constructor */
    public ToaPostStructure(String dataPrivalId, String postId, ToaSysmanageStructure infoSet) {
        this.dataPrivalId = dataPrivalId;
        this.postId = postId;
        this.infoSet = infoSet;
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
     *             column="POST_STRUCTURE_READONLY"
     *             length="1"
     *         
     */
    @Column(name="POST_STRUCTURE_READONLY",nullable=true)
    public String getPostStructureReadonly() {
        return this.postStructureReadonly;
    }

    public void setPostStructureReadonly(String postStructureReadonly) {
        this.postStructureReadonly = postStructureReadonly;
    }

    /** 
     *            @hibernate.property
     *             column="POST_STRUCTURE_HIDE"
     *             length="1"
     *         
     */
    @Column(name="POST_STRUCTURE_HIDE",nullable=true)
    public String getPostStructureHide() {
        return this.postStructureHide;
    }

    public void setPostStructureHide(String postStructureHide) {
        this.postStructureHide = postStructureHide;
    }

    /** 
     *            @hibernate.property
     *             column="POST_STRUCTURE_READWRITE"
     *             length="1"
     *         
     */
    @Column(name="POST_STRUCTURE_READWRITE",nullable=true)
    public String getPostStructureReadwrite() {
        return this.postStructureReadwrite;
    }

    public void setPostStructureReadwrite(String postStructureReadwrite) {
        this.postStructureReadwrite = postStructureReadwrite;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("dataPrivalId", getDataPrivalId())
            .toString();
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="false"
     *            @hibernate.column name="INFO_SET_CODE"         
     *         
     */
    @ManyToOne
    @JoinColumn(name="INFO_SET_CODE",nullable=false)
    @NotFound(action=NotFoundAction.IGNORE)
	public ToaSysmanageStructure getInfoSet() {
		return infoSet;
	}

	public void setInfoSet(ToaSysmanageStructure infoSet) {
		this.infoSet = infoSet;
	}

}
