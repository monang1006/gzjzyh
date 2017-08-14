package com.strongit.oa.webservice.client.bigant;



import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.webservice.util.WebServiceAddressUtil;

/**
 * 大蚂蚁客户端调用实现.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-6-24 下午03:35:03
 * @version  2.0.2.3
 * @classpath com.strongit.oa.webservice.client.bigant.BigAntClient
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class BigAntClient {

	static Logger logger = LoggerFactory.getLogger(BigAntClient.class);
	
	private static Service service = null;
	private static Call call = null;
	private static String url = null;
	static{
		service = new Service();
		try {
			url = WebServiceAddressUtil.getBigAntWebServiceAddress();
			call = (Call)service.createCall();
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	/**
	 * 发送大蚂蚁消息提醒
	 * @author:邓志城
	 * @date:2010-6-24 上午11:02:47
	 * @param title		标题
	 * @param message	消息
	 * @param sender	发送者
	 * @param receiver	接收者
	 * @param objects	扩展参数，第一个参数用于发送者密码,第二个参数为标题
	 * @return			操作结果
	 */
	public static String sendMessage(String title, String message, String sender,
			String receiver, Task task) {
		call.setTargetEndpointAddress(url);
		call.setOperationName("sendMessage");
		QName qnameAttachMent = new QName("http://xml.apache.org/axis/wsdd/providers/java","ns1:Task");
		call.registerTypeMapping(Task.class, 
								 qnameAttachMent, 
								 BeanSerializerFactory.class,
								 BeanDeserializerFactory.class);
		if(task == null) {
			task = new Task();
		}
		title = task.getBusinessName();
		message = message + "\r\n";
		Object[] params = new Object[]{title,message,sender,receiver,task};
		String ret = null;
		try {
			ret = (String)call.invoke(params);
		} catch (Exception e) {
			logger.error(e.toString());
			ret = "-1";
		}
		return ret;
	}
	
	public static void main(String...strings)throws Exception {
		Task task = new Task();
	    task.setPassword("1");
		sendMessage("", "http://127.0.0.1:8080/oa/theme/theme!login.action?t=admin", "admin", "admin", task);
		//sendMessage("", "工作提醒：请查阅《5555》。涂娟5", "chengpeng", "admin2", "maozai520","1");
	}
}
