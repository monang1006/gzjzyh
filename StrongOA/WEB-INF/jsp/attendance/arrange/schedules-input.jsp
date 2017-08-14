<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title><s:if test="schedulesId!=null&&schedulesId!=\"\"">
				编辑班次
			</s:if> <s:else>
				新增班次
			</s:else></title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/properties_windows.css">
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
		<SCRIPT type="text/javascript">	
			function submitForm(){
			    var schedulesName=document.getElementById("schedulesName").value;		//班次名称
			    var workStime=document.getElementById("workStime").value;				//上班时间
			    var registerStimeOn=document.getElementById("registerStimeOn").value;	//上班登记有效时间开始时间
			    var registerEtimeOn=document.getElementById("registerEtimeOn").value;	//上班登记有效时间结束时间
			    var workEtime=document.getElementById("workEtime").value;				//下班时间
			    var registerStimeOff=document.getElementById("registerStimeOff").value;	//下班登记有效时间开始时间
			    var registerEtimeOff=document.getElementById("registerEtimeOff").value;	//下班登记有效时间结束时间
			    var laterTime=document.getElementById("laterTime").value;			   	//迟到时间
				var earlyTime=document.getElementById("earlyTime").value;				//早退时间
				var skipTime=document.getElementById("skipTime").value;					//旷工时间
				var schedulesType="0";
				var schedulesOrder="空"
				if(document.getElementById("schedulesOrder")!=undefined){
					schedulesOrder=document.getElementById("schedulesOrder").value;
				}
				if(document.getElementById("schedulesType")!=undefined){
					schedulesType=document.getElementById("schedulesType").value;
				}
				if(schedulesName==""){
					alert("班次名称不能为空！");
					return;
				}else if(schedulesType=="0"&&workStime==""){
					alert("上班时间不能为空！");
					return;
				}else if(schedulesType=="0"&&workEtime==""){
					alert("下班时间不能为空！");
					return;
				}else if(skipTime==""){
					alert("旷工时间不能为空！");
					return;
				}/*else if(""==registerStimeOn||""==registerEtimeOn){
					alert("上班有效时间段不能为空！");
					return;
				}else if(""==registerStimeOff||""==registerEtimeOff){
					alert("下班有效时间段不能为空！");
					return;
				}*/else if(schedulesOrder!="空"&&schedulesOrder==""){
					alert("班次所在班组的排序号不能为空！");
					return;
				}else{	
					if(document.getElementById("jumpDay").checked==false){//如果没有选择次日，则上班时间必须小于下班时间
						workStime=workStime.replace(":","");
						workEtime=workEtime.replace(":","");
						if(schedulesType=="0"&&workStime>=workEtime){
							alert("下班时间应晚于上班时间！");
							document.getElementById("workStime").focus();
							return;
						}
					}else{
						document.getElementById("jumpDay").value="1";
					}　
					var pattern =/^\d+$/;
					if(!pattern.test(laterTime)){
						alert("迟到时间应为整数，请重新输入！");
						document.getElementById("laterTime").focus();
						return;
					}
					if(!pattern.test(laterTime)){
						alert("早退时间应为整数，请重新输入！");
						document.getElementById("laterTime").focus();
						return;
					}
					if(!pattern.test(skipTime)){
						alert("旷工时间应为整数，请重新输入！");
						document.getElementById("skipTime").focus();
						return;
					}
					if(schedulesOrder!="空"&&!pattern.test(schedulesOrder)){
						alert("班次所在班组序号应为整数，请重新输入！");
						document.getElementById("schedulesOrder").focus();
						return;
					}
					document.getElementById("myTable").submit();	   		
				}
			}
			//时分正则表达式
			function isHourMinute(time){
				var a=time.match(/^(\d\d?)\:(\d\d?)$/);
				return a!=null&&a[1]<24&&a[2]<60;
			}
			
			function init(){
				var isjump=document.getElementById("jumpDay").value;
				if(isjump=="1"){
					document.getElementById("jumpDay").checked=true;
				}
			}	
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;" onload="init()">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/attendance/arrange/schedules!save.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
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
														height="9">&nbsp;
													<s:if test="schedulesId!=null&&schedulesId!=\"\"">
														编辑班次
													</s:if>
													<s:else>
														新增班次
													</s:else>
												</td>
												<td width="70%">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="groupId" name="groupId" type="hidden" size="32"
								value="${groupId}">
							<input id="schedulesId" name="schedulesId" type="hidden" size="32"
								value="${schedulesId}">
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">班次名称(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="schedulesName" name="model.schedulesName"
											maxlength="25" type="text" size="49"
											value="${model.schedulesName}">
										<input id="isrest" name="model.isrest" maxlength="25"
											type="hidden" size="20" value="0">
									</td>
								</tr>
								<s:if test="scheGroup.logo!=null&&scheGroup.logo==\"1\"">
								<tr>
									<td width="30%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否是休息班(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<s:select cssStyle="width: 125px" 
											list="#{'否':'0','是':'1'}"
											id="schedulesType" name="model.schedulesType"
											listKey="value" listValue="key" >
										</s:select>
									</td>
								</tr>
								</s:if>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">上班时间(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										  <strong:newdate id="workStime"  name="model.workStime"
											dateform="HH:mm" width="125"
											nowvalue="${model.workStime}" />
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">上班登记有效时间段：</span>
									</td>
									<td class="td1" align="left">
									    <strong:newdate id="registerStimeOn"  name="model.registerStimeOn"
											dateform="HH:mm" width="125"
											nowvalue="${model.registerStimeOn}" />
										至
										<strong:newdate id="registerEtimeOn"  name="model.registerEtimeOn"
											dateform="HH:mm" width="125"
											nowvalue="${model.registerEtimeOn}" />
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">下班时间(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<strong:newdate id="workEtime"  name="model.workEtime"
											dateform="HH:mm" width="125"
											nowvalue="${model.workEtime}" />
								        <input id="jumpDay" name="model.jumpDay" type="checkbox" value="${model.jumpDay}">次日
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">下班登记有效时间段：</span>
									</td>
									<td class="td1" align="left">
									    <strong:newdate id="registerStimeOff"  name="model.registerStimeOff"
											dateform="HH:mm" width="125"
											nowvalue="${model.registerStimeOff}" />
										至
										<strong:newdate id="registerEtimeOff"  name="model.registerEtimeOff"
											dateform="HH:mm" width="125"
											nowvalue="${model.registerEtimeOff}" />	
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">迟到（分）：</span>
									</td>
									<td class="td1" align="left">
										<input id="laterTime" name="model.laterTime" maxlength="25"
											type="text" size="20" value="${model.laterTime}">
										 <font color="#909090">输入整数</font>			
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">早退（分）：</span>
									</td>
									<td class="td1" align="left">
										<input id="earlyTime" name="model.earlyTime" maxlength="25"
											type="text" size="20" value="${model.earlyTime}">
									    <font color="#909090">输入整数</font>			
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">旷工（分）(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="skipTime" name="model.skipTime" maxlength="25"
											type="text" size="20" value="${model.skipTime}">
										 <font color="#909090">输入整数</font>			
									</td>
								</tr>
							  	<s:if test="scheGroup.logo!=null&&scheGroup.logo==\"1\"">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">班次所在班组序号(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="schedulesOrder" name="model.schedulesOrder" maxlength="25"
											type="text" size="20" value="${model.schedulesOrder}">
										 <font color="#909090">输入整数</font>			
									</td>
								</tr>
								</s:if>
							</table>
							<br>
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input id="save" type="button" class="input_bg" value="保 存"
														onclick="submitForm()">
												</td>
												<td width="37%">
													<input id="close" type="button" class="input_bg"
														value="关 闭" onclick="window.close();">
												</td>
											</tr>
										</table>
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
