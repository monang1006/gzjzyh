package com.strongit.oa.common.workflow.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Task implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6717666992621370990L;
	//任务ID
	private String taskId;
	//任务名称
	private String taskName;
	//创建时间
	private Date createTime;
	//流程实例ID
	private String workflowInstanceId;
	//业务创建时间
	private Date bussinessCreateTime;
	//挂起状态
	private String suspendType;
	//发起人
	private String creater;
	//委派人
	private String assignor;
	//委派类型(0：委派，1：指派)
	private String assignType;
	//业务标题
	private String businessName;
	//处理人
	private String userName;
	//处理人id
	private String userId;
	//紧急程度
	private String jjcd;
	//密码信息
	private String password;
	//接收人密码（DES算法加密）
	private String desOfPassword;
	//处理链接
	private String url;
	//提醒内容
	private String content;
	//Html内容
	private String html;
	private String userLoginName;
	private String href;//查看详情链接
	public Task() {
		
	} 
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getWorkflowInstanceId() {
		return workflowInstanceId;
	}
	public void setWorkflowInstanceId(String workflowInstanceId) {
		this.workflowInstanceId = workflowInstanceId;
	}
	public Date getBussinessCreateTime() {
		return bussinessCreateTime;
	}
	public void setBussinessCreateTime(Date bussinessCreateTime) {
		this.bussinessCreateTime = bussinessCreateTime;
	}
	public String getSuspendType() {
		return suspendType;
	}
	public void setSuspendType(String suspendType) {
		this.suspendType = suspendType;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getAssignor() {
		return assignor;
	}
	public void setAssignor(String assignor) {
		this.assignor = assignor;
	}
	public String getAssignType() {
		return assignType;
	}
	public void setAssignType(String assignType) {
		this.assignType = assignType;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getJjcd() {
		return jjcd;
	}

	public void setJjcd(String jjcd) {
		this.jjcd = jjcd;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDesOfPassword() {
		if(desOfPassword != null) {
			try {
				desOfPassword = URLEncoder.encode(desOfPassword, "utf-8");//密文中有+
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return desOfPassword;
	}

	public void setDesOfPassword(String desOfPassword) {
		this.desOfPassword = desOfPassword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		StringBuilder html = new StringBuilder();
		html.append("<HTML><head><title>无标题文档</title><style type=\"text/css\">*{ padding:0; margin:0; }.jkkf{ padding:5px; background-color:#fff; font-size:12px; color:#000; font-family:\"宋体\"; }");
		html.append(".jkkf h3{ padding:3px; font-size:12px; background-color:#fef8c4; color:#8b460d; border-bottom:2px solid #d3d2be; line-height:1.5; }");
		html.append(".jkkf td{ padding:6px 0 6px 12px; border-bottom:1px solid #ccc;font-size:12px; color:#000; font-family:\"宋体\"; }");
		html.append("</style>");
		html.append("<script type=\"text/javascript\">  ");
		html.append("	function processDoc(obj) {");
		html.append("		var Width=screen.availWidth-10;");
		html.append("		var Height=screen.availHeight-30;");
		html.append("		window.open(obj.url,'sendDoc','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=yes, resizable=yes,location=no, status=no')");
		html.append("	}");
		html.append("</script>");
		html.append("</head><body oncontextmenu=\"return false;\"  scroll=\"no\"><div class=\"jkkf\"><h3>有任务需要您处理</h3><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
		html.append("<tr><td align=\"right\" valign=\"top\"><b>任务审批</b></td>");
		html.append("<td valign=\"top\">");
		html.append(taskName);
		html.append("</td><td align=\"right\" valign=\"top\"><b></b></td>");
		html.append("<td valign=\"top\">");
		html.append("</td></tr><tr><td align=\"right\" valign=\"top\"><b>处理人</b></td>");
		html.append("<td valign=\"top\">");
		html.append(userName);
		html.append("</td><td align=\"right\" valign=\"top\"><b>紧急程度</b></td>");
		html.append("<td valign=\"top\">");
		if("1".equals(jjcd)){
			html.append("平急");
		} else if("2".equals(jjcd)){
			html.append("加急");
		} else if("3".equals(jjcd)){
			html.append("特急");
		} else if("4".equals(jjcd)){
			html.append("特提");
		} else {
			html.append("无");
		}
		html.append("</td></tr><tr><td align=\"right\" valign=\"top\"><b>创建人</b></td> ");
		html.append("<td valign=\"top\">");
    	if(creater == null){
    		creater = userName;
    	} 
    	html.append(creater).append("</td><td align=\"right\" valign=\"top\"><b>创建时间</b></td> ");
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	if(null==createTime){
    		createTime=new Date();
    	}
		String startDate = sdf.format(createTime);
    	html.append("<td valign=\"top\">").append(startDate);
    	html.append("</td></tr><tr><td colspan=\"1\" align=\"right\" valign=\"top\"><b>描述</b></td>");
    	html.append("<td colspan=\"3\" valign=\"top\">");
    	content = content==null?"":content.replaceAll("\\s", "");
    	html.append(content);
    	if(href != null) {
    		href += "?taskId=" + taskId + "&instanceId=" + getWorkflowInstanceId() + "&j_username=" + userLoginName;
    		href += "&j_password="+getDesOfPassword()+"&j_flag=2";
    		
    	}
    	html.append("</td></tr><tr><td colspan=\"4\" align=\"right\"><a url='"+href+"' onclick='processDoc(this);' href=\"#\">查看详情&gt;&gt;&gt;</a></td>");
    	html.append("</table></div>");
    	/*html.append("<input type='hidden' id='j_username'").append(" value='").append(userLoginName).append("'>");
    	html.append("<input type='hidden' id='j_password'").append(" value='").append(getDesOfPassword()).append("'>");
    	html.append("<input type='hidden' id='instanceId'").append(" value='").append(getWorkflowInstanceId()).append("'>");
    	html.append("<input type='hidden' id='taskId'").append(" value='").append(taskId).append("'>");*/
    	html.append("</body></html>");
		return html.toString();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
