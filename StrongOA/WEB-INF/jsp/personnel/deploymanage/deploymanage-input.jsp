<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/meta.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>调配类别管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<base target="_self">
		<script type="text/javascript">
		function formsubmit(){
		
		 var inputDocument=document;
	 if(inputDocument.getElementById("pdepName").value==""){
    	alert("调配类别不能为空，请输入。");
    	inputDocument.getElementById("pdepName").focus();
    	return false;
    }
     if(inputDocument.getElementById("pdepIsveteran").value=="1"){
    	inputDocument.getElementById("pdepEditcode").value="";
    }
     
			document.forms[0].submit();
			 		
		}
		function checkVetern(strval){
		if(strval=="1"){
		if(document.getElementById("pdepEditcode").value!=""){
		alert("转入老干部调配信息字段将为空，确定转入吗？");
		document.getElementById("pdepEditcode").value="";
		}
		}
		
		}
		function addCodes(){
		  var struct="40288239230c361b01230c7a60f10015";//人员信息项主键
			var url= '<%=root%>/personnel/deploymanage/deploymanage!rowlist.action?struct='+struct;
  			var codes=showModalDialog(url,window,'dialogWidth:300pt ;dialogHeight:280pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
		}
	
</script>
	</head>
	<body oncontextmenu="return false;" style="overflow: auto">
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="20%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						调配类别管理
					</td>
					<td width="*">
						&nbsp;
					</td>
				</tr>
			</table>
			<s:form action="/personnel/deploymanage/deploymanage!save.action" method="post"
							id="22" theme="simple">
			<input type="hidden" id="deployId" name="model.pdepId"
								value="${model.pdepId}">
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right">
						<span class="wz">调配类别(<font color="red">*</font>)：</span>
					</td>
					<td class="td1"  align="left">
						<input id="pdepName" name="model.pdepName" value="${model.pdepName}" type="text" size="35" maxlength="16">
					</td>
				</tr>
				<tr>
					<td width="30%" height="21" class="biao_bg1" align="right">
						<span class="wz">调配信息字段：</span>
					</td>
					<td class="td1"  align="left">
				 <s:textarea cols="30" id="pdepEditname" name="model.pdepEditname" rows="6" readonly="true"></s:textarea>
		 <input id="pdepEditcode" name="model.pdepEditcode" value="${model.pdepEditcode}" type="hidden">
							             

					 <a id="addPerson" onclick="addCodes();" href="#">选择</a>
							     
					</td>
				</tr>
				
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">是否转入老干部表：</span>
					</td>
					<td class="td1"  align="left">
	 <s:select name="model.pdepIsveteran" id="pdepIsveteran" list="#{'0':'未转入','1':'转入'}" listKey="key" listValue="value" style="width:9.0em" onchange="checkVetern(this.value);" />
					</td>
				</tr>
				
				<tr>
					<td height="21" class="biao_bg1" align="right">
						<span class="wz">是否启用：</span>
					</td>
					<td class="td1"  align="left">
		<s:select name="model.pdepIsactiv"  list="#{'0':'未启用','1':'启用'}" listKey="key" listValue="value" style="width:9.0em" />
					</td>
				</tr>
			
							
				<tr>
					<td class="td1" colspan="2" align="center">
						<input name="Submit" type="button" onclick="formsubmit();" class="input_bg" value="保 存">&nbsp;&nbsp;&nbsp;
						<input name="Submit2" type="button" class="input_bg" value="关 闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
				</s:form>
		</DIV>
	</body>
</html>
