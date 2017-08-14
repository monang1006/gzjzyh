package com.strongit.oa.webservice.client.uums;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * 统一用户测试用例
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Jul 5, 2012 12:35:29 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.webservice.client.uums.UumsWebServiceClient
 */
public class UumsWebServiceClient extends TestCase {

	private Service service = null;

	private Call call = null;

	private String url = null;

	private static Document loginResponseXml = null;

	public void setUp() {
		service = new Service();
		try {
			call = (Call) service.createCall();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void tearDown() {
		service = null;
		call = null;
	}

	/**
	 * 测试用户登录验证服务
	 * 
	 * @author 严建
	 * @throws Exception
	 * @createTime Jul 5, 2012 10:23:34 AM
	 */
	@Test
	public void testUserLogin() throws Exception {
		userLogin("admin", "c4ca4238a0b923820dcc509a6f75849b");
	}

	/**
	 * 测试模块权限验证服务
	 * 
	 * @author 严建
	 * @throws Exception
	 * @createTime Jul 5, 2012 10:35:50 AM
	 */
	@Test
	public void testUserModuleValidate() throws Exception {
		userLogin("admin", "c4ca4238a0b923820dcc509a6f75849b");
		String sessionId = getSessionId(loginResponseXml);// 安全控制Session标识
		// 001-0001000100010001
		String subSystemCode = "001";// 子系统编号
		String userLoginName = "admin";// 用户登录名
		String privilModuleCode = "0001000100010001";// 权限模块编号
		userModuleValidate(sessionId, subSystemCode, userLoginName,
				privilModuleCode);
		// 001-0001
		subSystemCode = "001";// 子系统编号
		privilModuleCode = "000100010009";// 权限模块编号
		userModuleValidate(sessionId, subSystemCode, userLoginName,
				privilModuleCode);

	}

	/**
	 * 测试用户登出服务
	 * 
	 * @author 严建
	 * @throws Exception
	 * @createTime Jul 5, 2012 11:56:28 AM
	 */
	@Test
	public void testUserLogout() throws Exception {
		userLogout("admin", "c4ca4238a0b923820dcc509a6f75849b");
	}

	/**
	 * 测试根据用户登录名获取模块权限信息
	 * 
	 * @author 严建
	 * @throws Exception
	 * @createTime Jul 5, 2012 12:11:56 PM
	 */
	@Test
	public void testGetModuleInfoByUserLoginname() throws Exception {
		getModuleInfoByUserLoginname("admin",
				"c4ca4238a0b923820dcc509a6f75849b");
	}

	/**
	 * 用户登录验证服务
	 * 
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Jul 5, 2012 10:33:31 AM
	 */
	public void userLogin(String userLoginName, String password)
			throws Exception {
		url = "http://192.168.2.125:1988/oa/services/AuthenticationService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("userLogin");
		Object[] params = new Object[] { userLoginName, password };
		String ret = (String) call.invoke(params);
		loginResponseXml = xmlStringToDoc(ret);
		System.out.println("正在测试用户登录验证服务,登录名称：" + userLoginName + "|登录密码："
				+ password);
		if (isSuccessed(ret)) {
			System.out.println(ret);
			System.out.println("测试成功");
		} else {
			System.out.println(ret);
			System.out.println("测试失败");
		}
	}

	/**
	 * 获取sessionID
	 * 
	 * @author 严建
	 * @param loginResponseXml
	 * @return
	 * @throws Exception
	 * @createTime Jul 5, 2012 11:11:12 AM
	 */
	public static String getSessionId(Document loginResponseXml)
			throws Exception {
		NodeList nodelist = loginResponseXml.getElementsByTagName("data");
		Element data = (Element) nodelist.item(0);
		Node bean = data.getFirstChild();
		nodelist = bean.getChildNodes();
		int length = nodelist.getLength();
		String SessionId = "";
		for (int i = 0; i < length; i++) {
			Element property = (Element) nodelist.item(i);
			if ("SessionId".equals(property.getAttribute("name"))) {
				Element item = (Element) property.getFirstChild();
				SessionId = item.getAttribute("value");
			}
		}
		return SessionId;
	}

	/**
	 * xml字符串转换为Document
	 * 
	 * @author 严建
	 * @param content
	 * @return
	 * @throws Exception
	 * @createTime Jul 5, 2012 10:29:13 AM
	 */
	public static Document xmlStringToDoc(String content) throws Exception {
		StringReader sr = new StringReader(content);
		InputSource is = new InputSource(sr);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		return doc;
	}

	/**
	 * 判断是否调用成功
	 * 
	 * @author 严建
	 * @param ret
	 * @return
	 * @throws Exception
	 * @createTime Jul 5, 2012 12:04:33 PM
	 */
	public static boolean isSuccessed(String ret) throws Exception {
		boolean result = false;
		Document doc = xmlStringToDoc(ret);
		NodeList nodelist = doc.getElementsByTagName("status");
		Element status = (Element) nodelist.item(0);
		ret = status.getTextContent();
		String source = "01";
		if (source.indexOf(ret) == 1) {
			result = true;
		}
		return result;
	}

	/**
	 * 模块权限验证服务
	 * 
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Jul 5, 2012 10:35:22 AM
	 */
	public void userModuleValidate(String sessionId, String subSystemCode,
			String userLoginName, String privilModuleCode) throws Exception {
		if (loginResponseXml == null) {
			System.out.println("请先登录系统");
		} else {
			url = "http://192.168.2.125:1988/oa/services/AuthenticationService";
			call.setTargetEndpointAddress(url);
			call.setOperationName("userModuleValidate");
			Object[] params = new Object[] { sessionId, subSystemCode,
					userLoginName, privilModuleCode };
			String ret = (String) call.invoke(params);
			System.out.println("正在测试模块权限验证服务,子系统编号:" + subSystemCode
					+ "|权限模块编号" + privilModuleCode);
			if (isSuccessed(ret)) {
				System.out.println(ret);
				System.out.println("测试成功");
				Document doc = xmlStringToDoc(ret);
				NodeList nodelist = doc.getElementsByTagName("data");
				Element data = (Element) nodelist.item(0);
				Element item = (Element) data.getFirstChild();
				String temp = item.getAttribute("value");// 获取是否拥有该权限模块信息
				String source = "01";
				if (source.indexOf(temp) == 0) {
					System.out.println("0：没有权限");
				} else {
					System.out.println("1：有权限");
				}
			} else {
				System.out.println(ret);
				System.out.println("测试失败");
			}
		}
	}

	/**
	 * 用户登出服务
	 * 
	 * @author 严建
	 * @param sessionId
	 * @throws Exception
	 * @createTime Jul 5, 2012 11:51:00 AM
	 */
	public void userLogout(String userLoginName, String password)
			throws Exception {
		userLogin(userLoginName, password);
		if (loginResponseXml == null) {
			System.out.println("请先登录系统");
		} else {
			String sessionId = getSessionId(loginResponseXml);
			url = "http://192.168.2.125:1988/oa/services/AuthenticationService";
			call.setTargetEndpointAddress(url);
			call.setOperationName("userLogout");
			Object[] params = new Object[] { sessionId };
			String ret = (String) call.invoke(params);
			System.out.println("正在测试用户登出服务,登录名称：" + userLoginName + "|登录密码："
					+ password);
			if (isSuccessed(ret)) {
				System.out.println(ret);
				System.out.println("测试成功");
			} else {
				System.out.println(ret);
				System.out.println("测试失败");
			}
		}
	}

	/**
	 * 根据用户登录名获取模块权限信息
	 * 
	 * @author 严建
	 * @param userLoginName
	 * @param password
	 * @throws Exception
	 * @createTime Jul 5, 2012 12:06:24 PM
	 */
	public void getModuleInfoByUserLoginname(String userLoginName,
			String password) throws Exception {
		userLogin(userLoginName, password);
		if (loginResponseXml == null) {
			System.out.println("请先登录系统");
		} else {
			String sessionId = getSessionId(loginResponseXml);
			url = "http://192.168.2.125:1988/oa/services/UserService";
			call.setTargetEndpointAddress(url);
			call.setOperationName("getModuleInfoByUserLoginname");
			Object[] params = new Object[] { sessionId, userLoginName };
			String ret = (String) call.invoke(params);
			System.out.println("正在测试根据用户登录名获取模块权限信息,登录名称：" + userLoginName
					+ "|登录密码：" + password);
			if (isSuccessed(ret)) {
				System.out.println(ret);
				System.out.println("测试成功");
			} else {
				System.out.println(ret);
				System.out.println("测试失败");
			}
		}
	}
}
