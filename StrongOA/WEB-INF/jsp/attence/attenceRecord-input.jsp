<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		<title>上下班打卡</title>
		<%@include file="/common/include/meta.jsp" %>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript'
			src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<style type="text/css">
		#imageSrc
		{
			margin-left:5px;
		    filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src='<%=path%>/oa/image/meetingroom/LOGO.jpg');
		}
		</style>
		<base target="_self">
	</head>
	<body oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form id="form" name="form" method="post" enctype="multipart/form-data">
		
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td width="5%" align="center">
						<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
					</td>
					<td width="20%">
						上下班打卡
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
					<td rowspan="1" colspan="4"  height="40" class="td1" align="center">
						<span class="wz">今日出勤信息</span>
					</td>
					
				</tr>
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1" align="right">
						<span class="wz">系统当前时间：&nbsp;</span>
					</td>
					<td colspan="1" width="30%" class="td1" align="left">
						<input id="systime"  name="systime" value="<%= new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()) %>" type="text" style="width:95%;">
					</td>
					<td  colspan="2" width="50%" class="td1">
					</td>
					
				</tr>
				<tr>
				    <td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">签到时间：&nbsp;</span>
					</td>
					<td colspan="1" class="td1" align="left">
						<input id="worktime"  name="model.worktime" value="${model.worktime}" type="text" style="width:95%;">
					</td>
					<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">上班时间：&nbsp;</span>
					</td>
					<td  width="30%" colspan="1" class="td1">
						<input id="workstartime"  name="workstartime" value="${attendanceTime.worktime}" type="text" style="width:95%;">
					</td>
					
				</tr>
				<tr>
				   
				   <td colspan="1"  height="21" class="biao_bg1" align="right">
						<span class="wz">签退时间：&nbsp;</span>
					</td>
					<td colspan="1"  class="td1" align="left">
						<input id="leavetime"  name="model.leavetime" value="${model.leavetime}" type="text" style="width:95%;">
					</td>	
					<td  height="21" class="biao_bg1" align="right">
						<span class="wz">下班时间：&nbsp;</span>
					</td>
					<td  width="30%" class="td1" align="left">
						<input id="workendtime"  name="workendtime" value="${attendanceTime.leavetime}" type="text" style="width:95%;">
					</td>
					
				</tr>
				
            
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">备注：&nbsp;</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea  id="remarks" name="remarks" 
							             style="width:464px;height:150px;" >${model.sremarks}</textarea>
				    </td>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center" height="21">
					    <input name="Submit" type="button" class="input_bg" value="签到" onclick="signIn();">
						<input name="Submit2" type="button" class="input_bg" value="签退"
							onclick="signOut();">
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
<script language="javascript">	

	function signIn(){
		var t = $("#worktime").val();
		if(t!=""&&t!=null){
			if(confirm("你已签过到，此次签到将覆盖上次记录，是否继续操作？")){
				$("#form").attr("action","<%=path%>/attence/attenceRecord!save.action?state="+"in");
				$("#form").submit();
			}
		}else{
			$("#form").attr("action","<%=path%>/attence/attenceRecord!save.action?state="+"in");
			$("#form").submit();
		}
	}

	function signOut(){
		var t = $("#leavetime").val();
		if(t!=""&&t!=null){
			if(confirm("你已签退，此次签退将覆盖上次记录，是否继续操作？")){
				$("#form").attr("action","<%=path%>/attence/attenceRecord!save.action?state="+"out");
				$("#form").submit();
			}
		}else{
			$("#form").attr("action","<%=path%>/attence/attenceRecord!save.action?state="+"out");
			$("#form").submit();
		}
	}

</script>
	</body>
</html>
