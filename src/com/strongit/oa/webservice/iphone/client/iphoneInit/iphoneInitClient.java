package com.strongit.oa.webservice.iphone.client.iphoneInit;

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

public class iphoneInitClient extends TestCase {

	private Service service = null;
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://localhost/OA5.0/services/iphoneInitService";
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
	public void testGetIphoneBgUpdate() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneInitService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getIphoneBgUpdate");
		Object[] params = new Object[]{};//938022,
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetIphonebgPic() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneInitService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getIphonebgPic");
		Object[] params = new Object[]{};//938022,
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}	
	
	@Test
	public void testGetIphoneModularPics() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneInitService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getIphoneModularPics");
		Object[] params = new Object[]{};
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
