<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看车辆申请</title>
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
		<base target="_self">
	</head>
	<body oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						查看车辆申请
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
            <table width="100%">
				<tr>
					<td height="10">	
					  
					</td>
				</tr>
			</table>
			<table align="center" width="90%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
				<tr>
					<td rowspan="1" colspan="4"  height="60" class="td1" align="center">
						<span class="wz">车辆申请单</span>
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">用车人：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${model.caruser}
					</td>
					
					  <td colspan="1" width="20%" class="biao_bg1" align="right">
					    <span class="wz">申请状态：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
					    &nbsp;${applystatus2}
					</td>
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">申请人：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${model.applier}
					</td>
					<td colspan="1"  width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">车牌号：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						&nbsp;&nbsp;${model.toaCar.carno}
					</td>
				    
				</tr>
				
				<tr>
				    <td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">申请时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					&nbsp;<s:date name="model.applytime" format="yyyy-MM-dd HH:mm"/>
					</td>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">乘客人数：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						&nbsp;&nbsp;${model.passengernumber}
					</td>
					 
				</tr>
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">出车时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					&nbsp;<s:date name="model.starttime" format="yyyy-MM-dd HH:mm"/>
						
					</td>
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">回车时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
					&nbsp;<s:date name="model.endtime" format="yyyy-MM-dd HH:mm"/>
					</td>
			
				</tr>
				<tr>
					<td colspan="1"   height="21" class="biao_bg1" align="right">
						<span class="wz">目的地：&nbsp;</span>
					</td>
					<td colspan="2"  class="td1" align="left">
						&nbsp;&nbsp;${model.destination}
					</td>
				     <td colspan="1"  height="21" class="td1" align="center">
						<input id="needdriver" name="model.needdriver"  value="0" type="checkbox" disabled>自驾
						
						<script>
							var needdriver = "${model.needdriver}";
							if(needdriver=="1"){
								$("#needdriver").attr("checked",true);
								$("#needdriver").val("1");
							}
						</script>
						
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">事&nbsp;由：&nbsp;</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<textarea  id="applyreason" name="model.applyreason" readonly style="width:487px;height:120px;">${model.applyreason}</textarea>
					</td>
				</tr>
				<tr >
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">审批意见：&nbsp;</span>
					</td>
					<td class="td1" colspan="4" align="left">
						<textarea  id="approvalsuggestion" name="model.approvalsuggestion" readonly  style="width:487px;height:100px;">${model.approvalsuggestion}</textarea>
					</td>
				</tr>
				<tr>
					<td class="td1" colspan="5" align="center" height="21">
						<input name="Submit2" type="button" class="input_bg" value="关闭" onclick="window.close();">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
