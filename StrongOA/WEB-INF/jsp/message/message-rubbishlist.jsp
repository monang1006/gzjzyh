<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<style type="text/css">
a .{
	text-decoration: none;
}

.tbaCss tr td {
	padding: 3px 0;
}

.myshow {
	width: 50px;
}

.myhidden {
	width: 50px;
	padding:0px;
	overflow: hidden;
	background-color: #6d9ac7;
}
</style>
		<title>消息列表-收件箱</title>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js">
</script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js">
</script>
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'>
</script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript">
</script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript">
</script>
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<script src="js/work.js" type="text/javascript">
</script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script type="text/javascript">
function show(i) {
	$.blockUI( {
		overlayCSS : {
			backgroundColor : '#B3B3B3'
		}
	});
	$.blockUI( {
		message : '<font color="#008000"><b>' + i + '</b></font>'
	});
}
function hidden() {
	$.unblockUI();
}
function viewState(state) {
	if (state == "1")
		return "<img src='<%=path%>/oa/image/mymail/read.gif'>";
	else
		return "<img src='<%=path%>/oa/image/mymail/unread.gif'>";
}

function viewPri(pri) {
	if (pri == "0") {
		return "<img src='<%=path%>/oa/image/mymail/mark1.gif'>"
	} else {
		return "<img src='<%=path%>/oa/image/mymail/mark2.gif'>"
	}
}

