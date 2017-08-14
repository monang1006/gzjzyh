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
		<title>发送公文传输列表</title>
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
		function EE_EncodeCode(stringObj) {
			if (stringObj == "" || stringObj == undefined || stringObj == "undefined") {
				return stringObj;
			}
			var result = "";
			var charTemp = "";
			if (typeof(stringObj) != "string") {
				alert("EE_EncodeCode()方法的参数必须是字符串!");
				return;
			}
			for (var i = 0; i < stringObj.length; i++) {
				charTemp = stringObj.charAt(i);
				if (charTemp == encodeURI(encodeURI(charTemp))) {
					if (charTemp == escape(escape(charTemp))) {
						result += encodeURIComponent(encodeURIComponent(charTemp));
					} else {
						result += escape(escape(charTemp));
					}
				} else {
					result += encodeURI(encodeURI(charTemp));
				}
			}
			return result;
		}
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
									<strong>公文传输列表</strong>
								</td>
								</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="100%">
						<form id="myTableForm" action="<%=root%>/Send/send!query.action"
							method="post">
							<webflex:flexTable name="myTable" width="100%" height="200px"
								wholeCss="table1" property="senddocId" isCanDrag="true" showSearch="false"
								isCanFixUpCol="true" clickColor="#A9B2CA" footShow="showCheck"
								getValueType="getValueByProperty" collection="${page.result}"
								page="${page}">
								<%--<table width="30%" border="0" cellpadding="0" cellspacing="1"
									class="table1">
									<tr>
										<td width="7%" align="center" class="biao_bg1">
											<img id='img_sousuo' src="<%=root%>/images/ico/sousuo.gif"
												width="15" height="15" alt="">
										</td>
										<td width="93%" align="center" class="biao_bg1">
											<strong:newdate name="searchDate" id="searchDate"
												dateobj="${searchDate}" width="100%" skin="whyGreen"
												isicon="true" classtyle="search" title="输入创建日期"
												dateform="yyyy-MM-dd" />
										</td>
									</tr>
								</table>
								--%>
								<webflex:flexCheckBoxCol caption="选择" property="senddocId"
									showValue="senddocId" width="5%" isCheckAll="true"
									isCanDrag="false" isCanSort="false"></webflex:flexCheckBoxCol>
								<webflex:flexTextCol caption="标题" property="senddocId"
									showValue="WORKFLOWTITLE"
									onclick="ViewFormAndWorkflow(this.value);" isCanDrag="true"
									isCanSort="true" width="37%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="流水号" property="WORKFLOWCODE"
									showValue="WORKFLOWCODE" isCanDrag="true" isCanSort="true"
									width="10%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="主题词" property="senddocKeywords"
									showValue="senddocKeywords" isCanDrag="true" isCanSort="true"
									width="8%"></webflex:flexTextCol>
								<webflex:flexTextCol caption="密级" property="senddocSecretLvl"
									showValue="senddocSecretLvl" isCanDrag="true" isCanSort="true"
									width="20%"></webflex:flexTextCol>
								<webflex:flexDateCol caption="收文时间" property="senddocRecvTime"
									showValue="senddocRecvTime" isCanDrag="true"
									dateFormat="yyyy-MM-dd" isCanSort="true" width="20%">
								</webflex:flexDateCol>
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
				/*
				var item = null;
				item = new MenuItem("<%=root%>/images/ico/chakan.gif","查看附件","chakan",1,"ChangeWidthTable","checkMoreDis");
				sMenu.addItem(item);    
				item = new MenuItem("<%=root%>/images/ico/daoru.gif","确认签收","save",1,"ChangeWidthTable","checkOneDis");
				sMenu.addItem(item);    
				*/
				sMenu.addShowType("ChangeWidthTable");
				registerMenu(sMenu);
			}
		  </script>
	</body>
</html>
