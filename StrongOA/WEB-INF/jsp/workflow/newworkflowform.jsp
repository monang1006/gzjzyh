<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
		<title>处理单</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/map.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/eform/eform.js" type="text/javascript"></script>
		<script type="text/javascript">
			
			//初始化设置
			function initial(){
				if($("#pkFieldValue").val()!=""){
					FormInputOCX.AddFilterParams($("#tableName").val(),$("#pkFieldName").val(),$("#pkFieldValue").val());
			     	FormInputOCX.LoadFormData();//将业务数据加载到电子表单容器中
				}
			}
			//提交下一步
			function doSubmit(){
				var height=screen.availHeight-50;
       	  		var width=screen.availWidth/2;
				var formData = FormInputOCX.GetFormData();//得到电子表单数据
				var retCode = FormInputOCX.GetLastReturnCode();//得到电子表单返回代码,操作正常返回1
				if(retCode != 1){
					alert("电子表单获取数据失败！");
					return ;
				}
				//验证表单数据输入完整性
				if(formData == ""){
					return ;
				}
				$("#formData").val(formData);
				$("#fullFormData").val(FormInputOCX.GetData());
				var returnValue = OpenWindow("<%=request.getParameter("fromPath")%>!newWizard.action?formId="+formId+"&workflowName="+encodeURI(encodeURI('${workflowName}'))+"&businessName="+encodeURI(encodeURI($("#businessName").val()))+"&workflowId="+'${workflowId}'+"&fromPath=<%=request.getParameter("fromPath")%>",width, height, window);
				if(returnValue){
					if(returnValue == "OK"){
						alert("发送成功！");
						window.close();
					}else{
						alert("发送失败，请与管理员联系。");	
					}
				}
			}
			
			/**
			 * 返回到指定页面
			 * 判断语句为taskid是否为空
			 */
			function goBack(){
				window.close();
			}
			
		</script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" oncontextmenu="return false;" onunload="resumeConSignTask();">
		<DIV id=contentborder cellpadding="0">
			<form action="<%=request.getParameter("fromPath")%>!save.action" method="post">
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
				<!-- 完整的电子表单数据,含未绑定到数据库字段的控件内容,录入审批意见. 用于将数据库传输到档案中心.详见控件#GetData-->
				<s:hidden id="fullFormData" name="fullFormData"></s:hidden>
				<!-- 拟稿单位 -->
				<s:hidden id="orgName" name="orgName"></s:hidden>
				<!-- 拟稿人 -->
				<s:hidden id="userName" name="userName"></s:hidden>
				<table border="0" width="100%" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr>
						<td>&nbsp;</td>
						<td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
							处理单
						</td>
					</tr>
					<tr height="96%" >
						<td width="100%">
									<table border="0" width="100%" cellpadding="2" cellspacing="1">
										<tr>
											<td>
												<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
													<tr>
														<td>
															&nbsp;
														</td>
														<td >
														<a href="#" class="Operation" onclick="doSubmit();" id="hrfSend"><img src="<%=root%>/images/ico/songshen.gif" width="15"
																height="15" alt="" class="img_s">提交下一办理人&nbsp;</a>
														</td>
														<td width="5"></td>
														<td >
														<a class="Operation" href="javascript:doPrintForm();">
															<img src="<%=root%>/images/ico/tb-print16.gif" width="15"
																height="15" alt="" class="img_s">
														打印处理单&nbsp;</a>
														</td>
										                <td width="5"></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td align="center">
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
							</DIV>
						</td>
					</tr>
				</table>
			</form>
		</DIV>
	</body>

</html>
