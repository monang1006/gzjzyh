<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>重要文件跟踪</title>
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
		<script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
		<!--<script language="javascript" src="<%=path%>/common/js/combox/jquery.sexy-combo.js"></script>-->
		<script>
		
		</script>
		<style>
		#contentborder11 {
			BACKGROUND: white;
			OVERFLOW: auto;
			WIDTH: 100%;
			POSITION: absolute;
			HEIGHT: 100%;
		}
		</style>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()"  style="overflow:auto;">

		<table width="100%" border="0" cellspacing="0" cellpadding="00">
			<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>重要文件列表</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00"cellspacing="0">
									<tr>
										<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="viewMonitorData();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="sendReminder();"><img src="<%=root%>/images/operationbtn/urge.png"/>&nbsp;催&nbsp;办&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
				                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="delProcess();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
				                  		<td width="5"></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
<!--		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>-->
   <script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>  
		<div id="contentborder11" align="center"">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<s:form id="myTableForm" action="/traceDoc/traceDoc!workfolwTypelist.action" method="post">
						<input id="workflowType" name="workflowType" type="hidden" value="${workflowType} "/>
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="0" isCanDrag="true"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
							
							 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
							     <tr>
							     	<td>
							     		&nbsp;&nbsp;标题：&nbsp;<input name="traceProcessTitle" id="traceProcessTitle" type="text" class="search" title="请您输入标题" value="${traceProcessTitle}">
							       		&nbsp;&nbsp;发起人：&nbsp;<input name="startUserName" id="startUserName" type="text" class="search" title="请您输入发起人" value="${startUserName}">
							       		&nbsp;&nbsp;开始时间：&nbsp;<strong:newdate  name="searchDate" id="searchDate" skin="whyGreen" isicon="true"  classtyle="search" title="请输入开始时间" dateform="yyyy-MM-dd" dateobj="${searchDate}"/>
							       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
							       	</td>
							     </tr>
							</table>
							 <webflex:flexCheckBoxCol caption="选择" valuepos="2"
									valueshowpos="3" width="40px" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" valuepos="3" onclick="ViewFormAndWorkflow(this.value);" 
									valueshowpos="3" width="30%" isCanDrag="true" isCanSort="true" showsize="20"></webflex:flexTextCol>
									
								<webflex:flexTextCol caption="发起人" width="20%" valuepos="0" valueshowpos="0" align="center"
								isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
								
								<webflex:flexDateCol width="25%" caption="开始时间" valuepos="7" valueshowpos="7" showsize="16" dateFormat="yyyy-MM-dd HH:mm"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								
								<webflex:flexTextCol caption="流程名称" valuepos="1" valueshowpos="1" align="center"
								 	width="20%" isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
							</webflex:flexTable>
							<s:actionmessage />
						</s:form>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript" type="text/javascript">
	$("#img_sousuo").click(function(){
					$("form").submit();
				});
		
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;	
	
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewMonitorData",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/urge.png","催办","sendReminder",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delProcess",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
   
}


		function viewMonitorData(){
			var id = getValue();
			if(id!=""){
				if(id.split(",").length>1){
					alert("只可以查看一条记录。");
					return;
				}
			var result = id.split("$");
			var instanceId = result[0];
			//alert(ss);

        var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		var ret=WindowOpen("<%=path%>/traceDoc/traceDoc!viewProcessed.action?instanceId=" + instanceId,'processed',width, height, "重要文件");
			
	 //  var result=window.open("<%=path%>/traceDoc/traceDoc!viewProcessed.action?taskId="+ss+"&instanceId=" + id,'processed',"height=" + height + ",width=" + width + ","+
		//                                "top=0, left=30%, toolbar=no,menubar=no, scrollbars=yes,resizable=yes,location=no, fullscreen=yes,status=no");
		                                
			}else{
				alert("请选择要查看的记录。");
			}
		}
		
		
		function ViewFormAndWorkflow(){
				var id =$(window.event.srcElement).siblings().eq(1).find(":checkbox").val();
				var result = id.split("$");
			    var instanceId = result[0];
			    var width=screen.availWidth-10;
   		        var height=screen.availHeight-30;
   		        var ret=WindowOpen("<%=path%>/traceDoc/traceDoc!viewProcessed.action?instanceId=" + instanceId,'processed',width, height, "重要文件");
				
			}
		
		
	  
  	function delProcess(){
			var id = getValue();
		
			if(id == ""){
				alert("请选择要删除的记录。");
				return;
			}
			//else{	//Bug序号： 0000005959  删除多条数据
				//var ids = id.split(",");
			 	//if(ids.length>1){
			 		//alert("一次只能删除一项！");
			 	//	return ;
			 	//}
			//}
			if(confirm("确定要删除吗？")){
				//var result = id.split("$");
			//var instanceId = result[0];
				location = scriptroot + "/traceDoc/traceDoc!delete.action?ids=" +id;
			}
		}	
		
		//催办
		function sendReminder(){
			var id=getValue();
			var returnValue = "";
			if(id == ""){
				alert("请选择要催办的记录。");
			 	return ;
			}else{
				var ids = id.split(",");
			 	if(ids.length>1){
			 		alert("只可以催办一条记录。");
			 		return ;
			 	}
			}
		 	var result = id.split("$");
			var instanceId = result[0];
			var overDate = result[1];
		 	if(overDate == "1"){
		 		alert("流程已结束，无需催办。");
		 		return;
		 	}
		 	OpenWindow("<%=root%>/fileNameRedirectAction.action?toPage=senddoc/sendDoc-urgchoose.jsp?instanceId="+instanceId,400, 215, window);
		}
</script>
	</body>
</html>
