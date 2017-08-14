<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>在办文件跟踪</title>
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
		<script src="<%=path%>/common/js/common/common.js"
      type="text/javascript"></script>
		<!--<script language="javascript" src="<%=path%>/common/js/combox/jquery.sexy-combo.js"></script>-->
		<script>
		
		</script>
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;" onload="initMenuT()"  style="overflow:auto;">
	<div id="contentborder" align="center"">
	    <table width="100%" border="0" cellspacing="0" cellpadding="00">
							<tr>
								<td colspan="3" class="table_headtd">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
											<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>在办文件跟踪</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
							                 			<td class="Operation_list" onclick="viewMonitorData();"><img src="<%=root%>/images/operationbtn/view.png"/>&nbsp;查&nbsp;看&nbsp;</td>
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
	<script type="text/javascript" src="<%=path %>/common/js/newdate/WdatePicker.js"></script>
		
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<s:form id="myTableForm" action="/traceDoc/traceDoc!doingDocList.action" method="get">
							
							<webflex:flexTable name="myTable" width="100%" height="370px"
								wholeCss="table1" property="0" isCanDrag="true" showSearch="false"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByArray" collection="${page.result}"
								page="${page}">
							
								<webflex:flexCheckBoxCol caption="选择" valuepos="0"
									valueshowpos="5" width="5%" isCheckAll="true" isCanDrag="false"
									isCanSort="false"></webflex:flexCheckBoxCol>
								
								<webflex:flexTextCol caption="标题" valuepos="5"
									valueshowpos="5" width="30%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
														
								<webflex:flexDateCol width="20%" caption="开始时间" valuepos="1" valueshowpos="1" showsize="16"
									isCanDrag="true" isCanSort="true"></webflex:flexDateCol>
								
								<webflex:flexTextCol caption="当前处理人" valuepos="2" align="center"
									valueshowpos="2" width="25%" isCanDrag="true" isCanSort="true" showsize="30"></webflex:flexTextCol>
									
								<webflex:flexTextCol caption="流程名称" valuepos="3" valueshowpos="3" align="center"
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
		
		
var sMenu = new Menu();
function initMenuT(){
	sMenu.registerToDoc(sMenu);
	var item = null;	
	
	item = new MenuItem("<%=root%>/images/operationbtn/view.png","查看","viewMonitorData",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);
	<%--
	item = new MenuItem("<%=root%>/images/ico/tb-delete3.gif","删除","delProcess",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	--%>
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

         var width=screen.availWidth-10;
   		var height=screen.availHeight-30;
   		
   		var ret=WindowOpen("<%=path%>/traceDoc/traceDoc!viewProcessed.action?instanceId=" + id,'processed',width, height, "在办文件");
			
	//   var result=window.open("<%=path%>/traceDoc/traceDoc!viewProcessed.action?taskId="+id+"&instanceId=" + ss,'processed',"height=" + height + ",width=" + width + ","+
		//                                "top=0, left=30%, toolbar=no,menubar=no, scrollbars=yes,resizable=yes,location=no, fullscreen=yes,status=no");
		                                
			}else{
				alert("请选择要查看的记录。");
			}
		}
		
		
</script>
	</body>
</html>
