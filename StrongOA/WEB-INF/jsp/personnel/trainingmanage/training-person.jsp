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
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript' src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script language='javascript' src='<%=path%>/common/js/jquery/jquery-1.2.6.js'></script>
		<script language="javascript" src="<%=path%>/common/js/common/search.js"></script>
		<script language="javascript" src="<%=path%>/common/js/common/common.js"></script>
		<!--右键菜单脚本 -->
		<script language="javascript" src="<%=path%>/common/js/menu/menu.js"></script>
		<!--右键菜单脚本 -->
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.MultiFile.js'></script>
		<script language='javascript' src='<%=path%>/common/js/upload/jquery.blockUI.js'></script>
	</HEAD>
	
	<BODY class=contentbodymargin oncontextmenu="return false;" onLoad="initMenuT()">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form  id="trainingForm" action="/personnel/trainingmanage/training!person.action">
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
											<td width="15%">
												<img
													src="<%=frameroot%>/images/perspective_leftside/ico.gif"
													width="9" height="9">&nbsp;
												培训信息列表
											</td>
											<td width="*">&nbsp;
												
											</td>
											
											<td width="50">
												<a class="Operation" href="#" onClick="goBack()">
												<img src="<%=root%>/images/ico/message2.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">返回</span> </a>
											</td>
										
											<td width="5"></td>
											<td width="50">
												<a class="Operation" href="#" onClick="viewTraining()">
													<img src="<%=root%>/images/ico/chakan.gif" width="15"
														height="15" class="img_s"><span id="test"
													style="cursor:hand">查看</span> </a>
											</td>
											<td width="5"></td>
											
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<webflex:flexTable name="training" width="100%" height="364px"
							wholeCss="table1" property="trainId" isCanDrag="true"
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
									    <s:textfield name="sName"  cssClass="search" title="请输入培训人员名称"></s:textfield>
									</td>
									<td width="20%" class="biao_bg1">
									   
									</td>
									<td width="15%" class="biao_bg1">
									  
									</td>
							
									<td width="15%" class="biao_bg1">
                                  
									</td>
									
									<td width="15%" class="biao_bg1">
 									
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
		
						    <webflex:flexDateCol caption="开始时间" property="trainStartdate" showsize="16" dateFormat="yyyy-MM-dd"
								showValue="trainStartdate" isCanDrag="true" width="15%" isCanSort="true"></webflex:flexDateCol>
							<webflex:flexDateCol caption="结束时间" property="trainEnddate" showsize="16" dateFormat="yyyy-MM-dd"
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
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewTraining",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
function goBack(){
	window.location = "<%=root%>/personnel/trainingmanage/training.action";
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
