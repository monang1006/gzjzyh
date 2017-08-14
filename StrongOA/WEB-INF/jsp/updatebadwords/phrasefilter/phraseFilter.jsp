<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!-- 列表页面样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<s:form action="/updatebadwords/phrasefilter/phraseFilter.action"
					id="myTableForm" theme="simple">
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
													<strong>过滤模块列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												
												  <tr>
												  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="setSwitch();" style="width:90px"><img src="<%=root%>/images/operationbtn/install.png"/>&nbsp;开关设置&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="filtrateMouldView();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
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
						 
							<webflex:flexTable name="myTable" showSearch="false" width="100%"
								height="370px" wholeCss="table1" property="filtrateModuleId"
								isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA"
								footShow="showCheck" getValueType="getValueByProperty"
								collection="${page.result}" page="${page}">
								<%--		 	<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">--%>
								<%--        		<tr>--%>
								<%--          			<td width="5%" align="center"  class="biao_bg1"><img src="<%=root%>/images/ico/sousuo.gif" width="17" height="16"></td>--%>
								<%--          			<td width="25%" align="center"  class="biao_bg1"><input name="textfield1" type="text" style="width:100%"></td>--%>
								<%--          			<td width="20%" align="center"  class="biao_bg1"><input name="textfield2" type="text" style="width:100%"></td>--%>
								<%--          			<td width="50%" align="center" class="biao_bg1"><input name="textfield3" type="text" style="width:100%"></td>--%>
								<%--          		</tr>--%>
								<%--      		</table> --%>
								<webflex:flexCheckBoxCol caption="选择"
									property="filtrateModuleId" showValue="moduleId" width="5%"
									isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="模块名" property="filtrateModuleId"
									showValue="moduleId" width="25%" isCanDrag="true"
									isCanSort="true" onclick="view(this.value)"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="是否开启" property="filtrateOpenstate"
									mapobj="${map1}" showValue="filtrateOpenstate" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="过滤提示" property="filtrateMsg"
									showValue="filtrateMsg" width="50%" isCanDrag="true"
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
	item = new MenuItem("<%=root%>/images/operationbtn/install.png","开关设置","setSwitch",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查 看","filtrateMouldView",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);	
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

<%--function filtrateMouldAdd(){--%>
<%--	var id="";--%>
<%--	location="<%=path%>/updatebadwords/phraseFilter/phraseFilter!input.action";--%>
<%--}--%>
function filtrateMouldView(){
	var id=getValue();
	if(id==""){
		alert("请选择要查看的记录。");
	}else if(id.indexOf(",")!=-1){
		alert("只可以查看一条记录。");
	}else{
	OpenWindow("<%=path%>/updatebadwords/phrasefilter/phraseFilter!view.action?id="+id,'400pt','210pt',window);
	}
}

function view(value){
	OpenWindow("<%=path%>/updatebadwords/phrasefilter/phraseFilter!view.action?id="+value,'400pt','210pt',window);
}

function setSwitch(){
	var id=getValue();
	if(id==""){
		alert("请选择要开关设置的记录。");
	}else if(id.indexOf(",")!=-1){
		alert("只可以选择开关设置一条记录。");
	}else{
		location="<%=path%>/updatebadwords/phrasefilter/phraseFilter!save.action?id="+id;
	}
}
</script>
	</BODY>