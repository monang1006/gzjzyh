/**
 * 
 */
package com.strongit.oa.webservice.client.columnArticles;

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
public class ColumnArticlesClient extends TestCase {
	private Service service = null;	
	private Call call = null;
	private String url = null;
	
	public void setUp(){
		service = new Service();
		try {
			call = (Call)service.createCall();
			url = "http://localhost/OA5.0/services/columnArticlesService";
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
	public void testGetColumnArticles() throws Exception {
		call.setOperationName("getColumnArticles");
		Object[] params = new Object[]{"10","1"};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}
	
	@Test
	public void testLoadArticleContent() throws Exception {
		call.setOperationName("loadArticleContent");
		Object[] params = new Object[]{"402882f033a0038f0133a0d192ec0001",1};
		String ret = (String)call.invoke(params);
		System.out.println(ret);
	}

}
