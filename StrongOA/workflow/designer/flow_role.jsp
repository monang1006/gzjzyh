<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
<HEAD>
<TITLE>模型属性设置</TITLE>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link type="text/css" href="js/jquery/jquery.ui.all.css"
	rel="stylesheet" />
<script language=jscript src="js/workflow/const.js"></script>
<script language=jscript src="js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.tabs.min.js"></script>
<script language=jscript src="js/pagescript/flow_transition.js"></script>
<script type="text/javascript" src="js/workflow/interface.js"></script>
<script type="text/javascript">
	$(function() {
		$("#tabs").tabs();
	});
</script>
<style>
body {
	background-color: buttonface;
	scroll: no;
	margin: 7px, 0px, 0px, 7px;
	border: none;
	overflow: hidden;
}
</style>
<script language=javascript>
	function onlyNum() {
		if (!((event.keyCode >= 48 && event.keyCode <= 57)
				|| (event.keyCode >= 96 && event.keyCode <= 105) || event.keyCode == 8))
			event.returnValue = false;
	}
</script>
</HEAD>

<BODY>
	<table border="0" cellpadding="0" cellspacing="0" height="385px" width="100%">
		<tr>
			<td id="contentscell" valign="top" align="center" height="100%"
				width="100%">
				<div id="tabs" height="100%" width="100%" align="center"
					style="margin-top: 5px; margin-left: 5px; margin-right: 5px;">
					<ul class="title">
						<li><a href="#tabs-1">基本属性</a></li>
					</ul>
					<!-- Tab Page 1 Content Begin -->
					<div id="tabs-1" height="100%" width="100%">
						<TABLE border=0 width="100%" height="100%">
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>基本属性</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage1_2>路由编号</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="lineId" value="" class=txtput disabled>
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD></TD>
											<TD><span id=tabpage1_3>路由名称</span>&nbsp;&nbsp; <script>
												if (currentStatus == "edit") {
													document
															.write("<INPUT TYPE=\"text\" readOnly=\"true\" NAME=\"lineName\" value=\"\" class=txtput>");
												} else {
													document
															.write("<INPUT TYPE=\"text\" NAME=\"lineName\" value=\"\" class=txtput>");
												}
											</script></TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD></TD>
											<TD><span id=tabpage1_3>路由类型</span>&nbsp;&nbsp; <FONT
												style="font-size: 10pt;" COLOR="#919CD0"><INPUT
													TYPE="radio" id="lineType" NAME="lineType" value="Line"
													checked><span id=tabpage1_5>直线</span>&nbsp;<INPUT
													TYPE="radio" NAME="lineType" id="lineType" value="PolyLine"><span
													id=tabpage1_6>折线</span>&nbsp;</FONT></TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage1_7>起始节点</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="startNode" value="" class=txtput disabled>
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD></TD>
											<TD><span id=tabpage1_8>目标节点</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="endNode" value="" class=txtput disabled>
											</TD>
											<TD></TD>
										</TR>
										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE></TD>
								<TD>&nbsp;</TD>
							</TR>

							<TR height="100%">
								<TD></TD>
								<TD></TD>
								<TD></TD>
							</TR>
						</TABLE>
					</div>
					<!-- Tab Page 1 Content End -->
					<!-- 业务扩展页面 -->
					<jsp:include page="ext/flow_transition_ext.jsp" />
			</td>
		</tr>
	</table>

	<table cellspacing="1" cellpadding="0" border="0"
		style="position: absolute; bottom: 20px; width: 100%;">
		<tr>
			<td width="100%" style="text-align: right;">
				<div style="padding-right: 5px;">
					<input type=button id="btnOk" class=btn value="确 定"
						onclick="jscript: okOnClick();"> &nbsp;&nbsp;&nbsp; <input
						type=button id="btnCancel" class=btn value="取 消"
						onclick="jscript: cancelOnClick();">
				</div></td>

		</tr>
	</table>
</BODY>
</HTML>