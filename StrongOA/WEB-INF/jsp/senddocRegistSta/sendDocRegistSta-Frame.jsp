<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
		String selectType = request.getParameter("selectType");
	 %>
<html>
  <head>
    <%@include file="/common/include/meta.jsp"%>
    <title>发文登记统计</title>
    <script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <script type="text/javascript">
		function scrollIntoView(){
			var a=document.getElementById("iframe");
	        if (a){
	        	a.scrollIntoView(true);
	        }       
		}
		
		function setFramebyOrg(orgId){
//			var conFrame = document.frames["iframe"];
//			conFrame.setFramebyOrg(orgId);
			var a=document.getElementById("iframe");
			a.src = "<%=path%>/senddocRegistSta/sendDocRegistSta!wjtjList.action?selectType=<%=selectType%>&roomId=" + orgId;
		}
    </script>
  </head>
  <body>
	<div id=contentborder >
		<div id="div_wjtj" style="position:relative; overflow:hidden; height:100%;">
			<iframe id="iframe_wjtj" height="100%" frameborder="0" width="100%" src="<%=path%>/senddocRegistSta/sendDocRegistSta!wjtj.action?selectType=<%=selectType%>"></iframe>			
		</div>
		<div style=" left:0; top:21px; right:0px; bottom:0; overflow:hidden; height:95%;">
			<iframe id="iframe" height="100%" frameborder="0" width="100%" src="<%=path%>/senddocRegistSta/sendDocRegistSta!wjtjList.action?selectType=<%=selectType%>" scrolling="no"></iframe>	
		</div>
	</div>		  
  </body>
</html>
