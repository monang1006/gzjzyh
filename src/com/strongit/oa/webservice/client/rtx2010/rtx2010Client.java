package com.strongit.oa.webservice.client.rtx2010;
 
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.junit.Test;


/**
 * @description 类描述
 * @className columnArticlesClient
 * @company Strongit Ltd. (C) copyright
 * @author 申仪玲
 * @email shenyl@strongit.com.cn
 * @created 2011-11-7 下午07:04:59
 * @version 3.0
 */
public class rtx2010Client extends TestCase {
	private Service service = null;	
	private Call call = null;
	private String url = null;
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://192.168.2.122:8080/oa5.0/services/rtx2010WebService";
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
	
	public void testQueryUserIsExists( ) {
		call.setOperationName("queryUserStatus");
		Object[] params = new Object[] {"admin123123"};
		int ret =0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("*******************************");
			e.printStackTrace();
		}
		System.out.println(ret);
	}

}
