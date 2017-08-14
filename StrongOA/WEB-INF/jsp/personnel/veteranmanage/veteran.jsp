<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/common/include/rootPath.jsp"%>
<%@ page import="java.util.*"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp" %>
		<title>老干部信息列表</title>
		
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
	
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>

		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form  id="myTableForm" action="/personnel/veteranmanage/veteran.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>&nbsp;</td>
											<td width="30%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												老干部信息列表
											</td>
											<td width="*">&nbsp;
												
											</td>
											<td width="70%"><table align="right"><tr>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onClick="addVeteran()">
													<img src="<%=root%>/images/ico/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onClick="editVeteran()">
													<img src="<%=root%>/images/ico/bianji.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑</span> </a>
											</td>
						                 	<td width="5"></td>
											<td >
												<a class="Operation" href="#" onClick="deleteVeteran()">
													<img src="<%=root%>/images/ico/shanchu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onClick="viewVeteran()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onClick="addVeteranRegard()">
													<img src="<%=root%>/images/ico/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加慰问信息</span> </a>
											</td>
											<td width="5"></td>
											</tr></table></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="myTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
								class="table1">
								<tr>
									<td width="5%" align="center" class="biao_bg1">
										<img
											src="<%=frameroot%>/images/perspective_leftside/sousuo.gif" id="img_sousuo" style="cursor: hand;" title="单击搜索"
											width="17" height="16">
									</td>
									<td width="25%" class="biao_bg1">
									    <s:textfield name="personName"  cssClass="search" title="老干部姓名"></s:textfield>							
									</td>									
									<td width="20%" class="biao_bg1">
									    <s:textfield name="personPset"  cssClass="search" title="职位"></s:textfield>
									</td>
																	
									<td width="10%" class="biao_bg1">
										<!--<s:select name="personSax" list="#{'':'姓别','男':'男','女':'女'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();' style="width:100%"/>-->
										
										<s:select list="saxList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="性 别" onchange='$("#img_sousuo").click();'
							id="personSax" name="personSax" style="width:100%"/>
									</td>
									<td width="20%" class="biao_bg1">
									<s:select list="levelList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="待遇级别类型" onchange='$("#img_sousuo").click();'
							id="personLevel" name="personLevel" style="width:100%"/>

									</td>
									<td width="20%" class="biao_bg1">
									<s:select list="healthList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="健康状况" onchange='$("#img_sousuo").click();'
							id="healthState" name="healthState" style="width:100%"/>
									
									</td>
									<td width="5%" class="biao_bg1">
								 &nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="personId"
								showValue="personName" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							<webflex:flexTextCol caption="老干部姓名" property="personName"
								showValue="personName" isCanDrag="true" width="25%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="职位"  property="personPset"
								showValue="personPset" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
								 <webflex:flexEnumCol caption="性别" mapobj="${saxMap}" property="personSax"
								showValue="personSax" isCanDrag="true" width="10%" isCanSort="true"></webflex:flexEnumCol>
						   <webflex:flexEnumCol caption="待遇类型" mapobj="${veternMap}" property="personTreatmentLevel"
								showValue="personTreatmentLevel" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexEnumCol>
							<webflex:flexEnumCol caption="健康状态" mapobj="${healthMap}" property="personHealthState"
								showValue="personHealthState" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexEnumCol>
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
	
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addVeteran",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editVeteran",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteVeteran",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewVeteran",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加慰问信息","addVeteranRegard",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addVeteran(){

  window.showModalDialog("<%=path%>/personnel/veteranmanage/veteran!input.action",window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
	
}
function addVeteranRegard(){
 var id=getValue();
    
		if(id==null || id==""){
		alert("请选择老干部！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能选择一个老干部！");
   		 		return ;
   		 	}
   		 }
	  window.showModalDialog("<%=path%>/fileNameRedirectAction.action?toPage=/personnel/veteranmanage/veteran_regard.jsp?personId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:550px');
}
function editVeteran(){
      var id=getValue();
    
		if(id==null || id==""){
		alert("请选择老干部！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能编辑一份老干部信息！");
   		 		return ;
   		 	}
   		 }
   	  window.showModalDialog("<%=path%>/personnel/veteranmanage/veteran!input.action?personId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
	
}
function deleteVeteran(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择老干部！");
			return;
		}
	if(confirm("您确定要删除吗？")){
	    var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能删除一个老干部信息！");
   		 		return ;
   		 	}
    location="<%=path%>/personnel/veteranmanage/veteran!delete.action?personId="+id;	
    }	
}
function viewVeteran(){
    var id=getValue();
	if(id==null || id==""){
		alert("请选择老干部！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能查看一份老干部信息！");
   		 		return ;
   		 	}
   		 }
   		
    window.showModalDialog("<%=path%>/personnel/veteranmanage/veteran!viewPlay.action?personId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:350px');
	
}
$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("Form").submit();
        });     
      });
</script>
	</BODY>
</HTML>
