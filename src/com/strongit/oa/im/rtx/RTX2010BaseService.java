package com.strongit.oa.im.rtx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 import org.apache.struts2.ServletActionContext;
import com.strongit.oa.address.AddressOrgManager;
import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.model.Task;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.im.service.AbstractBaseService;
import com.strongmvc.service.ServiceLocator;

/**
 * rtx2010版实现
 * 
 * @author strong_yangwg
 * 
 */
public class RTX2010BaseService extends AbstractBaseService {
//	private final RTX2010Service rtxManager = RTX2010Service.getInstance(); // 通过单例模式得到
//	private final Rtx2010WebServiceManager rtxManager = Rtx2010WebServiceManager.getInstance(); // 通过单例模式得到

	private Rtx2010WebServiceManager rtxManager;
	IUserService userService;// 注入统一用户接口

	AddressOrgManager orgManager;

	public RTX2010BaseService() {
		this.userService = (IUserService) ServiceLocator
				.getService("userService");
		this.orgManager = (AddressOrgManager) ServiceLocator
				.getService("addressOrgManager");
		rtxManager = new Rtx2010WebServiceManager();
	}
	
	

	@Override
	public String addDept(String deptId, String parentDeptId, String deptName,
			String desc, Object... objects) {
		int result = rtxManager.addDept(deptId, deptName, deptName,
				parentDeptId);
		return String.valueOf(result);
	}

