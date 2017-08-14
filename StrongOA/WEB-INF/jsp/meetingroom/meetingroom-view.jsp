<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>查看会议室</title>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	</head>
	<body oncontextmenu="return false;" >
		<DIV id=contentborder align=center>
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>
					&nbsp;
					</td>
					<td width="40%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						查看会议室
					</td>
					<td width="60%">
						&nbsp;
					</td>
				</tr>
			</table>
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
				<tr>
					<td colspan="1" width="20%" height="21" class="biao_bg1"
						align="right">
						<span class="wz">会议室名称：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						${model.mrName }
					</td>
				</tr>
				<tr>
					<td height="21" width="15%" class="biao_bg1" align="right">
						<span class="wz">可容纳人数：</span>
					</td>
					<td class="td1" width="35%" align="left">
						${model.mrPeople}
					</td>
					<td height="21" width="20%" class="biao_bg1" align="right">
						<span class="wz">会议室地点：</span>
					</td>
					<td class="td1" align="left" width="35%">
						${model.mrLocation}
					</td>
				</tr>
				<tr>
					<td height="21" width="15%" class="biao_bg1" align="right">
						<span class="wz">会议室状态：</span>
					</td>
					<td class="td1" width="35%" align="left">
						<script>
						var state = "${model.mrState}";
						if(state=="1"){
							document.write("停止使用");
						}else if(state=="2"){
							document.write("已删除");
						}else{
							document.write("正常使用");
						}
						</script>
					</td>
					<td height="21" width="15%" class="biao_bg1" align="right">
						<span class="wz">会议室类型：</span>
					</td>
					<td class="td1" align="left" width="35%">
						${model.mrType}
					</td>
				</tr>
				<tr>
					<td height="21" width="15%" class="biao_bg1" align="right">
						<span class="wz">会议室说明：</span>
					</td>
					<td class="td1" colspan="3" align="left">
						<div style="margin-left: 10px">
						<textarea rows="10" cols="50" id="description" name="description"  readonly="readonly"
							style="width: 100%;overflow:auto">${model.mrRemark}</textarea>
						</div>
					</td>
				</tr>
				<tr>
					<td height="21" width="15%" class="biao_bg1" align="right">
						<span class="wz">图片预览：</span>
					</td>
					<td id="imgPre" class="td1" colspan="3" align="left">
					</td>
					<script>
							var mrImg = "${model.mrImg}";
							var url = "";
							if(""!=mrImg&&null!=mrImg){
								url = "<%=path%>/meetingroom/meetingroom!viewImg.action?mrId=${model.mrId}";
								$("#imgPre").html("<img src=\""+url+"\" width=\"350px\" height=\"250px\">");
							}else{
								url = "<%=path%>/oa/image/meetingroom/nophoto.jpg";
								$("#imgPre").html("<img src=\""+url+"\" width=\"160px\" height=\"215px\">");
							}
							$("#imgPre").css("display","");
					</script>
				</tr>
				<tr>
					<td class="td1" colspan="4" align="center">
						<input name="Submit2" type="submit" class="input_bg" value="关闭"
							onclick="javascript:window.close();">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
