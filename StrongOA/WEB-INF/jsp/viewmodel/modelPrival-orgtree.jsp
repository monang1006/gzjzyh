<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>人员选择</title>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css"
			rel="stylesheet">
		<link href="<%=root%>/workflow/css/windows.css" type="text/css"
			rel="stylesheet">
      <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>	
		<script language="javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js"
			type="text/javascript"></script>
	</head>

	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div  cellpadding="0">
			<table border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td height="10px">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
						  function setNodeItem(checkFlag, id, name) {

						      if (checkFlag) {
						    	  parent.addOption("org", name, id,geshu);						        
						      } else {
						        parent.removeOption("org", name, id);
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

//							      if (checkBoxes[i].value.substring(0, 1) == 'c') {
//							        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
//							      } else {
							       selectTreeNode(checkFlag, checkBox.value);
//							      }
						        }
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
						  function selectTreeNode1(checkFlag, nodeValue) {
							  var checkBoxes=document.getElementsByName('rightCheckBox');
						      for(var i=0; i<d.aNodes.length; i++) {
						    	if (d.aNodes[i].pid == nodeValue) {
						    		
						          var checkBox = document.getElementById(d.aNodes[i].id);
							      
						          setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
//							      if (checkBoxes[i].value.substring(0, 1) == 'c') {
//							        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
//							      } else {
							       selectTreeNode1(checkFlag, checkBox.value);
//							      }
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
						  var geshu=0;
						 
						 function gotoSelectCheck(checkFlag,nodeValue){					      
//					        if (nodeValue.substring(0, 1) == 'c') {
	                          selectTreeNode(checkFlag,nodeValue);
					          geshu=$("input[name='rightCheckBox']:checked").length;
					          SelectNodeItem(checkFlag,nodeValue);
					          selectTreeNode1(checkFlag, nodeValue);
//					        } else {
//					          SelectNodeItem(checkFlag,nodeValue);
//					        }
						  }	
						 function unChecked(objvalue){
					      	alert("unChecked");	
				   				var checkBoxes=document.getElementsByName("rightCheckBox");
				    			if (checkBoxes != null){
				  					for(i=0; i<checkBoxes.length; i++){
				    					if(checkBoxes[i].value == objvalue){
				      						checkBoxes[i].checked=false;
				       					}
				    				}
				    			} 
				 		  }
					     function initChecked(objSelect) {
                           // objSelect = objSelect == "" ? [] : objSelect.split(",");
                            var checkBoxes = document.getElementsByName('rightCheckBox');
                            if(objSelect!=null&&objSelect!=""){
                            	 if (objSelect.length != 0) {
                                     for(var j=0; j<objSelect.length; j++){
     		                            for(var i=0; i<checkBoxes.length; i++){
     		                              if (checkBoxes[i].value == objSelect[j].value) {
     		                                  checkBoxes[i].checked = true;
     		                              }
     		                            }
     				                }
                                 }
                            }
                           
                          }
				 		 function show(i){
								$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3'}});
								$.blockUI({ message: '<span class="wz">'+i+'</span>',css:{width:'160px',height:'25px'}});
								
							}
							function hidden(){
								$.unblockUI();
							}
				 		  function deleteSelectedNodeItem(nodeValue) {
				 		  	 var checkBoxes=document.getElementsByName('rightCheckBox');
						    for(var i=0; i<d.aNodes.length; i++){
						      if (d.aNodes[i].id == nodeValue) {
						      	var checkBox = document.getElementById(d.aNodes[i].id);
							   
							      checkBox.checked = false;							        

						        break;
							  }
						    }
				 		  }
				 		  
                          var imageRootPath='<%=root%>/workflow';
                          var d = new dTree('d');           
						  d.add('${topTreeNodeId}','-1','组织机构','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:set var="topTreeId" value="${topTreeNodeId}"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      d.add('<c:out value="${tree[0]}"/>','${topTreeNodeId}','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:when>
						    <c:otherwise>
						      d.add('<c:out value="${tree[0]}"/>','<c:out value="${tree[1]}"/>','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  document.write(d);
						  //initChecked(initData);						                            
						  parent.frameInitChecked('modelPrival-orgtree');
						</script>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
