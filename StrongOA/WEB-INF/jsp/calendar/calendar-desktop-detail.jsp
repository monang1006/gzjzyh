<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.bo.ToaCalendar"/>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=root%>/oa/css/desktop/style.css">
	<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
    
    <title>calendar-desktop-detail.jsp</title>
    <style>
	.linkgray10{
					color:#999;
					font-size:10px;
				}
    </style>
  </head>
  
  <body style="background-color: #F5F5F5 ; ">
    <% 
		List  list= (List)request.getAttribute("list");
		StringBuffer innerHtml = new StringBuffer();		
		StringBuffer link = new StringBuffer();
		link.append("javascript:parent.window.parent.refreshWorkByTitle('").append(path)
			.append("/calendar/calendar.action")
			.append("', '日程安排'")
			.append(");");
		for(int i=0;i<list.size();i++){//获取在条数范围内的列表
			ToaCalendar cal = (ToaCalendar)list.get(i);
			
			if(cal.getCalCon()==null|cal.getCalCon()==""){
				cal.setCalCon("无说明");
			}
			//标题连接
			String url = "";
			url = path+"/calendar/calendar!input.action?calendarId="+cal.getCalendarId();
			String titleLink = "var a = parent.window.showModalDialog('"+url+"',window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');if(a=='reload'){location.reload();}else{return;}";
			
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			innerHtml.append("<div class=\"linkdiv\" title=\"\">");
			innerHtml.append("	<IMG SRC=\"").append(path).append("/oa/image/desktop/littlegif/news_bullet.gif\" WIDTH=\"15\" HEIGHT=\"10\" BORDER=\"0\" ALT=\"\">");
			String title = cal.getCalTitle();
			if(title.length()>9)//如果显示的内容长度大于设置的主题长度，则过滤该长度
				title = title.substring(0,9)+"...";
			innerHtml.append("	<span title=\"").append(/*cal.getCalCon() 显示内容会有<>标签的问题*/cal.getCalTitle()).append("\"><span class=\"hand\" onclick=\"").append(titleLink).append("\"> ").append(title).append("</span>");
			innerHtml.append("</span><br>");
			innerHtml.append("	<span class =\"linkgray10\">开始：(").append(st.format(cal.getCalStartTime())).append(")<br>结束：(").append(st.format(cal.getCalEndTime())).append(")</span>");
			innerHtml.append("	</div>");
		}
		out.print(innerHtml.toString());
	%>
  </body>
</html>
