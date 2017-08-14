<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
<HEAD>
<html:base />
<TITLE>模型属性设置</TITLE>
<link rel="stylesheet" type="text/css" href="css/style.css">
<link type="text/css" href="js/jquery/jquery.ui.all.css"
	rel="stylesheet" />
<script language=jscript src="js/workflow/const.js"></script>
<script language=jscript src="js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="js/jquery/jquery.ui.tabs.min.js"></script>
<script language=jscript src="js/pagescript/flow_subprocess.js"></script>
<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
<script type="text/javascript" src="js/pagescript/event_action.js"></script>
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
</HEAD>

<BODY>
	<table border="0" cellpadding="0" cellspacing="0" height="410px" width="100%">
		<tr>
			<td id="contentscell" valign="top" align="center" height="100%"
				width="100%">
				<div id="tabs" height="100%" width="100%" align="center"
					style="margin-top: 5px; margin-left: 5px; margin-right: 5px;">
					<ul class="title">
						<li><a href="#tabs-1">节点设置</a>
						</li>
						<li><a href="#tabs-2">子流程设置</a>
						</li>
					</ul>
					<!-- Tab Page 1 Content Begin -->
					<div id="tabs-1" class="tab_content">
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
											<TD width="50%"><span id=tabpage1_2>节点编号</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="nodeId" value="" class=txtput disabled>
											</TD>
											<TD width=5></TD>
											<TD><span id=tabpage1_3>节点名称</span>&nbsp;&nbsp; <script>
												if (currentStatus == "edit") {
													document
															.write("<INPUT TYPE=\"text\" readOnly=\"true\" NAME=\"nodeLabel\" value=\"\" class=txtput>");
												} else {
													document
															.write("<INPUT TYPE=\"text\" NAME=\"nodeLabel\" value=\"\" class=txtput>");
												}
											</script>
											</TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage1_7>横向位置</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="leftlocate" value="" class=txtput disabled>
											</TD>
											<TD width=5></TD>
											<TD><span id=tabpage1_8>纵向位置</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="toplocate" value="" class=txtput disabled>
											</TD>
										</TR>
										<TR valign=top>
											<TD></TD>
											<TD><span id=tabpage1_3>节点类型</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="nodeType" value="" class=txtput disabled>
											</TD>
											<TD width=5></TD>
											<TD></TD>
										</TR>
										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE></TD>
								<TD>&nbsp;</TD>
							</TR>
							
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>表单设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>挂接表单</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="togetherform" value="" class=txtput
												disabled> &nbsp;&nbsp;&nbsp; <a id="setform"
												href="#" onclick="setForms('main')">选择表单</a> <INPUT TYPE="hidden"
												NAME="formid" value=""><INPUT
												TYPE="hidden" NAME="formpriv" value="superadmin">
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
							
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>节点事件动作设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>节点进入</span>&nbsp;&nbsp; <input
												type="text" event-attr="event-name" name="node-enter-name"
												class="txtput" readonly="readonly" /> <input type="button"
												class="select-event" value="选择" /> <input type="button"
												class="clear-event" value="清除" /> <br /> <input
												type="text" name="nodeenteraction" event-attr="event-class"
												class="txtput" style="margin-left: 57px; width: 330px"
												readonly="readonly" /></TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>节点离开</span>&nbsp;&nbsp; <input
												type="text" event-attr="event-name" name="node-leave-name"
												class="txtput" readonly="readonly" /> <input type="button"
												class="select-event" value="选择" /> <input type="button"
												class="clear-event" value="清除" /> <br /> <input
												type="text" name="nodeleaveaction" event-attr="event-class"
												class="txtput" style="margin-left: 57px; width: 330px"
												readonly="readonly" /></TD>
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

						</TABLE>
					</div>
					<!-- Tab Page 1 Content End -->

					<!-- Tab Page 2 Content Begin -->
					<div id="tabs-2" class="tab_content">
						<TABLE border=0 width="100%" height="100%">
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>子流程设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>子流程指定</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="subProcessName" value="" class=txtput
												disabled> &nbsp;&nbsp;&nbsp; <a href="#"
												onclick="setProcess()">选择流程</a> <INPUT TYPE="hidden"
												NAME="subProcessId" value="">
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>子流程表单</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="subProcessForm" value="" class=txtput
												disabled> &nbsp;&nbsp;&nbsp; <a href="#"
												onclick="setForms('sub')">选择表单</a> <INPUT TYPE="hidden"
												NAME="subFormid" value="">
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>子流程类型</span>&nbsp;&nbsp; <script>
												if (currentStatus == "edit") {
													document
															.write("<input type=\"radio\" name=\"synchronize\" value=\"1\" disabled>同步</input>&nbsp;&nbsp;<input type=\"radio\" name=\"synchronize\" value=\"0\" disabled>异步</input>");
												} else {
													document
															.write("<input type=\"radio\" name=\"synchronize\" value=\"1\">同步</input>&nbsp;&nbsp;<input type=\"radio\" name=\"synchronize\" value=\"0\">异步</input>");
												}
											</script>
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>父流程表单数据可见</span>&nbsp;&nbsp; <input
												type="checkbox" name="fromParent" value="1">
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>结束时数据返回父流程</span>&nbsp;&nbsp; <input
												type="checkbox" name="toParent" value="1">
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>子流程业务名称</span>&nbsp;&nbsp; <input
												type="text" class="txtput" name="plugins_subBusiName"
												id="plugins_subBusiName" value="">
													&nbsp;&nbsp;&nbsp; @{bname}表示父流程业务名称
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>子流程发起人指定</span>&nbsp;&nbsp; <input
												type="radio" name="useSuperStartor" id="useSuperStartor_1"
												value="1">父流程发起人 <input type="radio"
												name="useSuperStartor" id="useSuperStartor_2" value="2">上步处理人
												<input type="radio" name="useSuperStartor"
												id="useSuperStartor_3" value="3" checked>不指定 <input
												type="hidden" name="plugins_useSuperStartor"
												id="plugins_useSuperStartor" value="0">
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>允许为子流程指定第一步处理人</span>&nbsp;&nbsp;
												<input type="checkbox" name="setStartor" id="setStartor"
												value="1"> <input type="hidden"
												name="plugins_setStartor" id="plugins_setStartor" value="0">
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>允许在子流程中指定父流程下一步处理人</span>&nbsp;&nbsp;
												<input type="checkbox" name="setSuperActor"
												id="setSuperActor" value="1"> <input type="hidden"
												name="plugins_setSuperActor" id="plugins_setSuperActor"
												value="0">
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

							
						</TABLE>
					</div>
					<!-- Tab Page 2 Content End -->
					<!-- 业务扩展页面 -->
					<jsp:include page="ext/flow_subprocess_ext.jsp" />
				</div>
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
				</div>
			</td>

		</tr>
	</table>
