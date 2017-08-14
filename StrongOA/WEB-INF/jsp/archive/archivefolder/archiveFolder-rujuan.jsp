<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单脚本 -->
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!-- 
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		 -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script language="javascript" type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form action="/archive/archivefolder/archiveFolder.action" id="myTableForm" theme="simple">
							<input type="hidden" name="archiveSortId" value="${archiveSortId}">
							<input type="hidden" name="forward" value="rujuan">
							<input type="hidden" name="model.folderAuditing" id="folderAuditing" value="${model.folderAuditing }">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" class="table_headtd">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
													<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
												</td>
												<td align="left">
													<strong>档案卷(盒)列表</strong>
												</td>
												<td align="right">
													<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td>
																<table border="0" align="right" cellpadding="0" cellspacing="0">
																	<tr>
																		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
													                 	<td class="Operation_list" onclick="queding();"><img src="<%=root%>/images/operationbtn/rujuan.gif"/>&nbsp;入&nbsp;卷&nbsp;</td>
													                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
												                  		<td width="5"></td>
												                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
													                 	<td class="Operation_list" onclick="quxiao();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;取&nbsp;消&nbsp;</td>
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
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="folderId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}" page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1_search">
									<tr>
										<td>
								       		&nbsp;&nbsp;卷（盒）号：&nbsp;<input name="model.folderNo" id="model.folderNo" type="text" class="search" title="请输入卷（盒）号" value="${model.folderNo}">
								       		&nbsp;&nbsp;题名：&nbsp;<input name="model.folderName" id="model.folderName" type="text" class="search" title="请输入题名" value="${model.folderName}">
								       		<br>
								       		&nbsp;&nbsp;卷（盒）年度：&nbsp;<strong:newdate id="folderDate_str" dateform="yyyy" isicon="true" name="folderDateY" dateobj="${model.folderDate}" classtyle="search" title="卷（盒）年度" />
								       		&nbsp;&nbsp;保管期限：&nbsp;<s:select list="limitdictList" listKey="dictItemCode" listValue="dictItemName" headerKey="" headerValue="请选择保管期限" id="folderLimitId" name="model.folderLimitId" onchange="changelimitName(this)" onchange="search()" />
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
								       	</td>
								</table>
								<webflex:flexRadioCol caption="选择" property="folderId" showValue="folderName" width="10%" isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
								<webflex:flexTextCol caption="卷（盒）号" property="folderArrayNo" showValue="folderNo"  showsize="50" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="题名" property="folderId" showValue="folderName" showsize="50"  width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="年度" property="folderDate"  showValue="folderDate" width="12%" isCanDrag="true" isCanSort="true" dateFormat="yyyy"></webflex:flexDateCol>
								<webflex:flexTextCol caption="保管期限" property="folderLimitId" showValue="folderLimitName"  width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="机构" property="folderDepartmentName" showValue="folderDepartmentName" showsize="50"  width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexEnumCol caption="卷(盒)状态" mapobj="${statemap}" property="folderAuditing" showsize="50"  showValue="folderAuditing" width="18%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
							</webflex:flexTable>
						</s:form>
					</td>
				</tr>
			</table>
			<IFRAME style="display: none;" id="helpframe"></IFRAME>
		</DIV>
		<script language="javascript">
			var sMenu = new Menu();
			function initMenuT(){
				$("#img_sousuo").click(function(){
					search();
				});
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/rujuan.gif","入卷","queding",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
					item = new MenuItem("<%=root%>/images/operationbtn/del.png","取消","quxiao",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
			var archiveSortId = document.getElementById("archiveSortId").value;
			function queding(){
			    var folderId=getSingleValue();
				if(archiveSortId=="null"||archiveSortId==""){
					alert("请先选择类目!");
					return;
				}	
				if(folderId==""||folderId=="null"||folderId==null){
					alert("请选择案卷!");
					return;
				}
					window.parent.returnValue=folderId;   
					window.close();
			}
			function quxiao(){
				window.close();
			}
			
			function search(){
			    myTableForm.submit();
				window.location.reload();
			}
			
			function viewFile(value){
				var archiveSortId=document.getElementById("archiveSortId").value;
				location="<%=path%>/archive/archivefolder/archiveFolder!input.action?forward=viewDetail&folderId="+value+"&archiveSortId="+archiveSortId;
			}
		</script>
	</BODY>
</HTML>
