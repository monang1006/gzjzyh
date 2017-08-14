package com.strongit.oa.common.user.util;

import org.apache.log4j.Logger;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

public class TimeKit {
  static Logger logger = Logger.getLogger(TimeKit.class.getName());

  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 获取当前时候


   * @param1:  String disply  时间显示格式
   * return    String　返回当前时间格式字符串


   */

  public final static String now(String disply) {
    String format = "yyyy-MM-dd HH:mm:ss";
    if (disply.equalsIgnoreCase("long")) {
      format = "yyyy-MM-dd HH:mm:ss";
    }
    else if (disply.equalsIgnoreCase("short")) {
      format = "yyyy-MM-dd";
    }
    else if (disply.equalsIgnoreCase("year")) {
      format = "yyyy";
    }
    else if (disply.equalsIgnoreCase("month")) {
      format = "MM";
    }
    else if (disply.equalsIgnoreCase("day")) {
      format = "dd";
    }
    else if (disply.equalsIgnoreCase("time")) {
      format = "HH:mm:ss";
    }else if(disply.equalsIgnoreCase("yearmonth")) {
      format = "yyyyMM";
    }else {
     format =disply;
    }
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date).toString();
  }
  /* Create Datae:  2006-06-01        Create:  吴和建


   * Description: 将字符串时间格式数据，转换成Date数据类型
   * @param1:  String date  时间字符串


   * return    Date　返回转换后的Date类型对象
   */
  public final static Date getDateByString(String date) {
    DateFormat format = DateFormat.getDateInstance();
    try {
      return format.parse(date);
    }
    catch (Exception e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 将字符串时间格式数据，转换成DateTime数据类型
   * @param1:  String date  时间字符串


   * return    Date　返回转换后的DateTime类型对象
   */
  public final static Date getDateTimeByString(String date,String format) {
	  SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			  format);
    try {
      return simpleDateFormat.parse(date);
    }
    catch (Exception e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  public static String nowTime() {
      Calendar now = Calendar.getInstance();
      int year = now.get(Calendar.YEAR);
      int month = now.get(Calendar.MONTH) + 1;
      int day = now.get(Calendar.DAY_OF_MONTH);
      int hour = now.get(Calendar.HOUR_OF_DAY);
      int min = now.get(Calendar.MINUTE);
      int sec = now.get(Calendar.SECOND);
      String time = String.valueOf(year)+ String.valueOf(month)+
                    String.valueOf(day) + String.valueOf(hour)+
                    String.valueOf(min) + String.valueOf(sec);
      return time;
  }

  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 将Date时间对象格式化成字符串


   * @param1:  Date date  时间对象
   * @param2:  String disply 格式字符串


   * return    String　返回转换后时间字符串
   */
  public final static String formatDate(Date date, String disply) {
    if (date == null) {
      return null;
    }
    String format = "yyyy-MM-dd hh:mm:ss";
    if (disply.equalsIgnoreCase("long")) {
      format = "yyyy-MM-dd hh:mm:ss";
    }
    else if (disply.equalsIgnoreCase("short")) {
      format = "yyyy-MM-dd";
    }
    else if (disply.equalsIgnoreCase("year")) {
      format = "yyyy";
    }
    else if (disply.equalsIgnoreCase("month")) {
      format = "MM";
    }
    else if (disply.equalsIgnoreCase("day")) {
      format = "dd";
    }
    else if (disply.equalsIgnoreCase("time")) {
      format = "hh:mm:ss";
    }else{
      format=disply;
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);

    return dateFormat.format(date).toString();
  }
  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 将dateStr时间对象格式化成字符串


   * @param1:  String dateStr  时间字符串


   * @param2:  String disply 格式字符串


   * return    String　返回格式化后时间字符串


   */
  public final static String format(String dateStr, String disply) {
    String format = "yyyy-MM-dd hh:mm:ss";
    if (disply.equalsIgnoreCase("long")) {
      format = "yyyy-MM-dd hh:mm:ss";
    }
    else if (disply.equalsIgnoreCase("short")) {
      format = "MM-dd";
    }
    else if (disply.equalsIgnoreCase("year")) {
      format = "yyyy";
    }
    else if (disply.equalsIgnoreCase("month")) {
      format = "MM";
    }
    else if (disply.equalsIgnoreCase("day")) {
      format = "dd";
    }
    else if (disply.equalsIgnoreCase("time")) {
      format = "hh:mm:ss";
    }
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        "yyyy-MM-dd");
    try {
      date = simpleDateFormat.parse(dateStr);
    }
    catch (ParseException ex) {
      logger.error(dateStr + " parse error!");
    }
    simpleDateFormat = new SimpleDateFormat(format);
    return simpleDateFormat.format(date).toString();
  }



  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 将Calendar时间对象格式化成字符串


   * @param1:  Calendar cal　时间对象
   * return    String　返回格式化后时间字符串（年-月-日）
   */
  public final static String getStringFromCalendar(Calendar cal) {
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    return String.valueOf(year) + "-" + String.valueOf(month) + "-"+String.valueOf(day);
  }

  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 获取字符时间类型加月数后时间对象
   * @param1:  String baseDate　待加时间字符串


   * @param2:  int m　增加月数
   * return    Date　返回加月数后的时间对象


   */
  public final static Date getDateAddMonthByString(String baseDate,int m) throws ParseException {
	  DateFormat startdf = DateFormat.getDateInstance();
      startdf.parse(baseDate);
      Calendar startDate = startdf.getCalendar();
      startDate.add(Calendar.MONTH,m);
      return getDateByString(getStringFromCalendar(startDate));
  }
  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 获取时间类型加月数后时间对象
   * @param1:  Date baseDate　待加时间对象
   * @param2:  int m　增加月数
   * return    Date　返回加月数后的时间对象


   */
  public final static Date getDateAddMonthByDate(Date baseDate,int m) {
	  DateFormat startdf = DateFormat.getDateInstance();
      startdf.format(baseDate);
      Calendar startDate = startdf.getCalendar();
      startDate.add(Calendar.MONTH,m);
      return getDateByString(getStringFromCalendar(startDate));
  }
  
  /* Create Date:  2006-06-01        Create:  吴和建



   * Description: 获取时间类型加日期数后时间对象

   * @param1:  Date baseDate　待加时间对象
   * @param2:  int d　增加日期数

   * return    Date　返回加日期数后的时间对象


   */
  
  public final static Date getDateAddDayhByDate(Date baseDate,int d) {
	  DateFormat startdf = DateFormat.getDateInstance();
      startdf.format(baseDate);
      Calendar startDate = startdf.getCalendar();
      startDate.add(Calendar.DATE,d);
      return getDateByString(getStringFromCalendar(startDate));
  }

  /* Create Date:  2006-06-01        Create:  吴和建


   * Description: 获取两时间对象间的月分之差


   * @param1:  Date fromDate　开始时间对象


   * @param2:  Date toDate　结束时间对象
   * return    int　返回结束时间对象减开始时间对象的月份差


   */
  public final static int getDifferenceBetweenDate(Date fromDate,Date toDate){
	  DateFormat startdf = DateFormat.getDateInstance();
      startdf.format(fromDate);
      Calendar startDateCal = startdf.getCalendar();

      DateFormat toDatedf = DateFormat.getDateInstance();
      toDatedf.format(toDate);
      Calendar toDateCal = toDatedf.getCalendar();

    return (toDateCal.get(Calendar.YEAR)-startDateCal.get(Calendar.YEAR))*12+(toDateCal.get(Calendar.MONTH)-startDateCal.get(Calendar.MONTH));

  }  
   

}
