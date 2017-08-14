<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<base target="_self"/>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>选择议题</title>
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
			<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
<script type="text/javascript">
		 function showimg(topicStatus){      
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
				rv = "<font color='red'>占用中</font>&nbsp&nbsp";
			}
			if(topicStatus == '4'){
				rv = "<font color='blue'>占用完毕</font>&nbsp&nbsp";
			}
			return rv;
		}
		
		</SCRIPT>
	</HEAD>

	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT();">
	 <script type="text/javascript"
      src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form theme="simple" id="myTableForm" action="/meetingmanage/meetingtopic/meetingtopic!topicList.action">
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
											<td width="40%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9"  align="center">&nbsp;
												选择议题
											</td>
											<td width="*">
												&nbsp;
											</td>
											<td >
												<a class="Operation" href="#" onclick="confirm1()"> <img
														src="<%=root%>/images/ico/queding.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">确定&nbsp;</span> </a>
											</td>
											<td width="5"></td>
											<td >
												<a class="Operation" href="#" onclick="cancel()"> <img
														src="<%=root%>/images/ico/quxiao.gif"
														width="15" height="15" class="img_s"><span id="test"
													style="cursor:hand">取消&nbsp;</span> </a>
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
									<td width="5%"  align="center" class="biao_bg1">
										<img src="<%=root%>/images/ico/sousuo.gif" id="img_sousuo" width="15" height="15" style="cursor: hand;" title="单击搜索">
									</td>
									<td width="25%" class="biao_bg1">	
								<s:textfield name="tSubject" cssClass="search" title="请输入议题主题"></s:textfield> 
									</td>
									
									<td width="25%" class="biao_bg1">		
									<s:textfield name="tSorts" cssClass="search" title="请输入分类名称"></s:textfield>		
									</td>
																	
									<td width="25%" class="biao_bg1">
									 <strong:newdate name="tEstime" id="tEstime" 
                     				 skin="whyGreen" isicon="true" dateobj="${tEstime}" dateform="yyyy-MM-dd" width="96%"></strong:newdate>
									</td>
									<td width="15%" class="biao_bg1">
								     <s:select name="tStatus"  list="#{'':'选状态','2':'已审核','3':'占用中'}" listKey="key" listValue="value" style="width:6.5em" />
									</td>
									<td width="5%" class="biao_bg1">
								 		&nbsp;
									</td>
								</tr>
							
							</table>
							
							<webflex:flexCheckBoxCol caption="选择" property="topicId"
								showValue="topicSubject" width="5%" isCheckAll="true" isCanDrag="false"
								isCanSort="false"></webflex:flexCheckBoxCol>	
							<webflex:flexEnumCol caption="议题状态" mapobj="${meetingMap}" property="topicStatus" showValue="topicCode" width="0" isCanDrag="true" isCanSort="true"></webflex:flexEnumCol>							
							<webflex:flexTextCol caption="议题主题" property="topicSubject"
								showValue="topicSubject" width="25%" isCanDrag="true" isCanSort="true"showsize="30"></webflex:flexTextCol>
							<webflex:flexTextCol caption="议题分类" property="topicsort.topicsortId"
								showValue="topicsort.topicsortName" width="25%" isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
							<webflex:flexDateCol caption="创建时间" property="topicEstime" showValue="topicEstime" dateFormat="yyyy-MM-dd" width="25%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
						<webflex:flexTextCol caption="议题状态" property="topicStatus" showValue="javascript:showimg(topicStatus)" width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/ico/queding.gif","确 定","confirm1",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/quxiao.gif","取 消 ","cancel",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function confirm1(){

	var id=getValue();	
	if(id == null || id == ""){
		window.close();
	}else{
		var ss=$(":checked").parent().next().attr("value");
 		window.dialogArguments.document.getElementById("topId").value=id;
		$.post( 	           
	  		"<%=path%>/meetingmanage/meetingtopic/meetingtopic!topsubject.action",
			{topId:id},
			function(topSubject){
				if (ss=="2"){
		   			window.dialogArguments.document.getElementById("topSubject").value=topSubject;
		   			window.close();
		   			}
		   		else{
		   			if(confirm("该议题已被使用，若要使用，将覆盖原来与之关联的会议!")){
		   				window.dialogArguments.document.getElementById("topSubject").value=topSubject;
		   				window.close();
		   			}
		   			
		   		}
			}		
		)
	}
}
 
function cancel(){

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
		