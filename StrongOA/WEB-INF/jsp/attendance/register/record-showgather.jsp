<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sun.java_cup.internal.internal_error"%>
<%@page import="com.strongit.oa.attendance.register.MyRecord"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>

<%@include file="/common/include/rootPath.jsp"%>

<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<!--右键菜单样式 -->
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
			<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<!--右键菜单脚本 -->
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
			<style>
			
			TH{
				padding:0px 10px;			
			}
			.td1 {
					background-color: #ffffff;
					text-indent: 15px;
				}
		</style>

	</HEAD>
	<BODY class="contentbodymargin"  oncontextmenu="return false;" >
		<DIV id=contentborder align=center>
		<s:form id="myTableForm" theme="simple"
							action="/attendance/register/record!userRecordList.action">
				<input type="hidden" id="orgId" name="orgId" value="${orgId }">
				<input type="hidden" id="disLogo" name="disLogo" value="${disLogo }">
				
				<input type="hidden" name=userId id="userId"
					value="${userId }">
				<s:hidden name="beginTime" id="beginTime"></s:hidden>
				<s:hidden name="endTime" id="endTime"></s:hidden>
				<input type="hidden" name="begindate" id="begindate"
					value="${begindate }">
					<input type="hidden" name="enddate" id="enddate"
					value="${enddate }">
				<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td  style="height: 40"
										 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="00">
											<tr>
												<td>
												&nbsp;
												</td>
												<td width="25%" height="40">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9" onclick="search()">&nbsp;
												   考勤明细列表
												</td>
												<td width="75%" style="height: 40">
													
												</td>
											</tr>
											
										</table>
									</td>
								</tr>
							</table>
								
							<table width="100%" id="myTable_div" cellpadding=0 cellspacing=0
								onmousedown="downBody(this)" onmouseover="moveBody(this)"
								onmouseup="upBody(this)" onselectstart="selectBody(this)">
								<div id="myDiv"
									style="display:none; height:201px; left:12px; position:absolute; top:50px; width:28px; z-index:1">
									<hr id="myLine" width="1" size="200" noshade color="#F4F4F4">
								</div>
								<!-- 定位表格,距离顶,左,右2个像素,-->
								<tr>
									<td valign=top align="left">
									
										<!-- 定位表格包含数据表格开始-->
										<!-- 数据显示表格定义开始-->
										<!--表格定义 直接拷贝 idColNums定义为表格相应的数据区的ID列　nameColNum定义为表格相应的数据区的名称列(列值为下面Td序号从0开始)-->
										<table id="myTable" style="vertical-align: top;" align="left"
											cellSpacing=1 cellPadding=1 border=0 class="table1"
											width="100%" height="100%">
											<!--thead 表格Title定义开始，表格的宽度定义不要在这里定义放到数据区定义，所有TD的class="headerTD"-->
											<!--第一个Td为空-->
											<!--第二个Td为全选复选框，该复选框的ID　一定要为　id="checkall"-->
											<!--第三个Td及以后是显示数据区的Title定义所有的TD class="headerTD"-->
											<thead>
												<tr style="position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;">
													<td style="display:none"></td>
													<th class="biao_bg2" nowrap width="5%" >
														<input id="checkall" type="checkbox" 
															onclick="checkAll(this,document.getElementById('myTable_td'),'#A9B2CA',true)">
													</th>
													<th nowrap class="biao_bg2" width="" height="" showsize="20" 
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">出勤日期</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">姓名</th>
															<!-- <th class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">类型编号</th> -->	
																<%
																int k=Integer.parseInt(request.getAttribute("bancisize").toString());
																for(int i=0;i<k;i++){
																%>
																<th nowrap class="biao_bg2" width="" height=""  showsize="40" 
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">第<%= i+1 %>趟班</th>
																<%
																}
																 %>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">应出勤</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">迟到</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">早退</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">缺勤</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">类型</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">描述</th>
																<th nowrap class="biao_bg2" width="" height=""
																onmousemove="moveCol(this,document.getElementById('myTable_div'))"
																onclick="sort(this,true)">是否计算</th>
													<th nowrap class="biao_bg2" style="text-indent: 0px;"></th>
													<s:set value="dataRowTitle.size()" name="procount" />
												</tr>
											</thead>
											<!--thead 表格Title定义结束-->
											<!--tbody 表格数据区定义开始-->
											<!--第一个TD是提供记录ID值，不会在页面上显示-->
											<!--第二个ＴＤ的复选框，该Td的id一定要为　id="chkButtonTd"；　该复选框的值为记录ID值，复选框的ID和Name一定要为　id="chkButton" name="chkButton"-->
											<!--第三个TD及后面是显示数据区的定义，每项数据前加&nbsp;　如果需要在这里定义表格宽度-->
											<tbody oncontextmenu="chickRightMouse(event)"
												onmousedown="TableMouseDown('#A9B2CA')">
												
												<c:forEach begin="${begin}" end="${end}"
													items="${recordList}" var="vo"><tr>
													<td style="display:none">
														${record.attendId}
																</td>
													<td align="center" width="4%" id="chkButtonTd" class="td1"
														style="text-indent: 0px;" align="center"
														onmousemove="moveCol(this,document.getElementById('myTable_div'))">
														<input value="${vo.attendId }" showValue="${vo.userName }"
															onclick="checkValue(this,document.getElementById('myTable_td'),'#A9B2CA',true);"
															type="checkbox" id="chkButton" name="chkButton" />
													</td>
													<td nowrap align="left" style="overflow : visible; text-overflow:clip;background-color: #ffffff;" >
														${vo.attendTime}
													</td>
													<td nowrap align="left" class="td1">
														${vo.userName }
													</td>
													<c:if test="${vo.workList!=null}">
													<c:forEach items="${vo.workList}" var="bo" varStatus="index">
															<td nowrap align="left" class="td1">
																${bo }
															</td>
														</c:forEach>
												    </c:if>
													<td nowrap align="left" class="td1">${vo.shouldAttendHours }</td>
													<td nowrap align="left" class="td1">${vo.attendLaterTime }</td>
													
													<td nowrap align="left" class="td1">${vo.attendEarlyTime }</td>
													<td nowrap align="left" class="td1">${vo.absenceHours }</td>
													<td nowrap align="left" class="td1">${vo.attendanceType }</td>
													<td nowrap align="left" class="td1">${vo.attendDesc }</td>
													<td nowrap align="left" class="td1">
													<c:if test="${vo.isCalcu=='1' }">
													是
													</c:if>
													<c:if test="${vo.isCalcu=='0' }">
													否
													</c:if></td>
													<td class="td1" style="text-indent: 0px;"></td>
													</tr>
												</c:forEach> 
												</tbody>
											<!--tbody 表格数据区定义结束-->
											<!--tfoot 表格提示区定义开始-->
											<tfoot>
												<tr>
													<td nowrap colspan="<%=11+Integer.parseInt(request.getAttribute("bancisize").toString()) %>"
														align="left" class="td1" id="myTable_td">
														请选择:</td>
												</tr>
												<script>
													setTableStatus(myTable_td); setFootNum(1);
												</script>
											</tfoot>
											<!--tfoot 表格提示区定义开始-->
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<!-- 数据显示表格定义结束-->
										<!-- 分页显示表格定义开始-->
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr style="position:relative;top:expression(this.parentElement.offsetParent.parentElement.scrollTop);z-index:1;">
												<td width="2%" class="biao_bg2">
													&nbsp;
													<s:if test="infopage.orderBy!=null">
														<input type="hidden" id="orderBy" name="infopage.orderBy"
															value="${infopage.orderBy}">
													</s:if>
													<s:if test="infopage.order!=null">
														<input type="hidden" id="order" name="infopage.order"
															value="${infopage.order}">
													</s:if>
												</td>
												<td width="5%" height="25" class="biao_bg2">
													当前
												</td>
												<td width="10%" class="biao_bg2">
												
													${newpage }/
													${pages }
												</td>
												<td width="5%" class="biao_bg2">
													页
												</td>
												<td width="5%" class="biao_bg2">
													每页
												</td>
												<td width="5%" class="biao_bg2">
													<input id="pagesize" name="pagesize" type="text"
														size="2" value="${pagesize}">
												</td>
												<td width="6%" class="biao_bg2">
													<input name="Submit" type="button" class="input_bg"
														value="设置" onclick="gotoPage()">
												</td>
												<td width="7%" class="biao_bg2">
													数据总量
												</td>
												<td width="4%" class="biao_bg2">
													<%=request.getAttribute("count") %>
												</td>
												<td width="10%" class="biao_bg2">
													&nbsp;
												</td>
												<c:if test="${newpage>1}">
													<td width="6%" class="biao_bg2">
														<input name="Submit2" type="submit" class="input_bg"
															value="首页" onclick="gotoPage(1)">
													</td>
													<td width="7%" class="biao_bg2">
														<input name="Submit22" type="submit" class="input_bg"
															value="上一页" onclick="gotoPage(${newpage-1})">
													</td>
												</c:if>
												<c:if test="${newpage<pages}">
													<td width="7%" class="biao_bg2">
														<input name="Submit23" type="submit" class="input_bg"
															value="下一页" onclick="gotoPage(${newpage+1})">
													</td>
													<td width="6%" class="biao_bg2">
														<input name="Submit24" type="submit" class="input_bg"
															value="尾页"
															onclick="gotoPage(${pages })">
													</td>
												</c:if>
												<td width="7%" class="biao_bg2">
													转到
												</td>
												<td width="3%" class="biao_bg2">
													<input id="newpage" name="newpage" type="text" size="4"
														value="${newpage}">
												</td>
												<td width="2%" class="biao_bg2">
													页
												</td>
												<td width="5%" class="biao_bg2">
													<input name="Submit242" type="submit" class="input_bg"
														value="转" onclick="gotoPage()">
												</td>
												<td  class="biao_bg2">
													&nbsp;
												</td>
											</tr>
										</table>
										<!-- 分页显示表格定义结束-->

									</td>
								</tr>
							</table>
					
					</td>
				</tr>
			</table>
			
			</s:form>
		</DIV>
		<script language="javascript">
		setTableBorder(myTable);
		setHasCheck("true");
		setOrColor('#ffffff');
		
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	//item = new MenuItem("<%=root%>/images/ico/bianji.gif","编辑","",1,"ChangeWidthTable","checkMoreDis");
	//sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/ico/bianji.gif","计算考勤","calAttendence",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

	function calAttendence(){
		var orgId=document.getElementById("orgId").value;
		OpenWindow("<%=path%>/fileNameRedirectAction.action?toPage=/attendance/attendmaintain/calAttendence.jsp?orgId="+orgId,390,285,window);	
	}


	function gotoPage(no){
			if(no!=null&&no!=0)
			document.getElementById('newpage').value=no;
		document.getElementById('myTableForm').submit();
	}
	function submitForm(){
		document.getElementById('disLogo').value="search";
    	//document.forms[0].submit();
    	document.getElementById('myTableForm').submit();
    }
</script>
	</BODY>
</HTML>
