<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title></title>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<title>邮件提示</title>
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
					</TD>
				</tr>
				<tr>
					<td>
						可以用鼠标点击左边的树形结构，进行邮件查看，可以用邮箱列表中的右键菜单进行相应操作。

					</TD>
				</tr>
				<tr>
					<td>
						可以在右边的框架中进行邮件查看和相应操作。


					</TD>
				</tr>
			</table>
		</DIV>
	</body>
</html>
