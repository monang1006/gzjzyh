<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>议题分类列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
	
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
	
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
	<!-- 	<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script> -->
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
			<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		 <script type="text/javascript">
		 function showimg(topicStatus){
      
			var rv = '' ;
			if(topicStatus == '0'){
				rv = "<font color='#90036'>已停用</font>&nbsp&nbsp";
			}
			if(topicStatus == '1'){
				rv = "<font color='#63ad00'>已启用</font>&nbsp&nbsp";
			}
			
			return rv;
		}
		
		 
		 </script> 
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<DIV id=contentborder align=center style="float:right">
		<s:form theme="simple" id="myTableForm" action="/meetingmanage/meetingtopicsort/meetingtopicsort.action">
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>议题分类列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addClassificTitle();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editClassificTitle();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="deleteClassificTitle();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="setDisabled();"><img src="<%=root%>/images/operationbtn/close.png"/>&nbsp;停&nbsp;用&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="setRevive();"><img src="<%=root%>/images/operationbtn/open.png"/>&nbsp;启&nbsp;用&nbsp;</td>
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
						<webflex:flexTable name="myTable" width="100%" height="364px"
							wholeCss="table1" property="id" isCanDrag="true"
							isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
							getValueType="getValueByProperty" collection="${page.result}" page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="0"
								class="table1_search">
								<tr>
									<td>
							       		&nbsp;&nbsp;分类编号：&nbsp;<input name="querysortId" id="querysortId" type="text" class="search" title="请您输入分类编号" value="${querysortId }">
							       		&nbsp;&nbsp;分类名称：&nbsp;<input name="sortName" id="sortName" type="text" class="search" title="请您输入分类名称" value="${sortName }">
							       		&nbsp;&nbsp;审批流程：&nbsp;<input name="proName" id="proName" type="text" class="search" title="请您输入审批流程" value="${proName }">
							       		&nbsp;&nbsp;分类描述：&nbsp;<input name="sortDes" id="sortDes" type="text" class="search" title="请您输入分类描述" value="${sortDes}">
							       		&nbsp;&nbsp;启用状态：&nbsp;<s:select name="sortstatus" list="#{'':'选状态','1':'已启用','0':'已停用'}"  listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							      <!--  	</td>
									<td width="5%"  align="center" class="biao_bg1">
										          <img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="17" height="16" style="cursor: hand;" title="单击搜索">
									</td>
									<td width="15%" class="biao_bg1">
											<s:textfield name="querysortId" cssClass="search" title="请输入分类编号"></s:textfield>
									</td>
									<td width="20%" class="biao_bg1">
										<s:textfield name="sortName" cssClass="search" title="请输入分类名称"></s:textfield>
									</td>
									<td width="15%" class="biao_bg1">
										<s:textfield name="proName" cssClass="search" title="请输入审批流程"></s:textfield>
									</td>
									<td width="35%" class="biao_bg1">
										<s:textfield name="sortDes" cssClass="search" title="请输入分类描述"></s:textfield>
									</td>
									<td width="10%" class="biao_bg1">
								     <s:select name="sortstatus"  list="#{'':'选状态','1':'已启用','0':'已停用'}" listKey="key" listValue="value" style="width:6.3em"/>
									</td>
									<td width="5%" class="biao_bg1">
										&nbsp;
									</td> -->
								</tr>
							</table>
							<webflex:flexCheckBoxCol caption="选择" property="topicsortId"
								showValue="topicsortNo" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>
				<webflex:flexEnumCol caption="启用状态" mapobj="${disableMap}" property="isDisbled" showValue="isDisbled" width="0" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>	
							<webflex:flexTextCol caption="分类编号" property="topicsortNo" showValue="topicsortNo"
								width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="分类名称" property="topicsortName"
								showValue="topicsortName" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="审批流程" property="processName"
								showValue="processName" width="15%" isCanDrag="true" isCanSort="true" onclick="pdmImage(this.value)"></webflex:flexTextCol>
							<webflex:flexTextCol caption="分类描述" property="topicsortDemo"
								showValue="topicsortDemo" width="35%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
				<webflex:flexTextCol caption="启用状态" property="topicsortId" showValue="javascript:showimg(isDisbled)" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
						</webflex:flexTable>
					</td>
				</tr>
			</table>
			</s:form>
		</DIV>
		
		<script type="text/javascript">
var sMenu = new Menu();
function initMenuT(){
 $("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addClassificTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editClassificTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","deleteClassificTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/guanbi.gif","停用","setDisabled",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/kaiguan.gif","启用","setRevive",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function addClassificTitle(){
	var width=screen.availWidth/3;
    var height=screen.availHeight/2;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort!input.action",window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
	
}
function editClassificTitle(){
	   var id=getValue();
		if(id==null || id==""){
		alert("请选择需要修改的记录！");
			return;
		}
var ss=$(":checked").parent().next().attr("value");
  
	if(ss=='0'){
		alert("该记录已经停用，不能编辑了！");
		return;
   }
     
	if(id.length >32){
		alert('一次只能修改一条记录!');
		return;
	}	
	var width=screen.availWidth/3;
    var height=screen.availHeight/2;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort!input.action?sortId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
}
function deleteClassificTitle(){
	var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
			return;
		}
		if(confirm("您确定要删除吗？")){
		$.post(
		"<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort!delete.action",
		{sortId:id},
		function(data){
	
			if(data=='deletefalsedeletetrue'){	
		      alert('删除失败，请先删除与之关联的议题。')    
		}
		else if(data=='deletetrue'){
		   location="<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort.action"; 
		}
		}
	)
  // location="<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort!delete.action?sortId="+id;	
 }	
}
function setDisabled(){
      var id=getValue();
	if(id==null || id==""){
		alert("请选择需要停用的记录！");
			return;
		}
		var ss=$(":checked").parent().next().attr("value");
   if(ss=='0'){
   alert("该记录已经停用了！");
   return;
   }
	if(id.length >32){
		alert('一次只能停用一条记录!');
		return;
	}
	if(confirm("您确定要停用吗？")){
   location="<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort!setDisabled.action?sortId="+id;	
 }		

}
function setRevive(){
	  var id=getValue();
	  var ss=$(":checked").parent().next().attr("value");
	if(id==null || id==""){
		alert("请选择需要启用的记录！");
			return;
		}
		
	if(id.length >32){
		alert('一次只能启用一条记录!');
		return;
	}
	
	if(ss=='1'){  
		alert("该记录已经启用了！");
   		return;
 	  }

	if(confirm("您确定要启用吗？")){
   location="<%=path%>/meetingmanage/meetingtopicsort/meetingtopicsort!setRevive.action?sortId="+id;	
 }		
}
function pdmImage(processName){
   // alert(processName);
	if(processName=='' || processName=='null')
	{
		alert('该会议分类不走流程，请重新选择！');
		return;
	}
		var width=screen.availWidth-10;
        var height=screen.availHeight-30;
		OpenWindow("<%=root%>/meetingmanage/meetingtopicsort/meetingtopicsort!pdimage.action?proName="+encodeURI(encodeURI(processName)), 
		                                   width, height, window);
}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });

</script>
	</BODY>
</HTML>
