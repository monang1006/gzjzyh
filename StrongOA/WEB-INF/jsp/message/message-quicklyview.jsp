<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>详细内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/status_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css"
			rel="stylesheet" type="text/css">
	<BODY class=contentbodymargin>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellpadding="0" cellspacing="1"
				class="table1">
				<tr>
					<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;">
						<span class="wz">日&nbsp;&nbsp;期：</span>
					</td>
					<td class="td1" width="35%">
						<span class="wz">
							<s:date name="model.msgDate" format="yyyy-MM-dd HH:mm"/>
						</span>
					</td>
					<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;">
						<span class="wz">发&nbsp;件&nbsp;人：</span>
					</td>
					<td class="td1" width="35%">
						<span class="wz">${model.msgSender }</span></span>
					</td>	
				</tr>
				<tr>
					<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;">
						<span class="wz">标&nbsp;&nbsp;题：</span>
					</td>
					<td class="td1" width="85%" colspan="3">
						<span class="wz">${model.msgTitle }</span>
					</td>
				</tr>
				<s:if test="attachMentList!=null">
					<tr>
						<td class="biao_bg1" width="15%" align="right" style="padding-right: 0px;">
							<span class="wz">附&nbsp;&nbsp;件：</span>
						</td>
						<td class="td1" width="85%" colspan="3">
							<s:iterator value="attachMentList" status="statu" id="att">
								<div> 
									<img src="<%=path%>/oa/image/mymail/yes.gif">
									<a href="../message/messageAtt!download.action?attachId=<s:property value="#att.attachId"/>"
										target="myIframe">
										<s:property value="#att.attachName" />
									</a>
								</div>
							</s:iterator> 
						</td>
					</tr>
				</s:if>
				<tr>
					<td class="td1" width="100%" colspan="4" id="ArticleContent">
						<%--<iframe id="iFrame1" name="iFrame1" height="300px" width="100%"
							onload="this.height=iFrame1.document.body.scrollHeight"
							frameborder="0"
							src="<%=root%>/message/message!quicklyview.action?forward=showInfo&msgId=${model.msgId }"
							scolling=no></iframe>
					--%>
						<div id="afficheDesc" style="display: none">${model.msgContent}</div>
						<script type="text/javascript">
				         document.getElementById("ArticleContent").innerHTML = document.getElementById('afficheDesc').innerText;
				        </script>
					</td>
				</tr>
			</table>
		</div>
	</BODY>