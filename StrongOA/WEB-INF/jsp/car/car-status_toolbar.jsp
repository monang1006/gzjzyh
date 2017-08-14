<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>内容</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/status_toolbar.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
	<script type="text/javascript">
		function changeFrame(){
			if(window.parent.parent.frames.container.rows=="30%,*"){
				window.parent.parent.frames.container.rows="0,*";
				document.getElementById("doubleclickcolumn").title="双击还原";
			}else{
				window.parent.parent.frames.container.rows="30%,*";
				document.getElementById("doubleclickcolumn").title="双击以最大化";
			}
		}
		
		function goback(){
			parent.parent.location = "<%=path%>/car/carApply.action";
		}
	</script>
	</HEAD>
	<BODY class=toolbarbodymargin>
		<DIV id=toolbarborder>
			<DIV id=toolbar>
				<TABLE height=25 cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR>
							<TD>
								&nbsp;
							</TD>
							<TD class=text id=doubleclickcolumn valign="middle" noWrap align=left width="50%" height=25>
								查看车辆使用情况
							</TD>
							<td width="50%"><table align="right"><tr>
							<TD >
												<a class="Operation" href="#" onclick="goback();">
													<img src="<%=root%>/images/ico/message2.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">返回申请列表&nbsp;</span> </a>
							</TD>
							</tr></table></td>
							<td width="5"></td>
							<td width="5"></td>
							<td width="5"></td>
						</TR>
					</TBODY>
				</TABLE>
			</DIV>
		</DIV>
	</BODY>
</HTML>
