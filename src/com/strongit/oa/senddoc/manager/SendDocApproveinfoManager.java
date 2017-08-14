package com.strongit.oa.senddoc.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jbpm.taskmgmt.exe.TaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.approveinfo.ApproveinfoManager;
import com.strongit.oa.bo.ToaApproveinfo;
import com.strongit.oa.bo.ToaDefinitionPlugin;
import com.strongit.oa.common.custom.user.ICustomUserService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.IWorkService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.comparator.BaseComparator;
import com.strongit.oa.common.workflow.comparator.DateComparator;
import com.strongit.oa.common.workflow.comparator.PersonComparator;
import com.strongit.oa.common.workflow.comparator.groupby.GroupByAfterSorted;
import com.strongit.oa.common.workflow.comparator.groupby.GroupByDepartment;
import com.strongit.oa.common.workflow.comparator.util.SortConst;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.uums.bo.TUumsBaseOrg;
import com.strongit.uums.bo.TUumsBaseUser;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.exception.WorkflowException;
import com.strongmvc.exception.SystemException;

/**
 * Manager 意见域控制
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 12, 2012 2:46:45 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.manager.SendDocApproveinfoManager
 */
@Service
@Transactional
@OALogger
public class SendDocApproveinfoManager {
	@Autowired
	IWorkflowService workflowService;// 工作流服务类
	
	@Autowired
	IWorkService workService;// 工作流服务类

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected DefinitionPluginService definitionPluginService;// 流程定义插件服务类.
	@Autowired
	protected ICustomUserService customUserService;
	@Autowired
	protected INodesettingPluginService nodesettingPluginService;
	@Autowired
	protected IUserService userService;// 统一用户服务
	@Autowired
	protected SendDocAnnalManager annalManager;// 意见管理
	@Autowired
	protected ApproveinfoManager approveinfoManager;// 意见管理

