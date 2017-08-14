<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="java.util.*"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<HTML>
	<HEAD>
		<title>会议计划列表</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
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
												会议室信息列表
											</td>
											<td width="70%">
											
											<table border="0" align="right" cellpadding="00" cellspacing="0">
											<td >
												<a class="Operation" href="#" onclick="addMeetingRoom()">
													<img src="<%=root%>/images/ico/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="editMeetingRoom()">
													<img src="<%=root%>/images/ico/bianji.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="deleteMeetingRoom()">
													<img src="<%=root%>/images/ico/shanchu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">删除&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="viewMeetingRoom()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看&nbsp;</span> </a>
											</td>
											</table>
											<td width="5"></td>
											<%--<td width="80">
												<a class="Operation" href="#" onclick="addApplication()">
													<img src="<%=root%>/images/ico/shengqing.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">申请使用</span> </a>
											</td>
											<td width="5"></td>
											<td width="80">
												<a class="Operation" href="#" onclick="startUsing()"> <img
														src="<%=root%>/images/ico/kaishi.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">开始使用</span> </a>
											</td>
											<td width="5"></td>
											<td width="80">
												<a class="Operation" href="#" onclick="endUsing()"> <img
														src="<%=frameroot%>/images/jieshu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">结束使用</span> </a>
											</td>
											<td width="5"></td>
											<td width="110">
												<a class="Operation" href="#" onclick="viewHistory()"> <img
														src="<%=root%>/images/ico/chakanlishi.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看历史记录</span> </a>
											</td>
											<td width="5"></td>
										--%></tr>
									</table>
								</td>
							</tr>
						</table>
				</tr>
				 <tr>
				 	<td>
						<s:form id="myTableForm" action="meetingroom.action" method="get">
						<input type="hidden" name="model.mrName" id="mrName" value="${model.mrName}">
						<input type="hidden" name="model.mrLocation" id="mrLocation" value="${model.mrLocation}">
						<input type="hidden" name="model.mrPeople" id="mrPeople" value="${model.mrPeople}">
						<input type="hidden" name="model.mrType" id="mrType" value="${model.mrType}">
							<webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${page.result}" page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
						         	 <tr>
								       <td width="4%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="17" height="16"/></td>
								       <td width="16%"  class="biao_bg1"><input name="searchmrName" id="searchmrName" type="text" style="width=100%" class="search" title="输入名称" value="${model.mrName}"></td>
								       <td width="30%"  class="biao_bg1"><input name="searchmrLocation" id="searchmrLocation" type="text" style="width=100%" class="search" title="输入地点" value="${model.mrLocation}"></td>
								       <td width="15%"  class="biao_bg1"><input name="searchmrPeople" id="searchmrPeople" type="text" style="width=100%" class="search" title="输入人数" value="${model.mrPeople}"></td>
								       <td width="20%"  class="biao_bg1"><input name="searchmrType" id="searchmrType" type="text" style="width=100%" class="search" title="类型" value="${model.mrType}"></td>
								       <td width="15%" align="center" class="biao_bg1"><s:select name="model.mrState" list="#{'':'---- 所有状态 ----','0':'正常使用','1':'停止使用'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
								       <td class="biao_bg1">&nbsp;</td>
								     </tr>
						        </table>
						        <webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="会议室名称" valuepos="0" valueshowpos="1" width="16%" isCanDrag="true" isCanSort="true" onclick="view(this.value)"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议室地点" valuepos="3" valueshowpos="3" width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="可容纳人数" valuepos="2" valueshowpos="2" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议室类型" valuepos="4" valueshowpos="4" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="会议室状态" mapobj="${statemap}" valuepos="6" valueshowpos="6" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol> 
							  </webflex:flexTable>
						</s:form>
				 	</td>
				</tr>
			</table>
				 
		</DIV>
		<script language="javascript">
		
		$(document).ready(function(){
				$("#img_sousuo").click(function(){
					$("#mrName").val(encodeURI($("#searchmrName").val()));
					$("#mrLocation").val(encodeURI($("#searchmrLocation").val()));
					$("#mrPeople").val(encodeURI($("#searchmrPeople").val()));
					$("#mrType").val(encodeURI($("#searchmrType").val()));
					
					$("form").attr("action",window.location);
					$("form").submit();
				});
				
				$("#mrName").val(encodeURI($("#searchmrName").val()));
				$("#mrLocation").val(encodeURI($("#searchmrLocation").val()));
				$("#mrPeople").val(encodeURI($("#searchmrPeople").val()));
				$("#mrType").val(encodeURI($("#searchmrType").val()));
			}); 
		
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewMeetingRoom",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
/*	item = new MenuItem("<%=root%>/images/ico/shengqing.gif","申请使用","addApplication",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/kaishi.gif","开始使用","startUsing",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/jieshu.gif","结束使用","endUsing",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakanlishi.gif","查看历史记录","viewHistory",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
*/	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addMeetingRoom(){
	var a = OpenWindow("<%=path%>/meetingroom/meetingroom!input.action",'480pt','320pt','addRoomWindow');
	if(a=="reload"){
		document.location.reload();
	}
}
function editMeetingRoom(){
	var id = getValue();
	if(id==null|id==""){
		alert("请选择要编辑的会议室！");
	}else if(id.indexOf(",")>0){
		alert("一次只能编辑一个会议室！");
	}else{
		var a = OpenWindow("<%=path%>/meetingroom/meetingroom!input.action?mrId="+id,'500pt','500pt','addRoomWindow');
		if(a=="reload"){
			document.location.reload();
		}
	}
}
function deleteMeetingRoom(){
	var id = getValue();
	if(id==null|id==""){
		alert("请选择要删除的会议室！");
		return;
	}else if(id.indexOf(",")>0){
		alert("一次只能删除一个会议室！");
		return;
	}else{
		if(!confirm("确定要删除选中的会议室？")){
			return ;
		}
	}
	var actionUrl = "<%=path%>/meetingroom/meetingroom!delete.action?mrId="+id;
	$.ajax({
	  		type:"post",
	  		dataType:"text",
	  		url:actionUrl,
	  		data:"",
	  		success:function(msg){
	  		//alert(msg)
		  			if("reload"==msg){
						document.location.reload();
		  			}else{
		  				alert("删除会议室出错，请重新尝试！");
		  			}
		  			
	  		}
	  	});
}
function viewMeetingRoom(){
	var id = getValue();
	if(id==null|id==""){
		alert("请选择要查看的会议室！");
	}else if(id.indexOf(",")>0){
		alert("一次只能查看一个会议室！");
	}else{
		OpenWindow("<%=path%>/meetingroom/meetingroom!view.action?mrId="+id,'500pt','500pt','editRoomWindow');
	}
}

function view(id){
	OpenWindow("<%=path%>/meetingroom/meetingroom!view.action?mrId="+id,'450pt','400pt','editRoomWindow');
}
function addApplication(){
	location = "<%=path%>/meetingroom/meetingroom!selectRoom.action";
}
function startUsing(){
	OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=meetingroom/meetingroom-selectframe.jsp",'800pt','500pt','usingWindow')
}
function endUsing(){
	location="<%=path%>/logistics_management/meetingroom/meetingRoom/frame.jsp?type=start";
}
function viewHistory(){
	location="<%=path%>/logistics_management/meetingroom/meetingRoom/viewHistory.jsp";
}
</script>
	</BODY>
</HTML>
