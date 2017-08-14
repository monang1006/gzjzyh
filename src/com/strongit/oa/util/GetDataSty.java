package com.strongit.oa.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class GetDataSty {
	private synchronized String StrPlace(String str,String str1, String str2){   
        String   ret_str ="";   
        int  loc=0,len=str1.length();   
        for (int i=0;i<str.length();i++){   
                int j=str.indexOf(str1,loc);   
                if (j>=0){   
                        ret_str+=str.substring(loc,j)+str2;   
                        loc=j+len;   
                        i=j;   
                }   
        }   
        if   (loc<str.length()){   
                ret_str += str.substring(loc, str.length());   
        }   
        return ret_str;   
}

	 public String getSql(String sql){
		 Properties properties = new Properties(); 
		  URL in = this.getClass().getClassLoader().getResource("appconfig.properties");
		try {
			properties.load(new FileInputStream(in.getFile()));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		 String dataName=sql;	
		 String URL =properties.getProperty("hibernate.connection.url");//获取jdbc.properties文件中hibernate.connection.url的值
		  if(URL!=null&&!URL.startsWith("jdbc:microsoft")){//SQLServer数据库
			 /**********处理ORACLE中的to_date函数***********/
			 dataName=StrPlace(dataName,"to_date(","");
			 dataName=StrPlace(dataName,",'yyyy-mm-dd hh-mi-ss')","");
			 dataName=StrPlace(dataName,",'yyyy-MM-dd hh-mm-ss')","");
			 dataName=StrPlace(dataName,",'yyyy-mm-dd hh24-mi-ss')","");
			 dataName=StrPlace(dataName,",'yyyy-MM-dd hh24-mm-ss')","");
			 dataName=StrPlace(dataName,",'yyyy-mm-dd')","");
			 dataName=StrPlace(dataName,",'yyyy-MM-dd')","");
			 /**********处理ORACLE中的1=1***********/
			 dataName=StrPlace(dataName,"1=1","(1=1)");
			 /**********处理ORACLE中的to_char函数***********/
			 dataName=StrPlace(dataName,"to_char(","substring(convert(char,");
			 dataName=StrPlace(dataName,"'yyyy-mm-dd hh-mi-ss'","20),0,20");
			 dataName=StrPlace(dataName,"'yyyy-MM-dd hh-mm-ss'","20),0,20");
			 dataName=StrPlace(dataName,"'yyyy-mm-dd hh24-mi-ss'","20),0,20");
			 dataName=StrPlace(dataName,"'yyyy-MM-dd hh24-mm-ss'","20),0,20");
			 dataName=StrPlace(dataName,"'yyyy-mm-dd'","20),0,11");
			 dataName=StrPlace(dataName,"'yyyy-mm'","20),0,8");
			 dataName=StrPlace(dataName,"'yyyy-MM'","20),0,8");
			 dataName=StrPlace(dataName,"'yyyy'","20),0,5");
			 /**********处理ORACLE中的length()函数***********/
			 dataName=StrPlace(dataName,"length(","len(");
			 dataName=StrPlace(dataName,"LENGTH(","LEN(");
		     return dataName;
		   
		 }
		 else if(URL!=null&&URL.startsWith("jdbc:mysql")){
			 dataName="mysql";
		 }
		 else if(URL!=null&&URL.startsWith("jdbc:db2")){
			 dataName="db2";
		 }
	    
	    return dataName;
	 }
	 public String getDataBaseSty(){
		 String name="";
		 Properties properties = new Properties(); 
		  URL in = this.getClass().getClassLoader().getResource("jdbc.properties");
		try {
			properties.load(new FileInputStream(in.getFile()));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 	
		 String URL =properties.getProperty("hibernate.connection.url");//获取jdbc.properties文件中hibernate.connection.url的值
		 if(URL!=null&&!URL.startsWith("jdbc:microsoft")){//SQLServer数据库
			 name="sqlserver";
		 }else if(URL!=null&&URL.startsWith("jdbc:mysql")){
			 name="mysql";
		 } else if(URL!=null&&URL.startsWith("jdbc:db2")){
			 name="db2";
		 } else if(URL!=null&&URL.startsWith("jdbc:oracle")){
			 name="oracle";
		 } else{
			 name="";
		 }
		 return name;
	 }
}