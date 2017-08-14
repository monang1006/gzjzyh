<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	
		<title>增加组织机构</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK  type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css" >
	
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" >
	<DIV id="msgdiv" style="display: none;">
		</DIV>
		<script type="text/javascript">
			var id = '${extOrgId}'
			if(id == ""){//设置操作权限后的跳转
				id = "${orgId}";
			}
			//alert(document.getElementById("msgdiv").children[0].children[0].children[0].innerHTML);
		//	window.dialogArguments.location = '<%=path%>/usermanage/usermanage!ogrlist.action?extOrgId='+id;
			//资源复制成功弹出提示 0000048441
			var mark = '${mark}';
			if(mark==1){
				alert("资源复制成功。");
			}
			window.dialogArguments.submitForm();
			window.close();
		</script>
	</body>
</html>
