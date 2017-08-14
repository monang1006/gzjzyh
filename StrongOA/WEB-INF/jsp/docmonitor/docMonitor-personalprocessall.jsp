<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>已办流程列表</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/workflow/common.js"
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
     //重定向到此页面
      function reloadPage() {
      	window.location = "<%=root%>/work/work!listprocessed.action?state="+$("#state").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&formId="+$("#formId").val()+"&workflowType=${workflowType}&excludeWorkflowType=${excludeWorkflowType}&handleKind=${handleKind}";
      }	      
        //返回到此页面的父页面
        function goBack(){
        	window.location.reload();
        }
    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<%--
                <td width="20%">
                  &nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
                    alt="">
                 	已办事宜列表
                </td>
               --%>
								<td width="75%" >
									<table border="0" align="right" cellpadding="00"
										cellspacing="0">
										<tr>
											<td>
												<a class="Operation" href="javascript:gotoView();"><img
														src="<%=root%>/images/ico/page.gif" width="15" height="15"
														class="img_s">查阅&nbsp;</a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="javascript:Cur_workflowView();"><img
														src="<%=root%>/images/ico/chakanlishi.gif" width="15"
														height="15" class="img_s">办理记录&nbsp;</a>
											</td>
											<td width="5"></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<s:form name="myTableForm"
							action="/docmonitor/docMonitor!processAll.action">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="taskId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty"
								collection="${page.result}" page="${page}"
								pagename="page">
								  <s:hidden name="param.grantCode"></s:hidden>
								<s:hidden name="workflowType" id="workflowType"></s:hidden>
								<s:hidden name="excludeWorkflowType" id="excludeWorkflowType"></s:hidden>
								<s:hidden name="state" id="state"></s:hidden>
								<s:hidden name="handleKind" id="handleKind"></s:hidden>
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="3%" align="center" class="biao_bg1">
											<input type='button' class='input_bg' value='搜 索' onclick="document.getElementById('myTableForm').submit();" />
											<%--
											<img id="img_search" style="cursor: hand;"
												src="<%=root%>/images/ico/sousuo.gif" width="17" height="16">
											--%>
										</td>
										<td width="100%" class="biao_bg1">
											<s:textfield name="param.businessName" title="请输入标题"
												cssClass="search"></s:textfield>
										</td>
									</tr>
								</table>
								<s:if test="%{state!=\"1\"}">
									<webflex:flexCheckBoxCol caption="选择" property="taskId"
										showValue="businessName" width="4%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="标题" property="taskId"
										showValue="businessName" onclick="getinfo(this.value)"
										width="61%" isCanDrag="false" isCanSort="false" showsize="35"></webflex:flexTextCol>
									<webflex:flexDateCol caption="发起时间"
										property="workflowStartDate" showValue="workflowStartDate"
										width="15%" isCanDrag="false" dateFormat="yyyy-MM-dd"
										isCanSort="false"></webflex:flexDateCol>
									<webflex:flexTextCol caption="当前办理人" property="actorName"
										showValue="actorName" width="20%" isCanDrag="false"
										isCanSort="false" showsize="8"></webflex:flexTextCol>
								</s:if>
								<s:else>
									<webflex:flexCheckBoxCol caption="选择" property="taskId"
										showValue="businessName" width="4%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="标题" property="taskId"
										showValue="businessName" onclick="getinfo(this.value)"
										width="51%" isCanDrag="false" isCanSort="false" showsize="30"></webflex:flexTextCol>
									<webflex:flexDateCol caption="发起时间"
										property="workflowStartDate" showValue="workflowStartDate"
										width="15%" isCanDrag="false" dateFormat="yyyy-MM-dd"
										isCanSort="false"></webflex:flexDateCol>
									<webflex:flexDateCol caption="办结时间" property="workflowEndDate"
										showValue="workflowStartDate" width="15%" isCanDrag="false"
										dateFormat="yyyy-MM-dd" isCanSort="false"></webflex:flexDateCol>
									<webflex:flexTextCol caption="主办人" property="mainActorName"
										showValue="mainActorName" width="15%" isCanDrag="false"
										isCanSort="false" showsize="8"></webflex:flexTextCol>
								</s:else>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
      var sMenu = new Menu();
      function initMenuT(){
        sMenu.registerToDoc(sMenu);
        var item = null;
        item = new MenuItem("<%=root%>/images/ico/page.gif","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	    item = new MenuItem("<%=root%>/images/ico/chakanlishi.gif","办理记录 ","Cur_workflowView",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item); 
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      
      
      
      //处理状态（查看流程图）
      function Cur_workflowView(){ 
      	  var taskId = getValue();
      	  if(taskId == ""){
      		alert("请选择要查看的流程！");
      		return ;
      	  }else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("一次只能查看一项流程！");
      			return ;
      		}
      	  }
      	  var info = getInfo(taskId);
      	  var instanceId = info[0];//获取流程实例ID     
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          WindowOpen("<%=root%>/fileNameRedirectAction.action?toPage=/work/work-container.jsp?instanceId="+instanceId+"&taskId="+taskIds+"&type=processurgency",'Cur_workflowView',width, height, "办理记录");
      }
      //获取列表中勾选中的信息
      function getInfo(id){
          var info = new Array();
          <c:forEach items="${page.result}" var="obj" varStatus="status">
         	var bid = '<c:out value="${obj.taskId}"/>';//任务实例ID
         	if(bid == id){
         	   info[0] = '<c:out value="${obj.instanceId}"/>';//流程实例ID
         	   info[1] = '<c:out value="${obj.workflowType}"/>';//流程类型ID
         	   info[2] = '<c:out value="${obj.businessName}"/>';//标题
         	   info[3] = '<c:out value="${obj.workflowName}"/>';//标题
         	   info[4] = '<c:out value="${obj.workflowEndDate}"/>';//标题
         	 }
          </c:forEach>
          return info;
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
   		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewform.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI(info[3])),'todo',width, height, "查阅");
      }
  //得到相关信息【通过任务ID和流程ID链接】
function getinfo(workId){
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
   		  var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewform.action?taskId="+taskId+"&instanceId="+instanceId+"&workflowName="+encodeURI(encodeURI(info[3])),'todo',width, height, "查阅");
}
    </script>
	</body>
</html>
