<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<title>设置调查对象</title>
		<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height: 100%
}
</style>
<script>
	 function validate(){
		//检查
		var usernames=document.getElementById("orgusername").value;
		if(usernames.length<1){
             if(!confirm("确认设置调查对象为空！")){
                return false ;
              }		
 		}
 		
	   return true ;	 
	 }
	 function save(){
		 document.getElementById("smsForm").submit();
	 }
	 String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
		
		$(document).ready(function() {
			//选择接收人
			$("#addPerson").click(function(){
				var ret=OpenWindow(this.url,"600","400",window);
			});
			
			//清空接收人
			$("#clearPerson").click(function(){
				$("#orgusername").val("");
				$("#orguserid").val("");
			});
			
			if("${model.smsId}"!=""&&"${model.smsId}"!=null){
				var smsForm = document.getElementById("smsForm");
				smsForm.smsCon.value = '${model.smsCon}';
				if("${model.smsRecvId}"!=""&&"${model.smsRecvId}"!=null&&"${model.smsRecvId}"!="null"){
					smsForm.recvUserIds.value = "${model.smsRecvId}";
					$("#orgusername").val("${model.smsRecver}");
				}else{
					$("#checknum").attr("checked",true);
					$("#addNumArea").show();
					$("#ownerNum").val("${model.smsRecvnum}");
				}
			}
		});
		
	 function changNum(obj){
		 	if(obj.checked){
			 	$("#addNumArea").show();
		 	}else{
		 		$("#addNumArea").hide();
		 	}
		 }
</script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" >
	
	<input type="submit" id="submit" name="submit" value="" style="display: none">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center >
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form action="target!setTarget.action" method="post" onsubmit="return validate()" theme="simple" name="smsForm" id="smsForm">
							<input type="hidden" name="vid" value="${vid}">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
							
								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>调查对象设置</strong>
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
							<td>
	
								<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
									<tr>
										<td class="biao_bg1" align="right" valign=top>
											<span class="wz"><font color=red>*</font>选择参与人：</span>
										</td>
										<td class="td1">
										<textarea cols="40" id="orgusername" name="usernames" rows="10" readonly="true">${usernames}</textarea>
										<input type="hidden" id="orguserid" name="userids" value="${userids}"></input>
										<br>&nbsp;&nbsp;
										<a id="addPerson" url="<%=root%>/address/addressOrg!tree.action" href="#" class="button">添加</a>&nbsp;&nbsp;&nbsp;<a id="clearPerson" href="#"  class="button">清空</a>
										</td>
									</tr>
									
								<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>									
									</table>
									</td>
								</tr>
								
							</table>
						</td>
					</tr>
					
				</table>
			</form>
			</td></tr></table>
		</DIV>
		
	</body>

</html>
