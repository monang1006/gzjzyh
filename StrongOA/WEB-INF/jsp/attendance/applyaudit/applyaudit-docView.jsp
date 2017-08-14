<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
	  <%@include file="/common/include/meta.jsp" %>
		<title>查看申请单</title>
		<link href="<%=frameroot%>/css/properties_windows.css"
			type="text/css" rel="stylesheet">
        <link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
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
	//由eform.js中定义的doNext()回调
	function goBack(){
		alert("发送成功！");
   		window.dialogArguments.document.location.reload();
   		window.close();
	}
	//初始化设置
	function initial(){
		if($("#pkFieldValue").val()!=""){
			FormInputOCX.AddFilterParams($("#tableName").val(),$("#pkFieldName").val(),$("#pkFieldValue").val());
	     	FormInputOCX.LoadFormData();//将业务数据加载到电子表单容器中
		}
		if(FormInputOCX.OfficeActiveDocument){
			with (FormInputOCX.OfficeActiveDocument.Application) {//痕迹保留
			    UserName = '${userName}';
			}
		}
	}
  //保存表单成功以后的回调函数
    function AfterSaveFormData(){
    	alert("保存成功！");
    	window.close();
    }			
    function goup(){
        window.close();
    }
     //是否可以补填申请单
   function isCanRewriter(applytypeid){
   		$.post("<%=path%>/attendance/applytype/applyType!isCanRewriter.action",
   				{"typeId":applytypeid},
   		     	function(data){
   		     	$("#isbutian").val(data);
     	});
   }
   function getTypeName(id){
     		var info ;
			<s:iterator value="typeList">
	      		if(id == '${typeId}'){
	      			info='${typeName}';
	      		}
	      	</s:iterator>
	      	return info;
   }
 
    </script>
	</head>
	<base target="_self">
	<body class="contentbodymargin" oncontextmenu="return false;">	
	<form id="recvdocForm" action="<%=path%>/attendance/applyaudit/applyaudit!eFormDateSave.action"
	  method="post" enctype="multipart/form-data">
	<!-- 业务数据名称 -->
			<s:hidden id="businessName" name="businessName"></s:hidden>
			<!-- 电子表单模板id -->
			<s:hidden id="formId" name="formId"></s:hidden>
			<!-- 任务id -->
			<s:hidden id="taskId" name="taskId"></s:hidden>
			<!-- 流程实例id -->
			<s:hidden id="instanceId" name="instanceId"></s:hidden>
			<!-- 列表模式,区别是从【待办,在办,主办,已办】列表页面跳转过来 -->
			<s:hidden id="listMode" name="listMode"></s:hidden>
			<!-- 业务表名称 -->
			<s:hidden id="tableName" name="tableName"></s:hidden>
			<!-- 业务表主键名称 -->
			<s:hidden id="pkFieldName" name="pkFieldName"></s:hidden>
			<!-- 业务表主键值 -->
			<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
			<!-- 电子表单数据 -->
			<s:hidden id="formData" name="formData"></s:hidden>
			<s:hidden id="userName" name="userName"></s:hidden>
			<s:hidden id="userId" name="userId"></s:hidden>
	        <s:hidden id="workId" name="workId"></s:hidden>
		    <s:hidden id="applyId" name="applyId"></s:hidden>
			<input type="hidden" id="applyStime" value="${model.applyStime}"/>
			<input type="hidden" id="applyEtime" value="${model.applyEtime}"/>
		<div id="contentborder" align="center">
			<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">	
				<input type="hidden" name="formId" id="formId"/>			
				<tr>
					<td height="40" class="tabletitle">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								&nbsp;
								</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									考勤申请单
								</td>
								<td width="70%">
									<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>

												<td width="*">
													&nbsp;
												</td>
												
												<td width="5"></td>
												${returnFlag }
												<td width="50">
													<a class="Operation" href="javascript:goup();">
														<img src="<%=root%>/images/ico/ht.gif" width="15"
															height="15" class="img_s"> 返回</a>
												</td>
												<td width="5">
													&nbsp;
												</td>
											</tr>
										</table>
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
		          <td height="100%" align="center"><object height="850" width="100%"
		              classid="clsid:750B2722-ADE6-446A-85EF-D9BAEAB8C423"
		              codebase="<%=root%>/common/eformOCX/FormInputOCX.CAB<%=OCXVersion%>"
		              id="FormInputOCX"><param name="Visible" value="0" />
		              <param name="AutoScroll" value="0" />
		              <param name="AutoSize" value="0" />
		              <param name="AxBorderStyle" value="1" />
		              <param name="Caption" value="FormInputOCX" />
		              <param name="Color" value="4278190095" />
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
		            </object>
		          </td>
        		</tr>
			</table>
		</div></form>
	</body>
</html>