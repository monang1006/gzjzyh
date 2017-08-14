<HTML><HEAD><TITLE>报表类型</TITLE>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@include file="/common/include/meta.jsp" %>
<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet"><!--右键菜单样式 -->
<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css  rel=stylesheet>
<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT><!--右键菜单脚本 -->
<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
<!--<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>-->
<script type="text/javascript">
	
	$(document).ready(function() {
				var parWin = window.dialogArguments;
				var sortId=parWin.document.getElementById("sortId").value;
				if(sortId!=null&&sortId!=""){
					var checkobjs=document.getElementsByName("chkRadio");
					for(var i=0;i<checkobjs.length;i++){
						var currentTr=checkobjs[i].value;
						if(currentTr==sortId){
							checkobjs[i].checked=true;
							break;
							
						}
					}
					
				}
				
			});
		


	function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
	
	function search(){
		myTableForm.submit();	
	}
	
	
	function sureReportSort(){
		var sortId =getSingleValue();
		if(""==sortId|null==sortId){
			alert("请选择报表类型!");
			return;
		}
		if(sortId.indexOf(",")>0){
			alert("只能选择一个报表类型！");
			return;
		}
		
		var flag = false;
	
	var checkobjs=document.getElementsByName("chkRadio");
	var sortName;
	var chkvalue="";
	for(var i=0;i<checkobjs.length;i++){
		if(checkobjs[i].checked==true){
			sortName = checkobjs[i].parentElement.parentElement.cells[2].value;
			flag=true;
			break;
		}
	}
	
	
	if(flag){
		var parWin = window.dialogArguments;
		parWin.document.getElementById("sortName").value=sortName;
		parWin.document.getElementById("sortId").value=sortId;
		window.close();
 
	}else{
		alert("请重新选择报表类型！");
		return;
	}
		
	}
	
	function closeReportSort(){
		window.close();
	}
	
</script>
</HEAD>
<base target="_self"/>
<BODY class=contentbodymargin oncontextmenu="return false;" onload=initMenuT()>
<DIV id=contentborder align=center>
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
													<strong>报表类型</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												   <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="sureReportSort();"><img src="<%=root%>/images/operationbtn/preserve.png"/>&nbsp;确&nbsp;定&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="closeReportSort();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;取&nbsp;消&nbsp;</td>
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
	<s:form action="/report/workflowReportSort.action" id="myTableForm" theme="simple">
    	 <input type="hidden" name="checkSort" id="checkSort" value="${checkSort}">
    	 
	   <webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="sortId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;类型名称：&nbsp;<input name="model.sortName" type="text" id="sortName" class="search"  title="类型名称" value="${model.sortName}" maxlength="30">
							       		&nbsp;&nbsp;类型描述：&nbsp;<input name="model.sortDesc" type="text" id="sortDesc"  class="search"  title="类型描述" value="${model.sortDesc}" maxlength="30">
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search()"/>
							       	</td>
							     </tr>
							</table> 
			
			<webflex:flexRadioCol caption="选择" property="sortId"
				showValue="sortName" width="10%" 
				isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>

			<webflex:flexTextCol caption="类型名" property="sortName"
				showValue="sortName" width="40%" isCanDrag="true" showsize="30"
				isCanSort="true"></webflex:flexTextCol>
			<webflex:flexTextCol caption="类型描述" property="sortDesc"
				showValue="sortDesc" width="50%" isCanDrag="true" showsize="30"
				isCanSort="true"></webflex:flexTextCol>
		</webflex:flexTable>
	</s:form>
	</table>
      </td>
  </tr>
</table>
</DIV>
<script language="javascript">
var sMenu = new Menu();
function initMenuT(){

    sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/operationbtn/preserve.png","确定","sureReportSort",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
		item = new MenuItem("<%=root%>/images/operationbtn/close.png","取消","closeReportSort",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
</script>
</BODY></HTML>
