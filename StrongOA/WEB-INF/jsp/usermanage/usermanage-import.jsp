<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@	include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>用户信息导入</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_list.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/scripts/common.js"></SCRIPT>
		<s:head/>
		<%
			String excelOrgId=(String)request.getParameter("excelOrgId");
		 %>
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder align=center>
		<iframe id="hiddenFrame" name="hiddenFrame" style="width:0;height:0;"></iframe>
		<s:form action="/usermanage/usermanage!importdata.action" id="myTableForm" name="veteranform" target="hiddenFrame" method="post" enctype="multipart/form-data">
	    <input type="hidden" id=excelOrgId value="<%=excelOrgId %>" name="excelOrgId" />
			<table width="100%"
				style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
				<tr>
					<td>&nbsp;</td>
					<td width="20%">
						<img src="<%=frameroot%>/images/ico/ico.gif">&nbsp;
						导入用户信息
					</td>
					<td width="80%">
						<table border="0" align="right" cellpadding="00" cellspacing="0">
							<tr>
								<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
			                 	<td style="background:url(<%=frameroot%>/images/ch_h_m.gif) repeat-x;font-weight: bold;color:white;cursor: pointer;" id="importButton">&nbsp;导&nbsp;入&nbsp;</td>
			                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
			               		<td width="5"></td>
			               		<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
			                 	<td class="Operation_input1" onclick="closeWindow();">&nbsp;关&nbsp;闭&nbsp;</td>
			                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
            <table width="100%">
				<tr>
					<td height="10">	
					</td>
				</tr>
			</table>
			<table align="center" width="100%" border="0" cellpadding="0" 
				cellspacing="1" class="table1">
				<tr>
				<td class="biao_bg1" align="right">选择导入的文件: 
			<%-- <s:file label="选择装载文件" id="upFile" name="upFile"></s:file> --%>
				</td>
				<td class="td1" align="left">
					 <input class="upFileBtn" type="file" id="upFile" onkeydown="return false;" name="upFile" />&nbsp;<font color='#999999'>只能上传.xls类型的文件
				</td>
				</tr>
			</table>
		</s:form>
		<DIV id="LOADING" onClick="showLoading(false)"
			style="position: absolute; top: 30%; left: 38%; display: none"
			align=center>
			<font color="#16387C"><strong id="showMessage">正在导入，请稍候...</strong>
			</font>
			<br>
			<IMG src="<%=frameroot%>/images/tab/loading.gif">
		</DIV>
	 <table align="center">
		</DIV>
		<SCRIPT type="text/javascript">
		
/** 
 * 显示/隐藏加载信息
 * @param bl true/false
 */
 
function showLoading(bl){
	var state = bl ? "block" : "none";
	$("#LOADING")[0].style.display = state;
}

/**
 * 改变展示消息
 * @param message 展示消息
 */
function changeMessage(message){
	$("#showMessage").html(message);
}
	
		function closeWindow(){
	//	window.dialogArguments.location="<%=path %>/orderfinance/bankdraft/bankdraft.action";
		window.close();
	}	
	
	function resetButton(){
		$("#importButton").attr("disabled", false);
	}
	
	$("#importButton").click(function(){
    	$("#importButton").attr("disabled", true);
		if($.trim($("#upFile").val()).endsWith(".xls")){
			showLoading(true);
			alert("导入的用户\"密码\"列为空，则默认为1。");
			$("#myTableForm").submit();
		}else{
			alert("导入文件格式必须为xls格式。");
			resetButton();
		}
	});
		</SCRIPT>
	</body>
</html>