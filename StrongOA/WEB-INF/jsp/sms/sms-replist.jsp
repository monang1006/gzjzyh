<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<HTML>
	<HEAD>
	<%@include file="/common/include/meta.jsp" %>
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script language='javascript' src="<%=path%>/common/js/common/common.js" ></script>
	<%--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		
		--%><TITLE>手机短信发送回复记录</TITLE>
			
		<script type="text/javascript">
		
		function gotoView(){
			var id = getValue();
			viewSms(id);
		}
		
		function viewSms(id){
			if(id==null|id==""){
				alert("请选择要查阅的短信记录。");
			} else if(id.indexOf(",")!=-1){
				alert("一次只能查阅一条短信记录。");
			} else{
				//var url = "<%=path%>/sms/sms!view.action?smsId="+id;
				var url = '<%=root%>/reply_message/messageReply!smsRepView.action?bussinessId='+id;
				var a = window.showModalDialog(url,window,'dialogWidth:850px;dialogHeight:700px;help:no;status:no;scroll:no');
				if("reload"==a){
					document.location.reload();
				}else if("resend"==a){
					reSend(id);
				}
			}
		}
		
		$(document).ready(function(){
			//搜索脚本
				$("#img_sousuo").click(function(){
					$("#smsSender").val(encodeURI($("#searchsmsSender").val()));
					$("#smsModelName").val(encodeURI($("#searchsmsModelName").val()));
					$("#smsCon").val(encodeURI($("#searchsmsCon").val()));
					$("form").submit();
				});
				
				$("#smsSender").val(encodeURI($("#searchsmsSender").val()));
				$("#smsModelName").val(encodeURI($("#searchsmsModelName").val()));
				$("#smsCon").val(encodeURI($("#searchsmsCon").val()));
				
			}); 
			
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									
											<table width="100%" border="0" cellspacing="0" cellpadding="00">
						                       <tr>
							                    <td class="table_headtd_img" >
								                  <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                    </td>
							                    <td align="left">
								                    <strong>手机短信发送回复记录</strong>
							                    </td>
											<td>
												&nbsp;
											</td>
											
											<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								               <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/Consult_the_reply.png"/>&nbsp;查&nbsp;阅&nbsp;回&nbsp;复&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
							                  		
								                 	</tr>
								            </table>
											</td>
											
											<%--<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
							                  		<td ><a class="Operation" href="javascript:gotoView();"><img src="<%=root%>/images/ico/page.gif" width="15" height="15"  class="img_s">查阅回复&nbsp;</a></td>
								                 	<td width="5"></td>
								                </tr>
								            </table>
											</td>
										--%></tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				 </tr>
				 
				 
				 
				 
				 <tr>
				 	<td>
						<s:form id="myTableForm" action="sms!searchRepList.action" method="get">
						<input type="hidden" name="inputType" value="${inputType}">
						<input type="hidden" name="repSms.smsSenderName" id="smsSender" value="${repSms.smsSenderName}">
						<input type="hidden" name="repSms.modelName" id="smsModelName" value="${repSms.modelName}">
						<input type="hidden" name="repSms.smsRepCon" id="smsCon" value="${repSms.smsRepCon}">
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${page.result}" page="${page}">
							
							
							<%--<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
					         	 <tr>
							       <td width="3%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="15" height="15" ></td>
							       <td width="15%"  class="biao_bg1"><input name="searchsmsSender" id="searchsmsSender" type="text" style="width=100%" class="search" title="输入姓名" value="${repSms.smsSenderName }"></td>
							       <td width="15%"  class="biao_bg1"><input name="searchsmsModelName" id="searchsmsModelName" type="text" style="width=100%" class="search" title="输入模块名称" value="${repSms.modelName }"></td>
							       <td width="52%"  class="biao_bg1"><input name="searchsmsCon" id="searchsmsCon" type="text" style="width=100%" class="search" title="输入短信内容" value="${repSms.smsRepCon }"></td>
							       <td width="15%" align="center" class="biao_bg1"><strong:newdate dateform="yyyy-MM-dd" name="repSms.smsRepSendTime" id="smsSendTime" dateobj="${repSms.smsRepSendTime}" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="发送日期"/></td>
							       <td class="biao_bg1">&nbsp;</td>
							     </tr>
					        </table>
					        
							--%>
							
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float: left;">&nbsp;&nbsp;发送人：&nbsp;<input name="searchsmsSender" id="searchsmsSender" type="text"  class="search" title="输入姓名" value="${repSms.smsSenderName }">
							       		</div>
							       		<div style="float: left;">&nbsp;&nbsp;相关模块：&nbsp;<input name="searchsmsModelName" id="searchsmsModelName" type="text"  class="search" title="输入模块名称" value="${repSms.modelName }">
							       		</div>
							       		<div style="float: left;">&nbsp;&nbsp;短信内容：&nbsp;<input name="searchsmsCon" id="searchsmsCon" type="text"  class="search" title="输入短信内容" value="${repSms.smsRepCon }">
							       		</div>
							       		<div style="float:left;width:300px;">&nbsp;&nbsp;发送时间：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="repSms.smsRepSendTime" id="smsSendTime" dateobj="${repSms.smsRepSendTime}" skin="whyGreen" isicon="true"  classtyle="search" title="发送日期"/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       	</td>
							     </tr>
							     
							</table>
							
							
							<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="5" width="3%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="发送人" valuepos="5" valueshowpos="5" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="相关模块" valuepos="6" valueshowpos="6" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="短信内容" valuepos="3" valueshowpos="3" width="52%" isCanDrag="true" isCanSort="true" showsize="36"></webflex:flexTextCol>
							<webflex:flexDateCol caption="日期" valuepos="4" valueshowpos="4" width="15%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>	
													
						  </webflex:flexTable>
						  </s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
		var sMenu = new Menu();
		function initMenuT(){
			sMenu.registerToDoc(sMenu);
			var item = null;
			item = new MenuItem("<%=root%>/images/operationbtn/Consult_the_reply.png","查阅回复","gotoView",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>
		 
	</BODY>
</HTML>
