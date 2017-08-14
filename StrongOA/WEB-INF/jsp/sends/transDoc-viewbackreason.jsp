<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  	<head>
    	<title>查看公文退回原因</title>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
  	</head>
  	<body class=contentbodymargin>
  		<div id=contentborder align=center>
  			<br>
  			<form>
							<span class="wz">查看公文退回原因 </span>
							<br><br>
						<table width="100%" height="60%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<tr>
								<td class="td1">
									<textarea id="content" style="width: 100%;height: 100%">${model.rest2 }</textarea>
								</td>
							</tr>
						</table>
						<table width="85%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="15%" height="34">
												&nbsp;
											</td>
											
											<td width="15%">
												&nbsp;
											</td>
										
											<td width="70%">
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
