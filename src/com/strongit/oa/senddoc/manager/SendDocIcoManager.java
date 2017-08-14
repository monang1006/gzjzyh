package com.strongit.oa.senddoc.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.strongit.oa.common.user.IUserService;
import com.strongit.oa.common.workflow.IWorkflowConstService;
import com.strongit.oa.common.workflow.IWorkflowService;
import com.strongit.oa.common.workflow.model.TaskBusinessBean;
import com.strongit.oa.component.formtemplate.util.FormGridDataHelper;
import com.strongit.oa.senddoc.manager.service.ISendDocIcoService;
import com.strongit.oa.senddoc.util.GridViewColumUtil;
import com.strongit.oa.util.annotation.OALogger;
import com.strongit.workflow.workflowInterface.ITaskService;

/**
 * 显示图标 Manager
 * 
 * @author 严建
 * @company Strongit Ltd. (C) copyright
 * @date Dec 20, 2011 3:47:40 PM
 * @version 1.0.0.0
 * @classpath com.strongit.oa.senddoc.SendDocIcoManager
 */
@Service
@Transactional
@OALogger
public class SendDocIcoManager implements ISendDocIcoService {
	@Autowired
	protected IWorkflowService workflow; // 工作流服务

	@Autowired
	IWorkflowConstService workflowConstService;

	@Autowired
	SendDocManager manager;
	
	@Autowired
	ITaskService  iTaskService ;
	
	@Autowired
	IUserService iUserService;

	private static Properties properties = FormGridDataHelper
			.getColorSetProperties();

	/**
	 * 列表中显示红黄预警小图标
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 5:46:25 PM
	 * @return void
	 */
	private void gridViewRedYellowGreenIco(StringBuilder picImage,
			Date nodeEnter, String rootPath) throws Exception {
		picImage.append("&nbsp;").append(
				getRedYellowGreenIcoInfo(nodeEnter, rootPath).genImgTagHtml());
	}

	/**
	 * 显示红黄绿图标
	 * 
	 * @description
	 * @param innerHtml
	 *            页面显示html
	 * @param startDate
	 *            开始时间
	 * @param rootPath
	 *            工程路径
	 * @author 严建
	 * @createTime Dec 24, 2011 2:59:19 PM
	 * @return
	 * @throws Exception
	 */
	private void loadRedYellowGreenIco(StringBuffer innerHtml, Date startDate,
			String rootPath) throws Exception {
		innerHtml.append(getRedYellowGreenIcoInfo(startDate, rootPath)
				.genImgTagHtml());
	}

