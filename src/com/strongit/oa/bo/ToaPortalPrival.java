package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_OA_PORTAL_PRIVAL")
public class ToaPortalPrival implements Serializable {

	private String id;// 主键ID

	private String privalType;// 权限类型

	private String privalId;// 权限ID

	private String portalId;// 门户ID
	
	private String privalName;		//权限名

	private String rest1;// 备用字段

	private String rest2;// 备用字段
	
	public ToaPortalPrival() {
	}

	  @Id
	  @Column(name="ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="PORTALID",nullable=true)
	public String getPortalId() {
		return portalId;
	}

	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	@Column(name="PRIVALID",nullable=true)
	public String getPrivalId() {
		return privalId;
	}

	public void setPrivalId(String privalId) {
		this.privalId = privalId;
	}

	@Column(name="PRIVALTYPE",nullable=true)
	public String getPrivalType() {
		return privalType;
	}

	public void setPrivalType(String privalType) {
		this.privalType = privalType;
	}

	@Column(name="REST1",nullable=true)
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name="REST2",nullable=true)
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name="PRIVALNAME",nullable=true)
	public String getPrivalName() {
		return privalName;
	}

	public void setPrivalName(String privalName) {
		this.privalName = privalName;
	}

}
