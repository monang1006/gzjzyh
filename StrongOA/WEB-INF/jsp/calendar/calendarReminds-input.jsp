<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@page import="java.net.URLEncoder"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%> 
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.io.UnsupportedEncodingException"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<%
	String caltitle = (String)request.getParameter("title") ; 
	
	try {	
			if(caltitle!=null&&caltitle!=""){
				caltitle = URLEncoder.encode(caltitle, "utf-8");
				caltitle = URLDecoder.decode(caltitle, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	String stime = (String)request.getParameter("stime"); 
	String etime = (String)request.getParameter("etime"); 
%>
<html>
	<head>
		<title>设置提醒方式</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">

			String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
            
	$(document).ready(function(){
          $.ajax({
			type:"post",
			dataType:"text",
			url:"<%=path%>/calendar/calendar!hasSmsRight.action",
			data: "",
			success:function(msg){ 
				if("true"==msg){//开启
					var t = "<input type=\"checkbox\" id=\"sms\" name=\"Set_REMIND\" value = \"0\" onclick=\"setREMIND(this)\"> 手机短信";
					$("#remType").html($("#remType").html()+t);
				}else{
					var t = "<input type=\"checkbox\" name=\"Set_REMIND\" id=\"sms\" value=\"0\" disabled=\"disabled\">发送短信";
					$("#remType").html($("#remType").html()+t);
				}
			}
		});
		
	});
		 function save(){
		 	var remindTime = document.getElementById("remindTime").value;//yyyy-MM-dd HH:mm:ss 2009-04-24 20:50:33
		 	
		 	var remindCon = document.getElementById("remindCon").value;//提醒内容
		 	var setTime = document.getElementById("setTime").value;//提前时间

	/*	
		 	var remindTYPE = document.getElementById("remindTYPE").value;//提醒类型
			//提醒方式 0000：消息 邮件 RTX 短信
			var Set_REMIND = document.getElementsByName("Set_REMIND");
			var str = "";
			for(var i=0;i<Set_REMIND.length;i++){
				str += Set_REMIND[i].value;
				if(i==0&&Set_REMIND[i].value==1){
					remindTYPE +="msg"+" ";
				}
				if(i==1&&Set_REMIND[i].value==1){
					remindTYPE +="mail"+" ";
				}
				if(i==2&&Set_REMIND[i].value==1){
					remindTYPE +="rtx"+" ";
				}
				if(i==3&&Set_REMIND[i].value==1){
					remindTYPE +="sms"+" ";
				}
				
			}
			if(""!=remindTYPE&&null!=remindTYPE){
				remindTYPE = remindTYPE.substring(0,remindTYPE.length-1);
			}else{
				alert("请至少选择一种提醒方式！");
				return;
			}
			
		*/	
			if(""==remindCon.trim()){
				alert("请输入提醒内容！");
				return;
			}
			
			//提醒时间
			var now = new Date();
			var stime = "<%=stime%>";
			var stime = "<%=stime%>";
			var setTime= $("#setTime").val();
			var stimes=stime.split(" ");
			var stimes1=stimes[0].split("-");
			var stimes2=stimes[1].split(":");
			var time=new Date(stimes1[0],stimes1[1]-1,stimes1[2],stimes2[0],stimes2[1],stimes2[2]);
			var date=time.getTime()-now.getTime();
			if(setTime.split("_")[0]=="m"){
				var h= parseInt(setTime.split("_")[1])*60*1000;
				if(date<h){
					alert("开始时间已不足"+setTime.split("_")[1]+"分钟。");
					return ;
				}
			}
			if(setTime.split("_")[0]=="h"){
				var h= parseInt(setTime.split("_")[1])*3600*1000;
				if(date<h){
					alert("开始时间已不足"+setTime.split("_")[1]+"小时。");
					return ;
				}
			}
			if(setTime.split("_")[0]=="d"){
				var h= parseInt(setTime.split("_")[1])*24*3600*1000;
				if(date<h){
					alert("开始时间已不足"+setTime.split("_")[1]+"天。");
					return ;
				}
			}
			if(""!=stime|null!=stime){
				var temp = stime.split(" ");
				var tempD = temp[0].split("-");
				var tempT = temp[1].split(":");
				now.setYear((tempD[0]));
				now.setMonth((tempD[1])-1);
				now.setDate((tempD[2]));
				now.setHours((tempT[0]));
				now.setMinutes((tempT[1]));
				now.setSeconds((tempT[2]));
			}
		
			var setT = setTime.split("_");
			if(setT[0]=="m"){
				now.setMinutes(parseInt(now.getMinutes())-parseInt(setT[1]));
			}else if(setT[0]=="h"){
				now.setHours(parseInt(now.getHours())-parseInt(setT[1]));
			}else if(setT[0]=="d"){
				now.setDate(parseInt(now.getDate())-parseInt(setT[1]));
			}else if(setT[0]=="w"){
				now.setDate(parseInt(now.getDate())-(parseInt(setT[1])*7));
			}

			remindTime = now.getYear()+"-"+formatToNum(parseInt(now.getMonth())+1)+"-"+formatToNum(now.getDate())+" "
					+formatToNum(now.getHours())+":"+formatToNum(now.getMinutes())+":"+formatToNum(now.getSeconds());

			//	alert( "选中的提醒方式:"+getRemindValue().toLowerCase());
		 	//returnValue = remindTime+","+remindTYPE+","+remindCon;
			//	 returnValue = setTime+","+str+","+remindCon;

			//是否提醒共享人 remindShare:0(不提醒)； remindShare:1(提醒)；
			var RS = document.getElementsByName("REMIND_SHARE");
			var remindShare = "";
			for(var i=0;i<RS.length;i++) {
		      if(RS[i].checked) {remindShare = RS[i].value;break;}
		    }
		    var reValue = getRemindValue();
			if(0==reValue){
				alert("请选择提醒方式！");
				return ;
			}
			
			returnValue = remindTime+","+reValue.toLowerCase()+","+remindCon+","+remindShare;
			window.close();
		 }
		 
		//如果时间位数是个位数 则在前加0
		function formatToNum(num){
			if(num<10){
				return "0"+num;
			}else{
				return num;
			}
		}				 
		
		
		 function del(){
	 		document.getElementById("remindTime").value="";
	 		document.getElementById("remindCon").value="";
		 }
		 function setType(val){
		 	document.getElementById("remindTYPE").value = val;
		 }
		 
		 //提醒方式
		 function setREMIND(obj){
		 	if("1"==obj.value){
		 		obj.value = "0";
		 	}else{
		 		obj.value = "1";
		 	}
		 }
		 
		 //获取提醒方式
			function getRemindValue(){
				var returnValue = "";
				$("#StrRem").find("input:checkbox:checked").each(function(){
					returnValue = returnValue + $(this).val() + " ";
				});
				if(returnValue!=""){
					returnValue = returnValue.substring(0,returnValue.length-1);
				}
				return returnValue;
			}
		 
		 
		 function init(){
		 	document.getElementById("remindCon").value = "日程活动安排:<%=caltitle%>"+ "(起止时间：<%=stime%> 到 <%=etime%> )";
		 }
		</script>
	</head>
	<base target="_self"/>
	<body onload="init();" style="background-color:#ffffff">
	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
	
	<DIV align=center>
	<a id="reload" style="display: none" href=""></a>
	<s:form action="calendarActivity!saveForm.action" name="form">
	<input type="hidden" id="activityId" name="model.activityId" value="${activityId}">
	<input type="hidden" id="remindTYPE" name="remindTYPE" value="">
	<input type="hidden" id="remindTime" name="remindTime" value="">
	<input type="hidden" id="remindJsonArr" name="remindJsonArr" value="">
	<table width="100%" border="0" cellspacing="0" cellpadding="00">
		<tr>
			<td height="40"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">				
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>设置提醒方式</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="del();">&nbsp;清&nbsp;空&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
					
				</table>
			</td>
		</tr>
	</table>
		
	<table border="0" cellpadding="0" cellspacing="1" width="100%" height="100%">
		<tr>
		<td valign="top" align="left" width="70%">
			<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
				<%--<tr id="time">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">提醒时间(<FONT color="red">*</FONT>)：
						</span>
					</td>
					<td class="td1">
						&nbsp;<strong:newdate name="remindTime" id="remindTime" dateform="yyyy-MM-dd HH:mm:ss"
							width="40%" skin="whyGreen" isicon="true"></strong:newdate>
					</td>
				</tr>
				<tr id="isRemind_sel">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">提醒方式(<FONT color="red">*</FONT>)：
						</span>
					</td>
					<td class="td1">
						<input type="radio" name="REMIND_TYPE" value = "0" onclick="setType(this.value)">使用内部消息提醒&nbsp;&nbsp;
						<input type="radio" name="REMIND_TYPE" value = "1" onclick="setType(this.value)" checked > 使用电子邮件提醒
					</td>
				</tr>
				--%>
				<tr id="time">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz"><FONT color="red">*</FONT>&nbsp;提前：&nbsp;
						</span>
					</td>
					<td class="td1" style="padding-left:5px;" >
						<script>
								document.write("<select style=\"width: 60%;background-color:#ffffff\" id=\"setTime\"");
									document.write("<option value=\"m_"+0+"\">0分钟</option>");
									for(var i=0;i<60;i+=15){
										document.write("<option value=\"m_"+i+"\">"+i+"分钟</option>");
									}
									document.write("<option value=\"h_1\" selected=\"selected\">1小时</option>");
									for(var i=2;i<13;i++){
										document.write("<option value=\"h_"+i+"\">"+i+"小时</option>");
									}
								document.write("<option value=\"d_1\">"+1+"天</option>");
								document.write("</select>");
								</script>
					</td>
				</tr>
				<%--<tr id="isRemind_sel">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">提醒方式(<FONT color="red">*</FONT>)：
						</span>
					</td>
					<td class="td1" id="remType">
						<input type="checkbox" id="msg" name="Set_REMIND" value = "1" onclick="setREMIND(this)" checked>内部消息&nbsp;&nbsp;
						<input type="checkbox" id="mail" name="Set_REMIND" value = "0" onclick="setREMIND(this)"> 电子邮件&nbsp;&nbsp;
						<input type="checkbox" id="rtx" name="Set_REMIND" value = "0" onclick="setREMIND(this)">即时通讯&nbsp;&nbsp;
						<script>
						</script>
					</td>
				</tr>
				--%>
				<tr id="isRemind_sel">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz"><FONT color="red">*</FONT>&nbsp;提醒方式：&nbsp;
						</span>
					</td>
					<td class="td1" id="StrRem" style="padding-left:5px;">
						<strong:remind includeRemind="RTX,SMS" isOnlyRemindInfo="true" code="<%=GlobalBaseData.SMSCODE_CALENDAR %>"/>
					</td>
				</tr>
				
				<tr id="isRemind_con">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz"><FONT color="red">*</FONT>&nbsp;提醒内容：&nbsp;
						</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<input id="remindCon" name="remindCon" type="text" value=""   style="width: 90%;background-color:#ffffff"
							 size="45" maxlength="200">
					</td>
				</tr>
				<!-- 屏蔽提醒共享人
				<tr id="isRemind_con">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz"><FONT color="red">*</FONT>&nbsp;提醒共享人：&nbsp;
						</span>
					</td>
					<td class="td1" style="padding-left:5px;">
						<input type="radio" name="REMIND_SHARE" value = "0" checked> 不提醒&nbsp;&nbsp;
						&nbsp;<input type="radio" name="REMIND_SHARE" value = "1" >提醒&nbsp;&nbsp;
						<br>
					</td>
				</tr>
				 -->
				<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
			</table>
		</td>
		</tr>
	</table>
	</s:form>
	</DIV>
	</body>
</html>