	/**
	 * 获取配置文件中，红黄绿图标相关的信息
	 * 
	 * @author yanjian
	 * @param startDate
	 * @param rootPath
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 12:22:25 PM
	 */
	private IocBean getRedYellowGreenIcoInfo(Date startDate, String rootPath)
			throws Exception {
		IocBean iocbean = new IocBean(rootPath);
		if (startDate == null) {
			startDate = new Date();
		}
		long time = new Date().getTime() - startDate.getTime();
		long hours = time / (1000 * 60 * 60);
		String green_value = properties.getProperty("green_value");
		int green_value_intValue = 72;
		if (green_value != null && !"".equals(green_value)) {
			try {
				green_value_intValue = Integer.parseInt(green_value) * 24;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		String yellow_value = properties.getProperty("yellow_value");
		int yellow_value_intValue = 144;
		if (yellow_value != null && !"".equals(yellow_value)) {
			try {
				yellow_value_intValue = Integer.parseInt(yellow_value) * 24;
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (hours <= green_value_intValue) {// 在3天内
			iocbean.setPicPath(properties.getProperty("green"));
			iocbean.setTitle(properties.getProperty("green_titleShow"));
		} else if (hours <= yellow_value_intValue) {// 6天内办理
			iocbean.setPicPath(properties.getProperty("yellow"));
			iocbean.setTitle(properties.getProperty("yellow_titleShow"));
		} else {// 超过7天办理
			iocbean.setPicPath(properties.getProperty("red"));
			iocbean.setTitle(properties.getProperty("red_titleShow"));
		}
		return iocbean;
	}

	/**
	 * 列表中显示蓝预警小图标
	 * 
	 * @author 严建
	 * @param picImage
	 * @param nodeEnter
	 * @param rootPath
	 * @throws Exception
	 * @createTime Feb 17, 2012 2:06:19 PM
	 */
	private void gridViewBlueIco(StringBuilder picImage, String rootPath)
			throws Exception {
		picImage.append("&nbsp;").append(
				getBlueIcoInfo(rootPath).genImgTagHtml());
	}

	/**
	 * 显示蓝图标
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param rootPath
	 * @throws Exception
	 * @createTime Feb 17, 2012 1:07:38 PM
	 */
	private void loadBlueIco(StringBuffer innerHtml, String rootPath)
			throws Exception {
		innerHtml.append(getBlueIcoInfo(rootPath).genImgTagHtml());
	}

	/**
	 * 获取配置文件中，蓝图标相关的信息
	 * 
	 * @author yanjian
	 * @param rootPath
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 12:19:22 PM
	 */
	private IocBean getBlueIcoInfo(String rootPath) throws Exception {
		IocBean iocbean = new IocBean(rootPath);
		iocbean.setTitle(properties.getProperty("blue_titleShow"));
		iocbean.setPicPath(properties.getProperty("blue"));
		return iocbean;
	}

	/**
	 * 显示已办图标
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param rootPath
	 * @throws Exception
	 * @createTime Feb 17, 2012 1:10:37 PM
	 */
	private void loadProcessedRedYellowGreenIco(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, String rootPath)
			throws Exception {
		if (taskbusinessbean.getWorkflowEndDate() != null) {
			this.loadBlueIco(innerHtml, rootPath);
		} else {
			this.loadRedYellowGreenIco(innerHtml, taskbusinessbean
					.getWorkflowStartDate(), rootPath);
		}
	}

	/**
	 * 列表控件中的限时小图标
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 29, 2011 5:45:59 PM
	 * @return void
	 */
	private void gridViewTimeOutIco(StringBuilder picImage,
			Object taskStartDate, Object timeOut, String rootPath)
			throws Exception {
		picImage.append("&nbsp;").append(
				getTimeOutIcoInfo(rootPath, (String) timeOut,
						(Date) taskStartDate).genImgTagHtml());
	}

	/**
	 * 显示公文限时图标
	 * 
	 * @description
	 * @param innerHtml
	 *            页面显示html
	 * @param startDate
	 *            开始时间
	 * @param rootPath
	 *            工程路径
	 * @param timeOut
	 *            显示时间
	 * @author 严建
	 * @createTime Dec 24, 2011 3:10:30 PM
	 * @return void
	 */
	private void loadTimeOutIco(StringBuffer innerHtml, Date startDate,
			String timeOut, String rootPath) throws Exception {
		innerHtml.append(getTimeOutIcoInfo(rootPath, timeOut, startDate)
				.genImgTagHtml());
	}

	/**
	 * 获取配置文件中，公文限时相关的信息
	 * 
	 * @author yanjian
	 * @param rootPath
	 * @param timeOut
	 * @param startDate
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 10:34:44 AM
	 */
	private IocBean getTimeOutIcoInfo(String rootPath, String timeOut,
			Date startDate) throws Exception {
		IocBean iocbean = new IocBean(rootPath);
		if (timeOut != null && !"".equals(timeOut)) {
			StringBuilder titleShow = new StringBuilder();
			String recTime = new SimpleDateFormat("yyyy-MM-dd")
					.format(startDate);
			titleShow.append(properties.getProperty("clock_titleShow").replace(
					"{1}", recTime).replace("{2}", timeOut.toString()));
			iocbean.setTitle(titleShow.toString());
			iocbean.setPicPath(properties.getProperty("clock"));
		} else {
			iocbean.setTitle("非限时办理");
			iocbean.setPicPath(properties.getProperty("noclock"));
		}
		return iocbean;
	}

	/**
	 * 显示退回信息
	 * 
	 * @author 严建
	 * @param taskId
	 * @return
	 * @createTime Mar 23, 2012 12:27:06 PM
	 */
	public String showBackInfo(String taskId) {
		String getBackInfo = workflowConstService.getBackInfo(taskId);
		JSONObject jsonObj = JSONObject.fromObject(getBackInfo);
		String backinfo = "";
		if (jsonObj != null && !jsonObj.toString().equals("null")) {
			String userinfo = "";
			if (jsonObj.containsKey("userName")
					&& jsonObj.containsKey("orgName")) {
				userinfo = "退回人员：" + jsonObj.getString("userName") ;
				System.out.println(jsonObj.containsKey("rest1"));
				if (jsonObj.containsKey("rest1")
						&& jsonObj.containsKey("rest1") && "1".equals( jsonObj.getString("rest1"))) {
					
				}else{
					userinfo += ("【"+ jsonObj.getString("orgName") + "】");
				}
				userinfo += "\r\n";
			}
			String suggestion = "";
			if (jsonObj.containsKey("suggestion")) {
				suggestion = "退回意见：" + jsonObj.getString("suggestion") + "\r\n";
			}
			String backTime = "";
			if (jsonObj.containsKey("backTime")) {
				backTime = "退回时间：" + jsonObj.getString("backTime") + "\r\n";
			}
			backinfo = userinfo + suggestion + backTime;
			String[] backinfos = backinfo.split("\r\n");
			StringBuilder backinfoTemp = new StringBuilder();
			for (String str : backinfos) {
				backinfoTemp.append(str).append("&#xd;");
			}
			backinfo = backinfoTemp.toString();
		} else {
			backinfo = "系统退文,详情请查看【办理记录】";
		}
		return backinfo;
	}

	/**
	 * 列表加载退回图标
	 * 
	 * @author 严建
	 * @param picImage
	 * @param isBackspace
	 * @param taskId
	 * @param rootPath
	 * @throws Exception
	 * @createTime Mar 23, 2012 12:32:57 PM
	 */
	private void gridViewBackspaceIco(StringBuilder picImage,
			String isBackspace, String taskId, String rootPath)
			throws Exception {
		picImage.append("&nbsp;").append(
				getBackspaceIcoInfo(isBackspace, taskId, rootPath).genImgTagHtml());
	}

	/**
	 * 显示退文图标
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 3:17:12 PM
	 * @return void
	 */
	private void loadBackspaceIco(StringBuffer innerHtml, String isBackspace,
			String taskId, String rootPath) throws Exception {
		innerHtml.append(getBackspaceIcoInfo(isBackspace, taskId, rootPath)
				.genImgTagHtml());
	}

	/**
	 * 获取配置文件中，退文相关的信息
	 * 
	 * @author yanjian
	 * @param isBackspace
	 * @param taskId
	 * @param rootPath
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 11:13:16 AM
	 */
	private IocBean getBackspaceIcoInfo(String isBackspace, String taskId,
			String rootPath) throws Exception {
		IocBean iocbean = new IocBean();
		StringBuilder picPath = new StringBuilder();
		StringBuilder title = new StringBuilder();
		if (isBackspace != null && isBackspace.equals("1")) {
			picPath.append(properties.getProperty("back"));
			title.append(showBackInfo(taskId));
		} else {
			picPath.append(properties.getProperty("noback"));
			title.append("非退文");
		}
		iocbean.setRootPath(rootPath);
		iocbean.setPicPath(picPath.toString());
		iocbean.setTitle(title.toString());
		return iocbean;
	}

	/**
	 * 加载指派图标
	 * 
	 * @author 严建
	 * @param picImage
	 * @param assignType
	 * @param rootPath
	 * @throws Exception
	 * @createTime Mar 23, 2012 12:35:55 PM
	 */
	private void gridViewAssignTypeIco(StringBuilder picImage,
			String assignType, String rootPath) throws Exception {
		picImage.append("&nbsp;").append(
				getAssignTypeIcoInfo(rootPath, assignType).genImgTagHtml());
	}

	/**
	 * 显示代办文图标
	 * 
	 * @description
	 * @author 严建
	 * @createTime Dec 24, 2011 3:17:12 PM
	 * @return void
	 */
	private void loadAssignTypeIco(StringBuffer innerHtml, String assignType,
			String rootPath) throws Exception {
		innerHtml.append(getAssignTypeIcoInfo(rootPath, assignType)
				.genImgTagHtml());
	}

	/**
	 * 获取配置文件中，代办文相关的信息
	 * 
	 * @author yanjian
	 * @param rootPath
	 * @param assignType
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 11:21:07 AM
	 */
	private IocBean getAssignTypeIcoInfo(String rootPath, String assignType)
			throws Exception {
		IocBean iocbean = new IocBean();
		StringBuilder picPath = new StringBuilder();
		StringBuilder title = new StringBuilder();
		//判断assignType是否含有数字
		boolean isDigit = false;
		if(assignType != null){
			for(int i=0; i<assignType.length(); i++){
				char ch = assignType.charAt(i);
				if(Character.isDigit(ch)){
					isDigit = true;
					break;
				}
			}			
		}
		//assignType含数字则不为人名
		if (assignType != null && !isDigit) {
			picPath.append(properties.getProperty("instead"));
			title.append("该任务由["+assignType+"]委托代办");
		} else if(assignType != null && assignType.equals("1")){	//"1"为指派
			picPath.append(properties.getProperty("instead"));
			title.append("指派");
		} else {
			picPath.append(properties.getProperty("noinstead"));
			title.append("非代办");
		}
		iocbean.setRootPath(rootPath);
		iocbean.setPicPath(picPath.toString());
		iocbean.setTitle(title.toString());
		return iocbean;
	}

	/**
	 * 加载是否被签收图标
	 * 
	 * @author yanjian
	 * @param picImage
	 * @param isReceived
	 * @param rootPath
	 * @throws Exception
	 *             Sep 11, 2012 8:40:00 PM
	 */
	private void gridViewDocIsReceivedIco(StringBuilder picImage,
			String isReceived,String taskId, String rootPath) throws Exception {
		picImage.append("&nbsp;").append(
				getDocIsReceivedIcoInfo(rootPath, isReceived,taskId).genImgTagHtml());
	}

	/**
	 * 显示是否签收的图标
	 * 
	 * @author yanjian
	 * @param innerHtml
	 * @param isReceived
	 * @param rootPath
	 * @throws Exception
	 *             Sep 11, 2012 9:34:27 PM
	 */
	private void loadDocIsReceivedIco(StringBuffer innerHtml,
			String isReceived,String taskId, String rootPath) throws Exception {
		innerHtml.append(getDocIsReceivedIcoInfo(rootPath, isReceived,taskId)
				.genImgTagHtml());
	}

	/**
	 * 获取配置文件中，公文签收相关的信息
	 * 
	 * @author yanjian
	 * @param rootPath
	 * @param isReceived
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 11:34:42 AM
	 */
	private IocBean getDocIsReceivedIcoInfo(String rootPath, String isReceived,String taskId)
			throws Exception {
		IocBean iocbean = new IocBean(rootPath);
		iocbean.setId("isReceivedIco"+taskId);
		if (isReceived != null && isReceived.equals("1")) {
			iocbean.setPicPath(properties.getProperty("received"));
			iocbean.setTitle(properties.getProperty("received_titleShow"));
		} else {
			iocbean.setPicPath(properties.getProperty("noreceived"));
			iocbean.setTitle(properties.getProperty("noreceived_titleShow"));
		}
		return iocbean;
	}

	/**
	 * 
	 * 列表显示意见征询图标
	 * 
	 * @description
	 * @author 严建
	 * @param picImage
	 * @param businessType
	 * @param businessId
	 * @param rootPath
	 * @throws Exception
	 * @createTime Jun 19, 2012 9:27:21 PM
	 */
	private void gridViewYjzxIco(StringBuilder picImage, String businessType,
			String businessId, String rootPath) throws Exception {
		picImage.append("&nbsp;").append(
				getYjzxIcoInfo(rootPath, businessType, businessId)
						.genImgTagHtml());
	}

	/**
	 * 显示意见征询图标（自办文时办理意见征询的业务,业务标题前显示“询”图标）
	 * 
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param rootPath
	 */
	private void loadYjzxIco(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, String rootPath)
			throws Exception {
		innerHtml.append(getYjzxIcoInfo(rootPath,
				taskbusinessbean.getBusinessType(),
				taskbusinessbean.getBsinessId()).genImgTagHtml());
	}

	/**
	 * 获取配置文件中，意见征询相关的信息
	 * 
	 * @author yanjian
	 * @param rootPath
	 * @param businessType
	 * @param businessId
	 * @return
	 * @throws Exception
	 *             Sep 12, 2012 12:32:47 PM
	 */
	private IocBean getYjzxIcoInfo(String rootPath, String businessType,
			String businessId) throws Exception {
		IocBean iocbean = new IocBean(rootPath);
		if ("0".equals(businessType) && businessId != null) {
			StringBuilder titleShow = new StringBuilder();
			String sql = "select * from t_oa_yjzx where YJZXID='"
					+ businessId.split(";")[2] + "'";
			Map map = manager.queryForMap(sql);
			StringBuilder info = new StringBuilder("意见征询标题：");
			info.append(map.get("WORKFLOWTITLE")).append("&#xd;");
			info.append("意见征询单位：").append(
					map.get("SEND_UNIT") == null ? "" : map.get("SEND_UNIT"))
					.append("&#xd;");
			Date time = (Date) map.get("PERSON_OPERATE_DATE");
			String strTime = "";
			if (time != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				strTime = sdf.format(time);
			}
			info.append("限时办理时间：").append(strTime).append("&#xd;");
			info.append("意见征询内容：").append(
					map.get("ZXNR") == null ? "" : map.get("ZXNR"));
			titleShow.append(info);
			iocbean.setPicPath(properties.getProperty("yjzx"));
			iocbean.setTitle(info.toString());
		}else{
			iocbean.setPicPath("");
			iocbean.setTitle("非意见征询");
		}
		return iocbean;
	}

	/**
	 * 加载待办列表列显示图片
	 * 
	 * @author yanjian
	 * @param map
	 * @throws Exception
	 *             Sep 11, 2012 8:46:23 PM
	 */
	public void todoGridViewIco(Map map) throws Exception {
		StringBuilder picImage = new StringBuilder();
		String rootPath = ServletActionContext.getRequest().getContextPath();
		// 加载是否被签收图标
		this.gridViewDocIsReceivedIco(picImage, (String) map.get("isReceived"), (String) map.get("taskId"),
				rootPath);
		this.gridViewRedYellowGreenIco(picImage, (Date) map
				.get("processstartdate"), rootPath);
		// 显示公文期限图标
		this.gridViewTimeOutIco(picImage, map.get("processStartDate"), map
				.get("timeOut"), rootPath);

		// 通过标识区分正常办理文、代办文、退文
		this.gridViewBackspaceIco(picImage, (String) map.get("isBackspace"),
				(String) map.get("taskId"), rootPath);
		// 加载指派图标
		//任务实例id
		long tid = Long.parseLong((String) map.get("taskId"));
		//委托类型(“0”：委托；“1”：指派)
		String tAssignType = (String) map.get("assignType");
		if(tAssignType != null && "0".equals(tAssignType)){
			//查找在指定的任务实例上指定的人员是否存在委托代理
			String[] de = iTaskService.getDelegationIdAndOriUser(tid,iUserService.getCurrentUser().getUserId(),"assigned");
			//该任务最初始处理人name
			tAssignType = iUserService.getUserNameByUserId(de[1]);			
		}
		//修改待办事宜委派图标显示  2013-12-04  BY qibaohua
		this.gridViewAssignTypeIco(picImage, tAssignType,
				rootPath);
		// 添加图片列
		GridViewColumUtil.addPngColumICO(map, picImage);
	}

	/**
	 * 加载废除列表图标
	 * 
	 * @author yanjian
	 * @param map
	 * @throws Exception
	 *             Sep 11, 2012 8:53:14 PM
	 */
	public void repealGridViewIco(Map map) throws Exception {
		StringBuilder picImage = new StringBuilder();
		String rootPath = ServletActionContext.getRequest().getContextPath();
		this.gridProcessedViewRedYellowGreenIco(picImage, (Date) map
				.get("processStartDate"), (Date) map.get("processEndDate"),
				rootPath);
		this.gridViewTimeOutIco(picImage, map.get("processStartDate"), map
				.get("timeOut"), rootPath);
		// 添加图片列
		GridViewColumUtil.addPngColumICO(map, picImage);
	}

	/**
	 * 加载已办列表图标
	 * 
	 * @author yanjian
	 * @param map
	 * @throws Exception
	 *             Sep 11, 2012 8:57:45 PM
	 */
	public void processedGridViewIco(Map map) throws Exception {
		StringBuilder picImage = new StringBuilder();
		String rootPath = ServletActionContext.getRequest().getContextPath();
		this.gridProcessedViewRedYellowGreenIco(picImage, (Date) map
				.get("processStartDate"), (Date) map.get("processEndDate"),
				rootPath);
		this.gridViewTimeOutIco(picImage, map.get("processStartDate"), map
				.get("timeOut"), rootPath);
		this.gridViewYjzxIco(picImage, (String) map.get("businessType"),
				(String) map.get("businessId"), rootPath);
		// 添加图片列
		if(!"1".equals((String)map.get("processStatus"))){
			GridViewColumUtil.addPngColumICO(map, picImage);
		}
	}

	/**
	 * 已办列表中显示红黄预警小图标
	 * 
	 * @author 严建
	 * @param picImage
	 * @param processStartDate
	 * @param processEndDate
	 * @param rootPath
	 * @throws Exception
	 * @createTime Feb 17, 2012 2:15:39 PM
	 */
	private void gridProcessedViewRedYellowGreenIco(StringBuilder picImage,
			Date processStartDate, Date processEndDate, String rootPath)
			throws Exception {
		if (processEndDate != null) {
			this.gridViewBlueIco(picImage, rootPath);
		} else {
			this
					.gridViewRedYellowGreenIco(picImage, processStartDate,
							rootPath);
		}
	}

	/**
	 * 加载桌面待办事宜
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param rootPath
	 * @createTime Feb 16, 2012 6:41:04 PM
	 */
	public void loadToDoIco(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, String rootPath)
			throws Exception {
		//如果是待办文件显示  是否签收的图标,待签收文件 不显示 是否签收的图标
		if(taskbusinessbean.getSortType()!=null && taskbusinessbean.getSortType().equals("sign") ){ 
			/* 显示是否签收的图标 */
		    this.loadDocIsReceivedIco(innerHtml, taskbusinessbean.getIsReceived(),taskbusinessbean.getTaskId(),
				rootPath);
			}
	
		/* 显示红黄蓝图标 */
		this.loadProcessedRedYellowGreenIco(innerHtml, taskbusinessbean,
				rootPath);
		/* 显示公文期限图标 */
		this.loadTimeOutIco(innerHtml, taskbusinessbean.getWorkflowStartDate(),
				taskbusinessbean.getEndTime(), rootPath);
		/* 显示退文图标 */
		this.loadBackspaceIco(innerHtml, taskbusinessbean.getIsBackspace(),
				taskbusinessbean.getTaskId(), rootPath);
		/* 显示代办文图标 */
		//任务实例id
		long tid = Long.parseLong(taskbusinessbean.getTaskId());
		//委托类型(“0”：委托；“1”：指派)
		String tAssignType = taskbusinessbean.getAssignType();
		if(tAssignType != null && "0".equals(tAssignType)){
			//查找在指定的任务实例上指定的人员是否存在委托代理
			String[] de = iTaskService.getDelegationIdAndOriUser(tid,iUserService.getCurrentUser().getUserId(),"assigned");
			//该任务最初始处理人name
			tAssignType = iUserService.getUserNameByUserId(de[1]);			
		}
		this.loadAssignTypeIco(innerHtml, tAssignType,
				rootPath);
	}

	/**
	 * 加载桌面已办事宜
	 * 
	 * @author 严建
	 * @param innerHtml
	 * @param taskbusinessbean
	 * @param rootPath
	 * @createTime Feb 16, 2012 6:41:04 PM
	 */
	public void loadProcessedIco(StringBuffer innerHtml,
			TaskBusinessBean taskbusinessbean, String rootPath)
			throws Exception {
		/* 显示红黄蓝图标 */
		if (taskbusinessbean.getWorkflowEndDate() != null) {
			this.loadBlueIco(innerHtml, rootPath);
		} else {
			this.loadRedYellowGreenIco(innerHtml, taskbusinessbean
					.getWorkflowStartDate(), rootPath);
		}
		/* 显示公文期限图标 */
		this.loadTimeOutIco(innerHtml, taskbusinessbean.getWorkflowStartDate(),
				taskbusinessbean.getEndTime(), rootPath);
		// 加载意见征询图标
		this.loadYjzxIco(innerHtml, taskbusinessbean, rootPath);
	}

	/** 内部类IocBean */
	private class IocBean {
		private String rootPath = "";

		private String picPath = "";

		private String title = "";
		private String id="";
		public IocBean() {

		}

		public IocBean(String rootPath) {
			this.rootPath = rootPath;
		}

		public String getRootPath() {
			return rootPath;
		}

		public void setPicPath(String picPath) {
			this.picPath = picPath;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setRootPath(String rootPath) {
			this.rootPath = rootPath;
		}

		/**
		 * 生成<Img>的Html
		 * 
		 * @author yanjian
		 * @param rootPath
		 * @param iocbean
		 * @return Sep 12, 2012 10:46:07 AM
		 */
		public String genImgTagHtml() {
			StringBuilder imgHtml = new StringBuilder();
			if (picPath != null && !"".equals(picPath)) {// 没有设置图片地址时，设置一个空的图片(隐藏，确保列表不变形)
				imgHtml
						.append("<img id=\""+id+"\" src=\"")
						.append(rootPath)
						.append(picPath)
						.append(
								"\" title=\""
										+ title
										+ "\" style=\"vertical-align:middle;width:16px;height:16px;width:16px;height:16px;\"/> ");
			}else{
				imgHtml
				.append("<img id=\""+id+"\"  src=\"")
				.append(rootPath)
				.append(picPath)
				.append(
						"\" title=\""
								+ title
								+ "\" style=\"vertical-align:middle;width:16px;height:16px;width:16px;height:16px;display:none;\"/> ");
			}
			return imgHtml.toString();
		}

		public void setId(String id) {
			this.id = id;
		}
	}

}
