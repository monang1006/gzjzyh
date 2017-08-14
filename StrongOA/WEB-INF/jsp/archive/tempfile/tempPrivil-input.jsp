<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存栏目</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<SCRIPT type="text/javascript">
		function validatesubmit(){

			var form=document.getElementById("columnForm");
			var desc=document.getElementById('tempfileprivilDesc').value;
			$("#userId").val($("#orguserid").val());
			if($("input[name=model\.clumnIsprivate]:checked").val()=='0'){
				if($("#orgusername").val()==''){
					alert("请添加用户");
					return false;
				}
			}
			if(desc.length>200){
			   alert("描述请不要超过200字！");
			   return false;
			}
			//form.submit();
			columnForm.submit();
			
		}
		
		function cancel(){
			parent.location.reload();
		}
		
		
		$(document).ready(function(){
			
	        //选择人员
			$("#addPerson").click(function(){
				var ret=OpenWindow(this.url,"600","400",window);
			});
			
			//清空人员
			$("#clearPerson").click(function(){
				$("#orgusername").val("");
				$("#orguserid").val("");
			});
	        
      	}); 
      	
    
		setPageListenerEnabled(true);
		
		
	</SCRIPT>
	</head>
		<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="columnForm" theme="simple"
							action="<%=root%>/archive/tempfile/tempPrivil!save.action" method="POST">
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
														height="9" onclick="search()">&nbsp;
													设置权限
												</td>
												<td width="70%">
													<table border="0" align="right" cellpadding="0"
														cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td >
															<a class="Operation" href="javascript:validatesubmit();">
																<img src="<%=root%>/images/ico/baocun.gif" width="14"
																	height="14" class="img_s">
															
																保存&nbsp;</a>
															</td>															
															<td width="5">
															</td>
															<td >
															<a class="Operation" href="javascript:window.close();">
																<img src="<%=root%>/images/ico/guanbi.gif"
																	width="15" height="15" class="img_s">
															
																关闭&nbsp;</a>
															</td>
															
															<td width="5">
																&nbsp;
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="tempfileprivilId" name="model.tempfileprivilId" type="hidden"
								size="32" value="${model.tempfileprivilId}">	
							<input id="tempfileId" name="model.toaArchiveTempfile.tempfileId" type="hidden"
								size="32" value="${model.toaArchiveTempfile.tempfileId}">	
							<input type="hidden" name="userId" id="userId">		
							<input type="hidden" name="tempfileIds" value="${tempfileIds}">									
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">授权用户：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:textarea cols="30" id="orgusername" name="userName" rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="orguserid" value="<s:property value='userId'/>" />
											<a id="addPerson" url="<%=root%>/address/addressOrg!tree.action" href="#">添加</a>&nbsp;<a id="clearPerson" href="#">清空</a>
											
											
									</td>
								</tr>
								
							<!--  <tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">授权部门：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:textarea cols="30" id="orgusername1" name="tempfileprivilOrg" rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="orguserid1" name="model.tempfileprivilOrg" value="${model.tempfileprivilOrg }" />
											<a href="JavaScript:addPerson1();">添加部门</a>&nbsp;<a href="JavaScript:clearPerson1();">清空</a>
									</td>
								</tr>
								 <tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">授权小组：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										
										
									</td>
								</tr> -->
							<!--  	<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">类型：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="tempfileprivilType" width="100" name="model.tempfileprivilType" type="text" size="32" value="${model.tempfileprivilType}" maxlength="30">
									</td>
								</tr>-->
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">描述：</span>
									</td>
									<td class="td1" colspan="30" align="left">
									<s:textarea cols="30" id="tempfileprivilDesc" name="model.tempfileprivilDesc" rows="4" ></s:textarea>
									<br><font color="red">请不要超过200字</font>
									</td>
								</tr>
								
								
							
							</table>
							<table id="annex" width="90%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
							<br>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input name="Submit" type="button" class="input_bg" value="保 存" onclick="validatesubmit();">
												</td>
												<td width="37%">
													<input name="Submit2" type="button" class="input_bg" value="关 闭" onclick="javascript:javascript:window.close();">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
