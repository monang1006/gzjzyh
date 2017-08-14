<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath_cb.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="web" uri="/tags-web/component"%>
<html>
	<head>
		<title>信息处理</title>

		<%@include file="/common/include/meta.jsp"%>
		
	</head>

	<body>
			<div class="main_up_out">

				<div id="gridDiv" class="grid" style="width:auto;height:460px; display:block; padding: 0;overflow:hidden;overflow-y:hidden">
				<iframe width="100%" src="<%=root%>/xxbs/action/infoReport!officeStream.action?toId=${toId}"
				 height="460px" marginwidth="20px"
				 marginheight="50px" frameborder="0" scrolling="yes"></iframe>
				</div>
				</div>



	</body>
<script type="text/javascript">
$(function(){
	$.ajax({
		url: "<%=root%>/xxbs/action/infoReport!officeStream.action?toId=${toId}",
		success: function(data){
			//$("#gridDiv").html(data);
		},
		cache: false,
		dataType: 'html'
	});

});
</script>
</html>