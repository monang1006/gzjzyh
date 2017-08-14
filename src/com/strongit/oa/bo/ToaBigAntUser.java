package com.strongit.oa.bo;

import java.util.Date;

/**
 * 大蚂蚁人员实体
 *	@author 李俊勇
 *	@date Apr 19, 2010 11:29:22 AM
 */
public class ToaBigAntUser {
	private int Col_ID;				//人员ID
	private String Col_LoginName;	//帐号
	private String Col_Name;		//用户名
	private String Col_PWord;		//密码
	private String Col_IsSuper;		//1 是超级用户  0不是
	private String Col_IsSystem;	//1 是系统帐号  0不是
	private String Col_Disabled;	//1 为禁用帐号  0有效帐号
	private int Col_EnType;			//1 MD5加密    0 明码
	private int Col_DomainID;		//域ID
	private Long Col_ItemIndex;		//排序值(小的在前面)
	private Date Col_Dt_Create;		//创建时间
	private String Col_Creator_Name;//创建者
	private int Col_OnlineState;	//在线状态 0不在线 1在线
	private String Col_Note;		//用户备注
	private String Col_EMail;		//EMAIL
	private String Col_Mobile;		//手机号码
	private int Col_Sex;			//性别
	private String Col_Description;//描述
	private Date Col_Birthday;		//生日
	private String Col_h_Province;	//家庭所在省份
	private String Col_h_City;		//家庭所在城市
	private String Col_h_Address;	//家庭地址
	private String Col_h_PostCode;	//家庭邮编
	private String Col_h_Phone;		//家庭电话
	private String Col_h_Fax;		//家庭传真
	
	private String Col_o_CompanyName;//单位名称
	private String Col_o_Address;		//单位地址
	private String Col_o_PostCode;	//单位邮编
	private String Col_o_JobTitle;	//职务
	private String Col_o_Phone;		//单位电话
	private String Col_o_Fax;		//单位传真
	private String Col_ECard;		//身份证号码
	private String Col_DeptInfo;	//所在部门名称
	private String Col_IsDelete;	//1 用户已删除 0 有效
	
