<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>会议管理</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/fckeditor2/fckeditor.js"
			type="text/javascript"></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
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
   	function topicList(){  
   	 	
   	 	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingtopic/meetingtopic!topicList.action?dt"+ new Date(),window,'help:no;status:no;scroll:no;dialogWidth:720px; dialogHeight:650px');
	}      
	function formsubmit(){
		
		 var inputDocument=document;
    if(inputDocument.getElementById("conferenceCode").value==""){
    	alert("会议编号不能为空，请输入。");
    	inputDocument.getElementById("conferenceCode").focus();
    	return false;
    }
      
    if(inputDocument.getElementById("conferenceName").value==""){
    	alert("会议主题不能为空，请输入。");
    	inputDocument.getElementById("conferenceName").focus();
    	return false;
    }
   
    if(inputDocument.getElementById("conferenceStime").value==""){
    	alert("请选择会议开始时间。");
    	inputDocument.getElementById("conferenceStime").focus();
    	return false;
    }
    if(inputDocument.getElementById("conferenceEndtime").value==""){
    	alert("请选择会议结束时间。");
    	inputDocument.getElementById("conferenceEndtime").focus();
    	return false;
    }
    
     var stime=inputDocument.getElementById("conferenceStime").value;
     var etime=inputDocument.getElementById("conferenceEndtime").value;
    if(date2string(stime)>=date2string(etime)){
			alert("会议开始时间不能比结束时间晚！");
					return false;
				}
    if(inputDocument.getElementById("conferenceAddr").value==""){
    	alert("会议地点不能为空，请输入。");
    	inputDocument.getElementById("conferenceAddr").focus();
    	return false;
    }
	
	var oEditor = FCKeditorAPI.GetInstance('content');
				     var acontent = oEditor.GetXHTML();
		document.getElementById("conferenceContent").value=acontent;
		
		//获取被删除的附件id
			var delAttachIds = document.getElementById("delAttachIds").value;
			if(delAttachIds.length>0){
            		delAttachIds = delAttachIds.substring(0,delAttachIds.length-1);
            	}
            document.getElementById("delAttachIds").value = delAttachIds;
            
       document.forms(0).action="<%=path%>/meetingmanage/meetingconference/meetingconference!save.action";
         document.forms(0).submit();			 
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
		 	attachDownLoad.src = "<%=path%>/meetingmanage/meetingconference/meetingconference!down.action?attachId="+id;
		 }	 
		 		     	
</script>
		<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>

			<table width="100%"
				style="FILTER: progid : DXImageTransform.Microsoft.Gradient (gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
				<tr>
					<td>&nbsp;</td>
					<td width="40%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						会议管理
					</td>
					<td width="60%">
						&nbsp;
					</td>
				</tr>
			</table>
			<iframe id="attachDownLoad" src=''
				style="display: none; border: 4px solid #CCCCCC;"></iframe>
			<s:form
				action="/meetingmanage/meetingconference/meetingconference!save.action"
				method="post" enctype="multipart/form-data">
				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
				<input type="hidden" id="conId" name="model.conferenceId"
					value="${model.conferenceId}">
				<input type="hidden" id="conferenceContent" name="model.conferenceContent">
				<div id="con" style="display: none">
					${model.conferenceContent}
				</div>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议编号(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="conferenceCode" name="model.conferenceCode"
								value="${model.conferenceCode}" type="text" size="45"
								maxlength="16">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议主题(<font color="red">*</font>)：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="conferenceName" name="model.conferenceName"
								value="${model.conferenceName}" type="text" size="45"
								maxlength="25">
						</td>
					</tr>

					<tr>
						<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">会议时间(<font color="red">*</font>)：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<strong:newdate name="model.conferenceStime" id="conferenceStime"
								width="145" skin="whyGreen" isicon="true"
								dateobj="${model.conferenceStime}"
								dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>

							&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;
							<strong:newdate name="model.conferenceEndtime"
								id="conferenceEndtime" width="145" skin="whyGreen" isicon="true"
								dateobj="${model.conferenceEndtime}"
								dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
						</td>

					</tr>

					<tr>
						<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">会议地点(<font color="red">*</font>)：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<input id="conferenceAddr" name="model.conferenceAddr"
								value="${model.conferenceAddr}" type="text" size="45"
								maxlength="25">
						</td>
					</tr>

					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议议题&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:textarea cols="40" id="topSubject" name="topSubject"
								rows="4" readonly="true"></s:textarea>
							<input type="hidden" name="topId" id="topId"
								value="${topId}"></input>
							<input type="button" class="input_bg" value="添 加" onClick="topicList();">
						</td>
					</tr>

					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议描述&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="conferenceDemo" name="model.conferenceDemo"
								value="${model.conferenceDemo}" type="text" size="45"
								maxlength="50">
						</td>
					</tr>

					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">会议内容&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<script type="text/javascript"
								src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js">
							</script>

							<script type="text/javascript">													 
								var oFCKeditor = new FCKeditor( 'content' );
								oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
								oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
								oFCKeditor.Width = '80%' ;
								oFCKeditor.align = 'left';
                                oFCKeditor.Height = '280' ;								
								oFCKeditor.Value= document.getElementById("con").innerText;
								oFCKeditor.Create() ;													 
                             </script>
						</td>
					</tr>

					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">附 件&nbsp;&nbsp;&nbsp;：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							&nbsp;&nbsp;
							<input type="file" onkeydown="return false;" name="upload"
								class="multi required" style="width: 70%;" />
							${attachFiles}
						</td>
					</tr>

				</table>
			</s:form>

			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="25%" align="center">
									<input id="Submit" type="button" class="input_bg"
										onclick="formsubmit();" value="保  存">
								</td>
								
															<td width="25%" align="center">
									<input id="Submit" type="button" class="input_bg"
										onclick="cancel();" value="取  消">
								</td>

							</tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
