<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>

<html>
	<head>
		<title>工作日志</title>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows.css">
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery.imgareaselect-0.6.2.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/OfficeControl/officecontrol.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style type="text/css">
			body,table,tr,td,div {
				margin: 0px;
			}
		</style>
		<script type="text/javascript">
		
		String.prototype.trim = function() {
            var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
            strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
            return strTrim;
        }
         
		//关闭窗口
		function windowclose(){
			window.close();
		}
		
		//提交表单
		function save(){
			//标题验证
			var wlogTitle = document.getElementById("wlogTitle");
			if(""==wlogTitle.value.trim()|null==wlogTitle.value.trim()){
				alert("请输入文档标题！");
				wlogTitle.focus();
				return ;
			}
			//时间验证
			var stime = document.getElementById("wlogStartTime").value; 
			if(stime==null|stime==""){
				alert("请输入开始时间！");
				return;
			}
			
			var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
	        var ret = TANGER_OCX_OBJ.SaveToUrl(form.action,
	                                           "wordDoc",
	                                           "",
	                                           $("#wlogTitle").val(),
	                                           "form"); 
	        eval("var doc = "+ret);
	        if (doc.success == "yes") {
	        	if(doc.operateType=="viewAdd"){
	        		window.dialogArguments.refreshDayContent(doc.setDate);
	        	}else if(doc.operateType=="viewEdit"){
	        		window.dialogArguments.parent.refreshDayContent(doc.setDate);
	        	}else{
	        		window.dialogArguments.submitForm();
	        	}
	        	 window.close();
	        }else{
	            alert("保存文档失败!");
	        }
		}
		
		//获取说明内容
         function getWLogConContent(form) {
             var oEditor = FCKeditorAPI.GetInstance('content');
             var acontent = oEditor.GetXHTML();
			 form.wLogCon.value = acontent;
         }
         
         //删除附件
		 function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 
		 //下载附件
		 function download(id){
		 	var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/worklog/workLog!down.action?attachId="+id;
		 }
		 
		 $(document).ready(function() {
			var message = $(".actionMessage").text();
			if(message!=null && message!=""){
				if(message.indexOf("error")>-1){
					message = message.replace("error","");
					alert(message);
				}else{
					window.dialogArguments.submitForm();
					window.close();
				}
			}
			initWordOCX();
			var workLogId = $("#workLogId").val()
			if ((workLogId != "") && (workLogId != undefined)) {
			    openFromURL($("#workLogId").val());
			}
		});
		
		//初始化WORD控件
        function initWordOCX() {
              var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
              	//TANGER_OCX_OBJ.AddCustomMenu2(7,'模板');
                TANGER_OCX_OBJ.CreateNew("Word.Document");                
                //禁用新建功能
                TANGER_OCX_OBJ.FileNew=false;
                //禁用保存功能
                TANGER_OCX_OBJ.FileSave=false;
                //禁用打印功能
                TANGER_OCX_OBJ.FilePrint=false;
                //禁用关闭功能
                TANGER_OCX_OBJ.FileClose=false;
                //禁用另存为功能
                TANGER_OCX_OBJ.FileSaveAs=false;
            }
      
      
      function openFromURL(workLogId) {
          var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          TANGER_OCX_OBJ.OpenFromURL("<%=root%>/worklog/workLog!opendoc.action?workLogId="+workLogId);
      }
      
      function loadTempLate(doctemplateId) {
      		var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
          	TANGER_OCX_OBJ.OpenFromURL("<%=root%>/doctemplate/doctempItem/docTempItem!opendoc.action?doctemplateId="+doctemplateId);
      }
      function loadTemplate(){
      		 var docgroupId=document.getElementById("docgroupId").value;
	         var url="<%=root%>/doctemplate/doctempItem/docTempItem!getDocTempItemList.action?docgroupId="+docgroupId;
	         OpenWindow(url,350,450,window);
      }
      
	</script>
	<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
			<s:form action="/worklog/workLog!save.action" id="form" name="form" enctype="multipart/form-data"
				theme="simple">
				<input type="hidden" id="workLogId" name="workLogId" value="${model.workLogId}">
				<input type="hidden" id="wlogUserName" name="model.wlogUserName" value="${model.wlogUserName}">
				<input type="hidden" id="userId" name="model.userId" value="${model.userId}">
				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
				<input type="hidden" id="setDate" name="setDate" value="${setDate}">
				<input type="hidden" id="operateType" name="operateType" value="${operateType}">
				<input type="hidden" id="startTime" name="startTime" value="${startTime}">
				<input type="hidden" id="endTime" name="endTime" value="${endTime}">
				<div id="desc" style="display: none">
					${model.wlogCon}
				</div>
				<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
				<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td>
									&nbsp;
									</td>
									<td width="30%">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
										<script>
											 var id = document.getElementById("workLogId").value;
											 if(id==null|id==""|id==" "){
											 	document.write("新建工作日志");
											 }else{
											 	document.write("修改工作日志");
											 }												 
										</script>
									</td>
									<td width="70%">
										<table border="0" align="right" cellpadding="00"
											cellspacing="0">
											<tr>
												<td >
													<a class="Operation" href="javascript:save();"><img
															src="<%=root%>/images/ico/baocun.gif" width="14"
															height="14" class="img_s">保存&nbsp;</a>
												</td>
												<td width="5"></td>
												<td >
													<a class="Operation" href="javascript:windowclose();"><img
															src="<%=root%>/images/ico/quxiao.gif" width="14"
															height="14" class="img_s">取消&nbsp;</a>
												</td>
												<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				
					<tr>
						<td width="100%" valign="top" height="100%">
							<table width="100%" height="100%" align="center" border="0"
								cellpadding="0" cellspacing="1" class="table1" style="padding-top: 0;margin-top: 0">
								<tr>
									<td class="biao_bg1" width="16%" align="right">
										<span class="wz">标题(<font color=red>*</font>)：</span>
									</td>
									<td class="td1" width="100%">
										<input id="wlogTitle" name="model.wlogTitle" type="text"
											value="${model.wlogTitle}" size="62" maxlength="45">
									</td>
								</tr>
							
								<tr id="onceTaskTime" style="display: ">
									<td nowrap class="biao_bg1" align="right">
										<span class="wz">日期(<font color=red>*</font>)：</span>
									</td>
									<td class="td1" title="请输入开始时间和结束时间">
										<strong:newdate name="model.wlogStartTime" id="wlogStartTime"
											dateform="yyyy-MM-dd"
											dateobj="${model.wlogStartTime}" width="120px"
											skin="whyGreen" isicon="true"></strong:newdate><%--
										&nbsp;<span class="wz">至</span> &nbsp;
										<strong:newdate name="model.wlogEndTime" id="wlogEndTime"
											dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.wlogEndTime}"
											width="150px" skin="whyGreen" isicon="true"></strong:newdate>
									--%></td>
								</tr>
								<tr>
									<td nowrap class="biao_bg1" align="right">
										<span class="wz">附件：</span>
									</td>
									<td class="td1"
										title="每次上传附件总大小不能超过<%=(com.strongit.oa.util.GlobalBaseData.ATTACH_SIZE / 1024)%>KB">
										<input type="file" style="width: 255px;"
											onkeydown="return false;" class="multi" name="file" />
										${attachFiles}
									</td>
								</tr>
								<tr>
									<td nowrap class="biao_bg1" align="right">
										<span class="wz">模板类别：</span>
									</td>
									<td class="td1">
										<s:select id="docgroupId" name="docgroupId"
													list="typeList" headerKey="" headerValue=""
													listKey="docgroupId" listValue="docgroupName" cssStyle="width:190px"/>
										<input type="button" value="加载模板" class="input_bg"
											onclick="loadTemplate();">
									</td>
								</tr>
								<tr>
									<td colspan="2" valign="top" height="100%">
										<%--<script type="text/javascript"
											src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
										<script type="text/javascript">
											var oFCKeditor = new FCKeditor( 'content' );
											oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
											oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
											oFCKeditor.Width = '100%' ;
				                            oFCKeditor.Height = '260' ;
											oFCKeditor.Value = document.getElementById('desc').innerText;
											oFCKeditor.Create() ;
										</script>
									--%>
										<object id="TANGER_OCX_OBJ"
											classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
											codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=4,0,3,2"
											width="100%" height="100%">
											<param name="ProductCaption" value="思创数码科技股份有限公司">
											<param name="ProductKey" value="B339688E6F68EAC253B323D8016C169362B3E12C">
											<param name="BorderStyle" value="1">
											<param name="TitlebarColor" value="42768">
											<param name="TitlebarTextColor" value="0">
											<param name="TitleBar" value="false">
											<param name="MenuBar" value="true">
											<param name="Toolbars" value="true">
											<param name="IsResetToolbarsOnOpen" value="true">
											<param name="IsUseUTF8URL" value="true">
											<param name="IsUseUTF8Data" value="true">
											<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
										</object>
									</td>
								</tr>
								<%--<tr align="center" style="border: none;background: white">
									<td colspan="2" nowrap>
										<input type="button" value="保 存" class="input_bg"
											onclick="save();">
										&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="button" value="取 消" class="input_bg"
											onclick="windowclose();">
									</td>
								</tr>								
							--%></table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
