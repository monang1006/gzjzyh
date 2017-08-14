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
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel="stylesheet">
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
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()"  style="overflow:auto;">
	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center"">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<s:form id="myTableForm" action="/workflowDesign/action/processMonitor!monitorList.action" method="get">
							<input type="hidden" id="isFind" name="isFind" value="${isFind}" />
							<input type="hidden" id="processId" name="processId" value="${processId}" />
							<input type="hidden" name="proName" id="proName" value="${proName}" />
							<input type="hidden" name="processName" id="processName" value="${processName}" />
							<input type="hidden" name="startUserName" id="startUserName" value="${startUserName}" />
							<input type="hidden" name="workflowName" id="workflowName" value="${workflowName}" />
							<input type="hidden" name="searchDateTEXT" id="searchDateTEXT" value="${searchDateString}" />
							<input type="hidden" name="dayTEXT" id="dayTEXT" value="${day}" />
							<input type="hidden" name="mytype" id="mytype" value="${mytype}" />
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="2" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
									
							<div id="search_tr" style="display: none">
								<table width="100%" height="10%" border="0" cellpadding="0"
									cellspacing="1" align="center" class="table1">
									<br>
									<tr>
										<td width="10%" height="21" class="biao_bg1" align="right">
											<span class="wz">文件标题：</span>
										</td>
										<td class="td1" align="left" width="20%">
											<input name="processNameTEXT" id="processNameTEXT" type="text" style="width=100%" class="search" title="输入标题" value="${processName }">
										</td>
										<td width="15%" height="21" class="biao_bg1" align="right">
											<span class="wz"> 
											当前处理人：
											</span>

										</td>
										<td class="td1" align="left" width="15%">
											<input name="workflowNameTEXT" id="workflowNameTEXT" type="text" style="width=100%" class="search" title="输入当前处理人" value="${workflowName }">
										</td>
										<td width="15%" height="21" class="biao_bg1" align="right">
											<span class="wz"> 
											主办人员：
											</span>

										</td>
										<td class="td1" align="left" width="15%">
											<input name="startUserNameTEXT" id="startUserNameTEXT" type="text" style="width=100%" class="search" title="输入主办人员" value="${startUserName }">
										</td>
									</tr>
									<tr id="seniorSearchTr1" style="display: block">
										<td width="15%" height="21" class="biao_bg1" align="right" width="42%">
											<span class="wz">主办处室：</span>
										</td>
										<td class="td1" align="left">
											<select id="deptName" name="deptName" multiple="multiple" >
									            	<option value="" selected="selected">全部处室</option>
										            <option value="">秘书一处</option>
										            <option value="">秘书二处</option>
										            <option value="">秘书三处</option>
									        </select>
										</td>
										</td>
										
										<td width="15%" height="21" class="biao_bg1" align="right" width="42%">
											<span class="wz">选择流程：</span>
										</td>
										<td class="td1" colspan="3" align="left">
											<input type="hidden" name="processDefinitionNames" id="processDefinitionNames" value='${processDefinitionNames==null?"":processDefinitionNames}'>
											<select id="processDefinitionNamesTemp" name="processDefinitionNamesTemp" multiple="multiple" >
									            	<option value="" selected="selected">全部流程</option>
										            <option value="">发文流程</option>
									            	<option value="">收文流程</option>
										            <option value="">自办文</option>
									        </select>
										</td>
										</td>
									</tr>
								</table>
							</div>
									
									
								<table width="100%" border="0" cellpadding="0" cellspacing="1" style="display:none"
									class="table1">
									<tr id="">
										<td width="3%" align="center" class="biao_bg1"><img id='img_sousuo' src="<%=root%>/images/ico/sousuo.gif" width="15" height="15" alt=""></td>
							       		<td class="biao_bg1">&nbsp;</td>
									</tr>
								</table>
								
								<s:if test="state ==0">
								
								<webflex:flexCheckBoxCol caption="选择" valuepos="2"
									valueshowpos="0" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" valuepos="0"
									valueshowpos="0" width="30%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="主办人员" width="12%" valuepos="1" valueshowpos="1"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="主办处室" width="10%" valuepos="11" valueshowpos="11"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
									
								<webflex:flexTextCol caption="当前处理人" width="10%" valuepos="8" valueshowpos="8"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="前一步处理人" width="12%" valuepos="10" valueshowpos="10"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="状态" valuepos="4" valueshowpos="4"
								 	width="13%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="流程名称" valuepos="6" valueshowpos="6"
								 	width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								 	
							 	</s:if>
								<s:else>
								
								<webflex:flexCheckBoxCol caption="选择" valuepos="2"
									valueshowpos="0" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" valuepos="0"
									valueshowpos="0" width="50%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="主办人员" width="15%" valuepos="1" valueshowpos="1"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="主办处室" width="15%" valuepos="11" valueshowpos="11"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="流程名称" valuepos="6" valueshowpos="6"
								 	width="17%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
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
	item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看","viewMonitorData",1,"ChangeWidthTable","checkOneDis");
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

		function viewProcess(){
			var id = getValue();
			if(id!=""){
				if(id.split(",").length>1){
					alert("对不起,一次只能查看一项!");
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
				alert("请选择记录!");
			}
		} 

		function viewMonitorData(){
			var id = getValue();
			if(id!=""){
				if(id.split(",").length>1){
					alert("对不起,一次只能监控一项!");
					return;
				}
				if(window.parent.document.getElementById("isShowparentBtn") != undefined){
					window.parent.document.getElementById("isShowparentBtn").value = "false";
				}
				if(window.parent.document.getElementById("parentBtn") != undefined){
					window.parent.document.getElementById("parentBtn").style.display = "none";
				}
				location = scriptroot + "/workflowDesign/action/processMonitor!input.action?forward=org&instanceId=" + id+"&modelType="+'${modelType}'
								+"&proName="+encodeURI(encodeURI("${ProName}"))+"&processId=${processId}" +"&mytype=0"+ "&workflowName="+encodeURI(encodeURI("${workflowName}"));
			}else{
				alert("请选择记录!");
			}
		}
		
		function delProcess(){
			var id = getValue();
			var processId = '${processId}';
			if(id == ""){
				alert("请选择记录!");
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
			 $("#processDefinitionNamesTemp").dropdownchecklist({firstItemChecksAll: true, maxDropHeight: 150 ,width:300});
			 $("#deptName").dropdownchecklist({firstItemChecksAll: true, maxDropHeight: 150 ,width:300});
		}); 
		
		function sousuoClick(){
			$("#isFind").val("1");
			fromSubmit();
		}
		
		function fromSubmit(){
			var day = $.trim($("#day").val());
				if(day != "" && isNaN(day)){
					alert("请输入正确的天数.");
					return ;
				}
				var processDefinitionNamesTemp=$("#processDefinitionNamesTemp").val();
				processDefinitionNamesTemp=String(processDefinitionNamesTemp);
				/**/
				if(processDefinitionNamesTemp!=""){
					if(processDefinitionNamesTemp.split(",")[0]==""){
						processDefinitionNamesTemp="";
					}
				}
				
				var processDefinitionNames=encodeURI(processDefinitionNamesTemp);
				$("#processDefinitionNames").val(processDefinitionNames);
				$("#processName").val(encodeURI($("#processNameTEXT").val()));
				$("#startUserName").val(encodeURI($("#startUserNameTEXT").val()));
				$("#workflowName").val(encodeURI($("#workflowNameTEXT").val()));
				$("#searchDate").val($("#searchDate").val());
				$("#proName").val(encodeURI($("#proName").val()));
				$("form").submit();
				$("body").mask("正在查询数据,请稍候...");
		}
		
		//分页按钮触发的事件
		function doFromSubmit(objForm){
			$("#isFind").val("0");
			fromSubmit();
		}
		
</script>
	</body>
</html>
