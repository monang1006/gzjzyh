<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>事件动作类型</title>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
<script type="text/javascript" src="<%=jsroot%>/common/jquery.js"></script>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/transform.js" type="text/javascript"></script>
		
<script type="text/javascript">

$(document).ready(function(){
	
    $("#eatName").keypress(
        function(event){
            if(event.keyCode == 13){
            	//alert();
                return false;
            }
        }
    );
});

	$(function(){
		oldName = $("#eatName").val();
		
	});
	function submitForm() {
		 var eatName = document.getElementById("eatName").value;
		 var eatDetail = document.getElementById("eatDetail").value;
		if ($.trim(eatName) == '') {
			alert('类型名称不能为空。');
			document.getElementById("eatName").focus();
			return false;
		}
		if (document.getElementById("eatDetail").value.length > 200) {
			alert("类型描述字数不能大于200。");
			return false;
		}
		//对输入框内容进行特殊字符转换
		document.getElementById("eatName").value = transForSpecialSign(eatName);
		document.getElementById("eatDetail").value = transForSpecialSign(eatDetail);
		 
		if(oldName != eatName){
			$.ajax({
				url:scriptroot + "/eventActions/action/eventType!checkEventTypeByName.action",
				type:"post",
				dataType:"text",
				data:"eatName=" + eatName,
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
	<body class="contentbodymargin" >
		<div id="contentborder" align="center">
			<form id="meetform"
				action="<%=root%>/eventActions/action/eventType!save.action"
				method="POST" onsubmit="return false">
				<input type="hidden" id="eatId" name="eatId" value="${model.eatId }" />
				
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<script>
											var id = "${model.eatId}";
											if(id==null|id==""){
												window.document.write("<strong>新建事件动作类型</strong>");
											}else{
												window.document.write("<strong>编辑事件动作类型</strong>");
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
				</td>
				</tr>
				<tr>
					<td>
					<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" align="center">
						<tr>
							<td class="biao_bg1" align="right" style="padding-left: 55px;">
								<span class="wz"><font color=red>*</font>&nbsp;类型名称：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="eatName" name="eatName" type="text" maxLength="50" size="50"
								value="${model.eatName }"/>
											 
								&nbsp;
							</td>
						</tr>
						<tr>
						<td class="biao_bg1" align="right" valign="top"  style="padding-left: 55px;">
							<span class="wz">类型描述：&nbsp;</span>
						</td>
						<td colspan="3" class="td1">&nbsp; 
							<textarea rows="10" cols="39" id="eatDetail" 
								name="model.eatDetail" style="overflow: auto">${model.eatDetail}</textarea>
						</td>
						</td>
					</tr>
							</table>
					</td>
				</tr>
				
			</table>
		</form>
		</DIV>
	</body>
</html>