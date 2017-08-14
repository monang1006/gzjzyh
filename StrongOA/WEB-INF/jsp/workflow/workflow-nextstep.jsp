<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
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
    <link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"  type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script src="<%=path%>/oa/js/workflow/workflow.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
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
width:33%;
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
    	$("#remind").hide();
    	var tmpcount=0;
    	tmpcount= $("#onlyRemainArea input:checked").length;
    	if(tmpcount>0){
    		$("#remind").show();
    	}
    	 $("#yj").change(function(){
        	var value = $(this).val();
        	if(value != "0"){
        		$("#suggestion").val(value);
        	}
        });
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
    	//$("#timerRemind").append("定时提醒：<input style=\"margin-left:25px;\" type='button' name='btnTimer' id='btnTimer' class=\"input_bg\" onclick='editTimer();' value='设置'><table><tr><td id=\"remindArea\"></td></tr></table>");
    	if($("#doaction").val() == "batchDONext"){//批量签收 yanjian
    		$("input[name=rtx]").attr("checked",false);
    		$("#onlyRemainArea").hide();
    		$("#genzong").attr("checked",false);
    		$("#genzong").hide();
    		$("#genzongspan").hide();
    		$("#form").attr("action","<%=root %>/senddoc/sendDoc!batchHandleNextValidate.action");
    		
    	}
    	
    	//alert($("body",window.opener.document).html());//parent.opener.document.getElementById("suggestionStyle"));
    	//$("#suggestion").val($("#suggestionOld").val());
    	
    	if(window.dialogArguments.document.getElementById("suggestionStyle")){
	    	var suggestionStyle = window.dialogArguments.document.getElementById("suggestionStyle").value;
			//如果是设置为笔形意见则不显示意见框 modify by luosy 2014-03-31
	    	if("0"==suggestionStyle){
	    		$("#suggestionFieldset").hide();
	    	}
    	}
    	
    	
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
			remindArea.innerHTML+="<div id="+arr[0]+" style=\"display: \">&nbsp;"+arr[1]+"&nbsp;定时提醒, 提醒人员：[ "+arr[2]+" ] <input type='button' value='删除' class='input_bg' onclick='delremind(\""+arr[0]+"\");'><hr></div>";
			remindCount++;
		}
		document.getElementById("remindSet").value=remindContent;
		//alert("hao="+$("#remindSet").val());
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
    	
		function checkParentWindow(){
			var parentLength ;
			try{
				parentLength = $("body",window.dialogArguments.opener.document).length;
			}catch(e){
				if(window.dialogArguments.location){
					parentLength=1;
				}else{
					parentLength=0;
				}
			}
			//alert("parentLength:"+parentLength+"\n"+window.dialogArguments.opener.document);
			//alert("parentLength:"+parentLength+"\n"+window.dialogArguments.location);
			if(parentLength>0){
			}else{
				//window.dialogArguments.checkParentWindow();
				//window.close();
				var alertInfo = "父窗口页面已发生变化，或被关闭，或已退出本系统，不能继续操作该页面，请重新登录打开本页面后操作!\n\n是否关闭当前页面？";
				if(confirm(alertInfo)){
					window.close();
					return false;
				}
			}
		}
    </script>
  </head>
  <base target="_self">
  <body onmousedown="checkParentWindow()">
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
			  <!-- 拟稿单位 -->
			  <s:hidden id="orgName" name="orgName"></s:hidden>
			  <!-- 拟稿人 -->
			  <s:hidden id="userName" name="userName"></s:hidden>
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
			  <!-- 流程流水号 -->
			<s:hidden id="workflowCode" name="workflowCode"></s:hidden>
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
			  <!-- 审批意见控制 -->
			  <s:hidden id="suggestionrequired" name="suggestionrequired"></s:hidden>
			  <!-- 审批意见内容 -->
			  <s:hidden id="suggestionOld" name="suggestion"></s:hidden>
   			  <div id="contentborder">
   			  <table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										流程办理
									</td>
									<td align="right">
										<div align="center" id="btn_second" style="display: display;">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<!-- 第一次进入此页面,只显示下一步按钮和取消按钮 -->
											<tr>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="doSubmit();">&nbsp;完&nbsp;成&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
							                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
							                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
						                  		<td width="6"></td>
											</tr>
										</table>
										</div>
										<div align="center" id="btn_three" style="display: none;">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="lastStep();">&nbsp;上&nbsp;一&nbsp;步&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="doSubmit();">&nbsp;完&nbsp;成&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
							                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
							                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
						                  		<td width="6"></td>
											</tr>
						                  		</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
						<div style="padding: 10px 10px 10px 10px;text-align: left;font-size: 14px;">
			            <%-- --%><fieldset id="suggestionFieldset">
				          <legend style="font-size: 14px;">
				            	填写意见
				          </legend>
				          <div style="padding: 10px 10px 10px 10px; text-align: left;font-size: 14px;">
				            <div>
				            	 <%--
				              <b>我的意见：</b>
				              <select id="yj" style="width:300px;">
									<option value="0">&lt;选择以前的意见&gt;</option>
									<s:iterator value="ideaLst">
										<option value="${dictItemName }">${dictItemName }</option>
									</s:iterator>
								</select>&nbsp;&nbsp;
								 --%>
								<select id="yj" style="width:490px;">
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
								&nbsp; 
								<a id=""  href="#" class="button" style="margin:1px;" onclick="editDict();">常用意见</a>&nbsp;
								 <%--
								<span style="cursor:hand;" onclick="javascript:viewPDImage();">点击查看流程图</span>
								<s:if test="taskId != null && taskId.length()>0">
					              &nbsp;
					              <span style="cursor:hand;" onclick="javascript:annal();">处理记录</span>
									<span style="cursor:hand;" onclick="javascript:viewWorkflow();">处理记录</span>
								</s:if>
								<s:else>
									  <span style="cursor:hand;" onclick="javascript:viewPDImage();">处理记录</span>
								</s:else>
								--%>
				            </div>
				            <div>
				              <textarea style="width: 100%" id="suggestion" name="suggestion" rows="12" wrap="on" 
				              onpropertychange="if(value.length>2000) value=value.substr(0,2000)" ></textarea>
				            </div>
				          </div>
				        </fieldset>
				       <br>
			            <fieldset>
			              <legend style="font-size: 14px;">
			                选择下一步骤
			              </legend>              
			              <div style="padding: 10px 6px 10px 6px; text-align: left;font-size: 14px;">
			                <div id="nextstep" class="megamenu">
			                &nbsp;
			                </div>
			              </div>         
			            </fieldset>
			            <br>
			            <fieldset id="userFieldset">
							<legend style="font-size: 14px;">
								选择人员
							</legend>
							<iframe id="chooseUser" name="chooseUser" frameborder="0" width="100%" title="选择处理人员"></iframe>
							<div id="users" style="padding: 10px 10px 10px 10px;">
							</div>
						</fieldset>
			          </div>
			         <div id="onlyRemainArea">
					  <!-- 提醒方式标签 -->
					  <strong:remind isDisplayContent="true" isShowButton="false" includeRemind="RTX,SMS" rtxChecked="checked" isDisplayInfo="false" defaultRemindContent="工作提醒：请查阅《${businessName}》。${userName}" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
			      	</div>
			      	<div style="padding: 10px 10px 10px 10px;text-align: left;font-size: 14px;">
			      	<fieldset>
						<legend style="font-size: 14px;">
							重要文件跟踪
						</legend>
						<div id="users" style="padding: 10px 10px 10px 10px;">
							<input type="checkbox" id="genzong" name="genzong" /><span id="genzongspan">重要文件跟踪</span>
						</div>
					</fieldset>
					</div>
    </div>
    </form>
  </body>
</html>
