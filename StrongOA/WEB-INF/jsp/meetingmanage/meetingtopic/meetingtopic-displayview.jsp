<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp"%>
		<title>查看议题</title>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
			   <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
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
	
		     	
</script>
<base target="_self">
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
		
				<table width="100%"
					style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
					<tr>
						<td>&nbsp;</td>
						<td width="20%">
							<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						查看议题
						</td>
						<td width="80%">
							&nbsp;
						</td>
						
					<td width="5"></td>
					</tr>
				</table>
				<iframe id="attachDownLoad" src="" style="display:none;border:4px solid #CCCCCC;"></iframe>
				<s:form action="" method="post" enctype="multipart/form-data">
				<input type="hidden" id="delAttachIds" name="delAttachIds" value="">
				<input type="hidden" id="topicId" name="model.topicId"
								value="${model.topicId}">
				<input type="hidden" id="meetingtopicId" name="meetingtopicId"
								value="${meetingtopicId}">
			<input id=topicContent name="model.topicContent" value="${model.topicContent}" type="hidden">
				<div id="con" style="display: none">${model.topicContent}</div>
				<table width="100%" height="10%" border="0" cellpadding="0"
					cellspacing="1" align="center" class="table1">
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题编号：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="topicCode" name="model.topicCode"  value="${model.topicCode}" readonly="true" type="text" size="45">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题主题：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input id="topicSubject" name="model.topicSubject" value="${model.topicSubject}" readonly="true" type="text" size="45">
						</td>
					</tr>
					<tr>
						<td width="20%" height="21" class="biao_bg1" align="right">
							<span class="wz">议题分类：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<s:select list="sortList" listKey="topicsortId"
											listValue="topicsortName" headerKey="123" headerValue="请选择分类"
											id="topicsort" name="model.topicsort.topicsortId" style="width:15.5em" disabled="true" />
											
						</td>
					</tr>
					
				
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">议题内容：</span>
						</td>
						<td class="td1" colspan="3" align="left">
						<!--<div id="ArticleContent" style="width: 60%"></div>-->
						<textarea id="ArticleContent" rows="5" cols="30" style="width: 70%" readonly="true"></textarea>
						
					</td>
					</tr>
					<script type="text/javascript">
						document.getElementById("ArticleContent").innerHTML=document.getElementById("con").innerText;
					</script>
					<tr>
						<td height="21" class="biao_bg1" align="right">
							<span class="wz">查看附件：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							&nbsp;&nbsp;<!--  <input type="file" onkeydown="return false;" name="upload" class="multi required" readonly="true" style="width: 63%;"/>
							-->
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
								<td width="10%" height="34">
									&nbsp;
								</td>
								</td>
								<td width="30%">
								<!--  	<input name="Submit" type="button" class="input_bg" onclick="formsubmit();" value="保  存">
								-->
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
