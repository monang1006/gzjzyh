<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择委派事项</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type="text/javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js"></script>
		<script type="text/javascript">
		//初始化选择项
		$(document).ready(function(){
			var params = window.dialogArguments;
			var objProcess = params.window.document.getElementById("dhDeleProcess");
			if(objProcess){
				var processes = objProcess.value;
				if(processes != ""){
					var processData = processes.split("|");
					for(var i=0;i<document.getElementsByName("rightCheckBox").length;i++){
						var obj = document.getElementsByName("rightCheckBox")[i];
						if (obj.value.substring(0,1) == "u"){//流程节点
							for(var j=0;j<processData.length;j++){
								var info = processData[j].split(",");
								var id = info[0];
								var name = info[1];
								if(id == obj.value.substring(1)){
									obj.checked = true;
								}
							}
						}
					}
				}
			}
		});
		
		
			var imageRootPath='<%=root%>/workflow';
			var formname=new Array();
			 //得到选择表单的id,name
		    function getAllCheckValue(){
				var allvalue="";
				for(var i=0;i<document.getElementsByName("rightCheckBox").length;i++){
					if(document.getElementsByName("rightCheckBox")[i].checked){
						var obj = document.getElementsByName("rightCheckBox")[i];
						if (obj.value.substring(0,1) == "u"){
							allvalue = allvalue + "|" + obj.value.substring(1) + "," + formname[obj.value.substring(1)];
						}
					}
				}
			//窗口关闭时返回值
			if(allvalue != ""){
				allvalue = allvalue.substring(1);
			}
			window.returnValue = allvalue;
			window.close();
		}
		
						
						  function setNodeItem(checkFlag, id, name) {
						  }
						
						  function selectTreeNode(checkFlag, nodeValue) {
						      var checkBoxes=document.getElementsByName('rightCheckBox');
						      for(var i=0; i<d.aNodes.length; i++) {
						        if (d.aNodes[i].pid == nodeValue) {
							      if (checkBoxes[i].checked != checkFlag) {
							        checkBoxes[i].checked = checkFlag;
							      }

							      if (checkBoxes[i].value.substring(0, 1) == 'u') {
							        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
							      } else {
							        selectTreeNode(checkFlag, checkBoxes[i].value);
							      }
						        }
						      }
						  }
						  
						  function SelectNodeItem(checkFlag,nodeValue) {
						    var checkBoxes=document.getElementsByName('rightCheckBox');
						    for(var i=0; i<d.aNodes.length; i++){
						      if (checkBoxes[i].value == nodeValue) {
							    if (checkBoxes[i].checked != checkFlag) {
							      checkBoxes[i].checked = checkFlag;							        
  						        }
						        setNodeItem(checkFlag, d.aNodes[i].id, d.aNodes[i].name);
						        break;
							  }
						    }
						  }
						
						  function gotoSelectCheck(checkFlag,nodeValue){				  							      
					        if (nodeValue.substring(0, 1) == 'c') {						          
					          selectTreeNode(checkFlag,nodeValue);
					        } else {
					          SelectNodeItem(checkFlag,nodeValue);
					        }
						  }						  
					      
	</script>
	</head>
	<base target="_self">
	<body  topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" style="background-color:white" cellpadding="0">
			<form action="">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center">
										<table border="0" align="center" cellpadding="00" cellspacing="0">
											<tr>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="getAllCheckValue();">&nbsp;确&nbsp;定&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
						                  		<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
							                 	<td class="Operation_input1" onclick="javasctipr:window.close();">&nbsp;取&nbsp;消&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
						                  		<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<div style="width: 100px;" align="center">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td height="5"></td>
					</tr>
					<tr>
						<td valign="top">
							<table align="center" border="0" cellpadding="2" cellspacing="0">
								<tr>
									<td height="10px"></td>
									<td>
										<div class="dtree" style="font-size:14px;">
											<script type="text/javascript">
												d = new dTree('d');	
												d.add("c0",-1,'选择流程','','','','','0');
												var valueArray = new Array();
													<%-- 参数为0则为多选且未初始化选上，为3则为单选 --%>
												    <c:forEach items="${processList}" var="process">
												    	if(valueArray['c<c:out value="${process[2]}"/>'] != "true"){
												    		valueArray['c<c:out value="${process[2]}"/>'] = "true";
												    		d.add('c<c:out value="${process[2]}"/>','c0','<c:out value="${process[3]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','0');
												    	}
														d.add('u<c:out value="${process[0]}"/>',
														'c<c:out value="${process[2]}"/>',
														'<c:out value="${process[1]}"/>',
														'','','','<%=root%>/workflow/images/tree/renyuan.gif','0');
														formname["<c:out value="${process[0]}"/>"] = '<c:out value="${process[1]}"/>';
												    </c:forEach>													
												document.write(d);
												//d.openAll();
												  d.closeAll();
												</script>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div>
			</form>
		</div>
	</body>
</html>