<HTML><HEAD><TITLE>电子表单列表</TITLE>
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


	function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3' }});
				$.blockUI({ message: '<font color="#008000"><b>'+i+'</b></font>' });
			}
			function hidden(){
				$.unblockUI();
			}
	
	$(document).ready(function(){
		var parWin = window.dialogArguments;
				var definitionFormid=parWin.document.getElementById("definitionFormid").value;
				if(definitionFormid!=null&&definitionFormid!=""){
					var checkobjs=document.getElementsByName("chkRadio");
					for(var i=0;i<checkobjs.length;i++){
						var currentTr=checkobjs[i].value;
						if(currentTr==definitionFormid){
							checkobjs[i].checked=true;
							break;
							
						}
					}
					
				}
	
	
		//搜索功能
		$("#img_sousuo").click(function(){
			$("#searchTitle").val(encodeURI($("#title").val()));
			$("form").submit();
		});
	});
	
	
	function sureEform(){
		var eformId =getSingleValue();
		if(""==eformId|null==eformId){
			alert("请选择一个表单!");
			return;
		}
		if(eformId.indexOf(",")>0){
			alert("只能选择一张表单！");
			return;
		}
		
		var flag = false;
	
	var checkobjs=document.getElementsByName("chkRadio");
	var formName;
	var chkvalue="";
	for(var i=0;i<checkobjs.length;i++){
		if(checkobjs[i].checked==true){
			formName = checkobjs[i].parentElement.parentElement.cells[2].value;
			flag=true;
			break;
		}
	}
	
	
	if(flag){
		var parWin = window.dialogArguments;
		parWin.document.getElementById("definitionFormname").value=formName;
		parWin.document.getElementById("definitionFormid").value=eformId;
		var info=eformId+","+formName;
		window.returnValue = info; // 返回数组对象
		parWin.document.getElementById("workflowDiv").style.display="block";
		
		parWin.document.getElementById("definitionWorkflowname").value="";
		parWin.document.getElementById("definitionWorkflowid").value="";
		window.close();
 
	}else{
		alert("请重新选择表单！");
		return;
	}
		
	}
	
	function closeEform(){
		window.close();
	}
	
	function clickTitle(id){
		var a = OpenWindow("<%=path%>/eformManager/eformManager!view.action?id="+id,700,600);
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
													<strong>表单列表</strong>
												</td>
							               <td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
												<tr>
												    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="sureEform();"><img src="<%=root%>/images/operationbtn/preserve.png"/>&nbsp;确&nbsp;定&nbsp;</td>
					                              <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                                  <td width="5"></td>
            	                                  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                              <td class="Operation_list" onclick="closeEform();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;取&nbsp;消&nbsp;</td>
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
  <s:form id="myTableForm" action="/eformManager/eformManager.action" method="get">
    	 <input type="hidden" name="model.title" id="searchTitle" value="${model.title}">
    	 <input type="hidden" name="reportEform" id="reportEform" value="${reportEform}">
    	 
	     <webflex:flexTable name="myTable" width="100%" height="100%" wholeCss="table1" property="0" isCanDrag="true" isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck" getValueType="getValueByArray" collection="${page.result}" page="${page}">
	     <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							       <td>
							       		&nbsp;&nbsp;表单名称：&nbsp;<input id="title" type="text"  class="search" title="请您输入表单名称" value="${model.title}">
							       		&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							</table> 
			
			<webflex:flexRadioCol caption="选择" valuepos="0" valueshowpos="1" width="10%"  isCanDrag="false" isCanSort="false"></webflex:flexRadioCol>
<%--			<webflex:flexCheckBoxCol caption="选择" valuepos="0" valueshowpos="0" width="5%" isCheckAll="true" isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>--%>
			<webflex:flexTextCol caption="表单名称" valuepos="1" valueshowpos="1" width="60%" isCanDrag="true" isCanSort="true" showsize="34"></webflex:flexTextCol>
		
			
		
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
	item = new MenuItem("<%=root%>/images/operationbtn/preserve.png","确定","sureEform",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/close.png","取消","closeEform",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
</script>
</BODY></HTML>