function isAtt(hasAtt){
				if(hasAtt=="1"){
					return "<img src='<%=path%>/oa/image/mymail/yes.gif'>";
				}else{
					return "<img src='<%=path%>/oa/image/mymail/no.gif'>";
				}
			}
			
			
			$(document).ready(function(){
			  var d1x_click = false;
			  $("body").not($("#d1x")).click(function(){
				  if(!d1x_click)
				  {
				  	$("#d1x").css("display","none");
				  }

			  	  d1x_click = false;
			  });
			  $("#d1x").click(function(){
			  	  d1x_click = true;
			  });

			  $("#test").click(function(){
			    
			    var folders = <s:property value="#request.folderList.size"/>;
			  	if(folders<1){
			  		alert("对不起，您还没有添加自定义文件夹。\n没有可以选择的文件夹。");
			  		return;
			  	}
			  	
			  	var offset = $("#test").offset();
				var d1x = $("#d1x");
				if(d1x.css("display") == "block")
				{
					d1x.css("display","none");
				}
				else
				{
					d1x.css("display","block");
				}
				d1x.css("position","absolute");
				d1x.css("left",offset.left);
				d1x.css("top",offset.top+$("#test").height()+3);
				d1x_click = true;
				return false;
			  });
				$("#img_sousuo").click(function(){
					var st = $("#starttime").val();
					var se = $("#endtime").val();
					st = st.replace("-","").replace("-","");
					se = se.replace("-","").replace("-","");
					if((st!=""&&se!="")&&st>se){
						alert("截止日期不能早于起始日期。");
						return ;
					}
					$("#searchTitle").val(encodeURI($.trim($("#textfield").val())));
					$("#beginDate").val($("#starttime").val());
					$("#endDate").val($("#endtime").val());
					$("form").submit();
				});
			}); 
			
			//将选中的消息移动到 文件夹
			function changeFolder(folderid){
				msgid=getValue();
				if(msgid==null||msgid==""){
					alert("请选择要移动的记录。");
				}else{
				  	$.ajax({
				  		type:"post",
				  		dataType:"text",
				  		url:"<%=root%>/message/message!changeFolder.action",
				  		data:"msgId="+msgid+"&folderId="+folderid,
				  		success:function(msg){
				  			if(msg=="true"){
				  				var thisFolder = "${folderId }";
				  				parent.parent.msg_manager_tree.freashUnread(thisFolder);
				  				parent.parent.msg_manager_tree.freashUnread(folderid);
				  				window.location.reload();
							}else if(msg=="false"){
								alert("文件夹更换失败。");
				  			}else if(msg=="nofolder"){
				  				alert("文件夹不存在。");
				  			}else{
				  				alert("文件夹更换出现异常。");
				  			}
				  		}
				  	});
				}
				$("#d1x").css("display","none");
			}
			function OpenMessage(Url, Width, Height, WindowObj) {
				
				var ParamValue = window.open(Url,"_blank","width=" + Width + ",height=" + Height +
		                                ",top=100,left=300,status=no,help=no,scrollbars=no");
					
					return ParamValue;
			}
			function onunLoadClick(){
       			window.parent.parent.msg_manager_tree.freashAllUnread();
      		}
		</script>

	</head>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();onunLoadClick();" >
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js">
</script>
<div style="exportDiv">
			<iframe id="exportIframe" name="exportIframe" style="display: none;"></iframe>
			<form id="exportForm" action="" target="exportIframe" method="post">
			<input id="exportInput" name="mailId" type="hidden" value="">
			</form>
		</div>
		<DIV id=contentborder onmousewheel="$('#d1x').css('display','none');"
			onscroll="$('#d1x').css('display','none');" align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="55"
									style="FILTER: progid : DXImageTransform.Microsoft.Gradient ( gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff );">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>消息列表 : 垃圾箱</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td>
															<table border="0" align="right" cellpadding="00"
																cellspacing="0">
																<tr>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="readmail();"><img src="<%=root%>/images/operationbtn/reading.png"/>&nbsp;阅&nbsp;读&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="resend();"><img src="<%=root%>/images/operationbtn/Forward.png"/>&nbsp;转&nbsp;发&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" id="test" onclick=""><img src="<%=root%>/images/operationbtn/Transfer.png"/>&nbsp;转&nbsp;移&nbsp;到&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
											                 	<td class="Operation_list" onclick="realDel();"><img src="<%=root%>/images/operationbtn/completely_del.png"/>&nbsp;彻&nbsp;底&nbsp;删&nbsp;除&nbsp;</td>
											                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
										                  		<td width="5"></td>
										                  		<td width="2%"></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr higth="20"/>
													<tr align="right">
														<td>
															<table border="0" align="right" cellpadding="00"
																cellspacing="0">
																<tr>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="exportMail(1);"><img src="<%=root%>/images/operationbtn/Export_to_eml.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;eml&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="exportMail(2);"><img src="<%=root%>/images/operationbtn/Afolderto_export.png"/>&nbsp;导&nbsp;出&nbsp;为&nbsp;文&nbsp;件&nbsp;夹</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="2%"></td>
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
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<s:form id="myTableForm" action="/message/message.action"
							method="get">
							<input type="hidden" name="folderId" id="folderId"
								value="${folderId }">
							<input type="hidden" name="type" id="type" value="${returnType }">
							<input type="hidden" name="searchTitle" id="searchTitle"
								value="${searchTitle}">
							<input type="hidden" name="beginDate" id="beginDate"
								value="${firstDate }">
							<input type="hidden" name="endDate" id="endDate"
								value="${otherDate }">
							<webflex:flexTable name="myTable" width="100%" height="100%"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" pageSize="10"
								getValueType="getValueByArray" onclick="quicklyVeiwMail(this)"
								collection="${page.result}" page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
								 <tr>
								 	<td>
							       		&nbsp;&nbsp;标题：&nbsp;<input name="textfield" id="textfield"  type="text" class="search" title="请您输入标题" value="${searchTitle }">
							       		&nbsp;&nbsp;起始日期：&nbsp;<strong:newdate  name="starttime" id="starttime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入起始日期" dateform="yyyy-MM-dd" dateobj="${starttime}"/>
							       		&nbsp;&nbsp;截止日期：&nbsp;<strong:newdate  name="endtime" id="endtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入截止日期" dateform="yyyy-MM-dd" dateobj="${endtime}"/>
							      		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							     	</td>
							     </tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" valuepos="0"
									valueshowpos="1" width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexTextCol caption="标题" valuepos="0" valueshowpos="1"
									width="53%" isCanDrag="true" isCanSort="true"
									onclick="toViewMail(this.value)" showsize="35"></webflex:flexTextCol>
								<webflex:flexDateCol caption="日期" valuepos="3" valueshowpos="3"
									width="22%" isCanDrag="true" isCanSort="true"
									dateFormat="yyyy-MM-dd HH:mm" showsize="16"></webflex:flexDateCol>
								<webflex:flexTextCol caption="状态" valuepos="2"
									valueshowpos="javascript:viewState(2)" width="6%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="附件" valuepos="3"
									valueshowpos="javascript:isAtt(7)" width="6%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<%--<webflex:flexTextCol caption="类型" valuepos="6" valueshowpos="javascript:viewPri(6)" width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
			--%>
								<webflex:flexTextCol caption="发件人" valuepos="5" valueshowpos="5"
									width="13%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
		</DIV>
		<div id="d1x" class="Operation" style="width: 60px;">
			<div style="border-top: 1px solid #fff; border-left: 1px solid #fff; border-right: 1px solid #B4C5E0; border-bottom: 1px solid #B4C5E0; padding: 5px;">
				<table width="100%" border="0" cellspacing="0" class="tbaCss"
					cellpadding="0">
					<s:iterator value="#request.folderList" status="statu"
						id="msgFolder">
						<tr>
							<td
								onclick="changeFolder('<s:property value="#msgFolder.msgFolderId"/>')"
								onmouseout="this.className='myshow'"
								onmouseover="this.className='myhidden'">
								<s:property value="#msgFolder.msgFolderName" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>
		</div>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT() {
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/reading.png", "阅读", "readmail", 1,
			"ChangeWidthTable", "checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Forward.png", "转发", "resend", 1,
			"ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/completely_del.png", "彻底删除", "realDel",
			1, "ChangeWidthTable", "checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Export_to_eml.png", "导出为eml",
			"exportMail(1)", 1, "ChangeWidthTable", "checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Afolderto_export.png", "导出为文件夹",
			"exportMail(2)", 1, "ChangeWidthTable", "checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
	registerMenu(sMenu);
}
//主办工作(办理待办工作)
function readmail() {
	var redId = getValue();
	if (redId == null || redId == "") {
		alert("请选择要阅读的记录。");
	} else {
		if (redId.indexOf(",") != -1) {
			alert("只可以阅读一条记录。");
		} else {
			toViewMail(redId);
		}
	}
}

