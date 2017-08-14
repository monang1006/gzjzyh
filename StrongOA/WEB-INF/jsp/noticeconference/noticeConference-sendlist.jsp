<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>已发送会议通知</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">

		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>

		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script type="text/javascript">
			function qksousuo(){
				$("#searchtitle").attr("value","");
				$("#searchconferenceAddr").attr("value","");
				$("#searchStime").attr("value","");
				$("#searchEtime").attr("value","");
			}
		 </script>
	</HEAD>
<style>
.btn1_mouseout {
	BORDER-RIGHT: #7b9ebd 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #7b9ebd 1px solid;
	PADDING-LEFT: 2px;
	FONT-SIZE: 12px;
	FILTER: progid :                         
		   DXImageTransform.Microsoft.Gradient (        
		       GradientType =     0, StartColorStr =      #ffffff, EndColorStr
		=   
		 #cecfde );
	BORDER-LEFT: #7b9ebd 1px solid;
	CURSOR: hand;
	COLOR: black;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #7b9ebd 1px solid
}

.btn1_mouseover {
	BORDER-RIGHT: #FF0000 1px solid;
	PADDING-RIGHT: 2px;
	BORDER-TOP: #FF0000 1px solid;
	PADDING-LEFT: 2px;
	FONT-SIZE: 12px;
	FILTER: progid :                         
		   DXImageTransform.Microsoft.Gradient (     
		            GradientType =     0, StartColorStr =      #ffffff,
		EndColorStr =      #CAE4B6 );
	BORDER-LEFT: #FF0000 1px solid;
	CURSOR: hand;
	COLOR: black;
	PADDING-TOP: 2px;
	BORDER-BOTTOM: #FF0000 1px solid
}
</style>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
												&nbsp;
											</td>
											<td width="45%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9" align="center">
												&nbsp; 已发送会议通知
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="70%">
												<table align="right">
													<tr>
														<td >
															<a class="Operation" onclick="sendsms()" href="#">
																<img src="<%=root%>/images/ico/message.gif" width="15" height="15" class="img_s">发送短信&nbsp;
															</a>
														</td>
														<td width="5"></td>
														<td>
															<a class="Operation" href="#" onclick="readconference()">
																<img src="<%=root%>/images/ico/chakan.gif" width="15"
																	height="15" class="img_s"><span id="test"
																style="cursor: hand">查看报名情况&nbsp;</span> </a>
														</td>
														<td width="5"></td>
														<td>
															<a class="Operation" href="#" onclick="send()"> <img
																	src="<%=root%>/images/ico/fasong.gif" width="15"
																	height="15" class="img_s"><span id="test"
																style="cursor: hand">通知下发&nbsp;</span> </a>
														</td>


														<td width="5"></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<s:form id="myTableForm" method="get" 
							action="/noticeconference/noticeConference.action">
							<input type="hidden" name="state" value="${state}" />
							<webflex:flexTable name="myTable" width="100%" height="364px"
								wholeCss="table1" property="id" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>

										<td width="33%" class="biao_bg1">
											<input name="searchtitle" id="searchtitle" class="search"
												value="${searchtitle}" type="text" title="请输入会议标题" />
										</td>
										<td width="22%" class="biao_bg1">
											<input name="searchconferenceAddr" id="searchconferenceAddr"
												value="${searchconferenceAddr}" type="text" class="search"
												title="请输入会议地点" />
										</td>
										<td class="biao_bg1" width="20%">
											<strong:newdate name="searchRegendtime" id="searchStime"
												skin="whyGreen" isicon="true" dateobj="${searchRegendtime}"
												dateform="yyyy-MM-dd HH:mm" width="100%" classtyle="search"
												title="开始时间"></strong:newdate>
										</td>
										<td class="biao_bg1" width="20%">
											<strong:newdate name="searchRegendtime" id="searchEtime"
												skin="whyGreen" isicon="true" dateobj="${searchRegendtime}"
												dateform="yyyy-MM-dd HH:mm" width="100%" classtyle="search"
												title="结束时间"></strong:newdate>
										</td>
										<td width="5%" class="biao_bg1">
											<input type='button' id="img_sousuo" class=btn1_mouseout
												onmouseover="this.className='btn1_mouseover'"
												onmouseout="this.className='btn1_mouseout'"value='搜 索' />
										</td>

										<td width="5%" class="biao_bg1">
											&nbsp;
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="conferenceId"
									showValue="conferenceId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>

								<webflex:flexTextCol caption="会议标题" property="conferenceTitle"
									showValue="conferenceTitle" width="36%" isCanDrag="true"
									isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议地点" property="conferenceAddr"
									showValue="conferenceAddr" width="20%" isCanDrag="true"
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<%--	<webflex:flexDateCol caption="开始日期" property="conferenceStime"
									showValue="conferenceStime" width="12%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>

								<webflex:flexDateCol caption="结束日期" property="conferenceEtime"
									showValue="conferenceEtime" width="12%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
									
								<webflex:flexTextCol caption="承办单位" property="conferenceUndertaker"
									showValue="conferenceUndertaker" width="15%" isCanDrag="true"
									isCanSort="true" showsize="30"></webflex:flexTextCol> --%>

								<webflex:flexDateCol caption="报名截止时间"
									property="conferenceRegendtime"
									showValue="conferenceRegendtime" width="15%" isCanDrag="true"
									isCanSort="true" dateFormat="yyyy-MM-dd HH:mm"></webflex:flexDateCol>


							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			<iframe scr='' id='tempframe' name='tempframe' style='display: none'></iframe>


		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
  
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/message.gif","发送短信","sendsms",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看报名情况","readconference",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/fasong.gif","通知下发","send",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	//item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删 除","del",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);

	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//查看报名情况
function readconference(){
	var time = new Date();
	var conferenceId=getValue();
	if(conferenceId==null||conferenceId==""){
		alert("请选择要查看的会议通知！");
	}else{
		if(conferenceId.indexOf(",")!=-1){
			alert("一次只能查看一条会议通知！");
		}else{
			 var width=screen.availWidth-10;
		  	 var height=screen.availHeight-30;
		  	 OpenWindow('<%=path%>/noticeconference/noticeConference!viewReport.action?conNoticeId='+conferenceId+'&state=${state}&time='+time.getTime(), width, height, window);
		  	 
			//OpenWindow('<%=path%>/conference/watingSendConfer/watingSendConfer!editDraft.action?readedit=1&model.conferenceId='+conferenceId, width, height, window);
		}
	}
	callback();
}
 

//会议通知下发
function send(){
	var time = new Date();
	var conferenceId=getValue();
	if(conferenceId==null||conferenceId==""){
		alert("请选择要下发的会议通知！");
	}else{
		if(conferenceId.indexOf(",")!=-1){
			alert("一次只能下发一条会议通知！");
		}else{
		
		    //
			 var width=screen.availWidth*3/4;
		     var height=screen.availHeight*3/4;  		
			 var url="<%=path%>/noticeconference/noticeConference!assignDeptListTree.action?conNoticeId="+conferenceId+"&state=${state}&time="+time.getTime();	  
			 var ret = showDialog(url, width, height);   	
			 if(ret && ret == 'OK'){
				 $("form").submit();
			   } 
		}
	}
	callback();
} 
function callback(){
 if(str=='1'){
         
           window.parent.refreshWorkByTitle('<%=root%>/noticeconference/noticeConference.action?state=1','已发会议通知');
		   window.parent.setSelectedIndex(1);
        }else{
          window.location.href = window.location.href;
        }
		} 
function callback(){
		//alert("添加成功!");
		window.location.href = window.location.href;
	}
	
//删除会议通知
function del(){
	var conferenceId=getValue();
	if(conferenceId==null||conferenceId==""){
		alert("请选择要删除的会议通知！");
	}else{
		if(confirm("删除选中的会议通知，确定?")==true){
			  	$.ajax({
			  		type:"post",
			  		dataType:"text",
			  		url:"<%=root%>/conference/watingSendConfer/watingSendConfer!deleteConIfo.action",
			  		data:"model.conferenceId="+conferenceId,
			  		success:function(msg){
			  			if(msg=="0"){
			  				window.parent.location.reload();
						}else{
							alert("删除失败请您重新删除!");
			  			}
			  		}
			  	});
			}
 	}
}
//导出参会人员 
function exportmen(){
	var conferenceId=getValue();
	if(conferenceId==null||conferenceId==""){
		alert("请选择要导出会议通知！");
	}else{
		if(conferenceId.indexOf(",")!=-1){
			alert("一次只能导出一条会议通知！");
		}else{
			document.getElementById('tempframe').src='<%=root%>/conference/watingSendConfer/watingSendConfer!exportPeople.action?conferenceId='+conferenceId;
		}
	}
}

function quicklyConference(object){
	var conferenceId=getValue();
	window.parent.conference_Information.location='<%=path%>/conference/watingSendConfer/watingSendConfer!getConferenceSend.action?conferenceId='+conferenceId;
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	var conferenceStime=new Date($("#searchStime").val().replace(/-/g,"/"));//开始日期
			var conferenceEtime=new Date($("#searchEtime").val().replace(/-/g,"/"));//结束日期
        	if(conferenceEtime.getTime()-conferenceStime.getTime()<0){
				alert("【开始日期】必须比【结束日期】早，请重新选择！");
				$("#searchStime").attr("value","");
				$("#searchEtime").attr("value","");
				return;
			}
        	$("form").submit();

        });     
      });

//发送短信
function sendsms(){
	var conferenceId=getValue();
	if(conferenceId==null||conferenceId==""){
		alert("请选择要发送信息的会议通知！");
	}else{
	var url = "<%=path%>/sms/sms!noticeinput.action?moduleCode=<%=GlobalBaseData.SMSCODE_SMS %>&conferenceId="+conferenceId;
	var a = window.showModalDialog(url,window,'dialogWidth:400pt;dialogHeight:350pt;help:no;status:no;scroll:no');
		if("reload"==a){
			//location= "<%=path%>/sms/sms!allList.action";
			//alert("已提交服务器发送");
			document.location.reload();
		}
	}
}

</script>
	</BODY>
</HTML>
