package com.strongit.oa.senddoc.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.workflow.INodeService;
import com.strongit.oa.common.workflow.INodesettingPluginService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBean;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.common.workflow.plugin.util.NodesettingPluginConst;
import com.strongit.oa.common.workflow.service.nodeservice.pojo.BackBean;
import com.strongit.oa.senddoc.bo.NodeMapHtml;
import com.strongit.oa.senddoc.bo.ParamBean;
import com.strongit.oa.senddoc.bo.PreNodeMapHtml;
import com.strongit.oa.senddoc.bo.PreSuperNodeMapHtml;
import com.strongit.oa.senddoc.manager.service.ISendDocIcoService;
import com.strongit.oa.util.StringUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.businesscustom.CustomWorkflowConst;
import com.strongmvc.orm.hibernate.Page;

/**
 * Action
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Mar 23, 2012 1:39:56 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.manager.SendDocHtmlManager
 */
@Service
@Transactional
@OALogger
public class SendDocHtmlManager {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ISendDocIcoService sendDocIcoManager;

	@Autowired
	private SendDocLinkManager sendDocLinkManager;

	@Autowired
	private IWorkflowService workflowService;
	@Autowired
	private INodesettingPluginService nodesettingPluginService;// 节点插件服务类
	@Autowired
	private INodeService nodeService;

	@Autowired
	private IWorkflowConstService workflowConstService;// 工作流常量服务类

