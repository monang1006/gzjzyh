<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<title>信息模板</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet" />
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet />
		<link href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet />
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<%--		<script src="<%=path%>/common/js/common/search.js"--%>
<%--			type="text/javascript"></script>--%>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<DIV id=contentborder align=center>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td height="40"
										style="FILTER: progid :     DXImageTransform.Microsoft.Gradient (   gradientType =     0, startColorStr =   #ededed, endColorStr =     #ffffff );">
		<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
			<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>模板列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addTemplate();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editTemplate();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="deleteTemplate();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="2%"></td>
					                 </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>	
				<td>
					<s:form id="myTableForm" action="/infopubTemplate/infoTemplate!list.action" method="post">
					<webflex:flexTable name="myTable" width="100%" height="370px"
											wholeCss="table1" property="code" isCanDrag="true"
											isCanFixUpCol="true" clickColor="#A9B2CA"
											footShow="showCheck" getValueType="getValueByProperty"
											collection='${page.result}' page="${page}">
					        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;模板名称：&nbsp;<input name="templateName" id="templateName"  type="text" class="search" title="请您输入模板名称" value="${model.templateName }">
							       		&nbsp;&nbsp;模板标识：&nbsp;<input name="templateDesc" id="templateDesc" type="text"  class="search" title="请您输入模板标识" value="${model.templateDesc }">
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							</table> 
							<webflex:flexCheckBoxCol caption="选择" property="templateId" 
								showValue="afficheTitle" width="4%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="模板名称" property="templateName" 
								showValue="templateName" width="50%" isCanDrag="true" isCanSort="true" ></webflex:flexTextCol>
							<webflex:flexTextCol caption="模板标识" property="templateDesc" align="center"
								showValue="templateDesc" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
		
						</webflex:flexTable>
					</s:form>
				</td>
			</tr>
		</table>
		</DIV>
		<script language="javascript">
		$(document).ready(function(){
			$("#img_sousuo").click(function(){
				$("form").submit();
			});
		}); 
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTemplate",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editTemplate",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","deleteTemplate",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			
// 添加一条信息模板
function addTemplate(){
	var width=screen.availWidth/2-100;
 	var height=screen.availHeight/2-115;
	var result=window.showModalDialog("<%=path%>/infopubTemplate/infoTemplate!input.action",
										window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+"status:no;help:no;scroll:no;");
		}
		
function editTemplate(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要编辑的记录。");
			return;
		}
		
	if(id.length >32){
		alert('只可以编辑一条记录。');
		return;
	}
	var width=screen.availWidth/2-100;
 	var height=screen.availHeight/2-115;
	var result=window.showModalDialog("<%=path%>/infopubTemplate/infoTemplate!input.action?tId="+id,
										window,"dialogWidth:" + width + "pt;dialogHeight:" + height + 
										"pt;"+"status:no; help:no; scroll:no; resizable:yes; menubar:yes; minimize:yes; maximize:yes;");
}

function deleteTemplate(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择要删除的记录。");
			return;
		}

	if(confirm("确定要删除吗？")){
   		location="<%=path%>/infopubTemplate/infoTemplate!delete.action?tId="+id;	
 	}
}		




function search() {
	submitForm();
}
function submitForm(){
	document.getElementById("myTableForm").submit();
}
</script>
	</body>
</html>
