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
		<style media="screen" type="text/css">
		.text-overflow {  
			    display:block;/*内联对象需加*/  
			    width:31em;  
			    word-break:keep-all;/* 不换行 */  
			    white-space:nowrap;/* 不换行 */  
			    overflow:hidden;/* 内容超出宽度时隐藏超出部分的内容 */  
			    text-overflow:ellipsis;/* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/  
			} 
		</style>
	</head>

	<body onload="window.focus();">
		<div id="contentborder" onclick="this.blur();">
			<TABLE cellSpacing=1 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR>
						<TD class=pagehead1>
							<!--  &nbsp;
							<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
							&nbsp;催办记录-->
						</TD>
					</TR>
					<TR>
						<TD class=pagehead2 style="display: none"> 

						</TD>
					</TR>
				</TBODY>
			</TABLE>
			<table cellSpacing=1 cellPadding=1 width="100%" border="0"
				class="table1">
				<tr class="biao_bg2">
					<td class="td4" align="center" width="15%">
						<strong>催办人</strong>
					</td>
					<td class="td4" align="center" width="40%">
						<strong>催办内容</strong>
					</td>
					<td class="td4" align="center" width="20%">
						<strong>被催办人</strong>
					</td>
					<td class="td4" align="center" width="10%">
						<strong>催办方式</strong>
					</td>
					<td class="td4" align="center" width="15%">
						<strong>催办时间</strong>
					</td>
				</tr>
				<s:iterator value="modelList" var="processurgency">
				<tr class="biao_bg1">
					<td  align="center" width="15%" title="<s:property value="#processurgency.urgencyederName"/>">
						<span><s:property value="#processurgency.urgencyerName"/></span>
					</td>
					<td  align="left" width="40%" title="<s:property value="#processurgency.handlerMes"/>">
						<span><s:property value="#processurgency.handlerMes"/></span>
					</td>
					<td  align="center" width="20%" title="<s:property value="#processurgency.urgencyederName"/>">
						<span><s:property value="#processurgency.urgencyederName"/></span>
					</td>
					<td  align="center" width="10%" title="<s:property value="#processurgency.remindType"/>">
						<span><s:property value="#processurgency.remindType"/></span>
					</td>
					<td  align="center" width="15%" title="<s:property value="#processurgency.showUrgencyDate"/>">
						<span><s:property value="#processurgency.showUrgencyDate"/></span>
					</td>
				</tr>
				</s:iterator>
			</table>
			<br />
		</div>

		<div align="center">
			<font style="font: bold"> </font>
			<table>
				<tr height="80">
					<td align="center">
						<input type="button" id="btnNo" class="input_bg"
							icoPath="<%=frameroot%>/images/quxiao.gif"
							onclick="window.close()" value="关 闭" />
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
