<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	taglib uri="/tags/web-remind" prefix="remind"%>
<%@	taglib uri="/tags/web-newdate" prefix="sq"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.io.UnsupportedEncodingException"%>
<%
	    response.setHeader("Cache-Control", "no-store");
	    response.setHeader("Pragrma", "no-cache");
	    response.setDateHeader("Expires", 0);
%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
    <title>强制取回任务</title>
    <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
  	 <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
	 <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
  	<script src="<%=path%>/oa/js/workflow/common.js" type="text/javascript"></script>
  	<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		function save(){
			var conent = $("#remindCon").val();
			conent = $.trim(conent);
			if(conent==""){
				alert("提醒内容不能为空。");
				$("#remindCon").val("");
				return;
			}
			var remindType = "";
			//获取提醒方式
			$("input:checkbox:checked").each(function(){
				remindType = remindType + $(this).val() + ",";
			});
			if(remindType!=""){
				remindType = remindType.substring(0,remindType.length-1);
			}else{
				if(!confirm("您没有设置提醒方式，系统无法给相关处理人发送提醒，是否继续？")){
					return;
				}
			}
			var returnValue = new Array(2);
			//提醒内容
			returnValue[0] = ";;;2;"+$("#remindCon").val();
			//提醒方式
			returnValue[1] = remindType;
			window.returnValue = returnValue;
			window.close();
		}
		
		$(document).ready(function(){
			<%
				String instanceId = (String)request.getParameter("instanceId");
			%>
			 $.post("<%=root%>/senddoc/sendDoc!getCurrentUserInfo.action",{instanceId:'<%=instanceId%>'},function(ret){
			 		$("#remindobject").html(ret);
      			});       
		});
		</script>
  </head>
  <base target="_self">
  <body class="contentbodymargin">
  	<DIV id=contentborder align=center>
		<s:form action="/senddoc/sendDoc!urgencyProcessByPerson.action">
  		<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>设置提醒</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;确&nbsp;定&nbsp;</td>
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
						<td>
						<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
						<font color="#999999">风险提示：该操作可能导致其他正在处理此文的用户无法正常办理。</font>
						<tr >
							<td class="biao_bg1" align="right" >
								<span class="wz">&nbsp;提醒对象：&nbsp;</span>
							</td>
							<td class="td1" id="remindobject" style="padding-left:5px;">
							</td>
						</tr>
						<tr id="isRemind_type" >
							<td nowrap class="biao_bg1" align="right">
								<span class="wz"><FONT color="red">*</FONT>&nbsp;<span class="wz">提醒方式：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								<remind:remind isShowButton="false" isOnlyRemindInfo="true"
									includeRemind="RTX,SMS" rtxChecked="checked"
									code="<%=GlobalBaseData.SMSCODE_WORKFLOW%>" />
							</td>
						</tr>
						<tr id="isRemind_con"  >
							<td nowrap class="biao_bg1" align="right" valign="top">
								<span class="wz"><FONT color="red">*</FONT>&nbsp;<span class="wz">提醒内容：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								<textarea id="remindCon" name="remindCon" type="text"
									style="width: 90%" rows="11"></textarea>
							</td>
						</tr>
						</s:form>
					</td>
				</tr>
			</table>
  	</DIV>
  </body>
</html>