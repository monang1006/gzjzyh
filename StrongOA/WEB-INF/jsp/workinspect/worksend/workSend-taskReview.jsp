<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<base target="_self">
		<title>任务评语</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
			<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
     <script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script language="javascript">
		function getDemoLen(demo){
			var demoLen=demo.length;
			var demoLen2=0;
		    for (var i=0;i<demoLen;i++) {
		        if ((demo.charCodeAt(i) < 0) || (demo.charCodeAt(i) > 255))
		            demoLen2 +=2;
		        else
		            demoLen2 += 1;
		    }
		    return demoLen2;
		}
		$(document).ready(function(){
				$("#save").click(function(){
					if($(":radio:checked").length==0){
						alert("请选择完成等级!");
						$(":radio").focus();
						return false;
					}
					$("#reviewsDemo").val($.trim($("#reviewsDemo").val()));
					//if($("#reviewsDemo").val()==""){
					//	alert("请填写评语内容!");
					//	$("#reviewsDemo").focus();
					//	return false;
					//}
					if(getDemoLen($("#reviewsDemo").val())>1000){
						alert("评语内容不能超过500个汉字!");
						$("#reviewsDemo").focus();
						return false;
					}
					$("#save").attr({'disabled':true});
					$("#myForm").submit();
				});
			});
	</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="20%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												任务评语
											</td>
											<td width="1%">
												&nbsp;
											</td>
											<td width="85%">
												
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<s:form id="myForm"
							action="/workinspect/worksend/workSend!saveTaskReview.action" theme="simple">
							<s:hidden id="worktaskId" name="model.worktaskId"></s:hidden>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">任务标题：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										${model.worktaskTitle}
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">完成等级(<font color="red">*</font>)：</span>
									</td>
									<td class="td1" colspan="3" align="left">
								<s:radio id="commpleteLevel" name="model.commpleteLevel" list="#{'0':'按时完成' , '1':'超期完成' }" listKey="key" listValue="value" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">评语内容：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<textarea id="reviewsDemo" name="model.reviewsDemo"
											rows="5" cols="68">${model.reviewsDemo}</textarea>
									</td>
								</tr>
							</table>
							<table id="annex" width="90%" height="10%" border="0"
								cellpadding="0" cellspacing="1" align="center" class="table1">
							</table>
						<table align="center" width="90%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td align="center" valign="middle">
									<table width="27%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="34%">
												<input id="save" type="button" class="input_bg"
													value="保 存">
											</td>
											<td width="33%">
												<input id="reset" type="reset" class="input_bg"
													value="重 写">
											</td>
											<td width="33%">
												<input id="c" type="button" class="input_bg"
													value="关闭" onclick="window.close();">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>