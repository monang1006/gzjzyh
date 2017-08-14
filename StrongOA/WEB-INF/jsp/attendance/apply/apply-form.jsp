<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>考勤申请单</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
			<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>	
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
		
	      function goBack(){
	         window.location = "<%=root%>/attendance/applyaudit/applyaudit.action";
	      }
    
      //提交下一步处理人
      function submitNext() {
        var formId = $("#formId").val();
        var workId = $("#workId").val();
        var taskId = $("#taskId").val();
        var returnValue = "";
        var eform = document.getElementById("FormInputOCX");
        var formData=eform.GetFormData();
        if(formData == ""){
			return ;
		}
		var ret=eform.GetLastReturnCode();				
		if(ret==1){
			
				var instanceId = $("#instanceId").val();
				  returnValue = OpenWindow("<%=root%>/attendance/apply/apply!nextstep.action?workId="+workId+
				                                 "&taskId="+taskId+"&instanceId="+instanceId+"&formId="+formId+"&timestampt="+new Date().getTime(), 
				                                 420, 370, window);
				        
				        if (returnValue == "OK") {
					       window.location = "<%=root%>/attendance/applyaudit/applyaudit.action";
				        }
				
		}   
      }
      
      //查看流程图
      function workflowView(){      
          var width=screen.availWidth-10;;
          var height=screen.availHeight-30;
          var ReturnStr=OpenWindow("<%=root%>/attendance/apply/apply!workflowPic.action?instanceId="+$("#instanceId").val(), 
                                   width, height, window);
      }
      
      $(document).ready(function(){
          var FormInputOCX = document.getElementById("FormInputOCX");
          FormInputOCX.InitialData('<%=basePath%>/services/EFormService?wsdl');
          FormInputOCX.SetFormTemplateID($("#formId").val());
          //.width和.height必须是小写
		  FormInputOCX.width=FormInputOCX.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
  		  FormInputOCX.height=FormInputOCX.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
         var ret=eform.AddFilterParams('T_OA_ATTEN_APPLY','APPLY_ID','${applyId}');
          ret=FormInputOCX.LoadFormData();
          });
          
      //驳回
      function bh(){
      //var result = saveForm(false);
      	if(result == "error"){
      		return;
      	}
      	var width=screen.availWidth-10;;
        var height=screen.availHeight-30;
        var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+$("#taskId").val()+"&type=bohui", 
                                   width, height, window);
        if(ReturnStr!=undefined && ReturnStr!=""){
        	var ret = OpenWindow("<%=root%>/attendance/apply/apply!initBack.action", 
		                                   400, 300, window);
		    if(ret){//审批意见
		    	show("任务正在被驳回，请稍后...");
	        	location = "<%=root%>/attendance/apply/apply!backlast.action?formId="+$("#formId").val()+
			      													"&taskId="+$("#taskId").val()+
			      													"&applyId="+$("#applyId").val()+
			      													"&returnNodeId="+ReturnStr+
			      													"&suggestion="+encodeURI(encodeURI(ret));
			}      													
        }
      }
      
      //回退
      function ht(){
      	var result = saveForm(false);
      	if(result == "error"){
      		return;
      	}
      	var width=screen.availWidth-10;;
        var height=screen.availHeight-30;
        var ReturnStr=OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=workflowDesign/action/processType-workflowPic.jsp?taskId="+$("#taskId").val()+"&type=return", 
                                   width, height, window);
        if(ReturnStr && ReturnStr!=""){
        	var ret = OpenWindow("<%=root%>/sworkflowPic!initBack.action", 
		                                   400, 300, window);
		    if(ret){//审批意见
		    	show("任务正在被退回，请稍后...");
	        	location = "<%=root%>/attendance/apply/apply!back.action?formId="+$("#formId").val()+
			      													"&taskId="+$("#taskId").val()+
			      													"&applyId="+$("#applyId").val()+
			      													"&returnNodeId="+ReturnStr+
			      													"&suggestion="+encodeURI(encodeURI(ret));
		    }  													
        }
      }
      
      //回退上一步
      function htsyb(){
      	var result = saveForm(false);
      	if(result == "error"){
      		return;
      	}
      	if(confirm("退回给上一处理人员，确定？")){
      		var ret = OpenWindow("<%=root%>/attendance/apply/apply!initBack.action", 
		                                   400, 300, window);
		    if(ret){//审批意见
		    	show("任务正在被退回，请稍后...");
		      	location = "<%=root%>/attendance/apply/apply!backlast.action?formId="+$("#formId").val()+
				      													"&taskId="+$("#taskId").val()+
				      													"&applyId="+$("#applyId").val()+
				      													"&suggestion="+encodeURI(encodeURI(ret));
			}	      													
      	}
      }
			      function show(i){
					$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
					$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
				  }      
    function goToPrint() {
			var FormInputOCX = document.getElementById("FormInputOCX");
			var myPrint = FormInputOCX.PrintPage(0);
			if(myPrint==-1){
				alert("打印失败，请您重新打印");
			}
		}

    </script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onunload="resumeConSignTask();">
		<s:hidden id="formId" name="formId"></s:hidden>
		<s:hidden id="applyId" name="applyId"></s:hidden>
		<s:hidden id="workId" name="workId"></s:hidden>
		<s:hidden id="taskId" name="taskId"></s:hidden>
		<s:hidden id="instanceId" name="instanceId"></s:hidden>
		<s:hidden id="isStartWorkflow" name="isStartWorkflow"></s:hidden>
		<s:hidden id="tableName" name="tableName"></s:hidden>
		<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
		<s:hidden id="formData" name="formData"></s:hidden>
		
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0"
				cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="40" class="tabletitle" colspan="2">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								&nbsp;
								</td>
								<td width="20%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									考勤申请单
								</td>
								<td>
									&nbsp;
								</td>
								<td >
									<a class="Operation" href="#" onclick="workflowView();"><img
											src="<%=root%>/images/ico/chakan.gif" width="15"
											height="15" class="img_s">处理状态&nbsp;</a>
								</td>
								<td width="5">
								<td class="zjxyb">
									<a class="Operation" href="javascript:submitNext();"><img
											src="<%=root%>/images/ico/zhuanjiaoxiayibu.gif" width="15"
											height="15" class="img_s">提交下一处理人&nbsp;</a>
								</td>
								<td width="5" class="zjxyb"></td>
								<td class="bh"  style="display: none;">
									<a class="Operation" href="javascript:bh();"><img
											src="<%=root%>/images/ico/guanbi.gif" width="15"
											height="15" class="img_s">驳回&nbsp;</a>
								</td>
								<td class="bh" width="5" style="display: none;">
								<td class="htsyb" style="display: none;">
									<a class="Operation" href="javascript:htsyb();"><img
											src="<%=root%>/images/ico/shang.gif" width="15"
											height="15" class="img_s">退回上一处理人&nbsp;</a>
								</td>
								<td width="5" class="htsyb" style="display: none;">
								</td>
								<td class="ht" style="display: none;">
									<a class="Operation" href="javascript:ht();"><img
											src="<%=root%>/images/ico/ht.gif" width="15"
											height="15" class="img_s">退回&nbsp;</a>
								</td>
								
								<td width="5"></td>
								<td >
								<a class="Operation" href="javascript:goToPrint();">
									<img src="<%=root%>/images/ico/tb-print16.gif" width="15"
										height="15" alt="" class="img_s">
								打印处理单&nbsp;</a>
								</td>
								<td width="5"></td>
								<td >
									<a class="Operation" href="javascript:goBack();"><img
											src="<%=root%>/images/ico/ht.gif" width="15"
											height="15" class="img_s">返回&nbsp;</a>
								</td>
								<td width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				
						</table>
					<table width="100%" height="177" border="0" cellpadding="0"
					cellspacing="1" class="table1">
					<tr>
					<td height="100%" width="85%">
						<object classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
							codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
							id="FormInputOCX">
							<param name="Visible" value="0" />
							<param name="Font" value="MS Sans Serif" />
							<param name="KeyPreview" value="0" />
							<param name="PixelsPerInch" value="96" />
							<param name="PrintScale" value="1" />
							<param name="Scaled" value="-1" />
							<param name="DropTarget" value="0" />
							<param name="HelpFile" value="" />
							<param name="ScreenSnap" value="0" />
							<param name="SnapBuffer" value="10" />
							<param name="DoubleBuffered" value="0" />
							<param name="Enabled" value="-1" />
							<param name="AutoScroll" value="1" />
							<param name="AutoSize" value="0" />
							<param name="AxBorderStyle" value="0" />
							<param name="Color" value="4278190095" />
						</object>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
