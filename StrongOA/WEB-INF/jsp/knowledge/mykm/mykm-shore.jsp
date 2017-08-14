<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/tags/web-flex" prefix="webflex"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/tags/web-remind" prefix="strong"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<html>
  <head>
  <%@include file="/common/include/meta.jsp"%>
    <base href="<%=basePath%>">
    
    <title>知识分享</title>
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
			window.history.go(-1);
			window.close();
		}
		
		function onSub(){
		if($("#mykmId").val()==null||$("#mykmId").val()=="")
		 {
		
		 }
		 if($("#orgusername").val()==""||$("#orguserid").val()==""){
		   alert("请先选择分享人！");
		   return;
		 }
		 if($("#mydoce").val().length>200){
		 alert("描述请不要超过200字！");
		 return;
		 }
		 getRemindValue();
		 sortFrom.submit();
		}
		//选择人员
		function addPerson(){
			var ret=OpenWindow("<%=root%>/address/addressOrg!tree.action","600","400",window);
		}
		//清空分享人员
		function clearPerson(){
			$("#orgusername").val("");
			$("#orguserid").val("");
		}
		 //获取提醒方式
			function getRemindValue(){
				var returnValue = "";
				$("#StrRem").find("input:checkbox:checked").each(function(){
					returnValue = returnValue + $(this).val() + ",";
				});
				if(returnValue!=""){
					returnValue = returnValue.substring(0,returnValue.length-1);
				}
				$("#mode").val(returnValue);
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
							action="<%=root%>/knowledge/mykm/mykm!saveShore.action" method="POST">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
								<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>知识分享</strong>
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
							<input id="mykmUrl" name="model.mykmUrl" type="hidden"
								size="32" value="${model.mykmUrl}">	
							<input id="mykmUser" name="model.mykmUser" type="hidden"
								size="32" value="${model.mykmUser}">	
								<input type="hidden" name="mode" id="mode">
									<input id="mykmId" name="model.mykmId" type="hidden" value="${model.mykmId }">								
							<table width="100%" height="10%" border="0" cellpadding="0" cellspacing="1" align="center" class="table1">
								<tr>
									<td height="21" class="biao_bg1" align="right">
										<span class="wz">分享方式：&nbsp;</span>								</td>
									<td class="td1" id="StrRem"  align="left">
									
									<strong:remind msgChecked="checked" exceptRemind="SMS" isOnlyRemindInfo="true" code="<%=GlobalBaseData.SMSCODE_KNOWLEDGE %>"/>
									</td>
								</tr>
								
								<tr>
									<td  height="21" class="biao_bg1" valign="top" align="right">
										<span class="wz">分享人员：&nbsp;</span>							</td>
									<td class="td1" align="left">
									<s:textarea title="双击选择接收人" cols="20" id="orgusername" name="receiverNames" ondblclick="addPerson()"  rows="4" readonly="true"></s:textarea>
										<input type="hidden" id="orguserid" name="recvUserIds" value="${recvUserIds}"></input>
<%--										<a id="addPerson" href="#" class="button" onclick="addPerson();">添加</a>--%>
<%--										<a id="clear" href="#" class="button" onclick="clearPerson();">清空</a>--%>
									<a class="button" href="JavaScript:addPerson();">添加</a>&nbsp;<a class="button" href="JavaScript:clearPerson();">清空</a>
									</td>
								</tr>
								
							<tr><td height="21" class="biao_bg1" align="right">	
								描&nbsp;&nbsp;&nbsp;&nbsp;述：&nbsp;</span>
								</td><td>
								<textarea  rows="5" cols="40" id="mydoce" name="mytitle" maxlength="200"  class="required"></textarea>
								</td></tr>
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
