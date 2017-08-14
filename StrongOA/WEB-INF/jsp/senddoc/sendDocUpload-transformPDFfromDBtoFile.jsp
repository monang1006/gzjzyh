<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>办结文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
			var openFlag = true;
			function Transform(){
				if(openFlag){
					alert("开始导出数据到服务器。。。");
					$("body").mask("操作处理中,请稍候...");
					location=parent.scriptroot+"/senddoc/sendDocUpload!transformPDFfromDBtoFile.action";
					openFlag = false;
				}
			}
			function callback(){
				 $("body").unmask();
				 alert("DPF文件导出成功。");
			}
		</script>
	</head>
	<body scroll="no">
			<fieldset style="width: 100%">
				<table style="width: 100%; height: 100%">
					<tr>
						<td>
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">
							<span class="wz">导出数据库PDF到服务器</span>
						</td>
					</tr>
					<tr>
						<td align=center>
							<input id="transform" name="transform" onclick="Transform();" type="button" value="执行导出" />
						</td>
					</tr>
				</table>
			</fieldset>
	</body>
</html>
