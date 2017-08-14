<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看附件</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<style media="screen" type="text/css">
    		.tabletitle {
      			FILTER:progid:DXImageTransform.Microsoft.Gradient(
                            gradientType = 0, 
                            startColorStr = #ededed, 
                            endColorStr = #ffffff);
    		}
    
    		.hand {
      			cursor:pointer;
    		}
    	</style>
		<script type="text/javascript">
		 	//初始化WORD控件
            function initWordOCX() {
              	var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
                TANGER_OCX_OBJ.CreateNew("Word.Document");                
                //禁用新建功能
                TANGER_OCX_OBJ.FileNew=false;
                //禁用保存功能
                TANGER_OCX_OBJ.FileSave=false;
                //禁用打印功能
                TANGER_OCX_OBJ.FilePrint=false;
                //禁用关闭功能
                TANGER_OCX_OBJ.FileClose=false;
                //禁用另存为功能
                TANGER_OCX_OBJ.FileSaveAs=false;
            }
      
      
      		function openFromURL(appendId) {
          		var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          		var fileId = document.getElementById("fileId").value;
<%--          		var appendId=$("#appendId").val();--%>
          		TANGER_OCX_OBJ.OpenFromURL("<%=root%>/archive/archivefile/achiveFile!openAnnex.action?fileId="+fileId+"&appendId="+appendId);
      		}
      
      		function closeDoc() {
        		window.returnValue = "OK";
        		window.close();
      		}
      
      		$(document).ready(function(){			  
			  	initWordOCX();
			  	var fileId = $("#fileId").val();
			  	var appendId="${appendId}"
			  		if ((fileId != "") && (fileId != null)&&(appendId != "") && (appendId != null)) {
			     		openFromURL(appendId);
			  		}
			}); 
    	</script>
	</head>
	<base target="_self">
	<body class="contentbodymargin" oncontextmenu="return false;">
		<s:form id="senddocForm" name="senddocForm" action="" enctype="multipart/form-data" method="post">
			<s:hidden id="fileId" name="model.fileId"></s:hidden>
<%--			<s:hidden id="appendId" name="appendId" value="${appendId}"></s:hidden>--%>
			<div id="contentborder" align="center">
				<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td width="100%" class="tabletitle" height="40">
							<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
								<tr>
									<td>
									&nbsp;
									</td>
									<td width="30%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" alt="">&nbsp;
										查看附件
									</td>
									<td width="70">
										<img src="<%=root%>/images/ico/guanbi.gif" width="15" height="15" alt="">
										<span class="hand" onclick="closeDoc();">关闭</span>
									</td>
									<td width="23">
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="100%" valign="top" height="100%">
							<table width="100%" height="100%" align="center" border="0" cellpadding="0" cellspacing="1" class="table1" style="padding-top: 0;margin-top: 0">
								<tr>
									<td width="100" nowrap class="biao_bg1" align="right">
										<span class="wz">标题：</span>
									</td>
									<td nowrap class="td1">
										<s:textfield id="fileName" name="fileName" maxlength="256" cssStyle="width: 80%;" readonly="true"></s:textfield>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="2" valign="top" height="100%">
									<%--	<object id="TANGER_OCX_OBJ" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=4,0,3,2" width="100%" height="850">
											<param name="ProductCaption" value="思创数码科技股份有限公司">
											<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
											<param name="BorderStyle" value="1">
											<param name="TitlebarColor" value="42768">
											<param name="TitlebarTextColor" value="0">
											<param name="TitleBar" value="false">
											<param name="MenuBar" value="false">
											<param name="Toolbars" value="true">
											<param name="IsResetToolbarsOnOpen" value="true">
											<param name="IsUseUTF8URL" value="true">
											<param name="IsUseUTF8Data" value="true">
											<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
										</object>
										--%>
										<script type="text/javascript">
											document.write(OfficeTabContent);
										</script>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</s:form>
	</body>
</html>