	/**
	 * @author:		luosy 2013-11-29
	 * description:	根据流程实例获取本流程的审批意见/根据流程实例获取本流程的审批意见
	 * modifyer:
	 * description:
	 * @param instanceId
	 *            流程实例id
	 * @return Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,意见记录id
	 *         ,处理人机构名称,最后一个委派/指派人名称,任务处理类型]>>
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getWorkflowApproveinfo(String instanceId,String taskId)
			throws SystemException {
		 Date xxDate=new Date();
		  
			Map<String, List<String[]>> appMap = new HashMap<String, List<String[]>>();
			try {
				List<Object[]> approveinfoList = new ArrayList<Object[]>();
				Set<String> instanceIds = new HashSet<String>();
				instanceIds.add(instanceId);
				if (instanceId != null && !instanceId.equals("")) {
					List<Object[]> parentList = workflowService
							.getMonitorParentInstanceIds(new Long(instanceId));
					 System.out.println((new Date()).getTime()-xxDate.getTime()+"**开始*****1111111*****************************");
						List<Long> insIds=new ArrayList<Long>();
					if (parentList != null && !parentList.isEmpty()) {
						for (Object[] objs : parentList) {
							instanceIds.add(objs[0].toString());
							List<Object[]> childs = workflowService
									.getMonitorChildrenInstanceIds(new Long(objs[0]
											.toString()));
							// System.out.println((new Date()).getTime()-xxDate.getTime()+"*******2222222*****************************");
							if (childs != null && !childs.isEmpty()) {
								for (Object[] child : childs) {
									instanceIds.add(child[0].toString());
								}
							}
						}
				//	List<Object[]> childs =workService.getMonitorChildrenInstanceIds(insIds);
					//	System.out.println((new Date()).getTime()-xxDate.getTime()+"*******22222222*****************************");
					//	if (childs != null && !childs.isEmpty()) {
					//		for (Object[] child : childs) {
					//			instanceIds.add(child[0].toString());
						//	}
					//	}
					} else {
						List<Object[]> childs = workflowService
								.getMonitorChildrenInstanceIds(new Long(instanceId));
						if (childs != null && !childs.isEmpty()) {
							for (Object[] child : childs) {
								instanceIds.add(child[0].toString());
							}
						}
					}
					List<Long> processInstanceIds=new ArrayList<Long>();
					for (String id : instanceIds) {
						processInstanceIds.add(new Long(id));
						//approveinfoList.addAll(workflowService
						//		.getBusiFlagByProcessInstanceId(id));
					}
					approveinfoList.addAll(workService.getBusiFlagByProcessInstanceIds(processInstanceIds));// 根据流程实例IDS批量得到对应业务标识
					 System.out.println((new Date()).getTime()-xxDate.getTime()+"***333333333333*****************************");
				}
				
				String definitionId = workflowService
						.getProcessFileIdByProcessInstanceId(instanceId);
				String sortValue = null;// 排序方式
				if (definitionId != null) {
					List<ToaDefinitionPlugin> plugins = definitionPluginService
							.find(definitionId);
					// System.out.println((new Date()).getTime()-xxDate.getTime()+"*******3333333333333333*****************************");
					if (plugins != null && !plugins.isEmpty()) {
						for (ToaDefinitionPlugin plugin : plugins) {
							if ("plugins_suggestion".equals(plugin
									.getDefinitionPluginName())) {
								sortValue = plugin.getDefinitionPluginValue();// 得到排序方式
								break;
							}
						}
					}
				} else {
					logger.error("未找到流程实例为'" + instanceId + "'的流程。");
				}
				logger.info("流程处理意见排序方式：" + sortValue);
				final Map<String, Long> userMap = new HashMap<String, Long>();
				Map<String, TUumsBaseOrg> orgMap = new HashMap<String, TUumsBaseOrg>();
				
				List<String> userIds=new ArrayList<String>();
			    for (Object[] objs : new ArrayList<Object[]>(approveinfoList)) {
				            userIds.add((String)objs[5]);  	
				      }
				   
				 List<TUumsBaseUser> uList=workService.getUsersByUserIds(userIds);
				 List<TUumsBaseOrg> oList=workService.getOrgInfosByUserIds(userIds);
					//System.out.println("用户数量："+uList.size());
					//System.out.println("机构数量："+oList.size());
			System.out.println((new Date()).getTime()-xxDate.getTime()+" 查询组装******444444444444**********************");
			
			
				for (Object[] objs : new ArrayList<Object[]>(approveinfoList)) {
					  TwfBaseNodesetting nodeSetting = (TwfBaseNodesetting) objs[8];// 得到节点对象
					  
						if(nodeSetting.getNsNodeName().equals("局长")){//修改意见域重名问题
						  System.out.println(nodeSetting.getNsNodeName()+" select t.nsp_pluginclobvalue from T_WF_BASE_NODESETTINGPLUGIN t where t.ns_id ="+nodeSetting.getNsId()+" and t.nsp_pluginname ='plugins_businessFlag' for update");//+":\n"+nodeSetting.getPlugin("plugins_businessFlag"));
						}
					  
			            Object suggestion = objs[4];// 意见内容如果为空 显示名字在表单上
			            String info = nodeSetting.getPlugin("plugins_businessFlag");// 节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
						if (info == null) {
							continue;// 如果意见未绑定控件,则不予处理
						}
//						2013-11-23 1:26:54  Modified by capuchin  --------End-------------
						String showsuggest = nodeSetting
								.getPlugin("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
						if ("1".equals(showsuggest)) {
						   
						} else {
							String backSuggestion = "";
							String lastchar = "";
							if (suggestion != null) {
								backSuggestion = suggestion.toString();
//								System.out.println(backSuggestion);
//								2013-11-23 0:29:06  Modified by capuchin  --------Start----------- 5个空白字符串标识为退回意见 该代码不是本人所写，只是做个标记，饶了我吧！！
								if(StringUtil.isOnlyBlank(backSuggestion)){
		                            approveinfoList.remove(objs);// 删除退回意见
		                            continue;
		                        }
								if (backSuggestion.length() >= 5) {
									lastchar = backSuggestion.substring(backSuggestion
											.length() - 5);
									if ("     ".equals(lastchar)) {
										approveinfoList.remove(objs);// 删除退回意见
										continue;
									}
								}
//								2013-11-23 0:29:11  Modified by capuchin  --------End-------------
							}
						}
						
						String showBlankSuggest = nodeSetting
						.getPlugin("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
						if ("1".equals(showBlankSuggest)) {
						    
						} else {
							String blankSuggestion = "";
							if (suggestion != null) {
								blankSuggestion = suggestion.toString();
								if(StringUtil.isOnlyBlank(blankSuggestion)){
									approveinfoList.remove(objs);// 删除退回意见
									continue;
								}
							}
						}
				       
					if (info != null && info.length() > 0) {
						info = URLDecoder.decode(info, "utf-8");
						info = info.replaceAll("#", ",");
						JSONArray array = JSONArray.fromObject(info);
						JSONObject firstObj = array.getJSONObject(0);
						if (firstObj.containsKey("type")) {// 输入意见字段
							String fieldName = firstObj.getString("fieldName");
							String userId = (String) objs[5];
							String[] objsReturn = null;
							if (userId != null) {	
								String orgId = null;
								TUumsBaseUser user=null;
								if (!userMap.containsKey(userId)) {
									//User user = userService
									//		.getUserInfoByUserId(userId);
									 user=this.getBaseUser(uList, userId);
									userMap.put(userId,user.getUserSequence());
									orgId = user.getOrgId();
										
								}
								
								firstObj.put("taskTime", objs[7]);// 任务处理时间,排序用到
								Object taskTimeJSONObj = firstObj.get("taskTime");
								String time = Long.toString(System
										.currentTimeMillis());
								if (taskTimeJSONObj instanceof JSONObject) {
									JSONObject jsonTaskTime = (JSONObject) taskTimeJSONObj;
									time = jsonTaskTime.getString("time");
								}
								String assignId = null;
								String assignName = null;
								if (objs.length >= 13) {
									assignId = (String) objs[13];// 得到第一个指派/委派人id
									assignName = (String) objs[14];
								}
								TUumsBaseOrg org = null;
								if (orgId == null) {
								   // org = userService
									//	.getUserDepartmentByUserId(userId);
									org=this.getBaseOrg(oList,this.getBaseUser(uList, userId).getOrgId());
								  //  System.out.println((new Date()).getTime()-xxDate.getTime()+"CCCCCCCCCCC查询单位******444444444444**********************");
									orgId = org.getOrgId();
									orgMap.put(orgId, org);
								}
							
								if (!orgMap.containsKey(orgId)) {
									//org = userService.getDepartmentByOrgId(orgId);
									org=this.getBaseOrg(oList, orgId);
								//System.out.println((new Date()).getTime()-xxDate.getTime()+"BBBBBBB查询单个单位******444444444444**********************");
									orgMap.put(orgId, org);
								} else {
									org = orgMap.get(orgId);
								}
							
								String taskStartDate = String.valueOf(objs[0]);// 添加根据任务开始顺序排序方式数据(使用任务id自动增长，作为排序数据)
								if (sortValue.startsWith("department")) {
									objsReturn = new String[] { (String) suggestion,
											userId, time, orgId,
											org.getOrgSequence().toString(),
											org.getOrgName(), (String) objs[6],
											objs[7].toString(), assignId,
											assignName, objs[15].toString(),
											org.getOrgName(), (String) objs[12],
											taskStartDate, (String) objs[10]};// //意见内容,处理人id,处理时间
									// 机构id,机构序号,机构名称,
									// 处理人名称,处理时间,
									// 委托人id,委托人名称，记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务开始时间,任务处理类型
								} else {
									objsReturn = new String[] { (String) suggestion,
											userId, time, (String) objs[6],
											objs[7].toString(), assignId,
											assignName, objs[15].toString(),
											org.getOrgName(), (String) objs[12],
											taskStartDate,(String) objs[10] };// //意见内容,处理人id,处理时间,处理人名称,处理时间
									// 委托人id,委托人名称，记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务开始时间,任务处理类型
								}
								}
								
						//	System.out.println((new Date()).getTime()-xxDate.getTime()+"AAAAAA******444444444444**********************");
							if (!appMap.containsKey(fieldName)) {
								List<String[]> objList = new ArrayList<String[]>();
								objList.add(objsReturn);
								appMap.put(fieldName, objList);
							} else {
								appMap.get(fieldName).add(objsReturn);
							}
							array.remove(firstObj);
						}
					}
				}
				 System.out.println((new Date()).getTime()-xxDate.getTime()+"*****循环**44444444444444444444*****************************");
				Set<String> keySet = appMap.keySet();        
				boolean isDepartmentSort = false;
				for (String key : keySet) {
	                List<String[]> valueList = appMap.get(key);
	                List<String[]> result = new ArrayList<String[]>();
	                if (valueList == null || valueList.isEmpty()
	                        || valueList.size() == 1) {// 如果记录不存在或只有一条记录就没必要排序了
	                    if (valueList != null && !valueList.isEmpty()) {
	                        for (String[] strs : valueList) {
	                            if (sortValue.startsWith("department")) {
	                                //意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
	                                result.add(new String[] { strs[0], strs[1],
	                                        strs[6], strs[7], strs[8], strs[9],
	                                        strs[10], strs[11], strs[12],strs[14] });
	                                if(!isDepartmentSort){
	                                    isDepartmentSort = true;
	                                }
	                            } else {
	                                //意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
	                                result.add(new String[] { strs[0], strs[1],
	                                        strs[3], strs[4], strs[5], strs[6],
	                                        strs[7], strs[8], strs[9],strs[11] });
	                            }
	                        }
	                    }
	                    appMap.put(key, result);
	                    continue;
	                }
	                if (sortValue == null) {// 默认按人员排序号顺序排序-按人员排序号进行排序
	                    Collections.sort(valueList, new PersonComparator<String[]>(
	                            SortConst.SORT_ASC, 1, userMap));
	                } else {
	                    if (sortValue.startsWith("departmentnumber")) {
	                        List<List<String[]>> groupByList = null;
	                        List<String[]> results = null;
	                        BaseComparator<String[]> comparator = null;
	                        int sortType = -1;
	                        if (sortValue.startsWith("departmentnumber_asc_and")) {
	                            sortType = SortConst.SORT_ASC;
	                        } else {
	                            sortType = SortConst.SORT_DESC;
	                        }
	                        groupByList = new GroupByDepartment(valueList, sortType)
	                                .groupBy();
	                        if (sortValue.endsWith("asc")) {
	                            sortType = SortConst.SORT_ASC;
	                        } else {
	                            sortType = SortConst.SORT_DESC;
	                        }
	                        if (sortValue.indexOf("_personnumber_") != -1) {
	                        	if(sortValue.indexOf("_personnumber_asc")!=-1){//判断是人员升序还是降序
									comparator = new PersonComparator<String[]>(
											SortConst.SORT_ASC, 1, userMap);
								}else{
									comparator = new PersonComparator<String[]>(
											SortConst.SORT_DESC, 1, userMap);
									
								}
	                        } else if (sortValue.indexOf("_date_") != -1) {
	                            comparator = new DateComparator<String[]>(sortType,
	                                    2);
	                        } else if (sortValue.indexOf("_taskstart_") != -1) {// 按任务开始顺序排序
	                            comparator = new DateComparator<String[]>(sortType,
	                                    13);
	                        }
	                        results = new GroupByAfterSorted(groupByList,
	                                comparator).getSortedResult();
	                        valueList = results;
	                    } else {
	                        if (sortValue.equals(SortConst.SORT_TYPE_DATE_DESC)) {// 按日期降序
	                            Collections.sort(valueList,
	                                    new DateComparator<String[]>(
	                                            SortConst.SORT_DESC, 2));
	                        } else if (sortValue
	                                .equals(SortConst.SORT_TYPE_DATE_ASC)) {// 按日期升序
	                            Collections.sort(valueList,
	                                    new DateComparator<String[]>(
	                                            SortConst.SORT_ASC, 2));
	                        } else if (sortValue
	                                .equals(SortConst.SORT_TYPE_TASKSTART_ASC)) {// 按任务开始顺序升序
	                            Collections.sort(valueList,
	                                    new DateComparator<String[]>(
	                                            SortConst.SORT_ASC, 10));
	                        } else if (sortValue
	                                .equals(SortConst.SORT_TYPE_TASKSTART_DESC)) {// 按任务开始顺序降序
	                            Collections.sort(valueList,
	                                    new DateComparator<String[]>(
	                                            SortConst.SORT_DESC, 10));
	                        } else if (sortValue
	                                .equals(SortConst.SORT_TYPE_PERSON_ASC)) {// 按人员排序号升序
	                            Collections.sort(valueList,
	                                    new PersonComparator<String[]>(
	                                            SortConst.SORT_ASC, 1, userMap));
	                        } else if (sortValue
	                                .equals(SortConst.SORT_TYPE_PERSON_DESC)) {// 按人员排序号降序
	                            Collections.sort(valueList,
	                                    new PersonComparator<String[]>(
	                                            SortConst.SORT_DESC, 1, userMap));
	                        }
	                    }

	                }
	                if (valueList != null && !valueList.isEmpty()) {
	                    for (String[] strs : valueList) {
	                        if (sortValue.startsWith("department")) {
	                            //意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
	                            result.add(new String[] { strs[0], strs[1],
	                                    strs[6], strs[7], strs[8], strs[9],
	                                    strs[10], strs[11], strs[12],strs[14] });
	                        } else {
	                            //意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
	                            result.add(new String[] { strs[0], strs[1],
	                                    strs[3], strs[4], strs[5], strs[6],
	                                    strs[7], strs[8], strs[9],strs[11] });
	                        }
	                    }
	                }
	                appMap.put(key, result);
	            }  
				if(isDepartmentSort){
	                appMap.put("isDepartmentSort",null);
	            }
				userMap.clear();
			} catch (WorkflowException ex) {
				throw new SystemException(ex);
			} catch (UnsupportedEncodingException e) {
				throw new SystemException(e);
			}
			 System.out.println((new Date()).getTime()-xxDate.getTime()+"*****结束**5555555555555555555*****************************");
			// workService.errorSubProcess();
			return appMap;
		}
	
	/**
	 * @author 严建
	 * 
	 * @param instanceId
	 *            流程实例id
	 * @return Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,意见记录id
	 *         ,处理人机构名称,最后一个委派/指派人名称,任务处理类型]>>
	 * @throws SystemException
	 * @createTime Mar 12, 2012 2:48:05 PM
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getAllWorkflowApproveinfo(String instanceId,String taskId)
		throws SystemException {
        Map<String, List<String[]>> appMap = new HashMap<String, List<String[]>>();
        try {
            List<Object[]> approveinfoList = new ArrayList<Object[]>();
            Set<Long> instanceIds = new HashSet<Long>();
//            instanceIds.add(instanceId);
            Map<String, Object[]> usersInfoForMap = null;
            Map<Long, Map<String, String>> nodeSettingPluginMap = null;
            if (instanceId != null && !instanceId.equals("")) {
                String firstParentPid = "";
//                获取当前流程的所有父流程实例信息
                List<Object[]>  supProcessInstanceInfos = workflowService.getSupProcessInstanceInfos(instanceId);
//                List<Object[]> parentPList = (List<Object[]>) workflowService.getMonitorParentInstanceIds(new Long(instanceId));
                
                if(supProcessInstanceInfos == null || supProcessInstanceInfos.isEmpty()){//如果当前流程不存在父流程，则当前流程是最顶级流程
                    firstParentPid = instanceId;
                }else{//如果当前流程存在父流程，则取最顶级父流程信息
                    firstParentPid = supProcessInstanceInfos.get(0)[0].toString();
                }
                instanceIds.add(new Long(firstParentPid));
                List<Object[]> list = workflowService.getSubProcessInstanceInfos(firstParentPid);
                List<Long> childrenPIds = null; 
                if (list != null && !list.isEmpty()) {
                    childrenPIds = new ArrayList<Long>(list.size());
                    for (Object[] objs : list) {
                        childrenPIds.add(Long.valueOf(objs[0].toString()));
                    }
                    instanceIds.addAll(childrenPIds);
                }
//                List<Object[]> parentList = workflowService
//                        .getMonitorParentInstanceIds(new Long(instanceId));
//                if (parentList != null && !parentList.isEmpty()) {
//                    for (Object[] objs : parentList) {
//                        instanceIds.add(objs[0].toString());
//                        List<Object[]> childs = workflowService
//                                .getMonitorChildrenInstanceIds(new Long(objs[0]
//                                        .toString()));
//                        if (childs != null && !childs.isEmpty()) {
//                            for (Object[] child : childs) {
//                                instanceIds.add(child[0].toString());
//                            }
//                        }
//                    }
//                } else {
//                    List<Object[]> childs = workflowService
//                            .getMonitorChildrenInstanceIds(new Long(instanceId));
//                    if (childs != null && !childs.isEmpty()) {
//                        for (Object[] child : childs) {
//                            instanceIds.add(child[0].toString());
//                        }
//                    }
//                }
                List<Long> longInstanceIds = new ArrayList<Long>(instanceIds);
//                for (String id : instanceIds) {
//                    longInstanceIds.add(Long.valueOf(id));
////                    approveinfoList.addAll(workflowService
////                            .getBusiFlagByProcessInstanceId(id));
//                }
				approveinfoList.addAll(workflowService.getProcessHandlesAndNodeSettingByPiIds(longInstanceIds));
				if(taskId != null && !"".equals(taskId.trim())){
					List<ToaApproveinfo>  approveInfo =  approveinfoManager.findApproveInfoByBid(taskId);
					if(!approveInfo.isEmpty() && approveInfo.size()!=0){
						ToaApproveinfo approve = approveInfo.get(0);
						/*
						 * Object[]{(0)任务实例Id, (1)任务名称, (2)任务开始时间, (3)任务结束时间, (4)处理意见内容, 
						 * (5)处理人Id, (6)处理人名称, (7)处理时间, (8)节点设置对象, (9)审批数字签名, 
						 * (10)任务委派类型【0：委托；1：指派；“”：非委托】, (11)最后一个委托/指派人Id, 
						 * (12)最后一个委托/指派人名称, (13)第一个委托/指派人Id, (14)第一个委托/指派人名称,
						 *  (15)主键值, (16)流程实例Id}
						 */
						Object[] objs = new Object[17];
						objs[0] = taskId;
						TaskInstance task = workflowService.getTaskInstanceById(taskId);
						objs[1] = task.getName();
						objs[2] = approve.getAiDate();
						objs[3] = approve.getAiDate();
						objs[4] = approve.getAiContent();
						objs[5] = approve.getAiActorId();
						objs[6] = approve.getAiActor();
						objs[7] = approve.getAiDate();
						objs[8] = workflowService.getNodesettingByTid(taskId);
						objs[9] = null;
						objs[10] = null;
						objs[11] = null;
						objs[12] = null;
						objs[13] = approve.getAtoPersonId();
						objs[14] = approve.getAtoPersonName();
						objs[15] = approve.getAiId();
						objs[16] = task.getProcessInstance().getId();
						
