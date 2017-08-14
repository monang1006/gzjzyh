<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程展示树</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/dtree1.css" type="text/css"
			rel="stylesheet">
		<script language="javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	</head>
	<script language="javascript" type="text/javascript">
		var idsArray = new Array();
	
	    function getOrgUser(orgId,name) {
	      parent.project_work_content.location = scriptroot+ "/workflowDesign/action/processMonitor.action?processId="+orgId+"&proName="+encodeURI(encodeURI(name))+"&modelType="+'${modelType}';
	    }
	    
	    function gotolist(name){
	    	var ids = idsArray[name];
	    	ids = ids.substring(0,ids.length-1);
	    	parent.project_work_content.window.document.getElementById('iframe').src = scriptroot+ "/workflowDesign/action/processMonitor.action?processId="+ids+"&proName="+encodeURI(encodeURI(name))+"&modelType="+'${modelType}';
	    }
	    
	</script>
	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" width="10%" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				height="2">
				<tr height="2">
					<td height="2"></td>
				</tr>
			</table>
			<table width="95%" border="0" cellpadding="0" cellspacing="0"
				class="table_lb">
				<tr class="tableTRB">
					<td class="table_bk">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
                                var imageRootPath='<%=root%>/workflow';
                                var valueArray = new Array();
                                d = new dTree('d');
                                d.add('c0','-1','流程','','','','','2');
							    <c:forEach items="${treeList}" var="instance" varStatus="status">
						/*	    	
									if(valueArray['c<c:out value="${instance[3]}"/>'] != "true"){
							    		valueArray['c<c:out value="${instance[3]}"/>'] = "true";
							    		d.add('c<c:out value="${instance[3]}"/>','c0','<c:out value="${instance[4]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
							    	}
							    	var name = "<c:out value='${instance[1]}'/>_Vesion<c:out value='${instance[2]}'/>";
							        d.add('g<c:out value="${instance[0]}"/>','c<c:out value="${instance[3]}"/>','<c:out value="${instance[1]}"/>_Vesion<c:out value="${instance[2]}"/>','javascript:getOrgUser(<c:out value="${instance[0]}"/>, \''+name+'\');','','', '<%=root%>/workflow/images/tree/folder_closed.gif','2');
						*/	        
							        if(valueArray['c<c:out value="${instance[3]}"/>'] != "true"){
							    		valueArray['c<c:out value="${instance[3]}"/>'] = "true";
							    		d.add('c<c:out value="${instance[3]}"/>','c0','<c:out value="${instance[4]}"/>','javascript:void(0)','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
							    	}
							    	
							        if(valueArray['c<c:out value="${instance[1]}"/>'] != "true"){
							        	valueArray['c<c:out value="${instance[1]}"/>'] = "true";
							        	var name = "<c:out value='${instance[1]}'/>";
							       		d.add('g<c:out value="${instance[0]}"/>','c<c:out value="${instance[3]}"/>','<c:out value="${instance[1]}"/>','javascript:gotolist( \''+name+'\');','<c:out value="${instance[1]}"/>','', '<%=root%>/workflow/images/tree/folder_closed.gif','2');
							       		
							       		idsArray['<c:out value="${instance[1]}"/>'] = '<c:out value="${instance[0]}"/>'+",";
							        }else{
							        	idsArray['<c:out value="${instance[1]}"/>'] = idsArray['<c:out value="${instance[1]}"/>']+'<c:out value="${instance[0]}"/>'+",";
							        }
	      	 								
							        
							    </c:forEach>
   	 								
	                            document.write(d);
                            </script>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>