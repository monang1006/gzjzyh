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
		<title>会议通知列表</title>
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
		<s:form theme="simple" id="myTableForm" action="/meetingmanage/meetingnotice/meetingnotice!notList.action">
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
											<td width="55%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9" align="center">&nbsp;
												选择会议通知
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td width="45%"><table align="right"><tr>
											<td >
												<a class="Operation" href="#" onclick="addTitle()"> <img
														src="<%=root%>/images/ico/queding.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">确  定&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											
											<td >
												<a class="Operation" href="#" onclick="backSummary()"> <img
														src="<%=root%>/images/ico/ht.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">返  回&nbsp;</span> </a>
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
								getValueType="getValueByProperty" collection="${nopage.result}"
								page="${nopage}" pagename="nopage">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo"
												width="15" height="15" style="cursor: hand;" title="单击搜索">
										</td>
										<td width="25%" class="biao_bg1">
											<s:textfield name="noticeTitle" cssClass="search"
												title="请输入通知标题"></s:textfield>
										</td>

										<td width="30%" class="biao_bg1">

											<s:textfield name="noticeAddr" cssClass="search"
												title="请输入通知地址"></s:textfield>
										</td>

										<td class="biao_bg1" width="20%">
											<strong:newdate name="noticeStime" id="noticeStime"
												skin="whyGreen" isicon="true" dateobj="${noticeStime}"
												dateform="yyyy-MM-dd" width="112"></strong:newdate>
											&nbsp;&nbsp;
										</td>
										<td class="biao_bg1" width="20%">
											<strong:newdate name="noticeEndTime" id="noticeEndTime"
												skin="whyGreen" isicon="true" dateobj="${noticeEndTime}"
												dateform="yyyy-MM-dd" width="112"></strong:newdate>
										</td>
										
										<td width="5%" class="biao_bg1">
								 					&nbsp;
										</td>

									</tr>
								</table>
								<webflex:flexRadioCol caption="选择" property="noticeId"
									showValue="noticeTitle" width="5%" isCanDrag="false"
									isCanSort="false"></webflex:flexRadioCol>
								<webflex:flexTextCol caption="通知标题" property="noticeTitle"
									showValue="noticeTitle" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="通知地址" property="noticeAddr"
									showValue="noticeAddr" width="30%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="开始时间" property="noticeStime"
									showValue="noticeStime" dateFormat="yyyy-MM-dd" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="结束时间" property="noticeEndTime"
									showValue="noticeEndTime" dateFormat="yyyy-MM-dd" width="20%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>

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
	item = new MenuItem("<%=root%>/images/ico/quxiao.gif","返 回","backSummary",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function addTitle(){
  var id=getSingleValue();
	if(id==null || id==""){
		alert("请选择需要写纪要的会议!");
		return;
	
	}else{	
		document.forms(0).action="<%=path %>/meetingmanage/meetingsummary/meetingsummary!input.action?notId="+id;
		document.forms(0).submit();
	}		
	
}

function backSummary(){
	
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
		