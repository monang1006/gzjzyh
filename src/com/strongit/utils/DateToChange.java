package com.strongit.utils;

import java.util.Date;

public class DateToChange {
	
	 public String toYear(String year)
	{
		String years[] = year.split("");
		for(int i=0;i<years.length;i++){
			if(years[i].equals("1")){
				years[i]= "一";
			}
			else if(years[i].equals("2")){
				years[i]= "二";
			}
			else if(years[i].equals("3")){
				years[i]= "三";
			}
			else if(years[i].equals("4")){
				years[i]= "四";
			}
			else if(years[i].equals("5")){
				years[i]= "五";
			}
			else if(years[i].equals("6")){
				years[i]= "六";
			}
			else if(years[i].equals("7")){
				years[i]= "七";
			}
			else if(years[i].equals("8")){
				years[i]= "八";
			}
			else if(years[i].equals("9")){
				years[i]= "九";
			}
			
		}
		String yy = "";
		for(int j =0;j<years.length;j++){
			yy = yy+years[j];
		}
		return yy;
	}
	
	 public String toMonth(String month)
	{
			if(month.equals("01")){
				month= "一";
			}
			else if(month.equals("02")){
				month= "二";
			}
			else if(month.equals("03")){
				month= "三";
			}
			else if(month.equals("04")){
				month= "四";
			}
			else if(month.equals("05")){
				month= "五";
			}
			else if(month.equals("06")){
				month= "六";
			}
			else if(month.equals("07")){
				month= "七";
			}
			else if(month.equals("08")){
				month= "八";
			}
			else if(month.equals("09")){
				month= "九";
			}
			else if(month.equals("10")){
				month= "十";
			}
			else if(month.equals("11")){
				month= "十一";
			}
			else if(month.equals("12")){
				month= "十二";
			}
			
		return month;
	}

}
