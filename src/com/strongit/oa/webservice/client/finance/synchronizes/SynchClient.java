package com.strongit.oa.webservice.client.finance.synchronizes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.strongit.di.exception.SystemException;
import com.strongit.di.packet.Packet;
import com.strongit.di.util.XMLParser;
import com.strongit.oa.util.PropertiesUtil;
import com.strongit.oa.webservice.util.WebServiceAddressUtil;

/**
 * 组织机构用户同步客户端实现
 * 
 * @author Jianggb
 * @company Strongit Ltd. (C) copyright
 * @date 2009-7-14 上午09:57:46
 * @version 2.0.2.3
 * @classpath com.strongit.oa.webservice.client.finance.guideline.Client
 * @comment
 */
public class SynchClient {

	/**
	 * 获取组织机构同步的客户端实现
	 * 
	 * @author:Jianggb
	 * @date:2009-7-15 上午09:42:31
	 * @param String
	 *            sessionId 当前用户登陆的sessionId
	 * @param String
	 *            data XML格式的数据
	 */
	public static String  getOrgangesDate(String sessionId,String data){
		try{
			String url = WebServiceAddressUtil.getFinanceWebServiceAddress();
			String packetId = "financeOA.CommonAction";// 包ID
			String bizId = "B004";// 业务ID
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setOperationName("execute");
			call.setTargetEndpointAddress(url);
			Object[] params = new Object[]{sessionId,packetId,bizId,data};
			String ret = (String)call.invoke(params);
			return ret;
		// System.out.println(ret);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取用户同步的客户端实现
	 * 
	 * @author:Jianggb
	 * @date:2009-7-15 上午09:42:31
	 * @param String
	 *            sessionId 当前用户登陆的sessionId
	 * @param String
	 *            data XML格式的数据
	 */
	public static String  getUsersDate(String sessionId,String data){
		try{
			String url = WebServiceAddressUtil.getFinanceWebServiceAddress();
			String packetId = "financeOA.CommonAction";// 包ID
			String bizId = "B005";// 业务ID
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setOperationName("execute");
			call.setTargetEndpointAddress(url);
			Object[] params = new Object[]{sessionId,packetId,bizId,data};
			String ret = (String)call.invoke(params);
			return ret;
	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 根据用户ID获取财政用户密码
	 *@author 蒋国斌
	 *@date 2009-7-24 下午03:33:56 
	 * @param sessionId
	 * @param data
	 * @return
	 */
	public static String  getUserPassword(String sessionId,String data){
		try{
			String url = WebServiceAddressUtil.getFinanceWebServiceAddress();
			String packetId = "financeOA.CommonAction";// 包ID
			String bizId = "B003";// 业务ID
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setOperationName("execute");
			call.setTargetEndpointAddress(url);
			Object[] params = new Object[]{sessionId,packetId,bizId,data};
			String ret = (String)call.invoke(params);
			return ret;
		// System.out.println(ret);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据给定的机构编码字段生成XML字符
	 * 
	 * @author:Jianggb
	 * @date:2009-7-18 上午11:04:53
	 * @param branchId
	 *            业务处室编码
	 * @param createIp
	 *            客户端IP地址
	 * @return XML字符
	 * @throws SystemException
	 */
	public static String getPacketbyOrg(String branchId,
								   String createIp)throws SystemException{
		// 创建包对象
		Packet packet = new Packet();
		// 定义返回值
		String ret = "";
		// 格式化时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		String strNow = sdf.format(now);
		// 设置包头【版本号、客户端IP、创建时间】
		packet.setHead("VERSION", "1.0.0");
		packet.setHead("CREATEIP", createIp);
		packet.setHead("CREATETIME", strNow);
		
		packet.setVar("enterId", branchId);
		
		// 将数据包生成XML字符返回
		ret = XMLParser.generate(packet);
	//	XMLParser.generateToFile(packet, "c:/packet.xml");
		
		return ret;
	}
	/**
	 * 根据给定的userId字段生成XML字符
	 *@author 蒋国斌
	 *@date 2009-7-24 下午03:36:28 
	 * @param branchId
	 * @param createIp
	 * @return
	 * @throws SystemException
	 */
	public static String getPacketbyUser(String userId,
			   String createIp)throws SystemException{
// 创建包对象
Packet packet = new Packet();
// 定义返回值
String ret = "";
// 格式化时间
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// 获取当前时间
Calendar calendar = Calendar.getInstance();
Date now = calendar.getTime();
String strNow = sdf.format(now);
// 设置包头【版本号、客户端IP、创建时间】
packet.setHead("VERSION", "1.0.0");
packet.setHead("CREATEIP", createIp);
packet.setHead("CREATETIME", strNow);

packet.setVar("userId", userId);

// 将数据包生成XML字符返回
ret = XMLParser.generate(packet);
//	XMLParser.generateToFile(packet, "c:/packet.xml");
return ret;
}


	 public static String convUpperLower(String strIn)   
	  {   
	  String strOut =new String();   
	  int len=strIn.length();   
	  int i= 0;   
	  char ch;   
	                    
	  while   (i<len)   
	    {   
	  ch = strIn.charAt(i);   
	                            
	  if ( ch >=   'A'   &&   ch   <=   'Z'   )   
	  ch   =   (char)(ch   -   'A'   +   'a');   
	  else   if   (   ch   >=   'a'   &&   ch   <=   'z')   
	  ch   =   (char)(ch   -   'a'   +   'A');   
	    
	  strOut   +=   ch;   
	  i++;   
	    }                                       
	  return   strOut;       
	  } 
	 
	 public static String convLowerUpper(String strIn)   
	  {   
	  String strOut =new String();   
	  int len=strIn.length();   
	  int i= 0;   
	  char ch;   
	                    
	  while   (i<len)   
	    {   
	  ch = strIn.charAt(i);   
	                            
	  if ( ch >=   'a'   &&   ch   <=   'z'   )   
	  ch   =   (char)(ch   -   'a'   +   'A');   
	  else   if   (   ch   >=   'A'   &&   ch   <=   'Z')   
	  ch   =   (char)(ch   -   'A'   +   'a');   
	    
	  strOut   +=   ch;   
	  i++;   
	    }                                       
	  return   strOut;       
	  }   

	public static void p(String str){
		System.out.println(str);
	}
	public static void main(String[] args){
		try{
		String data=getPacketbyUser("005001","192.168.2.185");
		// String ret =getPacketbyUser("sessionId",null);
		 System.out.print(data);
			// p(getPacketbyOrg("202001","192.168.2.185"));AsD321
			//System.out.print(convUpperLower("3FC362D1016B412A4EEAB7BA204EF373"));
//  {userName=黄平, userType=4, userFax=null, userId=003011, userZip=null, loginType=null, isCompanyAdmin=0, userPwd=C4CA4238A0B923820DCC509A6F75849B, userMobileTel=null, userAddress=null, userIdCard=null, userEmail=null, userSex=1, userTel=null, resume=null, userPostTitle=null, enable=1}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
