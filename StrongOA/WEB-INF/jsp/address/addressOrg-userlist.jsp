<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData" />
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>系统通讯录-用户列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'>
</script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript">
</script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript">
</script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript">
</script>
		<!--<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript">
</script>-->
		<script src="<%=path%>/oa/js/address/address.js"
			type="text/javascript">
</script>
		<script type="text/javascript">
$(document).ready(function() {
	//搜索
		$("#img_sousuo").click(function() {
			$("form").submit();
		});
	});
</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js">
</script>
		<label id="l_actionMessage" style="display: none;">
			<s:actionmessage />
		</label>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<label id="l_address_groupName"><strong>
												<script type="text/javascript">
													if(${usershow}==1) {
														document.write("用户列表")
													}else{document.write("${orgName }")}
												</script></strong>
												</label>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td>
															<table border="0" align="right" cellpadding="00" cellspacing="0">
																<tr>
																
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="sendPhone();"><img src="<%=root%>/images/operationbtn/Send_text_messages.png"/>&nbsp;发&nbsp;短&nbsp;信&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="sendIM();"><img src="<%=root%>/images/operationbtn/Instant_messages.png"/>&nbsp;发&nbsp;即&nbsp;时&nbsp;消&nbsp;息&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
																	
																
																
																<!--
																	<td>
																		<a class="Operation" href="JavaScript:gotoView();"><img
																				class="img_s" src="<%=root%>/images/ico/chakan.gif"
																				width="15" height="15">查看&nbsp;</a>
																	</td>
																	<td width="5"></td>
																	 
																	<security:authorize ifAnyGranted="001-0001000400020004">
																		<td>
																			<a class="Operation"
																				href="JavaScript:gotoWriterMail();"><img
																					class="img_s"
																					src="<%=root%>/images/ico/tijiao.gif" width="15"
																					height="15">写邮件&nbsp;</a>
																		</td>
																		<td width="5"></td>
																	</security:authorize>
																	<security:authorize ifAnyGranted="001-0001000400020003">
																		<td>
																			<a class="Operation" href="JavaScript:sendMsg();"><img
																					class="img_s"
																					src="<%=path%>/oa/image/address/sms.gif"
																					width="15" height="15">发消息&nbsp;</a>
																		</td>
																		<td width="5"></td>
																	</security:authorize> 
																<security:authorize ifAnyGranted="001-0001000400020002">
																		<td>
																			<a class="Operation" href="JavaScript:sendPhone();"><img
																					class="img_s"
																					src="<%=path%>/oa/image/address/mobile_sms.gif"
																					width="15" height="15">发短信&nbsp;</a>
																		</td>
																		<td width="5"></td>
																	</security:authorize>
																	<security:authorize ifAnyGranted="001-0001000400020001">
																		<td>
																			<a class="Operation" href="JavaScript:sendIM();"><img
																					class="img_s"
																					src="<%=root%>/images/ico/rtx.gif"
																					width="16" height="16">发即时消息&nbsp;</a>
																		</td>
																		<td width="5"></td>
																	</security:authorize>-->
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
							<s:form id="myTableForm"
								action="/address/addressOrg!orguserlist.action">
								<input id="orgName" type="hidden" name="orgName"
									value="${orgName }" />
								<input id="usershow" type="hidden" name="usershow"
									value="${usershow }" />
								<!-- 用于将文件名传到后台然后传回此页面显示在<label> -->
								<s:hidden name="orgId"></s:hidden>
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="fileId" isCanDrag="true"	
									isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
									getValueType="getValueByProperty" collection="${page.result}"
									page="${page}">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table1_search">
										<tr>
											<td>
												&nbsp;&nbsp;姓名：&nbsp;<input name="userName" id="userName" type="text"  class="search" title="请您输入姓名" value="${model.userName }"/>
												&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
											</td>
											
											<!-- <td width="40px" align="center" class="biao_bg1">
												<img style="cursor: hand;" id="img_search" title="单击搜索"
													src="<%=root%>/images/ico/sousuo.gif" width="17"
													height="16">
											</td>
											<td width="20%" align="center" class="biao_bg1">
												<s:textfield id="userName" name="userName" cssClass="search" onkeyup="this.value=this.value.replace(/\s/,'')" 
												onafterpaste="this.value=this.value.replace(/\s/,'')"
													title="输入姓名"></s:textfield>
											</td>
											<td class="biao_bg1">
												&nbsp;
											</td> -->
										</tr>
									</table>
									<webflex:flexCheckBoxCol caption="选择" property="userId"
										showValue="userName" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="姓名" property="userName"
										showValue="userName" showsize="50" width="20%"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexNumricCol caption="电话" property="userTel"
										showValue="userTel" width="18%" showsize="12" isCanDrag="true"
										isCanSort="true"></webflex:flexNumricCol>
									<webflex:flexNumricCol caption="手机号码" property="rest2"
										showValue="rest2" showsize="13" width="13%" isCanDrag="true"
										isCanSort="true"></webflex:flexNumricCol>
									<webflex:flexTextCol caption="Email" property="userEmail"
										showValue="userEmail" width="19%" showsize="50"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									<webflex:flexTextCol caption="职务" property="userDescription"
										showValue="userDescription" showsize="50" width="25%"
										isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT() {
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/view.png", "查看", "gotoView", 1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	//<security:authorize ifAnyGranted="001-0001000400020004">	
		//item = new MenuItem("<%=root%>/images/ico/tijiao.gif","写邮件","gotoWriterMail",1,"ChangeWidthTable","checkOneDis");
		//sMenu.addItem(item);
	//</security:authorize>
	//<security:authorize ifAnyGranted="001-0001000400020003">	
	//	item = new MenuItem("<%=path%>/oa/image/address/sms.gif","发送消息","sendMsg",1,"ChangeWidthTable","checkOneDis");
		//sMenu.addItem(item);
	//</security:authorize>
	<security:authorize ifAnyGranted="001-0001000400020002">	
		item = new MenuItem("<%=path%>/images/operationbtn/Send_text_messages.png","发短信","sendPhone",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAnyGranted="001-0001000400020001">
		item = new MenuItem("<%=root%>/images/operationbtn/Instant_messages.png","发送即时消息","sendIM",1,"ChangeWidthTable","checkOneDis");
		sMenu.addItem(item);
	</security:authorize>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
//查看
function gotoView(){
	var id = getValue();
	if(id == ""){
		alert("请选择要查看的用户。");
		return ;
	}else{
		if(id.split(",").length>1){
			alert("一次只能查看一个用户信息。");
			return ;
		}
	}
	OpenWindow("<%=root%>/address/addressOrg!userDetail.action?userId="+id,"400","280",window);
}

//发送即时消息
function sendIM(){
	var id = getValue();
	if(id == ""){
		alert("请选择要发送即时消息的用户。");
		return ;
	}
	if(id.indexOf('${currentUserId}')!=-1){
		alert("不能对自己发送即时消息。");
		return ;
	}
	OpenWindow("<%=root%>/im/iM!input.action?receiveId="+id,"450","300",window);
}
//发送消息
function sendMsg(){
	if(getValue() == ""){
		alert("请选择要发送消息的用户。");
		return ;
	}
	var msgReceiverIds = getValue();
	OpenWindow("<%=root%>/message/message!view.action?forward=write&msgReceiverIds="+msgReceiverIds+"&moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS%>", '700', '500', window);
	//OpenWindow("<%=root%>/message/message!view.action?forward=write&msgReceiverIds="+msgReceiverIds, '700', '400', window);
}
//发送手机短信
function sendPhone(){
	if(getValue() == ""){
			alert("请选择要发送手机短信的用户。");
			return ;
	}
	var ret = OpenWindow("<%=path%>/sms/sms!input.action?moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS%>&recvUserIds="+getValue(), '450', '295', window);
	if("reload" == ret){
		alert("短信已提交服务器。");
	}
}
//写邮件
function gotoWriterMail(){
	//验证是否勾选
	if(getValue() == ""){
		alert("请选择要写邮件的用户。");
		return ;
	}
	//验证用户是否有邮箱
	var noEmail = new Array();
	var userEmail = "";
	var k=0;
	$(":checked").each(function(i){
	
	   	if($(this).attr("name")!='checkall'){
			var email = $(this).parent().parent().children().next().next().next().next().next().attr("value");
			var name =  $(this).parent().parent().children().next().next().attr("value");
			if(email && email!="" && email!="null"){
				userEmail = userEmail + name+"<"+email+">"+",";
			}else{
				noEmail[k] = name;
				k++;
			}
	   	}	
	});

	if(userEmail.length>0){
		userEmail = userEmail.substring(0,userEmail.length-1);
		if(noEmail.length>0){
			var userName = "";
			for(var j=0;j<noEmail.length;j++){
				userName += noEmail[j]+",";
			}
			if(userName.length>0){
				userName = userName.substring(0,userName.length-1);
			}
			alert("提示:以下人员["+userName+"]邮箱未设置，系统发送邮件过程中将忽略这些人员。");
		}
		$.post(
			"<%=root%>/address/address!getUserDefaultEmail.action",
			function(data){
				if("" == data){
					alert("对不起,您未配置默认邮箱，不能发送邮件。请先配置默认邮箱。");
				}else if(data == "error"){
					alert("对不起，获取默认邮箱出错。请与管理员联系。");
				}else{
					OpenWindow('<%=root%>/mymail/mail!otherModel.action?receiver='+encodeURI(encodeURI(userEmail)), '700', '400', window);
				}
			}
		);
	}else{
		alert("提示:您所选择的人员未设置邮箱，无法发送邮件。");
	}
	
}
</script>
	</BODY>
</HTML>