	private String Col_HomePage;	//OA中的用户id
	public String getCol_IsDelete() {
		return Col_IsDelete;
	}
	public void setCol_IsDelete(String col_IsDelete) {
		Col_IsDelete = col_IsDelete;
	}
	public int getCol_ID() {
		return Col_ID;
	}
	public void setCol_ID(int col_ID) {
		Col_ID = col_ID;
	}
	public String getCol_LoginName() {
		return Col_LoginName;
	}
	public void setCol_LoginName(String col_LoginName) {
		Col_LoginName = col_LoginName;
	}
	public String getCol_Name() {
		return Col_Name;
	}
	public void setCol_Name(String col_Name) {
		Col_Name = col_Name;
	}
	public String getCol_PWord() {
		return Col_PWord;
	}
	public void setCol_PWord(String col_PWord) {
		Col_PWord = col_PWord;
	}
	public String getCol_IsSuper() {
		return Col_IsSuper;
	}
	public void setCol_IsSuper(String col_IsSuper) {
		Col_IsSuper = col_IsSuper;
	}
	public String getCol_IsSystem() {
		return Col_IsSystem;
	}
	public void setCol_IsSystem(String col_IsSystem) {
		Col_IsSystem = col_IsSystem;
	}
	public String getCol_Disabled() {
		return Col_Disabled;
	}
	public void setCol_Disabled(String col_Disabled) {
		Col_Disabled = col_Disabled;
	}
	public int getCol_EnType() {
		return Col_EnType;
	}
	public void setCol_EnType(int col_EnType) {
		Col_EnType = col_EnType;
	}
	public int getCol_DomainID() {
		return Col_DomainID;
	}
	public void setCol_DomainID(int col_DomainID) {
		Col_DomainID = col_DomainID;
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
	public String getCol_Creator_Name() {
		return Col_Creator_Name;
	}
	public void setCol_Creator_Name(String col_Creator_Name) {
		Col_Creator_Name = col_Creator_Name;
	}
	public int getCol_OnlineState() {
		return Col_OnlineState;
	}
	public void setCol_OnlineState(int col_OnlineState) {
		Col_OnlineState = col_OnlineState;
	}
	public String getCol_Note() {
		return Col_Note;
	}
	public void setCol_Note(String col_Note) {
		Col_Note = col_Note;
	}
	public String getCol_EMail() {
		return Col_EMail;
	}
	public void setCol_EMail(String col_EMail) {
		Col_EMail = col_EMail;
	}
	public String getCol_Mobile() {
		return Col_Mobile;
	}
	public void setCol_Mobile(String col_Mobile) {
		Col_Mobile = col_Mobile;
	}
	public int getCol_Sex() {
		return Col_Sex;
	}
	public void setCol_Sex(int col_Sex) {
		Col_Sex = col_Sex;
	}
	public String getCol_Description() {
		return Col_Description;
	}
	public void setCol_Description(String col_Description) {
		Col_Description = col_Description;
	}
	public Date getCol_Birthday() {
		return Col_Birthday;
	}
	public void setCol_Birthday(Date col_Birthday) {
		Col_Birthday = col_Birthday;
	}
	public String getCol_h_Province() {
		return Col_h_Province;
	}
	public void setCol_h_Province(String col_h_Province) {
		Col_h_Province = col_h_Province;
	}
	public String getCol_h_City() {
		return Col_h_City;
	}
	public void setCol_h_City(String col_h_City) {
		Col_h_City = col_h_City;
	}
	public String getCol_h_Address() {
		return Col_h_Address;
	}
	public void setCol_h_Address(String col_h_Address) {
		Col_h_Address = col_h_Address;
	}
	public String getCol_h_PostCode() {
		return Col_h_PostCode;
	}
	public void setCol_h_PostCode(String col_h_PostCode) {
		Col_h_PostCode = col_h_PostCode;
	}
	public String getCol_h_Phone() {
		return Col_h_Phone;
	}
	public void setCol_h_Phone(String col_h_Phone) {
		Col_h_Phone = col_h_Phone;
	}
	public String getCol_h_Fax() {
		return Col_h_Fax;
	}
	public void setCol_h_Fax(String col_h_Fax) {
		Col_h_Fax = col_h_Fax;
	}
	public String getCol_o_CompanyName() {
		return Col_o_CompanyName;
	}
	public void setCol_o_CompanyName(String col_o_CompanyName) {
		Col_o_CompanyName = col_o_CompanyName;
	}
	public String getCol_o_Address() {
		return Col_o_Address;
	}
	public void setCol_o_Address(String col_o_Address) {
		Col_o_Address = col_o_Address;
	}
	public String getCol_o_PostCode() {
		return Col_o_PostCode;
	}
	public void setCol_o_PostCode(String col_o_PostCode) {
		Col_o_PostCode = col_o_PostCode;
	}
	public String getCol_o_JobTitle() {
		return Col_o_JobTitle;
	}
	public void setCol_o_JobTitle(String col_o_JobTitle) {
		Col_o_JobTitle = col_o_JobTitle;
	}
	public String getCol_o_Phone() {
		return Col_o_Phone;
	}
	public void setCol_o_Phone(String col_o_Phone) {
		Col_o_Phone = col_o_Phone;
	}
	public String getCol_o_Fax() {
		return Col_o_Fax;
	}
	public void setCol_o_Fax(String col_o_Fax) {
		Col_o_Fax = col_o_Fax;
	}
	public String getCol_ECard() {
		return Col_ECard;
	}
	public void setCol_ECard(String col_ECard) {
		Col_ECard = col_ECard;
	}
	public String getCol_DeptInfo() {
		return Col_DeptInfo;
	}
	public void setCol_DeptInfo(String col_DeptInfo) {
		Col_DeptInfo = col_DeptInfo;
	}
	public String getCol_HomePage() {
		return Col_HomePage;
	}
	public void setCol_HomePage(String col_HomePage) {
		Col_HomePage = col_HomePage;
	}
}
