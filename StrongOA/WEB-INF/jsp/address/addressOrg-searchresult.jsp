<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>通讯录搜索结果</TITLE>
		<%@include file="/common/include/meta.jsp" %>
		<link href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script type="text/javascript" src="<%=path %>/oa/js/mymail/jquery.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script type="text/javascript">
			//mail
			function sendmail(id,type){
				$.post(
					"<%=root%>/address/address!getUserDefaultEmail.action",
					function(data){
						if("" == data){
							alert("对不起，您未配置默认邮箱，不能发送邮件。请先配置默认邮箱！");
						}else if(data == "error"){
							alert("对不起，获取默认邮箱出错。请与管理员联系！");
						}else{
							$.post(
								"<%=root%>/address/address!getUserEmail.action",
								{userId:id,type:type},
								function(ret){
									if("null" == ret || ""==ret){
										alert("提示:您所选择的人员未设置邮箱，无法发送邮件！");
									}else if(ret == "error"){
										alert("对不起，获取用户邮箱出错。请与管理员联系！");
									}else{
										OpenWindow('<%=root%>/mymail/mail!otherModel.action?receiver='+encodeURI(encodeURI(ret)), '700', '400', window);
									}
								}
							);
						}
					}
				);
			}
			//msg
			function sendmsg(id){
				OpenWindow("<%=root%>/message/message!view.action?forward=write&msgReceiverIds="+id+"&moduleCode=<%=GlobalBaseData.SMSCODE_ADDRESS %>", '700', '400', window);
			}
			//phone
			function sendphone(id){
				var ret = OpenWindow("<%=path%>/sms/sms!input.action?recvUserIds="+id, '450', '330', window);
				if("reload" == ret){
					alert("短信已提交服务器。");
				}
			}
			//rtx
			function sendIM(id,currentUserId){
				if(id == currentUserId){
					alert("不能对自己发送即时消息。");
					return ;
				}
				var ret=OpenWindow("<%=root%>/im/iM!input.action?receiveId="+id,"450","295",window);
			}
			//prop
			function prop(id){
				var ret=OpenWindow("<%=root%>/address/address!initEdit.action?id="+id,"450","295",window);
			}
		</script>
	</HEAD>
	<BODY oncontextmenu="return false;">
		${searchResult }
	</BODY>
</HTML>
