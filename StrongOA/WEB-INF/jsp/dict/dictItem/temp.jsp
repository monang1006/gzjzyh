<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<title>查找字典类</title>
	</head>
	<%
		String dictType = request.getParameter("dictType");
	%>
	<script type="text/javascript" language="java">
	
		function save()
		{
			var dictName=document.getElementById("dictName").value;
			var type="<%=dictType%>";
			window.dialogArguments.parent.project_work_tree.setNameValue(dictName,type);
			window.dialogArguments.parent.project_work_tree.submitForm();
			//window.dialogArguments.parent.project_work_tree.location="<%=root%>/dict/dictType/dictType!tree.action?name="+dictName+"&type=<%=dictType%>";
			window.close();
		}
	
	</script>
	<BODY class="contentbodymargin">
	<DIV id=contentborder align=center>
	<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>查找字典类</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
		<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
			<tr>
				<td class="biao_bg1" align="right">
					<span class="wz">字典类名称：</span>
				</td>
				<td class="td1" style="padding-left:5px;">
					<input type="text" name="dictName" id="dictName">
				</td>
			</tr>
			<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
		</table>
		</td>
		</tr>
		</table>
		</DIV>
	</body>
</html>
