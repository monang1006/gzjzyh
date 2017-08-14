<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp" %>
		<title>查看流程</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css"
			type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
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
	  //返回	
      function goBack(){
     	/*var parentWin = window.opener;//父窗口
		if(typeof(parentWin.reloadPage) != "undefined"){
			parentWin.reloadPage();
		}*/
        window.close();
      }
      
       //催办
      function sendReminder(){
			 var returnValue = "";
			 	var state = '${state}';
			 	if(state == "1"){
			 		alert("流程已结束，无需催办！");
			 		return;
			 	}
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
		}
      
       //初始化设置表单只读
      function initial(){
      	$("#taskId").val("");//通过流程实例id得到意见,避免通过任务id去获取
      	formReader = getFormReader();
		var ret = formReader.SetFormReadOnly(true);// 设置表单只读
		var control = formReader.GetFormControl("Button_viewformdata");//将“查看原表单按钮”不设置为只读,"Button_viewformdata"为“查看原表单按钮”特有的name
					if(control!=null){
						control.SetProperty ('ReadOnly',false);
			}
      }
      
       //处理状态（查看流程图）
      function Cur_workflowView(){   
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+ $("#instanceId").val()+"&taskId=${taskId}&type=processurgency",'Cur_workflowView',width, height, "办理记录");
      }
      
    </script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="window.focus();">	
	<form id="form" action="<%=root %>/senddoc/sendDoc!list.action" method="post">
			<!-- 业务数据标识 tableName;pkFieldName;pkFieldValue  -->
			<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
	  		<!-- 业务数据名称 -->
			<s:hidden id="businessName" name="businessName"></s:hidden>
			<!-- 电子表单模板id -->
			<s:hidden id="formId" name="formId"></s:hidden>
			<!-- 任务id -->
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<!-- 流程实例ID -->
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<!-- 父流程实例ID -->
			<s:hidden id="parentInstanceId" name="parentInstanceId"></s:hidden>
			 <!-- 查看原表单相关 -->
			 <s:hidden id="personDemo" name="personDemo"></s:hidden>
			 <!-- PDF正文信息描述 -->
			 <s:hidden id="pdfContentInfo" name="pdfContentInfo"></s:hidden>
			 <!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- 电子表单V2.0 调用方法名参数 -->
			<s:hidden id="formAction" name="formAction"></s:hidden>
	  </form>
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">				
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<strong>流程处理单</strong>
								</td>
								<td>
									<table align="right">
									<tr>
	                			<td >
				                  <a class="button" href="#" onclick="JavaScript:Cur_workflowView();">办理记录</a>&nbsp;
				                </td>
				                <td width="5"></td>
				                <s:if test="viewReturn!=1&&state!=1">
				                <td >
				                  <a class="button" href="#" onclick="JavaScript:sendReminder();">催办</a>&nbsp;
				                </td>
				                <td width="5"></td>
				                </s:if>
				                <td >
				                  <a class="button" href="#" onclick="JavaScript:doPrintForm();">打印处理单</a>&nbsp;
				                </td>
				                <td width="5"></td>
								<td >
									<a class="button" href="#" onclick="javascript:goBack();">关闭</a>&nbsp;
								</td>
								<td width="5">
									&nbsp;
								</td>
								</tr>
					           </table>   
					           </td>  
								
							</tr>
						</table>
					</td>
				</tr>
				<tr>
						<td height="100%">				  
				               <div id="silverlightControlHost" style="position:relative;width: 100%;height: 100%">
							     	<object data="data:application/x-silverlight-2," type="application/x-silverlight-2" width="100%" height="100%" id="plugin">
									  <param name="source" value="<%=path%>/FormReader/StrongFormReader.xap"/>
									  <param name="onError" value="onSilverlightError" />
									  <param name="onLoad" value="onSilverlightLoad" />
									  <param name="background" value="white" />
									  <param name="minRuntimeVersion" value="4.0.50401.0" />
									  <param name="autoUpgrade" value="true" />
									  <a href="<%=path %>/detection/lib/Silverlight.exe" style="text-decoration:none">
							 			  <img src="<%=path %>/detection/images/SLMedallion_CHS.png" alt="Get Microsoft Silverlight" style="border-style:none"/>
									  </a>
								    </object><iframe id="_sl_historyFrame" style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
							  </div>
			           </td>
					</tr>
				
				</table>
		</div>
	</body>
</html>