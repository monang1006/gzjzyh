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
				<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type="text/javascript">
		function setForm(value){
			if(value==0){
				$("#dtree").show();
				$("#jsptree").hide();
			}else{
				$("#dtree").hide();
				$("#jsptree").show();
			}
		}
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
			
			
			
			var formname=new Array();	//表单名称
			var formid=new Array();		//表单id
			
			
			function getFormNameByFormId(id){
				var index  = -1;
				for(var i=0;i<formid.length;i++){
					if(id == formid[i]){
						index = i;
						break;
					}
				}
				return formname[index];
			}
			
			 //得到选择表单的id,name
		    function getAllCheckValue(){
				var allvalue=",";
				for(var i=0;i<document.getElementsByName("rightCheckBox").length;i++){
					if(document.getElementsByName("rightCheckBox")[i].checked){
						var obj = document.getElementsByName("rightCheckBox")[i];
						if (obj.value.substring(0,1) != "c"){
							//allvalue =obj.value.substring(1)+","+formname[obj.value.substring(1)] ;
							allvalue =obj.value.substring(1)+","+getFormNameByFormId(obj.value.substring(1)) ;
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
	<div align="center">
		Silverlight表单<input type="radio" value="0" name="form" onclick="setForm(0);" checked="checked">
		Jsp表单<input type="radio"  value="1" name="form" onclick="setForm(1);">
	</div>
	<hr>
		<div id="contentborder" cellpadding="0"  style="overflow: scroll;height: 85%;">
				<input type="hidden" name="formId" value="<c:out value="${formId}"/>">
			<form action="">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="top">
							<table align="center" border="0" cellpadding="2" cellspacing="0">
								<tr>
									<td>
										<div class="dtree"  id="dtree" >
											<script>
											d = new dTree('d');	
											d.add("c0",-1,'表单','','','','','2');
											d.add('u0',
													'c0',
													'流程启动表单',
													'','','','<%=path%>/workflow/images/tree/renyuan.gif','3');
													formname.push("流程启动表单");
													formid.push(0);
										        	//formname[0] = "流程启动表单";
												<%-- 参数为0则为多选且未初始化选上，为3则为单选 --%>
												<c:forEach items='${forms}' var="form">
													d.add('u<c:out value="${form[0]}"/>',
													'c0',
													'<c:out value="${form[1]}"/>',
													'','','3','<%=path%>/workflow/images/tree/renyuan.gif','3');
													formname.push("<c:out value="${form[1]}"/>");
													formid.push("<c:out value="${form[0]}"/>");
													//formname[<c:out value="${form[0]}"/>] = "<c:out value="${form[1]}"/>";
												</c:forEach>
												document.write(d);
												
											</script>
										</div>
										<div class="dtree"  id="jsptree" style="display: none;">
												<script>
												d2 = new dTree('d2');	
												d2.add("c0",-1,'表单','','','','','2');
													<%-- 参数为0则为多选且未初始化选上，为3则为单选 --%>
													<c:forEach items='${jspForm}' var="jspform">
													
														d2.add('u<c:out value="${jspform[0]}"/>',
														'c0',
														'<c:out value="${jspform[1]}"/>',
														'','','','<%=path%>/workflow/images/tree/renyuan.gif','3');
														formname.push("<c:out value="${jspform[1]}"/>");
														formid.push("<c:out value="${jspform[0]}"/>");
													</c:forEach>
										 		document.write(d2);
											</script>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>
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
	</body>
</html>