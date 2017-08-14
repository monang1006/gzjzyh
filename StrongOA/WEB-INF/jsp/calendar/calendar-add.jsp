<html>
	<head>
	<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
	<%@ taglib uri="/struts-tags" prefix="s"%>
	<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
	<%@include file="/common/include/rootPath.jsp"%> 
	<%@include file="/common/include/meta.jsp" %>
		<title>
		<s:if test="model.calendarId != null && model.calendarId !=''">
			编辑日程安排
		</s:if>
		<s:else>
			新建日程安排
		</s:else>
		</title>
	<% 
		response.setHeader("Cache-Control","no-store");
		response.setHeader("Pragrma","no-cache");
		response.setDateHeader("Expires",0);
		String remindData = request.getParameter("remindData");
	%>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<style type="text/css">
			 body, tr, td,div{
			    margin:0px;
			}
			#MultiFile1_wrap_labels div a{
			 color:blue;}
			
		#MultiFile1_wrap_labels div{
			
		    color:blue;
			}
		#MultiFile1_wrap_labels div span{
		color:#000;}
		</style>
		<script type="text/javascript">
		
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
            
		//提醒记数
		var remindCount=0;
		//提醒数据
		var remindContent="";
		//删除的提醒id
		var delremindIds="";
		
		//是否为周期循环日程
		function changeType(cal_type){
			if(0==cal_type){
				var s = $("#calStartTime").val();
				 var e = $("#calEndTime").val();
				 if(s!=null&&s!=""&&e!=null&&e!=""){
					 var ss = s.split(" ");
					 var ee = e.split(" ");
					 var sss = ss[0].split("-");
					 var eee = ee[0].split("-");
					 if(sss[0]<eee[0]){
					 }else if(sss[0]==eee[0]){
						 if(sss[1]<eee[1]){
						 }else if(sss[1]==eee[1]){
							 if(eee[2]-sss[2]>=7){
							 }else{
								 var sh = ss[1].split(":"); 
								 var eh = ee[1].split(":"); 
								 if(eee[2]-sss[2]<1){
									 alert("活动时间为一天内，不需要重复提醒。");
									 $("#isRep").attr("checked","checked");
									 $("#selremind").hide();
									 $("#day").hide();
									 return;
								 }else if(eee[2]-sss[2]==1){
									 if(eh[0]-sh[0]<=0){
										 alert("活动时间为一天内，不需要重复提醒。");
										 $("#isRep").attr("checked","checked");
										 $("#day").hide();
										 return;
									 }
								 }
							 }
						 }
					 } 
				 }
			}
			viewShow();
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
		   form.repeatType.value = form.TYPE.value;
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
				document.getElementById("isRemind_no").checked="";
				document.getElementById("isRemind_yes").checked="checked";
			}else{
				setRemind.style.display="none";
				ifRemind.value = 0 ;
				document.getElementById("isRemind_no").checked="checked";
				document.getElementById("isRemind_yes").checked="";
			}
		}
		
		//是否作为领导日程进行发布
		function changePubLeader(isleader){
			document.getElementById("isLeader").value = isleader;
			if(isleader=="0"){
				document.getElementById("ifPubleader_yes").checked="";
			 	document.getElementById("ifPubleader_no").checked="checked";
			}else{
				document.getElementById("ifPubleader_yes").checked="checked";
			 	document.getElementById("ifPubleader_no").checked="";
			}
		}
		
		//关闭窗口
		function windowclose(){
		window.close();
		}
		
		//提交表单
		function save(){
			//标题验证
			var calTitle = document.getElementById("calTitle");
			if(""==calTitle.value.trim()|null==calTitle.value.trim()){
				alert("请输入活动主题。");
				calTitle.focus();
				return;
			}

			//获取说明内容
			getCalContent(form);
			
			//获取提醒内容
			if(document.getElementById("ifRemind").value=="1"){
				if(!getRemindContent()){
					if(confirm("未设置提醒选项，确定不提醒？")){
						inRemind_change("no");
					}else{
						document.getElementById("remBTN").click();
						return ;
					}
				}
			}
			
			//获取循环日程信息
			if(document.getElementById("setisRep").checked){
				var res = getCalRepeatType();
				if(res==null|""==res){
				}else{
					alert(res);
					return ;
				}
			}else{
				form.repeatType.value= 0 ;
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
            
            //是否共享给用户
<%--            var shareTo = document.getElementById("shareToUserIds");--%>
<%--            if(document.getElementById("isShare_no").checked){--%>
<%--            	shareTo.value = "";--%>
<%--            }else{--%>
<%--            	if(""==shareTo.value|null==shareTo){--%>
<%--            		if(confirm("未设置共享选项，确定不共享？")){--%>
<%--						inRemind_change("no");--%>
<%--					}else{--%>
<%--						document.getElementById("addPerson").click();--%>
<%--						return ;--%>
<%--					}--%>
<%--            	}--%>
<%--            }--%>
            
			//时间验证
			var stime = form.calStartTime.value;
			var etime = form.calEndTime.value;
			if(stime==null|stime==""|etime==null|etime==""){
				alert("请输入活动的时间安排。");
				return;
			}else{
				if(date2string(stime)>=date2string(etime)){
					alert("活动开始时间不能比结束时间晚。");
					return;
				}
			}
			
			$("#activityId").val($("#toaCalendarActivity").val());
			form.submit();
		}
	$(document).ready(function() {
		
		$("#calStartTime").blur(function(){
			viewShow();
		});
		$("#calEndTime").blur(function(){
			viewShow();
		});
		var message = $(".actionMessage").text();
		if(message!=null && message!=""){
			if(message.indexOf("error")>-1){
				message = message.replace("error","");
				alert(message);
			}else{
				if(confirm("日程活动保存成功，是否继续添加活动安排？")){
					var reload = document.getElementById("reload");
					reload.href="<%=path%>/calendar/calendar!input.action?ifleader="+document.getElementById("ifleader").value;
	         		reload.click();
				}else{
					var act = document.getElementById("toaCalendarActivity");
					window.returnValue = "reload"+act.options.value+","+act.options[act.selectedIndex].text;
					window.close();
				}
			}
		}
		
		//共享文件时选择接收人
		$("#addPerson").click(function(){
			var ret=OpenWindow(this.url,"600","400",window);
		});
		
		//清空接收人
		$("#clearPerson").click(function(){
			$("#orgusername").val("");
			$("#orguserid").val("");
		});
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
							return "活动每次重复开始时间不能比结束时间晚。";
					}else if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空。";
					}else if(form.week_s.value==form.week_e.value){
						s = s.replace(":","");
						e = e.replace(":","");
						if(s>=e){
							return "活动每次重复开始时间不能比结束时间晚。";
						}
					}
					var sdate = nowTime.getDate()-nowTime.getDay()+parseInt(form.week_s.value);
					var edate = nowTime.getDate()-nowTime.getDay()+parseInt(form.week_e.value);
					
					var repSD = new Date();
					repSD.setDate(sdate);
					var repED = new Date();
					repED.setDate(edate);
					
					form.calRepeatStime.value = repSD.getYear()+"-"+(repSD.getMonth()+1)+"-"+repSD.getDate()+" "+form.weekReplayStart.value+":00";
					form.calRepeatEtime.value = repED.getYear()+"-"+(repED.getMonth()+1)+"-"+repED.getDate()+" "+form.weekReplayEnd.value+":00";
					form.repStart.value = form.calRepeatStime.value+".0";
					form.repEnd.value = form.calRepeatEtime.value;+".0"
					break;
				case "4" : //月(dd号 HH:mm)
					var s = form.monReplayStart.value;
					var e = form.monReplayEnd.value;
					if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空。";
					}else{
						s = s.replace("号","").replace(":","");
						e = e.replace("号","").replace(":","");
						if(s>=e){
							return "活动每次重复开始时间不能比结束时间晚。";
						}
					}
					form.calRepeatStime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+form.monReplayStart.value.replace("号","")+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+form.monReplayEnd.value.replace("号","")+":00";
					form.repStart.value = form.calRepeatStime.value+".0";
					form.repEnd.value = form.calRepeatEtime.value+".0";
					break;
				case "5" : //年(MM-dd HH:mm)
					var s = form.yearReplayStart.value;
					var e = form.yearReplayEnd.value;
					if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空。"
					}else{
						s = s.replace("-","").replace(" ","").replace(":","");
						e = e.replace("-","").replace(" ","").replace(":","");
						if(s>=e){
							return "活动每次重复开始时间不能比结束时间晚。"
						}
					}
					form.calRepeatStime.value = nowTime.getYear()+"-"+form.yearReplayStart.value+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+form.yearReplayEnd.value+":00";
					form.repStart.value = form.calRepeatStime.value+".0";
					form.repEnd.value = form.calRepeatEtime.value+".0";
					break;
				default : //日(HH:mm)
					form.repeatType.value = "2";
					var s = form.dayReplayStart.value;
					var e = form.dayReplayEnd.value;
					if(s==null|""==s|e==null|""==e){
						return "活动重复时间段不能为空。"
					}else{
						s = s.replace(":","");
						e = e.replace(":","");
						if(s>=e){
							return "活动每次重复开始时间不能比结束时间晚。"
						}
					}
					form.calRepeatStime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+nowTime.getDate()+" "+form.dayReplayStart.value+":00";
					form.calRepeatEtime.value = nowTime.getYear()+"-"+(nowTime.getMonth()+1)+"-"+nowTime.getDate()+" "+form.dayReplayEnd.value+":00";
					form.repStart.value = form.calRepeatStime.value+".0";
					form.repEnd.value = form.calRepeatEtime.value+".0";
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
         	var ifleader = document.getElementById("ifleader").value;
         	//参数
         	var calendarId = document.getElementById("calendarId").value;
         	var calTitle = document.getElementById("calTitle").value;
         	var calStartTime = document.getElementById("calStartTime").value;
         	var calEndTime = document.getElementById("calEndTime").value;
         	var calPlace = document.getElementById("calPlace").value;
         	var acontent = FCKeditorAPI.GetInstance('content').GetXHTML();
         	reload.href="<%=path%>/calendar/calendar!refreshinput.action?activityId="+activityId+"&ifleader="+ifleader+"&model.calStartTime="+calStartTime+"&model.calEndTime="+calEndTime
         					+"&model.calendarId="+calendarId+"&model.calTitle="+encodeURI(encodeURI(calTitle))+"&model.toaCalendarActivity.activityId="+activityId
         					+"&model.calCon="+encodeURI(encodeURI(acontent))+"&model.calPlace="+encodeURI(encodeURI(calPlace));
         	reload.click();
         }
         //新建活动分类
         function addNewActivity(){
         	var newActivityName = document.getElementById("newActivityName");
         	if(newActivityName.value.trim()==null|newActivityName.value.trim()==""){
         		alert("请输入分类名称。");
         		return ;
         	}else if(newActivityName.value.trim().length>64){
         	
         		alert("分类名称超过最大字数，请控制在64个汉字以内。");
         		return ;
         	} else{
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
		                    		if(activityId=="exist"){
		                    			alert("该分类已存在。");
		                    		}else{
		                    			var activ = activityId.split(",");
						  				activityId = activ[0];
			                    		refreshDial(activityId);
		                    		}
		                    	}else{alert("不能添加分类。");}
	                    	}
	                }
	            );
         	}
         }
         
         //设置提醒
         function setRemind(){
         	var caltitle = document.getElementById("calTitle").value;
         	caltitle = encodeURIComponent(encodeURIComponent(caltitle));
         	var calStartTime = document.getElementById("calStartTime").value;
         	var calEndTime = document.getElementById("calEndTime").value;
         	var url = "<%=path%>/fileNameRedirectAction.action?toPage=calendar/calendarReminds-input.jsp?title="+caltitle+"&stime="+calStartTime+"&etime="+calEndTime;
			var a = window.showModalDialog(url,window,'dialogWidth:550px;dialogHeight:230px;help:no;status:no;scroll:no');
			if((typeof a)!="undefined"){
				var remindHtml = document.getElementById("remindContent");
				a="id"+remindCount+","+a;
				//添加提醒数据
				if(remindContent!=""&&remindContent!=null){
					remindContent+=";"
				}
				remindContent+=a;
				
				var arr=a.split(',');
				arr[2] = arr[2].replace("msg","内部消息");
				arr[2] = arr[2].replace("mail","电子邮件");
				arr[2] = arr[2].replace("rtx","即时通讯");
				arr[2] = arr[2].replace("sms","手机短信");
				
				var remShare = ""
				if("1"==arr[4]|1==arr[4]){
					remShare="是";
				}else{
					remShare="否";
				}
				
				remindHtml.innerHTML+="<div id="+arr[0]+" style=\"display: \">&nbsp;"
						+arr[1]+" 发送提醒（"+arr[2]
						+"），提醒共享人："+remShare+" <a  onclick='delremind(\""+arr[0]+"\");' class='button' >删除</a>";
<%--						+"），提醒共享人："+remShare+" <input type='button' value='删除' class='input_bg' onclick='delremind(\""+arr[0]+"\");'><hr></div>";--%>
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
         
         	if(confirm("删除此提醒设置选项，确定？")){
	         	var remindDiv = document.getElementById(id);
	         	var tempRemind=remindContent;
	         	var res="";
	         	var as = tempRemind.split(';');
	         	for(var i=0;i<as.length;i++){
	         		if(as[i].indexOf(id)!=0){
	         			res+=as[i]+";";
	         		}
	         	}
	         	
	         	remindDiv.style.display="none";
	         	remindContent = res.substring(0,res.length-1);
         	}
         
         }
         
         //获取提醒数据
         function getRemindContent(){
       		document.getElementById("remindData").value = remindContent;
         	if(""==remindContent.trim()|null==remindContent.trim()){
         		return false;
         	}else{
         		return true;
         	}
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
				 	if(remindData.length>0&&remindData.substring(remindData.length-1,remindData.length)==";"){
	            		remindData = remindData.substring(0,remindData.length-1);
	            	}
				 	//添加提醒数据
				 	var remindHtml = document.getElementById("remindContent");
				 	var as = remindData.split(';');
		         	for(var i=0;i<as.length;i++){
						var arr=as[i].split(',');
						if(arr[2]=="0"){arr[2]="站内消息";}
						if(arr[2]=="1"){arr[2]="电子邮件";}
						
						arr[2] = arr[2].replace("msg","内部消息");
						arr[2] = arr[2].replace("mail","电子邮件");
						arr[2] = arr[2].replace("rtx","即时通讯");
						arr[2] = arr[2].replace("sms","手机短信");
						
						if(arr[1].length>0){
		            		arr[1] = arr[1].substring(0,arr[1].length-2);
		            	}
		            	
		            	var remShare = ""
		            	if("1"==arr[4]|1==arr[4]){
							remShare="是";
						}else{
							remShare="否";
						}
		            	
						remindHtml.innerHTML+="<div id="+arr[0]+" style=\"display: \">&nbsp;"
								+arr[1]+" 发送提醒（"+arr[2]
							 +"），提醒共享人："+remShare+" <a  onclick='delremind(\""+arr[0]+"\");' class='button' >删除</a>";
								+"），提醒共享人："+remShare+" <input type='button' value='删除' class='input_bg' onclick='delDBremind(\""+arr[0]+"\");'><hr></div>";
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
		   	var shareUser = '${shareToUserIds}';
		   	if(shareUser!="[]"&&shareUser!=""&&shareUser!=null){
		   		document.getElementById("isShare_yes").checked = "checked";
		   		isShare_change("yes");
		   	}
		   //循环
		   var repeatType = '${model.repeatType}';
		   var RepS = "${repStart}";
		   var RepE = "${repEnd}";
		   
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
		   			
		   			var testDate1 = new Date();
					var t1 = RepS.split(" ")[0].split("-");
					testDate1.setYear(parseInt(t1[0]));
					testDate1.setMonth(parseInt(t1[1])-1);
					testDate1.setDate(t1[2]);
		   			form.week_s.value = testDate1.getDay();
					
					var testDate2 = new Date();
		   			var t2 = RepE.split(" ")[0].split("-");
					testDate2.setYear(parseInt(t2[0]));
					testDate2.setMonth(parseInt(t2[1])-1);
					testDate2.setDate(t2[2]);
		   			form.week_e.value = testDate2.getDay();
					
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
			
			//分类
			var activityId = document.getElementById("activityId").value;
			var act = document.getElementById("toaCalendarActivity");
			if(""!=activityId&&null!=activityId){
				act.options.value = activityId;
			}
			
			document.getElementById("calTitle").focus();
		 }
		 function viewShow(){
		//	 $("#setisRep").attr("checked",true);
		   if($("#setisRep").attr("checked")){
				$("#setisRep").click();
		   }
			// $("#selremind").hide();
			 var s = $("#calStartTime").val();
			 var e = $("#calEndTime").val();
			 if(s!=null&&s!=""&&e!=null&&e!=""){
				 var ss = s.split(" ");
				 var ee = e.split(" ");
				 var sss = ss[0].split("-");
				 var eee = ee[0].split("-");
				 //alert(sss[0])
				 //alert(eee[0])
				 if(sss[0]<eee[0]){
					 $("#TYPE").html("");
					 $("#TYPE").html("<option value=\"2\">按日重复</option>" +
					 					"<option value=\"3\">按周重复</option>" +
					 					"<option value=\"4\">按月重复</option>" +
					 					"<option value=\"5\">按年重复</option>");
				 }else if(sss[0]==eee[0]){
					 if(sss[1]<eee[1]){
						 $("#TYPE").html("");
						 $("#TYPE").html("<option value=\"2\">按日重复</option>" +
					 					"<option value=\"3\">按周重复</option>" +
					 					"<option value=\"4\">按月重复</option>");
					 }else if(sss[1]==eee[1]){
						 if(eee[2]-sss[2]>=7){
							 $("#TYPE").html("");
							 $("#TYPE").html("<option value=\"2\">按日重复</option>" +
					 					"<option value=\"3\">按周重复</option>");
						 }else{
							 $("#TYPE").html("");
							 $("#TYPE").html("<option value=\"2\">按日重复</option>");
						 }
					 }
				 } 
			 }
			 
			 
			 var remindContent = $("#remindContent").html();
			 if(remindContent.indexOf("发送提醒") != -1){
				 alert("当前已经设置了提醒内容,改变开始时间将删除原来的提醒。");
<%--				 if(confirm("当前已经设置了提醒内容，是否删除？")){--%>
					 $("#remindContent").html("&nbsp;&nbsp;&nbsp;<a  id=\"remBTN\"  onclick=\"setRemind();\" class=\"button\" title=\"点击添加提醒\">添加提醒</a>");
<%--				 }--%>
			 }
		 }
	</script>
	</head>
	<base target="_self"/>
	<body onload="init();">
	<DIV id=contentborder align=center>
	<a id="reload" style="display: none" href=""></a>
	<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>
	<s:form action="/calendar/calendar!save.action" name="form" method="post" enctype="multipart/form-data">
	<input type="hidden" id="activityId" name="activityId" value="${activityId}">
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
	<input type="hidden" id="repStart" name="repStart" value="${repStart}">
	<input type="hidden" id="repEnd" name="repEnd" value="${repEnd}">
	<input type="hidden" id="assignUserId" name="assignUserId" value="${model.assignUserId}">
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
																	<script>
																 var id = document.getElementById("calendarId").value;
																if(id==null|id==""){
																	window.document.write("<strong>新建日程安排</strong>");
																}else{
																	window.document.write("<strong>编辑日程安排</strong>");
																}
																</script>
															</td>
															<td align="right">
																<table border="0" align="right" cellpadding="00" cellspacing="0">
													                <tr>
													                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
													                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
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
		<tr>
			<td>
				<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;活动主题：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="calTitle" name="model.calTitle" type="text" value="${model.calTitle}"
											 size="45" maxlength="45">
								&nbsp;
								
							</td>
						</tr>
						<tr id="ifPubleaderTD" style="display: none">
							<td class="biao_bg1"  align="right">
								<span class="wz">是否公布：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;
								<input type="radio" name="ifPubleader_yes" value = "1" onclick="changePubLeader(this.value)">公布&nbsp;&nbsp;
								<input type="radio" name="ifPubleader_no" value = "0" checked onclick="changePubLeader(this.value)"> 不公布
							</td>
						</tr>
						<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">活动分类：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								<div style="padding-left:5px;">
								<s:select list="activitys" listKey="activityId" listValue="activityName"  headerKey="" headerValue="请选择活动分类"
									id="toaCalendarActivity" name="model.toaCalendarActivity.activityId"/>
								<span>
								<a  onclick="newActivity.style.display='';" class="button" style="margin:0px,0px,3px,0px"  title="单击添加新的活动分类">添加</a>
								</span>
								<span id="newActivity" style="display:none;color:#c0c0c0; ">
								<input id="newActivityName" type="text" value="" title="输入新活动分类名（最多64个汉字）" maxlength="64">
								<a  onclick="addNewActivity();" class="button" title="确定添加" style="color: black;">确认</a>
								<a  onclick="newActivity.style.display='none';" class="button" title="单击添加新的活动分类" style="color: black;">取消</a>
								</span>
								</div>
							</td>
						</tr>
						<tr id="onceTaskTime" style="display:" >
							<td nowrap class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;活动时间：&nbsp;</span>
							</td>
							<td class="td1" title="请输入活动的开始时间和结束时间" style="padding-left:5px;" >
								&nbsp;<strong:newdate name="model.calStartTime" id="calStartTime"  dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.calStartTime}"
									width="200px" skin="whyGreen" isicon="true"></strong:newdate>
							<span class="wz"> - </span>
								&nbsp;<strong:newdate name="model.calEndTime" id="calEndTime"   dateform="yyyy-MM-dd HH:mm:ss" dateobj="${model.calEndTime}"
									width="200px" skin="whyGreen" isicon="true"></strong:newdate>
							</td>
						</tr>
						<tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">活动地点：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input type="text" id="calPlace" name="model.calPlace" value="${model.calPlace}"  
									 size="45" maxlength="45">
							</td>
						</tr>
			            <tr>
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">附件：&nbsp;</span>
							</td>
							<td class="td1" title="每次上传附件总大小不能超过${defAttSize/1024/1024 }M">
								<div style="margin-left: 8px;">		
								<span class="wz" ><font color="gray">每次上传附件大小不能超过${defAttSize/1024/1024 }M</font></span>							
									<input type="file" style="width: 36%;" onkeydown="return false;" class="multi" name="file"/>
									${attachFiles}
								</div>
							</td>
						</tr>
						<tr>
							<td nowrap class="biao_bg1" align="right" valign="top">
								<span class="wz">说明：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:8px;">
								<script type="text/javascript" src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
								<script type="text/javascript">
									var oFCKeditor = new FCKeditor( 'content' );
									oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
									oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
									oFCKeditor.Width = '100%' ;
		                            oFCKeditor.Height = '260' ;
									oFCKeditor.Value = document.getElementById("desc").innerText;
									oFCKeditor.Create() ;
								</script>
							</td>
						</tr>
						
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz">周期循环活动：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;&nbsp;<input type="radio" id="isRep" name="isRep" value = "1" checked onclick="changeType(this.value)"> 否
								<input type="radio" id="setisRep" name="isRep" value = "0" onclick="changeType(this.value);">是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;
								
							</td>
						</tr>
						<tr id="selremind" style="display:none"  title="在这里输入每次循环的开始时间和结束时间"> 
							<td nowrap class="biao_bg1"  align="right">
								<span class="wz">重复周期：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">&nbsp;
								<select id="TYPE" name="TYPE" class="BigSelect" onchange="sel_change()"  >
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
								<span class="wz" ><font color="gray">说明:请注意将【活动时间段】设置在【活动时间】内</font></span>
							</td>
						</tr>
						<tr id="day" style="display:none" title="在这里输入每次循环的开始时间和结束时间"> 
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">活动时间段：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;" >
								&nbsp;每天的&nbsp;
								<strong:newdate name="dayReplayStart" id="dayReplayStart" dateform="HH:mm" 
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;开始 到&nbsp;
								<strong:newdate name="dayReplayEnd" id="dayReplayEnd" dateform="HH:mm" 
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;结束
							</td>
						</tr>
						<tr id="week" style="display:none" title="在这里输入每次循环的开始时间和结束时间"> 
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">活动时间段：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;"  >&nbsp;每周的&nbsp;
								<select name="REMIND_DATE3" id="week_s" class="BigSelect">
									<option value="0">
										星期日
									</option>
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
									
								</select>
								&nbsp;&nbsp;
								<strong:newdate name="weekReplayStart" id="weekReplayStart" dateform="HH:mm"
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;开始&nbsp;到&nbsp;
								<select name="REMIND_DATE3" id="week_e" class="BigSelect">
									<option value="0">
										星期日
									</option>
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
								</select>
								&nbsp;&nbsp;
								<strong:newdate name="weekReplayEnd" id="weekReplayEnd" dateform="HH:mm"
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;结束
							</td>
						</tr>
						<tr id="mon" style="display:none" title="在这里输入每次循环的开始时间和结束时间"> 
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">活动时间段：&nbsp;</span>
							</td>
							<td class="td1">&nbsp;每月的&nbsp;
								<strong:newdate name="monReplayStart" id="monReplayStart" dateform="dd号 HH:mm"
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;开始&nbsp;到&nbsp;
								<strong:newdate name="monReplayEnd" id="monReplayEnd" dateform="dd号 HH:mm"
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;结束
							</td>
						</tr>
						<tr id="year" style="display:none" title="在这里输入每次循环的开始时间和结束时间"> 
							<td nowrap class="biao_bg1" align="right">
								<span class="wz">活动时间段：&nbsp;</span>
							</td>
							<td class="td1">
								&nbsp;每年的&nbsp;
								<strong:newdate name="yearReplayStart" id="yearReplayStart" dateform="MM-dd HH:mm"
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;开始&nbsp;到&nbsp;
							<strong:newdate name="yearReplayEnd" id="yearReplayEnd" dateform="MM-dd HH:mm"
									width="100px" skin="whyGreen" isicon="true"></strong:newdate>
								&nbsp;&nbsp;结束
							</td>
						</tr>
						
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz">是否提醒：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;	<input type="radio" id="isRemind_no" name="isRemind" value="no" checked onclick="inRemind_change(this.value);"> 否
								<input type="radio" id="isRemind_yes" name="isRemind" value="yes" onclick="inRemind_change(this.value);">是&nbsp;&nbsp;
								&nbsp;
							</td>
							
						</tr>
						<tr id="setRemind" style="display:none" >
							<td nowrap class="biao_bg1" align="right" valign="top" >
								<span class="wz">设置提醒：&nbsp;</span>
							</td>
							<td class="td1" id="remindContent" style="padding-left:5px;">
								&nbsp;&nbsp;&nbsp;<a  id="remBTN"  onclick="setRemind();" class="button" title="点击添加提醒">添加提醒</a>
							</td>
						</tr>
						<!--   共享的功能屏蔽掉
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz">是否共享：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;	<input type="radio" name="isShare" id="isShare_no" value = "no" checked onclick="isShare_change(this.value)"> 否
								<input type="radio" name="isShare" id="isShare_yes" value = "yes" onclick="isShare_change(this.value)">是&nbsp;&nbsp;
								&nbsp;
							</td>
						</tr>
						<tr id="isShare_sel" style="display:none" >
							<td height="10%" class="biao_bg1" valign="top" align="right" valign="bottom" >
								<span class="wz">共享给：&nbsp;</span>
							<br><br><br></td>
							<td valign="top" class="td1" style="padding-left:5px;">
								<div style="margin-left: 5px">
								<s:textarea title="双击选择用户" cols="30" id="orgusername" name="shareToUserNames" ondblclick="addPerson.click();"  rows="4" readonly="true"></s:textarea>
								<input type="hidden" id="orguserid" name="shareToUserIds" value="${shareToUserIds}"></input>
								<a id="addPerson" class="button"  url="<%=root%>/address/addressOrg!tree.action" href="#">添加</a>&nbsp;<a id="clearPerson" class="button"   href="#">清空</a>
								</div>
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