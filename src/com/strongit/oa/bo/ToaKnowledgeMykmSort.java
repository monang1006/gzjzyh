/*
 * Copyright : Strong Digital Technological Co., LTD.
 * All right reserved.
 * JDK 1.5.0_14;?Struts：2.1.2;Spring 2.5.6;?Hibernate： 3.3.1.GA
 * Create Date: 2009-4-25
 * Autour: hull
 * Version: V1.0
 * Description： 知识类型BO
 */
package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/** 
 *        @hibernate.class
 *         table="T_OA_KNOWLEDGE_MYKM_SORT"
 *     
*/
@Entity
@Table(name="T_OA_KNOWLEDGE_MYKM_SORT",catalog="",schema="")
public class ToaKnowledgeMykmSort implements Serializable {

    /** identifier field */
    private String mykmSortId;

    /** nullable persistent field */
    private String mykmSortName;

    /** nullable persistent field */
    private String mykmSortDesc;

    /** nullable persistent field */
    private String mykmSortUser;


    /** full constructor */
    public ToaKnowledgeMykmSort(String mykmSortId, String mykmSortName, String mykmSortDesc, String mykmSortUser) {
        this.mykmSortId = mykmSortId;
        this.mykmSortName = mykmSortName;
        this.mykmSortDesc = mykmSortDesc;
        this.mykmSortUser = mykmSortUser;

    }

    /** default constructor */
    public ToaKnowledgeMykmSort() {
    }

    /** minimal constructor */
    public ToaKnowledgeMykmSort(String mykmSortId) {
        this.mykmSortId = mykmSortId;
       
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MYKM_SORT_ID"
     *         
     */
    @Id
	@Column(name="MYKM_SORT_ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
    public String getMykmSortId() {
        return this.mykmSortId;
    }

    public void setMykmSortId(String mykmSortId) {
        this.mykmSortId = mykmSortId;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_SORT_NAME"
     *             length="100"
     *         
     */
    @Column(name="MYKM_SORT_NAME",nullable=false)
    public String getMykmSortName() {
        return this.mykmSortName;
    }

    public void setMykmSortName(String mykmSortName) {
        this.mykmSortName = mykmSortName;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_SORT_DESC"
     *             length="400"
     *         
     */
    @Column(name="MYKM_SORT_DESC",nullable=true)
    public String getMykmSortDesc() {
        return this.mykmSortDesc;
    }

    public void setMykmSortDesc(String mykmSortDesc) {
        this.mykmSortDesc = mykmSortDesc;
    }

    /** 
     *            @hibernate.property
     *             column="MYKM_SORT_USER"
     *             length="32"
     *         
     */
    @Column(name="MYKM_SORT_USER",nullable=false)
    public String getMykmSortUser() {
        return this.mykmSortUser;
    }

    public void setMykmSortUser(String mykmSortUser) {
        this.mykmSortUser = mykmSortUser;
    }

    

    public String toString() {
        return new ToStringBuilder(this)
            .append("mykmSortId", getMykmSortId())
            .toString();
    }

}
