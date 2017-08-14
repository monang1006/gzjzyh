package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;


/**
 *   @hibernate.class
 *   table=T_OA_SERIALNUMBER_SORT
 * @author Administrator
 *
 */
@Entity
@Table(name="T_OA_SERIALNUMBER_SORT",catalog="",schema="")
public class ToaSerialnumberSort implements Serializable {
	
	/** identifier field */
	private String sortId;
	
	/** nullable persistent field */
	private String sortName;
	
	/** nullable persistent field */
	private String sortAbbreviation;
	
	/** nullable persistent field*/
	private Date sortTime;
	
	

	/** full constructor */
	public ToaSerialnumberSort(String sortId,String sortName,String sortAbbreviation,Date sortTime){
		this.sortId=sortId;
		this.sortName=sortName;
		this.sortAbbreviation=sortAbbreviation;
		this.sortTime=sortTime;
	}

	 /** default constructor */
	public ToaSerialnumberSort(){
	}
	
	/** minimal constructor */
	public ToaSerialnumberSort(String sortId){
		this.sortId=sortId;
	}
	
	 /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="SORT_ID"
     *         
     */
    @Id
	@Column(name="SORT_ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	/** 
     *            @hibernate.property
     *             column="SORT_NAME"
     *             length="50"
     *         
     */
	@Column(name="SORT_NAME",nullable=true)
	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	/** 
     *            @hibernate.property
     *             column="SORT_ABBREVIATION"
     *             length="100"
     *         
     */
	@Column(name="SORT_ABBREVIATION",nullable=true)
	public String getSortAbbreviation() {
		return sortAbbreviation;
	}

	public void setSortAbbreviation(String sortAbbreviation) {
		this.sortAbbreviation = sortAbbreviation;
	}


    public String toString() {
        return new ToStringBuilder(this)
            .append("sortId", getSortId())
            .toString();
    }

    /**
     *                 @hibernate.property
     *                 column="SORT_TIME"
     * 
     */
    @Column(name="SORT_TIME",nullable=true)
    public Date getSortTime() {
		return sortTime;
	}

	public void setSortTime(Date sortTime) {
		this.sortTime = sortTime;
	}
}
