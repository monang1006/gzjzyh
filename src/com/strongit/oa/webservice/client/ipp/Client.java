package com.strongit.oa.webservice.client.ipp;

import javax.xml.namespace.QName;

import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;

import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.oa.webservice.util.WebServiceAddressUtil;
import com.strongit.oa.webservice.vo.VoResult;

/**
 * IPP的客户端实现
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2009-7-15 上午10:56:19
 * @version  2.0.2.3
 * @classpath com.strongit.oa.webservice.client.ipp.Client
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class Client {

	/**
	 * 修改IPP提交的表单数据状态,并返回最后的审批意见
	 * @author:邓志城
	 * @date:2009-7-15 上午10:56:42
	 * @param feedBackCode
	 * @param suggestion
	 */
	public static void archiveIPP(String feedBackCode,String status,String suggestion){
		try{
			Service service = new Service();
			Call call = (Call)service.createCall();
			String url = WebServiceAddressUtil.getIPPWebServiceAddress();
			call.setTargetEndpointAddress(url);
			call.setOperationName("editRefer");
			//2:已办结
			Object[] params = new Object[]{feedBackCode,status,suggestion};
			String ret = (String)call.invoke(params);
			if("true".equals(ret)){
				LogPrintStackUtil.logInfo("修改IPP业务数据状态成功！");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void archiveWork(String vo){
		try{
			Service service = new Service();
			Call call = (Call)service.createCall();
			//String url = "http://192.168.2.168:8001/StrongOA/services/WorkflowService";
			String url = "http://192.168.2.62:8020/StrongOAIPP/services/WorkflowService";
			call.setTargetEndpointAddress(url);
			call.setOperationName("saveFormData");
			
			//2:已办结
			Object[] params = new Object[]{vo,null,null};
			String ret = (String)call.invoke(params);
			System.out.println(ret);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)throws Exception{
		VoResult vo = new VoResult();
		vo.setCardNumber("123");
		vo.setBirthday("3333");
		vo.setCardType("身份证");
		vo.setCity("南昌");
		vo.setCountry("中国");
		vo.setEducation("本科");
		vo.setJob("程序员");
		vo.setManBirthday("1125");
		vo.setManCardNumber("234");
		vo.setManCardType("身份证");
		vo.setManCity("南昌");
		vo.setManCountry("中国");
		vo.setManEducation("大学本科");
		vo.setManJob("It");
		vo.setManMarriage("未婚");
		vo.setManMobile("13607041072");
		vo.setManName("邓志城");
		vo.setManNation("汉族");
		vo.setManPermantType("");
		vo.setManProvince("江西省");
		vo.setMarriage("未婚");
		vo.setMobile("111111111111");
		vo.setName("婷婷");
		vo.setNation("汉族");
		vo.setPermantType("身份证");
		vo.setProvince("江西省");
		vo.setTitle("婚姻登记表单测试");
		String obj = JSONObject.fromObject(vo).toString();
		System.out.println(obj);
		JSONObject jobj = JSONObject.fromObject(obj);
		jobj.values();
		System.out.println(jobj.values());
		archiveWork(obj);
	}
}
