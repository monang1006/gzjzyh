<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>新建模板类别</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<SCRIPT type="text/javascript">
		function validatesubmit(){
			var form=document.getElementById("doctempTypeForm");
			return true;
		}
		function save(){
		  document.getElementById('doctempTypeForm').submit();
		
		}
		setPageListenerEnabled(true);
	</SCRIPT>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
				<tr>
	<td height="8px;"></td>
</tr>
					<td colspan="3" class="table_headtd">
					
						<form id="doctempTypeForm" theme="simple"
							action="<%=root%>/doctemplate/doctempType/docTempType!save.action" onSubmit="return validatesubmit();" method="POST">
										
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											
											<tr>
											
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>保存模板类别</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										                <tr>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
										                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
									                  		<td width="5"></td>
										                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
										                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
										                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
									                  		<td width="6"></td>
										                </tr>
										                <tr>
	<td height="8px;"></td>
</tr>
										            </table>
												</td>
												
											</tr>
										</table>
							<input id="docgroupId" name="model.docgroupId" type="hidden"
								size="32" value="${model.docgroupId}">
							<input id="docgroupParentId" name="model.docgroupParentId" type="hidden"
								size="32" value="${model.docgroupParentId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="0" align="center" class="table1">
<%--								<tr>--%>
<%--									<td width="30%" height="21" class="biao_bg1" align="right">--%>
<%--										<span class="wz">所属模板类别：</span>--%>
<%--									</td>--%>
<%--									<td class="td1" colspan="3" align="left">--%>
<%--										<s:if test="doctempTypeName==null">--%>
<%--											公文模板类别--%>
<%--										</s:if>--%>
<%--										<s:else>--%>
<%--											${doctempTypeName}--%>
<%--										</s:else>--%>
<%--										--%>
<%--									</td>--%>
<%--								</tr>--%>
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>模板类别名称：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="docgroupName" name="model.docgroupName" type="text"
											size="32" maxlength="25" value="${model.docgroupName}" class="required">
									</td>
								</tr>
								<s:if test="groupType!=null &&groupType.length()>0">
									<input id="docgroupType" name="model.docgroupType" type="hidden" size="32" value="${groupType}">
								</s:if>
								<s:else>
									<tr>
										<td width="30%" height="21" class="biao_bg1" align="right">
											<span class="wz"><FONT color="red">*</FONT>选择模板类型：&nbsp;</span>
										</td>
										<td class="td1" colspan="3" align="left">
												<s:select list="#{'0':'word控件','1':'编辑器'}" id="docgroupType" name="model.docgroupType">
												</s:select>											
										
										</td>
										
									</tr>
								</s:else>
								<%--<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">模板类型(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select list="#{'0':'work控件','1':'编辑器'}" id="docgroupType" name="model.docgroupType">
										</s:select>
									</td>
								</tr>
							--%>
							</table>

							<%--<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="50%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="33%">
													<input id="sb" type="submit" class="input_bg" value="保 存">
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>--%>
						</form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
