<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp" %>
<html>
	<head>
		<title>查看用户信息</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<link type="text/css" rel="stylesheet" href="<%=path%>/common/js/tabpane/css/luna/tab.css" />
		<script type="text/javascript" src="<%=path%>/common/js/tabpane/js/tabpane.js"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$("input:text").each(function(){
					if($(this).val() == ""){
						$(this).parent().parent().hide();
					}
				});
			});
		</script>
	</head>
	<body class="contentbodymargin">
	<DIV id=contentborder align=center>
		<div align=left style="width: 100%;padding:5px;">
					<tr>
					<td colspan="3" class="table_headtd">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>查看用户信息</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
				                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				</div>
		<fieldset style="width: 95%">
					<legend>
						<span class="wz">基本资料 </span>
					</legend>
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
						>
					<tr >
									          <td width="20%" align="right" height="21" class="biao_bg1"><span class="wz">用户名：&nbsp;</span> </td>
									          <td class="td1">
									          	<s:textfield cssClass="wztext" name="model.userLoginName"  readonly="true" cssStyle="width: 90%;"></s:textfield>
									         </td>
									    </tr>
									    <tr>
									          <td width="20%" align="right" height="21" class="biao_bg1"><span class="wz">姓&nbsp;&nbsp;名：&nbsp;</span> </td>
									          <td class="td1">
									            <s:textfield cssClass="wztext" name="model.userName" readonly="true"  cssStyle="width: 90%;"></s:textfield>
									         </td>
									    </tr>
									    <tr>     
									          <td width="20%" align="right" height="21" class="biao_bg1"><span class="wz">性&nbsp;&nbsp;别：&nbsp;</span> </td>
									          <td class="td1">
									            <s:textfield cssClass="wztext" name="model.sex" readonly="true"  cssStyle="width: 90%;"></s:textfield>
									         </td>
									         
									    </tr>
									    <tr>     
									          <td width="20%" align="right" valign="top" height="21" class="biao_bg1"><span class="wz">部&nbsp;&nbsp;门：&nbsp;</span> </td>
									          <td class="td1">
									            <s:textarea cssClass="wztext"  name="model.deptName" cssStyle="width:90%;height:63px;" readonly="true"></s:textarea>
									         </td>
									    </tr>
									    <tr height="5">
									    	<td></td><td></td>
									    </tr>
								    </table>
								    </td>
								    </tr>
								    </table>
								    </fieldset>
				<fieldset style="width: 95%">
					<legend>
						<span class="wz">联系方式 </span>
					</legend>  
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
						>
					<tr >
								          <td width="20%" align="right" height="21" class="biao_bg1"><span class="wz">电子邮件：</span> </td>
								          <td class="td1">
								            <s:textfield cssClass="wztext" name="model.email" readonly="true" cssStyle="width: 90%;"></s:textfield>
								         </td>
								    </tr>  
								    <tr>  
								          <td width="20%" align="right" height="21" class="biao_bg1"><span class="wz">联系地址：</span> </td>
								          <td class="td1">
								            <s:textfield cssClass="wztext" name="model.address" readonly="true" cssStyle="width: 90%;"></s:textfield>
								         </td>
								         
								    </tr>
								        
								    <tr>
								          <td width="20%" align="right" width="12%" height="21" class="biao_bg1"><span class="wz">电话：</span> </td>
								          <td class="td1">
								            <s:textfield cssClass="wztext" name="model.tel" readonly="true" cssStyle="width: 90%;"></s:textfield>
								         </td>
								   </tr>      
								   <tr>      
								          <td width="20%" align="right" width="12%" height="21" class="biao_bg1"><span class="wz">手机：</span> </td>
								          <td class="td1">
								            <s:textfield cssClass="wztext" name="model.mobile" readonly="true" cssStyle="width: 90%;"></s:textfield>
								         </td>
								    </tr>
								    <tr height="0">
								    	<td></td><td></td>
								    </tr>
								        
								    </table>
								    </fieldset>
								    
		<!--  		<table cellspacing="1" cellpadding="0" border="0" style="position: absolute; top: 240px; right: 10px;">
					<tr>
						<td width="100%"></td>
						<td><a id="input_bg"  href="#" class="button" onclick="JavaScript:window.close();">关 闭</a></td>
					</tr>
				</table>-->
		</DIV>
	</body>

</html>
