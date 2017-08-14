<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
	<script language="javascript">

function save() {
	var managetDemo = $("#managetDemo").val();
	if (managetDemo == null || managetDemo == "") {
		alert("请填写内容!");
		return;
	}
	var managetProgress = $("#managetProgress").val();
	if (managetProgress == null || managetProgress == "") {
		alert("请填写工作进度!");
		return;
	}
	//var managetState = $("#managetState").val();
	//if (managetState == null || managetState == "") {
	//	alert("请选择工作状态!");
	//	return;
	//} 
	$("#mytable").submit();
}
</script>


	
</script>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js">
</script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js">
</script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript">
</script>
		<base target="_self">

		<title></title>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
	</head>

	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<iframe id="attachDownLoad" src=''
				style="display: none; border: 4px solid #CCCCCC;"></iframe>
			<s:form id="mytable"
				action="/workinspect/worktodo/workTodo!SaveHandle.action"
				method="POST" theme="simple">
				<input type="hidden" name="summary.TOsWorktaskSend.sendtaskId"
					id="sendtaskId" value="${summary.TOsWorktaskSend.sendtaskId}" />
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">

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
										<span class="wz">内容</span>
									</td>
									<td class="td1" colspan="7" align="left">
										<textarea id="managetDemo" name="summary.managetDemo"
											value="${summary.managetDemo}" cols="60" rows="4">${summary.managetDemo}</textarea>
										<font color="red">*</font>
									</td>
								</tr>
								<tr>
									<td width="15%" height="40" class="biao_bg1" align="right">
										<span class="wz">工作进度</span>
									</td>
									<td class="td1" align="left">
										<input id="managetProgress" name="summary.managetProgress"
											type="text" value="${summary.managetProgress}">
										%
										<font color="red">*</font>
									</td>
									<td width="15%" height="40" class="biao_bg1" align="right">
										<span class="wz">工作状态</span>
									</td>
									<td class="td1" align="left">
										<s:radio list="#{'0':'完成','1':'处理中' }" id="managetState"
											name="summary.managetState" cssStyle="width:30px;"  value="0"/>
										<font color="red">*</font>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input type="file" id="file" name="file" size="60"
											class="multi" />
										<s:if test="attachList!=null&&attachList.size()>0">
											<s:iterator id="vo" value="attachList">
												<div id="${vo.attachId}">
													<a href="#" onclick="delAttach('${vo.attachId}');"
														style='cursor: hand;'>[删除]</a>
													<br>
												</div>
											</s:iterator>
										</s:if>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</td>
				</tr>
				</table>
				<br />
				<table width="90%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="30%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="10%">
										<input type="button" Class="input_bg"
											onclick="javascript:save()" value="提交">
									</td>
									<td width="10%">
										<input name="Submit2" type="button" class="input_bg"
											value="重写">
									</td>
									<td width="10%">
										<a class="Operation" href="javascript:window.self.close();">返回&nbsp;</a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>

	</body>
</html>