<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<html>
	<head>
		<title>个人办理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="kiben" content="no-cache">


		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'>
</script>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'>
</script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js">
</script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js">
</script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js">
</script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js">
</script>
		<DIV id=contentborder align=center>
			<s:form action="/workinspect/worktodo/workTodo!list.action" method="POST" theme="simple" id="myTableForm">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
													&nbsp;
												</td>
												<td width="20%" align="left">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">
													&nbsp; 个人办理
												</td>
												<td width="80%">
													<s:if test="module==\"orgType\"">
													</s:if>
													<s:else>
														<table border="0" align="right" cellpadding="00"
															cellspacing="0">
															<tr>
																<td>
																	<table border="0" align="right" cellpadding="00"
																		cellspacing="0">
																		<tr>
																			<td width="*">
																				&nbsp;
																			</td>
																			<td align="right">
																				<a class="Operation" href="javascript:viewPost();"><img
																							src="<%=frameroot%>/images/shanchu.gif"
																							width="15" height="15" class="img_s">工作评语</a>
																			</td>
																			<td width="3"></td>

																			<td align="right">
																				<a class="Operation"
																					href="javascript:history.go(-1);"> <img
																						src="<%=root%>/images/ico/cexiao.gif" width="15"
																						height="15" class="img_s">返回&nbsp;</a>
																			</td>
																			<td width="3"></td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</s:else>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="364px"
								wholeCss="table1" property="sendtaskId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}" ondblclick="view(this.value);">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=frameroot%>/images/sousuo.gif" id="img_sousuo"
												width="17" height="16" style="cursor: hand;" title="单击搜索" >
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:textfield id="selectTaskTitle"
												name="selectTaskTitle"
												cssClass="search" title="标题" maxlength="50"></s:textfield>
										</td>
										<td width="16%" align="center" class="biao_bg1">
											<s:textfield id="selectTaskUser"
												name="selectTaskUser" cssClass="search"
												title="任务来源" maxlength="50"></s:textfield>
										</td>
										<td width="30%" align="left" class="biao_bg1">
											<strong:newdate name="selectTaskSendTime"
												id="selectTaskSendTime" width="120" skin="whyGreen" isicon="true"
												 dateform="yyyy-MM-dd" title="发送时间"></strong:newdate>
											&nbsp;&nbsp;至&nbsp;&nbsp;
											<strong:newdate name="selectTaskSendTime2"
												id="selectTaskSendTime2" width="120" skin="whyGreen"
												isicon="true" dateform="yyyy-MM-dd" title="发送时间"></strong:newdate>
										</td>
										<td width="10%" align="left" class="biao_bg1">
											<s:select name="selectTaskState" list="typeMap" headerKey=""
												headerValue="请选择" cssClass="search" title="状态"></s:select>
										</td>
										<td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>

                               
								<webflex:flexCheckBoxCol caption="选择" property="sendtaskId"
									showValue="sendtaskId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<%--<webflex:flexTextCol caption="#" property="caruser" showsize="5"
									showValue="caruser" isCanDrag="true" width="5%"
									isCanSort="true"></webflex:flexTextCol>
						      	--%><webflex:flexTextCol caption="文件标题" property="worktaskTitle"
									showValue="TOsWorktask.worktaskTitle" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="办理状态" mapobj="${typeMap}"
									property="taskState" showValue="taskState" width="18%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="工作来源" property="worktaskSender"
									showsize="2000" showValue="TOsWorktask.worktaskSender" isCanDrag="true"
									width="20%" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="到达时间" property="taskRecvTime"
									showsize="16" dateFormat="yyyy-MM-dd"
									showValue="taskRecvTime" isCanDrag="true" width="15%"
									isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="办理期限" property="taskcompleteTime"
									showsize="16" dateFormat="yyyy-MM-dd"
									showValue="taskcompleteTime" isCanDrag="true" width="15%"
									isCanSort="true"></webflex:flexDateCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
				<br />
				<br />
				<table width="100%">
					<tr>
						<td align="left">
							<font size="2">状态说明： <br> 1、<font color="red">●</font>-->已经超过办理期限的工作<br>
								2、<font color="blue">●</font>-->当前日期为办理期限最后一天<br> 3、<font
								color="green">●</font>-->在办理期限之内的工作<br> 4、●-->办结件<br>
							</font>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
<script language="javascript">
function view(val){//双击查看办理纪要
	window.showModalDialog('<%=path%>/workinspect/worktodo/workTodo!view.action?sendtaskId='+val,window,'help:no;status:no;scroll:no;dialogWidth:750px; dialogHeight:570px');
}
function viewPost() {
	var id = getValue();
	if (id == null || id == "") {
		alert("请选择需要查看的记录！");
		return;
	}
	var ifind=id.indexOf(",");
	if (ifind!=-1) {
		alert("只能查看一条记录!");
		return;
	}
	window.showModalDialog('<%=path%>/workinspect/worktodo/workTodo!viewDev.action?sendtaskId='+id,window,'help:no;status:no;scroll:no;dialogWidth:420px; dialogHeight:280px');
}
$(document).ready(function() {
	$("#img_sousuo").click(function() {
		$("form").submit();
	});
});
</script>
	</BODY>
</HTML>
