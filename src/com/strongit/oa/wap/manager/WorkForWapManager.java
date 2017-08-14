package com.strongit.oa.wap.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hdf.extractor.WordDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.strongit.oa.wap.util.AttachmentHelper;

import com.strongit.oa.bo.WorkflowAttach;
import com.strongit.oa.common.eform.IEFormService;
import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.oa.common.eform.model.EFormField;
import com.strongit.oa.common.service.BaseManager;
import com.strongit.oa.common.user.model.Organization;
import com.strongit.oa.common.user.model.User;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.WorkFlowTypeConst;
import com.strongit.oa.prsnfldr.util.FileUtil;
import com.strongit.oa.senddoc.manager.SendDocManager;
import com.strongit.oa.util.MessagesConst;
import com.strongit.oa.util.PathUtil;
import com.strongit.oa.util.UUIDGenerator;
import com.strongmvc.exception.DAOException;
import com.strongmvc.exception.ServiceException;
import com.strongmvc.exception.SystemException;
import com.strongmvc.orm.hibernate.Page;

/**
 * @author       邓志城
 * @company      Strongit Ltd. (C) copyright
 * @date         2009年2月12日10:31:52
 * @version      1.0.0.0
 * @comment      工作处理Manager
 */
@Service
@Transactional
public class WorkForWapManager extends BaseManager{
	@Autowired IWorkflowService workflowService;
	@Autowired
	private IEFormService eformService;

	/**
	 * 提交工作流【外部系统调用OA的工作流审批】
	 * 流程启动者为OA系统专门提供一个匿名用户
	 * @author:邓志城
	 * @date:2009-7-8 下午05:59:39
	 * @param workflowName
	 * @param businessId
	 * @param businessName
	 * @param tansitionName
	 * @param concurrentTrans
	 * @param sugguestion
	 * @throws SystemException
	 */
	public void handleWorkflowWithUser(String workflowName,
									   String businessId,
									   String businessName,
									   String tansitionName, 
									   String concurrentTrans,
									   String sugguestion)throws SystemException{
		//这里的userId为初始化数据
		String userId = "402882282262726001226289c8cb0001";
		workflow.startWorkflow("0", workflowName, userId,
				businessId, businessName, null, tansitionName, concurrentTrans,
				sugguestion);
	}

	/**
	 * 获取工作处理审批意见字典项
	 * @author 邓志城
	 * @Date 2009年5月4日14:07:03
	 * @return 工作处理意见列表
	 */
	public List getIdeas() throws SystemException,ServiceException{
		return super.getItemsByDictValue(WorkFlowTypeConst.WORKIDEA);
	}

	/* 转换特殊字符，将<>以及双引号等转换
	 * @author  hecj
	 * @date    2012-8-17 下午01:08:12
	 * @param
	 * @return
	 * @throws
	 */
	public String changeSpecialWords(String str){
		str=str.replace("&lt;", "<");
		str=str.replace("&gt;", ">");
		str=str.replace("&quot;", "\"");
		str=str.replace("&amp;", "&");
		return str;
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
	    /*String td1="30%";
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
		if(isUseCASign.equals("1")){
			td4 = 120;
			trString = trString + "<td class=\"td5\"  align=\"center\" width=\""+td5+"\" >"+
			"<strong>CA签名</strong>"+
			"</td>";
		}
		trString = trString +"</tr>";
		for (int i = 0; i < listAllAnnal.size(); i++) {
			List<Object[]> annalTempL = listAllAnnal.get(i);
			String title = "";
			if (i != 0) {
				title = "<br/>";
			}
			for (int j = 0; j < annalTempL.size(); j++) {
				Object[] objs = annalTempL.get(j);
				if (j == 0) {
					title = title + "<font style=\"font: bold;color:blue;\">"
							+ objs[9].toString() + "</font>";
					if (objs[12] == null) {
						title = title + "(<font color=\"red\">正在办理</font>)";
					}
					int colspan = 5;
					if (isUseCASign.equals("1")) {
						colspan = 6;
					}
					tableString = tableString
							+ "<tr class=\"biao_bg1\" style=\"BACKGROUND-COLOR:#FFFFFF;\">"
							+ "<td align=\"left\"  colspan=\"" + colspan
							+ "\">" + title + "</td>" + "</tr>";
					tableString = tableString + trString;
				}
				if (objs[1] != null) {
					tableString = tableString
							+ "<tr class=\"biao_bg1\">"
							+ "<td align=\"left\">"
							+ objs[1]
							+ "</td>"
							+ "<td align=\"left\">"
							+ objs[4]
							+ "</td>"
							+ "<td align=\"left\" title='"
							+ (objs[5] == null ? "" : objs[5])
							+ "'>"
							+ (objs[8] == null ? "" : objs[8])
							+ "</td>"
							+ "<td align=\"left\">"
							+ (objs[6] == null ? ""
									: new java.text.SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss")
											.format((Date) objs[6])) + "</td>";

					if (isUseCASign.equals("1")) {
						tableString = tableString + "<td align=\"left\">"
								+ (objs[7] == null ? "" : objs[7]) + "</td>";
					}
					tableString = tableString + "</tr>";
				}

			}
		}
		tableString = tableString+"</table><br/>";*/
		
		String tableString = "<table>";
		for (int i = 0; i < listAllAnnal.size(); i++) {
			List<Object[]> annalTempL = listAllAnnal.get(i);
			String title = "";
			
			tableString=tableString+"<tr><td style=\"border-bottom:0px solid #bed5e6;\">";
			for (int j = annalTempL.size()-1; j >=0; j--) {
				Object[] objs = annalTempL.get(j);
				if (j == annalTempL.size()-1) {
					title = title + "<font style=\"font: bold;color:blue;\">"
							+ objs[9].toString() + "</font>";
					if (objs[12] == null) {
						title = title + "(<font color=\"red\">正在办理</font>)";
					}
					int colspan = 2;
					
					tableString = tableString
							+ "<table width=\"100%\"><tr class=\"biao_bg1\" style=\"BACKGROUND-COLOR:#FFFFFF;\">"
							+ "<td align=\"left\"  colspan=\"" + colspan
							+ "\">" + title + "</td></tr></table>";
				}
				if(j!=annalTempL.size()-1){
					tableString = tableString+"<br/>";
				}
				if (objs[1] != null) {
					tableString = tableString
							+ "<table><tr class=\"biao_bg1\"><td nowrap=\"nowrap\">处理时间:</td>"
							+ "<td align=\"left\">"
							+ (objs[6] == null ? ""
									: new java.text.SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss")
									.format((Date) objs[6]))
							+ "</td></tr>"
							+ "<tr><td>任务名称:</td><td align=\"left\">"
							+ objs[1]
							+ "</td></tr>"
							+ "<tr><td>处理人:</td>"
							+ "<td align=\"left\">"
							+ objs[4] + "</td></tr><tr><td>处理意见:</td><td align=\"left\" title='"
							+ (objs[5] == null ? "" : objs[5])
							+ "'>"
							+ (objs[8] == null ? "" : objs[8])
							+ "</td></tr>";

					tableString = tableString + "</tr></table>";
				}
			}
			tableString=tableString+"</td></tr>";
		}
		tableString = tableString+"</table>";
		request.setAttribute("tableString", tableString);
	}
	
