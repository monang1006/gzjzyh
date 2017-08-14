<%@ page contentType="text/html; charset=utf-8" %>
<%@include file="/common/include/rootPath.jsp" %>
<HTML><HEAD><TITLE>表单域字段设置</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
			<script type="text/javascript">
				$(document).ready(function(){
					var params = window.dialogArguments;
					if(params.length > 1){//传入参数不为空
						document.getElementById("formId").value=params[0];
						document.getElementById("flagId").value=params[1];
						document.getElementById("permitReassigned").value=params[2];
						
					}
					$("form").submit();
				});
				
			</script>
</HEAD>
<base target="_self"/>
<BODY>
<DIV id=contentborder align=center>
请稍后,正在加载...
<form action="<%=root %>/common/eform/eForm!fieldSet.action" method="post">
	<input type="hidden" name="formId" id="formId"/>
	<input type="hidden" name="flagId" id="flagId"/>
	<input type="hidden" name="permitReassigned" id="permitReassigned"/>
</form>
</DIV>
</BODY></HTML>

