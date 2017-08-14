<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>子流程</title>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=path%>/workflow/js/dtree_checkbox.js"></script>
		<script type="text/javascript">
			var imageRootPath='<%=root%>/workflow';
			
			/**
			 * 页面初始化选中已选择表单
			 */
			window.onload = function(){
				var radios = document.getElementsByName("rightCheckBox");
				for(var i=0; i<radios.length; i++){
					if(radios[i].value == "u<c:out value='${subProcessId}'/>"){
						radios[i].checked = true;
						break;
					}
				}
			}
			
			var formname=new Array();
			 //得到选择表单的id,name
		    function getAllCheckValue(){
				var allvalue=",";
				for(var i=0;i<document.getElementsByName("rightCheckBox").length;i++){
					if(document.getElementsByName("rightCheckBox")[i].checked){
						var obj = document.getElementsByName("rightCheckBox")[i];
						if (obj.value.substring(0,1) != "c"){
							allvalue =obj.value.substring(1)+","+formname[obj.value.substring(1)];
							break;
						}
					}
				}
			if(allvalue==","){
				allvalue = "";
			}
			//窗口关闭时返回值  
			window.returnValue = allvalue;
			window.close();
		}
		
			function gotoSelectCheck(checkFlag,nodeValue){
			}			
	</script>
	</head>
	<base target="_self">
	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" style="height: 430px;" cellpadding="0">
			<%--		<input type="hidden" name="formId" value="<c:out value="${formId}"/>">--%>
			<form action="">
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
										<div class="dtree">
											<script type="text/javascript">
												d = new dTree('d');	
												var typeArray = new Array();
												d.add("c0",-1,'子流程','','','','','2');
													<%-- 参数为0则为多选且未初始化选上，为3则为单选 --%>
												    <c:forEach items="${subProcesses}" var="process">
												    	if(typeArray['c<c:out value="${process[2]}"/>'] != "true"){
												    		d.add('c<c:out value="${process[2]}"/>','c0','<c:out value="${process[3]}"/>','','','','<%=root%>/workflow/images/tree/folder_closed.gif','2');
												    		typeArray['c<c:out value="${process[2]}"/>'] = "true";
												    	}
														d.add('u<c:out value="${process[0]}"/>',
														'c<c:out value="${process[2]}"/>',
														'<c:out value="${process[1]}"/>',
														'','','','<%=path%>/workflow/images/tree/renyuan.gif','3');
														formname[<c:out value="${process[0]}"/>] = "<c:out value="${process[1]}"/>";
												    </c:forEach>													
													
												document.write(d);
												d.openAll();
												</script>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div>
				<div style="height: 90px;">
				<table align="center">
					<tr>
						<td>
							<%--							<INPUT type="hidden" name="Indent_input">--%>
							<%--							<input type="hidden" name="const_value">--%>
							<input type="button" value="确定" class='input_bg'
								onclick="getAllCheckValue();">
							<input type="button" value="取消" class='input_bg'
								onclick="javasctipr:window.close();">
						</td>
					</tr>
				</table>
               </div>
			</form>

	</body>
</html>
