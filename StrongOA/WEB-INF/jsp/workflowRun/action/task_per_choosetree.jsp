<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>人员选择</title>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css"
			rel="stylesheet">
		<link href="<%=root%>/workflow/css/windows.css" type="text/css"
			rel="stylesheet">
		<script language="javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js"
			type="text/javascript"></script>
	</head>

	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" cellpadding="0" style="border-top-width:1px">
			<table border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td height="10px">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
						  function setNodeItem(checkFlag, id, name) {
						      if (checkFlag) {
						        parent.addOption(name, id);						        
						      } else {
						        parent.removeOption(name, id);
						      }
						  }
						
						  function selectTreeNode(checkFlag, nodeValue) {
						      var checkBoxes=document.getElementsByName('rightCheckBox');
						      for(var i=0; i<d.aNodes.length; i++) {
						        if (d.aNodes[i].pid == nodeValue) {
						          var checkBox = document.getElementById(d.aNodes[i].id);
							      if (checkBox.checked != checkFlag) {
							        checkBox.checked = checkFlag;
							      }
							      if (checkBox.value.substring(0, 1) == 'u' || checkBox.value.substring(0, 1) == 's') {
							        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
							      } else {
							        //selectTreeNode(checkFlag, checkBoxes[i].value);
							        
							        
							        //gotoSelectCheck(checkFlag, checkBoxes[i].value);//解决全选有些不能选中的问题
							        gotoSelectCheck(checkFlag, checkBox.value);//解决全选有些不能选中的问题 modify by luosy 2014-03-27
							      }
						        }
						      }
						  }
						  
						  function SelectNodeItem(checkFlag,nodeValue) {
						    var checkBoxes=document.getElementsByName('rightCheckBox');
						    for(var i=0; i<d.aNodes.length; i++){
						      if (d.aNodes[i].id == nodeValue) {
						      	var checkBox = document.getElementById(d.aNodes[i].id);
							    if (checkBox.checked != checkFlag) {
							      checkBox.checked = checkFlag;							        
  						        }

						        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
						        
						        break;
							  }
						    }
						  }
						
						  function gotoSelectCheck(checkFlag,nodeValue){						      
					        if (nodeValue.substring(0, 1) == 'g') {						          
					          selectTreeNode(checkFlag,nodeValue);
					        } else {
					          SelectNodeItem(checkFlag,nodeValue);
					        }
						  }						  
					      
					      function selectedFunc(checkFlag, objvalue){
				   				var checkBoxes=document.getElementsByName("rightCheckBox");
				    			if (checkBoxes != null){
				  					for(i=0; i<checkBoxes.length; i++){
				    					if(checkBoxes[i].value == objvalue){
				      						checkBoxes[i].checked=checkFlag;
				       					}
				    				}
				    			} 
				 		  }
				 		  
				 		  function initChecked(objSelect) {
                            var objSelect = objSelect; //parent.document.getElementById("tempSendTo");
                            var checkBoxes = document.getElementsByName('rightCheckBox');
                            if (objSelect.length != 0) {
                                for(var j=0; j<objSelect.length; j++){
		                            for(var i=0; i<checkBoxes.length; i++){
		                              if (checkBoxes[i].value.substring(0, 1) == 'u' || checkBoxes[i].value.substring(0, 1) == 's'){
			                              if (checkBoxes[i].value == objSelect[j].value) {
			                                  checkBoxes[i].checked = true;
			                              }
			                          }
		                            }
				                }
                            }
                          }
				 		  
                          var imageRootPath='<%=root%>/workflow';                           
						  var d = new dTree('d');
						  d.add('g${topTreeNodeId}','-1','组织机构','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2','',true);
						  <c:set var="topTreeId" value="${topTreeNodeId}"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      d.add('g<c:out value="${tree[0]}"/>','g${topTreeNodeId}','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2','',true);
						      //d.add('s0','g<c:out value="${tree[0]}"/>','发起人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');	
						    </c:when>
						    <c:otherwise>
						      d.add('g<c:out value="${tree[0]}"/>','g<c:out value="${tree[1]}"/>','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2','',true);
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  //var valueArray = new Array();
						  <c:forEach items="${userList}" var="utree" varStatus="status">
						  		//if(valueArray['u<c:out value="${utree[0]}"/>c<c:out value="${utree[2]}"/>'] != "true"){
						  		d.add('u<c:out value="${utree[0]}"/>','g<c:out value="${utree[2]}"/>','<c:out value="${utree[1]}"/>','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2','<%=root%>/workflow/images/tree/renyuan.gif',true);
						  		//}
						  </c:forEach>						  
						  document.write(d);
						  //initChecked();
						  parent.frameInitChecked('instance_select');
						</script>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>