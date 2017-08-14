<%@ page contentType="text/html; charset=utf-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>报表显示页</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>	
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>	
	</head>
	<script type="text/javascript">
		function exportPdfs(){
			location=scriptroot+"/workflowreport/workFlowReport!exportpdf.action?processTypeId="+${processTypeId};
		}
			
		function exportExcels(){	
			location=scriptroot+"/workflowreport/workFlowReport!exportxls.action?processTypeId="+${processTypeId};
		}
		
		$(document).ready(function(){
			$("#column").attr("disabled","disabled");
			$("#chartview").click(function(){
				$("#column").attr("disabled","");
				$("#expexcel").css("display","none");
				$("#exppdf").css("display","none");
				$("#chartview").attr("disabled","disabled");
				$("#SearchContent").attr("src","<%=root%>/workflowreport/chart.action?processTypeId=${processTypeId}");
			})
			$("#column").click(function(){
				$("#chartview").attr("disabled","");
				$("#expexcel").css("display","");
				$("#exppdf").css("display","");
				$("#column").attr("disabled","disabled");
				$("#SearchContent").attr("src","<%=root%>/workflowreport/report_HTML.action?processTypeId=${processTypeId}");
			})
		});
		
	</script>
	<body class="contentbodymargin">
		<DIV id=contentborder align=center>
			<s:hidden id="processTypeId" name="processTypeId"/>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%" align="right">
						<table>
							<tr>
								<td>
									<a id="column" name="column" class="Operation" href=#>
										<img src="<%=root%>/images/ico/tb-list16.gif" width="15" height="15" class="img_s">
										列表显示&nbsp;</a>
								</td>
								<td width="5"></td>
								<td>
									<a id="chartview" name="chartview" class="Operation" href=#>
										<img src="<%=root%>/images/ico/system_ico.gif" width="15" height="15" class="img_s">
										图形显示&nbsp;</a>
								</td>
								<td width="5"></td>
							</tr>
						</table>
						<hr>
						<iframe id='SearchContent' style="display:" name='SearchContent' src='<%=root%>/workflowreport/report_HTML.action?processTypeId=${processTypeId}' 
							frameborder=0 scrolling=yes width='100%' height='390'></iframe>	
						<hr>
						<table align="right">
							
							<tr>
								<td>
									<a id="expexcel" name="expexcel" class="Operation"  href="javascript:exportExcels();">
										<img src="<%=root%>/images/ico/page_excel.gif" width="15" height="15" class="img_s">
										导出为excel&nbsp;</a>
								</td>
								<td width="5"></td>
								<td>
									<a id="exppdf" name="exppdf" type="button" class="Operation" href="javascript:exportPdfs();">
										<img src="<%=root%>/images/ico/file_extension_pdf.gif" width="15" height="15" class="img_s">
										导出为pdf&nbsp;</a>
								</td>
								<td width="5"></td>
							</tr>
						</table>		
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>