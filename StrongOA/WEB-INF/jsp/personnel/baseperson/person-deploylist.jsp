<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<HTML>
	<HEAD>
		<TITLE>人员调配记录列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<SCRIPT language="javascript"
			src="<%=path%>/oa/js/personnel/common.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onload="initMenuT()">
		<script type="text/javascript"
      		src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/personnel/baseperson/person!viewDeloyInfo.action" method="post">
				<input type="hidden" name="orgId" id="orgId" value="${orgId}">
				<input type="hidden" name="personId" id="personId" value="${personId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="40"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>&nbsp;</td>
												<td width="20%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													人员调配记录列表
												</td>
												<td width="5%">
													&nbsp;
												</td>
												<td width="82%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="100%" align="right">
																<table width="100%" border="0" align="right"
																	cellpadding="0" cellspacing="0">
																	<tr>
																		<td width="*">
																			&nbsp;
																		</td>
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:viewinfo();"> <img
																					src="<%=root%>/images/ico/chakan.gif" width="15"
																					height="15" class="img_s">查看</a>
																		</td>
																		<td width="5"></td>	
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:del();"> <img
																					src="<%=root%>/images/ico/shanchu.gif" width="15"
																					height="15" class="img_s">删除</a>
																		</td>
																		<td width="5"></td>	
																		<td width="58" align="right">
																			<a class="Operation" href="javascript:goback();"> <img
																					src="<%=root%>/images/ico/ht.gif" width="15"
																					height="15" class="img_s">返回</a>
																		</td>
																		<td width="5"></td>	
																	</tr>
																</table>
															</td>
														</tr>
														<tr higth="20"></tr>
														<tr>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>

							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="deployinfoId" isCanDrag="true" 
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty"  collection="${deployInfoList}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												id="img_sousuo" height="16" style="cursor: hand;"
												title="单击搜索">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <strong:newdate id="exchangeTime" name="deployinfo.exchangeTime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${deployinfo.exchangeTime}" classtyle="search" title="调配开始日期"/>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											 <strong:newdate id="lastTime" name="deployinfo.lastTime"
												dateform="yyyy-MM-dd"  isicon="true" width="100%"
												dateobj="${deployinfo.lastTime}" classtyle="search" title="调配结束日期"/>
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:select list="deployTypeMap" id="pdepId" name="deployinfo.deployInfo.pdepId"
												listKey="key" listValue="value" headerKey=""
												headerValue="请选择调配类别" cssStyle="width:100%"
												onchange="$('#img_sousuo').click();" />
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<input  type="text" id="oldInfos" name="deployinfo.oldInfos"
												value="${deployinfo.oldInfos}" class="search" title="请输入调配前信息" />
										</td>
										<td width="25%" align="center" class="biao_bg1">
											<input  type="text" id="newInfos" name="deployinfo.newInfos"
												value="${deployinfo.newInfos}" class="search" title="请输入调配后信息" />
										</td>
										<%--<td width="18%" align="center" class="biao_bg1">
											<input type="text" id="exchangeWhy" name="deployinfo.exchangeWhy"
												value="${deployinfo.exchangeWhy}" class="search" value=""
												title="请输入调配原因" />
										</td>
										--%><td width="*%" align="center" class="biao_bg1">
											&nbsp;
										</td>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="deployinfoId"
									showValue="personName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="人员" property="deployinfoId"
									showValue="personName" width="15%" isCanDrag="true"
									isCanSort="true" onclick="getinfo(this.value)"></webflex:flexTextCol>
								<webflex:flexDateCol caption="调配日期" 
									property="exchangeTime" showValue="exchangeTime" width="15%" showsize="20"
									isCanDrag="true" isCanSort="true" dateFormat="yyyy-MM-dd" ></webflex:flexDateCol>
								<webflex:flexEnumCol caption="调配类别"  mapobj="${deployTypeMap}" property="deployInfo.pdepId"
									showValue="deployInfo.pdepId" width="15%" isCanDrag="true"
									isCanSort="true"></webflex:flexEnumCol>	
								<webflex:flexTextCol caption="调配前" property="oldInfos"
									showValue="oldInfos" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>	
								<webflex:flexTextCol caption="调配后" property="newInfos"
									showValue="newInfos" width="25%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>	
								<%--<webflex:flexTextCol caption="调配原因" property="exchangeWhy"
									showValue="exchangeWhy" width="18%" isCanDrag="true"
									isCanSort="true"></webflex:flexTextCol>	
							--%></webflex:flexTable>
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
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewinfo",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/fankui.gif","返回","goback",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

function del(){
	var orgId=$("#orgId").val();
	var personId=$("#personId").val();
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择用户！');
		return;
	}
	window.location="<%=path%>/personnel/baseperson/person!deleteDeployInfo.action?orgId="+orgId+"&keyid="+id+"&personId="+personId;
}

function goback(){
    var orgId=document.getElementById("orgId").value;
	window.location="<%=path%>/personnel/baseperson/person.action?orgId="+orgId;
}

function getinfo(value){
	var orgId=$("#orgId").val();
	window.showModalDialog("<%=path%>/personnel/baseperson/person!viewDeployDetails.action?orgId="+orgId+"&keyid="+value,window,"help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:450px");
}

function viewinfo(){
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择调配记录！');
		return;
	}
	if(id.length >32){
		alert('只能查看一条调配信息！');
		return;
	}
	var orgId=$("#orgId").val();
	window.showModalDialog("<%=path%>/personnel/baseperson/person!viewDeployDetails.action?orgId="+orgId+"&keyid="+id,window,"help:no;status:no;scroll:no;dialogWidth:500px; dialogHeight:450px");
}

 $(document).ready(function(){
    $("#img_sousuo").click(function(){
    	$("Form").submit();
    });     
  });
</script>
	</BODY>
</HTML>
