<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>提交下一步流程</title>
    <link
      href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script type="text/javascript">      
      //判断是否选择处理人
      function isChooseActors() {
          var strActorFlag = $("#actorFlag").val();
          if ((strActorFlag == "activeSet") || (strActorFlag == "notActiveSet")) {
              //判断是否选择了处理人
              if ($("#strTaskActors").val() == "") {
                  return false;
              } else {
                  return true;
              }
          } else {
              return true;
          }
      }
      
      //选择下一步步骤
      function chooseNextStep(tansId) {   
          var transitionId = tansId;
          var transitionName = document.getElementById(tansId).value;
          $("#transitionId").val(transitionId);
          $("#transitionName").val(transitionName);
          
          //获取处理人解析信息
          $.ajax({ url: "<%=root%>/attendance/apply/apply!getNextTaskActorsInfo.action",
             type: "post",
             dataType: "text",
             data: "taskId="+$("#taskId").val()+"&transitionId="+transitionId+"&concurrentTrans=",
             success: function(msg){
                        if (msg != "") { 
                           eval("var actorInfo = " + msg);
                           $("#actorFlag").val(actorInfo.actorFlag);
                           $("#nodeId").val(actorInfo.nodeId);
                           $("#isSelectOtherActors").val(actorInfo.isSelectOtherActors);
                           $("#maxTaskActors").val(actorInfo.maxTaskActors);
                                                    
                           if (actorInfo.actorFlag == "decideNode" ||
                             actorInfo.actorFlag == "subProcessNode" ||
                             actorInfo.actorFlag == "endNode") {                             
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
      
      //选择处理人
      function chooseActors() {
          if (($("#transitionId").val() == "") && ($("#concurrentTrans").val() == "")) {
              alert("请选择下一步骤！");
              return;
          }
          // //scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=assign
          var taskActors=$("#taskActor").val();
          if(taskActors!=''&& taskActors!=null){
          	taskActors=$("#taskActor").val();
          }else{
          	taskActors="";
          }
          var url =scriptroot+"/workflowRun/action/runUserSelect!input.action?dispatch=assign&nodeId=" + $("#nodeId").val() +
                     "&maxTaskActors="+$("#maxTaskActors").val()+"&isSelectOtherActors="+$("#isSelectOtherActors").val()+
                     "&taskId="+ $("#taskId").val()+"&taskActors="+taskActors;
          var str = OpenWindow(encodeURI(url), 400, 400, window);
          //用户ID|用户名称,用户ID|用户名称
          var strTaskActors="";
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
	          //组合用户ID和节点ID传给后台处理
	          for(var j=0;j<userid.length;j++){
	          	if(userid[j]!=null && userid[j]!=''){
	          		strTaskActors+=","+userid[j].substring(1)+"|"+$("#nodeId").val();
	          	}
	          }
	          $("#users").html(username.substring(1));
          }
          $("#strTaskActors").val(strTaskActors.substring(1));  
      }
      
      function submit_form() {
          articlesAppForm.submit();
          //window.returnValue = "OK";
          //window.close();
      }
      
      function cancel() {
          window.returnValue = "";
          window.close();
      }
      
      //构造下一步步骤的单选元素
      function generateNextStep(transArray) {
          var innerHtml = "";
          
          for (var i=0; i < transArray.length; i++) {
              innerHtml += "<input type='radio' id='"+transArray[i].id+"' name='transition' value=" + 
                           transArray[i].name + " onclick='chooseNextStep("+transArray[i].id+");'>" +
                           transArray[i].name + "<br>";
          }
          
          $("#nextstep").html(innerHtml);
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
               break;
             case 2:
               $("#last").css("display","inline");
               $("#next").css("display","none");
               $("#finish").css("display","inline");
               $("#step1").css("display","none");
               $("#step2").css("display","block");
               break;
         }
      }
      
      //处理步骤
      function processStep(step) {
         var ret = true;
         switch(step) {
             case 1:
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
             dispalyStep(step);
         }
      }
      
      $(document).ready(function(){
        $("#last").css("display","none");
        $("#finish").css("display","none");
        
        $.ajax({ url: "<%=root%>/attendance/applyaudit/applyaudit!getNextTransitions.action",
             type: "post",
             dataType: "text",
             data: "taskId="+$("#taskId").val(),
             success: function(msg){
                         eval("var trans = " + msg);
                         generateNextStep(trans);
                      }
          });
      }); 
      
      //查看流程图
      function viewPDImage(){
      	var width=screen.availWidth-10;
        var height=screen.availHeight-30;
	 OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+$("#instanceId").val(), 
                                   width, height, window);
      }
       //查阅
      function openDoc(){
      	 var width=screen.availWidth-10;;
         var height=screen.availHeight-30;
      //   alert($("#workId").val());
      //   alert($("#formId").val());
         var ReturnStr=OpenWindow("<%=root%>/attendance/apply/apply!viewDoc.action?workId="+$("#workId").val()+"&formId="+$("#formId").val(), 
                                   width, height, window);
      }
    </script>
  </head>
  <base target="_self">
  <body class="contentbodymargin">
  <s:form id="articlesAppForm" name="articlesAppForm" action="/attendance/applyaudit/applyaudit!cnhandleNextStep.action" method="post">
  <s:hidden id="taskId" name="taskId"></s:hidden>
  <s:hidden id="transitionId" name="transitionId"></s:hidden>
  <s:hidden id="transitionName" name="transitionName"></s:hidden>
  <s:hidden id="concurrentTrans" name="concurrentTrans"></s:hidden>
  <s:hidden id="cancleid" name="cancleid"></s:hidden>
  <s:hidden id="topicName" name="topicName"></s:hidden>
  <s:hidden id="strTaskActors" name="strTaskActors"></s:hidden>
  <s:hidden id="actorFlag" name="actorFlag"></s:hidden>
  <s:hidden id="nodeId" name="nodeId"></s:hidden>
  <s:hidden id="taskActor" name="taskActor"></s:hidden>
  <s:hidden id="instanceId" name="instanceId"></s:hidden>
  <s:hidden id="formId" name="formId"></s:hidden>
  <s:hidden id="workId" name="workId"></s:hidden>
  <s:hidden id="isSelectOtherActors" name="isSelectOtherActors"></s:hidden>
  <s:hidden id="maxTaskActors" name="maxTaskActors"></s:hidden>
    <div id="contentborder">
      <div id="step1"
        style="padding: 15px 15px 15px 15px; font-size: 12px;">
        <fieldset>
          <legend>
            第一步：下一步处理
          </legend>
          <div style="padding: 15px 15px 15px 15px;text-align: left;">
            <fieldset>
              <legend>
                选择步骤
              </legend>              
              <div style="padding: 15px 15px 15px 15px; text-align: left;">
                <div>
                  请选择提交步骤：&nbsp;&nbsp;&nbsp;&nbsp;
                   <span style="cursor:hand; color:green;" onclick="javascript:viewPDImage();">点击查看流程图</span>
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
                <input id="actorBtn" type="button" value="选择人员" class="input_bg" onclick="chooseActors();">
              </div>
            </fieldset>
          </div>
      </fieldset>
    </div>
      <div id="step2"
        style="padding: 15px 15px 15px 15px; font-size: 12px;display:none;">
        <fieldset>
          <legend>
            第二步：填写处理意见
          </legend>
          <div style="padding: 15px 15px 15px 15px; text-align: left;">
            <div>
              <div>
                标题： <span><b><a href="javascript:openDoc();"><s:text name="topicName"></s:text></a></b></span>&nbsp;&nbsp;&nbsp;&nbsp;
               	<a href="javascript:openDoc();"><span style="color:green;">查阅</span></a>
              </div>
            </div>
            <div>
              <hr>
            </div>
            <div>
              <b>我的意见：</b>
              <select name="yj">
                <option value="0">
                  快捷输入
                </option>
                <option value="同意">
                  同意
                </option>
                <option value="请领导审核">
                  请领导审核
                </option>
              </select>
              &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:hand;color:green;" onclick="javascript:viewPDImage();">审批记录</span>
            </div>
            <div>
              <textarea cols="53" name="suggestion" rows="10" wrap="on"></textarea>
            </div>
          </div>
        </fieldset>
      </div>
      <div style="width: 100%;" align="center">
        <span><input type="button" id="last" value="上一步"
            onclick="lastStep();" class="input_bg"></span>
        <span><input type="button" id="next" value="下一步"
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