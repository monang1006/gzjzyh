package com.strongit.oa.webservice.iphone.client.iphoneDocManage;

import java.util.ArrayList;
import java.util.Date;
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

public class iphoneDocManageClient extends TestCase {

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
		url = "http://192.168.2.71:8888/oa/services/iphoneDocManageService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getFileInfo");
		Object[] params = new Object[]{"939800","ff80808140e144100140ed1a28360d70","4028823343747b4a014374b02f520002","size","desc"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	
}
