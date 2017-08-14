package com.strongit.oa.webservice.iphone.client.iphoneAddressBook;

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

public class iphoneAddressBookClient extends TestCase {

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
	public void testGetSubOrgInfo() throws Exception  {		
 		url = "http://192.168.2.85:1314/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getSubOrgInfo");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F", "001001"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetParentOrgInfo() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getParentOrgInfo");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F","0", ""};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
			
	@Test
	public void testGetUserAddressGroup() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getUserAddressGroup");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}

	@Test
	public void testGetUsersByOrgSysCode() throws Exception  {		
 		url = "http://192.168.2.85:1314/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getUsersByOrgSysCode");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F", "001001", "10" ,"1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testGetComUsedAddress() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("getComUsedAddress");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F", "2" , "1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testAddUserToComUsedAddress() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("addUserToComUsedAddress");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F", "6006800BBDF034DFE040007F01001D9F"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testDeleteUserFromComUsedAddress() throws Exception  {		
 		url = "http://localhost/oa/services/iphoneAddressBookService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("deleteUserFromComUsedAddress");
		Object[] params = new Object[]{"6006800BBDF034DFE040007F01001D9F", "20" , "1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
}
