package com.strongit.oa.webservice.util;

import java.util.Properties;

import org.apache.xmlbeans.xml.stream.EndPrefixMapping;

import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.util.PropertiesUtil;
import com.strongmvc.exception.SystemException;

/**
 * 根据webserviceaddress.properties中定义的地址获取WEBSERVICE路径
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-7-15 上午10:25:14
 * @version  2.0.2.3
 * @classpath com.strongit.oa.webservice.util.WebServiceAddressUtil
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class WebServiceAddressUtil {


	/**
	 * 获取财政WEBSERVICE地址
	 * @author:邓志城
	 * @date:2009-7-15 上午10:26:09
	 * @return
	 * @throws Exception
	 */
	public static String getFinanceWebServiceAddress()throws Exception{
		String endpointAddress = "http://";//请求的服务地址
		Properties prop = PropertiesUtil.getPropertiesWithFileName("webserviceaddress.properties");
		String ip = prop.getProperty("finance.service.ip");//获取财政提供的服务地址
		String port = prop.getProperty("finance.service.ip.port");//获取IPP提供服务地址的端口
		String context = prop.getProperty("finance.service.context");//获取财政工程上下文路径
		if(ip == null ){
			throw new SystemException("请先配置财政提供的WEBSERVICE服务地址！");
		}
		if(context == null || "".equals(context)){
			context = "";
		}else{
			context = "/"+context;
		}
		endpointAddress = endpointAddress + ip +  ":" + port + context + "/services/FinanceService";
		LogPrintStackUtil.logInfo("财政服务地址：" + endpointAddress);
		return endpointAddress;
	}
	
	/**
	 * 获取IPP的WEBSERVICE地址
	 * @author:邓志城
	 * @date:2009-7-15 上午10:26:33
	 * @return
	 * @throws Exception
	 */
	public static String getIPPWebServiceAddress()throws Exception{
		String endpointAddress = "http://";//请求的服务地址
		Properties prop = PropertiesUtil.getPropertiesWithFileName("webserviceaddress.properties");
		String ip = prop.getProperty("ipp.service.ip");//获取IPP提供的服务地址
		String port = prop.getProperty("ipp.service.ip.port");//获取IPP提供服务地址的端口
		String context = prop.getProperty("ipp.service.context");//获取IPP工程上下文路径
		if(ip == null){
			throw new SystemException("请先配置IPP提供的WEBSERVICE服务地址！");
		}
		if(context == null || "".equals(context)){
			context = "";
		}else{
			context = "/"+context;
		}
		endpointAddress = endpointAddress + ip + ":" + port + context + "/services/IPPService";
		LogPrintStackUtil.logInfo("IPP服务地址：" + endpointAddress);
		return endpointAddress;
	}

	/**
	 * 得到大蚂蚁WEBSERVICE地址
	 * @author:邓志城
	 * @date:2010-6-24 上午10:53:51
	 * @return
	 * @throws Exception
	 */
	public static String getBigAntWebServiceAddress()throws Exception {
		Properties prop = PropertiesUtil.getPropertiesWithFileName("webserviceaddress.properties");
		return prop.getProperty("bigant.service.url");
	}
	
	/**
	 * 测试用的
	 * @author:邓志城
	 * @date:2009-7-15 上午10:39:36
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)throws Exception{
		getFinanceWebServiceAddress();
		getIPPWebServiceAddress();
	}
	
	/**
	 * 获取退回的模式
	 * 0:退回到上一步
	 * 1:退回到拟稿节点
	 */
	public static String getBackModule()throws Exception {
		Properties prop = PropertiesUtil.getPropertiesWithFileName("webserviceaddress.properties");
		return prop.getProperty("ios_workflow_back_module");
	}
}
