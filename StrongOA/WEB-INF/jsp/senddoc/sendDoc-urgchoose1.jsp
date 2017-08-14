<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","No-cache");
response.setDateHeader("Expires",-10);
%>
<html>
  <head>
    <%@include file="/common/include/meta.jsp" %>
  <title>反馈</title>
    <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
  	 <link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
	 <script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
  	<script src="<%=path%>/oa/js/workflow/common.js" type="text/javascript"></script>
  	<style>
  	#orgusername{
  	 width:420px;
  	}
  	</style>
  	<script type="text/javascript">
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
		
		$(document).ready(function(){
		  /**  获取 文件相关的所有处理人员（已流转过的环节处理人） 并显示在收件人里*/
		   var instanceId=<%=request.getParameter("instanceId") %>;
		   var  url="<%=root%>/senddoc/sendDoc!feedBackProcessPerson.action";	
		      $.post(url,
	    	   {instanceId:instanceId},
	    	   function(retCode){
	    	     var userInfo=retCode.split(":");
	    	     $("#orguserid").val(userInfo[0]);
	    	     $("#orgusername").html(userInfo[1]);
	    	     //$("#handlerMes").html("《"+userInfo[2]+"》的反馈信息：");
	    	     $("#xiha").html("标题:《"+userInfo[2]+"》的反馈信息");
	    	     $("#xiha1").val("《"+userInfo[2]+"》的反馈信息");
	    	   }
	     	);
			});
					
			
  	</script>
  	</head>
  <base target="_self">
  <body class="contentbodymargin">
  
  	<DIV id=contentborder align=center>
  		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
					<s:form action="/senddoc/sendDoc!feedBackProcessByPerson.action">
							<input type="hidden" id="xiha1" name="xiha1"/>
							<input id=" " type="hidden" name="instanceId" value="<%=request.getParameter("instanceId") %>" /><!-- 流程实例id -->
							<input id="feedBack" type="hidden" name="feedBack" value="<%=request.getParameter("feedBack") %>" /><!-- 流程实例id -->
							<table height="30" width="100%" border="0" cellspacing="0"
								cellpadding="0">
								<tr>
									<td
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								                  <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                  </td>
												<td align="left">
												<strong> 反馈
			                                      </strong>
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="urgencyProcessByPerson();">&nbsp;确&nbsp;定&nbsp;</td>
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
							<td colspan="2"><span class="wz" style="color:#999999">&nbsp;&nbsp;&nbsp;&nbsp;默认将文件的反馈意见以即时消息或者内部邮件的形式发送给文件相关的所有处理人员(已流转过的环节处理人)</span></td>
						    </tr>
					          <tr>
									<td align="left" width="15%" valign="top">
										<span >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">*</font>收件人：</span>
									</td>
									<td class="td1" align="left">
									   <s:textarea title="双击选择收件人" cols="40" id="orgusername" name="msgReceiverNames" ondblclick="addPerson();"  rows="4" readonly="true"></s:textarea>
										<input type="hidden" id="orguserid" name="orguserid" value="${orguserid}" ></input>
										<a id="addPerson"  href="#" class="button" onclick="addPerson()">添加</a>&nbsp;
										<a id="clearPerson" href="#" class="button" onclick="clickClear()">清空</a>
											
									</td>
				          	</tr>
						     <tr>
						     <td colspan="2">
							<!-- 提醒方式标签 -->
							<strong:remind isShowButton="false" includeRemind="RTX,MSG"  code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
		 					 	</td>
		 					 </tr>
		 					 <tr>
							<td class="table1_td" colspan="2"></td>
							<td></td>
						</tr>
		 					 </table>	
						</s:form>
					</td>
				</tr>
			</table>
  	</DIV>
  </body>
</html>