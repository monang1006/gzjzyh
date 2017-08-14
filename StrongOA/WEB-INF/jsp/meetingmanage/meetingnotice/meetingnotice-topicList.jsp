<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
	<base target="_self">
    <%@include file="/common/include/meta.jsp"%>
		<title>议题列表</title>
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
	
		<script language='javascript'
			src='<%=request.getContextPath()%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript"
			src="<%=request.getContextPath()%>/common/js/menu/menu.js"></SCRIPT>
	
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/common.js"></script>
		
		 <script type="text/javascript">
		 function showimg(topicStatus){
  //alert(noticeIs);
      
			var rv = '' ;
			if(topicStatus == '0'){
				rv = "<font color='#90036'>未送审</font>&nbsp&nbsp";
			}
			if(topicStatus == '1'){
				rv = "<font color='#ff1119'>审核中</font>&nbsp&nbsp";
			}
			if(topicStatus == '2'){
				rv = "<font color='#63ad00'>已审核</font>&nbsp&nbsp";
			}
			
			if(topicStatus == '3'){
				rv = "<font color='#ff970d'>开会中</font>&nbsp&nbsp";
			}
			if(topicStatus == '4'){
				rv = "<font color='#38d7ff'>已结束</font>&nbsp&nbsp";
			}
			
			return rv;
		}
		 
		 
		 </script> 
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
		 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/meetingmanage/meetingnotice/meetingnotice!topicList.action">
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
											<td width="75%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9" align="center">&nbsp;
												选择会议议题
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td >
												<a class="Operation" href="#" onclick="addTitle()"> <img
														src="<%=root%>/images/ico/queding.gif"
														width="14" height="14" class="img_s"><span id="test"
													style="cursor:hand">确  定&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="backNotice()"> <img
														src="<%=root%>/images/ico/ht.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">返  回&nbsp;</span> </a>
											</td>
											
											<td width="5"></td>
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
								<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="17" height="16" style="cursor: hand;" title="单击搜索">
									</td>
									<td width="15%" class="biao_bg1">
										<s:textfield name="topiccode" cssClass="search" title="请输入议题编号"></s:textfield>  
									</td>
									<td width="20%" class="biao_bg1">
										
										<s:textfield name="topicsubject" cssClass="search" title="请输入议题主题"></s:textfield> 
									</td>
									<td width="20%" class="biao_bg1">
										
										<s:textfield name="topicsort" cssClass="search" title="请输入议题分类名称"></s:textfield> 
									</td>
									
									<td class="biao_bg1" width="15%">
									 <strong:newdate name="startDate" id="startDate" 
                      skin="whyGreen" isicon="true" dateobj="${startDate}" dateform="yyyy-MM-dd" width="102"></strong:newdate>
									</td>
									<td class="biao_bg1" width="15%">
									 <strong:newdate name="endDate" id="endDate" 
                      skin="whyGreen" isicon="true" dateobj="${endDate}" dateform="yyyy-MM-dd" width="103"></strong:newdate>
									</td>
									<td width="10%" class="biao_bg1">
								     <s:select name="meetinstatus"  list="#{'2':'已审核','0':'等待中','1':'审核中','3':'开会中','4':'开会完'}" listKey="key" listValue="value" style="width:5.5em" disabled="true"/>
									</td>
								<td width="5%" class="biao_bg1">
								 &nbsp;
									</td>
								</tr>
							</table>
							<webflex:flexRadioCol caption="选择" property="topicId"
								showValue="topicCode" width="5%" isCanDrag="false"
								isCanSort="false"></webflex:flexRadioCol>
					<webflex:flexTextCol caption="议题编号" property="topicCode" showValue="topicCode"
								width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题主题" property="topicSubject"
								showValue="topicSubject" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题分类" property="topicsort.topicsortName"
								showValue="topicsort.topicsortName" width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							<webflex:flexDateCol caption="开始时间" property="topicEstime" showValue="topicEstime" dateFormat="yyyy-MM-dd" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束时间" property="topicEndtime" showValue="topicEndtime" dateFormat="yyyy-MM-dd" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
				<webflex:flexTextCol caption="会议状态" property="topicId" showValue="javascript:showimg(topicStatus)" width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","确 定","addTitle",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/quxiao.gif","返 回","backNotice",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTitle(){
  var id=getSingleValue();
	//alert(id);
		if(id==null || id==""){
		alert("请选择需要写通知的记录！");
			return;
		}
  //var result=window.showModalDialog("<%=path %>/fileNameRedirectAction.action?toPage=/meetingmanage/meetingnotice/meetnoticeframe.jsp?meetingId="+id,window,'help:no;status:no;scroll:no;dialogWidth:700px; dialogHeight:580px');
 // window.close();
  document.forms(0).action="<%=path %>/fileNameRedirectAction.action?toPage=/meetingmanage/meetingnotice/titleframe.jsp?meetingId="+id;
  document.forms(0).submit();
}
function backNotice(){
	//location="<%=path %>/meetingmanage/meetingnotice/meetingnotice.action";
	window.close();
}


$(document).ready(function(){
        $("#img_sousuo").click(function(){
        	$("form").submit();
        });     
      });

</script>
	</BODY>
</HTML>
		