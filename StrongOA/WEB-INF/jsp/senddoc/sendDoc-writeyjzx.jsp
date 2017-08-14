<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.net.URLDecoder" />
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%
	String title = request.getParameter("businessName");
	if (title != null) {
		title = URLDecoder.decode(title, "utf-8");
	}
%>
<html>
	<head>
		<title>意见征询登记</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<style type="text/css">
		.fontCss{
			font-family: Verdana;
			font-size:16;
		}
		</style>
		<script type="text/javascript">
			function doSubmit(){
				if($.trim($("#title").val()) == ""){
					alert("意见征询标题不可为空，请输入.");
					return ;
				}
				if(document.getElementById("content").value.length > 1500){
					alert("意见征询内容不能超过1500个字.");
					return ;
				}
				$("body").mask("操作处理中,请稍候...");
				$.post("<%=root%>/bgt/senddoc/sendDoc!save.action",
					{
						'model.id':'<%=request.getParameter("docId")%>',
						'model.title' : $("#title").val(),
						'model.unit' : $("#unit").val(),
						'model.strDate' : $("#date").val(),
						'model.content' : $("#content").val(),
						'model.instanceId ':'<%=request.getParameter("instanceId")%>'
					}, function(ret) {
						$("body").unmask();
						if (ret == "0") {
							window.returnValue = "ok";
							window.close();
						} else if (ret == "-1") {
							//error
							alert("对不起，系统出现错误，请与管理员联系。");
						}
					}
				);
			}
		</script>
	</head>
	<body class=contentbodymargin>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id=contentborder align=center>
			<form>
				<fieldset style="width: 90%">
					<legend>
						<span class="wz">意见征询信息</span>
					</legend>
					<div style="height:1px"></div>
					<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="1" class="table1">
						<tr>
							<td width="25%" height="21" class="biao_bg1">
								<span class="wz">意见征询标题<font color=red>*</font>：</span>
							</td>
							<td class="td1">
								<input id="title" maxlength="250" name="title" value="<%=title%>" style="width: 100%;" class="fontCss">
							</td>
						</tr>
						<tr>
							<td width="20%" height="21" class="biao_bg1">
								<span class="wz">意见征询单位：</span>
							</td>
							<td class="td1">
								<input id="unit" maxlength="100" name="unit" style="width: 100%;" class="fontCss">
							</td>
						</tr>
						<tr>
							<td width="20%" height="21" class="biao_bg1">
								<span class="wz">限时办理时间：</span>
							</td>
							<td class="td1">
								<strong:newdate name="date" id="date" width="100%" skin="whyGreen"
									dateform="yyyy-MM-dd HH:mm" classtyle="fontCss" isicon="true"></strong:newdate>
							</td>
						</tr>
						<tr>
							<td width="20%" height="21" class="biao_bg1">
								<span class="wz">意见征询内容：</span>
							</td>
							<td class="td1">
								<textarea id="content" style="width: 100%;" rows="12"  class="fontCss"></textarea>
							</td>
						</tr>
					</table>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td align="center" valign="middle">
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30%" height="34">
											&nbsp;
										</td>
										<td width="10%">
											<input id="save" name="save" onclick="doSubmit();" type="button" class="input_bg"
												value="确定">
										</td>
										<td width="20%">
											&nbsp;
										</td>
										<td width="10%">
											<input name="button" type="button" onclick="window.close();" class="input_bg" value="关闭">
										</td>
										<td width="30%">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</fieldset>
			</form>
		</div>
	</body>
</html>
