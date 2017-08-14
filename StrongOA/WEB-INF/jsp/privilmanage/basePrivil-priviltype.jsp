<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>资源类型</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript"
			src="<%=root %>/common/js/jquery/jquery-1.2.6.js"></script>
		<base target="_self">
	</head>

	<script type="text/javascript">
	    var numtest = /^\d+$/; 
			
		function formsubmit(){
			 var inputDocument = document; 
			 		
		    if(document.getElementById("typeName").value==""){
		    	alert("资源类型名称不能为空，请输入。");
		    	document.getElementById("typeName").focus();
		    	return false;
		    }
		    if(document.getElementById("typeSequence").value==""){
		    	alert("资源类型排序序号不能为空，请输入。");
		    	document.getElementById("typeSequence").focus();
		    	return false;
		    }
		    var sequence=document.getElementById("typeSequence").value;
		    if(!numtest.test(sequence)){
				       alert('排序必须为数字！');
				       document.getElementById("typeSequence").focus();
				       return;
			        }
		    if(document.getElementById("typeMemo").value.length > 200){
		    	alert("资源类型描述字数不能大于200!");
		    	return false;
		    }
			document.forms[0].submit();
		}
	</script>
	<iframe style="width:0; height:0; display:none" id="hiddenFrame" name="hiddenFrame"></iframe>
	<body class="contentbodymargin" oncontextmenu="return false;">
		<DIV id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img src="<%=path%>/common/images/ico.gif" width="9"
													height="9" alt="">&nbsp;
												资源类型管理
											</td>
											<td width="70%">

											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>

						<s:form action="/privilmanage/basePrivil!savePrivilType.action" method="post"
							id="22" theme="simple" target="hiddenFrame">
							<input type="hidden" name="typeId" id="typeId" value="${privilType.typeId }"/>
							<input type="hidden" name="systemId" id="systemId" value="${systemId }"/>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">资源类型名称(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="privilType.typeCategory ==\"0\" || privilType.typeCategory == \"1\"">
											<input id="typeName" name="privilType.typeName"
												type="text" maxLength="21" style="width:11em"
												value="${privilType.typeName}" readOnly="true">
												&nbsp;<font color="red">(系统类型不允许更改)</font>
										</s:if>
										<s:else>
											<input id="typeName" name="privilType.typeName"
												type="text" maxLength="21" style="width:11em"
												value="${privilType.typeName}">
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="15%" height="21" class="biao_bg1" align="right">
										<span class="wz">排序序号(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="typeSequence" name="privilType.typeSequence" type="text"
											size="22" style="width:11em" value="${privilType.typeSequence}"/>
									</td>
								</tr>
								<s:if test="privilType.typeCategory != \"0\" && privilType.typeCategory != \"1\"">
									<tr>
										<td width="25%" height="21" class="biao_bg1" align="right">
											<span class="wz">自定义类别：</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<input id="typeCategory" name="privilType.typeCategory"
												type="text" maxLength="21" style="width:11em"
												value="${privilType.typeCategory}">
										</td>
									</tr>
								</s:if>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">资源类型描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="6" cols="30" id="typeMemo"
											name="privilType.typeMemo">${privilType.typeMemo}</textarea>
									</td>
								</tr>
							</table>
						</s:form>
						<table id="annex" width="90%" height="10%" border="0"
							cellpadding="0" cellspacing="1" align="center" class="table1">

						</table>

						<table width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%">
												<input type="button" class="input_bg"
													value="保  存" onclick="formsubmit();">
											</td>
											<td width="30%">
												<input type="button" class="input_bg"
													value="取  消" onclick="window.close();">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>

	</body>
</html>