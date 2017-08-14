<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="java.util.*"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<title>车辆信息列表</title>
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
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form  theme="simple" id="carForm" action="/car/car.action">
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
												车辆信息列表
											</td>
											<td width="65%"><table align="right"><tr>
											<td >
												<a class="Operation" href="#" onclick="addCar()">
													<img src="<%=root%>/images/ico/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="editCar()">
													<img src="<%=root%>/images/ico/bianji.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑&nbsp;</span> </a>
											</td>
						                 	<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="deleteCar()">
													<img src="<%=root%>/images/ico/shanchu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">删除&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="viewCar()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看&nbsp;</span> </a>
											</td>
											
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="reportCar()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看报表&nbsp;</span> </a>
											</td>
											
											<td width="5"></td>
											</tr></table></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="car" width="100%" height="364px"
							wholeCss="table1" property="carId" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img
											src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" id="img_sousuo" style="cursor: hand;" title="单击搜索"
											width="17" height="16">
									</td>
									<td width="15%" class="biao_bg1">
									    <s:textfield name="carno"  cssClass="search" title="请输入车牌号"></s:textfield>
							
									</td>
									<td width="12%" class="biao_bg1">
										<s:select list="carTypeList" listKey="dictItemValue"  listValue="dictItemName"  onchange='$("#img_sousuo").click();' headerKey="" headerValue="请选择车型"
						                	id="cartpye" name="cartype" style="width:100%;"/>
									</td>
									<td width="14%" class="biao_bg1">
									    <s:textfield name="carbrand"  cssClass="search" title="请输入品牌"></s:textfield>
									</td>
									<td width="10%" class="biao_bg1">
									    <s:textfield name="takenumber" cssClass="search" title="可乘人数"></s:textfield>
									</td>
									<td width="12%" class="biao_bg1">
									    <s:textfield name="driver"  cssClass="search" title="请输入司机"></s:textfield>
									</td>
									<td width="10%" class="biao_bg1">
											<strong:newdate id="buydate1" name="buydate1"  dateobj="${buydate1}" dateform="yyyy-MM-dd"  width="100%"  isicon="true"  classtyle="search" title="起始日期"/>
									</td>
									
									<td width="10%" class="biao_bg1">
											<strong:newdate id="buydate2" name="buydate2"  dateobj="${buydate2}"  dateform="yyyy-MM-dd"  width="100%"  isicon="true" classtyle="search" title="结束日期"/>
									</td>
									<td width="12%" class="biao_bg1">
										<s:select list="carStatusList" listKey="dictItemValue" listValue="dictItemName"  onchange='$("#img_sousuo").click();' headerKey="" headerValue="请选择状态"
							                    id="status" name="status" style="width:100%;"/>
									</td>
									<td class="biao_bg1">
										&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="carId"
								showValue="carno" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="车牌号" property="carno"
								showValue="carno" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexEnumCol caption="车型" mapobj="${carTypeMap}" property="cartype"
								showValue="cartype" isCanDrag="true" width="12%" isCanSort="true"></webflex:flexEnumCol>
							<webflex:flexTextCol caption="品牌"  property="carbrand"
								showValue="carbrand" isCanDrag="true" width="14%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="可乘人数" property="takenumber"
								showValue="takenumber" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexTextCol>
						    <webflex:flexTextCol caption="司机" property="driver"
								showValue="driver" isCanDrag="true" width="12%" isCanSort="true"></webflex:flexTextCol>
						    <webflex:flexDateCol caption="购置日期" property="buydate" showsize="16" dateFormat="yyyy-MM-dd"
								showValue="buydate" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexDateCol>
						   <webflex:flexEnumCol caption="车辆状态" mapobj="${carStstusMap}" property="status"
								showValue="status" isCanDrag="true" width="12%" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addCar",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editCar",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteCar",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewCar",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addCar(){
    var result=window.showModalDialog("<%=path%>/car/car!input.action",window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:600px');
	
    //  var returnValue = "";
	// returnValue=OpenWindow("<%=path%>/car/car!input.action",'500pt','420pt','addCarWindow');
   //	if (returnValue == "OK") {
	//   window.location="<%=path%>/car/car.action";
   //	}
}
function editCar(){
      var id=getValue();
		if(id==null || id==""){
		alert("请选择车辆！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能编辑一辆车！");
   		 		return ;
   		 	}
   		 }
	var result=window.showModalDialog("<%=path%>/car/car!edit.action?carId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:600px');

	// var returnValue = "";
	//  returnValue=OpenWindow("<%=path%>/car/car!input.action",'500pt','420pt','addCarWindow');
	// if (returnValue == "OK") {
	 //  window.location="<%=path%>/car/car.action";
	// }
}
function deleteCar(){
	// alert("只有被停用的车辆才能删除，删除车辆后，需删除对应的车辆申请单！");
	var id=getValue();
	if(id==null || id==""){
		alert("请选择车辆！");
			return;
		}
		if(confirm("您确定要删除吗？")){
    location="<%=path%>/car/car!delete.action?carId="+id;	
    }	
}
function viewCar(){
    var id=getValue();
	if(id==null || id==""){
		alert("请选择车辆！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能查看一辆车！");
   		 		return ;
   		 	}
   		 }
	OpenWindow("<%=path%>/car/car!view.action?carId="+id,'500pt','460pt','editCarWindow');
}

function reportCar(){
  OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=/car/carReport.jsp",'800pt','600pt','editCarWindow');
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("Form").submit();
        });     
      });
</script>
	</BODY>
</HTML>
