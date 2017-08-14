<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/menu/menu.js" language="javascript"></script>
		<script src="<%=path%>/common/js/grid/ChangeWidthTable.js"
			language="javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
			<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
			<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>-->
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple"
							action="/infotable/infotype/infoType.action">
							<input id="infoSetCode" name="infoSetCode" type="hidden"
								size="32" value="${infoSetCode}">
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
													<strong>信息项分类列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyTypeAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyTypeEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyTypeDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
								
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="propertypeId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;width:450px">&nbsp;&nbsp;信息项分类名称：&nbsp;<input name="model.propertypeName"  type="text"  value="${model.propertypeName}" class="search" title="请输入信息项分类名称">
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/></div>
							       	</td>
							     </tr>
							</table> 
								<webflex:flexCheckBoxCol caption="选择" property="propertypeId"
									showValue="propertypeName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="信息项分类名称" property="propertypeName"
									showValue="propertypeName" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","propertyTypeAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","propertyTypeEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","propertyTypeDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
var infoSetCode = document.getElementById("infoSetCode").value;
function propertyTypeAdd(){
	window.showModalDialog("<%=path%>/infotable/infotype/infoType!input.action?infoSetCode="+infoSetCode,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:250px');
}
function propertyTypeEdit(){
	var propertypeId = getValue();
	if(propertypeId == null||propertypeId == ''){
		alert('请选择要编辑的记录。');
		return;
	}
	if(propertypeId.length > 32){
		alert('只可以编辑一条记录。');
		return;
	}
	window.showModalDialog("<%=path%>/infotable/infotype/infoType!input.action?propertypeId="+propertypeId+"&infoSetCode="+infoSetCode,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:250px');
}
function propertyTypeDel(){
	var propertypeId = getValue();
	if(propertypeId == null||propertypeId == ''){
		alert('请选择要删除的记录。');
		return;
	}
	if(!confirm("确定要删除吗？")){
        return;
    }
	window.showModalDialog("<%=path%>/infotable/infotype/infoType!delete.action?propertypeId="+propertypeId+"&infoSetCode="+infoSetCode,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:250px');
}
function search(){
	submitForm();
}
function submitForm(){
	document.getElementById("myTableForm").submit();
}
</script>
	</BODY>
</HTML>
