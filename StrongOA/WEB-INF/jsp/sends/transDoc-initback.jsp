<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  	<head>
    	<title>退回公文</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			//确认退回
			function doSubmit(){
				var content = $("#content").val();
				if($.trim(content) == ""){
					alert("请填写退回原因。");
					return ;
				}
				if(content.length > 500){
					alert("退回原因请控制在500字以内。");
					return ;
				}
				var id = "<%=request.getParameter("id")%>";
				$.post("<%=root%>/sends/transDoc!doBack.action",{"model.rest2":content,"model.docId":id},function(ret){
					if(ret == "0"){
						window.returnValue = "0";
						window.close();
					}else if(ret == "-1"){
						alert("退回失败，请与管理员联系。");
						window.close();
					}
				});
			}
		</script>
  	</head>
  	<body class=contentbodymargin>
  		<div id=contentborder align=center>
  			<br>
  			<form>
							<span class="wz">确定要退回公文，请填写退回原因 </span>
							<br><br>
						<table width="100%" height="60%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<tr>
								<td class="td1">
									<textarea id="content" style="width: 100%;height: 100%"></textarea>
								</td>
							</tr>
						</table>
						<table width="85%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="34%" height="34">
												&nbsp;
											</td>
											
											<td width="29%">
												&nbsp;
											</td>
										
											<td width="29%">
												<input id="save" name="save" onclick="doSubmit();" type="button" class="input_bg" value="确认退回">
											</td>
											<td width="29%">
												&nbsp;
											</td>
											<td width="37%">
												<input name="button" type="button" onclick="window.close();" class="input_bg" value="关 闭">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
  			</form>
  		</div>
  	</body>
</html>
