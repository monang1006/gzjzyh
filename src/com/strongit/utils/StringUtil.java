package com.strongit.utils;

import java.util.Collection;
import java.util.Map;

public class StringUtil {
	/**
	 * 如果字符串为NULL，返回空
	 * @param str
	 * @return
	 */
	public static String checkNull(String str){
		if(null==str){
			return "";
		}
		return str;
	}
	
	 /**
	 * author :万俊龙
	 * 日期:2012年12月13日
	 * 判断对象是否Empty(null或元素为0)<br>
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 * 
	 * <pre>
     *      isEmpty(null)   =   true;
     *      isEmpty("")     =   true;
     *      isEmpty("  ")   =   false;
     * </pre>
	 */
	public static boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * author :万俊龙
	 * 日期:2012年12月13日
	 * 判断对象是否为NotEmpty(!null或元素>0)<br>
	 * @param pObj
	 *            待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj == "")
			return false;
		if (pObj instanceof String) {
			if (((String) pObj).length() == 0) {
				return false;
			}
		} else if (pObj instanceof Collection) {
			if (((Collection) pObj).size() == 0) {
				return false;
			}
		} else if (pObj instanceof Map) {
			if (((Map) pObj).size() == 0) {
				return false;
			}
		}
		return true;
	}
	 
	/***
	 * 
	* 方法简要描述： 把字符串转成utf8编码，保证中文文件名不会乱码
	*	 
	* 方法详细描述
	*  
	*
	* JavaDoc tags,比如
	* @Date:Apr 18, 2013
	* @Author 万俊龙
	* @param s
	* @return
	* @version	1.0
	* @see
	 */
     public static String toUtf8String(String s){
         StringBuffer sb = new StringBuffer();
         for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if (c >= 0 && c <= 255){sb.append(c);}
           else{
               byte[] b;
               try { b = Character.toString(c).getBytes("utf-8");}
               catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
               }
               for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
     /**
      * 获取当前jdk版本
      * 
      * @description
      *
      * @author  hecj
      * @date    Mar 26, 2014 11:33:56 AM
      * @param   
      * @return  String
      * @throws
      */
	public static String getCurJDKVersion(){
		return System.getProperty("java.version");
	}
}
