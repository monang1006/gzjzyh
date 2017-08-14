package com.strongit.tag.web.workflow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.strongit.oa.common.eform.model.EFormComponent;
import com.strongit.tag.web.date.DateTag;
import com.strongit.tag.web.util.ResponseUtils;

/**
 * 自定义查询标签
 * @author 邓志城
 * @company  Strongit Ltd. (C) copyright
 * @date 2010-11-11 下午01:45:54
 * @version  2.0.7
 * @classpath com.strongit.tag.web.workflow.QueryTag
 * @comment
 * @email dengzc@strongit.com.cn
 */
public class QueryTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6681754253986703663L;

	private List<EFormComponent> queryColumnList;//查询字段列表
	
	@Override
	public int doStartTag() throws JspException {
		StringBuilder html = new StringBuilder();//生成HTML数据
		startTable(html);
		createBody(html);
		endTable(html);
		ResponseUtils.write(pageContext, html.toString());
		return EVAL_PAGE;
	}

	StringBuilder initHtmlStyle(StringBuilder style) {
		if(style != null) {
			style.append("<link href=\"").append(getFrameRoot()).append("/css/properties_windows_list.css\" type=\"text/css\" rel=\"stylesheet\" />");
		}
		return style;
	}

	void startTable(StringBuilder html) {
		//html.append("<table id=\"searchtable\" style=\"display:none;\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" >");
		//html.append("<input type='button' class='input_bg' value='搜 索' onclick=\"document.getElementById('myTableForm').submit();\" />");
		//html.append("&nbsp<img id='img_sousuo' src=\""+getRootPath()+"/images/ico/sousuo.gif\"" +
				//" width=\"15\" height=\"15\" alt=\"单击进行搜索\" onclick=\"document.getElementById('myTableForm').submit();\">&nbsp");
//	    html.append("<table id=\"searchtable\" style=\"display:none;\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"1\" >");
	    html.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"table1_search\" >");
//        html.append("<script language=\"javascript\">");
//        html.append("$(document).ready(function(){");
//        html.append("var temp = $(\"#nodeName\").val()+\"\"; if(temp!=\"undefined\" && temp!=\"\"){ $(\"#searchtable\").show();  $(\"#searchtable\").show(); $(\"#label_search\").text(\"隐藏查询条件\");return;}");
//        html.append("$.each( $(\".searchinput\"), function(i, n){");
//        html.append(" if(n&&n.value!=\"\"){ $(\"#searchtable\").show(); $(\"#searchtable\").show(); $(\"#label_search\").text(\"隐藏查询条件\");return; }");
//        html.append("}); ");
//        html.append("}); ");
//        html.append("</script>");
	      html.append("<tr><td>");
	}

	void createBody(StringBuilder html) {
		if(queryColumnList != null && !queryColumnList.isEmpty()) {
//			List<EFormComponent> queryColumnListTemp = new LinkedList<EFormComponent>();
			for(int i=0;i < queryColumnList.size();i++) {
				EFormComponent eform = queryColumnList.get(i);
				/*if(i % 6 == 0 && i != 0) {
					html.append("</td>");
					html.append("</tr>");
				}
				if(i % 6 == 0) {
					html.append("<tr>");
					html.append("<td>");
				}*/
//				if(eform.getType().equals("Strong.Form.Controls.DateTimePicker")){
//					queryColumnListTemp.add(eform);
//					continue;
//				}
				
				html.append(createComponent(eform));
				html.append("&nbsp;");
				
			}
//			if(!queryColumnListTemp.isEmpty()){
//				for(int i=0;i < queryColumnListTemp.size();i++) {
//					EFormComponent eform = queryColumnListTemp.get(i);
//					if(!eform.getLable().equals("-")){
//						html.append("<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
//					}
//					html.append(createComponent(eform));
//					html.append("&nbsp;");
//				}
//			}else{
//				queryColumnListTemp = null;
//			}
			/*if(!html.toString().endsWith("</tr>")) {
				html.append("</td></tr>");
			}*/
		}
	}
	
	void endTable(StringBuilder html) {
//		html.append("&nbsp<input id=\"img_sousuo\" type=\"button\"" 
//                + " width=\"15\" height=\"15\" alt=\"单击进行搜索\" onclick=\"document.getElementById('myTableForm').submit();\">&nbsp");
		html.append("&nbsp;&nbsp;&nbsp;&nbsp;<input id=\"img_sousuo\" type=\"button\" title=\"单击进行搜索\" onclick=\"document.getElementById('myTableForm').submit();\"/>");
		html.append("</td></tr></table>");
	}

	/**
	 * 生成Html组件
	 * @author:邓志城
	 * @date:2010-11-11 下午04:27:17
	 * @param eform
	 * @return
	 */
	StringBuilder createComponent(EFormComponent eform) {
		StringBuilder component = new StringBuilder();
		if(eform.getType().equals("Strong.Form.Controls.Edit")) {//单行|多行文本框
			component.append("&nbsp;&nbsp;").append(eform.getLable()).append("：&nbsp;");
			component.append("<input class=\"search\" type=\"text\" name=\"").append(eform.getFieldName())
			.append("\" title=\"请输入").append(eform.getLable())
			.append("\" value=\"")
				.append(eform.getValue()== null?"":eform.getValue()).append("\"" +
						//modify yanjian 2011-12-03 文本框搜索添加验证
//						"onkeyup=\"if(value.match(/[^(^\u4E00-\u9FA5|a-z|A-Z|0-9)]/g)){alert('只能输入数字、字母和汉字');} value=value.replace(/[^(\u4E00-\u9FA5|a-z|A-Z|0-9)]/g,'')\"" +
						//-end
						"/>");
		}else if(eform.getType().equals("Strong.Form.Controls.DateTimePicker")) {//日期类型
			DateTag dateTag = new DateTag();
			dateTag.setName(eform.getFieldName());//名称
			dateTag.setId(eform.getFieldName());//ID
			dateTag.setSkin("whyGreen");
			dateTag.setIsicon(true);
			dateTag.setDateform("yyyy-MM-dd");
			String value = eform.getValue();
			if(value != null && !"".equals(value)) {
				java.text.DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				try {
					date = format.parse(value);
					dateTag.setDateobj(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			String dataHtml = dateTag.htmlFactory();
			component.append("<span class=\"wz  searchinput\">").append(eform.getLable()).append("&nbsp;</span>");
			component.append(dataHtml);
		}else if(eform.getType().equals("Strong.Form.Controls.ComboxBox")) {
			component.append("	<span class=\"wz searchinput\">").append(eform.getLable()).append(":&nbsp;</span>");
			component.append("	<select class=\"searchinput\" onchange=\"$('#img_sousuo').click();\"  name=\"").append(eform.getFieldName()).append("\">");
			component.append("<option value=\"\">全部</option>"); 
			String items = eform.getItems();
			String value = eform.getValue();
			String[] itemData = items.split(";");
			for(String data : itemData) {
				if(!"".equals(data)) {
					String[] datas = data.split(",");
					if(value != null && value.equals(datas[0].toString())) {
						component.append("<option value=\"").append(datas[0]).append("\" selected=\"selected\">").append(datas[1]).append("</option>"); 
					}else{
						component.append("<option value=\"").append(datas[0]).append("\">").append(datas[1]).append("</option>"); 
					}
				}
			}
			component.append("	</select>");
		}
		return component;
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
	
	public List<EFormComponent> getQueryColumnList() {
		return queryColumnList;
	}

	public void setQueryColumnList(List<EFormComponent> queryColumnList) {
		this.queryColumnList = queryColumnList;
	}
	
	
	
	
}
