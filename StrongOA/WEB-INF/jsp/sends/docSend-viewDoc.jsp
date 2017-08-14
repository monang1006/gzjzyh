<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
		<title>公文文件查看</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
		function windowClose(){
			window.close(); 
		}
		
		//接收\拒收
		function sends(){ 
			var id = "${docId}";
			var boo=OpenWindow('<%=root%>/sends/docSend!orgTree.action?docId='+id, '600', '400', window);
			if(boo=="reload"){
				returnValue = "reload";
				window.close();
			}
		} 
		
		//公文分发
	     function sendDoc() {
				var id = $("#docId").val();
				//var zs = window.parent.frames[0].document.getElementById("docSubmittoDepart_id").value;
				//var cs = window.parent.frames[0].document.getElementById("docCcDepart_id").value;
<%--				function getSendIds(obj){--%>
<%--					var temp = '';--%>
<%--					$.each(obj,function(){--%>
<%--					    if(temp == "")--%>
<%--							temp = arguments[1];--%>
<%--					    else--%>
<%--						    temp += ',' + arguments[1];--%>
<%--					});--%>
<%--					return  temp;--%>
<%----%>
<%--				}--%>
				
				var url = "<%=path%>/sends/docSend!viewSendOrg2.action?docId="+id+"&zs="+ window.parent.frames[0].window.newSelectedZObj.ids+"&cs="+ window.parent.frames[0].window.newSelectedObj.ids;
				var a = OpenWindow(url,'550', '400', window);
				if(a=="reload"){
					window.returnValue="reload";
					window.close();
					//window.close();
				}
	      }
	</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;" scroll="no">
	<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
			<form id="form" action="<%=root %>/sends/transDoc!save.action" method="post">
				<input type="hidden" id="senddocId" name="senddocId" value="<%=request.getParameter("senddocId")%>">
				<input type="hidden" id="docId" name="docId" value="<%=request.getParameter("docId")%>">
				<input type="hidden" id="showType" name="showType" value="<%=request.getParameter("showType")%>">
				<table width="100%" height="5%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="5%" align="center">
										<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
									</td>
									<td width="10%">
										查看公文
									</td>
									<td>
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>										
											<td>
												<a class="Operation" href="javascript:sendDoc();">
													<img src="<%=frameroot%>/images/songshen.gif" width="15"
														height="16" class="img_s">分发</a>
											</td>
											<td width="5"></td>

											<td>
												<a class="Operation" href="javascript:windowClose();"> <img
														src="<%=frameroot%>/images/guanbi.gif" width="15"
														height="15" class="img_s">关闭</a>
											</td>
											<td width="5">
												&nbsp;
											</td>
										</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<table width="100%" height="94%">
					<tr>
						<td width="100%">
							<iframe id='view' name='view'  scrolling="no" align="top" width="100%" height="100%"  frameborder="1" 
							src="<%=path%>/receives/recvTDoc!input2.action?archiveId=${docId}&forwardStr=${showType}&newDate="+newDate()"">
							</iframe>
						</td>
					</tr>
					<iframe id="annexFrame" style="display:none"></iframe>
				</table>
			</form>
	</body>
</html>
