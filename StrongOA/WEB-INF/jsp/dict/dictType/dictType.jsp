<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--列表样式 -->
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/menu/menu.js" language="javascript"></script>
		<script src="<%=path%>/common/js/grid/ChangeWidthTable.js" language="javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
		<s:form id="myTableForm" theme="simple" action="/dict/dictType/dictType.action">
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
													<strong>字典类列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
														  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="dictAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="dictEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="dictDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
			
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="dictCode" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection='${page.result}'
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;字典类值：&nbsp;<input name="model.dictValue" type="text"  value="${model.dictValue}"  class="search" title="请输入字典类值"></div>
							       		<div style="float: left;width:250px">&nbsp;&nbsp;字典类名称：&nbsp;<input name="model.dictName" type="text"  value="${model.dictName}"  class="search" title="请输入字典类名称"></div>
							       		<div style="float: left;width:250px;margin-top: 5px;">&nbsp;&nbsp;系统字典类：&nbsp;<s:select name="model.dictIsSystem" list="#{'':'请选择系统类别','1':'是','0':'否'}"  listKey="key" listValue="value"    onchange="javascript:document.getElementById('myTableForm').submit();"  /></div>
							       		<div style="float: left;width:450px;margin-top: 3px;">&nbsp;&nbsp;字典类类型：&nbsp;<s:select name="model.dictType" list="#{'':'请选择类型','A':'国标码','B':'地方码','C':'自定义码','D':'其他'}" listKey="key"  listValue="value"  onchange="javascript:document.getElementById('myTableForm').submit();"  />
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/></div>
							       	</td>
							     </tr>
							</table> 
								<webflex:flexCheckBoxCol caption="选择" property="dictCode" showValue="dictName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="字典类值" property="dictValue" showValue="dictValue" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="字典类名称" property="dictName" showValue="dictName" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="是否为系统字典类" mapobj="${sysmap}" property="dictIsSystem" showValue="dictIsSystem" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="字典类类型" mapobj="${typemap}" property="dictType" showValue="dictType" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","dictAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","dictEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","dictDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function dictAdd(){
	window.showModalDialog("<%=path%>/dict/dictType/dictType!input.action",window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
}
function dictEdit(){
	var dictCode = getValue();
	if(dictCode==""||dictCode=="null"){
		alert("请选择要编辑的记录。");
		return;
	}
	else if(dictCode.indexOf(",")!=-1){
		alert("只可以编辑一条记录。");
		return;
	}
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==dictCode){
			var showValue = chkButtons[i].showValue;
			if(showValue=="1"){
				alert("该字典类是系统字典类，不可修改。");
				return;
			}
			break;
		}
	}
	window.showModalDialog("<%=path%>/dict/dictType/dictType!input.action?dictCode="+dictCode,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:250px');
}
function dictDel(){
	var id=getValue();
	if(id==null||id==""||id=="null"){
		alert("请选择要删除的记录。");
		return;
	}
	if(!confirm("确定要删除吗？")){
        return;
    }
    
    $.ajax({
		type:"post",
		url:"<%=path%>/dict/dictType/dictType!isExistDictItem.action",
		data:{
			dictCode:id			
		},
		success:function(data){
			if(data==""||(data!=""&&confirm(data)==true)){		
				 location="<%=path%>/dict/dictType/dictType!delete.action?dictCode="+id;
			}
		},
		error:function(data){
			alert("对不起，操作异常"+data);
		}	
	});
	
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
