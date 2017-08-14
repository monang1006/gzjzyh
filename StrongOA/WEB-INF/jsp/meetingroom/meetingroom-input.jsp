<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<title>会议室信息</title>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
			
		<style type="text/css">
		#imageSrc
		{
			margin-left:14px;
		    filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src='<%=path%>/oa/image/meetingroom/LOGO.jpg');
		}
		</style>	
		<script>
			
			//验证图片
			function validimg(img){
				if(img==""|img==null){
					return true;
				}
				var ext = img.substring(img.lastIndexOf(".") + 1,img.length);
				if(ext ==("jpg")|ext=="gif"|ext=="JPG"|ext=="GIF"){
						return true;
					}else{
						alert("请输入正确的图片格式(jpg或gif)！");
						return false;
						}
			}
			
			function init(){
				var newPreview = document.getElementById("imageSrc");
			    newPreview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = "<%=path%>/oa/image/meetingroom/LOGO.jpg";
			    newPreview.style.width = "140px";
			    newPreview.style.height = "47px";
			}
			function PreviewImg(imgFile)
			{
			    //新的预览代码，支持 IE6、IE7。
			    var newPreview = document.getElementById("imageSrc");
			    newPreview.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgFile.value;
			    newPreview.style.width = "350px";
			    newPreview.style.height = "250px";
			}
			
			function save(){
				if($.trim($("#mrName").val())==""){
					alert("请输入会议室名称！");
					$("#mrName").val("");
					$("#mrName").focus();
					return;
				}
				if($.trim($("#mrName").val()).length>15){
					alert("输入会议室名过长！\n请控制在15个字以内");
					$("#mrName").val("");
					$("#mrName").focus();
					return;
				}else{
					var re1 = new RegExp("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z0-9])*$");
			 		if (!re1.test($.trim($("#mrName").val()))){
				 		alert("输入会议室名包含特殊字符！\n请输入汉字，数字或字母");
						$("#mrName").val($.trim($("#mrName").val()));
						$("#mrName").focus();
						return;
			 		}
				}
				$("#mrName").val($.trim($("#mrName").val()));
				
				if($.trim($("#mrType").val())==""){
					alert("请选择会议室类型！");
					$("#mrType").focus()
					return;
				}
				if($.trim($("#mrLocation").val())==""){
					alert("请输入会议室地点！");
					$("#mrLocation").focus()
					return;
				}
				$("#mrLocation").val($.trim($("#mrLocation").val()));
				if($.trim($("#mrPeople").val())==""){
					alert("请输入会议室容纳人数！");
					$("#mrPeople").focus()
					return;
				}
				
				var num_reg= /^[0-9]+$/;///^(\d{*})$/;
				if(!num_reg.test($.trim($("#mrPeople").val()))){
					alert("会议室容纳人数格式不对，只能输入正整数！\n请确认后重新输入！");
					return ;
				}else{
					var mrP = $.trim($("#mrPeople").val());
					mrP = formatNumZore(mrP);
					$("#mrPeople").val(mrP)
				}
				
				
				if($("#mrRemark").val().length>2000){
					alert("会议室说明过长，请控制在两千字以内！");
					$("#mrRemark").focus();
					return;
				}
				if(validimg($("#file").val())){
				}else{
					return;
				}
				var roomState = document.getElementsByName("roomState");
				for(var i=0;i<roomState.length;i++){
					if(roomState[i].checked){
						$("#mrState").val(roomState[i].value);
					}
				}
				form.submit();
			}
	
	function formatNumZore(num){
		if(num.indexOf("0")==0){
			num = num.replace("0","");
			return formatNumZore(num);
		}else{
			return num;
		}
	}
	
	$(document).ready(function() {
		var message = $(".actionMessage").text();
		if(message!=null && message!=""){
			if(message.indexOf("error")>-1){
				 alert("无法根据给出路径找到图片,请重新选择图片!");
			}else{
				returnValue='reload';
				window.close();
			}
		}
	});
			
		</script>
	</head>
		<base target="_self">
	<body class=contentbodymargin oncontextmenu="return false;" >
		<DIV id=contentborder align=center>
		<s:form name="form" action="/meetingroom/meetingroom!save.action" method="post" enctype="multipart/form-data">
		<input type="hidden" id="mrState" name="model.mrState" value="${model.mrState}">
		<label id="actionMessage" style="display:none;"><s:actionmessage/></label>
			<input type="hidden" id="mrId" name="model.mrId" value="${model.mrId}">
				<input type="hidden" id="topOrgcode" name="model.topOrgcode"
								value="${model.topOrgcode}">
								
				<input type="hidden" id="departmentId" name="model.departmentId"
								value="${model.departmentId}">
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td >
					&nbsp;
					</td>
					<td width="40%">
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
						保存会议室
					</td>
					<td width="60%">
						&nbsp;
					
					<table border="0" align="right" cellpadding="00" cellspacing="0">
					<td >
						<a class="Operation" href="#" onclick="save();">
							<img src="<%=root%>/images/ico/baocun.gif" width="14"
								height="14" class="img_s"><span id="test"
							style="cursor:hand">保存&nbsp;</span> </a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="window.close();">
							<img src="<%=root%>/images/ico/quxiao.gif" width="14"
								height="14" class="img_s"><span id="test"
							style="cursor:hand">取消&nbsp;</span> </a>
					</td>
					</table>
					<td width="5"></td>
					</td>
				</tr>
			</table>

			<table align="center" width="100%" border="0" cellpadding="0"
				cellspacing="1" class="table1">
								
				<tr valign="top">
					<td colspan="1" width="15%" height="21" class="biao_bg1" align="right">
						<span class="wz">名称(<font color="red">*</font>)：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						&nbsp;<input id="mrName" name="model.mrName" value="${model.mrName}" type="text"  maxlength="15" size="42">
					</td>
				</tr>
				<tr>
						<td height="21" class="biao_bg1" width="25%" align="right">
							<span class="wz">申请状态：</span>
						</td>
						<td class="td1" colspan="3" align="left">
							<input type="radio" id="ROOM_OPEN" name="roomState" value = "0" onclick=" ">正常使用&nbsp;&nbsp;
							<input type="radio" id="ROOM_CLOSE" name="roomState" value = "1" onclick=" "> 停止使用&nbsp;&nbsp;
							<script>
								//初始化申请状态
								var maState ="${model.mrState }";
								if("1"==maState){
									$("#ROOM_CLOSE").attr("checked",true);
								}else {
									$("#ROOM_OPEN").attr("checked",true);
								}
							</script>
						</td>
					</tr>
				<tr valign="top" title="可在字典项管理中对会议室类型进行管理">
					<td height="21" class="biao_bg1" align="right">
						<span class="wz"> 类型(<font color="red">*</font>)：</span>
					</td>
					<td class="td1" align="left">
					<div style="margin-left: 14px">
					<s:select list="mrTypeList" listKey="dictItemName" listValue="dictItemName"  headerKey="" headerValue="请选择会议室类型"
							id="mrType" name="model.mrType"/>
					</div>
					</td>
				</tr>
				<tr valign="top">
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">地点(<font color="red">*</font>)：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						&nbsp;<input id="mrLocation" name="model.mrLocation" value="${model.mrLocation}" maxlength="30" type="text" size="42">
					</td>
				</tr>
				<tr valign="top">
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">容纳人数(<font color="red">*</font>)：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						&nbsp;<input id="mrPeople" name="model.mrPeople" value="${model.mrPeople}" type="text" maxlength="18" size="42">
					</td>
				</tr>
				<tr valign="top">
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">说明：</span>
					</td>
					<td colspan="3" class="td1" align="left">
						&nbsp;<s:textarea id="mrRemark" name="model.mrRemark" cols="58" rows="10"></s:textarea>
					</td>
				</tr>
				<tr valign="top">
					<td colspan="1" height="21" class="biao_bg1" align="right">
						<span class="wz">图片：</span>
					</td>
					<td colspan="3" class="td1" align="left" id="imgTD">
						<div style="margin-left: 14px">
						<%--
							<INPUT id="image" name=image TYPE="file" onchange="javascript:PreviewImg(this);" style="display: block;">
							</div>
							<div id="imageSrc"></div>
						--%>
						<INPUT id="file" name="file" TYPE="file" style="display: block;" onkeyup="hideImg(this.value);" >
						</div>
						<div id="imgPre" style="margin-left: 4px;display: none;">
						</div>
						<script>
							var mrImg = "${model.mrImg}";
							var mrId = "${model.mrId}";
							if(""==mrId|null==mrId){
							}else{
								if(""!=mrImg&&null!=mrImg){
									var url = "<%=path%>/meetingroom/meetingroom!viewImg.action?mrId=${model.mrId}";
									$("#imgPre").html("<img src=\""+url+"\" width=\"350px\" height=\"250px\">");
									$("#imgPre").css("display","");
								}else{
									var url = "<%=path%>/oa/image/meetingroom/nophoto.jpg";
									$("#imgPre").html("<img src=\""+url+"\" width=\"350px\" height=\"250px\">");
									$("#imgPre").css("display","");
								}
							}
							function hideImg(path){
								if(""!=path&&null!=path){
									$("#imgPre").css("display","none");
								}else{
									$("#imgPre").css("display","");
								}
							}
						</script>
					</td>
				</tr>
				
				<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td >
					&nbsp;
					</td>
					<td width="40%">
					</td>
					<td width="60%">
						&nbsp;
					
					<table border="0" align="left" cellpadding="00" cellspacing="0">
					<td >
						<a class="Operation" href="#" onclick="save();">
							<span id="test" style="cursor:hand">保存</span> </a>
					</td>
					<td width="5"></td>
					<td >
						<a class="Operation" href="#" onclick="window.close();">
							<span id="test" style="cursor:hand">取消</span> </a>
					</td>
					</table>
					<td width="5"></td>
					</td>
				</tr>
			</table>
			</table>
		</s:form>
		</DIV>
	</body>
</html>
