<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp" %>
<HTML>
	<HEAD><TITLE>详细内容</TITLE>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/status_windows.css" type=text/css rel=stylesheet>
	<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
	<BODY class=contentbodymargin>
		<div id="contentborder" align="center">	
			<table  width="100%" border="0" cellpadding="0" cellspacing="1"  class="table1">
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">日&nbsp;&nbsp;期：</span></td>
					<td class="td1" width="35%"><span class="wz"><s:date name="myMail.mailSendDate" format="yyyy-MM-dd HH:mm"/></span></td>
					<td class="biao_bg1" width="15%"><span class="wz">发件人：</span></td>
					<td class="td1" width="35%"><span class="wz">${myMail.mailSender }</span></span></td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">主&nbsp;&nbsp;题：</span></td>
					<td class="td1" width="75%" colspan="3"><span class="wz">${myMail.mailTitle }</span></td>
				</tr>
				<tr>
					<td class="biao_bg1" width="15%"><span class="wz">附&nbsp;&nbsp;件：</span></td>
					<td class="td1" width="75%" colspan="3">
					<span class="wz">
						<s:iterator value="#request.attList" status="statu" id="att">
							<a href="../mymail/mailAtt!download.action?attachId=<s:property value="#att.attachId"/>" target="myIframe" ><s:property value="#att.attachName" /></a>;
						</s:iterator>
					</span>
					</td>
				</tr>
				<tr>
					<td class="td1" width="100%" colspan="4"> 
						<iframe id="iFrame1" name="iFrame1" height="300px" width="100%" onload="this.height=iFrame1.document.body.scrollHeight" frameborder="0" src="<%=root %>/mymail/mail!showInfo.action?sendid=${myMail.mailId }" scolling=no></iframe>
					</td>
				</tr>
			</table>
		</div>
	</BODY>
