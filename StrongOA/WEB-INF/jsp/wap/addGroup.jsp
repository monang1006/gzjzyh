<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>新建联系组</title>
		<link rel="stylesheet" type="text/css"
			href="<%=root%>/oa/css/wap/style.css"></link>
		<link rel="stylesheet" type="text/css"
			href="<%=root%>/oa/css/wap/table.css"></link>
	</head>
	<%
	String groupId = request.getParameter("groupId");
	%>
	<body>
	<jsp:include flush="true" page="/oa/include/navigation.jsp"></jsp:include>
	<font color="red">${message}</font>
		<div class="gridlist">
			<div class="sec">
				<table style="width: 100%">
					<tr>
						<td align="left">
							<strong>新建联系组</strong>
						</td>
						<td align="right">
							<form
								action="<%=root%>/wap/addressGroup!getAddressList.action" method="post">
								<input type="hidden" id="groupId" name="groupId"
									value="<%=groupId%>" />
								<input name="Submit1" type="submit" class="button" value="返回" />
							</form>
						</td>
					</tr>
				</table>
			</div>
			<form action="<%=root %>/wap/addressGroup!wapSave.action" method="post">
			<input type="hidden" id="groupId" name="groupId"
									value="<%=groupId%>" />
			<table border="0" width="98%" bordercolor="#FFFFFF" cellspacing="0"
			cellpadding="0">
				<tr>
					<td width="100%">
						<table border="1" cellspacing="0" width="100%"
							bordercolordark="#FFFFFF" bordercolorlight="#000000"
							bordercolor="#333300" cellpadding="2">
							<tr>
								<td width="30%" align="right" height="20">
									名称(<font color="red">*</font>)：
								</td>
								<td width="70%">
										<input type="text" id="groupName" name="model.addrGroupName" value="${model.addrGroupName}" maxlength="12" style="width:95%"/>
								</td>
							</tr>
							<tr>
								<td align="right" class="titleTD" height="20">
									描述：
								</td>
								<td>
									<textarea id="groupDesc" name="model.addrGroupRemark" rows="5" style="width:95%">${model.addrGroupRemark }</textarea>
								<br/><font color="gray" size="2">*描述不超过256个字符</font>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div  align="center">
				<input type="submit" value="保存" class="button"/>
			</div>
			</form>
		</div>
		<jsp:include flush="true" page="/oa/include/person-bottom.jsp"></jsp:include>
	</body>
</html>
