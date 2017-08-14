<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongmvc.exception.SystemException"/>
<%@ taglib uri="/tags/web-bigtree" prefix="tree"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	List data = (List)request.getAttribute("data");
	if(data == null){
		throw new SystemException("数据为空！");
	}
%>
<html>
  <head>
  		<title>选择用户组</title>
  		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows.css">
		<LINK href="<%=path%>/common/css/tree.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<SCRIPT src="<%=root%>/common/bigtree/js/tree.js"></SCRIPT>
		<script type="text/javascript">
			var treeObj = null;
			//初始化
			//解决bug-2593  yanjian 2011-11-04
			function init(o){
				//var aHasSelected = "<%=request.getParameter("initData") != null ? String.valueOf(request.getParameter("initData")) : ""%>";
				
				var aHasSelected="";
				var initDatas;
				if(typeof(window.parent.dialogArguments.getInitData) == "undefined"){
					initDatas = [];
				}else{
					initDatas = window.parent.dialogArguments.getInitData();
				}
				
				if(initDatas.length > 0){
					var initData;
					for(var j=0; j<initDatas.length; j++){
						if(initDatas[j].substring(0, 1) == 'h'){
							initData = initDatas[j].split(",");
							if(aHasSelected==""){
								aHasSelected += initData[0];
							}else{
								aHasSelected += ","+initData[0];
							}
						}
					}
				}
				
				if(aHasSelected != ""){
	                o.aHasSelected = ","+aHasSelected+",";   
				}	
				treeObj = 0;
			}
			
			//取消选项
			function unChecked(objvalue){
   				
 		  	}

			//伪CHECKBOX的单击事件.
			function chkclick(item){
				//var items = $("#bigTreeDiv").getTSNs();//得到所有选择的项
				var id = item.nodeImg;
				var name = item.text;
				if(item.checkstate == "0" ){//勾选 || item.checkstate == "2"
					parent.addOption("group", name, id);
				} else {
					parent.removeOption("group",name,id);
				}
			}
		</script>
  </head>
  <base target=_self>
  <body>
 	<DIV id=contentborder cellpadding="0">
  		<tree:strongbigtree title="用户组" data="<%=data %>" dealclass="com.strongit.oa.common.tree.TreeImpl"
  				oncheckboxclick="chkclick" check="true" parentCascadecheck="false" cascadecheck="false"/>
	</DIV>

  </body>
</html>
