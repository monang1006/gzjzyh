<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>
			查看班组
		</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/jquery.validate.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/validate/formvalidate.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<base target="_self">
		<SCRIPT type="text/javascript">		
			function submitForm(){
			    var groupId=document.getElementById("groupId").value;
			    var groupName=document.getElementById("groupName").value;
			    var groupStime=document.getElementById("groupStime").value;
			    var groupDesc=document.getElementById("groupDesc").value;
				if(groupName==""){
					alert("班组名称不能为空！");
					return;
				}else if(groupStime==""){
					alert("有效时间不能为空！");
					return;
				}else if(groupDesc.length>200){
					alert("备注不能超过200个字符！");
					return;
				}else{	
					document.getElementById("myTable").submit();	   		
				}
			}	
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="" >
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													查看班组
												</td>
												<td width="*">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="groupId" name="groupId" type="hidden" size="32"
								value="${model.groupId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">班组名称：(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="groupName" name="model.groupName" maxlength="25" type="text"
											size="30" value="${model.groupName}" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">班组类型：(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<s:if test="model.groupName==\"1\"">
											<input maxlength="25" type="text"
											size="30" value="倒班" readonly="readonly">
										</s:if>
										<s:else>
											<input maxlength="25" type="text"
											size="30" value="行政办" readonly="readonly">
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">有效时间：(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input name="groupStime" id="groupStime" size="30" readonly="readonly" value="<s:date name="model.groupStime" format="yyyy-MM-dd"/>">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">失效时间：</span>
									</td>
									<td class="td1" align="left">
										<input name="groupEtime" id="groupEtime" size="30" readonly="readonly" value="<s:date name="model.groupEtime" format="yyyy-MM-dd"/>">
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">备注：</span>
									</td>
								
									<td class="td1" align="left">
										<textarea  rows="6" cols="40" id="groupDesc" 
											name="model.groupDesc" style="overflow: auto;">${model.groupDesc}</textarea>	
									</td>
								</tr>		
							</table>
							<br>
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
													<input id="close" type="button" class="input_bg"
														value="关 闭" onclick="window.close();">
												</td>
											</tr>
										</table>
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
