<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>

	<head>
		<base target="_self">
		<title>任务查看</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js">
</script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript">
</script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js">
</script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js">
</script>

		<script language="javascript">
function getDemoLen(demo) {
	var demoLen = demo.length;
	var demoLen2 = 0;
	for ( var i = 0; i < demoLen; i++) {
		if ((demo.charCodeAt(i) < 0) || (demo.charCodeAt(i) > 255))
			demoLen2 += 2;
		else
			demoLen2 += 1;
	}
	return demoLen2;
}
var numtest = /^\d+$/;
$(document).ready(
		function() {
			$("#btn_save").click(
					function() {
						$("#managetDemo").val($.trim($("#managetDemo").val()));
						if ($("#managetDemo").val() == "") {
							alert("请填写纪要内容!");
							$("#managetDemo").focus();
							return false;
						}
						if (getDemoLen($("#managetDemo").val()) > 1000) {
							alert("纪要内容不能超过500个汉字!");
							$("#managetDemo").focus();
							return false;
						}
						if ($("#managetProgress").val() == "") {
							alert("请填写任务进度!");
							$("#managetProgress").focus();
							return false;
						}
						if (!numtest.test($("#managetProgress").val())) {
							alert('任务进度必须为数字！');
							$("#managetProgress").focus();
							return false;
						}
						if ($("#managetProgress").val() > 100) {
							alert('任务进度输入值必须在0-100之间！');
							$("#managetProgress").focus();
							return false;
						}
						if ($(":radio:checked").length == 0) {
							alert("请选择任务状态!");
							$(":radio").focus();
							return false;
						}
						if (($(":radio:checked").val() == "1" && $(
								"#managetProgress").val() != "100")
								|| ($(":radio:checked").val() != "1" && $(
										"#managetProgress").val() == "100")) {
							alert("任务状态与任务进度不一致!");
							$(":radio").focus();
							return false;
						}
						$("#btn_save").attr( {
							'disabled' : true
						});
						$("#mytable").submit();
					});
		});

function getState() {
	if ($(":radio:checked").val() == "1") {
		$("#managetProgress").val("100");
	}
}

function delAttach(id){
		if(confirm("删除选定的附件，确定？")) { 
		 	if(id!=null&&id!=""){
			 	var delattIds = $("#delAttIds").val();
			 	delattIds += id+",";
			 	$("#summAtt").hide();
			 	$("#delAttIds").val(delattIds);
		 	}
		}
	}
</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onclick="showLi();" onload="showLi();">
		<DIV id=contentborder align=center>
			<iframe scr='' id='tempframe' name='tempframe' style='display: none'></iframe>
			<s:form id="mytable"
				action="/workinspect/worktodo/workTodo!SaveHandle.action"
				method="POST" theme="simple" enctype="multipart/form-data">
				<iframe id="hideFrame" name="hideFrame"
					style="width: 0; height: 0; display: none;"></iframe>
				<s:hidden id="summaryId" name="summary.summaryId"></s:hidden>
				<input type="hidden" id="sendtaskId"
					name="summary.TOsWorktaskSend.sendtaskId"
					value="${model.sendtaskId}" />
				<input type="hidden" name="yangyong" value="${yangyong}" />
				<input type="hidden" id="taskState" name="TOsWorktaskSend.taskState"
					value="${model.taskState}" />
				<input type="hidden" id="delAttIds" name="delAttIds" value="${delAttIds}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">

							<table width="100%" height="20%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">办理期限：</span>
									</td>
									<td class="td1" colspan="1" align="left">
										${model.TOsWorktask.worktaskStime} 至
										${model.TOsWorktask.worktaskEtime}
									</td>

									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">紧急程度：</span>
									</td>
									<td class="td1" colspan="1" align="left" width="33%">
										<script type="text/javascript">
