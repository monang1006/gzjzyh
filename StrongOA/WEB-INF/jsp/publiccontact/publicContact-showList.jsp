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
		<title>公共联系人</title>
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
		function isSearch(){
			$("#issearch").val("1");
			$("#myTableForm").action="<%=root%>/publiccontact/publicContact!showList.action";
		  	$("#myTableForm").submit();
		}
    	function exportPerson(){
    		var typeId = $("#typeId").val();
    		var pcId = getValue();
    		if(pcId==null||pcId==""){
    			if(confirm("没选择人员将会导出本类别下的所有人员，是否继续？")){
    				document.getElementById('tempframe').src="<%=path%>/publiccontact/publicContact!exportContacts.action?typeId="+typeId;
    			}else{
    				return;
    			}
    		}
    		document.getElementById('tempframe').src="<%=path%>/publiccontact/publicContact!exportContacts.action?pcId="
    		                         +pcId+"&typeId="+typeId;
    	}
		</script>
		<base target="_self" />
	</head>
	<body class="contentbodymargin" oncontextmenu="return false;"
		onload="initMenuT();window.focus();">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<iframe scr='' id='tempframe' name='tempframe' style='display:none'>
					</iframe>
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
									<strong>${pccmodel.pccName}</strong>
								</td>
								<td align="right">
									<table border="0" align="right" cellpadding="00" cellspacing="0">
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_l.jpg"/></td>
					                 	<td class="Operation_list" onclick="exportPerson();"><img src="<%=root%>/images/operationbtn/daochu.png"/>&nbsp;导出Excel&nbsp;</td>
					                 	<td width="4"><img src="<%=frameroot%>/images/bt_r.jpg"/></td>
					                 	<td width="5"></td>
					                 </table>
								</td>
								</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<form id="myTableForm" action=""
							method="post">
							<input type="hidden" id="typeId" name="typeId" value="${typeId}">
							<input type="hidden" id="issearch" name="issearch" value="">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="senddocId" isCanDrag="true" showSearch="false"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table1_search">
									<tr>
										<td>
											 &nbsp;&nbsp;姓名：&nbsp;<input id="searchname" name="searchname" cssClass="search" title="请输入姓名" style="width:110px;" maxlength="50" onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')" value="${searchname}"> 										
											 &nbsp;&nbsp;电话：&nbsp;<input id="searchtell" name="searchtell"  style="width:110px;"cssClass="search" title="请输入电话" maxlength="50"  onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')" value="${searchtell}">										
											 &nbsp;&nbsp;Email：&nbsp;<input id="searchemail" name="searchemail"  style="width:110px;" cssClass="search" title="请输入Email" maxlength="50"  onkeyup="this.value=this.value.replace(/\s/g,'')" onafterpaste="this.value=this.value.replace(/\s/g,'')" value="${searchemail}">
											 &nbsp;&nbsp;&nbsp;&nbsp;<input id="img_sousuo" type="button" onclick="isSearch();"/>
										</td>
									</tr>
								</table>
								<webflex:flexCheckBoxCol caption="选择" property="pcId"
									showValue="pcId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="姓名" property="pcId" align="center"
									showValue="pcName" isCanDrag="true"
									isCanSort="true" width="20%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="电话" property="pcId"  align="center"
									showValue="pcTell" isCanDrag="true" isCanSort="true"
									width="20%"></webflex:flexTextCol> 
								<webflex:flexTextCol caption="手机号码" property="pcId" align="center"
									showValue="pcPhone" isCanDrag="true" isCanSort="true"
									width="20%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="Email" property="pcId" align="center"
									showValue="pcEmail" isCanDrag="true" isCanSort="true"
									width="20%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="职务" property="pcId" align="center"
									showValue="pcPost" isCanDrag="true"
									isCanSort="true" width="20%">
								</webflex:flexTextCol>
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
				item = new MenuItem("<%=root%>/images/operationbtn/daochu.png","导出Excel","exportPerson",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);    
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
		  </script>
	</body>
</html>
