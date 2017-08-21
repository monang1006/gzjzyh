<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查询申请</title>
	<%@ include file="/common/include/meta.jsp"%>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_special.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>	
<style type="text/css">
		body{
			width:100%;
			margin : 0px;
			height: 100%
		}
		#inHtml{
			CURSOR: default; 
			width: 300px;
			height: 100px;
		}
		#setAllUser{ border:none;}
		
		
		</style>
<script type="text/javascript">
function formsubmit(){
    if($.trim(document.getElementById("templateName").value)==""){
    	alert("模板名称不能为空，请输入。");
    	document.getElementById("templateName").focus();
    	return false;
    }
    if($.trim(document.getElementById("templateDesc").value)==""){
    	alert("模板标识不能为空，请输入。");
    	document.getElementById("templateDesc").focus();
    	return false;
    }
    var tId = document.getElementById("templateId").value
    var tName= document.getElementById("templateName").value
    var tDesc = document.getElementById("templateDesc").value
    
	$.post("<%=root%>/infopubTemplate/infoTemplate!checkExit.action",
	{tId:tId,tName:tName,tDesc:tDesc},
		function(data){
		    if(data=="bosefalse"){
		        alert("模板名称和模板标识都已存在，请重新输入。");
		    	$("#templateName").val("");
		    	$("#templateDesc").val("");
	 			$("#templateName").focus();
		        return false;
		    }else if(data=="namefalse"){
				 alert("模板名称已存在，请重新输入。");
		    	$("#templateName").val("");
	 			$("#templateName").focus();
		        return false;
		    }else if(data=="descfalse"){
		    	alert("模板标识已存在，请重新输入。");
		    	$("#templateDesc").val("");
	 			$("#templateDesc").focus();
		    }else{
		    	document.getElementById("templatesave").submit();			
		    }
	});
}

function cancel(){
	window.close();
}     	
</script>
	<base target="_self"/>
	<body style="background-color:#ffffff">
	<DIV id=contentborder align=center>
	<s:form action="/infopubTemplate/infoTemplate!save.action" name="templatesave" id="templatesave" method="post" enctype="multipart/form-data">
		<input type="hidden" id="templateId" name="model.templateId" value="${model.templateId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>保存模板</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="formsubmit();">&nbsp;保&nbsp;存&nbsp;</td>
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
				</td>
				</tr>
				<tr>
					<td>
						<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
						<tr>
							<td class="biao_bg1" align="right" >
								<span class="wz"><font color=red>*</font>&nbsp;模板名称：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="templateName"  name="model.templateName" type="text" value="${model.templateName}"  size="45"
									maxlength="25"/>
											 
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;模板标识：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="templateDesc"  name="model.templateDesc" type="text" value="${model.templateDesc}"
											 size="45"
									maxlength="25"/>
											 
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right" valign="top">
								<span class="wz">模板内容：&nbsp;</span>
							</td>
							<td class="td1" style="height: 200px;padding-left: 5px">
								&nbsp;<textarea id="templateContent"  style="width: 80% ;height: 80%;word-break:break-all;" name="model.templateContent" type="text"  >${model.templateContent }</textarea>
											 
								&nbsp;
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
		</s:form>
		</DIV>
	</body>

</html>