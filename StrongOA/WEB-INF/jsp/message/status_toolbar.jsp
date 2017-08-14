<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
	<TITLE>内容</TITLE>
	<META http-equiv=Content-Type content="text/html; charset=UTF-8">
	<link href="<%=frameroot%>/css/properties_windows_list.css" rel="stylesheet" type="text/css">
	<LINK href="<%=frameroot%>/css/status_toolbar.css" type=text/css rel=stylesheet>
	<script type="text/javascript">
		function changeFrame(){
			if(window.parent.parent.frames.container.rows=="70%,*"){
				window.parent.parent.frames.container.rows="0,*";
				document.getElementById("doubleclickcolumn").title="双击还原";
			}else{
				window.parent.parent.frames.container.rows="70%,*";
				document.getElementById("doubleclickcolumn").title="双击以最大化";
			}
		}
	</script>
	</HEAD>
	<BODY class=toolbarbodymargin>
		<DIV id=toolbarborder>
			<DIV id=toolbar>
			  	<TABLE height=25 cellSpacing=0 cellPadding=0 width="100%" border=0>
				  	<TBODY>
					  	<TR>
					    	<TD vAlign=center align=middle width=1 height=25>&nbsp;</TD>
					    	<TD class=text id=doubleclickcolumn title=双击以最大化 vAlign=center ondblclick="changeFrame()" noWrap align=left width=* height=25>
					    	<table width="100%" border="0" cellspacing="0" cellpadding="00">
					    	<tr>
					    	<td class="table_headtd_img" style="padding-left:5px;">
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left" >
												<strong>快速阅读</strong>
											</td>
											</tr>
											</table>
						</TD>
					  	</TR>
				  	</TBODY>
			  	</TABLE>
		  	</DIV>
	  	</DIV>
  	</BODY>
</HTML>
