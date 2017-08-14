<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>流程监控</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<!--<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/combox/sexy-combo.css" />-->
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/combox/sexy.css" />
		<script language='javascript' src='<%=root%>/common/js/grid/ChangeWidthTable.js' type="text/javascript"></script>
		<script language="javascript" src="<%=root%>/common/js/menu/menu.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/common/search.js" type="text/javascript"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<link rel="stylesheet" href="<%=path%>/common/js/dropdownCheckList/css/ui.dropdownchecklist.css" type="text/css"></link>
		<script language="javascript" src="<%=path%>/common/js/dropdownCheckList/js/ui.core.js"></script>
		<script language="javascript" src="<%=path%>/common/js/dropdownCheckList/js/ui.dropdownchecklist.js"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<!--<script language="javascript" src="<%=path%>/common/js/combox/jquery.sexy-combo.js"></script>-->
		<script>
			function writeState(endtime){
				if(null==endtime||"null"==endtime){
					return "在办";
				}else{
					return "办毕";
				}
			}
			
			function writeTimeout(istimeout){
				if("0"==istimeout){
					return "否";
				}else{
					return "是";
				}
			}
			
			function doLeave(){
				location = "<%=root%>/workflowDesign/action/processMonitor!processUtil.action?instanceId=" + $("#tId").val() + "&transitionName=" + encodeURI(encodeURI($("#transName").val()));
			}
			
			function doProcess(){
				var display = $("#div_instance").css("display");
				if(display == "none"){
					$("#div_instance").show();
				} else {
					$("#div_instance").hide();
				}
			}
			
			function KeyPress(objTR){
        		//只允许录入数据字符 0-9 和小数点 
           		//var objTR = element.document.activeElement; 
           		var txtval=objTR.value; 
           		var key = event.keyCode;
           		if((key < 48||key > 57)){ //&&key != 46
            		event.keyCode = 0;
           		} else {
            		/*if(key == 46){
             			if(txtval.indexOf(".") != -1||txtval.length == 0)
              				event.keyCode = 0;
            		}*/
           		}
        	}
		</script>
	</head>
	<%
								String workflowType = "";
								String excludeWorkflowType = "";
								String personInfo = "";
								String timeInfo = "";
								if(request.getParameter("workflowType")!=null){
									workflowType = (String) request.getParameter("workflowType");
									excludeWorkflowType = (String) request.getParameter("excludeWorkflowType");
								}
								if(workflowType.equals("2")){
									personInfo =  "拟稿人";
									timeInfo = "拟稿时间";
								}else if(workflowType.equals("3")){
									personInfo = "登记人";
									timeInfo = "登记日期";
								}else{
									personInfo = "发起人";
									timeInfo = "启动日期";
								}
							%>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()"  style="overflow:auto;">