function resend(msgId) {
	var reId = getValue();

	if (msgId != "" && msgId != undefined && msgId != "undefined") {
		reId = msgId;
	}
	if (reId == null || reId == "") {
		alert("请选择要转发的记录。");
	} else {
		if (reId.indexOf(",") != -1) {
			alert("只可以转发一条记录。");
		} else {
			boo = OpenWindow(
					'<%=root%>/message/message!tran.action?msgId=' + reId,
					'700', '446', window);
			if (boo == "true") {
				window.location.reload();
			}
		}
	}
}

//删除信息 （移动到垃圾箱）
function del() {
	var delId = getValue();
	if (delId == null || delId == "") {
		alert("请选择要删除的记录。");
	} else {
		if (confirm("确定要删除吗？") == true) {
			$
					.ajax( {
						type : "post",
						dataType : "text",
						url : "<%=root%>/message/message!delete.action",
						data : "msgId=" + delId + "&forward=notreal",
						success : function(msg) {
							if (msg == "true") {
								//alert("删除成功！");
						window.parent.msg_main_content.personal_status_content.location = "<%=root%>/fileNameRedirectAction.action?toPage=message/info.jsp";
						window.location.reload();
					} else {
						alert("删除失败请您重新删除。");
					}
				}
					});
		}
	}
}

