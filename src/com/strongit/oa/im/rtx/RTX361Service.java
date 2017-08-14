package com.strongit.oa.im.rtx;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongmvc.exception.SystemException;

@Service
public class RTX361Service{	
	
	private Log logger = LogFactory.getLog(this.getClass());
	private String rtxServerIp;
	private String rtxServerPort;
	private String rtxPhp;
	
	private ToaImConfig config ;

	static RTX361Service instance = null;
	
	public static RTX361Service getInstance() {
		if(instance == null){
			try {
				instance = new RTX361Service();
			} catch (IOException e) {
				throw new SystemException(e);
			}
		}
		return instance ;
	}
	
	/**
	 * 构造函数
	 */
	public RTX361Service() throws IOException{
/*		//从rtx.properties配置文件获取RTX的HTTP服务IP和端口,以及处理请求的php页面
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("im.properties");
		InputStream is = url.openStream();
		Properties prop = System.getProperties();
		prop.load(is);
		
		rtxServerIp = prop.getProperty("rtx.httpServer.ip");
		rtxServerPort = prop.getProperty("rtx.httpServer.port");
		rtxPhp = prop.getProperty("rtx.httpServer.php");
		
		if (is != null) {
			is.close();
		}*/
	}
	
	/**
	 * 获取HTTP远端链接处理的结果
	 * @param objectName 对象名称
	 * @param proCode 协议编码
	 * @param prop 参数名称数组
	 * @param propMap 参数结合
	 * @return Object[] 包含两个参数的数组，{返回码,返回结果}
	 * @throws Exception
	 */
	private Object[] httpConnectionHandle(String objectName, int proCode,
			Object[] prop, Map<String, String> propMap)
			throws MalformedURLException, IOException, SystemException {
		Object[] ret = new Object[2];
		if(config == null){
			config = Cache.get();
		}
		rtxServerIp = config.getImconfigIp();
		rtxServerPort = config.getImconfigPort();
		rtxPhp = config.getImconfigUrl();
		//url请求格式如下，包含对象名称，命令码，请求参数
		//http://[serverIp]:[port]//[serverPhp]?objectName=[name]&cmd=[code]&[para=value]...
		StringBuffer urlPath = new StringBuffer();
		urlPath.append("http://").append(rtxServerIp)
		       .append(":").append(rtxServerPort).append("/").append(rtxPhp)
		       .append("?objectName=").append(objectName).append("&cmd=0x").append(Integer.toHexString(proCode));
		
		for (int i=0; i<prop.length; i++) {
			urlPath.append("&").append(prop[i]).append("=").append(propMap.get(prop[i]));
		}
		LogPrintStackUtil.printInfo(logger, "请求的Rtx地址:"+urlPath.toString());
		//通过HTTP访问RTX服务，获取处理结果
		URL url = new URL(urlPath.toString());
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();
		
		//HTTP头部信息格式
		//0 = HTTP/1.1 200 OK
		//1 = Wed, 13 Feb 2008 02:15:13 GMT
		//2 = Apache/2.0.49 (Win32) PHP/4.3.6
		//3 = PHP/4.3.7
		//4 = 0
		//5 = XXXXX
		//6 = text/html; charset=gb2312
		//返回的HTTP请求头中，第四位表示返回码，第五位表示返回字符串。
		int retCode = Integer.parseInt(httpConnection.getHeaderField(4));
		ret[0] = retCode;
		if (retCode == 0) {
			String encodeResult = new String(httpConnection.getHeaderField(5).getBytes("ISO-8859-1"), "GB2312");
			ret[1] = encodeResult;
		} else {
			throw new SystemException(urlPath.toString());
		}

		return ret;
	}
	
