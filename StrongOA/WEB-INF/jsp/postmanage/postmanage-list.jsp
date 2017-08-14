<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@	taglib prefix="s" uri="/struts-tags"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<!-- 
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		 -->
		<script type="text/javascript">
		/**
		* 设置权限
		* @author 邓志城
		* @date 2009年5月14日10:17:18 
		*/
		function setOptPrivil(){
			var id = getValue();
			if(id == ""){
				alert("请选择要设置权限的记录。");
				return ;
			}else{
				if(id.split(",").length>1){
					alert("只可以对一条记录设置权限。");
					return ;
				}
				window.showModalDialog('<%=path%>/optprivilmanage/baseOptPrivil!getPostOptPrivil.action?postId='+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:500px');
			}
		}
		
		function setDataPrivil(){
			var id = getValue();
			if(id == ""){
				alert("请选择要设置数据权限的记录。");
				return ;
			}else{
				if(id.split(",").length>1){
					alert("只可以对一条记录设置数据权限。");
					return ;
				}
				window.location="<%=root%>/dataprivil/postDataPrivil.action?postId="+id;
			}
		}
		/**
		* 权限复制
		*/
		function copyOptPrivil(){
			
		}
	</script>
	
	
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/postmanage/postContent!list.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td class="table_headtd_img">
													<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
												</td>
												<td algin="left">
													<strong>机构岗位列表</strong>
												</td>
												<td algin="right">
													<table border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
															<td class="Operation_list" onclick="addPost();"><img src="<%=root%>/images/operationbtn/add.png">&nbsp;新&nbsp;建&nbsp;</td>
															<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
															<td width="3"></td>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
															<td class="Operation_list" onclick="editPost();"><img src="<%=root%>/images/operationbtn/edit.png">&nbsp;编&nbsp;辑&nbsp;</td>
															<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
															<td width="3"></td>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
															<td class="Operation_list" onclick="deletePost();"><img src="<%=root%>/images/operationbtn/del.png">&nbsp;删&nbsp;除&nbsp;</td>
															<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
															<td width="3"></td>
															<%--<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
															<td class="Operation_list" onclick="setDataPrivil();"><img src="<%=root%>/images/operationbtn/Set_the_data_access.png">&nbsp;设置数据权限&nbsp;</td>
															<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
															<td width="3"></td>--%>
															<%--
															<td width="112">
																<a class="Operation" href="javascript:setOptPrivil();">
																	<img src="<%=root%>/images/ico/setprivil.gif"
																		width="15" height="15" class="img_s">设置操作权限</a>
															</td>
															<td width="5">
																&nbsp;
															</td>
															--%>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="postId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
									<tr>
										<td>
											&nbsp;&nbsp;岗位名称：&nbsp;<input name="postname" id="postname" type="text"  class="search" title="请输入岗位名称" value="${postname}">
								       		&nbsp;&nbsp;岗位说明：&nbsp;<input name="postdesc" id="postdesc" type="text"  class="search" title="请输入岗位说明" value="${postdesc}">
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button"/>
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="postId"
									showValue="postName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="岗位名称" property="postName"
									showValue="postName" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="岗位说明" property="postDescription"
									showValue="postDescription" width="60%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<%--<webflex:flexTextCol caption="分配人员" property="rest4"
									showValue="rest4" width="55%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>--%>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addPost",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editPost",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","deletePost",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				//item = new MenuItem("<%=root%>/images/operationbtn/Set_the_data_access.png","设置数据权限","setDataPrivil",1,"ChangeWidthTable","checkOneDis");
				//sMenu.addItem(item);
				//item = new MenuItem("<%=root%>/images/ico/setprivil.gif","设置操作权限","setOptPrivil",1,"ChangeWidthTable","checkOneDis");
				//sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			function addPost(){
				window.showModalDialog("<%=path%>/postmanage/postContent!input.action",window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:350px');	
			}
			
			function editPost(){
				var id=getValue();
				if(id==null || id==""){
					alert("请选择要编辑的记录。");
					return;
				}
				if(id.length>32){
					alert("只可以编辑一条记录。");
					return;
				}
				window.showModalDialog("<%=path%>/postmanage/postContent!input.action?wpId="+id,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:350px');		
			}
			
			function deletePost(){
				var id=getValue();
				if(id==null || id==""){
					alert("请选择要删除的记录。");
					return;
				}else{	
					if(confirm("确定要删除吗？")){
					location="<%=path%>/postmanage/postContent!delete.action?wpId="+id+",";
					}	
				}
			}
			
			function submitForm(){
				document.getElementById("myTableForm").submit();
			}
			
			$(document).ready(function(){
			        $("#img_sousuo").click(function(){
			        	$("form").submit();
			        	gotoPage(1);
			        });     
				});		      
		</script>
	</BODY>
</HTML>
