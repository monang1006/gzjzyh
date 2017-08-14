package com.strongit.oa.common.workflow.service.documentprivilegeservice;

import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.strongit.oa.common.workflow.IDocumentPrivilegeService;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.bo.TwfBaseNodesetting;
import com.strongit.workflow.exception.WorkflowException;
import com.strongit.workflow.workflowInterface.ITaskService;

/**
 * 
 * @author       严建
 * @company      Strongit Ltd. (C) copyright
 * @date         Apr 27, 2012 12:55:30 PM
 * @version      1.0.0.0
 * @classpath    com.strongit.oa.common.workflow.service.documentprivilegeservice.DocumentPrivilegeService
 */
@Service
@OALogger
public class DocumentPrivilegeService implements IDocumentPrivilegeService {
		
	@Autowired
	ITaskService workflowRun;
	/**
	 * 开始流程时获取文档操作权限
	 * 
	 * @param workflowName
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 * 
	 * (0)导出PDF,(1)导入模板,(2)打印,(3)保存,(4)保存并关闭,(5)页面设置,(6)保留痕迹,(7)不保留痕迹,(8) 显示痕迹,
	 * </p>
	 * <p>
	 * 
	 * (9)隐藏痕迹,(10)文件套红,(11)插入图片,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)插入手工绘画 ,
	 * </p>
	 * <p>
	 * (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 * </p>
	 * <p>
	 * 数据说明：0、无权限；1、有权限
	 * </p>
	 * @throws WorkflowException
	 */
	public String getStartWorkflowDocumentPrivilege(String workflowName)
			throws WorkflowException {
		return workflowRun.getSecondNodeWordPrivilByWorkflowName(workflowName);
	}
	/**
	 * 获取文档操作权限
	 * 
	 * @param taskId
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 * 
	 * (0)导出PDF,(1)导入模板,(2)打印,(3)保存草稿,(4)保存并关闭,(5)页面设置[改为存储草稿必须保存],(6)保留痕迹
	 * ,(7)不保留痕迹,(8)显示痕迹,
	 * </p>
	 * <p>
	 * 
	 * (9)隐藏痕迹,(10)文件套红,(11)檫除痕迹,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)插入手工绘画 ,
	 * </p>
	 * <p>
	 * (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 * </p>
	 * <p>
	 * 数据说明：0、无权限；1、有权限
	 * </p>
	 * @throws WorkflowException
	 * 
	 * 将页面设置存储是否草稿必填.dengzc 2011年7月2日10:48:01
	 */
	/*
	 * public String getDocumentPrivilege(String taskId) throws
	 * WorkflowException{ String wordPrivil = null; if(taskId != null &&
	 * !"".equals(taskId)) { wordPrivil =
	 * workflowRun.getWordPrivilByTaskId(taskId); }else{ wordPrivil =
	 * "1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";//只去掉保存草稿权限 }
	 * 
	 * String[] wordPrivils = wordPrivil.split(","); StringBuilder privilHtml =
	 * new StringBuilder(); privilHtml.append("<table width=\"100%\"
	 * align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\"
	 * class=\"table1\">"); if(wordPrivils != null && wordPrivils.length > 0){
	 * privilHtml.append("<tr><td nowrap align=\"center\" class=\"biao_bg1\">");
	 * privilHtml.append("文件操作"); privilHtml.append("</td></tr>"); //处理保存草稿
	 * if("1".equals(wordPrivils[3])){ privilHtml.append("<tr
	 * id=\"tr_saveDraft\" onclick=\"").append("saveDraft();");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("保存草稿"); privilHtml.append("</td></tr>"); } //处理打印
	 * if("1".equals(wordPrivils[2])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_PrintDoc(true);"); privilHtml.append("\"
	 * style=\"cursor: pointer; line-height: 20px;\">"); privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("打印"); privilHtml.append("</td></tr>"); }
	 * privilHtml.append("<tr><td nowrap align=\"center\" class=\"biao_bg1\">");
	 * privilHtml.append("文件编辑"); privilHtml.append("</td></tr>"); //草稿必填
	 * if("1".equals(wordPrivils[5])){ privilHtml.append("<tr id=\"draftRequired\" style=\"display:none;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("草稿必填"); privilHtml.append("</td></tr>"); } //处理保留痕迹
	 * if("1".equals(wordPrivils[6])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_SetMarkModify(true);");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("保留痕迹"); privilHtml.append("</td></tr>"); } //不留痕迹
	 * if("1".equals(wordPrivils[7])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_SetMarkModify(false);");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("不留痕迹"); privilHtml.append("</td></tr>"); } //显示痕迹
	 * if("1".equals(wordPrivils[8])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_ShowRevisions(true);");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("显示痕迹"); privilHtml.append("</td></tr>"); } //隐藏痕迹
	 * if("1".equals(wordPrivils[9])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_ShowRevisions(false);");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("隐藏痕迹"); privilHtml.append("</td></tr>"); } //檫除痕迹
	 * if("1".equals(wordPrivils[11])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_AcceptAllRevisions();");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("檫除痕迹"); privilHtml.append("</td></tr>"); } //插入模板
	 * if("1".equals(wordPrivils[10])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_AddTemplateFromURL();");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("插入模板"); privilHtml.append("</td></tr>"); }
	 * privilHtml.append("<tr><td nowrap align=\"center\" class=\"biao_bg1\">");
	 * privilHtml.append("电子认证"); privilHtml.append("</td></tr>"); //全屏手写签名
	 * if("1".equals(wordPrivils[18])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_DoHandSign2();"); privilHtml.append("\"
	 * style=\"cursor: pointer; line-height: 20px;\">"); privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("全屏手写签名"); privilHtml.append("</td></tr>"); }
	 * //加盖电子印章 if("1".equals(wordPrivils[13])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_AddSignFromLocal();");
	 * privilHtml.append("\" style=\"cursor: pointer; line-height: 20px;\">");
	 * privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("加盖电子印章"); privilHtml.append("</td></tr>"); }
	 * 
	 * //创建二维条码 if("1".equals(wordPrivils[15])){ privilHtml.append("<tr
	 * onclick=\"").append("TANGER_OCX_CreateBarCode();"); privilHtml.append("\"
	 * style=\"cursor: pointer; line-height: 20px;\">"); privilHtml.append("<td nowrap class=\"td1\">");
	 * privilHtml.append("创建二维条码"); privilHtml.append("</td></tr>"); } }
	 * privilHtml.append("</table>"); return privilHtml.toString(); }
	 */

