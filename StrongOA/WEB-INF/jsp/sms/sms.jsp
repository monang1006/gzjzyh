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
	<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
	<%--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>
		
		--%>
		
		<TITLE>手机短信发送记录列表</TITLE>
			
		<script type="text/javascript">
		
		function changeName(modelName){
			if(""!=modelName&&null!=modelName&&"null"!=modelName){
				return modelName;
			}else{
				return "短信平台";
			}
		}
		
		function sendsms(){
			var url = "<%=path%>/sms/sms!input.action?moduleCode=<%=GlobalBaseData.SMSCODE_SMS %>";
			var a = window.showModalDialog(url,window,'dialogWidth:450pt;dialogHeight:295pt;help:no;status:no;scroll:no');
			if("reload"==a){
				//location= "<%=path%>/sms/sms!allList.action";
				//alert("已提交服务器发送");
				//document.location.reload();
				window.location.reload();
			}
		}
		
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
				var url = "<%=path%>/sms/sms!view.action?smsId="+id;
				var a = window.showModalDialog(url,window,'dialogWidth:900px;dialogHeight:350px;help:no;status:no;scroll:no');
				if("reload"==a){
					document.location.reload();
				}else if("resend"==a){
					reSend(id);
				}
			}
		}
		
		function goReSend(){
			var id = getValue();
			reSend(id);
		}
		//重新发送
		function reSend(id){
			if(id==null|id==""){
				alert("请选择要重新发送的短信记录。");
			} else if(id.indexOf(",")!=-1){
				//alert("一次只能重新发送一条短信！");
				var pno = id.split(",");
				if(confirm("已选择的"+pno.length+"条短信记录，确定全部重新发送？")){
					var url = "<%=path%>/sms/sms!reSends.action";
					$.ajax({ 	type:"post",
								url:url,
								data:{
								smsId:id
								},
							success:function(data){
								if(data=="succ"){
									location.reload();
								}else if(data=="del"){
									alert("用户已删除或已被移动到别的单位，不能重新发送该短信记录。");
								}else{
									alert(data);					
								}
							},
							error:function(data){
								alert("对不起，操作异常。"+data);
							}
				   });
				}
			} else{
				var url = "<%=path%>/sms/sms!reSends1.action";
				$.ajax({	type:"post",
							url:url,
							data:{
								smsId:id
							},
						success:function(data){
							if(data=="del"){
								alert("用户已删除或已被移动到别的单位，不能重新发送该短信记录。");
							}else if(data=="succ"){
								var url = "<%=path%>/sms/sms!reSend.action?smsId="+id+"&moduleCode=<%=GlobalBaseData.SMSCODE_SMS %>";
								var a = window.showModalDialog(url,window,'dialogWidth:450pt;dialogHeight:295pt;help:no;status:no;scroll:no');
									if("reload"==a){
										//document.location.reload();
										window.location.reload();
									}
							}else{
								alert(data);
							}
						},
						error:function(data){
							alert("对不起，操作异常。"+data);
						}
				});
				<%-- var url = "<%=path%>/sms/sms!reSend.action?smsId="+id+"&moduleCode=<%=GlobalBaseData.SMSCODE_SMS %>";
				var a = window.showModalDialog(url,window,'dialogWidth:400pt;dialogHeight:350pt;help:no;status:no;scroll:no');
				if("reload"==a){
					document.location.reload();
				} --%>
			}
		}
		
		//删除
		function gotoRemove(){
			var id = getValue();
			if(id==null|id==""){
				alert("请选择要删除的短信记录。");
				return ;
			}
			var url = "<%=path%>/sms/sms!delete.action";
			if(confirm("删除此短信记录，确定？")){
					$.ajax({
						type:"post",
						url:url,
						data:{
							smsId:id
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
					$("#smsRecver").val(encodeURI($("#searchsmsRecver").val()));
					$("#smsRecvnum").val(encodeURI($("#searchsmsRecvnum").val()));
					$("#smsCon").val(encodeURI($("#searchsmsCon").val()));
					$("#modelName").val(encodeURI($("#searchmodelName").val()));
					$("#smsSenderName").val(encodeURI($("#searchsmsSenderName").val()));
					$("form").submit();
				});
				
				$("#smsRecver").val(encodeURI($("#searchsmsRecver").val()));
				$("#smsRecvnum").val(encodeURI($("#searchsmsRecvnum").val()));
				$("#smsCon").val(encodeURI($("#searchsmsCon").val()));
				$("#modelName").val(encodeURI($("#searchmodelName").val()));
				$("#smsSenderName").val(encodeURI($("#searchsmsSenderName").val()));
				
			}); 
			
		function formatNum(num){
			if("null"==num){
				return "未设置";
			}else{
				return num;
			}
		}
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
								                    <strong>手机短信发送记录</strong>
							                    </td>
											
											<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	        <td class="Operation_list" onclick="sendsms();"><img src="<%=root%>/images/operationbtn/Send_text_messages.png"/>&nbsp;发&nbsp;送&nbsp;短&nbsp;信&nbsp;</td>
					                 	        <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		        <td width="5"></td>
								                 	
								                	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="goReSend();"><img src="<%=root%>/images/operationbtn/Send_again.png"/>&nbsp;重&nbsp;新&nbsp;发&nbsp;送&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                	<%--<td ><a class="Operation" href="javascript:goReSend();"><img src="<%=root%>/images/operationbtn/public.png" width="15" height="15" class="img_s">重新发送&nbsp;</a></td>
								                 	<td width="5"></td>
							                  		--%>
							                  		
							                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/Consult_the_reply.png"/>&nbsp;查&nbsp;阅&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
							                  		<%--<td ><a class="Operation" href="javascript:gotoView();"><img src="<%=root%>/images/operationbtn/view.png" width="15" height="15" class="img_s">查阅&nbsp;</a></td>
								                 	<td width="5"></td>
								                 	--%>
								                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="gotoRemove();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
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
						<s:form id="myTableForm" action="sms!search.action" method="get">
						<input type="hidden" name="inputType" value="${inputType}">
						<input type="hidden" name="model.smsRecver" id="smsRecver" value="${model.smsRecver}">
						<input type="hidden" name="model.smsRecvnum" id="smsRecvnum" value="${model.smsRecvnum}">
						<input type="hidden" name="model.smsCon" id="smsCon" value="${model.smsCon}">
						<input type="hidden" name="model.modelName" id="modelName" value="${model.modelName}">
						<input type="hidden" name="model.smsSenderName" id="smsSenderName" value="${model.smsSenderName}">
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="smsId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							
							<s:if test="inputType=='list'">
								<%--<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
						         	 <tr>
								       <td width="3.5%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="15" height="15" ></td>
								       <td width="10%"  class="biao_bg1"><input name="searchsmsRecver" id="searchsmsRecver" type="text" style="width=100%" class="search" title="输入姓名" value="${model.smsRecver }"></td>
								       <td width="14%"  class="biao_bg1"><input name="searchsmsRecvnum" id="searchsmsRecvnum" type="text" style="width=100%" class="search" title="输入号码" value="${model.smsRecvnum }"></td>
								       <td width="28%"  class="biao_bg1"><input name="searchsmsCon" id="searchsmsCon" type="text" style="width=100%" class="search" title="输入短信内容" value="${model.smsCon }"></td>
								       <td width="16%" align="center" class="biao_bg1"><strong:newdate dateform="yyyy-MM-dd" name="model.smsSendTime" id="smsSendTime" dateobj="${model.smsSendTime}" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="发送时间"/></td>
								       <td width="8%"  class="biao_bg1"><input name="searchmodelName" id="searchmodelName" type="text" style="width=100%" class="search" title="业务类型" value="${model.modelName }"></td>
								       <td width="8%" align="center" class="biao_bg1"><s:select name="model.smsState" list="#{'':'全部','1':'已发送','0':'未发送'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
								       <td width="15%" align="center" class="biao_bg1"><s:select name="model.smsServerRet" list="#{'':'全部','0':'未发送','1':'发送成功','2':'发送失败'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
								       <td class="biao_bg1">&nbsp;</td>
								     </tr>
						        </table>
								
								 
								 --%>
								 
								 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td >
							       		<div style="float:left;">&nbsp;&nbsp;接收人：&nbsp;<input name="searchsmsRecver" id="searchsmsRecver" type="text" class="search" title="请您输入接收人姓名" value="${model.smsRecver }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;手机号码：&nbsp;<input name="searchsmsRecvnum" id="searchsmsRecvnum" type="text"  class="search" maxlength="20" title="输入号码" value="${model.smsRecvnum }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;短信内容：&nbsp;<input name="searchsmsCon" id="searchsmsCon" type="text"  class="search"  title="输入短信内容" value="${model.smsCon }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;发送时间：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.smsSendTime" id="smsSendTime" dateobj="${model.smsSendTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="发送时间"/>
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;业务类型：&nbsp;<input name="searchmodelName" id="searchmodelName" type="text"  class="search" title="业务类型" value="${model.modelName }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;发送状态：&nbsp;<s:select name="model.smsState" list="#{'':'全部','1':'已发送','0':'未发送'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		</div>
							       		<div style="float:left;width:270px;">&nbsp;&nbsp;返回状态：&nbsp;<s:select name="model.smsServerRet" list="#{'':'全部','0':'未发送','1':'发送成功','2':'发送失败'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" /></div>
							       		<%--&nbsp;&nbsp;接收人：&nbsp;<input name="searchsmsRecver" id="searchsmsRecver" style="width:80" type="text" class="search" title="请您输入接收人姓名" value="${model.smsRecver }">
							       		
							       		&nbsp;&nbsp;手机号码：&nbsp;<input name="searchsmsRecvnum" id="searchsmsRecvnum" style="width:100" type="text"  class="search"  title="输入号码" value="${model.smsRecvnum }">
							       		
							       		&nbsp;&nbsp;短信内容：&nbsp;<input name="searchsmsCon" id="searchsmsCon" type="text" style="width:120"  class="search"  title="输入短信内容" value="${model.smsCon }">
							       		
							       		&nbsp;&nbsp;发送时间：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.smsSendTime" id="smsSendTime" dateobj="${model.smsSendTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="发送时间"/>
							       		<br>
							       		&nbsp;&nbsp;业务类型：&nbsp;<input name="searchmodelName" id="searchmodelName" style="width:120" type="text"  class="search" title="业务类型" value="${model.modelName }">
							       		
							       		
							       		&nbsp;&nbsp;发送状态：&nbsp;<s:select name="model.smsState" list="#{'':'全部','1':'已发送','0':'未发送'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		
							       		&nbsp;&nbsp;返回状态：&nbsp;<s:select name="model.smsServerRet" list="#{'':'全部','0':'未发送','1':'发送成功','2':'发送失败'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		--%>
							       	</td>
							     </tr>
							     
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="smsId" showValue="smsRecver" 
									width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol align="center" caption="接收人" property="smsRecver" showsize="6" showValue="smsRecver" 
									width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol align="center" caption="手机号码" property="smsRecvnum" showsize="15" showValue="javascript:formatNum(smsRecvnum)" 
									width="14%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol  caption="短信内容" property="smsId" showsize="16" onclick="viewSms(this.value)" showValue="smsCon" 
									width="30%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol  caption="发送时间" property="smsSendTime" showsize="20" dateFormat="yyyy-MM-dd HH:mm:ss" showValue="smsSendTime" 
									width="16%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol align="center" caption="业务类型" property="modelName" showsize="15" showValue="javascript:changeName(modelName);" 
									width="8%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol align="center" caption="发送状态" mapobj="${statemap}" property="smsState" showValue="smsState" 
									width="8%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol align="center" caption="返回状态" mapobj="${typemap}" property="smsServerRet"  showValue="smsServerRet" 
									width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</s:if>
							<s:else>
								<%--<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
						         	 <tr>
								       <td width="4%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="15" height="15"></td>
								       <td width="10%"  class="biao_bg1"><input name="searchsmsRecver" id="searchsmsRecver" type="text" style="width=100%" class="search" title="输入姓名" value="${model.smsRecver }"></td>
								       <td width="12%"  class="biao_bg1"><input name="searchsmsRecvnum" id="searchsmsRecvnum" type="text" style="width=100%" class="search" title="输入号码" value="${model.smsRecvnum }"></td>
								       <td width="24%"  class="biao_bg1"><input name="searchsmsCon" id="searchsmsCon" type="text" style="width=100%" class="search" title="输入短信内容" value="${model.smsCon }"></td>
								       <td width="16%" align="center" class="biao_bg1"><strong:newdate dateform="yyyy-MM-dd" name="model.smsSendTime" id="smsSendTime" dateobj="${model.smsSendTime}" width="100%" skin="whyGreen" isicon="true"  classtyle="search" title="发送时间"/></td>
								       <td width="8%"  class="biao_bg1"><input name="searchmodelName" id="searchmodelName" type="text" style="width=100%" class="search" title="业务类型" value="${model.modelName }"></td>
								       <td width="8%"  class="biao_bg1"><input name="searchsmsSenderName" id="searchsmsSenderName" type="text" style="width=100%" class="search" title="发送人" value="${model.smsSenderName }"></td>
								       <td width="8%" align="center" class="biao_bg1"><s:select name="model.smsState" list="#{'':'全部','1':'已发送','0':'未发送'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
								       <td width="10%" align="center" class="biao_bg1"><s:select name="model.smsServerRet" list="#{'':'全部','0':'未发送','1':'发送成功','2':'发送失败'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/></td>
								       <td class="biao_bg1">&nbsp;</td>
								     </tr>
						        </table>
								--%>
								 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		<div style="float:left;">&nbsp;&nbsp;接收人：&nbsp;<input name="searchsmsRecver" id="searchsmsRecver" type="text" class="search" title="请您输入接收人姓名" value="${model.smsRecver }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;发送人：&nbsp;<input name="searchsmsSenderName" id="searchsmsSenderName" type="text" class="search" title="发送人" value="${model.smsSenderName }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;手机号码：&nbsp;<input name="searchsmsRecvnum" id="searchsmsRecvnum" type="text"  class="search"  title="输入号码" value="${model.smsRecvnum }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;短信内容：&nbsp;<input name="searchsmsCon" id="searchsmsCon" type="text"  class="search"  title="输入短信内容" value="${model.smsCon }">
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;发送时间：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.smsSendTime" id="smsSendTime" dateobj="${model.smsSendTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="发送时间"/>
							       		</div>
							       		<div style="float:left;">&nbsp;&nbsp;业务类型：&nbsp;<input name="searchmodelName" id="searchmodelName" type="text"  class="search" title="业务类型" value="${model.modelName }">
							       		</div>
							       		<div style="float:left;width:155px;">&nbsp;&nbsp;发送状态：&nbsp;<s:select name="model.smsState" list="#{'':'全部','1':'已发送','0':'未发送'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		</div>
							       		<div style="float:left;width:270px;">&nbsp;&nbsp;返回状态：&nbsp;<s:select name="model.smsServerRet" list="#{'':'全部','0':'未发送','1':'发送成功','2':'发送失败'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	    </div><%--
							       	    &nbsp;&nbsp;接收人：&nbsp;<input name="searchsmsRecver" id="searchsmsRecver" style="width:80" type="text" class="search" title="请您输入接收人姓名" value="${model.smsRecver }">
							       		
							       		&nbsp;&nbsp;手机号码：&nbsp;<input name="searchsmsRecvnum" id="searchsmsRecvnum" style="width:100" type="text"  class="search"  title="输入号码" value="${model.smsRecvnum }">
							       		
							       		&nbsp;&nbsp;短信内容：&nbsp;<input name="searchsmsCon" id="searchsmsCon" type="text" style="width:120"  class="search"  title="输入短信内容" value="${model.smsCon }">
							       		
							       		&nbsp;&nbsp;发送时间：&nbsp;<strong:newdate dateform="yyyy-MM-dd" name="model.smsSendTime" id="smsSendTime" dateobj="${model.smsSendTime}"  skin="whyGreen" isicon="true"  classtyle="search" title="发送时间"/>
							       		<br>
							       		&nbsp;&nbsp;业务类型：&nbsp;<input name="searchmodelName" id="searchmodelName" style="width:120" type="text"  class="search" title="业务类型" value="${model.modelName }">
							       		
							       		
							       		&nbsp;&nbsp;发送状态：&nbsp;<s:select name="model.smsState" list="#{'':'全部','1':'已发送','0':'未发送'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		
							       		&nbsp;&nbsp;返回状态：&nbsp;<s:select name="model.smsServerRet" list="#{'':'全部','0':'未发送','1':'发送成功','2':'发送失败'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	--%></td>
							     </tr>
							     
							</table>
								
								<webflex:flexCheckBoxCol caption="选择" property="smsId" showValue="smsRecver" 
									width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="接收人" property="smsRecver" showsize="6" showValue="smsRecver" 
									width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="手机号码" property="smsRecvnum" showsize="15" showValue="javascript:formatNum(smsRecvnum)" 
									width="12%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="短信内容" property="smsId" showsize="16" onclick="viewSms(this.value)" showValue="smsCon" 
									width="24%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="发送时间" property="smsSendTime" showsize="20" dateFormat="yyyy-MM-dd HH:mm:ss" showValue="smsSendTime" 
									width="16%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="业务类型" property="modelName" showsize="15" showValue="javascript:changeName(modelName);" 
									width="8%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="发送人" property="smsSenderName" showsize="5" showValue="smsSenderName" 
									width="8%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="发送状态" mapobj="${statemap}" property="smsState" showValue="smsState" 
									width="8%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexEnumCol caption="返回状态" mapobj="${typemap}" property="smsServerRet"  showValue="smsServerRet" 
									width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</s:else>
							
							
							
							
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
			item = new MenuItem("<%=root%>/images/operationbtn/Send_text_messages.png","发送短信","sendsms",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/Send_again.png","重新发送","goReSend",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/Consult_the_reply.png","查阅","gotoView",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>
		 
	</BODY>
</HTML>
