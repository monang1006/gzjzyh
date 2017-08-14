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
<script language=jscript src="js/pagescript/flow_node.js"></script>
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
	<table border="0" cellpadding="0" cellspacing="0" height="385px"
		width="100%">
		<tr>
			<td id="contentscell" valign="top" align="center" height="100%"
				width="100%">
				<div id="tabs" height="100%" width="100%" align="center"
					style="margin-top: 5px; margin-left: 5px; margin-right: 5px;">
					<ul class="title">
						<li><a href="#tabs-1">节点设置</a>
						</li>
						<li><a href="#tabs-2">自动代理设置</a>
						</li>
					</ul>
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
												href="#" onclick="setForms()">选择表单</a> <INPUT TYPE="hidden"
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
							
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>路由设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>进入节点时是否汇聚</span>&nbsp;&nbsp; <script>
												if (currentStatus == "edit") {
													document
															.write("<input type=\"radio\" name=\"preTranType\" value=\"0\" disabled></input>否&nbsp;&nbsp;<input type=\"radio\" name=\"preTranType\" value=\"1\" disabled>是");
												} else {
													document
															.write("<input type=\"radio\" name=\"preTranType\" value=\"0\"></input>否&nbsp;&nbsp;<input type=\"radio\" name=\"preTranType\" value=\"1\">是");
												}
											</script>
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
					<!-- Tab Page 1 Content End -->

					<!-- Tab Page 2 Content Begin -->
					<div id="tabs-2" class="tab_content">
						<TABLE border=0 width="100%" height="100%">
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>自动代理设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>自动代理</span>&nbsp;&nbsp; <input
												type="text" event-attr="event-name"
												name="node-delegate-name" class="txtput" readonly="readonly" />
												<input type="button" class="select-event" value="选择" /> <input
												type="button" class="clear-event" value="清除" /><br /><input
												type="text" name="nodeDelAction" event-attr="event-class"
												class="txtput" style="margin-left: 57px; width: 330px"
												readonly="readonly" />
												<div style="margin-left: 57px;"><font color="red">当流程进入该节点时会自动执行设置的代理类。</font></div>
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
					<jsp:include page="ext/flow_node_ext.jsp" />
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
