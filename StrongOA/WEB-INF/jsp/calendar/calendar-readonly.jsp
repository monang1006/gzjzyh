<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%> 
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
		<title>查看日程</title>
		<link rel="stylesheet" type="text/css" href="<%=frameroot%>/css/properties_windows.css">
		<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js" type="text/javascript"></script>
		
		<style type="text/css">
			 body, table, tr, td,div{
			    margin:0px;
			}
		</style>
		<script type="text/javascript">
		//提醒记数
		var remindCount=0;
		//提醒数据
		var remindContent="";
		//删除的提醒id
		var delremindIds="";
		
		//是否为周期循环日程
		function changeType(cal_type){
			var onceTaskTime = document.getElementById("onceTaskTime");
			var remindtime = document.getElementById("remindtime");
			var selremind = document.getElementById("selremind");
			var sel_day = document.getElementById("day");
			var sel_week = document.getElementById("week");
			var sel_month = document.getElementById("mon");
			var sel_year = document.getElementById("year");
			if(cal_type==0){//已选择周期性活动
				selremind.style.display="";
				sel_day.style.display="";
			}else{
				selremind.style.display="none";
				sel_day.style.display="none";
				sel_week.style.display="none";
				sel_month.style.display="none";
				sel_year.style.display="none";
			
			}
		}

		//设置日程视图的展现模式
		var aff_type="day";
		function sel_change()
		{
		   if(aff_type!="")
		      document.getElementById(aff_type).style.display="none";
		   if(form.TYPE.value=="2")
		      aff_type="day";
		   if(form.TYPE.value=="3")
		      aff_type="week";
		   if(form.TYPE.value=="4")
		      aff_type="mon";
		   if(form.TYPE.value=="5")
		      aff_type="year";
		   document.getElementById(aff_type).style.display="";
		}
		
		//是否选择共享
		function isShare_change(isShare){
			var isShare_sel=document.getElementById("isShare_sel");
			if(isShare=="yes"){
				isShare_sel.style.display="block";
			}else{
				isShare_sel.style.display="none";
			}
		}
		
		//是否选择提醒
		function inRemind_change(isRemind){
			var ifRemind=document.getElementById("ifRemind");
			var setRemind=document.getElementById("setRemind");
			if(isRemind=="yes"){
				setRemind.style.display="block";
				ifRemind.value = 1 ;
				document.getElementById("isRemind_yes").checked="checked";
			}else{
				setRemind.style.display="none";
				ifRemind.value = 0 ;
				document.getElementById("isRemind_no").checked="checked";
			}
		}
		
		//是否作为领导日程进行发布
		function changePubLeader(isleader){
			document.getElementById("isLeader").value = isleader;
		}
		
		//关闭窗口
		function windowclose(){
		window.close();
		}
		
		//提交表单
		function save(){
			//获取说明内容
			getCalContent(form);
			//获取提醒内容
			if(document.getElementById("isRemind").value=="yes"){
				getRemindContent();
			}
			//获取被删除的附件id
			var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
            	}
            document.getElementById("delAttachIds").value = delAttachIds;
            //获取被删除的提醒id
			var delRemindIds = document.getElementById("delRemindIds").value;
			if(delRemindIds.length>0){
            		delRemindIds = delRemindIds.substring(0,delRemindIds.length-1);
            	}
            document.getElementById("delRemindIds").value = delRemindIds;
			//时间验证
			var stime = form.calStartTime.value;
			var etime = form.calEndTime.value;
			if(stime==null|stime==""|etime==null|etime==""){
				alert("请输入日程的活动时间");
				return false;
			}else{
				if(date2string(stime)>=date2string(etime)){
					alert("日程的活动开始时间不能比结束时间晚");
					return false;
				}
			}
			form.submit();
		}
	$(document).ready(function() {
		var message = $(".actionMessage").text();
		if(message!=null && message!=""){
			alert(message);
			window.returnValue = "reload";
			window.close();
		}
	});
		//获取说明内容
         function getCalContent(form) {
             var oEditor = FCKeditorAPI.GetInstance('content');
             var acontent = oEditor.GetXHTML();
			 form.calCon.value = acontent;
         }
         
         //获取日程循环信息
         function getCalRepeatType(){
         	var nowTime = new Date();
         	switch (form.repeatType.value){
				case "3" : //周(HH:mm)
					var s = form.weekReplayStart.value;
					var e = form.weekReplayEnd.value;
					if(form.week_s.value>form.week_e.value){
							return "日程的活动每次重复开始时间不能比结束时间晚！";
					}else if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空！";
					}else{
						s = s.replace(":","");
						e = e.replace(":","");
						if(s>=e){
							return "日程的活动每次重复开始时间不能比结束时间晚！";
						}
					}
					var sdate = nowTime.getDate()-nowTime.getDay()+parseInt(form.week_s.value);
					var edate = nowTime.getDate()-nowTime.getDay()+parseInt(form.week_e.value);
					form.calRepeatStime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+sdate+" "+form.weekReplayStart.value+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+edate+" "+form.weekReplayEnd.value+":00";
					break;
				case "4" : //月(dd号 HH:mm)
					var s = form.monReplayStart.value;
					var e = form.monReplayEnd.value;
					if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空！";
					}else{
						s = s.replace("号","").replace(":","");
						e = e.replace("号","").replace(":","");
						if(s>=e){
							return "日程的活动每次重复开始时间不能比结束时间晚！";
						}
					}
					form.calRepeatStime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+form.monReplayStart.value.replace("号","")+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+form.monReplayEnd.value.replace("号","")+":00";
					break;
				case "5" : //年(MM-dd HH:mm)
					var s = form.yearReplayStart.value;
					var e = form.yearReplayEnd.value;
					if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空！"
					}else{
						s = s.replace("-","").replace(" ","").replace(":","");
						e = e.replace("-","").replace(" ","").replace(":","");
						if(s>=e){
							return "日程的活动每次重复开始时间不能比结束时间晚！"
						}
					}
					form.calRepeatStime.value = nowTime.getYear()+"-"+form.yearReplayStart.value+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+form.yearReplayEnd.value+":00";
					break;
				default : //日(HH:mm)
					form.repeatType.value = "2";
					var s = form.dayReplayStart.value;
					var e = form.dayReplayEnd.value;
					if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空！"
					}else{
						s = s.replace(":","");
						e = e.replace(":","");
						if(s>=e){
							return "日程的活动每次重复开始时间不能比结束时间晚！"
						}
					}
					form.calRepeatStime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+nowTime.getDate()+" "+form.dayReplayStart.value+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+nowTime.getDate()+" "+form.dayReplayEnd.value+":00";
					break;
			}
         }
         //转换时间格式(yyyy-MM-dd HH:mm)--->(yyyyMMddHHmm)
         function date2string(stime){
         	var arrsDate1=stime.split('-');
         	stime=arrsDate1[0]+""+arrsDate1[1]+""+arrsDate1[2];
         	var arrsDate2=stime.split(' ');
         	stime=arrsDate2[0]+""+arrsDate2[1];
         	var arrsDate3=stime.split(':');
         	stime=arrsDate3[0]+""+arrsDate3[1]+""+arrsDate3[2];
         	return stime;
         }
         //刷新窗口
         function refreshDial(activityId){
         	var reload = document.getElementById("reload");
         	//参数
         	var calendarId = document.getElementById("calendarId").value;
         	var calTitle = document.getElementById("calTitle").value;
         	var calStartTime = document.getElementById("calStartTime").value;
         	var calEndTime = document.getElementById("calEndTime").value;
         	var calPlace = document.getElementById("calPlace").value;
         	var acontent = FCKeditorAPI.GetInstance('content').GetXHTML();
         	reload.href="<%=path%>/calendar/calendar!refreshinput.action?model.calStartTime="+calStartTime+"&model.calEndTime="+calEndTime
         					+"&model.calendarId="+calendarId+"&model.calTitle="+encodeURI(encodeURI(calTitle))+"&model.toaCalendarActivity.activityId="+activityId
         					+"&model.calCon="+encodeURI(encodeURI(acontent))+"&model.calPlace="+encodeURI(encodeURI(calPlace));
         	reload.click();
         }
         //新建活动分类
         function addNewActivity(){
         	var newActivityName = document.getElementById("newActivityName");
         	if(newActivityName.value==null|newActivityName.value==""){
         		alert("请输入分类名称!");
         		return false;
         	}else{
	         	var myAjax = new Ajax.Request(
	                 '<%=path%>/calendar/calendarActivity!save.action', // 请求的URL
	                {
	                    //参数
	                    parameters : 'model.activityName='+encodeURI(encodeURI(newActivityName.value)),
	                   
	                    // 使用GET方式发送HTTP请求
	                    method:  'post', 
	                    // 指定请求成功完成时需要执行的js方法
	                    onComplete: function(response){
		                    	var activityId = response.responseText||"no response text";
		                    	if(activityId!="no response text"){
		                    		refreshDial(activityId);
		                    	}else{alert("不能添加分类");}
	                    	}
	                }
	            );
         	}
         }
         
         //设置提醒
         function setRemind(){
         	var url = "<%=path%>/fileNameRedirectAction.action?toPage=calendar/calendarReminds-input.jsp";
			var a = window.showModalDialog(url,window,'dialogWidth:550px;dialogHeight:330px;help:no;status:no;scroll:no');
			if((typeof a)!="undefined"){
				var remindHtml = document.getElementById("remindContent");
				a="id"+remindCount+","+a;
				
				//添加提醒数据
				if(remindContent!=""&&remindContent!=null){
					remindContent+=";"
				}
				remindContent+=a;
				
				var arr=a.split(',');
				if(arr[2]=="0"){arr[2]="站内消息";}
				if(arr[2]=="1"){arr[2]="电子邮件";}
				remindHtml.innerHTML+="<div id="+arr[0]+" style=\"display: \">&nbsp;提醒时间为："
						+arr[1]+"，提醒方式为："+arr[2]+" <input type='button' value='删除' class='input_bg' onclick='delremind(\""+arr[0]+"\");'><hr></div>";
				
				remindCount++;
			}
         }
         //删除数据库中的提醒
         function delDBremind(id){
         	document.getElementById("delRemindIds").value+=id+",";
         	delremind(id);
         }
         //删除提醒
         function delremind(id){
         	var remindDiv = document.getElementById(id);
         	var tempRemind="";
         	var res="";
         	if(remindContent.length>0){
            		tempRemind = remindContent.substring(0,remindContent.length-1);
            	}
         	var as = tempRemind.split(';');
         	for(var i=0;i<as.length;i++){
         		if(as[i].indexOf(id)!=0){
         			res+=as[i]+";";
         		}
         	}
         	remindDiv.style.display="none";
         	remindContent = res;
         }
         
         //获取提醒数据
         function getRemindContent(){
         	if(remindContent.length>0){
            		remindContent = remindContent.substring(0,remindContent.length-1);
            	}
         	document.getElementById("remindData").value = remindContent;
         }
 		//删除附件
		 function delAttach(id){
		 	var attach = document.getElementById(id);
		 	var delAttachIds = document.getElementById("delAttachIds").value;
		 	document.getElementById("delAttachIds").value += id + ",";
		 	attach.style.display="none";
		 }
		 //下载附件
		 function download(id){
		  var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/calendar/calendar!down.action?attachId="+id;
		 }
		 
		 //初始化页面
		 function init(){
		 //提醒
		 	var remindData = "${remindData}";
		 	if(remindData!=null&&remindData!=""){
		 		inRemind_change("yes");
		 		if("noRemind"!=remindData){
				 	if(remindData.length>0){
	            		remindData = remindData.substring(0,remindData.length-1);
	            	}
				 	//添加提醒数据
				 	var remindHtml = document.getElementById("remindContent");
				 	var as = remindData.split(';');
		         	for(var i=0;i<as.length;i++){
						var arr=as[i].split(',');
						if(arr[2]=="0"){arr[2]="站内消息";}
						if(arr[2]=="1"){arr[2]="电子邮件";}
						if(arr[1].length>0){//
		            		arr[1] = arr[1].substring(0,arr[1].length-2);
		            	}
						remindHtml.innerHTML+="<div id="+arr[0]+" style=\"display: \">&nbsp;提醒时间为："
								+arr[1]+"，提醒方式为："+arr[2]+" <input type='button' value='删除' class='input_bg' onclick='delDBremind(\""+arr[0]+"\");'><hr></div>";
		         	}
					
					remindContent = remindData;
		 		}
		 	}
		   //领导
		 	var ifleader = document.getElementById("ifleader").value;
		 	if(ifleader=="1"){
		 		var ifPubleaderTD=  document.getElementById("ifPubleaderTD");
		 		ifPubleaderTD.style.display="";
			 	if(document.getElementById("isLeader").value=="1"){
			 		document.getElementById("ifPubleader_yes").checked="checked";
			 		document.getElementById("ifPubleader_no").checked="";
		 		}
		 	}
		   //共享
		   	var share = '${model.toaCalendarShares}';
		   	if(share!="[]"&&share!=""&&share!=null){
		   		document.getElementById("isShare_yes").checked = "checked";
		   		isShare_change("yes");
		   	}
		   //循环
		   var repeatType = '${model.repeatType}';
		   var RepS = "${model.calRepeatStime}";
		   var RepE = "${model.calRepeatEtime}"
		   switch (form.repeatType.value){
		   		case "2" : //日(HH:mm)
		   			document.getElementById("setisRep").checked = "checked";
		   			changeType(0);
		   			form.TYPE.value = "2";
		   			sel_change();
		   			form.dayReplayStart.value = RepS.substring(RepS.indexOf(" "),RepS.length-5);
		   			form.dayReplayEnd.value = RepE.substring(RepE.indexOf(" "),RepE.length-5);
		   			break;
				case "3" : //周(HH:mm)
					document.getElementById("setisRep").checked = "checked";
		   			changeType(0);
		   			form.TYPE.value = "3";
		   			sel_change();
		   			form.weekReplayStart.value = RepS.substring(RepS.indexOf(" "),RepS.length-5);
		   			form.weekReplayEnd.value = RepE.substring(RepE.indexOf(" "),RepE.length-5);
					break;
				case "4" : //月(dd号 HH:mm)
					document.getElementById("setisRep").checked = "checked";
		   			changeType(0);
		   			form.TYPE.value = "4";
		   			sel_change();
		   			form.monReplayStart.value = RepS.substring(8,RepS.length-5).replace(" ","号 ");
		   			form.monReplayEnd.value = RepE.substring(8,RepE.length-5).replace(" ","号 ");
					break;
				case "5" : //年(MM-dd HH:mm)
					document.getElementById("setisRep").checked = "checked";
		   			changeType(0);
		   			form.TYPE.value = "5";
		   			sel_change();
		   			form.yearReplayStart.value = RepS.substring(5,RepS.length-5);
		   			form.yearReplayEnd.value = RepE.substring(5,RepE.length-5);
					break;
				default : //不循环
					break;
			}
		 }
	</script>
	</head>
	<base target="_self"/>
	<body onload="">
	<DIV id=contentborder align=center>
	<a id="reload" style="display: none" href=""></a>
	<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
	<s:form action="/calendar/calendar!save.action" name="form" method="post" enctype="multipart/form-data">
	<input type="hidden" id="calendarId" name="model.calendarId" value="${model.calendarId}">
	<input type="hidden" id="remindData" name="remindData" value="${remindData}">
	<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
	<input type="hidden" id="delRemindIds" name="delRemindIds" value="">
	<input type="hidden" id="calCon" name="model.calCon" value="${model.calCon}">
	<input type="hidden" id="ifRemind" name="ifRemind" value="${ifRemind}">
	<input type="hidden" id="ifleader" name="ifleader" value="${ifleader}">
	<input type="hidden" id="isLeader" name="model.isLeader" value="${model.isLeader}">
	<input type="hidden" id="repeatType" name="model.repeatType" value="${model.repeatType}">
	<input type="hidden" id="calRepeatStime" name="model.calRepeatStime" value="${model.calRepeatStime}">
	<input type="hidden" id="calRepeatEtime" name="model.calRepeatEtime" value="${model.calRepeatEtime}">
	<div id="desc" style="display: none">${model.calCon}</div>
	<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
		<table width="100%" border="0" cellspacing="0" cellpadding="00">
				      	<tr>
								<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
														<tr>
															<td class="table_headtd_img" >
																<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
															</td>
															<td align="left">
															<strong>查看日程</strong>
															</td>
															<td align="right">
																<table border="0" align="right" cellpadding="00" cellspacing="0">
													                <tr>
													                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
													                 	<td class="Operation_input1" onclick="windowclose();">&nbsp;关&nbsp;闭&nbsp;</td>
													                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
												                  		<td width="6"></td>
													                </tr>
													            </table>
															</td>
														</tr>
									 </table>
								</td>
							</tr>
		<tr>
		<td valign="top" align="left" width="70%">
			<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
				<tr>
					<td class="biao_bg1" align="right">
						<span class="wz">共享人：</span>
					</td>
					<td class="td1">
					<span class="wz">${model.calUserName}</span>
					</td>
				</tr>
				<tr>
					<td class="biao_bg1"align="right">
						<span class="wz">主题内容：</span>
					</td>
					<td class="td1">
						<span class="wz">${model.calTitle}</span>
					</td>
				</tr>
				<tr>
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">活动分类：</span>
					</td>
					<td class="td1">
					<span class="wz">${model.toaCalendarActivity.activityName}</span>
					</td>
				</tr>
				<tr id="onceTaskTime" style="display:" >
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">时间：</span>
					</td>
					<td class="td1">
						<s:date name="model.calStartTime" format="yyyy-MM-dd HH:mm" nice="false"/>
					<span class="wz"> 到:</span>
						<s:date name="model.calEndTime" format="yyyy-MM-dd HH:mm" nice="false"/>
					</td>
				</tr>
				<tr>
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">地点：</span>
					</td>
					<td class="td1">
						<span class="wz">${model.calPlace}</span>
					</td>
				</tr>
				<tr>
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">附件：</span>
					</td>
					<td class="td1">
						<div>
						${attachFiles}
						</div>
					</td>
				</tr>
				<tr>
					<td nowrap class="biao_bg1" align="right" valign="top" >
						<span class="wz">说明：</span>
					</td>
					<td class="td1" style="margin-left: 14px;margin-right: 4px;margin-top: 4px">
						<div id="ArticleContent" style="width: 100%">
						</div>
					</td>
				</tr>
					<script>
						document.getElementById("ArticleContent").innerHTML = document.getElementById("desc").innerText;
					</script>
						
						<%--<script type="text/javascript" src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
						<script type="text/javascript">
							var oFCKeditor = new FCKeditor( 'content' );
							oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
							oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
							oFCKeditor.Width = '100%' ;
                            oFCKeditor.Height = '260' ;
							oFCKeditor.Value = document.getElementById("desc").innerText;
							oFCKeditor.Create() ;
						</script>
					--%>
				
				<%--<tr>
					<td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<span class="wz"> 是否为周期循环日程</span>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" id="setisRep" name="isRep" value = "0" onclick=" ">是&nbsp;&nbsp;
						<input type="radio" name="isRep" value = "1" checked onclick=" "> 不是
					</td>
				</tr>
				<tr id="selremind" style="display:none" > 
					<td nowrap class="biao_bg1"  align="right">
						<span class="wz">重复周期：</span>
					</td>
					<td class="td1">&nbsp;
						<select name="TYPE" class="BigSelect" onchange="sel_change()">
							<option value="2">
								按日重复
							</option>
							<option value="3">
								按周重复
							</option>
							<option value="4">
								按月重复
							</option>
							<option value="5">
								按年重复
							</option>
						</select>
					</td>
				</tr>
				<tr id="day" style="display:none">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">活动时间段：</span>
					</td>
					<td class="td1" >
						&nbsp;每天的&nbsp;
						<strong:newdate name="dayReplayStart" id="dayReplayStart" dateform="HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;开始
						到&nbsp;
						<strong:newdate name="dayReplayEnd" id="dayReplayEnd" dateform="HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;结束
					</td>
				</tr>
				<tr id="week" style="display:none">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">活动时间段：</span>
					</td>
					<td class="td1">&nbsp;每周的&nbsp;
						<select name="REMIND_DATE3" id="week_s" class="BigSelect">
							<option value="1">
								星期一
							</option>
							<option value="2">
								星期二
							</option>
							<option value="3">
								星期三
							</option>
							<option value="4">
								星期四
							</option>
							<option value="5">
								星期五
							</option>
							<option value="6">
								星期六
							</option>
							<option value="0">
								星期日
							</option>
						</select>
						&nbsp;&nbsp;
						<strong:newdate name="weekReplayStart" id="weekReplayStart" dateform="HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;开始
						<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;
						<select name="REMIND_DATE3" id="week_e" class="BigSelect">
							<option value="1">
								星期一
							</option>
							<option value="2">
								星期二
							</option>
							<option value="3">
								星期三
							</option>
							<option value="4">
								星期四
							</option>
							<option value="5">
								星期五
							</option>
							<option value="6">
								星期六
							</option>
							<option value="0">
								星期日
							</option>
						</select>
						&nbsp;&nbsp;
						<strong:newdate name="weekReplayEnd" id="weekReplayEnd" dateform="HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;结束
					</td>
				</tr>
				<tr id="mon" style="display:none">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">活动时间段：</span>
					</td>
					<td class="td1">&nbsp;每月的&nbsp;
						<strong:newdate name="monReplayStart" id="monReplayStart" dateform="dd号 HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;开始
						<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;
						<strong:newdate name="monReplayEnd" id="monReplayEnd" dateform="dd号 HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;结束
					</td>
				</tr>
				<tr id="year" style="display:none">
					<td nowrap class="biao_bg1" align="right">
						<span class="wz">活动时间段：</span>
					</td>
					<td class="td1">
						&nbsp;每年的&nbsp;
						<strong:newdate name="yearReplayStart" id="yearReplayStart" dateform="MM-dd HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;开始
						<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;到&nbsp;
					<strong:newdate name="yearReplayEnd" id="yearReplayEnd" dateform="MM-dd HH:mm"
							width="100px" skin="whyGreen" isicon="true"></strong:newdate>
						&nbsp;&nbsp;结束
					</td>
				</tr>
				
				<tr>
					<td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
						<span class="wz"> 活动提醒选项：</span>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" id="isRemind_yes" name="isRemind" value="yes" checked="" onclick=" ">提醒&nbsp;&nbsp;
						<input type="radio" id="isRemind_no" name="isRemind" value="no" checked="" onclick=" "> 不提醒
					</td>
				</tr>
				<tr id="setRemind" style="display:none" >
					<td nowrap class="biao_bg1" align="right" valign="top">
						<span class="wz">设置提醒：</span>
					</td>
					<td class="td1" id="remindContent">
						&nbsp;<input type="button" value="添加提醒" class="input_bg" onclick="setRemind();">
					</td>
				</tr>
				
				<tr>
					<td style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);" colspan="2">
						<span class="wz"> 日程共享给...</span>&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="radio" name="isShare" id="isShare_yes" value = "yes" onclick=" ">
								共享
							&nbsp;&nbsp;
						<input type="radio" name="isShare" id="isShare_no" value = "no" checked onclick=" ">
								不共享
					</td>
				</tr>
				<tr id="isShare_sel" style="display:none">
					<td height="10%" class="biao_bg1" align="right" valign="bottom" >
						<span class="wz">选择用户：</span>
					</td>
					<td valign="top" class="td1">
						<div style="margin-left: 14px">
						<s:textarea cols="30" id="orgusername" name="shareToUserNames"   rows="4" readonly="true"></s:textarea>
						<input type="hidden" id="orguserid" name="shareToUserIds" value="${shareToUserIds}"></input>
						</div>
						</td>
				</tr>
				--%>
			</table>
		</td>
		</tr>
	</table>
	</s:form>
	</DIV>
	</body>
</html>