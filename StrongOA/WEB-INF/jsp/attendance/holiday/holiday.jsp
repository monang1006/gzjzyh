<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>我的收藏列表</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
       
   
        function showisEnable(value){
           if(value=="0"){
              return "启用";
           }else{
              return "禁用";
           }
        }
       </script>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;"
		onLoad="initMenuT()">
		<script language="javascript" type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form theme="simple" id="myTableForm"
				action="/attendance/holiday/holiday.action">
				<input type="hidden" name="disLogo" id="disLogo" value="${disLogo}">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					style="vertical-align: top;">
					<tr>
						<td height="100%">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">


								<tr>
									<td height="40"
										style="FILTER: progid : DXImageTransform . Microsoft . Gradient(gradientType = 0, startColorStr = #ededed, endColorStr = #ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="30%">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													法定假日列表
												</td>
												<td width="5%">
													&nbsp;
												</td>
												<td width=70%">
													<table width="100%" border="0" align="right"
														cellpadding="0" cellspacing="0">
														<tr>
															<td width="*">
																&nbsp;
															</td>
															<td width="70">
																<a class="Operation" href="javascript:show();"> <img
																		src="<%=root%>/images/ico/chakan.gif" width="15"
																		height="15" class="img_s">查看</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:addHoliday();"> <img
																		src="<%=root%>/images/ico/tianjia.gif" width="15"
																		height="15" class="img_s">添加</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:editHoliday();"> <img
																		src="<%=root%>/images/ico/bianji.gif" width="15"
																		height="15" class="img_s">编辑</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:del();"> <img
																		src="<%=root%>/images/ico/shanchu.gif" width="15"
																		height="15" class="img_s">删除</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:updateYESenable();"> <img
																		src="<%=root%>/images/ico/kaishi.gif" width="15"
																		height="15" class="img_s">启用</a>
															</td>
															<td width="5"></td>
															<td width="70">
																<a class="Operation" href="javascript:updateNOenable();"> <img
																		src="<%=root%>/images/ico/guanbi.gif" width="15"
																		height="15" class="img_s">禁用</a>
															</td>
															<td width="5">
																&nbsp;
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="mykmId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="5%" align="center" class="biao_bg1">
											<img src="<%=root%>/images/ico/sousuo.gif" width="17"
												height="16" style="cursor: hand;" title="单击搜索"
												onClick="getListBySta();">
										</td>
										<td width="20%" align="center" class="biao_bg1">
											<input id="holidayName" name="model.holidayName" type="text"
												style="width: 100%" maxlength="100"
												value="${model.holidayName}" class="search" title="假日名称">
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="holidayStime" name="model.holidayStime"
												dateform="MM-dd" isicon="true" width="100%"
												nowvalue="${model.holidayStime}" classtyle="search"
												title="假日开始时间" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="holidayEtime" name="model.holidayEtime"
												dateform="MM-dd" isicon="true" width="100%"
												nowvalue="${model.holidayEtime}" classtyle="search"
												title="假日结束时间" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="henableTime" name="model.henableTime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${model.henableTime}" classtyle="search"
												title="生效时间" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<strong:newdate id="hdisableTime" name="model.hdisableTime"
												dateform="yyyy-MM-dd" isicon="true" width="100%"
												dateobj="${model.hdisableTime}" classtyle="search"
												title="失效时间" />
										</td>
										<td width="15%" align="center" class="biao_bg1">
											<s:select id="isEnable" style="width:100%;"
												name="model.isEnable" list="#{'是否启用':'','启用':'0','禁用':'1'}"
												listKey="value" listValue="key"></s:select>
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="holidayId"
									showValue="holidayName" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="假日名称" property="holidayName"
									showValue="holidayName" width="20%" isCanDrag="true"
									isCanSort="true" showsize="20"></webflex:flexTextCol>
								<webflex:flexTextCol caption="假日开始时间" property="holidayStime"
									showValue="holidayStime" width="15%" isCanDrag="true"
									isCanSort="true" showsize="20"></webflex:flexTextCol>
								<webflex:flexTextCol caption="假日结束时间" property="holidayEtime"
									showValue="holidayEtime" width="15%" isCanDrag="true"
									isCanSort="true" showsize="20"></webflex:flexTextCol>
								<webflex:flexDateCol caption="生效时间" property="henableTime"
									showValue="henableTime" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexDateCol caption="失效时间" property="hdisableTime"
									showValue="hdisableTime" dateFormat="yyyy-MM-dd" width="15%"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="是否启用" property="isEnable"
									showValue="javascript:showisEnable(isEnable);" width="15%"
									isCanDrag="true" isCanSort="true" showsize="15"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","show",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/ico/tianjia.gif","添加","addHoliday",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","editHoliday",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/ico/shanchu.gif","删除","del",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","启用","updateYESenable",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);   
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","禁用","updateNOenable",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
    sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}
 function addHoliday(){
           var audit= window.showModalDialog("<%=root%>/attendance/holiday/holiday!input.action",window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
        }
function editHoliday(){
	var id=getValue();
	if(id==""){
	alert("请选择一条申请单类型!");
	return;
	}
	if(id.length>32){
	alert("不可以同时修改多条申请单类型!");
	return;
	}
	 var audit= window.showModalDialog("<%=root%>/attendance/holiday/holiday!input.action?holidayId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
}
function show(){
	var id=getValue();
	if(id==""){
	alert("请选择要查看的法定假日!");
	return;
	}
	if(id.length>32){
	alert("不可以同时查看多条法定假日!");
	return;
	}
	 var audit= window.showModalDialog("<%=root%>/attendance/holiday/holiday!show.action?holidayId="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:400px');
		
}
function del(){//废除稿件
	var id=getValue();
	if(id == null||id == ''){
		alert('请选择要删除的法定假日！');
		return;
	}
	if(confirm("确定删除吗?")) 
	{ 
	   location = "<%=root%>/attendance/holiday/holiday!delete.action?holidayId="+id
	} 
		
}
function updateYESenable(){//启用
	var id=getValue();
	if(id==null||id==""){
	  alert("请先选择申请单类型");
	  return;
	}
	 location = "<%=root%>/attendance/holiday/holiday!isEnableType.action?holidayId="+id+"&isEnable=0";
}
function updateNOenable(){//禁用
	var id=getValue();
	if(id==null||id==""){
	  alert("请先选择申请单类型");
	  return;
	}
	 location = "<%=root%>/attendance/holiday/holiday!isEnableType.action?holidayId="+id+"&isEnable=1";
}
function getListBySta(){	//根据属性查询
	document.getElementById("disLogo").value="search";
	document.getElementById("myTableForm").submit();
}



</script>
	</BODY>
</HTML>
