<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>文件借阅审核</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<%
			String borrowId = request.getParameter("borrowId");
		%>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
			function onSub(){
			      var value=$("#desc").text();
			       if(value.length >=200)
			       {
			       alert("审核意见不可以超过200字！");
			          return ;
			       }
			  archiveBorrowForm.submit();
			}
		</script>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="archiveBorrowForm" theme="simple" action="/archive/archiveborrow/archiveBorrow!audit.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>文件借阅审核</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="onSub();">&nbsp;确&nbsp;定&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
										                </tr>
										            </table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  height="21" class="biao_bg1" align="right">
										<span class="wz">是否通过：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input type="radio" checked="checked" name="model.borrowAuditing" value="1">
										通过
										<input type="radio" name="model.borrowAuditing" value="3">
										不通过
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1"  align="right">
										<span class="wz">借阅申请人：</span>
									</td>
									<td class="td1" align="left" colspan="2">
									${model.borrowPersonname}
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1"  align="right">
										<span class="wz">借阅原因：</span>
									</td>
									<td class="td1" align="left" colspan="2">
									<textarea rows="4" cols="50" id="borrowDesc" readonly="readonly"  onkeydown="return false;"  name="model.borrowDesc">${model.borrowDesc}</textarea>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1"  align="right">
										<span class="wz">审核意见：</span>
									</td>
									<td class="td1" align="left" colspan="2">
									<textarea rows="6" cols="50" id="desc"  name="model.borrowAuditingDesc">${model.borrowAuditingDesc}</textarea>
									<br>
										<font color="#999999">请不要超过200字</font></td>
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