<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/grid/ChangeWidthTable.js" language="javascript"></script>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/menu/menu.js" language="javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<SCRIPT>
			function viewState(state){
				if(state=="1")
					return "<img src='<%=root%>/images/ico/read.gif'>";
				else 
					return "<img src='<%=root%>/images/ico/unread.gif'>";
			}
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td height="100%">
						<s:form id="myTableForm" theme="simple" action="/archive/archiveborrow/archiveBorrow!auditing.action">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td colspan="3" class="table_headtd">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td class="table_headtd_img" >
								        <img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							                 </td>
												<td align="left">
													<strong>待审核的借阅信息列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
														<tr>
														<s:if test="model.borrowAuditing==\"0\"">
														  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="borrowAuditing();"><img src="<%=root%>/images/operationbtn/audit.png"/>&nbsp;审&nbsp;核&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  </s:if>
				                                  
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
			
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="borrowId" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;文件编号：&nbsp;<input name="model.toaArchiveFile.fileNo" type="text"  value="${model.toaArchiveFile.fileNo}" class="search" title="文件编号">
							       		&nbsp;&nbsp;文件名称：&nbsp;<input name="model.toaArchiveFile.fileTitle" type="text"  value="${model.toaArchiveFile.fileTitle}" class="search" title="文件名称">
							       		&nbsp;&nbsp;申请人：&nbsp;<input name="model.borrowPersonname" type="text"  value="${model.borrowPersonname}" class="search" title="申请人">
							       		&nbsp;&nbsp;借阅时间：&nbsp;<strong:newdate id="borrowFromtime" name="model.borrowFromtime" dateform="yyyy-MM-dd"  dateobj="${model.borrowFromtime}" isicon="true" classtyle="search" title="借阅时间" width='146px'/></br>
							       		&nbsp;&nbsp;归还时间：&nbsp;<strong:newdate id="borrowEndtime" name="model.borrowEndtime" dateform="yyyy-MM-dd"  dateobj="${model.borrowEndtime}" isicon="true" classtyle="search" title="归还时间" width='146px'/>
							       		&nbsp;&nbsp;申请状态：&nbsp;<s:select name="model.borrowAuditing" list="#{'0':'待审','1':'已审','3':'驳回'}" listKey="key" listValue="value"  onchange="search()"  style='width:146px'/>
							       		&nbsp;&nbsp;查看状态：&nbsp;<s:select name="model.borrowViewState" list="#{'':'查看状态','0':'未查看','1':'已查看'}" listKey="key" listValue="value"  onchange="search()" style='width:146px'/>
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
								
								<webflex:flexCheckBoxCol caption="选择" property="borrowId" showValue="toaArchiveFile.fileNo" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="文件编号" property="toaArchiveFile.fileNo" showValue="toaArchiveFile.fileNo" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="文件名称" property="toaArchiveFile.fileTitle" showValue="toaArchiveFile.fileTitle" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="申请人" property="borrowPersonid" showValue="borrowPersonname" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								
								<webflex:flexDateCol caption="借阅时间" property="borrowFromtime" showValue="borrowFromtime" width="14%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								<webflex:flexDateCol caption="归还时间" property="borrowEndtime" showValue="borrowEndtime" width="14%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								<webflex:flexEnumCol caption="申请状态" mapobj="${statemap}" property="borrowAuditing" showValue="borrowAuditing" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="查看状态" property="borrowViewState" showValue="javascript:viewState(borrowViewState)" width="12%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	<s:if test="model.borrowAuditing==\"0\"">
	item = new MenuItem("<%=root%>/images/operationbtn/audit.png","审核","borrowAuditing",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</s:if>
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function borrowAuditing(){
	var borrowId = getValue();
	if(borrowId=="null"||borrowId==""){
		alert("请至少选择一条借阅信息!");
		return;
	}else if(borrowId.length>32){
		alert("一次只能审核一条借阅信息，请重新选择!");
		return;
	}else{
	
		window.showModalDialog("<%=path%>/archive/archiveborrow/archiveBorrow!state.action?borrowId="+borrowId,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:450px');
	}
	
//	window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=archive/archiveborrow/borrowAuditingState.jsp?borrowId="+borrowId,window,'help:no;status:no;scroll:no;dialogWidth:550px; dialogHeight:450px');
}
function search(){
	myTableForm.submit();
}
function submitForm(){
 	myTableForm.submit();
}
</script>
	</BODY>
</HTML>
