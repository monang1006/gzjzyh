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
    <LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
    <link href="<%=frameroot%>/css/search.css" type="text/css" rel="stylesheet">
    <script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js"  type="text/javascript"></script>    
    <script src="<%=path%>/common/js/common/common.js"  type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>   
    <script src="<%=path%>/oa/js/workflow/common.js" type="text/javascript"></script>
    <script src="<%=path%>/oa/js/senddoc/exportExcel.js" type="text/javascript"></script>
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
  //转换时间格式(yyyy-MM-dd)--->(yyyyMMdd)
    function date2string(stime){
     	var arrsDate1=stime.split('-');
     	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
     	return stime;
     }   
	$(document).ready(function(){
       //搜索
       $("#img_sousuo").click(function(){
        //时间验证
        var state='${state}';
        if(state!=1){
		var stime = $("#startDate").val();
		var etime = $("#endDate").val();
			if(stime!=null && stime!="" && etime!=null && etime!=""){
				if(date2string(stime)>date2string(etime)){
					alert("发起开始时间不能大于发起结束时间。");
					return;
				}
			
			}
        }else if(state=1){
        	var stime = $("#workflowEndDatestartDate").val();
    		var etime = $("#workflowEndDateendDate").val();
    			if(stime!=null && stime!="" && etime!=null && etime!=""){
    				if(date2string(stime)>date2string(etime)){
    					alert("发起开始时间不能大于发起结束时间。");
    					return;
    				}
    			
    			}
        }
       		$("form").submit();
       });
	});
     //重定向到此页面
      function reloadPage() {
      	//window.location = "<%=root%>/work/work!processed.action?state="+$("#state").val()+"&workflowName="+encodeURI(encodeURI($("#workflowName").val()))+"&formId="+$("#formId").val()+"&workflowType=${workflowType}&excludeWorkflowType=${excludeWorkflowType}&handleKind=${handleKind}";
      //	$("#img_sousuo").click();
      }	      
        //返回到此页面的父页面
        function goBack(){
       	 reloadPage();
        }
    </script>
  </head>
  <body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
