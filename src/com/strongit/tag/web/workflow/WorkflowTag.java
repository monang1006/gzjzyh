package com.strongit.tag.web.workflow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.tag.web.util.ResponseUtils;
import com.strongit.uums.optprivilmanage.BaseOptPrivilManager;
import com.strongmvc.exception.SystemException;
import com.strongmvc.service.ServiceLocator;

/**
 * 工作流界面显示标签：
 * 1、流程草稿
 * 2、待办工作
 * 3、已办工作
 * 4、主办工作
 * 主要结构是流程挂接在流程类型下.
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-11-6 下午03:02:24
 * @version  2.0.7
 * @classpath com.strongit.tag.web.workflow.WorkflowTag
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class WorkflowTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1655367614952037319L;

	private List<Object[]> typeList ;									//流程类型列表Object[]{类型id,类型名称,数量}
	
	private Map<String, List<Object[]>>	 workflowMap;					//流程数据 Map<流程类型,List<Object[流程名称,流程启动表单id,数量]>>
	
	private List<Object[]> leftTypeList ;									//流程类型列表Object[]{类型id,类型名称,数量}
	
	private Map<String, List<Object[]>>	 leftWorkflowMap;					//流程数据 Map<流程类型,List<Object[流程名称,流程启动表单id,数量]>>
	
	private List<Object[]> rightTypeList ;									//流程类型列表Object[]{类型id,类型名称,数量}
	
	private Map<String, List<Object[]>>	 rightWorkflowMap;					//流程数据 Map<流程类型,List<Object[流程名称,流程启动表单id,数量]>>
	
	private String title ;												//页面标题
	
	private String href ;												//单击流程名称的目标链接地址
	
	private boolean showTitle = false;								    //是否统计左右两列
	
	private String isPop = "true";										//是否弹出窗口显示
	
	private String workflowType;										//流程类别,来文草稿中增加“公文签收”按钮，通过流程类别控制按钮是否显示.
	
	private BaseOptPrivilManager privilManager;							//权限服务类,用于验证“公文签收”功能权限
	
	private String titleTemp;											//仅适用于该类内部作为临时变量，比提供外界接口
	private Integer count;//新建流程的统计数
	
	private boolean freedomWorkflow = false; //是否是自由流
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public int doStartTag() throws JspException {
		StringBuilder html = new StringBuilder();
		initHtmlStyle(html);
		initJavaScript(html);
		initTitle(html);
		initBody(html);
		ResponseUtils.write(pageContext, html.toString());
		return EVAL_PAGE;
	}
	
	private StringBuilder initHtmlStyle(StringBuilder style) {
		if(style != null) {
			style.append("<link href=\"").append(getFrameRoot()).append("/css/properties_windows_list.css\" type=\"text/css\" rel=\"stylesheet\" />");
			style.append("<link href=\"").append(getFrameRoot()).append("/css/workflowTag.css\" type=\"text/css\" rel=\"stylesheet\" />");
		}
		return style;
	}

	private StringBuilder initJavaScript(StringBuilder javascript) {
		if(javascript != null){
			javascript.append("<script>");
			
			/*javascript.append("	$(document).ready(function(){");
			javascript.append("		$(\".cookiemodule\").each(function(){");
			javascript.append("			var module_id = $(this).attr(\"moduleid\");");
			javascript.append("			var display = getCookie(module_id);");
			javascript.append("			if(display && display != null && display != \"\"){");
			javascript.append("				var body_i = document.getElementById('module_'+module_id+'_body');");
			javascript.append("				var img_i  = document.getElementById('img_resize_'+module_id);");
			javascript.append("				body_i.style.display = display;");
			javascript.append("				if(display == \"block\"){");
			javascript.append("					img_i.src=img_i.src.substring(0,img_i.src.lastIndexOf('/')+1)+'expand_arrow.png';");
			javascript.append("					img_i.title='折叠';");
			javascript.append("				}else{");
			javascript.append("					img_i.src=img_i.src.substring(0,img_i.src.lastIndexOf('/')+1)+'collapse_arrow.png';");
			javascript.append("					img_i.title='展开';");
			javascript.append("				}");
			javascript.append("			}");
			javascript.append("		});");
			javascript.append("	});");*/
			
			javascript.append("	function _resize(module_id){");
			javascript.append("		var body_i = document.getElementById('module_'+module_id+'_body');");
			javascript.append("		var img_i  = document.getElementById('img_resize_'+module_id);");
			javascript.append("		if(body_i.style.display == 'none'){");
			javascript.append("			body_i.style.display = 'block';");
			javascript.append("			img_i.src=img_i.src.substring(0,img_i.src.lastIndexOf('/')+1)+'expand_arrow.png';");
			javascript.append("			img_i.title='折叠';");
			javascript.append("		} else {");
			javascript.append("			body_i.style.display='none';");
			javascript.append("			img_i.src=img_i.src.substring(0,img_i.src.lastIndexOf('/')+1)+'collapse_arrow.png';");
			javascript.append("			img_i.title='展开';");
			javascript.append("		}");
			//javascript.append("		setCookie(module_id,body_i.style.display,7);");
			javascript.append("	}");
			
			javascript.append("	function openForm(Url,IsPop){");
			javascript.append("		var Width=screen.availWidth-10;");
			javascript.append("		var Height=screen.availHeight-30;");
			javascript.append("		if(IsPop == true){");
			javascript.append("			window.open(Url,'sendDocCreate','height='+Height+', width='+Width+', top=0, left=0, toolbar=no, ' + 'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');");
			javascript.append("		} else {");
			javascript.append("			location = Url;");
			javascript.append("		}");
			javascript.append("	}");
			//增加Cookie操作支持
			javascript.append("	function setCookie(c_name,value,expiredays){");
			javascript.append("		var exdate=new Date();");
			javascript.append("		exdate.setDate(exdate.getDate() + expiredays);");
			javascript.append("		document.cookie = c_name + \"=\" + escape(value)+((expiredays==null) ? \"\" : \";expires=\"+exdate.toGMTString());");
			javascript.append("	}");
			
			javascript.append("	function getCookie(c_name){");
			javascript.append("		if (document.cookie.length > 0){");
			javascript.append("			var c_start = document.cookie.indexOf(c_name + \"=\");");
			javascript.append("			if (c_start != -1){");
			javascript.append("				var c_start = c_start + c_name.length + 1;");
			javascript.append("				var c_end = document.cookie.indexOf(\";\",c_start);");
			javascript.append("				if (c_end == -1){");
			javascript.append("					c_end = document.cookie.length");
			javascript.append("				}");
			javascript.append("				return unescape(document.cookie.substring(c_start,c_end));");
			javascript.append("			}");
			javascript.append("		}");
			javascript.append("	}");
			
			javascript.append("	function checkCookie(name){");
			javascript.append("		var username = getCookie(name);");
			javascript.append("	}");
			
			javascript.append("</script>");
		}	
		return javascript;
	}

	private StringBuilder initTitle(StringBuilder titleHtml) {
		if(titleHtml != null) {
			if(showTitle) {//分左右2列分别统计
					int leftCount = 0;
					int rightCount = 0;
					if(leftTypeList != null && leftTypeList.size() > 0) {
						for(int i = 0 ;i < leftTypeList.size();i++) {
							Object[] typeInfo = (Object[])leftTypeList.get(i);
							if(typeInfo.length == 3) {
								Object count = typeInfo[2];
								if(count != null) {
									leftCount += Integer.parseInt(count.toString());
								}
							}
						}						
					}
					if(rightTypeList != null && rightTypeList.size() > 0) {
						for(int j=0;j<rightTypeList.size();j++) {
							Object[] typeInfo = (Object[])rightTypeList.get(j);
							if(typeInfo.length == 3) {
								Object count = typeInfo[2];
								if(count != null) {
									rightCount += Integer.parseInt(count.toString());
								}
							}
						}						
					}
					if(leftCount + rightCount > 0) {
						title = title+"(<b><font color='red'>"+(leftCount + rightCount)+"</font></b>)";
					}
					titleTemp ="未完成(<b><font color='red'>"+leftCount+"</font></b>)" +
								"#" +
								"已完成(<b><font color='red'>"+rightCount+"</font></b>)";
					/*StringBuilder strTitle = new StringBuilder(title);
					strTitle.append("：未完成：(<b><font color='red'>").append(leftCount)
							.append("</font></b>)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							//.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							//.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							//.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							//.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							//.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
							.append("已完成：(<b><font color='red'>").append(rightCount)
							.append("</font></b>)");
					title = strTitle.toString();*/
			} else {
				if(typeList != null && !typeList.isEmpty()) {
					int total = 0;
					for(int i = 0 ;i < typeList.size();i++) {
						Object[] typeInfo = (Object[])typeList.get(i);
						if(typeInfo.length == 3) {
							Object count = typeInfo[2];
							if(count != null) {
								total += Integer.parseInt(count.toString());
							}
						}
					}
					
					if(total > 0) {
						title = title + "(<b><font color='red'>"+total+"</font></b>)";
					}
					if(count!=null){
						title = title + "(<b><font color='red'>"+count+"</font></b>)";//新建流程的数量统计,特殊的count属性
					}
				}
				//yanjian 2012-04-11 
				else{
					title = title + "(<b><font color='red'>0</font></b>)";
				}				
			}
			
			titleHtml.append("<div id=\"contentborder\">");
			titleHtml.append("	<table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"3\" >");
			titleHtml.append("		<tr>");
			titleHtml.append("			<td class=\"tabletitle\" style=\"padding-left : 12px;\">");
			if(title != null && !"".equals(title)){//add 严建 增加控件属性title判断，当title为null或空串时，不显示图标 
				titleHtml.append("				&nbsp;&nbsp;<img src=\"").append(getFrameRoot()).append("/images/ico/ico.gif\"").append(" align=\"absmiddle\">");
				titleHtml.append("&nbsp;&nbsp;");
				titleHtml.append("				<span class=\"big3\">").append(title).append("</span> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ");
			}
			if(workflowType != null && "3".equals(workflowType)) {//来文草稿
				privilManager = (BaseOptPrivilManager)ServiceLocator.getService("baseOptPrivilManager");
				if(privilManager == null) {
					System.err.println("注入BaseOptPrivilManager失败！");
					throw new SystemException("注入BaseOptPrivilManager失败！");
				}
				if(privilManager.checkPrivilBySysCode("001-0002000200080001")) {//公文签收权限验证通过
					titleHtml.append("		<input type=button name=getDocs class=\"input_bg\" id=getDocs style=\"display:none;\" value=\"公文签收\" onclick=\"getDocs()\"/>");									
				}
			}
			titleHtml.append("			</td>");
			titleHtml.append("		</tr>");
			titleHtml.append("	</table>");
		}
		return titleHtml;
	}

	StringBuilder initBody(StringBuilder bodyHtml) {
		if(bodyHtml != null) {
			bodyHtml.append("<table width=\"99%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin-top:5px;margin-right:5px\">");
			bodyHtml.append("	<tr> <td width=\"50%\" valign=\"top\">");
			bodyHtml.append("		<div id=\"div_l\">");
			StringBuilder rightHtml = new StringBuilder();
			if(showTitle) {//按左右两列分别统计
				bodyHtml.append("<div>&nbsp;&nbsp;"+titleTemp.split("#")[0]+"</div>");
				if(leftTypeList != null && !leftTypeList.isEmpty()) {
					for(int i=0;i<leftTypeList.size();i++) {
						Object[] typeInfo = (Object[])leftTypeList.get(i);
						bodyHtml.append(this.createBody(typeInfo, leftWorkflowMap,"0"));
					}					
				}
				rightHtml.append("<div>&nbsp;&nbsp;"+titleTemp.split("#")[1]+"</div>");
				if(rightTypeList != null && !rightTypeList.isEmpty()){
					for(int j=0;j<rightTypeList.size();j++) {
						Object[] typeInfo = (Object[])rightTypeList.get(j);
						rightHtml.append(this.createBody(typeInfo, rightWorkflowMap,"1"));
					}					
				}
			} else {
				if(typeList != null && !typeList.isEmpty()) {
					for(int i = 0 ;i < typeList.size();i++) {
						Object[] typeInfo = (Object[])typeList.get(i);
						if(i % 2 == 0) {
							bodyHtml.append(this.createBody(typeInfo, workflowMap,""));
						} else {
							rightHtml.append(this.createBody(typeInfo, workflowMap,""));
						}
					}	
				} 				
			}
			bodyHtml.append("<div class=\"shadow\"></div>");
			bodyHtml.append("</div>");
			bodyHtml.append("</td>");
			bodyHtml.append("<td width=\"50%\" valign=\"top\">");
			bodyHtml.append("	<div id=\"div_r\">");
			bodyHtml.append(rightHtml);
			bodyHtml.append("<div class=\"shadow\"></div></div>");
			bodyHtml.append("</td></tr></table></div>");
		}
		return bodyHtml;
	}

	/**
	 * 
	 * @author:邓志城
	 * @date:2010-11-16 下午06:55:48
	 * @param typeInfo
	 * @param workflowMap
	 * @param processStatus  流程状态（“0”：待办；“1”：办毕）
	 * @return
	 */
	StringBuilder createBody(Object[] typeInfo,Map<String, List<Object[]>>	workflowMap,String processStatus) {
		StringBuilder bodyHtml = new StringBuilder();
		bodyHtml.append("<div id=\"module_").append(typeInfo[0]).append("\" class=\"module listColor\">");
		bodyHtml.append("<div class=\"head\" onclick=\"_resize('").append(typeInfo[0]).append("$").append(processStatus).append("');\"").append(" style=\"cursor:pointer\">"); 
		bodyHtml.append("<h4 id=\"module_").append(typeInfo[0]).append("_head\" class=\"moduleHeader\">");
		bodyHtml.append("<img class=\"cookiemodule\" moduleid=\"").append(typeInfo[0]).append("$")
			.append(processStatus).append("\" id=\"img_resize_").append(typeInfo[0]).append("$").append(processStatus).append("\" src=\"").append(getRootPath()).append("/oa/image/work/expand_arrow.png\" title=\"折叠\" />");
		bodyHtml.append("<span id=\"module_").append(typeInfo[0]).append("_text\" class=\"text\">");
		bodyHtml.append(typeInfo[1]);
		if(typeInfo.length == 3) {
			bodyHtml.append("(<b><font color='red'>").append(typeInfo[2]).append("</font></b>)");
		}
		bodyHtml.append("</span>");
		bodyHtml.append("</h4>");
		bodyHtml.append("</div>");
		bodyHtml.append("<div class=\"cookiemodule\" moduleid=\"").append(typeInfo[0]).append("$").append(processStatus)
			.append("\" id=\"module_").append(typeInfo[0]).append("$").append(processStatus).append("_body\" class=\"module_body\">");
		bodyHtml.append("	<div id=\"module_").append(typeInfo[0]).append("_ul\" class=\"module_div\">"); 
		if(workflowMap != null && !workflowMap.isEmpty()){
			List<Object[]> workflowList = workflowMap.get(typeInfo[0].toString());
			if(workflowList != null && !workflowList.isEmpty()) {
				for(int j=0;j<workflowList.size();j++){
					Object[] workflowInfo = workflowList.get(j);
					String workflowName = workflowInfo[0].toString();
					String encodeWorkflowName = null;
					Object objFormId = workflowInfo[1];
					String formId = "0";
					if(objFormId != null) {
						formId = String.valueOf(objFormId);
						if(formId.indexOf(",")!=-1) {
							formId = formId.split(",")[1];
						}
					}
					
					try {
						encodeWorkflowName = URLEncoder.encode(URLEncoder.encode(workflowName, "utf-8"),"utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					bodyHtml.append("<ul><li>");
					bodyHtml.append("<a href=\"#\" onclick='openForm(\"");
					bodyHtml.append(getRootPath()).append(href); 
					if(href.indexOf("?")!=-1){
						if(!freedomWorkflow){
							bodyHtml.append("&workflowName=").append(encodeWorkflowName);
						}
						bodyHtml.append("&formId=").append(formId); 
					}else{
						if(!freedomWorkflow){
							bodyHtml.append("?workflowName=").append(encodeWorkflowName); 
						}
						bodyHtml.append("&formId=").append(formId);
					}
					if(!"".equals(processStatus)) {
						bodyHtml.append("&state=").append(processStatus); 
					}
					if(!freedomWorkflow){
						bodyHtml.append("&workflowType=").append(typeInfo[0]); 
					}
					bodyHtml.append("\","+isPop+");'>");
					bodyHtml.append("<span>");
					bodyHtml.append(workflowName);
					if(workflowInfo.length == 3) {
						bodyHtml.append("(<b><font color='red'>").append(workflowInfo[2]).append("</font></b>)");
					}
					bodyHtml.append("</span>");
					bodyHtml.append("</a>");
					bodyHtml.append("</ul></li>");
					
				}
			}
			
		}
		
		bodyHtml.append("	</div>");
		bodyHtml.append("</div>");
		bodyHtml.append("</div>");
		return bodyHtml;
	}
	
	private String getRootPath(){
		HttpServletRequest httpServletRequest = (HttpServletRequest)pageContext.getRequest();
		String rooturl=httpServletRequest.getContextPath();
		return rooturl;
	}

	private String getFrameRoot() {
		String frameroot= (String) pageContext.getSession().getAttribute("frameroot"); 
		if(frameroot==null||frameroot.equals("")||frameroot.equals("null")){
			frameroot= "/frame/theme_gray";
		}
		return	getRootPath() + frameroot;
	}
	
	public void setTypeList(List<Object[]> typeList) {
		this.typeList = typeList;
	}

	public void setWorkflowMap(Map<String, List<Object[]>> workflowMap) {
		this.workflowMap = workflowMap;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getIsPop() {
		return isPop;
	}

	public void setIsPop(String isPop) {
		this.isPop = isPop;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public List<Object[]> getLeftTypeList() {
		return leftTypeList;
	}

	public void setLeftTypeList(List<Object[]> leftTypeList) {
		this.leftTypeList = leftTypeList;
	}

	public Map<String, List<Object[]>> getLeftWorkflowMap() {
		return leftWorkflowMap;
	}

	public void setLeftWorkflowMap(Map<String, List<Object[]>> leftWorkflowMap) {
		this.leftWorkflowMap = leftWorkflowMap;
	}

	public List<Object[]> getRightTypeList() {
		return rightTypeList;
	}

	public void setRightTypeList(List<Object[]> rightTypeList) {
		this.rightTypeList = rightTypeList;
	}

	public Map<String, List<Object[]>> getRightWorkflowMap() {
		return rightWorkflowMap;
	}

	public void setRightWorkflowMap(Map<String, List<Object[]>> rightWorkflowMap) {
		this.rightWorkflowMap = rightWorkflowMap;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getTitleTemp() {
		return titleTemp;
	}

	public void setTitleTemp(String titleTemp) {
		this.titleTemp = titleTemp;
	}

	public boolean isFreedomWorkflow()
	{
		return freedomWorkflow;
	}

	public void setFreedomWorkflow(boolean freedomWorkflow)
	{
		this.freedomWorkflow = freedomWorkflow;
	}

}
