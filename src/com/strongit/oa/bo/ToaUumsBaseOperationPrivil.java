package com.strongit.oa.bo;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 操作权限管理BO类
 * @date 2009年4月27日13:42:14
 * @author 邓志城
 *
 */
@Entity
@Table(name="T_UUMS_BASE_OPERATION_PRIVIL")
public class ToaUumsBaseOperationPrivil implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5472546926176134060L;
	
	/**
	 * 权限流水号
	 */
	private String privilId;
	/**
	 * 权限系统编码
	 */
	private String privilSyscode;
	/**
	 * 权限名称
	 */
	private String privilName;
	/**
	 * 权限属性
	 */
	private String privilAttribute;
	/**
	 * 权限类型（模块权限 OR 功能权限）
	 */
	private String privilType;
	/**
	 * 权限是否启用
	 */
	private String privilIsactive;
	/**
	 * 权限排序号
	 */
	private String privilSequence;
	/**
	 * 权限描述
	 */
	private String privilDescription;
	/**
	 * 预留字段1
	 */
	private String rest1;
	/**
	 * 预留字段2
	 */
	private String rest2;
	/**
	 * 预留字段3
	 */
	private String rest3;
	/**
	 * 预留字段4
	 */
	private String rest4;
	
	/**
	 * 用户权限关联中间表
	 */
	private Set<ToaInfoOperationPrivil> toaInfoOperationprivil;
	/**
	 *	岗位权限关联中间表
	 */
	private Set<ToaPostOperationPrivil> toaPostOperationPrivil;
	
	@Id
	@Column(name="PRIVIL_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getPrivilId() {
		return privilId;
	}
	public void setPrivilId(String privilId) {
		this.privilId = privilId;
	}
	
	@Column(name="PRIVIL_ISACTIVE")
	public String getPrivilIsactive() {
		return privilIsactive;
	}
	public void setPrivilIsactive(String privilIsactive) {
		this.privilIsactive = privilIsactive;
	}
	
	@Column(name="PRIVIL_ATTRIBUTE")
	public String getPrivilAttribute() {
		return privilAttribute;
	}
	public void setPrivilAttribute(String privilAttribute) {
		this.privilAttribute = privilAttribute;
	}
	@Column(name="PRIVIL_DESCRIPTION")
	public String getPrivilDescription() {
		return privilDescription;
	}
	public void setPrivilDescription(String privilDescription) {
		this.privilDescription = privilDescription;
	}
	
	@Column(name="PRIVIL_NAME")
	public String getPrivilName() {
		return privilName;
	}
	public void setPrivilName(String privilName) {
		this.privilName = privilName;
	}
	
	@Column(name="PRIVIL_SEQUENCE")
	public String getPrivilSequence() {
		return privilSequence;
	}
	public void setPrivilSequence(String privilSequence) {
		this.privilSequence = privilSequence;
	}
	
	@Column(name="PRIVIL_SYSCODE")
	public String getPrivilSyscode() {
		return privilSyscode;
	}
	public void setPrivilSyscode(String privilSysCode) {
		this.privilSyscode = privilSysCode;
	}
	
	@Column(name="PRIVIL_TYPE")
	public String getPrivilType() {
		return privilType;
	}
	public void setPrivilType(String privilType) {
		this.privilType = privilType;
	}
	
	@Column(name="REST1")
	public String getRest1() {
		return rest1;
	}
	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}
	
	@Column(name="REST2")
	public String getRest2() {
		return rest2;
	}
	
	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}
	
	@Column(name="REST3")
	public String getRest3() {
		return rest3;
	}
	
	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
	
	@Column(name="REST4")
	public String getRest4() {
		return rest4;
	}
	
	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}
	
	@OneToMany(mappedBy="toaUumsBaseOptPrivil",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	public Set<ToaInfoOperationPrivil> getToaInfoOperationprivil() {
		return toaInfoOperationprivil;
	}
	
	public void setToaInfoOperationprivil(
			Set<ToaInfoOperationPrivil> toaInfoOperationprivil) {
		this.toaInfoOperationprivil = toaInfoOperationprivil;
	}
	@OneToMany(mappedBy="privil",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	public Set<ToaPostOperationPrivil> getToaPostOperationPrivil() {
		return toaPostOperationPrivil;
	}
	
	public void setToaPostOperationPrivil(
			Set<ToaPostOperationPrivil> toaPostOperationPrivil) {
		this.toaPostOperationPrivil = toaPostOperationPrivil;
	}
	@Override
	public boolean equals(Object obj) {
		ToaUumsBaseOperationPrivil t = (ToaUumsBaseOperationPrivil)obj;
		if(t.getPrivilId() == null) {
			return false;
		}
		return t.getPrivilId().equals(this.getPrivilId());
	}

}
