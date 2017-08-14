<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存表单</title>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows_add.css">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			language="javascript"></script>
		<script type="text/javascript" language="javascript"
			src="<%=root%>/uums/js/md5.js"></script>
		<style type="text/css">
body,table,tr,td,div {
	margin: 0px;
}
</style>
		<script type="text/javascript">
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
            
		$(document).ready(function() {
		  	var message = $(".actionMessage").text();
			if(message!=null && message!=""){
				if(message.indexOf("error")>-1){
					message = message.replace("error","");
					alert(message);
				}else if(message.indexOf("false")>-1){
					message = message.replace("false","");
					alert(message);
					window.close();
				}else{
					window.returnValue = "reload";
					window.close();
				}
			}
		});
            
		
		 function checkfile(){
		 	var efile = $("#efile").val();
		 	if(efile.trim()==""){
		 		return false;
		 	}
		 	var s =  efile.split(".");
		 	var ext = s[s.length-1];
		 	if(ext=="xml"||ext=="XML"||ext=="EF"||ext=="ef"){
		 		return true;
		 	}else{
		 		return false;
		 	}
		 }
		 
		 function save(){
		 	var id = $("#id").val();
		 	var title = $("#title").val();
		 	var type=$("#type").val();
		 	var orgCode=$("#orgCode").val();
		 	var JSONStr = '{ "title":"' +title+'", "type":"' +type+'", "orgCode":"'+orgCode+ '"}'; 
		 	var JSONObj = eval('(' + JSONStr + ')');

		 	title = title.trim();
		 	if(""==title){
		 		alert("请输入表单名称。");
		 		$("#title").val("");
		 		$("#title").focus();
		 		return ;
		 	}
		 	if(orgCode != "0"){
			 	var name =  $("#orgName").val();
			 	name = name.trim();
			 	if(""==name){
			 		alert("请选择所属部门。");
			 		$("#name").val("");
			 		$("#name").focus();
			 		return ;
			 	}
		 	}
			
			$.post("<%=root%>/eformManager/eformManager!checkTitle.action",
			{id:id,title:title},
			function(data){
			    if(data=="false"){
			        alert("表单名称已存在，请重新输入。");
			    	$("#title").val("");
		 			$("#title").focus();
			        return false;
			    }else{
					window.returnValue=JSONObj;    
					window.close();}
			});
		 }
		 
		 	function tree(){
				window.showModalDialog("<%=path%>/eformManager/eformManager!getOrgTree.action",window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:420px');
				}
				
			function setAreaCodeId(orgCode, orgName){
					document.getElementById("orgCode").value = orgCode ;
					document.getElementById("orgName").value = orgName ;
				}
		</script>
	</head>
	<base target="_self" />
	<body>
		<DIV align="center">
			<s:form action="/eformManager/eformManager!formdesigner.action"
				name="form" method="post" enctype="multipart/form-data">
				<label id="l_actionMessage" style="display: none;">
					<s:actionmessage />
				</label>
				<input type="hidden" id="id" name="model.id" value="${id}">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							 <strong>保存表单</strong>							 </script>
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
				<table border="0" cellpadding="0" cellspacing="0" width="100%"
					height="100%">
					<tr>
						<td valign="top" align="left" width="70%">
							<table border="0" cellpadding="0" cellspacing="0" class="table1"
								width="100%">
								<tr>
									<td class="biao_bg1" width="20%" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;表单名称：&nbsp;</span>
									</td>
									<td class="td1" width="80%">
										<input id="title" name="model.title" type="text"
											value="${model.title}"
											onkeydown="if(event.keyCode==13){save(); return false;}"
											size="45" maxlength="20">
									</td>
								</tr>
								<tr>
									<td nowrap class="biao_bg1" align="right">
										<span class="wz"><font color="red">*</font>&nbsp;表单类型：&nbsp;</span>
									</td>
									<td class="td1">
										<s:select name="model.type" id="type"
											list="#{'SF':'启动表单','QF':'查询表单','VF':'展现表单'}" listKey="key"
											listValue="value" style="width:40%" />
									</td>
								</tr>
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">所属机构：&nbsp;</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<%--<input id="orgName" name="orgName"
												value="${orgName}" type="text" size="22"
												readonly="readonly">
											--%>
											<s:textfield id="orgName" name="orgName" size="22"
												readonly="true"></s:textfield>
											<input id="orgCode" name="model.orgCode" value="${model.orgCode}" type="hidden" size="22">
											<input type="button" name="btnChooseBank" value="..."
												onclick="tree();" class="input_bg">
										</td>
										
									</tr>
									<tr>
									<td>
									</td>
									</tr>
								
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>
