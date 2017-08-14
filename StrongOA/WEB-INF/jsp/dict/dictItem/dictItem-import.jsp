<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<HTML>
	<HEAD>
		<TITLE>字典项导入</TITLE>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript"
			src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/scripts/common.js"></SCRIPT>
	
		<s:head />
	</head>
	<base target="_self" />
	<body class=contentbodymargin oncontextmenu="return false;">
				<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<!-- 增加form    -->
		<DIV id=contentborder align=center>
		<iframe id="hiddenFrame" name="hiddenFrame" style="width:0;height:0;"></iframe>
		<s:form action="/dict/dictItem/dictItem!importdata.action" id="myTableForm" name="veteranform" target="hiddenFrame" method="post" enctype="multipart/form-data">
	<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
				<tr>
					<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							  
										<strong>导入字典项信息</strong>
									
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" id="importButton">&nbsp;导&nbsp;入&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
				                  	
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="closeWindow();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
							
						</tr>
					</table>
				</td>
				</tr>
				<tr>
					<td>
			
			<input type="hidden" name="dictCode" id="dictCode"/>
			<iframe scr='' id='tempframe' name='tempframe' style='display:none'></iframe>
			<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
				<tr>
									<td  class="biao_bg1" align="right">
										<span class="wz">选择导入文件: </span>
									</td>
									<td class="td1" style="margin-left: 5px;">
										<input type="file" id="upFile" onkeydown="return false;" name="upFile" />
                                        <a  href="#" class="button" onclick="moban()">下载模板</a></br>
									</td>
								</tr>
				
				<tr>
				<td  class="biao_bg1" align="right">
								<span class="wz"></span>
				</td>
                   <td class="td1" style="margin-left: 5px;">
                    <font color="#999999">请参照下载模板格式导入，模板中的单元格为必填项。</br>可选状态为0(表示可选)或1(表示不可选)</font>
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
	function moban(){
	  document.getElementById('tempframe').src="<%=path%>/dict/dictItem/dictItem!moban.action";
	}
	function resetButton(){
		$("#importButton").attr("disabled", false);
	}
	
	$("#importButton").click(function(){
    	$("#importButton").attr("disabled", true);
		if($.trim($("#upFile").val()).endsWith(".xls")){
			showLoading(true);
			$("#dictCode").val('<%=request.getParameter("dictCode")%>');
			$("#myTableForm").submit();
		}else{
			alert("导入文件格式必须为xls格式。");
			resetButton();
		}
	});
			
		</SCRIPT>
	</body>
</html>
