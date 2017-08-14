<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>报表类型</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/search.css" type=text/css rel=stylesheet>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript" src="<%=path%>/common/js/grid/ChangeWidthTable.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script> -->
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT()">
		<script src="<%=path%>/common/js/newdate/WdatePicker.js" type="text/javascript"></script>
		<DIV id=contentborder align=center>
		<s:form action="/report/reportDefine.action" id="myTableForm" theme="simple">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
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
													<strong>报表定义列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="addReport();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;增&nbsp;加&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="editReport();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
				                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="addPrivil();"><img src="<%=root%>/images/operationbtn/install.png"/>&nbsp;设置权限&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="delReport();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
				<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="definitionId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;报表名称：&nbsp;<input name="model.definitionName" id="definitionName" type="text"  class="search" value="${model.definitionName}"  title="报表名称">
							       		&nbsp;&nbsp;报表种类：&nbsp;<input name="model.toaReportSort.sortName" id="" type="text"  class="search"  value="${model.toaReportSort.sortName}" title="报表种类">
							       		&nbsp;&nbsp;表单名称：&nbsp;<input name="model.definitionFormname" id="definitionFormname" type="text"  class="search"  value="${model.definitionFormname}" title="表单名称">
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
								
								<webflex:flexCheckBoxCol caption="选择" property="definitionId"
									showValue="definitionName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>

								<webflex:flexTextCol caption="报表名称" property="definitionId"
									showValue="definitionName" width="30%" isCanDrag="true" showsize="30"
									isCanSort="true" align="center"></webflex:flexTextCol>
<%--								<webflex:flexTextCol caption="报表种类" property="toaReportSort.sortId"--%>
<%--									showValue="toaReportSort.sortId" width="30%" isCanDrag="true" showsize="30"--%>
<%--									isCanSort="true"></webflex:flexTextCol>--%>
								<webflex:flexEnumCol caption="报表种类" mapobj="${sortNameMap}" 
									property="toaReportSort.sortId" showValue="toaReportSort.sortId" width="30%" isCanDrag="true" 
									isCanSort="true" align="center"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="表单名称" property="definitionFormname"
									showValue="definitionFormname" width="35%" isCanDrag="true" showsize="30"
									isCanSort="true" align="center"></webflex:flexTextCol>
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
	
	<security:authorize ifAllGranted="001-0002000300010001">
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","增加","addReport",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	
	<security:authorize ifAllGranted="001-0002000300010002">
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editReport",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0002000300010001">
	item = new MenuItem("<%=root%>/images/operationbtn/install.png","设置权限","addPrivil",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	</security:authorize>
	<security:authorize ifAllGranted="001-0002000300010003">
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delReport",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	</security:authorize>
	
	
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
	//设置权限
	function addPrivil(){
		var id=getValue();
		if(id=="")
			alert("请选择报表！");
		else if(id.indexOf(",")!=-1)
			alert("请选择一个报表！");
		else{
			var url = "<%=path%>/report/reportPrivilset.action?definitionId="+id;//"uuid_definitionId";
			var a = window.showModalDialog(url,window,'dialogWidth:650px;dialogHeight:580px;help:no;status:no;scroll:no');
			if("reload"==a){
				document.location.reload();
			}
		}
	}

function addReport(){		//添加文件
	var url="<%=path%>/report/reportDefine!input.action?date="+(new Date);
	window.showModalDialog(url,window,'dialogWidth:650px;dialogHeight:350px;help:no;status:no;scroll:no');
	
}
function editReport(){	//编辑文件
	var id=getValue();
	if(id=="")
		alert("请选择需编辑的报表！");
	else if(id.indexOf(",")!=-1)
		alert("请选择一个文件！");
	else{
		//location="<%=path%>/report/reportDefine!input.action?definitionId="+id;
		
		 window.showModalDialog("<%=path%>/report/reportDefine!input.action?definitionId="+id,window,'dialogWidth:650px;dialogHeight:350px;help:no;status:no;scroll:no');
	  }
	  
}

	function delReport(){
		var id=getValue();
	if(id==""){
		alert("请选择需要删除定义报表！");
	}	
	else{
		if(confirm("当前报表下的显示项和权限数据将全部删除，确定删除吗？")){
			location="<%=path%>/report/reportDefine!delete.action?definitionId="+id;
		}
	  }
		
	}
	
	function search(){
		myTableForm.submit();
	}
</script>
			
		



	</BODY>
</HTML>
