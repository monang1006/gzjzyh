package com.strongit.oa.webservice.iphone.client.iphoneArticles;

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

public class iphoneArticlesClient extends TestCase {

	private Service service = null;
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://localhost/oa/services/iphoneArticlesService";
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
	public void testGetMyColumn() throws Exception  {		
		url = "http://localhost/oa/services/iphoneArticlesService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getMyColumn");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F", "8", "1"};//ff8080812fddec2c012fde282cfc005e
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}	

	@Test
	public void testGetColumnArticles() throws Exception  {		
		url = "http://localhost/oa/services/iphoneArticlesService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getColumnArticles");
		Object[] params = new Object[]{"", "8", "1"};//ff8080812fddec2c012fde282cfc005e
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}	

}
