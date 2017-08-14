package com.strongit.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.strongmvc.exception.SystemException;

/**
 * 日期工具类
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date Aug 13, 2011
 * @classpath com.strongit.oa.util.DateCountUtil
 * @version 3.0.2
 * @email dengzc@strongit.com.cn
 * @tel 0791-8186916
 */
public class DateCountUtil implements java.io.Serializable {

	/**
	 * 
	 */
	public static final String serialVersionUID = "StrongSoft100130156";//作为密钥
	
	public static final String DES_KEY = "STRONGSOFT_2011";//大蚂蚁链接的用户密码DES算法密钥
	
	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 天数差
	 * @throws ParseException
	 */
	public static long getDistDates(Date startDate, Date endDate) {
		long totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		long timeend = calendar.getTimeInMillis();
		totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
		return totalDate;
	}

	/**
	 * 之前的做法是采用当前日期作为密钥,
	 * 如果遇到大蚂蚁离线消息就会存在密钥失效的问题。
	 * 这里改成固定密钥的方式。
	 * @return
	 * @throws SystemException
	 */
	public static String getNowCompactDatetime() throws SystemException {
		/*String strDate = null;
		try {
			SimpleDateFormat dsdFormat = new SimpleDateFormat("yyyyMMdd");
			strDate = dsdFormat.format(Calendar.getInstance().getTime());
		} catch (Exception ex) {
			throw new SystemException(ex);
		}
		return strDate;*/
		return DES_KEY;
	}

	/**
	 * 接受YYYY-MM-DD的日期字符串参数,返回两个日期相差的天数
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(String start, String end)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(start);
		Date endDate = sdf.parse(end);
		return getDistDates(startDate, endDate);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String start = "2011-08-01";
		String end = "2011-08-05";
		System.out.println(getDistDates(start, end));
	}

}
