package com.strongit.oa.mymail.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeUtility;
/**
 * 
 * 进行相关的有关邮件处的字符串处理
 * @author yuhz
 *
 */
public class StringUtil {
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:46:41 AM
	 * @desc: 根据字符将字符床转化为List
	 * @param content  所要操作的字符串内容
	 * @param sign	   根据该字符串进行分割
	 * @return List<String>
	 */
	public static List<String> stringToList(String content,String sign){
		List<String> list=null;
		if(content!=null&&!"".equals(content)){
			if(content.indexOf(",")!=-1){
				String temp[]=content.split(",");
				for(int i=0;i<temp.length;i++){
					temp[i]=utf2q(temp[i]);
				}
				list=Arrays.asList(temp);
			}else{
				list=new ArrayList<String>();
				list.add(utf2q(content));
			}
		}else{
			list=new ArrayList<String>();
		}
		return list;
	}
	/**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc:发件人中带有中文的情况的处理
	 * @param str
	 * @return String
	 */
	public static String utf2q(String str){
		if(str!=null&&str.indexOf("<")!=-1){
			if(str.indexOf("<")==0){
				return str;
			}else{
				try {
					str=MimeUtility.encodeText(str.substring(0, str.indexOf("<")))+str.subSequence(str.indexOf("<"),str.length());
					return str;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					return str;
				}
			}
		}else{
			return str;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:49:52 AM
	 * @desc: 根据标志字符来找出对应字符进行解码Q编码至UTF-8
	 * @param content
	 * @param sign
	 * @return String
	 */
	public static String stringToString(String content,String sign){
		String reStr="";
		if(content!=null&&!"".equals(content)){
			if(content.indexOf(",")!=-1){
				String temp[]=content.split(",");
				for(int i=0;i<temp.length;i++){
					if(i!=temp.length-1){
						reStr=reStr+q2utf(temp[i])+",";
					}else{
						reStr=reStr+q2utf(temp[i]);
					}
				}
			}else{
				reStr=q2utf(content);
			}
		}else{
			reStr="";
		}
		return reStr;
	}
	
	/**
     * @author：yuhz
     * @time：Feb 14, 200910:43:20 AM
     * @desc:收件人中带有Q或者B编码的解码情况
	 * @param str
	 * @return String
	 */
	public static String q2utf(String str){
		if(str!=null&&str.indexOf("<")!=-1){						//如果存在"<"号
			if(str.indexOf("<")==0){								//如果"<"号在第一位不做任何处理
				return str;
			}else{													//如果"<"号不在第一位则转码
				try {
					String temp=str.substring(0, str.indexOf("<"));
					if(temp.indexOf("\"")!=-1){
						str=MimeUtility.decodeText(temp.substring(temp.indexOf("\"")+1,temp.lastIndexOf("\"")))+str.subSequence(str.indexOf("<"),str.length());
					}else{
						str=MimeUtility.decodeText(temp)+str.subSequence(str.indexOf("<"),str.length());
					}
					return str;
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					return str;
				}
			}
		}else{														//不存在"<"号的话默认是没有Q或B编码的文字
			return str;
		}
	}
	
	public static String getRetrunStr(String str){
		if(str==null||"null".equals(str)){
			return "";
		}else{
			return str;
		}
	}

}
