<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <title>我的请求</title>
    <link href="<%=frameroot%>/css/properties_windows.css"
      type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/Silverlight/Silverlight.js"
			type="text/javascript"></script>
	<script src="<%=path%>/oa/js/eform/grid.js" type="text/javascript"></script>
    <script type="text/javascript">
    		
    	  //右键菜单实现
    	  function loadContentMenu(gridControl){
    	  	if(gridControl != null){
    	  		gridControl.PopupMenuCallbackCommand = "GridPopupMenuCallbackCommand";
    	  		gridControl.PopupMenuFontSize=18;
    	  		gridControl.AddPopupMenuItem("查阅","openDoc");
                gridControl.AddPopupMenuItem("办理状态", "workflowView");
                gridControl.AddPopupMenuItem("催办", "sendReminder");
    	  	}
    	  }
    	  
    		//重定向到此页面
	      function reloadPage() {
	      	window.location = "<%=root%>/component/formdata/formGridData!hostedby.action?type="+$("#type").val()+"&formId="+$("#formId").val()
      				+"&tableName="+$("#tableName").val();
	      }	
	         
	      function showSearchForm(){
	      	document.getElementById("iframe_search").contentWindow.loadFormTemplate();
	      }
	         
    	  //查看
	      function openDoc() {
              var bussinessId = getValue();
              if(bussinessId == ""){
              	alert("请选择要查看的流程！");
              	return ;
              }else{
              	var bids = bussinessId.split(",");
              	if(bids.length>1){
              		alert("一次只能查看一个流程！");
              		return ;
              	}
              }
              var result = bussinessId.split("$");
			  var instanceId = result[0];
			  var overDate = getValue("processEndDate");
			 	if(overDate != ""){
			 		overDate = "1";
			 	}
              var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+overDate,'hostedby',width, height,"");
	      }

           //处理状态（查看流程图）
	      function workflowView(){   
	      	  var bussinessId = getValue();
	      	  if(bussinessId == ""){
              	alert("请选择要查看的流程！");
              	return ;
              }else{
              	var bids = bussinessId.split(",");
              	if(bids.length>1){
              		alert("一次只能查看一个流程！");
              		return ;
              	}
              }
	          var result = bussinessId.split("$");
			  var instanceId = result[0];
	          var width=screen.availWidth-10;;
	          var height=screen.availHeight-30;
	           var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId=${taskId}", 
                                   width, height, window);
	      }
    </script>
  </head>
  <body class="contentbodymargin" scroll="no">
  <form id="form" name="form" method="post">
			<s:hidden name="formId"></s:hidden>
			<input id="formAction" type="hidden" name="formAction" />
			<s:hidden name="tableName"></s:hidden>
			<s:hidden name="type"></s:hidden>
		</form>
    <div id="contentborder"  align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="35">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td>
				  <table align="right">
					<tr>
					<%--<td >
	                  <a class="Operation" href="javascript:showSearchForm();"><img
	                      src="<%=root%>/images/ico/chakan.gif" width="15" height="15"
	                      class="img_s">显示查询条件&nbsp;</a> 
	                </td>
	                <td width="5">
	                --%><td >
	                  <a class="Operation" href="javascript:openDoc();"><img
	                      src="<%=root%>/images/ico/page.gif" width="15" height="15"
	                      class="img_s">查阅&nbsp;</a> 
	                </td>
	                <td width="5">
	                </td>
	                <td >
	                  <a class="Operation" href="javascript:workflowView();"><img
	                      src="<%=root%>/images/ico/chakan.gif" width="15" height="15"
	                      class="img_s">办理状态&nbsp;</a> 
	                </td>
	                <td width="5">
	                </td>
	                <td >
	                  <a class="Operation" href="javascript:sendReminder();"><img
	                      src="<%=root%>/images/ico/emergency.gif" width="15" height="15"
	                      class="img_s">催办&nbsp;</a> 
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
        </table>
        <iframe id="iframe_search" style="width: 100%;border: none;overflow: hidden;display: none;" src="<%=root%>/fileNameRedirectAction.action?toPage=component/formdata/formGridData-search.jsp"></iframe>
          <div id="silverlightControlHost"
			style="position:relative;height: 93%">
			<object data="data:application/x-silverlight-2,"
				type="application/x-silverlight-2" width="100%" height="100%"
				id="plugin">
				<param name="source"
					value="<%=path%>/GridFormReader/StrongFormReader.xap" />
				<param name="onError" value="onSilverlightError" />
				<param name="onLoad" value="onSilverlightLoad" />
				<param name="background" value="white" />
				<param name="minRuntimeVersion" value="4.0.50401.0" />
				<param name="autoUpgrade" value="true" />
				<a href="<%=path%>/detection/lib/Silverlight.exe"
					style="text-decoration:none"> <img
						src="<%=path%>/detection/images/SLMedallion_CHS.png"
						alt="Get Microsoft Silverlight" style="border-style:none" /> </a>
			</object>
			<iframe id="_sl_historyFrame"
				style="visibility:hidden;height:0px;width:0px;border:0px"></iframe>
		</div>
      
    </div>
    <iframe name="myiframe" src="" style="display:none;"> </iframe>
    <script language="javascript">
      //催办
      function sendReminder(){
		     var id=getValue();
			 var returnValue = "";
			 if(id == ""){
			 	alert("请选择要催办的流程！");
			 	return ;
			 }else{
			 	var ids = id.split(",");
			 	if(ids.length>1){
			 		alert("一次只能催办一个流程！");
			 		return ;
			 	}
			 }
		 	var result = id.split("$");
			var instanceId = result[0];
		 	var overDate = getValue("processEndDate");
		 	if(overDate != ""){
		 		overDate = "1";
		 	}
		 	if(overDate == "1"){
		 		alert("流程已结束，无需催办！");
		 		return;
		 	}
		 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
		}
   	
 	/**
		@param InstanceIdAndStatus 流程实例id$流程状态
	*/
	function ViewFormAndWorkflow(InstanceIdAndStatus) {
		var result = InstanceIdAndStatus.split("$");
		var instanceId = result[0];
		//var fullContextPath = $("form").attr("action");
  		//var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
  		//document.getElementById('blank').contentWindow.setWorkId("",instanceId,contextPath); 
  		
  		var width=screen.availWidth-10;
   			  var height=screen.availHeight-30;
   			  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewHostedBy.action?instanceId="+instanceId+"&state="+result[1],'hostedby',width, height,"");
  		
	}  
    </script>
  </body>
</html>
