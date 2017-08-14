<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>操作内容</TITLE>
		<%@include file="/common/include/meta.jsp"%>
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
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script src="<%=path%>/common/js/common/search.js"
			type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
		        $("input:checkbox").parent().next().next().next().hide();//隐藏表单ID列         
		     });
		</script>
		<style>
			TH{
				padding:0px 14px;			
			}
		</style>
	</HEAD>
	<BODY class=contentbodymargin  oncontextmenu="return false;">
	<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%" align="center">
						<s:form id="myTableForm" theme="simple"
							action="/attendance/report/attendReport.action">
							<input type="hidden" name="pkey" value="${pkey}" />
							<input type="hidden" name="orgId" value="${orgId}" />
							<input type="hidden" name="personId" value="${personId}">
							<input type="hidden" name="infoItems" value="${infoItems}">
							<input type="hidden" name="infoSetCode" value="${infoSetCode}" />	
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="40"
											 style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
											<table width="100%" border="0" cellspacing="0"
												cellpadding="00">
												<tr>
													<td>
												&nbsp;
												</td>
													<td width="30%">
														<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
															height="9">&nbsp;
														考勤月报表1111
													</td>
													<td width="70%">
														<table border="0" align="right" cellpadding="00"
															cellspacing="0">
															<tr>
																<td width="*">
																	&nbsp;
																</td>
																	
																	<td width="5"></td>																				
															</tr>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
								<table width="90%" border="0"  cellpadding="0" cellspacing="0"
										>
									<tr>
									<td width="30%" ></td>
										<td width="400" align="center" >
											<strong><font size="4"><% Date date=new Date();
											SimpleDateFormat format=new SimpleDateFormat("yyyy年");
											  out.print( format.format(date)); %>  考勤汇总表 
											  <hr></font> </strong> 
										</td>
										<td width="30%" ></td>
									</tr>
										<tr>
										<td width="30%" align="left" >
									单位：小时
									</td>
									<td width="100%" align="right"  colspan="2">
									填报日期:
									<% 
											SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
											  out.print( df.format(date));%>
									</td>
										
									</tr>
							</table>	
							<s:if test="infopage.result.size>0">
							<table width="90%" id="" cellpadding=0 cellspacing=0>
								
								<!-- 定位表格,距离顶,左,右2个像素,-->
								<tr>
									<td valign=top align="left">
										<!-- 定位表格包含数据表格开始-->
										<!-- 数据显示表格定义开始-->
										<!--表格定义 直接拷贝 idColNums定义为表格相应的数据区的ID列　nameColNum定义为表格相应的数据区的名称列(列值为下面Td序号从0开始)-->
										<table id="myTable"  align="left"
										 cellPadding=0 cellspacing="0"
											width="100%" height="100%" >
											<!--thead 表格Title定义开始，表格的宽度定义不要在这里定义放到数据区定义，所有TD的class="headerTD"-->
											<!--第一个Td为空-->
											<!--第二个Td为全选复选框，该复选框的ID　一定要为　id="checkall"-->
											<!--第三个Td及以后是显示数据区的Title定义所有的TD class="headerTD"-->
											
												<tr >
												<td align="center" rowspan="2" height="60"
													style="border: 1px solid #363636;">
													序号
												</td>
												<s:iterator value="columnList" var="rowtitle"
													status="status">
													<s:if test="infoItemDatatype!='15'">
														<s:if
															test="infoItemField!='STATISTICS_TIME'&&infoItemField!='SHOULD_ATTEND_DAYS'&&infoItemField!='USER_ID'">
														<s:if
															test="infoItemField=='AFFAIR_TIME'||infoItemField=='ILL_TIME'||infoItemField=='INJURE_TIME'||infoItemField=='FUNERAL_LEAVE'">
														</s:if>
														<s:elseif
															test="infoItemField=='ANNUAL_LEAVE'||infoItemField=='WEDDING_LEAVE'||infoItemField=='MATEMITY_LEAVE'"></s:elseif>
														<s:else>
															<td nowrap rowspan="2" height="" align="center"
																style="border: 1px solid #363636; border-left: 0"
																showsize="15">
																<s:property value="infoItemSeconddisplay" />
															</td>
														</s:else>
														</s:if>
														
													</s:if>
												</s:iterator>
												<td  align="center" colspan="8" height="30"  style="border:1px solid #363636; border-left: 0px; border-bottom: 0px;" >
													    假期
													</td>
												</tr>
												
												<tr>
														<s:iterator value="columnList" var="rowtitle"
														status="status">
														<s:if test="infoItemDatatype!='15'">
														<s:if test="infoItemField=='STATISTICS_TIME'&&infoItemField=='SHOULD_ATTEND_DAYS'">
														</s:if>
															<s:elseif test="infoItemField=='AFFAIR_TIME'||infoItemField=='ILL_TIME'||infoItemField=='INJURE_TIME'||infoItemField=='FUNERAL_LEAVE'||infoItemField=='ANNUAL_LEAVE'||infoItemField=='WEDDING_LEAVE'||infoItemField=='MATEMITY_LEAVE'">
															<td  nowrap   height="" align="center" style="border:1px solid #363636; border-left: 0"  showsize="15">
																<s:property value="infoItemSeconddisplay"/>
															</td>
															</s:elseif>
														</s:if>
													</s:iterator>
													
												</tr>
										
											<!--thead 表格Title定义结束-->
											<!--tbody 表格数据区定义开始-->
											<!--第一个TD是提供记录ID值，不会在页面上显示-->
											<!--第二个ＴＤ的复选框，该Td的id一定要为　id="chkButtonTd"；　该复选框的值为记录ID值，复选框的ID和Name一定要为　id="chkButton" name="chkButton"-->
											<!--第三个TD及后面是显示数据区的定义，每项数据前加&nbsp;　如果需要在这里定义表格宽度-->
											<tbody >
												<s:iterator value="infopage.result" id="infoRowList"
													status="status">
													<tr>
													<td nowrap align="center"  style="border: 1px solid #363636;  border-top:0;">
															${status.index+1 }
															</td>
														<s:iterator value="infoRowList" id="property"
															status="status">
															
															<s:if test="infoItemDatatype==\"15\"">
																<td style="display:none" align="center" style="border: 1px solid #363636; border-left: 0; border-top:0;">
																	<s:property value="infoItemValue" />
																</td>
															</s:if>											
															<s:else>
															<s:if
															test="infoItemField!='STATISTICS_TIME'&&infoItemField!='SHOULD_ATTEND_DAYS'&&infoItemField!='USER_ID'&&infoItemField!='ORG_ID'">
															
																<td nowrap align="center"  style="border: 1px solid #363636; border-left: 0; border-top:0;">
																	<s:if test="infoItemDatatype==\"1\"">
																		${refernceDesc} 
																	</s:if>
																	<s:elseif test="infoItemDatatype==\"11\"">
																		<s:if test="infoItemValue!=null">
																			<img width='30' height='30'
																				src='<%=path%>/<s:property value="infoItemValue"/>' />
																		</s:if>
																	</s:elseif>
																	<s:else>
																		${infoItemValue} 
																	</s:else>
																</td>
																</s:if>
															</s:else>
														</s:iterator>
													</tr>
												</s:iterator>
											</tbody>
							
										</table>
								</td></tr>
								<tr>
									<td>
										<!-- 数据显示表格定义结束-->
										<!-- 分页显示表格定义开始-->
										<table width="100%" border="0" cellspacing="0" align="center" cellpadding="0">
											<tr >
												<td  align="right">
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
											
													${infopage.pageNo}/
													${infopage.totalPages }
												
													页
											&nbsp;&nbsp;&nbsp;&nbsp;
													数据总量
												
													${infopage.totalCount}
												
												<s:if test="infopage.isHasPre()==true">
														<input name="Submit2" type="submit" class="input_bg"
															value="首页" onclick="gotoPage(1)">
												
														<input name="Submit22" type="submit" class="input_bg"
															value="上一页" onclick="gotoPage(${infopage.pageNo-1})">
											
												</s:if>
												<s:if test="infopage.isHasNext()==true">
											
														<input name="Submit23" type="submit" class="input_bg"
															value="下一页" onclick="gotoPage(${infopage.pageNo+1})">
												
														<input name="Submit24" type="submit" class="input_bg"
															value="尾页"
															onclick="gotoPage(<s:property value="infopage.totalPages"/>)">
												
												</s:if>
												
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													转到
											
													<input id="pageNo" name="infopage.pageNo" type="text" size="4"
														value="${infopage.pageNo}">
													页
													<input name="Submit242" type="submit" class="input_bg"
														value="跳转" onclick="gotoPage()">
												</td>
											</tr>
										</table>
										<!-- 分页显示表格定义结束-->
									</td>
								</tr>
							</table>
							</s:if>
							<s:else>
							暂无考勤信息
							</s:else>
						</s:form>
					</td>
				</tr>
			</table>
			<IFRAME style="display: none;" id="delhelpframe"></IFRAME>
		</DIV>
	<script language="javascript">
	
		
		function viewInfo(){
		  var id=getValue();
		  if(id.length>32){
		     alert("不可以同时查看多条记录！");
		     return;
		  }
		  if(id==""){
		  alert("请选择一条记录！");
		  return;
		  }
		   top.perspective_content.actions_container.personal_properties_toolbar.navigate("<%=root%>/attendance/gather/gather!showRecord.action?pkey="+id,"考勤明细查询");
		
		}
		
		function selectCol(){
			var url= '<%=root%>/infotable/infoTable!rowlist.action?forward=searchrow&struct=40280cc22526195301252649bd340001';
  			showModalDialog(url,window,'dialogWidth:300pt ;dialogHeight:350pt;status:no;help:no;scroll:no;status:0;help:0;scroll:0;');
		}
		
		function setSelectRow(str){
			document.all.infoItems.value=str;
		}
		
		function gotoPage(no){
			if(no!=null&&no!=""){
				document.getElementById('pageNo').value=no;
			}
			document.getElementById('myTableForm').submit();
		}
		
		function submitForm(){
	    	document.forms[0].submit();
	    }
	    
	    $(document).ready(function(){
		    $("#img_sousuo").click(function(){
		    	var statisticsStime=document.getElementById("statisticsStime").value;
		    	var statisticsEtime=document.getElementById("statisticsEtime").value;
		    	if(statisticsStime>statisticsEtime){
		    		alert("汇总开始时间必须晚于汇总结束时间！");
		    		return;
		    	}
		    	$("Form").submit();
		    });     
		  });
</script>
</BODY>
</HTML>
