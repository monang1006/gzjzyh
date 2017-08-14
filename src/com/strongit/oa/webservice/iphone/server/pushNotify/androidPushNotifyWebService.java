/**  
 * @title: pushNotifyWebService.java
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @description: TODO
 * @author  hecj
 * @date Jan 8, 2014 2:27:07 PM
 */

package com.strongit.oa.webservice.iphone.server.pushNotify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sun.misc.BASE64Encoder;

import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaPushNotify;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.oa.util.PathUtil;
import com.strongit.uums.webservice.AuthenticationHandler;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.service.ServiceLocator;

/**
 * 推送消息给android
 */
public class androidPushNotifyWebService {
	private static  String APPID = "Gm10p6JrPU89aX7aeA1Ws6";
	private static  String APPKEY = "psbfB9L1248buQsdelXgXA";
	private static final String MASTERSECRET = "SRDK9ydmCh9SJp5eAXP9a6";
	private static  String CLIENTID = "8f5331c853dcd9b2ba4a8482df0c6e35";
	private static final String API = "http://sdk.open.api.igexin.com/apiex.htm"; 	//OpenService接口地址
	private static String STATUS_SUC  = "1";//返回成功状态
	private static String STATUS_FAIL = "0";//返回失败状态
	private PushNotifyManager pushManager;
	// 短信通道的模块对应manager
	private SmsPlatformManager platformManager;

