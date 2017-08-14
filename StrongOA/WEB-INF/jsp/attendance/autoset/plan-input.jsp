<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<title>增加文件</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/jquery.validate.js"></script>
		<script type="text/javascript"
			src="<%=jsroot%>/validate/formvalidate.js"></script>
		<script type="text/javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/upload/jquery.blockUI.js"></script>
		<style type="text/css">
.number {
	font-size: 3;
	font-family: 宋体;
}
</style>
		<script type="text/javascript">
			function onSub(){
				var planStime=$("#planStime").val();
				if($("#planStime").val()!=""&&$("#planEtime").val()!=""){
				    if($("#planStime").val()>$("#planEtime").val()){
					    alert("开始时间不能晚于结束时间！");
					    return ;
				    }
				}
			    if($("#planTime").val()==""){
			        alert("请设置执行时间点！");
			        return;
			    }
			    if($("#planStime").val()==""){
			    	alert("开始时间不可以为空！");
			    	return ;
			    }
			    if($("#intervalTime").val()>5){
			    	alert("汇总时间延后时间不能超过5天！");
			    	return ;
			    }
			    var time=planStime.substring(8,10);
			    if(time>28){
			    	alert("由于最少月份只有28天，为了方便统计，请输入日小于29的时间！")
			    	return;
			    }
			    document.getElementById("mytable").submit();
			}
			
			function isAutoGather(value){
				if(value=="0"){
					document.getElementById("gather").style.display="none";
				}else{
					document.getElementById("gather").style.display="";
				}
			}
			function init(){
				var planFrequency="${model.planFrequency}";
				if(planFrequency=="1"){
					document.getElementById("gather").style.display="";
				}
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="" onload="init()">
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="mytable" action="/attendance/autoset/plan!save.action"
							enctype="multipart/form-data" theme="simple">
							<s:hidden id="planId" name="model.planId"></s:hidden>
							<s:hidden id="planLtime" name="model.planLtime"></s:hidden>
							<div align=left style="width: 100%">
								<table width="100%" border="0" cellspacing="0"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);"
									cellpadding="0">
									<tr>
										<td height="60">
											<table width="100%" border="0" cellspacing="0"
											style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);"
											cellpadding="00">
												<tr>
													<td>
												&nbsp;
													</td>
													<td width="30%">
														<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
															height="9">&nbsp;
														执行计划
													</td>
													<td width="70%">

													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
							<table width="60%" border="0" cellpadding="0" cellspacing="1"
								align="center">
								<tr>
									<td colspan="3" align="center">
										<fieldset>
											<legend>
												考勤计划频度
											</legend>
											<div style="padding: 10px 10px 10px 10px; text-align: left;">
												<s:radio id="planFrequency" name="model.planFrequency"
													list="#{'0':'不执行','1':'每月执行一次'}" onselect="0" onclick="isAutoGather(this.value)"/>
											</div>
										</fieldset>
										<br>
										<div id="gather" style="display: none">
										<fieldset>
											<legend>
												是否自动汇总考勤
											</legend>
											<div style="padding: 10px 10px 10px 10px; text-align: left;">
												<s:radio id="isAuto" name="model.isAuto"
													list="#{'0':'否','1':'是'}" onselect="0" />
												&nbsp;&nbsp;&nbsp;&nbsp;
												<span>自动计算后第</span>
															<input type="text" size="3" id="intervalTime"
																name="model.intervalTime" value="${model.intervalTime }">
															<span>天自动汇总考勤</span>
											</div>
										</fieldset>
										</div>
										<br>
										<fieldset>
											<legend>
												参数配置
											</legend>
											<div style="padding: 10px 10px 10px 10px; text-align: left;">
												<table>
													<tr>
														<td nowrap align="right">
															<span>开始时间(<FONT color="red">*</FONT>):</span>
														</td>
														<td nowrap>
															<strong:newdate id="planStime" name="model.planStime"
																dateform="yyyy-MM-dd" isicon="true"
																dateobj="${model.planStime}" />
														</td>
													</tr>
													<tr>
														<td nowrap align="right">
															<span>结束时间:</span>
														</td>
														<td nowrap>
															<strong:newdate id="planEtime" name="model.planEtime"
																dateform="yyyy-MM-dd" isicon="true"
																dateobj="${model.planEtime}" />
														</td>
													</tr>
													<tr>
														<td nowrap align="right">
															<span>执行时间点(<FONT color="red">*</FONT>)：</span>
														</td>
														<td nowrap>
															<strong:newdate id="planTime" name="model.planTime"
																dateform="HH:mm" isicon="true"
																nowvalue="${model.planTime}" />
														</td>
													</tr>
												</table>

											</div>
										</fieldset>
									</td>
								</tr>

							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%" align="center">
													<br>
													<input name="save" type="button" class="input_bg"
														value="保 存" onclick="onSub();">
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
