<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
Date date=new Date();
SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
String dateStr=sdf.format(date);
%>
<html>
	<head>
		<title>考勤计算</title>
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
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<base target="_self">
		<SCRIPT type="text/javascript">	
			 function radioOrg(str){
			 	var url="<%=path%>/fileNameRedirectAction.action?toPage=/attendance/arrange/scheGroup-selectperson.jsp?orgId=${param.orgId}";
			   	var audit=OpenWindow(url,320,380,window);
			    if(audit==undefined||audit==null){
			      	return false;
			    }else{  
					document.getElementById("personOpts").value=audit;
			    	var auditArr=audit.split(",");
			    	var values="";
			    	var personId="";
			    	var personName="";
			    	for(var j=0; j<auditArr.length; j++){	
			    		values=auditArr[j].split("|");	    
						personId+= ","+values[0];
						personName+=","+values[1];
					}
					if(personId.length!=0){
						personId=personId.substring(1);
					}
					if(personName.length != 0){
						personName = personName.substring(1);
					}
	             	$("#personIds").val(personId);
	             	$("#personNames").val(personName);	
			    }
			}
			
			function submitForm(){
				var startTime=document.getElementById("startTime").value;
				var endTime=document.getElementById("endTime").value;
				var personIds=document.getElementById("personIds").value;
				if(startTime==""||endTime==""){
					alert("请选择考勤计算的开始时间和结束时间！");	
					return false;		
				}else if(startTime>endTime){
					alert("结束时间需晚于或等于开始时间！")
					return;
				}
				if(personIds==""){
					alert("请选择要计算考勤的人员！");
					return false;
				}
				showLoading(true);
				$("#save").attr("disabled","false");
				$("#close").attr("disabled","false");
				$("#selectPer").attr("disabled","false");
				document.getElementById("myTable").submit();
			}
			function showLoading(bl){
				var state = bl ? "block" : "none";
				LOADING.style.display = state;
			}
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<form id="myTable" method="post"  target="hidden_frame"
				action="<%=root%>/attendance/attendmaintain/maintain!cal.action">
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
													考勤计算
												</td>
												<td width="*">
													&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<input type="hidden" id="personIds" name="personIds">
								<input type="hidden" id="type" name="type" value="calAttend">
								<input type="hidden" id="personOpts" name="personOpts">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">日期：</span>
									</td>
									<td class="td1" align="left">
									    <strong:newdate id="startTime" name="startTime"
											dateform="yyyy-MM-dd" width="125" 
											dateobj="${startTime}" maxdate="<%=dateStr%>"/>
										至
										<strong:newdate id="endTime"  name="endTime"
											dateform="yyyy-MM-dd" width="125"
											dateobj="${endTime}" maxdate="<%=dateStr%>"/>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">人员：</span>
									</td>
									<td class="td1" align="left">									
										 <textarea rows="13" cols="35" readonly="readonly" id="personNames" name="personNames"  style="overflow: auto;"></textarea>
										 	<input id="selectPer" type="button" class="input_bg" value="选 择"  onclick="radioOrg('per')">
									</td>
								</tr>
								
							</table>
							<br>
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td width="29%">
													<input id="save" type="button" class="input_bg" value="计 算"
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
			</form>
		</DIV>
		<DIV id=LOADING
			style="position: absolute; top: 30%; left: 38%; display: none"
			align=center>
			<font color=#16387C><strong>正在计算，请稍候...</strong> </font>
			<br>
			<IMG src="<%=frameroot%>/images/tab/loading.gif">
		</DIV>
			<iframe name='hidden_frame' id="hidden_frame" style='display:none'></iframe>
	</body>
</html>
