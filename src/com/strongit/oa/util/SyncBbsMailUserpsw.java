package com.strongit.oa.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.strongmvc.exception.SystemException;

/**
 * 同步论坛、邮件的用户名密码
 * @Create Date: 2011-11-29
 * @author luosy
 * @version 1.0
 */
public class SyncBbsMailUserpsw {

	public final static String FILENAME = "appconfig.properties";

	public final static String BBSURL = "bbsurl";
	public final static String MAILURL = "mailurl";

	public final static String SUCCESS = "success";
	public final static String FAILURE = "Failure";
	
	
	private static PropertiesUtil pu = new PropertiesUtil();
	
	/**
	 * @author:luosy
	 * @description:	同步论坛用户
	 * @date : 2011-11-29
	 * @modifyer:
	 * @description:
	 * @param userName
	 * @param passWord
	 * @return
	 * @throws Exception
	 */
	public String setUserPswByUrl(String userName,String passWord,String urlType) throws Exception{
		try{
			if(null==userName||null==passWord||"".equals(userName)||"".equals(passWord)){
				return this.FAILURE;
			}
			
			StringBuilder url = new StringBuilder();
			StringBuffer responseStr = new StringBuffer(""); 
			url.append(pu.getProperty(urlType, FILENAME));
			url.append("?")
				.append("username=").append(userName)
				.append("&")
				.append("password=").append(passWord);
			URL httpUrl = new URL(url.toString());
			HttpURLConnection urlConnection = (HttpURLConnection) httpUrl .openConnection();
			urlConnection.setUseCaches(false);
			urlConnection.setDefaultUseCaches(false);
			urlConnection.setDoOutput(true); 
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Accept", "text/html");
			urlConnection.setRequestProperty("Content-type", "text/html");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Connection", "close");
			urlConnection.connect(); 
			DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
			out.flush();
			out.close();
			
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(urlConnection .getInputStream()));
			String inputLine; 
			while ((inputLine = in.readLine()) != null) {
				responseStr.append(inputLine); 
			}
			
			//System.out.println(responseStr);//begin[1]end or begin[0]end
			String ret = "";
			if(responseStr!=null&&responseStr.length()>0&&responseStr.indexOf("begin[")>-1){
				ret = responseStr.substring("begin[".length(), responseStr.indexOf("]end"));
			}
			
			if("1".equals(ret)){
				return this.SUCCESS;
			}else{
				throw new Exception(urlType+"外部用户同步失败!");
			}
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception(urlType+"外部用户同步失败!");
		}
	}
	
	/**
	 * @author:luosy
	 * @description:	获取邮件或论坛登录地址
	 * @date : 2011-11-29
	 * @modifyer:
	 * @description:
	 * @param key
	 * @return
	 */
	public String getloginURL(String key){
		try {
			return pu.getProperty(key, FILENAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
