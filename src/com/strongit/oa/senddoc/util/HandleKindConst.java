package com.strongit.oa.senddoc.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Const 文的处理类别
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 14, 2012 11:22:48 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.util.HandleKind
 */
public class HandleKindConst {

	/**
	 * 个人办公
	 */
	private static String HANDLE_KIND_PERSONAL_PREFIX = "personal"; // 前缀

	private static String HANDLE_KIND_PERSONAL_NUM = "0"; // 标识号

	/**
	 *发文处理
	 */
	private static String HANDLE_KIND_SENDDOC_PREFIX = "senddoc"; // 前缀

	private static String HANDLE_KIND_SENDDOC_NUM = "1"; // 标识号

	/**
	 * 收文处理
	 */
	private static String HANDLE_KIND_RECVDOC_PREFIX = "recvdoc"; // 前缀

	private static String HANDLE_KIND_RECVDOC_NUM = "2"; // 标识号

	private static Map<String, String> ruleMap = new HashMap<String, String>();
	static {
		ruleMap.put(HANDLE_KIND_PERSONAL_NUM, HANDLE_KIND_PERSONAL_PREFIX);
		ruleMap.put(HANDLE_KIND_SENDDOC_NUM, HANDLE_KIND_SENDDOC_PREFIX);
		ruleMap.put(HANDLE_KIND_RECVDOC_NUM, HANDLE_KIND_RECVDOC_PREFIX);
	}

	/**
	 * 根据HANDLE_KIND返回对应的结果
	 * 
	 * @author 严建
	 * @param num
	 * @param result
	 * @return
	 * @createTime Mar 14, 2012 11:59:10 AM
	 */
	public static String getReturnString(String num, String result) {
		if (!ruleMap.containsKey(num)) {
			return result;
		} else {
			return ruleMap.get(num).concat(result);
		}
	}

	
}
