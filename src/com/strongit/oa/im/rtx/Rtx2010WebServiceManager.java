package com.strongit.oa.im.rtx;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.im.cache.Cache;

public class Rtx2010WebServiceManager {

	private Service service = null;
	private Call call = null;
	private String url = null;
	
	private static ToaImConfig config;

	
	private static Rtx2010WebServiceManager instance = null;

  
	public static Rtx2010WebServiceManager getInstance() {
		if (instance == null) {
			instance = new Rtx2010WebServiceManager();
		}
		
		if (config == null) {
			config = Cache.get();
		}
		return instance;
	}
	
	
	public Rtx2010WebServiceManager() {
		ToaImConfig config = Cache.get();
		String serverIP = config.getImconfigIp();
  		service = new Service();
		try {
			call = (Call) service.createCall();
			url = "http://"+config.getImconfigIp()+":8724/oa/services/rtx2010WebService";
			call.setTargetEndpointAddress(url);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public int queryUserStatus(String userName) {
		call.setOperationName("queryUserStatus");
		Object[] params = new Object[] { userName };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	
	public int queryUserIsExists(String userName) {
		call.setOperationName("queryUserIsExists");
		Object[] params = new Object[] { userName };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	

	public int addDept(String deptId, String detpInfo, String deptName, String parentDeptId) {
		call.setOperationName("addDept");
		Object[] params = new Object[] { deptId, detpInfo, deptName, parentDeptId };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public int deleteUser(String userName) {
		call.setOperationName("deleteUser");
		Object[] params = new Object[] { userName };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public String getUserName(String userCode) {
		call.setOperationName("getUserName");
		Object[] params = new Object[] { userCode };
		String ret = "";
		try {
			ret = (String) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public String listUserOfDept(String deptId) {
		call.setOperationName("listUserOfDept");
		Object[] params = new Object[] { deptId };
		String ret = "";
		try {
			ret = (String) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public String listChildDeptIdOfDept(String deptId) {
		call.setOperationName("listChildDeptIdOfDept");
		Object[] params = new Object[] { deptId };
		String ret = "";
		try {
			ret = (String) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
 			e.printStackTrace();
		}
		return ret;
	}

	public String getDeptName(String deptId) {
		call.setOperationName("getDeptName");
		Object[] params = new Object[] { deptId };
		String ret = "";
		try {
			ret = (String) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public void deleteAlldDept() {
		call.setOperationName("deleteAlldDept");
		Object[] params = new Object[] {};
		String ret = "";
		try {
			ret = (String) call.invoke(params);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
     * 设置用户简单资料，支持设置部门ID
     * @param UserName String 用户帐号
     * @param DeptID String 部门ID
     * @param ChsName String 用户姓名
     * @param email String 邮箱地址
     * @param gender String 性别，男为0，女为1
     * @param mobile String 手机号码
     * @param phone String  电话
     * @param pwd String 密码
     * @return int  0 操作成功 非0为失败
      */
	public int updateUser(String userName, String deptID, String chsName, String email, String gender, String mobile, String phone) {
		call.setOperationName("updateUser");
		Object[] params = new Object[] { userName, deptID, chsName, email, gender, mobile, phone };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public int addUser(String userName, String deptID, String chsName, String password) {
		call.setOperationName("addUser");
		Object[] params = new Object[] { userName, deptID, chsName, password };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}
	

	public int sendRtxMsg(String receivers, String rtxMessage, String rtxMsgTitle, String msgUrlLink) throws UnsupportedEncodingException {
		call.setOperationName("sendRtxMsg");
		rtxMsgTitle = URLEncoder.encode(rtxMsgTitle,"gbk");//因消息推是通过网址来传递，需转码后才能保证不丢失空格，回车等特殊字符。
		rtxMessage = URLEncoder.encode(rtxMessage,"gbk");
//		rtxMessage = URLDecoder.decode(rtxMessage);
		Object[] params = new Object[] { receivers, rtxMessage, rtxMsgTitle, msgUrlLink };
		int ret = 0;
		try {
			ret = (Integer) call.invoke(params);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return ret;
	}

}