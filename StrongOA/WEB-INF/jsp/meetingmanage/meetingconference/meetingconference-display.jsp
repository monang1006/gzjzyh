<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>查看会议</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
			
      	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
      <script language="javascript" src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
      	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" type="text/javascript"></script>
      	<script src="<%=path%>/common/js/fckeditor2/fckeditor.js" type="text/javascript" ></script>
	
		<script type="text/javascript">
	
		 //下载附件
 function download(id){
		 var attachDownLoad = document.getElementById("attachDownLoad");
		 	attachDownLoad.src = "<%=path%>/meetingmanage/meetingtopic/meetingtopic!down.action?attachId="+id;
		 }
		 
function viewNotice(){

	 var id=document.getElementById("conId").value;
	 var status=document.getElementById("conStatus").value;
	 if(status=="0"){
	 	alert("该会议还没写会议通知!");
	 	return;
	 }else{
	 	var width=screen.availWidth/2;
    	var height=screen.availHeight*2/3;
  	 	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingnotice/meetingnotice!display.action?conId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
  	 }
}	
	 
function viewSummary(){

	 var id=document.getElementById("conId").value;
	 var status=document.getElementById("conStatus").value;
	if(status!="2"){
		alert("该会议还没写会议纪要!");
		return ;
	}
	else{ 
		var width=screen.availWidth/2;
    	var height=screen.availHeight*2/3;
		var result=window.showModalDialog("<%=path%>/meetingmanage/meetingsummary/meetingsummary!display.action?conId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
	} 
}
	
function closew(){
	window.close();
}
		     	
</script>
<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
	   <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		
				<table width="100%"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					<tr>
						<td>&nbsp;</td>
						<td width="40%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						查看会议
						</td>
						<td width="*">
							&nbsp;
						</td>
						
						<td >
												<a class="Operation" href="#" onclick="viewNotice()"> <img
														src="<%=root%>/images/ico/chakan.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">查看通知&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="viewSummary()"> <img
														src="<%=root%>/images/ico/shenyue.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">查看纪要&nbsp;</span> </a>
											</td>
											<td width="5"></td>
												<td >
												<a class="Operation" href="#" onclick="closew()">
													<img
														src="<%=root%>/images/ico/guanbi.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">关  闭&nbsp;</span> </a>
											</td>
											
					</tr>
				</table>
				<iframe id="attachDownLoad" src='' style="display:none;border:4px solid #CCCCCC;"></iframe>

				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
				<input type="hidden" id="conId" name="model.conId"
								value="${model.conferenceId}">								
				<input type="hidden" id="conStatus" name="model.conferenceStatus"
								value="${model.conferenceStatus}">
				<input type="hidden" id="conferenceContent" name="model.conferenceContent" value=${model.conferenceContent}>
					<div id="con" style="display: none">
						${model.conferenceContent}
					</div>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议编号：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="conferenceCode" name="model.conferenceCode"
								value="${model.conferenceCode}" readonly="true" type="text" size="55"
								maxlength="16">
						</td>
					</tr>
					<tr>
							<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议主题：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="conferenceName" name="model.conferenceName"
								value="${model.conferenceName}" readonly="true" type="text" size="55"
								maxlength="25">
						</td>
					</tr>
					<tr>
						<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">会议时间：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<strong:newdate name="model.conferenceStime" id="conferenceStime"
								width="175" skin="whyGreen" isicon="true"
								dateobj="${model.conferenceStime}"
								dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>

							&nbsp;&nbsp;&nbsp;&nbsp;--&nbsp;&nbsp;&nbsp;
							<strong:newdate name="model.conferenceEndtime"
								id="conferenceEndtime" width="175" skin="whyGreen" isicon="true"
								dateobj="${model.conferenceEndtime}"
								dateform="yyyy-MM-dd HH:mm:ss"></strong:newdate>
						</td>
					</tr>
					
				<tr>
					<td colspan="1" height="21" class="biao_bg1" align="right">
							<span class="wz">会议地点：</span>
						</td>
						<td colspan="3" class="td1" align="left">
							<input id="conferenceAddr" name="model.conferenceAddr"
								value="${model.conferenceAddr}" readonly="true" type="text" size="55"
								maxlength="25">
						</td>
				</tr>
			
				<tr>
					<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议议题：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:textarea cols="45" id="topSubject" name="topSubject" readonly="true"
								rows="4" readonly="true"></s:textarea>
							<input type="hidden" name="topId" id="topId"
								value="${topId}"></input>
						</td>
				</tr>
					
					<tr>
						<td width="15%" height="21" class="biao_bg1" align="right">
							<span class="wz">会议描述：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="conferenceDemo" name="model.conferenceDemo"
								value="${model.conferenceDemo}" readonly="true" type="text" size="55"
								maxlength="50">
						</td>
					</tr>
				
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">会议内容：</span>
						</td>
						<td class="td1" colspan="3" align="left" >
							<script type="text/javascript"
								src="<%=request.getContextPath()%>/common/js/fckeditor2/fckeditor.js">
							</script>

							<script type="text/javascript">													 
								var oFCKeditor = new FCKeditor( 'content' );
								oFCKeditor.BasePath	= '<%=request.getContextPath()%>/common/js/fckeditor2/'
								oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
								oFCKeditor.Width = '85%' ;
								oFCKeditor.align = 'left';
                                oFCKeditor.Height = '280' ;								
								oFCKeditor.Value= document.getElementById("con").innerText;
								oFCKeditor.Create() ;													 
                             </script>
						</td>
					</tr>
					
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">会议附件：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							&nbsp;&nbsp;
							<input type="file" onkeydown="return false;" name="upload"
								class="multi required" style="width: 63%;" />
							${attachFiles}
						</td>
						</td>
					</tr>
				
				</table>
				
			
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
						<table width="27%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td width="10%" height="34">
									&nbsp;
								</td>
								<td width="30%">
									<input name="Submit2" type="button" class="input_bg"
										value="关  闭" onclick="window.close();">
								</td>
							</tr>
						</table>
					
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