	@Override
	public String addUser(String deptId, String loginName, String password,
			String realName, String mobile, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delDept(String deptId, String cascade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delUser(String userLoginName) {
		return String.valueOf(rtxManager.deleteUser(userLoginName));
	}

	//phj：改写
	@Override
	public List getChildDeptList(String deptId) {
		// TODO Auto-generated method stub
		String childId =  rtxManager.listChildDeptIdOfDept(deptId);
		
		List childDeptIdList = new ArrayList();

		if(!childId.equals("")){
			String childDeptIdArray[] = childId.split(",");
			if (childDeptIdArray != null && childDeptIdArray.length > 0) {
				for (String childDeptId : childDeptIdArray) {
					childDeptIdList.add(childDeptId);
				}
			}
		}
		return childDeptIdList;
	}
	
	
//	@Override
//	public List getChildDeptList(String deptId) {
//		// TODO Auto-generated method stub
//		return rtxManager.listChildDeptIdOfDept(deptId);
// 	}

	@Override
	public Object[] getDepartmentInfo(String deptId) {
		// TODO Auto-generated method stub
		return null;
	}

	//phj：改写
	@Override
	public List getDeptUserList(String deptId) {
		String childId =  rtxManager.listUserOfDept(deptId);
		List deptUserList = new ArrayList();

		if(childId!=null){
			String childDeptIdArray[] = childId.split(",");
			if (childDeptIdArray != null && childDeptIdArray.length > 0) {
				for (String childDeptId : childDeptIdArray) {
					deptUserList.add(childDeptId);
				}
			}
		}else{
			deptUserList=null;
		}
		
		return deptUserList;
	}
	
	
//	@Override
//	public List getDeptUserList(String deptId) {
//		
//		return   rtxManager.listUserOfDept(deptId);  
//	}

	@Override
	public String getUserInfo(String userLoginName) {
		return rtxManager.getUserName(userLoginName);
	}

	@Override
	public Object[] imTreeInfo(String deptId) {
		if (deptId == null || deptId.length() == 0)
			deptId = "0";
		List<Object[]> deptIdList = new ArrayList<Object[]>();
//		List<String> childDeptIdList = rtxManager.listChildDeptIdOfDept(deptId);
		
		
		String childId =  rtxManager.listChildDeptIdOfDept(deptId);
		String childDeptIdArray[] = childId.split(",");
		List<String> childDeptIdList = new ArrayList<String>();
		if (childDeptIdArray != null && childDeptIdArray.length > 0) {
			for (String childDeptId : childDeptIdArray) {
				childDeptIdList.add(childDeptId);
			}
		}
		
		this.logger.info("子部门数："
				+ (childDeptIdList == null ? 0 : childDeptIdList.size()));
		String[] hasChild = new String[childDeptIdList.size()];
		if (childDeptIdList != null) {
			// 子部门作排序
			Collections.sort(childDeptIdList);
			String deptName = "";
			// 列出所有的子部门
			for (String id : childDeptIdList) {
				deptName = rtxManager.getDeptName(id);
				deptIdList.add(new Object[] { id, deptName, deptId });
			}
			// 查询该部门的子部门是否有子部门或用户
			for (int i = 0; i < deptIdList.size(); i++) {
				Object[] org = deptIdList.get(i);
				
//				List<String> childDpetIds = rtxManager.listChildDeptIdOfDept(org[0].toString());
				String childIds =  rtxManager.listChildDeptIdOfDept(deptId);
				String childDeptIdArrays[] = childIds.split(",");
				
//				if (childDpetIds != null && !childDpetIds.isEmpty()) {
				if (childDeptIdArrays != null && childDeptIdArrays.length>0) {
					hasChild[i] = "1";// 组织机构下部门
				} else {
					String userList = rtxManager.listUserOfDept(org[0].toString());
					
//					List<String> userList = rtxManager.listUserOfDept(org[0].toString());
 					String userListArray[] = userList.split(",");
//					if (userList != null && !userList.isEmpty()) {
					if (userListArray != null && userListArray.length>0) {
						hasChild[i] = "1";// 组织机构下有人员
					} else {
						hasChild[i] = "0";
					}
				}
			}
		}
		return new Object[] { deptIdList, hasChild };
	}
	
	@Override
	public String getSessionKey(String strUser) {
		String strSessionKey = "";
		//获取腾讯通服务器地址
		
		ToaImConfig config = Cache.get();
		String serverIP = config.getImconfigIp();
		String imconfigPort = config.getImconfigPort();
 		//	url = "http://"+serverIP+":6666/StrongOA5.0/services/rtx2010WebService";
		
		String strURL = "http://"+serverIP+":"+imconfigPort+"/GetSession.cgi?receiver=" + strUser;

		try {
			java.net.URL url = new URL(strURL);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			strSessionKey = reader.readLine();

		} catch (Exception e) {
			System.out.println("系统出错" + e);
		}

		return strSessionKey;
	}

	@Override
	public String imUserInfo(String deptId, Object... objects) {
		System.out.println("加载该部门的子部门或员工:" + deptId);
		StringBuffer str = new StringBuffer("");
		List<String> userList = this.getDeptUserList(deptId);
		// 将人员挂接到此树下
		if (userList != null && !userList.isEmpty()) {
			for (String userCode : userList) {
				str
						.append("<li id="
								+ userCode
								+ "><span>"
								+ rtxManager.getUserName(userCode)
								+ "</span><label style='display:none;'>person</label></li>\n");
			}
		}
		List<String> deptList = this.getChildDeptList(deptId);
//		if (deptList != null && !deptList.isEmpty()) {//如果还有子部门
			if (  deptList.size()!=0) {//如果还有子部门
			int i = 0;
			this.logger.info("该部门所有的子部门ID（未排序前）：" + deptList.toString());
			// 子部门作排序
			//Collections.sort(deptList);
			Collections.sort(deptList, new Comparator() {
				public int compare(Object id1, Object id2) {
					if (Integer.parseInt(String.valueOf(id1)) > Integer
							.parseInt(id2.toString())) {
						return 1;
					} else if (Integer.parseInt(String.valueOf(id1)) < Integer
							.parseInt(id2.toString())) {
						return -1;
					}
					return 0;
				}

			});
			this.logger.info("该部门所有的子部门ID（排序后）：" + deptList.toString());
			for (String deptInfoId : deptList) {
				this.logger.info("查询该部门是否有子部门或用户：" + deptInfoId);
				// 如果当前部门下无人员
				userList = this.getDeptUserList(deptInfoId);
				if (userList == null || userList.isEmpty()) { // 无子节点
					str
							.append("<li id="
									+ deptInfoId
									+ " leafChange='folder-org-leaf'><span>"
									+ rtxManager.getDeptName(deptInfoId)
									+ "</span><label style='display:none;'>org</label></li>\n");
				} else {// 此部门下还存在人员或子部门
					str.append("<li id=" + deptInfoId + " ><span>"
							+ rtxManager.getDeptName(deptInfoId) + "</span>\n");
					str.append("<ul class=ajax>\n");
					str.append("<li id="
							+ deptInfoId
							+ i
							+ ">{url:"
							+ ServletActionContext.getRequest()
									.getContextPath()
							+ "/im/iM!synajaxtree.action?deptId=" + deptInfoId
							+ "}</li>\n");
					str.append("</ul>\n");
					str.append("</li>\n");
				}
				i++;
			}
		}
		return str.toString();
	}

	@Override
	public boolean isUserExist(String userLoginName) {
		return rtxManager.queryUserIsExists(userLoginName) == 0 ? true : false;
	}
	
	
	public int getUserStatus(String userLoginName) {
		return rtxManager.queryUserStatus(userLoginName);
	}
	
	
	

	@Override
	/***************************************************************************
	 * 需要安装RTX SDK程序
	 */
	public String oa2im() {
		this.logger.info("开始同步部门信息..................");
		// 列出所有的部门
		List<Organization> organizationList = orgManager.getAllDeparments();
		if (organizationList != null && organizationList.size() > 0) {
			// 删除所有部门
			rtxManager.deleteAlldDept();
			// 排序
			AddressOrgManager.CompatorOrg compator = orgManager.new CompatorOrg();
			Collections.sort(organizationList, compator);
			// 对应的RTXID
			Map<String, String> orgIdMap = new HashMap<String, String>();
			int deptId = 1;
			int pDeptId = 0;
			for (Organization organization : organizationList) {
				orgIdMap.put(organization.getOrgId(), String.valueOf(deptId));
				this.logger.info("部门名称:" + organization.getOrgName());
				if (!orgIdMap.containsKey(organization.getOrgParentId())) {
					pDeptId = 0;
				} else {
					pDeptId = Integer.parseInt(orgIdMap.get(organization
							.getOrgParentId()));
				}
				// 同步部门信息
				int isSuccess = rtxManager.addDept(String.valueOf(deptId), "",
						organization.getOrgName(), String.valueOf(pDeptId));
				if (isSuccess == 0) {
					// 查询并同步用户信息
					List<User> userList = userService
							.getUsersByOrgID(organization.getOrgId());
					if (userList != null) {
						for (User user : userList) {
							if ("1".equals(user.getUserIsdel())) {
								// 删除OA系统中已删除的RTX用户
								rtxManager.deleteUser(user.getUserName());
								continue;
							}
							this.logger.info("同步用户：" + user.getUserName());
							if (rtxManager.queryUserIsExists(user
									.getUserLoginname()) != 0) {
								// 新增用户信息
								int isOK = rtxManager.addUser(user.getUserLoginname(), String.valueOf(deptId), user.getUserName(), "");
								
								//同步用户的电话号码
 								//int isUpdateOk = rtxManager.updateUser(user.getUserLoginname(), user.getUserLoginname(), user.getUserName(), "", "", cellPhone, "");
								int isUpdateOk = rtxManager.updateUser(user.getUserLoginname(), String.valueOf(deptId), user.getUserName(),user.getUserEmail(), "", user.getUserTel(), "");
								
								
								this.logger.info("新增用户["
										+ user.getUserLoginname() + "]是否成功:"
										+ isOK+"; 设置手机号码是否成功"+isUpdateOk);
							} else {
								// 修改用户信息
								int isOK = rtxManager.updateUser(user
										.getUserLoginname(), String
										.valueOf(deptId), user.getUserName(),
										user.getUserEmail(), "", user
												.getUserTel(), "");
								this.logger.info("修改用户["
										+ user.getUserLoginname() + "]是否成功:"
										+ isOK);
							}
						}
					}
				}
				this.logger.info("是否成功:" + isSuccess);
				deptId++;
			}
		}
		this.logger.info("同步数据完成");
		return null;
	}

	@Override
	public String sendMessage(String sender, String receiver, String message,
			Task task) {
		System.out.println("-->发送RTX消息:" + message);
		String senderName = userService.getUserInfoByLoginName(sender).getUserName();
		try {
			rtxManager.sendRtxMsg(receiver, message, "来自OA‘"+senderName+"’的消息", null);
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendNotify(String title, String message, String receiver,
			Object... objects) {
		try {
			rtxManager.sendRtxMsg(receiver, message, title, null);
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
