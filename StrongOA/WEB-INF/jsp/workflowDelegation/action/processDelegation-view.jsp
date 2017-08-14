<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看委派设置</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" type="text/javascript">
		function init(){
				var deIsStart;
				if('${model.deIsStart}'==null){
					deIsStart = '0';
				}else{
					deIsStart = '${model.deIsStart}';
				}
				document.getElementById("deIsStart").value = deIsStart;
				if(deIsStart == '0'){
					$("#no").attr("checked",true);
				}else{
					$("#yes").attr("checked",true);
				}
				var deRest1;
				if('${model.deRest1}'==null){
					deRest1 = '0';
				}else{
					deRest1 = '${model.deRest1}';
				}
				if(deRest1 == '0'){
					$("#deStartDateTR").show();
					$("#deEndDateTR").show();
				}
			}
		function cancel(){
			window.close();	
		}						
			
</script>
	</head>
	<body class="contentbodymargin" onload="init()">
		<script type="text/javascript"
			src="<%=root%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" cellpadding="0">
			<form name="delegationForm"
				action="<%=root%>/workflowDelegation/action/processDelegation!save.action"
				method="post">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
									<strong>查看委派设置</strong>
									</td>
									<td align="right">
										<table border="0" align="right" cellpadding="00" cellspacing="0">
											<tr>
						                  		<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
							                 	<td class="Operation_input1" onclick="cancel();">&nbsp;关&nbsp;闭&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
						                  		<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<center>
					<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
							<tr>
								<td class="biao_bg1" align="right">
									<span class="wz">当前用户：</span>
								</td>
								<td class="td1">
									<span class="wz"> ${userInfo.userName}</span>
								</td>
							</tr>
							<tr>
								<td align="right" class="biao_bg1" >
									登录帐号：
								</td>
								<td class="td1">
									<span class="wz"> ${userInfo.userLonginName}</span>
								</td>
							</tr>
						<tr>
							<td align="right" class="biao_bg1" >
								委派人：
							</td>
							<td class="td1">
								<span class="wz">${model.dhDeleRealname}</span>
							</td>
						</tr>
						<tr > 
							<td align="right" class="biao_bg1" >
								委派事项：
							</td>
							<td class="td1" >
								<span class="wz">
<%--								<select id="selectProcess" size="3" style="width: 200"  >--%>
									<c:forEach items="${model.toaInfoDeledetails}" var="detail" >
<%--										<option onfocus="blur();">--%>
											<c:out value="${detail.ddProName},"/>
											
<%--										</option>--%>
									</c:forEach></span>
<%--								</select>--%>
							</td>
						</tr>
						<tr id="deStartDateTR" style="display: none;">
							<td align="right" class="biao_bg1">
								开始时间：
							</td>
							<td class="td1">
								<span class="wz">
								<s:date name="model.deStartDate" format="yyyy年MM月dd日 HH点mm分"/></span>
								
							</td>
						</tr>
						<tr id="deEndDateTR"  style="display: none;">
							<td align="right" class="biao_bg1">
								结束时间：
							</td>
							<td class="td1">
							<span class="wz">
								<s:date name="model.deEndDate" format="yyyy年MM月dd日 HH点mm分"/></span>
								
							</td>
						</tr>
						<tr>
							<td align="right" class="biao_bg1" >
								过期设置：
							</td>
							<td class="td1">
							 <s:if test="model.deRest1==1">
							 <span class="wz">永不过期</span>
							 </s:if>
							 <s:else>
							  <span class="wz">允许过期</span>
							 </s:else>
							</td>
						</tr>
						<tr>
							<td align="right" class="biao_bg1" >
								委派设置： 
							</td>
							<td class="td1">
								<!--<span class="wz">
								<c:choose>
									<c:when test="${model.deIsDoingDele == '1'}">
										<input type="checkbox" name="deIsDoingDele" value="1"
											checked disabled>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="deIsDoingDele" value="1"
											disabled>
									</c:otherwise>
								</c:choose></span>
								委派已办任务
								<br />
								<span class="wz">
								<c:choose>
									<c:when test="${model.deIsDoingRece == '1'}">
										<input type="checkbox" name="deIsDoingRece" value="1"
											checked disabled>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="deIsDoingRece" value="1"
											disabled>
									</c:otherwise>
								</c:choose></span>
								收回已办任务
								<br />
							--><span class="wz">
								<c:choose>
									<c:when test="${model.deIsEndRece == '1'}">
										<input type="checkbox" name="deIsEndRece" value="1" checked
											disabled>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="deIsEndRece" value="1"
											disabled>
									</c:otherwise>
								</c:choose></span>
									<!--收回办结任务-->
									收回已办文件
										<font color = '#999999'>委派周期结束之后，处理完的文件回到设置人的文件列表</font>	
							</td>
						</tr>
						<tr>
							<td align="right" class="biao_bg1">
								委派事由：
							</td>
							<td class="td1" style="word-break:break-all;line-height: 1.4">
								&nbsp;<span class="wz">${model.dhDeleReason}</span>
								
							</td>
						</tr>
						<tr>
							<td align="right" class="biao_bg1">
								立即生效：
							</td>
							<td class="td1">
								<script type="text/javascript">
									function ljsx(obj){//立即生效设置
										$("#deIsStart").val(obj.value);
									}
								</script>
							
								<input id="no" type="radio" name="radio" value="0" onclick="ljsx(this)"  disabled="disabled"/><label for="no">否</label>
								<input id="yes" type="radio" name="radio" value="1" onclick="ljsx(this)" disabled="disabled"/><label for="yes">是</label>
								<input type="hidden" name="model.deIsStart" id="deIsStart" value="">
							</td>
						</tr>
					</table>
				</center>
			</form>
		</div>
	</body>
</html>
