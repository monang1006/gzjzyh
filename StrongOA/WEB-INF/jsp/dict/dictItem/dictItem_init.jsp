<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title></title>
		<link href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<SCRIPT language=javascript>

			function newInfoSet(){            //新建信息集

   				alert("请选择父信息集");
			}

			function editInfoSet(){           //编辑信息集

  				alert("请选择父信息集");

			}

			function deleteInfoSet(){ //删除信息集

   				alert("请选择父信息集");
 			}
 
 			function createInfoSet(){            //构建信息集

   				alert("请选择父信息集");
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
						用鼠标左键点击字典类，可以查看这个字典类的字典项信息。


					</TD>
				</tr>
				 <!--<tr>
					<td>
						用鼠标右键点击功能模块，可以对选中字典项进行操作。


					</TD>
				</tr> -->
			</table>
		</DIV>
	</body>
</html>
