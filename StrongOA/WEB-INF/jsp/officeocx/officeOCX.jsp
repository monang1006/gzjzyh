<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!--  引用标签处,如:-->
<%@taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/OfficeControl/version.jsp"%>
<html>
	<head>
		<title>office控件公文页面</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
		<link href="<%=path%>/common/frame/css/properties_windows.css"
			type="text/css" rel="stylesheet">	
		<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
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
            
            function DisableSomeActions() {
              var TANGER_OCX= document.getElementById("TANGER_OCX_OBJ");
              var openpath=document.getElementsByName("openpath");
              //禁用新建功能
              TANGER_OCX.FileNew=false;
              //禁用保存功能
              TANGER_OCX.FileSave=false;
              //禁用打印功能
              TANGER_OCX.FilePrint=false;
              //禁用关闭功能
              TANGER_OCX.FileClose=false;
              //禁用另存为功能
              TANGER_OCX.FileSaveAs=false;           
           	  //TANGER_OCX.OpenFromURL("<%=path%>/"+openpath.value);
           	  //with(TANGER_OCX.ActiveDocument.Application)
           	  
			  //获取当前系统用户
              UserName = '当前系统用户';

              TANGER_OCX.ActiveDocument.TrackRevisions = true;
              TANGER_OCX.ActiveDocument.CommandBars("Reviewing").Enabled = false;
              TANGER_OCX.ActiveDocument.CommandBars("Track Changes").Enabled = false;
              TANGER_OCX.IsShowToolMenu = false;  //关闭或打开工具菜单
              TANGER_OCX.ActiveDocument.ShowRevisions = true;  
            }
             $(document).ready(function(){
				var opentypevalue=$("#opentype").val();
				if(opentypevalue=='new'){
					initWordOCX();
				}else{
					DisableSomeActions();
				}
			});
            
		</script>
	</head>
	<base target="_self">
	<body class="contentbodymargin" oncontextmenu="return false;" >
		<s:form id="officeForm" action="" method="POST"
			ENCTYPE="multipart/form-data">
			<%--是新建:0还是编辑:1--%>
			
			<input id="opentype" name="model.opentype" type="hidden" value="${model.opentype}">
			<%--如果是编辑则打开文档的URL地址--%>
			<s:hidden name="model.openpath" id="openpath" />
			<%--保存文档的URL地址 --%>
			<s:hidden name="model.savepath" id="savepath" />
			<%--功能按钮权限控制 --%>

			<div id="contentborder" align="center">
				<table width="100%" height="100%" border="0" cellspacing="0"
					cellpadding="0" style="vertical-align: top;">
					<tr>
						<td valign="top" width="10%" height="100%">
							<table width="100%" align="center" border="0" cellpadding="0"
								cellspacing="1" class="table1">
								<tr>
									<td nowrap align="center" class="biao_bg1">
										文件操作
									</td>
								</tr>
								<tr onclick="" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										打开文件
									</td>
								</tr>
								<tr onclick="" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										页面设置
									</td>
								</tr>
								<tr onclick="saveDoc();"
									style="cursor: pointer; line-height: 20px;" title="保存为草稿">
									<td nowrap class="td1">
										<img src="<%=path%>/common/images/baocun.gif" width="14"
											height="14" alt="">
										保存
									</td>
								</tr>
								<tr onclick="" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										保存到本地
									</td>
								</tr>
								<tr onclick="closeDoc();"
									style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										<img src="<%=path%>/common/images/guanbi.gif" width="14"
											height="14" alt="">
										关闭
									</td>
								</tr>
								<tr>
									<td nowrap align="center" class="biao_bg1">
										公文模板
									</td>
								</tr>
								<tr onclick="" style="cursor: pointer; line-height: 20px;">
									<td nowrap class="td1">
										打开模板
									</td>
								</tr>
							</table>
						</td>
						<td width="90%">
							<table width="100%" height="100%" align="center" border="0"
								cellpadding="0" cellspacing="1" class="table1">

								<tr>
									<td colspan="2" valign="top" height="100%">
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
											<span style="color: red">不能装载文档控件。请在检查浏览器的选项中检查浏览器的安全设置。</span>
										</object>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</s:form>
	</body>
</html>
