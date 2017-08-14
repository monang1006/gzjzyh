package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *   @hibernate.class
 *   table=T_OA_SERIALNUMBER_REGULATION
 * @author Administrator
 *
 */
@Entity
@Table(name="T_OA_SERIALNUMBER_REGULATION",catalog="",schema="")
public class ToaSerialnumberRegulation implements Serializable{

	/** identifier field */
	private String regulationId;
	
	/** nullable persistent field */
	private String sortId;
	
	/** nullable persistent field*/
	private String regulationUnits;
	
	/** nullable persistent field*/
	private String regulationRule;
	
	/** nullable persistent field*/
	private String regulationParagraph;
	
	/** nullable persistent field*/
	private String regulationUser;
	
	/** nullable persistent field*/
	private Date regulationTime;
	
	/** nullable persistent field*/
	private String regulationName;
	
	private String regulationSort;			//文号规则类型：如，发文文号规则；收文文号规则
	
	
	 /** full constructor */
	public ToaSerialnumberRegulation(String regulationId,String sortId,String regulationUnits,
			String regulationRule,String regulationUser,Date regulationTime,String regulationName,String regulationSort){
		  
		this.regulationId=regulationId;
		this.regulationUnits=regulationUnits;
		this.regulationRule=regulationRule;
		this.regulationUser=regulationUser;
		this.regulationTime=regulationTime;
		this.regulationName=regulationName;
		this.regulationSort=regulationSort;
		
	}
	
	/** default constructor*/
	public ToaSerialnumberRegulation(){
	}
	
	/**minimal constructor*/
	public ToaSerialnumberRegulation(String regulationId){
		this.regulationId=regulationId;
	}
	

	 /** 
    *            @hibernate.id
    *             generator-class="assigned"
    *             type="java.lang.String"
    *             column="REGULATION_ID"
    *         
    */
   @Id
	@Column(name="REGULATION_ID")
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid",strategy="uuid")
	public String getRegulationId() {
		return regulationId;
	}
	public void setRegulationId(String regulationId) {
		this.regulationId = regulationId;
	}
	
	/** 
     *            @hibernate.property
     *             column="SORT_ID"
     *             length="32"
     *         
     */
	@Column(name="SORT_ID",nullable=true)
	public String getSortId() {
		return sortId;
	}
	public void setSortId(String sortId) {
		this.sortId = sortId;
	}
	
	/**
	 * @hibernate.property
	 * column="REGULATION_UNITS"
	 * length="32"
	 *
	 */
	@Column(name="REGULATION_UNITS",nullable=false)
	public String getRegulationUnits() {
		return regulationUnits;
	}
	public void setRegulationUnits(String regulationUnits) {
		this.regulationUnits = regulationUnits;
	}
	
	/**
	 * @hibernate.property
	 * column="REGULATION_RULE"
	 * length="100"
	 * 
	 */
	@Column(name="REGULATION_RULE",nullable=true)
	public String getRegulationRule() {
		return regulationRule;
	}
	public void setRegulationRule(String regulationRule) {
		this.regulationRule = regulationRule;
	}
	
	/**
	 * @hibernate.property
	 * column="REGULATION_PARAGRAPH"
	 * length="1"
	 * 
	 */
	@Column(name="REGULATION_PARAGRAPH",nullable=false)
	public String getRegulationParagraph() {
		return regulationParagraph;
	}

	public void setRegulationParagraph(String regulationParagraph) {
		this.regulationParagraph = regulationParagraph;
	}
	
	/**
	 * @hibernate.property
	 * column="REGULATION_USER"
	 * length="32"
	 * @return
	 */
	@Column(name="REGULATION_USER",nullable=true)
	public String getRegulationUser() {
		return regulationUser;
	}
	public void setRegulationUser(String regulationUser) {
		this.regulationUser = regulationUser;
	}
	
	/** 
     *            @hibernate.property
     *             column="REGULATION_TIME"
     *      
     *         
     */
	@Column(name="REGULATION_TIME",nullable=true)
	public Date getRegulationTime() {
		return regulationTime;
	}
	public void setRegulationTime(Date regulationTime) {
		this.regulationTime = regulationTime;
	}
	
	/** 
     *            @hibernate.property
     *             column="REGULATION_NAME"
     *           length="50"
     *         
     */
	@Column(name="REGULATION_NAME",nullable=true)
	public String getRegulationName() {
		return regulationName;
	}
	public void setRegulationName(String regulationName) {
		this.regulationName = regulationName;
	}

	/** 
     *            @hibernate.property
     *             column="REGULATION_SORT"
     *           length="2"
     *         
     */
	@Column(name="REGULATION_SORT",nullable=true)
	public String getRegulationSort() {
		return regulationSort;
	}

	public void setRegulationSort(String regulationSort) {
		this.regulationSort = regulationSort;
	}

	
}
