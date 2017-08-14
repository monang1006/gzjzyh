<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ page import="com.strongit.bo.ListTest"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<!-- saved from url=(0058)http://111.111.111.111:0000/chinaspis/perspective_toolbar.jsp -->
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<!--右键菜单样式 -->
		<script language='javascript' src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
			<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm" action="/basesystem/baseSystem.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER: progid :       DXImageTransform.Microsoft.Gradient (       gradientType =       0, startColorStr =       #ededed, endColorStr =       #ffffff );">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>外挂系统列表</strong>
												</td>
												<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
															<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="addTempFile();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="editTempFile();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="deleteTempFile();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
															<%--  <td width="4%"><img src="<%=root%>/images/ico/bianji.gif" width="14" height="15"></td>--%>
															<%--  <td width="18%"><a href="javascript:supper();">系统管理员设置</a></td>                --%>
														</tr>
													</table>
												</td>
												<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="id" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
									<tr>
										<td>
							    	   	<div style="float: left; ">
							       		&nbsp;&nbsp;系统编码：&nbsp;<input name="selectsystemid" id="selectsystemid" style="width: 140px;" type="text" class="search" title="请输入系统编码" value="${selectsystemid}"
							       		onKeyDown="javascript:if (event.keyCode==13) $('#img_sousuo').click();">
							       		</div>
							       		<div style="float: left; ">
							       		&nbsp;&nbsp;系统名称：&nbsp;<input name="selectsystemname" id="selectsystemname" style="width: 140px;" type="text" class="search" title="请输入系统名称" value="${selectsystemname}"
							       		onKeyDown="javascript:if (event.keyCode==13) $('#img_sousuo').click();">
							       		</div>
							       		<div style="float: left; padding-top:5px;width:170px;">
							       		&nbsp;&nbsp;状态：&nbsp;<s:select name="systemisact" list="#{'':'是否启用','1':'是','0':'否'}" listKey="key" listValue="value"  onchange='$("#img_sousuo").click();'/>
							       		</div>
							       		<div style="float: left;width: 315px; ">
							       		&nbsp;&nbsp;日期：&nbsp;<strong:newdate name="endDate" id="endDate" dateobj="${endDate}" skin="whyGreen" isicon="true" classtyle="search" title="请输入日期" dateform="yyyy-MM-dd"></strong:newdate>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       		</div>
							       		</td>
							       	</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="sysId"
									showValue="sysName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="系统编码" property="sysSyscode"
									showValue="sysSyscode" width="20%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="系统名称" property="sysName"
									showValue="sysName" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="启用标志" mapobj="${systemMap}"
									property="sysIsactive" showValue="sysIsactive" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexDateCol caption="启用时间" property="sysStartdate"
									showValue="sysStartdate" dateFormat="yyyy-MM-dd" width="30%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
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
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addTempFile",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editTempFile",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","deleteTempFile",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
			<%--	item = new MenuItem("<%=root%>/images/ico/bianji.gif","超级用户设置","supper",1,"ChangeWidthTable","checkOneDis");--%>
			<%--	sMenu.addItem(item);	--%>
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			function addTempFile(){				
				window.showModalDialog("<%=path%>/basesystem/baseSystem!input.action",window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:450px');		
			}
			function editTempFile(){
				var id=getValue();
				if(id==null || id==""){
					alert("请选择需要编辑的记录。");
					return;
				}
				if(id.length>32){
					alert("只可以编辑一条记录。");
					return;
				}	
				window.showModalDialog("<%=path%>/basesystem/baseSystem!input.action?sysId="+id,window,'help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:450px');		
			}
			function deleteTempFile(){
				var id=getValue();
				if(id==null || id==""){
					alert("请选择需要删除的记录。");
					return;
				}
				if(id.indexOf("402882101eba1e2f011eba6482540001")>-1){
					alert("不允许删除默认系统，如有必要可直接修改系统名称。");
					return;
				}
				
				if(confirm("危险操作,是否确定要删除。")){
				
					location="<%=path%>/basesystem/baseSystem!delete.action?sysId="+id+",";
				}else{
					return;
				}				
			}
			function supper(){
				var id=getValue();
				if(id==null || id==""){
					alert("请选择系统记录。");
					return;
				}
				if(id.length>32){
					alert("请选择一条系统记录。");
					return;
				}	
				var result = window.showModalDialog("<%=path%>/basesystem/baseSystem!tree.action?sysId="+id,window,'help:no;status:no;scroll:no;dialogWidth:400px; dialogHeight:500px');
				if(result=="RELOAD"){
			    location="<%=path%>/basesystem/baseSystem.action";
			     
				}
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