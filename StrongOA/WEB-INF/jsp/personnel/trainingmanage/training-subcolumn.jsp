<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存栏目</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css"
			type=text/css rel=stylesheet>
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
			var columnname=$("#clumnName").val();
			columnname=columnname.replace(/(^\s*)|(\s*$)/g,"");
		    if(columnname==null||columnname=="")
			 {
			    alert("栏目名称不可以为空！");
			    return  ;
			 }
			 if(columnname=="<\%%>"){
			 alert("栏目名称不可以输入“<\%%>”！");
			    return  ;
			 }
		
			form.submit();
		}
		function cancel(){
			parent.location.reload();
		}
		//新建子栏目
		function newSubColumn(){
			var folderId = '${clumnId}';
			parent.project_work_content.document.location="<%=root%>/personnel/trainingmanage/training!newSubColumn.action?clumnId="+folderId;
		}
		//新建顶级栏目
		function newColumn(){
			parent.project_work_content.document.location="<%=root%>/personnel/trainingmanage/training!newSubColumn.action";
		}
		
	
	</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="columnForm" theme="simple"
							action="<%=root%>/personnel/trainingmanage/training!saveCloumn.action" method="POST">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>&nbsp;</td>
												<td width="34%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9" onclick="search()">&nbsp;
													<s:if test="null==clumnViewName">
														新建顶级栏目 
													</s:if>
													<s:else>
														<s:text name="clumnViewName"/>栏目信息
													</s:else>
												</td>
												<td>
													&nbsp;
												</td>
												<td width="61%">
													<table border="0" align="right" cellpadding="0"
														cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td width="50">
															<a class="Operation" href="javascript:validatesubmit();">
																<img src="<%=root%>/images/ico/baocun.gif" width="14"
																	height="14" class="img_s">
															
																保存</a>
															</td>															
															<td width="5">
															</td>
															<td width="100">
															<a class="Operation" href="javascript:newColumn();">
																<img src="<%=path%>/oa/image/prsnfldr/newfolder.gif"
																	width="15" height="15" class="img_s">
															
																新建顶级栏目</a>
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
						<input id="clumnParent" name="column.clumnParent" type="hidden"
								size="32" value="${clumnId}">	
																			
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">所属栏目：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:if test="null==clumnViewName">
											顶级栏目
										</s:if>
										<s:else>
											<s:text name="clumnViewName"></s:text>
										</s:else>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目名称(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnName" name="column.clumnName" type="text" size="32" value="${column.clumnName}" class="required" maxlength="20"> 
									</td>
								</tr>
							
								
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">栏目说明：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="clumnMemo" name="column.clumnMemo" type="text" size="32" value="${column.clumnMemo}" maxlength="30">
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
													<input name="Submit" type="button" class="input_bg" onclick="validatesubmit()" value="保 存" >
												</td>
												<td width="37%">
													<input name="Submit2" type="button" class="input_bg" value="返 回" onclick="cancel();">
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
