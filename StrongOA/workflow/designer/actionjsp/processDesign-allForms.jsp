<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>表单</title>
		<link href="<%=root%>/workflow/css/dtree.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=path%>/workflow/js/dtree_checkbox.js"></script>
		<script type="text/javascript">
			var imageRootPath='<%=path%>/workflow';
			
			function gotoSelectCheck(checkFlag,nodeValue){
			}
			
			/**
			 * 页面初始化选中已选择表单
			 */
			window.onload = function(){
				var radios = document.getElementsByName("rightCheckBox");
				for(var i=0; i<radios.length; i++){
					if(radios[i].value == "u<c:out value='${formId}'/>"){
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
							allvalue =obj.value.substring(1)+","+formname[obj.value.substring(1)] ;
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
	</script>
	</head>
	<base target="_self">
	<body topmargin="0" leftmargin="0" class="contentbodymargin">
		<div id="contentborder" cellpadding="0">
			<%--		<input type="hidden" name="formId" value="<c:out value="${formId}"/>">--%>
			<form action="">
				<div style="height:330px;overflow: auto;">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 50px;">
					<tr>
						<td height="5"></td>
					</tr>
					<tr>
						<td valign="top">
							<table align="left" border="0" cellpadding="2" cellspacing="0">
								<tr>
									<td height="10px"></td>
									<td>
										<div class="dtree" style="padding-left: 10px; ">
											<script type="text/javascript">
												d = new dTree('d');	
												d.add("c0",-1,'表单','','','','','2');
												d.add('u0',
														'c0',
														'流程启动表单',
														'','','','<%=path%>/workflow/images/tree/renyuan.gif','3');
												formname[0] = "流程启动表单";
													<%-- 参数为0则为多选且未初始化选上，为3则为单选 --%>
													<c:forEach items="${forms}" var="form">
														d.add('u<c:out value="${form[0]}"/>',
														'c0',
														'<c:out value="${form[1]}"/>',
														'','','','<%=path%>/workflow/images/tree/renyuan.gif','3');
														formname[<c:out value="${form[0]}"/>] = "<c:out value="${form[1]}"/>";
													</c:forEach>
												document.write(d);
												//d.closeAll();
												</script>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div>
				<table cellspacing="1" cellpadding="0" border="0"
					style="position: absolute; bottom: 20px; width: 100%;">
					<tr>
						<td width="100%" style="text-align: center;">
							<div >
								<input type="button" value="确定" class='input_bg'
								onclick="getAllCheckValue();">
							<input type="button" value="取消" class='input_bg'
								onclick="javascript:window.close();">
							</div></td>
			
					</tr>
				</table>
				
			</form>
		</div>
	</body>
</html>