package com.strongit.oa.webservice.iphone.client.iphonePublicContact;

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

public class iphonePublicContactClient extends TestCase {

	private Service service = null;
	private Call call = null;
	private String url = null;
	private Dom4jUtil dom = new Dom4jUtil();
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://192.168.2.179:8080/OA/services/tjxihaWebService";
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
	public void testSss() throws Exception {
		url = "http://192.168.2.179:8080/OA/services/iphonePublicContactWebService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getPubContInfo");
		Object[] params = new Object[]{"939800","40288233436f591e01436f6ef5420002"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	
}
