<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title></title>
		<link href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<title>页面模型</title>
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
		</script>
	</head>
	<BODY class=contentbodymargin>
	
		<DIV id=contentborder cellpadding="0">
			<table>
				<tr>
					<td>
						<b>操作提示：</b>
					</td>
				</tr>
				<tr>
					<td>
						可以用鼠标点击左边的树形结构，进行页面模型相关信息查看、进行相关页面模型信息的编辑。

					</td>
				</tr>
				<tr>
					<td>
						页面模型可以对生成页面路径进行配置
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
