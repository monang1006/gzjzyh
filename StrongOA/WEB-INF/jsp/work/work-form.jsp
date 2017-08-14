<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp" %>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
		<title>新建工作</title>
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
				if($("#taskId").val() == ""){//草拟状态
					if($("#pkFieldValue").val()!=""){
						FormInputOCX.AddFilterParams($("#tableName").val(),$("#pkFieldName").val(),$("#pkFieldValue").val());
				     	FormInputOCX.LoadFormData();//将业务数据加载到电子表单容器中
					}else{
						initWord();//草拟时，默认打开空的WORD2003格式文档.
					}
				}else{
					var TANGER_OCX_OBJ = FormInputOCX.OfficeControl; 
					if(!TANGER_OCX_OBJ){//校验是否存在WORD
						return ;
					} 
					if(TANGER_OCX_OBJ.DocSize == 0){
						initWord();
					}
				}
			}
			
			/**
			 * 返回到指定页面
			 * 判断语句为taskid是否为空
			 */
			function goBack(){
				if($("#taskId").val() == ""){//返回到新建页面
					location = "<%=root%>/fileNameRedirectAction.action?toPage=/work/work.jsp";
				}else{//返回到(待办|在办)页面
					if($("#listMode").val() == 1){//从待办页面进入此页面
						location = "<%=root%>/fileNameRedirectAction.action?toPage=/work/work-todomain.jsp";
					}else if($("#listMode").val() == 4){
						location = "<%=root%>/fileNameRedirectAction.action?toPage=/work/work-doingmain.jsp";
					}
				}
			}
			
		</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onunload="resumeConSignTask();">
		<DIV id=contentborder cellpadding="0">
			<form action="<%=root %>/work/work!save.action" method="post">
				<!-- 业务数据 格式：tableName;pkName;pkValue -->
				<s:hidden id="bussinessId" name="bussinessId"></s:hidden>
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
				<!-- 节点上挂接的工作流插件信息 -->
			    <s:hidden id="pluginInfo" name="pluginInfo"></s:hidden>
				<table border="0" width="100%" bordercolor="#FFFFFF" cellspacing="0"
					cellpadding="0">
					<tr>
						<td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;
							<script type="text/javascript">
								if($("#taskId").val() == ""){
									document.write("新建工作");
								}else{
									document.write("审批工作");
								}
							</script>
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
														<td id="fullScreen" style="display: none;" >
															<a class="Operation" href="javascript:doFullScreen();">
																<img src="<%=root%>/images/ico/songshen.gif" width="15"
																	height="15" class="img_s">全屏模式&nbsp;</a>
														</td>
														<td width="5">
															&nbsp;
														</td>
														<td id="td_startworkflow" style="display: none ;">
															<a class="Operation" href="javascript:showForm();"><img
																	src="<%=root%>/images/ico/songshen.gif" width="15"
																	height="15" class="img_s">启动新流程&nbsp;</a>
														</td>
														<td width="5"></td>
														${returnFlag }
														<td >
															<a class="Operation" onclick="goBack();" href="#"><img src="<%=root%>/images/ico/songshen.gif" width="15" height="15" alt="" class="img_s">返回&nbsp;</a>
														</td>
														<td width="5">&nbsp;</td>
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
												<!-- 千航OFFICE控件 -->
									              <object id="TANGER_OCX_OBJ"
													classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
													codebase="<%=root%>/common/OfficeControl/OfficeControl.cab<%=OCXVersion%>"
													width="100%" height="100%">
													<param name="ProductCaption" value="思创数码科技股份有限公司">
													<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
													<param name="BorderStyle" value="1">
													<param name="TitlebarColor" value="42768">
													<param name="TitlebarTextColor" value="0">
													<param name="TitleBar" value="false">
													<param name="MenuBar" value="false">
													<param name="Toolbars" value="true">
													<param name="IsResetToolbarsOnOpen" value="true">
													<param name="IsUseUTF8URL" value="true">
													<param name="IsUseUTF8Data" value="true">
													<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
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
