<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>流程查询</title>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		 <script language='javascript'
      src='<%=path%>/common/js/grid/ChangeWidthTable.js'
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
      type="text/javascript"></script>
    <script language="javascript" src="<%=path%>/common/js/menu/menu.js"
      type="text/javascript"></script>    
    <script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
    <script src="<%=path%>/common/js/common/search.js"
      type="text/javascript"></script>


		<style media="screen" type="text/css">
    .tabletitle {
      FILTER:progid:DXImageTransform.Microsoft.Gradient(
                            gradientType = 0, 
                            startColorStr = #ededed, 
                            endColorStr = #ffffff);
    }
    
    .hand {
      cursor:pointer;
    }
    


    
    
    </style>

		<script type="text/javascript">
		function writeState(endtime){
				
				var tp = "${taskType}";
				if(tp=="0"){
					return "待办";
				}else if(tp=="1"){
					return "在办";
				}else if(tp=="2"){
					return "非办结";
				}else if(tp=="3"){
					return "办结";
				}else{
					if(null==endtime||"null"==endtime){
						return "在办";
					}else{
						return "办毕";
					}
				}
			}
		function search(){
			if($("#orguserid").val()==""){
				$("#startUserName").val("");
			}
			var processStartDate=$("#processStartDate").val();
			var processEndDate=$("#processEndDate").val();
			var taskStartDateStart=$("#taskStartDateStart").val();
			var taskStartDateEnd=$("#taskStartDateEnd").val();
			var startUserName=$("#orgusername").val();

			if(taskStartDateStart!=null&&taskStartDateStart!=""&&taskStartDateEnd!=null&&taskStartDateEnd!=""){
				if(taskStartDateStart>taskStartDateEnd){
					alert("任务接收时间上限不能大于任务接收时间下限。");
					
					return;
				}
			}
			if(processStartDate!=null&&processStartDate!=""&&processEndDate!=null&&processEndDate!=""){
				if(processStartDate>processEndDate){
					alert("流程创建时间上限不能大于流程创建时间下限。");
					
					return;
				}
			}
			if(startUserName!=null&&startUserName!=""){
				$("#startUserName").val(startUserName);
			}
			$("#isFind").val("1");
			document.getElementById("myTableForm").submit();
		}
		//隐藏查询
		function disableSearch(){
			var showValue=document.getElementById("foundationSearch").style.display;
			if(showValue!=null&&showValue!=""&&showValue=="block"){
				document.getElementById("foundationSearch").style.display="none";
				$("#isShow").html("显示查询");
				$("#menu").html($("#menu").html().replace("隐藏", "显示"));
			}
			if(showValue!=null&&showValue!=""&&showValue=="none"){
				document.getElementById("foundationSearch").style.display="block";
				$("#isShow").html("隐藏查询");
				$("#menu").html($("#menu").html().replace("显示", "隐藏"));
			}
			
		}
		//是否显示高级查询
		function seniorSearch(){
			//var showValue=document.getElementById("seniorSearch").style.display;
			var showValueTr1=document.getElementById("seniorSearchTr1").style.display;
			//var showValueTr2=document.getElementById("seniorSearchTr2").style.display;
			if(showValueTr1!=null&&showValueTr1!=""&&showValueTr1=="block"){
				//document.getElementById("seniorSearch").style.display="none";
				//document.getElementById("seniorSearchTr1").style.display="none";
			}
			if(showValueTr1!=null&&showValueTr1!=""&&showValueTr1=="none"){
				//document.getElementById("seniorSearch").style.display="block";
				document.getElementById("seniorSearchTr1").style.display="block";
			}
			/*
			if(showValueTr2!=null&&showValueTr2!=""&&showValueTr2=="block"){
				//document.getElementById("seniorSearch").style.display="none";
				document.getElementById("seniorSearchTr2").style.display="none";
			}
			if(showValueTr2!=null&&showValueTr2!=""&&showValueTr2=="none"){
				//document.getElementById("seniorSearch").style.display="block";
				document.getElementById("seniorSearchTr2").style.display="block";
			}
			*/
		}
		
	$(document).ready(function(){
		
		$("#orgusername").val("${startUserName}");
		
		//选择接收人
		$("#addPerson").click(function(){
			var ret=OpenWindow(this.url,"600","400",window);
		});
		
		//清空接收人
		$("#clearPerson").click(function(){
			$("#orgusername").val("");
			$("#orguserid").val("");
			$("#startUserName").val("");
		});
	
		
		var taskTypeValue = "${taskType}";
		
		$(".taskTypeClass").each(function(i){
			if(this.value==taskTypeValue&&taskTypeValue!=""){
				this.selected="selected";
				$("#taskType").val(taskTypeValue);
			}
		});
		
		var processStatusValue = "${processStatus}";
		$(".processStatusClass").each(function(i){
			if(this.value==processStatusValue&&processStatusValue!=""){
				this.selected="selected";
				$("#processStatus").val(processStatusValue);
			}
		});
		
		if("${processStatus}"!=""||"${processStartDate}"!=""||"${processEndDate}"!=""||"${taskNodeName}"!=""||"${processName}"!=""){
					seniorSearch();
				}
	});	

/**
			* 查看详情
			* @param instanceId 流程实例ID
			* @param businessId 业务数据{表名;主键字段名;主键值}
			*/
			function viewInfo(TaskIdAndInstanceId){
				var id = getValue();
				//alert(id);
				if(TaskIdAndInstanceId==''||TaskIdAndInstanceId==null||TaskIdAndInstanceId=="display"){
					TaskIdAndInstanceId=id;
				}
				
			/*
					TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
					var taskId = TaskIdAndInstanceId[0];
					var instanceId = TaskIdAndInstanceId[1];
					var fullContextPath = $("form").attr("action");
			  		var contextPath = fullContextPath.substring(0,fullContextPath.indexOf("!"));//得到上下文路径
			  		document.getElementById('blank').contentWindow.setWorkId(taskId,instanceId,contextPath); 
			  		*/
		  //var TaskIdAndInstanceId = getValue();
          if(TaskIdAndInstanceId == ""){
          	alert("请选择要查看的记录。");
          	return ;
          }else{
          	var taskIds = TaskIdAndInstanceId.split(",");
          	if(taskIds.length>1){
          		alert("只可以查看一条记录。");
          		return ;
          	}
          }  
      	TaskIdAndInstanceId = TaskIdAndInstanceId.split("$");
		var taskId = TaskIdAndInstanceId[0];
		var instanceId = TaskIdAndInstanceId[1];
		var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
      	 var ret=WindowOpen("<%=root%>/senddoc/sendDoc!viewProcessed.action?instanceId="+instanceId+"&taskId="+taskId+"&state=1",'processed',width, height, "");
			  		
			}
			//查看流程图
		    function workflowView(instanceId){      
		        var width=screen.availWidth-10;;
		        var height=screen.availHeight-30;
		        var ReturnStr=OpenWindow("<%=root%>/work/work!PDImageView.action?instanceId="+instanceId, 
		                                   width, height, window);
		    }
	
	</script>
	</head>
	<base target="_self">
	<BODY class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT();">
<!--		<script type="text/javascript"-->
<!--			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>-->
		<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<DIV id=contentborder>
		<div style="height: 40px;" class="tabletitle" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
		      <tr>
		         <td colspan="3" class="table_headtd">
			 		<table width="100%" border="0" align="right" cellpadding="0" cellspacing="0">
				<tr>
					<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>公文查询</strong>
							</td>
					<td align="right">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr>
							    <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
						                 	<td class="Operation_list" onclick="viewInfo();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
						                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                  		<td width="5"></td>
								<td style="display: none;">
									<a class="Operation" href="JavaScript:search();"><img
											class="img_s" src="<%=root%>/images/ico/search.gif"
											width="15" height="15" alt="">搜索&nbsp;</a>
								</td>
								<td width="5">
									&nbsp;
								</td>
								<%--
								<td>
									<a class="Operation" href="JavaScript:disableSearch();"><img
											class="img_s" src="<%=root%>/images/ico/chakan.gif"
											width="15" height="15" alt=""><span id="isShow">显示查询</span>&nbsp;</a>
								</td>
								<td width="5">
									&nbsp;
								</td>
								<td>
									<a class="Operation" href="JavaScript:seniorSearch();"><img
											class="img_s" src="<%=root%>/images/ico/search.jpg"
											width="15" height="15" alt="">高级查询&nbsp;</a>
								</td>
								--%>
						  <td width="2%"></td>
														</tr>

													</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							<tr>
							<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td>
						<s:form name="myTableForm"
							action="/senddoc/sendDoc!searchWorkflow.action" method="POST">
							<input type="hidden" id="isFind" name="isFind" value="">
							<input type="hidden" id="workflowType" name="workflowType" value="${workflowType}">
							<div id="foundationSearch" >
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
									<tr>
										<td>
											&nbsp;&nbsp;公文标题：&nbsp;<input name="businessName" id="businessName" type="text" class="search" title="请您输入文件标题" value="${businessName}">
							       			&nbsp;&nbsp;<s:if test="workflowType==2">拟稿人:</s:if> 
											<s:elseif test="workflowType==3">登记人:</s:elseif>
											<s:else>创建人:</s:else>
											<s:textfield title="单击选择人"  id="orgusername"
												name="msgReceiverNames" onclick="addPerson.click();"
												 readonly="true"></s:textfield>
											<input type="hidden" id="startUserName" name="startUserName"
												value="${startUserName}"></input>
											<input type="hidden" id="orguserid" name="startUserId"
												value="${startUserId}"></input>
											<a id="addPerson" url="<%=root%>/address/addressOrg!tree.action" href="#"><%--<input type="button" id="addUser" class="button" style="height: 24px;" value="添加">
											</a><a id="clearPerson" href="#"><input type="button" id="clearUser" class="button" style="height: 24px;" value="清空">--%> </a>
											&nbsp;&nbsp;处理日期：&nbsp;<strong:newdate  name="taskEndDateStart" id="taskEndDateStart" skin="whyGreen" isicon="true"  classtyle="search" title="请输入办理日期" dateform="yyyy-MM-dd" dateobj="${taskEndDateStart}"/>
							       			&nbsp;&nbsp;到：&nbsp;<strong:newdate  name="taskEndDateEnd" id="taskEndDateEnd" skin="whyGreen" isicon="true"  classtyle="search" title="请输入办理日期" dateform="yyyy-MM-dd" dateobj="${taskEndDateEnd}"/>
											&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="search();"/>
										</td>
									</tr>
									<%--
									<tr >
			
										<td width="15%" height="21" class="biao_bg1" align="right" width="42%">
											<span class="wz">接收时间：</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<strong:newdate name="taskStartDateStart"
												id="taskStartDateStart" skin="whyGreen" isicon="true"
												dateobj="${taskStartDateStart}" dateform="yyyy-MM-dd"
												width="130"></strong:newdate>
											-
											<strong:newdate name="taskStartDateEnd" id="taskStartDateEnd"
												skin="whyGreen" isicon="true" dateobj="${taskStartDateEnd}"
												dateform="yyyy-MM-dd" width="130"></strong:newdate>
												
												
												
										</td>
			
									</tr>
									--%>
									<tr id="seniorSearchTr1" style="display: block">
										<%--<td width="15%" height="21" class="biao_bg1" align="right">
											<span class="wz">文件状态：</span>
										</td>
										<td class="td1" align="left" width="35%">
											<select name="processStatus" onchange="search();"
												style="width:123px">
												<option class="processStatusClass" value="">
													所有
												</option>
												<option class="processStatusClass" value="0">
													未办结
												</option>
												<option class="processStatusClass" value="1">
													办结
												</option>

											</select>
										</td>

										<td width="20%" height="21" class="biao_bg1" align="right"
											width="42%">
											<span class="wz">
											<s:if test="workflowType==2">拟稿日期：</s:if> 
											<s:elseif test="workflowType==3">登记日期：</s:elseif>
											<s:else>创建日期： </s:else> 
											</span>
										</td>
										<td class="td1" align="left" width="80%" colspan="3">
											<strong:newdate name="processStartDate" id="processStartDate"
												skin="whyGreen" isicon="true" dateobj="${processStartDate}"
												dateform="yyyy-MM-dd" width="100"></strong:newdate>
											-
											<strong:newdate name="processEndDate" id="processEndDate"
												skin="whyGreen" isicon="true" dateobj="${processEndDate}"
												dateform="yyyy-MM-dd" width="100"></strong:newdate>
										</td>
										--%>
										
									</tr>
									<%--
										<tr id="seniorSearchTr2" style="display: none">
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">任务节点名：</span>
											</td>
											<td class="td1" align="left" width="35%">
												<input type="text" name="taskNodeName"  value="${taskNodeName}" maxlength="50">
											</td>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">流程名：</span>
											</td>
											<td class="td1" align="left" width="35%" >
												<input type="text" name="processName" value="${processName}" maxlength="50">
												<input type="hidden" id="workflowType" name="workflowType" value="${workflowType}" ></input>
											</td>
										</tr>
								--%>
								</table>
								<%--
								<div id="seniorSearch" style="display: none">
								
									<table width="100%"  border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
										<tr>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">流程状态：</span>
											</td>
											<td class="td1" align="left" width="35%">
												<select name="processStatus" onchange="search();" style="width:123px">
													<option class="processStatusClass" value="">
														所有
													</option>
													<option class="processStatusClass" value="0">
														待办
													</option>
													<option class="processStatusClass" value="1">
														办毕
													</option>
													
												</select>
											</td>
				
											<td width="15%" height="21" class="biao_bg1" align="right" width="42%">
												<span class="wz">创建时间：</span>
											</td>
											<td class="td1" align="left" width="35%">
												<strong:newdate name="processStartDate"
													id="processStartDate" skin="whyGreen" isicon="true"
													dateobj="${processStartDate}" dateform="yyyy-MM-dd"
													width="120"></strong:newdate>
												--
												<strong:newdate name="processEndDate" id="processEndDate"
													skin="whyGreen" isicon="true" dateobj="${processEndDate}"
													dateform="yyyy-MM-dd" width="120"></strong:newdate>									
											</td>
										</tr>
										<tr>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">任务节点名：</span>
											</td>
											<td class="td1" align="left" width="35%">
												<input type="text" name="taskNodeName"  value="${taskNodeName}" size="50" maxlength="50">
											</td>
											<td width="15%" height="21" class="biao_bg1" align="right">
												<span class="wz">流程名：</span>
											</td>
											<td class="td1" align="left" width="35%" >
												<input type="text" name="processName" value="${processName}" size="50" maxlength="50">
												<input type="hidden" id="workflowType" name="workflowType" value="${workflowType}" ></input>
												
												<table width="100%"  border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
													<tr>
														<td width="35%">
															<select name="workflowType"
																onchange='search();' 
																class="search" title="请选择流程类型" style="width:100px">
																	<option value="0">
																		全部
																	</option>
																	
											       	
																<c:forEach items="${workflowTypeList}" var="type" varStatus="status">
																	<script>										
																	if("${workflowType}"=="<c:out value="${type[0]}"/>"){
																		document.write("<option selected value='"+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
																	}else{
																	
																		document.write("<option value='"+"<c:out value="${type[0]}"/>"+"'>"+"<c:out value="${type[1]}"/>"+"</option>");
																	}
																	</script>
																</c:forEach>
															</select>
														
														</td>
														<td  class="biao_bg1" align="left" width="18%">
														
															<span class="wz">流程名：</span>
														</td>
														<td width="45%">
														
														</td>
													</tr>
												</table>
												
											</td>
										</tr>
									</table>
								</div>
							--%>
							</div>
							<%
								String workflowType = "";
								String personInfo = "";
								String timeInfo = "";
								if(request.getParameter("workflowType")!=null){
									workflowType = (String) request.getParameter("workflowType");
								}
								if(workflowType.equals("2")){
									personInfo =  "拟稿人";
									timeInfo = "处理时间";
								}else if(workflowType.equals("3")){
									personInfo = "登记人";
									timeInfo = "登记日期";
								}else{
									personInfo = "创建人";
									timeInfo = "创建日期";
								}
							%>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="10" isCanDrag="false"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" showSearch="false"
								collection="${workflowPage.result}" pagename="workflowPage"
								page="${workflowPage}">
								<webflex:flexCheckBoxCol caption="选择" valuepos="10" 
									valueshowpos="4" width="2%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="" valuepos="" valueshowpos="17"
									isCanDrag="false" width="" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="公文标题" valuepos="10"
									valueshowpos="4" onclick="JavaScript:viewInfo(this.value);"
									isCanDrag="false" width="45%" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="<%=personInfo %>" valuepos="2" valueshowpos="2" align="center"
									isCanDrag="false" width="15%" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol caption="<%=timeInfo %>" valuepos="8"
									valueshowpos="8" width="15%" isCanDrag="false"
									dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>
								<%--
								<webflex:flexTextCol caption="流程名" valuepos="12"
									valueshowpos="3" isCanDrag="true" width="20%" isCanSort="true"></webflex:flexTextCol>
								--%>
								<webflex:flexTextCol caption="当前处理人" valuepos="6" showsize="8" align="center"
									valueshowpos="6" isCanDrag="false" width="20%" isCanSort="true"></webflex:flexTextCol>
								<%--
								<webflex:flexDateCol caption="接收日期" valuepos="7" valueshowpos="7" width="10%" isCanDrag="true"
									dateFormat="yyyy-MM-dd" isCanSort="true"></webflex:flexDateCol>
								--%>
							</webflex:flexTable>
						</td>
					</tr>

				</table>
			</s:form>
		</DIV>
			<%--<div align="center" height="50%">
				<iframe id="blank" name="frame_query" width="100%"
					src="<%=path%>/fileNameRedirectAction.action?toPage=/workflow/viewinfo.jsp"
					height="100%" frameborder="no" border="0" marginwidth="0"
					marginheight="0" scrolling="no"></iframe>
			</div>
			--%>
			<script language="javascript">
				 correctPNG(); 	//IE6中正常显示透明PNG 
				 var sMenu = new Menu();
			      function initMenuT(){
			        sMenu.registerToDoc(sMenu);
			        var item = null;
			        item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewInfo",1,"ChangeWidthTable","checkMoreDis");
				 	sMenu.addItem(item);
			        sMenu.addShowType("ChangeWidthTable");
			        registerMenu(sMenu);
			        $("#isFind").val("0");
			      }
				
				
			</script>
		</DIV>
	</body>

</html>
