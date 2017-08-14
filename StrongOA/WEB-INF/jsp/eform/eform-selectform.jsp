<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>机构挂接表单</title>
		<%@include file="/common/include/rootPath.jsp"%>
		<script type="text/javascript">
			//向选择框中添加
			function addOpt(strName,strValue){
				if(!hasSelect(strName,strValue)){
					var objSelect = document.getElementById("sel_accept");
					var  objOption  =  new  Option(strName,strValue);  
		      		objSelect.options.add(objOption, objSelect.options.length);
				}
			}
			//判断是否已经选择
			function hasSelect(strName,strValue){
				var isExist = false;
				var objSelect = document.getElementById("sel_accept");
				for(var i=0;i<objSelect.options.length;i++){
					if(strName == objSelect.options[i].text && objSelect.options[i].value == strValue){
						isExist = true;
						break;
					}
				}
				return isExist;
			}
			//取消选中人员
			function removeOpt(id){
				var objSelect = document.getElementById("sel_accept");
				for(var i=0;i<objSelect.options.length;i++){
					if(id == objSelect.options[i].value){
						objSelect.removeChild(objSelect.options[i]);
						break;
					}
				}
			}
			//初始化SELECT
			function initSelect(id){
				if(id == "" || id == ","){
					return ;
				}
				for(var i=0;i<id.length;i++){
					addOpt(id[i],id[i]);
				}
			}	
		</script>
	</head>
<body>
	<select id="sel_accept" style="display: none;"></select>
	<iframe width="100%" height="100%" src="<%=root%>/common/eform/eForm.action?orgId=<%=request.getParameter("orgId") %>"></iframe>
</body>
</html>