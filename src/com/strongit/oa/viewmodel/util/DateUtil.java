package com.strongit.oa.viewmodel.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * Title: DateUtil.java
 * </p>
 * <p>
 * Description: 日期处理
 * </p>
 * <p>
 * Strongit Ltd. (C) copyright 2009
 * </p>
 * <p>
 * Company: Strong
 * </p>
 * 
 * @author 于宏洲
 * @date 2010-06-18
 * @version 1.0
 * @classpath com.strongit.oa.viewmodel.util.DateUtil
 */
public class DateUtil {
	public static String getUtilTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(new Date());
	}

	/**
	 * 将日期字符串，转为Date
	 * 
	 * @description
	 * @author 严建
	 * @param dateString
	 * @param formatString
	 * @return
	 * @createTime Jun 6, 2012 10:45:35 AM
	 */
	public static Date parse(String dateString, String formatString) {
		Date result = null;
		DateFormat format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 将Date，转为日期字符串
	 * 
	 * @description
	 * @author 严建
	 * @param date
	 * @param formatString
	 * @return
	 * @createTime Jun 7, 2012 4:43:22 PM
	 */
	public static String format(Date date,String formatString){
		String result = null;
		DateFormat format = new SimpleDateFormat(formatString);
		result = format.format(date);
		return result;
	}
}
