<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看附件</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/OfficeControl/officecontrol.js" type="text/javascript"></script>
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
      
      
      function openFromURL(tempfileId) {
          var tfileAppedId=$("#tfileAppedId").val();
          var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          TANGER_OCX_OBJ.OpenFromURL("<%=root%>/archive/tempfile/tempFile!openAnnex.action?tempfileId="+tempfileId+"&tfileAppedId="+tfileAppedId);
      }
      
      function closeDoc() {
        window.returnValue = "OK";
        window.close();
      }
      
      $(document).ready(function(){			  
			  initWordOCX();
			  var tempfileId = $("#tempfileId").val()
			  if ((tempfileId != "") && (tempfileId != undefined)) {
			     openFromURL($("#tempfileId").val());
			  }
			}); 
    </script>
    
	</head>
	<base target="_self">
	<body class="contentbodymargin" oncontextmenu="return false;">
		<s:form id="senddocForm" name="senddocForm"
			action="" enctype="multipart/form-data"
			method="post">
			<s:hidden id="tempfileId" name="model.tempfileId"></s:hidden>
			<s:hidden id="tfileAppedId" name="tfileAppedId"></s:hidden>
			<s:hidden id="listMode" name="listMode"></s:hidden>
			<s:file id="wordDoc" name="wordDoc" cssStyle="display:none;"></s:file>
			<div id="contentborder" align="center">
				<table width="100%" height="100%" border="0" cellspacing="0"
					cellpadding="0" style="vertical-align: top;">
					<tr>
						<td width="100%" class="tabletitle" height="40">
							<table width="100%" border="0" align="right" cellpadding="0"
								cellspacing="0">
								<tr>
									<td width="50%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
											alt="">&nbsp;
										查看附件
									</td>
									<td width="*">
										&nbsp;
									</td>		
									<td width="20">
										<img src="<%=root%>/images/ico/guanbi.gif" width="14"
											height="14" alt="">
									</td>
									<td width="34">
										<span class="hand" onclick="closeDoc();">关闭</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td width="100%" valign="top" height="100%">
							<table width="100%" height="100%" align="center" border="0"
								cellpadding="0" cellspacing="1" class="table1" style="padding-top: 0;margin-top: 0">
								<tr>
									<td width="100" nowrap class="biao_bg1" align="right">
										<span class="wz">标题：</span>
									</td>
									<td nowrap class="td1">
										<s:textfield id="fileFileName" name="fileFileName"
											maxlength="256" cssStyle="width: 80%;" readonly="true"></s:textfield>
										&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="5" valign="top" height="100%">
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
