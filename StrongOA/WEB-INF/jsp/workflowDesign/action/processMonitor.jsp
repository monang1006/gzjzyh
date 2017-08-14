<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html style="width:100%; height:100%;">
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<%response.setHeader("cache-control","Private"); %> 
		<title>流程监控</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css" rel="stylesheet">
		<LINK href="<%=frameroot%>/css/search.css" type=text/css  rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css" rel="stylesheet">
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
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>
		<!--<script language="javascript" src="<%=path%>/common/js/combox/jquery.sexy-combo.js"></script>-->
		<script>
			//在IE6中正常显示透明PNG 
			function correctPNG() 
			{
			     var arVersion = navigator.appVersion.split("MSIE");
			     var version = parseFloat(arVersion[1]);
			     if ((version >= 5.5) && (document.body.filters)) 
			     {
			       for(var j=0; j<document.images.length; j++)
			       {
			           var img = document.images[j];
			           var imgName = img.src.toUpperCase();
			           if (imgName.substring(imgName.length-3, imgName.length) == "PNG")
			           { 
			             var imgID = (img.id) ? "id='" + img.id + "' " : "";
			             var imgClass = (img.className) ? "class='" + img.className + "' " : "";
			             var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' ";
			             var imgStyle = "display:inline-block;" + img.style.cssText ;
			             if (img.align == "left") imgStyle = "float:left;" + imgStyle;
			             if (img.align == "right") imgStyle = "float:right;" + imgStyle;
			             if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle;
			             var strNewHTML = "<span " + imgID + imgClass + imgTitle
			             + " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"
			             + "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
			             + "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>" ;
			             img.outerHTML = strNewHTML;
			             j = j-1;
			           }
			       }
			     }    
			}
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
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()"  style="width:100%; height:100%; overflow:auto;">
	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		<div id="content" align="center" style=" width:100%;BACKGROUND: white;POSITION: absolute;HEIGHT: 100%;" >
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<s:form id="myTableForm" action="/workflowDesign/action/processMonitor.action" method="post">
							<input type="hidden" id="isFind" name="isFind" value="${isFind}" />
							<input type="hidden" id="processId" name="processId" value="${processId}" />
							<input type="hidden" name="proName" id="proName" value="${proName}" />
							<input type="hidden" name="processName" id="processName" value="${processName}" />
							<input type="hidden" name="startUserName" id="startUserName" value="${startUserName}" />
							<input type="hidden" name="workflowName" id="workflowName" value="${workflowName}" />
							<input type="hidden" name="searchDateTEXT" id="searchDateTEXT" value="${searchDateString}" />
							<input type="hidden" name="dayTEXT" id="dayTEXT" value="${day}" />
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="2" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
							<table width="100%" border="0" cellpadding="0" cellspacing="1"
									class="table1_search">
									<tr>
										<td>
										  <div style="float: left;width:200px;">
								       		&nbsp;&nbsp;标题：&nbsp;<input name="processNameTEXT" id="processNameTEXT" type="text" class="search" style="width:120px;"  title="输入标题" value="${processName}">
								       		</div>
								       		<div style="float: left;width:240px;">
								       		&nbsp;&nbsp;当前处理人：&nbsp;<input name="workflowNameTEXT" id="workflowNameTEXT" type="text" class="search" style="width:120px;" title="输入当前处理人" value="${workflowName}">
								       		</div>
								       		<div style="float: left;width:200px;">
								       		&nbsp;&nbsp;发起人：&nbsp;<input name="startUserNameTEXT" id="startUserNameTEXT" type="text"  class="search"  style="width:120px;" title="输入发起人" value="${startUserName}">
								       		</div>
								       		<div style="float: left;width:210px;">
								       		&nbsp;&nbsp;启动日期：&nbsp;<strong:newdate name="searchDate" id="searchDate" skin="whyGreen" isicon="true"  classtyle="search" title="输入启动日期"/>
								       		</div>
								       		<div style="float: left;width:140px;padding-top:5px;">
								       		&nbsp;&nbsp;状态：&nbsp;<s:select id="state"  name="state" list="#{'':'全部','0':'在办','1':'办毕'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
								       		</div>
								       		<div id="showDay" style="float: left;width:300px;display: none;">
								       		&nbsp;&nbsp;任务停留天数大于等于：&nbsp;<input name="day" id="day" type="text" class="search"  style="width:120px;" title="输入任务超过的天数" value="${day}" 
								       			onkeyup="value=value.replace(/[^\d]/g,'') "onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">
								       		</div>
								       			<s:if test="(proName==\"\") || (proName==null)">
								       		<div style="float: left;width:340px;padding-top: 5px;">
								       		&nbsp;&nbsp;流程名称：&nbsp;<input type="hidden" name="processDefinitionNames" id="processDefinitionNames" value="<s:property value='processDefinitionNames==null?\"\":processDefinitionNames'/>"/>
										<select id="processDefinitionNamesTemp" name="processDefinitionNamesTemp" multiple="multiple"   >
								            	<s:if test="processDefinitionNames==\"\" ">
								            	<option value="" selected="selected">全部</option>
								            	</s:if><s:else>
								            	<option value="" >全部</option>
								            	</s:else>
								            	<s:iterator value="%{processDefinitionNameMapIds}" >
								            	<s:if test="processDefinitionNames.indexOf(value) != -1" >
									            <option value="<s:property value='value'/>" selected="selected"><s:property value='key'/></option>
								            	</s:if>
								            	<s:else>
									            <option value="<s:property value='value'/>"><s:property value='key'/></option>
								            	</s:else>
								            	</s:iterator>
								        </select>
								       		</div>
								        </s:if>
								       		<div style="float: left;width:300px;padding-top: 3px;">
								       		&nbsp;&nbsp;是否超期：&nbsp;<s:select name="timeout" list="#{'':'全部','0':'未超期','1':'超期'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
								       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
								       		</div>
								       	</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" valuepos="2"
									valueshowpos="0" width="3%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexFlagCol caption="" valuepos="2"
									valueshowpos="9" width="1%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexFlagCol>
								<webflex:flexTextCol caption="标题" valuepos="0"
									valueshowpos="0" width="20%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
								<webflex:flexTextCol caption="当前处理人" width="12%" valuepos="7" valueshowpos="7"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="发起人" width="10%" valuepos="1" valueshowpos="1"
									isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexDateCol width="15%" caption="启动时间" valuepos="3" valueshowpos="3" showsize="16"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								<webflex:flexTextCol caption="状态" valuepos="4" valueshowpos="4" align="center"
								 	width="15%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="任务停留天数" valuepos="8" valueshowpos="8" align="center"
								 	width="10%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="超期" valuepos="5" valueshowpos="javascript:writeTimeout(5);" align="center"
								 	width="5%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								<webflex:flexTextCol caption="流程名称" valuepos="6" valueshowpos="6"
								 	width="10%" isCanDrag="true" isCanSort="true" showsize="8"></webflex:flexTextCol>
							</webflex:flexTable>
							<s:actionmessage />
						</s:form>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript" type="text/javascript">
		
		//correctPNG();		//IE6中正常显示透明PNG 
		
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;	
	item = new MenuItem("<%=root%>/images/operationbtn/Operation.png","运行情况","viewProcess",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/Monitor.png","监控","viewMonitorData",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delProcess",1,"ChangeWidthTable","checkMoreDis");
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
					alert("只可以查看一条记录的运行情况。");
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
				alert("请选择要查看运行情况的记录。");
			}
		} 

		function viewMonitorData(){
			var id = getValue();
			if(id!=""){
				if(id.split(",").length>1){
					alert("只可以监控一条记录。");
					return;
				}
				if(window.parent.document.getElementById("isShowparentBtn") != undefined){
					window.parent.document.getElementById("isShowparentBtn").value = "false";
				}
				if(window.parent.document.getElementById("parentBtn") != undefined){
					window.parent.document.getElementById("parentBtn").style.display = "none";
				}
				location = scriptroot + "/workflowDesign/action/processMonitor!input.action?instanceId=" + id+"&modelType="+'${modelType}'
								+"&proName="+encodeURI(encodeURI("${ProName}"))+"&processId=${processId}" + "&workflowName="+encodeURI(encodeURI("${workflowName}"));
			}else{
				alert("请选择要监控的记录。");
			}
		}
		
		function delProcess(){
			var id = getValue();
			var processId = '${processId}';
			if(id == ""){
				alert("请选择要删除的记录。");
				return;
			}
			if(confirm("确定要删除吗？")){
				location = scriptroot + "/workflowDesign/action/processMonitor!delete.action?ids=" + id + "&processId="+processId+"&page.pageNo=${page.pageNo}";
			}
		}
		
		
		
		$(document).ready(function(){
			$("input").focus();
			//当选择在办时  显示 任务停留天数大于等于
			var tmeo = document.getElementById("state").value;
			if(tmeo!=null&&tmeo!=""&&document.getElementById("state").value==0){
				$("#showDay").css("display","");
			}
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
			  $("#processDefinitionNamesTemp").dropdownchecklist({firstItemChecksAll: true, maxDropHeight: 150 ,width:200});
			  
		}); 

		function fromSubmit(){
				//var checkText=$("#processDefinitionNamesTemp").find("option:selected").text();
				//alert("text:"+$("#processDefinitionNamesTemp").find("option:selected").text());
				//alert("id:"+$("#processDefinitionNamesTemp").find("option:selected").attr("id"));
				var day = $.trim($("#day").val());
				if(day != "" && isNaN(day)){
					alert("请输入正确的天数。");
					return ;
				}
			var processDefinitionNamesTemp=$("#processDefinitionNamesTemp").val();
				processDefinitionNamesTemp=String(processDefinitionNamesTemp);
				/**/
				// 2013-12-6 0:26:15  Modified by capuchin  --------Start----------- 解决翻页会丢失流程类型条件的问题//
				if(processDefinitionNamesTemp!=""){
					if(processDefinitionNamesTemp.split(",")[0]==""){
						// processDefinitionNamesTemp=“” ;// 该段代码已被capuchin注释
						processDefinitionNamesTemp=processDefinitionNamesTemp.substr(1); // ,1111,1123 以逗号开头的数据之前被认为无效数据，实则，只需做截取即可
					}
				}
				// 2013-12-6 0:26:15  Modified by capuchin  --------End----------- 解决翻页会丢失流程类型条件的问题//
				
				var processDefinitionNames=encodeURI(processDefinitionNamesTemp);
				if(processDefinitionNames=="null" || processDefinitionNames==null || typeof(processDefinitionNames)=="undefined"){
			  	  $("#processDefinitionNames").val("");
				}
				else{
				  $("#processDefinitionNames").val(processDefinitionNames);
				  }
				$("#processName").val(encodeURI($("#processNameTEXT").val()));
				$("#startUserName").val(encodeURI($("#startUserNameTEXT").val()));
				$("#workflowName").val(encodeURI($("#workflowNameTEXT").val()));
				$("#searchDate").val($("#searchDate").val());
				$("#proName").val(encodeURI($("#proName").val()));
				$("form").submit();
				$("body").scrollLeft(0);//滚动条移动到界面左侧
				$("body").mask("正在查询数据,请稍候...");
		}
		
		//分页按钮触发的事件
		function doFromSubmit(objForm){
			$("#isFind").val("0");
			fromSubmit();
		}
	$(window).resize(function(){
	 var id=$("#content table:first");
	  var width=$(window).width();
	  if(width<=767){
		  id.css("width","767px");
	  }else{
		  id.css("width","100%");
	  }
});
</script>
	</body>
</html>
