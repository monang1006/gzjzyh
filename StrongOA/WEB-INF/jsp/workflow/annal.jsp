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
		<style  media="screen" type="text/css">
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
		<div id="contentborder"   onclick="this.blur();">
			<TABLE cellSpacing=1 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR>
						<TD class=pagehead1>
						 <!-- &nbsp;
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">
							&nbsp;${isTransmitting == 1?"转办":"办理"}记录 -->
						</TD>
					</TR>
					<TR>
						<TD class=pagehead2 style="display: none">

							<TABLE style="FLOAT: right; WIDTH: 150px" cellSpacing=0 cellPadding=0>
								<TBODY>
									<TR>
										<td></td>
									</TR>
								</TBODY>
							</TABLE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			${tableString}
		</div>

		<div align="center">
		<font style="font: bold"> </font>
			<table>
				<tr height="80">
					<td align="center">
						<input type="button" id="btnNo" class="input_bg"
							icoPath="<%=root%>/images/ico/quxiao.gif"
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
