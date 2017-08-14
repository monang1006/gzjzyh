<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%> 

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>人员选择配置信息</title>
		<link href="<%=root%>/workflow/css/dtree1.css" type="text/css"
			rel="stylesheet">
		<link href="<%=root%>/common/css/windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css"
			rel="stylesheet">
		<script language="javascript"
			src="<%=root%>/workflow/js/dtree_checkbox.js" type="text/javascript"></script>
	</head>

	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" cellpadding="0">
			<table align="center" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td height="10px">
						<div class="dtree">
							<script language="javascript" type="text/javascript">
							
							function gotoSelectCheck(checkFlag,nodeValue){
							}	
							
							var asName = [];
							
						    function getAllCheckValue(){
						    	var allvalue = [];
								for(var i=0; i<document.getElementsByName("rightCheckBox").length; i++){
									if(document.getElementsByName("rightCheckBox")[i].checked){
										var obj = document.getElementsByName("rightCheckBox")[i];
										allvalue.push({name:asName["" + obj.value + ""], alias:obj.value});
									}
								}
								if(pageType === "ag"){
									window.dialogArguments.resetAssignerSet(allvalue);
								}else if(pageType === "ra"){
									window.dialogArguments.resetReAssignerSet(allvalue);
								}
								window.close();
						  }
				 		  
				 		  function initChecked(objSelect) {
                            objSelect = (objSelect == "" ? [] : objSelect.split(","));
                            var checkBoxes=document.getElementsByName('rightCheckBox');
                            
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
                          
                          var pageType = "<%=String.valueOf(request.getParameter("pageType"))%>";
				 		  
                          var imageRootPath='<%=root%>/workflow';
						  var d = new dTree('d');
						  d.add('0','-1','人员选择配置信息','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
						  <c:forEach items="${asLst}" var="tree" varStatus="status">
						      d.add('<c:out value="${tree.asFlag}"/>','0','<c:out value="${tree.asName}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','-2');
						      asName['<c:out value="${tree.asFlag}"/>'] = "<c:out value="${tree.asName}"/>";
						  </c:forEach>
						  document.write(d);
						  
						  var initSelect = "";
						  if(pageType === "ag"){//任务处理人
						  	initSelect = window.dialogArguments.getAssignerSet();
						  }else if(pageType === "ra"){//任务指派人员
						  	initSelect = window.dialogArguments.getReAssignerSet();
						  }
						  
						  initChecked(initSelect);
						</script>
						</div>
					</td>
				</tr>
			</table>
			<table align="center">
				<tr>
					<td>
						<input type="button" value="确定" class='input_bg'
							onclick="getAllCheckValue();">
						<input type="button" value="取消" class='input_bg'
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
