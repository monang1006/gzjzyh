package com.strongit.oa.webservice.client.oaMessage;

import java.util.Date;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.junit.Test;

import com.strongit.oa.util.Dom4jUtil;

import junit.framework.TestCase;

public class OaMessageClient extends TestCase {
	private Service service = null;	
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();

	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://192.168.2.122:8080/StrongOA/services/oaMessageService";
			call.setTargetEndpointAddress(url);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void tearDown(){
		service = null;
		call = null;
	}
	
	@Test
	public void testGetMsgReceived() throws Exception {   
		url = "http://localhost:8080/OA5.0/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getMsgReceived");
		Object[] params = new Object[]{"ff80808126498f7701267e3f9f8d4acd",null,null,null,"15","1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetMessageInfo() throws Exception {
		url = "http://localhost:8080/OA5.0/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getMessageInfo");
		Object[] params = new Object[]{"402881f036a422e90136a44c56a800a8"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	@Test
	public void testLoadMessageAttachment() throws Exception {
		url = "http://192.168.2.122:8080/StrongOA/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("loadMessageAttachment");
		Object[] params = new Object[]{"402882fa35dbfeb30135dc0527370003"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	@Test
	public void testGetOrgInfo() throws Exception {
		url = "http://192.168.2.122:8080/StrongOA/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getOrgInfo");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F","001"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetSystemAddressUsers() throws Exception {
		url = "http://192.168.2.122:8080/StrongOA/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getSystemAddressUsers");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F","402882d824f0e70e0124f0f530b5021b"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testQuickSend() throws Exception {
		url = "http://localhost/oa/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("QuickSend");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F",null,"6006800BBDF034DFE040007F01001D9F","申仪玲转发邮件2","中华人民共和国万岁"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
		testGetMsgSended();
	}
	
	@Test
	public void testGetQuickReply() throws Exception {
		url = "http://192.168.2.122:8080/StrongOA/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getQuickReply");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F","402882353585f3e1013585f50ffe0001","6006800BBDF034DFE040007F01001D9F","y适当放松的 ","yyyyy斯蒂芬森y"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}

	@Test
	public void testGetParentOrgInfo() throws Exception {
		url = "http://192.168.2.85:1314/oa/services/oaMessageService";
		//url = "http://localhost/oa/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getParentOrgInfo");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F","0","001001001"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetMsgSended() throws Exception {   
		url = "http://localhost/oa/services/oaMessageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getMsgSended");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F",null,null,null,"15","1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	

}
