<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="java.text.NumberFormat" />
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-query" prefix="strong"%>
<%@ include file="/common/include/rootPath.jsp"%>
<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
<%
  //获取流程标题
  String workflowtitle=request.getParameter("workflowtitle");
 
%>
<html>
	<head>
		<%@ include file="/common/include/meta.jsp"%>
		<title>转发邮件</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript">
			function sendM(){
				var mailContent=$("#mailContent").val();
				var mailMain=$("#mailMain").val();
				var orgusername=$("#orgusername").val();
				if($.trim(mailMain)==null||""==$.trim(mailMain)){
					alert("请输入邮件主题。");
					return;
				}
				if($.trim(orgusername)==null||""==$.trim(orgusername)){
					alert("请输入收件人。");
					return;
				}
				if($.trim(mailContent)==null||""==$.trim(mailContent)){
					alert("请输入邮件内容。");
					return;
				}
				if(mailContent.length>1000){
					alert("邮件内容长度不能大于1000。");
					return;
				}
				$("#sendMailForm").submit();
			}
			//选择接收人
			function addPerson(){
				var url="<%=root%>/address/addressOrg!tree.action?isShowAllUser=1&typewei=feedBack";		
				var ret=OpenWindow(url,"600","400",window);
			}
			//清空接收人
			 function clickClear(){
				
				$("#orgusername").val("");
				$("#orguserid").val("");
					}
			
		</script>
	</head>
	<body class="contentbodymargin" >
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
		<s:form id="sendMailForm" theme="simple" action="/senddoc/sendDoc!saveMail.action">
		<!-- 流程名称 -->
							<s:hidden id="workflowName" name="workflowName"></s:hidden>
							<!-- 表单id -->
							<s:hidden id="formId" name="formId"></s:hidden>
							<!-- 表名称 -->
							<s:hidden id="tableName" name="tableName"></s:hidden>
							<!-- 公文处理类别 0：个人办公 1：发文处理 2：收文处理-->
							<s:hidden id="handleKind" name="handleKind"></s:hidden>
							<s:hidden id="workflowType" name="workflowType"></s:hidden>
							<input type="hidden" name="workflowtitle" id="workflowtitle" value="${workflowtitle }" />
							<s:hidden id="pkFieldValue" name="pkFieldValue"></s:hidden>
							
							<!-- 资源名称 -->
							<input type="hidden" name="privilName" id="privilName" value="<%=privilName%>" />
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
							<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>
									转发邮件
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
										    <td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="sendM()">&nbsp;发&nbsp;送&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<%--<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="resett()">&nbsp;主题重置&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="5"></td> --%>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1"  onclick="window.close()">&nbsp;取&nbsp;消&nbsp;</td>
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
					<tr>
						<td  class="biao_bg1" align="right" width="20%">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;邮件主题：</span>
						</td>
						<td class="td1" align="left" style="padding-left:5px;" width="80%" >
						<input id="mailMain" name="mailMain" type="text" style="border:1px solid #b3bcc3;background-color:#ffffff;" size="52" value="${mailMain}" maxLength="100"/> 
						</td>
					</tr>
					<tr>
						<td class="biao_bg1" align="right" width="20%" valign="top">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;收件人：</span>
						</td>
						<td class="td1"  align="left" style="padding-left:5px;" width="80%" > 
						   <s:textarea title="双击选择收件人" cols="40" id="orgusername" name="msgReceiverNames" ondblclick="addPerson();"  rows="4" readonly="true"></s:textarea>
						     <input type="hidden" id="orguserid" name="orguserid" value="${orguserid}" ></input>
							<table width="100%">
								<tr width="100%">
									<td align="left" width="50%">
										<a id="addPerson"  href="#" class="button" onclick="addPerson()">添加</a>&nbsp;
										<a id="clearPerson" href="#" class="button" onclick="clickClear()">清空</a>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr><td > <br> </td></tr>
					<tr>
						<td  class="biao_bg1" align="right" valign="top" width="20%" >
							<span class="wz"><FONT color="red">*</FONT>&nbsp;关联文件：</span>
						</td>
						 <td class="td1" style="width:349px;word-break:break-all;line-height: 1.4" width="80%" >
										
									${workflowtitle}

									</td>
						
					</tr>
					<tr>
						<td  class="biao_bg1" align="right" valign="top" width="20%">
							<span class="wz"><FONT color="red">*</FONT>&nbsp;邮件内容：</span>
						</td>
						<td class="td1"  align="left" style="padding-left:5px;" width="80%" >
						   <textarea cols=40 rows=8 id="mailContent" name="mailContent" onpropertychange="if(value.length>1000) value=value.substr(0,1000)" ></textarea>
						 
						 <br>
							<font color="#999999">请不要超过1000字</font></td>
					</tr>
					
					<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
				</table>
				<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
				</table>

			</s:form>
		</DIV>
	</body>
</html>
