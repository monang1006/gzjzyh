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
			href="<%=frameroot%>/css/properties_windows_list.css">
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>

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
			var etime = document.getElementById("wlogEndTime").value;
			if(stime==null|stime==""|etime==null|etime==""){
				alert("请输入开始时间！");
				return;
			}else{
				if(stime>=etime){
					alert("日志开始时间须早于日志结束时间！");
					return;
				}
			}	
			getWLogConContent(form);
			form.submit();
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
<%--			initWordOCX();--%>
<%--			var workLogId = $("#workLogId").val()--%>
<%--			if ((workLogId != "") && (workLogId != undefined)) {--%>
<%--			    openFromURL($("#workLogId").val());--%>
<%--			}--%>
		});
		
		 
		 //初始化WORD控件
        function initWordOCX() {
              var TANGER_OCX_OBJ = document.getElementById("TANGER_OCX_OBJ");
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
	</script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<iframe id="attachDownLoad" src=''
			style="display:none;border:4px solid #CCCCCC;"></iframe>
		<div id="contentborder" align="center">
			<%--<table width="100%" height="100%" border="0" cellspacing="0"
				cellpadding="0" style="vertical-align: top;">
				<tr>
					<td width="100%" class="tabletitle" height="40">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								&nbsp;
								</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
										alt="">&nbsp;
									查看工作日志
								</td>
								<td width="65%">
								<table align="right">
								<tr>
								<td>
									<a class="Operation" href="#" onclick="window.close();">
									<img src="<%=root%>/images/ico/guanbi.gif" width="15" height="15" alt="" class="img_s">
									<span style="cursor:hand">关闭</span></a>
								</td>
								</tr>
								</table>
								</td>
								<td width="5%">
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>--%>
				
				<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查看工作日志</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
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
					
					<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
					
						<input type="hidden" id="wlogTxtCon" name="model.wlogTxtCon" value="${model.wlogTxtCon}">
						<div id="desc" style="display: none">${model.wlogTxtCon}</div>
						<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
						
						<table width="100%" height="100%" align="center" border="0"
							cellpadding="0" cellspacing="1" class="table1"
							style="padding-top: 0;margin-top: 0">
							<tr>
								<td class="biao_bg1" width="16%" align="right">
									<span class="wz"><font color=red>*</font> 标题：</span>
								</td>
								<td class="td1" width="100%">
									<span class="wz">${model.wlogTitle}&nbsp;</span>
										
										
								</td>
							</tr>

							<%--<tr id="onceTaskTime" style="display: ">
								<td nowrap class="biao_bg1" align="right">
									<span class="wz">日期(<font color=red>*</font>)：</span>
								</td>
								<td class="td1" title="请输入开始时间和结束时间">

									<strong:newdate name="model.wlogStartTime" id="wlogStartTime"
										dateform="yyyy-MM-dd" dateobj="${model.wlogStartTime}"
										width="120px" skin="whyGreen" isicon="true" disabled="true"></strong:newdate>
									
								</td>
							</tr>--%>
							
							
							<tr>
											<td class="biao_bg1" align="right">
												<span class="wz">日期：</span>
											</td>
											<td class="td1">
												<s:date name="model.wlogStartTime" format="yyyy年MM月dd日 HH点mm分"/>
											</td>
										</tr>
							
							
							
							
							
							
							<tr>
								<td nowrap class="biao_bg1" align="right">
									<span class="wz">附件：</span>
								</td>
								<td class="td1"
									title="每次上传附件总大小不能超过<%=(com.strongit.oa.util.GlobalBaseData.ATTACH_SIZE / 1024)%>KB">

									${attachFiles}

								</td>
							</tr>
							<tr>
								<td align="left" colspan="2" height="100%">
									<script type="text/javascript"
											src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
										<script type="text/javascript">
											var oFCKeditor = new FCKeditor( 'content' );
											oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
											oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
											oFCKeditor.Width = '100%' ;
				                            oFCKeditor.Height = '500' ;
											oFCKeditor.Value = document.getElementById('desc').innerText;
											oFCKeditor.Create() ;
										</script>
					
									<%--<object id="TANGER_OCX_OBJ"
										classid="clsid:C9BC4DFF-4248-4a3c-8A49-63A7D317F404"
										codebase="<%=root%>/common/OfficeControl/OfficeControl.cab#Version=4,0,3,2"
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
									</object>--%>
								</td>
							</tr>
							<%--<tr align="center" style="border: none;background: white">
									<td colspan="2" nowrap>
										<input type="button" value="关闭" class="input_bg"
											onclick="windowclose();">
									</td>
								</tr>
							--%>
						</table>
				</td>
				</tr>
			</table>
		</div>
	</body>
</html>
