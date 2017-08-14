<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<HTML><HEAD><TITLE>操作内容</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<!--引用页面样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
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
<s:form theme="simple" id="myTableForm" action="/baselog/baseLog.action">
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
				<strong>登录日志列表</strong>
				</td>
			<td align="right">
            <table  border="0" align="right" cellpadding="0" cellspacing="0">
            	<tr>
            	    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					<td class="Operation_list" onclick="view();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				    <td width="5"></td>
					<td width="2%"></td>
				</tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				<tr>
					<td>
     <webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="logId" isCanDrag="true"  isCanFixUpCol="true" clickColor="#A9B2CA" ondblclick="viewDBclick(this.value);" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
		 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
		  <tr>
			   <td>
				 <div style="float: left;">&nbsp;&nbsp;登录名：&nbsp;<s:textfield name="username"  cssClass="search" title="请输入登录名"></s:textfield></div>
				 <div style="float: left;">&nbsp;&nbsp;操作描述：&nbsp;<s:textfield name="desc"  cssClass="search" title="请输入操作描述"></s:textfield></div>
				 <div style="float: left;width:450px">&nbsp;&nbsp;操作结果：&nbsp;<s:select name="result"  list="#{'':'请选择','1':'成功','0':'失败'}" cssClass="search" title="请输入操作结果" onchange='$("#img_sousuo").click();'></s:select>
				 &nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button"/></div>
				</td>
		</tr>
       </table>
		<webflex:flexCheckBoxCol caption="选择" property="logId" showValue="logOpname" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
		<webflex:flexTextCol caption="登录名" property="logOpname" showValue="logOpname" align="center" width="12%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexTextCol caption="操作描述" property="rest3" showValue="rest3" width="15%" align="center" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexEnumCol caption="操作结果" mapobj="${resultMap}" property="logOpresult"  align="center" width="12%" showValue="logOpresult" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
		<webflex:flexTextCol caption="操作人IP" property="logOpip" showValue="logOpip" width="16%" align="center" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		<webflex:flexDateCol caption="操作开始时间 " property="logStartDate" showsize="50"
									showValue="logStartDate" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	     <webflex:flexDateCol caption="操作结束时间 " property="logEndDate" showsize="50"
									showValue="logEndDate" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
	     </webflex:flexTable>
      
  </tr>
</table>
</s:form>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","view",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function view(){
	var id=getValue();
	
	if(id==null || id==""){
		alert("请选择要查看的记录。");
		return;
	}
	if(id.split(",").length > 1){
		alert("只可以查看一条记录。");
		return;
	}
	window.showModalDialog("<%=path%>/baselog/baseLog!view.action?logId="+id,window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
}

function viewDBclick(id) {
	window.showModalDialog("<%=path%>/baselog/baseLog!view.action?logId="+id,window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        	gotoPage(1);
        });     
      });
      
</script>
</BODY></HTML>
