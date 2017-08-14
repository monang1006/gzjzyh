<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
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
	<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script language='javascript' src="<%=path%>/common/js/common/common.js" ></script>
	<!--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>-->
	
		<TITLE>日程事务活动列表</TITLE>
			
		<script type="text/javascript">
		$(document).ready(function(){
		});
		//查看
		function gotoView(){
			var id = getValue();
			view(id);
		}
		function view(id){
			var ifleader = document.getElementById("ifleader").value;
			if(id==null|id==""){
				alert("请选择要查看的记录。");
			}else if(id.indexOf(",")>0){
				alert("只可以查看一条记录。");
			}else{
			var url = "<%=path%>/calendar/calendar!input.action?ifleader="+ifleader+"&calendarId="+id;
			var a = window.showModalDialog(url,window,'dialogWidth:700px;dialogHeight:580px;help:no;status:no;scroll:no');
			if("reload"==a){
				window.location.reload();
			}
			if(undefined!=a){
				if(a.indexOf("reload")==0){
					window.location.reload();
				}
			}
			}
		}
		
		//跳转回视图
		function gotoCal(){
			location = "<%=path%>/calendar/calendar.action?ifLeader="+ifleader.value;
		}
		
		
		function gotoRemove1(){
			var id = getValue();
			if(id==null|id==""){
				alert("请选择要删除的活动安排。");
			}else{
				location = "<%=path%>/calendar/calendar!delete.action?ifleader="+ifleader.value+"&calendarId="+id+"&inputType=list";
			}
		}

		function removeUser(){
			var id = getValue();
			if(id==null|id==""){
				alert("请选择要删除的记录。");
			}else{
				if(confirm("确定要删除吗？")){
					var url = "<%=path%>/calendar/calendar!deleteUser.action?assignUserId="+id+"&treeUserId="+$("#treeUserId").val();
					$.post(url);
					window.location.reload();
					//$("#myTableForm").submit();
					
				}
			}
		}

		//添加新授权成员
			function addUser(){
				var txtvalue= $("#treeUserId").val();
				if(txtvalue==""){
					alert("请从左边树形用户列表选择一个用户。")
					return;
				}
				var url = "<%=path%>/calendar/calendar!assignList.action?treeUserId="+txtvalue;
				
				var a = window.showModalDialog(url,window,'dialogWidth:870px;dialogHeight:630px;help:no;status:no;scroll:no');
				//alert("aaa="+a);
				//window.close();
				//if(undefined==a){
					//window.close();
				//}
				if("reload"==a){
					window.location.reload();
				}
				//if(undefined!=a){
					//if(a.indexOf("reload")==0){
						//window.location.reload();
					//}
				//}
			}

		
			
		function hasRemind(rem){
			if("1"==rem){
				return "有";
			}else{
				return "无";			
			}
		}
		
		
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"style='overflow:auto;' onload=initMenuT()>	
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				 <tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								   <strong>代办用户列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addUser();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="removeUser();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
				 <tr>
				 	<td>
						<s:form id="myTableForm" action="/calendar/calendar!assignUserList.action" method="get">
						<input type="hidden" name="treeUserId" id="treeUserId" value="${treeUserId}">
						
						<webflex:flexTable name="myTable" showSearch="false" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" pagename="assignPage" collection="${assignPage.result}" page="${assignPage}">
						  	<webflex:flexCheckBoxCol caption="选择" valuepos="1" valueshowpos="0" width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="用户名"   valuepos="0" valueshowpos="0" width="95%" isCanDrag="true" isCanSort="true" showsize="18"></webflex:flexTextCol> <!-- 去掉onclick="view(this.value)" -->
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
			item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addUser",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","removeUser",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		</script>
	</BODY>
</HTML>
