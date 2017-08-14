<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@	taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<title>
			<s:if test="model.ptId==null">
				新建流程类型
			</s:if>
			<s:else>
				编辑流程类型
			</s:else>
		</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel="stylesheet">
		<base target="_self" />
		<script type="text/javascript">
		$(document).ready(function(){
			   $("#ptName").keypress(
				        function(event){
				            if(event.keyCode == 13){
				                return false;
				            }
				        }
				    );
		    });
		
		
		
  	String.prototype.trim = function() {
				var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
				strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
			    return strTrim;
			}
		
		//拦截特殊字符  
			function CheckCode(t) {  
			    var Error = "";  
			    //var re = /^[\u4e00-\u9fa5a-z]+$/gi;  
			   var re = /^[a-z\d\u4E00-\u9FA5]+$/i; 
			    if (!re.test(t)) {  
			        Error = "中含有特殊字符，拒绝输入。";  
			    }  
			    return Error;  
			}  

				
	   function submitForm(){
		  if(document.getElementById("ptName").value.trim()==''){
		  	alert('类型名称不能为空。');
		  	document.getElementById("ptName").focus();
		  	return false;
	   	  }else if(document.getElementById("ptName").value.trim()=='null'){
	   			alert('类型名称不能为null。');
			  	document.getElementById("ptName").focus();
			  	return false;
	   	  }else{
	   	  var ptName=document.getElementById("ptName").value
	   	    var ret=CheckCode(ptName);
	             if(ret!=null && ret!=""){
	                 alert("类型名称"+ret);
	               document.getElementById("ptName").focus();
	                 return false;
	             }
	   	  }
	   	   if(document.getElementById("ptDescription").value.length > 200){
	   	 	alert("类型描述字数不能大于200。");
	   	 	return false;
	   	  }
	      document.forms[0].submit();
	   }
	  
	   function cancel(){
		 window.close();
	   }
	
</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<div id="contentborder" align="center">
			<form id="meetform" action="<%=path %>/workflowDesign/action/processType!save.action" method="POST">
				<input type="hidden" id="ptId" name="ptId" value="${model.ptId }" />
				<table width="100%" class="table_headtd">
					<tr>
						<td class="table_headtd_img" >
							<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
						</td>
						<td align="left">
							<script>
							var id = "${model.ptId}";
							if(id==null|id==""){
								window.document.write("<strong>新建流程类型</strong>");
							}else{
								window.document.write("<strong>编辑流程类型</strong>");
							}
							</script>
						</td>
						<td align="right">
							<table border="0" align="right" cellpadding="00" cellspacing="0">
				                <tr>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
				                 	<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
			                  		<td width="5"></td>
				                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
				                 	<td class="Operation_input1" onclick="cancel();">&nbsp;取&nbsp;消&nbsp;</td>
				                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
			                  		<td width="6"></td>
				                </tr>
				            </table>
						</td>
					</tr>
				</table>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz"><font color='red'>*</font>&nbsp;类型名称：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="ptName" name="model.ptName" type="text" maxLength="50" size="30"
								value="${model.ptName }">
						</td>
					</tr>
					<tr>
						<td height="21" class="biao_bg1" align="right" valign="top">
							<span class="wz">类型描述：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<textarea rows="10" cols="60" id="ptDescription"
								name="model.ptDescription" style="overflow: auto">${model.ptDescription }</textarea>
						</td>
					</tr>
					<tr>
						<td class="table1_td"></td>
						<td></td>
					</tr>
				</table>
			</form>
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>