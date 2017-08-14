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
		 
		 </script> 
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload=initMenuT()>
		 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/meetingmanage/meetingconference/meetingconference!conList.action">
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
											<td width="65%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9" align="center">&nbsp;
												选择会议
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

									<td width="20%" class="biao_bg1">										
										<s:textfield name="conferenceName" cssClass="search" title="请输入会议主题"></s:textfield> 
									</td>									
									
									<td width="25%" class="biao_bg1">										
										<s:textfield name="conferenceDemo" cssClass="search" title="请输入会议描述"></s:textfield> 
									</td>
									
									<td width="20%" class="biao_bg1">										
										<s:textfield name="conferenceAddr" cssClass="search" title="请输入会议地址"></s:textfield> 
									</td>
								
									<td class="biao_bg1" width="15%">
											<strong:newdate name="conferenceStime" id="conferenceStime"
												skin="whyGreen" isicon="true" dateobj="${conferenceStime}"
												dateform="yyyy-MM-dd" width="95%"></strong:newdate>
										</td>
										<td class="biao_bg1" width="15%">
											<strong:newdate name="conferenceEndtime"
												id="conferenceEndtime" skin="whyGreen" isicon="true"
												dateobj="${conferenceEndtime}" dateform="yyyy-MM-dd"
												width="95%"></strong:newdate>
										</td>
								
								<td width="5%" class="biao_bg1">
								 &nbsp;
									</td>
								</tr>
							</table>
								<webflex:flexRadioCol caption="选择" property="conferenceId"
									showValue="conferenceName" width="5%" isCanDrag="false"
									isCanSort="false"></webflex:flexRadioCol>
								<webflex:flexTextCol caption="会议主题" property="conferenceName"
									showValue="conferenceName" width="20%" isCanDrag="true"
									isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议描述" property="conferenceDemo"
									showValue="conferenceDemo" width="25%" isCanDrag="true"
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexTextCol caption="会议地址" property="conferenceAddr"
									showValue="conferenceAddr" width="20%" isCanDrag="true"
									isCanSort="true" showsize="15"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="conferenceStime"
									showValue="conferenceStime" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="conferenceEndtime"
									showValue="conferenceEndtime" dateFormat="yyyy-MM-dd"
									width="15%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
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
	if(id==null || id==""){
		window.close();
	}else{	
	 window.dialogArguments.document.getElementById("conId").value=id;

		$.post( 	           
	  		"<%=path%>/meetingmanage/meetingconference/meetingconference!conname.action",
			{conId:id},
			function(conName){
		   		window.dialogArguments.document.getElementById("conName").value=conName;
			}
			)
		$.post( 	           
	  		"<%=path%>/meetingmanage/meetingconference/meetingconference!topSubject.action",
			{conId:id},
			function(topSubject){
				window.dialogArguments.document.getElementById("topSubject").value=topSubject;
				window.close();	
				}
		)
		
	}		
	
}

function backNotice(){
	
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
		