	/**
	
	/**
	 * 将表单信息组装成table格式的内容
	 * @Description:TODO
	 * @Param String taskId 任务ID
	 * @Author:hecj
	 * @Return
	 * @Date 2012-2-21 上午11:45:18 
	 * @Throws
	 */
	public String formInfoToHtml(String taskId,String bussinessId,String formId)throws ServiceException,SystemException,Exception{
		if (!"0".equals(bussinessId)) {
			String[] args = bussinessId.split(";");
			String tableName = args[0];
			String pkFieldName = args[1];
			String pkFieldValue = args[2];
			List<EFormComponent> formList=new ArrayList<EFormComponent>();
			StringBuffer formHtml=new StringBuffer();
			try {
				Map<String,EFormComponent> mapComponent = eformService.getFieldInfo(formId);
				if(mapComponent.isEmpty()) {
					throw new SystemException("表单[" + formId + "]未绑定任何数据库字段！");
				}
				mapComponent.remove(IEFormService.MAINTABLENAME);
				Set<Map.Entry<String, EFormComponent>> entrys = mapComponent.entrySet();
				List<String> columns = new ArrayList<String>(entrys.size());
				String contentFieldName = null;//正文字段  这里只考虑只有一个正文的情况
				for(Map.Entry<String, EFormComponent> entry : entrys) {
					String fieldName = entry.getKey();
					if(fieldName == null || fieldName.length() == 0) {
						continue;
					}
					EFormComponent eformField = entry.getValue(); 
					if("Office".equals(eformField.getType())) {
						contentFieldName = eformField.getFieldName();
					} else if("Strong.Form.Controls.ComboxBox".equals(eformField.getType())) {//下拉控件
						String items = eformField.getItems();
						if(items.indexOf(";") == -1) {//下拉列表数据是从数据字典中读取
							if(eformField.getSelTableName() != null && !"".equals(eformField.getSelTableName()) && 
									eformField.getSelCode() != null && !"".equals(eformField.getSelCode()) && 
									eformField.getSelName() != null && !"".equals(eformField.getSelName())) { 
								StringBuilder query = new StringBuilder("select ").append(eformField.getSelCode())
								.append(",").append(eformField.getSelName()).append(" from ").append(eformField.getSelTableName())
								.append(" where ").append(eformField.getSelFilter()); 
								List lst = jdbcTemplate.queryForList(query.toString());
								StringBuilder builderItems =  new StringBuilder();
								if(!lst.isEmpty()) {
									for(int i=0;i<lst.size();i++) {
										Map map = (Map)lst.get(i);
										builderItems.append(map.get(eformField.getSelCode())).append(",")
											.append(map.get(eformField.getSelName())).append(",")
											.append(map.get(eformField.getSelCode())).append(";");
									}
								}
								eformField.setItems(builderItems.toString());
							}
						} 
					}
					columns.add(fieldName);
				}
				if(columns.size()>0){
					String strColumns = StringUtils.join(columns, ",");
					StringBuilder sqlQuery = new StringBuilder("select ");
					sqlQuery.append(strColumns).append(" from ").append(tableName);
					sqlQuery.append(" where ").append(pkFieldName).append(" ");
					sqlQuery.append("=").append("'").append(pkFieldValue).append("'");
					Map result = jdbcTemplate.queryForMap(sqlQuery.toString());
					if(result != null && !result.isEmpty()) {
						Set keySet = result.keySet();
						for(Iterator it=keySet.iterator();it.hasNext();) {
							String key = (String)it.next();
							Object value = result.get(key);
							if(key.equals(contentFieldName)) {//得到正文
								
							} else {
								EFormComponent component = mapComponent.get(key);
								
							}
						}
					}
				}
				if(columns==null||columns.size()==0){			//如果没有表单信息
					return formHtml.toString();
				}
				
				formHtml.append("<table width='100%'>");
				for(EFormComponent eform:formList){
					if(eform.isPK()){		//如果是主键则直接返回
						continue;
					}
					formHtml.append(this.createComponent(eform).toString());
				}
				formHtml.append("</table>");
				return formHtml.toString();
			} catch (Exception e) {
				throw new SystemException(e);
			}
		} else {
			throw new SystemException("流程数据不存在或已删除。");
		}
	}
	
	
	/*
	 * 
	 * Description:组装表单信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 16, 2010 2:28:20 PM
	 */
	public String generateFormInfo(String taskId) throws ServiceException,SystemException,Exception{
		StringBuffer formHtml=new StringBuffer();
		List<EFormComponent> list =this.getFieldInfo(taskId, false);
		if(list==null||list.size()==0){			//如果没有表单信息
			return formHtml.toString();
		}
		if(list.size()==1&&list.get(0).isPK()){	//表单只有主键信息
			formHtml.append("<table width='100%'><tr><td></td></tr></table>");
			return formHtml.toString();
		}
		formHtml.append("<table width='100%'>");
		for(EFormComponent eform:list){
			if(eform.isPK()){		//如果是主键则直接返回
				continue;
			}
			formHtml.append(this.createComponent(eform).toString());
		}
		formHtml.append("</table>");
		return formHtml.toString();
	}
	
	/*
	 * 
	 * Description:组装表单信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 16, 2010 2:28:20 PM
	 */
	public String generateFormInfo(String taskId,String view) throws ServiceException,SystemException,Exception{
		StringBuffer formHtml=new StringBuffer();
		List<EFormComponent> list =this.getFieldInfo(taskId, false);
		if(list==null||list.size()==0){			//如果没有表单信息
			return formHtml.toString();
		}
		if(list.size()==1&&list.get(0).isPK()){	//表单只有主键信息
			formHtml.append("<table width='100%'><tr><td></td></tr></table>");
			return formHtml.toString();
		}
		formHtml.append("<table width='100%'>");
		for(EFormComponent eform:list){
			if(eform.isPK()||!eform.isVisible() ){		//如果是主键则直接返回
				continue;
			}
			formHtml.append(this.createComponent(eform,view).toString());
		}
		formHtml.append("</table>");
		return formHtml.toString();
	}
	
