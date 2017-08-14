<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@taglib uri="/tags/web-remind" prefix="stron"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>会议通知管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<script language="javascript"
			src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
	

		<script type="text/javascript">

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
      /*
       function showimg(topicSubject){ 
       if (conferenceId!=null) 
       return
       }    */
         
	function formsubmit(){
		 var inputDocument=document;
		 var inputDocument=document;
		 var noticeTitle= $.trim($("#noticeTitle").val());
		 
		 noticeTitle = noticeTitle.replace(new RegExp("\"","gm"),"“");
		 noticeTitle = noticeTitle.replace(/[\']/gm,"’");
		 noticeTitle = noticeTitle.replace(new RegExp("\n","gm")," ");
		 noticeTitle = noticeTitle.replace(new RegExp("\r","gm"),"");
		 noticeTitle = noticeTitle.replace(new RegExp("<","gm"),"＜");
		 noticeTitle = noticeTitle.replace(new RegExp(">","gm"),"＞");
		 noticeTitle = noticeTitle.replace(/[\\]/gm, "＼");
		 $("#noticeTitle").val(noticeTitle);
    if(inputDocument.getElementById("noticeTitle").value==""){
    	alert("通知标题不能为空，请输入。");
    	inputDocument.getElementById("noticeTitle").focus();
    	return false;
    }
   
    if(inputDocument.getElementById("noticeStime").value==""){
    	alert("请选择会议开始时间。");
    	inputDocument.getElementById("noticeStime").focus();
    	return false;
    }
    if(inputDocument.getElementById("noticeEndTime").value==""){
    	alert("请选择会议结束时间。");
    	inputDocument.getElementById("noticeEndTime").focus();
    	return false;
    }
    var stime=inputDocument.getElementById("noticeStime").value;
    var etime=inputDocument.getElementById("noticeEndTime").value;
    if(date2string(stime)>=date2string(etime)){
			alert("会议开始时间不能比结束时间晚！");
					return false;
				}
				
    if(inputDocument.getElementById("noticeAddr").value==""){
    	alert("会议地点不能为空，请输入。");
    	inputDocument.getElementById("noticeAddr").focus();
    	return false;
    }
    if(inputDocument.getElementById("orgusername").value==""){
    	alert("参会人员不能为空，请输入。");
    	inputDocument.getElementById("orgusername").focus();
    	return false;
    }
		//获取被删除的附件id
		var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
          	}
          document.getElementById("delAttachIds").value = delAttachIds;
          document.forms(0).action="<%=path%>/meetingmanage/meetingnotice/meetingnotice!save.action";        
		  document.forms[0].submit();			
		
		}
	function cancel(){
		window.close();
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
		 	attachDownLoad.src = "<%=path%>/meetingmanage/meetingnotice/meetingnotice!down.action?attachId="+id;
		 }
		 
		  //获取提醒方式
	function getRemindValue(){
				var returnValue = "";
				$("#StrRem").find("input:checkbox:checked").each(function(){
					returnValue = returnValue + $(this).val() + ",";
				});
				if(returnValue!=""){
					returnValue = returnValue.substring(0,returnValue.length-1);
				}
				return returnValue;
			}	
		 
function sendNotice(){

 var inputDocument=document;
   if(inputDocument.getElementById("noticeTitle").value==""){
    	alert("通知标题不能为空，请输入。");
    	inputDocument.getElementById("noticeTitle").focus();
    	return false;
    }
   
    if(inputDocument.getElementById("noticeStime").value==""){
    	alert("请选择会议开始时间。");
    	inputDocument.getElementById("noticeStime").focus();
    	return false;
    }
    if(inputDocument.getElementById("noticeEndTime").value==""){
    	alert("请选择会议结束时间。");
    	inputDocument.getElementById("noticeEndTime").focus();
    	return false;
    }
    var stime=inputDocument.getElementById("noticeStime").value;
    var etime=inputDocument.getElementById("noticeEndTime").value;
    if(date2string(stime)>=date2string(etime)){
			alert("会议开始时间不能比结束时间晚！");
					return false;
				}
				
    if(inputDocument.getElementById("noticeAddr").value==""){
    	alert("会议地点不能为空，请输入。");
    	inputDocument.getElementById("noticeAddr").focus();
    	return false;
    }
    if(inputDocument.getElementById("orgusername").value==""){
    	alert("参会人员不能为空，请输入。");
    	inputDocument.getElementById("orgusername").focus();
    	return false;
    }
   
		//获取被删除的附件id
		var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
          	}
          document.getElementById("delAttachIds").value = delAttachIds;
	var sendtype=getRemindValue();
   
	 if(sendtype=="" || sendtype==undefined){
    	alert("请选择发送方式。");
     return false;
  }
	 var notId=document.getElementById("notId").value;
	document.forms[0].action="<%=path %>/meetingmanage/meetingnotice/meetingnotice!send.action?sendtype="+sendtype;
	document.forms[0].submit();	 
	}
	
