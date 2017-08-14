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
		<title>培训信息列表</title>		
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
	
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>

		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	
	<BODY class=contentbodymargin oncontextmenu="return false;" onLoad="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form  id="myTableForm" action="/personnel/trainingmanage/training.action">
		
		<input id="clumnId" name="column.clumnId" type="hidden"
								size="32" value="${clumnId}">
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
											<td width="20%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												培训信息列表
											</td>
											<td width="*">&nbsp;
												
											</td>
											
											<td width="80%"><table align="right"><tr>
											<td >
												<a class="Operation" href="#" onClick="addTraining()">
													<img src="<%=root%>/images/ico/tianjia.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">添加</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onClick="editTraining()">
													<img src="<%=root%>/images/ico/bianji.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">编辑</span> </a>
											</td>
						                 	<td width="5"></td>
											<td  >
												<a class="Operation" href="#" onClick="deleteTraining()">
													<img src="<%=root%>/images/ico/shanchu.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">删除</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onClick="viewByPerson()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">人员查询</span> </a>
											</td>
											<td width="5"></td>
											<td>
												<a class="Operation" href="#" onClick="viewByOrg()">
													<img src="<%=root%>/images/ico/chakan.gif" width="14"
														height="14" class="img_s"><span id="test"
													style="cursor:hand">机构查询</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onClick="viewTraining()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
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
											width="15" height="15" >
									</td>
									
									
									<td width="30%" class="biao_bg1">
									    <s:textfield name="trainTopic"  cssClass="search" title="请输入培训主题"></s:textfield>
									</td>
									<td width="20%" class="biao_bg1">
									    <s:textfield name="trainCommpany" cssClass="search" title="请输入主办单位"></s:textfield>
									</td>
							
									<td width="15%" class="biao_bg1">
											<strong:newdate id="sDate" name="sDate" dateform="yyyy-MM-dd"  width="100%"  isicon="true" classtyle="search" title="请输入开始日期" />
									</td>
										
									<td width="15%" class="biao_bg1">
											<strong:newdate id="eDate"  name="eDate"  dateform="yyyy-MM-dd"  width="100%"  isicon="true" classtyle="search" title="请输入结束日期" />
									</td>
									<td width="15%" class="biao_bg1">
			<s:select list="trainTypeList" listKey="dictItemCode" listValue="dictItemName"  headerKey="" headerValue="请选择培训类型" onchange='$("#img_sousuo").click();'
							id="trainType" name="trainType" style="width:100%"/>
									</td>
									<td class="biao_bg1">&nbsp;
										
									</td>
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="trainId"
								showValue="trainTopic" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
							
							<webflex:flexTextCol caption="培训主题"  property="trainTopic"
								showValue="trainTopic" isCanDrag="true" width="30%" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="主办单位" property="trainCommpany"
								showValue="trainCommpany" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
		
						    <webflex:flexDateCol caption="开始日期" property="trainStartdate" showsize="16" dateFormat="yyyy-MM-dd"
								showValue="trainStartdate" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束日期" property="trainEnddate" showsize="16" dateFormat="yyyy-MM-dd"
								showValue="trainEnddate" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexDateCol>
								
						   <webflex:flexEnumCol caption="培训类型" mapobj="${traintypeMap}" property="trainType"
								showValue="trainType" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexEnumCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addTraining",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editTraining",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteTraining",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","人员查询","viewByPerson",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","机构查询","viewByOrg",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewTraining",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addTraining(){

	var clumnId=document.getElementById('clumnId').value;
	if(clumnId=='' || clumnId==null){
		alert('请选择一个培训栏目！');
		return ;
	}
   var result=window.showModalDialog("<%=path%>/personnel/trainingmanage/training!input.action?clumnId="+clumnId,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:600px');
	//window.location = "<%=root%>/personnel/training!input.action";
}
function viewByPerson(){
	window.location = "<%=root%>/personnel/trainingmanage/training!person.action";
}

function viewByOrg(){
  	window.location = "<%=root%>/personnel/trainingmanage/training!personOrg.action";
}
function editTraining(){
      var id=getValue();
    
		if(id==null || id==""){
		alert("请选择培训记录！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能编辑一份培训记录！");
   		 		return ;
   		 	}
   		 }
      var result=window.showModalDialog("<%=path%>/personnel/trainingmanage/training!input.action?trainingId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:600px');
  
}
function deleteTraining(){
	// alert("只有被停用的车辆才能删除，删除车辆后，需删除对应的车辆申请单！");
	var id=getValue();
	if(id==null || id==""){
		alert("请选择培训记录！");
			return;
		}
		if(confirm("您确定要删除吗？")){
    location="<%=path%>/personnel/trainingmanage/training!delete.action?trainingId="+id;	
    }	
}
function viewTraining(){
    var id=getValue();
	if(id==null || id==""){
		alert("请选择培训记录！");
			return;
		}else{
   		 	var Ids = id.split(",");
   		 	if(Ids.length>1){
   		 		alert("一次只能查看一份培训记录！");
   		 		return ;
   		 	}
   		 }
    var result=window.showModalDialog("<%=path%>/personnel/trainingmanage/training!view.action?trainingId="+id,window,'help:no;status:no;scroll:no;dialogWidth:660px; dialogHeight:600px');
}
$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("Form").submit();
        });     
      });
</script>
	</BODY>
</HTML>
