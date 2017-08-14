<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@taglib uri="/tags/web-newdate" prefix="sq"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="java.io.UnsupportedEncodingException"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);

	String remindContent = request.getParameter("remindContent");
	try {
		remindContent = URLDecoder.decode(remindContent, "utf-8");
		System.out.println("remindContent:" + remindContent);
	} catch (Exception e) {
		remindContent = "";
	}
%>
<html>
	<head>
		<title>设置提醒方式</title>
		<%@include file="/common/include/meta.jsp"%>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows.css">

		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		$(document).ready(function(){
			//添加提醒人员
			$("#addPerson").click(function(){
				var url = "<%=path%>/fileNameRedirectAction.action?toPage=address/addressOrg-selectperson.jsp";
				var a = window.showModalDialog(url,window,'dialogWidth:750px;dialogHeight:550px;help:no;status:no;scroll:no')
			});
			//清空提醒人员
			$("#clearPerson").click(function(){
				$("#orgusername").val("");
				$("#orguserid").val("");
			});
			//初始化日期数据  
			init();
		});
		//初始化
		function init(){
			//var now=new Date();
			//form.EndTime.value=now.getYear()+"-"+(now.getMonth()+1)+"-"+(now.getDay()+1)+" "+now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
		}
		//选择是否是周期性循环定时
		function changeType(cal_type){
			if(cal_type==1){//选择循环
				selremind.style.display="";//重复周期的下拉框
				validTime.style.display="";//重复周期的有效时间
				timer.style.display="none";//不循环的提醒时间
				sel_change();
			}else{//不循环
				selremind.style.display="none";
				validTime.style.display="none";
				timer.style.display="";
				day.style.display="none";
				week.style.display="none";
				mon.style.display="none";
				year.style.display="none";
				
			}
		}
		
		function save(){
			var restate="null";
			var remindState = document.getElementsByName("remindState");
			for(var i=0;i<remindState.length;i++){
				if(remindState[i].checked){
					restate = remindState[i].value;
				}
			}
			
			var strUser=$("#orgusername").val();
			var strId=$("#orguserid").val();
			
			if(restate=="null"){
				alert("请设置提醒对象!");
				return;
			}
			if((strUser=="")||(strId=="")){
			}
			
			var remindTime=form.remindTime.value;
			var compareTime=new Date(form.remindTime.value.replace(/-/g,"/"));
			if(compareTime.getTime()<=new Number("<%=new Date().getTime()%>")){
				alert("定时提醒时间不能比当前时间早!");
				return;
			}
			
			//alert(remindTime+";"+strUser+";"+strId+";"+restate);
			var conent = $("#remindCon").val();
			conent = $.trim(conent);
			if(conent==""){
				alert("提醒内容不能为空！");
				$("#remindCon").val("");
				return;
			}
			
			window.returnValue = remindTime+";"+strUser+";"+strId+";"+restate+";"+$("#remindCon").val();
			window.close();
			//$("form").submit();
		}
		
		//设置不同循环显示的界面
		function sel_change()
		{
		   document.getElementById("day").style.display="none";
		   document.getElementById("week").style.display="none";
		   document.getElementById("mon").style.display="none";
		   document.getElementById("year").style.display="none";
		   var selVal=form.TYPE.value;
		   if(selVal=="2")
		      aff_type="day";
		   if(selVal=="3"){
		      aff_type="week";
		   }
		   if(selVal=="4"){
		      aff_type="mon";
		   }   
		   if(selVal=="5"){
		      aff_type="year";
		   }
		   document.getElementById(aff_type).style.display="";
		   form.repeatType.value = selVal;
		}

		</script>
	</head>
	<base>
	<body>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV align=center>
			<a id="reload" style="display: none" href=""></a>
			<s:form action="message/message!saveRemind.action" name="form">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td>
										&nbsp;
									</td>
									<td width="227">
										<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9"
											alt="">
										&nbsp; 设置定时提醒
									</td>
									<td>

									</td>
									<td width="290">
								</tr>
							</table>
						</td>
					</tr>
				</table>

				<table border="0" cellpadding="0" cellspacing="1" width="100%"
					height="100%">
					<tr>
						<td valign="top" align="left" width="70%">
							<table border="0" cellpadding="0" cellspacing="1" class="table1"
								width="100%">
								<tr id="timer">
									<td align="right" class="biao_bg1">
										定时时间(
										<font color=red>*</font>)：
									</td>
									<td class="td1">
										&nbsp;
										<sq:newdate name="remindTime" dateobj="<%=new Date()%>"
											id="remindTime" dateform="yyyy-MM-dd HH:mm:ss" width="200px"
											skin="whyGreen" isicon="true"></sq:newdate>
									</td>
								</tr>
								<tr id="remindset" title="在这里设置提醒对象">
									<td nowrap class="biao_bg1" align="right" height="26">
										设置提醒对象(
										<font color=red>*</font>)：
									</td>
									<td class="td1">
										<input type="radio" id="remindState0" name="remindState"
											value="0" onclick=" ">
										<label>
											只提醒自己
										</label>
										&nbsp;&nbsp;
										<br>
										&nbsp;&nbsp;
										<input type="radio" id="remindState2" name="remindState"
											value="2" onclick=" ">
										只提醒办理人&nbsp;&nbsp;
										<br>
										&nbsp;&nbsp;
										<input type="radio" id="remindState1" name="remindState"
											value="1" onclick=" ">
										提醒自己和办理人&nbsp;&nbsp;
									</td>
								</tr>
								<tr id="isShare_sel" title="在这里指定要提醒的对象">
									<td height="10%" class="biao_bg1" valign="top" align="right"
										valign="bottom">
										提醒其他人员：
										<br>
										<br>
										<br>
									</td>
									<td valign="top" class="td1">
										<div style="margin-left: 14px">
											<s:textarea title="双击选择用户" cols="30" id="orgusername"
												name="orgusername" ondblclick="addPerson.click();" rows="4"
												readonly="true"></s:textarea>
											<input type="hidden" id="orguserid" name="orguserid"></input>
											<a id="addPerson"
												url="<%=root%>/address/addressOrg!tree.action" href="#">添加</a>&nbsp;
											<a id="clearPerson" href="#">清空</a>
										</div>
									</td>
								</tr>

								<tr id="isRemind_con">
									<td nowrap class="biao_bg1" align="right">
										<span class="wz">提醒内容(<FONT color="red">*</FONT>)： </span>
									</td>
									<td class="td1">
										&nbsp;
										<input id="remindCon" name="remindCon" type="text"
											value="<%=remindContent%>" style="width: 90%" size="45"
											maxlength="45">
									</td>
								</tr>

								<tr align="center" class=biao_bg1>
									<td colspan="2" nowrap>
										<input type="button" value="保存" class="input_bg"
											onclick="save();">
										&nbsp;&nbsp;
										<input type="button" value="取消" class="input_bg"
											onclick="window.close();">
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