<!--  <script type="text/javascript"-->
<!--      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>-->
<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
    <div id="contentborder" align="center">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
        <tr>
          <td colspan="3" class="table_headtd">
            <table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
              <tr>
                <td class="table_headtd_img" >
					<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
				</td>
				<td align="left">
					<strong>
						<s:if test='%{state=="2" && workflowType=="3"}'>
						<strong>已办来文</strong>
						</s:if>
	                	<s:if test='%{state=="1" && workflowType=="3"}'>
	                	    <strong>办结来文</strong>
						</s:if>
						<s:if test='%{state=="2" && workflowType=="2"}'>
	                	  <strong>已审公文</strong>
						</s:if>
						<s:if test='%{state=="0"}'>
						<strong>已办事宜</strong>
						</s:if>
						<s:if test='%{state=="1" && workflowType!="2" && workflowType!="3"}'>
						<strong>办结事宜</strong>
						</s:if>
						<s:if test='%{state=="1" && workflowType=="2"}'>
	                	  <strong>办结公文</strong>
						</s:if>
					</strong>
				</td>
				<td align="right">
                  <table border="0" align="right" cellpadding="00" cellspacing="0">
                  <tr>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="relativeWf();"><img src="<%=root%>/images/operationbtn/urge.png"/>&nbsp;关&nbsp;联&nbsp;流&nbsp;程&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
	                 	<td width="5"></td>
                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
	                 	<td width="5"></td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="Cur_workflowView();"><img src="<%=root%>/images/operationbtn/Handle_Record.png"/>&nbsp;办&nbsp;理&nbsp;记&nbsp;录&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
	                 	<td width="5"></td>
	                 	<s:if test="%{state!=\"1\"}">
		                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
		                 	<td class="Operation_list" onclick="fetchTask();"><img src="<%=root%>/images/operationbtn/Retrieve_the_process.png"/>&nbsp;取&nbsp;回&nbsp;</td>
		                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
		                 	<td width="5"></td>
	                  	</s:if>
	                  	<s:if test="%{state!=\"1\"}">
			            <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="sendReminder();"><img src="<%=root%>/images/operationbtn/urge.png"/>&nbsp;催&nbsp;办&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
	                 	<td width="5"></td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="feedBack();"><img src="<%=root%>/images/operationbtn/Reply.png"/>&nbsp;反&nbsp;馈&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
	                 	<td width="5"></td>
	                 	</s:if>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	<td class="Operation_list" onclick="tjst();"><img src="<%=root%>/images/operationbtn/Statistics_view.png"/>&nbsp;统&nbsp;计&nbsp;视&nbsp;图&nbsp;</td>
	                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
	                 	<td width="5"></td>
	                 	<s:if test="%{state!=\"1\"}">
		                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
		                 	<td class="Operation_list" onclick="CellAreaExcel();"><img src="<%=root%>/images/operationbtn/Export_to_excel.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;EXCEL&nbsp;</td>
		                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
		                 	<td width="5"></td>
	                 	</s:if>
		               
		                 
	                  	<%--<security:authorize ifAnyGranted="001-0002000100050001, 001-0001000100050001">
			                <td ><a  class="Operation" href="javascript:fetchTask();" ><img class="img_s" src="<%=frameroot%>/images/fankuihuizhi.gif" width="15" height="15">取回工作&nbsp;</a></td>
			                <td width="5">
			                  &nbsp;
			                </td>
			            </security:authorize>    
	                  	--%>
                  	</tr>
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
              <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
                <tr>
                	<td>
						&nbsp;&nbsp;标题：&nbsp;<input name="businessName" id="businessName" type="text" class="search" title="请您输入标题" value="${businessName }">
			       		 <s:if test="%{state!=\"1\"}">
			       		&nbsp;&nbsp;发起开始时间：&nbsp;<strong:newdate  name="startDate" id="startDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入发起开始时间" dateform="yyyy-MM-dd" dateobj="${startDate}"/>
			       		&nbsp;&nbsp;发起结束时间：&nbsp;<strong:newdate  name="endDate" id="endDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入发起结束时间" dateform="yyyy-MM-dd" dateobj="${endDate}"/>
			       		</s:if>
			       		<s:else>
			       		&nbsp;&nbsp;办结开始时间：&nbsp;<strong:newdate  name="workflowEndDatestartDate" id="workflowEndDatestartDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入办结开始时间" dateform="yyyy-MM-dd" dateobj="${workflowEndDatestartDate}"/>
			       		&nbsp;&nbsp;办结结束时间：&nbsp;<strong:newdate  name="workflowEndDateendDate" id="workflowEndDateendDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入办结结束时间" dateform="yyyy-MM-dd" dateobj="${workflowEndDateendDate}"/>
			       		</s:else>
			       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
	                </td>
	                <%--
                  <td width="15%" class="biao_bg1">
                    <s:textfield name="userName" title="请输入主办人" cssClass="search"></s:textfield> 
                  </td>
                  --%>
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
				 <webflex:flexTextCol caption="当前办理人" align="center"
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
	              <webflex:flexTextCol caption="主办人" align="center"
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
        item = new MenuItem("<%=root%>/images/operationbtn/urge.png","关联流程","relativeWf",1,"ChangeWidthTable","checkMoreDis");
        sMenu.addItem(item);
        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查阅","gotoView",1,"ChangeWidthTable","checkMoreDis");
	        sMenu.addItem(item);
	    item = new MenuItem("<%=root%>/images/operationbtn/Handle_Record.png","办理记录 ","Cur_workflowView",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item); 
        <security:authorize ifAnyGranted="001-0002000100050001, 001-0001000100050001"></security:authorize>
         <s:if test="%{state!=\"1\"}">
	    item = new MenuItem("<%=root%>/images/operationbtn/Retrieve_the_process.png","取回","fetchTask",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item);
	    </s:if>
	    <s:if test="%{state!=\"1\"}">
        item = new MenuItem("<%=root%>/images/operationbtn/urge.png","催办","sendReminder",1,"ChangeWidthTable","checkMoreDis");
	    sMenu.addItem(item);
	    item = new MenuItem("<%=root%>/images/operationbtn/Reply.png","反馈","feedBack",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
		 </s:if>
        sMenu.addShowType("ChangeWidthTable");
        registerMenu(sMenu);
      }
      
      //关联流程
      function relativeWf(){
          var taskId = getValue();
          if(taskId == ""){
          	alert("请选择要关联的记录。");
          	return ;
          }else{
          	var taskIds = taskId.split(",");
          	if(taskIds.length>1){
          		alert("只可以关联一条记录。");
          		return ;
          	}
          }
          
          var info = getInfo(taskId);
          var instanceId   = info[0]; 
          //var iWidth=1200; //弹出窗口的宽度;
			//var iHeight=450; //弹出窗口的高度;
			//var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
			//var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
			//WindowOpen("<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId="+instanceId,'todo',iWidth, iHeight,iTop,iLeft);
			showModalDialog("<%=root%>/relativeworkflow/relativeWorkflow.action?toPiId="+instanceId, 
                  window,"dialogWidth:750px;dialogHeight:420px;status:no;help:no;scroll:yes;");
     }
      function sendReminder(){
		     var id=getValue();
		      if(id == ""){
			 	alert("请选择要催办的记录。");
			 	return ;
			 }else{
			 	var ids = id.split(",");
			 	if(ids.length>1){
			 		alert("只可以催办一条记录。");
			 		return ;
			 	}
			 }
			 if(checkSelectedOneDis()){
			  var info = getInfo(id); 
			 	var overDate = info[1];
			 	var instanceId = info[0];
			 	if(overDate == "1"){
			 		alert("流程已结束，无需催办。");
			 		return;
		 	    }
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=work/work-urgchoose.jsp?instanceId="+instanceId,400, 300, window);
			 }
			 
			 
			 
			}
      
      //反馈
		function feedBack(){
			var id=getValue();
			var returnValue = "";
			if(id == ""){
				alert("请选择要反馈的记录。");
				return ;
			}else{
				var ids = id.split(",");
				if(ids.length>1){
					alert("只可以反馈一条记录。");
					return ;
				}
			}
			
			if(checkSelectedOneDis()){
			  var info = getInfo(id); 
			 	var overDate = info[1];
			 	var instanceId = info[0];
			 	if(overDate == "1"){
			 		alert("流程已结束，无需反馈。");
			 		return;
		 	    }
			 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose1.jsp?instanceId="+instanceId+"&feedBack=true",560, 290, window);
			 }
		}
		
      
      //处理状态（查看流程图）
      function Cur_workflowView(){ 
      	  var taskId = getValue();
      	  if(taskId == ""){
      		alert("请选择要查看的办理记录。");
      		return ;
      	  }else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("只可以查看一条办理记录。");
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
      		alert("请选择要取回的记录。");
      		return ;
      	}else{
      		if(taskId.split(",").length>1){
      			alert("只可以取回一条记录。");
      			return ;
      		}
      		if(confirm("确定要取回吗？")){
	      		doFetchTask(taskId);
	      	}	
      	}
      }
            //查阅
      function gotoView(){
      	var taskId = getValue();
      	if(taskId == ""){
      		alert("请选择要查阅的记录。");
      		return ;
      	}else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("只可以查阅一条记录。");
      			return ;
      		}
      	}
      	var info = getInfo(taskId);
      	var instanceId = info[0];//获取流程实例ID
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,'processed',width, height, "");
   		//var ret=OpenWindow("<%=root%>/work/work!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,width, height, window);
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
      		alert("请选择要查阅的记录。");
      		return ;
      	}else{
      		var taskIds = taskId.split(",");
      		if(taskIds.length>1){
      			alert("只可以查阅一条记录。");
      			return ;
      		}
      	}
      	var info = getInfo(taskId);
      	var instanceId = info[0];//获取流程实例ID
      	var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,'processed',width, height, "");
   		//var ret=OpenWindow("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId,width, height, window);
}
function tjst(){
	window.location.href="<%=path%>/senddoc/sendDoc!processedWorkflow.action?filterSign=${filterSign}&state=${state}&workflowType=${workflowType}&excludeWorkflowType=${excludeWorkflowType}&handleKind=${handleKind}&excludeWorkflowTypeName=${excludeWorkflowTypeName}";
}
    </script>
  </body>
</html>
