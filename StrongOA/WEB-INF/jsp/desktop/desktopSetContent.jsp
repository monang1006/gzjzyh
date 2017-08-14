<%@ page language="java" pageEncoding="utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>默认桌面设置</title>
		<link type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<SCRIPT>
			function refreshFrame(obj){
				var objs = document.getElementsByTagName("td");
				for(var i=0;i<objs.length;i++){
					if(objs[i].className=="deskset_over"){
						objs[i].className = "deskset_out";
						objs[i].childNodes[0].className = "";
					}
				}
				obj.className="deskset_over";
				obj.childNodes[0].className="deskset";
				var frame = document.getElementById("contentframe");
				var url = "<%=path%>/desktop/desktopWhole.action?defaultType="+obj.value;
				if(frame.src!=url)
					frame.src = url;
			}
		</SCRIPT>
	</head>
	<body style="margin: 0 ">
			<div class="deskline"></div>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="80" valign="bottom" class="deskset_over" value="0" onclick="refreshFrame(this)">
						<div class="deskset">
							个人桌面
						</div>
					</td>
					<td width="80" class="deskset_out" onclick="refreshFrame(this)" value="1">
						<div>
						领导桌面
						</div>
					</td>
					<td class="set">
						&nbsp;
					</td>
				</tr>
			</table>
			<iframe id="contentframe" width="100%" height="95%" frameborder="0"
				src="<%=path%>/desktop/desktopWhole.action?defaultType=0"
				marginheight="0" marginwidth="0" scrolling="no" />
	</body>
</html>