	/**
	 * 获取公文正文内容
	 * @param taskId
	 * @return
	 */
	public String getDocContent(String taskId,HttpServletRequest request){
		String content ="";
		try{
			String strNodeInfo = workflowService.getNodeInfo(taskId);
			String[] arrNodeInfo = strNodeInfo.split(",");
			String bussinessId = arrNodeInfo[2];			//业务数据id
			String formId = arrNodeInfo[3];					//表单模板id
			List<EFormField> fieldList = getFormTemplateFieldList(formId);
			EFormField eformField = null;
			for(EFormField field : fieldList) {
				if("Office".equals(field.getType())) {//找到OFFICE控件类型
					eformField = field;
					break;
				}
			}
			if(eformField == null) {
				throw new SystemException("表单中不存在正文。");
			}else{
				content=content+eformField.getFieldname()+">"+bussinessId;
				
				/*BASE64Decoder decode = new BASE64Decoder(); 
		        byte [] b = decode.decodeBuffer(content);*/
				
				
				
			}
		}catch(Exception ex){
			content="-1";
		}
		return content;
	}
	
	
	 /* 
	 * Description:生成控件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 16, 2010 6:46:30 PM
	 */
	public StringBuilder createComponent(EFormComponent eform,String isView) {
		String dateFormat="";
		StringBuilder component = new StringBuilder();
		if(eform.getType().equals("Strong.Form.Controls.Edit")||eform.getType().equals("Strong.Form.Controls.ComboxBox")
				||eform.getType().equals("TEFMemo")||eform.getType().equals("Strong.Form.Controls.DateTimePicker")){
		}else{
			return component;
		}
		String filedValue=eform.getValue()== null?"":eform.getValue();
		component.append("<tr class='sec'>").append("<td align='right' width='35%'>")
			.append("".equals(eform.getCaption())?eform.getLable():eform.getCaption()).append(eform.isRequired()?"<font style='vertical-align: middle;color:red'>*</font>":"")
			.append("：</td>").append("<td width='65%'>");
		if(eform.getType().equals("Strong.Form.Controls.Edit")||eform.getType().equals("Strong.Form.Controls.DateTimePicker")) {//单行|日期控件
			if(eform.getType().equals("Strong.Form.Controls.DateTimePicker")){		//如果为日期控件并且日期值不为空
				component.append("<input type='text' style='width:92%;' name='").append(eform.getFieldName()).append("'");
				dateFormat=this.tranDateFormat(eform.getDateFormat());
				SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
				if("".equals(filedValue)){		//pengxq修改
					filedValue=sdf.format(new Date());
				}
				try {
					Date date=sdf.parse(filedValue);
					filedValue=sdf.format(date);
					component.append(" value='").append(filedValue).append("'")
					.append(" readonly='readonly'").append("/>");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				String rows="1";
				if(filedValue.length()>8){
					/*if(filedValue.length()>8 && filedValue.length()<=15){
						rows="2";
					}else if(filedValue.length()>15 && filedValue.length()<=23){
						rows="3";
					}else if(filedValue.length()>23 && filedValue.length()<=30){
						rows="4";
					}else{
						rows="5";
					}*/
					rows=String.valueOf((filedValue.length()/8)+1);
					component.append("<textarea style='width:92%;' rows='"+rows+"' name='").append(eform.getFieldName()).append("'")
					.append(" readonly='readonly'").append(">")
					.append(filedValue)
					.append("</textarea>");
				}else{
					component.append("<input type='text' style='width:92%;' name='").append(eform.getFieldName()).append("'");
					component.append(" value='").append(filedValue).append("'")
					.append(" readonly='readonly'").append("/>");
				}
			}
			
		}else if(eform.getType().equals("TEFMemo")){
			component.append("<textarea style='width:92%;' rows='3' name='").append(eform.getFieldName()).append("'")
				.append(" readonly='readonly'").append(">")
				.append(filedValue)
				.append("</textarea>");
		}else if(eform.getType().equals("Strong.Form.Controls.ComboxBox")) {
			component.append("<select name='").append(eform.getFieldName()).append("'")
				.append(" disabled='disabled'").append(">");
			String items = eform.getItems();
			String[] itemData = items.split(";");
			for(String data : itemData) {
				if(!"".equals(data)) {
					String[] datas = data.split(",");
					if(filedValue.equals(datas[0].toString())) {
						component.append("<option value='").append(datas[2]).append("' selected='selected'>").append(datas[2]).append("</option>"); 
					}else{
						component.append("<option value='").append(datas[2]).append("'>").append(datas[2]).append("</option>"); 
					}
				}
			}
			component.append("</select>");
		}
		component.append("</td>").append("</tr>\n");
		return component;
	}
	
	/*
	 * 
	 * Description:生成控件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 16, 2010 6:46:30 PM
	 */
	public StringBuilder createComponent(EFormComponent eform) {
		String dateFormat="";
		StringBuilder component = new StringBuilder();
		if(eform.getType().equals("Strong.Form.Controls.Edit")||eform.getType().equals("Strong.Form.Controls.ComboxBox")
				||eform.getType().equals("TEFMemo")||eform.getType().equals("Strong.Form.Controls.DateTimePicker")){
		}else{
			return component;
		}
		String filedValue=eform.getValue()== null?"":eform.getValue();
		component.append("<tr class='sec'>").append("<td align='right' width='35%'>")
			.append("".equals(eform.getCaption())?eform.getLable():eform.getCaption()).append(eform.isRequired()?"<font style='vertical-align: middle;color:red'>*</font>":"")
			.append("：</td>").append("<td width='65%'>");
		if(eform.getType().equals("Strong.Form.Controls.Edit")||eform.getType().equals("Strong.Form.Controls.DateTimePicker")) {//单行|日期控件
			component.append("<input type='text' style='width:90%;' name='").append(eform.getFieldName()).append("'");
			if(eform.getType().equals("Strong.Form.Controls.DateTimePicker")){		//如果为日期控件并且日期值不为空
				dateFormat=this.tranDateFormat(eform.getDateFormat());
				SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
				if("".equals(filedValue)){		//pengxq修改
					filedValue=sdf.format(new Date());
				}
				try {
					Date date=sdf.parse(filedValue);
					filedValue=sdf.format(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			component.append(" value='").append(filedValue).append("'")
				.append(eform.isReadonly()?" readonly='readonly'":"").append("/>");
		}else if(eform.getType().equals("TEFMemo")){
			component.append("<textarea style='width:90%;' rows='3' name='").append(eform.getFieldName()).append("'")
				.append(eform.isReadonly()?" readonly='readonly'":"").append(">")
				.append(filedValue)
				.append("</textarea>");
		}else if(eform.getType().equals("Strong.Form.Controls.ComboxBox")) {
			component.append("<select name='").append(eform.getFieldName()).append("'")
				.append(eform.isReadonly()?" readOnly='readOnly'":"").append(">");
			String items = eform.getItems();
			String[] itemData = items.split(";");
			for(String data : itemData) {
				if(!"".equals(data)) {
					String[] datas = data.split(",");
					if(filedValue.equals(datas[0].toString())) {
						component.append("<option value='").append(datas[0]).append("' selected='selected'>").append(datas[0]).append("</option>"); 
					}else{
						component.append("<option value='").append(datas[0]).append("'>").append(datas[0]).append("</option>"); 
					}
				}
			}
			component.append("</select>");
		}
		component.append("</td>").append("</tr>\n");
		return component;
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jan 4, 2011 11:44:43 AM
	 */
	public String tranDateFormat(String dateFormat){
		if(dateFormat.indexOf("年")+1==dateFormat.length()){
			dateFormat=dateFormat.replace("年", "");
		}else{
			dateFormat=dateFormat.replace("年", "-");
		}
		if(dateFormat.indexOf("月")+1==dateFormat.length()){
			dateFormat=dateFormat.replace("月", "");
		}else{
			dateFormat=dateFormat.replace("月", "-");
		}
		dateFormat=dateFormat.replace("日", "");
		
		if(dateFormat.indexOf("时")+1==dateFormat.length()){
			dateFormat=dateFormat.replace("时", "");
		}else{
			dateFormat=dateFormat.replace("时", ":");
		}
		if(dateFormat.indexOf("分")+1==dateFormat.length()){
			dateFormat=dateFormat.replace("分", "");
		}else{
			dateFormat=dateFormat.replace("分", ":");
		}
		dateFormat=dateFormat.replace("秒", "");
		
		dateFormat=dateFormat.replace(" ", "");
		if(dateFormat.length()>10){
			dateFormat=dateFormat.substring(0,10)+" "+dateFormat.substring(10,dateFormat.length());
		}
		return dateFormat;
	}
	
	/*
	 * 
	 * Description:更新表单信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 17, 2010 4:59:43 PM
	 */
	public String updateFormInfo(String updateSql)throws ServiceException,SystemException,Exception{
		String result="";
		executeJdbcUpdate(updateSql);
		return result;
	}
	
	public void execuSQL(String sql){
		Connection con = super.getConnection();
		Statement smt=null;
		ResultSet rs = null;
		FileOutputStream output=null;
		InputStream is = null;
		try {
			smt = con.createStatement();
			smt.execute(sql);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try {
				if(is!=null){
					is.close();
				}
				if(output!=null){
					output.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(smt!=null){
					smt.close();
				}
				if(con!=null && !con.isClosed()){
					con.close();
				}
				
			}catch (Exception e){	
				e.printStackTrace();
				throw new SystemException("关闭记录集异常！",e);
			}	
		}
	}
	
	/*
	 * 
	 * Description:保存表单信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 16, 2010 8:44:49 PM
	 */
	public String walidateFormInfo(HttpServletRequest request,String taskId)throws ServiceException,SystemException,Exception{
		String message="";
		String dateFormat="";
		String reg="^[1-9]+\\d*|0";   
		List<EFormComponent> list =getFieldInfo(taskId, false);
		if(list==null||list.size()==0){		//如果没有表单信息
			return message;
		}
		String[] info =getFormIdAndBussinessIdByTaskId(taskId);
		String infos[]=info[0].split(";");	//表名|主键|主键值
		StringBuffer sql=new StringBuffer("update ").append(infos[0]).append(" set ");
		for(EFormComponent eform:list){
			String filedName=eform.getFieldName();
			String value=request.getParameter(filedName)==null?"":request.getParameter(filedName);
			if(eform.isRequired()&&("".equals(value))){			//必填验证
				message="请填写"+eform.getLable();
				return message;
			}
			if("2".equals(eform.getValueType())&&!"".equals(value)&&!startCheck(reg,value)){//数字验证
				message=eform.getLable()+"必须输入整数！";
				return message;
			}
			if(!"".equals(value)&&eform.getType().equals("Strong.Form.Controls.DateTimePicker")){		//日期验证
				try{
					dateFormat=this.tranDateFormat(eform.getDateFormat());
					//dateFormat="YYYY-MM-DD";
					SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
					sdf.parse(value);
				}catch(ParseException e){
					message=eform.getLable()+"日期格式有误，正确格式为："+dateFormat+"！";
					return message;
				}
			}
			if(eform.getType().equals("TEFMemo")||eform.getType().equals("Strong.Form.Controls.DateTimePicker")){				//文本域进行长度验证		
				/*if(value.length()>Integer.parseInt(eform.getMaxLength())){	//长度验证
					message=eform.getLable()+"不能超过"+eform.getMaxLength()+"个字符！";
					return message;
				}*/					
			}
			if(eform.getType().equals("Strong.Form.Controls.DateTimePicker")){		//日期控件
				String className = eform.getColumnClassName();
				dateFormat="YYYY-MM-DD";
				if("java.lang.String".equals(className)) {
					sql.append(filedName).append("='").append(value).append("',");	
				}else{
					sql.append(filedName).append("=to_date('").append(value).append("','").append(dateFormat).append("'),");	
				}
			}else{
				sql.append(filedName).append("='").append(value).append("',");	
			}
		}
		sql=new StringBuffer(sql.toString().substring(0,sql.toString().length()-1));
		sql.append(" where ").append(infos[1]).append("='").append(infos[2]).append("'");
		message=sql.toString();
		return message;
	}
	
	/*
	 * 
	 * Description: 验证
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 17, 2010 9:53:36 AM
	 */
	public boolean startCheck(String reg,String value)   
    {   
        boolean tem=false;   
        Pattern pattern = Pattern.compile(reg);   
        Matcher matcher=pattern.matcher(value);   
        tem=matcher.matches();   
        return tem;   
    }  
	
	/*
	 * 
	 * Description:展现附件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 17, 2010 7:20:30 PM
	 */
	public String createAttachHtml(HttpServletRequest request,String[] info,String taskId)throws ServiceException,SystemException,Exception{
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		InputStream is =null;
		String rtn="";
		StringBuffer html=new StringBuffer();
		try {
			
			String root=request.getContextPath();
			String path=request.getRealPath("/")+"doc"+File.separator;	//工程所在磁盘路径
			String infos[]=info[0].split(";");
			//List<String[]> attachList=getAttachsBySenddocId(path,infos[2],"android");
			
			
			String pkFieldValue="";
			if(infos[2]!=null&&!"".equals(infos[2].toString())){
				pkFieldValue=infos[2].toString();
			}
			
			List<WorkflowAttach> workflowAttachs = workflowAttachManager.getWorkflowAttachsByDocId(pkFieldValue);
			//Properties formProperties =new Properties();
			//formProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("form.properties"));
			//String urlRoot=formProperties.getProperty("attach.service.url");
			
			for(WorkflowAttach attach:workflowAttachs){
				if(attach.getAttachContent()!=null){
					
					is = FileUtil.ByteArray2InputStream(attach.getAttachContent());
					String fileName=attach.getAttachName();;
					String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
					String filepath = AttachmentHelper.saveFile(root, is, fileName);
					//html.append("<img src='"+root+"/oa/image/work/yes.gif'/><a href='"+urlRoot+"/work/mobile!viewAttach.action?attachId="+attach.getDocattachid()+"&taskId="+taskId+"'>").append(attach.getAttachName()).append("</a><br>");
					html.append("<img src='"+root+"/oa/image/work/yes.gif'/><a href='"+filepath+"'>").append(attach.getAttachName()).append("</a><br>");
				}
			}
			/*for(WorkflowAttach attach:workflowAttachs){
				html.append("<img src='"+root+"/oa/image/work/yes.gif'/><a href='"+root+"/work/work!downloadAttach.action?attchId="+attach.getDocattachid()+"'>").append(attach.getAttachName()).append("</a><br>");
			}*/
			String isExistOffice=getDocContent(path,info);
			if(!"".equals(isExistOffice)){
				String docPath=root+"/doc/"+infos[2]+".doc";
				html.append("<img src='"+root+"/oa/image/work/yes.gif'/><a href='"+docPath+"'>草稿.doc</a>\n");
			}
		}catch(Exception e){
			
		}
		return html.toString();
	}
	
	/*
	 * 
	 * Description:获取公文附件并生成附件文件
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 3, 2010 10:20:33 AM
	 */
	public List<String[]> getAttachsBySenddocId(String path,String senddocId,String sysType)throws ServiceException,SystemException{
		FileOutputStream output=null;
		ResultSet rs = null;
		InputStream is = null;
		String totalPath="";
		try{
			List<String[]> attachLst = new ArrayList<String[]>();
			String sql = "select DOCATTACHID,ATTACH_NAME,ATTACH_CONTENT from T_DOCATTACH t where t.DOC_ID='"+senddocId+"'";
			rs = super.executeJdbcQuery(sql);
			while(rs.next()){
				String[] docAttach=new String[2];
				docAttach[0]=rs.getString(1);
				docAttach[1]=rs.getString(2);
				attachLst.add(docAttach);
				if("android".equals(sysType)){
					String ss=docAttach[1].substring(docAttach[1].lastIndexOf("."),docAttach[1].length());
					totalPath=path+docAttach[0]+ss;
					File file=new File(totalPath);
					file.delete();
					output=new FileOutputStream(file);
					is = rs.getBinaryStream("ATTACH_CONTENT");
					int input = 0;
					byte[] buf = new byte[8192];
					while((input=is.read(buf))!=-1){
						output.write(buf, 0, input);
					}
				}
			}
			return attachLst;
		}catch(Exception e){
			throw new SystemException("查询公文附件出错了！");
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(is!=null){
					is.close();
				}
				if(output!=null){
					output.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 18, 2010 5:12:29 PM
	 */
	public String downloadAttachment(String attchId,HttpServletRequest request,HttpServletResponse response)throws ServiceException,SystemException{
		ResultSet rs = null;
		InputStream is = null;
		OutputStream outputStream=null;
		BufferedInputStream br=null;
		String message="";
		try{
			
			//InputStream is = null;
			InputStream isprop = Thread.currentThread().getContextClassLoader().getResourceAsStream("webserviceaddress.properties");
			Properties prop = new Properties();
			prop.load(isprop);
			String urlpath=prop.getProperty("attach.service.url");
			
			WorkflowAttach attach = workflowAttachManager.get(attchId);
			byte[] attachContent = attach.getAttachContent();
			if (attachContent != null) {
				is = FileUtil.ByteArray2InputStream(attachContent);
			}
			
			
			//WorkflowAttach attach = workflowAttachManager.getWithoutContent(attchId);
			if(attach != null ) {
				
				
					//String attName = attach.getAttachName();
					//List<Item> aitems = new ArrayList<Item>();//附件显示项
					//is = FileUtil.ByteArray2InputStream(attach.getAttachContent());
					/*String path = AttachmentHelper.saveFile(root, is, attName);
					logger.info("生成附件：" + path);
					Item aItem = new Item("string",path);//附件路径
					aitems.add(aItem);
					aItem = new Item("string",attName);//附件名称
					aitems.add(aItem);
					aItem = new Item("string",(AttachmentHelper.getFileSize(attach.getAttachContent().length)));//附件大小
					aitems.add(aItem);
					attachment.setItems(aitems);
					atts.add(attachment);*/
					String fileName=attach.getAttachName();;
					String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
					//is = rs.getBinaryStream("ATTACH_CONTENT");
					
					response.reset();
					response.setCharacterEncoding("utf-8");
					response.setContentType("application/x-msdownload");
					response.setHeader("Content-Type",fileType);
					response.addHeader("Content-Disposition", "attachment;filename=" +
						    new String(fileName.getBytes("gb2312"),"iso8859-1"));
					outputStream = new BufferedOutputStream(response.getOutputStream());
					br = new BufferedInputStream(is);
					byte[] buf = new byte[1024];
					int len = 0;
					while((len = br.read(buf)) >0){
						outputStream.write(buf,0,len);
					}
				
			}
			
			/*String sql = "select DOCATTACHID,ATTACH_NAME,ATTACH_CONTENT from T_DOCATTACH t where t.DOCATTACHID='"+attchId+"'";
			rs = super.executeJdbcQuery(sql);
			if(rs.next()){
				String fileName=rs.getString(2);
				String fileType=fileName.substring(fileName.indexOf(".")+1,fileName.length());
				is = rs.getBinaryStream("ATTACH_CONTENT");
				response.reset();
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Type",fileType);
				response.addHeader("Content-Disposition", "attachment;filename=" +
					    new String(fileName.getBytes("gb2312"),"iso8859-1"));
				outputStream = new BufferedOutputStream(response.getOutputStream());
				br = new BufferedInputStream(is);
				byte[] buf = new byte[1024];
				int len = 0;
				while((len = br.read(buf)) >0){
					outputStream.write(buf,0,len);
				}
			}*/
			return message;
		}catch(Exception e){
			throw new SystemException("查询公文附件出错了！");
		}finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(outputStream!=null){
					outputStream.close();
				}
				if(br!=null){
					br.close();
				}
				if(is!=null){
					is.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 2, 2010 10:17:00 AM
	 */
	public String getDocContent(String path,String[] infos){
		String fields="";
		String formId=infos[1];
		String[] talbeAndKey=infos[0].split(";");
		String tableName=talbeAndKey[0];
		String pkey=talbeAndKey[1];
		String pvalue=talbeAndKey[2];
		List<EFormField> list=eformService.getFormTemplateComponents(formId);
		for(EFormField filed:list){
			if(filed==null){
				continue;
			}
			String type=filed.getType().trim();
			if("TEFOffice".equals(type)){
				String typeName=filed.getFieldname();
				if(typeName!=null&&!"".equals(typeName)){
					fields=typeName;
					break;
				}
			}
		}
		if("".equals(fields)){
			return fields;
		}
		StringBuffer queryStr=new StringBuffer("select ")	
			.append(fields)
			.append(" from ")
			.append(tableName)
			.append(" where ")
			.append(pkey)
			.append("='")
			.append(pvalue)
			.append("'");
		Connection con = super.getConnection();
		Statement smt=null;
		ResultSet rs = null;
		FileOutputStream output=null;
		InputStream is = null;
		try {
			smt = con.createStatement();
			rs= smt.executeQuery(queryStr.toString());		
			String totalPath=path+pvalue+".doc";
			File file=new File(totalPath);
			file.delete();
			output=new FileOutputStream(file);
			if(rs!=null&&rs.next()){
				is = rs.getBinaryStream(1);
				if(is != null) {
					int input = 0;
					byte[] buf = new byte[8192];
					while((input=is.read(buf))!=-1){
						output.write(buf, 0, input);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(is!=null){
					is.close();
				}
				if(output!=null){
					output.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(smt!=null){
					smt.close();
				}
				if(con!=null && !con.isClosed()){
					con.close();
				}
				
			}catch (Exception e){	
				e.printStackTrace();
				throw new SystemException("关闭记录集异常！",e);
			}	
		}
		return fields;
	}
	
	/**
	 * 保存业务数据【暂时用于IPP请求OA】
	 * @author:邓志城
	 * @date:2009-7-7 上午10:54:00
	 * @return
	 * @throws SystemException
	 */
	public String saveBusinessData(String info,
								   DataHandler[] attachments,
								   String[] fileNames)throws SystemException{
		StringBuffer sql = new StringBuffer("insert into T_OA_IPP_WORKFLOW(WORKFLOWID,MANNAME,MANCARDTYPE,MANCARDNUMBER,MANBIRTHDAY,MANCOUNTRY,MANNATION,MANJOB,MANEDUCATION,MANPERMENTTYPE,MANMARRIAGE,MANPROVINCE,MANCITY,MANMOBILE,NAME,CARDTYPE,CARDNUMBER,BIRTHDAY,NATION,JOB,EDUCATION,PERMENTTYPE,MARRIAGE,PROVINCE,CITY,MOBILE,COUNTRY,FEEDBACKCODE) ");
		//产生UUID
		UUIDGenerator gen = new UUIDGenerator();
		String uuid = gen.generate().toString();
		JSONObject objInfo = JSONObject.fromObject(info);
		/**           男方信息                      **/
		String manName = objInfo.getString("manName");//姓名
		String manCardType = objInfo.getString("manCardType");//证件类型
		String manCardNumber = objInfo.getString("manCardNumber");//证件编号
		String manBirthday = objInfo.getString("manBirthday");//出生日期
		String manCountry = objInfo.getString("manCountry");//国籍
		String manNation = objInfo.getString("manNation");//民族
		String manJob = objInfo.getString("manJob");//职业
		String manEducation = objInfo.getString("manEducation");//文化程度
		String manPermantType = objInfo.getString("manPermantType");//户籍类别
		String manMarriage = objInfo.getString("manMarriage");//婚姻状况
		String manProvince = objInfo.getString("manProvince");//户口所在地
		String manCity = objInfo.getString("manCity");//户口所在市
		String manMobile = objInfo.getString("manMobile");//电话
		/**     女方信息                      **/
		String name = objInfo.getString("name");
		String cardType = objInfo.getString("cardType");
		String cardNumber = objInfo.getString("cardNumber");
		String birthday = objInfo.getString("birthday");
		String country = objInfo.getString("country");
		String nation = objInfo.getString("nation");
		String job = objInfo.getString("job");
		String education = objInfo.getString("education");
		String permantType = objInfo.getString("permantType");
		String marriage = objInfo.getString("marriage");
		String province = objInfo.getString("province");
		String city = objInfo.getString("city");
		String mobile = objInfo.getString("mobile");
		//咨询反馈号
		String feedBackCode = objInfo.getString("feedBackCode");
		
		sql.append("values('"+uuid+"','"+manName+"','"+manCardType+"','"+manCardNumber+"','"+manBirthday+"','"+manCountry+"','"+manNation+"','"+manJob+"','"+manEducation+"','"+manPermantType+"','"+manMarriage+"','"+manProvince+"','"+manCity+"','"+manMobile+"','"+name+"','"+cardType+"','"+cardNumber+"','"+birthday+"','"+nation+"','"+job+"','"+education+"','"+permantType+"','"+marriage+"','"+province+"','"+city+"','"+mobile+"','"+country+"','"+feedBackCode+"')");
		//执行插入操作
		super.executeJdbcUpdate(sql.toString());
		saveAttachments(uuid, attachments, fileNames);
		//返回业务数据标示
		String ret = "T_OA_IPP_WORKFLOW;WORKFLOWID;"+uuid;
		return ret;
	}

	/**
	 * 根据ID获取咨询反馈号、手机号码
	 * @author:邓志城
	 * @date:2009-7-15 上午11:04:55
	 * @param id
	 * @return
	 * @throws SystemException
	 */
	public String[] getFeedBackCode(String id)throws SystemException{
		String[] ret = new String[2];
		try {
			String sql = "select FEEDBACKCODE,MOBILE from T_OA_IPP_WORKFLOW where WORKFLOWID='"+id+"'";
			ResultSet rs = super.executeJdbcQuery(sql);
			if(rs.next()){
				ret[0] = rs.getString("FEEDBACKCODE");
				ret[1] = rs.getString("MOBILE");
			}
		} catch (DAOException e) {
			e.printStackTrace();
			throw new SystemException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(e);
		}
		return ret;
	}
	
	/*
	 * 获得所有的流程类型
	 */
	public List<Object[]> getAllProcessTypeLists()throws SystemException,ServiceException{
		try {
			List<Object[]> typeList = workflow.getAllProcessTypeList();
			return typeList;
		}catch (ServiceException e) {
			throw new ServiceException(MessagesConst.find_error,new Object[] {"流程类型"});
		}
	}
	
	/******************以上是andorid手机调用方法***********************/
	/*
	 * 
	 * Description:获取指派人员列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Nov 10, 2010 11:54:05 AM
	 */
	public List<String[]> getWapReassignUsers(String nodeId,String taskId,String disLogo,String transitionId)throws ServiceException,SystemException{
		List<String[]> userList=new ArrayList<String[]>();
		if(nodeId != null && !"".equals(nodeId) && !"null".equals(nodeId)){
			userList =this.getWorkflowTaskActors(nodeId, taskId,transitionId); 				//获取待指派的人员列表
		}
		if(userList!=null&&!userList.isEmpty()){
			List<Organization> orgList = userService.getAllDeparments();	//获取所有机构列表
			Map<String,String> orgMap = new HashMap<String, String>();		//将机构信息放入Map中
			for(Organization organization : orgList){
				orgMap.put(organization.getOrgId(), organization==null?"":organization.getOrgName());
			}
			for(String[] info : userList){
				if("processToNext".equals(disLogo)){
					info[0]=info[0]+"|"+nodeId;
				}else{
					info[0]="u"+info[0]+","+info[1];
				}
				info[1]=info[1]+"("+ orgMap.get(info[2])+")";
			}
		}
		return userList;
	}
	
	/**
	 * 根据传入的文件流获取文件头
	 * @author  hecj
	 * @date    2012-8-20 下午04:43:43
	 * @param
	 * @return
	 * @throws
	 */
	public static String bytesToHexString(byte[] src) {  
		StringBuilder stringBuilder = new StringBuilder();  
		if (src == null || src.length <= 0) {  
			return null;  
		}  
		for (int i = 0; i < src.length; i++) {  
			int v = src[i] & 0xFF;  
			String hv = Integer.toHexString(v);  
			if (hv.length() < 2) {  
				stringBuilder.append(0);  
			}  
			stringBuilder.append(hv);  
		}  
		return stringBuilder.toString();  
	} 
	
	/**
	 * 
	 * 常见文件头信息
	 * 
	 * @author  hecj
	 * @date    2012-8-20 下午04:54:48
	 * @param   文件头信息
	 * @return
	 * @throws
	 */
	public String getFileTypeByHeadInfo(String headInfo)
    {     
        String fileType="";
		HashMap<String,String> FILE_TYPE_MAP=new HashMap<String,String>();
		FILE_TYPE_MAP.put("ffd8ffe000104a464946", "jpg"); //JPEG (jpg)     
        FILE_TYPE_MAP.put("89504e470d0a1a0a0000", "png"); //PNG (png)     
        FILE_TYPE_MAP.put("47494638396126026f01", "gif"); //GIF (gif)     
        FILE_TYPE_MAP.put("49492a00227105008037", "tif"); //TIFF (tif)     
        FILE_TYPE_MAP.put("424d228c010000000000", "bmp"); //16色位图(bmp)     
        FILE_TYPE_MAP.put("424d8240090000000000", "bmp"); //24位位图(bmp)     
        FILE_TYPE_MAP.put("424d8e1b030000000000", "bmp"); //256色位图(bmp)     
        FILE_TYPE_MAP.put("41433130313500000000", "dwg"); //CAD (dwg)     
        FILE_TYPE_MAP.put("3c21444f435459504520", "html"); //HTML (html)
        FILE_TYPE_MAP.put("3c21646f637479706520", "htm"); //HTM (htm)
        FILE_TYPE_MAP.put("48544d4c207b0d0a0942", "css"); //css
        FILE_TYPE_MAP.put("696b2e71623d696b2e71", "js"); //js
        FILE_TYPE_MAP.put("7b5c727466315c616e73", "rtf"); //Rich Text Format (rtf)     
        FILE_TYPE_MAP.put("38425053000100000000", "psd"); //Photoshop (psd)     
        FILE_TYPE_MAP.put("46726f6d3a203d3f6762", "eml"); //Email [Outlook Express 6] (eml)       
        FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "doc"); //MS Excel 注意：word、msi 和 excel的文件头一样     
        //FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "vsd"); //Visio 绘图     
        FILE_TYPE_MAP.put("5374616E64617264204A", "mdb"); //MS Access (mdb)      
        FILE_TYPE_MAP.put("252150532D41646F6265", "ps");     
        FILE_TYPE_MAP.put("255044462d312e350d0a", "pdf"); //Adobe Acrobat (pdf)   
        FILE_TYPE_MAP.put("2e524d46000000120001", "rmvb"); //rmvb/rm相同  
        FILE_TYPE_MAP.put("464c5601050000000900", "flv"); //flv与f4v相同  
        FILE_TYPE_MAP.put("00000020667479706d70", "mp4"); 
        FILE_TYPE_MAP.put("49443303000000002176", "mp3"); 
        FILE_TYPE_MAP.put("000001ba210001000180", "mpg"); //     
        FILE_TYPE_MAP.put("3026b2758e66cf11a6d9", "wmv"); //wmv与asf相同    
        FILE_TYPE_MAP.put("52494646e27807005741", "wav"); //Wave (wav)  
        FILE_TYPE_MAP.put("52494646d07d60074156", "avi");  
        FILE_TYPE_MAP.put("4d546864000000060001", "mid"); //MIDI (mid)   
        FILE_TYPE_MAP.put("504b0304140000000800", "zip");    
        FILE_TYPE_MAP.put("526172211a0700cf9073", "rar");   
        FILE_TYPE_MAP.put("235468697320636f6e66", "ini");   
        FILE_TYPE_MAP.put("504b03040a0000000000", "jar"); 
        FILE_TYPE_MAP.put("4d5a9000030000000400", "exe");//可执行文件
        FILE_TYPE_MAP.put("3c25402070616765206c", "jsp");//jsp文件
        FILE_TYPE_MAP.put("4d616e69666573742d56", "mf");//MF文件
        FILE_TYPE_MAP.put("3c3f786d6c2076657273", "xml");//xml文件
        FILE_TYPE_MAP.put("494e5345525420494e54", "sql");//xml文件
        FILE_TYPE_MAP.put("7061636b616765207765", "java");//java文件
        FILE_TYPE_MAP.put("406563686f206f66660d", "bat");//bat文件
        FILE_TYPE_MAP.put("1f8b0800000000000000", "gz");//gz文件
        FILE_TYPE_MAP.put("6c6f67346a2e726f6f74", "properties");//bat文件
        FILE_TYPE_MAP.put("cafebabe0000002e0041", "class");//bat文件
        FILE_TYPE_MAP.put("49545346030000006000", "chm");//bat文件
        FILE_TYPE_MAP.put("04000000010000001300", "mxp");//bat文件
        FILE_TYPE_MAP.put("504b0304140006000800", "docx");//docx文件
        //FILE_TYPE_MAP.put("d0cf11e0a1b11ae10000", "wps");//WPS文字wps、表格et、演示dps都是一样的
        FILE_TYPE_MAP.put("6431303a637265617465", "torrent");
        
          
        FILE_TYPE_MAP.put("6D6F6F76", "mov"); //Quicktime (mov)  
        FILE_TYPE_MAP.put("FF575043", "wpd"); //WordPerfect (wpd)   
        FILE_TYPE_MAP.put("CFAD12FEC5FD746F", "dbx"); //Outlook Express (dbx)     
        FILE_TYPE_MAP.put("2142444E", "pst"); //Outlook (pst)      
        FILE_TYPE_MAP.put("AC9EBD8F", "qdf"); //Quicken (qdf)     
        FILE_TYPE_MAP.put("E3828596", "pwl"); //Windows Password (pwl)         
        FILE_TYPE_MAP.put("2E7261FD", "ram"); //Real Audio (ram)
        Iterator<String> map=FILE_TYPE_MAP.keySet().iterator();//将map的键放入set里面
        while(map.hasNext()){
        	String key=map.next();
        	if(key.toLowerCase().startsWith(headInfo.toLowerCase())||headInfo.toLowerCase().equals(key.toLowerCase())){
        		fileType=FILE_TYPE_MAP.get(key);
        		break;
        	}
        }
        return fileType;
    } 
	
	/**
	 * 保存附件
	 * @author:邓志城
	 * @date:2009-7-9 下午04:05:20
	 * @param fkId
	 * @param attachments
	 * @param fileNames
	 */
	@SuppressWarnings("deprecation")
	private void saveAttachments(String fkId,DataHandler[] attachments,String[] fileNames)throws SystemException{
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		InputStream is = null;
		try{
			//transfer(attachments, fileNames);
			con = super.getConnection();
			//产生UUID
			UUIDGenerator gen = new UUIDGenerator();
			if(attachments!=null && attachments.length>0){
				con.setAutoCommit(false);
				for(int i=0;i<attachments.length;i++){
					String sql = "insert into T_DOCATTACH(DOCATTACHID,DOC_ID,ATTACH_NAME,ATTACH_CONTENT)values(?,?,?,empty_blob())";
					String id = gen.generate().toString();
					psmt = con.prepareStatement(sql);
					psmt.setString(1, id);
					psmt.setString(2, fkId);
					psmt.setString(3, fileNames[i]);
					psmt.executeUpdate();
					sql = "select * from T_DOCATTACH where DOCATTACHID='"+id+"' for update";
					psmt = con.prepareStatement(sql);
					rs = psmt.executeQuery();
					if(rs.next()){
						sql = "update T_DOCATTACH set ATTACH_CONTENT=? where DOCATTACHID=?";
						psmt = con.prepareStatement(sql);
						is = attachments[i].getInputStream();
						psmt.setBinaryStream(1, is, is.available());
						psmt.setString(2, id);
						psmt.executeUpdate();
					}
				}
				con.commit();
			}
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
				
			}
			throw new SystemException("保存附件异常！",e);
		}finally{
			try{
				if(is!=null){
					is.close();
				}
				if(rs!=null){
					rs.close();
				}
				if(psmt!=null){
					psmt.close();
				}
				if(con!=null && !con.isClosed()){
					con.close();
				}
			}catch(Exception e){
				e.printStackTrace();
				throw new SystemException("关闭记录集异常！",e);
			}
		}
	}
	
	/*
	 * 
	 * Description:获取所有流程类型
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 11, 2010 2:45:13 PM
	 */
	public Map<String,String> getProccessTypeIdAndName(Page pageTodo)throws SystemException,ServiceException{
		Map<String,String> typeMap=new HashMap<String,String>();
		List<Object[]> list=pageTodo.getResult();
		try{
			List<Object[]> typeList = workflow.getAllProcessTypeList();
			Object[] typeObj;
			Object[] obj;
			int k;
			for(int i=0;i<typeList.size();i++){
				typeObj=typeList.get(i);
				k=0;
				for(int j=0;list!=null&&j<list.size();j++){
					obj=list.get(j);
					if(typeObj[0].equals(obj[8])){
						k+=1;
					}
				}
				typeMap.put(typeObj[0].toString(), typeObj[1].toString()+"(<font color='red'>"+k+"</font>)");
			}
			return typeMap;
		}catch(Exception e){
			e.printStackTrace();
			throw new SystemException("获取流程类型类表异常！",e);
		}
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 22, 2010 3:47:57 PM
	 */
	public List<Object[]> getProccessTypeIdAndNameList(Page pageTodo)throws SystemException,ServiceException{
		List<Object[]> list=pageTodo.getResult();
		String tempValue;
		String tempValue1;
		List<Object[]> typeAndNameList=new ArrayList<Object[]>();
		Object[] workFlow;
		try{
			List<Object[]> typeList = workflow.getAllProcessTypeList();
			Object[] typeObj;
			Object[] obj;
			int k;
			for(int i=0;i<typeList.size();i++){
				typeObj=typeList.get(i);
				k=0;
				for(int j=0;list!=null&&j<list.size();j++){
					obj=list.get(j);
					tempValue=typeObj[0].toString();	
					tempValue1=obj[8]==null?null:obj[8].toString();
					if(tempValue1!=null&&tempValue.equals(tempValue1)){
						k+=1;
					}
				}
				if(k!=0){
					workFlow=new Object[2];
					workFlow[0]=typeObj[0].toString();
					workFlow[1]=typeObj[1].toString()+"(<font color='red'>"+k+"</font>)";
					typeAndNameList.add(workFlow);
				}
			}
			return typeAndNameList;
		}catch(Exception e){
			e.printStackTrace();
			throw new SystemException("获取流程类型类表异常！",e);
		}
	}

	/*
	 * 
	 * Description:获取当前用户所有代办和在办任务
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Mar 11, 2010 10:48:41 AM
	 */
	@Transactional(readOnly = true)
	public Page getAllTodoWorks(Page page,String workflowType,String businessName,String userName,Date startDate,Date endDate,String isBackSpace) throws SystemException,ServiceException{
		try {
			User curUser = userService.getCurrentUser();//获取当前用户
			String searchType = "all";
			return workflow.getTasksTodo(page, curUser.getUserId(), searchType, workflowType,businessName,userName,startDate,endDate,isBackSpace);
		} catch (ServiceException e) {
			throw new ServiceException(MessagesConst.operation_error,new Object[] {"获取待办工作"});
		}
	}
	
}
