<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="java.net.URLEncoder" %>
<%		
	String oaUrl = URLEncoder.encode("系统安装注意事项与常见问题.doc","utf-8");
	String rtxUrl = URLEncoder.encode("RTX客户端安装与配置.doc","utf-8");
	String realPath = request.getRealPath("/");
%> 

<html>
	<head>
		<title>系统帮助文件下载</title>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
	</head>

	<BODY>
		<h2 align=center> 
			&equiv;系统帮助文件下载&equiv; 
		</h2>
		<a name="notice" />
				<h3>
					◆文件链接
				</h3>
				<OL>
					<li>
						下载说明
					</li>
					<BR>
					如果您是初次使用我们的系统，我们建议您仔细查看我们的帮助文档，上面说明了一些系统在使用过程中可能碰到的问题和RTX即时通讯工具客户端的安装与配置
					<BR>
					如果打开链接时不能正常下载，您可以在链接上击右键，选择“
					<font color=green>目标另存为(<u>A</u>)...</font>”
					<li>
						下载列表
					</li>
				</OL>
				<TABLE border=1 align=center style="width:90%;BORDER-COLLAPSE: collapse;border:solid 1 #999999">
					<TR align=center>
						<TD nowrap>
							序号
						</TD>
						<TD nowrap>
							链接下载
						</TD>
						<TD nowrap>
							文件说明
						</TD>
					</TR>
					<TR>
						<TD>
							1
						</TD>
						<TD>
							<a id="oa" href="<%=path %>/common/helpfiles/oahelper.doc">系统安装注意事项与常见问题</a>
						</TD>
						<TD>
							系统安装注意事项与常见问题
						</TD>
					</TR>
					<TR class="tr_bg">
						<TD>
							2
						</TD>
						<TD>
							<a id="rtx" href="<%=path %>/common/helpfiles/rtxhelper.doc">RTX客户端安装与配置</a>
						</TD>
						<TD>
							RTX客户端安装与配置
						</TD>
					</TR>		
				</TABLE>
	</BODY>
</html>
