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
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/menu/menu.js" language="javascript"></script>
		<script src="<%=path%>/common/js/grid/ChangeWidthTable.js" language="javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
			
			function structureDate(){
				if(!confirm("对已构建信息集的数据同步可能需要花费一段时间，确定要继续操作吗？")){
					return;
				}
				show("同步已构建信息集初始化字段,请耐心等待...");
				$.post("<%=path%>/infotable/infoset/infoSet!dataInfoSet.action",
					function(data){
						if(data == "ok"){
							show("对已构建信息集同步初始化字段完成。");
							setTimeout("hidden()", 2000);
<%--							setTimeout("over()", 2000);--%>
						}else{
							show("对已构建信息集同步初始化字段失败，请重试或与管理员联系。");
							setTimeout("hidden()", 2000);
						}
						
					});
			}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple" action="/infotable/infoset/infoSet.action" method="get">
							<input type="hidden" id="infoSetParentid" name="infoSetParentid" value="${infoSetParentid}" />
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
													<strong>信息集列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="structureAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="structureEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="structureDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="structureCreate();"><img src="<%=root%>/images/operationbtn/Construction_database.png"/>&nbsp;构&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="structureDrop();"><img src="<%=root%>/images/operationbtn/Undo_database.png"/>&nbsp;撤&nbsp;销&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <security:authorize ifAllGranted="001-0005000200060001">
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="structureDate();"><img src="<%=root%>/images/operationbtn/Data_synchronization.png"/>&nbsp;数据同步&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  </security:authorize>
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
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="infoSetCode" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;信息集值：&nbsp;<input name="model.infoSetValue" type="text"  value="${model.infoSetValue}" class="search" title="请输入信息集值"></div>
							       		<div style="float: left;width:250px">&nbsp;&nbsp;信息集名称：&nbsp;<input name="model.infoSetName"  type="text"  value="${model.infoSetName}" class="search" title="请输入信息集名称"></div>
							       		<div style="float: left;">&nbsp;&nbsp;主键值：&nbsp;<input name="model.infoSetPkey" type="text"  value="${model.infoSetPkey}" class="search" title="请输入主键值"></div>
							       		<div style="float: left;">&nbsp;&nbsp;是否构建：&nbsp;<s:select name="model.infoSetState" list="#{'':'全部','0':'未构建','1':'已构建'}" style="width:146px;"  onchange="search()" /></div>
							       		<div style="float: left;width:450px">&nbsp;&nbsp;信息集类型：&nbsp;<s:select name="model.infoSetIsSystem" list="#{'':'全部','0':'普通信息集','1':'系统信息集'}"  style="width:146px;" onchange="search()" />
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/></div>
							       	</td>
							     </tr>
							</table> 
								<webflex:flexCheckBoxCol caption="选择" property="infoSetCode" showValue="infoSetIsSystem" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="信息集值" property="infoSetValue" showValue="infoSetValue" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="信息集名称" property="infoSetName" showValue="infoSetName" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="主键值" property="infoSetPkey" showValue="infoSetPkey" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="是否构建" mapobj="${statemap}" property="infoSetState" showValue="infoSetState" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="信息集类型" mapobj="${sytypemap}" property="infoSetIsSystem" showValue="infoSetIsSystem" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","structureAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","structureEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","structureDel",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addLine();
	item = new MenuItem("<%=root%>/images/operationbtn/Construction_database.png","构建","structureCreate",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Undo_database.png","撤销","structureDrop",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function structureAdd(){
	var infoSetParentid = document.getElementById("infoSetParentid").value;
	location="<%=path%>/infotable/infoset/infoSet!input.action?infoSetParentid="+infoSetParentid;
}
function structureEdit(){
	var id=getValue();
	if(id==""){
		alert("请选择要编辑的记录。");
		return;
	}
	else if(id.indexOf(",")!=-1){
		alert("只可以编辑一条记录。");
		return;
	}
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==id){
			var infoSetIsSystem = chkButtons[i].showValue;
			var infoSetState = chkButtons[i].parentElement.parentElement.cells[5].value;
			if(infoSetIsSystem=="1"){
				alert("该信息集是系统信息集，不可修改。");
				return;
			}
<%--			else if(infoSetState=="1"){
				alert("该信息集已构建，请先撤销信息集构建！");
				return;
			}--%>
			break;
		}
	}
	var infoSetState = chkButtons[i].parentElement.parentElement.cells[5].value;
	if(infoSetState=="1"){
				alert("该信息集已构建，请先撤销信息集构建再进行编辑。");
				return;
			}
	var infoSetParentid = document.getElementById("infoSetParentid").value;
	location="<%=path%>/infotable/infoset/infoSet!input.action?infoSetCode="+id+"&infoSetParentid="+infoSetParentid;
}
function structureDel(){
	var id=getValue();
	if(id==""){
		alert("请选择要删除的记录。");
		return;
	}
	if(!confirm("确定要删除吗？")){
        return;
    }
	var infoSetParentid = document.getElementById("infoSetParentid").value;
	location="<%=path%>/infotable/infoset/infoSet!delete.action?infoSetCode="+id+"&infoSetParentid="+infoSetParentid;
}
function structureCreate(){
	var id=getValue();
	if(id==""){
		alert("请选择要构建的记录。");
		return;
	}
	var infoSetParentid = document.getElementById("infoSetParentid").value;
	location="<%=path%>/infotable/infoset/infoSet!create.action?infoSetCode="+id+"&infoSetParentid="+infoSetParentid;
}
function structureDrop(){
	var id=getValue();
	if(id==""){
		alert("请选择要撤销的记录。");
		return;
	}
	if(confirm("确定要撤销吗？")) 
	{ 
		var infoSetParentid = document.getElementById("infoSetParentid").value;
		location="<%=path%>/infotable/infoset/infoSet!destroy.action?infoSetCode="+id+"&infoSetParentid="+infoSetParentid;
	}
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