	/**
	 * 获取文档操作权限
	 * 
	 * @param taskId
	 * @return
	 *            <p>
	 *            数据结构：
	 *            </p>
	 *            <p>
	 * 
	 * (0)导出正文,(1)导入模板,(2)打印,(3)保存草稿,(4)保存并关闭,(5)页面设置[改为存储草稿必须保存],(6)保留痕迹
	 * ,(7)不保留痕迹,(8)显示痕迹,
	 * </p>
	 * <p>
	 * 
	 * (9)隐藏痕迹,(10)文件套红,(11)擦除痕迹,(12)只读,(13)加盖电子印章,(14)加盖电子印章(从服务器),(15)生成二维条码 ,
	 * </p>
	 * <p>
	 * (16)插入手写签名,(17)全屏手工绘画,(18)全屏手写签名
	 * </p>
	 * <p>
	 * 数据说明：0、无权限；1、有权限
	 * </p>
	 * @throws WorkflowException
	 * 
	 * 将页面设置存储是否草稿必填.dengzc 2011年7月2日10:48:01
	 */
	public String getDocumentPrivilege(TwfBaseNodesetting nodeSetting)
			throws WorkflowException {
		String frameRoot = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("frameroot");
		if (frameRoot == null || frameRoot.equals("")
				|| frameRoot.equals("null"))
			frameRoot = "/frame/theme_gray";
		frameRoot = ServletActionContext.getRequest().getContextPath()
				+ frameRoot;

		String wordPrivil = nodeSetting.getNsTaskWordPrivil();
		/*
		 * if (taskId != null && !"".equals(taskId)) { wordPrivil =
		 * workflowRun.getWordPrivilByTaskId(taskId); } else { if (workflowName !=
		 * null && !"".equals(workflowName)) {// 得到开始后的第一个节点权限设置 wordPrivil =
		 * workflowRun .getSecondNodeWordPrivilByWorkflowName(workflowName); }
		 * else { wordPrivil = "1,1,0,0,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1";//
		 * 只去掉保存草稿权限 } }
		 */
		String[] wordPrivils = null;
        if(wordPrivil!=null && wordPrivil.length()>0){
		 wordPrivils = wordPrivil.split(",");
        }
		StringBuilder privilHtml = new StringBuilder();
		if (wordPrivils != null && wordPrivils.length > 0) {
			// 导出正文
			if ("1".equals(wordPrivils[0])) {
				privilHtml.append("<td id=\"tr_saveContent\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:saveContent();\" id=\"hrf_clbccg\">导出正文</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\"></td>");
			}
			// 处理保存草稿
			if ("1".equals(wordPrivils[3])) {
				privilHtml.append("<td id=\"tr_saveDraft\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:saveDraft();\" id=\"hrf_clbccg\">保存草稿</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\"></td>");
			}
			// 处理打印
			if ("1".equals(wordPrivils[2])) {
				privilHtml.append("<td id=\"tr_print\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_PrintDoc(true);\" id=\"hrf_cldy\">打印正文</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\"></td>");
			}
			// 草稿必填
			if ("1".equals(wordPrivils[5])) {
				privilHtml
						.append("<td id=\"draftRequired\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\"  id=\"hrf_cgbt\">草稿必填</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 处理保留痕迹
			if ("1".equals(wordPrivils[6])) {
				privilHtml.append("<td id=\"showMark\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_SetMarkModify(true);\" id=\"hrf_clbchj\">保留痕迹</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 不留痕迹
			if ("1".equals(wordPrivils[7])) {
				privilHtml.append("<td id=\"hideMark\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_SetMarkModify(false);\" id=\"hrf_blhj\">不留痕迹</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 显示痕迹
			if ("1".equals(wordPrivils[8])) {
				privilHtml
						.append("<td id=\"showRevisions\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_ShowRevisions(true);\" id=\"hrf_xshj\">显示痕迹</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 隐藏痕迹
			if ("1".equals(wordPrivils[9])) {
				privilHtml
						.append("<td id=\"hideRevisions\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_ShowRevisions(false);\" id=\"hrf_ychj\">隐藏痕迹</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 擦除痕迹
			if ("1".equals(wordPrivils[11])) {
				privilHtml.append("<td id=\"acceptRevisions\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_AcceptAllRevisions();\" id=\"hrf_cchj\">擦除痕迹</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 插入模板
			if ("1".equals(wordPrivils[10])) {
				privilHtml.append("<td id=\"addTemplate\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button\" onclick=\"javascript:TANGER_OCX_AddTemplateFromURL();\" id=\"hrf_crmb\">插入模板</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\" style=\"display:none;\"></td>");
			}
			// 全屏手写签名
			/*
			 * if("1".equals(wordPrivils[18])){ privilHtml.append("<td id=\"\" >");
			 * privilHtml.append(" <a href=\"#\" class=\"Operation\"
			 * onclick=\"javascript:TANGER_OCX_DoHandSign2();\"
			 * id=\"hrf_qpsxqm\"><img src=\"" + frameRoot +
			 * "/images/songshen.gif\" width=\"15\" height=\"15\" alt=\"\"
			 * class=\"img_s\">全屏手写签名&nbsp;</a>"); privilHtml.append("</td>");
			 * privilHtml.append("<td width=\"5\"></td>"); }
			 */
			// 加盖电子印章
			if ("1".equals(wordPrivils[13])) {
				privilHtml.append("<td id=\"addSign\" style=\"display:none;\">");
				privilHtml
						.append("	<a href=\"#\" class=\"button6\" onclick=\"javascript:TANGER_OCX_AddSignFromLocal();\" id=\"hrf_jgdzqz\">加盖电子印章</a>");
				privilHtml.append("</td>");
				privilHtml.append("<td width=\"5\"></td>");
			}
			/*
			 * if ("1".equals(wordPrivils[15])) { privilHtml.append("<td id=\"\" >");
			 * privilHtml .append(" <a href=\"#\" class=\"Operation\"
			 * onclick=\"javascript:TANGER_OCX_CreateBarCode();\"
			 * id=\"hrf_crmb\"><img src=\"" + frameRoot +
			 * "/images/songshen.gif\" width=\"15\" height=\"15\" alt=\"\"
			 * class=\"img_s\">生成二维条码&nbsp;</a>"); privilHtml.append("</td>"); }
			 */
		}
		return privilHtml.toString();
	}
}
