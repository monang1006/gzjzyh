package com.strongit.workflow.workflowDesign.action.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.workflow.manager.AdapterBaseWorkflowManager;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.senddoc.bo.UserBeanTemp;
import com.strongit.oa.util.DateCountUtil;
import com.strongit.oa.util.StringUtil;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Jun 6, 2012 9:27:16 AM
 * @version      1.0.0.0
 * @classpath    com.strongit.workflow.workflowDesign.action.util.ProcessInstanceDataUtil
 */
public class ProcessInstanceDataUtil {

	private List<String> itemsList = new ArrayList<String>();

	
	@Autowired
	IUserService userService;// 统一用户服务

	private String type;

	public ProcessInstanceDataUtil(String type) {
		this.type = type;
	}

	public List<String> getToSelectItems() {
		if ("org".equals(type)) {
			itemsList.add("businessName");
			itemsList.add("startUserName");
			itemsList.add("processInstanceId");
			itemsList.add("processStartDate");
			itemsList.add("processEndDate");
			itemsList.add("processTimeout");
			itemsList.add("processName");
			itemsList.add("startUserId");
			itemsList.add("mainActorId");
			itemsList.add("workflowAliaName");
		} else if ("dept".equals(type)) {
			itemsList.add("businessName");
			itemsList.add("startUserName");
			itemsList.add("processInstanceId");
			itemsList.add("processStartDate");
			itemsList.add("processEndDate");
			itemsList.add("processTimeout");
			itemsList.add("processName");
			itemsList.add("startUserId");
			itemsList.add("mainActorId");
			itemsList.add("workflowAliaName");
		} else if ("user".equals(type)) {
			itemsList.add("businessName");
			itemsList.add("startUserName");
			itemsList.add("processInstanceId");
			itemsList.add("processStartDate");
			itemsList.add("processEndDate");
			itemsList.add("processTimeout");
			itemsList.add("processName");
			itemsList.add("startUserId");
		} else {
			itemsList.add("businessName");
			itemsList.add("startUserName");
			itemsList.add("processInstanceId");
			itemsList.add("processStartDate");
			itemsList.add("processEndDate");
			itemsList.add("processTimeout");
			itemsList.add("processName");
			itemsList.add("startUserId");
		}
		return itemsList;
	}

	public String getSql(String userId, String orgId, String startUserName) {
		String sql = null;
		if ("org".equals(type)) {
			sql = "select user.userId from TUumsBaseOrg org,TUumsBaseUser user where org.orgId = "
					+ "user.orgId and org.rest2 like '%" + userId + "%'";
			if (startUserName != null && !"".equals(startUserName) &&!"null".equals(startUserName)) {
				sql += " and user.userName like '%" + startUserName + "%' ";
			}
			if (orgId != null && !"".equals(orgId)) {// 查询具体处室的数据
				String[] orgIdarr = orgId.split(",");
				sql += " and (";
				for (String org : orgIdarr) {
					sql += " org.orgId ='" + org + "' or";
				}
				sql = sql.substring(0, sql.length() - 2);
				sql += " ) ";
			}
		} else if ("dept".equals(type)) {
			sql = "select user.userId from TUumsBaseOrg org,TUumsBaseUser user where org.orgId = "
				+ "user.orgId and org.rest2 like '%" + userId + "%'";
			if (startUserName != null && !"".equals(startUserName) &&!"null".equals(startUserName)) {
				sql += " and user.userName like '%" + startUserName + "%' ";
			}
		} else if ("user".equals(type)) {
			sql = "'" + userId + "'";
		} else {
			sql = "select baseUser.userId from TUumsBaseUser baseUser where baseUser.orgId='"
					+ orgId + "'";
		}
		return sql;
	}

	@SuppressWarnings("unchecked")
	public Page<TaskBean> getResult(Page page,AdapterBaseWorkflowManager adapterBaseWorkflowManager)
			throws SystemException {
		List result = page.getResult();
		List<TaskBean> list = getResult(result, adapterBaseWorkflowManager);
		if(list == null || list.isEmpty()){
			page.setTotalCount(0);
		}
		page.setResult(list);
		return page;
	}

