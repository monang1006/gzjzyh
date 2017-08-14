package com.strongit.oa.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.strongmvc.exception.ServiceException;

/**
 * 字符串Util
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Feb 14, 2012 11:36:28 AM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.util.StringUtil
 */
public class StringUtil {
	/**
	 * 将obj转换为String
	 * 
	 * @author 严建
	 * @param obj
	 * @return
	 * @createTime Feb 14, 2012 11:36:52 AM
	 */
	public static String castString(Object obj) {
		return (obj == null ? null : String.valueOf(obj));
	}

	public static String nullOfString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	/**
	 * 将换行转换为＜BR＞
	 * 
	 * @author 严建
	 * @param str
	 * @return
	 * @createTime Mar 23, 2012 9:28:29 AM
	 */
	public static String deWrap(String content) {
		if (content == null) {
			throw new NullPointerException("enWrap方法的参数不能为null");
		}
		String[] contents = content.split("＜BR＞");
		String content_bak = null;
		for (int i = 0; i < contents.length; i++) {
			if (content_bak == null) {
				content_bak = "";
			}
			if (i < contents.length - 1) {
				content_bak += contents[i] + "\r\n";
			} else {
				content_bak += contents[i];
			}
		}
		if (content_bak != null) {
			content = content_bak;
		}
		return content;
	}

	/**
	 * ＜BR＞转换为换行
	 * 
	 * @author 严建
	 * @param str
	 * @return
	 * @createTime Mar 23, 2012 9:28:33 AM
	 */
	public static String enWrap(String content) {
		if (content == null) {
			throw new NullPointerException("deWrap方法的参数不能为null");
		}
		String[] contents = content.split("\r\n");
		String content_bak = null;
		for (int i = 0; i < contents.length; i++) {
			if (content_bak == null) {
				content_bak = "";
			}
			if (i < contents.length - 1) {
				content_bak += contents[i] + "＜BR＞";
			} else {
				content_bak += contents[i];
			}
		}
		if (content_bak != null) {
			content = content_bak;
		}
		return content;
	}

	/**
	 * 数组删掉指定列
	 * 
	 * @description
	 * @author 严建
	 * @param objs
	 * @param index
	 * @return
	 * @createTime May 7, 2012 5:20:21 PM
	 */
	public static Object[] arraydeleteAt(Object[] objs, int index) {
		if (objs == null) {
			throw new NullPointerException("objs is not null");
		}
		if (index < 0) {
			throw new ArithmeticException(" index is not less than 0");
		}
		if (index > objs.length - 1) {
			throw new ArithmeticException(
					" (index+1) is not more than objs's length ");
		}
		Object[] temp = new Object[objs.length - 1];
		int indexT = 0;
		for (Object obj : objs) {
			if (indexT == index) {
				continue;
			} else {
				temp[indexT] = obj;
				indexT++;
			}
		}
		return temp;
	}

	/**
	 * 字符串是否只包含空白符
	 * 
	 * @author 严建
	 * @param str
	 * @return
	 * @createTime May 14, 2012 10:22:31 AM
	 */
	public static boolean isOnlyBlank(String str) {
		if (str == null) {
			throw new NullPointerException("str is not null");
		}
		String dest = "";

		if (str != null) {

			Pattern p = Pattern.compile("\\s*|\t|\r|\n");

			Matcher m = p.matcher(str);

			dest = m.replaceAll("");

		}

		return (dest.length() == 0 ? true : false);

	}

	/**
	 * 将字符串列表转换为以","为分隔符的字符串
	 * 
	 * @description
	 * @author 严建
	 * @return
	 * @createTime May 18, 2012 11:06:45 AM
	 */
	public static String stringListToString(List<String> list) {
		if (list == null) {
			throw new NullPointerException(" list is not null");
		}
		if (list.isEmpty()) {
			throw new ServiceException(" list is Empty");
		}
		StringBuilder result = new StringBuilder();
		for (String str : list) {
			result.append(",").append(str);
		}
		result.deleteCharAt(0);
		return result.toString();
	}

	/**
	 * 将以","为分隔符的字符串转换为字符串列表
	 * 
	 * @description
	 * @author 严建
	 * @param str
	 * @return
	 * @createTime May 18, 2012 3:58:42 PM
	 */
	public static List<String> stringToStringList(String str) {
		if (str == null) {
			throw new NullPointerException(" str is not null");
		}
		if (str.length() == 0) {
			throw new ServiceException(" str length is not zero");
		}
		String[] strs = str.split(",");
		return Arrays.asList(strs);
	}

	public static void main(String[] args) {
		String ss = "1＜BR＞2＜BR＞3＜BR＞4＜BR＞5";
		String s1 = deWrap(ss);
		System.out.println(s1);
		String s2 = enWrap(s1);
		System.out.println(s2);
	}
}
