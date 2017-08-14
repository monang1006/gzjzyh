package com.strongit.oa.webservice.client.finance.guideline;

import java.io.File;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import com.strongit.oa.webservice.util.WebServiceAddressUtil;

/**
 * 指标系统的客户端实现
 * @author 邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date 2009-7-14 上午09:57:46
 * @version      2.0.2.3
 * @classpath com.strongit.oa.webservice.client.finance.guideline.Client
 * @comment
 */
public class Client {

	/**
	 * 收发文流转到指标系统的客户端实现
	 * @author:邓志城
	 * @date:2009-7-14 上午09:42:31
	 * @param String sessionId 当前用户登陆的sessionId
	 * @param String data XML格式的数据
	 */
	public static void archiveGuideLine(String sessionId,String data){
		try{
			String url = WebServiceAddressUtil.getFinanceWebServiceAddress();
			String packetId = "financeOA.QuotasAction";//包ID
			String bizId = "B001";//业务ID
			Service service = new Service();
			Call call = (Call)service.createCall();
			call.setOperationName("execute");
			call.setTargetEndpointAddress(url);
			Object[] params = new Object[]{sessionId,packetId,bizId,data};
			String ret = (String)call.invoke(params);
			System.out.println(ret);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testArchiveGuideLine()throws Exception{
		String sessionId = "testSessionId";
		SAXReader reader = new SAXReader();
		reader.setEncoding("UTF-8");
		Document doc = reader.read(new File("c:\\packet.xml"));
		String data = doc.asXML();
		System.out.println(data);
		archiveGuideLine(sessionId, data);
	}
	
}
