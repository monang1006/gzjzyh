package com.strongit.oa.im.rtx;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rtx.RTXSvrApi;

import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.im.cache.Cache;

/**
 * RTX2010操作
 * 
 * @author strong_yangwg
 * 
 */
public class RTX2010Service {

	private Logger logger = LoggerFactory.getLogger(RTX2010Service.class);

	private static ToaImConfig config;

	private static RTX2010Service instance = null;

	private static RTXSvrApi rtxApi = null;

	public static RTX2010Service getInstance() {
		if (config == null) {
			config = Cache.get();
		}
		if (instance == null) {
			instance = new RTX2010Service();
		}
		return instance;
	}

	private boolean init() {
		if (rtxApi == null) {
			rtxApi = new RTXSvrApi();
		}
		if (rtxApi != null && rtxApi.Init()) {
			if (rtxApi.getServerPort() != 6000) {
				rtxApi.setServerPort(6000);
			}
			return true;
		}
		return false;
	}

	/**
	 * 得到RTX服务器IP
	 * 
	 * @return
	 */
	public String getServerIp() {
		return this.rtxApi.getServerIP();
	}

	/**
	 * 得到RTX服务器端口
	 * 
	 * @return
	 */
	public int getServerPort() {
		return this.rtxApi.getServerPort();
	}

	/**
	 * 增加或更新部门信息，
	 * 
	 * @param deptId
	 *            为数字型，不能为字符型
	 * @param DetpInfo
	 *            部门描述
	 * @param DeptName
	 *            部门名称
	 * @param ParentDeptId
	 *            如果没有上级部门，则为0
	 * @return 0 成功;非零 失败
	 */
	public int addDept(String deptId, String detpInfo, String deptName,
			String parentDeptId) {
		this.logger.info("同步部门信息:" + deptId);
		int isSuccess = -1;
		if (this.init()) {
			this.logger.info("服务器信息:" + rtxApi.getServerIP() + ","
					+ rtxApi.getServerPort());
			// // 新增部门
			isSuccess = rtxApi
					.addDept(deptId, detpInfo, deptName, parentDeptId);
		} else {
			this.logger.info("初始化失败");
		}
		rtxApi.UnInit();
		return isSuccess;
	}

	/**
	 * 得到RTX部门名称
	 * 
	 * @param deptId
	 * @return
	 */
	public String getDeptName(String deptId) {
		if (this.init()) {
			return rtxApi.GetDeptName(deptId);
		}
		return null;
	}

	/**
	 * 列出某部门的用户
	 * 
	 * @param deptId
	 * @return
	 */
	public List listUserOfDept(String deptId) {
		List userList = new ArrayList();
		if (this.init()) {
			this.logger.info("服务器信息:" + rtxApi.getServerIP() + ","
					+ rtxApi.getServerPort());
			String rtxUserArray[] = rtxApi.getDeptUsers(deptId);
			if (rtxUserArray == null || rtxUserArray.length == 0)
				return null;
			for (String rtxUser : rtxUserArray) {
				userList.add(rtxUser);
			}
		}
		return userList;
	}

	/**
	 * 列出某部门所有子部门ID
	 * 
	 * @param deptId
	 * @return
	 */
	public List listChildDeptIdOfDept(String deptId) {
		this.logger.info("列出子部门:" + deptId);
		List childDeptIdList = new ArrayList();
		if (this.init()) {
			this.logger.info("服务器信息:" + rtxApi.getServerIP() + ","
					+ rtxApi.getServerPort());
			String childDeptIdArray[] = rtxApi.getChildDepts(deptId);
			if (childDeptIdArray != null && childDeptIdArray.length > 0) {
				for (String childDeptId : childDeptIdArray) {
					childDeptIdList.add(childDeptId);
				}
			}
		}
		return childDeptIdList;
	}

	/**
	 * 删除部门信息
	 * 
	 * @return
	 */
	public int deleteDeptInfo(String deptId) {
		int isSuccess = -1;
		if (this.init()) {
			// 判断部门是否存在
			if (rtxApi.deptIsExist(deptId) == 0) {
				// 删除部门信息
				final String type = "0";// 0:不删除该部门下的用户 ;1：删除该部门下的用户
				rtxApi.deleteDept(deptId, type);
			}
		}
		rtxApi.UnInit();
		return isSuccess;
	}

	/**
	 * 列出某部门所有的子部门
	 * 
	 * @param rtxApi
	 * @param deptIdList
	 * @param deptId
	 */
	private void listAllChildDeptId(List<String> deptIdList, String deptId) {
		if (this.init()) {
			this.logger.info("服务器信息:" + rtxApi.getServerIP() + ","
					+ rtxApi.getServerPort());
			String[] deptIdArray = rtxApi.getChildDepts(deptId);
			// 是否列出子部门ID
			if (deptIdArray != null && deptIdArray.length > 0) {
				for (String childDeptId : deptIdArray) {
					deptIdList.add(childDeptId);
					listAllChildDeptId(deptIdList, childDeptId);
				}
			}
			rtxApi.UnInit();
		}
	}

