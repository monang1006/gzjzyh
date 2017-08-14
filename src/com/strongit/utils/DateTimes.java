package com.strongit.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author 钟伟
 * @version 1.0
 * @date 2012-5-24
 */

public class DateTimes
{
	static public Date ymd2Date(String strDate)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try
		{
			date = sdf.parse(strDate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}
	
	static public Date ymd2DateTime(String strDate)
	{
		Date date = ymd2Date(strDate);
		return date2DateTime(date);
	}
	
	static public Date ymd2DateTime(String strDate, Boolean isLast)
	{
		Date date = ymd2Date(strDate);
		return date2DateTime(date, isLast);
	}
	
	static public Date date2DateTime(Date date)
	{
		return date2DateTime(date, false);
	}
	
	static public Date date2DateTime(Date date, Boolean isLast)
	{
		Calendar dar = Calendar.getInstance();
		dar.setTime(date);
		if(isLast)
		{
			dar.set(Calendar.HOUR_OF_DAY, 23);
			dar.set(Calendar.MINUTE, 59);
			dar.set(Calendar.SECOND, 59);		
		}
		else
		{
			dar.set(Calendar.HOUR_OF_DAY, 0);
			dar.set(Calendar.MINUTE, 0);
			dar.set(Calendar.SECOND, 0);
		}
		return dar.getTime();
	}
	
	static public Boolean isDate(String checkValue)
	{
	        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");  
	        Date d = null;  
	        if(checkValue != null && !checkValue.equals(""))  
	        {  
	            if(checkValue.split("/").length > 1)  
	            {  
	                dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	            }  
	            if (checkValue.split("-").length > 1)  
	            {  
	                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	            }  
	        }else  
	        {  
	            return false;  
	        }  
	        try  
	        {  
	            d = dateFormat.parse(checkValue);  
	            System.out.println(d);  
	        }  
	        catch(Exception e)  
	        {  
	            System.out.println("格式错误");  
	            return false;  
	        }  
	        String eL= "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-9]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";  
	        Pattern p = Pattern.compile(eL);   
	        Matcher m = p.matcher(checkValue);   
	        boolean b = m.matches();  
	        if(b)  
	        {  
	              
	            System.out.println("格式正确");
	            return true;
	        }  
	        else  
	        {  
	            System.out.println("格式错误");
	            return false;
	        }  
	}
	
}
