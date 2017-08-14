<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>保存外部系统链接</title>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	    <LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
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
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript">
			function submitForm(){
			    var systemName=document.getElementById("systemName").value;
			    var linkUrl=document.getElementById("linkUrl").value;
				var systemDesc=document.getElementById("systemDesc").value;
				var msg="";
				if($.trim(systemName)==""){
					if(msg==""){
					  document.getElementById("systemName").focus();
					}
					msg="系统名称不能为空。\n";
					
				}
				
				
				if($.trim(linkUrl)==""){
					
					//alert("系统链接不能为空");
					if(msg==""){
					 document.getElementById("linkUrl").focus();
					}
					msg+="系统链接不能为空。\n";
					//return false;
				}
				if($.trim(systemDesc).length>2000){
					
					if(msg==""){
						document.getElementById("systemDesc").focus();
					}
					msg+="系统描述不能超过2000个字。";
					//return false;
				}
				if(msg!=""){
					alert(msg);
					return false;
				}
				document.getElementById("myTable").submit();
			}
		</script>
		
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/systemlink/sysLink!save.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							    <s:if test="model.linkId!=null">
										<strong>编辑外部系统链接</strong>
									</s:if>
									<s:else>
										<strong>新建外部系统链接</strong>
									</s:else>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
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
						<input id="linkId" name="linkId" type="hidden" size="32"
								value="${model.linkId}">
							<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;系统名称：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="systemName" name="systemName" class="required" maxlength=25 type="text" size="50" value="${model.systemName}">
									</td>
								</tr>
								<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;系统链接：</span>
									</td>
									<td class="td1" colspan="3" align="left">
											<input id="linkUrl" name="linkUrl" class="required" type="text" size="50" maxlength=100  value="${model.linkUrl}">
									</td>
								</tr>
								<tr>
									<td height="21" class="biao_bg1" align="right" valign="top">
										<span class="wz">系统描述：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea rows="9" cols="38" id="systemDesc"
											name="systemDesc"  style="overflow: auto;">${model.systemDesc}</textarea><font id="boldtext">(请不要超过2000字)</font>
										<%--							<input id="filtrateRaplace" name="filtrateRaplace" type="text" size="30" value="${modle.filtrateRaplace}" class="required">--%>
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
