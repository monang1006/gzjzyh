<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="/tags/web-flex" prefix="webflex"%>
<%@taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>公共联系人类型管理</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/search.css" type="text/css"
			rel="stylesheet">
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
	    	function ViewFormAndWorkflow(id){
	    		var ids = id.split("\#");
				var senddocId = ids[0];
				var WORKFLOWNAME = ids[1];
				 var width=screen.availWidth-10;
		  		var height=screen.availHeight-30;
		  		var ret=WindowOpen("<%=root%>/Send/send!input.action?senddoc.senddocId="+senddocId+"&workflowNameParame="+EE_EncodeCode(WORKFLOWNAME),'senddocchakan',width, height, "公文传输");
	    	}
	    	
	    	function reloadPage(){
	    		window.location = "<%=root%>/Send/send!query.action";
	    	}
	    	function add(){
	    		var url = "<%=root%>/publiccontact/publicContact!addType.action";
				var a = window.showModalDialog(url,window,'dialogWidth:300pt;dialogHeight:170pt;help:no;status:no;scroll:no');
				if(a==true){
					$("#myTableForm").submit();
				}
	    	}
	    	function edit(){
	    		var id = getValue();
	    		if(id==null||id==""){
	    			alert("请选择要编辑记录。");
	    			return;
	    		}
	    		if(id.indexOf(",")!=-1){
	    			alert("一次只能编辑一条记录。");
	    			return;
	    		}
	    		var url = "<%=root%>/publiccontact/publicContact!editType.action?typeId="+id;
				var a = window.showModalDialog(url,window,'dialogWidth:300pt;dialogHeight:170pt;help:no;status:no;scroll:no');
				if(a==true){
					$("#myTableForm").submit();
				}
	    	}
	    	function del(){
	    		var id = getValue();
	    		if(id==null||id==""){
	    			alert("请选择要删除的记录。");
	    			return;
	    		}
	    		if(confirm("确定要删除吗？注：该类型下的所有人员也将删除。")){
	    			var url = "<%=root%>/publiccontact/publicContact!delType.action?typeId="+id;
		    		$.post(url, function(data) {
					  	$("#myTableForm").submit();
					});
	    		}
	    	}
		</script>
		<base target="_self" />
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();window.focus();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
						<table width="100%" border="0" align="right" cellpadding="0"
							cellspacing="0">
							<tr>
								<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
								</td>
								<td align="left">
									<strong>公共联系人类型管理</strong>
								</td>
								<td>
									<table border="0" align="right" cellpadding="00" cellspacing="0">
										<tr>
										  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
										  <td class="Operation_list" onclick="add();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
	                 	                  <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		                  <td width="5"></td>
										  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	                  <td class="Operation_list" onclick="edit();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
	                 	                  <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		                  <td width="5"></td>
										  <td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
	                 	                  <td class="Operation_list" onclick="del();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
	                 	                  <td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
                  		                  <td width="5"></td>
										  <td width="2%"></td>
										</tr>

									</table>
								</td>
								</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<form id="myTableForm" action="<%=root%>/publiccontact/publicContact!typeManage.action"
							method="post">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="pccId" isCanDrag="true" showSearch="false"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${pccpage.result}"
								page="${pccpage}">
								<webflex:flexCheckBoxCol caption="选择" property="pccId"
									showValue="pccId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="类型名称" property="pccId"
									showValue="pccName" isCanDrag="true"
									isCanSort="true" width="37%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="序号" property="pccId"
									showValue="pccNum" isCanDrag="true" isCanSort="true"
									width="10%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="备注" property="pccId"
									showValue="pccOther" isCanDrag="true" isCanSort="true"
									width="8%"></webflex:flexTextCol>
							</webflex:flexTable>
						</form>
					</td>
				</tr>
			</table>
		</div>
		<script language="javascript">
		    var sMenu = new Menu();
			function initMenuT(){
				sMenu.registerToDoc(sMenu);
				var item = null;
				item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","add",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);    
				item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","edit",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);    
				item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","del",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);    
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
		  </script>
	</body>
</html>
