package com.strongit.oa.common.business.processendinfo.util;

/**
 * 定义流程结束信息常量
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 29, 2012 4:39:18 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.common.business.processendinfo.util.ProcessEndInfoConst
 */
public class ProcessEndInfoConst {
	/**
	 * 一个子流程驳回，其他子流程自动结束
	 */
	public static String PROCESS_END_STYLE_AUTO = "0";

	/**
	 * 流程正常结束
	 */
	public static String PROCESS_END_STYLE_NORMAL = "1";

	/**
	 * 流程通过驳回方式结束
	 */
	public static String PROCESS_END_STYLE_OVERRULE = "2";
}