	@SuppressWarnings("unchecked")
	public List<TaskBean> getResult(List objlist,AdapterBaseWorkflowManager adapterBaseWorkflowManager)
			throws SystemException {
		List<TaskBean> list = new ArrayList<TaskBean>();
		if (objlist != null && !objlist.isEmpty()) {
			Map<String, Object[]> map = adapterBaseWorkflowManager.getCustomUserService().getManagerUserInfoForMap(adapterBaseWorkflowManager.getUserService().getCurrentUser().getUserId());
			Map<String, UserBeanTemp> AllUserIdMapUserBeanTem = new HashMap<String, UserBeanTemp>();
				itemsList = getToSelectItems();
				for (int i = 0; i < objlist.size(); i++) {
					Object[] objs = (Object[]) objlist.get(i);
					Object[] objects = null;
					TaskBean bean = new TaskBean();
					if (itemsList.indexOf("processInstanceId") != -1) {
						bean.setInstanceId(objs[itemsList
						                        .indexOf("processInstanceId")].toString()); // 流程实例id
					} else {
						throw new SystemException("必须查询processInstanceId参数!");
					}
					if (itemsList.indexOf("businessName") != -1) {
						bean
						.setBusinessName(StringUtil.castString(objs[itemsList
												                      .indexOf("businessName")])); // 业务标题
					}
					if (itemsList.indexOf("processName") != -1) {
						bean.setWorkflowName(objs[itemsList.indexOf("processName")]
						                          .toString()); // 流程名称
					}
					if (itemsList.indexOf("processStartDate") != -1) {
						bean.setWorkflowStartDate((Date) objs[itemsList
						                                      .indexOf("processStartDate")]); // 流程启动日期
					}
					if (itemsList.indexOf("processEndDate") != -1) {
						bean.setWorkflowEndDate((Date) objs[itemsList
						                                    .indexOf("processEndDate")]); // 流程结束日期
					}
					if (itemsList.indexOf("startUserName") != -1) {
						bean.setStartUserName((String) objs[itemsList
						                                    .indexOf("startUserName")]); // 发起人名称
					}
					if (itemsList.indexOf("mainActorId") != -1) {
						if(map != null){//解决【流程查询与监控】记录不到数据，Bug序号： 0000002520  yanjian 2012-04-25 14:50
						    objects = map.get((String) objs[itemsList
						                                    .indexOf("mainActorId")]);
						    if(objects != null){
								bean.setStartUserName(StringUtil
										.castString(objects[1] == null ? ""
												: objects[1])); // 发起人名称
						    }
						}else{
							System.out.println("########################################该用户没有配置权限！");
						}
					}
					if (itemsList.indexOf("workflowAliaName") != -1) {
					bean
							.setWorkflowAliaName(StringUtil
									.castString(objs[itemsList
											.indexOf("workflowAliaName")] == null ? objs[itemsList
											.indexOf("processName")].toString()
											: objs[itemsList
													.indexOf("workflowAliaName")]));
					bean.setWorkflowName(bean.getWorkflowAliaName());
				}
					if (itemsList.indexOf("processTimeout") != -1) {
						bean.setWorkflowIsTimeOut((String) objs[itemsList
						                                        .indexOf("processTimeout")]); // 流程是否超期
					}
					// 部门或机构下的流程实例查询主办部门名称
					if ("org".equals(type) || "dept".equals(type)) {
						if (objects != null) {
							bean.setStartUserDeptName(StringUtil.castString(objects[3]));
						}
					}
					// 处理流程当前处理人情况
					if (bean.getWorkflowEndDate() != null) {
						bean.setNodeName("办毕");
					} else {
						StringBuilder cruuentUser = new StringBuilder();
						List[] listTemp = adapterBaseWorkflowManager.getSendDocUploadManager()
						.getUserBeanTempArrayOfProcessStatusByPiId(
								bean.getInstanceId(),
								AllUserIdMapUserBeanTem);
					    for(int index = 0;index<listTemp[0].size(); index++){
						   if(" ".equals(listTemp[1].get(index).toString())){
							   cruuentUser.append(","+listTemp[0].get(index).toString());
						   }else{
							   cruuentUser.append(","+listTemp[0].get(index).toString()+"["+listTemp[1].get(index).toString()+"]");
						   }
					    }
					    cruuentUser.deleteCharAt(0);
					    bean.setActorName(cruuentUser.toString());
						Object[] returnObjs =adapterBaseWorkflowManager.getWorkflowService()
						.getProcessStatusByPiId(bean.getInstanceId());// 得到此流程实例下的运行情况
						Collection col = (Collection) returnObjs[6];// 处理任务信息
						if (col != null && !col.isEmpty()) {
							//StringBuilder strUserName = new StringBuilder();// 人员姓名
							StringBuilder strNodeName = new StringBuilder();// 节点名称
							StringBuilder nodeEnterTime = new StringBuilder();// 节点进入时间
							for (Iterator it = col.iterator(); it.hasNext();) {
								Object[] itObjs = (Object[]) it.next();
								//String userId = (String) itObjs[3];
								String taskFlag = (String) itObjs[0];
								Date nodeEnter = (Date) itObjs[2];
								Date now = new Date();
								long day = DateCountUtil.getDistDates(nodeEnter,
										now);
								//StringBuilder userName = new StringBuilder();
//								if (userId != null && !"".equals(userId)) {
//									String[] userIds = userId.split(",");
//									Organization organization;
//									for (String id : userIds) {
//										organization = userService
//										.getUserDepartmentByUserId(id);
//										userName.append(userService
//												.getUserNameByUserId(id));
//										if (organization != null) {
//											userName.append("(").append(
//													organization.getOrgName())
//													.append(")");
//										}
//										userName.append(",");
//									}
//									// userName.deleteCharAt(userName.length() - 1);
//								}
								/*
								 * if (userName.length() > 0) {
								 * strUserName.append("[").append(userName)
								 * .append("]"); }
								 */
								//strUserName.append(userName);
								//获取节点名称--状态                 任务停留天数--流程状态描述信息
								if ("subProcess".equals(taskFlag)) {// 子流程节点
									strNodeName.append("[").append(
											(String) itObjs[1]).append("]");
								} else {// 任务节点
									strNodeName.append("[").append(
											(String) itObjs[1]).append("]");
								}
								if (nodeEnterTime.length() == 0) {
									if (day == 0) {
										nodeEnterTime.append("正在办理中");
									} else {
										nodeEnterTime.append(day).append("天未办理");
									}
								}
							}
							bean.setWorkflowStatusDesc(nodeEnterTime.toString()); // 流程状态描述信息
							bean.setNodeName(strNodeName.toString()); // 流程当前所处节点名称
//							if (strUserName.length() > 0) {
//								strUserName.deleteCharAt(strUserName.length() - 1);
//							}
//							bean.setActorName(strUserName.toString()); // 流程当前所处人员名称信息
						} //else {
//							bean.setNodeName("办毕");
//						}
					}
					list.add(bean);
			}
		}
		return list;
	}

	public List<String> getItemsList() {
		return itemsList;
	}

	public void setType(String type) {
		this.type = type;
	}
}
