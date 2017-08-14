<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<link type="text/css" rel="stylesheet" href="<%=frameroot%>/css/strongitmenu.css">
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<script type="text/javascript">
			function showLogo(value){
				if(value=="1"){
					return "<img src='<%=root%>/oa/image/dataprivil/restore_user.gif'>";
				}else{
					return "";
				}
			}
		</script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" theme="simple" action="/dataprivil/postDataPrivil.action">
				<input type="hidden" id="postId" name="postId" value="${postId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>${postName}的信息集权限列表</strong>
												</td>	
												<td align="right">
	            									<table border="0" align="right" cellpadding="0" cellspacing="0">
		                								<tr>
		                									<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="setInfoSetPrivil();"><img src="<%=root%>/images/operationbtn/Set_the_data_access.png"/>&nbsp;设置信息集权限&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="setInfoItemPrivil();"><img src="<%=root%>/images/operationbtn/Set_the_data_access.png"/>&nbsp;设置信息项权限&nbsp;</td>
										                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
									                  		<td width="5"></td>
									                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										                 	<td class="Operation_list" onclick="goback();"><img src="<%=root%>/images/operationbtn/back.png"/>&nbsp;返&nbsp;回&nbsp;</td>
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
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="dataPrivalId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}" >
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
									<tr>
										<td>
								       		&nbsp;&nbsp;信息集：&nbsp;<s:select id="infoSetCode" name="infoSetCode" list="infoSet" listKey="infoSetCode" headerKey="" headerValue="请选择信息集" listValue="infoSetName" onchange="submitForm()"></s:select>
								       		&nbsp;&nbsp;状态：&nbsp;<s:select id="status" name="status" list="#{'请选择':'','只读':'0','读写':'1','隐藏':'2'}" listKey="value" listValue="key" onchange="submitForm()"></s:select>
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="infoSet.infoSetCode"
									showValue="infoSet.infoSetName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="信息集值" property="infoSet.infoSetCode"
									showValue="infoSet.infoSetValue" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="信息集名称" property="infoSet.infoSetCode"
									showValue="infoSet.infoSetName" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="只读" property="postStructureReadonly"
									showValue="javascript:showLogo(postStructureReadonly)" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="读写" property="postStructureReadwrite"
									showValue="javascript:showLogo(postStructureReadwrite)" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="隐藏" property="postStructureHide"
									showValue="javascript:showLogo(postStructureHide)" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				$("#img_sousuo").click(function(){
					submitForm();
				});
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/Set_the_data_access.png","设置信息集权限","setInfoSetPrivil",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Set_the_data_access.png","设置信息项权限","setInfoItemPrivil",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/operationbtn/Return.png","返回","goback",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			function setInfoSetPrivil(){
				var postId=document.getElementById("postId").value;
				var url="<%=root%>/dataprivil/postDataPrivil!getInfoSets.action?postId="+postId
				OpenWindow(url,650, 400, window);
			}
			
			function setInfoItemPrivil(){
				var postId=document.getElementById("postId").value;
				var infoSetCode=getValue();
				if(infoSetCode==""){
					alert("请选择信息集!")
				}else if(infoSetCode.indexOf(",")!=-1){
					alert("请选择一个信息集！");
				}else{
					var url="<%=root%>/dataprivil/postDataPrivil!getInfoItems.action?postId="+postId+"&infoSetCode="+infoSetCode
					OpenWindow(url,650, 400, window);
				}
			}
			
			function goback(){
				window.location="<%=path%>/postmanage/postContent.action";
			}
			
			function submitForm(){
				document.getElementById("myTableForm").submit();
			}
		</script>
	</BODY>
</HTML>