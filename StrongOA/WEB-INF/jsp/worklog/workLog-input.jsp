<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>

<html>
	<head>
		<title>操作内容</title>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows_add.css">
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
		<script src="<%=path%>/common/OfficeControl/officecontrol.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style type="text/css">
			body,table,tr,td,div {
				margin: 0px;
			}
		
		</style>
		
		<script type="text/javascript">
	    $(document).ready(function(){
		$(".biao_bg1").css("width","75px");
		});
  
  </script>
		
		
		<script type="text/javascript">
		
		String.prototype.trim = function() {
            var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
            strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
            return strTrim;
        }
         
		//关闭窗口
		function windowclose(){
			window.returnValue = "OK";
			window.close();
		}
		
		//提交表单
		function save(){
			//标题验证
			var wlogTitle = document.getElementById("wlogTitle");
			if(""==wlogTitle.value.trim()|null==wlogTitle.value.trim()){
				alert("请输入文档标题。");
				wlogTitle.focus();
				return ;
			}
			//时间验证
			var stime = document.getElementById("wlogStartTime").value; 
			if(stime==null|stime==""){
				alert("请输入所添加的日志时间。");
				return;
			}
			
			//获取编辑内容
				getWLogConContent(form);
				$("form").submit();	
				
		}		
		
		//获取说明内容
         function getWLogConContent(form) {
             var oEditor = FCKeditorAPI.GetInstance('content');
             var acontent = oEditor.GetXHTML();
			 form.wlogTxtCon.value = acontent;
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
				}
				else if(message.indexOf("success")>-1){
			        eval("var doc = "+message);
				        if (doc.success == "yes") {
				        //	window.dialogArguments.submitForm();//刷新父页面
				        	window.returnValue = "OK";
		                	window.close();
			        }else{
			            alert("保存文档失败。");
			        }
					
				}
				else{
					window.dialogArguments.submitForm();
					window.close();
				}
			}
			
		});
		

	</script>
	<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<a id="reload" style="display: none" href=""></a>
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

				<input type="hidden" id="wlogTxtCon" name="model.wlogTxtCon" value="${model.wlogTxtCon}">
				<div id="desc" style="display: none">${model.wlogTxtCon}</div>
				<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
										<script>
											 var id = document.getElementById("workLogId").value;
											 if(id==null|id==""|id==" "){
											 	window.document.write("<strong>保存工作日志</strong>");
											 }else{
											 	
											 	window.document.write("<strong>保存工作日志</strong>");
											 }												 
										</script>
									</td>
									<td>
										&nbsp;
									</td>
									<%--<td width="70%">
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
								--%>
								
								<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="windowclose();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
								
								
								
								
								</tr>
							</table>
						</td>
					</tr>
				
					<tr>
					<td>
					<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								
								<tr>
									<td class="biao_bg1" width="16%" align="right">
										<span class="wz"><font color=red>*</font> 标题：</span>
									</td>
									<td class="td1" width="100%">
										<input id="wlogTitle" name="model.wlogTitle" type="text"
											value="${model.wlogTitle}" size="62" maxlength="45">
									</td>
								</tr>
							
								<tr id="onceTaskTime" style="display: ">
									<td nowrap class="biao_bg1" align="right">
										<span class="wz"><font color=red>*</font> 时间：</span>
									</td>
									<td class="td1" title="请输入开始时间">
										<strong:newdate name="wlogStartTime1" id="wlogStartTime"
											dateform="yyyy-MM-dd HH:mm"
											dateobj="${model.wlogStartTime}" width="160px"
											skin="whyGreen" isicon="true"></strong:newdate><%--
										&nbsp;<span class="wz">至</span> &nbsp;
										<strong:newdate name="model.wlogEndTime" id="wlogEndTime"
											dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.wlogEndTime}"
											width="150px" skin="whyGreen" isicon="true"></strong:newdate>
									--%></td>
								</tr>
								<tr>
									<td nowrap class="biao_bg1" align="right" valign="top">
										<span class="wz">附件：</span>
									</td>
									<td class="td1"
										title="每次上传附件总大小不能超过<%=(com.strongit.oa.util.GlobalBaseData.ATTACH_SIZE / 1024)%>KB">
										<input type="file" style="width: 255px;"
											onkeydown="return false;" class="multi" name="file" />
										${attachFiles}
									</td>
								</tr>
<%--								<tr>
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
								</tr>--%>
								<tr>
									<td colspan="2" valign="top"  align="center" height="100%">
										<script type="text/javascript"
											src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
										<script type="text/javascript">
											var oFCKeditor = new FCKeditor( 'content' );
											oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
											oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
											oFCKeditor.Width = '95%' ;
				                            oFCKeditor.Height = '400' ;
											oFCKeditor.Value = document.getElementById('desc').innerText;
											oFCKeditor.Create() ;
										</script>
									</td>
								</tr>
								<tr>
				<td class="table_td"></td>
				<td></td>
				</tr>
				
													
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