<!--	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>-->
<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<div id="contentborder" align="center"">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<s:form id="myTableForm" action="/workflowDesign/action/processMonitor!monitorList.action" method="post">
							<input type="hidden" id="isFind" name="isFind" value="${isFind}" />
							<input type="hidden" id="mytype" name="mytype" value="${mytype}" />
							<input type="hidden" id="processId" name="processId" value="${processId}" />
							<input type="hidden" name="proName" id="proName" value="${proName}" />
							<input type="hidden" name="processName" id="processName" value="${processName}" />
							<input type="hidden" name="startUserName" id="startUserName" value="${startUserName}" />
							<input type="hidden" name="workflowName" id="workflowName" value="${workflowName}" />
							<input type="hidden" name="searchDateTEXT" id="searchDateTEXT" value="${searchDateString}" />
							<input type="hidden" name="dayTEXT" id="dayTEXT" value="${day}" />
							<input type="hidden" name="workflowType" id="workflowType" value="<%=workflowType%>" />
							<input type="hidden" name="excludeWorkflowType" id="excludeWorkflowType" value="<%=excludeWorkflowType%>" />
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="instanceId" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
							 	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
									<tr>
										<td>${state }
										&nbsp;&nbsp;标题：&nbsp;<input name="processNameTEXT" id="processNameTEXT" type="text"  class="search" title="请您输入标题" value="${processName }">
							       		<%--&nbsp;&nbsp;当前处理人：&nbsp;<input name="workflowNameTEXT" id="workflowNameTEXT" type="text" class="search" title="请您输入当前处理人" value="${workflowName}">
							       		--%>
							       		<%
							       			if("2".equals(workflowType)){
							       				%>
							       				&nbsp;&nbsp;拟稿人：&nbsp;<input name="startUserNameTEXT" id="startUserNameTEXT"   type="text" class="search" title="请您输入 拟稿人" value="${startUserName}"/>
							       				&nbsp;&nbsp;拟稿日期：&nbsp;<strong:newdate  name="searchDate" id="searchDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入你搞日期" dateform="yyyy-MM-dd" dateobj="${searchDate}"/>
							      		 <%
							       			}else{
							       				%>
							       				&nbsp;&nbsp;登记人：&nbsp;<input name="startUserNameTEXT" id="startUserNameTEXT"   type="text" class="search" title="请您输入登记人" value="${startUserName}">
							       				&nbsp;&nbsp;登记日期：&nbsp;<strong:newdate  name="searchDate" id="searchDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入登记日期" dateform="yyyy-MM-dd" dateobj="${searchDate}"/>
							       		<%	
							       			}
							       				%>
										<%--
										<td width="15%" align="center" class="biao_bg1">
											 <!--
												 	<select id="basic-combo" name="basic-combo"  size="1">
												 	<option value="">任务持续时间</option>
											        <option value="1">超过1天未办理</option>
											        <option value="2">超过2天未办理</option>
											        <option value="3">超过3天未办理</option>
											        <option value="4">超过4天未办理</option>
											        <option value="5">超过5天未办理</option>
											        <option value="6">超过6天未办理</option>
											        <option value="7">超过7天未办理</option>
											      </select>
										     -->
										     <input name="day" id="day" type="text" class="search" title="输入任务超过的天数" value="${day }">
										</td>
										--%>
										<s:if test="state ==1">
							       		<%
							       				List<String> doneyearList = new LinkedList<String>();
							       				String currentYear = "";
							       				//doneyearList.add(currentYear);
							       				for(int i=2000;i<2020;i++){
							       					doneyearList.add(i+"");
							       				}
							       				doneyearList = new ArrayList(doneyearList);
							       				request.setAttribute("doneyearList",doneyearList);
							       				if(request.getAttribute("doneYear")!=null && !"".equals(request.getAttribute("doneYear"))){
							       					currentYear = (String)request.getAttribute("doneYear");
							       				}
							       				request.setAttribute("currentYear",currentYear);
							       		%>
							       		&nbsp;&nbsp;办结年份：&nbsp;
							       		<s:select  title="办结年份" id="doneYear" name="doneYear" list="#request.doneyearList"  value="#request.currentYear"  onchange='changeDoneYear(this.value);'>
										</s:select>
										</s:if>
										&nbsp;&nbsp;办理状态：&nbsp;<s:select name="state" list="#{'':'全部','0':'在办','1':'办毕'}"  listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
										&nbsp;&nbsp;是否超期：&nbsp;<s:select name="timeout" list="#{'':'全部','0':'未超期','1':'超期'}"  listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
										<%--<td width="11%" align="center" class="biao_bg1">
										<input type="hidden" name="processDefinitionNames" id="processDefinitionNames" value='${processDefinitionNames==null?"":processDefinitionNames}'>
										<select id="processDefinitionNamesTemp" name="processDefinitionNamesTemp" multiple="multiple" >
								            	<option value="" selected="selected">全部</option>
								            	<s:iterator value="%{processDefinitionNameMapIds}" >
									            <option value="<s:property value='value'/>"><s:property value='key'/></option>
								            	</s:iterator>
								        </select>
								       
										</td>
										--%><%--<td width="11%" align="center" class="biao_bg1">
										</td>
							       		<td class="biao_bg1">&nbsp;</td>
										--%>
										&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
										</td>
									</tr>
								</table>
								<s:if test="state == 0">
								<webflex:flexCheckBoxCol caption="选择" property="instanceId"
									showValue="businessName" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" property="businessName"   onclick="ViewFormAndWorkflow(this.value);" 
									showValue="businessName" width="52%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
									
									<webflex:flexTextCol caption="<%=personInfo %>" width="15%" property="startUserName"
									showValue="startUserName" align="center"
									isCanDrag="true" isCanSort="true" showsize="7"></webflex:flexTextCol>
									<webflex:flexDateCol caption="<%=timeInfo %>" property="workflowStartDate"
									showValue="workflowStartDate" width="15%" isCanDrag="true"
									dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="当前处理人" width="15%" property="actorName"
									showValue="actorName" align="center"
									isCanDrag="true" isCanSort="true" showsize="7"></webflex:flexTextCol>
								</s:if>
								<s:else>
								<webflex:flexCheckBoxCol caption="选择" property="instanceId"
									showValue="businessName" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" property="businessName"  onclick="ViewFormAndWorkflow(this.value);" 
									showValue="businessName" width="67%" isCanDrag="true" isCanSort="true" showsize="42"></webflex:flexTextCol>
									
									<webflex:flexTextCol caption="<%=personInfo %>" width="15%" property="startUserName"
									showValue="startUserName" align="center"
									isCanDrag="true" isCanSort="true" showsize="7"></webflex:flexTextCol>
									<webflex:flexDateCol caption="办结时间" property="workflowEndDate"
									showValue="workflowEndDate" width="15%" isCanDrag="true"
									dateFormat="yyyy-MM-dd HH:mm" isCanSort="true"></webflex:flexDateCol>
								</s:else>
							</webflex:flexTable>
							<s:actionmessage />
						</s:form>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript" type="text/javascript">
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;
	
	//item = new MenuItem("<%=root%>/images/ico/chakanlishi.gif","运行情况","viewProcess",1,"ChangeWidthTable","checkOneDis");
	
	//sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewMonitorData",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	
	//sMenu.addLine();
	//item = new MenuItem("<%=root%>/images/ico/tb-change.gif","冻结列","frezeColum",1,"ChangeWidthTable","checkOneDis");
	//sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
    
    //显示流程监控父窗体中的控制按钮
    if(window.parent.document.getElementById("isShowparentBtn") != undefined){
		if(window.parent.document.getElementById("isShowparentBtn").value = "true"){
			if(window.parent.document.getElementById("parentBtn") != undefined){
				window.parent.document.getElementById("parentBtn").style.display = "";
			}
		}
	}
}
  			function ViewFormAndWorkflow(){
				var id =$(window.event.srcElement).siblings().eq(1).find(":checkbox").val();
					var url=scriptroot+"/workflowDesign/action/processMonitor!viewformSelect.action?instanceId="+id;
                	var width= window.screen.availWidth-10;
		   			var height= window.screen.availHeight-30;
		   			var win = window.open(url,'viewformSelect','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 
		  							'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
			}

		function viewProcess(){
			var id = getValue();
			if(id!=""){
				if(id.split(",").length>1){
					alert("对不起,一次只能查看一个流程。");
					return;
				}
				if(window.parent.document.getElementById("isShowparentBtn") != undefined){
					window.parent.document.getElementById("isShowparentBtn").value = "false";
				}
				if(window.parent.document.getElementById("parentBtn") != undefined){
					window.parent.document.getElementById("parentBtn").style.display = "none";
				}
				location = scriptroot + "/workflowDesign/action/processMonitor!processView.action?instanceId=" + id+"&modelType="+'${modelType}'
								+"&proName="+encodeURI(encodeURI("${ProName}"))+"&processId=${processId}" + "&workflowName="+encodeURI(encodeURI("${workflowName}"));
			}else{
				alert("请选择要查看的流程。");
			}
		} 

		function viewMonitorData(){
			var id = getValue();
			if(id!=""){
				if(id.split(",").length>1){
					alert("对不起,一次只能查看一个流程。");
					return;
				}
					var url=scriptroot+"/workflowDesign/action/processMonitor!viewformSelect.action?instanceId="+id;
                	var width= window.screen.availWidth-10;
		   			var height= window.screen.availHeight-30;
		   			var win = window.open(url,'viewformSelect','height='+height+', width='+width+', top=0, left=0, toolbar=no, ' + 
		  							'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
			}else{
				alert("请选择要查看的流程。");
			}
		}
		
		function delProcess(){
			var id = getValue();
			var processId = '${processId}';
			if(id == ""){
				alert("请选择记录。");
				return;
			}
			if(confirm("确定要删除吗？")){
				location = scriptroot + "/workflowDesign/action/processMonitor!delete.action?ids=" + id + "&processId="+processId;
			}
		}
		
		
		
		$(document).ready(function(){
			/*$("#basic-combo").sexyCombo({
				emptyText: "Choose a state..."
			});*/
			$("#img_sousuo").click(function(){
				/*var dayId = $("input[name=basic-combo__sexyComboHidden]").val();
				var dayName = $("input[name=basic-combo__sexyCombo]").val();*/
				$("#isFind").val("1");
				fromSubmit();
			});
			 //$("#processDefinitionNamesTemp").dropdownchecklist({ firstItemChecksAll: true, maxDropHeight: 100 });
			// $("#processDefinitionNamesTemp").dropdownchecklist({firstItemChecksAll: true, maxDropHeight: 150 ,width:120});
		}); 
		
		function fromSubmit(){
			var day = $.trim($("#day").val());
				if(day != "" && isNaN(day)){
					alert("请输入正确的天数。");
					return ;
				}
				
				/*
				var processDefinitionNamesTemp=$("#processDefinitionNamesTemp").val();
				processDefinitionNamesTemp=String(processDefinitionNamesTemp);
				if(processDefinitionNamesTemp!=""){
					if(processDefinitionNamesTemp.split(",")[0]==""){
						processDefinitionNamesTemp="";
					}
				}
				var processDefinitionNames=encodeURI(processDefinitionNamesTemp);
				$("#processDefinitionNames").val(processDefinitionNames);
				*/
				
				
				$("#processName").val(encodeURI($("#processNameTEXT").val()));
				$("#startUserName").val(encodeURI($("#startUserNameTEXT").val()));
				//$("#workflowName").val(encodeURI($("#workflowNameTEXT").val()));
				$("#searchDate").val($("#searchDate").val());
				//$("#proName").val(encodeURI($("#proName").val()));
				$("form").submit();
				$("body").mask("正在查询数据,请稍候。");
		}
		
		//分页按钮触发的事件
		function doFromSubmit(objForm){
			$("#isFind").val("0");
			fromSubmit();
		}
		function changeDoneYear(obj){
			$("#img_sousuo").click();
		}
</script>
	</body>
</html>
