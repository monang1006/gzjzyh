<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>增加机构</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript"
			src="<%=root%>/common/js/jquery/jquery-1.2.6.js"></script>

		<script language="javascript">

	  //保存
    function onSub(){
    var orgSyscode=$("#orgSyscode").val();
    var orgName=$("#orgName").val();
    if(orgSyscode==""){
    alert("机构编码不可以为空！");
    return false;
    }else if(orgName==""){
    alert("机构名称不可以为空！");
    return false;
    }
    $("#veteranform").attr("action","<%=path%>/personnel/personorg/personOrg!merge.action");
    veteranform.submit();
    }
	</script>
	</head>
		<base target="_self"/>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form action="" id="veteranform" name="veteranform" method="post"
				enctype="multipart/form-data">
				<input type="hidden" id="orgId" name="orgId" value="${model.orgid}">
				<input type="hidden" id="userOrgid" name="userOrgid" value="${model.userOrgid}">
				<input type="hidden" id="userOrgcode" name="userOrgcode" value="${model.userOrgcode}">
				<input type="hidden" id="iscode" name="iscode"
					value="${model.orgSyscode}">
					<input type="hidden" id="moveOrgId" name="moveOrgId" value="${moveOrgId }">
				<table width="100%" border="0" cellspacing="0" cellpadding="00"
					style="FILTER: progid :DXImageTransform.Microsoft.Gradient(gradientType=0, startColorStr=#ededed, endColorStr=#ffffff);">
					<br>
					<tr>
						<td>&nbsp;</td>
						<td width="20%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
							合并后机构信息
						</td>
						<td width="1%">
							&nbsp;
						</td>
						<td width="79%">
							<table width="100%" border="0" align="right" cellpadding="0"
								cellspacing="0">
								<tr>
									<td width="100%" align="right">
										<table width="100%" border="0" align="right" cellpadding="0"
											cellspacing="0">
											<tr>
												<td width="*">
													&nbsp;
												</td>
												<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td height="10">
						</td>
					</tr>
				</table>

				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">机构编号(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgSyscode" name="model.orgSyscode" type="text"
								onkeydown="return false;" size="22" value="${model.orgSyscode }">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">是否删除：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select name="model.orgIsdel" list="#{'0':'未删除','1':'已删除'}"
								id="orgIsdel" listKey="key" listValue="value" style="width:11em" />
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">机构名称(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgName" name="model.orgName" type="text" size="22"
								value="${model.orgName}">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">机构代码：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgCode" name="model.orgCode" type="text" size="22"
								value="${model.orgCode }">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">机构性质：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<select id="orgNature" name="model.orgNature">
								<s:iterator id="vo" value="dictlist">
									<option value="${vo.dictItemCode }">
										${vo.dictItemName }
									</option>
								</s:iterator>
							</select>
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">电话：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgTel" name="model.orgTel" type="text" size="22"
								value="${model.orgTel }">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">传真：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgFax" name="model.orgFax" type="text" size="22"
								value="${model.orgFax }">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">负责人：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgManager" name="model.orgManager" type="text"
								size="22" value="${model.orgManager }">
						</td>
					</tr>

					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">地址：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="orgAddr" name="model.orgAddr" type="text" size="60"
								value="${model.orgAddr }">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">机构描述：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<textarea id="orgDescription" name="model.orgDescription"
								rows="4" cols="38">${model.orgDescription }</textarea>
						</td>
					</tr>
				</table>
				<table id="annex" width="90%" height="10%" border="0"
					cellpadding="0" cellspacing="1" align="center" class="table1">
				</table>
				<table width="90%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td align="center" valign="middle">
							<table width="27%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="30%">
										<input name="Submit2" type="button" class="input_bg"
											value="保 存" onclick="onSub();">
									</td>
									<td width="30%">
										<input name="Submit2" onclick="window.close();" type="button" class="input_bg"
											value="取 消">
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
