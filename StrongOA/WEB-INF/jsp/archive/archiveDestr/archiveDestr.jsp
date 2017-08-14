<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link type=text/css rel=stylesheet href="<%=frameroot%>/css/search.css">
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel="stylesheet">
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form id="myTableForm" name="myTableForm" theme="simple" action="/archive/archiveDestr/archiveDestr.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
													档案销毁列表
												</td>
												<td width="70%">
													<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td >
																<a class="Operation" href="javascript:destroyAuditing();"><img src="<%=root%>/images/ico/shenhe.gif" width="14" height="14" class="img_s">审核&nbsp;</a>
															</td>
															<td width="5"></td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px" wholeCss="table1" property="id" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17" height="16" onclick="search();" style="cursor: hand;">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<input id="destroyFolderNo" name="destroyFolderNo" value="${ destroyFolderNo}" type="text" style="width:100%" class="search"  title="请输入销毁案卷编号">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<input id="destroyFolderName" name="destroyFolderName" value="${destroyFolderName}" type="text" style="width:100%" class="search"  title="请输入销毁案卷名称">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<strong:newdate id="destroyApplyTime" title="请选择申请销毁日期" name="destroyApplyTime" dateobj="${destroyApplyTime}" dateform="yy-MM-dd" isicon="true" width="100%" classtyle="search"  />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:select id="destroyAuditingType" name="model.destroyAuditingType" list="#{'全部':'','待审核':'0','审核中':'1','已销毁':'2','不通过':'3'}" listKey="value" listValue="key" cssStyle="width:100%" onchange="search();"></s:select>
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<strong:newdate id="destroyFolderDate" title="请选择销毁案卷创建日期" name="destroyFolderDate"  dateform="yy-MM-dd" isicon="true" width="100%" classtyle="search" dateobj="${destroyFolderDate}" />
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="destroyId" showValue="destroyFolderName" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="案卷编号" property="destroyFolderNo" showValue="destroyFolderNo" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="案卷题名" property="destroyId" showValue="destroyFolderName" width="20%" isCanDrag="true" isCanSort="true" onclick="viewFile(this.value);"></webflex:flexTextCol>
								<webflex:flexDateCol caption="申请销毁日期" property="destroyApplyTime" showValue="destroyApplyTime" width="20%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
								<webflex:flexEnumCol caption="案卷销毁状态" mapobj="${map}" property="destroyAuditingType" showValue="destroyAuditingType" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexDateCol caption="销毁案卷日期" property="destroyFolderDate" showValue="destroyFolderDate" width="20%" isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd"></webflex:flexDateCol>
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
	item = new MenuItem("<%=root%>/images/ico/shenhe.gif","审核","destroyAuditing",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function destroyAuditing(){
	var id=getValue();
	if(id==""){
		alert("请选择需销毁的文件！");
	     return;
		}
	$.ajax({
		type:"post",
		url:"<%=path%>/archive/archiveDestr/archiveDestr!getDestrStatus.action",
		data:{
			destroyId:id				
		},
		success:function(data){
			if(data!=null&&data == "0"){
				 window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=archive/archiveDestr/archiveDestr-audit.jsp?destroyId="+id,window,'help:no;status:no;scroll:no;dialogWidth:600px; dialogHeight:500px');
			}else if(data!=null&&data=="1"){
				alert("该案卷正在销毁审核中！");
			}else if(data!=null&&data=="2"){
				alert("该案卷已审核，不需再审");
			}else{
				alert("对不起，操作异常"+data);
			}
		},
		error:function(data){
			alert("对不起，操作异常"+data);
		}
	});
}

function viewFile(value){
	location = "<%=path%>/archive/archiveDestr/archiveDestr!view.action?destroyId="+value;
}

function search(){
	submitForm();
}

function submitForm(){
	document.getElementById("myTableForm").submit();
}
</script>
	</BODY>
</HTML>
