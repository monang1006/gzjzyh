package com.strongit.oa.webservice.client.ipp;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.JAFDataHandlerDeserializerFactory;
import org.apache.axis.encoding.ser.JAFDataHandlerSerializerFactory;

import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.webservice.util.WebServiceAddressUtil;

public class NotifyWebService {
	
	public static String WebServiceAddress = "";
	
	public NotifyWebService()throws Exception{
		try {
			WebServiceAddress = WebServiceAddressUtil.getIPPWebServiceAddress();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}
	}
	/**
	 * author:luosy
	 * description:  发布公告到IPP
	 * modifyer:
	 * description:
	 * @param lanmuId 栏目ID
	 * @param author 作者
	 * @param title 标题
	 * @param content 内容
	 * @param afficheId 公告ID 
	 * @return
	 */
	public String sendToIpp(String lanmuId,String author,String title,String content,String afficheId,DataHandler[] dataHandlers,String[] fileName)throws Exception{
		try{
			IPPServiceService ippServiceFactory=new IPPServiceServiceLocator();
			IPPService_PortType ippService=ippServiceFactory.getIPPService();
			String res=ippService.addsArticle(new Object[]{lanmuId, title, author, content, afficheId,dataHandlers,fileName});
			

//			Service service = new Service();
//			Call call = (Call)service.createCall();
//			String url = WebServiceAddressUtil.getIPPWebServiceAddress();
//			LogPrintStackUtil.logInfo("IPP服务地址："+url);
//			call.setTargetEndpointAddress(url);
//			call.setOperationName("addsArticle");
//			QName qnameAttachMent = new QName(
//					"http://xml.apache.org/axis/wsdd/providers/java",
//					"ns1:DataHandler");
//			call.registerTypeMapping(DataHandler.class,
//					 qnameAttachMent,
//					 JAFDataHandlerSerializerFactory.class,
//					 JAFDataHandlerDeserializerFactory.class);
//			
//			Object[] params = new Object[]{lanmuId, title, author, content, afficheId,dataHandlers,fileName};
//			String res = (String)call.invoke(params);
			
			if("true".equals(res)){
				LogPrintStackUtil.logInfo("发布公告到IPP成功！");
			}
		
			if("true".equals(res)){
				return "success";
			}else {
				return "fail";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	
	/**
	 * author:luosy
	 * description: 获取IPP 栏目信息
	 * modifyer:
	 * description:
	 * @return
	 * @throws Exception
	 */
	public List getLanmuInfo()throws Exception{
		try{
//			IPPServiceService ippServiceFactory=new IPPServiceServiceLocator();
//			IPPService_PortType ippService=ippServiceFactory.getIPPService();
			ClinetHelp help=new ClinetHelp();
//			String result=ippService.getColumnTree();
			

			Service service = new Service();
			Call call = (Call)service.createCall();
			String url = WebServiceAddressUtil.getIPPWebServiceAddress();
			LogPrintStackUtil.logInfo("IPP服务地址："+url);
			call.setTargetEndpointAddress(url);
			call.setOperationName("getColumnTree");
			String result = (String)call.invoke(new Object[]{});
			
			List resultList=help.xmlToList(result);
			return resultList;
		}catch(Exception e){
			throw new Exception();
		}
	}
	
}
