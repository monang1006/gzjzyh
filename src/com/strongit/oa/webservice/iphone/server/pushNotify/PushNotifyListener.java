/**  
* @title: PushNotifyListener.java
* @package com.strongit.oa.webservice.iphone.server.pushNotify
* @description: TODO
* @author  hecj
* @date Mar 23, 2014 11:30:53 AM
*/


package com.strongit.oa.webservice.iphone.server.pushNotify;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jfree.data.time.TimeTableXYDataset;
import org.json.JSONException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.strongit.oa.bo.ToaPushNotifyToken;
import com.strongit.oa.sms.SmsService;
import com.strongit.oa.sms.SmsEngineListener.ReplyTimer;

/**
 * 
 * ServletContextListener 能够监听servletContext整个生命周期，实际也是监听web应用的生命周期
 * 每个web关联一个servletContext
 * 当servlet容器启动时初始化，当初始化完成或者终止操作后会会触发ServletContextEvent事件，调用完才会执行filter
 * @classname: PushNotifyListener	
 * @author hecj
 * @date Mar 23, 2014 11:30:53 AM
 * @version
 * @company STRONG CO.,LTD.
 * @package com.strongit.oa.webservice.iphone.server.pushNotify
 * @update
 */
public class PushNotifyListener implements ServletContextListener{
	
	private ServletContext context =null;
	private ReplyTimer rt = null;
	private static int rate = 60;//任务触发间隔时间(秒)
	public static String apnsHost="1";
 
	/**
	 * Overriding method
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 23, 2014 11:31:30 AM
	 * @param sce
	 * @return  
	 * @throws
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		context.log("推送服务已关闭:"+new Date());
		rt.stop();
		
	}

	static{
		try{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource("im.properties");
			InputStream is = url.openStream();
			Properties prop = System.getProperties();
			prop.load(is);
			
			String second = prop.getProperty("push.replytime.second");
			apnsHost=prop.getProperty("push.apns.host.formal");
			if (is != null) {
				is.close();
			}
			rate = new Integer(second);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Overriding method
	 * @description
	 *
	 * @author  hecj
	 * @date    Mar 23, 2014 11:31:30 AM
	 * @param sce
	 * @return  
	 * @throws
	 */
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		context=sce.getServletContext();
		rt=new ReplyTimer();
		rt.start();
		context.log("推送服务开启:"+new Date());
	}
	/**
	 * 时钟循环
	 * @classname: ReplyTimer	
	 * @author hecj
	 * @date Mar 23, 2014 12:40:08 PM
	 * @version
	 * @company STRONG CO.,LTD.
	 * @package com.strongit.oa.webservice.iphone.server.pushNotify
	 * @update
	 */
	public class ReplyTimer{
		private final Timer timer=new Timer();
		public void start(){
			try{
				timer.schedule(new ReplyTask(), new Date(),rate*1000);
			}catch (Exception e) {
				context.log("推送的ReplyTimer()出错  at:"+new Date());
				// TODO: handle exception
			}
		}
		public void stop() {
			timer.cancel();
		}
	}
	
	public class ReplyTask extends TimerTask{
		private PushNotifyManager pushNotifyManager;
		/**
		 * Overriding method
		 * @description
		 *
		 * @author  hecj
		 * @date    Mar 23, 2014 12:35:44 PM
		 * @return  
		 * @throws
		 */
		public void run() {
			// TODO Auto-generated method stub

	 		if(pushNotifyManager == null){
				//注入SmsService 接口
	 			pushNotifyManager = (PushNotifyManager)WebApplicationContextUtils.getWebApplicationContext(context).getBean("pushNotifyManager");
			}else{
				List moduleList= pushNotifyManager.getAllModule();
				try{
					String rtn=pushNotifyManager.pushNotify(moduleList);
					if("1".equals(rtn)){
						context.log("推送消息成功 at:"+new Date());
					}else if("-1".equals(rtn)){
						context.log("推送消息失败 at:"+new Date());
					}
				}catch (InvalidDeviceTokenFormatException e) {
					// TODO Auto-generated catch block
					context.log("推送设备令牌格式异常  at:"+new Date());
				} catch (CommunicationException e) {
					// TODO Auto-generated catch block
					context.log("推送通讯异常  at:"+new Date());
				} catch (KeystoreException e) {
					// TODO Auto-generated catch block
					context.log("推送解析证书异常  at:"+new Date());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					context.log("推送解析json出错  at:"+new Date());
				}
			}
		}
		
	}
}
