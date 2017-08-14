<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/root_path.jsp"%>
<%@ include file="/common/OfficeControl/version.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		
		<title>查看自由流程</title>

		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
		<link type="text/css" rel="stylesheet" href="<%=themePath%>/css/component.css" />
		
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/component.js"></script>
		<script src="<%=path%>/oa/js/eform/fw_eform.js" type="text/javascript"></script>

<style>
#main-table{
	width: 100%;
	height: 100%;
}
#main-title{
	float:left;
}

#toolbar{
	width: 100%;
	padding: 10px 0;
}

fieldset{
	padding: 10px;
}

</style>
	</head>
	
	
<body style="background-color:#ffffff;">
<script type="text/javascript" src="<%=path%>/common/js/Silverlight/Silverlight.js"></script>
<script type="text/javascript">

isView = true;

var bussinessId = "${model.fwFormBizTable};${model.fwFormBizPk};${model.fwFormBizId}";
var fwFormBizTable = "${model.fwFormBizTable}";
var fwFormBizPk = "${model.fwFormBizPk}";
var fwFormBizId = "${model.fwFormBizId}";
 
</script>


<div id="toolbar">
		<table border="0" align="right" cellpadding="0" cellspacing="0">
			<tr>
				<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif" /></td>
				<td class="Operation_input1" onclick="back();">&nbsp;关&nbsp;闭&nbsp;</td>
				<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif" /></td>
				<td width="6"></td>
			</tr>
		</table>
</div>

<table id="main-table">
	<tr height="100%">
		<td width="70%" height="100%" id="out-td" valign="top">
			<form id="form1" name="form1">
			<!-- 电子表单模板id -->
			<input type="hidden" id="formId" name="formId" value="${formId}"/>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<input type="hidden" id="formAction" name="formAction"/>
			
			<input type="hidden" id="fwId" name="fwId" value="${model.fwId}"/>
			<input type="hidden" id="ftId" name="ftId" value="${ftId}"/>
			<input type="hidden" id="jsonHandles" name="jsonHandles"/>
			
			<div style="position: relative; height: 100%">
				<object data="data:application/x-silverlight-2,"
					type="application/x-silverlight-2" width="100%" height="100%"
					id="plugin">
					<param name="source"
						value="<%=path%>/FormReader/StrongFormReader.xap" />
					<param name="onError" value="onSilverlightError" />
					<param name="onLoad" value="onSilverlightLoad" />
					<param name="background" value="white" />
					<param name="minRuntimeVersion" value="4.0.50401.0" />
					<param name="autoUpgrade" value="true" />
					<a href="<%=path%>/detection/lib/Silverlight.exe"
						style="text-decoration: none"> <img
							src="<%=path%>/detection/images/SLMedallion_CHS.png"
							alt="Get Microsoft Silverlight" style="border-style: none" />
					</a>
				</object>
			</div>
				<iframe id="_sl_historyFrame"
					style="visibility: hidden; height: 0px; width: 0px; border: 0px"></iframe>
			</form>
		</td>
	</tr>
</table>	

    
<script type="text/javascript">

$(function(){
	
	var autoHeight = function(){
		$("#plugin").height($("#main-table").height() - 50);
	};
	
	autoHeight();
	$(window).resize(function(){
		autoHeight();
	});
	
});


function back(){
	window.close();
}

</script>
</body>
</html>