<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看日志</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<SCRIPT>
			
		
	
		//关闭窗体
		function col(){
		window.close();
		}
		
		
 		</SCRIPT>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin  oncontextmenu="return false;">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>

		<s:form id="mykmForm" theme="simple"  action="/knowledge/mykm/mykm!save.action" modth="post">
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
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
													<strong>查看日志</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="col();">&nbsp;关&nbsp;闭&nbsp;</td>
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
							<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
								
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz" >操作人姓名：</span>
                                    </td>
                                    <td class="td1">
									<span class="wz">${model.opeUser}</span>
								  </td>
								</tr>
								
								<tr>
									<td class="biao_bg1" align="right">
										<span class="wz">操作人IP地址：</span>	
									</td>
									<td class="td1">
									<span class="wz">${model.opeIp}</span>
								  </td>
									
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">操作时间：</span>
									</td>
									<td class="td1">
									
									<s:date name="model.opeTime" format="yyyy年MM月dd日 HH点mm分"/>
								  </td>
									
								</tr>
							
								<tr>
									<td  class="biao_bg1"  align="right">
										<span class="wz">日志信息：</span>
									</td>
									<td class="td1" style="word-break:break-all;line-height: 1.4">
										
									${model.logInfo}

									</td>
								</tr>
								
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						
					</td>
				</tr>
			</table>
						</s:form>
		</DIV>
	</body>
</html>
