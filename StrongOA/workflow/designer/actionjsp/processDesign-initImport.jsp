<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<HTML>
	<HEAD>
		<TITLE>流程模型导入</TITLE>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=root%>/workflow/designer/css/style.css">
		<link type="text/css" href="<%=root%>/workflow/designer/js/jquery/jquery.ui.all.css"
			rel="stylesheet" />
		<script language=jscript src="<%=root%>/workflow/designer/js/workflow/const.js"></script>
		<script language=jscript src="<%=root%>/workflow/designer/js/jquery/jquery-1.4.2.js"></script>
		<script type="text/javascript" src="<%=root%>/workflow/designer/js/jquery/jquery.ui.core.min.js"></script>
		<script type="text/javascript" src="<%=root%>/workflow/designer/js/jquery/jquery.ui.widget.min.js"></script>
		<script type="text/javascript" src="<%=root%>/workflow/designer/js/jquery/jquery.ui.tabs.min.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/global.js"></script>
	
		<script type="text/javascript">
			$(function() {
				$("#tabs").tabs();
			});
			
			
//  top.functionModule="1170103";//相应功能模块编码设置
  	String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
			}
			
   function submitForm(){
	  var filename = document.getElementsByName("processFile")[0].value.trim();
	  if(filename==''){
	  	alert('请选择一个要导入的模型文件！');
	  	document.getElementsByName("processFile")[0].focus();
	  	return false;
   	  }
	  
	  var pos = filename.lastIndexOf(".");
	  var ext = filename.substring(pos+1);
	  ext = ext.toLowerCase();
	  if(ext != "xml"){
		  alert("模型文件是以.xml为扩展名。");
		  return false;
	  } 
	  
      document.forms[0].submit();
  }
  
   function cannel(){
	 window.close();
   }

		</script>

		<style>
body {
	background-color: buttonface;
	scroll: no;
	margin: 7px, 0px, 0px, 7px;
	border: none;
	overflow: hidden;
}
.tab_content{
	height:100px;
}
</style>
</HEAD>
<base target="_self" />
<body oncontextmenu="return false;">
	<s:form action="/workflowDesign/action/processDesign!fileImport.action" 
		method="post" enctype="multipart/form-data">
	<table border="0" cellpadding="0" cellspacing="0" height="150px"
		width="100%">
		<tr>
			<td id="contentscell" valign="top" align="center" height="100%"
				width="100%">
				<div id="tabs" height="100%" width="100%" align="center"
					style="margin-top: 5px; margin-left: 5px; margin-right: 5px;">
					<ul class="title">
						<li><a href="#tabs-1">导入模型</a></li>
					</ul>
					<div id="tabs-1" class="tab_content">
						<TABLE border=0 width="100%" height="100%">
							<TR valign=top>
								<TD></TD>
								<TD width="100%" valign=top>
									<div class="tit1">
										<span id=tabpage1_1>请选择要导入的流程模型文件：</span>
									</div>
									<TABLE border=0 width="100%" height="100%"
										style="font-size: 9pt;">
										<TR valign=top>
											<TD width=5></TD>
											<TD width="100%">&nbsp;&nbsp;
												<s:file name="processFile" cssStyle="width:400px;height:25px;"/>
											</TD>
											
										</TR>
									
									</TABLE>
								</TD>
								<TD>&nbsp;</TD>
							</TR>
						</TABLE>
					</div>
				</div></td>
		</tr>
	</table>
	
	<table cellspacing="1" cellpadding="0" border="0"
		style="position: absolute; bottom: 20px; width: 100%;">
		<tr>
			<td width="100%" style="text-align: right;">
				<div style="padding-right: 15px;">
					<input type=button id="btnOk" class=btn value="确 定"
						onclick="submitForm();"> &nbsp;&nbsp;&nbsp; 
					<input type=button id="btnCancel" class=btn value="取 消"
						onclick="cannel();">
				</div></td>
		</tr>
	</table>
	</s:form>
</body>

</HTML>