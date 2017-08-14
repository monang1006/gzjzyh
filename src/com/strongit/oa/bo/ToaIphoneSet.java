package com.strongit.oa.bo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name="T_OA_IPHONESET")
public class ToaIphoneSet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//背景图片更新时间
	private Date  iphoneBgUpdate;	
	//模块图标更新时间
	private Date iphoneModularUpdate;
	@Id
	@GeneratedValue	
	private String iphonesetId;
	
	private String rest1;

	private String rest2;

	private String rest3;

	private String rest4;
	
	public ToaIphoneSet() {

	}
	

	public ToaIphoneSet(String iphonesetId, Date  iphoneBgUpdate,Date  iphoneModularUpdate,
			String rest1, String rest2, String rest3, String rest4) {
		super();
		this.iphonesetId = iphonesetId;
		this.iphoneBgUpdate = iphoneBgUpdate;
		this.iphoneModularUpdate = iphoneModularUpdate;
		this.rest1 = rest1;
		this.rest2 = rest2;
		this.rest3 = rest3;
		this.rest4 = rest4;
	}

	@Id
    @Column(name="IPHONESET_ID", nullable=false, length=32)
	@GeneratedValue(generator="hibernate-uuid")
	@GenericGenerator(name="hibernate-uuid", strategy="uuid")
	public String getIphonesetId() {
		return iphonesetId;
	}
	public void setIphonesetId(String iphonesetId) {
		this.iphonesetId = iphonesetId;
	}
	
	@Column(name="IPHONESET_BGUPDATE", nullable=true)
	public Date getIphoneBgUpdate() {
		return iphoneBgUpdate;
	}


	public void setIphoneBgUpdate(Date iphoneBgUpdate) {
		this.iphoneBgUpdate = iphoneBgUpdate;
	}

	@Column(name="IPHONESET_MODULARUPDATE", nullable=true)
	public Date getIphoneModularUpdate() {
		return iphoneModularUpdate;
	}


	public void setIphoneModularUpdate(Date iphoneModularUpdate) {
		this.iphoneModularUpdate = iphoneModularUpdate;
	}


	@Column(name="REST1", nullable=true)
	public String getRest1() {
		return rest1;
	}
	public void setRest1(String rest1) {
		this.rest1 = rest1;
	}
	
	@Column(name="REST2", nullable=true)
	public String getRest2() {
		return rest2;
	}
	public void setRest2(String rest2) {
		this.rest2 = rest2;
	}
	
	@Column(name="REST3", nullable=true)
	public String getRest3() {
		return rest3;
	}
	public void setRest3(String rest3) {
		this.rest3 = rest3;
	}
	
	@Column(name="REST4", nullable=true)
	public String getRest4() {
		return rest4;
	}	
	public void setRest4(String rest4) {
		this.rest4 = rest4;
	}
	

}
