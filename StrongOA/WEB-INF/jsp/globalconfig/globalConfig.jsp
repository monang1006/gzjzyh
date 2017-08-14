<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
  	<head>
    	<title>系统全局配置</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type="text/css" rel=stylesheet>
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				<s:iterator value="plugins">
					var pluginName = '<s:property value="globalName"/>';
					var pluginValue = '<s:property value="globalValue"/>'; 
					var index = pluginName.indexOf("_");
					var pluginNameSuffix = pluginName.substring(index+1);//
					$("input[name='radio_"+pluginNameSuffix+"'][value='"+pluginValue+"']").attr("checked",true);
					$("#"+pluginName).val(pluginValue);
				</s:iterator>
			});
		
			function doSubmit(){
				var $plugins = $("[name^='plugins_']");
				var retJson = "[";
	       		$.each($plugins, function(index, domElement) {
		        	retJson += "{'pluginName':'"+domElement.name+"','pluginValue':'"+domElement.value+"'";
		        	retJson += ",'pluginDesc':'"+domElement.pluginDesc+"','pluginModule':'"+domElement.pluginModule+"'},";
		        });
		        if(retJson != "["){
		        	retJson = retJson.substring(0,retJson.length - 1);
		        }
		        retJson += "]";
		        $.post("<%=path%>/globalconfig/globalConfig!save.action",{pluginData:retJson},function(retCode){
		        	if(retCode == "0"){
		        		//alert("操作成功！");
		        		window.location.reload();
		        	} else if(retCode == "-1"){
		        		alert("很抱歉，系统出现错误，请与管理员联系。");
		        	}
		        });
			}
			
			function doOnClick(objRadio){
				var objRadioName = objRadio.name;
				var index = objRadioName.indexOf("_");
				var pluginNameSuffix = objRadioName.substring(index+1);//
				$("#plugins_"+pluginNameSuffix).val(objRadio.value);
			}
			
		</script>
  	</head>
  	<body class=contentbodymargin>
  		<div id=contentborder align=center>
  			<form>
  				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>流程设置</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="doSubmit();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
				</tr>
				<tr>
				<td align="center">
	  			<fieldset style="width: 90%">
	  					<legend>
							<span class="wz">流程设置</span>
						</legend>
						<table width="100%" height="80%" border="0" style="border-top: 0px;" cellpadding="0" cellspacing="0" class="table1">
							<tr>
								<td width="30%" height="21" class="biao_bg1" aligt="right">
									<span class="wz">退回节点选择方式：</span>
								</td>
								<td width="70%" class="td1">
									<input id="plugins_backspace" pluginModule="0" name="plugins_backspace" pluginDesc="退回节点选择方式" value="0" type="hidden"/>
									<s:radio name="radio_backspace"  onclick="doOnClick(this);" value="0" list="#{'0':'列表','1':'流程图','2':'列表和流程图'}"></s:radio>
								</td>
							</tr>
							<tr>
								<td width="30%" height="21" class="biao_bg1" aligt="right">
									<span class="wz">驳回节点选择方式：</span>
								</td>
								<td width="70%" class="td1">
									<input id="plugins_overrule" pluginModule="0" name="plugins_overrule" pluginDesc="驳回节点选择方式" value="0" type="hidden"/>
									<s:radio name="radio_overrule"  onclick="doOnClick(this);" value="0" list="#{'0':'列表','1':'流程图','2':'列表和流程图'}"></s:radio>
								</td>
							</tr>
							<tr>
								<td width="30%" height="21" class="biao_bg1" aligt="right">
									<span class="wz">主办权限：</span>
								</td>
								<td width="70%" class="td1">
									<input id="plugins_maindoing" pluginModule="0" name="plugins_maindoing" pluginDesc="启用主办功能" value="1" type="hidden"/>
									<s:radio name="radio_maindoing"  onclick="doOnClick(this);" value="1" list="#{'0':'不启用','1':'启用'}"></s:radio>
								</td>
							</tr>
							<tr>
								<td width="30%" height="21" class="biao_bg1" aligt="right">
									<span class="wz">来文草稿公文签收：</span>
								</td>
								<td width="70%" class="td1">
									<input id="plugins_getDocs" pluginModule="0" name="plugins_getDocs" pluginDesc="启用来文草稿公文签收功能" value="1" type="hidden"/>
									<s:radio name="radio_getDocs"  onclick="doOnClick(this);" value="1" list="#{'0':'不启用','1':'启用'}"></s:radio>
								</td>
							</tr>
						</table>
	  			</fieldset>
	  			</td>
	  			</tr>
  			</form>
  		</div>
  	</body>
</html>
