<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程文件导出</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
			
	
	

	<script type="text/javascript">
		 var opener = window.dialogArguments;
		
		function show(){
		
		if($("input[name=radioValue]:checked").val()=='0'){
			 document.getElementById("localImport").style.display="block";
			 document.getElementById("urlImport").style.display="none";
		}else{
		     document.getElementById("localImport").style.display="none";
			 document.getElementById("urlImport").style.display="block";
			
		}
	}
 	//页面加载时调用
		$(document).ready(function(){
			$("input[name=radioValue]:eq(0)").attr("checked",true);

        });
//  top.functionModule="1170103";//相应功能模块编码设置
  	String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
			}
			
   function submitForm(){
   		var radioValue=$("input[name=radioValue]:checked").val();
   		if(radioValue!=null&&radioValue=="1"){
   			var modelName=document.getElementById("modelName").value.trim();
   			var processType = document.getElementsByName("processType")[0].value;
   			var pfId=document.getElementById("pfId").value;
   			if(modelName==''){
   				alert('请输入表单模板名称！');
			  	document.getElementById("modelName").focus();
				return false;
   			}else{
   				 $.post("<%=path%>/workflowDesign/action/processDesign!isHasModelName.action",
		          {"modelName":modelName,"processType":processType},
		          function(data){
		              if(data=="1"){
		               
		                alert('当前表单模板名称已存在，请重新输入！');
					  	document.getElementById("modelName").focus();
						return false;
		              }else{
		              	 window.returnValue = "1,"+modelName+","+pfId+","+processType;
						 window.close();
		              }
		          });
   			
   				
   			}
   		}else{
   			
	   	  window.returnValue = "0";
	   	  window.close();
   		}
  }
  
   function cannel(){
	 window.close();
   }


  

   
  </script>
  </head>
  
	<base target="_self" />


	<body class="contentbodymargin" oncontextmenu="return false;">
		<div align="center">
			<form id="processForm" action="<%=path%>/workflowDesign/action/processDesign!fileExport.action" method="POST">
				<input type="hidden" name="processfile" id="processfile"/>
				<input type="hidden" name="pfId" id="pfId" value="${pfId}" />
				<div id="div1" style="padding: 10px 10px 10px 10px;">
					<div>
						<s:radio id="radioValue" name="radioValue" list="#{'0':'本地导出','1':'url导出'}" onclick="show();"  onselect="0"/>
					</div>
					<br>
					
					<div id="localImport" style="display: block;">
						<table>
							<tr>
								<td colspan="2" height="15">
									确认本地导出模板，点击【确认】
								</td>
							</tr>
							<tr>
								<td colspan="2" height="15">
									
									
								</td>
							</tr>
						</table>
					</div>
					
					<div id="urlImport"  style=" display: none;" align="center">
						<table>
						
							<tr>
								<td colspan="2" height="15">
									<table>
										<tr>
											<td>
												<span class="wz"><FONT color="red">*</FONT>&nbsp;选择流程类型：</span>
											</td>
											<td>
												<select name="processType" style="width:175px;" class="search">
													<c:forEach items="${types}" var="type" varStatus="status">
														<script>
														if("${processType}"=="<c:out value="${type[0]}"/>"){
															document.write("<option selected value='"+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
														}else{
															document.write("<option value='"+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
														}
														</script>
													</c:forEach>
												</select>
												
											</td>
										</tr>
										<tr>
											<td>
												<span class="wz"><FONT color="red">*</FONT>&nbsp;输入模板名称：</span>
											</td>
											<td>
												<input id="modelName" name="modelName" type="modelName" size="30" maxlength="100" value="${modelName}"   >
											</td>
										</tr>
<%--										<tr>
											<td>
												<span class="wz">输入URL地址：</span>
											</td>
											<td>
												<input id="modelUrl" name="modelUrl" type="text"
											size="30" 
											  maxlength="100">
											</td>
										</tr>--%>
									</table>
								</td>
							</tr>
						</table>
						<br>
					</div>
					<div style="width: 100%;" align="center">
					<a id="addPerson"  href="#" class="button" onclick="submitForm();">确定</a>
					<a id="addPerson"  href="#" class="button" onclick="cannel();">取消</a>
						<input type="hidden" class="input_bg" value="确定" onclick="submitForm();">
								&nbsp;&nbsp;
						<input type="hidden" class="input_bg" value="取消" onclick="cannel();">
					</div>
				
				</div>
				
			</form>
		</div>
	</body>
</html>