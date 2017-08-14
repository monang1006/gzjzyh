<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>列表控件映射</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript"
			src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/fckeditor2/fckeditor.js"
			type="text/javascript"></script>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>

		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>

<script type="text/javascript"> 
     
function formsubmit(){

	if(document.getElementById("mapPrivil").value=="123"){
	   	alert("请选择资源模块");
	   	document.getElementById("mapPrivil").focus();
	   	return false;
    } 
    
    if(document.getElementById("mapForm").value=="123"){
	   	alert("请选择列表控件");
	   	document.getElementById("mapForm").focus();
	   	return false;
    }  
    
    if(document.getElementById("mapType").value=="123"){
	   	alert("请选择列表控件");
	   	document.getElementById("mapType").focus();
	   	return false;
    } 
       
	$("form").submit();
	 
}
				 
		     	
</script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>

			<table width="100%"
				style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
				<tr>
					<td>&nbsp;</td>
					<td width="30%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						列表控件映射
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<s:form action="/componentMap/componentMap!save.action"
				method="post" enctype="multipart/form-data">
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
						<tr>
						<td width="30%" height="21" class="biao_bg1" align="right">
							<span class="wz">资源模块(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select list="privilList" listKey="privilId"
								listValue="privilName + '(' + privilSyscode + ')'" headerKey="123" headerValue="请选择资源模块"
								id="mapPrivil" name="model.mapPrivil.privilId"
								style="width:15.5em" />

						</td>
					</tr>
					<tr>
						<td width="30%" height="21" class="biao_bg1" align="right">
							<span class="wz">流程名称&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select list="processList" listKey="pfId"
								listValue="pfName" headerKey="123" headerValue="请选择流程名称"
								id="mapProcess" name="model.mapProcess.pfId"
								style="width:15.5em" />

						</td>
					</tr>
					<tr>
						<td width="30%" height="21" class="biao_bg1" align="right">
							<span class="wz">列表控件(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select list="efomList" listKey="id"
								listValue="title" headerKey="123" headerValue="请选择列表控件"
								id="mapForm" name="model.mapForm.id"
								style="width:15.5em" />

						</td>
					</tr>
					
					<tr>
						<td width="30%" height="21" class="biao_bg1" align="right">
							<span class="wz">用途类型(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
						
						<s:select list="#{'0':'展现列表','1':'搜索列表'}" listKey="key" 
							listValue="value" headerKey="123" headerValue="请选择用途类型"
							id = "mapType" name="model.mapType"
							style="width:15.5em" />

						</td>
					</tr>

				</table>
			</s:form>

			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="10%" height="34">
									&nbsp;
								</td>
								</td>
								<td width="30%">
									<input name="Submit" type="button" class="input_bg"
										onclick="formsubmit();" value="保  存">
								</td>
								<td width="30%">
									<input name="Submit2" type="button" class="input_bg"
										value="关  闭" onclick="closes();">
								</td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
