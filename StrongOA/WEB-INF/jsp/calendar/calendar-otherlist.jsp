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
	<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
	<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
	<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
	<script language='javascript' src="<%=path%>/common/js/common/common.js" ></script>
	<!--<script language='javascript' src='<%=path%>/common/js/common/search.js'></script>-->
	
		<TITLE>日程事务活动列表</TITLE>
			
		<script type="text/javascript">
		
		//查看
		function gotoView(){
			var id = getValue();
			view(id);
		}
		function view(id){
			//alert(id);
			var ifleader = document.getElementById("ifleader").value;
			if(id==null|id==""){
				alert("请选择要编辑记录。");
			}else if(id.indexOf(",")>0){
				alert("只可以编辑一条记录。");
			}else{
			var url = "<%=path%>/calendar/calendar!assigninput.action?ifleader="+ifleader+"&calendarId="+id;
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
			location = "<%=path%>/calendar/calendar!showlist.action?ifLeader="+ifleader.value;
		}
		
		
		function gotoRemove1(){
			var id = getValue();
			if(id==null|id==""){
				alert("请选择要删除的记录。");
			}else{
				location = "<%=path%>/calendar/calendar!delete.action?ifleader="+ifleader.value+"&calendarId="+id+"&inputType=list";
			}
		}

		function gotoRemove(){
			var id = getValue();
			var url = "<%=path%>/calendar/calendar!delete.action";
			if(id==null|id==""){
				alert("请选择要删除的记录。");
			}else{
			if(confirm("确定要删除吗？")){
					$.ajax({
						type:"post",
						url:url,
						data:{
							ifleader:ifleader.value,
							calendarId:id,
							inputType:'list'
						},
						success:function(data){
								if(data!="" && data!=null){
									alert(data);					
								}else{
									// alert("删除成功");
									location.reload() ;
								}
							},
						error:function(data){
							alert("对不起，操作异常"+data);
						}
				   });
				}
			}
		}

		//添加新事务
			function addTask(){
				var url = "<%=path%>/calendar/calendar!assigninput.action?ifleader="+ifleader.value;
				var a = window.showModalDialog(url,window,'dialogWidth:750px;dialogHeight:630px;help:no;status:no;scroll:no');
				if("reload"==a){
					window.location.reload();
				}
				if(undefined!=a){
					if(a.indexOf("reload")==0){
						window.location.reload();
					}
				}
			}

			$(document).ready(function(){
				$("#img_sousuo").click(function(){
					var startDate = document.getElementById("calStartTime").value;
					var endDate = document.getElementById("calEndTime").value;
					if((startDate!=""&&endDate!="")&&startDate>endDate){
						alert("开始日期不能大于结束日期。");
						return;
					}
					$("form").submit();
				});
			}); 
			
			
		function hasRemind(rem){
			if("1"==rem){
				return "有";
			}else{
				return "无";			
			}
		}
		
		
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<input type="hidden" id="ifleader" value="${ifleader}"/>
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
								<strong><script>
								          var input = "${inputType }";
												if(input=="leader"){
													window.document.write("查看领导日程列表");
												}
												else if(input=="share"){
													window.document.write("查看共享日程列表");
												}
												else if(input=="assign"){
													window.document.write("他人日程列表");
												}
												else{
													window.document.write("个人日程列表");
												}
 												</script></strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addTask();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="gotoView();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="gotoRemove();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="gotoCal();"><img src="<%=root%>/images/operationbtn/public.png"/>&nbsp;视&nbsp;图&nbsp;</td>
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
						<s:form id="myTableForm" action="calendar!otherlist.action?inputType=assign" method="post">
<%--						<input type="hidden" name="model.calTitle" id="calTitle" value="${model.calTitle}">--%>
<%--						<input type="hidden" name="model.toaCalendarActivity.activityName" id="activityName" value="${model.toaCalendarActivity.activityName }">--%>
<%--						<input type="hidden" name="model.calPlace" id="calPlace" value="${model.calPlace}">--%>
						<webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
					         	 <tr>
					         	   <td>
					         	  		 <div style="float: left; ">
							            &nbsp;&nbsp;活动主题：&nbsp;<input name="calTitle" style="width: 140px;" id="calTitle" type="text" class="search" title="请您输入活动主题" value="${calTitle}">
							            </div>
<%--							       		&nbsp;&nbsp;分类：&nbsp;<input name="searchactivityName" id="searchactivityName" type="text" class="search" title="请您输入分类" value="${model.toaCalendarActivity.activityName }">--%>
										 <div style="float: left;width: 200px;">
							       		&nbsp;&nbsp;开始日期：&nbsp;<strong:newdate  name="calStartTime" id="calStartTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入开始日期" dateform="yyyy-MM-dd" dateobj="${calStartTime}"/>
							       		</div>
							       		 <div style="float: left;width: 200px;">
							       		&nbsp;&nbsp;结束日期：&nbsp;<strong:newdate  name="calEndTime" id="calEndTime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入结束日期" dateform="yyyy-MM-dd" dateobj="${calEndTime}"/>
							       		</div>
							       		 <div style="float: left;">
							       		&nbsp;&nbsp;活动地点：&nbsp;<input  name="calPlace" style="width: 140px;" id="calPlace" type="text" class="search" title="请您输入活动地点" value="${calPlace }">
							       		</div>
							       		 <div style="float: left; width: 335px" >
							       		&nbsp;&nbsp;有无提醒：&nbsp;<s:select name="calHasRemind" style="width:70px;" list="#{'':'全部','0':'无','1':'有'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       	</td>
							     </tr>
					        </table>
						  	<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="1" width="4%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="活动主题" valuepos="0" valueshowpos="1" width="20%" isCanDrag="true" isCanSort="true" onclick="view(this.value)" showsize="18"></webflex:flexTextCol>
<%--							<webflex:flexTextCol caption="活动分类" valuepos="6" align="center" valueshowpos="6" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>--%>
							<webflex:flexDateCol caption="开始时间" valuepos="3" valueshowpos="3" width="14%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="20"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束时间" valuepos="4" valueshowpos="4" width="14%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd HH:mm" showsize="20"></webflex:flexDateCol>
							<webflex:flexTextCol caption="活动地点" valuepos="7" align="center" valueshowpos="7" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="有无提醒" valuepos="1" align="center" valueshowpos="javascript:hasRemind(8);" width="13%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						  	<webflex:flexTextCol caption="用户名" valuepos="5"  align="center" valueshowpos="5" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
			item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTask",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","gotoView",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","gotoRemove",3,"ChangeWidthTable","checkOneDis");
			sMenu.addItem(item);
			sMenu.addShowType("ChangeWidthTable");
		    registerMenu(sMenu);
		}
		
		</script>
		 
	</BODY>
</HTML>
