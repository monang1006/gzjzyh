package com.strongit.gzjzyh;

public class GzjzyhConstants {
	
	//默认银行账号所属机构编码
	public final static String DEFAULT_BANKORG_SYSCODE = "001999";
	
	//未激活账号角色编码
	public final static String UNACTIVE_ROLE = "005";
	//警官所属角色编码
	public final static String POLICE_ROLE = "003";
	//银行账号所属角色编码
	public final static String BANK_ROLE = "004";
	
	//新增
	public final static String OPERATION_TYPE_ADD = "0";
	//修改
	public final static String OPERATION_TYPE_EDIT = "1";
	//删除
	public final static String OPERATION_TYPE_DELETE = "2";
	
	//待审核
	public final static String STATUS_WAIT_AUDIT = "1";
	//审核通过
	public final static String STATUS_AUDIT_PASS = "2";
	//退回
	public final static String STATUS_AUDIT_BACK = "0";

}
