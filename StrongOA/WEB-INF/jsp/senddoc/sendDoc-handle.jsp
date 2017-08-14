<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择流程处理人</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<style media="screen" type="text/css">
    .tabletitle {
      FILTER:progid:DXImageTransform.Microsoft.Gradient(
                            gradientType = 0, 
                            startColorStr = #ededed, 
                            endColorStr = #ffffff);
    }
    
    .hand {
      cursor:pointer;
    }
    </style>
	</head>
	<body  class="contentbodymargin" oncontextmenu="return false;">
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td>
						  		<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"alt="">&nbsp;
									选择流程处理人
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<input type="radio" name="selectType">
						可选处理人
						<input type="radio" name="selectType">
						其他处理人
						<hr>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
									<div
										style="width: 200px; height: 400px; border: solid 1px #506eaa;">
										&nbsp;
									</div>
								</td>
								<td width="400" align="center">
									<input type="button" id="last" value="添加>>" onclick=""
										class="input_bg">
									<br>
									<br>
									<input type="button" id="last" value="<<删除" onclick=""
										class="input_bg">
									<br>
									<br>
									<br>
									<br>
									<input type="button" id="last" value="全部添加>>" onclick=""
										class="input_bg">
									<br>
									<br>
									<input type="button" id="last" value="<<全部删除" onclick=""
										class="input_bg">
								</td>
								<td>
									<div
										style="width: 200px; height: 400px; border: solid 1px #506eaa;">
										&nbsp;
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td width="5%" align="center">
						<hr>
						<input type="button" id="last" value="确定" onclick="submit();"
							class="input_bg">
						<input type="button" id="last" value="取消" onclick="cancel();;"
							class="input_bg">
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
