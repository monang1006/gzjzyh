<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
		
		<title>查看新闻公告</title>
	<style type="text/css">
		body {
			width:100%;
			margin-left: 0px;
			margin-top: 0px;
			margin-right: 0px;
			margin-bottom: 0px;
			height: 100%
		}
	</style>
	<script>
	//页面取消返回
		 function backToList(){
			var inputType = document.getElementById("inputType").value;
			location = "<%=path%>/notify/notify.action?inputType="+inputType;
			if(inputType=="mylist"){
				location = "<%=path%>/notify/notify!mylist.action";
			}else{
				location = "<%=path%>/notify/notify.action?inputType="+inputType;
			}
		 }
		 
		 function viewnull(){
		 	if(""=="${model.afficheId}"){
		 		alert("您查看的公告不存在或已删除！");
		 		window.location = "<%=path%>/notify/notify.action?inputType="+inputType;
		 	}
		 }
	</script>
	</head>
	<body onload="viewnull()" onblur="self.focus()">
		<div id="afficheDesc" style="display: none">${model.afficheDesc}</div>
		<input type="hidden" id="inputType" name="inputType" value="${inputType}">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<iframe id="downloadframe" name="downloadframe" style="display: none;"></iframe>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00" >
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>查看通知公告</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table border="0" cellpadding="0" cellspacing="1" class="table1" style="margin-left:3%;margin-right:3%"
							width="94%">
							<tr>
								<td class="biao_bg1" style="width:100%" align="center">
									<script>
										var color = "${model.afficheTitleColour}";
										var bold = "${model.afficheTitleBold}";
										document.write("<font color="+color+" size=5>");
										if(bold==1){
											document.write("<b>${model.afficheTitle}</b>");
										}else{
											document.write("${model.afficheTitle}");
										}
										document.write("</font>");
									</script>
								</td>
							</tr>
							<tr>
								<td class="td1" width="100%" align="right">
									<span class="wz"><font color="black">公告作者：</font> </span>
									&nbsp;${model.afficheAuthor}

									&nbsp;&nbsp;&nbsp;&nbsp;<span class="wz">公告部门：</span>
									&nbsp;${model.afficheGov}
								</td>
							</tr>
							<tr style="display: none">
								<td nowrap class="td1" width="100%" align="right">
									<span class="wz">有效期 ：</span>
									<strong:newdate name="afficheTime" id="afficheTime" dateform="yyyy-MM-dd" dateobj="${model.afficheTime}"
									width="150px" skin="whyGreen" ></strong:newdate>
									<span class="wz">到:</span> 
									<strong:newdate name="afficheUsefulLife" id="afficheUsefulLife" dateform="yyyy-MM-dd" dateobj="${model.afficheUsefulLife}"
									width="150px" skin="whyGreen" ></strong:newdate>
								</td>
							</tr>
							<tr>
								<td nowrap class="td1" width="100%" align="right">
									<span class="wz">有效期 ：</span>
									<script>
										var day = document.getElementById("afficheTime");
										document.write(day.value);
									</script>
									&nbsp;&nbsp;
									<span class="wz">到:</span> 
									<script>
										var day = document.getElementById("afficheUsefulLife");
										document.write(day.value);
										if(day.value==null|""==day.value){
											document.write("<I>(未指定过期日期)</I>");
										}
									</script>
									&nbsp;&nbsp;
								</td>
							</tr>
							
							<tr >
								<td width="100%" class="td1" id="ArticleContent" >
								</td>
							</tr>
							<tr>
								<td class="td1" width="100%" align="left">
									<span class="wz"><font color="black">附件：</font> </span>
									&nbsp;${attachFiles}
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
			</table>
		</DIV>
				<script type="text/javascript">
		         document.getElementById("ArticleContent").innerHTML = document.getElementById('afficheDesc').innerText;
		        </script>
	</body>

</html>