function realDel() {
	var delId = getValue();
	if (delId == null || delId == "") {
		alert("请选择要彻底删除的记录。");
	} else {
		if (confirm("确定要彻底删除吗？") == true) {
			$
					.ajax( {
						type : "post",
						dataType : "text",
						url : "<%=root%>/message/message!delete.action",
						data : "msgId=" + delId + "&forward=real",
						success : function(msg) {
							if (msg == "true") {
								//alert("彻底删除成功！");
						window.parent.msg_main_content.personal_status_content.location = "<%=root%>/fileNameRedirectAction.action?toPage=message/info.jsp";
						window.location.reload();

					} else {
						alert("彻底删除失败请您重新删除。");
					}
				}
					});
		}
	}
}
function gotoHtml(mailid) {
	window.parent.mail_main_content.personal_status_content.location = "status_content.htm";//?mailid="+mailid;
}

function toView() {
	var msgId = getValue();
	if (msgId == null || msgId == "") {
		alert("请选择要阅读的记录。");
	} else {
		if (msgId.indexOf(",") != -1) {
			alert("只可以阅读一条记录。");
		} else {
			toViewMail(msgId);
		}
	}
}
var infoFlag = false;
function toViewMail(msgId) {
	msg = OpenMessage(
			'<%=root%>/message/message!view.action?forward=view&msgId=' + msgId,
			'700', '400', window);
	parent.parent.msg_manager_tree.freashUnread("${folderId }");
	if (msg == "reply") {
		OpenWindow('<%=root%>/message/message!reply.action?msgId=' + msgId,
				'700', '500', window);
	} else if (msg == "del") {
		infoFlag = true;
		window.location.reload();
	} else if (msg == "resend") {
		OpenWindow('<%=root%>/message/message!tran.action?msgId=' + msgId,
				'700', '500', window);
	}
}

//导出邮件
function exportMail(temp) {
	var mailId = getValue();
	//var temp;//1表示eml,2表示txt
	if (mailId == null || mailId == "") {
		alert("请选择要导出的记录。");
	} else if(mailId.split(",").length>100){
		alert("一次最多只能导出100条记录。");
	}else {
		if (temp == 2) {
			$("#exportInput").val(mailId);
			$("#exportForm").attr("action","<%=root%>/message/message!exportMail.action");
			$("#exportForm").submit();
			//window.parent.msg_main_content.personal_status_content.location = "<%=root%>/message/message!exportMail.action?mailId="
			//		+ mailId;
		} else if (temp == 1) {
			
			$("#exportInput").val(mailId);
			$("#exportForm").attr("action","<%=root%>/message/message!exportMails.action");
			$("#exportForm").submit();
			//window.parent.msg_main_content.personal_status_content.location = "<%=root%>/message/message!exportMails.action?mailId="
				//	+ mailId;
		}
	}
}

function quicklyVeiwMail(object) {

	var msgId = object.value;
	if (msgId != null && msgId != "" && msgId != "null" && !infoFlag) {
		window.parent.msg_main_content.personal_status_content.location = '<%=root%>/message/message!quicklyview.action?forward=quicklyview&msgId=' + msgId
		$('img:eq(0)', object)
				.attr("src", "<%=path%>/oa/image/mymail/read.gif");
		parent.parent.msg_manager_tree.freashUnread("${folderId }");
	} else {
		window.parent.msg_main_content.personal_status_content.location = "<%=root%>/fileNameRedirectAction.action?toPage=message/info.jsp";
		infoFlag = false;
	}

}

function gotoWrite() {
<%--	var boo=OpenWindow('<%=root%>/fileNameRedirectAction.action?toPage=message/message-write.jsp', '700', '500', window);				--%>
	var boo=OpenWindow('<%=root%>/message/message!view.action?forward=write&moduleCode=<%=GlobalBaseData.SMSCODE_MESSAGE%>', '700', '500', window);	
	if(boo=="true"){
		window.location.reload();
	}else if(boo=="false"){
	}
	window.parent.parent.msg_manager_tree.freashAllUnread();
}

function submitForm(){
	document.getElementById("myTableForm").submit();
}

</script>
	</BODY>
</HTML>
