<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp" %>
		<title>查看流程</title>
		<link href="<%=frameroot%>/css/properties_windows.css"
			type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/Silverlight/Silverlight.js" type="text/javascript"></script>
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
		//查看时所有控件都要设为只读
		function initial(){
			$("#taskId").val("");//通过流程实例id得到意见,避免通过任务id去获取
			formReader = getFormReader();
			/*
			 * 上传领导批示单不再使用附件控件形式，去掉对附件控件为空的判断 yanjian 2012-02-24 12:56
			 * */
			if(formReader.GetFormControl("doneSuggestion_content")){
				var doneSuggestion_contentIsExist = (formReader.GetFormControl("doneSuggestion_content").Value != ""?true:false)
				if(doneSuggestion_contentIsExist){
					formReader.SetFormTabVisibility("attachName",true);
					formReader.SetFormTabVisibility("doneSuggestion",true);
				}
			}
			var ret = formReader.SetFormReadOnly(true);// 设置表单只读
		}
	    //查看办理记录
	  function annal(){
		 var taskId = $("#taskId").val();
		 OpenWindow(contextPath+"!annallist.action?taskId=${taskId}", 
	                                   500, 300, window);
	  }
	  
	  function validation(){
	  	var mytableName = $("#tableName").val();
	  	var mypkFieldName = $("#pkFieldName").val();
	  	var mypkFieldValue = $("#pkFieldValue").val();
	  	OpenWindow(contextPath+"!validation.action?tableName="+mytableName+"&pkFieldName="+mypkFieldName+"&pkFieldValue="+mypkFieldValue,
	  			750,360,window);
	  }
	  
	  //返回	
      function goBack(){
      /*
      	  var parentWin = window.opener || window.dialogArguments;//父窗口
          if(typeof(parentWin.reloadPage) != "undefined"){
			parentWin.reloadPage();
		  }
		  */
    	window.close();
      }
      
      //处理状态（查看流程图）
      function Cur_workflowView(flag){
      	 var width=screen.availWidth-10;;
     	 var height=screen.availHeight-30;
     	 WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+$("#instanceId").val()+"&taskId="+${taskId}+"&flag="+flag,'Cur_workflowView',width, height, "");
         //var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+$("#instanceId").val()+"&taskId="+${taskId}+"&flag="+flag, 
           //                        width, height, window);
      }
      
       
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
	<base target="_self"/>
	<body class="contentbodymargin" onload="window.focus();">
	 <form id="form" action="<%=root %>/work/work!list.action" method="post">
	  <!-- 业务数据名称 -->
			<s:hidden id="businessName" name="businessName"></s:hidden>
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
			<!-- 电子表单V2.0 调用方法名参数 -->
			<s:hidden id="formAction" name="formAction"></s:hidden>
			<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
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
									流程处理单
								</td>
								<td>
									<table align="right">
									<tr>
	               				<!--
	               				 <td >
				                  <a class="Operation" href="#" onclick="JavaScript:validation();"><img
				                                    src="<%=root%>/images/ico/chakan.gif" width="15"
				                                    height="15" class="img_s">验证二维码&nbsp;</a>
				                </td>
				                  -->
				                <td width="5"></td>
				                <td >
				                  <a class="Operation" href="#" onclick="JavaScript:Cur_workflowView('00000');"><img
				                                    src="<%=root%>/images/ico/chakanlishi.gif" width="15"
				                                    height="15" class="img_s">办理记录&nbsp;</a>
				                </td>
				                 <td width="5"></td><%--
				                 <td >
				                  <a class="Operation" href="#" onclick="JavaScript:sendReminder();"><img
				                                    src="<%=root%>/images/ico/chakan.gif" width="15"
				                                    height="15" class="img_s">催办&nbsp;</a>
				                </td>
				                --%><%--
				                 <security:authorize ifAnyGranted="001-0002000100050001, 001-0001000100050001">
				                <td width="5"></td>
				                <td >
				                  <a class="Operation" href="#" onclick="JavaScript:fetchTask();"><img
				                                    src="<%=root%>/images/ico/fankuihuizhi.gif" width="15"
				                                    height="15" class="img_s">取回工作&nbsp;</a>
				                </td>
				                </security:authorize>
				                --%><%--<td width="5"></td>
				                <td >
				                  <a class="Operation" href="#" onclick="JavaScript:annal();"><img
				                                    src="<%=root%>/images/ico/chakan.gif" width="15"
				                                    height="15" class="img_s">办理记录&nbsp;</a>
				                </td>
				                <td width="5"></td>
				                --%>
				                <td >
				                  <a class="Operation" href="#" onclick="JavaScript:doPrintForm();"><img
				                                    src="<%=root%>/images/ico/tb-print16.gif" width="15"
				                                    height="15" class="img_s">打印处理单&nbsp;</a>
				                </td>
				                <td width="5"></td>
								<td >
									<a class="Operation" href="javascript:goBack();"><img
                                    src="<%=root%>/images/ico/guanbi.gif" width="15"
                                    height="15" class="img_s">关闭&nbsp;</a>
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