<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>视图录入信息</title>
		<!--  引用公共样式文件-->
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<!--  引用公共及自定义js文件-->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/oa/js/infotable/selectbasedata_infoinput.js" type="text/javascript" language="javascript"></script>
		<SCRIPT src="<%=path%>/oa/js/infotable/choosetext.js" type="text/javascript" language="javascript"></SCRIPT>
	</head>
	<base target="_self" />
	<body class=contentbodymargin  onkeydown='if (event.keyCode==83 && event.ctrlKey) submitValidateForm()'>
		<DIV id=contentborder align=center>
			<s:form action="/infotable/infoTable!save.action" name="comform" id="comform">
				<input type="hidden" name="propertyList">
				<input type="hidden" name="pkey" value="${pkey}">
				<input type="hidden" name="struct" value="${struct}">
				<input type="hidden" id="keyid" value="${keyid}">
				<DIV style="display: none">
					<SELECT name="checkvalue">
					</SELECT>
					<SELECT name="textvalue">
					</SELECT>
				</DIV>
				<center>
					<table border="0" width="100%" bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
						<tr>
									<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="50%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
												<s:if test="keyid==null">
													增加信息
												</s:if>
												<s:else>
													编辑信息
												</s:else>
												</td>
												<td width="10%">
													&nbsp;
												</td>
												<td width="35%">
												</td>
											</tr>
										</table>
									</td>
								</tr>
						<tr>
							<td width="100%">
								<table width="100%" id="testtb">
									<s:if test="proList==null">
									<tr><td>
										<s:iterator value="datalist" id="pro">
											<s:if test="infoItemField==pkey">
												<script language="JavaScript">
													flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="infoItemValue"/>","<s:property value="properTypeName"/>");																										
												</script>
											</s:if>
											<s:elseif test="fid!=null && infoItemField==fpro">
												<script language="JavaScript">
													flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","${fid}","<s:property value="properTypeName"/>");																										
												</script>
											</s:elseif>
											<s:elseif test="funcPro!=null && infoItemField==funcPro">
												<script language="JavaScript">
													flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","2","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="funcid"/>,<s:property value="funcname"/>","<s:property value="properTypeName"/>");																										
												</script>
											</s:elseif>
											<s:elseif test="otherPro!=null && infoItemField==otherPro">
												<script language="JavaScript">
													flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","2","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="othername"/>","<s:property value="properTypeName"/>");																										
												</script>
											</s:elseif>
											<s:else>
												<script language="JavaScript">
													prostr+="<s:property value="infoItemField"/>"+",";
													flag=chooseText(flag,"<s:property value="infoItemDatatype"/>","<s:property value="infoItemProset"/>","<s:property value="infoItemField"/>","<s:property value="infoItemDictCode"/>","<s:property value="infoItemFlag"/>","<s:property value="infoItemSeconddisplay"/>","<s:property value="infoItemLength"/>","<s:property value="infoItemDecimal"/>","<s:property value="infoItemValue"/>","<s:property value="properTypeName"/>");																					
												</script>
											</s:else>
										</s:iterator>
										</td></tr>
									</s:if>
									<s:else>
										<tr>
											<td colspan="4">
												没有结构
											</td>
										</tr>
									</s:else>
									<!--基本信息结束-->
								</table>
								<script language="JavaScript">
										if(prostr!="")	
											document.all.propertyList.value=prostr;
									</script>
							</td>
						</tr>
					</table>
					<br>
					<table border="0" width="95%" bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
						<tr>
							<td align="center" colspan="4">
								<INPUT type="submit" class="anniu" value="保存">
							</td>
						</tr>
					</table>
				</center>
			</s:form>
		</DIV>
	</body>
</html>
