<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>
			<s:if test="groupId!=null&&groupId!=\"\"">
				编辑班组
			</s:if>
			<s:else>
				新增班组
			</s:else>
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
			    var groupEtime=document.getElementById("groupEtime").value;
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
				    if(groupEtime!=""&&groupStime>groupEtime){
				    	alert("失效时间必须晚于有效时间！");
				    	return;
				    }
					document.getElementById("myTable").submit();	   		
				}
			}	
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/attendance/arrange/scheGroup!save.action" >
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
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													<s:if test="groupId!=null&&groupId!=\"\"">
														编辑班组
													</s:if>
													<s:else>
														新增班组
													</s:else>
												</td>
												<td width="70%">
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
										<span class="wz">班组名称(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="groupName" name="model.groupName" maxlength="25" type="text"
											size="29" value="${model.groupName}" >
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">班组类型(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<s:if test="groupId!=null&&groupId!=\"\"">	
													
											<s:if test="logo==\"1\"">
												<input type="text" value="倒班" readonly="readonly" size="29">
											</s:if>
											<s:else>
												<input type="text" value="行政办" readonly="readonly" size="29">	
											</s:else>
											<input type="hidden" id="logo" name="model.logo" value="${model.logo}">
										</s:if>
										<s:else>
											<s:select cssStyle="width: 170px" 
												list="#{'行政班':'0','倒班':'1'}"
												id="logo" name="model.logo"
												listKey="value" listValue="key" >
											</s:select>
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">有效时间(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<strong:newdate id="groupStime"  name="model.groupStime"
											dateform="yyyy-MM-dd" width="170"
											dateobj="${model.groupStime}" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">失效时间：</span>
									</td>
									<td class="td1" align="left">
										<strong:newdate id="groupEtime" name="model.groupEtime"
											dateform="yyyy-MM-dd" width="170"
											dateobj="${model.groupEtime}" />
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
												<td width="29%">
													<input id="save" type="button" class="input_bg" value="保 存" onclick="submitForm()">
												</td>
												<td width="37%">
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
