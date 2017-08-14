<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>    
<html>
<head>
<title>更改映射</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>		
	<script type="text/javascript">
		$(document).ready(function(){
			var parentWindow = window.dialogArguments
			var plaintext = parentWindow.document.getElementById("plaintext");
			$("#column").text(plaintext.value+"：");
			$("#btnOK").click(function(){
				var sel_column = $("#sel_column").get(0);
				window.returnValue = sel_column.options[sel_column.selectedIndex].text;
				window.close();
			});
			$("#btnCancel").click(function(){window.returnValue = "";window.close();});
		});
	</script>	
</head>
<body>
	
	<br>
				&nbsp;&nbsp;&nbsp;&nbsp;<span class="wz" >选择文本字段对应的通讯录字段：</span><br>
				&nbsp;&nbsp;&nbsp;&nbsp;<span id="column" class="wz" ></span>
				<br>&nbsp;&nbsp;&nbsp;&nbsp;
					<select id="sel_column" style="width: 80%;">
						<option>姓名</option>
						<option>职务</option>
						<option>电子邮件</option>
						<option>手机1</option>
						<option>手机2</option>
						<option>性别</option>
						<option>生日</option>
						<option>QQ</option>
						<option>MSN</option>
						<option>主页</option>
						<option>爱好</option>
						<option>国家</option>
						<option>家庭电话1</option>
						<option>家庭电话2</option>
						<option>省</option>
						<option>城市</option>
						<option>传真</option>
						<option>家庭地址</option>
						<option>公司</option>
						<option>职位</option>
						<option>部门</option>
						<option>公司电话1</option>
						<option>公司电话2</option>
						<option>邮编</option>
						<option>附注信息</option>
					</select><br><br>
	<div align="right">
					<input type="button" id="btnOK"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"/>&nbsp;&nbsp;
					<input type="button" id="btnCancel"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  取消"/>&nbsp;&nbsp;
	</div>
</body>
</html>