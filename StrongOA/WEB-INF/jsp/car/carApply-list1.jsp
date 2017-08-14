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
		<title>车辆审批情况列表</title>
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
		<s:form  theme="simple" id="carApplyForm" action="/car/carApply!list1.action" method="get">
		<input type="hidden" id="carId" name="carId" value="${carId}">
		<input type="hidden" id="carno" name="carno" value="${carno}">
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
											<td width="30%">
												<img src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												车辆${carno}审批情况列表
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td >
												<a class="Operation" href="#" onclick="history.go(-1);">
													<img src="<%=root%>/images/ico/tb-list16.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">视图&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="addApply()">
													<img src="<%=root%>/images/ico/shengqing.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">申请&nbsp;</span> </a>
											</td>	
											<td width="5"></td>
											
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
											width="17" height="16">
									</td>
									<td  class="biao_bg1" style="display:none"><input id="carId" name="model.toaCar.carId" value="${carId}" type="text" title="车辆ID" class="search">
									</td>
									<td  class="biao_bg1" style="display:none"><input id="carno" name="model.toaCar.carno" value="${carno}" type="text" title="车牌号" class="search">
									</td>
									<td width="15%" class="biao_bg1"><input id="applier" name="model.applier" type="text" title="请输入申请人" class="search">
									</td>
									<td width="25%" class="biao_bg1"><input id="destination" name="model.destination" type="text" title="请输入目的地" class="search">
									</td>
									<td width="21%" class="biao_bg1"><strong:newdate id="stime" name="model.starttime" isicon="true" dateform="yyyy-MM-dd HH:mm" 
										    width="100%" />
									</td>
									<td width="21%" class="biao_bg1"><strong:newdate id="etime" name="model.endtime" isicon="true" dateform="yyyy-MM-dd HH:mm" 
										    width="100%" />
									</td>
									<td width="13%" align="center" class="biao_bg1"><s:select name="model.applystatus" list="#{'':'选择状态','0':'待提交','1':'审批中','3':'已批准','4':'已撤销'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
									<td class="biao_bg1">&nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="applicantId"
								showValue="applier" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="申请人" property="applier" showsize="6"
								showValue="applier" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="目的地" property="destination" showsize="10"
								showValue="destination" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="起始时间" property="starttime" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
								showValue="starttime" isCanDrag="true"  width="21%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束时间" property="endtime" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
								showValue="endtime" isCanDrag="true" width="21%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexEnumCol caption="申请状态" property="applystatus" mapobj="${applyStstusMap}"
								showValue="applystatus" isCanDrag="true" width="13%" isCanSort="true"></webflex:flexEnumCol>
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
			item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewApply",1,"ChangeWidthTable","checkMoreDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
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
		
		function addApply(){
			//OpenWindow("<%=path%>/car/car!applyinput.action",'500pt','420pt','editApplyWindow');
			var carId = $("#carId").val();
			var carno = $("#carno").val();
			if(""==carId|null==carId){
				alert("没有车辆信息，无法申请！\n请联系车辆管理员录入车辆信息！");
				return;
			}
			window.parent.parent.parent.location="<%=path%>/car/carApply!input.action?carId="+carId+"&carno="+carno;
		}

	 $(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("#applier").val(encodeURI($.trim($("#applier").val())));
        	$("#destination").val(encodeURI($.trim($("#destination").val())));
        	$("#carno").val(encodeURI($.trim($("#carno").val())));
        	$("Form").submit();
        });     
      });
</script>
	</BODY>
</HTML>

