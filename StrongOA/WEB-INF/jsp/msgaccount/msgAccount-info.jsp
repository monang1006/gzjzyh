<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>


<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<title>查看短信</title>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#delBtn").click(function(){
			if(confirm("确定要删除吗？")){
			  	$.ajax({
			  		type:"post",
			  		dataType:"text",
			  		url:"<%=root%>/msgaccount/msgAccount!delete.action",
			  		data:"ids="+$("#delId").val(),
			  		success:function(msg){
			  			if(msg=="true"){
			  				window.returnValue="true";
			  				window.close();
						}else{
							alert("删除失败,请您重新进行删除");
			  			}
			  		}
			  	});
			  }
			});
		});
	</script>
	</head>
	<body class="contentbodymargin" >
		<input type="hidden" name="delId" id="delId" value="${message.replyMessageId }">
		<DIV id=contentborder align=center >
			<form name="form1">
				<%--<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td height="30" colspan="2"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													查看手机短信
												</td>
												<td>
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
								--%>
								
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
													<strong>查看手机短信</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="$('#delBtn').click()">&nbsp;删&nbsp;除&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
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
									<td>
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
										<tr>
											<td width="25%" height="21" class="biao_bg1" align="right">
												<span class="wz">运营商号码：</span>
											</td>
											<td class="td1">
												<span class="wz">${message.replyNumber}</span>
											</td>
										</tr>
										<tr>
											<td width="25%" height="21" class="biao_bg1" align="right">
												<span class="wz">短信内容：</span>
											</td>
											<td  class="td1">
												<div style="margin: 5px;">${message.replyContent}</div>
											</td>
										</tr>
										<tr>
											<td width="25%" height="21" class="biao_bg1" align="right">
												<span class="wz">接收时间：</span>
											</td>
											<td class="td1">
												<s:date name="message.replyTime" format="yyyy年MM月dd日 HH点mm分"/>
											</td>
										</tr>
										<tr>
											<td width="25%" height="21" class="biao_bg1" align="right">
												<span class="wz">对应短信猫号码：</span>
											</td>
											<td class="td1">
												<span class="wz">${message.senderNumber}</span>
											</td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr align="center">
						<td>
						<br>
							<input type="hidden" class="input_bg" id="delBtn" value="删除" />
							<input type="hidden" class="input_bg" value="关闭" onclick="window.close();" />&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</form>
		</DIV>
	</body>

</html>
