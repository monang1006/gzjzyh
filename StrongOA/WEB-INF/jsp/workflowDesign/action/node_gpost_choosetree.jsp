<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/web-tree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>人员选择</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=path%>/common/css/treeview.css" type=text/css rel=stylesheet>
		<SCRIPT src="<%=root%>/common/js/mztree_check/mztreeview_check.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" type="text/javascript">
	  		$(document).ready(function(){
		  		/*
		  		var initData = "<%=request.getParameter("initData") != null ? String.valueOf(request.getParameter("initData")) : ""%>";
		  		initChecked(initData);
		  		*/
		  		 //解决bug-2593  yanjian 2011-11-04
			    var aHasSelected = "";
				if(typeof(window.parent.dialogArguments.getInitData) == "undefined"){
					initDatas = [];
				}else{
					initDatas = window.parent.dialogArguments.getInitData();
				}
				
				if(initDatas.length > 0){
					var initData;
					for(var j=0; j<initDatas.length; j++){
						if(initDatas[j].substring(0, 1) == 'g'){
							initData = initDatas[j].split(",");
							if(aHasSelected==""){
								aHasSelected += initData[0];
							}else{
								aHasSelected += ","+initData[0];
							}
						}
					}
				}
				  
				initChecked(aHasSelected);	
	  			$("input:checkbox").click(function(){
  					var info = $(this).val();
  					var infos = info.split(",");
  					var id=infos[0];
  					var name = infos[1];
	  				if(this.checked){
	  					parent.addOption("gpost", name, id);
	  				}else{
	  					parent.removeOption("gpost", name,id);
	  				}
	  			});
	  			
	  			function initChecked(objSelect) {
                   objSelect = objSelect == "" ? [] : objSelect.split(",");
                   $("input:checkbox").each(function(){
                   	var id = $(this).val();
                   	for(var i=0;i<objSelect.length;i++){
                   		if(id.indexOf(objSelect[i])!=-1){
                   			$(this).attr("checked",true);
                   		}
                   	}
                   });
                   
                 }
	  		});
	  		
	  		function unChecked(objvalue){
				$("input[value^='"+objvalue+"']").attr("checked",false);	   				
			}
	</script>
	</head>
	<body class="contentborder">
	<div id="contentborder" cellpadding="0">
		<tree:strongtree title="岗位"  check="true" dealclass="com.strongit.workflow.workflowDesign.util.PostTreeDeal" data="${list}" target="project_work_content"  />	
	</div>
	</body>
</html>