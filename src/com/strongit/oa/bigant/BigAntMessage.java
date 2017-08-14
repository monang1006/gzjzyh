package com.strongit.oa.bigant;

import java.io.FileInputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/*import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;*/
import com.strongit.oa.bo.ToaBigAntUser;
/**
 * 向大蚂蚁用户消息发送类
 *	@author 李俊勇
 *	@date Feb 2, 2010 4:02:00 PM
 */
public class BigAntMessage {/*
	private static ActiveXComponent msg;
	private static ActiveXComponent session;
	private static ActiveXComponent loginInfo;
	private static String server;
	private static String port;
	static{
		try {
			Properties properties = new Properties();
			URL url = BigAntMessage.class.getClassLoader().getResource("bigant.properties");
			properties.load(new FileInputStream(URLDecoder.decode(url.getFile(), "UTF-8")));
			server = properties.getProperty("bigant.server");
			port = properties.getProperty("bigant.port");
			msg = new ActiveXComponent("AntCom.AntMsg");
			session = new ActiveXComponent("AntCom.AntSyncSession"); 
			loginInfo = new ActiveXComponent("AntCom.AntLoginInfo");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	*//**
	 * 向大蚂蚁用户发送消息
	 *	@author 李俊勇
	 *	@date 2010 4:00:45 PM
	 * 	@param title    消息标题
	 * 	@param content  消息内容
	 * 	@param user		消息发送者
	 * 	@param Receiver  消息接收者
	 *//*
	public  void sendMessage(String title,String content,ToaBigAntUser user,String Receiver){
		Dispatch message = msg.getObject();
		Dispatch.put(message,"Subject",title); 
		Dispatch.put(message,"Content",content); 
		Dispatch.call(message,"AddReceiver", Receiver,""); 
		Dispatch.put(message,"ContentType","Text/Text"); 
		Dispatch info = loginInfo.getObject();
		Dispatch.put(info,"Server",server); 
		Dispatch.put(info,"ServerPort",port); 
	    Dispatch.put(info,"LoginName",user.getCol_LoginName()); 
		Dispatch.put(info,"PassWord",user.getCol_PWord()); 
		Dispatch ses = session.getObject(); 
		Dispatch.call(ses, "Login",info); 
		Dispatch.call(ses, "SendMsg",message,0);
	}
*/}
