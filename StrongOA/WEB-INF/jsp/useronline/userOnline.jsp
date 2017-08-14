<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<HTML><HEAD><TITLE>操作内容</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<!-- 列表外部样式引用改变 -->
<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
			<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
			<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
</HEAD>
<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
<DIV id=contentborder align=center>
<s:form theme="simple" id="myTableForm" action="/useronline/userOnline.action">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
  <tr>
    <td height="100%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="3" class="table_headtd">
       <table width="100%" border="0" cellspacing="0"
											cellpadding="00">
        <tr>
          <td class="table_headtd_img" >
			<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
				</td>
				<td align="left">
				<strong>在线用户列表</strong>
				</td>
			</tr>
				</table>
				</td>
				<tr>
					<td>
     <webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="userId" isCanDrag="true"  isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
		  <tr>
			   <td>
				 <div style="float: left;width:450px">&nbsp;&nbsp;用户名称：&nbsp;<s:textfield name="username"  cssClass="search" title="请输入用户名称"></s:textfield>
				 &nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button"/></div>
				</td>
		</tr>
       </table>
        
		<webflex:flexCheckBoxCol caption="选择" property="userId" showValue="userRealName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="用户名称" property="userRealName" showValue="userRealName" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="登录IP地址" property="loginIp" showValue="loginIp" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="登录时间" property="loginDate" showValue="loginDate" width="35%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
	</webflex:flexTable>
      </td>
  </tr>
</table>
</s:form>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
      
</script>
</BODY></HTML>
