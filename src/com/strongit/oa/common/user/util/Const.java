package com.strongit.oa.common.user.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Const {
	// 用户类型
	public static String USER_TYPE_SUPER = "0"; // 超级管理员
	public static String USER_TYPE_SYSTEM = "1"; // 系统管理员
	public static String USER_TYPE_MANAGER = "2"; // 管理员
	public static String USER_TYPE_USER = "3"; // 普通用户
	public static String USER_TYPE_ANONY = "4"; // 匿名用户
	// 编码规则代码
	public static String RULE_CODE_ORG = "org";
	public static String RULE_CODE_PRIVIL = "privil";
	public static String RULE_CODE_GROUP = "group";
	public static String RULE_CODE_AREA = "area";
	public static String RULE_CODE_USER = "user";
	// 编码规则类型
	public final static int RULE_TYPE_PARALLEL = 0; // 同级
	public final static int RULE_TYPE_NEXT = 1; // 下级
	// 是否
	public final static String IS_YES = "1"; // 是
	public final static String IS_NO = "0"; // 否
	// 权限类型
	public final static String PRIVIL_TYPE_MANAGE = "0"; // 管理资源权限
	public final static String PRIVIL_TYPE_BUSINESS = "1"; // 业务资源权限
	// 排序字段长度
	public final static int INFO_SEQUENCE_LENGTH = 10;
	// 上移下移标识
	public final static String MOVE_DOWN_IN_TREE = "down"; // 下移
	public final static String MOVE_UP_IN_TREE = "up"; // 上移

	// 对外Web Service接口异常返回信息
	public final static String SERVICE_AUTHENTICATION_EXCEPTION = "服务访问权限异常";
	public final static String SERVICE_DAO_EXCEPTION = "数据级异常";
	public final static String SERVICE_SYSTEM_EXCEPTION = "系统级异常";
	public final static String SERVICE_SERVICE_EXCEPTION = "服务级异常";
	public final static String SERVICE_ELSE_EXCEPTION = "其他未知异常";

	// 对外Web Service连接的合法性标识
	public final static String CURRENT_SERVICE_VALIDITY = "session";

	// 关系型数据库和Ldap关联查询时查询条件每次允许的最大个数
	public final static int RELATION_MAXNUM = 100;

	// 统计登录系统的人数
	public static int LOGIN_SYS_NUM = 0;
	
	//在线用户列表
	public static Map<String,Object> userMap = new LinkedHashMap<String, Object>();
	
	/**
	 * 日志级别
	 */
	//操作日志级别
	public final static String LOG_GRADE_OPERATION = "0";
	//系统日志级别
	public final static String LOG_GRADE_SYSTEM = "1";
	//安全日志级别
	public final static String LOG_GRADE_SECURITY = "2";
}
