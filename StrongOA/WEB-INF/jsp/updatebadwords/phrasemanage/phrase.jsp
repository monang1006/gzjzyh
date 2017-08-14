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
		<!--列表样式 -->
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows_list.css">
		<link type="text/css" rel="stylesheet"
			href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<!--<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>-->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()" >
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" theme="simple"
				action="/updatebadwords/phrasemanage/phrase.action">
				<s:actionmessage />
				<input type="hidden" id="disLogo" name="disLogo" value="${disLogo}">
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
													<strong>词语管理列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="filtrateAdd();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="filtrateEdit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="filtrateDel();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
								wholeCss="table1" property="filtrateId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}" ondblclick="view(this.value);">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;不良词汇：&nbsp;<input id="filtrateWord"  name="filtrateWord" type="text"
												 class="search" title="请输入不良词汇"
												value="${filtrateWord }"></div>
							       		<div style="float: left;">&nbsp;&nbsp;代替词：&nbsp;<input id="filtrateRaplace"   name="filtrateRaplace"
												type="text"  class="search"
												title="请输入替代词语" value="${filtrateRaplace }"></div>
							       		<div style="float: left;width:450px;">&nbsp;&nbsp;添加日期：&nbsp;<strong:newdate id="filtrateTime" name="filtrateTime"
												dateform="yyyy-MM-dd"  isicon="true"
												classtyle="search" title="请输入添加日期" dateobj="${filtrateTime}" />
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/></div>
							       	</td>
							     </tr>
							</table> 
									
								
								<webflex:flexCheckBoxCol caption="选择" property="filtrateId"
									showValue="filtrateWord" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="不良词汇" property="filtrateWord"
									showValue="filtrateWord" width="35%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="代替词" property="filtrateRaplace"
									showValue="filtrateRaplace" width="35%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="添加日期" property="filtrateTime"
									showValue="filtrateTime" width="25%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","filtrateAdd",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);

	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","filtrateEdit",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);

	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","filtrateDel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function filtrateAdd(){
	var id="";
	window.showModalDialog("<%=path%>/updatebadwords/phrasemanage/phrase!input.action",window,'help:no;status:no;scroll:no;dialogWidth:470px; dialogHeight:300px');
}
function filtrateEdit(){
	var id=getValue();
	if(id==""){
		alert("请选择要编辑的记录！");
	}else if(id.indexOf(",")!=-1){
		alert("只可以编辑一条记录！");
	}
	else
		window.showModalDialog("<%=path%>/updatebadwords/phrasemanage/phrase!input.action?id="+id,window,'help:no;status:no;scroll:no;dialogWidth:470px; dialogHeight:300px');
}
function filtrateDel(){
	var id=getValue();
	if(id=="")
		alert("请选择要删除的记录！");
	else{
		if(confirm("确定要删除吗？"))
			location="<%=path%>/updatebadwords/phrasemanage/phrase!delete.action?id="+id;
	}
}

function view(value){
	window.showModalDialog("<%=path%>/updatebadwords/phrasemanage/phrase!view.action?id="
								+ value, window,
						'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:300px');
	}

	function submitForm() {
		document.getElementById("myTableForm").submit();
	}
	function search() {
		document.getElementById("disLogo").value = "search";
		submitForm();
	}
</script>
	</BODY>
</HTML>
