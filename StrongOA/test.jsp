<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.net.URLDecoder"/>
<jsp:directive.page import="java.text.SimpleDateFormat"/>
<jsp:directive.page import="java.util.Date"/>
<HTML>
<head>
<title>无标题文档</title>
<style type="text/css">
*{ padding:0; margin:0; }
.jkkf{ padding:5px; background-color:#fff; font-size:12px; color:#000; font-family:"宋体"; }
.jkkf h3{ padding:3px; font-size:12px; background-color:#fef8c4; color:#8b460d; border-bottom:2px solid #d3d2be; line-height:1.5; }
.jkkf td{ padding:6px 0 6px 12px; border-bottom:1px solid #ccc;font-size:12px; color:#000; font-family:"宋体"; }
</style>
</head>
  <body scroll="no">
  	<div class="jkkf">
		<h3>有任务需要你处理</h3>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
		  <tr>
		    <td colspan="4"><b>任务审批</b>
		    	<%
		    		String taskName = request.getParameter("taskName");
		    		if(taskName != null){
		    			taskName = URLDecoder.decode(taskName,"utf-8");
		    			out.println(taskName);
		    		}
		    	%>
		    </td>
		  </tr>
		  <tr>
		    <td align="right" valign="top"><b>处理人</b></td>
		    <td valign="top">
		    <%
		    	String userName = request.getParameter("userName");
		    	if(userName != null){
		    		userName = URLDecoder.decode(userName,"utf-8");
		    	}
		    	out.println(userName);
		    %>
		    </td>
		    <td align="right" valign="top"><b>紧急程度</b></td>
		    <td valign="top">
		    	<%
		    		String jjcd = request.getParameter("jjcd");
		    		if("1".equals(jjcd)){
		    			out.println("平急");
		    		} else if("2".equals(jjcd)){
		    			out.println("加急");
		    		} else if("3".equals(jjcd)){
		    			out.println("特急");
		    		} else if("4".equals(jjcd)){
		    			out.println("特提");
		    		} else {
		    			out.println("无");
		    		}
		    	%>
		    </td>
		  </tr>
		  <tr>
		    <td align="right" valign="top"><b>创建人</b></td> 
		    <td valign="top">
		    <%
		    	String startUserName = request.getParameter("startUserName");
		    	if(startUserName != null){
		    		startUserName = URLDecoder.decode(startUserName,"utf-8");
		    	} else {
		    		startUserName = userName;
		    	}
		    	out.println(startUserName);
		    %>
		    </td>
		    <td align="right" valign="top"><b>创建时间</b></td> 
		    <td valign="top">
		    <%
		    	String startDate = request.getParameter("startDate");
		    	out.println(startDate);
		    %>	
		    </td>
		  </tr>
		  <tr>
		    <td colspan="1" align="right" valign="top"><b>描述</b></td>
		    <td colspan="3" valign="top">
		    <%
		    	String suggestion = request.getParameter("suggestion");
		    	if(suggestion != null){
		    		suggestion = URLDecoder.decode(suggestion,"utf-8");
		    	}
		    	out.println(suggestion);
		    %>
		    </td>
		  </tr>
		  <tr>
		    <td colspan="4" align="right"><a href="#">查看详情&gt;&gt;&gt;</a></td>
		  </tr>
		</table>
	</div>
  </body>
</html>
