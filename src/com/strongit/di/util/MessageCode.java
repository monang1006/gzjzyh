package com.strongit.di.util;

/**
 * <p>
 * Title: Strong Data Interchange System
 * </p>
 * <p>
 * Description: Message Code
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007 Strong Co. Ltd.
 * </p>
 * <p>
 * Company: Strong Co. Ltd.
 * </p>
 * 
 * @author xuzm
 * @version 1.0
 */
public class MessageCode {

	public static String MESSAGETYPE_SYSTEM = "0";// 系统级消息类型

	public static String MESSAGETYPE_BUSINESS = "1";// 业务级消息类型

	public static String MESSAGETYPE_COMMUNICATE = "2";// 通信级消息类型

	public static String MESSAGETYPE_SYSTEM_UNKNOWN = "00";// 未知或其他

	public static String MESSAGETYPE_SYSTEM_DATABASE_CONN = "02";// 数据库连接异常

	public static String MESSAGETYPE_SYSTEM_DATABASE_SQL = "03";// 数据库SQL异常

	public static String MESSAGETYPE_SYSTEM_PARA = "03";// 参数无效

	public static String MESSAGETYPE_BUSINESS_NODATA = "10";// 没有数据

	public static String MESSAGETYPE_SYSTEM_NOBID = "21";// 没有BID

	public static String MESSAGETYPE_BUSINESS_URSEPWD = "91";// 用户密码错误

	public static String MESSAGETYPE_BUSINESS_NOURSE = "92";// 没有该用户

	public static String MESSAGETYPE_BUSINESS_NOURSEORPWD = "93";// 没有该用户
	public static String MESSAGETYPE_BUSINESS_NOPID = "22";// 没有pid
	public static String MESSAGETYPE_BUSINESS_INVALIDXML = "23";// xml数据格式异常

	/**
	 * 获取组装之后的业务级异常编码
	 * 
	 * @param messageCode
	 * @return
	 */
	public static String getBusinessMessageCode(String messageCode) {
		return new StringBuffer().append(MESSAGETYPE_BUSINESS).append(
				messageCode).toString();
	}

	/**
	 * 获取组装之后的业务级异常编码
	 * 
	 * @return
	 */
	public static String getBusinessMessageCode() {
		return new StringBuffer().append(MESSAGETYPE_BUSINESS).append(
				MESSAGETYPE_SYSTEM_UNKNOWN).toString();
	}

	/**
	 * 获取组装之后的系统级异常编码
	 * 
	 * @param messageCode
	 * @return
	 */
	public static String getSystemMessageCode(String messageCode) {
		return new StringBuffer().append(MESSAGETYPE_SYSTEM)
				.append(messageCode).toString();
	}

	/**
	 * 获取组装之后的系统级异常编码
	 * 
	 * @return
	 */
	public static String getSystemMessageCode() {
		return new StringBuffer().append(MESSAGETYPE_SYSTEM).append(
				MESSAGETYPE_SYSTEM_UNKNOWN).toString();
	}

	/**
	 * 获取组装之后的SQL异常编码
	 * 
	 * @return
	 */
	public static String getSystemSQLExMessageCode() {
		return new StringBuffer().append(MESSAGETYPE_SYSTEM).append(
				MESSAGETYPE_SYSTEM_DATABASE_SQL).toString();
	}

	/**
	 * 获取组装之后的数据库连接异常编码
	 * 
	 * @return
	 */
	public static String getSystemDBConnExMessageCode() {
		return new StringBuffer().append(MESSAGETYPE_SYSTEM).append(
				MESSAGETYPE_SYSTEM_DATABASE_CONN).toString();
	}

	/**
	 * 获取组装之后的用户密码错误编码
	 * 
	 * @return
	 */
	public static String getSystemUserPwdExMessageCode() {
		return new StringBuffer().append(MESSAGETYPE_BUSINESS).append(
				MESSAGETYPE_BUSINESS_URSEPWD).toString();
	}
	
	

	/**
	 * 
	 * @author qintai StrongOA_CST Apr 21, 2010 11:43:03 AM
	 * @param contextPath
	 *            路径
	 * @param methodName
	 *            submitForm方法名
	 * @param msg
	 *            提示
	 * @return
	 */

	public static String closeAndRemandHtml(String contextPath,
			String methodName, String msg) {
		StringBuffer returnhtml = new StringBuffer("");
		returnhtml.append("<script> var scriptroot = '").append(contextPath)
				.append("'</script>").append("<SCRIPT src='").append(
						contextPath).append(
						"/common/js/commontab/workservice.js'>").append(
						"</SCRIPT>").append("<SCRIPT src='")
				.append(contextPath)
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>");
		if (msg != null && !"".equals(msg)) {
			// returnhtml.append("alert('").append(msg).append("');");
		}
		if (methodName != null && !"".equals(methodName)
				&& !"null".equals(methodName)) {
			returnhtml.append("window.dialogArguments.").append(methodName)
					.append("();");
		}
		returnhtml.append("window.returnValue ='reload';window.close();").append("</script>");
		return returnhtml.toString();
	}
	
	public static String closeAndHtml(String contextPath,
			String methodName, String msg) {
		StringBuffer returnhtml = new StringBuffer("");
		returnhtml.append("<script> var scriptroot = '").append(contextPath)
				.append("'</script>").append("<SCRIPT src='").append(
						contextPath).append(
						"/common/js/commontab/workservice.js'>").append(
						"</SCRIPT>").append("<SCRIPT src='")
				.append(contextPath)
				.append("/common/js/commontab/service.js'>")
				.append("</SCRIPT>").append("<script>");
//		if (msg != null && !"".equals(msg)) {
//			returnhtml.append("alert('").append(msg).append("');");
//		}
		if (methodName != null && !"".equals(methodName)
				&& !"null".equals(methodName)) {
			returnhtml.append("window.dialogArguments.").append(methodName)
					.append("();");
		}
		returnhtml.append("window.returnValue ='unreload';window.close();").append("</script>");
		return returnhtml.toString();
	}
}
