package com.strongit.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class CommonUtilWay {
	/**
	 * @param String firstdate 相对大的日期
	 * @param String lastdate  相对小的日期
	 * 
	 */
	public static  int compareStringDate(String firstdate,String lastdate,int type){
		if(firstdate==null||lastdate==null) return GlobalBaseData.nullresult;
		try{
			
			int yearone = 0, monthone = 0, dayone = 0;

			int yeartwo = 0, monthtwo = 0, daytwo = 0;
			
			if (firstdate.length() >= 4) {
				if (firstdate.substring(0, 4) != null|| !"".equals(firstdate.substring(0, 4))) {

					yearone = Integer.parseInt(firstdate.substring(0, 4));

				}
			}

			if (firstdate.length() >= 5) {
				if (firstdate.substring(5, 7) != null|| !"".equals(firstdate.substring(5, 7))) {

					monthone = Integer.parseInt(firstdate.substring(5, 7));

				}
			}

			if (firstdate.length() >=8) {

				if (firstdate.substring(8, 10) != null|| !"".equals(firstdate.substring(8, 10))) {

					dayone = Integer.parseInt(firstdate.substring(8, 10));

				}
			}

			if (lastdate.length() >= 4) {
				if (lastdate.substring(0, 4) != null|| !"".equals(lastdate.substring(0, 4))) {

					yeartwo = Integer.parseInt(lastdate.substring(0, 4));

				}
			}
			if (lastdate.length() >= 5) {
				if (lastdate.substring(5, 7) != null|| !"".equals(lastdate.substring(5, 7))) {

					monthtwo = Integer.parseInt(lastdate.substring(5, 7));

				}
			}

			if (lastdate.length() >=8) {
				if (lastdate.substring(8, 10) != null|| !"".equals(lastdate.substring(8, 10))) {

					daytwo = Integer.parseInt(lastdate.substring(8, 10));

				}
			}
			
			if(type==GlobalBaseData.compare_year){//只比较年
				monthone=monthtwo=0;
				dayone=daytwo=0;
			}else if(type==GlobalBaseData.compare_yearmonth){//比较年月
				dayone=daytwo=0;
			}
				
			
			if(yearone>yeartwo)
				return GlobalBaseData.morethan;
			else if(yearone==yeartwo&&monthone>monthtwo)
				return GlobalBaseData.morethan;
			else if(yearone==yeartwo&&monthone==monthtwo&&dayone>daytwo)
				return GlobalBaseData.morethan;
			else if(yearone==yeartwo&&monthone==monthtwo&&dayone==daytwo)
				return GlobalBaseData.equal;
			else 
				return GlobalBaseData.lessthan;
		}catch(NullPointerException e){
			e.printStackTrace();
			return GlobalBaseData.nullresult;
		}catch(NumberFormatException e){
			e.printStackTrace();
			return GlobalBaseData.nullresult;
		}
	}
	
	
	/**
	 * 功能描述：日期间的差值
	 * @param String firstdate 相对大的日期
	 * @param String lastdate  相对小的日期
	 * 
	 */
	
	public static  int computerDate(String firstdate,String lastdate){
		if(firstdate==null||lastdate==null) return 0;
		try{
			
			int yearone = 0, monthone = 0, dayone = 0;

			int yeartwo = 0, monthtwo = 0, daytwo = 0;

			if (firstdate.length() >=4) {
				if (firstdate.substring(0, 4) != null|| !"".equals(firstdate.substring(0, 4))) {

					yearone = Integer.parseInt(firstdate.substring(0, 4));

				}
			}

			if (firstdate.length() >=5) {
				if (firstdate.substring(5, 7) != null|| !"".equals(firstdate.substring(5, 7))) {

					monthone = Integer.parseInt(firstdate.substring(5, 7));

				}
			}

			if (firstdate.length() >=8) {

				if (firstdate.substring(8, 10) != null|| !"".equals(firstdate.substring(8, 10))) {

					dayone = Integer.parseInt(firstdate.substring(8, 10));

				}
			}

			if (lastdate.length() >=4) {
				if (lastdate.substring(0, 4) != null|| !"".equals(lastdate.substring(0, 4))) {

					yeartwo = Integer.parseInt(lastdate.substring(0, 4));

				}
			}
			if (lastdate.length() >=5) {
				if (lastdate.substring(5, 7) != null|| !"".equals(lastdate.substring(5, 7))) {

					monthtwo = Integer.parseInt(lastdate.substring(5, 7));

				}
			}

			if (lastdate.length() >=8) {
				if (lastdate.substring(8, 10) != null|| !"".equals(lastdate.substring(8, 10))) {

					daytwo = Integer.parseInt(lastdate.substring(8, 10));

				}
			}
	
			
			if (yearone >= yeartwo) {
				if (yeartwo == yearone) {
					return 1;
				} else {
					
					if (dayone != 0 && daytwo != 0) {//日期中的日不存在时

						if ((yearone - yeartwo) >= 1&& ((monthone - monthtwo) < 0 || (monthone == monthtwo && (dayone - daytwo) <= 0))) {

							return (yearone - yeartwo);

						} else if ((yearone - yeartwo) >= 1	&& (((monthone - monthtwo) > 0) || (monthtwo == monthone && (dayone - daytwo) > 0))) {

							return (yearone - yeartwo) + 1;

						} else {

							return 0;
						}
					}else{
						
						if((yearone - yeartwo) >= 1 && (monthone - monthtwo) <=0 ){
							return (yearone - yeartwo);
						}else if((yearone - yeartwo) >= 1	&& ((monthone - monthtwo) > 0)){
							
							return (yearone - yeartwo) + 1;
							
						}else{
							return 0;
						}
						
						
					}
					
				}
			}else{
				
				return 0;
			}

		}catch(NullPointerException e){
			e.printStackTrace();
			return 0;
		}catch(NumberFormatException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 功能描述：获取当前时间的下一个月

	 * @param String date 当前时间
	 * 
	 */
	public static   String getNextMonth(String date){
		if(date==null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");		
		Date nowDate=null;
		try {
			nowDate = sdf.parse(date);
		} catch (ParseException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar can=Calendar.getInstance();
		can.setTime(nowDate);
		can.add(Calendar.MONTH,1);//当前月加1
		String nextMonth = sdf.format(can.getTime());
		return nextMonth;
	}
    
	/**
	 * 取得以数字开头并包含其它类型字符中开头上的数字如：12354中国，其返回结果就是12354
	 * @param value
	 * @return
	 */
   public static String getNumber(String value){
	   int length=value.length();
	   int j=0;
	   String numberValue="0";
	   for(int i=0;i<length;i++){
		  String tempchar=value.substring(i,i+1);	
		   j=i;
		   if(String.valueOf(tempchar).equals(".")){
			   continue;
		   }
		   try{
			   Integer.parseInt(String.valueOf(tempchar));
		   }catch(Exception e){
			   numberValue=value.substring(0,i);
			   break;
		   }		
	   }
	   return numberValue;
   }
}
