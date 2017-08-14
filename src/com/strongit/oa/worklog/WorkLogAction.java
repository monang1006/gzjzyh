package com.strongit.oa.worklog;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

import com.strongit.oa.attachment.IAttachmentService;
import com.strongit.oa.bo.ToaAttachment;
import com.strongit.oa.bo.ToaDoctemplateGroup;
import com.strongit.oa.bo.ToaWorkLog;
import com.strongit.oa.bo.ToaWorkLogAttach;
import com.strongit.oa.doctemplate.doctempType.DocTempTypeManager;
import com.strongit.oa.util.GlobalBaseData;
import com.strongit.oa.util.LogPrintStackUtil;
import com.strongit.tag.web.grid.stronger.FlexTableTag;
import com.strongmvc.orm.hibernate.Page;
import com.strongmvc.webapp.action.BaseActionSupport;

/**
 * 
 * @company  Strongit
 * @author 	 彭小青
 * @date 	 May 21, 2010 3:34:53 PM
 * @version  2.0.4
 * @comment  工作日志管理 
 * 第一个版本 2010-06-01 10:00
 */
@SuppressWarnings("serial")
@ParentPackage("default")
@Results( { @Result(name = BaseActionSupport.RELOAD, value = "workLog.action", type = ServletActionRedirectResult.class) })
public class WorkLogAction extends BaseActionSupport<ToaWorkLog> {

	private Page<ToaWorkLog> page = new Page<ToaWorkLog>(FlexTableTag.MAX_ROWS, true);
	List<ToaAttachment> attachments=new ArrayList<ToaAttachment>();
	List<ToaDoctemplateGroup> typeList=new ArrayList<ToaDoctemplateGroup>();
	@Autowired private WorkLogManager manager;
	@Autowired private IAttachmentService attchmentService;
	@Autowired private DocTempTypeManager typeManager;
	private ToaWorkLog model = new ToaWorkLog();
	private String workLogId;		// 工作日志ID
	private String attachId;		// 附件ID
	private String delAttachIds;	// 删除附件记录ids
	private File[] file;			// 附件
	private File wordDoc;			//日志说明文档
	private String[] fileFileName;
	private String setDate;			// 指定查看日期
	private String jsonArr;			// 初始化json数组
	private String attachFiles;		// 附件脚本
	private String inputType;		// 视图类型
	private String operateType;		// 操作类型
	private String currentMonth;	// 选择的月份
	private String currentYear;		// 选择的年份
	private String startTime;		// 工作日志开始时间
	private String endTime;			// 工作日志结束时间
	private String wlogStartTime1;

	public WorkLogAction() {
		
	}

	/*
	 * 
	 * Description:工作日志列表
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 5:02:32 PM
	 * @return     
	 */
	public String list() throws Exception {
		getRequest().setAttribute("backlocation",
				getRequest().getContextPath() + "/worklog/workLog.action");
		if(model.getWlogTitle()!=null&&!model.getWlogTitle().equals("")){
			model.setWlogTitle(URLDecoder.decode(model.getWlogTitle(), "utf-8"));
		}
		if(model.getWlogUserName()!=null&&!model.getWlogUserName().equals("")){	//wlogUserName
			//model.setWlogUserName(URLDecoder.decode(model.getWlogUserName(), "utf-8"));
			model.setWlogUserName(URLDecoder.decode(model.getWlogUserName(), "utf-8"));
		}
		
		page = manager.searchByWorkLog(page, model, operateType);
		return SUCCESS;
	}
	
