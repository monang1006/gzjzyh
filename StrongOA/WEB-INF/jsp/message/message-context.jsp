<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title></title>
		<link href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" src="<%=root %>/common/js/common/common.js"></script>
		<title>消息提示</title>
		<script type="text/javascript">
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
			function gotoWrite(){
				//window.parent.location="writemail.jsp";
				//fileNameRedirectAction.action?toPage=mymail/writemail.jsp
				var boo=OpenWindow('<%=root%>/fileNameRedirectAction.action?toPage=message/message-write.jsp', '700', '500', window);				
				if(boo=="true"){
					window.location.reload();
				}else if(boo=="false"){
				}
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
						可以用鼠标点击左边的树形结构，进行消息查看，可以用消息列表列表中的右键菜单进行相应操作。

					</TD>
				</tr>
				<tr>
					<td>
						可以在右边的框架中进行消息查看和相应操作。


					</TD>
				</tr>
			</table>
		</DIV>
	</body>
</html>