	public androidPushNotifyWebService() {
		pushManager = (PushNotifyManager) ServiceLocator
				.getService("pushNotifyManager");
		platformManager= (SmsPlatformManager)ServiceLocator
		.getService("smsPlatformManager");
	}
	/**
	 * 推送android 设备信息
	 * author  taoji
	 * @param sessionId
	 * @param userId
	 * @param clientid
	 * @param appid
	 * @param appkey
	 * @param mastersecret
	 * @return
	 * @date 2014-3-6 下午06:53:45
	 */
	public String doPushNoity(String sessionId,String userId,
			String clientid, String appid,String appkey,String mastersecret) {
			Dom4jUtil dom = new Dom4jUtil();
			//验证用户
			if(AuthenticationHandler.getUserInfo(sessionId)==null){
				return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
			}
			if(clientid!=null&&!"".equals(clientid)){
				CLIENTID= clientid;
			}
			if(appid!=null&&!"".equals(appid)){
				APPID = appid;
			}
			if(appkey!=null&&!"".equals(appkey)){
				APPKEY = appkey;
			}
			String rets = null;
			try{
				List<ToaPushNotify> lst = pushManager
				.findNotifyByUserIdAndModuleNo(userId, null,
						"  order by t.moduleNo");
				/**
				 * 短信通道对应的模块表
				 */
				HashMap<String, String> nameMap = new HashMap<String, String>();
				List<ToaBussinessModulePara> moduleList = platformManager
						.getAllObj();
				if (moduleList != null && moduleList.size() > 0) {
					Iterator<ToaBussinessModulePara> it = moduleList.iterator();
					ToaBussinessModulePara para = null;
					while (it.hasNext()) {
						para = it.next();
						nameMap.put(para.getBussinessModuleCode(), para
								.getBussinessModuleName());
					}
				}
				String moduleName = null;
//				if(lst!=null&&lst.size()>0){
//					for (ToaPushNotify notify : lst) {
//						moduleName = nameMap.get(notify.getModuleNo());
//						if (notify != null && moduleName != null) {
//							//没有更新不推送
//							if(notify.getModuleNo()!=null&&notify.getMessageCount()==0){
//								continue;
//							}
//							/**
//							 * 推送内容
//							 */
//							String alert = "您有" + notify.getMessageCount() + "条"
//							+ nameMap.get(notify.getModuleNo()) + "未查看!";
//							IIGtPush push = new IGtPush(API, APPKEY, MASTERSECRET);
//							ListMessage message = new ListMessage();
//							//通知模版：支持TransmissionTemplate、LinkTemplate、NotificationTemplate，此处以TransmissionTemplate为例
//							NotificationTemplate template = new NotificationTemplate();
//							template.setAppId(APPID); 
//							template.setAppkey(APPKEY); 
//						
//							template.setTitle(nameMap.get(notify.getModuleNo())); 
//							template.setText(alert);
//							
//							template.setTransmissionType(2); 
//							/**
//							 * i1 待办事宜 	i3 内部邮件 
//							 * i2 公文管理 	i4 领导日程 
//							 */
//							template.setTransmissionContent(notify.getModuleNo());
//							message.setData(template);
//							// message.setOffline(true); //用户当前不在线时，是否离线存储,可选
//							// message.setOfflineExpireTime(72 * 3600 * 1000); //离线有效时间，单位为毫秒，可选
//							List<Target> targets = new ArrayList<Target>();
//							Target target1 = new Target();
//							target1.setAppId(APPID);
//							target1.setClientId(CLIENTID);
//							targets.add(target1);
//							
//							String contentId = push.getContentId(message);
//							//单推 
//							IPushResult ret = push.pushMessageToList(contentId, targets);
//							System.out.println(ret.getResponse().toString());
//							rets = dom.createXmlUnreads(STATUS_SUC, null,null);
//							
//							//清空数据库内容
//							pushManager.updateToaPushNotify(userId);
//						}
//					}
//				}else{
//					rets = dom.createXmlUnreads(STATUS_FAIL, null,null);
//				}
		} catch (ServiceException ex) {
			rets = dom.createItemResponseData(STATUS_FAIL, "推送消息发生服务级异常:"+ex, null, null);
		}
		return rets;
	}
	/**
	 * 检查版本号是否最新  不是! 返回apk 数据
	 * author  taoji
	 * @param sessionId
	 * @param userId
	 * @param versionId
	 * @return
	 * <?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>1</status>
			<fail-reason/>
			<data><item type="string" value="BASE64Encoder的strongOA.apk"/>
			</data>
		</service-response>
	数据为空
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>2</status>
		<fail-reason/>
		<data/>
	</service-response>
	服务调用失败时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>0</status>
		<fail-reason>异常描述</fail-reason>
		<data />
	</service-response>
	 */
	public String checkVersion(String sessionId , String userId, String versionId){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		//验证用户
		if(AuthenticationHandler.getUserInfo(sessionId)==null){
			return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
		}
		String Path = "";
		String filePath = "";
		Path = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"strongOA.apk";
		filePath = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"strongOA.txt";
		String temp = "";
		String encoding="GBK";
		try {
			String txtPath = PathUtil.getRootPath()+ filePath;	
	        File file=new File(txtPath);
	        if(file.isFile() && file.exists()){ //判断文件是否存在
	            InputStreamReader read = new InputStreamReader(
	            		new FileInputStream(file),encoding);//考虑到编码格式
	            BufferedReader bufferedReader = new BufferedReader(read);
	            String lineTxt = null;
	            while((lineTxt = bufferedReader.readLine()) != null){
	                //System.out.println(lineTxt);
	            	temp = temp+lineTxt;
	            }
	            read.close();
			}else{
			    System.out.println("找不到指定的文件");
			}
	        if(!temp.equals(versionId)){
	        	String picPath = PathUtil.getRootPath()+ Path;	
				FileInputStream fis = new FileInputStream(picPath);
				if (fis != null) {
	 				BASE64Encoder encoder = new BASE64Encoder();
	 				String content = encoder.encode(FileUtil.inputstream2ByteArray(fis));
					ret = dom.createItemResponseData(STATUS_SUC, null, "string",
							content);
				}
	        }else{
	        	ret = dom.createItemResponseData("2", null, "string","");
	        }
		} catch (Exception e) {
			// TODO: handle exception
//			ret = dom.createItemResponseData(STATUS_FAIL, "获取iphone界面图片数据级异常:"+e.getMessage(), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "获取界面图片数据级异常:"+e.getMessage(), null, null);
		}
		return ret;
	}
	/**
	 * 检查版本号是否最新
	 * author  taoji
	 * @param sessionId
	 * @param userId
	 * @param versionId
	 * @param mark   android or ios
	 * @return
	 * <?xml version="1.0" encoding="UTF-8"?>
		<service-response>
			<status>1</status>
			<fail-reason/>
			<data>	
				ios  返回版本号
			</data>
		</service-response>
	数据为空
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>2</status>
		<fail-reason/>
		<data/>
	</service-response>
	服务调用失败时返回数据格式如下：
	<?xml version="1.0" encoding="UTF-8"?>
	<service-response>
		<status>0</status>
		<fail-reason>异常描述</fail-reason>
		<data />
	</service-response>
	 */
	public String checkV(String sessionId , String userId, String versionId,String mark ){
		String ret = null;
		Dom4jUtil dom = new Dom4jUtil();
		//验证用户
		if(AuthenticationHandler.getUserInfo(sessionId)==null){
			return dom.createItemResponseData(STATUS_FAIL, "用户未通过验证！", null, null);
		}
		String filePath = "";
		if("android".equals(mark)){
			filePath = "oa" +File.separator + "image" + File.separator + "android" + File.separator +"strongOA.txt";
		}else{
			filePath = "oa" +File.separator + "image" + File.separator + "iphone" + File.separator +"strongOA.txt";
		}
		String temp = "";
		String encoding="GBK";
		try {
			String txtPath = PathUtil.getRootPath()+ filePath;	
			File file=new File(txtPath);
			if(file.isFile() && file.exists()){ //判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);//考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null){
					//System.out.println(lineTxt);
					temp = temp+lineTxt;
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件");
			}
			if(!temp.equals(versionId)){
				if("android".equals(mark)){
					ret = dom.createItemResponseData(STATUS_SUC, null, "string",
							"");
				}else{
					ret = dom.createItemResponseData(STATUS_SUC, null, "string",
							temp);
				}
			}else{
				ret = dom.createItemResponseData("2", null, "string","");
			}
		} catch (Exception e) {
			// TODO: handle exception
//			ret = dom.createItemResponseData(STATUS_FAIL, "获取iphone界面图片数据级异常:"+e.getMessage(), null, null);
			ret = dom.createItemResponseData(STATUS_FAIL, "获取界面图片数据级异常:"+e.getMessage(), null, null);
		}
		return ret;
	}
}
