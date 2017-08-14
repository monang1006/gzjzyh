<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>查看模块</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
		<script type="text/javascript">
$(document).ready(function(){
	$("#back").click(function(){
		window.close();
	});
});
</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<%--<form id="myTable" action="<%=path%>/updatebadwords/phraseFilter/phraseFilter!save.action">--%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>查看过滤模块</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close()">&nbsp;关&nbsp;闭&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="5"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				
						<input id="filtrateModuleId" name="filtrateModuleId" type="hidden"
							size="32" value="${modle.filtrateModuleId}">
						<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
							<tr>
								<td class="biao_bg1" align="right">
									<span class="wz">过滤模块：</span>
									
								</td>
								<td class="td1" >
								    <span class="wz">${modle.moduleId}</span>
									
								</td>
							</tr>
							<tr>
								<td  class="biao_bg1" align="right">
									<span class="wz">是否开启：</span>
										<input type="hidden" id="filtrateOpenstate"
										value="${modle.filtrateOpenstate}" size="30"
										readonly="readonly">
								</td>
								<td class="td1" >
								<s:if test="modle.filtrateOpenstate!=null&&modle.filtrateOpenstate==\"0\"">
								 <span class="wz">开启</span>
									
								</s:if>
								<s:else>
								  <span class="wz">关闭</span>
									
								</s:else>
								</td>
							</tr>

							<tr>
								<td  class="biao_bg1" align="right">
									<span class="wz">过滤提示：</span>
								</td>
								<td class="td1" style="word-break:break-all;line-height: 1.4">
									${modle.filtrateMsg}
								</td>
							</tr>
						</table>
						
					</td>
				</tr>
			</table>
			<%--	</form>--%>
		</DIV>
	</body>
</html>
