/**  
 * 
 * 推送的接口
 * 
 * 如果应用更换了或者ios账号更换了，重新生成了证书或者新的证书密码等，都需要更新，否则会影响发送,android也一样
 * 
* @title: IPushNotify.java
* @package com.strongit.oa.webservice.iphone.server.pushNotify
* @description: TODO
* @author  hecj
* @date Mar 25, 2014 12:34:09 PM
*/


package com.strongit.oa.webservice.iphone.server.pushNotify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.Device;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import net.sf.json.JSONObject;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaPushNotify;
import com.strongit.utils.StringUtil;
import com.strongmvc.exception.ServiceException;

/**
 * @classname: IPushNotify	
 * @author hecj
 * @date Mar 25, 2014 12:34:09 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @update
 */
@Service
public class PushNotify {
	@Autowired
	private PushNotifyManager pushNotifyManager;
	/**
	 * 推送
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 25, 2014 12:36:08 PM
	 * 			clientType
	 * 				客户端类型 0:ios 1:android
	 * @param   moduleNo
	 * 				模块标示 例如:i1,i2
	 * @return  void
	 * @throws JSONException 
	 * @throws InvalidDeviceTokenFormatException 
	 * @throws KeystoreException 
	 * @throws CommunicationException 
	 * @throws
	 */
	public String pushIt(String moduleNo,String clientType) throws CommunicationException, KeystoreException, InvalidDeviceTokenFormatException, JSONException{
		String rtn="";
		if("0".equals(clientType)){
			//ios推送
			rtn=doPushWithIOS(moduleNo);
		}else if("1".equals(clientType)){
			//rtn=doPushWithAndroid(moduleNo);
		}
		return rtn;
	}
	/**
	 * android推送
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 25, 2014 12:45:34 PM
	 * @param   
	 * @return  void
	 * @throws IOException 
	 * @throws
	 */
	public String doPushWithAndroid(String moduleNo){
		String ret = "";
		return ret;
		
		
/**
 * 注释android推送功能(doPushWithAndroid)，推送功能不兼容jdk1.5
 * @modify by luosy 2014-10-24
 */
//		// 能获取到appId就表示没读取配置没有错误
//		if (StringUtil.isNotEmpty(pushNotifyWebService.androidAppId)) {
//			/**
//			 * 根据模块标示获取对应的令牌列表
//			 */
//			List<Object> tokenList = pushNotifyManager.getTokenListEachModule(moduleNo, "1");
//			if (tokenList != null && tokenList.size() > 0) {
//				/**
//				 * 初始化消息设置
//				 */
//				String appId = pushNotifyWebService.androidAppId;
//				String appKey = pushNotifyWebService.androidAppKey;
//				String masterSecret = pushNotifyWebService.androidMasterSecret;
//				IIGtPush push = new IGtPush(pushNotifyWebService.androidPushUrl, appKey, masterSecret);
//				/**
//				 * 初始化通知消息模板 ----begin--------------
//				 */
//				// 通知模版：支持TransmissionTemplate、LinkTemplate、NotificationTemplate，此处以TransmissionTemplate为例
//				NotificationTemplate template = new NotificationTemplate();
//				template.setAppId(appId);
//				template.setAppkey(appKey);
//				// 收到消息客户端的触发行为 2代表客户端自动启动 1点击消息就启动
//				template.setTransmissionType(1);
//				/**
//				 * i1 待办事宜 i3 内部邮件 i2 公文管理 i4 领导日程 i5 通知公告
//				 */
//				template.setTransmissionContent(moduleNo);
//				/**
//				 * 获取模块标示与模块名称的对应map
//				 */
//				HashMap<String, String> nMap = pushNotifyManager
//						.moduleNameMap();
//				/**
//				 * 推送内容
//				 */
//				String alert = "您有新的" + nMap.get(moduleNo) + "未查看!";
//				template.setTitle(nMap.get(moduleNo));
//				template.setText(alert);
//
//				/**
//				 * ----end---------------
//				 */
//				/**
//				 * 初始化消息
//				 */
//				ListMessage message = new ListMessage();
//				message.setData(template);
//				message.setOffline(true); // 用户当前不在线时，是否离线存储,可选
//				message.setOfflineExpireTime(72 * 3600 * 1000); // 离线有效时间，单位为毫秒，可选
//
//				String contentId = push.getContentId(message);
//				/**
//				 * 需要发送的移动设备列表
//				 */
//				List<Target> targets = new ArrayList<Target>();
//				/**
//				 * 获取令牌列表
//				 */
//				Target target = null;
//				for (Object tokenId : tokenList) {
//					target = new Target();
//					target.setAppId(appId);
//					target.setClientId(tokenId.toString());
//					targets.add(target);
//				}
//				// 获取某个模块对应的设备id，群推
//				IPushResult pRet = push.pushMessageToList(contentId, targets);
//				Map rMap= pRet.getResponse();
//				if("ok".equals(rMap.get("result"))){
//					ret="1";
//				}else{
//					ret="-1";
//				}
//			}
//		}
//		return ret;
	}
	/**
	 * ios推送
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 25, 2014 12:44:43 PM
	 * @param   
	 * @return  String
	 * @throws
	 */
	public String doPushWithIOS(String moduleNo) throws CommunicationException, KeystoreException, InvalidDeviceTokenFormatException, JSONException{
		String rtn="";
		//能获取到证书路径就表示没错误
		if (StringUtil.isNotEmpty(pushNotifyWebService.certFileRealPath)) {
			/**
			 * 根据模块标示获取对应的令牌列表
			 */
			List<Object> tokenList = pushNotifyManager.getTokenListEachModule(moduleNo,"0");
			if (tokenList != null && tokenList.size() > 0) {
				/**
				 * 初始化推送服务器
				 */
				PushNotifyBean push = new PushNotifyBean();
				/**
				 * 是否是正式服务器,1是 0测试服务器
				 */
				boolean apnsHost;
				if("1".equals(PushNotifyListener.apnsHost)){
					apnsHost=true;
				}else{
					apnsHost=false;
				}
				AppleNotificationServer connect = push.initAPNSConnect(pushNotifyWebService.certFileRealPath,pushNotifyWebService.certPass, apnsHost);
				PushNotificationManager pushManager = new PushNotificationManager();
				pushManager.initializeConnection(connect);
				
				List<PushedNotification> notifications = new ArrayList<PushedNotification>();
				List<Device> device = new ArrayList<Device>();
				/**
				 * 获取令牌列表
				 */
				for (Object tokenId : tokenList) {
					device.add(new BasicDevice(tokenId.toString()));
				}
				/**
				 * 获取模块标示与模块名称的对应map
				 */
				HashMap<String, String> nMap=pushNotifyManager.moduleNameMap();
				/**
				 * 推送内容
				 */
				JSONObject json = new JSONObject();
				String alert = "您有新的"+ nMap.get(moduleNo) + ",请查收!";
				/**
				 * 系统消息
				 */
				JSONObject apsDict = new JSONObject();
				apsDict.put("alert", alert);
				apsDict.put("badge", -1);
				apsDict.put("sound", "default");
				json.put("aps", apsDict);
				/**
				 * 自定义消息
				 */
				JSONObject data = new JSONObject();
				if (StringUtil.isNotEmpty(data)) {
					json.put("data", data);
				}
				PushNotificationPayload payload = PushNotificationPayload
						.fromJSON(json.toString());
				notifications = pushManager.sendNotifications(
						payload, device);
				List<PushedNotification> successfulNotifications = PushedNotification
						.findSuccessfulNotifications(notifications);
				List<PushedNotification> failureNotifications = PushedNotification
				.findFailedNotifications(notifications);
				if (successfulNotifications != null&& successfulNotifications.size() > 0) {
					rtn = "1";
				}
				if(failureNotifications!=null&&failureNotifications.size()>0){
					rtn="-1";
				}
				// 停止连接APNS
				pushManager.stopConnection();
				//每次推送完一个模块的消息就退出，等下次轮训，ios推送服务器的间隔时间是5分钟
			}
		}
		return rtn;
	}
}
