<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ page import="java.util.*"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>车辆申请列表</title>
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
	</HEAD>
	
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form  theme="simple" id="carApplyForm" action="/car/carApply.action" method="get">
		<s:hidden id="bohuicount" name="bohuicount"></s:hidden>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="35%">
												<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												车辆申请列表
											</td>
											<td width="65%"><table align="right"><tr>
											<td >
												<a class="Operation" href="#" onclick="addApply()">
													<img src="<%=root%>/images/ico/shengqing.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">申请&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="editApply()">
													<img src="<%=root%>/images/ico/bianji.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="submitApply()">
													<img src="<%=root%>/images/ico/tijiao.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">提交&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="viewApply()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="deleteApply()">
													<img src="<%=root%>/images/ico/shanchu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">删除&nbsp;</span> </a>
											</td>
											<td width="5" ></td>
											</tr></table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="carApply" width="100%" height="364px"
							wholeCss="table1" property="applicantId" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" id="img_sousuo" style="cursor: hand;" title="单击搜索"
											width="15" height="15">
									</td>
									<td width="12%" class="biao_bg1"><s:textfield id="caruser" name="model.caruser"  cssClass="search" title="请输入用车人"></s:textfield>
									</td>
									<td width="28%" class="biao_bg1"><s:textfield id="destination"  name="model.destination" cssClass="search" title="请输入目的地"></s:textfield>
									</td>
									<td width="13%" class="biao_bg1"><s:textfield id="carno" name="model.toaCar.carno"  cssClass="search" title="请输入车牌号"></s:textfield>
									</td>									
									<td width="16%" class="biao_bg1"><strong:newdate id="stime" name="model.starttime"  isicon="true" dateform="yyyy-MM-dd HH:mm" width="100%" classtyle="search"  title="开始日期" />
									</td>
									<td width="16%" class="biao_bg1"><strong:newdate id="etime" name="model.endtime"  isicon="true" dateform="yyyy-MM-dd HH:mm" width="100%" classtyle="search"  title="结束日期" />
									</td>
									<td width="11%" align="center" class="biao_bg1"><s:select name="model.applystatus" list="#{'':'选择状态','0':'待提交','1':'审批中','3':'已批准','4':'已撤销'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/>
									</td>
									<td class="biao_bg1"> &nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="applicantId" showValue="toaCar.carno" 
								width="5%" isCheckAll="true" isCanDrag="false"isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="用车人" property="caruser" showsize="5"
								showValue="caruser" isCanDrag="true" width="12%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="目的地" property="destination" showsize="15"
								showValue="destination" isCanDrag="true" width="28%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="车牌号" property="toaCar.carno"
								showValue="toaCar.carno" isCanDrag="true" width="13%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="起始时间" property="starttime" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
								showValue="starttime" isCanDrag="true"  width="16%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束时间" property="endtime" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
								showValue="endtime" isCanDrag="true" width="16%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexEnumCol caption="申请状态" property="applystatus" mapobj="${applyStstusMap}"
								showValue="applystatus" isCanDrag="true" width="11%" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","申请","addApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/tijiao.gif","提交","submitApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteApply",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addApply(){
   location="<%=root%>/fileNameRedirectAction.action?toPage=car/car-selectframe.jsp";
	//OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=car/car-selectframe.jsp",'800pt','500pt','carselectframeWindow');
}

function editApply(){
  
     var id=getValue();
		if(id==null || id==""){
		alert("请选择车辆申请！");
			return;
		}else{
   		 	var applicantIds = id.split(",");
   		 	if(applicantIds.length>1){
   		 		alert("一次只能编辑一份车辆申请！");
   		 		return ;
   		 	}
   		 }
	 var apply_status_Id = $(":checked").parent().next().next().next().next().next().next().attr("value");
	 if(apply_status_Id!="0"){
		alert("已提交申请，不能编辑！");
			return;
		}
 	var result=window.showModalDialog("<%=path%>/car/carApply!edit.action?applicantId="+id,window,'help:no;status:no;scroll:no;dialogWidth:720px; dialogHeight:400px');

//	var returnValue = "";
//	returnValue=OpenWindow("<%=path%>/car/carApply!edit.action?applicantId="+id,'500pt','250pt','editApplyWindow');
//	if (returnValue == "OK") {
//	   window.location="<%=path%>/car/carApply.action";
//	}

   }

function deleteApply(){
	// alert("删除申请！");
	var id=getValue();
	if(id==null || id==""){
		alert("请选择车辆申请！");
		return;
	}else{
	 	var ids = id.split(",");
	 	if(ids.length>1){
	 		alert("一次只能删除一份车辆申请！");
	 		return ;
	 	}
	 }
		
	var apply_status_Id = $(":checked").parent().next().next().next().next().next().next().attr("value");
	if(apply_status_Id!="0"){
		alert("已提交申请，不能删除！");
		return;
	}
	if(confirm("您确定要删除吗？")){
    	location="<%=path%>/car/carApply!delete.action?applicantId="+id;	
    }	
}

function viewApply(){
     var id=getValue();
		if(id==null || id==""){
		alert("请选择车辆申请！");
			return;
		}else{
   		 	var applicantIds = id.split(",");
   		 	if(applicantIds.length>1){
   		 		alert("一次只能查看一份车辆申请！");
   		 		return ;
   		 	}
   		 }
	OpenWindow("<%=path%>/car/carApply!view.action?applicantId="+id,'520pt','400pt','viewApplyWindow');
}

function submitApply(){
     var applicantId = getValue();
          if(applicantId == "" || applicantId == null){
   		 	alert("请选择车辆申请！");
   		 	return ;
   		 }else{
   		 	var applicantIds = applicantId.split(",");
   		 	if(applicantIds.length>1){
   		 		alert("一次只能提交一份车辆申请！");
   		 		return ;
   		 	}
   		 }
   	var apply_status_Id = $(":checked").parent().next().next().next().next().next().next().attr("value");
	 if(apply_status_Id!="0"){
		alert("该申请已提交！");
			return;
		}
         var bussinessId = "T_OA_CARAPPLICANT;APPLICANTID;"+applicantId;
         var contextPath = "<%=root%>/car/carApply";
         //改为直接提交到流程
         var ret = OpenWindow("<%=root%>/car/carApply!wizard.action?bussinessId="+bussinessId+
         					  "&timestampt="+new Date().getTime()+"&fromPath="+contextPath, 
                               550, 500, window);
       if("OK" == ret){
        	window.location = "<%=root%>/car/carApply.action";
       }               
}

	$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("#caruser").val(encodeURI($.trim($("#caruser").val())));
        	$("#destination").val(encodeURI($.trim($("#destination").val())));
        	$("#carno").val(encodeURI($.trim($("#carno").val())));
        	$("Form").submit();
        });     
     });
</script>
	</BODY>
</HTML>
