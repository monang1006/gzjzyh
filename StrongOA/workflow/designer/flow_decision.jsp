<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String designRoot = path + "/workflow/designer";
%>
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
		<script language=jscript src="js/pagescript/flow_decision.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
		<script type="text/javascript" src="js/pagescript/event_action.js"></script>
		<script type="text/javascript" src="js/workflow/interface.js"></script>
		<script type="text/javascript">
			$(function() {
				$("#tabs").tabs();
				$("#firstDecision").focus();
			});
		</script>
</HEAD>

<BODY class="whole">
	<tr>
		<td id="contentscell" valign="top" align="center" height="100%"
			width="100%">
			<div id="tabs" height="100%" width="100%" align="center"
				style="margin-top: 5px; margin-left: 5px; margin-right: 5px;">
				<ul class="title">
					<li><a href="#tabs-1">节点设置</a>
					</li>
					<li><a href="#tabs-2">条件设置</a>
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
									<span id=tabpage1_1>条件设置</span>
								</div>
								<TABLE border=0 width="100%" height="100%"
									style="font-size: 9pt; overflow: auto;">
									<TR valign=top>
										<TD width=5></TD>
										<TD><a href='#' title='新建条件' onclick="newTransition()"><img
												src='<%=designRoot%>/images/workflow/tianjia.gif' border='0'
												align='absmiddle' />&nbsp;新建条件</a> <a href='#' title='删除条件'
											onclick="delTransition()"><img
												src='<%=designRoot%>/images/workflow/shanchu.gif' border='0'
												align='absmiddle' />&nbsp;删除条件</a> <a href='#' title='确定条件'
											onclick="addTransition()"><img
												src='<%=designRoot%>/images/workflow/queding.gif' border='0'
												align='absmiddle' />&nbsp;确定条件</a>
										</TD>
										<TD></TD>
									</TR>
									<TR valign=top>
										<TD width=5></TD>
										<TD><span id=tabpage2_7>条件选项</span>&nbsp; <select
											id="conditionItem" onchange="defaultInput(this)" class=txtput>
												<option value="请选择">请选择</option>
												<!-- 				<option value="[主表单.月收入]">主表单.月收入</option>
				<option value="[主表单.季度收入]">主表单.季度收入</option>
				<option value="[主表单.年收入]">主表单.年收入</option> -->
												<option value="[当前处理人]">当前处理人</option>
												<option value="[发起人]">发起人</option>
										</select> &nbsp;&nbsp; <span id=tabpage2_7>路由名称</span>&nbsp; <input
											type="text" id="transitionName" class=txtput>
										</TD>
										<TD></TD>
									</TR>

									<TR valign=top>
										<TD width=5></TD>
										<TD>
											<table border="0" id="logicTable" width="95%">
												<tr>
													<td width="40%"><input id="firstDecision" type="text" style="width: 95%"
														onfocus="focusInput(this)" class=txtput>
													</td>
													<td width="10%"><select class=txtput>
															<option value="small">&lt;</option>
															<option value="smallE">&lt;=</option>
															<option value="big">&gt;</option>
															<option value="bigE">&gt;=</option>
															<option value="NumEqu">数值相等</option>
															<option value="NumNEqu">数值不等</option>
															<option value="StrEqu">字符相等</option>
															<option value="StrNEqu">字符不等</option>
															<option value="isNull">为空</option>
															<option value="notNull">不为空</option>
															<option value="isGroupOf">岗位为</option>
													</select>
													</td>
													<td width="20%"><input type="text" style="width: 95%"
														class=txtput>
													</td>
													<td width="10%"><select class=txtput>
															<option value="并且">并且</option>
															<option value="或者">或者</option>
													</select>
													</td>
													<td width="20%"><a href='#' title='增加'
														onclick='addNewTR()'><img
															src='<%=designRoot%>/images/workflow/drop-add.gif'
															border='0' align='absmiddle' /></a><a href='#'
														title='删除' onclick='delTR(this)'><img
															src='<%=designRoot%>/images/workflow/delete.gif'
															border='0' align='absmiddle' /></a>
													</td>
												</tr>
											</table>
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
									<span id=tabpage1_1>路由条件</span>
								</div>
								<TABLE border=0 width="100%" height="100%"
									style="font-size: 9pt;">
									<TR valign=top>
										<TD width=5></TD>
										<TD width="98%"><select name="logicSets" size="5"
											style="width: 98%" ondblclick="initEditTransition(this)"
											class=txtput>
										</select> <br /><br /> 默认选择路由&nbsp;&nbsp; <input type="text"
											name="elseSelect" id="elseSelect" class=txtput>
											<div><font color="red">默认选择路由为迁移线名称，当所有条件都不满足时则自动选择默认路由流转。</font></div>
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

						<%-- add by caidw--%>
						<TR valign=top>
							<TD></TD>
							<TD width="100%" valign=top>
								<div class="tit1">
									<span id=tabpage1_1>条件代理设置</span>
								</div>
								<TABLE border=0 width="100%" height="100%"
									style="font-size: 9pt;">
									<TR valign=top>
										<TD width=5></TD>
										<TD width="98%"><span id=tabpage2_8>条件代理</span>&nbsp;&nbsp;
											<input type="text" event-attr="event-name"
											name="decide-handle-name" class="txtput" readonly="readonly" />
											<input type="button" class="select-event" value="选择" /> <input
											type="button" class="clear-event" value="清除" /><br /> <input
											type="text" name="decide-handle-class"
											event-attr="event-class" class="txtput" style="margin-left:57px;width:330px;"
											readonly="readonly" />
											<div style="margin-left: 57px;"><font color="red">若设置了条件代理，则条件节点优先根据代理类进行条件判断。</font></div>
										</TD>
										<TD></TD>
									</TR>
								</TABLE></TD>
							<TD>&nbsp;</TD>
						</TR>
						<%-- the end --%>
						
					</TABLE>
				</div>
				<!-- Tab Page 2 Content End -->
				<!-- 业务扩展页面 -->
				<jsp:include page="ext/flow_decision_ext.jsp" />
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
