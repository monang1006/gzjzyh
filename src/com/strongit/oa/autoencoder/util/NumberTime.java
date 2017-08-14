package com.strongit.oa.autoencoder.util;

import java.util.Calendar;
import java.util.Date;

/**
 * <p>Title: NumberTime.java</p>
 * <p>Description: 对于固定字符串上的时间字段进行处理</p>
 * <p>Strongit Ltd. (C) copyright 2009</p>
 * <p>Company: Strong</p>
 * @author 	 于宏洲
 * @date 	 Sep 29, 2010
 * @version  1.0
 */
public class NumberTime {
	
	public static final int longYear = 1;
	public static final int shortYear = 2;
	public static final int month = 3;
	public static final int day = 4;
	private int type = longYear;
	
	public NumberTime(int type){
		this.type = type;
	}
	
	/**
	 * @author:于宏洲
	 * @des   :根据构造函数中type的不同生成不同的日期需求的内容
	 * @return
	 * @date  :Sep 29, 2010
	 */
	public String getNumberTime(){
		Date date = new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		if(type==NumberTime.longYear){
			return String.valueOf(cal.get(cal.YEAR));
		}else if(type==NumberTime.shortYear){
			String sYear = "";
			String year = String.valueOf(cal.get(cal.YEAR));
			sYear = year.substring(year.length()-2, year.length());
			return sYear;
		}else if(type==NumberTime.month){
			String month = String.valueOf(cal.get(cal.MONTH)+1);
			return month;
		}else if(type==NumberTime.day){
			String day = String.valueOf(cal.get(cal.DAY_OF_MONTH));
			return day;
		}
		return null;
	}
	
	public static void main(String args[]){
		System.out.println(new NumberTime(NumberTime.day).getNumberTime());
	}
}
