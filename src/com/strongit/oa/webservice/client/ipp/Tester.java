package com.strongit.oa.webservice.client.ipp;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class Tester {

	/**
	 * 修改咨询反馈状态，填写审核意见
	 * 
	 */
	public static void main(String[] args)throws Exception{
		// TODO Auto-generated method stub
		/*IPPServiceService serviceFactory=new IPPServiceServiceLocator();
		try {
			IPPService_PortType service=serviceFactory.getIPPService();
			
			 * 1、咨询反馈号
			 * 2、咨询反馈状态
			 * 3、审核意见
			 
			String ret = service.editRefer("oa200971311917","0","tester");
			System.out.println(ret);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		testIPP();
	}

	public static void testIPP()throws Exception{
		Service service = new Service();
		Call call = (Call)service.createCall();
		String url = "http://192.168.2.137/services/IPPService";
		call.setTargetEndpointAddress(url);
		call.setOperationName("editRefer");
		Object[] params = new Object[]{"oa200971311917","0","tester"};
		System.out.println(call.invoke(params));
	}
	
}
