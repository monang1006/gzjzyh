/**  
* @title: pushServiceTest.java
* @package com.strongit.oa.webservice.iphone.client.pushNotify
* @description: TODO
* @author  hecj
* @date Jan 8, 2014 2:32:03 PM
*/


package com.strongit.oa.webservice.iphone.client.pushNotify;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import javapns.communication.ConnectionToAppleServer;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;

import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.json.JSONException;
import org.junit.Test;

import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.webservice.iphone.server.pushNotify.PushNotifyBean;

import junit.framework.TestCase;


/**
 * @classname: pushServiceTest	
 * @author hecj
 * @date Jan 8, 2014 2:32:03 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.client.pushNotify
 * @update
 */
public class pushNotifyClient extends TestCase{
	private Service service = null;
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();
	public void setUp(){
		//初始化
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://localhost:8090/oa/services/pushNotifyService";
			call.setTargetEndpointAddress(url);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void tearDown(){
		//结束
		service = null;
		call = null;
	}
	
	@Test
	public void testPushMessageToIOS() throws JSONException, RemoteException{
		call.setOperationName("doPushNoity");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F","93af67158a3edcc71765ba80345fa1ef36b224af9bc9915ccea0fdd324654ca4"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	/**
	 * TODO
	 * 
	 * @param args
	 * @throws JSONException 
	 * @throws KeystoreException 
	 * @throws CommunicationException 
	 */
	public static void main(String[] args) throws JSONException, CommunicationException, KeystoreException {
		/**
		 * payLoad不能超过256字节,是json格式
		 */
		PushNotifyBean push=new PushNotifyBean();
		
		/**
		 * 推送内容
		 */
		String alert="未查看邮件5条";
		int count=6;
		String sound="default";
		JSONObject json=new JSONObject();
		json.put("title","今天召开常委会会议第13届12次会议!");
		push.initPayLoad(alert, count, sound,json);
		/**
		 * 设备令牌
		 */
		String deviceToken="93af67158a3edcc71765ba80345fa1ef36b224af9bc9915ccea0fdd324654ca4";
		push.initDeviceToken(deviceToken);
		/**
		 * 初始证书
		 */
		String certificatePath = "c:\\iPhoneOA2.0.p12";//前面生成的用于JAVA后台连接APNS服务的*.p12文件位置
	    String certificatePass = "strong88186889";//p12文件密码。
	    boolean isProductServer=false;//false为测试服务器 true产品服务器
		push.initAPNSConnect(certificatePath, certificatePass, isProductServer);
		
		push.doPush();
		
	}
}
