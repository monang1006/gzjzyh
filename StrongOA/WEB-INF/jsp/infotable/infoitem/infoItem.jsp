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
		<!-- 列表样式 -->
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/menu/menu.js" language="javascript"></script>
		<script src="<%=path%>/common/js/grid/ChangeWidthTable.js" language="javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple" action="/infotable/infoitem/infoItem.action">
							<input id="infoSetCode" name="infoSetCode" type="hidden" size="32" value="${infoSetCode}">
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
													<strong>信息项列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyCreate();"><img src="<%=root%>/images/operationbtn/Construction_database.png"/>&nbsp;构&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="propertyDrop();"><img src="<%=root%>/images/operationbtn/Undo_database.png"/>&nbsp;撤&nbsp;销&nbsp;</td>
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
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="infoItemCode" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left; width:220px;">&nbsp;&nbsp;别名：&nbsp;<input name="model.infoItemSeconddisplay" type="text"   value="${model.infoItemSeconddisplay}" class="search" title="请输入别名"></div>
							       		<div style="float: left; padding-top:5px;width:200px;">&nbsp;&nbsp;数据类型：&nbsp;<s:select name="model.infoItemDatatype"   list="#{'':'全部','0':'字符串','1':'代码','2':'数值','3':'年','4':'年月','5':'年月日','6':'年月日时间','10':'文件','11':'图片','12':'电话号码','13':'文本','14':'备注','15':'主外键','20':'大字段'}"  onchange="search()"  /></div>
							       		<div style="float: left; width:220px;">&nbsp;&nbsp;长度：&nbsp;<input name="model.infoItemLength"   type="text"  value="${model.infoItemLength}" class="search" title="长度"></div>
							       		<div style="float: left; padding-top:5px; width:180px;">&nbsp;&nbsp;读写：&nbsp;<s:select name="model.infoItemProset"  list="#{'':'全部','0':'可读写','1':'不可读','2':'只读'}"   onchange="search()" /></div>
							       		<div style="float: left;  padding-top:5px; width:180px;">&nbsp;&nbsp;构建状态：&nbsp;<s:select name="model.infoItemState"   list="#{'':'全部','0':'未构建','1':'已构建'}"  onchange="search()" /></div>
							       		<div style="float: left;  padding-top:5px;">&nbsp;&nbsp;必填：&nbsp;<s:select name="model.infoItemFlag" list="#{'':'全部','0':'否','1':'是'}"    onchange="search()" />
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/></div>
							       	</td>
							     </tr>
							</table> 
								<webflex:flexCheckBoxCol caption="选择" property="infoItemCode" showValue="infoItemIsSystem" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="信息项值" property="infoItemField" showValue="infoItemField" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="别名" property="infoItemSeconddisplay" showValue="infoItemSeconddisplay" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="数据类型" mapobj="${datetypemap}" property="infoItemDatatype" showValue="infoItemDatatype" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="长度" property="infoItemLength" showValue="infoItemLength" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="构建状态" mapobj="${statemap}" property="infoItemState" showValue="infoItemState" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="读写" mapobj="${prosetmap}" property="infoItemProset" showValue="infoItemProset" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="必填" mapobj="${flagmap}" property="infoItemFlag" showValue="infoItemFlag" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","propertyAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","propertyEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","propertyDel",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addLine();
	item = new MenuItem("<%=root%>/images/operationbtn/Construction_database.png","构建","propertyCreate",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Undo_database.png","撤销","propertyDrop",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function propertyAdd(){
	var infoSetCode = document.getElementById("infoSetCode").value;
	if(infoSetCode==""){
		alert("请选择信息集！");
		return;
	}
	location="<%=path%>/infotable/infoitem/infoItem!input.action?infoSetCode="+infoSetCode;
}
function propertyEdit(){
	var infoItemCode = getValue();
	var infoSetCode = document.getElementById("infoSetCode").value;
	if(infoItemCode==""){
		alert("请选择要编辑的记录。");
		return;
	}
	else if(infoItemCode.indexOf(",")!=-1){
		alert("只可以编辑一条记录。");
		return;
	}
	var chkButtons = document.getElementsByName("chkButton");
	for(var i=0;i<chkButtons.length;i++){
		if(chkButtons[i].value==infoItemCode){
			var infoItemIsSystem = chkButtons[i].showValue;
			//var infoItemState = chkButtons[i].parentElement.parentElement.cells[6].value;
			var infoItemDatatype = chkButtons[i].parentElement.parentElement.cells[4].value;
			if(infoItemIsSystem=="1"){
				alert("该信息项是系统信息项，不可修改。");
				return;
			}
<%--			else if(infoItemState=="1"){
				alert("该信息项已构建，请先撤销信息项构建！");
				return;
			}--%>
			else if(infoItemDatatype=="15"){
				alert("该信息项是主外键，不可修改。");
				return;
			}	
			break;
		}
	}
	
	location="<%=path%>/infotable/infoitem/infoItem!input.action?infoItemCode="+infoItemCode+"&infoSetCode="+infoSetCode+"&type=1";
}
function propertyDel(){
	var infoItemCode = getValue();
	if(infoItemCode==""){
		alert("请选择要删除的记录。");
		return;
	}
	
	var infoSetCode = document.getElementById("infoSetCode").value;
	  $.post('<%=path%>/infotable/infoitem/infoItem!IsHasDelete.action',
             { 'infoItemCode':infoItemCode},
              function(data){             
              if(data=='0'){  
              	if(confirm("确定要删除吗？")){           
					location="<%=path%>/infotable/infoitem/infoItem!delete.action?infoItemCode="+infoItemCode+"&infoSetCode="+infoSetCode;
			    }
              }else{
              	  alert("当前信息项【"+data+"】已使用，不能删除。 ");
              	  return;
              }
       });	
}
function propertyCreate(){
	var infoItemCode = getValue();
	if(infoItemCode==""){
		alert("请选择要构建的记录。");
		return;
	}
	var infoSetCode = document.getElementById("infoSetCode").value;
	location="<%=path%>/infotable/infoitem/infoItem!create.action?infoItemCode="+infoItemCode+"&infoSetCode="+infoSetCode;
}
function propertyDrop(){
	var infoItemCode = getValue();
	if(infoItemCode==""){
		alert("请选择要撤销的记录。");
		return;
	}
	var infoSetCode = document.getElementById("infoSetCode").value;
	 $.post('<%=path%>/infotable/infoitem/infoItem!IsHasDelete.action',
             { 'infoItemCode':infoItemCode},
              function(data){             
              if(data=='0'){  
              	if(confirm("确定要撤销吗？")){           
					location="<%=path%>/infotable/infoitem/infoItem!destroy.action?infoItemCode="+infoItemCode+"&infoSetCode="+infoSetCode;
					
			    }
              }else{
              	  alert("当前信息项【"+data+"】已使用，不能撤消构建。 ");
              	  return;
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
