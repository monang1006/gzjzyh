<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>会议申请</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="30%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												申请单列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
											<td >
												<a class="Operation" href="#" onclick="addApplication();">
													<img
														src="<%=root%>/images/ico/tianjia.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">新申请&nbsp;</span> </a>
											</td>
											<td width="5"></td>	
											<s:if test="inputType=='yes'">
												<td >
													<a class="Operation" href="#" onclick="editApplication();">
														<img
															src="<%=root%>/images/ico/chuli.gif"
															width="15" height="15" class="img_s"><span id="test"
														style="cursor:hand">处理&nbsp;</span> </a>
												</td>
												<td width="5"></td>	
												<td >
												<a class="Operation" href="#" onclick="deleteApplication();">
													<img
														src="<%=root%>/images/ico/shanchu.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">删除&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											</s:if>
											<s:else>
												<td >
													<a class="Operation" href="#" onclick="editApplication();">
														<img
															src="<%=root%>/images/ico/chakan.gif"
															width="15" height="15" class="img_s"><span id="test"
														style="cursor:hand">查看&nbsp;</span> </a>
												</td>
												<td width="5"></td>	
											</s:else>
											<td >
												<a class="Operation" href="#" onclick="parent.viewAll();">
													<img
														src="<%=root%>/images/ico/tb-list16.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">视图&nbsp;</span> </a>
											</td>
											</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<s:form id="myTableForm" action="meetingApply.action" method="get">
									<input type="hidden" name="inputType" id="inputType" value="${inputType}">
									<input type="hidden" name="model.maMeetingdec" id="maMeetingdec" value="${model.maMeetingdec}">
									<input type="hidden" name="model.toaMeetingroom.mrName" id="mrName" value="${model.toaMeetingroom.mrName}">
									<input type="hidden" name="model.maCreaterName" id="maCreaterName" value="${model.maCreaterName}">
									<webflex:flexTable name="myTable" width="100%" height="364px" wholeCss="table1" property="maId" isCanDrag="true"
										isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
										<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
								         	 <tr>
										       <td width="5%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="15" height="15"></td>
										       <td width="35%" align="center"  class="biao_bg1"><input name="searchmaMeetingdec" id="searchmaMeetingdec" type="text" style="width=100%" class="search" title="会议议题" value="${model.maMeetingdec}"></td>
										       <td width="16%" align="center"  class="biao_bg1"><input name="searchmrName" id="searchmrName" type="text" style="width=100%" class="search" title="会议室名称" value="${model.toaMeetingroom.mrName }"></td>
										       <td width="13%" align="center"  class="biao_bg1"><input name="searchmaCreater" id="searchmaCreater" type="text" style="width=100%" class="search" title="申请人" value="${model.maCreaterName}"></td>
										       <td width="20%" align="center" class="biao_bg1"><strong:newdate  name="model.maSubmittime" id="maSubmittime" width="98%" skin="whyGreen" isicon="true"  classtyle="search" title="申请时间"/></td>
										       <%--<td width="14%" align="center" class="biao_bg1"><strong:newdate  name="model.maAppstarttime" id="maAppstarttime" width="98%" skin="whyGreen" isicon="true"  classtyle="search" title="结束时间"/></td>
										       <td width="14%" align="center" class="biao_bg1"><strong:newdate  name="model.maAppendtime" id="maAppendtime" width="98%" skin="whyGreen" isicon="true"  classtyle="search" title="结束时间"/></td>
										       --%>
										       <td width="13%" align="center" class="biao_bg1"><s:select name="model.maState" list="#{'':'全部','0':'新申请','1':'审批通过','2':'审批驳回','3':'结束使用'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
										       <td class="biao_bg1">&nbsp;</td>
										     </tr>
											<webflex:flexCheckBoxCol caption="选择" property="maId"
												showValue="maMeetingdec" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
											<webflex:flexTextCol caption="会议议题" property="maMeetingdec"
												showValue="maMeetingdec" isCanDrag="true" width="35%" isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="会议室名称" property="toaMeetingroom.mrName"
												showValue="toaMeetingroom.mrName" isCanDrag="true" width="16%" isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="申请人" property="maCreaterName"
												showValue="maCreaterName" isCanDrag="true" width="13%" isCanSort="true"></webflex:flexTextCol>
											<webflex:flexDateCol caption="申请时间" property="maSubmittime" showsize="18"
												showValue="maSubmittime" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexDateCol>
											<%--<webflex:flexDateCol caption="开始使用时间" property="maAppstarttime" showsize="16"
												showValue="maAppstarttime" isCanDrag="true" width="14%" isCanSort="true"></webflex:flexDateCol>
											<webflex:flexDateCol caption="结束使用时间" property="maAppendtime" showsize="16"
												showValue="maAppendtime" isCanDrag="true" width="14%" isCanSort="true"></webflex:flexDateCol>
											--%>
											<webflex:flexEnumCol caption="申请状态" mapobj="${statemap}"  property="maState"
												showValue="maState"  width="13%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol> 
										</table>
									</webflex:flexTable>
								</s:form>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","新申请","addApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	<s:if test="inputType=='yes'">
		item = new MenuItem("<%=root%>/images/ico/bianji.gif","处理","editApplication",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteApplication",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
	</s:if>
	<s:else>
		item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","editApplication",1,"ChangeWidthTable","checkMoreDis");
		sMenu.addItem(item);
	</s:else>
	
	item = new MenuItem("<%=root%>/images/ico/tijiao.gif","视图","view",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
	//切换到视图
	function view(){
		parent.viewAll();	
	}
	//新建申请单
	function addApplication(){
		var url = "<%=path%>/meetingroom/meetingApply!input.action";
		var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');
		if("reload"==a){
			document.location.reload();
		}
	}
function editApplication(){
	var id = getValue();
	if(id==null|id==""){
		alert("请选择要处理的会议申请！");
		return;
	}else if(id.indexOf(",")>0){
		alert("一次只能处理一个会议申请！");
		return;
	}else{
		viewApply(id);
	}
}
function deleteApplication(){
	var id = getValue();
	if(id==null|id==""){
		alert("请选择要删除的会议申请！");
		return;
	}else if(id.indexOf(",")>0){
		alert("一次只能删除一个会议申请！");
		return;
	}else{
		if(!confirm("确定要删除选中的会议室申请？")){
			return;
		}
		var actionUrl = "<%=path%>/meetingroom/meetingApply!delete.action?maId="+id;
		$.ajax({
		  		type:"post",
		  		dataType:"text",
		  		url:actionUrl,
		  		data:"",
		  		success:function(msg){
			  			if("reload"==msg){
							document.location.reload();
			  			}else{
			  				alert(msg);
			  			}
			  			
		  		}
		  	});
		//alert("只有待提交和已提交的申请单能执行删除操作！");
	}
}
function viewApplication(){
	OpenWindow("addApplication.jsp",'480pt','270pt','addAppWindow');
}
function submitApplication(){
	alert("提交申请单，更改申请单的状态为已提交");
}

	//查看处理 申请单
 	 	function viewApply(maId){
 	 		var url = "<%=path%>/meetingroom/meetingApply!view.action?maId="+maId+"&inputType=${inputType}";
  			var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');
		if(undefined==a){
			return;
		}
		if("reload"==a){
		//	parent.changeRoom("${mrId}");
			document.location.reload();
		}
		if(a.indexOf("swich,")==0){
			var aa = a.split(",");
			viewApply(aa[1]);
			
		}
 	 	}
$(document).ready(function(){
				$("#img_sousuo").click(function(){
					$("#maMeetingdec").val(encodeURI($("#searchmaMeetingdec").val()));
					$("#mrName").val(encodeURI($("#searchmrName").val()));
					$("#maCreaterName").val(encodeURI($("#searchmaCreater").val()));
					$("form").submit();
				});
				$("#maMeetingdec").val(encodeURI($("#searchmaMeetingdec").val()));
				$("#mrName").val(encodeURI($("#searchmrName").val()));
				$("#maCreaterName").val(encodeURI($("#searchmaCreater").val()));
			}); 
</script>
	</BODY>
</HTML>
