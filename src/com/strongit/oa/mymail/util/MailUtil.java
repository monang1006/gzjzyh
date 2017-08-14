package com.strongit.oa.mymail.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.Security;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeUtility;

import com.strongit.oa.bo.ToaMailBox;

public class MailUtil {

	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:53:43 AM
	 * @desc: 建立javamail session
	 * @param toaMailBox  ToaMailBox对象
	 * @return Store
	 */
	public Store getMailSession(ToaMailBox toaMailBox){
		String username=toaMailBox.getPop3Account();
		String password=toaMailBox.getPop3Pwd();
		String port=toaMailBox.getPop3Port();
		String server=toaMailBox.getPop3Server();
		String isSSL=toaMailBox.getPop3Ssl();
		String serverType=toaMailBox.getGetServerType();
		if("1".equals(serverType)){
			serverType="pop3";
		}else if("2".equals(serverType)){
			serverType="imap";
		}
		Properties prop;
		if("1".equals(isSSL)){
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			prop = System.getProperties();//System.getProperties();//new Properties();
			prop.setProperty("mail."+serverType+".socketFactory.class", SSL_FACTORY);
			prop.setProperty("mail."+serverType+".socketFactory.fallback", "true");
			prop.setProperty("mail."+serverType+".port", port);
			prop.setProperty("mail."+serverType+".socketFactory.port", port);
		}else{
			Security.removeProvider(new com.sun.net.ssl.internal.ssl.Provider().getName());
			prop = System.getProperties();
		}
		int portnum=0;
		
		try{
			portnum=Integer.parseInt(port);
		}catch(Exception e){
			e.printStackTrace();
		}
		URLName urln = new URLName(serverType,server,portnum,null,username, password);
		Session session=null;
		try{
			session=Session.getInstance(prop, null);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		if(session!=null){
			try {
				return session.getStore(urln);
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}else{
			return null;
		}
	}
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:54:25 AM
	 * @desc: 将二进制流转化为字符串
	 * @param is  传入的二进制流
	 * @return String
	 * @throws IOException

	 */
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:55:29 AM
	 * @desc: 
	 * @param box
	 * @param store
	 * void
	 */
	public synchronized static void release(Folder box,Store store){
		try{

			if(box.isOpen()){
				box.close(true);
			}
			if(store.isConnected()){
				store.close();
			}
		}catch(Exception e){
			//这里可能会存在文件夹关闭异常IMAP协议;
			if(box.isOpen()){
				try {
					box.close(true);
				} catch (MessagingException d) {
					// TODO Auto-generated catch block
					d.printStackTrace();
				}
			}
			if(store.isConnected()){
				try{
					store.close();
				}catch(Exception d){
					d.printStackTrace();
				}
			}
		}finally{			
			box=null;
			store=null;
		}
	}
	
	/**
	 * 
	 * @author：yuhz
	 * @time：Feb 14, 200910:56:03 AM
	 * @desc: 进行解码操作
	 * @param str 所要解码的字符串
	 * @return String
	 * @throws Exception
	 * 
	 */
    public static String deCode(String str) throws Exception{
    	String returnStr;
    	if(str.matches("=\\?[\\w-]+\\?[bBqQ]\\?.*?")){
    		returnStr=MimeUtility.decodeText(str);
    	}else{
	    	try{
	    		returnStr=new String(str.getBytes("ISO8859_1"),"GBK");
	    	}catch(Exception e){
	    		returnStr=null;
	    	}
    	}
    	return returnStr;
    }
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:56:49 AM
     * @desc: 将发送者解码
     * @param str 所要解码的字符串
     * @return String
     * @throws Exception
     * 
     */
    public static String deSenderCode(String str) throws Exception{
    	String returnStr;
    	if(str.matches("=\\?[\\w-]+\\?[bBqQ]\\?.*?")){
    		returnStr=MimeUtility.decodeText(str);
    	}else{
	    	try{
	    		returnStr=new String(str.getBytes("ISO8859_1"),"GBK");
	    	}catch(Exception e){
	    		returnStr=null;
	    	}
    	}
    	if(returnStr.indexOf("\"")==0){
    		String temp=returnStr.substring((returnStr.indexOf("\"")+1),(returnStr.lastIndexOf("\"")));
    		String tempLast=returnStr.substring(returnStr.lastIndexOf("\"")+1,returnStr.length());
    		returnStr=temp+tempLast;
    	}
    	return returnStr;
    }
	
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:57:31 AM
     * @desc: 将接收者的进行解码操作并将接收者中的双引号去掉
     * @param receiver
     * @return String
     * @throws Exception
     */
	public static String getReceiver(String receiver) throws Exception{
		String returnStr="";
		if(receiver.indexOf(",")!=-1){
			String[] rec=receiver.split(",");
			for(int i=0;i<rec.length;i++){
				if(rec[i].indexOf("\"")!=-1){
					String temp=rec[i].substring((rec[i].indexOf("\"")+1),(rec[i].lastIndexOf("\"")));
					String tempLast=rec[i].substring(rec[i].lastIndexOf("\""),rec[i].length());
					returnStr=returnStr+MimeUtility.decodeText(temp)+tempLast;
				}else{
					returnStr=returnStr+rec[i];
				}
				if(i!=rec.length-1){
					returnStr=returnStr+",";
				}
			}
		}else{
			returnStr=receiver;
		}
		//System.out.println(returnStr);
		return returnStr;
	}
    /**
     * 
     * @author：yuhz
     * @time：Feb 14, 200910:58:56 AM
     * @desc: 对所传进的字符数据进行解码
     * @param strs
     * @return String
     */
    public static String deCodeSubject(String [] strs){
    	String subject="";
    	try{
	        for (String str : strs){
	            subject = new String(str.getBytes("ISO8859_1"), "GBK");
	        }
	        subject = MimeUtility.decodeText(subject);//base64解码
	        
    	}catch(Exception e){
    		
    	}
    	return subject;
    }

}
