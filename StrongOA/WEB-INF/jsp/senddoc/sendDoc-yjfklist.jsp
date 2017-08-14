<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  	<head>
    	<title>意见征询反馈信息</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
  	</head>
  	<body class=contentbodymargin>
  		<div id=contentborder align=center>
			<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<%
					String[] paths = (String[])request.getAttribute("path");//得到图片文件列表（路径）
					int len = 0;
					if(paths != null && paths.length > 0) {
						//记录是否存在附件的标记,用于判断是否需要显示意见征询反馈Tab项
						out.println("<input id='count' type='hidden' value='1'>");
						len = paths.length;
						for(int i=0;i<paths.length;i++){
							out.println("<tr><td class=\"td1\">");
							//A4 297mm;210mm
							out.println("<img style=\"width: 210mm;height: 297mm;\" src="+paths[i]+" />");
							out.println("</td>");
						}
					} else {
						out.println("<input id='count' type='hidden' value='0'>");
					}
				%>
			</table>
  		</div>
  	</body>
</html>
<<script type="text/javascript">
<!--
	var count = "<%=len%>";
	if(count != "0"){
		var formTabName = "<%=request.getParameter("tabName")%>";
		if(formTabName == "null" || formTabName == ""){
			alert("参数tabName不可为空。");
		}
		parent.formReader.SetFormTabVisibility(formTabName,true);
	}
//-->
</script>
