<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>新建工作委托</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script> 
	</head>
<body oncontextmenu="return false;">
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
	<div style="height:100%;overflow: auto;">
		<form action="">
			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;新建工作委托</td
				</tr>
			<tr>
				<td width="15%" height="21" class="biao_bg1"><span class="wz">选择流程</span></td>
				<td class="td1">
					<select style="width: 30%;">
						<option value="">请选择流程</option>
					</select>
				</td>
			</tr>
			<tr>
				<td width="15%" height="21" class="biao_bg1"><span class="wz">被委托人</span></td>
				<td  class="td1">
					<input type="text" style="width: 30%;" readOnly name="TO_NAME">
			    	<a href="javascript:;">选择</a>
			    	<a href="javascript:;">清空</a>
				</td>
			</tr>
			<tr>
				<td width="15%" height="21" class="biao_bg1"><span class="wz">委托事由</span></td>
				<td  class="td1">
					<textarea rows="10" cols="52"></textarea>
				</td>
			</tr>
			<tr>
				<td width="15%" height="21" class="biao_bg1"><span class="wz">有效期</span></td>
				<td  class="td1">
					<strong:newdate name="search2" id="search2" width="40%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm"></strong:newdate>
						至
					<strong:newdate name="search2" id="search2" width="40%" skin="whyGreen" isicon="true" dateform="yyyy-MM-dd HH:mm"></strong:newdate><br>
					<input type="checkbox" name="ALWAYS_ON" id="ALWAYS_ON" />一直有效
				</td>
			</tr>
			<tr>
				<td colspan="2"  class="td1" align="center">
        			<input type="button" class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  确定"  title="添加委托规则" />&nbsp;&nbsp;&nbsp;&nbsp;
        			<input type="button" icoPath="<%=root%>/images/ico/quxiao.gif" onclick="window.close();" class="input_bg" value="  取消" />
				</td>
			</tr>
		</table>
	</form>
</DIV>	
</body>
</html>