<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="java.util.ArrayList"%>
<html>
	<head>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet"
			type="text/css">
		<link href="<%=frameroot%>/css/properties_windows.css"
			rel="stylesheet" type="text/css">
	</head>
	<body>
		<span class="wz"> ${model.msgContent } 
		<s:if test="attachMentList!=null&&attachMentList.size()>0">
				<table>
					<br>
					<br>
					<br>
					附件：
					<hr align="left" width="100%">
					<s:iterator id="ls" value="attachMentList" status="ls">
						<tr>
							<td rowspan="2">
								<img src='<%=path%>/oa/image/mymail/yes.gif'>
							</td>
							<td>
								<s:property value="attachName" />
							</td>
						</tr>
						<tr>
							<td>
								
								<a
									href="<%=root%>/message/messageAtt!download.action?attachId=<s:property  value='attachId'/>">下载附件</a>

							</td>
						</tr>
					</s:iterator>
					<table>
			</s:if>
		</span>
	</body>
</html>