</BODY>
</HTML>
<script type="text/javascript">
<!--
	function ownedHandlePlugins() {
		if ($("#useSuperStartor_1")[0].checked) {
			$("#plugins_useSuperStartor").val("1");
		} else if ($("#useSuperStartor_2")[0].checked) {
			$("#plugins_useSuperStartor").val("2");
		} else if ($("#useSuperStartor_3")[0].checked) {
			$("#plugins_useSuperStartor").val("3");
		}
		if ($("#setStartor")[0].checked) {
			$("#plugins_setStartor").val("1");
		} else {
			$("#plugins_setStartor").val("0");
		}
		if ($("#setSuperActor")[0].checked) {
			$("#plugins_setSuperActor").val("1");
		} else {
			$("#plugins_setSuperActor").val("0");
		}
	}

	function ownedInitPlugins() {
		if ($("#plugins_useSuperStartor").val() == "1") {
			$("#useSuperStartor_1")[0].checked = true;
		} else if ($("#plugins_useSuperStartor").val() == "2") {
			$("#useSuperStartor_2")[0].checked = true;
		} else if ($("#plugins_useSuperStartor").val() == "3") {
			$("#useSuperStartor_3")[0].checked = true;
		}
		if ($("#plugins_setStartor").val() == "1") {
			$("#setStartor")[0].checked = true;
		}
		if ($("#plugins_setSuperActor").val() == "1") {
			$("#setSuperActor")[0].checked = true;
		}
	}
//-->
</script>
