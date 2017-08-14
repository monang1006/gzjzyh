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

	<body topmargin="0" leftmargin="0">
		<div cellpadding="0">
			<table align="center" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td height="10px">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
						  function setNodeItem(checkFlag, id, name) {
						      if (checkFlag) {
						        parent.addOption("other", name, id);						        
						      } else {
						        parent.removeOption("other", name, id);
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

							      if (checkBox.value.substring(0, 1) == 's') {
							        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
							      } else {
							        selectTreeNode(checkFlag, checkBox.value);
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
                            var checkBoxes=document.getElementsByName('rightCheckBox');
                            
                            if (objSelect.length != 0) {
                                for(var j=0; j<objSelect.length; j++){
		                            for(var i=0; i<checkBoxes.length; i++){
		                              if (checkBoxes[i].value.substring(0, 1) == 's')
			                              if (checkBoxes[i].value == objSelect[j]) {
			                                  checkBoxes[i].checked = true;
			                              }
		                            }
				                }
                            }
                          }
				 		  
                          var imageRootPath='<%=root%>/workflow';                           
						  var d = new dTree('d');
						  d.add('g1','-1','组织机构','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  d.add('s0$1','g1','发起人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s1$1','g1','发起人部门负责人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
					      d.add('s2$1','g1','发起人所在机构负责人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
					      d.add('s3$1','g1','前步处理人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s4$1','g1','前步处理人部门负责人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
					      d.add('s5$1','g1','前步处理人所在机构负责人','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s6$1','g1','当前用户所在机构人员','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s7$1','g1','当前用户所在部门人员','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s8$1','g1','当前用户所在机构人员(不含自己)','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s9$1','g1','当前用户所在部门人员(不含自己)','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s10$1','g1','当前用户(顶级/二级)所在部门/机构人员','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s11$1','g1','主办人员','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
						  d.add('s12$1','g1','当前用户所在部门分管领导','','','','<%=root%>/workflow/images/tree/renyuan.gif','-2');
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
									if(initDatas[j].substring(0, 1) == 's'){
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
						  //parent.frameInitChecked('node_person_select');
						</script>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<body>
		<br>
		<br>
	</body>
</html>
