<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程文件导入</title>
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
		function show(){
		
		if($("input[name=radio]:checked").val()=='0'){
			 document.getElementById("localImport").style.display="block";
			 document.getElementById("urlImport").style.display="none";
		}else{
		     document.getElementById("localImport").style.display="none";
			 document.getElementById("urlImport").style.display="block";
			
		}
	}
 	//页面加载时调用
		$(document).ready(function(){
			$("input[name=radio]:eq(0)").attr("checked",true);

        });
//  top.functionModule="1170103";//相应功能模块编码设置
  	String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
			}
			
   function submitForm(){
	  if(document.getElementsByName("processFile")[0].value.trim()==''){
	  	alert('请选择一个要导入的模型文件！');
	  	document.getElementsByName("processFile")[0].focus();
	  	return false;
   	  }
      document.forms[0].submit();
  }
  
   function cannel(){
	 window.close();
   }

   
  </script>
  </head>
  
	<base target="_self" />


	<body class="contentbodymargin" oncontextmenu="return false;">
		<div align="center">
			<s:form
				action="/workflowDesign/action/processDefinition!fileImport.action"
				method="post" enctype="multipart/form-data">
				
				<div id="div1" style="padding: 10px 10px 10px 10px;">
					<div>
						<s:radio id="radio" name="radio" list="#{'0':'本地导入','1':'url导入'}" onclick="show();"  onselect="0"/>
					</div>
					<br>
					
					<div id="localImport" style="display: block;">
						<s:file name="processFile" />
					</div>
					
					<div id="urlImport"  style=" display: none;">
						<table>
							<tr>
								<td nowrap>
									选择模板：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<select  style="width: 110px;" id="modelId">
										<s:iterator id="vo" value="modelList">
											<option value="${vo.modelId }">
												${vo.modelName }
											</option>
										</s:iterator>
									</select>
								</td>
							</tr>
						</table>
						<br>
					</div>
					<div style="width: 100%;" align="center">
						<input type="button" class="input_bg" value="确定" onclick="submitForm();">
								&nbsp;&nbsp;
						<input type="button" class="input_bg" value="取消" onclick="cannel();">
					</div>
				
				</div>
				
			</s:form>
		</div>
	</body>
</html>