	/**
	 * 添加用户
	 * @param deptId
	 * @param loginName
	 * @param password
	 * @param realName
	 * @param mobile
	 * @return
	 * @throws SystemException
	 */
	public String addUser(String deptId, String loginName, String password,
			String realName, String mobile) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[5];
		prop[0] = "DEPTID";
		prop[1] = "NICK";
		prop[2] = "PWD";
		prop[3] = "NAME";
		prop[4] = "MOBILE";
		map.put(prop[0].toString(), deptId);
		map.put(prop[1].toString(), loginName);
		map.put(prop[2].toString(), password);
		map.put(prop[3].toString(), realName);
		map.put(prop[4].toString(), mobile);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_USERMANAGER,
					RTXConst.PRO_ADDUSER, prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 删除用户
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public String delUser(String userLoginName) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "USERNAME";
		map.put(prop[0].toString(), userLoginName);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_USERMANAGER,
					RTXConst.PRO_DELUSER, prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return ret;
	}
	
	/**
	 * 更新用户信息
	 * @param deptId
	 * @param mobile
	 * @param realName
	 * @param loginName
	 * @param password
	 * @param rtxNo
	 * @return
	 * @throws SystemException
	 */
	public String updateUser(String deptId, String mobile, String realName,
			String loginName, String password, String rtxNo)
			throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[6];
		prop[0] = "DEPTID";
		prop[1] = "MOBILE";
		prop[2] = "NAME";
		prop[3] = "NICK";
		prop[4] = "PWD";
		prop[5] = "UIN";
		map.put(prop[0].toString(), deptId);
		map.put(prop[1].toString(), mobile);
		map.put(prop[2].toString(), realName);
		map.put(prop[3].toString(), loginName);
		map.put(prop[4].toString(), password);
		map.put(prop[5].toString(), rtxNo);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_USERMANAGER, RTXConst.PRO_GETUSERSMPLINFO, prop,
					map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 获取用户简单信息
	 * @param userLoginName
	 * @return "userRTXNo:用户RTX编号,userName:用户名"
	 * @throws SystemException
	 */
	public String getUserInfo(String userLoginName) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "USERNAME";
		map.put(prop[0].toString(), userLoginName);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_USERMANAGER,
					RTXConst.PRO_GETUSERSMPLINFO, prop, map);

			ret = result[1].toString();
		}catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 添加部门
	 * @param deptId
	 * @param parentDeptId
	 * @param deptName
	 * @param deptDesc
	 * @return
	 * @throws SystemException
	 */
	public String addDept(String deptId, String parentDeptId, String deptName,
			String deptDesc)
			throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[4];
		prop[0] = "DEPTID";
		prop[1] = "PDEPTID";
		prop[2] = "NAME";
		prop[3] = "INFO";
		map.put(prop[0].toString(), deptId);
		map.put(prop[1].toString(), parentDeptId);
		map.put(prop[2].toString(), deptName);
		map.put(prop[3].toString(), deptDesc);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_USERMANAGER, RTXConst.PRO_ADDDEPT,
					prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 删除部门
	 * @param deptId 部门ID
	 * @param isDelUsersDepts 是否删除部门下所有用户和子部门(0-不删除,1-删除)
	 * @return
	 * @throws SystemException
	 */
	public String delDept(String deptId, String isDelUsersDepts) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[2];
		prop[0] = "DEPTID";
		prop[1] = "COMPLETEDELBS";
		map.put(prop[0].toString(), deptId);
		map.put(prop[1].toString(), isDelUsersDepts);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_USERMANAGER, RTXConst.PRO_DELDEPT,
					prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 修改部门信息
	 * @param deptId
	 * @param parentDeptId
	 * @param deptName
	 * @param deptDesc
	 * @return
	 * @throws SystemException
	 */
	public String updateDept(String deptId, String parentDeptId, String deptName,
			String deptDesc) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[4];
		prop[0] = "DEPTID";
		prop[1] = "PDEPTID";
		prop[2] = "NAME";
		prop[3] = "INFO";
		map.put(prop[0].toString(), deptId);
		map.put(prop[1].toString(), parentDeptId);
		map.put(prop[2].toString(), deptName);
		map.put(prop[3].toString(), deptDesc);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_USERMANAGER, RTXConst.PRO_SETDEPT,
					prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 获取部门下所有用户
	 * @param deptId 部门ID
	 * @return "user1,user2,user3,...,userN"
	 */
	public String getDeptUsers(String deptId) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "DEPTID";
		map.put(prop[0].toString(), deptId);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_DEPTMANAGER, RTXConst.PRO_GETDEPTUSERS,
					prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 获取部门下所有子部门
	 * @param deptId 部门ID(部门ID为0时可以获取一级部门)
	 * @return "dept1,dept2,dept3,...,deptN"
	 */
	public String getDeptSubDepts(String deptId) throws SystemException {
		if(deptId == null || "".equals(deptId)){
			deptId = "0";
		}
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "PDEPTID";
		map.put(prop[0].toString(), deptId);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_DEPTMANAGER, RTXConst.PRO_GETSUBDEPTS,
					prop, map);

			ret = result[1].toString();
		}catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 获取部门信息
	 * @param deptId
	 * @return "deptName:部门名称,deptId:部门ID"
	 */
	public String getDeptInfo(String deptId) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "DEPTID";
		map.put(prop[0].toString(), deptId);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_USERMANAGER, RTXConst.PRO_GETDEPTINFO,
					prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 发送即时消息
	 * @param sender
	 * @param receivers
	 * @param msg
	 * @param password
	 * @return
	 * @throws SystemException
	 */
	public String sendIM(String sender, String receivers, String msg,
			String password) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[4];
		prop[0] = "SENDER";
		prop[1] = "RECVUSERS";
		prop[2] = "IMMSG";
		prop[3] = "SDKPASSWORD";
		map.put(prop[0].toString(), sender);
		map.put(prop[1].toString(), receivers);
		map.put(prop[2].toString(), msg);
		map.put(prop[3].toString(), password);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_RTXSYS,
					RTXConst.PRO_SYS_SENDIM, prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 发送消息提醒
	 * @param receivers
	 * @param msg
	 * @param msgId
	 * @param type
	 * @param title
	 * @param delayTime
	 * @return
	 * @throws SystemException
	 */
	public String sendNotify(String receivers, String msg, String msgId,
			String type, String title, String delayTime) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[6];
		prop[0] = "USERNAME";
		prop[1] = "MSGINFO";
		prop[2] = "MSGID";
		prop[3] = "TITLE";
		prop[4] = "TYPE";
		prop[5] = "DELAYTIME";
		map.put(prop[0].toString(), receivers);
		map.put(prop[1].toString(), msg);
		map.put(prop[2].toString(), msgId);
		map.put(prop[3].toString(), type);
		map.put(prop[4].toString(), title);
		map.put(prop[5].toString(), delayTime);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_RTXSYS,
					RTXConst.PRO_SYS_SENDNOTIFY, prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 用户是否存在
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public String isUserExist(String userLoginName) throws SystemException {		
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "USERNAME";
		map.put(prop[0].toString(), userLoginName);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_USERMANAGER,
					RTXConst.PRO_IFUSEREXIST, prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 获取用户在线状态
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public String getUserStatus(String userLoginName) throws SystemException {
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "USERNAME";
		map.put(prop[0].toString(), userLoginName);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(RTXConst.OBJNAME_RTXSYS,
					RTXConst.PRO_SYS_GETUSERSTATUS, prop, map);

			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}

		return ret;
	}
	
	/**
	 * 获取用户的sessionKey，用于登陆RTX
	 * @param userLoginName
	 * @return
	 * @throws SystemException
	 */
	public String getSessionKey(String userLoginName) throws SystemException{
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[1];
		prop[0] = "USERNAME";
		map.put(prop[0].toString(), userLoginName);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_RTXSYS, RTXConst.PRO_SYS_GETSESSIONKEY, prop, map);
			
			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return ret;
	}

	/**
	 * 校验登陆sessionKey
	 * @param userLoginName
	 * @param sessionKey
	 */
	public String checkSessionKey(String userLoginName, String sessionKey) throws SystemException{
		Map<String, String> map = new HashMap<String, String>();
		Object[] prop = new Object[2];
		prop[0] = "USERNAME";
		prop[1] = "SESSIONKEY";
		map.put(prop[0].toString(), userLoginName);
		map.put(prop[1].toString(), sessionKey);
		String ret = null;

		try {
			Object[] result = httpConnectionHandle(
					RTXConst.OBJNAME_RTXSYS, RTXConst.PRO_SYS_USERLOGINVERIFY, prop, map);
			
			ret = result[1].toString();
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return ret;
	}	
}
