package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="T_OA_DESKTOP_PORTAL")
public class ToaDesktopPortal implements Serializable {
	
	private String portalId;//门户ID
	
	private String portalName;//门户名
	
	private String portalDesktopId;//桌面方案ID
	
     private String departmentId;//DEPARTMENT_ID
	
	private String topOrgcode;// TOP_ORGCODE
	
	  private String isMoren;//是否为默认门户

	    /** nullable persistent field */
	   private Date setTime;//创建时间
	   
	   private String isEdit ;// 是否可编辑 IS_EDIT

		private String secrec ;// 排序号 SECREC
	   
	   private String rest1;// 备用字段

		private String rest2;// 备用字段
	    
	

	public ToaDesktopPortal() {
	}

	public ToaDesktopPortal(String portalId, String portalName,
			String portalDesktopId) {
		super();
		this.portalId = portalId;
		this.portalName = portalName;
		this.portalDesktopId = portalDesktopId;
	}


	@Column(name="IS_MOREN",nullable=true)
	  public String getIsMoren() {
		return isMoren;
	}

	public void setIsMoren(String isMoren) {
		this.isMoren = isMoren;
	}
	
	@Column(name="SET_TIME",nullable=true)
	public Date getSetTime() {
		return setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	@Id
	  @Column(name="PORTAL_ID", nullable=false, length=32)
	  @GeneratedValue(generator="hibernate-uuid")
	  @GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getPortalId() {
		return portalId;
	}

	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}
	
	@Column(name="PORTAL_NAME",nullable=true)
	public String getPortalName() {
		return portalName;
	}

	public void setPortalName(String portalName) {
		this.portalName = portalName;
	}
	
	@Column(name="PORTAL_DESKTOP_ID",nullable=true)
	public String getPortalDesktopId() {
		return portalDesktopId;
	}

	public void setPortalDesktopId(String portalDesktopId) {
		this.portalDesktopId = portalDesktopId;
	}
	
	@Column(name = "DEPARTMENT_ID")
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "TOP_ORGCODE")
	public String getTopOrgcode() {
		return topOrgcode;
	}

	public void setTopOrgcode(String topOrgcode) {
		this.topOrgcode = topOrgcode;
	}

	@Column(name = "IS_EDIT")
	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	@Column(name = "REST1")
	public String getRest1() {
		return rest1;
	}

	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}

	@Column(name = "REST2")
	public String getRest2() {
		return rest2;
	}

	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}

	@Column(name = "SECREC")
	public String getSecrec() {
		return secrec;
	}

	public void setSecrec(String secrec) {
		this.secrec = secrec;
	}


}
