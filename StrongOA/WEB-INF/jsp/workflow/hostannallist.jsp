<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>办理记录查看</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link type="text/css" rel="stylesheet"
			href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
	</head>

	<body>
		<div id="contentborder" align="center">
			<div>
				<table cellSpacing=1 cellPadding=1 width="100%" border="0"
					class="table1">
					<tr class="biao_bg2">
						<td align="center" width="100">
							<strong>名 称</strong>
						</td>
						<td align="center" width="80">
							<strong>处理人</strong>
						</td>
						<td align="center" width="51%">
							<strong>处理意见</strong>
						</td>
						<td align="center" width="80">
							<strong>处理时间</strong>
						</td>
					</tr>
					<s:iterator value="liuList" status="stat">
						<tr class="biao_bg1">
							<td align="left">
								<s:property value="aiTaskname" />
							</td>
							<td align="center">
								<s:property value="aiActor" />
							</td>
							<td align="left">
								<s:property value="aiContent" />
							</td>
							<td align="left">
								<s:date name="aiDate" format="yyyy-MM-dd HH:mm" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>

			<div align="center">
				<table>

				</table>
			</div>
	</body>
</html>
