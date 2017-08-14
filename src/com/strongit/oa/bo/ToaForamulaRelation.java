package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_FORAMULA_RELATION"
 *     
*/
@Entity
@Table(name="T_OA_FORAMULA_RELATION")
public class ToaForamulaRelation implements Serializable {

    /** identifier field */
    private String relationId;

    /** nullable persistent field */
    private String privilsort;

    /** nullable persistent field */
    private String privilsortId;
    
    private String privilsortName;

    /** nullable persistent field */
    private String foramulaId;

    /** full constructor */
    public ToaForamulaRelation(String relationId, String privilsort, String privilsortId, String privilsortName,String foramulaId) {
        this.relationId = relationId;
        this.privilsort = privilsort;
        this.privilsortId = privilsortId;
        this.foramulaId = foramulaId;
        this.privilsortName=privilsortName;
    }

    /** default constructor */
    public ToaForamulaRelation() {
    }

    /** minimal constructor */
    public ToaForamulaRelation(String relationId) {
        this.relationId = relationId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="RELATION_ID"
     *         
     */
    @Id
	@Column(name="RELATION_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
    public String getRelationId() {
        return this.relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSORT"
     *             length="2"
     *         
     */
    @Column(name="PRIVILSORT",nullable=true)
    public String getPrivilsort() {
        return this.privilsort;
    }

    public void setPrivilsort(String privilsort) {
        this.privilsort = privilsort;
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSORT_ID"
     *             length="32"
     *         
     */
    @Column(name="PRIVILSORT_ID",nullable=true)
    public String getPrivilsortId() {
        return this.privilsortId;
    }

    public void setPrivilsortId(String privilsortId) {
        this.privilsortId = privilsortId;
    }

    /** 
     *            @hibernate.property
     *             column="FORAMULA_ID"
     *             length="32"
     *         
     */
    @Column(name="FORAMULA_ID",nullable=true)
    public String getForamulaId() {
        return this.foramulaId;
    }

    public void setForamulaId(String foramulaId) {
        this.foramulaId = foramulaId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("relationId", getRelationId())
            .toString();
    }

    /** 
     *            @hibernate.property
     *             column="PRIVILSORT_NAME"
     *             length="100"
     *         
     */
    @Column(name="PRIVILSORT_NAME",nullable=true)
	public String getPrivilsortName() {
		return privilsortName;
	}

	public void setPrivilsortName(String privilsortName) {
		this.privilsortName = privilsortName;
	}

}
