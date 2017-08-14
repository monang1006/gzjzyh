<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@include file="/common/include/rootPath.jsp"%>
<%
Date date=new Date();
SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
String dateStr=sdf.format(date);
%>
<HTML>
	<HEAD>
		<TITLE>上下班记录</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=path%>/common/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<!--右键菜单样式 -->
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css
			rel=stylesheet>
		<LINK href="<%=frameroot%>/css/search.css" type=text/css
			rel=stylesheet>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/menu/menu.js"></SCRIPT>
		<!--右键菜单脚本 -->
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/address/address.js"
			type="text/javascript"></script>
		<SCRIPT type="text/javascript">
			function init(){
				var startDate="${startDate}";
				var endDate="${endDate}";
				var   Nowdate=new   Date();  
				if(startDate==null||startDate==""){
  					document.getElementById("startDate").value=new Date(Nowdate.getYear(),Nowdate.getMonth(),1);   	
				}
				if(endDate==null||endDate==""){
					 document.getElementById("endDate").value=new Date();
				}
			}
			 //情况说明
			function describe(){
				var id=getValue();
				if(id==""){
					alert("请选择一条考勤登记记录！");
					return;
				}else if(id.indexOf(",")!=-1){
					alert("只能选择一条考勤登记记录！");
					return;
				}
			    window.showModalDialog("<%=root%>/attendance/register/register!getAttandenceByTime.action?startDate="+id,window,'help:no;status:no;scroll:no;dialogWidth:450px; dialogHeight:300px');
			}
			
			function submitForm(){
				document.getElementById("myTableForm").submit();
			}
		</SCRIPT>
	</HEAD>
	<BODY class=contentbodymargin oncontextmenu="return false;" onload="initMenuT();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td>
												&nbsp;
												</td>
											<td width="30%">
												<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
													height="9">&nbsp;
												上下班记录
											</td>
											<td width="70%">
												<table width="100%" border="0" align="right" cellpadding="0"
													cellspacing="0">
													<tr>
														<td width="100%" align="right">
															<table width="100%" border="0" align="right"
																cellpadding="0" cellspacing="0">
																<tr>
																	<td width="*">
																		&nbsp;
																	</td>
																	<td width="90" align="right">
																		<a class="Operation" href="javascript:describe();">
																			<img src="<%=root%>/images/ico/bianji.gif"
																				width="15" height="15" class="img_s">情况说明</a>
																	</td>
																	<td width="5"></td>
																	<td width="58" align="right">
																		<a class="Operation" href="javascript:goback();">
																			<img src="<%=root%>/images/ico/ht.gif"
																				width="15" height="15" class="img_s">返回</a>
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
							<s:form id="myTableForm"
								action="/attendance/register/register!viewRegisterRecord.action">
								<webflex:flexTable name="myTable" width="100%" height="370px"
									wholeCss="table1" property="scheduleId" isCanDrag="true"
									showSearch="false" isCanFixUpCol="true" clickColor="#A9B2CA"
									footShow="null" isShowMenu="true" 
									getValueType="getValueByArray" collection="${page.result}"
									page="${page}">
									<table width="100%" border="0" cellpadding="0" cellspacing="1"
										class="table1">
										<tr>
											<td width="5%" align="center" class="biao_bg1">
												<img src="<%=root%>/images/ico/sousuo.gif" width="17"
													id="img_sousuo" height="16" style="cursor: hand;"
													title="单击搜索">
											</td>
											<td width="13%" align="center" class="biao_bg1">
												<strong:newdate id="startDate" name="startDate"
													dateform="yyyy-MM-dd" isicon="true" width="100%"  maxdate="<%=dateStr%>"
													dateobj="${startDate}" classtyle="search" title="请选择起始日期" />
											</td>
											<td width="13%" align="center" class="biao_bg1">
												<strong:newdate id="endDate" name="endDate" maxdate="<%=dateStr%>"
													dateform="yyyy-MM-dd" isicon="true" width="100%"
													dateobj="${endDate}" classtyle="search" title="请选择截止日期" />
											</td>
											<td class="biao_bg1">
												&nbsp;
											</td>
										</tr>
									</table>
									<webflex:flexCheckBoxCol caption="选择" valuepos="0"
										valueshowpos="0" width="5%" isCheckAll="true"
										isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
									<webflex:flexDateCol caption="考勤日期" valuepos="0" dateFormat="yyyy-MM-dd"
										valueshowpos="0" width="13%" isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
									<%
										int maxNum = 0;
										String tempNum1;
										String tempNum2;
										String width="10%";
										String max = (String) request.getAttribute("maxNum");
										if (max != null) {
											maxNum = Integer.parseInt(max);
										}
										if(maxNum!=0){	//&&80/(maxNum*2)<18
											width=String.valueOf(72/(maxNum*2))+"%";
										}				
										for (int i = 1; i < maxNum+1; i++) {
											tempNum1=String.valueOf(i*2-1);	
											tempNum2=String.valueOf(i*2);			
									%>
										<webflex:flexDateCol caption="上班" valuepos="<%=tempNum1%>"
											valueshowpos="<%=tempNum1%>" width="<%=width%>" isCanDrag="true"
											isCanSort="true" dateFormat="yyyy-MM-dd HH:mm:ss" showsize="20"></webflex:flexDateCol>
										<webflex:flexDateCol caption="下班" valuepos="<%=tempNum2%>"
											valueshowpos="<%=tempNum2%>" width="<%=width%>" isCanDrag="true"
											isCanSort="true" dateFormat="yyyy-MM-dd HH:mm:ss" showsize="20"></webflex:flexDateCol>
									<%
									}
									%>
									<webflex:flexTextCol caption="说明" valuepos="<%=String.valueOf(maxNum*2+1)%>" valueshowpos="<%=String.valueOf(maxNum*2+1)%>" width="10%" onclick="viewInfo(this.value)" showsize="6" isCanDrag="true">
									</webflex:flexTextCol>
								</webflex:flexTable>
							</s:form>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
		<SCRIPT type="text/javascript">
			
			var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/ico/bianji.gif","情况说明","describe",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				item = new MenuItem("<%=root%>/images/ico/fankui.gif","返回","goback",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);
				sMenu.addShowType("ChangeWidthTable");
			    registerMenu(sMenu);
			}
		
			function goback(){
				window.location="<%=root%>/attendance/register/register.action";
			}
			
			function viewInfo(value){
				if(value!=null&&value!="null"&&value!="")
					alert(value);
			}
			
			 $(document).ready(function(){
			    $("#img_sousuo").click(function(){
			    	var startDate=document.getElementById("startDate").value;
			    	var endDate=document.getElementById("endDate").value;
			    	if(startDate==""||startDate==null){
			    		alert("请输入起始日期！");
			    		return;
			    	}
			    	if(endDate==""||endDate==null){
			    		alert("请输入截止日期！");
			    		return;
			    	}
			    	if(startDate>endDate){
			    		alert("开始时间不能晚于截止时间！");
			    		return;
			    	}
			    	$("Form").submit();
			    });     
			  });
			  
		</SCRIPT>
	</BODY>
</HTML>
