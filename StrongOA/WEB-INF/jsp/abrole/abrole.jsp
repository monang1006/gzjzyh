<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
	<HEAD>
		<TITLE>委托规则</TITLE>
		<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>

	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/abrole/abrole.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													委托规则列表
												</td>
												<td width="70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td width="5"></td>
															<td>
																<a class="Operation" href="javascript:addTempFile();"><img
																		src="<%=root%>/images/ico/tianjia.gif" width="14"
																		height="14" class="img_s">添加&nbsp;</a>
															</td>
															<td width="5"></td>
															<td>
																<a class="Operation" href="javascript:editTempFile();"><img
																		src="<%=root%>/images/ico/bianji.gif" width="15"
																		height="15" class="img_s">修改&nbsp;</a>
															</td>
															<td width="5"></td>
															<td >
																<a class="Operation" href="javascript:cancelTempFile();"><img
																		src="<%=root%>/images/ico/shanchu.gif" width="15"
																		height="15" class="img_s">取消&nbsp;
															</td>
															<%--                  <td width="4%"><img src="<%=root%>/images/ico/bianji.gif" width="14" height="15"></td>--%>
															<%--                  <td width="18%"><a href="javascript:supper();">系统管理员设置</a></td>                --%>

														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="id" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo"
												width="17" height="16" style="cursor: hand;" title="单击搜索">
										</td>
										&nbsp;
										<%--<td width="20%" align="center" class="biao_bg1">
											<s:textfield name="selectsystemid" cssClass="search"
												title="请输入系统编码"></s:textfield>
										</td>--%>
										<c:if test="${isSuperManager == '1' }">
											<td width="20%" align="center" class="biao_bg1">
												<s:textfield name="srcName" cssClass="search"
													title="请输入委托人姓名"></s:textfield>
											</td>
										</c:if>										
										<td width="20%" align="center" class="biao_bg1">
											<s:textfield name="targetName" cssClass="search"
												title="请输入被委托人姓名"></s:textfield>
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<strong:newdate name="startDate" id="startDate"
												skin="whyGreen" isicon="true" dateobj="${startDate}"
												dateform="yyyy-MM-dd HH:mm:ss" width="100%"></strong:newdate>
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<strong:newdate name="endDate" id="endDate"
												dateobj="${endDate}" skin="whyGreen" isicon="true"
												dateform="yyyy-MM-dd HH:mm:ss" width="100%"></strong:newdate>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:select name="status"
												list="#{'':'是否有效','2':'挂起','1':'有效','0':'无效'}" listKey="key"
												listValue="value" style="width:10em" />
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="abroleId"
									showValue="abroleId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<c:if test="${isSuperManager == '1' }">
									<webflex:flexTextCol caption="委托人" property="abroleSrcUsername"
										showValue="abroleSrcUsername" width="20%" isCanDrag="true"
										isCanSort="true"></webflex:flexTextCol>
								</c:if>									
								<webflex:flexTextCol caption="被委托人" property="abroleTargetUsername"
									showValue="abroleTargetUsername" width="20%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="abroleStartTime"
									showValue="abroleStartTime" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="abroleEndTime"
									showValue="abroleEndTime" dateFormat="yyyy-MM-dd HH:mm:ss" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexEnumCol caption="是否有效" mapobj="${activeMap}"
									property="abroleEnabled" showValue="abroleEnabled" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","增加","addTempFile",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","取消","cancelTempFile",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
<%--	item = new MenuItem("<%=root%>/images/ico/bianji.gif","超级用户设置","supper",1,"ChangeWidthTable","checkOneDis");--%>
<%--	sMenu.addItem(item);	--%>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addTempFile(){
	window.showModalDialog("<%=path%>/abrole/abrole!input.action",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:350px');
}

function editTempFile(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要修改的记录！");
		return;
	}
	if(id.length>32){
		alert("只能修改一条记录!");
		return;
	}	
	window.showModalDialog("<%=path%>/abrole/abrole!input.action?abroleId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:350px');
}

//取消该委托办理规则
function cancelTempFile(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要取消的规则！");
		return;
	}
	if(id.length>32){
		alert("只能选择一条规则!");
		return;
	}
	location = "<%=path%>/abrole/abrole!cancel.action?abroleId=" + id;	
}

function deleteTempFile(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
		return;
	}
	if(confirm("危险操作,是否确定要删除!")){
	
		location="<%=path%>/basesystem/baseSystem!delete.action?abroleId="+id+",";
	}else{
		return;
	}
	

	
}
function supper(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择系统记录！");
		return;
	}
	if(id.length>32){
		alert("请选择一条系统记录！");
		return;
	}	
	var result = window.showModalDialog("<%=path%>/basesystem/baseSystem!tree.action?sysId="+id,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:500px');
	if(result=="RELOAD"){
    location="<%=path%>/basesystem/baseSystem.action";
     
	}
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
</script>
	</BODY>
</HTML>
