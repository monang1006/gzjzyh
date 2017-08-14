<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!-- 列表外部样式引用改变 -->
        <link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link type="text/css" rel="stylesheet"
			href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<!-- <script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script> -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" theme="simple"
				action="/systemlink/sysLink.action">
				<s:actionmessage />
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
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
				<strong>外部系统链接列表</strong>
				</td>
			<td align="right">
            <table  border="0" align="right" cellpadding="0" cellspacing="0">
            	<tr>
            	    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					<td class="Operation_list" onclick="sysLinkAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				    <td width="5"></td>
				    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					<td class="Operation_list" onclick="sysLinkEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				    <td width="5"></td>
            	    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					<td class="Operation_list" onclick="sysLinkDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
				<webflex:flexTable name="myTable" width="100%" height="365px"
								wholeCss="table1" property="linkId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}" ondblclick="view(this.value);">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;系统名称：&nbsp;<input id="systemName" name=systemName type="text" style="width:120px;" class="search" title="请输入系统名称" value="${systemName }"></div>
							       		<div style="float: left;">&nbsp;&nbsp;外部系统链接：&nbsp;<input id="linkUrl" name="linkUrl" type="text" style="width:120px;"
												 class="search" title="请输入外部系统链接"
												value="${linkUrl }"></div>
							       		<div style="float: left;width:450px">&nbsp;&nbsp;系统描述：&nbsp;<input id="systemDesc" name="systemDesc" type="text" style="width:120px;"
												 class="search" title="请输入系统描述"
												value="${systemDesc }">
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="submitForm()"/></div>
							       	</td>
							     </tr>
							</table> 
									
								<webflex:flexCheckBoxCol caption="选择" property="linkId"
									showValue="systemName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="外部系统名称" property="linkId"
									showValue="systemName" width="15%" isCanDrag="true"
									isCanSort="true" onclick="view(this.value)"></webflex:flexTextCol>
								<webflex:flexTextCol caption="外部系统链接" property="linkUrl"
									showValue="linkUrl" width="35%" isCanDrag="true" showsize="40"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="系统描述" property="systemDesc"
									showValue="systemDesc" width="45%" isCanDrag="true"
									showsize="50" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","sysLinkAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);

	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","sysLinkEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);

	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","sysLinkDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function sysLinkAdd(){
	window.showModalDialog("<%=path%>/systemlink/sysLink!input.action",window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
}
function sysLinkEdit(){
	var id=getValue();
	if(id==""){
		alert("请选择要编辑的记录。");
	}else if(id.indexOf(",")!=-1){
		alert("只可以编辑一条记录。");
	}
	else
		window.showModalDialog("<%=path%>/systemlink/sysLink!input.action?linkId="+id,window,'help:no;status:no;scroll:no;dialogWidth:650px; dialogHeight:350px');
}
function sysLinkDel(){
	var id=getValue();
	if(id=="")
		alert("请选择要删除的记录。");
	else{
		if(confirm("确定要删除吗？")){
			$.ajax({
				type:"post",
				url:"<%=path%>/systemlink/sysLink!delete.action",
				data:{
					linkId:id			
				},
				success:function(data){
					if(data != ""){
					     alert(data+"删除失败。");	
					}
				    location= window.location;
				},
				error:function(data){
					alert("对不起，操作异常"+data);
				}
			
			});									
		}
	}
}

function view(value){
	window.showModalDialog("<%=path%>/systemlink/sysLink!view.action?linkId="
								+ value, window,
						'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:350px');
	}

	function submitForm() {
		document.getElementById("myTableForm").submit();
		}
</script>
	</BODY>
</HTML>
