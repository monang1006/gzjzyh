<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
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
		
		--%><TITLE>手机短信发送权限列表</TITLE>
		
					
		<script type="text/javascript">
		//搜索
		$(document).ready(function(){
				$("#img_sousuo").click(function(){
					$("form").submit();
				});
			});
		
		//选择用户
		function addperson(){
			var url = "<%=path%>/smscontrol/smscontrol!input.action";
			var a =OpenWindow(url, '360', '120', window);
			if("reload"==a){
				//alert("已提交服务器发送");
				//document.location.reload();
				location = "<%=path%>/smscontrol/smscontrol.action";
			}
		}
		
		//开启用户权限
		function satrts(){
			var ids = getValue();
			if(ids==""|ids==null){
				alert("请选择要开启权限的记录。");
			}else{
				if(confirm("开启是指工作流中的信息提醒方式允许选择短信提醒，确定要开启吗？")){
				var url = "<%=path%>/smscontrol/smscontrol!openRight.action";
				$.ajax({
					type:"post",
					url:url,
					data:{
						controlId:ids
					},
					success:function(data){
						if(data=="success"){
							$("form").submit();
						}else{
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			    	});
				}
			}
			
		}
		
		//关闭用户权限
		function closes(){
			var ids = getValue();
			if(ids==""|ids==null){
				alert("请选择要禁用权限的记录。");
			}else{
				var url = "<%=path%>/smscontrol/smscontrol!closeRight.action";
				$.ajax({
					type:"post",
					url:url,
					data:{
						controlId:ids
					},
					success:function(data){
						if(data=="success"){
							$("form").submit();
						}else{
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			    });
			}
		}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<%--<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td width="30%" align="left">
												&nbsp;<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												手机短信权限列表
											</td>
											<td>
												&nbsp;
											</td>
											--%>
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
								                    <strong>手机短信权限列表</strong>
							                    </td>
											
											
											
											
											<%-- <td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                 	<td ><a class="Operation" href="javascript:addperson();"><img src="<%=root%>/images/ico/user_add.gif" width="15" height="15" class="img_s">添加用户&nbsp;</a></td>
								                 	<td width="5"></td>
							                  		<td ><a class="Operation" href="javascript:open();"><img src="<%=root%>/images/ico/queding.gif" width="15" height="15" class="img_s">开启&nbsp;</a></td>
								                 	<td width="5"></td>
								                 	<td ><a class="Operation" href="javascript:close();"><img src="<%=root%>/images/ico/quxiao.gif" width="15" height="15" class="img_s">禁用&nbsp;</a></td>
								                 	<td width="5"></td>
								                </tr>
								            </table>
											</td> --%>
									
										<td width="70%">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	        <td class="Operation_list" onclick="addperson();"><img src="<%=root%>/images/operationbtn/Add_user.png"/>&nbsp;添&nbsp;加&nbsp;用&nbsp;户&nbsp;</td>
					                 	        <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		        <td width="5"></td>
								                 	
								                	
								                	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="satrts();"><img src="<%=root%>/images/operationbtn/Send_again.png"/>&nbsp;开&nbsp;启&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		            <td width="5"></td>
								                	
							                  		
							                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	            <td class="Operation_list" onclick="closes();"><img src="<%=root%>/images/operationbtn/Consult_the_reply.png"/>&nbsp;禁&nbsp;用&nbsp;</td>
					                 	            <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
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
						<s:form id="myTableForm" action="smscontrol!search.action">
						<input type="hidden" name="inputType" value="${inputType}">
						<webflex:flexTable name="myTable" width="100%" height="365px" wholeCss="table1" property="smsId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
					        <%-- <tr>
					          <td width="5%" align="center"  class="biao_bg1"><img id="img_sousuo" style="cursor: hand;" src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" width="15" height="15"></td>
					          <td width="35%"  class="biao_bg1"><input name="model.smscontrolDepartment" id="smscontrolDepartment" type="text" style="width=100%" class="search" title="输入部门" value="${model.smscontrolDepartment }"></td>
					          <td width="35%"  class="biao_bg1"><input name="model.smscontrolUserName" id="smscontrolUserName" type="text" style="width=100%" class="search" title="输入姓名" value="${model.smscontrolUserName }"></td>
					          <td width="25%"  class="biao_bg1">
					            <s:select name="model.smsSendRight" list="#{'':'全部','1':'开启','0':'禁用'}" listKey="key" listValue="value" style="width:100%" onchange='$("#img_sousuo").click();'/>
					          </td>
					          <td class="biao_bg1">&nbsp;</td>
					          </tr> --%>
					          <tr>
							       <td>
							       		&nbsp;&nbsp;部门：&nbsp;<input name="model.smscontrolDepartment" id="smscontrolDepartment" type="text" class="search"  title="输入部门" value="${model.smscontrolDepartment }">
							       		&nbsp;&nbsp;用户：&nbsp;<input name="model.smscontrolUserName" id="smscontrolUserName" type="text"  class="search"  title="输入姓名" value="${model.smscontrolUserName }">
							       		&nbsp;&nbsp;发送权限：&nbsp; <s:select name="model.smsSendRight"  list="#{'':'全部','1':'开启','0':'禁用'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
					          
					          
					          
					          
					        </table>
							<webflex:flexCheckBoxCol caption="选择" property="smscontrolId"
								showValue="smscontrolUserName" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="部门" property="smscontrolDepartment"
								showValue="smscontrolDepartment" width="35%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="用户" property="smscontrolUserName"
								showValue="smscontrolUserName" width="35%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexEnumCol caption="发送权限" mapobj="${statemap}"  property="smsSendRight"
								showValue="smsSendRight" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
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
				item = new MenuItem("<%=root%>/images/operationbtn/Add_user.png","添加用户","addperson",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Send_again.png","开启","satrts",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Consult_the_reply.png","禁用","closes",3,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		</script>	
	</BODY>
</HTML>
