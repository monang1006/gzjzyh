package com.strongit.oa.senddoc;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.strongit.oa.common.AbstractBaseWorkflowAction;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.service.BaseWorkflowManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.senddoc.manager.SendDocDesktopManager;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.senddoc.manager.SendDocUploadManager;
import com.strongit.oa.util.ListUtils;
import com.strongmvc.orm.hibernate.Page;

/**
 * Action
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Dec 13, 2011 3:51:48 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.senddoc.SendDocDesktopAction
 */
public class SendDocDesktopAction extends AbstractBaseWorkflowAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6618612640389740749L;
	@Autowired
	SendDocDesktopManager manager;
	@Autowired
	SendDocManager sendDocManager;
	@Autowired
	SendDocUploadManager sendDocUploadManager;
	private String processStatus;
	/**
	 * @description 桌面显示主办列表
	 * @author 严建
	 * @createTime Dec 13, 2011 3:52:33 PM
	 * @return String 
	 */
	@SuppressWarnings("unchecked")
	public String showDesktopHostByWork() throws Exception {
//		 获取blockId
		String blockId = getRequest().getParameter("blockid");
		String subLength = getRequest().getParameter("subLength");
		String showNum = getRequest().getParameter("showNum");
		String sectionFontSize = getRequest().getParameter("sectionFontSize");
		String showCreator = null;
		String showDate = getRequest().getParameter("showDate");
		if (blockId != null) {
			Map<String, String> map = sendDocManager.getDesktopParam(blockId);
			showNum = map.get("showNum");// 显示条数
			subLength = map.get("subLength");// 主题长度
			showCreator = map.get("showCreator");// 是否显示作者
			showDate = map.get("showDate");// 是否显示日期
			sectionFontSize = map.get("sectionFontSize");//是否显示字体大小
		}

		int num = 0, length = 0;
		if (showNum != null && !"".equals(showNum) && !"null".equals(showNum)) {
			num = Integer.parseInt(showNum);
		}

		if (subLength != null && !"".equals(subLength)
				&& !"null".equals(subLength)) {
			length = Integer.parseInt(subLength);
		}

		// 获取主办公文
		Page pageHosted = new Page(num, false);
		List<Object[]> HostedWorkflow  = manager.getHostedWorkflowForDesktop(null);
		pageHosted = ListUtils.splitList2Page(pageHosted, HostedWorkflow);

		String rootPath = getRequest().getContextPath();
		StringBuffer innerHtml = new StringBuffer();
		String userId = "";
		if (pageHosted.getResult() != null) {
			String processInstanceId = null;
			@SuppressWarnings("unused")
			Object processEndDate = null;
			Object processStartDate = null;
			String title = null;
			innerHtml.append("<script type=\"text/javascript\">function reloadPage() {window.location.reload();}</script>");
			for (Iterator iterator = pageHosted.getResult().iterator(); iterator
					.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				processInstanceId = object[0].toString();
				processEndDate = object[2];
				processStartDate = object[6];
				title = object[4] == null ? "" : object[4].toString();
				title = title.replace("\\r\\n", " ");
				title = title.replace("\\n", " ");
				if (title.length() > length && length > 0) {
					title = title.substring(0, length) + "...";
				}
				innerHtml.append("<div class=\"linkdiv\" title=\"\">");
				// 图标
				innerHtml.append("<img src=\"").append(rootPath).append(
						"/oa/image/desktop/littlegif/news_bullet.gif").append(
						"\"/> ");

				// 如果显示的内容长度大于设置的主题长度，则过滤该长度
				StringBuilder clickTitle = new StringBuilder();
				clickTitle
						.append("var Width=screen.availWidth-10;var Height=screen.availHeight-30;");
				clickTitle
						.append("var a = window.open('")
						.append(getRequest().getContextPath())
						.append("/senddoc/sendDoc!viewHostedBy.action?instanceId=")
						.append(processInstanceId)
						.append(
								"','hostedby','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no');");

				innerHtml.append("<span style=\"font-size:"+sectionFontSize+"px\" title=\"").append(
						"("
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm")
										.format(processStartDate) + ")\">").append(
						"<a href=\"#\" onclick=\"").append(
						clickTitle.toString()).append("\">").append(title)
						.append("</a></span>");

				// 显示发起人信息
				if ("1".equals(showCreator)) {
					if (object[7] == null) {
						object[7] = sendDocManager
								.getFormIdAndBussinessIdByTaskId(object[0]
										.toString())[0];
					}
					bussinessId = String.valueOf(object[7]);
					if (!"0".equals(bussinessId)) {
						String departmentName = null;
						String[] args = bussinessId.split(";");

						tableName = String.valueOf(args[0]);
						pkFieldName = String.valueOf(args[1]);
						pkFieldValue = String.valueOf(args[2]);
						Map map = sendDocManager.getSystemField(pkFieldName,
								pkFieldValue, tableName);
						userId = (String) map
								.get(BaseWorkflowManager.WORKFLOW_AUTHOR);
						if (userId != null) {
							userName = adapterBaseWorkflowManager.getUserService().getUserNameByUserId(userId);
							Organization department = adapterBaseWorkflowManager.getUserService()
									.getUserDepartmentByUserId(userId);
							departmentName = department.getOrgName();
							innerHtml.append("<span class =\"linkgray\">")
									.append(
											userName + "[" + departmentName
													+ "]").append("</span>");

						}
					}
				}

				// 显示日期信息
				if ("1".equals(showDate)) {
					SimpleDateFormat st = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm");
					innerHtml.append("<span class =\"linkgray10\">").append(
							" (" + st.format(object[1]) + ")")
							.append("</span>");
				}

				innerHtml.append("</div>");
			}

		}
		logger.info("*********desktop*********");
		logger.info(innerHtml.toString());
		logger.info("*********desktop*********");
		return renderHtml(innerHtml.toString());// 用renderHtml将设置好的html代码返回桌面显示
	}
	
	
	
	/**
	 * （办公门户）我的办文的数据统计
	 * @author 严建
	 * @return
	 * @throws Exception
	 * @createTime Jan 6, 2012 5:06:42 PM
	 */
	public String showWorkTotal() throws Exception{
		Map<String,Integer>  doingWorkMap = manager.getDoingWorkKindsGroupInfo();
		int signSize = doingWorkMap.get("sign");//待办
		int notsignSize = doingWorkMap.get("notsign");//代签收
		Map<String,Integer>  doneWorkMap = manager.getDoneWorkKindsGroupInfo();
		int endSize = doneWorkMap.get("end");//办结
		int noendSize = doneWorkMap.get("noend");//在办
		return this.renderText("signSize:"+signSize+";notsignSize:"+notsignSize+";endSize:"+endSize+";noendSize:"+noendSize);
	}
	
	@Override
	protected String getDictType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseManager getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWorkflowType() {
		// TODO Auto-generated method stub
		return null;
	}



	public String getProcessStatus() {
		return processStatus;
	}



	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

}
