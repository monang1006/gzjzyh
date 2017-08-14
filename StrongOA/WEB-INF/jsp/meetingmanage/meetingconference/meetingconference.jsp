<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>会议列表</title>
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
		 function showimg(conferenceStatus){      
			var rv = '' ;
			if(conferenceStatus == '0'){
				rv = "<font color='#90036'>未开始</font>&nbsp&nbsp";
			}
			if(conferenceStatus == '1'){
				rv = "<font color='#ff1119'>进行中</font>&nbsp&nbsp";
			}
			if(conferenceStatus == '2'){
				rv = "<font color='#63ad00'>已结束</font>&nbsp&nbsp";
			}						
			return rv;
		}
		 
		 
		 </script>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/meetingmanage/meetingconference/meetingconference.action">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>会议列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
						            <tr>
						            	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="addTitle();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;添&nbsp;加&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="editTitle();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="deleteTitle();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="viewTitle();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
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
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table1_search">
									<tr>
										<td>
							       		&nbsp;&nbsp;会议主题：&nbsp;<input name="conferenceName" id="conferenceName" type="text" class="search" title="请您输入标题" value="${conferenceName}">
							       		&nbsp;&nbsp;会议描述：&nbsp;<input name="conferenceDemo" id="conferenceDemo" type="text" class="search" title="请您输入发布部门" value="${conferenceDemo }">
							       		&nbsp;&nbsp;会议地址：&nbsp;<input name="conferenceAddr" id="conferenceAddr" type="text" class="search" title="请您输入发布部门" value="${conferenceAddr }">
							       		&nbsp;&nbsp;开始时间：&nbsp;<strong:newdate  name="conferenceStime" id="conferenceStime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${conferenceStime}"/>
							       		&nbsp;&nbsp;结束时间：&nbsp;<strong:newdate  name="conferenceEndtime" id="conferenceEndtime" skin="whyGreen" isicon="true"  classtyle="search" title="请输入有效日期" dateform="yyyy-MM-dd" dateobj="${conferenceEndtime}"/>
							       		</td>
							       		</tr>
							       		<tr>
							       		<td>
							       		&nbsp;&nbsp;会议状态：&nbsp;<s:select name="conferenceStatus" list="#{'':'选状态','0':'未开始','1':'进行中','2':'已结束'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
									<!-- 	<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo"
												width="15" height="15" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="20%" class="biao_bg1">
											<s:textfield name="conferenceName" cssClass="search"
												title="请输入会议主题"></s:textfield>
										</td>
										<td width="20%" class="biao_bg1">
											<s:textfield name="conferenceDemo" cssClass="search"
												title="请输入会议描述"></s:textfield>
										</td>

										<td width="15%" class="biao_bg1">
											<s:textfield name="conferenceAddr" cssClass="search"
												title="请输入会议地址"></s:textfield>
										</td>

										<td class="biao_bg1" width="15%">
											
											<strong:newdate name="conferenceStime" id="conferenceStime"
												skin="whyGreen" isicon="true" dateobj="${conferenceStime}"
												dateform="yyyy-MM-dd" width="110" classtyle="search"  title="开始日期"></strong:newdate>
												
										</td>
										<td class="biao_bg1" width="15%">
										
											<strong:newdate name="conferenceEndtime"
												id="conferenceEndtime" skin="whyGreen" isicon="true"
												dateobj="${conferenceEndtime}" dateform="yyyy-MM-dd"
												width="110" classtyle="search"  title="结束日期"></strong:newdate>
										</td>
										<td width="15%" class="biao_bg1">
											<s:select name="conferenceStatus"
												list="#{'':'选状态','0':'未开始','1':'进行中','2':'已结束'}"
												listKey="key" listValue="value" style="width:6.5em" />
										</td>
										<td width="5%" class="biao_bg1">
											&nbsp;
										</td> -->
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="conferenceId"
									showValue="conferenceName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexEnumCol caption="会议状态" mapobj="${meetingMap}" property="conferenceStatus"
									showValue="conferenceCode" width="0" isCanDrag="true" 
									isCanSort="true"></webflex:flexEnumCol>	
								<webflex:flexTextCol caption="会议主题" property="conferenceName"
									showValue="conferenceName" width="20%" isCanDrag="true"
									isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议描述" property="conferenceDemo"
									showValue="conferenceDemo" width="20%" isCanDrag="true"
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议地址" property="conferenceAddr"
									showValue="conferenceAddr" width="15%" isCanDrag="true"
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="conferenceStime"
									showValue="conferenceStime" dateFormat="yyyy-MM-dd" 
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="conferenceEndtime"
									showValue="conferenceEndtime" dateFormat="yyyy-MM-dd"
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="会议状态" property="conferenceStatus"
									showValue="javascript:showimg(conferenceStatus)" width="10%"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>


							</webflex:flexTable>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<script language="javascript">
var sMenu = new Menu();
function initMenuT(){
  $("input:checkbox").parent().next().hide(); //隐藏第二列
	sMenu.registerToDoc(sMenu);
	var item = null;
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添 加","addTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编 辑","editTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删 除","deleteTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查 看","viewTitle",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTitle(){
	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingconference/meetingconference!input.action",window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");
}
function editTitle(){

	var id=getValue();	
		if(id==null || id==""){
		alert("请选择需要编辑的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能编辑一条记录!');
		return;
	}	
	var ss=$(":checked").parent().next().attr("value");

   if(ss!='0'){
   		alert('该会议已经进行不允许编辑！');
   		return ;
   }
   else{
   	var width=screen.availWidth/2;
    var height=screen.availHeight*2/3;
	var result=window.showModalDialog("<%=path%>/meetingmanage/meetingconference/meetingconference!input.action?conId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");

}	
}
function deleteTitle(){
var id=getValue();
	if(id==null || id==""){
		alert("请选择需要删除的记录！");
			return;
		}
	var ss=$(":checked").parent().next().attr("value");
   
     if(ss!='0'){
   		alert('该会议已经进行不允许删除！');
   		return ;
   }

	if(confirm("您确定要删除吗？")){
   		location="<%=path%>/meetingmanage/meetingconference/meetingconference!delete.action?conId="+id;	
 }	
}
function viewTitle(){
	var id=getValue();
	
		if(id==null || id==""){
		alert("请选择需要查看的记录！");
			return;
		}
		if(id.length >32){
		alert('一次只能查看一条记录!');
		return;
	}	
		var width=screen.availWidth/2;
		var height=screen.availHeight*2/3
		var result=window.showModalDialog("<%=path%>/meetingmanage/meetingconference/meetingconference!display.action?conId="+id,window,"dialogWidth:" + width + "pt;dialogHeight:" + height + "pt;"+
		                                "status:no;help:no;scroll:no;");

}

$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });
</script>
	</BODY>
</HTML>
