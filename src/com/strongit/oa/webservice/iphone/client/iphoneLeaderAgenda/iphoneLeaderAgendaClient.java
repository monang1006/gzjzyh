package com.strongit.oa.webservice.iphone.client.iphoneLeaderAgenda;

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

public class iphoneLeaderAgendaClient extends TestCase {

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
		url = "http://192.168.2.179:8080/OA/services/iphoneLeaderAgendaWebService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getCalendarAttachList");
		Object[] params = new Object[]{"939800","4028823343799d74014379b137360002","10","1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	
}