	/**
	 * 桌面显示代办事宜
	 * 
	 * @author 严建
	 * @param page
	 * @param innerHtml
	 * @param parambean
	 * @throws Exception
	 * @createTime Mar 23, 2012 1:39:10 PM
	 */
	public void loadDesktopTodoHtml(Page<TaskBusinessBean> page,
			StringBuffer innerHtml, ParamBean parambean) throws Exception {
		if (innerHtml == null) {
			throw new NullPointerException("参数innerHtml不能为null");
		}
		String sectionFontSize = parambean.getSectionFontSize();
		String type=parambean.getType();
		List resList = page.getResult();
		if (resList != null) {
			innerHtml
					.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = resList.iterator(); iterator.hasNext();) {

				TaskBusinessBean taskbusinessbean = (TaskBusinessBean) iterator
						.next();
				taskbusinessbean.setSortType(type);//传一个type，来标识待签收还是待办
				innerHtml.append("<table class=\"linkdiv\" style=\"font-size:"+sectionFontSize+"px\" width=\"100%\" title=\"\">");
				innerHtml.append("<tr>");
				innerHtml.append("<td>");
				sendDocIcoManager.loadToDoIco(innerHtml, taskbusinessbean,
						parambean.getRootPath());
				sendDocLinkManager.genTodoTitle(innerHtml, taskbusinessbean,
						parambean);
				innerHtml.append("</tr>");
				innerHtml.append("</table>");
				innerHtml.append("</div>");
			}

		}
		// 【更多】跳转连接
		sendDocLinkManager.genTodoClickMoreLink(innerHtml, parambean
				.getRootPath(), parambean.getWorkflowType(), parambean
				.getType());
		innerHtml.append("<input type=\"hidden\" class=\"listsize\" value=\"")
				.append(
						page == null ? 0 : (page.getTotalCount() == -1 ? 0
								: page.getTotalCount())).append("\" />");
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
	}

	/**
	 * 退文列表【退回】
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 27, 2012 6:36:26 PM
	 */
	public String preNodeMapHtml(String taskId) {
		String nodeId = workflowService.getNodeIdByTaskInstanceId(taskId);
		String preNodes = nodeService.getPreNodeByNodeId(nodeId);
		PreNodeMapHtml prenodemaphtml = new PreNodeMapHtml(nodeService
				.getBackSpaceForMapGroupByTaskNodeName(taskId),preNodes);
		return getBackListHtml(prenodemaphtml);
	}
	/**
	 * 退文节点【退回】
	 * 
	 * @author 
	 * @param taskId
	 *   @param nodeName
	 * @return
	 * @createTime 
	 */
	public String NodeMapHtml(String taskId,String nodeName) {
		String nodeId = workflowService.getNodeIdByTaskInstanceId(taskId);
		String preNodes = nodeService.getPreNodeByNodeId(nodeId);
		PreNodeMapHtml prenodemaphtml = new PreNodeMapHtml(nodeService
				.getBackSpaceForMapGroupByTaskNodeName(taskId),preNodes);
		return getBackHtml(prenodemaphtml,nodeName);
	}
	
	/**
	 * 驳回节点【驳回】
	 * 
	 * @author 
	 * @param taskId
	 *   @param nodeName
	 * @return
	 * @createTime 
	 */
	public String NodeMapHtml2(String taskId,String nodeName) {
		String nodeId = workflowConstService.getSuperProcessNodeIdByTid(taskId)
		+ "";
		String preNodes = nodeService.getPreNodeByNodeId(nodeId);
		PreSuperNodeMapHtml PreSuperNodeMapHtml = new PreSuperNodeMapHtml(
				nodeService.getOverRuleForMapGroupByTaskNodeName(taskId),
				preNodes);
		return getBackHtml2(PreSuperNodeMapHtml,nodeName);
	}
	
	/**
	 * 退文节点 
	 * 
	 * @author
	 * @param map
	 * @param nodeName 节点名称
	 * @return
	 * @createTime Mar 30, 2012 3:59:47 PM
	 */
	private String getBackHtml(NodeMapHtml nodemaphtml,String nodeName) {
		Map<String, List<BackBean>> map = nodemaphtml.getMap();
		//当前任务之前的节点名称
		String preNodes = nodemaphtml.getPreNodes();
		String  flag="";
			if (map != null) {
					if (preNodes != null && !"".equals(preNodes)) {
						String[] preNodeArr = preNodes.split(",");
							for (String pnode : preNodeArr) {
								//当前任务之前的节点名称 等于 nodeName(节点名称)							
								if(pnode.equals(nodeName)){
									List<BackBean> backbeanlist = map.get(pnode);
											if (backbeanlist != null && !backbeanlist.isEmpty()) {
												BackBean backbean = backbeanlist.get(0);
													String plugins_notbackspace = nodesettingPluginService
													.getNodesettingPluginValue(StringUtil
															.castString(backbean.getTaskNodeId()),
															NodesettingPluginConst.PLUGINS_NOTBACKSPACE);
													if("1".equals(plugins_notbackspace)){//控制节点上设置了不可退回的节点信息
														flag="success";
													}
										}
									}
							}
						}
			}	
			return flag;
	}
	
	/**
	 * 驳回节点 
	 * 
	 * @author
	 * @param map
	 * @param nodeName 节点名称
	 * @return
	 * @createTime Mar 30, 2012 3:59:47 PM
	 */
	private String getBackHtml2(NodeMapHtml nodemaphtml,String nodeName) {
		Map<String, List<BackBean>> map = nodemaphtml.getMap();
		//当前任务之前的节点名称
		String preNodes = nodemaphtml.getPreNodes();
		String  flag="";
			if (map != null) {
					if (preNodes != null && !"".equals(preNodes)) {
						String[] preNodeArr = preNodes.split(",");
							for (String pnode : preNodeArr) {
								//当前任务之前的节点名称 等于 nodeName(节点名称)							
								if(pnode.equals(nodeName)){
									List<BackBean> backbeanlist = map.get(pnode);
											if (backbeanlist != null && !backbeanlist.isEmpty()) {
												BackBean backbean = backbeanlist.get(0);
													String plugins_notbackspace = nodesettingPluginService
													.getNodesettingPluginValue(StringUtil
															.castString(backbean.getTaskNodeId()),
															NodesettingPluginConst.PLUGINS_NOTOVERRULE);
													if("1".equals(plugins_notbackspace)){//控制节点上设置了不可驳回的节点信息
														flag="success";
													}
										}
									}
							}
						}
			}	
			return flag;
	}

	/**
	 * 退文列表【驳回】
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 30, 2012 4:00:48 PM
	 */
	public String preSuperNodeMapHtml(String taskId) {
		String nodeId = workflowConstService.getSuperProcessNodeIdByTid(taskId)
				+ "";
		String preNodes = nodeService.getPreNodeByNodeId(nodeId);
		PreSuperNodeMapHtml PreSuperNodeMapHtml = new PreSuperNodeMapHtml(
				nodeService.getOverRuleForMapGroupByTaskNodeName(taskId),
				preNodes);
		return getBackListHtml(PreSuperNodeMapHtml);
	}

	/**
	 * 生成退文列表
	 * 
	 * @author 严建
	 * @param map
	 * @param preNodes
	 * @return
	 * @createTime Mar 30, 2012 3:59:47 PM
	 */
	private String getBackListHtml(NodeMapHtml nodemaphtml) {
		Map<String, List<BackBean>> map = nodemaphtml.getMap();
		String preNodes = nodemaphtml.getPreNodes();
		String tableString = "<table cellSpacing=1 cellPadding=1 width=\"100%\" border=\"0\" class=\"table1\">";
		tableString += "<tr class=\"biao_bg2\">"
				+ "<td class=\"td1\" align=\"center\" width=\"10%\" >"
				+ "<strong>节点名称</strong>" + "</td>"
				+ "<td class=\"td2\" align=\"center\" >"
				+ "<strong>处理人</strong>" + "</td>"
				+ "<td class=\"td3\" width=\"10%\" align=\"center\">"
				+ "<strong>操作</strong>" + "</td>";
		tableString = tableString + "</tr>";
		if (map != null) {
			if (preNodes != null && !"".equals(preNodes)) {
				String[] preNodeArr = preNodes.split(",");
				for (String pnode : preNodeArr) {
					List<BackBean> backbeanlist = map.get(pnode);
					if (backbeanlist != null && !backbeanlist.isEmpty()) {
						BackBean backbean = backbeanlist.get(0);
						if(CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_OVERRULE.equals(nodemaphtml.getBackType())){
							String plugins_notoverrule = nodesettingPluginService
							.getNodesettingPluginValue(StringUtil
									.castString(backbean.getTaskNodeId()),
									NodesettingPluginConst.PLUGINS_NOTOVERRULE);
							if("1".equals(plugins_notoverrule)){//控制节点上设置了不可驳回的节点信息不显示在列表中
								continue;
							}
						}
						if(CustomWorkflowConst.WORKFLOW_TASK_BACKTYPE_BACKSPACE.equals(nodemaphtml.getBackType())){
							String plugins_notbackspace = nodesettingPluginService
							.getNodesettingPluginValue(StringUtil
									.castString(backbean.getTaskNodeId()),
									NodesettingPluginConst.PLUGINS_NOTBACKSPACE);
							if("1".equals(plugins_notbackspace)){//控制节点上设置了不可驳回的节点信息不显示在列表中
								continue;
							}
						}
						int rowspan = backbeanlist.size();
						tableString = tableString
						+ "<tr class=\"biao_bg1\">"
						+ "<td align=\"left\" rowspan="
						+ rowspan
						+ " >"
						+ backbean.getTaskNodeName()
						+ "</td>"
						+ "<td align=\"center\">"
						+ backbean.getUserName()
						+ "</td>"
						+ "<td align=\"center\"><a href='#' onclick=\"selectNode('"
						+ backbean.getTaskNodeId() + "','"
						+ backbean.getTaskNodeName() + "','"
						+ backbean.getUserId() + "','"
						+ backbean.getUserName()
						+ "');\">退回</a>" + "</td>";
						tableString = tableString + "</tr>";
						if (rowspan > 1) {
							for (int i = 1; i < rowspan; i++) {
								backbean = backbeanlist.get(i);
								tableString = tableString
										+ "<tr class=\"biao_bg1\">"
										+ "<td align=\"center\">"
										+ backbean.getAiActorName()
										+ "</td>"
										+ "<td align=\"center\"><a href='#' onclick=\"selectNode('"
										+ backbean.getTaskNodeId() + "','"
										+ backbean.getTaskNodeName() + "','"
										+ backbean.getUserId() + "','"
										+ backbean.getUserName()
										+ "');\">退回</a>" + "</td>";
								tableString = tableString + "</tr>";
							}
						}
					}

				}
			}
		}
		tableString = tableString + "</table><br/>";
		return tableString;
	}

	/**
	 * 生成退回列表HTML
	 * 
	 * @author 严建
	 * @param taskId
	 *            任务id
	 * @return
	 * @createTime Mar 27, 2012 2:24:53 PM
	 * @see {@link #preNodeMapHtml(String)}
	 */
	@Deprecated
	public String preNodeListHtml(String taskId) {
		List<TaskBean> nodeList = workflowService.getPreTaskNodeList(taskId);
		String tableString = "<table cellSpacing=1 cellPadding=1 width=\"100%\" border=\"0\" class=\"table1\">";
		tableString += "<tr class=\"biao_bg2\">"
				+ "<td class=\"td1\" align=\"center\" width=\"10%\" >"
				+ "<strong>节点名称</strong>" + "</td>"
				+ "<td class=\"td2\" align=\"center\" >"
				+ "<strong>处理人</strong>" + "</td>"
				+ "<td class=\"td3\" width=\"10%\" align=\"center\">"
				+ "<strong>操作</strong>" + "</td>";
		tableString = tableString + "</tr>";
		for (int i = 0; i < nodeList.size(); i++) {
			TaskBean taskBean = nodeList.get(i);
			if (taskBean.getNodeName() != null) {
				tableString = tableString
						+ "<tr class=\"biao_bg1\">"
						+ "<td align=\"left\">"
						+ taskBean.getNodeName()
						+ "</td>"
						+ "<td align=\"center\">"
						+ taskBean.getActorName()
						+ "</td>"
						+ "<td align=\"center\"><a href='#' onclick=\"selectNode('"
						+ taskBean.getNodeId() + "','" + taskBean.getNodeName()
						+ "');\">退回</a>" + "</td>";
				tableString = tableString + "</tr>";
			}
		}
		tableString = tableString + "</table><br/>";
		return tableString;
	}

}
