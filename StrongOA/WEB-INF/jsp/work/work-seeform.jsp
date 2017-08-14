<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
	  <%@include file="/common/include/meta.jsp" %>
		<title>查看表单</title>
		<link href="<%=frameroot%>/css/properties_windows.css"
			type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
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
      $(document).ready(function(){
          var eform = document.getElementById("FormInputOCX");
          eform.InitialData('<%=basePath%>/services/EFormService?wsdl');
          eform.SetFormTemplateID($("#formId").val());
          //.width和.height必须是小写
		  eform.width=eform.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
  		  eform.height=eform.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
          eform.SetFieldsReadOnly(true);
      });
    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">	
	  <s:hidden id="formId" name="formId"></s:hidden>
	  <s:hidden id="docId" name="docId"></s:hidden>
	  <s:hidden id="bussinessId" name="bussinessId"></s:hidden>
	  <s:hidden id="taskId" name="taskId"></s:hidden>
	  <s:hidden id="instanceId" name="instanceId"></s:hidden>
	  <s:hidden id="isStartWorkflow" name="isStartWorkflow"></s:hidden>
	  <s:hidden id="tableName" name="tableName"></s:hidden>
	  <s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
	  <s:hidden id="formData" name="formData"></s:hidden>
		<div id="contentborder" align="center">
			<table width="90%" height="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">				
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									查看表单
								</td>
								<td>
									&nbsp;
								</td>
				                <td >
				                  <!--  a class="Operation" href="#" onclick="workflowView();"><img
				                                    src="<%=root%>/images/ico/chakan.gif" width="15"
				                                    height="15" class="img_s">查看流程图&nbsp;</a-->
				                </td>
				                <td width="5"></td>
								<td >
									 <!-- a class="Operation" href="#" onclick="window.close();"><img
                                    src="<%=root%>/images/ico/songshen.gif" width="15"  
                                    height="15" class="img_s">关 闭&nbsp;</a-->
								</td>
								<td width="5">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
          <td height="100%" align="center"><object height="850" width="90%"
              classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
              codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
              id="FormInputOCX"><param name="Visible" value="0" />
              <param name="AutoScroll" value="0" />
              <param name="AutoSize" value="0" />
              <param name="AxBorderStyle" value="1" />
              <param name="Caption" value="FormInputOCX" />
              <param name="Color" value="4278190095" />
              <param name="Font" value="MS Sans Serif" />
              <param name="KeyPreview" value="0" />
              <param name="PixelsPerInch" value="96" />
              <param name="PrintScale" value="1" />
              <param name="Scaled" value="-1" />
              <param name="DropTarget" value="0" />
              <param name="HelpFile" value="" />
              <param name="ScreenSnap" value="0" />
              <param name="SnapBuffer" value="10" />
              <param name="DoubleBuffered" value="0" />
              <param name="Enabled" value="-1" />
            </object>
          </td>
        </tr>
				
			</table>
		</div>
	</body>
</html>