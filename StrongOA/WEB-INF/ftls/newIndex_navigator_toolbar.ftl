<!--树型菜单上的工具栏-->
<!--newIndex_navigator_toolbar.ftl-->
<!--首页/主框架工作区/树型结构区域/上部区域-->
﻿<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>导航器工具栏</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/navigator_toolbar.css"
			type=text/css rel=stylesheet>

	</HEAD>
	<BODY class=toolbarbodymargin>
		<DIV id=toolbarborder>
			<DIV id=toolbar>
				<table height=25 cellspacing=0 cellpadding=0 width="100%" border=0>
					<tbody>
						<tr>
							<td valign=center align=middle width=15 height=25>
								&nbsp;
							</td>
							<td class=text id=doubleclickcolumn title=双击以最大化 valign=center
								nowrap align=left width=* height=25>
								管理菜单
							</td>
						</tr>
					</tbody>
				</table>
			</DIV>			
		</DIV>
	</BODY>
</HTML>
