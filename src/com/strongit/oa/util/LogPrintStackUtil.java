package com.strongit.oa.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LogPrintStackUtil {

	private static Log logger = LogFactory.getLog(LogPrintStackUtil.class);
	
	public final static String errorMessage = "对不起,操作失败。请重试或与管理员联系."	;
	
	private static String ajaxMessage	; 
	
	/**
	 * author:dengzc
	 * description:记录出错信息
	 * modifyer:
	 * description:
	 * @param logger
	 * @param e
	 */
	public static void printErrorStack(Log logger,Exception e){
		if(logger.isErrorEnabled()){
			logger.error("出现如下异常:", e);
		}
	}

	/**
	 * 记录info信息
	 * @author:邓志城
	 * @date:2009-5-8 下午02:49:41
	 * @param String info 信息内容
	 */
	public static void logInfo(String info){
		if(logger.isInfoEnabled()){
			logger.info(info);
		}
	}

	/**
	 * 记录出错信息
	 * @author:邓志城
	 * @date:2009-9-10 上午09:54:29
	 * @param errorInfo
	 */
	public static void error(String errorInfo){
		if(logger.isErrorEnabled()){
			logger.error(errorInfo);
		}
	}
	
	/**
	 * 记录异常信息
	 * @author:邓志城
	 * @date:2009-5-8 下午02:51:32
	 * @param e 异常信息
	 */
	public static void logException(Exception e){
		if(logger.isErrorEnabled()){
			logger.error(e);
		}
	}

	/**
	 * 记录错误信息和异常信息
	 * @author:邓志城
	 * @date:2009-5-8 下午02:53:05
	 * @param e 异常信息
	 * @param info 错误信息
	 */
	public static void logExceptionInfo(Exception e,String info){
		if(logger.isErrorEnabled()){
			logger.error(info, e);
		}
	}
	
	/**
	 * author:dengzc
	 * description:记录出错信息
	 * modifyer:
	 * description:
	 * @param logger
	 * @param e
	 */
	public static void printInfoStack(Log logger,Exception e){
		if(logger.isInfoEnabled()){
			logger.info("出现如下异常:",e);
		}
	}
	
	/**
	 * author:dengzc
	 * description:记录信息
	 * modifyer:
	 * description:
	 * @param logger
	 * @param e
	 */
	public static void printInfo(Log logger,String str){
		if(logger.isInfoEnabled()){
			logger.info(str);
		}
	}

	public static String getAjaxMessage() {
		return ajaxMessage;
	}

	public static void setAjaxMessage(String ajaxMessage) {
		LogPrintStackUtil.ajaxMessage = ajaxMessage;
	}
}
