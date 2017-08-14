<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@page import="java.text.DateFormat"%>
<jsp:directive.page import="com.strongit.oa.common.workflow.WorkFlowTypeConst"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>

<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<title>提交流程向导</title>
		<link
			href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <style media="screen" type="text/css">
    .hand {
      cursor:pointer;
    }
    </style>
    <script type="text/javascript">  
    
    //判断是否选择处理人
      function isChooseActors() {
           var strActorFlag = $("#actorFlag").val();
           if (strActorFlag == "activeSet") {
             //判断是否选择了处理人
             if ($("#concurrentMode").val() == "1"){
     	  	   var concurrentTaskActors = document.getElementsByName("strConcurrentTaskActors");
     	  	   for(var i=0; i<concurrentTaskActors.length; i++){
     	  		   if(concurrentTaskActors[i].value == null || concurrentTaskActors[i].value == ""){
     	  		       return false;
     	  		   }
     	  	   }
     	  	   return true;
             } else if ($("#strTaskActors").val() == "") {
                 return false;
             } else {
                 return true;
             }
           } else {
             return true;
          }
      }    
      //完成处理
      function submit_form(){
      	if($("#concurrentMode").val() == "1"){
      	  	var concurrentTaskActors = document.getElementsByName("strConcurrentTaskActors");
      	  	var taskActors = "";
      	  	for(var i=0; i<concurrentTaskActors.length; i++){
      	  		taskActors = "," + concurrentTaskActors[i].value;
      	  	}
      	  	$("#strTaskActors").val(taskActors.substring(1));
      	  }
          senddocForm.submit();
          $("#finish").attr("disabled",true);
      }
      
       //构造下一步步骤的单选元素
      function generateNextStep(transArray) {
          var innerHtml = "";
          var concurrentMode = "";
          for (var i=0; i < transArray.length; i++) {
          	if(transArray[i].concurrentMode == "1"){
          		innerHtml += "<input type='checkbox' id='"+transArray[i].id+"' name='transition' value=" + 
	                           transArray[i].name + "' disabled checked>" +
	                           transArray[i].name + "<br>";
	            concurrentMode = "1";
          	}else{
              innerHtml += "<input type='radio' id='"+transArray[i].id+"' name='transition' value=" + 
                           transArray[i].name + " onclick='chooseNextStep("+transArray[i].id+");'>" +
                           transArray[i].name + "<br>";
          	}
	      }
	      
	      if(concurrentMode == "1"){
			$("#concurrentNextstep").html(innerHtml);
			$("#concurrentMode").val("1");
			$("#transitionName").val("所有并发");
			$("#transitionId").val("0");
			$.ajax({ url: "<%=root%>/senddoc/sendDoc!getConcurrentNextTaskActorsInfo.action",
	             type: "post",
	             dataType: "text",
	             data: "workflowName="+$("#workflowName").val()+"&transitionId="+$("#transitionId").val()+"&concurrentTrans=",
	             success: function(msg){
	             	  var taskToSetHtml = "";
	             	  var tasksToSet = msg.split("|");
			          for(var i=0; i<tasksToSet.length; i++){
			          	eval("var taskToSet ="+ tasksToSet[i]);
			          	taskToSetHtml = taskToSetHtml + "<div style=\"padding: 15px 15px 15px 15px;\">" + taskToSet.nodeName +"处理人：<span id=\"users_" + taskToSet.nodeId + "\">&nbsp;</span> &nbsp;";
			          	if(taskToSet.actorFlag == "activeSet"){
			          		taskToSetHtml = taskToSetHtml + "<input type=\"button\" value=\"选择人员\" class=\"input_bg\" onclick=\"chooseConcurrentActors(" + taskToSet.nodeId + ");\">"
			          									  + "<input type='hidden' id='taskActor_" + taskToSet.nodeId + "'><input type='hidden' id='strTaskActors_" + taskToSet.nodeId + "' name='strConcurrentTaskActors'></div>";
			          		$("#actorFlag").val(taskToSet.actorFlag);
			          	}else{
			          		taskToSetHtml = taskToSetHtml + "<input type=\"button\" value=\"选择人员\" class=\"input_bg\" onclick=\"chooseConcurrentActors(" + taskToSet.nodeId + ");\" disabled></div>"
			          	}
			          }
			          $("#concurrentTaskSet").html(taskToSetHtml);
	              }
          	 });
	      }else{
	      	$("#step1").css("display","block");
          	$("#nextstep").html(innerHtml);
	      }
          $("#next").attr("disabled",false);
      }
      
      //选择工作流程
      function chooseWorkflow(radioElement) {
          var workflowName = radioElement.value;
          if ($("#workflowName").val() == workflowName) {
              return;
          }
          
          $("#workflowName").val(workflowName);
          $("#transitionId").val("");
          
          $.ajax({ url: "<%=root%>/attendance/apply/apply!getNextTransitions.action",
             type: "post",
             dataType: "text",
             data: "workflowName="+workflowName,
             success: function(msg){			     
										     eval("var trans = " + msg);
										     generateNextStep(trans);
										  }
          });
      }

	  //选择并发处理人
	  function chooseConcurrentActors(nodeId){
          var taskActors=$("#taskActor_" + nodeId).val();
          if(taskActors!=''&& taskActors!=null){
          	taskActors=$("#taskActor_" + nodeId).val();
          }else{
          	taskActors="";
          }
          var url =scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=assign&taskId=0&nodeId=" + nodeId +
                     "&taskActors="+taskActors;
          var str = OpenWindow(encodeURI(url), 400, 400, window);          
          //用户ID|用户名称,用户ID|用户名称
          if(str!=null && str!=''){
          	  //回传选人动作
	          $("#taskActor_" + nodeId).val(str);
	          var usestr = str.split(",");
	          var userinfo="";
	          var username="";
	          var userids="";
	          for(var i=0;i<usestr.length;i++){
	          	var userinfo=usestr[i].split("|");
	          	userids+=","+userinfo[0];
	          	username+=","+userinfo[1];
	          }
	          //获取用户ID
	          var userid = userids.split(",");
	          var strTaskActors="";
	          //组合用户ID和节点ID传给后台处理
	          for(var j=0;j<userid.length;j++){
	          	if(userid[j]!=null && userid[j]!=''){
	          		strTaskActors+=","+userid[j].substring(1)+"|"+nodeId;
	          	}
	          }
	          $("#users_" + nodeId).html(username.substring(1));
	          $("#strTaskActors_" + nodeId).val(strTaskActors.substring(1));  
          }
      }     

      //选择处理人
      function chooseActors() {
          if (($("#transitionId").val() == "") && ($("#concurrentTrans").val() == "")) {
              alert("请选择下一步骤！");
              return;
          }

          var taskActors=$("#taskActor").val();
          if(taskActors!=''&& taskActors!=null){
            taskActors=$("#taskActor").val();
          }else{
            taskActors="";
          }
          var url =scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=assign&taskId=0&nodeId=" + $("#nodeId").val() +
                     "&maxTaskActors="+$("#maxTaskActors").val()+"&isSelectOtherActors="+$("#isSelectOtherActors").val()+
                     "&taskActors="+taskActors;
          var str = OpenWindow(encodeURI(url), 400, 400, window);          
          //用户ID|用户名称,用户ID|用户名称
          if(str!=null && str!=''){
              //回传选人动作
            $("#taskActor").val(str);
            var usestr = str.split(",");
            var userinfo="";
            var username="";
            var userids="";
            for(var i=0;i<usestr.length;i++){
              var userinfo=usestr[i].split("|");
              userids+=","+userinfo[0];
              username+=","+userinfo[1];
            }
            //获取用户ID
            var userid = userids.split(",");
            var strTaskActors="";
            //组合用户ID和节点ID传给后台处理
            for(var j=0;j<userid.length;j++){
              if(userid[j]!=null && userid[j]!=''){
                strTaskActors+=","+userid[j].substring(1)+"|"+$("#nodeId").val();
              }
            }
            $("#users").html(username.substring(1));
            $("#strTaskActors").val(strTaskActors.substring(1));  
          }
      }
      
      //选择下一步步骤
      function chooseNextStep(id) {   
          var transitionId = id;
          $("#transitionId").val(transitionId);
          $("#transitionName").val(document.getElementById(transitionId).value);
          
          //获取处理人解析信息
          $.ajax({ url: "<%=root%>/attendance/apply/apply!getNextTaskActorsInfo.action",
             type: "post",
             dataType: "text",
             data: "workflowName="+$("#workflowName").val()+"&transitionId="+transitionId+"&concurrentTrans=",
             success: function(msg){
             			if (msg != "") { 
	                         eval("var actorInfo = " + msg);
	                         $("#actorFlag").val(actorInfo.actorFlag);
	                         $("#nodeId").val(actorInfo.nodeId);
	                         $("#isSelectOtherActors").val(actorInfo.isSelectOtherActors);
	                         $("#maxTaskActors").val(actorInfo.maxTaskActors);
	                        if(actorInfo.actorFlag == "activeSet"){//允许动态选择处理人
	                        	//执行完之后让选人按钮可用
             					$("#actorBtn").attr("disabled",false);
	                        }else{
	                        	$("#actorBtn").attr("disabled",true);
	                        }
	                        if (actorInfo.actorFlag == "decideNode" || actorInfo.actorFlag == "subProcessNode" ||actorInfo.actorFlag == "endNode") {			                       
				                   document.getElementById("actorBtn").disabled=true;
				            }
			            } else {
		                    $("#actorFlag").val("endNode");
                          	$("#nodeId").val("");
                          	$("#isSelectOtherActors").val("");
                          	$("#maxTaskActors").val("0");
		                    document.getElementById("actorBtn").disabled=true;
		                }
             }
          });
      }
      
      var step = 1;
      //显示步骤
      function dispalyStep(step) {
         switch(step) {
             case 1:               
               $("#last").css("display","none");
               $("#next").css("display","inline");
               $("#finish").css("display","none");
               $("#step1").css("display","block");
               $("#step2").css("display","none");
               $("#msg").css("display","none");
               $("#concurrentStep1").css("display","none");
               $("#step3").css("display","none");
               break;
             case 2:
               $("#last").css("display","inline");
               $("#next").css("display","inline");
               $("#finish").css("display","none");
               $("#step1").css("display","none");
               $("#msg").css("display","inline");
               if($("#concurrentMode").val() == "1"){
               		$("#concurrentStep1").css("display","block");
               }else{
               		$("#step2").css("display","block");
               }
               $("#step3").css("display","none");
               break;
             case 3:
               $("#last").css("display","inline");
               $("#next").css("display","none");
               $("#finish").css("display","inline");
               $("#step1").css("display","none");
               $("#step2").css("display","none");
               $("#msg").css("display","none");
               $("#concurrentStep1").css("display","none");
               $("#step3").css("display","block");
               break;
         }
      }
      
      //处理步骤
      function processStep(step) {
         var ret = true;
         switch(step) {
             case 1:
               if ($("#workflowName").val() == "") {
                   ret = false;
                   alert("请选择流程！");                   
               }
               break;
             case 2:
               if ($("#transitionId").val() == "") {
                   ret = false;
                   alert("请选择下一步骤！");
               } else if (!isChooseActors()) {
                   ret = false;
                   alert("请选择下一步处理人！");
               }
               break;
         }
         
         return ret;
      }
      
      //上一步处理
      function lastStep() {
         step -= 1;
         dispalyStep(step);         
      }
      
      //下一步处理
      function nextStep() {
          if (processStep(step)) {
             step += 1;
              getRemindValue();
             dispalyStep(step);
          }
      }
      
      //退出
      function cancel() {
          window.returnValue = "";
          window.close();
      }
      
      $(document).ready(function(){
        var inputCount = 125;
        $("#last").css("display","none");
        $("#finish").css("display","none");
        
        $("#yj").change(function(){
        	var value = $(this).val();
        	if(value != "0"){
        		$("#txyj").val(value);
        	}
        });
        //限制意见输入长度
	      $("#txyj").keyup(function(){
	      	if($(this).val().length>inputCount){
	      		$("#errorInput").show();
	      		$(this).val($(this).val().substring(0,inputCount));
	      	}else{
	      		$("#errorInput").hide();
	      	}
	      }); 
      }); 
      
      //预览流程
      function workFlowView(workflowId) {
          $("#workflowId").val(workflowId);
          
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/attendance/apply/apply!workflowPic.action?workflowId="+workflowId, 
                                   width, height, window);
      }
      
      function workFlowView2() {
          //var workflowId = $("#workflowId").val();
          var workflowId = $("input:radio:checked").attr("id");
          workFlowView(workflowId);
      }
      //查阅
      function openDoc(){
      	 var width=screen.availWidth-10;;
         var height=screen.availHeight-30;
         var ReturnStr=OpenWindow("<%=root%>/attendance/apply/apply!viewDoc.action?workId="+$("#workId").val()+"&formId="+$("#formId").val(), 
                                   width, height, window);
      }
      
      		//获取提醒方式
			function getRemindValue(){
				var returnValue = "";
				$("#msg").find("input:checkbox:checked").each(function(){
					returnValue = returnValue + $(this).val() + ",";
				});
				if(returnValue!=""){
					returnValue = returnValue.substring(0,returnValue.length-1);
				}
				$("#remindType").val(returnValue);
			}
           
         /**
	      *   检查输入的提示信息
	      */
           function  checkValue()
           {
               var text  = document.getElementById("handlerMes").value; 
               if($.trim(text)=="")
               {
                   document.getElementById("handlerMes").value = "销假单转交提醒：<s:property value='model.applyTime'/> (<%=DateFormat.getDateTimeInstance().format(new Date())%>)";
               }
           }
      
    </script>
	</head>
	<base target="_self">
	<body class="contentbodymargin">
	
	<s:form id="senddocForm" name="senddocForm" action="/attendance/apply/apply!cnhandleWorkflow.action" method="post">
	<!-- 标示调用消息接口的模块类型 -->
	<input type="hidden" name="moduleType" value="<%=WorkFlowTypeConst.APPLYCANDOC %>"/>
	<s:hidden id="formId" name="formId"></s:hidden>
	<s:hidden id="workId" name="workId"></s:hidden>
	<s:hidden id="workflowId" name="workflowId"></s:hidden>
	<s:hidden id="workflowName" name="workflowName"></s:hidden>
	<s:hidden id="transitionId" name="transitionId"></s:hidden>
	<s:hidden id="transitionName" name="transitionName"></s:hidden>
	<s:hidden id="concurrentTrans" name="concurrentTrans"></s:hidden>
	<s:hidden id="model.applyId" name="model.applyId"></s:hidden>
	<s:hidden id="model.applyTypeName" name="model.applyTypeName"></s:hidden>
	<s:hidden id="strTaskActors" name="strTaskActors"></s:hidden>
	<s:hidden id="taskActor" name="taskActor"></s:hidden>
	<s:hidden id="actorFlag" name="actorFlag"></s:hidden>
	<s:hidden id="nodeId" name="nodeId"></s:hidden>
	<s:hidden id="isSelectOtherActors" name="isSelectOtherActors"></s:hidden>
	<s:hidden id="maxTaskActors" name="maxTaskActors"></s:hidden>
	<s:hidden id="concurrentMode" name="concurrentMode"></s:hidden>
	<s:hidden id="remindType" name="remindType"></s:hidden>
	
		<div id="contentborder">
			<div id="step1"
				style="padding: 15px 15px 15px 15px; font-size: 12px;">
				<fieldset>
					<legend> 
						第一步：选择流程 
					</legend>
					<div style="padding: 15px 15px 15px 15px; text-align: left;">
						请选择工作处理流程：
						<div>
							<hr>
						</div>
						<div>
							<s:iterator value="workflows" status="status">
                <input type="radio" id="<s:property value="workflows[#status.index][1]" />" name="workflow" onclick="chooseWorkflow(this);" value="<s:property value="workflows[#status.index][0]"/>">
                <s:property value="workflows[#status.index][0]" />&nbsp;&nbsp;&nbsp;&nbsp;<span class="hand"
                  onclick="workFlowView('<s:property value="workflows[#status.index][1]" />');">预览</span>
                <br>
              </s:iterator>
						</div>
					</div>
				</fieldset>
			</div>
			<div id="concurrentStep1"
					style="padding: 15px 15px 15px 15px; font-size: 12px;display:none">
					<fieldset>
						<legend>
							第二步：下一步处理
						</legend>
						<div style="padding: 15px 15px 15px 15px;text-align: left;">
							<fieldset>
								<legend>
									选择步骤
								</legend>
								<div style="padding: 15px 15px 15px 15px; text-align: left;">
									<div>
										请选择提交步骤：&nbsp;&nbsp;&nbsp;&nbsp;
										<span style="cursor:hand;" onclick="javascript:workFlowView2();">点击查看流程图</span>
									</div>
									<div>
										<hr>
									</div>
									<div id="concurrentNextstep">
										&nbsp;
									</div>
								</div>
							</fieldset>
							<br>
							<fieldset>
								<legend>
									选择下一步处理人员
								</legend>
								<div id="concurrentTaskSet">
								</div>
							</fieldset>
						</div>
					</fieldset>
			</div>
			<div id="step2"
				style="padding: 15px 15px 15px 15px; font-size: 12px;display:none;">
				<fieldset>
					<legend>
						第二步：下一步处理
					</legend>
					<div style="padding: 15px 15px 15px 15px;text-align: left;">
						<fieldset>
							<legend>
								选择步骤
							</legend>
							<div style="padding: 15px 15px 15px 15px; text-align: left;">
								<div>
									请选择提交步骤：&nbsp;&nbsp;&nbsp;&nbsp;
									<span class="hand" onclick="workFlowView2();">点击查看流程图</span>
								</div>
								<div>
									<hr>
								</div>
								<div id="nextstep">
								&nbsp;
								</div>
							</div>
						</fieldset>
						<br>
						<fieldset>
							<legend>
								选择下一步处理人员
							</legend>
							<div style="padding: 15px 15px 15px 15px;">
								处理人：
								<span id="users">&nbsp;</span>
								&nbsp;
								<input id="actorBtn" disabled="disabled" type="button" value="选择人员" class="input_bg" onclick="chooseActors();">
							</div>
						</fieldset>
						
					</div>
			</fieldset>
		</div>
		
		
		 <strong:remind isDisplayInfo="false" defaultRemindContent="销假管理转交提醒:${model.applyTypeName }" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
			<div id="step3"
				style="padding: 15px 15px 15px 15px; font-size: 12px;display:none;">
				<fieldset>
					<legend>
						第三步：填写处理意见
					</legend>
					<div style="padding: 15px 15px 15px 15px; text-align: left;">
						<div>
							<div>
								标题：<span><b><s:property value="model.applyTypeName"></s:property></b>
                          (${canc.cancleTime})销假
                      	</span>&nbsp;&nbsp;&nbsp;&nbsp;
								<span class="hand" onclick="JavaScript:openDoc();" style="color:green;">查阅</span>
							</div>
						</div>
						<div>
							<hr>
						</div>
						<div>
							<b>我的意见：</b>
							<s:select name="yj" id="yj" list="ideaLst" listKey="dictItemName" listValue="dictItemName" headerKey="0" headerValue="快捷输入"></s:select>
							<span id="errorInput" style="display:none;"><font color="red">意见不能超过125个汉字！</font></span>
						</div>
						<div>
							<textarea cols="53" id="txyj" id="suggestion" name="suggestion" rows="10" wrap="on"></textarea>
						</div>
					</div>
				</fieldset>
			</div>
			<div style="width: 100%;" align="center">
				<span><input type="button" id="last" value="上一步"
						onclick="lastStep();" class="input_bg"></span>
				<span><input type="button" id="next" value="下一步" disabled="disabled"
						onclick="nextStep();" class="input_bg"></span>
				<span><input type="button" id="finish" value="完成"
						onclick="submit_form();" class="input_bg"></span>
				<span><input type="button" value="取消" class="input_bg"
						onclick="cancel();"></span>
			</div>
		</div>
		</s:form>
	</body>
</html>