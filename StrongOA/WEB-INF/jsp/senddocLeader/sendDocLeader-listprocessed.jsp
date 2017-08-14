<%@page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>已办工作列表</title>
    <link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
    <link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <link href="<%=frameroot%>/css/search.css" type="text/css" rel="stylesheet">
    <script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js"  type="text/javascript"></script>    
    <script src="<%=path%>/common/js/common/common.js"  type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>   
    <script src="<%=path%>/oa/js/workflow/common.js" type="text/javascript"></script>
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
     //重定向到此页面
      function reloadPage() {
      	//window.location = "<%=root%>/work/work!processed.action?state="+$("#state").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&formId="+$("#formId").val()+"&workflowType=${workflowType}&excludeWorkflowType=${excludeWorkflowType}&handleKind=${handleKind}";
      	$("#img_search").click();
      }	      
        //返回到此页面的父页面
        function goBack(){
       	 reloadPage();
        }
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
  <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td height="40" class="tabletitle">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
              <td width="20%">
                &nbsp; <img src="<%=frameroot%>/images/ico.gif" width="9" height="9"  alt=""/>  
                	<s:if test="%{state!=\"1\"}">在办跟踪</s:if>
                	<s:else>办结事宜</s:else>
                </td>
                <td width="75%">
                  <table border="0" align="right" cellpadding="00" cellspacing="0">
                  <tr>
                  <td >
	                  <a class="Operation" href="javascript:gotoView();"><img
	                      src="<%=frameroot%>/images/chakan.gif" width="15" height="15"
	                      class="img_s">查阅&nbsp;</a> 
	                </td>
	                <td width="5"></td>
                  	<td >
	                  <a class="Operation" href="javascript:Cur_workflowView();"><img
	                      src="<%=frameroot%>/images/banli.gif" width="15" height="15"
	                      class="img_s">办理记录&nbsp;</a> 
	                </td>
	                <td width="5"></td>
	                 <!--<s:if test="%{state!=\"1\"}">
	                <td ><a  class="Operation" href="javascript:fetchTask();" >
	                <img class="img_s" src="<%=frameroot%>/images/fankuihuizhi.gif" width="15" height="15">取回流程&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
                  	</s:if>-->
                   <!--  <td >
	                  <a class="Operation" href="javascript:tjst();">&nbsp;统计视图&nbsp;</a> 
	                </td> -->
	                <td width="5"></td>
	                <%--
	                  <td ><a  class="Operation" href="javascript:sendReminder();"><img class="img_s" src="<%=frameroot%>/images/cuiban.gif" width="15" height="15" alt="">催办&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
                  	--%><%--<security:authorize ifAnyGranted="001-0002000100050001, 001-0001000100050001">
		                <td ><a  class="Operation" href="javascript:fetchTask();" ><img class="img_s" src="<%=frameroot%>/images/fankuihuizhi.gif" width="15" height="15">取回工作&nbsp;</a></td>
		                <td width="5">
		                  &nbsp;
		                </td>
		            </security:authorize>    
                  	--%></tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td height="100%">
          <s:form name="myTableForm" action="/work/work!listprocessed.action?noTotal=1">
              <webflex:flexTable name="myTable" width="100%" height="200px"
              wholeCss="table1" property="businessName" isCanDrag="true"
              isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
              getValueType="getValueByProperty" collection="${pageWorkflow.result}"
              page="${pageWorkflow}" pagename="pageWorkflow">
              <s:hidden name="workflowType" id="workflowType"></s:hidden>
              <s:hidden name="excludeWorkflowType" id="excludeWorkflowType"></s:hidden>
               <s:hidden name="excludeWorkflowTypeName" id="excludeWorkflowTypeName"></s:hidden>
              <s:hidden name="state" id="state"></s:hidden>
              <s:hidden name="handleKind" id="handleKind"></s:hidden>
              <table width="100%" border="0" cellpadding="0" cellspacing="1"
                class="table1">
                <tr>
                  <td width="3%" align="center" class="biao_bg1">
                     <img id="img_search" style="cursor: hand;" src="<%=frameroot%>/images/sousuo.gif" width="17"
                      height="16">
                  </td>
                  <td width="37%" class="biao_bg1">
                    <s:textfield name="businessName" title="请输入标题" cssClass="search"></s:textfield> 
                  </td>
                  <%--
                  <td width="15%" class="biao_bg1">
                    <s:textfield name="userName" title="请输入主办人" cssClass="search"></s:textfield> 
                  </td>
                  --%>
	                  <td width="21%" align="center" class="biao_bg1">
		                   <strong:newdate  name="startDate" id="startDate" width="98%"
		                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${startDate}" classtyle="search" title="输入发起开始时间"></strong:newdate>
		                   <%-- 
	                   <input type="text" name="startDate" id="startDate" width="100%" class = "search" onfocus="WdatePicker()" title="输入发起开始时间">
	                  --%>
	                  </td>
	                  <td width="21%" align="center" class="biao_bg1">
	                    	<strong:newdate  name="endDate" id="endDate" width="98%"
	                      skin="whyGreen" isicon="true" dateform="yyyy-MM-dd" dateobj="${endDate}" classtyle="search" title="输入发起结束时间"></strong:newdate>
	                    <%--  
	                  <input type="text" name="endDate" id="endDate" width="100%" class = "search" onfocus="WdatePicker()" title="输入发起结束时间">
	                  --%>
	                  </td>
                 
                </tr>
              </table>
               <s:if test="%{state!=\"1\"}">
	               <webflex:flexCheckBoxCol caption="选择" property="taskId"
										showValue="businessName" width="4%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
	              <webflex:flexTextCol caption="标题"
										property="taskId" showValue="businessName" onclick="getinfo(this.value)" showsize="35"
										width="64%" isCanDrag="false" isCanSort="false"></webflex:flexTextCol>
				 <webflex:flexDateCol caption="发起时间"
										property="workflowStartDate" showValue="workflowStartDate"
										width="15%" isCanDrag="false"
										dateFormat="yyyy-MM-dd" isCanSort="false"></webflex:flexDateCol>
				 <webflex:flexTextCol caption="当前办理人"
										property="actorName" showValue="actorName"
										width="17%" isCanDrag="false" isCanSort="false" showsize="8"></webflex:flexTextCol>
               </s:if>
               <s:else>
 	              <webflex:flexCheckBoxCol caption="选择" property="taskId"
										showValue="businessName" width="4%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
	              <webflex:flexTextCol caption="标题"
										property="taskId" showValue="businessName" onclick="getinfo(this.value)" showsize="30"
										width="54%" isCanDrag="false" isCanSort="false"></webflex:flexTextCol>
				 <webflex:flexDateCol caption="发起时间"
										property="workflowStartDate" showValue="workflowStartDate"
										width="15%" isCanDrag="false"
										dateFormat="yyyy-MM-dd" isCanSort="false"></webflex:flexDateCol>
				<webflex:flexDateCol caption="办结时间"
										property="workflowEndDate" showValue="workflowEndDate"
										width="15%" isCanDrag="false"
										dateFormat="yyyy-MM-dd" isCanSort="false"></webflex:flexDateCol>
	              <webflex:flexTextCol caption="主办人"
										property="mainActorName" showValue="mainActorName"
										width="12%" isCanDrag="false" isCanSort="false" showsize="8"></webflex:flexTextCol>
               </s:else>
           	</webflex:flexTable>
           </s:form>
          </td>
        </tr>
      </table>
      	<%--<div align="center" height="50%">
			<iframe id="blank" name="frame_query" width="100%" src="<%=path %>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0"  scrolling="no"></iframe>
		</div>	
      
    --%></div>
    <script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
        item = new MenuItem("<%=frameroot%>/images/chakan.gif","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	    item = new MenuItem("<%=frameroot%>/images/banli.gif","办理记录 ","Cur_workflowView",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item); 
        <security:authorize ifAnyGranted="001-0002000100050001, 001-0001000100050001"></security:authorize>
        /// <s:if test="%{state!=\"1\"}">
	    //item = new MenuItem("<%=frameroot%>/images/fankuihuizhi.gif","取回流程","fetchTask",1,"ChangeWidthTable","checkMoreDis");
	    //sMenu.addItem(item);
	    //</s:if>
        // item = new MenuItem("<%=frameroot%>/images/cuiban.gif","催办","sendReminder",1,"ChangeWidthTable","checkMoreDis");
	      //  sMenu.addItem(item);
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      
      function sendReminder(){
		     var id=getValue();
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
			 if(checkSelectedOneDis()){
			  var info = getInfo(id); 
			 	var overDate = info[1];
			 	var instanceId = info[0];
			 	if(overDate != "" && overDate!=null){
			 		alert("流程已结束,无需催办！");
			 		return;
			 	}
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=work/work-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
			 }
			}
      
      
      
      //处理状态（查看流程图）
      function Cur_workflowView(){ 
      	  var taskId = getValue();
      	  if(taskId == ""){
      		alert("请选择要查看的工作！");
      		return ;
      	  }else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("一次只能查看一项工作！");
      			return ;
      		}
      	  }
      	  var info = getInfo(taskId);
      	  var instanceId = info[0];//获取流程实例ID     
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
         // var ReturnStr=OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+instanceId, 
          //                         width, height, window);
          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+instanceId+"&taskId="+taskIds,'Cur_workflowView',width, height, "办理记录");
          //var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+instanceId+"&taskId="+taskIds, 
            //                       width, height, window);
      }
      //获取列表中勾选中的信息
      function getInfo(id){
          var info = new Array();
          <c:forEach items="${pageWorkflow.result}" var="obj" varStatus="status">
         	var bid = '<c:out value="${obj.taskId}"/>';//任务实例ID
         	if(bid == id){
         	   info[0] = '<c:out value="${obj.instanceId}"/>';//流程实例ID
         	   info[1] = '<c:out value="${obj.workflowType}"/>';//流程类型ID
         	   info[2] = '<c:out value="${obj.businessName}"/>';//标题
         	   info[3] = '<c:out value="${obj.workflowName}"/>';//标题
         	 }
          </c:forEach>
          return info;
       }
      //取回任务
      function fetchTask(){
      	var taskId = getValue();
      	if(taskId == ""){
      		alert("请选择要取回的工作！");
      		return ;
      	}else{
      		if(taskId.split(",").length>1){
      			alert("一次只能取回一项工作！");
      			return ;
      		}
      		if(confirm("取回选定的工作，确定？")){
	      		doFetchTask(taskId);
	      	}	
      	}
      }
            //查阅
      function gotoView(){
      	var taskId = getValue();
      	if(taskId == ""){
      		alert("请选择要查阅的公文！");
      		return ;
      	}else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("一次只能查阅一份公文！");
      			return ;
      		}
      	}
      	var info = getInfo(taskId);
      	var instanceId = info[0];//获取流程实例ID
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		WindowOpen("<%=root%>/senddocLeader/sendDocLeader!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,'processed',width, height, "");
      }
       function getInstanceId(taskId){
      	var info = new Array();
      	<s:iterator value="pageWorkflow.result" var="obj">
      		if(taskId == '${obj.taskId}'){
      			info[0] = '${obj.instanceId}';//流程实例id
      			info[1] = taskId;
      		}
      	</s:iterator>
      	return info ;
      }
  //得到相关信息【通过任务ID和流程ID链接】
function getinfo(workId){
/*
	var info = getInstanceId(workId);
	var instanceId = info[0];
	 var fullContextPath = $("form").attr("action");
  	var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
	document.getElementById('blank').contentWindow.setWorkId(workId,instanceId,contextPath); 
	*/
	var taskId = workId;
      	if(taskId == ""){
      		alert("请选择要查阅的公文！");
      		return ;
      	}else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("一次只能查阅一份公文！");
      			return ;
      		}
      	}
      	var info = getInfo(taskId);
      	var instanceId = info[0];//获取流程实例ID
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		WindowOpen("<%=root%>/senddocLeader/sendDocLeader!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,'processed',width, height, "");
   		//var ret=OpenWindow("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,width, height, window);
}
function tjst(){
	window.location.href="<%=path%>/senddocLeader/sendDocLeader!processedWorkflow.action?filterSign=${filterSign}&state=${state}&workflowType=${workflowType}&excludeWorkflowType=${excludeWorkflowType}&handleKind=${handleKind}";
}
    </script>
  </body>
</html>
