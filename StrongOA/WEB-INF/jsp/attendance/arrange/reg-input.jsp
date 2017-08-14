<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>设置轮班规则</title>
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

			function changeWay(obj){
				obj.checked=true;
				if(obj.value=="2"){
					document.getElementById("month").style.display="";
					document.getElementById("week").style.display="none";
					document.getElementById("day").style.display="none";
				}else if(obj.value=="1"){
					document.getElementById("week").style.display="";
					document.getElementById("month").style.display="none";
					document.getElementById("day").style.display="none";
				}else{
					document.getElementById("day").style.display="";
					document.getElementById("week").style.display="none";
					document.getElementById("month").style.display="none";
				}	
			}
			
			function changeRangeType(obj){
				obj.checked=true;
				if(obj.value=="1"){
					document.getElementById("regStime").disabled=true;
					document.getElementById("regEtime").disabled=true;
					document.getElementById("regSmonth").disabled=false;
					document.getElementById("regEmonth").disabled=false;
				}else{
					document.getElementById("regStime").disabled=false;
					document.getElementById("regEtime").disabled=false;
					document.getElementById("regSmonth").disabled=true;
					document.getElementById("regEmonth").disabled=true;
				}	
			}
			
			function isValidPageNum(str) {
			    str = str.replace(/ /g, "");
			    var pageNumberRegExp = new RegExp("(\\d+(-\\d+)?)(,\\d+(-\\d+)?)*,?", "g");
			    var result = str.match(pageNumberRegExp);
			    if (result != null && result.length == 1 && result[0] == str) {
			        return true;
			    }
			    return false;
			} 
			
			function submitForm(){
			    var pattern =/^\d+$/;
				//轮班方式赋值
				var ways=document.getElementsByName("way");	 
				for(var i=0;i<ways.length;i++){
					if(ways[i].checked==true){
						document.getElementById("repeatWay").value = ways[i].value;
						break;
					}
				}
				
				//如果是按每天的方式
				if(document.getElementById("repeatWay").value=="0"){
					var repeatStime=document.getElementById("repeatStime").value;
					var contiguoousDays=document.getElementById("contiguoousDays").value;
					if(repeatStime==""){
						alert("请选择轮班开始时间！");
						document.getElementById("repeatStime").focus();
						return;
					}
					if(contiguoousDays==""){
						alert("请输入连续天数！");
						document.getElementById("contiguoousDays").focus();
						return;
					}		
					if(!pattern.test(contiguoousDays)){
						alert("连续天数应为整数，请重新输入！");
						document.getElementById("contiguoousDays").focus();
						return;
					}
				}
				//如果是按每周的方式
				if(document.getElementById("repeatWay").value=="1"){
					var weekDays=document.getElementsByName("weekDays");
					var str="";
					var k=0;
					for(var i=0;i<weekDays.length;i++){
						if(weekDays[i].checked==true){
							str+=","+weekDays[i].value;
							k++
							break;
						}
					}
					if(k==0){
						alert("请选择轮班时间！");		
						return;			
					}else{
						str=str.substring(1);
						document.getElementById("weekDays").value=str;
					}
				}
				
				//如果是按每月的方式
				if(document.getElementById("repeatWay").value=="2"){
					var monthDays=document.getElementById("monthDays").value;
					if(monthDays==""){
						alert("请填写轮班时间");
						document.getElementById("monthDays").focus();
						return;
					}else{
						if(!isValidPageNum(monthDays)){
							alert("格式不正确，请重新输入！");
							document.getElementById("monthDays").focus();
							document.getElementById("monthDays").value="";
							return;
						}
					}
					if(document.getElementById("isContainLastDay").checked==true)
						document.getElementById("isContainLastDay").value="1";
					
				}
				//规则范围赋值
				var rangeTypes=document.getElementsByName("rangeType");
				for(var i=0;i<rangeTypes.length;i++){
					if(rangeTypes[i].checked==true){
						document.getElementById("rangeWay").value = rangeTypes[i].value;
						break;
					}
				}
				//按时间段
				if(document.getElementById("rangeWay").value=="0"){
					var regStime=document.getElementById("regStime").value;
					var regEtime=document.getElementById("regEtime").value;
					if(regStime==""){
						alert("规则范围的开始时间不能为空！");
						return;
					}
					if(regEtime!=""&&regStime>regEtime){
						alert("规则范围的开始时间不能晚于结束时间！");
						document.getElementById("regEtime").value="";
						return;
					}				
				}
				
				//按月份
				if(document.getElementById("rangeWay").value=="1"){
					var regSmonth=document.getElementById("regSmonth").value;
					var regEmonth=document.getElementById("regEmonth").value;
					if(regSmonth==""||regEmonth==""){
						alert("规则范围的月份不能为空！");
						return;
					}	
				}
				document.getElementById("myTable").submit();
			}	
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;"
		onload="init()">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/attendance/arrange/reg!save.action">
				<s:hidden id="regId" name="model.regId"></s:hidden>
				<s:hidden id="scheId" name="scheId"></s:hidden>
				<s:hidden id="logo" name="scheGroup.logo"></s:hidden>
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
													设置轮班规则
												</td>
												<td width="70%">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									 <td width="22%" height="21" class="biao_bg1" align="right">
										<span class="wz">班次名称(<FONT color="red">*</FONT>)：</span>
									</td>
									<td class="td1" align="left">
										<input id="scheduleName" name="scheduleName" maxlength="50"
											type="text" size="57" value="${scheduleName}" readonly="readonly">
									</td>
								</tr>
								<tr>
									<td width="22%" height="21" class="biao_bg1" align="right">
										<span class="wz">规则描述：</span>
									</td>
									<td class="td1" align="left">
										<input id="regDesc" name="model.regDesc" maxlength="50"
											type="text" size="57" value="${model.regDesc}">
									</td>
		   
								</tr>
								
							</table>
							<br>
							<fieldset>
								<legend>
									轮班方式(<FONT color="red">*</FONT>)
								</legend>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="22%" class="td1"
											style="border-right:#cccccc solid 1px;">
											<s:hidden id="repeatWay" name="model.repeatWay"></s:hidden>
											<table>
												<tr>
													<td>
														<input type="radio" name="way" value="0" onclick="changeWay(this)">
														每天
													</td>
												</tr>
												<tr>
													<td>
														<input type="radio" name="way" value="1" onclick="changeWay(this)">
														每周
													</td>
												</tr>
												<tr>
													<td>
														<input type="radio" name="way" value="2" onclick="changeWay(this)">
														每月
													</td>
												</tr>
											</table>
										</td>
										<td class="td1" align="left">
											<div id="day" style="display:none">
												<table width="100%">
													<tr>
														<td>
															从
															<strong:newdate id="repeatStime" name="model.repeatStime"
																dateform="yyyy-MM-dd" width="100"
																dateobj="${model.repeatStime}" />
															起每
															<s:select
																list="#{'1':'一','2':'二','3':'三','4':'四','5':'五','6':'六','7':'七','8':'八','9':'九','10':'十','11':'十一','12':'十二','13':'十三','14':'十四'}"
																id="repeatCycle" name="model.repeatCycle" listKey="key"
																listValue="value" />
															天连续
															<input id="contiguoousDays" name="model.contiguoousDays"
																maxlength="3" type="text" size="8"
																value="${model.contiguoousDays}">
														</td>
													</tr>
													<tr>
														<td>

														</td>
													</tr>
													<tr>
														<td>

														</td>
													</tr>
												</table>
											</div>
											<div id="week" style="display:none">
												<table>
												<s:hidden id="weekDays" name="model.weekDays"></s:hidden>
													<tr valign="top">
														<td>	
															<input type="checkbox" name="weekDays" value="7"">周日
															<input type="checkbox" name="weekDays" value="1"">周一
															<input type="checkbox" name="weekDays" value="2"">周二
															<input type="checkbox" name="weekDays" value="3"">周三
														</td>
													</tr>
													<tr>
														<td>	
															<input type="checkbox" name="weekDays" value="4"">周四
															<input type="checkbox" name="weekDays" value="5"">周五
															<input type="checkbox" name="weekDays" value="6"">周六
														</td>
													</tr>
													<tr>
														<td>	
									
														</td>
													</tr>
												</table>
											</div>
											<div id="month" style="display:none">
												<table>
													<tr>
														<td>
															每月的
															<input id="monthDays" name="model.monthDays"
																maxlength="60" type="text" size="30"
																value="${model.monthDays}">号 <font color="#909090">例如1,3,5-7</font>	
														</td>
													</tr>
													<tr>
														<td>
															 包含一个月的最后一天<input id="isContainLastDay" name="model.isContainLastDay" type="checkbox" value="${model.isContainLastDay}">
														</td>
													</tr>
													<tr>
														<td>
														</td>
													</tr>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</fieldset>
							<br>					
							<fieldset>
								<legend>
									规则范围(<FONT color="red">*</FONT>)
								</legend>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="22%" class="td1"
											style="border-right:#cccccc solid 1px;">
											<s:hidden id="rangeWay" name="model.rangeWay"></s:hidden>
											<table>
												<tr>
													<td>
														<input type="radio" name="rangeType" value="0" onclick="changeRangeType(this)">
														按时间段
													</td>
												</tr>
												<tr>
													<td>
														<input type="radio" name="rangeType" value="1" onclick="changeRangeType(this)">
														按月份
													</td>
												</tr>
											</table>
										</td>
										<td class="td1" align="left">
												<table width="100%">
													<tr>
														<td>
															从（S）:
															<strong:newdate id="regStime" name="model.regStime"
																dateform="yyyy-MM-dd" width="100"
																dateobj="${model.regStime}" />
															到（B）:
															<strong:newdate id="regEtime" name="model.regEtime"
																dateform="yyyy-MM-dd" width="100"
																dateobj="${model.regEtime}" />
														</td>
													</tr>
													<tr>
														<td>
															从（S）:
															<s:select cssStyle="width:100" 
																list="#{'1':'一月','2':'二月','3':'三月','4':'四月','5':'五月','6':'六月','7':'七月','8':'八月','9':'九月','10':'十月','11':'十一月','12':'十二月'}"
																id="regSmonth" name="model.regSmonth" listKey="key"
																listValue="value"/>
															到（B）:
															<s:select  cssStyle="width:100"
																list="#{'1':'一月','2':'二月','3':'三月','4':'四月','5':'五月','6':'六月','7':'七月','8':'八月','9':'九月','10':'十月','11':'十一月','12':'十二月'}"
																id="regEmonth" name="model.regEmonth" listKey="key"
																listValue="value" />
														</td>
													</tr>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</fieldset>		
							<br>
							<table width="100%"  height="40" border="0" cellspacing="0" cellpadding="00">
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
	<script type="text/javascript">
		function init(){
		    var regId=document.getElementById("regId").value;
		    var repeatWay=document.getElementById("repeatWay").value;
		    var rangeWay=document.getElementById("rangeWay").value;
		    var logo=document.getElementById("logo").value;
		    var wayobj= document.getElementsByName("way");
	    	if(logo=="1"){
	    		if(regId==null||regId==""){
		    		repeatWay="0";
		    		rangeWay="0";  
	    		} 	
	    		for(var i=1;i<wayobj.length;i++){
	    			wayobj[i].disabled=true;
	    		}
	    	}else{
	    		if(regId==null||regId==""){
	    			repeatWay="1";
	    			rangeWay="0";
	    		}
	    		wayobj[0].disabled=true;
	    	
	    	}
		    changeWay(document.getElementsByName("way")[repeatWay]);
		    changeRangeType(document.getElementsByName("rangeType")[rangeWay]);
			if(repeatWay=="2"){
				 var isContainLastDay=document.getElementById("isContainLastDay").value;
				if(isContainLastDay=="1")
					document.getElementById("isContainLastDay").checked=true;
			}else if(repeatWay=="1"){
				var weekDays=document.getElementById("weekDays").value;
				var weekDaysobj=document.getElementsByName("weekDays");
				for(var i=0;i<weekDaysobj.length;i++){
					if(weekDays.indexOf(weekDaysobj[i].value)!=-1)
						weekDaysobj[i].checked=true;	
				}
			}
		}
	</script>
</html>
