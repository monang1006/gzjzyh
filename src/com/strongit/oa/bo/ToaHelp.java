package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/** 
 *        @hibernate.class
 *         table="T_OA_HELP"
 *     
*/
@Entity
@Table(name="T_OA_HELP",catalog="",schema="")
public class ToaHelp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7141513423389695127L;
	private String helpId;
	private String helptreeId;
	private String helpDesc;
	
	public ToaHelp(String helpId,String helptreeId,String helpDesc){
		this.helpId=helpId;
		this.helptreeId=helptreeId;
		this.helpDesc=helpDesc;
	}
	public ToaHelp(){
		
	}
    @Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name="HELP_DESC", columnDefinition="CLOB", nullable=true)
	public String getHelpDesc() {
		return helpDesc;
	}
	public void setHelpDesc(String helpDesc) {
		this.helpDesc = helpDesc;
	}
    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="MAIL_ID"
     *         
     */
	@Id
	@Column(name="HELP_ID", nullable=true, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getHelpId() {
		return helpId;
	}
	public void setHelpId(String helpId) {
		this.helpId = helpId;
	}
	@Column(name="HELP_TREE_ID")
	public String getHelptreeId() {
		return helptreeId;
	}
	public void setHelptreeId(String helptreeId) {
		this.helptreeId = helptreeId;
	}


}
