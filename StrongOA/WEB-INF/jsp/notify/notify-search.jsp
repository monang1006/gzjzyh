<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>

<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
		
		<title>查找通知公告</title>
		
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
		
		function search(){
			var form = document.getElementById("notifyForm");
			 //选择公告类型 
			//var state = document.getElementById("state").value;
			//form.afficheState.value = state;
			
			form.submit();
		}
		//页面取消返回
		 function backToList(){
			//var inputType = document.getElementById("inputType");
			//location = "<%=path%>/notify/notify.action?inputType="+inputType.value;
			history.go(-1);
		 }
		</script>
	</head>
	<body >
	<DIV id=contentborder align=center>
	<s:form action="notify!getsearchlist.action" theme="simple" name="notifyForm">
	
	<input type="hidden" id="afficheId" name="model.afficheId" value="${model.afficheId}">
	<input type="hidden" id="inputType" name="inputType" value="${inputType}">
	<input type="hidden" id="afficheTitleColour" name="model.afficheTitleColour" value="${model.afficheTitleColour}">
	<input type="hidden" id="afficheTitleBold" name="model.afficheTitleBold" value="${model.afficheTitleBold}">
	<input type="hidden" id="afficheTop" name="model.afficheTop" value="${model.afficheTop}">
	<input type="hidden" id="afficheState" name="model.afficheState" value="${model.afficheState}">
	
	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
											&nbsp;
											</td>
											<td width="227">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												查找通知公告
											</td>
											<td>
												&nbsp;
											</td>
											<td width="190">
											<table width="110" border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
							              			<td ><a class="Operation" href="javascript:search();"><img src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" class="img_s">查找&nbsp;</a></td>
								                 	<td width="5"></td>
								                 	<td ><a class="Operation" href="javascript:backToList();"><img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">取消&nbsp;</a></td>
								                 	<td width="5"></td>
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
					<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
						<tr>
							<td class="biao_bg1" width="10%">
								<span class="wz">标题 ：</span>
							</td>
							<td class="td1" width="90%">
								&nbsp;<input id="afficheTitle" name="model.afficheTitle" type="text" value="${model.afficheTitle}"
											 size="65" maxlength="65">
								<span class="wz">所查找的通知公告的标题包含的文字</span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="10%">
								<span class="wz">公告作者 ：</span>
							</td>
							<td class="td1" width="90%">
								&nbsp;<input id="afficheAuthor" name="model.afficheAuthor" type="text" value="${model.afficheAuthor}"
											  size="65" maxlength="65">
								<span class="wz">所查找的通知公告的作者</span>
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" width="10%">
								<span class="wz">公告部门：</span>
							</td>
							<td class="td1" width="90%">
								&nbsp;<input id="afficheGov" name="model.afficheGov" type="text" value="${model.afficheGov}"
											 size="65" maxlength="65">
								<span class="wz">所查找的通知公告的发布部门</span>
							</td>
						</tr>
						
						<tr>
							<td nowrap class="biao_bg1">
								<span class="wz">有效期 ：</span>
							</td>
							<td class="td1">
								&nbsp;<span class="wz">终止日期:</span>
								<strong:newdate name="afficheUsefulLife" id="afficheUsefulLife" dateform="yyyy-MM-dd" dateobj="${model.afficheUsefulLife}"
									width="150px" skin="whyGreen" isicon="true"></strong:newdate>
								<font class="wz"> 所查找的通知公告的有效期限 </font>
							</td>
						</tr>
						<tr>
							<td nowrap class="biao_bg1">
								<span class="wz">内容 ：</span>
							</td>
							<td align="left" class="td1">
 		 						&nbsp;<input id="afficheTitle" name="model.afficheDesc" type="text" value=""
											 size="65" maxlength="65">                             
     							<span class="wz">所查找的通知公告的内容包含的文字</span>
     						</td>
						</tr>
						
					</table>
					</td>
				</tr>
			</table>
		</s:form>
		</DIV>
	</body>

</html>
