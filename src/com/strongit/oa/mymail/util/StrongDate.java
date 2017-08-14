/**
 * 作用：通用的日期作用类
 * 作者：于宏洲
 * 日期：2008.06.15
 * 版本：1.0
 */

package com.strongit.oa.mymail.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class StrongDate {
	
	public static final int heng=1;			//日期格式2008-06-16
	public static final int xiegang=2;		//日期格式2008/06/16
	public static final int fulldate=3;		//日期格式2008/06/16 09:46:32
	public static final int dian=4;			//日期格式2008.06.16
	public static final int chinese=5;		//日期格式2008年06月16日
	public static final int fullheng=6;		//日期格式2008-06-16 09:46:32

	/**
	 * 作用：将字符串转化为日期类型
	 * @param operDate :当前要进行操作的日期
	 * @param type     :希望通过操作返回的日期类型
	 * @return
	 */
	
	public static Date strToDate(String operDate,int type){
		Date nowDate=null;
		SimpleDateFormat dateFormat=null;
		if(type==heng){
			dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		}else if(type==xiegang){
			dateFormat=new SimpleDateFormat("yyyy/MM/dd");
		}else if(type==fulldate){
			dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
		}
		try{
			nowDate=dateFormat.parse(operDate);
		}catch(Exception e){
			e.printStackTrace();
		}
		return nowDate;
	}
	
	/**
	 * 
	 * @param operDate :日期
	 * @param type     :返回的日期字符串的类型
	 * @return         :返回日期型字符串
	 */
	
	public static String getDate(Date operDate,int type){
		
		String nowDate=null;
		
		if(operDate==null){
			return "";
		}
		
		SimpleDateFormat dateFormat=null;
		
		if(type==StrongDate.heng){
			
			dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			
		}else if(type==StrongDate.xiegang){
			
			dateFormat=new SimpleDateFormat("yyyy/MM/dd");
			
		}else if(type==StrongDate.fulldate){
			
			dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
			
		}else if(type==dian){
			
			dateFormat=new SimpleDateFormat("yyyy.MM.dd");
			
		}else if(type==chinese){
			
			dateFormat=new SimpleDateFormat("yyyy年MM月dd日");
			
		}else if(type==fullheng){
			dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
		nowDate=dateFormat.format(operDate);	
		
		return nowDate;
		
	}
	
	/**
	 * 作　　用：获得一个月的第一天
	 * @param operDate 当前所要操作的日期
	 * @return
	 */
	
    public static String getMonthBegin(Date operDate) {
    	
    	String begin=null;
    	
    	int year=operDate.getYear()+1900;
    	
    	int month=operDate.getMonth()+1;
    	
    	if(month<10){
    		
    		begin=year+"-0"+month+"-01";
    		
    	}else{
    		
    		begin=year+"-"+month+"-01";
    		
    	}
    	
		return begin;
		
	}   
	
	/**
	 * 作　　用：获得一个月的最后一天
	 * @param  operDate :要进行操作的日期
	 * @return
	 * @设计思想：主要是利用Calendar类进行日期操作，首先得到当前月份的第一天，
	 *           然后在进行对月份加1然后对日期减1就得到了这个月的最后一天
	 */
    
    public static String getMonthEnd(Date operDate){   	
    		Date montBegin=strToDate(getMonthBegin(operDate),heng);
            Calendar calendar=Calendar.getInstance();   
            calendar.setTime(montBegin);   
            calendar.add(Calendar.MONTH,1);   
            calendar.add(Calendar.DAY_OF_YEAR,-1); 
            return getDate(calendar.getTime(),heng);   
    }
    
    /**
     * 作   用：得到所操作日期的是星期几
     * @param operDate
     * @return
     */
    
    public static String getWeekday(String operDate){
    	String weekday=null;
    	Date nowDate=strToDate(operDate,heng);
    	SimpleDateFormat format=new SimpleDateFormat("E");
    	weekday=format.format(nowDate);
    	return weekday;
    }
    
    /**
     * 作   用：上面方法的一个重载方法
     * @param operDate
     * @return
     */
    
    public static String getWeekday(Date operDate){
    	String weekday=null;
    	SimpleDateFormat format=new SimpleDateFormat("E");
    	weekday=format.format(operDate);
    	return weekday;
    }
	
}
