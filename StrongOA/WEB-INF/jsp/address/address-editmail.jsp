<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp" %>    
<html>
<head>
<title>输入</title>
<%@include file="/common/include/meta.jsp" %>
<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>		
	<script type="text/javascript">
		$(document).ready(function(){
			var parentWindow = window.dialogArguments
			var mailList = parentWindow.document.getElementById("mailList");
			$("#newMail").val(mailList.options[mailList.selectedIndex].text);
			$("#btnOK").click(function(){
				var email = $("#newMail").val();
				var reg=/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/gi;
				
				if($.trim(email) != ""){
					if(reg.test(email)){
						//mailList.options[mailList.selectedIndex].value=email;
						mailList.options[mailList.selectedIndex].text =email;
						window.close();
					}else{
						alert("您输入的不是有效的Email地址，请正确填写。");
						$("#newMail").get(0).focus();
						return false;
					}
				}else{
					window.returnValue = "";
					window.close();
				}
			});
			$("#btnCancel").click(function(){window.returnValue = "";window.close();});
		});
	</script>	
</head>
<body>
	<div align="center" style="height:100%;width:100%;overflow: auto;">
	<br>
				<span class="wz" >请输入有效的E-mail地址：</span><br><br>
					<input type="text" id="newMail" style="width: 55%;"/><br><br>
					<input type="button" id="btnOK"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"/>&nbsp;&nbsp;
					<input type="button" id="btnCancel"  class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  取消"/>
	</div>
</body>
</html>