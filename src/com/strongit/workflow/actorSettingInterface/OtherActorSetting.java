/**
 * 人员选择配置信息其它相关信息解析类
 * @author 喻斌
 * @company Strongit Ltd. (c) copyright
 * @date Jun 17, 2010 2:34:24 AM
 * @version 1.0
 * @classpath com.strongit.workflow.actorSettingInterface.OtherActorSetting
 */
package com.strongit.workflow.actorSettingInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.workflowInterface.ITaskService;

@Service
@Transactional(readOnly = true)
public class OtherActorSetting extends AbstractActorSetting {

	private ITaskService taskService;

	@Autowired
	IUserService userService; // 注入统一用户接口

	@Autowired
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
	}

	public ITaskService getTaskService() {
		return this.taskService;
	}

	@Autowired
	IWorkflowService workflowService;

	// protected String prefix = "s";

	/**
	 * 根据传入的处理人信息和任务实例Id解析出对应的人员信息集合
	 * 
	 * @author 喻斌
	 * @date Jun 17, 2010 2:17:12 AM
	 * @param actorInfos
	 *            -处理人信息集
	 * @param taskId
	 *            -任务实例Id，为“0”则表示流程尚未启动
	 * @return Map<String, Object[]>
	 *         人员信息集合，其中String为人员Id，Object[]数据格式为{(0)人员Id, (1)人员名称,
	 *         (2)组织机构Id,(3)人员排序号}
	 * @throws WorkflowException
	 */
	@Override
	public Map<String, Object[]> parseActors(Collection<String> actorInfos,
			String taskId) throws WorkflowException {
		super.prefix = "s";
		Map<String, Object[]> actorMap = new HashMap<String, Object[]>(0);
		if (actorInfos != null && !actorInfos.isEmpty()) {
			String oriUserId = null;// 发起人
			Set<String> currentUsers = null;// 当前处理人
			Set<String> currentDeparts = null;// 当前处理人部门
			List departList = null;// 发起人部门列表
			String userName = null;// 人员名称
			if (taskId == null || "0".equals(taskId)) {
				oriUserId = getUupInterface().getCurrentUserInfo().getUserId();
			}
			String[] st;
			String userId;
			for (String actorInfo : actorInfos) {
				if (this.match(actorInfo)) {
					st = actorInfo.substring(1).split(",")[0].split("\\$");
					userId = st[0];
					if ("0".equals(userId)) {// 发起人

						if (oriUserId == null) {
							oriUserId = this.getTaskService()
									.getProcessStartorByTaskId(taskId);
							if (oriUserId == null) {// 2010-03-01：若流程实例启动但尚未指定发起人(目前主要是子流程中存在),则发起人取当前登录用户
								oriUserId = getUupInterface()
										.getCurrentUserInfo().getUserId();
							}
						}
						if (departList == null) {
							departList = getUupInterface().getDepartmentByUser(
									oriUserId);
						}
						String departId;
						User oriUser = userService
								.getUserInfoByUserId(oriUserId);
						userName = oriUser.getUserName();
						if (userName != null) {
							for (int k = 0; k < departList.size(); k++) {
								departId = String.valueOf(departList.get(k));
								actorMap.put(oriUserId, new String[] {
										oriUserId, userName, departId,
										oriUser.getUserSequence().toString() });
							}
						}
					} else if ("1".equals(userId)) {// 发起人部门负责人
						/*
						 * if (oriUserId == null) { oriUserId =
						 * this.getTaskService()
						 * .getProcessStartorByTaskId(taskId); } if(oriUserId ==
						 * null){// added by dengzc 2010年7月29日13:26:55 continue; }
						 * if (departList == null) { departList =
						 * getUupInterface().getDepartmentByUser( oriUserId); }
						 * String departId; for (int k = 0; k <
						 * departList.size(); k++) { departId =
						 * String.valueOf(departList.get(k)); userId =
						 * getUupInterface().getDepartmentManager( departId); if
						 * (userId != null && !"".equals(userId)) { userName =
						 * getUupInterface().getUsernameById( userId); if
						 * (userName != null) { actorMap.put(userId, new
						 * String[] { userId, userName, departId }); } } }
						 */
						// 改为支持多个负责人的情况 added by dengzc 2011年1月7日11:39:09
						if (oriUserId == null) {
							oriUserId = this.getTaskService()
									.getProcessStartorByTaskId(taskId);
						}
						if (oriUserId == null) {// added by dengzc
							// 2010年7月29日13:26:55
							// continue;
							oriUserId = getUupInterface().getCurrentUserInfo()
									.getUserId();
						}

						if (departList == null) {
							departList = getUupInterface().getDepartmentByUser(
									oriUserId);
						}
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(oriUserId);
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int f=0;f<orgidsss.length;f++){
							String departId=orgidsss[f];
							userId = getUupInterface().getDepartmentManager(
									departId);
							if (userId != null && !"".equals(userId)) {// 这里可能是多个人员
								String[] userIds = userId.split(",");
								for (int i = 0; i < userIds.length; i++) {
									String uid = userIds[i];
									if (uid.length() > 32) {
										uid = uid.substring(1);
									}
									User user = userService
											.getUserInfoByUserId(uid);
									if (user.getUserId() != null) {
										actorMap.put(user.getUserId(),
												new String[] {
														user.getUserId(),
														user.getUserName(),
														user.getOrgId(),
														user.getUserSequence()
																.toString() });
									}
								}
							}
						}
					} else if ("2".equals(userId)) {// 发起人上级部门负责人
						/*
						 * if (oriUserId == null) { oriUserId =
						 * this.getTaskService()
						 * .getProcessStartorByTaskId(taskId); } if(oriUserId ==
						 * null){// added by dengzc 2010年7月29日13:26:55 continue; }
						 * if (departList == null) { departList =
						 * getUupInterface().getDepartmentByUser( oriUserId); }
						 * String departId; for (int k = 0; k <
						 * departList.size(); k++) { departId =
						 * getUupInterface().getParentDepart(
						 * String.valueOf(departList.get(k))); userId =
						 * getUupInterface().getDepartmentManager( departId); if
						 * (userId != null && !"".equals(userId)) { userName =
						 * getUupInterface().getUsernameById( userId); if
						 * (userName != null) { actorMap.put(userId, new
						 * String[] { userId, userName, departId }); } } }
						 */
						// 改为支持多个负责人的情况 added by dengzc 2011年1月7日11:39:09
						if (oriUserId == null) {
							oriUserId = this.getTaskService()
									.getProcessStartorByTaskId(taskId);
						}
						if (oriUserId == null) {// added by dengzc
							// 2010年7月29日13:26:55
							// continue;
							oriUserId = getUupInterface().getCurrentUserInfo()
									.getUserId();
						}
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(oriUserId);
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int f=0;f<orgidsss.length;f++){
							String departId=orgidsss[f];
							if(departId!=null&&!"".equals(departId)){
								TUumsBaseOrg org = userService.getParentOrgByOrgId(departId);
								String orgManager = org.getOrgManager();
								if (orgManager != null && !"".equals(orgManager)) {
									String[] userIds = orgManager.split(",");
									for (int i = 0; i < userIds.length; i++) {
										String uid = userIds[i];
										if (uid.length() > 32) {
											uid = uid.substring(1);
										}
										User user = userService
										.getUserInfoByUserId(uid);
										if (user.getUserId() != null) {
											actorMap.put(user.getUserId(),
													new String[] {
												user.getUserId(),
												user.getUserName(),
												user.getOrgId(),
												user.getUserSequence()
												.toString() });
										}
									}
								}
							}
						}
//						TUumsBaseOrg org = userService
//								.getSupOrgByUserIdByHa(oriUserId);// 得到用户所在分级授权组织机构

					} else if ("3".equals(userId)) {// 上步处理人
						if (currentUsers == null) {
							currentUsers = this.getTaskService()
									.getCurrentHandlersByTaskId(taskId);
						}
						Iterator<String> iterator = currentUsers.iterator();
						while (iterator.hasNext()) {
							User user = userService
									.getUserInfoByUserId(iterator.next());
							if (user != null) {
								actorMap.put(user.getUserId(), new String[] {
										user.getUserId(), user.getUserName(),
										user.getOrgId(),
										user.getUserSequence().toString() });// 若存在一个人员属于多个部门,则只取其中一个部门的信息数据
							}
						}
					} else if ("4".equals(userId)) {// 上步处理人组织机构负责人
						if (currentUsers == null) {
							currentUsers = this.getTaskService()
									.getCurrentHandlersByTaskId(taskId);
						}
						if (currentDeparts == null) {// 上步处理人所属组织机构
							currentDeparts = new HashSet<String>();
							Iterator<String> iterator = currentUsers.iterator();
							while (iterator.hasNext()) {
								TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(iterator.next());
								String[] orgidsss = uumsuer.getOrgIds().split(",");
								for(int f=0;f<orgidsss.length;f++){
									String departId=orgidsss[f];
									if(departId!=null&&!"".equals(departId)){
										userId = getUupInterface().getDepartmentManager(
												departId);
										/*
										 * if (userId != null && !"".equals(userId)) { users =
										 * getUupInterface().getUserInfoById( userId); if
										 * (users != null && !users.isEmpty()) {
										 * actorMap.put(users.get(0)[0], users.get(0));//
										 * 若存在一个人员属于多个部门,则只取其中一个部门的信息数据 } }
										 */
										if(userId!=null&&!"".equals(userId)){
											String[] userIds = userId.split(",");
											for (int i = 0; i < userIds.length; i++) {
												String uid = userIds[i];
												if (uid.length() > 32) {
													uid = uid.substring(1);
												}
												User user = userService
												.getUserInfoByUserId(uid);
												if (user.getUserId() != null) {
													actorMap.put(user.getUserId(),
															new String[] {
														user.getUserId(),
														user.getUserName(),
														user.getOrgId(),
														user.getUserSequence()
														.toString() });
												}
											}
										}
									}
								}
							}
						}
						
					} else if ("5".equals(userId)) {// 上步处理人上级组织机构负责人
						if (currentUsers == null) {
							currentUsers = this.getTaskService()
									.getCurrentHandlersByTaskId(taskId);
						}

						if (currentDeparts == null) {// 上步处理人所属组织机构
							currentDeparts = new HashSet<String>();
							Iterator<String> iterator = currentUsers.iterator();
							while (iterator.hasNext()) {
								TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(iterator.next());
								String[] orgidsss = uumsuer.getOrgIds().split(",");
								for(int f=0;f<orgidsss.length;f++){
									String departId=orgidsss[f];
									if(departId!=null&&!"".equals(departId)){
										/*
										 * String parentOrgId = getUupInterface()
										 * .getParentDepart(iterator.next());// 上级组织机构Id信息
										 */TUumsBaseOrg org = userService
												.getSupOrgByOrgIdByHa(departId);
										if (org != null) {
											String parentOrgId = org.getOrgId();
											if (parentOrgId != null
													&& !"".equals(parentOrgId)) {
												userId = getUupInterface()
														.getDepartmentManager(parentOrgId);// 上级组织机构负责人Id信息
												if (userId != null && !"".equals(userId)) {
													/*
													 * users =
													 * getUupInterface().getUserInfoById(
													 * userId); if (users != null &&
													 * !users.isEmpty()) {
													 * actorMap.put(users.get(0)[0], users
													 * .get(0));//
													 * 若存在一个人员属于多个部门,则只取其中一个部门的信息数据 }
													 */
													String[] userIds = userId.split(",");
													for (int i = 0; i < userIds.length; i++) {
														String uid = userIds[i];
														if (uid.length() > 32) {
															uid = uid.substring(1);
														}
														User user = userService
																.getUserInfoByUserId(uid);
														if (user.getUserId() != null) {
															actorMap
																	.put(
																			user
																					.getUserId(),
																			new String[] {
																					user
																							.getUserId(),
																					user
																							.getUserName(),
																					user
																							.getOrgId(),
																					user
																							.getUserSequence()
																							.toString() });
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} else if ("6".equals(userId)) {// 当前用户所在机构人员
						User user = userService.getCurrentUser();
						//String orgId = user.getOrgId();
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(user.getUserId());
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int k=0;k<orgidsss.length;k++){
							String orgId = orgidsss[k];
							if (orgId != null&&!"".equals(orgId)) {
								List<User> userList = userService
								.getAllUserInfoByHa(orgId);// 当前用户所在分级授权机构人员
								for (int i = 0; i < userList.size(); i++) {
									User curUser = userList.get(i);
									actorMap.put(curUser.getUserId(), new String[] {
										curUser.getUserId(),
										curUser.getUserName(),
										curUser.getOrgId(),
										curUser.getUserSequence().toString() });
								}
							}
						}

					} else if ("7".equals(userId)) {// 当前用户所在部门人员
						User user = userService.getCurrentUser();
						//String orgId = user.getOrgId();
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(user.getUserId());
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int k=0;k<orgidsss.length;k++){
							String orgId = orgidsss[k];
							if (orgId != null&&!"".equals(orgId)) {
								List<User> userList = userService
								.getUsersByOrgID(orgId);
								for (int i = 0; i < userList.size(); i++) {
									User curUser = userList.get(i);
									actorMap.put(curUser.getUserId(), new String[] {
										curUser.getUserId(),
										curUser.getUserName(),
										curUser.getOrgId(),
										curUser.getUserSequence().toString() });
								}
							}
						}	

					} else if ("8".equals(userId)) {// 当前用户所在分级授权机构人员（不含自己）
						User user = userService.getCurrentUser();
						//String orgId = user.getOrgId();
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(user.getUserId());
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int k=0;k<orgidsss.length;k++){
							String orgId = orgidsss[k];
							if (orgId != null&&!"".equals(orgId)) {
								List<User> userList = userService
								.getAllUserInfoByHa(orgId);// 当前用户所在分级授权机构人员
								for (int i = 0; i < userList.size(); i++) {
									User curUser = userList.get(i);
									if (!curUser.getUserId().equals(
											user.getUserId())) {
										actorMap.put(curUser.getUserId(),
												new String[] {
											curUser.getUserId(),
											curUser.getUserName(),
											curUser.getOrgId(),
											curUser.getUserSequence()
											.toString() });
									}
								}
							}
						}

					} else if ("9".equals(userId)) {// 当前用户所在部门人员（不含自己）
						User user = userService.getCurrentUser();
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(user.getUserId());
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int k=0;k<orgidsss.length;k++){
							String orgId = orgidsss[k];
							if (orgId != null&&!"".equals(orgId)) {
								List<User> userList = userService
								.getUsersByOrgID(orgId);
								for (int i = 0; i < userList.size(); i++) {
									User curUser = userList.get(i);
									if (!curUser.getUserId().equals(
											user.getUserId())) {
										actorMap.put(curUser.getUserId(),
												new String[] {
											curUser.getUserId(),
											curUser.getUserName(),
											curUser.getOrgId(),
											curUser.getUserSequence()
											.toString() });
									}
								}
							}
						}

					} else if ("10".equals(userId)) {// 如当前用户所在机构为二级机构，则显示该二级机构的所有人员，如当前用户所在机构为顶级机构，则显示当前用户所在部门人员。
						User user = userService.getCurrentUser();
						String suporgcode = user.getSupOrgCode();
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(user.getUserId());
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int k=0;k<orgidsss.length;k++){
							String orgId = orgidsss[k];
							if (orgId != null&&!"".equals(orgId)) {
								List<User> userList;
								TUumsBaseOrg  currOrg = userService.getOrgInfoByOrgId(orgId);
								String  isorg = currOrg.getIsOrg();
								if(isorg!=null&&!"1".equals(isorg)&&("001").equals(suporgcode)){
									userList = userService.getUsersByOrgID(orgId);
								}else{
									userList = userService.getAllUserInfoByHa(orgId);
								}
								if (userList == null) {
									userList = userService.getUsersByOrgID(orgId);
									for (int i = 0; i < userList.size(); i++) {
										User curUser = userList.get(i);
										actorMap.put(curUser.getUserId(),
												new String[] {
														curUser.getUserId(),
														curUser.getUserName(),
														curUser.getOrgId(),
														curUser.getUserSequence()
																.toString() });
									}
								}
								for (int i = 0; i < userList.size(); i++) {
									User curUser = userList.get(i);
									actorMap.put(curUser.getUserId(), new String[] {
											curUser.getUserId(),
											curUser.getUserName(),
											curUser.getOrgId(),
											curUser.getUserSequence().toString() });
								}
							}
						}
						
					} else if ("11".equals(userId)) {// 显示主办人员信息 yanjian
						if (oriUserId == null) {
							oriUserId = workflowService
									.getMainActorIdByTaskInstanceId(taskId);
						}
						if (departList == null) {
							departList = getUupInterface().getDepartmentByUser(
									oriUserId);
						}
						String departId;
						User oriUser = userService
								.getUserInfoByUserId(oriUserId);
						userName = oriUser.getUserName();

						if (userName != null) {
							for (int k = 0; k < departList.size(); k++) {
								departId = String.valueOf(departList.get(k));
								actorMap.put(oriUserId, new String[] {
										oriUserId, userName, departId,
										oriUser.getUserSequence().toString() });
							}
						}
					}else if("12".equals(userId)){//分管领导对口秘书（财政厅OA）
						String curUserId = userService.getCurrentUser().getUserId();
						//Organization rest4Dept = userService.getUserDepartmentByUserId(curUserId);
						TUumsBaseUser uumsuer = userService.getUumsUserInfoByUserId(curUserId);
						String[] orgidsss = uumsuer.getOrgIds().split(",");
						for(int k=0;k<orgidsss.length;k++){
							String orgId = orgidsss[k];
							Organization rest4Dept = userService.getDepartmentByOrgId(orgId);
							//分管领导
							String rest4 = rest4Dept.getRest2();
							if(rest4 != null && !"".equals(rest4)){
								String useridsDept[] = rest4.split(",");
								for(int i=0;i<useridsDept.length;i++){
									oriUserId = useridsDept[i].substring(1);
									if(oriUserId!=null&&!oriUserId.equals("")){
										User oriUser = userService.getUserInfoByUserId(oriUserId);
										userName = oriUser.getUserName();
										if (userName != null) {
											actorMap.put(oriUserId, new String[] {
													oriUserId, userName, rest4Dept.getOrgId(),
													oriUser.getUserSequence().toString() });
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return actorMap;
	}
}