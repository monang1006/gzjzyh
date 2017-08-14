<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/eformOCX/version.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>内部分发统计</title>
		<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
		<LINK href="<%=path%>/common/js/tabpane/css/luna/tab.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<script language='javascript' src='<%=root%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language="javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<style type="text/css">
			.AutoNewline
			{
			  word-break: break-all;/*必须*/
			}
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
			$(document).ready(function(){
		          var eform = document.getElementById("FormInputOCX");
		          eform.InitialData('<%=basePath%>/services/EFormService?wsdl');
		          eform.SetFormTemplateID(101);
		          //.width和.height必须是小写
				  eform.width=eform.MaxWidth;//设置电子表单OCX的宽度为内表单最度的宽度
		  		  eform.height=eform.MaxHeight;//设置电子表单OCX的高度为内表单最高的高度
		  		  //alert('${docId}');
		          var ret=eform.AddFilterParams("T_OA_DOCDIS", "SENDDOC_ID", '${docId}');
		          ret=eform.LoadFormData();
		          eform.SetFieldsReadOnly(true);
		        // initWordOCX();
			});
			function initWordOCX() {
			   var FormInputOCX = document.getElementById("FormInputOCX");
			   var TANGER_OCX_OBJ = FormInputOCX.OfficeControl;
			   FormInputOCX.SetOfficePageActive();
			   TANGER_OCX_OBJ.ToolBars=false;
			   TANGER_OCX_OBJ.Menubar=false; 
			   TANGER_OCX_OBJ.SetReadOnly(true,"");
			}
			
			function setType(){
		   		return "view";
		   }
		</script>
	</head>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="45" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td>&nbsp;</td>
								<td width="30%">
									<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
									内部分发统计
								</td>
								<td width="70%">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<DIV class=tab-pane id=tabPane1>
				<SCRIPT type="text/javascript">
					tp1 = new WebFXTabPane( document.getElementById( "tabPane1") ,false );
				</SCRIPT>
				<div class=tab-page id=tabPage1>
					<H2 class=tab>
						内部分发统计
					</H2>
					<tbody>
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
						<tr>
							<td class="biao_bg1" width="15%" align="right">
								<span class="wz">接收状态：</span>
							</td>
							<td class="td1" width="75%">
								<div id="allre" style="display:;">
									<span class="wz">接收文档总数：<font color=red>${hasreply+unreply}</font>人，已确认签收：<font color=red>${hasreply}</font>人，尚未签收：<font color=red>${unreply}</font>人</span>
								</div> 
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right">
								<span class="wz">标&nbsp;&nbsp;&nbsp;&nbsp;题：</span>
							</td>
							<td class="td1" width="75%">
								<span class="wz">${docDis.senddocTitle}</span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right">
								<span class="wz">发送日期：</span>
							</td>
							<td class="td1" width="75%">
								<span class="wz"><s:date name="afterflow.getDocDate" format="yyyy-MM-dd"/></span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="15%" align="right">
								<span class="wz">未进行签收的人员：</span>
							</td>
							<td class="td1 AutoNewline" width="75%">
									<span class="wz">${unReplyUser}</span>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="button" name="more" id="more" class="button" style="display: none;" value="更多">
							</td>
						</tr>
						<tr>
							<td class="td1" width="100%" colspan="2">
								<iframe align="top" width="98%" height="300" frameborder="0" src="<%=root %>/docdis/docDis!viewInnerflow.action?docId=${docId}"></iframe>
							</td>
						</tr>
						<tr>
							<td class="td1" width="100%" colspan="2" align="center" cosplan=2>
								<input type="button" class="input_bg" id="print" name="print" value="打印" onclick="window.print()">
							</td>
						</tr>
					</table>
					</tbody>
					<SCRIPT type="text/javascript">tp1.addTabPage(document.getElementById( "tabPage1" ));</SCRIPT>
				</div>
				<div class=tab-page id=tabPage2 style="display:none">
					<H2 class=tab>
						文档内容
					</H2>
					<SCRIPT type="text/javascript">
						tp2 = new WebFXTabPane( document.getElementById( "tabPane2") ,false );
					</SCRIPT>
					<div id="formef">
						<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0"
							style="vertical-align: top;">				
							<tr>
								<td height="40" class="tabletitle">
									<table width="100%" border="0" align="right" cellpadding="0"
										cellspacing="0">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
													alt="">
												发文处理单
											</td>
											<td width="70%">
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
					</div>
				</div>
			</DIV>
		</div>
	</body>
</html>
