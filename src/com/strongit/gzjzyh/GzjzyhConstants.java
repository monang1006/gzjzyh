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
	
	//新增银行账号
	public final static String OPERATION_TYPE_ADD_BANKACCOUNT = "0";
	//修改银行账号
	public final static String OPERATION_TYPE_EDIT_BANKACCOUNT = "1";
	//删除银行账号
	public final static String OPERATION_TYPE_DELETE_BANKACCOUNT = "2";
	//提交查询申请
	public final static String OPERATION_TYPE_ADD_APP = "3";
	//拒签查询申请
	public final static String OPERATION_TYPE_REFUSED_APP = "4";
	//反馈查询申请
	public final static String OPERATION_TYPE_RETURN_APP = "5";
	//银行账号个人信息修改
	public final static String OPERATION_TYPE_EDIT_BANKACCOUNT_PERSONAL = "6";
	
	//待审核
	public final static String STATUS_WAIT_AUDIT = "1";
	//审核通过
	public final static String STATUS_AUDIT_PASS = "2";
	//退回
	public final static String STATUS_AUDIT_BACK = "0";
	
	//HTTP通信成功报文
	public final static String HTTP_RESPONSE_SUCCESS = "000000";
	//通信协议报文类型
	public static final String GZJZYH_CONTENT_TYPE = "application/gzjzyh-data";

}
