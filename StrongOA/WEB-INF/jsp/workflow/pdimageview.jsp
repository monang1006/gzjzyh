<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.uums.optprivilmanage.BaseOptPrivilManager"/>
<jsp:directive.page import="com.strongmvc.service.ServiceLocator"/>
<jsp:directive.page import="com.strongit.oa.common.workflow.IWorkflowService"/>
<jsp:directive.page import="com.strongit.workflow.bo.TwfBaseNodesetting"/>
<%@ taglib uri="/tags/strongitJbpm" prefix="strongit"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	String flag = request.getParameter("flag");
	String supProcessInstanceId = (String)request.getParameter("supProcessInstanceId");
	if(supProcessInstanceId == null){
		supProcessInstanceId = "";
	}
System.out.println(flag);
System.out.println(supProcessInstanceId);
	String extendFun = "";
	if(flag != null){
		if("00000".equals(flag)){//参数从已办流程中传递过来
			BaseOptPrivilManager pm = (BaseOptPrivilManager)ServiceLocator.getService("baseOptPrivilManager");
			String sysCode = "001-0001000100120001";//退回功能的资源权限编码
			if(pm.checkPrivilBySysCode(sysCode)){
				extendFun = "退回,doBackSpace";
			}
		} else if("00001".equals(flag)){//从senddoc-input.jsp中传递过来的参数
			//IWorkflowService workflowService = (IWorkflowService)ServiceLocator.getService("workflowService");
			//String taskId = request.getParameter("taskId");
			//String nodeId = workflowService.getNodeIdByTaskInstanceId(taskId);
			//TwfBaseNodesetting nodeSet = workflowService.getNodesettingByNodeId(nodeId);
			//String transitionName = nodeSet.getPlugin("plugins_chkModifySuggestion");
			//if(transitionName != null && "1".equals(transitionName)) {//允许修改意见
			//	extendFun = "退回,doBackSpace";
			//}
		}
	}
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看流程图</title>
		<link href="<%=frameroot%>/css/windows.css" type="text/css" rel="stylesheet">
<%--		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>--%>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<script type="text/javascript">
	    $(document).ready(function(){
		//$(".Operation_input").css("background","url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;");
		
		});
  </script>
		
		

		
		
		<script type="text/javascript">
		var supProcessInstanceId = '<%=supProcessInstanceId%>';
		//退回
		function doBackSpace(processInstanceId,nodeId) {
			$.post("<%=path%>/senddoc/sendDoc!findCurrentTask.action",
					{instanceId:processInstanceId,nodeId:nodeId},function(ret){
						if(ret != ""){
							var taskIds = ret.split(",");
							var taskId = taskIds[0];
							var width=screen.availWidth-10;
						    var height=screen.availHeight-30;
							var ReturnStr=OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+taskId+"&type=return", 
								                                   width, height, window);
							if(ReturnStr){
								//var ret = OpenWindow(scriptroot + "/fileNameRedirectAction.action?toPage=workflow/initback.jsp",400, 300, window);
								var ret =showModalDialog(Url, 
		                                window, 
		                                "dialogWidth:400pt;dialogHeight:300pt;"+
		                                "status:no;help:no;scroll:no;");
								if(ret){
								    $.post("<%=path%>/senddoc/sendDoc!back.action",{taskId:taskId,returnNodeId:ReturnStr,suggestion:encodeURI(ret),instanceId:processInstanceId,nodeId:nodeId},
										   function(retCode){
										   		if(retCode == "0"){
										   			alert("退回成功！");
										   			window.returnValue = "ok";
										   			window.close();
										   		}else if(retCode == "-1"){
										   			alert("任务实例不存在或已删除！");
										   		}else if(retCode == "-2"){
										   			alert("任务退回过程中出现异常！");
										   		}else if(retCode == "-3"){
										   			alert("任务退回过程中出现异常！");
										   		}
										   }			
									);
								}                                   
							}	
						}else{
							alert("请在流程图中框中区域点击【退回】");
							return ;
						}
					})
                                   
		}
		
		//编辑处理意见
		function editDesc(processInstanceId,nodeId) {
			var ret = OpenWindow("<%=path%>/workflowDesign/action/processMonitor!annalList.action?processId="+processInstanceId+
								 "&nodeId="+nodeId,
								 500, 400, window);
			if(ret){
				location.reload();
			}								 
		}
		
		function viewSub(processInstanceId,nodeId,subProcessInstanceId){
			supProcessInstanceId += ("," + processInstanceId) ;
			if(supProcessInstanceId.indexOf(",") == 0){
				supProcessInstanceId = supProcessInstanceId.substring(1);
			}
			window.location.href="<%=path%>/senddoc/sendDoc!PDImageView.action?instanceId="+subProcessInstanceId+"&supProcessInstanceId="+supProcessInstanceId;
		}
		
		function backView(){
			var last = supProcessInstanceId.lastIndexOf(",");
			if(supProcessInstanceId !=""){
				var instanceId = "";
				var subProcessInstanceId = "";
				if(last != -1){
					 instanceId = supProcessInstanceId.substring(last+1);
					 subProcessInstanceId = supProcessInstanceId.substring(0,last);
				}else{
					 instanceId = supProcessInstanceId;
					 subProcessInstanceId = "";
				}
				window.location.href="<%=path%>/senddoc/sendDoc!PDImageView.action?instanceId="+instanceId+"&supProcessInstanceId="+subProcessInstanceId;
			}
		}
	</script>

	</head>
	<body class="contentbodymargin">
		<!--  oncontextmenu="return false;"> -->
		<div id="contentborder" align="center">

			<table width="98%">
				<tr>
					<td height="5"></td>
				</tr>
			</table>
			     <table width="98%">
			     <tr><%--
			     
			     
			     <input id="supProcessInstanceId" type="button" value="返回" style="display: none;font-size: 12pt"  onclick="backView();">
			     
			     --%>
			     
			   <td id="supProcessInstanceId" align="right">
				<table border="0" align="right" cellpadding="0" cellspacing="0">
			     <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
				 <td class="Operation_input" style="cursor: pointer;background:url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;" onclick="backView();">&nbsp;返&nbsp;回&nbsp;</td>
				 <td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				 <td width="5"></td>
				     </table>
				     </td>
			     </tr>
			     <script type="text/javascript">
				     if(supProcessInstanceId == ""){
						document.getElementById("supProcessInstanceId").style.display = "none";
					}else{
						document.getElementById("supProcessInstanceId").style.display = "block";
					}
			     </script>
			     </tr>
        <tr>
          <td align="center">
            <%
            String tokenInstanceId = (String) request.getAttribute("token");
            System.out.println(tokenInstanceId);
            %>
            <strongit:processImagePopToken
              token="<%=Long.parseLong(tokenInstanceId)%>" isurger="0" subprocessFunction="viewSub" extendsFunction="<%=extendFun %>"/>

          </td>
        </tr>

      </table>
			<input type="hidden" name="processId"
							value="${model[0].processInstanceId }">
		</div>
	</body>
</html>
<%--<script type="text/javascript">--%>
<%--	//显示层隐藏--%>
<%--	document.getElementById("processImage").onmouseover = function(myevent){--%>
<%--		var obj;--%>
<%--		if(typeof(event) != 'undefined'){--%>
<%--			obj = event.srcElement;--%>
<%--		}else{--%>
<%--			obj = myevent.target;--%>
<%--		}--%>
<%--		if(this == obj){--%>
<%--			changeDisplay();--%>
<%--		}--%>
<%--	}--%>
<%--</script>--%>
