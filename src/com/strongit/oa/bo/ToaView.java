package com.strongit.oa.bo;

import java.util.Date;

/**
 * 大蚂蚁机构实体
 *	@author 李俊勇
 *	@date Apr 19, 2010 11:25:45 AM
 */
public class ToaView {
	private int Col_ID;				//机构ID
	private String Col_Name;		//机构名称
	private int Col_Type; 			//类型 1 系统视图 2自定义视图
	private String Col_Description;	//描述
	private int Col_OwnerID;		//所属者ID
	private Long Col_ItemIndex;		//排序值
	private String Col_Creator_Name;//创建者 -- 存储OA中的机构id
	private Date Col_Dt_Create;		//创建时间
	
	public ToaView(){
		
	}
	
	public ToaView(String Col_Name,Long ItemIndex,String Col_Creator_Name){
		this.Col_Name = Col_Name ;
		this.Col_ItemIndex = ItemIndex;
		this.Col_Creator_Name = Col_Creator_Name;
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
	public int getCol_Type() {
		return Col_Type;
	}
	public void setCol_Type(int col_Type) {
		Col_Type = col_Type;
	}
	public String getCol_Description() {
		return Col_Description;
	}
	public void setCol_Description(String col_Description) {
		Col_Description = col_Description;
	}
	public int getCol_OwnerID() {
		return Col_OwnerID;
	}
	public void setCol_OwnerID(int col_OwnerID) {
		Col_OwnerID = col_OwnerID;
	}
	public Long getCol_ItemIndex() {
		return Col_ItemIndex;
	}
	public void setCol_ItemIndex(Long col_ItemIndex) {
		Col_ItemIndex = col_ItemIndex;
	}
	public String getCol_Creator_Name() {
		return Col_Creator_Name;
	}
	public void setCol_Creator_Name(String col_Creator_Name) {
		Col_Creator_Name = col_Creator_Name;
	}
	public Date getCol_Dt_Create() {
		return Col_Dt_Create;
	}
	public void setCol_Dt_Create(Date col_Dt_Create) {
		Col_Dt_Create = col_Dt_Create;
	}
}
