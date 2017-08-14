<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>纪要显示列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>

		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT> 
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>

		<script type="text/javascript">
			 
		 function showimg(isGuidang){
			var rv = '' ;
			if(isGuidang == '0'){
				rv = "<font color='#90036'>未归档</font>&nbsp&nbsp";
			}
			if(isGuidang == '1'){
				rv = "<font color='#ff1119'>已归档</font>&nbsp&nbsp";
			}
			return rv;
		}
		
		$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
		 
		 </script>

	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/meetingmanage/meetingsummary/meetingsummary.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
								<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>纪要显示列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addTitle()"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;纪&nbsp;要&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editTitle()"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;纪&nbsp;要&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="deleteTitle()"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;纪&nbsp;要&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="word()"><img src="<%=root%>/images/operationbtn/public.png"/>&nbsp;导&nbsp;出&nbsp;word&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
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
								getValueType="getValueByProperty" collection="${sumpage.result}"
								page="${sumpage}" pagename="sumpage">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
							       		&nbsp;&nbsp;纪要名称：&nbsp;<input name="summtitle" id="summtitle" type="text" class="search" title="请您输入纪要名称" value="${summtitle }">
							       		&nbsp;&nbsp;记录时间：&nbsp;<strong:newdate  name="summtime" id="summtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" />
							       		&nbsp;&nbsp;会议地点：&nbsp;<input name="summaddr" id="summaddr" type="text" class="search" title="请您输入会议地点" value="${summaddr }">
							       		&nbsp;&nbsp;是否归档：&nbsp;<s:select name="model.isguidang" list="#{'':'全部','0':'未发布','1':'已发布','2':'已过期'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
										<!-- <td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo"
												width="15" height="15" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="30%" class="biao_bg1">
											<s:textfield name="summtitle" cssClass="search"
												title="请输入纪要名称"></s:textfield>
										</td>

										<td width="25%" class="biao_bg1">
											<strong:newdate name="summtime" id="summtime" skin="whyGreen"
												isicon="true" dateobj="${summtime}" dateform="yyyy-MM-dd"
												classtyle="search" title="日期" width="100%"></strong:newdate>
										</td>

										</td>
										<td width="25%" class="biao_bg1">
											<s:textfield name="summaddr" cssClass="search"
												title="请输入会议地点"></s:textfield>
										</td>

										<td width="15%" class="biao_bg1">
											<s:select name="isguidang"
												list="#{'':'是否归档','0':'未归档','1':'已归档'}" listKey="key"
												listValue="value" style="width:100%" />
										</td>

										<td width="5%" class="biao_bg1">
											&nbsp;
										</td> -->

									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="summaryId"
									showValue="summaryTitle" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexEnumCol caption="是否归档" mapobj="${guidangMap}"
									property="isGuidang" showValue="summaryNo" width="0"
									isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>
								<webflex:flexTextCol caption="纪要名称" property="summaryTitle"
									showValue="summaryTitle" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="记录时间" property="summaryTime"
									showValue="summaryTime" dateFormat="yyyy-MM-dd" width="25%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="会议地点" property="summaryAddr"
									showValue="summaryAddr" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="是否归档" property="isGuidang"
									showValue="javascript:showimg(isGuidang)" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
			<iframe id="annexFrame" style="display: none"></iframe>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
   $("input:checkbox").parent().next().hide(); //隐藏第二列
// $("input:checkbox").parent().next().next().next().next().next().next().next().hide(); //隐藏倒数第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tb-add.gif","添加纪要","addTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑纪要","editTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除纪要","deleteTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/daochu.gif","导出word","word",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	item = new MenuItem("<%=root%>/images/ico/daoru.gif","归 档","guidang",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTitle(){
	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingnotice/meetingnotice!notList.action",window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
}	

function editTitle(){
    	var id=getValue();
	
		if(id==null || id==""){
		alert("请选择需要编辑的记录！");
			return;
		}
		
	var ss=$(":checked").parent().next().attr("value");
   if(ss!='0'){
   	alert('该会议纪要已经归档,不允许再编辑！');
   		return ;
   }
	if(id.length >32){
		alert('一次只能编辑一条记录!');
		return;
	}	
	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
 	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingsummary/meetingsummary!input.action?sumId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
	
}

function deleteTitle(){

	var id=getValue();	
		if(id==null || id==""){
			alert("请选择需要删除的记录！");
			return;
		}
		
	var ss=$(":checked").parent().next().attr("value");
   		if(ss!='0'){
   			alert('该会议纪要已经归档,不允许删除！');
   			return ;
   }
	if(confirm("您确定要删除吗？")){
	location="<%=path%>/meetingmanage/meetingsummary/meetingsummary!delete.action?sumId="+id;
	}
}
//导出word
function word(){
	var id=getValue();
	if(id==""){
	   alert("请选择要导出的记录！");
	   return ;
	}
	if(id.split(",").length>1){
	   alert("不可以同时导出多天记录！");
	   return ;
	}
    var frame=document.getElementById("annexFrame");
   	frame.src="<%=path%>/meetingmanage/meetingsummary/meetingsummary!word.action?sumId="+id;
		
}

//直接归档
function guidang(){

   var id=getValue();
	
		if(id==null || id==""){
		alert("请选择需要归档的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能归档一条记录!');
		return;
	}	
 var ss=$(":checked").parent().next().attr("value");
  if(ss!='0'){
   	alert('该会议纪要已经归档！');
   		return ;
   }
    $.post(
		"<%=path%>/meetingmanage/meetingsummary/meetingsummary!guiDang.action",
		{sumId:id},
		function(data){
	
			if(data=='fail'){	
		      alert('归档失败，请检查原因！')    
		}
		else if(data=='OK'){
		   alert('归档成功！');
		   location="<%=path%>/meetingmanage/meetingsummary/meetingsummary.action"; 
		}
		}
	)

}
function viewAnnex(value){	//查看附件

}
	

	

</script>
	</BODY>
</HTML>
