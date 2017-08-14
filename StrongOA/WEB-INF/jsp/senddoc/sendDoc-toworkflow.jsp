<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>流程办理</title>
    <link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"  type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script src="<%=path%>/oa/js/workflow/workflow.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
    <script src="<%=path%>/oa/js/eform/eform.js?version=1111" type="text/javascript"></script>
    <script src="<%=path%>/oa/js/workflow/bgtyjzx.js" type="text/javascript"></script>
    <style>
	.megamenu{
height:100%;
background: white;
/*border: 1px solid #f06b24;*/
/*border-width: 5px 1px;*/
padding: 10px;
font: normal 15px Verdana;

}

.megamenu .column{
/*float: left;*/
width: 100%; /*width of each menu column*/
/*height:120px;*/
margin-right: 5px;
}

.megamenu .column ul{
margin: 0;
padding: 0;
list-style-type: none;
}

.megamenu .column ul li{
padding-bottom: 5px;
font-size: 14px;
width:100%;
float:left;
}

.megamenu .column h3{
background: #e0e0e0;
font: bold 18px Arial;
margin: 0 0 5px 0;
cursor: pointer;
}


</style>
    <script type="text/javascript">    
    $(document).ready(function(){ 
			if(parent.getDoNextModel() != "isMenuButton"){
				$(window.parent.document).find("#privilege").show();//显示节点设置的按钮
			}else{
				if(typeof(window.parent.maskPrivilege) != "undefined"){
			 		window.parent.maskPrivilege();
		 		}
			}
    	var suggestionStyle = $(window.parent.document).find("#suggestionStyle").val();
    	if($(window.parent.document)){
    		/* yanjian 2011-02-12 15:42 动态生成提交按钮*/
    		if($(window.parent.document).find("td[id=toNext]").length>0){
    			$("#buttonDiv").html("");
    			$("#zywjgz").html("");
    			$(window.parent.document).find("td[id=toNext]").each(function(){
    				//$("#buttonDiv").html($("#buttonDiv").html()+"&nbsp;&nbsp;&nbsp;<input type=\"button\" class=\"input_bg\" id=\"doNext\" buttonName=\""+$(this).attr("buttonName")+"\" value=\""+$(this).attr("buttonName")+"\" onclick=\"toDoNext(this);\"><br/>")
    				$("#buttonDiv").html($("#buttonDiv").html()+"&nbsp;&nbsp;&nbsp;<a href='#' class=\"button\" id=\"doNext\" buttonName=\""+$(this).attr("buttonName")+"\"  onclick=\"javascript:toDoNext(this);\">"+$(this).attr("buttonName")+"</a><br/><br/>")
    			});
    			//$("#buttonDiv").html($("#buttonDiv").html()+"&nbsp;&nbsp;<input type=\"checkbox\" id=\"genzong\" name=\"genzong\" />重要文件跟踪");
    			$("#zywjgz").html($("#zywjgz").html()+"<input type=\"checkbox\" id=\"genzong\" name=\"genzong\" />重要文件跟踪");
    		}
    	
			$("#businessName").val($(window.parent.document).find("#businessName").val());
			if($("#handlerMes")){
				$("#handlerMes").val("工作提醒：请查阅《"+$(window.parent.document).find("#businessName").val()+"》。${userName}");
			}
			if($(window.parent.document).find("#instanceId").val() != ""){//当流程实例id存在时
				var falg = false;
				if($(window.parent.document).find("#toPrev").length != 0){//存在退回上一步按钮
					if($(window.parent.document).find("#toPrev").is(":visible")){
						falg = true;
					}
				}
				if($(window.parent.document).find("#toReturnBack").length != 0){//存在驳回按钮
					if($(window.parent.document).find("#toReturnBack").is(":visible")){
						falg = true;
					}
				}
				if($(window.parent.document).find("#toBack").length != 0){//存在退回按钮
					if($(window.parent.document).find("#toBack").is(":visible")){
						falg = true;
					}
				}
				if(falg){
					$("#backmessage").show();
				}
			}
		}
    	if(suggestionStyle == "0"){
    		$("#suggestionStyle").remove();   	
    	}
        insId=$("#instanceId").val();
       // alert(insId);
    	$.post(
		"<%=path%>/senddoc/sendDoc!sendTrace.action",
		{insId:insId},
		function(data){
	
			if(data=='1'){	
		      document.getElementById("genzong").checked=true;
		}
		else {
		   		document.getElementById("genzong").checked=false;
			}
		}
	)
    	$("#remind").hide();
    	var tmpcount=0;
    	tmpcount= $("#onlyRemainArea input:checked").length;
    	if(tmpcount>0){
    		$("#remind").show();
    	}
	    	
		    	$("#onlyRemainArea input:checkbox").click(function(){
		    		var icount=0;	
		    		icount= $("#onlyRemainArea input:checked").length;
		    		if (icount>0)
		    		{
		    			$("#remind").show();
		    		}else{
		    			$("#remind").hide();
		    		}
		    	});
		    	//不显示定时提醒 yanjian 2012-12-01 
		    	//$("#timerRemind").append("定时提醒：<input style=\"margin-left:25px;\" type='button' name='btnTimer' id='btnTimer' class=\"input_bg\" onclick='editTimer();' value='设置'><table><tr><td id=\"remindArea\"></td></tr></table>");
    	}
    );
    	var remindCount=0;//添加定时提醒显示div的id增量
    	var remindContent="";//添加定时提醒显示的div的内容
    	var step = 2; //相当于senddoc-wizard.jsp中的第二步页面,为了代码重用,这里初始化为2 
    	
    	  //编辑意见
      function editDict(){
          var ReturnStr=OpenWindow("<%=root%>/suggestion/approvalSuggestion.action?state=state",600,360, window);
              if(ReturnStr!=null&&ReturnStr!=""&&ReturnStr=="ok"){
              		 $.post('<%=root%>/suggestion/approvalSuggestion!select.action',
			              function(data){
				              $("#yj").html(data);			          
			       });
              }                         	
      }
      
      function viewWorkflow(){
      	var taskId = document.getElementById("taskId").value;
		  var instanceId =document.getElementById("instanceId").value;
		  //alert("taskId:"+taskId+"&&&&&instanceId:"+instanceId);
      	var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=/senddoc/sendDoc-container.jsp?instanceId="+instanceId+"&taskId="+taskId, 
                                   width, height, window);
      }
      //删除提醒
      function delremind(id){
         if(confirm("删除此提醒设置选项，确定？")){
	         var remindDiv = document.getElementById(id);
	         	var tempRemind=remindContent;
	         	var res="";
	         	var as = tempRemind.split('##');
	         	for(var i=0;i<as.length;i++){
	         		if(as[i].indexOf(id)!=0){
	         			res+=as[i]+"##";
	         		}
	         	}
	         	remindDiv.style.display="none";
	         	remindContent = res.substring(0,res.length-2);
	         	document.getElementById("remindSet").value=remindContent;
         }
      }
      
      //展示机构列表，选择迁移线，分发下一步
		function yijian() {
			window.parent.yijian();
		}
      
      //定时提醒设置
      function editTimer(){
        var url = "<%=path%>/fileNameRedirectAction.action?toPage=senddoc/timerRemind.jsp";
		var a = window.showModalDialog(url,window,'dialogWidth:550px;dialogHeight:330px;help:no;status:no;scroll:no')
		if((typeof a)!="undefined"){
			var remindArea=document.getElementById("remindArea");
			a="id"+remindCount+";"+a;
			if(remindContent!=""&&remindContent!=null){
				remindContent+="##"
			}
			remindContent+=a;
			var arr=a.split(";");
			
			var reminduser = "";
			if(arr[4]=="1"){
				reminduser="${userName}、办理人";
			}else if(arr[4]=="2"){
				reminduser="办理人";
			}else if(arr[4]=="0"){
				reminduser="${userName}";
			}
			remindArea.innerHTML+="<div id="+arr[0]+" style=\"display: \">&nbsp;"+$("#businessName").val()+"&nbsp;定时提醒，提醒人员：[ "+reminduser+","+arr[2]+" ] <input type='button' value='删除' class='input_bg' onclick='delremind(\""+arr[0]+"\");'><hr></div>";
			remindCount++;
		}
		document.getElementById("remindSet").value = remindContent;
		//alert("hao="+$("#remindSet").val());
      }	
      /**
      * 清除处理人信息
      */
      function clearAcrtorSetting(){
        selectCount = 0;
        $("input[id^=strTaskActors_]").val("");//清楚用户id
     	$("input[id^=taskActor_]").val("");
      }
      
      function doSelectUI(id) {
        /**每次点击分组时清楚处理人信息*/
        clearAcrtorSetting();
        /*++++++++++++++++++++++++*/
      	$("#userFieldset").hide();
      	$("#chooseUser").hide();
      	
      	$("input[name='transition']").each(function(){
      		if($(this).attr("type")=="checkbox" && $(this).attr("checked")==true){
      			$(this).click();
      		}	
			$(this).attr("checked",false);
  		 });
      	
      	var b = $("#ul_" + id).is(":visible");
      	$("ul[id ^= ul_]").hide();
      	$("img[id ^= img_]").attr("src",'<%=root%>/oa/image/work/collapse_arrow.png');
      	if(b){
      		$("#ul_" + id).hide();
      	} else {
	      	$("#ul_" + id).show();
	      	var input0 = $("#ul_" + id+" li").children().eq(0);
			var group =input0.attr("group");
	        var groupLength = $("input[group='"+group+"']").length;//获取分组迁移线数量
			if(input0.attr("type")=="radio" ){
				if(groupLength == 1){
					input0.click();
				}
			}
      	}
      	$("#img_" + id).attr("src",'<%=root%>/oa/image/work/expand_arrow.png');
      }	
      
      
      function doMenuSelectUI(id) {
       /**每次点击分组时清楚处理人信息*/
        clearAcrtorSetting();
        /*++++++++++++++++++++++++*/
      	$("#userFieldset").hide();
      	$("#chooseUser").hide();
      	var b = $("#ul_" + id).is(":visible");
      	$("ul[id ^= ul_]").hide();
      	$("img[id ^= img_]").attr("src",'<%=root%>/oa/image/work/collapse_arrow.png');
      	$("#ul_" + id).show();
      	
      	$("input[name='transition']").each(function(){
      		if($(this).attr("type")=="checkbox" && $(this).attr("checked")==true){
      			$(this).click();
      		}	
			$(this).attr("checked",false);
  		 });
			  		 
		var input0 = $("#ul_" + id+" li").children().eq(0);
		var group =input0.attr("group");
        var groupLength = $("input[group='"+group+"']").length;//获取分组迁移线数量
		if(input0.attr("type")=="radio" ){
			if(groupLength == 1){
				input0.click();
			}
		}
      	$("#img_" + id).attr("src",'<%=root%>/oa/image/work/expand_arrow.png');
      }
      
      
      function toDoNext(obj){
     	//var buttonName =  $("#doNext").val();
     	var buttonName =  $("#doNext").html();
        var genzon=document.getElementById("genzong");
         if(genzon.checked==true){
          $("#isGenzong").val('1');
         }else
         $("#isGenzong").val('0');
         if(typeof($(window.parent.document).find("#isValidate").val()) =="string" && $(window.parent.document).find("#isValidate").val() != ""){
         	if(uploadDoneSuggestion()){
         		parent.doNext(obj);
         	}
         }else{
        	 $("td[id=toNext][buttonName="+buttonName+"] a",window.parent.document).click();
	      	//parent.doNext(obj);
         }
      }

		/**
		 * 上传办结信息和扫描附件
		 * 作者：严建 2012年02月23日 11:31
		 */    	
    	function uploadDoneSuggestion(){
    	  var width=(screen.availWidth-10)/2;
          var height=(screen.availHeight-30)/2;
          var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDocUpload!upload.action?bussinessId="
	       									+$(window.parent.document).find("#bussinessId").val(), 
                                   width, height, window);
           if(typeof(ReturnStr)=="string"){
           		if(window.parent){
           			if(window.parent.formReader.GetFormControl("doneSuggestion_content")){//办结意见回传到表单文本控件doneSuggestion_content
		           		 window.parent.formReader.GetFormControl("doneSuggestion_content").Value = ReturnStr;
           			}
           		}
           		return true;
           }else{
           		return false;
           }
    	}
        var cap_max = 100;//可发送的字数
		function onCharsChange(varField, dd) {
			var suggestion = document.getElementById("suggestion");
			var charsmonitor1 = document.getElementById("charsmonitor1");
			var charsmonitor2 = document.getElementById("charsmonitor2");
			var leftChars = getLeftChars(varField);
			if (leftChars >= 0) {
				//charsmonitor1.value=cap_max-leftChars;
				//charsmonitor2.value=leftChars;
				charsmonitor1.innerHTML = cap_max - leftChars;
				charsmonitor2.innerHTML = leftChars;
				return true;
			} else {
				charsmonitor1.value = cap_max;
				charsmonitor2.value = "0";
				window.alert("意见内容超过字数限制!");
				var len = suggestion.value.length + leftChars;
				suggestion.value = suggestion.value.substring(0, len);
				leftChars = getLeftChars(suggestion);
				if (leftChars >= 0) {
					charsmonitor1.innerHTML = cap_max - leftChars;
					charsmonitor2.innerHTML = leftChars;
				}
				return false;
			}
			$('#suggestion').focus();
		}
		function getLeftChars(varField) {
			var cap = cap_max;
			var leftchars = cap - varField.value.length;
			return (leftchars);
		}
		
		function checkParentWindow(){
			parent.checkParentWindow();
		}
    </script>
  </head>
  <base target="_self">
  <body oncontextmenu="return false;" onmousedown="checkParentWindow()">
		<form id="form" name="form" action="<%=root %>/senddoc/sendDoc!handleNextStep.action" method="post">
			  <!-- 业务数据 格式：tableName;pkName;pkValue -->
			  <s:hidden id="bussinessId" name="bussinessId"></s:hidden>
			  <!-- 流程实例id,用于查看流程图 -->
			  <s:hidden id="instanceId" name="instanceId"></s:hidden>
			  <!-- 业务数据标题 -->
			  <s:hidden id="businessName" name="businessName"></s:hidden>
			  <!-- 电子表单模板id -->
			  <s:hidden id="formId" name="formId"></s:hidden>
			  <!-- 任务id -->
			  <s:hidden id="taskId" name="taskId"></s:hidden>
			  <!-- 迁移线名称 -->
			  <s:hidden id="transitionName" name="transitionName"></s:hidden>
			  <!-- 任务处理人 -->
			  <s:hidden id="strTaskActors" name="strTaskActors"></s:hidden>
			  <!-- 提醒方式 -->
			  <s:hidden id="remindType" name="remindType"></s:hidden>
			  <!-- 电子表单数据 -->
			  <s:hidden id="formData" name="formData"></s:hidden>
			  <!-- 业务表主键值 -->
			  <s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			  <!-- 流程名称 -->
			  <s:hidden id="workflowName" name="workflowName"></s:hidden>
			  <!-- 流程类别 -->
			  <s:hidden id="workflowType" name="workflowType"></s:hidden>
			  <!-- 定时提醒设置 -->
			  <s:hidden id="remindSet" name="remindSet"></s:hidden>
			  <!-- 选择协办处室迁移线标志 -->
			  <s:hidden id="returnFlag" name="returnFlag"></s:hidden>
			  <!-- 迁移线ID -->
			  <s:hidden id="transitionId" name="transitionId"></s:hidden>
			  <s:hidden id="isGenzong" name="isGenzong"></s:hidden>
   			  <div id="contentborder">
						<div style="padding: 10px 10px 10px 10px;text-align: left;font-size: 14px;">
						<font size="2" color="blue" id="backmessage" style="display: none;"></font><br>
			            <fieldset id="suggestionStyle">
				          <legend style="font-size: 14px;">
				            	<!-- 我的意见-->
				            	<script>
				            		var nodeName = $("#toNext",parent.document).attr("nodeName");
				            		document.write("当前环节：" + nodeName + "&nbsp;");
				            	</script>
				          </legend>
				          <div style="padding: 10px 10px 12px 10px; text-align: left;">
				            <div>
								<select id="yj" style="width: 325">
									<option value="0">&lt;选择常用意见&gt;</option>
									<s:iterator value="ideaLst">
									<s:if test='%{suggestionContent.length()>20}'>
										<option value="${suggestionContent }" title="${suggestionContent }"><s:property value="suggestionContent.substring(0,20)" />...</option>
								    </s:if>
									<s:else>
									<option value="${suggestionContent }" title="${suggestionContent }">${suggestionContent }</option>
									</s:else>
									</s:iterator>
								</select>	
				            </div>		       
								<a id=""  href="#" class="button" onclick="editDict();">常用意见</a>&nbsp;						
				            <div>
				            </div>
				            <div>
				              <textarea style="width: 325" id="suggestion" name="suggestion"
				               onpaste="return onCharsChange(this);"
				               onpropertychange="return onCharsChange(this);"
				               onfocus="return onCharsChange(this);" rows="3" wrap="on">${exitSuggestion}</textarea>
				            <table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<tr>
										<span class="wz"><font color="#999999">
												&nbsp;已输入<font color="green"><span
													id="charsmonitor1">0</span></font>个字，剩余<font color="blue"><span
													id="charsmonitor2">100</span></font>个字，最多输入<font color="red">100</font>个字
										</font> </span>
										<!-- 用不同颜色表示不同状态的字数 -->
									</tr>
								</tr>
							</table>
				            </div>
				             
				          </div>
				        </fieldset>

						<br>
			            <fieldset>
			              <legend style="font-size: 14px;">
			                	选择下一步骤
			              </legend><%--              
			              <div style="padding: 10px 6px 10px 6px; text-align: left;">
			                <div id="nextstep" class="megamenu">
			                &nbsp;
			                </div>
			              </div>         
			            --%>
			             <div style="text-align: left;">
			                <div id="nextstep" class="megamenu">
			                &nbsp;
			                </div>
			              </div> 
			            </fieldset>
			            <br>
			            <fieldset id="userFieldset">
							<legend>
								选择人员
							</legend>
							<iframe id="chooseUser" name="chooseUser" frameborder="0"  width="100%" height="170px" onload="" title="选择处理人员" scrolling="auto"></iframe>
							<div id="users" style="padding: 10px 10px 10px 10px;">
							</div>
						</fieldset>
			          </div>
			         <div id="onlyRemainArea">
					  <!-- 提醒方式标签 -->
					  <strong:remind isDisplayContent="true" isShowButton="false" includeRemind="RTX,SMS"  isDisplayInfo="true" defaultRemindContent="工作提醒：请查阅《${businessName}》。${userName}" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
			      	</div>
			      	<div>
				      	<div id="buttonDiv">
				      		<s:if test="workflowName == '征求意见流程' || workflowName == '公文转办流程'">
							  <%--<input type="button" id="doNext" value="反馈意见" onclick="yijian();">--%>
							  <a href="#" class=\"button\" id="doNext" onclick="yijian();">反馈意见</a>
							</s:if>
							<s:else>
								<%--<input type="button" id="doNext" value="提交" onclick="toDoNext();">--%>
								<a href="#" class=\"button\" id="doNext" onclick="toDoNext();">提交</a>
							</s:else>
				      	</div>	
			      	</div>	
			      	<div style="padding: 10px 10px 10px 10px;text-align: left;font-size: 14px;">
			      	<fieldset>
			              <legend style="font-size: 14px;">
			                	重要文件跟踪
			              </legend>
			              <div id="zywjgz" style="padding: 10px 10px 10px 10px;text-align: left;font-size: 14px;">
							  <input type="checkbox" id="genzong" name="genzong" />重要文件跟踪
			              </div>
			              <div style="text-align: left;">
			              </div> 
		            </fieldset>
		            </div>
    </div>
    </form>
				<div style=”width:0;height:0”>
				<object style="display:none" id="TANGER_OCX" classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404" 
				codebase="<%=root%>/OfficeControl.cab#version=5,0,1,8" width="0%" height="0%">
				<param name="MakerCaption" value="思创数码科技股份有限公司">
				<param name="MakerKey" value="5C1FF1F1177246B272DB34DD8ADA318222D19F65">
				<param name="ProductCaption" value="南昌市政府办公厅">
				<param name="ProductKey" value="6FF53935850C053DB500DE8375B967E3C9255A1D">
				<SPAN STYLE="color:red">该网页需要控件浏览.浏览器无法装载所需要的文档控件.请检查浏览器选项中的安全设置.</SPAN>
				</div>
    
  </body>
</html>