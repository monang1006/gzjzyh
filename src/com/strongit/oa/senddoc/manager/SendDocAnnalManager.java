package com.strongit.oa.senddoc.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.bo.TJbpmTaskExtend;
import com.strongit.oa.common.business.jbpmtaskextend.IJbpmTaskExtendService;
import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.plugin.DefinitionPluginService;
import com.strongit.oa.senddoc.CAAuth;
import com.strongit.oa.util.annotation.OALogger;

/**
 * 办理记录接管理 Manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Dec 20, 2011 3:47:40 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocAnnalManager
 */
@Service
@Transactional
@OALogger
public class SendDocAnnalManager  {
	@Autowired
	protected DefinitionPluginService definitionPluginService;// 流程定义插件服务类.
	@Autowired
	protected IUserService userService;// 统一用户服务

	@Autowired
	protected IWorkflowService workflow; // 工作流服务
	@Autowired
	protected IJbpmTaskExtendService jbpmTaskExtendService;
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 根据流程实例id判断当前用户是否可以查看办理状态中的意见信息
	 * @author 严建
	 * @param processInstanceId	流程实例id
	 * @return
	 * @createTime Jan 9, 2012 10:46:50 AM
	 */
	public boolean isShowAnnal(String processInstanceId){
		String definitionId = workflow.getProcessFileIdByProcessInstanceId(processInstanceId);
		//办理意见不可见的人员信息
		String  plugins_reassignActor = definitionPluginService.getPlugin(definitionId,"plugins_reassignActor");
		//当前用户是否可以看到办理意见
		boolean canAnnallist = true;
		if(plugins_reassignActor != null && plugins_reassignActor.indexOf(userService.getCurrentUser().getUserId()) != -1){
			canAnnallist = false;
		}
		return canAnnallist;
	}
	
