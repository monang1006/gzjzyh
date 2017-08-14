<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<title>档案销毁审核</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<base target="_self">
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		
	</head>
	<%
	String destroyId = request.getParameter("destroyId");
	%>
	<body class=contentbodymargin oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form action="/archive/archiveDestr/archiveDestr!auditRecord.action" theme="simple" id="myTableForm">
				<s:hidden id="destroyId" name="destroyId"></s:hidden>
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%"  border="0" cellspacing="0" cellpadding="0">
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
													档案销毁审核
												</td>
												<td width="70%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否通过(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1"  align="left">
										<input type="radio" name="isPass" value="1">
										通过
										<input type="radio" name="isPass" value="0">
										不通过
									</td>
								</tr>
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
							
										<span class="wz">审核时间(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1"  align="left">
										<strong:newdate id="auditTime" name="auditTime" dateform="yyyy-MM-dd HH:mm:ss" width="175" isicon="true"/>
									</td>
								</tr>
							
								<tr>
									<td height="21" class="biao_bg1"  align="right">
										<span class="wz">销毁原因：</span>
									</td>
									<td class="td1" align="left" colspan="2">
									<textarea  id="testearea" rows="5" maxlength="400" readonly="readonly"  class="required"  cols="40" name="model.destroyApplyDesc">${model.destroyApplyDesc}</textarea>
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1"  align="right">
										<span class="wz">审核意见：</span>
									</td>
									<td class="td1" align="left" colspan="2">
									<textarea  id="auditDesc" rows="5"   cols="40" name="auditDesc"></textarea><br>
										<font color="red">请不要超过200字</font>
								</tr>
							<%-- 	<tr>
									
									
									<script type="text/javascript"
											src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js"></script>
										<script type="text/javascript">
													 
													var oFCKeditor = new FCKeditor( 'auditDesc' );
													oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
													oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
													oFCKeditor.Width = '100%' ;
                                                    oFCKeditor.Height = '250' ;
													
													oFCKeditor.Value	= '\n          \t\t' ;
													oFCKeditor.Create() ;
													 
                                                    </script>
									</td>
								</tr>--%>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
							
								<td width="29%">
										<input name="save" type="button" class="input_bg"
											value="确 定" onclick="queding()">
								</td>
								<td width="37%">
									<input name="Submit2" type="submit" class="input_bg"
										value="取 消" onclick="window.close();">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<SCRIPT type="text/javascript">
			var obj=document.getElementById("destroyId");
			obj.value="<%=destroyId%>";
			
			function queding(){
				var k=0;
				var obj=document.getElementsByName("isPass");
				for(var i=0;i<obj.length;i++){
					if(obj[i].checked==true){
						k++;
					}
				}
				if(k==0){
					alert("请选择是否通过审核!") ;
					return false;
				}
				
				var auditTime=document.getElementById("auditTime").value;
				if(auditTime==null||auditTime==""||auditTime=="null"){
					alert("请填写销毁审核时间");
					return false;
				}
				var desc=document.getElementById('auditDesc').value;
				if(desc.length>=200){
				    alert("审核意见请不要超过200字！");
				    return false;
				}
				
				
				document.getElementById("myTableForm").submit();				
			}
			
		</SCRIPT>
	</body>
</html>
