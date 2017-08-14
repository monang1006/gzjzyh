<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/meta.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>议题分类管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<base target="_self">
		<script type="text/javascript">
		function formsubmit(){
		
		 var inputDocument=document;
	 if(inputDocument.getElementById("topicsortNo").value==""){
    	alert("议题分类编号不能为空，请输入。");
    	inputDocument.getElementById("topicsortNo").focus();
    	return false;
    }
     if(inputDocument.getElementById("topicsortNo").value.length>16){
   	    alert("议题分类编号长度超过限制，请重新输入。");
    	inputDocument.getElementById("topicsortNo").focus();
    	return false;
    }
    if(inputDocument.getElementById("topicsortName").value==""){
    	alert("议题分类名称不能为空，请输入。");
    	inputDocument.getElementById("topicsortName").focus();
    	return false;
    }
   
    
   
	if(inputDocument.getElementById("topicsortDemo").value.length >=250){
		alert("议题分类描述字数不能大于250!");
		return false;
	}
	
			document.forms[0].submit();
			 
		  //window.returnValue="success";
		   
		   //   window.close();
		
			
			
		
		}
	
</script>
	</head>
	<body oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;&nbsp;
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						议题分类管理
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<s:form action="/meetingmanage/meetingtopicsort/meetingtopicsort!save.action" method="post"
							id="22" theme="simple">
			<input type="hidden" id="sortId" name="topicsortId"
								value="${model.topicsortId}">
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td width="35%" height="21" class="biao_bg1" align="right">
						<span class="wz">分类编号(<font color="red">*</font>)：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="topicsortNo" name="model.topicsortNo" value="${model.topicsortNo}" type="text" size="30" maxlength="30">
					</td>
				</tr>
				<tr>
					<td width="35%" height="21" class="biao_bg1" align="right">
						<span class="wz">分类名称(<font color="red">*</font>)：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<input id="topicsortName" name="model.topicsortName" value="${model.topicsortName}" type="text" size="30" maxlength="30">
					</td>
				</tr>
				<tr>
					<td width="35%" height="21" class="biao_bg1" align="right">
						<span class="wz">审批流程&nbsp;&nbsp;&nbsp;：</span>
					</td>
				
					<td class="td1" colspan="3" align="left">
				<s:select list="processList" headerKey="" headerValue=" 请选择流程" id="processName" name="model.processName" style="width:15em" />
					</td>
				</tr>
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">分类描述&nbsp;&nbsp;&nbsp;：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<textarea rows="10" cols="35" id="topicsortDemo" name="model.topicsortDemo">${model.topicsortDemo}</textarea>
						<font color="red"><br>最多只能输入250字</font>
					</td>
				</tr>
			
							
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit" type="button" onclick="formsubmit();" class="input_bg" value="保 存">
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
				</s:form>
		</DIV>
	</body>
</html>
