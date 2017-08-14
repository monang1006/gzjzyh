<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<title>界面设置</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK type=text/css rel=stylesheet href="<%=frameroot%>/css/properties_windows_list.css">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/common/common.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/common/search.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.MultiFile.js"></script>
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/upload/jquery.blockUI.js"></script>

	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<script type="text/javascript" language="javascript" src="<%=jsroot%>/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form action="/theme/theme!mySave.action" id="mytable" theme="simple">
				<table width="100%" border="0" cellspacing="0" cellpadding="0" style="vertical-align: top;">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										<strong>界面设置</strong>
									</td>
										<td align="right">
													<table border="0" align="right" cellpadding="00" cellspacing="0">
										            <tr>
										            	<td width="4"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
									                 	<td class="Operation_input" onclick="$('#save').click();">&nbsp;保&nbsp;存&nbsp;设&nbsp;置&nbsp;</td>
									                 	<td width="4"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
								                  		<td width="5"></td>
									                 
									                </tr>
									            </table>
												</td>
												<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<input id="baseId" name="modle.baseId" type="hidden" size="32" value="${modle.baseId}">
							<input id="userId" name="modle.userId" type="hidden" value="${modle.userId}"> 
							<table width="100%" height="10%" border="0" cellpadding="0"
								cellspacing="1" align="center" class="table1">
								<tr>
									<td width="20%" colspan="4" height="21" class="biao_bg1"
										align="center">
										<span class="wz">界面主题与菜单</span>
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">界面主题：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select cssStyle="width: 170px"
											list="#{'灰色-14号字体':'/frame/theme_gray',
												'蓝色-14号字体':'/frame/theme_blue',
												'绿色-14号字体':'/frame/theme_green',
												'红色-14号字体':'/frame/theme_red'}"
											id="modle.baseInterfaceThemes" name="modle.baseInterfaceThemes"
											listKey="value" listValue="key">
										</s:select>
									</td>
								</tr>
								<tr style="display:none">
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz">默认展开菜单：</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<s:select cssStyle="width: 170px"
											list="%{defaultMenuMap}"
											id="modle.baseDefaultStartMenu" name="modle.baseDefaultStartMenu"
											listKey="key" listValue="value" theme="simple">
										</s:select>
									</td>
								</tr>
								<tr>
									<td width="20%" height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;快捷菜单图标：&nbsp;</span>
									</td>
									<s:hidden id="baseMenuIcon" name="modle.baseMenuIcon"></s:hidden>
									<td class="td1" colspan="3" align="left">
										<input type="radio" name="baseMenuIcon" value="0">
										<label for="MENU_IMAGE0" style="cursor:hand">
											每个菜单使用不同图标
										</label>
										<input type="radio" name="baseMenuIcon" value="1">
										<label for="MENU_IMAGE1" style="cursor:hand">
											不显示菜单图标
										</label>
										<input type="radio" name="baseMenuIcon" value="01.gif">
										<img src="<%=root%>/images/ico/01.gif" align=absmiddle>
										&nbsp;
										<input type="radio" name="baseMenuIcon" value="02.gif">
										<img src="<%=root%>/images/ico/02.gif" align=absmiddle>
										&nbsp;
										<input type="radio" name="baseMenuIcon" value="03.gif">
										<img src="<%=root%>/images/ico/03.gif" align=absmiddle>
										&nbsp;
									</td>
								</tr>
							</table>
							<table width="90%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td align="center" valign="middle">
										<table width="27%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td></td>
											</tr>
											
											<tr>
												<td width="40%">
													<input id="save" name="save" type="hidden" class="input_bg" value="保存设置并应用">
												</td>
											</tr>
										
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
		<div id="weather" title="点击城市名称可以更改城市">
			<span> <span id="city"></span> <span id="picture"></span> <span id="desc"></span> <span id="descs"></span> </span>
		</div>
		<script type="text/javascript">
$(document).ready(function(){

  var value=document.getElementById("baseMenuIcon").value;
  $("input[name='baseMenuIcon']").each(function(){	
  		if($(this).val()==value){			
			$(this).attr("checked","checked");
		}
  });
  

  $("#save").click(function(){
  		var k=0;
		var objs = document.getElementsByName("baseMenuIcon");	//菜单图标
		for(var i=0;i<objs.length;i++){
			if(objs[i].checked==true){
				document.getElementById("baseMenuIcon").value = objs[i].value;
				k++;
				break;
			}
		}
		if(k==0){
			alert("请选择菜单图标！");
			return false;
		}else{
			k=0;
		}
		document.getElementById("mytable").submit();
  });

});
</script>
	</body>
</html>
