<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
	<%
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String dateStr=sdf.format(date);
    %>
		<title>考勤汇总</title>
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
			 window.name="MyModalDialog"; 
			 function radioOrg(str){//选择人员
			 	var url="<%=path%>/fileNameRedirectAction.action?toPage=/attendance/arrange/scheGroup-selectperson.jsp?orgId=${param.orgId}";
			   	var audit=OpenWindow(url,320,380,window);
			    if(audit==undefined||audit==null||audit==""){
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
	             	$("#userIds").val(personId);
	             	$("#personNames").val(personName);	
			    }
			}
		//保存	
			function submitForm(){
				var startTime=document.getElementById("beginTime").value;
				var endTime=document.getElementById("endTime").value;
				var userIds=document.getElementById("userIds").value;
				if(startTime==""||endTime==""){
					alert("请选择考勤汇总的开始时间和结束时间！");	
					return false;		
				}else if(startTime>endTime){
					alert("结束时间需晚于或等于开始时间！")
					return;
				}
				if(userIds==""){
					alert("请选择要汇总考勤的人员！");
					return false;
				}
				showLoading(true);
				$("#myTable").submit();
			}
			function showLoading(bl){
				var state = bl ? "block" : "none";
				LOADING.style.display = state;
			}
		</SCRIPT>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<s:form id="myTable" theme="simple"
				action="/attendance/gather/gather!save.action" target="MyModalDialog">
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
													考勤汇总
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
								<input type="hidden" id="userIds" name="userIds">
								<input type="hidden" id="type" name="type" value="calAttend">
								<input type="hidden" id="personOpts" name="personOpts">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">日期：</span>
									</td>
									<td class="td1" align="left">
									    <strong:newdate id="beginTime" name="beginTime"
											dateform="yyyy-MM-dd" width="125" 
											dateobj="${beginTime}" maxdate="<%=dateStr%>" />
										至
										<strong:newdate id="endTime"  name="endTime"
											dateform="yyyy-MM-dd" width="125"
											dateobj="${endTime}"  maxdate="<%=dateStr%>" />
											
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">人员：</span>
									</td>
									<td class="td1" align="left">									
										 <textarea rows="13" cols="35" id="personNames" name="userNames"  onkeydown="return false;"  style="overflow: auto;"></textarea>
										 	<input type="button" class="input_bg" value="选 择" onclick="radioOrg('per')">
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
													<input id="save" type="button" class="input_bg" value="汇  总"
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
		<DIV id=LOADING
			style="position: absolute; top: 30%; left: 38%; display: none"
			align=center>
			<font color=#16387C><strong>正在汇总，请稍候...</strong> </font>
			<br>
			<IMG src="<%=frameroot%>/images/tab/loading.gif">
		</DIV>
	</body>
</html>
