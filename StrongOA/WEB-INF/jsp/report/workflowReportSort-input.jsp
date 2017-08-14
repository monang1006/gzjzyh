<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%> 
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
		<title>报表类型</title>
		<!--页面样式  -->
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css
			rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script> 
	<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
	<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
            
		$(document).ready(function() {
			//选择接收人
		$("#addEform").click(function(){
			var ret=OpenWindow(this.url,"600","400",window);
		});
		
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
            
		 
		 function save(){
		 	var sortId=$("#sortId").val();
		 	var sortName = $("#sortName").val();
		 	var 
		 	sortName = sortName.trim();
		 	if(""==sortName){
		 		alert("请输入报表类型名称!");
		 		$("#sortName").val("");
		 		$("#sortName").focus();
		 		return ;
		 	}
		 	var sortSequence=$("#sortSequence").val().trim();
		 	if(sortSequence!=null&&sortSequence!=""){
			 	if(isNaN(sortSequence)){
			 	
			 		alert("排序号只能输入数字!");
			 		$("#sortSequence").val("");
		 			$("#sortSequence").focus();
			 		return ;
			 	}		 	
		 	}
		 	 $.post('<%=path%>/report/workflowReportSort!isHasSortName.action',
		             { 'sortName':sortName,'sortId':sortId},
		              function(data){
		              if(data=='1'){
		              	alert("报表类型名称已存在，请重新输入!");
				 		$("#sortName").val("");
				 		$("#sortName").focus();
				 		return ;
		              }else{	              
						form.submit();
		              }
		       });
		 	
		 }
		 
		 function goBack(){
			window.location="<%=path%>/report/workflowReportSort.action";
		} 
		
	
		</script>
	</head>
	<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;">
	<DIV  id=contentborder align=center>
	<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
	<s:form action="/report/workflowReportSort!save.action" name="form" method="post" enctype="multipart/form-data">
	<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
	<input type="hidden" id="sortId" name="model.sortId" value="${model.sortId}">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							   <script>
							 var id = document.getElementById("sortId").value;
							 if(id==null|id==""|id==" "){
							 	document.write("增加报表类型");
							 }else{
							 	document.write("编辑报表类型");
							 }
							 </script>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="goBack();">&nbsp;取&nbsp;消&nbsp;</td>
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
					<td class="biao_bg1" align="right">
						<span class="wz"><FONT color="red">*</FONT>&nbsp;类型名称：</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<input id="sortName" name="model.sortName" type="text" value="${model.sortName}"  onkeydown="if(event.keyCode==13){save(); return false;}"
							 size="50" maxlength="45" >
					</td>
				</tr>
				<tr>
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">类型描述：</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<input id="sortDesc" name="model.sortDesc" value="${model.sortDesc}" type="text" size="50" maxlength="45">
					</td>
				</tr>			
				<tr>
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">排序序号：</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<input id="sortSequence" name="model.sortSequence" value="${model.sortSequence}" type="text" size="50" maxlength="3">
					</td>
				</tr>			
				<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>		
				
		
	</table>
	</s:form>
	</DIV>
	</body>
</html>
