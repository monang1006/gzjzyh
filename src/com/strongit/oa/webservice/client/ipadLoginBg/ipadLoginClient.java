package com.strongit.oa.webservice.client.ipadLoginBg;

import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;
import org.dom4j.Element;
import org.junit.Test;

import com.strongit.oa.util.Dom4jUtil;

public class ipadLoginClient extends TestCase {

	private Service service = null;
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://localhost:8080/OA5.0/services/ipadLoginService";
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
	public void testGetIpadBgImage() throws Exception  {
			
 		url = "http://localhost:8080/OA5.0/services/ipadLoginService ";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getIpadBgImage");
		Object[] params = new Object[]{};//938022,
		
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	@Test
	public void testGetIpadBgUpdate() throws Exception  {
		
		url = "http://localhost:8080/OA5.0/services/ipadLoginService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getIpadBgUpdate");
		Object[] params = new Object[]{};//938022,
		
		//String taskId,String userId,String suggestion,String transitionName ,String taskActors
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	

	@Test
	public void testGetUserUnreads() throws Exception {
		url = "http://localhost:8080/OA5.0/services/ipadLoginService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getUserUnreads");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
}
