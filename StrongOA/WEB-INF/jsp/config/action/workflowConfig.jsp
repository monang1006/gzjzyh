<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@	taglib uri="/tags/web-flex" prefix="webflex"%>
<%@	taglib prefix="s" uri="/struts-tags"%>
<%@	include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>人员选择配置列表信息</title>
		<link href="<%=frameroot%>/css/strongitmenu.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_list.css" type="text/css"
			rel="stylesheet">
		<script language='javascript'
			src='<%=root%>/common/js/grid/ChangeWidthTable.js'
			type="text/javascript"></script>
		<script language="javascript" src="<%=root%>/common/js/menu/menu.js"
			type="text/javascript"></script>
		<LINK type=text/css rel=stylesheet
			href="<%=frameroot%>/css/search.css">
		<script type="text/javascript" language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/common/search.js"></script>	
		<script language="javascript" src="<%=path%>/common/js/common/windowsadaptive.js"></script>		
		<script type="text/javascript">
			function judgeStatus(asIsActive){
			  	var str;
				if(asIsActive == "1"){
					str = "启用";
				}else{
					str = "<font color='red'>停用</font>";
				}
				return str;
			}
		</script>

	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT()">
		<div id="contentborder" align="center">
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="40"
									style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
									<table width="100%" border="0" cellspacing="0" cellpadding="00">
										<tr>
											<td class="table_headtd_img" >
												<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
											</td>
											<td align="left">
												<strong>人员选择配置列表</strong>
											</td>
											<td align="right">
												<table border="0" align="right" cellpadding="00" cellspacing="0">
													<tr>
														<td>
															<table border="0" align="right" cellpadding="00"cellspacing="0">
																<tr>
																	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="addConfig();"><img src="<%=root%>/images/operationbtn/add.png"/>&nbsp;新&nbsp;建&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="editConfig();"><img src="<%=root%>/images/operationbtn/edit.png"/>&nbsp;编&nbsp;辑&nbsp;</td>
												                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
											                  		<td width="5"></td>
											                  		<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
												                 	<td class="Operation_list" onclick="delConfig();"><img src="<%=root%>/images/operationbtn/del.png"/>&nbsp;删&nbsp;除&nbsp;</td>
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
								</td>
							</tr>
							<tr>
								<td>
									<form id="myTableForm"
										action="<%=root%>/config/action/workflowConfig.action" method="post">
										<webflex:flexTable name="myTable" width="100%" height="370px"
											wholeCss="table1" property="asId" isCanDrag="true"
											isCanFixUpCol="true" clickColor="#A9B2CA"
											footShow="showCheck" getValueType="getValueByProperty"
											collection="${page.result}" page="${page}">
											<table width="100%" border="0" cellpadding="0"
												cellspacing="1" class="table1_search">
												<tr>
													<td>
											       		<div style="float: left; ">
											       		&nbsp;&nbsp;配置名称：&nbsp;<input name="searchAsName" id="searchAsName" type="text" class="search" title="请输入配置名称" value="${searchAsName}">
														</div>
							       						<div style="float: left; ">
											       		&nbsp;&nbsp;配置别名：&nbsp;<input name="searchAsAlias" id="searchAsAlias" type="text" class="search" title="请输入配置别名" value="${searchAsAlias}">
											       		</div>
							       						<div style="float: left; ">
											       		&nbsp;&nbsp;配置前缀：&nbsp;<input name="searchAsPrefix" id="searchAsPrefix" type="text" class="search" title="请输入配置前缀" value="${searchAsPrefix}">
											       		</div>
							       						<div style="float: left;width: 255px; ">
							       						&nbsp;&nbsp;状态：&nbsp;<s:select name="searchAsIsActive" list="#{'':'全部','1':'启用','0':'停用'}" listKey="key" listValue="value" onchange='$("#img_sousuo").click();'/>
											       		&nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" />
											       		</div>
											       	</td>
												</tr>
											</table>
											<webflex:flexCheckBoxCol caption="选择" property="asId"
												showValue="asName" width="3%" isCheckAll="true"
												isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
											<webflex:flexTextCol caption="配置名称" property="asName"
												showValue="asName" width="15%" isCanDrag="true"
												isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="配置别名" property="asAlias"
												width="15%" showValue="asAlias" isCanDrag="true"
												isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="配置前缀" property="asPrefix"
												width="15%" showValue="asPrefix" isCanDrag="true"
												isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="展现URL" property="asActionUrl"
												width="20%" showValue="asActionUrl" isCanDrag="true"
												isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="解析类" property="asHandlerClass"
												width="20%" showValue="asHandlerClass" isCanDrag="true"
												isCanSort="true"></webflex:flexTextCol>
											<webflex:flexTextCol caption="状态" property="asIsActive"
												width="15%" showValue="javascript:judgeStatus(asIsActive)"
												isCanDrag="true" isCanSort="true"></webflex:flexTextCol>
										</webflex:flexTable>
									</form>
								</td>
							</tr>
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
	item = new MenuItem("<%=root%>/images/operationbtn/add.png","新建","addConfig",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	item = new MenuItem("<%=root%>/images/operationbtn/edit.png","编辑","editConfig",1,"ChangeWidthTable","checkOneDis");
	sMenu.addItem(item);			
	item = new MenuItem("<%=root%>/images/operationbtn/del.png","删除","delConfig",1,"ChangeWidthTable","checkMoreDis");
	sMenu.addItem(item);
	sMenu.addShowType("ChangeWidthTable");
    registerMenu(sMenu);
}

		function addConfig(){
			var url="<%=root%>/config/action/workflowConfig!input.action";
			var returnvalue = window.showModalDialog(url,window,"dialogWidth:480px;dialogHeight:360px;status:no;help:no;scroll:no;status:0;help:0;scroll:0;");
		}

		//修改流程定义记录
		function editConfig(){
			var id = getValue();
			if(id == ""){
				alert("请选择要编辑的记录。");
				return ;
			}else{
				var ids = id.split(",");
				if(ids.length>1){
					alert("只可以编辑一条记录。");
					return ;
				}
			}
			var url = scriptroot + "/config/action/workflowConfig!input.action?asId=" + id;
			var returnvalue = window.showModalDialog(url,window,"dialogWidth:480px;dialogHeight:360px;status:no;help:no;scroll:no;status:0;help:0;scroll:0;")
		}
		
		function delConfig(){
		  var ids = getValue();
		  if(ids == ""){
				alert("请选择要删除的记录。");
				return ;
			}
          var ret = confirm("删除操作执行后不可恢复，是否继续操作？");
          if (ret) {
            location = "<%=path%>/config/action/workflowConfig!delete.action?ids=" + ids;	
          }		  
		}
</script>
	</body>
</html>
<script>
$(document).ready(function(){
  $("#img_sousuo").click(function(){
  	$("#myTableForm").submit();
  });     
  //获取焦点
	$("input").focus();
});
</script>