	/**
	 * 根据流程实例id获取所有的办理记录
	 * 
	 * @author 严建
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 * @createTime Feb 20, 2012 4:08:42 PM
	 */
	public List<List> allAnnal(String processInstanceId,String taskId) throws Exception{
		List<Long> PIdList =  this.getWholeProcessIdsByPID(processInstanceId);//流程实例ID列表
		List<List>  listAllAnnal =  this.allAnnal(PIdList,taskId);
		return listAllAnnal;
	}
	
	
	/**
	 * 根据一组流程实例id获取对应的办理记录
	 * @author 严建
	 * @param PIdList	流程实例ID列表
	 * @return
	 * @throws Exception
	 * @createTime Jan 9, 2012 11:11:54 AM
	 * 备注：设置流程办理记录不可见的用户不能看到办理记录中的办理意见（除退回意见）
	 * modify yanjian 如果当前任务是指派或者委派产生的任务，办理记录显示一条相关记录，标识该行为
	 */
	@SuppressWarnings("unchecked")
	public List<List> allAnnal(List<Long> PIdList, String taskId)
			throws Exception {
		
		//<-- 开始判断当前任务是否指派或者委派
//		Object[] newObjs = null;
//		String BprocessInstanceId = null;// taskId对应流程id
//		if (taskId != null && !"".equals(taskId)) {
//			List<String> Items = new LinkedList<String>();
//			Items.add("assignType");
//			Items.add("taskEndDate");
//			Items.add("assignHandlerName");
//			Items.add("toAssignHandlerName");
//			Items.add("taskNodeName");
//			Items.add("processInstanceId");
//			Map<String, Object> paramsmap = new HashMap<String, Object>();
//			paramsmap.put("taskId", taskId);
//			paramsmap.put("assignType", 2);
//			Items = new ArrayList<String>(Items);
//			List<Object[]> taskinfo = workflow.getTaskInfosByConditionForList(
//					Items, paramsmap, null, null, null, "", null);
//			if (taskinfo != null && !taskinfo.isEmpty()) {// 存在指派信息
//				Object[] objs = taskinfo.get(0);
//				if (objs[1] == null) {// 任务为结束
//					newObjs = new Object[4];
//					newObjs[0] = objs[4]; // 节点名称
//					newObjs[1] = objs[2]; // 委托/指派人名称(必须同时查询assignType属性)
//					newObjs[2] = objs[3]; // 被委托/指派人名称(必须同时查询assignType属性)
//					newObjs[3] = objs[0]; // 委托类型(“0”：委托；“1”：指派)
//					BprocessInstanceId = objs[5].toString();
//				}
//			}
//		}
		//-->判断结束
		List<Object[]> approveinfoList = new ArrayList<Object[]>();
		Map<Long,List<Object[]>> approveinfoMap = new LinkedHashMap<Long, List<Object[]>>();
		approveinfoList.addAll(workflow.getProcessHandlesAndNodeSettingByPiIds(PIdList));
		if (approveinfoList != null && !approveinfoList.isEmpty()) {
			for (Object[] objs : approveinfoList) {
				Long processInstanceId = Long.valueOf(objs[16].toString());
				if(!approveinfoMap.containsKey(processInstanceId)){
					List<Object[]> list = new ArrayList<Object[]>();
					list.add(objs);
					approveinfoMap.put(processInstanceId, list);
				}else{
					List<Object[]> list = approveinfoMap.get(processInstanceId);
					list.add(objs);
				}
			}
		}
		//-->判断结束
		List<List> listAllAnnal = new LinkedList<List>();
		List<String> toSelectItems = new LinkedList<String>();
		toSelectItems.add("processInstanceId");
		toSelectItems.add("businessName");
		toSelectItems.add("processName");
		toSelectItems.add("processEndDate");// 流程结束时间
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("processInstanceId", PIdList);
		List<Object[]> listAllAnnalTemp = new LinkedList<Object[]>();
		Map orderMap = new LinkedHashMap();// 当前流程上下文存在多个流程时，流程之间办理记录显示顺序按照流程开始升序排序
		orderMap.put("processStartDate", "0");
		listAllAnnalTemp = (List<Object[]>) workflow
				.getProcessInstanceByConditionForList(toSelectItems, paramsMap,
						orderMap);
		
		if (listAllAnnalTemp != null) {
			for (int j = 0; j < listAllAnnalTemp.size(); j++) {
				Object[] obj = (Object[]) listAllAnnalTemp.get(j);
//				String processInstanceId = obj[0].toString();
				Long processInstanceId = Long.valueOf(obj[0].toString());
				boolean canAnnallist = this.isShowAnnal(processInstanceId.toString());
//				List<Object[]> list = workflow
//						.getBusiFlagByProcessInstanceId(processInstanceId);
				List<Object[]> list = approveinfoMap.get(processInstanceId);
				if(list == null){
					list = new ArrayList<Object[]>(0);
				}
				// 添加按任务结束时间排序 yanjian 2012-12-12 21:04
				Collections.sort(list, new Comparator<Object[]>() {
					public int compare(Object[] arg0, Object[] arg1) {
						Long l1 = Long.MAX_VALUE;
						Long l2 = Long.MAX_VALUE;
						if (arg0[3] != null) {
							l1 = new Long(((Date) arg0[3]).getTime());
						}
						if (arg1[3] != null) {
							l2 = new Long(((Date) arg1[3]).getTime());
						}
						return l1.compareTo(l2);
					}

				});
				CAAuth ca = new CAAuth();
				List<Object[]> listTempL = new LinkedList<Object[]>();
				if (list == null || list.isEmpty()) {
					logger.info(obj[2] + "|" + obj[1] + ":没有办理记录");
					Object[] objs1 = { null, null, null, null, null, null,
							null, null, "", obj[2], obj[1], obj[0], obj[3] };
					listTempL.add(objs1);
				}
				for (int k = 0; k < list.size(); k++) {
					Object[] objs = list.get(k);
					Object signInfo = objs[9];
					StringBuilder userName = new StringBuilder();// 处理人名称
					if ("0".equals(objs[10])) {// 委托
						if (objs[14] != null) {
							if(objs[14].equals(objs[6])){
								/**
								 * 对于委托勾选"收回已办文件"，当委托到期，办理记录的处理人需要改回指派人
								 * 例如:张三委托李四办理
								 * 因为办理人是李四，所以办理记录是：此文由张三委托李四办理
								 * 但是当委托到期，那么办理人应该改回张三，办理记录就会显示：此文由张三委托张三办理
								 */
								userName.append("此文由[").append(objs[6]).append("]办理");
							}else{
								userName.append("此文由[").append(objs[14])
								.append("]委托[").append(objs[6]).append(
								"]办理");
							}
						} else {
							if (objs[12] != null) {
								userName.append("此文由[").append(objs[12])
										.append("]委托[").append(objs[6]).append(
												"]办理");
							} else {
								userName.append(objs[6]);
							}
						}
					} else if ("1".equals(objs[10])) {// 指派
						List<TJbpmTaskExtend> tJbpmTaskExtends = jbpmTaskExtendService.getInfoForListByTaskInstanceId(new Long[]{Long.valueOf(objs[0].toString())});
						for(TJbpmTaskExtend tJbpmTaskExtend:tJbpmTaskExtends){
							if(TJbpmTaskExtend.ACTION_ZHUBANBIANGENG.equals(tJbpmTaskExtend.getAction())){
								userName.append("主办人员由[").append(tJbpmTaskExtend.getFromUserName())
								.append("]变更为[").append(tJbpmTaskExtend.getToUserName()).append(
								"],");
							}else if(TJbpmTaskExtend.ACTION_ZHIPAI.equals(tJbpmTaskExtend.getAction())){
								userName.append("此文由[").append(tJbpmTaskExtend.getFromUserName())
								.append("]指派[").append(tJbpmTaskExtend.getToUserName()).append(
								"]办理,");
							}else if(TJbpmTaskExtend.ACTION_ZHIPAIDFANHUI.equals(tJbpmTaskExtend.getAction())){
								userName.append("此文由[").append(tJbpmTaskExtend.getFromUserName())
								.append("]指派返回,");
							}
						}
						if(userName.length() == 0){
							userName.append(objs[6].toString()).append(",");
						}
						userName.deleteCharAt(userName.length()-1);
					}else if ("2".equals(objs[10])) {// 代办    添加代办的类型    shenyl  20120607
						if (objs[14] != null) {
							userName.append("此文由[").append(objs[6])
							.append("]代[").append(objs[14]).append(
									"]办理");
						} else {
							if (objs[12] != null) {
								userName.append("此文由[").append(objs[6])
										.append("]代[").append(objs[12]).append(
												"]办理");
							} else {
								userName.append(objs[6]);
							}
						}
					}else {
						userName.append(objs[6]);
					}
					Object[] objs1 = { objs[0], objs[1], objs[2], objs[3],
							userName, objs[4], objs[7], objs[9], "", obj[2],
							obj[1], obj[0], obj[3] };
					if (objs[4] != null) {
						objs[4] = objs[4].toString()
								.replaceAll("\r\n", "<br/>");
					}
					objs1[8] = objs[4];
					if (canAnnallist == false) {
						objs1[5] = "";
						objs1[8] = "";
					}
					if (signInfo != null) {
						if (!"".equals(signInfo.toString())) {
							signInfo = ca.getSignInfo(signInfo.toString());
							String[] strSignInfo = signInfo.toString().split(
									",");
							signInfo = strSignInfo[0];
							objs1[9] = signInfo;
						}
					}
					listTempL.add(objs1);
				}
				
				
//				if (BprocessInstanceId != null
//						&& BprocessInstanceId.equals(processInstanceId)) {
//					String assignType = newObjs[3].toString();
//					StringBuilder action = new StringBuilder();
//					if ("0".equals(assignType)) {
//						action.append("委派");
//					} else if ("1".equals(assignType)) {
//						action.append("指派");
//					} else {
//						action.append("转交给");
//					}
//					StringBuilder userNameTemp = new StringBuilder("此文由[")
//							.append(newObjs[1]).append("]").append(action)
//							.append("[").append(newObjs[2]).append("]办理");
//					Object[] objs1 = { taskId, newObjs[0], BprocessInstanceId,
//							null, userNameTemp, "", null, "", "", obj[2],
//							obj[1], obj[0], obj[3] };
//					listTempL.add(objs1);
//				}
				listAllAnnal.add(listTempL);
			}
		}
		return listAllAnnal;
	}
	
	
	