$(document).ready(function() {
	$("#addCon").click(function(){
		var ret=OpenWindow(this.url,"600","400",window);
	});
	
	 $("#addPerson").click(function(){
		
		var ret=OpenWindow(this.url,"600","400",window);
	
	 });
	
	
});

 
	</script>
		<base target="_self">
	</head>

	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript"
			src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
				<tr>
					<td>&nbsp;</td>
					<td width="40%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						会议通知管理
					</td>
					<td width="60%"><table align="right"><tr>
						<td>
						</td>
						<td>
							<a class="Operation" href="#" onclick="cancel()">
							<img src="<%=root%>/images/ico/guanbi.gif" width="15" height="15" class="img_s">
							<span id="test" style="cursor:hand">关 闭&nbsp;</span> </a>
						</td>
					</tr></table></td>
				</tr>
			</table>
			<iframe id="attachDownLoad" src=''
				style="display: none; border: 4px solid #CCCCCC;"></iframe>
			<s:form
				action="/meetingmanage/meetingnotice/meetingnotice!save.action"
				method="post" enctype="multipart/form-data">
				<input type="hidden" id="notId" name="notId"
					value="${model.noticeId}">
				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">

				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">通知标题(<font color="red">*</font>)：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<input id="noticeTitle" name="model.noticeTitle" type="text"
								value="${model.noticeTitle}" size="55" maxlength="100">
						</td>
					</tr>

					<tr>
						<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">会议时间(<font color="red">*</font>)：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<strong:newdate name="model.noticeStime" id="noticeStime"
								width="138" skin="whyGreen" isicon="true"
								dateobj="${model.noticeStime}" dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>

							&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;
							<strong:newdate name="model.noticeEndTime" id="noticeEndTime"
								width="138" skin="whyGreen" isicon="true"
								dateobj="${model.noticeEndTime}" dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
						</td>

					</tr>

					<tr>
						<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">会议地点(<font color="red">*</font>)：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<input id="noticeAddr" name="model.noticeAddr"
								value="${model.noticeAddr}" type="text" size="55" maxlength="100">
						</td>
					</tr>

					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议主题&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:textfield cols="60" id="conName" name="conName" 
								readonly="true"></s:textfield>
							<input type="hidden" name="conId" id="conId" value="${conId}"></input>
						</td>
					</tr>
					
		
					<tr id="tr_hyyt">
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议议题&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:textarea cols="63" id="topSubject" name="topSubject" readonly="true"
								rows="4" readonly="true"></s:textarea>
							<input type="hidden" name="topSubject" id="topSubject"
								value="${topSubject}"></input>
						</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">参会人员(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:textarea cols="63" id="orgusername" name="userName" rows="4"
								readonly="true"></s:textarea>
							<input type="hidden" name="userId" id="orguserid"
								value="<s:property value='userId'/>"></input>

						</td>
					</tr>
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">发送方式：</span>
						</td>
						<td class="td1" id="StrRem" colspan="3" align="left">
							<stron:remind msgChecked="checked" isOnlyRemindInfo="true" isShowButton="false"
								code="<%=GlobalBaseData.SMSCODE_MEETING %>" />
						</td>
					</tr>
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">附 件&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							&nbsp;&nbsp;
							${attachFiles}
						</td>
					</tr>
				</table>
			</s:form>

		</DIV>
	</body>
</html>
