<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-newdate" prefix="sq"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
	String processOutTime = (String) (request
			.getParameter("processOutTime") == null ? "" : request
			.getParameter("processOutTime"));
%>
<html>
	<head>
		<title>设置流程期限</title>
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows_add.css">

		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		$(document).ready(function(){
			var processOutTime = "<%=processOutTime%>";
			if(processOutTime != ""){
				$("#remindTime").val(processOutTime);
			}
		});
		function save(){
			var remindTime=$("#remindTime").val();
			if(remindTime==""){
			 alert("流程期限不能为空，请设置流程期限。");
			 return;
			}
			var compareTime=new Date(remindTime.replace(/-/g,"/"));
			if(compareTime.getTime()<=new Number("<%=new Date().getTime()%>")){
				alert("设置时间不能比服务器当前时间早。");
				return;
			}
			window.returnValue = remindTime;
			window.close();
		}

		</script>
	</head>
	<base>
	<body>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								设置流程期限
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
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
				<tr>
					<td valign="top" align="left" width="70%">
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
							<tr style="padding-top:10px;">
								<td class="biao_bg1" align="right" valign="top">
									<span class="wz">注意事项：</span>
								</td>
								<td class="td1" style="padding-top:5px;padding-left:5px;">
									1、设置了流程期限的时间不能早于服务器的当前时间；
									<br />
									2、当流程期限设置和自动强制取回一起启用时，当流程
									<br />
									持续时间到达流程期限设置的时间时，系统会针对该流程
									<br />
									进行自动强制取回；
									<br />
									<br />
								</td>
							</tr>
							<tr id="timer">
								<td align="right" class="biao_bg1">
									<span class="wz"><font color=red>*</font>&nbsp;流程期限：</span>
								</td>
								<td class="td1" align="left">
									&nbsp;
									<sq:newdate name="remindTime" dateobj="<%=new Date()%>"
										id="remindTime" dateform="yyyy-MM-dd HH:mm:ss" width="85%"
										skin="whyGreen" isicon="true"></sq:newdate>
									&nbsp;
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
