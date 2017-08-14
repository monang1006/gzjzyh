package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * @hibernate.class
 * table="T_OA_CALENDAR_ASSIGN"
 *
 */

@Entity
@Table(name="T_OA_CALENDAR_ASSIGN")
public class ToaCalendarAssign implements Serializable{
	/**identifier field*/
	private String assignId;
	
	/**授权的用户ID*/
	private String assignUserId;
	
	/**被授权用户ID*/
	private String assignToUserId;

	/**full constructor*/
	public ToaCalendarAssign(String assignUserId,String assignToUserId){
		this.assignUserId=assignUserId;
		this.assignToUserId=assignToUserId;
	}
	public ToaCalendarAssign(){
		
	}
	
	/**
	 * @hibernate.property
	 * generator-class="assigned"
	 * column="ASSIGN_ID"
	 */
	@Id
	@Column(name="ASSIGN_ID",nullable=false,length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	public String getAssignId(){
		return this.assignId;
	}
	
	public void setAssignId(String assignId){
		this.assignId=assignId;
	}
	
	/**
	 * @hibernate.property
	 * column="ASSIGN_USER_ID"
	 * 授权的用户id
	 */
	@Column(name="ASSIGN_USER_ID",nullable=true)
	public String getAssignUserId(){
		return this.assignUserId;
	}
	
	public void setAssignUserId(String assignUserId){
		this.assignUserId=assignUserId;
	}
	
	/**
	 * @hibernate.property
	 * column="ASSIGN_TOUSER_ID"
	 * 被授权的用户id
	 */
	@Column(name="ASSIGN_TOUSER_ID",nullable=true)
	public String getAssignToUserId(){
		return this.assignToUserId;
	}
	
	public void setAssignToUserId(String assignToUserId){
		this.assignToUserId=assignToUserId;
	}
}
