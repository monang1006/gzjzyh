<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
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
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript"
			src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script> -->
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>	
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
			<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple"
							action="/dict/dictItem/dictItem.action">
							<input name="dictCode" value="${dictCode}" type="hidden">
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
													<strong>字典项列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="importExcels();"><img src="<%=root%>/images/operationbtn/daoru.png"/>&nbsp;导&nbsp;入&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
														 <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="dictitemAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="dictitemEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="dictitemDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
								wholeCss="table1" property="dictItemCode" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection='${page.result}'
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;字典项值：&nbsp;<input name="model.dictItemValue" 
												value="${model.dictItemValue}" type="text"
												 class="search" title="请输入字典项值"></div>
							       		<div style="float: left;width:250px">&nbsp;&nbsp;字典项名称：&nbsp;<input name="model.dictItemName" 
												value="${model.dictItemName}" type="text"
												 class="search" title="请输入字典项名称"></div>
							       		<div style="float: left;">&nbsp;&nbsp;查询码：&nbsp;<input name="model.dictItemDescspell" 
												value="${model.dictItemDescspell}" type="text"
												 class="search" title="请输入查询码"></div>
							       		<div style="float: left;width:350px">&nbsp;&nbsp;可选状态：&nbsp;<s:select name="model.dictItemIsSelect" 
												list="#{'':'请选择可选状态','0':'可选','1':'不可选'}"  
												onchange="javascript:document.getElementById('myTableForm').submit();" />
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/></div>
							       	</td>
							     </tr>
							</table> 
								
								<webflex:flexCheckBoxCol caption="选择" property="dictItemCode"
									showValue="dictItemName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="字典项值" property="dictItemValue"
									showValue="dictItemValue" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="字典项名称" property="dictItemName"
									showValue="dictItemName" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="查询码" property="dictItemDescspell"
									showValue="dictItemDescspell" width="20%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="可选状态" mapobj="${seltypemap}"
									property="dictItemIsSelect" showValue="dictItemIsSelect"
									width="25%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</webflex:flexTable>
							</td>
							</tr>
							</table>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","dictitemAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","dictitemEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","dictitemDel",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/daoru.png","导入","importExcels",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//导入导出EXCEL数据
function importExcels(){
	var dictCode="${dictCode}";
	if(dictCode==""||dictCode=="null"){
		alert("请先选择字典类！");
		return;
	}
  
	var ret=window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=dict/dictItem/dictItem-import.jsp?dictCode="+dictCode,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:150px');
	// window.open ('<%=path%>/fileNameRedirectAction.action?toPage=organisemanage/organisemanage-import.jsp', 'newwindow', 'height=200, width=600, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes'); //这句要写成一
  
 }
 
function dictitemAdd(){
	//location="dictitemAdd.jsp"
	var dictCode="${dictCode}";
	if(dictCode==""||dictCode=="null"){
		alert("请先选择字典类！");
		return;
	}
	
	window.showModalDialog("<%=path%>/dict/dictItem/dictItem!input.action?dictCode="+dictCode,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:350px');
}
function dictitemEdit(){
	//location="dictitemEdit.jsp"
	var dictitemcode =getValue();
	var dictCode="${dictCode}";
	if(dictitemcode==""||dictitemcode=="null"){
		alert("请选择要编辑的记录。");
		return;
	}
	else if(dictitemcode.indexOf(",")!=-1){
		alert("只可以编辑一条记录。");
		return;
	}
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==dictitemcode){
			var showValue = chkButtons[i].showValue;
			if(showValue=="1"){
				alert("该字典项是系统字典项，不可修改！");
				return ;
			}
			break;
		}
	} 
	window.showModalDialog("<%=path%>/dict/dictItem/dictItem!input.action?dictItemCode="+dictitemcode+"&dictCode="+dictCode,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:350px');
}
function dictitemDel(){
	var dictitemcode = getValue();
	var dictCode="${dictCode}";
	if(dictitemcode==""||dictitemcode=="null"){
		alert("请选择要删除的记录。");
		return;
	}
	if(!confirm("确定要删除吗？")){
        return;
    }
	location = "<%=path%>/dict/dictItem/dictItem!delete.action?dictItemCode="+dictitemcode+"&dictCode="+dictCode;
}
function search(){
	submitForm();
}
function submitForm(){
	document.getElementById("myTableForm").submit();
}

function reloadLocation(msg){
	if(msg.indexOf("成功")==-1)
		alert(msg);
	submitForm();
}
</script>
	</BODY>
</HTML>
