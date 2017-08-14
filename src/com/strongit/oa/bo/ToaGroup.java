package com.strongit.oa.bo;

import java.util.Date;

/**
 * 大蚂蚁部门实体
 *	@author 李俊勇
 *	@date Apr 19, 2010 11:25:18 AM
 */
public class ToaGroup {
	private int Col_ID;			//部门ID
	private String Col_Name;	//部门名称
	private String Col_Description;
	private Long Col_ItemIndex;
	private Date Col_Dt_Create;
	private int Col_DomainID;
	private String Col_Creator_Name;
	
	
	public String getCol_Description() {
		return Col_Description;
	}
	public void setCol_Description(String col_Description) {
		Col_Description = col_Description;
	}
	public Long getCol_ItemIndex() {
		return Col_ItemIndex;
	}
	public void setCol_ItemIndex(Long col_ItemIndex) {
		Col_ItemIndex = col_ItemIndex;
	}
	public Date getCol_Dt_Create() {
		return Col_Dt_Create;
	}
	public void setCol_Dt_Create(Date col_Dt_Create) {
		Col_Dt_Create = col_Dt_Create;
	}
	public int getCol_DomainID() {
		return Col_DomainID;
	}
	public void setCol_DomainID(int col_DomainID) {
		Col_DomainID = col_DomainID;
	}
	public int getCol_ID() {
		return Col_ID;
	}
	public void setCol_ID(int col_ID) {
		Col_ID = col_ID;
	}
	public String getCol_Name() {
		return Col_Name;
	}
	public void setCol_Name(String col_Name) {
		Col_Name = col_Name;
	}
	public String getCol_Creator_Name() {
		return Col_Creator_Name;
	}
	public void setCol_Creator_Name(String col_Creator_Name) {
		Col_Creator_Name = col_Creator_Name;
	}
}
