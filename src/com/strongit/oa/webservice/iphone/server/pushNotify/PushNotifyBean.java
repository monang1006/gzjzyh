/**  
* @title: PushNotifyBean.java
* @package com.strongit.oa.webservice.util
* @description: TODO
* @author  hecj
* @date Jan 8, 2014 4:43:17 PM
*/


package com.strongit.oa.webservice.iphone.server.pushNotify;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.json.JSONException;

import com.strongit.utils.StringUtil;

import javapns.communication.ConnectionToAppleServer;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

/**
 * java往ios推送消息
 * @classname: PushNotifyBean	
 * @author hecj
 * @date Jan 8, 2014 4:43:17 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.util
 * @update
 */
public class PushNotifyBean {
	/**
	 * 普通变量类型
	 * ----begin-----------
	 */
	private String alert;
	private int badge;
	private String sound;
	//自定义推送数据
	private JSONObject data; 
	/**
	 * 静态变量
	 */
	private static final String TEST_APNS_HOST="gateway.sandbox.push.apple.com";
	private static final String APNS_HOST="gateway.push.apple.com";
	private static final int APNS_PORT=2195;
	
	/**
	 * 对象变量
	 */
	private AppleNotificationServer connect;
	private Device device;
	/**
	 * 根据传入的参数初始化消息服务器
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 8, 2014 5:14:16 PM
	 * @param   isProductServer
	 * 				boolean 是否是推送的产品服务器 true：产品服务器 false：测试服务器
	 * 			certificatePath
	 * 				证书的路径 例如:c:\\abc.p12
	 * 			certificatePass
	 * 				证书的访问密码
	 * @return  void
	 * @throws
	 */
	public AppleNotificationServer initAPNSConnect(String certificatePath,String certificatePass, boolean isProductServer){
		if(connect==null){
			String host=null;
			if(isProductServer){
				host=APNS_HOST;
			}else{
				host=TEST_APNS_HOST;
			}
			try {
				connect=new AppleNotificationServerBasicImpl(certificatePath, certificatePass, ConnectionToAppleServer.KEYSTORE_TYPE_PKCS12, host, APNS_PORT);
			} catch (KeystoreException e) {
				// TODO Auto-generated catch block
				connect=null;
			}
		}
		return connect;
	}
	/**
	 * 设备令牌初始化
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 8, 2014 5:36:05 PM
	 * @param   deviceToken
	 * 				String 设备令牌 1、令牌与应用无关，与设备相关，每一个设备一个令牌 2、测试服务器与正式产品推送服务器的令牌不一致
	 * @return  void
	 * @throws
	 */
	public void initDeviceToken(String deviceToken){
		if(device==null){
			device=new BasicDevice();
			device.setToken(deviceToken);
		}
	}
	/**
	 * 初始化推送数据
	 * payLoad最大256字节
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 8, 2014 4:48:43 PM
	 * @param   alert
	 * 				String 推送的消息内容
	 * 			count
	 * 				int iphone应用图标上小红圈上的数值
	 * 			sound
	 * 				String 提示音
	 * @return  void
	 * @throws
	 */
	public void initPayLoad(String alert,int count,String sound,JSONObject data){
		this.alert=alert;
		this.badge=count;
		this.sound=sound;
		this.data=data;
	}
	/**
	 * 开始推送
	 * @description
	 *
	 * @author  hecj
	 * @date    Jan 8, 2014 5:08:31 PM
	 * @param   
	 * @return  String
	 * 			0:成功 1:证书验证失败 2:推送数据解析报错 3：初始化连接apns失败
	 * @throws  
	 * @throws
	 */
	public String doPush(){
		String rtn="";
		if(connect!=null){
			try {
				if(device!=null){
					if(StringUtil.isNotEmpty(alert)&&StringUtil.isNotEmpty(badge)){
						
						JSONObject json=new JSONObject();
						/**
						 * 系统消息
						 */
						JSONObject apsDict=new JSONObject();
						apsDict.put("alert", alert);
						apsDict.put("badge", badge);
						if(StringUtil.isNotEmpty(sound)){
							apsDict.put("sound", sound);
						}
						json.put("aps",apsDict);
						/**
						 * 自定义消息
						 */
						if(StringUtil.isNotEmpty(data)){
							json.put("data", data);
						}
						List<PushedNotification> notifications = new ArrayList<PushedNotification>();
						PushNotificationPayload payload=PushNotificationPayload.fromJSON(json.toString());
						PushNotificationManager manager=new PushNotificationManager();
						manager.initializeConnection(connect);
						
						PushedNotification notityed=manager.sendNotification(device, payload);
						
						notifications.add(notityed);
						
						List<PushedNotification> successfulNotifications = PushedNotification.findSuccessfulNotifications(notifications);

						if(successfulNotifications!=null&&successfulNotifications.size()>0){
							rtn="0";
						}

						//停止连接APNS
				        manager.stopConnection();
					}
				}else{
					rtn="1";
				}
			} catch (JSONException e) {
				rtn="2";
			}catch(CommunicationException e){
				rtn="3";
			}catch (KeystoreException e){
				rtn="1";
			}
		}else{
			rtn="3";
		}
		return rtn;
	}
}