	/**
	 * 删除所有部门及用户
	 */
	public void deleteAlldDept() {
		this.logger.info("删除所有部门信息");
		if (this.init()) {
			List<String> allChildDeptIdList = new ArrayList<String>();
			// 列出所有的部门
			listAllChildDeptId(allChildDeptIdList, "0");
			if (allChildDeptIdList != null) {
				for (String deptId : allChildDeptIdList) {
					this.logger.info("-->删除部门:" + deptId);
					// 删除部门及用户
					rtxApi.deleteDept(deptId, String.valueOf(1));
				}
			}
			rtxApi.UnInit();
		}
	}

	/**
	 * 新增人员信息
	 * 
	 * @param rtxServerIp
	 * @param rtxServerPort
	 * @param UserName
	 *            账号
	 * @param DeptID
	 *            为零，则不指定部门,数字型
	 * @param ChsName
	 *            中文名
	 * @param gender
	 *            性别 0:男，1:女
	 * @param Pwd
	 *            密码
	 * @return
	 */
	public int addUser(String userName, String deptID, String chsName,
			String password) {
		int isSuccess = -1;
		if (password == null)
			password = "";
		if (this.init()) {
			// 新增用户
			isSuccess = rtxApi.addUser(userName, deptID, chsName, password);
		}
		rtxApi.UnInit();
		return isSuccess;
	}

	/**
	 * 修改用户信息
	 * 
	 * @param userName
	 * @param deptID
	 *            数字型
	 * @param chsName
	 * @param email
	 * @param gender
	 * @param mobile
	 * @param phone
	 * @return
	 */
	public int updateUser(String userName, String deptID, String chsName,
			String email, String gender, String mobile, String phone) {
		int isSuccess = -1;
		if (this.init()) {
			// 新增用户
			isSuccess = rtxApi.SetUserSimpleInfoEx(userName, deptID, chsName,
					email, gender, mobile, phone, null);
		}
		rtxApi.UnInit();
		return isSuccess;
	}

	/**
	 * 删除用户信息
	 * 
	 * @param rtxServerIp
	 * @param rtxServerPort
	 * @param userCode
	 * @return
	 */
	public int deleteUser(String userName) {
		int isSuccess = -1;
		if (this.init()) {
			// 判断用户是否存在，如果存在则删除
			if (rtxApi.userIsExist(userName) == 0) {
				rtxApi.deleteUser(userName);
			}
		}
		rtxApi.UnInit();
		return isSuccess;
	}

	/**
	 * 得到用户名
	 * 
	 * @return
	 */
	public String getUserName(String userCode) {
		if (this.init()) {
			if (rtxApi.userIsExist(userCode) == 0) {
				return rtxApi.GetUserSimpleInfo(userCode)[6][1];
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 查询用户状态 0 :离线 1:在线 2:离开 -984:不存在 其他:服务器错误
	 * 
	 * @param rtxServerIp
	 * @param rtxServerPort
	 * @param userName
	 * @return
	 */
	public int queryUserStatus(String userName) {
		int iState = -1;
		if (this.init()) {
			// 查询
			iState = rtxApi.QueryUserState(userName);
		}
		rtxApi.UnInit();
		return iState;
	}

	/**
	 * 查询用户是否存在
	 * 
	 * @param userName
	 * @return
	 */
	public int queryUserIsExists(String userName) {
		int iState = -1;
		if (this.init()) {
			iState = rtxApi.userIsExist(userName);
		}
		rtxApi.UnInit();
		return iState;
	}

	/**
	 * 发送RTX消息 需要设置"SDKProperty.xml"加入发消息的服务器IP <sdkhttp> <IPLimit Enabled="1">
	 * <IP>192.168.2.220</IP> </IPLimit> </sdkhttp> </Property>
	 * 
	 * @param receivers
	 *            接收人
	 * @param rtxMessage
	 *            消息内容
	 * @param rtxMsgTitle
	 *            RTX消息标题
	 * @param msgUrlLink
	 *            RTX消息链接
	 * @return
	 */
	public int sendRtxMsg(String receivers, String rtxMessage,
			String rtxMsgTitle, String msgUrlLink) {
		int isSuccess = -1;
		HttpURLConnection httpConnection = null;
		try {
			String httpUrl = "http://" + config.getImconfigIp() + ":"
					+ config.getImconfigPort() + "/sendnotify.cgi?receiver="
					+ receivers;
			// 标题
			if (rtxMsgTitle == null || rtxMsgTitle.length() == 0) {
				rtxMsgTitle = "OA消息提醒";
			}
			httpUrl += "&title=" + rtxMsgTitle;
			// 消息内容
			if (msgUrlLink != null && msgUrlLink.length() > 0) {
				rtxMessage += "[点击|" + msgUrlLink + "]查收";
			}
			httpUrl += "&msg=" + rtxMessage;
			System.out.println("发送RTX消息：" + httpUrl);
			// 发送
			java.net.URL url = new URL(httpUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			if (httpConnection == null) {
				logger.error("发送RTX消息失败");
			}
			httpConnection.getHeaderField(4); // 连接上
			httpConnection.disconnect();
			return 0;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return isSuccess;
	}
}
