<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<%@include file="/common/include/rootPath.jsp"%>
	<%@include file="/common/OfficeControl/version.jsp"%>
		<title>导航区域</title>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script type="text/javascript">
		$(document).ready(function() {
				var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
			  		try{
						TANGER_OCX_OBJ.CreateNew("Word.Document");   
					}catch(e){
					}
			parent.focus();
		});
	</script>
	
	</head>
	
	<body>
		 <div>
			<script type="text/javascript">
											document.write(OfficeTabContent);
										</script>
				</div>
	
	</body>
</html>
