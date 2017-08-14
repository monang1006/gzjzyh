<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.sun.java_cup.internal.internal_error"%>
<%@page import="com.strongit.oa.attendance.register.MyRecord"%>
<%@page import="java.text.SimpleDateFormat"%>
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
		
		<script type="text/javascript">
		
		</script>
	</HEAD>
	<BODY class="contentbodymargin"  oncontextmenu="return false;" >
		<script language="javascript" type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
		<s:form id="myTableForm" theme="simple"
							action="/attendance/report/attendReport!dateReport.action">
                            <input type="hidden" id="orgId" name="orgId" value="${orgId }">
                            <input type="hidden" id="disLogo" name="disLogo" value="${disLogo }">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%" align="center">
						
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
												<td width="30%" height="40">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9" onclick="search()">&nbsp;
												   考勤明细列表
												</td>
												<td width="70%" style="height: 40">
													<table border="0" align="right" cellpadding="00"
														cellspacing="0">
														<tr>
														<td width="5"></td>
														
																		
															
															<td width="*">
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

					<table width="80%" border="0"  cellpadding="0" cellspacing="0"
										>
									<tr>
									<td width="30%" ></td>
										<td width="400" align="center" >
											<strong><font size="4"><% Date date=new Date();
											SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月份");
											  out.print( format.format(date)); %>  日考勤汇总表 
											  <hr></font> </strong> 
										</td>
										<td width="30%" ></td>
									</tr>
										<tr>
										<td width="30%" align="left" >
									</td>
									<td width="100%" align="right"  colspan="2">
									填报日期:
									<% 
											SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
											  out.print( df.format(date));%>
									</td>
										
									</tr>
							</table>	
		
							<table width="80%" id="myTable_div" cellpadding=0 cellspacing=0>
								
								<!-- 定位表格,距离顶,左,右2个像素,-->
								<tr>
									<td valign=top align="left">
									
										<!-- 定位表格包含数据表格开始-->
										<!-- 数据显示表格定义开始-->
										<!--表格定义 直接拷贝 idColNums定义为表格相应的数据区的ID列　nameColNum定义为表格相应的数据区的名称列(列值为下面Td序号从0开始)-->
										<table id="myTable"	cellSpacing=0 cellPadding=0 border=0 
											width="100%" height="100%">
										
										
												<tr>
													<td nowrap showsize="15" rowspan="2" align="center"
														style="border: 1px solid #363636;">
														出勤日期
													</td>
													<td nowrap align="center" showsize="15" 
														rowspan="2"
														style="border: 1px solid #363636; border-left: 0">
														姓名
													</td>
													<%
														int k = Integer.parseInt(request.getAttribute("bancisize")
																	.toString());
															for (int i = 0; i < k; i++) {
													%>
													<td nowrap align="center" showsize="15" rowspan="2"
														style="border: 1px solid #363636; border-left: 0">
														第<%=i + 1%>趟班
													</td>
													<%
														}
													%>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0;border-bottom: 0;"
														colspan="2" height="">
														单位/分
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0;border-bottom: 0;"
														colspan="3" width="" height="">
														单位/小时
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0;border-bottom: 0;" >&nbsp;</td>
												</tr>
												<tr>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0">
														迟到
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0" >
														早退
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0" >
														应出勤
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0" >
														加班
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0" >
														缺勤
													</td>
													<td nowrap align="center" showsize="15"
														style="border: 1px solid #363636; border-left: 0" >
														描述
													</td>
												</tr>
											<!--thead 表格Title定义结束-->
											<!--tbody 表格数据区定义开始-->
											<!--第一个TD是提供记录ID值，不会在页面上显示-->
											<!--第二个ＴＤ的复选框，该Td的id一定要为　id="chkButtonTd"；　该复选框的值为记录ID值，复选框的ID和Name一定要为　id="chkButton" name="chkButton"-->
											<!--第三个TD及后面是显示数据区的定义，每项数据前加&nbsp;　如果需要在这里定义表格宽度-->
											<tbody >
												
												<c:forEach begin="${begin}" end="${end}"
													items="${recordList}" var="vo"><tr>
													
													<td nowrap align="center" style="border: 1px solid #363636; border-top: 0;" >
														${vo.attendTime}
													</td>
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">
														${vo.userName }
													</td>
													<c:forEach items="${vo.workList}" var="bo" varStatus="index">
															<td nowrap align="center" width="160" style="border: 1px solid #363636; border-left: 0;border-top: 0;">
						                                     	${bo }&nbsp;
															</td>
														</c:forEach>
													
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">${vo.attendLaterTime }</td>
													
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">${vo.attendEarlyTime }</td>
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">${vo.shouldAttendHours }</td>
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">${vo.jiaBanHours }</td>
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">${vo.absenceHours }</td>
													<td nowrap align="center" style="border: 1px solid #363636; border-left: 0;border-top: 0;">&nbsp;${vo.attendDesc }</td>
													
													</tr>
												</c:forEach> 
												</tbody>
										
											
											<!--tfoot 表格提示区定义开始-->
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<!-- 数据显示表格定义结束-->
										<!-- 分页显示表格定义开始-->
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr >
												<td width="100%" align="right">
													&nbsp;
													<s:if test="infopage.orderBy!=null">
														<input type="hidden" id="orderBy" name="infopage.orderBy"
															value="${infopage.orderBy}">
													</s:if>
													<s:if test="infopage.order!=null">
														<input type="hidden" id="order" name="infopage.order"
															value="${infopage.order}">
													</s:if>
													当前
												
													${newpage }/
													${pages }
													页
											
												
													数据总量
													<%=request.getAttribute("count")%>
												
													&nbsp;
												
												<c:if test="${newpage>1}">
													
														<input name="Submit2" type="submit" class="input_bg"
															value="首页" onclick="gotoPage(1)">
													
														<input name="Submit22" type="submit" class="input_bg"
															value="上一页" onclick="gotoPage(${newpage-1})">
													
												</c:if>
												<c:if test="${newpage<pages}">
													
														<input name="Submit23" type="submit" class="input_bg"
															value="下一页" onclick="gotoPage(${newpage+1})">
													
														<input name="Submit24" type="submit" class="input_bg"
															value="尾页"
															onclick="gotoPage(${pages })">
													
												</c:if>
												
													转到
												
													<input id="newpage" name="newpage" type="text" size="4"
														value="${newpage}">
												
													页
												
													<input name="Submit242" type="submit" class="input_bg"
														value="转" onclick="gotoPage()">
												
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
