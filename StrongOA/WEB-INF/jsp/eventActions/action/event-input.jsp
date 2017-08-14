<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>事件动作</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
		<script src="<%=path%>/common/js/common/transform.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				oldName = $("#eaName").val();
			});
			function submitForm() {
				if ($.trim(document.getElementById("eaName").value) == '') {
					alert('事件动作名称不能为空。');
					document.getElementById("eaName").focus();
					return false;
				}
				if($("#eatId").val() == ""){
					alert("所属类型不能为空。");
					return false;
				}
				if($("#eaClass").val() == ""){
					alert("动作类路径不能为空。");
					return false;
				}
				if (document.getElementById("eaDetail").value.length > 200) {
					alert("事件动作描述字数不能大于200。");
					return false;
				}
				 //对输入框内容进行特殊字符转换
				 var eaName = document.getElementById("eaName").value;
				 document.getElementById("eaName").value = transForSpecialSign(eaName);
				 var eaDetail = document.getElementById("eaDetail").value;
				 document.getElementById("eaDetail").value = transForSpecialSign(eaDetail);
				 var eaClass = document.getElementById("eaClass").value;
				 var urlReg = /^[\w-]+(\.[\w-]+)*$/;
				if (!urlReg.test(eaClass)) {
					alert("您输入的类路径格式不正确。");
					return false;
				}
				 
				if(oldName != $("#eaName").val()){
					$.ajax({
						url:scriptroot + "/eventActions/action/event!checkEventByName.action",
						type:"post",
						dataType:"text",
						data:"eaName=" + $("#eaName").val(),
						success:function(msg){
							if(msg == ""){
			                 	document.forms[0].submit();
			                 }else{
			                 	alert(msg);
			                 }
						}
					});
				}else{
					document.forms[0].submit();
				}
				//document.forms[0].submit();
			}
		
			function cancel() {
				window.close();
			}
		</script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;">
		<div id="contentborder" align="center">
			<form id="meetform"
				action="<%=root%>/eventActions/action/event!save.action"
				method="POST">
				<input type="hidden" id="eaId" name="eaId" value="${model.eaId}" />
				<table width="100%"
					style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
					<tr>
						<td class="table_headtd_img">
							<img src="<%=frameroot%>/images/ico/ico.gif">
						</td>
						<%--<td align="left">
							<strong>新建事件动作</strong>
						</td>
						
						--%><td align="left">
								<script>
											var id = "${model.eaId}";
											if(id==null|id==""){
												window.document.write("<strong>新建事件动作</strong>");
											}else{
												window.document.write("<strong>编辑事件动作</strong>");
											}
											</script>
							</td>
						
						<td align="right">
							<table border="0" align="right" cellpadding="00" cellspacing="0">
				                <tr>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
				                 	<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
			                  		<td width="5"></td>
				                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
				                 	<td class="Operation_input1" onclick="cancel();">&nbsp;返&nbsp;回&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
			                  		<td width="6"></td>
				                </tr>
				            </table>
						</td>
					</tr>
				</table>
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" style="padding-left: 50px;" >
					<tr>
						<td  class="biao_bg1" align="right" style="padding-left:60px;">
							<span class="wz"><font color=red>*</font>&nbsp;事件动作名称：&nbsp;</span>
						</td>
						<td class="td1" style="padding-left:5px;">
							<input id="eaName" name="eaName" type="text" maxLength="50" style="width:340px;"
								value="${model.eaName }">
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right"  style="padding-left: 60px;">
							<span class="wz"><font color=red>*</font>&nbsp;所属类型：&nbsp;</span>
						</td>
						<td class="td1" style="padding-left:5px;" >
							<s:select list="types" cssStyle="width:200px;border:1px solid #b6b6b6"
							listKey="eatId" listValue="eatName"
							headerKey="" headerValue="" id="eatId"
							name="TWfInfoEventActionType.eatId" value="model.TWfInfoEventActionType.eatId" cssStyle="width:24.3em"/>
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1" align="right"  style="padding-left: 60px;">
							<span class="wz"><font color=red>*</font>&nbsp;动作类路径：&nbsp;</span>
						</td>
						<td class="td1" style="padding-left:5px;">
							<input id="eaClass" name="eaClass" type="text" maxLength="200" style="width:340px;"
								value="${model.eaClass}">
						</td>
					</tr>
					<tr>
						<td  class="biao_bg1" align="right" valign="top"  style="padding-left: 60px;">
							<span class="wz">事件动作描述：&nbsp;</span>
						</td>
						<td class="td1" style="padding-left:5px;">
							<textarea rows="10" cols="40" id="eaDetail"
								name="model.eaDetail" style="overflow: auto">${model.eaDetail}</textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>