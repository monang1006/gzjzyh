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
		<script language=jscript src="js/pagescript/flow_tasknode.js"></script>
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
	<table border="0" cellpadding="0" cellspacing="0" height="485px"
		width="100%">
		<tr>
			<td id="contentscell" valign="top" align="center" height="100%"
				width="100%">
				<div id="tabs" height="100%" width="100%" align="center"
					style="margin-top: 5px; margin-left: 5px; margin-right: 5px;">
					<ul class="title">
						<li><a href="#tabs-1">节点设置</a></li>
						<li><a href="#tabs-2">任务设置</a></li>
						<li><a href="#tabs-3">定时设置</a></li>
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
											<TD width="50%"><span id=tabpage1_2>节点编号</span>&nbsp;&nbsp;
												<INPUT TYPE="text" NAME="nodeId" value="" class=txtput
												disabled>
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
											</script></TD>
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
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage2_1>表单设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>挂接表单</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="togetherform" value="" class=txtput
												disabled> &nbsp;&nbsp;&nbsp; <a id="setform"
													href="#" onclick="setForms()">选择表单</a> <INPUT TYPE="hidden"
													NAME="formid" value=""><INPUT TYPE="hidden"
														NAME="formpriv" value="superadmin">
											</TD>

											<TD></TD>
										</TR>

										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>

									<div class="tit1">
										<span id=tabpage2_1>节点事件动作设置</span>
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
										<span id=tabpage2_1>路由设置</span>
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
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>离开节点时是否并发</span>&nbsp;&nbsp; <script>
												if (currentStatus == "edit") {
													document
															.write("<input type=\"radio\" name=\"nextTranType\" value=\"0\" disabled></input>否&nbsp;&nbsp;<input type=\"radio\" name=\"nextTranType\" value=\"1\" disabled>是");
												} else {
													document
															.write("<input type=\"radio\" name=\"nextTranType\" value=\"0\"></input>否&nbsp;&nbsp;<input type=\"radio\" name=\"nextTranType\" value=\"1\">是");
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
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

						
						</TABLE>
					</div>
					<div id="tabs-2" class="tab_content">
						<TABLE border=0 width="100%" height="100%">
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>

									<div class="tit1">
										<span id=tabpage2_1>任务信息</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>任务名称</span>&nbsp;&nbsp; <script>
												if (currentStatus == "edit") {
													document
															.write("<INPUT TYPE=\"text\" NAME=\"taskname\" value=\"\" class=txtput readOnly=\"true\">");
												} else {
													document
															.write("<INPUT TYPE=\"text\" NAME=\"taskname\" value=\"\" class=txtput>");
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
							
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>

									<div class="tit1">
										<span id=tabpage2_1>任务处理人设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>动态指定处理人</span>&nbsp;<input
												type="checkbox" name="isActiveactor" value="1"
												onclick="activeActor()">
												&nbsp;&nbsp;&nbsp;
												<span id=tabpage2_7>允许选择其他人</span>&nbsp;<input
												type="checkbox" name="canSelectOther" value="1">
												&nbsp;&nbsp;&nbsp; 
												<span id=tabpage2_7>最大参与人数</span>&nbsp;&nbsp; <input
												type="text" id="maxActors" name="maxActors" class=txtput>
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD>
												<table>
													<tr>
														<td><span id=tabpage2_7>处理人员</span></td><td><select
															name="realActors" id="realActors" size="3"
															style="width: 200px;"></select></td>
														<td><INPUT TYPE="hidden" NAME="handleactor"
															class=txtput disabled><input type="button"
																value="选择" onclick="setTaskActors('task')">
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
										<span id=tabpage2_1>会签设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>是否会签</span>&nbsp;<script>
												if (currentStatus == "edit") {
													document
															.write("<INPUT TYPE=\"checkbox\" NAME=\"iscountersign\" value=\"\" disabled>");
												} else {
													document
															.write("<INPUT TYPE=\"checkbox\" NAME=\"iscountersign\" value=\"\" onclick=\"counterSign();\">");
												}
											</script>
											
											<span id=tabpage2_2>是否修改文档</span>&nbsp;<script>
												if (currentStatus == "edit") {
													document
															.write("<INPUT TYPE=\"checkbox\" NAME=\"isDocEdit\" value=\"\" disabled>");
												} else {
													document
															.write("<INPUT TYPE=\"checkbox\" NAME=\"isDocEdit\" value=\"\">");
												}
											</script>
											&nbsp;&nbsp;&nbsp;
											<span id=tabpage2_2>会签类型</span>&nbsp;<script>
												if (currentStatus == "edit") {
													document
															.write("<input type=\"radio\" name=\"counterSignType\" value=\"0\" disabled>串行会签</input><input type=\"radio\" name=\"counterSignType\" value=\"1\" disabled>并行会签</input>");
												} else {
													document
															.write("<input type=\"radio\" name=\"counterSignType\" value=\"0\">串行会签</input><input type=\"radio\" name=\"counterSignType\" value=\"1\">并行会签</input>");
												}
											</script>
											
											</td>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_2>会签不通过时选择</span>&nbsp;&nbsp; 
											<%-- --%><INPUT TYPE="text" NAME="noApprove" value="" class=txtput disabled>
										
												<div><font color="red">此处值为迁移线名称，流程运行过程中选择该迁移线表示会签不通过，则所有未完成的会签任务会被强制结束；该值默认为”无“，表示不需要判断会签是否通过。</font></div>
											</TD>
											<TD></TD>
										</TR>
										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>
							
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage2_1>指派人员设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>允许指派</span>&nbsp;<input
												type="checkbox" name="reAssign" value="1"
												onclick="reAssign()">
												</TD>
											<TD></TD>
										</TR>
										
										<TR valign=top>
											<TD width=5></TD>
											<TD>
												<span id=tabpage2_7>允许进一步指派</span>&nbsp;<input
												type="checkbox" name="reAssignmore" value="1">
												<div><font color="red">进一步指派：被指派人可以将别人指派给自己的任务再次指派给其他人。</font></div>
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD>
												<table>
													<tr>
														<td><span id=tabpage2_7>指派人员</span></td>
														<td>
														<select name="reassignSelect" id="reassignSelect"
															size="3" style="width: 200px;"></select>
														</td>
														<td><input type="hidden" name="reassignActor">
														<input type="button" value="选择" name="reassignChoose"
																class="button" onclick="setTaskActors('reassign')">
														</td>
													</tr>
												</table></TD>
											<TD></TD>
										</TR>
									
										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>

									<div class="tit1">
										<span id=tabpage2_1>任务事件动作设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD></TD>
											<TD><span id=tabpage2_3>任务开始</span>&nbsp;&nbsp; <input
												type="text" event-attr="event-name" name="task-start-name"
												class="txtput" readonly="readonly" /> <input type="button"
												class="select-event" value="选择" /> <input type="button"
												class="clear-event" value="清除" /> <br /> <input
												type="text" name="taskstartaction" event-attr="event-class"
												class="txtput" style="margin-left: 57px; width: 330px"
												readonly="readonly" /> <br /> <span
												style="margin-left: 57px;">邮件提醒</span> <INPUT
												TYPE="checkbox" NAME="taskStartMes" value="">
													&nbsp;&nbsp;通知提醒 <INPUT TYPE="checkbox"
													NAME="taskStartMail" value="">
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD></TD>
											<TD><span id=tabpage2_4>任务结束</span>&nbsp;&nbsp; <input
												type="text" event-attr="event-name" name="task-end-name"
												class="txtput" readonly="readonly" /> <input type="button"
												class="select-event" value="选择" /> <input type="button"
												class="clear-event" value="清除" /> <br /> <input
												type="text" name="taskendaction" event-attr="event-class"
												class="txtput" style="margin-left: 57px; width: 330px"
												readonly="readonly" /> <br /> <span
												style="margin-left: 57px;">邮件提醒</span> <INPUT
												TYPE="checkbox" NAME="taskEndMes" value="">
													&nbsp;&nbsp;通知提醒 <INPUT TYPE="checkbox" NAME="taskEndMail"
													value="">
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

							<TR valign=top style="display: none">
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage2_1></span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>挂接动作</span>&nbsp;&nbsp; <INPUT
												TYPE="checkbox" NAME="isSetAction" value=""
												onclick="isSetAction();"> &nbsp;&nbsp;&nbsp; 
											</TD>
											<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD><span id=tabpage2_7>设置动作</span>&nbsp;&nbsp; <INPUT
												TYPE="text" NAME="actionSet" value="" class=txtput disabled>
													&nbsp;&nbsp;&nbsp; 
											</TD>
											<TD></TD>
										</TR>

										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							
						</TABLE>
					</div>
					<div id="tabs-3" height="100%" width="100%">
						<TABLE border=0 width="100%" height="100%">

							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage2_1>定时器设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD colspan="2"><span id=tabpage2_7>启用定时催办 <script>
												if (currentStatus == "edit") {
													document
															.write("<input type=\"checkbox\" name=\"doc_isTimer\" value=\"1\" disabled>");
												} else {
													document
															.write("<input type=\"checkbox\" name=\"doc_isTimer\" value=\"1\" onclick=\"setIsTimer()\">");
												}
											</script>
													<TD></TD>
										</TR>
										<TR valign=top>
											<TD width=5></TD>
											<TD valign="middle"><span id=tabpage2_7>任务持续时间</span>
											</TD>
											<TD align="left"><input type="text"
												name="doc_timer_data" style="width: 40"> &nbsp; <select
													name="doc_timer_init">
														<option value="day">天</option>
														<option value="hour">小时</option>
														<option value="minute">分钟</option>
												</select>
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD valign="middle"><span id=tabpage2_7>第一次催办</span>
											</TD>
											<TD align="left"><input type="text"
												name="pre_timer_data" style="width: 40"> &nbsp; <select
													name="pre_timer_init">
														<option value="day">天</option>
														<option value="hour">小时</option>
														<option value="minute">分钟</option>
												</select> &nbsp;持续时间到达前 
											</TD>
											<TD></TD>
										</TR>

										<TR valign=top>
											<TD width=5></TD>
											<TD valign="middle"><span id=tabpage2_7>重复催办间隔</span>
											</TD>
											<TD align="left"><input type="text" name="re_timer_data"
												style="width: 40"> &nbsp; <select
													name="re_timer_init">
														<option value="day">天</option>
														<option value="hour">小时</option>
														<option value="minute">分钟</option>
												</select>
											</TD>
											<TD></TD>
										</TR>

										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage2_1>催办方式设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD colspan="2"><span id=tabpage2_7>邮件催办<input
													type="checkbox" name="mail_timer" value="1">
											</span> &nbsp; <span id=tabpage2_7>通知催办<input type="checkbox"
													name="notice_timer" value="1">
											</span> &nbsp; <span id=tabpage2_7>短信息催办<input
													type="checkbox" name="mes_timer" value="1">
											</span> &nbsp; <span id=tabpage2_7>RTX催办<input
													type="checkbox" name="rtx_timer" value="1">
											</span>
												<TD></TD>
										</TR>

										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage2_1>催办人员设置</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD colspan="2"><span id=tabpage2_7>通知办理人<input
													type="checkbox" name="handler_notice" value="1">
											</span> &nbsp; <span id=tabpage2_7>通知发起人<input
													type="checkbox" name="start_notice" value="1">
											</span> &nbsp; <span id=tabpage2_7>通知流程管理员<input
													type="checkbox" name="owner_notice" value="1">
											</span>
												<TD></TD>
										</TR>

										<TR height="3">
											<TD></TD>
											<TD></TD>
											<TD></TD>
											<TD></TD>
										</TR>
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>

							
						</TABLE>
					</div>
					<!-- 业务扩展页面 -->
					<jsp:include page="ext/flow_tasknode_ext.jsp" />
				</div></td>
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