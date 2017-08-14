package com.engine.util;

/**
 * 
 * Project : StrongIPP
 * Copyright : Strong Digital Technology Co. Ltd.
 * All right reserved
 * @author 曹钦
 * @version 5.0, 2011-8-30
 * Description:数据防SQL注入过滤
 */

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ParameterHandle {
  private HashMap params = new HashMap();

  public ParameterHandle(HttpServletRequest request) {
    Enumeration names = request.getParameterNames();
    String [] replacev;
    try {
      while (names.hasMoreElements()) {
        String name = (String) names.nextElement();
        String value = getGBString(request.getParameterValues(name)[0]);
        //曹钦 20080808  增加数据过滤 begin
        if(value!=null){
        	replacev = new String[]{
        	"--@－－","&","\\\"","\\'","+","$","\\","|"," ","\"@”","%","'@’","<@《",">@》","&lt@《","&gt@》",";","{@｛","}@｝","(@（",")@）",",@，","％"
        	};
        	for(int i=0;i<replacev.length;i++)
        		if(replacev[i].indexOf("@")!=-1)
        	     value = value.replaceAll("\\Q"+replacev[i].split("@")[0]+"\\E",replacev[i].split("@")[1]);
        		else
        		 value = value.replaceAll("\\Q"+replacev[i]+"\\E","");
	        String values = value.toUpperCase(); 
	        replacev = new String[]{"CR","LF","EVAL","OPEN","SYSOPEN","SYSTEM","BS","FT","BEGIN","END",
	        "FOR ","OR ","BY ","AND ","SCRIPT ","INSERT ","SELECT ","UPDATE ","DELETE ","UNTIL ","SET ","DECLARE ","WHERE ",
	        "OPEN ","NEXT ","WHILE ","BEGIN "," BEGIN"," END","END "," CLOSE","CLOSE ","DEALLOCATE ","RTRIM ","CONVERT ","INTO ","FETCH ",
	        "WHEN ","IF ","AS ","LIKE ","EXEC ","ALTER ","DROP ","CREATE ","SHUTDOWN "," SHUTDOWN "
	        };
	        for(int i=0;i<replacev.length;i++)
	          while(values.indexOf(replacev[i])!=-1 ){
	        	value = (value.substring(0,values.indexOf(replacev[i]))+value.substring(values.indexOf(replacev[i])+replacev[i].length()));
	        	values = (values.substring(0,values.indexOf(replacev[i]))+values.substring(values.indexOf(replacev[i])+replacev[i].length()));	
	          }
        }else{
        value = "";
        }
        // 曹钦 20080808  增加数据过滤 end
        params.put(name, value);
     }
    }finally {
      names = null;
      replacev = null;
    }
  }

  public HashMap getParameters() {
    return this.params;
  }
  /**
   * 使含有汉字的字符正常显示汉字
   * @param strToTrans
   * @return
   */
  private  String getGBString(String strToTrans) {
    if (strToTrans == null || strToTrans.trim().length() == 0) {
      return null;
    }
    String tmpString = strToTrans;
      try {
		return new String(tmpString.getBytes("ISO8859_1"), "UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
    	return null;
	}
    
  }
}
