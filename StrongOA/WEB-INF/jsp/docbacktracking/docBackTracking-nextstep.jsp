<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%> 
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>流程办理</title>
    <link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"  type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script src="<%=path%>/oa/js/workflow/workflow.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
   	<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
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
font-size: 16px;
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
    
    /**
 * 过滤特殊字符
 * 
 * @author 严建
 * @date 2011年11月22日 15:12
 * @param {}
 *            suggestionValue:待处理字符
 * @return temp 处理之后的字符串
 */
function EE_Filter(suggestionValue) {
	var temp = "";
	if ("undefined" != (typeof(suggestionValue))) {
		if (suggestionValue.indexOf("\"") != -1) { // 处理英文形式的双引号
			suggestionValue = suggestionValue.replace(new RegExp("\"", "gm"),
					"“");
		}
		if (suggestionValue.indexOf("\'") != -1) { // 处理英文形式的单引号
			suggestionValue = suggestionValue.replace(new RegExp("\'", "gm"),
					"’");
		}
		if (suggestionValue.indexOf("<") != -1) { // 处理英文形式的单引号
			suggestionValue = suggestionValue.replace(new RegExp("<", "gm"),
					"＜");
		}
		if (suggestionValue.indexOf(">") != -1) { // 处理英文形式的单引号
			suggestionValue = suggestionValue.replace(new RegExp(">", "gm"),
					"＞");
		}
		if (suggestionValue.indexOf("\\") != -1) { // 处理英文形式的的\
			suggestionValue = suggestionValue.replace(/[\\]/gm, "＼");
		}
		if (suggestionValue.indexOf("%") != -1) { // 处理英文形式的的%
			suggestionValue = suggestionValue.replace(/[%]/gm, "％");
		}
		temp = suggestionValue;
	}
	return temp;
}
    $(document).ready(function(){ 
    	}
    );
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
      
      function doSelectUI(id) {
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
      
      /**
		 * 上传办结信息和扫描附件
		 * 作者：严建 2012年02月23日 11:31
		 */    	
   	function uploadDoneSuggestion(){
   	  var width=(screen.availWidth-10)/2;
         var height=(screen.availHeight-30)/2;
         var ReturnStr=OpenWindow("<%=root%>/senddoc/sendDocUpload!upload.action?bussinessId="
       									+$("#bussinessId").val(), 
                                  width, height, window);
          if(typeof(ReturnStr)=="string"){
          		return true;
          }else{
          		return false;
          }
   	}
   	function doSubmitBL(){
   		if($("#handleDate").val() == ""){
   			alert("办理时间是必填项！");
   			return;
   		}
		if(!processStep(step)){
			 return ;
		}
        var transitionNames = "";
        var transitionId = "";
        var taskActors = "";
        var returnDeptFlag = "";//协办处室标志
        var transitions = document.getElementsByName("transition");//得到所有迁移线集合
        for(var i=0; i<transitions.length; i++){
           if(transitions[i].checked == true){//得到选中的迁移线
               transitionNames += "," + transitions[i].value;//迁移线名称
               transitionId += "," + transitions[i].id;//迁移线名称
               if(transitions[i].transDept && transitions[i].transDept == "1"){
               	returnDeptFlag += "," + transitions[i].transDept;//协办处室标志
               }
               var flag = transitions[i].concurrentFlag;
               if(flag == 2){//子流程并且父流程需要选择人员
				   $("input:hidden[id^='strTaskActors_']").each(function(){
				   		if($(this).val()!=""){
					   		taskActors += "," + $(this).val();
				   		}
				   });
               }else{
               		if($("#strTaskActors_" + transitions[i].nodeid).val() != "" && 
	               			$("#strTaskActors_" + transitions[i].nodeid).val() != undefined){
				   		taskActors += "," + $("#strTaskActors_" + transitions[i].nodeid).val();
				   }
               }
            }
        }
  	  	if(taskActors != ""){
  	  		$("#strTaskActors").val(taskActors.substring(1));//任务处理人
  	  	}
  	  	if(returnDeptFlag != ""){
  	  		returnDeptFlag = returnDeptFlag.substring(1);
  	  	}
  	  	if(maxActors != "" && selectCount != ""){
  	  		if(selectCount > maxActors){
		  	  	alert("允许最大参与人数为" + maxActors + "人，您选择了" + selectCount + "人，请重新选择！");
		  	  	return ;
  	  		}
  	  	}
  	  	var isSubmit = true;
  	  	 if(typeof($(window.parent.document).find("#isValidate").val()) =="string" && 
  	  	 	$(window.parent.document).find("#isValidate").val() != "" && !uploadDoneSuggestion()){
  	  	 	isSubmit = false;
         }
         if(isSubmit){
         	$("#contentborder").hide();
	  	  	$("#returnFlag").val(returnDeptFlag);//协办处室标志,多个协办处室格式为:1,1,1
	  	  	$("#transitionName").val(transitionNames.substring(1));//迁移线
	  	  	$("#transitionId").val(transitionId.substring(1));//迁移线id
	  	  	$("#suggestion").val(EE_Filter($("#suggestion").val()));// modify
	  	  	var businessName = $("#businessName").val();
	  	  	businessName = businessName.replace(/\+/g, "%2B"); 
	  	  	$("#businessName").val(businessName);
	  	  	$("form").attr("action","<%=root %>/docbacktracking/docBackTracking!handleNextStep.action");
	  	  	$("form").submit();//提交表单,在流程草稿中直接提交流程
	        $("body").mask("正在提交流程,请稍后...");
         }
   }
   //查看审批意见
   function approveShow(){
	  		var width=screen.availWidth-10;
			var height=screen.availHeight-30;
			var param = "?1=1&instanceId="+$("#instanceId").val();
			var arg = window.showModalDialog("<%=root%>/docbacktracking/docBackTracking!approveinfolist.action"+param, 'a', "dialogWidth ="+width+"px;dialogHeight = "+height+"px;help=0",window);
   }
    </script>
  </head>
  <base target="_self">
  <body id="bodyHtml">
  		<input id="doaction" value="<%=request.getParameter("doaction") %>" type="hidden" name="doaction" >
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
			  <!-- 流程名称 -->
			  <s:hidden id="workflowName" name="workflowName"></s:hidden>
			  <!-- 流程类别 -->
			  <s:hidden id="workflowType" name="workflowType"></s:hidden>
			  <s:hidden id="CASignInfo" name="CASignInfo"></s:hidden>
			  <!-- 定时提醒设置 -->
			  <s:hidden id="remindSet" name="remindSet"></s:hidden>
			  <!-- 当前用户ID -->
			  <s:hidden id="userId" name="userIds"></s:hidden>
			  <!-- 当前用户姓名 -->
			  <s:hidden id="userName" name="userName"></s:hidden>
			  <!-- 选择协办处室迁移线标志 -->
			  <s:hidden id="returnFlag" name="returnFlag"></s:hidden>
			  <s:hidden id="transitionId" name="transitionId"></s:hidden>
			  <s:hidden id="isGenzong" name="isGenzong"></s:hidden>
			   <!-- 多个任务id以","分隔 -->
			  <s:hidden id="taskIds" name="taskIds"></s:hidden>
   			  <div id="contentborder">
   			  <table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="35"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td>&nbsp;</td>
									<td width="20%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									流程办理
									</td>
									<td>
										&nbsp;
									</td>
									<td width="75%">
									<%--
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td>
													<a class="Operation" href="#"
														onclick="doSubmit();"> <img
															src="<%=root%>/images/ico/queding.gif" width="14"
															height="14" class="img_s"><span id="test"
														style="cursor:hand">完成&nbsp;</span> </a>
												</td>
												<td width="5"></td>
												<td>
													<a class="Operation" href="#"
														onclick="window.close();"> <img
															src="<%=root%>/images/ico/quxiao.gif" width="14"
															height="14" class="img_s"><span id="test"
														style="cursor:hand">取消&nbsp;</span> </a>
												</td>
												<td width="5"></td>
											</tr>
										</table>
										 --%>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
						<div style="padding: 10px 10px 10px 10px;text-align: left;font-size: 12px;">
				       <br>
			            <fieldset>
			              <legend>
			                选择下一步骤
			              </legend>              
			              <div style="padding: 10px 6px 10px 6px; text-align: left;font-size: 12px;">
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
							<iframe id="chooseUser" name="chooseUser" frameborder="0" width="100%" title="选择处理人员"></iframe>
							<div id="users" style="padding: 10px 10px 10px 10px;">
							</div>
						</fieldset>
			          </div>
			          <fieldset>
				          <legend>
				            	填写意见
				          </legend>
				          &nbsp;&nbsp;当前环节处理人：<font color="red">${curActorId}</font>
				          <div style="padding: 10px 10px 10px 10px; text-align: left;font-size: 12px;">
				            <div>
				              <textarea style="width: 98%" id="suggestion" name="suggestion" rows="12" wrap="on"></textarea>
				            </div>
				            <div>
				           <font color="red">*</font>办理时间： <strong:newdate name="handleDate"
									id="handleDate" skin="whyGreen" isicon="true"
									dateobj="${handleDate}"
									dateform="yyyy-MM-dd HH:mm:ss" width="90%"></strong:newdate>
				            </div>
				            
				          </div>
				        </fieldset>
		        <!-- 第一次进入此页面,只显示下一步按钮和取消按钮 -->
				<div align="center" id="btn_second" style="display: block;">
					<span><input type="button" id="next" value="  完    成  " onclick="doSubmitBL();" class="input_bg"></span>
					&nbsp;&nbsp;
					<span><input type="button" id="approve" value="查看审批意见" onclick="approveShow();" class="input_bg"></span>
					&nbsp;&nbsp;
					<span><input type="button" value="  取    消  " class="input_bg" onclick="window.close();"></span>
				</div>
				<!-- 选择迁移线以后,点击下一步 -->
				<div align="center" id="btn_three" style="display: none;">
					<span><input type="button" id="last" value="上一步" onclick="lastStep();" class="input_bg"></span>
					<span><input type="button" id="finish" value="完成" onclick="doSubmit();" class="input_bg"></span>
					<span><input type="button" value="取消" class="input_bg" onclick="window.close();"></span>
				</div>
				 
    </div>
    </form>
  </body>
</html>
