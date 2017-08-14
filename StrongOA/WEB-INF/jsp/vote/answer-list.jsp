<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<HTML><HEAD><TITLE>查看问题答案</TITLE>
<%@include file="/common/include/meta.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
        <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
		
<SCRIPT>
</SCRIPT>

</HEAD>
<base target="_self ">
<BODY class=contentbodymargin oncontextmenu="return false;">
<DIV id=contentborder align=center>
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;" >
  <tr>
    <td width="100%" height="100%">
     <s:form theme="simple" id="myTableForm" target="_self" action="/vote/answer!answerTextPage.action">	
		<table width="100%" border="0" cellspacing="0" cellpadding="0" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);" > 
		 <tr>
		   <td height="10" >
			   <table width="100%" border="0" cellspacing="0" cellpadding="0">
				 <tr>
				<td width="20%" align="left">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;查看问题答案
					</td>
				</tr>
					</table>
				</td>
			</tr>
		</table>
	
	<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="surveyId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="null" getValueType="getValueByProperty" 
      collection="${page.result}" page="${page}">   
       <input type="hidden" name="qid" value="${qid}" />
	  	<webflex:flexTextCol caption="答案内容"  property="content" showValue="content" width="100%" showsize="45" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
		</s:form>
</td></tr></table>
</DIV>
</BODY>
</HTML>
