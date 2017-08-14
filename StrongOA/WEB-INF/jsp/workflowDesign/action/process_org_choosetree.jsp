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

		<script language="javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js"
			type="text/javascript"></script>
	</head>

	<body topmargin="0" leftmargin="0" class="">
		<!-- 处理dtree数据过多导致界面变形的问题  yanjian 2012-08-17 14:27  div#contentborder 添加样式"float: left;" -->
		<div  cellpadding="0" style="float: left;height:100%;overflow: auto;width:100%;">
			<table align="left" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td height="10px">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
						  function setNodeItem(checkFlag, id, name) {
						      if (checkFlag) {
						        parent.addOption("org", name, id);						        
						      } else {
						        parent.removeOption("org", name, id);
						      }
						  }
						
						  function selectTreeNode(checkFlag, nodeValue) {
						      var checkBoxes=document.getElementsByName('rightCheckBox');
   						      SelectNodeItem(checkFlag, nodeValue);
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
//					        if (nodeValue.substring(0, 1) == 'c') {						          
					          selectTreeNode(checkFlag,nodeValue);
//					        } else {
//					          SelectNodeItem(checkFlag,nodeValue);
//					        }
						  }						  
					      
					      function unChecked(objvalue){
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
                            objSelect = objSelect == "" ? [] : objSelect.split(",");
                            var checkBoxes = document.getElementsByName('rightCheckBox');
                            if (objSelect.length != 0) {
                                for(var j=0; j<objSelect.length; j++){
		                            for(var i=0; i<checkBoxes.length; i++){
		                              if (checkBoxes[i].value == objSelect[j]) {
		                                  checkBoxes[i].checked = true;
		                              }
		                            }
				                }
                            }
                          }
				 		  
                          var imageRootPath='<%=root%>/workflow';
                          var d = new dTree('d');           
						  d.add('o${topTreeNodeId}','-1','组织机构','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:set var="topTreeId" value="${topTreeNodeId}"/>
						  <c:forEach items="${orgList}" var="tree" varStatus="status">
						  <c:choose>
						    <c:when test="${tree[1] == topTreeId}">
						      d.add('o<c:out value="${tree[0]}"/>','o${topTreeNodeId}','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:when>
						    <c:otherwise>
						      d.add('o<c:out value="${tree[0]}"/>','o<c:out value="${tree[1]}"/>','<c:out value="${tree[2]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						    </c:otherwise>
						  </c:choose>						  
						  </c:forEach>
						  document.write(d);
						  /*
						  var initData = "<%=request.getParameter("initData") != null ? String
					.valueOf(request.getParameter("initData")) : ""%>";
						  
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
									if(initDatas[j].substring(0, 1) == 'o'){
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
						  //parent.frameInitChecked('process_org_select');
						</script>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
