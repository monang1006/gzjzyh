<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程文件导入</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css"
			rel="stylesheet">
	</head>
	<script type="text/javascript">
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
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;">
		<div align="center">
			<s:form
				action="/workflowDesign/action/processDefinition!fileImport.action"
				method="post" enctype="multipart/form-data">
				<table width="80%">
					<tr>
						<td colspan="2" height="15">
							请选择要导入的流程模型文件：
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<s:file name="processFile" />
						</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="button" class="input_bg" value="确定"
								onclick="submitForm();">
							&nbsp;&nbsp;
							<input type="button" class="input_bg" value="取消"
								onclick="cannel();">
						</td>
					</tr>
				</table>
			</s:form>
		</div>
	</body>
</html>