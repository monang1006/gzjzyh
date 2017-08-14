package com.strongit.workflow.workflowDesign.action.util;

/**
 * @author yanjian
 *
 * @createTime Aug 17, 2011 流程帮助类
 */
public class ProcessUtil {
	  /**
	   *  把字符串转成utf8编码，保证中文文件名不会乱码
	    * @param s
	    * @return
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
}
