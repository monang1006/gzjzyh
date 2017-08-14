<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="com.strongit.oa.util.GlobalBaseData"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<HTML>
	<HEAD>
		<%@include file="/common/include/meta.jsp"%>
		<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css"
			type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language='javascript'
			src="<%=path%>/common/js/common/common.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<%--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		
		--%>

		<TITLE>手机短信发送记录列表</TITLE>

		<script type="text/javascript">
		
		function add(){
			var url = "<%=path%>/systemset/systemset!imeiInput.action";
			var a = window.showModalDialog(url,window,'dialogWidth:450pt;dialogHeight:295pt;help:no;status:no;scroll:no');
			if(a=="OK"){
				window.location="<%=path%>/systemset/systemset!imeiSettingList.action?dt=" + new Date();
			}
		}
		
		function edit(){
			var id = getValue();
			var url = "<%=path%>/systemset/systemset!imeiInput.action?imei_Id="+id;
			var a = window.showModalDialog(url,window,'dialogWidth:450pt;dialogHeight:295pt;help:no;status:no;scroll:no');
			if(a=="OK"){
				window.location="<%=path%>/systemset/systemset!imeiSettingList.action?dt=" + new Date();
			}
		}
		
		//删除
		function gotoRemove(){
			var id = getValue();
			if(id==null|id==""){
				alert("请选择要删除的IMEI记录。");
				return ;
			}
			var url = "<%=path%>/systemset/systemset!deleteIMEI.action";
			if(confirm("删除此IMEI记录，确定？")){
					$.ajax({
						type:"post",
						url:url,
						data:{
							imei_Id:id
						},
						success:function(data){
							if(data!="" && data!=null){
								alert(data);					
							}else{
								//parent.project_work_content.document.location.reload() ;
								// alert("删除成功");
								location.reload() ;
							}
						},
						error:function(data){
							alert("对不起，操作异常。"+data);
						}
				   });
				}
		}
		
		function configsms(){
			location = "<%=path%>/collaborative_tools/sms/sms_config.jsp";
		}
		
		function confinesms(){
			location = "<%=path%>/collaborative_tools/sms/sms_sendconfine_list.jsp";
		}
		
		$(document).ready(function(){
			//搜索脚本
				$("#img_sousuo").click(function(){
					$("form").submit();
					gotoPage(1);
				});
				
			}); 
			
		function formatNum(num){
			if("null"==num){
				return "未设置";
			}else{
				return num;
			}
		}
		</script>

		<style>
.smsSendTime {
	width: 150px;
}
</style>

	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">

									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img">
												<img src="<%=frameroot%>/images/ico/ico.gif">
												&nbsp;
											</td>
											<td align="left">
												<strong>IMEI码列表</strong>
											</td>

											<td width="70%">
												<table border="0" align="right" cellpadding="00"
													cellspacing="0">
													<tr>
														<td width="4">
															<img src="<%=frameroot%>/images/bt_l.jpg" />
														</td>
														<td class="Operation_list" onclick="javascript:add();">
															<img
																src="<%=root%>/images/operationbtn/Send_text_messages.png" />
															&nbsp;新&nbsp;建&nbsp;
														</td>
														<td width="5"></td>
														<td width="4">
															<img src="<%=frameroot%>/images/bt_l.jpg" />
														</td>
														<td class="Operation_list" onclick="edit();">
															<img
																src="<%=root%>/images/operationbtn/Consult_the_reply.png" />
															&nbsp;编&nbsp;辑&nbsp;
														</td>
														<td width="4">
															<img src="<%=frameroot%>/images/bt_r.jpg" />
														</td>
														<td width="5"></td>
														<%--<td ><a class="Operation" href="javascript:gotoView();"><img src="<%=root%>/images/operationbtn/view.png" width="15" height="15" class="img_s">查阅&nbsp;</a></td>
								                 	<td width="5"></td>
								                 	--%>
														<td width="4">
															<img src="<%=frameroot%>/images/bt_l.jpg" />
														</td>
														<td class="Operation_list" onclick="gotoRemove();">
															<img src="<%=root%>/images/operationbtn/del.png" />
															&nbsp;删&nbsp;除&nbsp;
														</td>
														<td width="4">
															<img src="<%=frameroot%>/images/bt_r.jpg" />
														</td>
														<td width="5"></td>

														<%--<td ><a class="Operation" href="javascript:gotoRemove();"><img src="<%=root%>/images/operationbtn/del.png" width="15" height="15" class="img_s">删除&nbsp;</a></td>
								                 	<td width="5"></td>
								                --%>
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
						<s:form id="myTableForm" action="systemset!imeiSettingList.action" method="get">
							<webflex:flexTable name="myTable" width="100%" height="365px"
								wholeCss="table1" property="smsId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty"
								collection="${imeiPage.result}" page="${imeiPage}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
											<div style="float: left;">
												&nbsp;&nbsp;用户名：&nbsp;
												<input name="userNameParam" id="userNameParam"
													type="text" class="search" title="请您输入用户名"
													value="${userNameParam}">
											</div>
											<div style="float: left;width: 280px;">
												&nbsp;&nbsp;手机IMEI码：&nbsp;
												<input name="imeiCode" id="imeiCode" type="text"
													class="search" title="输入IMEI码" value="${imeiCode}">
											</div>
											<div style="float: left; width: 160px;">
												&nbsp;&nbsp;开启状态：&nbsp;
												<s:select name="isOpen"
													list="#{'':'全部','1':'开启','0':'关闭'}" listKey="key"
													listValue="value" onchange='$("#img_sousuo").click();' />
											</div>
											<div style="float: left; width: 270px;">
												<input id="img_sousuo" type="button" />
											</div>
										</td>
									</tr>

								</table>
								<webflex:flexCheckBoxCol caption="选择" property="imeiId"
									showValue="imeiId" width="4%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol align="center" caption="用户名"
									property="userId" showsize="6" showValue="userName"
									width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol align="center" caption="手机IMEI"
									property="iemiCode" showsize="15"
									showValue="iemiCode" width="38%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol align="center" caption="开启状态" mapobj="${stateMap}" property="isOpen" showValue="isOpen" 
									width="4%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
			item = new MenuItem("<%=root%>/images/operationbtn/Send_text_messages.png","新建","add",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/Send_again.png","编辑","edit",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>

	</BODY>
</HTML>
