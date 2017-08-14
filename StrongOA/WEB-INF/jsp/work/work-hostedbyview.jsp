<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp" %>
		<title>查看主办工作</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    	<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
    	<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
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
		  //返回到主办任务列表
	      function goBack(){
	           location = "<%=root%>/fileNameRedirectAction.action?toPage=work/work-hostedbymain.jsp";
	      }
	      //初始化设置表单只读
	      function initial(){
	      	var FormInputOCX = document.getElementById("FormInputOCX");
	        FormInputOCX.SetFieldsReadOnly(true);
	      }
	      //催办
	      function sendReminder(){
			var returnValue = "";
			var overDate = '${endDate}';
			if(overDate != "" && overDate!=null){
				 alert("流程已结束，无需催办！");
				 return;
			}
			OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=work/work-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
			//goBack();
			                                   
		 }
	    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">	
	<form action="<%=root %>/work/work!list.action" method="post">
	  <!-- 电子表单模板id -->
	  <s:hidden id="formId" name="formId"></s:hidden>
	  <!-- 任务id -->
	  <s:hidden id="taskId" name="taskId"></s:hidden>
	  <!-- 流程实例id -->
	  <s:hidden id="instanceId" name="instanceId"></s:hidden>
	  <!-- 业务表名称 -->
	  <s:hidden id="tableName" name="tableName"></s:hidden>
	  <!-- 业务表主键名称 -->
	  <s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
	  <!-- 业务表主键值 -->
	  <s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
	  </form>
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"
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
									工作处理单
								</td>
								<td>
									&nbsp;
								</td>
				                <td >
				                  <a class="Operation" href="#" onclick="viewPDImage();"><img
				                                    src="<%=root%>/images/ico/chakan.gif" width="15"
				                                    height="15" class="img_s">查看流程图&nbsp;</a>
				                </td>
				                <td width="5"></td>
				                 <td >
				                  <a class="Operation" href="#" onclick="JavaScript:sendReminder();"><img
				                                    src="<%=root%>/images/ico/cuiban.gif" width="15"
				                                    height="15" class="img_s">催办&nbsp;</a>
				                </td>
				                <td width="5"></td>
								<td >
									<a class="Operation"  href="#" onclick="goBack();"><img
                                    src="<%=root%>/images/ico/ht.gif" width="15"
                                    height="15" class="img_s">返回&nbsp;</a>
								</td>
								<td width="5">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
		          <td align="center"><object height="850" width="100%"
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
				<tr>
					<td height="40" align="center">
						<input name="Submit" type="button" class="input_bg" value="查看流程图" onclick="viewPDImage();">
						<input name="Submit" type="button" class="input_bg" value="催 办" onclick="sendReminder();">
						<input name="Submit2" type="button" class="input_bg" value="返 回" onclick="goBack();">     
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>