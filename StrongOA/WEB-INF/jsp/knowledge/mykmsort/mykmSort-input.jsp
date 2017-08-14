<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>

<html>
  <head>
  <%@include file="/common/include/meta.jsp"%>
    <base href="<%=basePath%>">
    
    <title>保存知识分类</title>
    	<LINK href="<%=frameroot%>/css/properties_windows_add.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/service.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></SCRIPT>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript" src="<%=path%>/common/js/commontab/workservice.js"></script>
		<script type="text/javascript">
		function showGo(){
		//window.history.go(-1);
		window.close();
		}
		
		function onSub(){
		var name=$("#mykmSortName").val();
		name=name.replace(/(^\s*)|(\s*$)/g,"");
		
		if(name==null||name=="")
		 {
		    alert("类型标题不可以为空！");
		    return  ;
		 }
		 if(name=="<\%%>"){
		 alert("类型标题不可以输入“<\%%>”！");
		    return  ;
		 }
		 if($("#mykmSortDesc").val().length>= 100){
		    alert("描述请不要超过100字！");
		    return;
		 }
		 //sortFrom.submit();
		 $.getJSON("<%=root%>/knowledge/mykmsort/mykmSort!save.action",
		 		{'model.mykmSortId':$("#mykmSortId").val(),
		 		 'model.mykmSortUser':$("#mykmSortUser").val(),
		 		 'model.mykmSortName':encodeURI($("#mykmSortName").val()),
		 		 'model.mykmSortDesc':encodeURI($("#mykmSortDesc").val())},function(data){
		 		 	var id = data[0].id;
		 		 	var name=data[0].name;
		 		 	var returnValue=id+","+name;
		 		 	window.returnValue=returnValue;
		 		 	window.close();
		 		 });
		}
		
		</script>

  </head>
 <base target="_self"/>
 <body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				style="vertical-align: top;">
				<tr>
					<td height="100%">
						<form id="sortFrom" theme="simple" name="sortFrom" 
							action="<%=root%>/knowledge/mykmsort/mykmSort!save.action" method="POST">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
									<strong>保存知识类型</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="onSub();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="showGo();">&nbsp;返&nbsp;回&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
				                  		<td width="6"></td>
					                </tr>
					            </table>
							</td>
							</tr>
							</table>
								</td>
								</tr>
							</table>
							<input id="mykmSortId" name="model.mykmSortId" type="hidden"
								size="32" value="${model.mykmSortId}">	
							<input id="mykmSortUser" name="model.mykmSortUser" type="hidden"
								size="32" value="${model.mykmSortUser}">	
																	
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz"><FONT color="red">*</FONT>&nbsp;类型名称：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
										<input id="mykmSortName" maxlength="25" name="model.mykmSortName" type="text" size="32" value="${model.mykmSortName}" class="required">
									</td>
								</tr>
								<tr>
									<td  height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">类型说明：&nbsp;</span>
									</td>
									<td class="td1" colspan="3" align="left">
									
									<textarea id="mykmSortDesc"  rows="5" cols="36" maxlength="100" name="model.mykmSortDesc" >${model.mykmSortDesc }</textarea>
									</td>
								</tr>
								<tr>
							<td class="table1_td"></td>
							<td></td>
						</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
