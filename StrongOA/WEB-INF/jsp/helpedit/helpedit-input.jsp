<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<html>
	<head>
		<title>帮助编辑</title>
		<%@include file="/common/include/meta.jsp"%>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript"
			src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<!-- 编辑外部样式引用改变 -->
        <link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>

		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>

		<script type="text/javascript">
	function save() {//提交表单
		document.getElementById("helpEditForm").submit();
	}
	function back() {
		location = scriptroot
				+ "/helpedit/helpedit!input.action?helpTreeId=${helpTreeId}";
	}
</script>
	</head>

	<body class="contentbodymargin">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								  <td colspan="3" class="table_headtd">
       <table width="100%" border="0" cellspacing="0"
											cellpadding="00">
        <tr>
         <td class="table_headtd_img" >
           <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
				</td>
				<td align="left">
				<strong>${helpTreeName}</strong>
				</td>
			
			<td align="right">
            <table  border="0" align="right" cellpadding="0" cellspacing="0">
            	<tr>
            	    <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				    <td width="5"></td>
				    <td width="8"></td>
				     <td width="8"></td>
				     <td width="8"></td>
				    <!-- <td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					<td class="Operation_input1" onclick="back();">&nbsp;关&nbsp;闭&nbsp;</td>
					<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				    <td width="6"></td> -->
				</tr>
				</table>
				</td>
						</tr>
					</table>
				</td>
				<tr>
					<td>
				
				<s:form id="helpEditForm" action="/helpedit/helpedit!save.action"
					theme="simple" enctype="multipart/form-data">
					<input type="hidden" id="helpTreeId" name="helpTreeId"
						value="${helpTreeId}">
					<input type="hidden" id="helpId" name="helpId" value="${helpId}">
					<table border="0" width="100%" cellpadding="2" cellspacing="1"
						height="440">
						<tr>
							<td colspan="2">
								<textarea id="helpDesc" name="helpDesc"
									style="display: none">
												${helpDesc}
											</textarea>
								<IFRAME ID="eWebEditor1"
									src="<%=path%>/common/ewebeditor/ewebeditor.htm?id=helpDesc&style=coolblue"
									frameborder="0" scrolling="no" width="850" height="440"></IFRAME>
							</td>
						</tr>
					</table>
				</s:form>
				
			</table>
		</DIV>
	</body>
</html>