	/**
	 * 根据id获取整个流程中的流程实例id
	 * @author 严建
	 * @param cuttenrPid	流程实例id
	 * @return
	 * @createTime Jan 9, 2012 11:16:55 AM
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getWholeProcessIdsByPID(String processInstanceId){
		String firstParentPid = "";
//		获取当前流程的所有父流程实例信息
		List<Object[]>  parentPList = workflow.getSupProcessInstanceInfos(processInstanceId);
//		List<Object[]> parentPList = (List<Object[]>) workflow.getMonitorParentInstanceIds(new Long(processInstanceId));
		List<Long> PIdList =  new LinkedList<Long>();//流程实例ID列表
		
		if(parentPList == null || parentPList.isEmpty()){//如果当前流程不存在父流程，则当前流程是最顶级流程
			firstParentPid = processInstanceId;
		}else{//如果当前流程存在父流程，则取最顶级父流程信息
			firstParentPid = parentPList.get(0)[0].toString();
		}
		PIdList.add(new Long(firstParentPid));
		List<Object[]> list = workflow.getSubProcessInstanceInfos(firstParentPid);
		List<Long> childrenPIds = null; 
		if (list != null && !list.isEmpty()) {
			childrenPIds = new ArrayList<Long>(list.size());
			for (Object[] objs : list) {
				childrenPIds.add(Long.valueOf(objs[0].toString()));
			}
			PIdList.addAll(childrenPIds);
		}
		
//		//获取当前流程的所有子流程实例信息
//		List<Object[]> childrenPList =(List<Object[]>) workflow.getMonitorChildrenInstanceIds(new Long(firstParentPid));
//		for(Object[] objs:childrenPList){
//			PIdList.add(new Long(objs[0].toString()));
//		}
		return PIdList;
	}
	
	/**
	 * @description
	 * @author 严建
	 * @param listAllAnnal	办理记录列表
	 * @param isUseCASign	是否开启ca认证
	 * @param cuttenrPid	当前流程实例id
	 * @param request		request请求
	 * @createTime Jan 11, 2012 10:22:29 AM
	 */
	@SuppressWarnings("unchecked")
	public void genAnnalHtmlOne(List<List> listAllAnnal,String isUseCASign,String cuttenrPid,HttpServletRequest request){
	    int td2 = 80;
	    int td4 = 160;
	    int td5 = 80;
		String tableString = "<table cellSpacing=1 cellPadding=1 width=\"100%\" border=\"0\" class=\"table1\">";
		String trString = "<tr class=\"biao_bg2\">"+
		"<td class=\"td4\" align=\"center\" width=\"15%\" >"+
			"<strong>环节名称</strong>"+
		"</td>"+
		"<td class=\"td4\" align=\"center\" width=\""+td2+"\" >"+
			"<strong>处理人</strong>"+
		"</td>"+
		"<td class=\"td4\" align=\"center\">"+
			"<strong>处理意见</strong>"+
		"</td>"+
		"<td class=\"td4\" align=\"center\" width=\""+td4+"\" >"+
			"<strong>处理时间</strong>"+
		"</td>";
		if(isUseCASign.equals("1")){
			td4 = 120;
			trString = trString + "<td class=\"td5\"  align=\"center\" width=\""+td5+"\" >"+
			"<strong>CA签名</strong>"+
			"</td>";
		}
		trString = trString +"</tr>";
		for(int i=0;i<listAllAnnal.size();i++){
			List<Object[]> annalTempL = listAllAnnal.get(i);
			String title = "";
			if(i!=0){
				title ="<br/>";
			}
			for(int j=0;j<annalTempL.size();j++){
				Object[] objs = annalTempL.get(j);
				if(j == 0){
					title = title + "<font style=\"font: bold;color:blue;\">"+objs[9].toString()+"</font>";
					if(objs[12]==null){
						title = title+"(<font color=\"red\">正在办理</font>)";
					}
					int colspan = 5;
					if(isUseCASign.equals("1")){
						colspan = 6;
					}
					tableString  = tableString +"<tr class=\"biao_bg1\" style=\"BACKGROUND-COLOR:#FFFFFF;\">"+
					"<td align=\"left\"  colspan=\""+colspan+"\">"+title+"</td>"+
					"</tr>";
					tableString  = tableString +trString;
				}
				if(objs[1] != null){
					tableString  = tableString +"<tr class=\"biao_bg1\">"+
					"<td align=\"center\">"+
					objs[1]+
				"</td>"+
				"<td align=\"center\">"+
					objs[4]+
				"</td>"+
				"<td align=\"left\" title='"+(objs[5] == null?"":objs[5])+"'>"+
					(objs[8] == null?"":objs[8])+
				"</td>"+
				"<td align=\"center\">"+
					(objs[6] == null ?"":new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objs[6]))+
				"</td>";
				
				if(isUseCASign.equals("1")){
					tableString  = tableString + "<td align=\"left\">"+
					(objs[7]==null?"":objs[7])+
				"</td>";
				}
				tableString  = tableString + "</tr>";
				}
				
			}
		}
		tableString = tableString+"</table><br/>";
		request.setAttribute("tableString", tableString);
	}
	/**
	 * @description
	 * @author hecj
	 * @param listAllAnnal	办理记录列表用于wapoa
	 * @param isUseCASign	是否开启ca认证
	 * @param cuttenrPid	当前流程实例id
	 * @param request		request请求
	 * @createTime Jan 11, 2012 10:22:29 AM
	 */
	@SuppressWarnings("unchecked")
	public void genAnnalHtmlOneForWap(List<List> listAllAnnal,String isUseCASign,String cuttenrPid,HttpServletRequest request){
	    String td1="30%";
		String td2 = "20%";
		String td4 = "30%";
	    String td5 = "20%";
		String tableString = "<table cellSpacing=1 cellPadding=1 width=\"100%\" border=\"0\" class=\"table1\">";
		String trString = "<tr class=\"biao_bg2\">"+
		"<td class=\"td1\" align=\"center\" width=\""+td1+"\" >"+
			"环节名称"+
		"</td>"+
		"<td class=\"td2\" align=\"center\" width=\""+td2+"\" >"+
			"处理人"+
		"</td>"+
		"<td class=\"td3\" align=\"center\" width=\""+ td4 +"\" >"+
			"处理意见"+
		"</td>"+
		"<td class=\"td4\" align=\"center\" width=\""+td5+"\" >"+
			"处理时间"+
		"</td>";
		/*if(isUseCASign.equals("1")){
			td4 = 120;
			trString = trString + "<td class=\"td5\"  align=\"center\" width=\""+td5+"\" >"+
			"<strong>CA签名</strong>"+
			"</td>";
		}*/
		trString = trString +"</tr>";
		for(int i=0;i<listAllAnnal.size();i++){
			List<Object[]> annalTempL = listAllAnnal.get(i);
			String title = "";
			if(i!=0){
				title ="<br/>";
			}
			for(int j=0;j<annalTempL.size();j++){
				Object[] objs = annalTempL.get(j);
				if(j == 0){
					title = title + "<font style=\"font: bold;color:blue;\">"+objs[9].toString()+"</font>";
					if(objs[12]==null){
						title = title+"(<font color=\"red\">正在办理</font>)";
					}
					int colspan = 5;
					if(isUseCASign.equals("1")){
						colspan = 6;
					}
					tableString  = tableString +"<tr class=\"biao_bg1\" style=\"BACKGROUND-COLOR:#FFFFFF;\">"+
					"<td align=\"left\"  colspan=\""+colspan+"\">"+title+"</td>"+
					"</tr>";
					tableString  = tableString +trString;
				}
				if(objs[1] != null){
					tableString  = tableString +"<tr class=\"biao_bg1\">"+
					"<td align=\"left\">"+
					objs[1]+
				"</td>"+
				"<td align=\"left\">"+
					objs[4]+
				"</td>"+
				"<td align=\"left\" title='"+(objs[5] == null?"":objs[5])+"'>"+
					(objs[8] == null?"":objs[8])+
				"</td>"+
				"<td align=\"left\">"+
					(objs[6] == null ?"":new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date)objs[6]))+
				"</td>";
				
				if(isUseCASign.equals("1")){
					tableString  = tableString + "<td align=\"left\">"+
					(objs[7]==null?"":objs[7])+
				"</td>";
				}
				tableString  = tableString + "</tr>";
				}
				
			}
		}
		tableString = tableString+"</table><br/>";
		request.setAttribute("tableString", tableString);
	}
}