var type = '${model.TOsWorktask.worktaskEmerlevel}';
var tName = "";
if (type == "0") {
	tName = "普通";
} else if (type == "1") {
	tName = "快速";
} else if (type == "2") {
	tName = "紧急";
}
document.write(tName);
</script>
									</td>
								</tr>

								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务编号：</span>
									</td>
									<td class="td1" colspan="1" align="left" width="33%">
										${model.TOsWorktask.worktaskNo}
									</td>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务分类：</span>
									</td>
									<td class="td1" colspan="1" align="left">
										${model.TOsWorktask.worktaskType}
									</td>
								</tr>

								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">发起人：</span>
									</td>
									<td class="td1" colspan="1" align="left">
										${model.TOsWorktask.worktaskUserName}
									</td>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">发起单位：</span>
									</td>
									<td class="td1" colspan="1" align="left">
										${model.TOsWorktask.worktaskUnitName}
									</td>
								</tr>

								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务内容：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.TOsWorktask.worktaskContent}
									</td>
								</tr>
								<tr>
									<td width="15%" height="40" class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<div>
											<s:if test="attachList!=null&&attachList.size()>0">
												<s:iterator id="vo" value="attachList">
													<div id="${vo.attachId}">
														<a
															href="<%=root%>/workinspect/worktodo/workTodo!download.action?attachId=<s:property  value='attachId'/>"
															target="tempframe" style='cursor: hand;color:#03C;'>${vo.attachFileName}</a>
														<br>

													</div>
												</s:iterator>
											</s:if>
										</div>
									</td>
								</tr>
								<br>
								<tr>
									<td width="100%" height="40" class="biao_bg1" align="center"
										colspan="4">
										<span class="wz">办理纪要</span>
									</td>
								</tr>
								<s:if test="summaryList!=null&&summaryList.size()>0">
									<tr>
										<td width="16%" height="40" rowspan="2" class="biao_bg1"
											align="left">
											处理人:<span class="wz"> ${model.operateUserName}</span>
											<br>
											<br>
											进度:<span class="wz"> (${summary.managetProgress}%) </span>
											<br>
											<br/>
											日期:<span class="wz"><s:date name="summary.managetTime"
													format="yyyy-MM-dd " /> </span>
										</td>
										<td class="td1" colspan="3" align="left">
											${summary.managetDemo}
										</td>
									</tr>
									<tr>
										<td class="td1" colspan="3" align="left">
											<div style="OVERFLOW-y: auto; HEIGHT: 100px;">
												<s:if
													test="summary.attachList!=null&&summary.attachList.size()>0">
													<s:iterator id="attach" value="summary.attachList">
														<div id="${attach.attachId}">
															<a
																href="<%=root%>/workinspect/worktodo/workTodo!download.action?attachId=<s:property  value='attachId'/>"
																target="tempframe" style='cursor: hand;color:#03C;'>${attach.attachFileName}</a>
															<br>
														</div>
													</s:iterator>
												</s:if>
											</div>
										</td>
									</tr>
								</s:if>
							</table>
						</td>
					</tr>
				</table>
				<div id="div1" style="display: block;">
					<table width="100%" border="0" cellspacing="0" cellpadding="00"
						id="yinchuangone">
						<tr>
						<td width="30%" align="center">
								<input name="btn_showQiaoshou" type="button" class="input_bg"  id="qianshou"
									value="签收" onclick="javascript:siGn();" style${model.taskState == "0"?"1":"" }="display:none "> 
									&nbsp;
								<input name="btn_showSummary" type="button" class="input_bg"
									value="填写办理纪要" onclick="javascript:showSummary();" style${model.taskState == "0"?"":"1" }="display:none ">
									&nbsp;
								<input id="btn_close" type="button" class="input_bg" value="关闭"
									onclick="javascript:window.close();">
							</td>
						</tr>
					</table>
				</div>
				<div id="summary" style="display: none;">
					<table width="100%" height="20%" border="0" cellpadding="0"
						cellspacing="1" align="center" class="table1">
						<tr>
							<td width="100%" height="40" class="biao_bg1" align="center"
								colspan="4">
								<span class="wz">发表或回复意见</span>
							</td>
						</tr>
						<tr>
							<td width="15%" height="40" class="biao_bg1" align="right">
								<span class="wz">内容(<font color="red">*</font>)：</span>
							</td>
							<td class="td1" colspan="7" align="left">
								<textarea id="managetDemo" name="summary.managetDemo"
									value="${summary.managetDemo}" cols="60" rows="4">${summary.managetDemo}</textarea>

							</td>
						</tr>
						<tr>
							<td width="17%" height="40" class="biao_bg1" align="right">
								<span class="wz">任务进度(<font color="red">*</font>)：</span>
							</td>
							<td class="td1" align="left">
								<input id="managetProgress" name="summary.managetProgress"
									type="text" value="${summary.managetProgress}" maxlength="3">
								%

							</td>
							<td width="17%" height="40" class="biao_bg1" align="right">
								<span class="wz">任务状态(<font color="red">*</font>)：</span>
							</td>
							<td class="td1" align="left">
								<s:radio id="managetState" name="summary.managetState"
									list="#{'1':'完成' , '0':'处理中' }" listKey="key" listValue="value"
									onclick="getState();" cssStyle="width:30px;"   value="0"  />
							</td>
						</tr>
						<tr>
							<td width="15%" height="21" class="biao_bg1" align="right">
								<span class="wz">附件：</span>
							</td>
							<td class="td1" colspan="3" align="left">
								<input type="file" id="file" name="file" size="60" class="multi" />
								<s:if
									test="summary.attachList!=null&&summary.attachList.size()>0">
									<s:iterator id="voatt" value="summary.attachList">
										<div id="summAtt">
											<a href="#" onclick="delAttach('${voatt.attachId}');"
												style='cursor: hand;'>[删除]</a>
											<a
												href="<%=root%>/workinspect/worktodo/workTodo!download.action?attachId=<s:property  value='attachId'/>"
												target="tempframe" style='cursor: hand;color:#03C;'>${attach.attachFileName}</a>
											<br>

										</div>
									</s:iterator>
								</s:if>
							</td>
						</tr>
					</table>
					<table width="90%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td align="center" valign="middle">
								<table width="30%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="10%">
											<input id="btn_save" type="submit" class="input_bg"
												value="提 交"> 
										</td>
										<td width="10%">
											<input id="btn_reset" type="reset" class="input_bg"
												value="重 写">
										</td>
										<td width="10%">
											<input id="btn_close" type="button" class="input_bg"
												value="关闭" onclick="javascript:window.close();">
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
			</s:form>
		</DIV>
		<script>
function showLi() {
	var taskStates = document.getElementById("taskState").value;
	if (taskStates == "2") {
		document.getElementById("yinchuangone").style.display = "none";//隐藏
	} else {
		document.getElementById("yinchuangone").style.display = "block";//显示
	}
}

function showSummary() {
	$("#summary").css("display", "block");
	$("#div1").css("display", "none");
}
/**
 * 签收工作
 * @param 
 */
function siGn() {
	/**
	var id = getValue();
	if (id == null || id == "") {
		alert("请选择需要签收的记录！");
		return;
	}
	var ifind=id.indexOf(",");
	if (ifind!=-1) {
		alert("只能签收一条记录!");
		return;
	}
	*/
	var id = $("#sendtaskId").val();
	if (confirm("您确定要签收吗?")) {
		$.ajax({
			type : "post",
			url : scriptroot + "/workinspect/worktodo/workTodo!siGn2.action",
			data : "sendtaskId=" + id,
			async : false,
			success : function(data) {
				if (data == "1") {
					alert("签收成功！");
					window.returnValue = "OK";
					window.close();
				}else{
					alert("该任务已签收，请不要重复签收!");
					window.close();
					}
			}
		});	
	}

}
</script>
	</body>
</html>