	/*
	 * 
	 * Description:新增、编辑、查看工作日志信息
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 5:02:32 PM
	 * @return     
	 */
	public String input() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		if (workLogId != null&&!"".equals(workLogId)&&!"null".equals(workLogId)) {	//修改或查看工作日志
			model = manager.getWlogById(workLogId); 	// 根据id获取对象
//			model.setWlogTxtCon(HtmlUtils.htmlUnescape(model.getWlogTxtCon()));//对编辑器中的内容，初始化它的段落格式
			Set att = model.getToaWorkLogAttaches();	// 获取附件
			if (att != null) {				//如果有附件
				attachFiles = "";
				Iterator it = att.iterator();
				while (it.hasNext()) {		//循环附件
					ToaWorkLogAttach objs = (ToaWorkLogAttach) it.next();
					ToaAttachment attachment = attchmentService.getAttachmentById(objs.getAttachId());
					if(attachment==null){
						continue;
					}
					attachFiles += "<div id="+attachment.getAttachId()+" style=\"display: \">";
					if(!"view".equals(operateType)){	//查看工作日志
						attachFiles+="<a href=\"javascript:delAttach('"+attachment.getAttachId()+"')\">[删除]</a>";
					}
					attachFiles+="<a href=\"javascript:download('"+ attachment.getAttachId()+"')\">"+ attachment.getAttachName()+"</a>&nbsp;</div>";
				}
			}
		}else {	//新增工作日志，初始化工作日志信息
			model = new ToaWorkLog();
			model.setWlogTitle("无标题");	
			Calendar now= Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(startTime!=null&&!"".equals(startTime)){
				format.parse(startTime);	
			}else{
				String temp=format.format(new Date());	
				format.parse(temp);
			}
			now=format.getCalendar();
			model.setWlogStartTime(format.parse(format.format(now.getTime())));		//默认当天开始
		}
		typeList=typeManager.getAllTypeTemplate();
		if(!"view".equals(operateType)){//添加或修改
//			return "add";
			return "input";
		}else{		//查看
			return "show";
		}
	}
	
	
	@Override
	protected void prepareModel() throws Exception {
		if(workLogId != null&&!"".equals(workLogId)&&!"null".equals(workLogId)){
			model = manager.getWlogById(workLogId); 
		}
	}
	
	/*
	 * 
	 * Description:保存工作日志
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 5:02:32 PM
	 * @return     
	 */
	@Override
	public String save() throws Exception {
		getRequest().setAttribute("backlocation", "javascript:cancel()");
		
		long temp = 0;
		if (file != null) {
			for (int i = 0; i < file.length; i++) {
				if (file[i] != null) {
					if (file[i].length() == 0) {
						addActionMessage("error不能上传空文件，请检查要上传的文件！");
						if(model.getWlogTxtCon()!=null&&!model.getWlogTxtCon().equals("")){
							model.setWlogTxtCon(HtmlUtils.htmlEscape(model.getWlogTxtCon()));
						}
						return "input";
//						return input();
					}
					temp += file[i].length();
				}
			}
			if (temp > 2048000) {
				addActionMessage("error不能上传附件，附件大小超出最大范围！");
//				return input();
				if(model.getWlogTxtCon()!=null&&!model.getWlogTxtCon().equals("")){
					model.setWlogTxtCon(HtmlUtils.htmlEscape(model.getWlogTxtCon()));
				}
				return "input";
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		model.setWlogStartTime(format.parse(wlogStartTime1));
		String msg = manager.saveInpage(model,delAttachIds,file,fileFileName,wordDoc);
		StringBuffer message = new StringBuffer("");
		message.append("{")
		.append("operateType: '").append(operateType).append("',")
		.append("setDate: '").append(setDate).append("',")
		.append("success: '").append(msg).append("'")
		.append("}");
		addActionMessage(message.toString());
//		thhis.renderText(message.toString());

		return "input";
	}
	
	
	
	
	/*
	 * 
	 * Description:删除工作日志
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 5:02:32 PM
	 * @return     
	 */
	public String delete() throws Exception {
		try {
			manager.deleteWorkLog(workLogId);
			addActionMessage("删除成功");
		} catch (Exception e) {
			LogPrintStackUtil.printErrorStack(logger, e);
			renderText(LogPrintStackUtil.errorMessage);
		}
		if ("list".equals(operateType)) {	//列表中删除工作日志
			return null;
		}else{
			return "view";
		}
	}
	
	/*
	 * 
	 * Description:获取桌面显示需要的工作日志
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 24, 2010 5:02:32 PM
	 * @return      跳转到桌面显示日程的IFRAME
	 */
	public String desktop() throws Exception {
		Calendar startWlog = Calendar.getInstance();
		currentYear=String.valueOf(startWlog.get(Calendar.YEAR));		//当前年份
		currentMonth=String.valueOf(startWlog.get(Calendar.MONTH)+1);	//当前月份
		return "small";
	}

	/*
	 * 
	 * Description:工作日志视图
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 28, 2010 3:57:53 PM
	 */
	public String viewpage() throws Exception {
		jsonArr = new JSONArray().toString();
		inputType = "year";
		if ("".equals(setDate) || null == setDate) {
			Calendar now = Calendar.getInstance();
			setDate = String.valueOf(now.get(Calendar.YEAR)) + "-"
					+ String.valueOf(now.get(Calendar.MONTH) + 1) + "-"
					+ String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		}

		return "view";
	}
	
	/*
	 * 
	 * Description:
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 28, 2010 4:15:39 PM
	 */
	public String changeWorkLog() {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String temp=sdf.format(new Date());
			sdf.parse(temp);
			Calendar startWlog =sdf.getCalendar();//默认当月的第一天
			if(setDate!=null&&!"".equals(setDate)){
				String dates[]=setDate.split("-");
				startWlog.set(Calendar.YEAR, Integer.parseInt(dates[0]));
				startWlog.set(Calendar.MONTH, Integer.parseInt(dates[1]));
				startWlog.set(Calendar.DATE, 0);
			}
			List calList = manager.getListByTime(startWlog, startWlog);
			JSONArray array = manager.makeWlogListToJSONArray(calList, true);
			return renderText(array.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * 
	 * Description:进入下载附件页面
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 31, 2010 9:41:02 AM
	 */
	public String downLoad(){
		if(workLogId!=null&&!"".equals(workLogId)){
			model = manager.getWlogById(workLogId); 	// 根据id获取对象
			Set att = model.getToaWorkLogAttaches();	// 获取附件
			if (att != null) {				//如果有附件
				Iterator it = att.iterator();
				while (it.hasNext()) {		//循环附件
					ToaWorkLogAttach objs = (ToaWorkLogAttach) it.next();
					ToaAttachment attachment = attchmentService.getAttachmentById(objs.getAttachId());
					if(attachment!=null){
						attachments.add(attachment);
					}
				}
			}
		}
		return "download";
	}
	
	/*
	 * 
	 * Description:日志视图快速创建
	 * param: 
	 * @author 	    彭小青
	 * @date 	    May 31, 2010 4:06:14 PM
	 */
//	public String add() throws Exception {
//		try {
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar now= Calendar.getInstance();
//			if(startTime!=null&&!"".equals(startTime)){
//				format.parse(startTime);
//				now=format.getCalendar();
//			}
//			model.setWlogStartTime(now.getTime());		//默认当天开始
//			model.setWlogTitle(java.net.URLDecoder.decode(model.getWlogTitle(), "utf-8"));
//			manager.saveInpage(model, null, null, null,wordDoc);
//			workLogId =model.getWorkLogId();
//			JSONObject obj = new JSONObject();
//			obj.put("id", workLogId);
//			obj.put("msg", "操作成功！");
//			ServletActionContext.getResponse().getWriter().print(obj.toString());
//			addActionMessage(obj.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return "view";
//	}
	
	
	public String add() throws Exception {
		
		prepareModel();
//		 时间范围
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			if(startTime != null&&!"".equals(startTime)&&!"null".equals(startTime)){			
				Calendar cals = Calendar.getInstance();
				cals.setTime(format.parse(startTime));
				model.setWlogStartTime(cals.getTime());// 开始时间
			}
			if(endTime != null&&!"".equals(endTime)&&!"null".equals(endTime)){				
				Calendar cale = Calendar.getInstance();
				cale.setTime(format.parse(endTime));
				model.setWlogEndTime(cale.getTime());// 结束时间
			}
		} catch (ParseException e) {
			e.printStackTrace();
			addActionMessage("不能添加！");
		}
		long temp = 0;
		if (file != null) {
			for (int i = 0; i < file.length; i++) {
				if (file[i] != null) {
					if (file[i].length() == 0) {
						addActionMessage("error不能上传空文件，请检查要上传的文件！");
						return input();
					}
					temp += file[i].length();
				}
			}
			if (temp > GlobalBaseData.ATTACH_SIZE) {
				addActionMessage("error不能上传附件，附件大小超出最大范围！");
				return input();
			}
		}
		manager.saveInpage(model,delAttachIds,file,fileFileName,wordDoc);

		JSONObject obj = new JSONObject();
		obj.put("id", model.getWorkLogId());
		obj.put("msg", "操作成功！");

		try {
			ServletActionContext.getResponse().getWriter().print(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		addActionMessage(obj.toString());
		return "view";
		
	}
	
	/*
	 * 
	 * Description:读取日志说明
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 4, 2010 3:59:59 PM
	 */
	public String opendoc() throws Exception{
		HttpServletResponse response = this.getResponse();
		if(workLogId!=null){
			ToaWorkLog workLog=manager.getWlogById(workLogId);
			manager.setContentToHttpResponse(response, workLog.getWlogCon());
		}
		return null;
	}
	
	/*
	 * 
	 * Description:附件数
	 * param: 
	 * @author 	    彭小青
	 * @date 	    Jun 4, 2010 4:28:15 PM
	 */
	public void getappendsize(List fileList){
		if(fileList!=null){
			Map<String,String> map = manager.getCount();
			for(Object temp:fileList){
				ToaWorkLog workLog=(ToaWorkLog)temp;
				workLog.setAppendsize(map.get(workLog.getWorkLogId())==null?"":map.get(workLog.getWorkLogId()));
			}
		}
	}
	
	/**
	 * author:luosy
	 * description:  下载 公共附件下的文件
	 * modifyer:
	 * description:
	 * @return
	 */
	public String down() throws Exception {
		HttpServletResponse response = super.getResponse();
		ToaAttachment file = attchmentService.getAttachmentById(attachId);
		response.reset();
		response.setContentType("application/x-download");         //windows
		OutputStream output = null;
		try{
			response.addHeader("Content-Disposition", "attachment;filename=" +
			         new String(file.getAttachName().getBytes("gb2312"),"iso8859-1"));
		    output = response.getOutputStream();
		    output.write(file.getAttachCon());
		    output.flush();
		} catch(Exception e) {
			if(logger.isErrorEnabled()){
				logger.error(e.getMessage(),e);
			}
		} finally {		 	    
		    if(output != null){
		      try {
				output.close();
			} catch (IOException e) {
				if(logger.isErrorEnabled()){
					logger.error(e.getMessage(),e);
				}
			}
		      output = null;
		    }
		}
		return null;
	}
	
	
	

	public void setPage(Page<ToaWorkLog> page) {
		this.page = page;
	}
	
	public Page getPage() {
		return page;
	}

	public void setWorkLogId(String aWorkLogId) {
		workLogId = aWorkLogId;
	}

	public String getWorkLogId() {
		return workLogId;
	}

	public void setModel(ToaWorkLog model) {
		this.model = model;
	}

	public ToaWorkLog getModel() {
		return model;
	}

	public String getJsonArr() {
		return jsonArr;
	}

	public String getSetDate() {
		return setDate;
	}

	public void setSetDate(String setDate) {
		this.setDate = setDate;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] file) {
		this.file = file;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(String attachFiles) {
		this.attachFiles = attachFiles;
	}

	public void setDelAttachIds(String delAttachIds) {
		this.delAttachIds = delAttachIds;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}


	public String getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(String currentMonth) {
		this.currentMonth = currentMonth;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public List<ToaAttachment> getAttachments() {
		return attachments;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getCurrentYear() {
		return currentYear;
	}

	public void setCurrentYear(String currentYear) {
		this.currentYear = currentYear;
	}

	public void setWordDoc(File wordDoc) {
		this.wordDoc = wordDoc;
	}

	public List<ToaDoctemplateGroup> getTypeList() {
		return typeList;
	}

	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}

	public String getWlogStartTime1() {
		return wlogStartTime1;
	}

	public void setWlogStartTime1(String wlogStartTime1) {
		this.wlogStartTime1 = wlogStartTime1;
	}
	
}