						approveinfoList.add(objs);
					}
				}
				int approveinfoListSize = approveinfoList.size();
				if(approveinfoListSize == 0){
					nodeSettingPluginMap = new LinkedHashMap<Long, Map<String,String>>(0);
					usersInfoForMap = new LinkedHashMap<String, Object[]>(0);
				}else{
					Set<String> userIds = new HashSet<String>(approveinfoListSize);
					Set<Long> nsids = new HashSet<Long>(approveinfoListSize);
					Set<String> pluginNames = new HashSet<String>(3);
					pluginNames.add("plugins_businessFlag");//节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
					pluginNames.add("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
					pluginNames.add("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
					for(int i=0;i<approveinfoListSize;i++){
						Object[] objs = approveinfoList.get(i);
						String userId = (String) objs[5];
						TwfBaseNodesetting nodeSetting = (TwfBaseNodesetting) objs[8];// 得到节点对象
						nsids.add(nodeSetting.getNsId());
						userIds.add(userId);
					}
					nodeSettingPluginMap = nodesettingPluginService.getNodesettingPluginValueForMap(nsids, pluginNames);
					usersInfoForMap = customUserService.getUsersInfoForMap(userIds);
				}
			}
			String definitionId = workflowService
					.getProcessFileIdByProcessInstanceId(instanceId);
			//当前人员是否可见办理意见(自己的意见可见)
			Boolean  isCanView = annalManager.isShowAnnal(instanceId);
			if(isCanView == false){
				for (Object[] objs : new ArrayList<Object[]>(approveinfoList)) {
					String userid =  objs[5].toString();// 得到处理人id
					String userId =  userService.getCurrentUser().getUserId();//当前用户id
					if(!userId.equals(userid)){
						approveinfoList.remove(objs);
					}
				}
			}
				
		
			String sortValue = null;// 排序方式
			if (definitionId != null) {
				List<ToaDefinitionPlugin> plugins = definitionPluginService
						.find(definitionId);
				if (plugins != null && !plugins.isEmpty()) {
					for (ToaDefinitionPlugin plugin : plugins) {
						if ("plugins_suggestion".equals(plugin
								.getDefinitionPluginName())) {
							sortValue = plugin.getDefinitionPluginValue();// 得到排序方式
							break;
						}
					}
				}
			} else {
				logger.error("未找到流程实例为'" + instanceId + "'的流程。");
			}
			logger.info("流程处理意见排序方式：" + sortValue);
			final Map<String, Long> userMap = new HashMap<String, Long>();
			Map<String, Organization> orgMap = new HashMap<String, Organization>();
			
			
			for (Object[] objs : new ArrayList<Object[]>(approveinfoList)) {
				TwfBaseNodesetting nodeSetting = (TwfBaseNodesetting) objs[8];// 得到节点对象
				Long ns_id = nodeSetting.getNsId();
				if(nodeSettingPluginMap != null){
					Map<String,String> pluginMap = nodeSettingPluginMap.get(ns_id);
//				String info = nodeSetting.getPlugin("plugins_businessFlag");// 节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
					String info = null;
					if(pluginMap != null){
						info = pluginMap.get("plugins_businessFlag");// 节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
					}
					if (info == null) {
						continue;// 如果意见未绑定控件,则不予处理
					}
					
//				String showBlankSuggest = nodeSetting.getPlugin("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
					String showBlankSuggest = pluginMap.get("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
					Object suggestion = objs[4];
					if ("1".equals(showBlankSuggest)) {
						//不作处理，显示!
					} else {
						//remove掉
						String blankSuggestion = "";
						// 意见内容如果为空,则不予显示在表单上
						if (suggestion == null || suggestion.toString().length() == 0) {
							approveinfoList.remove(objs);// 删除空意见
							continue;
						}else{
							blankSuggestion = suggestion.toString();
							if(StringUtil.isOnlyBlank(blankSuggestion)){
								approveinfoList.remove(objs);// 删除只含空字符串的意见
								continue;
							}
						}
					}
//				String showsuggest = nodeSetting.getPlugin("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
					String showsuggest = pluginMap.get("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
					if ("1".equals(showsuggest)) {
					} else {
						String backSuggestion = "";
						String lastchar = "";
						if (objs[4] != null) {
							backSuggestion = objs[4].toString();
							if (backSuggestion.length() >= 5) {
								lastchar = backSuggestion.substring(backSuggestion
										.length() - 5);
								if ("     ".equals(lastchar)) {
									approveinfoList.remove(objs);// 删除退回意见
									continue;
								}
							}
						}
					}
					if (info != null && info.length() > 0) {
						info = URLDecoder.decode(info, "utf-8");
						info = info.replaceAll("#", ",");
						JSONArray array = JSONArray.fromObject(info);
						JSONObject firstObj = array.getJSONObject(0);
						if (firstObj.containsKey("type")) {// 输入意见字段
							String fieldName = firstObj.getString("fieldName");
							String userId = (String) objs[5];
							String[] objsReturn = null;
							if (userId != null) {
								String orgId = null;
								Organization org = new Organization();
								Object[] userInfo= usersInfoForMap.get(userId);
								if (!userMap.containsKey(userId)) {
									
//								User user = userService
//										.getUserInfoByUserId(userId);
//								userMap.put(userId, user.getUserSequence());
//								orgId = user.getOrgId();
									Long userSequence = (Long) userInfo[2];
									userMap.put(userId, userSequence);
								}
								orgId = (String) userInfo[3];
								org.setOrgId(orgId);
								org.setOrgName((String) userInfo[4]);
								org.setOrgSequence((Long)userInfo[5]);
								
								firstObj.put("taskTime", objs[7]);// 任务处理时间,排序用到
								Object taskTimeJSONObj = firstObj.get("taskTime");
								String time = Long.toString(System
										.currentTimeMillis());
								if (taskTimeJSONObj instanceof JSONObject) {
									JSONObject jsonTaskTime = (JSONObject) taskTimeJSONObj;
									time = jsonTaskTime.getString("time");
								}
								String assignId = null;
								String assignName = null;
								if (objs.length >= 13) {
									assignId = (String) objs[13];// 得到第一个指派/委派人id
									assignName = (String) objs[14];
								}
//							Organization org = null;
//							if (orgId == null) {
//								org = userService
//										.getUserDepartmentByUserId(userId);
//								orgId = org.getOrgId();
//								orgMap.put(orgId, org);
//							}
//							if (!orgMap.containsKey(orgId)) {
//								org = userService.getDepartmentByOrgId(orgId);
//								orgMap.put(orgId, org);
//							} else {
//								org = orgMap.get(orgId);
//							}
								
								String taskStartDate = String.valueOf(objs[0]);// 添加根据任务开始顺序排序方式数据(使用任务id自动增长，作为排序数据)
								if (null!=sortValue && sortValue.startsWith("department")) {
									objsReturn = new String[] { (String) objs[4],
											userId, time, orgId,
											org.getOrgSequence().toString(),
											org.getOrgName(), (String) objs[6],
											objs[7].toString(), assignId,
											assignName, objs[15].toString(),
											org.getOrgName(), (String) objs[12],
											taskStartDate, (String) objs[10]};// //意见内容,处理人id,处理时间
									// 机构id,机构序号,机构名称,
									// 处理人名称,处理时间,
									// 委托人id,委托人名称，记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务开始时间,任务处理类型
								} else {
									objsReturn = new String[] { (String) objs[4],
											userId, time, (String) objs[6],
											objs[7].toString(), assignId,
											assignName, objs[15].toString(),
											org.getOrgName(), (String) objs[12],
											taskStartDate,(String) objs[10] };// //意见内容,处理人id,处理时间,处理人名称,处理时间
									// 委托人id,委托人名称，记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务开始时间,任务处理类型
								}
							}
							
							if (!appMap.containsKey(fieldName)) {
								List<String[]> objList = new ArrayList<String[]>();
								objList.add(objsReturn);
								appMap.put(fieldName, objList);
							} else {
								appMap.get(fieldName).add(objsReturn);
							}
							array.remove(firstObj);
						}
					}
				}
			}
			Set<String> keySet = appMap.keySet();
			boolean isDepartmentSort = false;
			for (String key : keySet) {
				List<String[]> valueList = appMap.get(key);
				List<String[]> result = new ArrayList<String[]>();
				if (valueList == null || valueList.isEmpty()
						|| valueList.size() == 1) {// 如果记录不存在或只有一条记录就没必要排序了
					if (valueList != null && !valueList.isEmpty()) {
						for (String[] strs : valueList) {
							if (null!=sortValue && sortValue.startsWith("department")) {
								//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
								result.add(new String[] { strs[0], strs[1],
										strs[6], strs[7], strs[8], strs[9],
										strs[10], strs[11], strs[12],strs[14] });
								if(!isDepartmentSort){
									isDepartmentSort = true;
								}
							} else {
								//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
								result.add(new String[] { strs[0], strs[1],
										strs[3], strs[4], strs[5], strs[6],
										strs[7], strs[8], strs[9],strs[11] });
							}
						}
					}
					appMap.put(key, result);
					continue;
				}
				if (sortValue == null) {// 默认按人员排序号顺序排序-按人员排序号进行排序
					Collections.sort(valueList, new PersonComparator<String[]>(
							SortConst.SORT_ASC, 1, userMap));
				} else {
					if (sortValue.startsWith("departmentnumber")) {
						List<List<String[]>> groupByList = null;
						List<String[]> results = null;
						BaseComparator<String[]> comparator = null;
						int sortType = -1;
						if (sortValue.startsWith("departmentnumber_asc_and")) {
							sortType = SortConst.SORT_ASC;
						} else {
							sortType = SortConst.SORT_DESC;
						}
						groupByList = new GroupByDepartment(valueList, sortType)
								.groupBy();
						if (sortValue.endsWith("asc")) {
							sortType = SortConst.SORT_ASC;
						} else {
							sortType = SortConst.SORT_DESC;
						}
						if (sortValue.indexOf("_personnumber_") != -1) {
							if(sortValue.indexOf("_personnumber_asc")!=-1){//判断是人员升序还是降序
								comparator = new PersonComparator<String[]>(
										SortConst.SORT_ASC, 1, userMap);
							}else{
								comparator = new PersonComparator<String[]>(
										SortConst.SORT_DESC, 1, userMap);
								
							}
						} else if (sortValue.indexOf("_date_") != -1) {
							comparator = new DateComparator<String[]>(sortType,
									2);
						} else if (sortValue.indexOf("_taskstart_") != -1) {// 按任务开始顺序排序
							comparator = new DateComparator<String[]>(sortType,
									13);
						}
						results = new GroupByAfterSorted(groupByList,
								comparator).getSortedResult();
						valueList = results;
					} else {
						if (sortValue.equals(SortConst.SORT_TYPE_DATE_DESC)) {// 按日期降序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_DESC, 2));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_DATE_ASC)) {// 按日期升序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_ASC, 2));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_TASKSTART_ASC)) {// 按任务开始顺序升序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_ASC, 10));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_TASKSTART_DESC)) {// 按任务开始顺序降序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_DESC, 10));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_PERSON_ASC)) {// 按人员排序号升序
							Collections.sort(valueList,
									new PersonComparator<String[]>(
											SortConst.SORT_ASC, 1, userMap));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_PERSON_DESC)) {// 按人员排序号降序
							Collections.sort(valueList,
									new PersonComparator<String[]>(
											SortConst.SORT_DESC, 1, userMap));
						}
					}

				}
				if (valueList != null && !valueList.isEmpty()) {
					for (String[] strs : valueList) {
						if (null!=sortValue && sortValue.startsWith("department")) {
							//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
							result.add(new String[] { strs[0], strs[1],
									strs[6], strs[7], strs[8], strs[9],
									strs[10], strs[11], strs[12],strs[14] });
						} else {
							//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
							result.add(new String[] { strs[0], strs[1],
									strs[3], strs[4], strs[5], strs[6],
									strs[7], strs[8], strs[9],strs[11] });
						}
					}
				}
				appMap.put(key, result);
			}
			if(isDepartmentSort){
				appMap.put("isDepartmentSort",null);
			}
			userMap.clear();
		} catch (WorkflowException ex) {
			throw new SystemException(ex);
		} catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
		return appMap;
	}
	

	/**
	 * @author:		luosy 2013-5-18
	 * description:	根据流程实例获取本流程的审批意见
	 * modifyer:
	 * description:
	 * @param instanceId
	 *            流程实例id
	 * @return Map<控件名称,List<String[意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,第一个委派/指派人名称,意见记录id
	 *         ,处理人机构名称,最后一个委派/指派人名称,任务处理类型]>>
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String[]>> getThisWorkflowApproveinfo(String instanceId) throws SystemException {
		Map<String, List<String[]>> appMap = new HashMap<String, List<String[]>>();
		try {
			List<Object[]> approveinfoList = new ArrayList<Object[]>();
			Set<Long> instanceIds = new HashSet<Long>();
			Map<String, Object[]> usersInfoForMap = null;
			Map<Long, Map<String, String>> nodeSettingPluginMap = null;
			if (instanceId != null && !instanceId.equals("")) {
				instanceIds.add(new Long(instanceId));
				List<Long> longInstanceIds = new ArrayList<Long>(instanceIds);
				approveinfoList.addAll(workflowService.getProcessHandlesAndNodeSettingByPiIds(longInstanceIds));
				int approveinfoListSize = approveinfoList.size();
				if(approveinfoListSize == 0){
					nodeSettingPluginMap = new LinkedHashMap<Long, Map<String,String>>(0);
					usersInfoForMap = new LinkedHashMap<String, Object[]>(0);
				}else{
					Set<String> userIds = new HashSet<String>(approveinfoListSize);
					Set<Long> nsids = new HashSet<Long>(approveinfoListSize);
					Set<String> pluginNames = new HashSet<String>(3);
					pluginNames.add("plugins_businessFlag");//节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
					pluginNames.add("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
					pluginNames.add("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
					for(int i=0;i<approveinfoListSize;i++){
						Object[] objs = approveinfoList.get(i);
						String userId = (String) objs[5];
						TwfBaseNodesetting nodeSetting = (TwfBaseNodesetting) objs[8];// 得到节点对象
						nsids.add(nodeSetting.getNsId());
						userIds.add(userId);
					}
					nodeSettingPluginMap = nodesettingPluginService.getNodesettingPluginValueForMap(nsids, pluginNames);
					usersInfoForMap = customUserService.getUsersInfoForMap(userIds);
				}
			}
			String definitionId = workflowService
					.getProcessFileIdByProcessInstanceId(instanceId);
			String sortValue = null;// 排序方式
			if (definitionId != null) {
				List<ToaDefinitionPlugin> plugins = definitionPluginService
						.find(definitionId);
				if (plugins != null && !plugins.isEmpty()) {
					for (ToaDefinitionPlugin plugin : plugins) {
						if ("plugins_suggestion".equals(plugin
								.getDefinitionPluginName())) {
							sortValue = plugin.getDefinitionPluginValue();// 得到排序方式
							break;
						}
					}
				}
			} else {
				logger.error("未找到流程实例为'" + instanceId + "'的流程。");
			}
			logger.info("流程处理意见排序方式：" + sortValue);
			final Map<String, Long> userMap = new HashMap<String, Long>();
			Map<String, Organization> orgMap = new HashMap<String, Organization>();
			
			
			for (Object[] objs : new ArrayList<Object[]>(approveinfoList)) {
				TwfBaseNodesetting nodeSetting = (TwfBaseNodesetting) objs[8];// 得到节点对象
				Long ns_id = nodeSetting.getNsId();
				Map<String,String> pluginMap = nodeSettingPluginMap.get(ns_id);
//				String info = nodeSetting.getPlugin("plugins_businessFlag");// 节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
				String info = pluginMap.get("plugins_businessFlag");// 节点上设置的控件信息（含绑定的意见控件,及其他控件属性设置）
				if (info == null) {
					continue;// 如果意见未绑定控件,则不予处理
				}
				
//				String showBlankSuggest = nodeSetting.getPlugin("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
				String showBlankSuggest = pluginMap.get("plugins_chkShowBlankSuggestion");// 是否允许显示空白意见，1是允许，0是不允许
				Object suggestion = objs[4];
				if ("1".equals(showBlankSuggest)) {
					//不作处理，显示!
				} else {
					//remove掉
					String blankSuggestion = "";
					// 意见内容如果为空,则不予显示在表单上
					if (suggestion == null || suggestion.toString().length() == 0) {
						approveinfoList.remove(objs);// 删除空意见
						continue;
					}else{
						blankSuggestion = suggestion.toString();
						if(StringUtil.isOnlyBlank(blankSuggestion)){
							approveinfoList.remove(objs);// 删除只含空字符串的意见
							continue;
						}
					}
				}
				
//				String showsuggest = nodeSetting.getPlugin("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
				String showsuggest = pluginMap.get("plugins_chkShowBackSuggestion");// 是否允许显示退回意见，1是允许，0是不允许
				if ("1".equals(showsuggest)) {
				} else {
					String backSuggestion = "";
					String lastchar = "";
					if (objs[4] != null) {
						backSuggestion = objs[4].toString();
						if (backSuggestion.length() >= 5) {
							lastchar = backSuggestion.substring(backSuggestion
									.length() - 5);
							if ("     ".equals(lastchar)) {
								approveinfoList.remove(objs);// 删除退回意见
								continue;
							}
						}
					}
				}
								
				if (info != null && info.length() > 0) {
					info = URLDecoder.decode(info, "utf-8");
					info = info.replaceAll("#", ",");
					JSONArray array = JSONArray.fromObject(info);
					JSONObject firstObj = array.getJSONObject(0);
					if (firstObj.containsKey("type")) {// 输入意见字段
						String fieldName = firstObj.getString("fieldName");
						String userId = (String) objs[5];
						String[] objsReturn = null;
						if (userId != null) {
							String orgId = null;
							Organization org = new Organization();
							Object[] userInfo= usersInfoForMap.get(userId);
							if (!userMap.containsKey(userId)) {
								
//								User user = userService
//										.getUserInfoByUserId(userId);
//								userMap.put(userId, user.getUserSequence());
//								orgId = user.getOrgId();
									Long userSequence = (Long) userInfo[2];
									userMap.put(userId, userSequence);
							}
							orgId = (String) userInfo[3];
							org.setOrgId(orgId);
							org.setOrgName((String) userInfo[4]);
							org.setOrgSequence((Long)userInfo[5]);
								
							firstObj.put("taskTime", objs[7]);// 任务处理时间,排序用到
							Object taskTimeJSONObj = firstObj.get("taskTime");
							String time = Long.toString(System
									.currentTimeMillis());
							if (taskTimeJSONObj instanceof JSONObject) {
								JSONObject jsonTaskTime = (JSONObject) taskTimeJSONObj;
								time = jsonTaskTime.getString("time");
							}
							String assignId = null;
							String assignName = null;
							if (objs.length >= 13) {
								assignId = (String) objs[13];// 得到第一个指派/委派人id
								assignName = (String) objs[14];
							}
//							Organization org = null;
//							if (orgId == null) {
//								org = userService
//										.getUserDepartmentByUserId(userId);
//								orgId = org.getOrgId();
//								orgMap.put(orgId, org);
//							}
//							if (!orgMap.containsKey(orgId)) {
//								org = userService.getDepartmentByOrgId(orgId);
//								orgMap.put(orgId, org);
//							} else {
//								org = orgMap.get(orgId);
//							}

							String taskStartDate = String.valueOf(objs[0]);// 添加根据任务开始顺序排序方式数据(使用任务id自动增长，作为排序数据)
							if (null!=sortValue && sortValue.startsWith("department")) {
								objsReturn = new String[] { (String) objs[4],
										userId, time, orgId,
										org.getOrgSequence().toString(),
										org.getOrgName(), (String) objs[6],
										objs[7].toString(), assignId,
										assignName, objs[15].toString(),
										org.getOrgName(), (String) objs[12],
										taskStartDate, (String) objs[10]};// //意见内容,处理人id,处理时间
								// 机构id,机构序号,机构名称,
								// 处理人名称,处理时间,
								// 委托人id,委托人名称，记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务开始时间,任务处理类型
							} else {
								objsReturn = new String[] { (String) objs[4],
										userId, time, (String) objs[6],
										objs[7].toString(), assignId,
										assignName, objs[15].toString(),
										org.getOrgName(), (String) objs[12],
										taskStartDate,(String) objs[10] };// //意见内容,处理人id,处理时间,处理人名称,处理时间
								// 委托人id,委托人名称，记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务开始时间,任务处理类型
							}
						}

						if (!appMap.containsKey(fieldName)) {
							List<String[]> objList = new ArrayList<String[]>();
							objList.add(objsReturn);
							appMap.put(fieldName, objList);
						} else {
							appMap.get(fieldName).add(objsReturn);
						}
						array.remove(firstObj);
					}
				}
			}
			Set<String> keySet = appMap.keySet();
			boolean isDepartmentSort = false;
			for (String key : keySet) {
				List<String[]> valueList = appMap.get(key);
				List<String[]> result = new ArrayList<String[]>();
				if (valueList == null || valueList.isEmpty()
						|| valueList.size() == 1) {// 如果记录不存在或只有一条记录就没必要排序了
					if (valueList != null && !valueList.isEmpty()) {
						for (String[] strs : valueList) {
							if (null!=sortValue && sortValue.startsWith("department")) {
								//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
								result.add(new String[] { strs[0], strs[1],
										strs[6], strs[7], strs[8], strs[9],
										strs[10], strs[11], strs[12],strs[14] });
								if(!isDepartmentSort){
									isDepartmentSort = true;
								}
							} else {
								//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
								result.add(new String[] { strs[0], strs[1],
										strs[3], strs[4], strs[5], strs[6],
										strs[7], strs[8], strs[9],strs[11] });
							}
						}
					}
					appMap.put(key, result);
					continue;
				}
				if (sortValue == null) {// 默认按人员排序号顺序排序-按人员排序号进行排序
					Collections.sort(valueList, new PersonComparator<String[]>(
							SortConst.SORT_ASC, 1, userMap));
				} else {
					if (sortValue.startsWith("departmentnumber")) {
						List<List<String[]>> groupByList = null;
						List<String[]> results = null;
						BaseComparator<String[]> comparator = null;
						int sortType = -1;
						if (sortValue.startsWith("departmentnumber_asc_and")) {
							sortType = SortConst.SORT_ASC;
						} else {
							sortType = SortConst.SORT_DESC;
						}
						groupByList = new GroupByDepartment(valueList, sortType)
								.groupBy();
						if (sortValue.endsWith("asc")) {
							sortType = SortConst.SORT_ASC;
						} else {
							sortType = SortConst.SORT_DESC;
						}
						if (sortValue.indexOf("_personnumber_") != -1) {
							comparator = new PersonComparator<String[]>(
									SortConst.SORT_ASC, 1, userMap);
						} else if (sortValue.indexOf("_date_") != -1) {
							comparator = new DateComparator<String[]>(sortType,
									2);
						} else if (sortValue.indexOf("_taskstart_") != -1) {// 按任务开始顺序排序
							comparator = new DateComparator<String[]>(sortType,
									13);
						}
						results = new GroupByAfterSorted(groupByList,
								comparator).getSortedResult();
						valueList = results;
					} else {
						if (sortValue.equals(SortConst.SORT_TYPE_DATE_DESC)) {// 按日期降序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_DESC, 2));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_DATE_ASC)) {// 按日期升序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_ASC, 2));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_TASKSTART_ASC)) {// 按任务开始顺序升序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_ASC, 10));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_TASKSTART_DESC)) {// 按任务开始顺序降序
							Collections.sort(valueList,
									new DateComparator<String[]>(
											SortConst.SORT_DESC, 10));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_PERSON_ASC)) {// 按人员排序号升序
							Collections.sort(valueList,
									new PersonComparator<String[]>(
											SortConst.SORT_ASC, 1, userMap));
						} else if (sortValue
								.equals(SortConst.SORT_TYPE_PERSON_DESC)) {// 按人员排序号降序
							Collections.sort(valueList,
									new PersonComparator<String[]>(
											SortConst.SORT_DESC, 1, userMap));
						}
					}

				}
				if (valueList != null && !valueList.isEmpty()) {
					for (String[] strs : valueList) {
						if (null!=sortValue && sortValue.startsWith("department")) {
							//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
							result.add(new String[] { strs[0], strs[1],
									strs[6], strs[7], strs[8], strs[9],
									strs[10], strs[11], strs[12],strs[14] });
						} else {
							//意见,处理人id,处理人名称,处理时间,第一个委派/指派人id,委托人姓名,记录id,所属机构名称,最后委托人名称(作为实际处理时间使用),任务处理类型
							result.add(new String[] { strs[0], strs[1],
									strs[3], strs[4], strs[5], strs[6],
									strs[7], strs[8], strs[9],strs[11] });
						}
					}
				}
				appMap.put(key, result);
			}
			if(isDepartmentSort){
				appMap.put("isDepartmentSort",null);
			}
			userMap.clear();
		} catch (WorkflowException ex) {
			throw new SystemException(ex);
		} catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
		return appMap;
	}
	
	public TUumsBaseUser getBaseUser(List<TUumsBaseUser> uList,String userId){
		for(Iterator<TUumsBaseUser> it=uList.iterator();it.hasNext();){
			TUumsBaseUser user = it.next();
			if(user.getUserId().equals(userId)){
				return user;
			}
		}
		return null;
	}
	
	public TUumsBaseOrg getBaseOrg(List<TUumsBaseOrg> uList,String orgid){
		for(Iterator<TUumsBaseOrg> it=uList.iterator();it.hasNext();){
			TUumsBaseOrg org = it.next();
			if(org.getOrgId().equals(orgid)){
				return org;
			}
		}
		return null;
	}
}
