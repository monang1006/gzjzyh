/**  
 * @title: pushNotifyWebService.java
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @description: TODO
 * @author  hecj
 * @date Jan 8, 2014 2:27:07 PM
 */

package com.strongit.oa.webservice.iphone.server.pushNotify;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import net.sf.json.JSONObject;

import org.apache.axis.transport.http.HTTPConstants;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;


import com.strongit.oa.bo.ToaBussinessModulePara;
import com.strongit.oa.bo.ToaPushNotify;
import com.strongit.oa.bo.ToaPushNotifyCloseMessage;
import com.strongit.oa.smsplatform.SmsPlatformManager;
import com.strongit.oa.util.Dom4jUtil;
import com.strongit.utils.StringUtil;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.service.ServiceLocator;

/**
 * 推送消息给ios os
 * 
 * @classname: pushNotifyWebService
 * @author hecj
 * @date Jan 8, 2014 2:27:07 PM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @update
 */
public class pushNotifyWebService {
	/**
	 * 变量定义
	 */
	private static String STATUS_SUC = "1";// 返回成功状态
	private static String STATUS_FAIL = "0";// 返回失败状态
	@Autowired
	private WebServiceContext context;
	/**
	 * manager注入
	 */
	// 推送ios消息的manager
	private PushNotifyManager pushManager;
	/**
	 * ios推送相关设置参数
	 */
	public static String certFileRealPath;//证书路径
	public static String certPass;//证书密码

	/**
	 * android推送相关设置参数
	 */
	public static String androidAppId;//应用id
	public static String androidAppKey;//应用主键
	public static String androidMasterSecret;//应用密码
	public static String androidPushUrl;//推送url
	
	public pushNotifyWebService() {
		pushManager = (PushNotifyManager) ServiceLocator.getService("pushNotifyManager");
		try{
			Properties prop;
			/**
			 * 初始ios相关证书参数
			 */
			/*MessageContext ctx = webServiceContext.getMessageContext();
			*/
			org.apache.axis.MessageContext cxt=org.apache.axis.MessageContext.getCurrentContext();
			HttpServletRequest request=null;
			if(cxt==null){
				MessageContext xmlCtx = context.getMessageContext();
				request = (HttpServletRequest) xmlCtx.get(AbstractHTTPDestination.HTTP_REQUEST);
			}else{
				request = (HttpServletRequest) cxt.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
			}
			prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("webserviceaddress.properties"));
			if(certFileRealPath==null){
				certPass= prop.getProperty("ios_pushnotify_cert_password");// p12文件密码。
				String certFileName=prop.getProperty("ios_pushnotify_cert_filename");
				certFileRealPath = request.getRealPath("/")+ "\\"+certFileName;// 前面生成的用于JAVA后台连接APNS服务的*.p12文件位置
			}
			if(androidAppId==null){
				/**
				 * 初始化andriod推送服务相关参数
				 */
				androidAppId=prop.getProperty("android_pushnotify_cert_appid");
				androidAppKey=prop.getProperty("android_pushnotify_cert_appkey");
				androidMasterSecret=prop.getProperty("android_pushnotify_cert_mastersecret");
				androidPushUrl=prop.getProperty("android_pushnotify_url");
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 推送消息
	 * 
	 * @description
	 * 
	 * @author hecj
	 * @date Jan 15, 2014 1:17:07 PM
	 * @param	clientType
	 * 				String 0:ios 1:andriod
	 * @return String
	 * @throws
	 */
	public String doPushNotity(String userId, String deviceToken,String clientType) {
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			if (StringUtil.isEmpty(userId)) {
				throw new ServiceException("参数userId不能为空");
			}
			if (StringUtil.isEmpty(deviceToken)) {
				throw new ServiceException("参数deviceToken不能为空");
			}
			if (StringUtil.isEmpty(clientType)) {
				throw new ServiceException("参数clientType不能为空");
			}
			pushManager.saveTokenWithUserId(deviceToken,userId,clientType);
			ret = dom.createXmlUnreads(STATUS_SUC, null,null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "推送消息发生服务级异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 设置模块关闭或者打开
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 23, 2014 6:33:03 PM
	 * @param   userId 用户id
	 * 			moduleName 模块标示
	 * 			stateFlag 开关状态 1:打开 0：关闭
	 * @return  String
	 * @throws
	 */
	public String setModuleOpen(String userId,String moduleName,String stateFlag){
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			if (StringUtil.isEmpty(userId)) {
				throw new ServiceException("参数userId不能为空");
			}
			if (StringUtil.isEmpty(moduleName)) {
				throw new ServiceException("参数moduleName不能为空");
			}
			if (StringUtil.isEmpty(stateFlag)) {
				throw new ServiceException("参数stateFlag不能为空");
			}
			pushManager.saveModuleClose(userId,moduleName,stateFlag);
			ret = dom.createXmlUnreads(STATUS_SUC, null,null);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "推送消息发生服务级异常:"+ex, null, null);
		}
		return ret;
	}
	/**
	 * 获取当前用户的模块所有关闭的推送模块
	 * 因为如果客户没有推送，那么数据库就没有任何记录，所以只需要推送关闭了的模块列表就可以
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 25, 2014 11:06:21 AM
	 * @param   
	 * @return  String
	 * @throws
	 */
	public String getModuleClose(String userId){
		Dom4jUtil dom = new Dom4jUtil();
		String ret = "";
		try {
			if (StringUtil.isEmpty(userId)) {
				throw new ServiceException("参数userId不能为空");
			}
			List<ToaPushNotifyCloseMessage> lst=  pushManager.getModuleClose(userId);
			String content="";
			if(lst!=null&&lst.size()>0){
				for(ToaPushNotifyCloseMessage cl: lst){
					if("0".equals(cl.getStateFlag())){
						content+=cl.getModuleNo()+",";
					}
				}
				if(!"".equals(content)){
					content=content.substring(0,content.length()-1);
				}
			}
			ret = dom.createItemResponseData(STATUS_SUC, null, "string", content);
		} catch (ServiceException ex) {
			ret = dom.createItemResponseData(STATUS_FAIL, "推送消息发生服务级异常:"+ex, null, null);
		}
		return ret;
		
	}

	public WebServiceContext getContext() {
		return context;
	}

	public void setContext(WebServiceContext context) {
		this.context = context;
	}
}
