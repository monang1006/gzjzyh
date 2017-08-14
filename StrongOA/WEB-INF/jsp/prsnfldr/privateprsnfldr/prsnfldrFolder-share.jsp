<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<title>个人文件柜-共享文件夹</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/checkboxvalidate.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				var folderProp = '<s:property value="folderProp" />';
				var folderProps = folderProp.split(",");
				if(null!=folderProp && folderProp!= ""){
					$("input:radio").each(function(){
						var rval = $(this).val();
						for(var i=0;i<folderProps.length;i++){
							if(rval == folderProps[i]){
								$(this).attr("checked",true);
							}
						}	
					});
				}
				if($("#orguserid").val()=='allPeople'||$("#orgusername").val()=='所有人'){
								$("#chk_isShareAllPeople").attr("checked",true);
						}
				 $("#chk_isShare").click(function(){
					if($("#chk_isShare").attr("checked")){
						$("#chk_isShareAllPeople").attr("disabled","");
						$("#addPerson").attr("disabled","");
					}else{
						$("#chk_isShareAllPeople").attr("checked",false);
						$("#chk_isShareAllPeople").attr("disabled","disabled");
						$("#orguserid").val("");
						$("#orgusername").val("");
					}
				});
					 //选中所有人
				 $("#chk_isShareAllPeople").click(function(){
				 if($("#chk_isShareAllPeople").attr("checked")){
						if(confirm("您确定共享给所有人吗？")==true){
							$("#orguserid").val("allPeople");
							$("#orgusername").val("所有人");
						}else{
							$("#chk_isShareAllPeople").attr("checked",false);
						}
					}else{
						$("#orguserid").val("");
						$("#orgusername").val("");
					}
				 });
			});
		
		</script>
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;">
<div style="height:100%;overflow: auto;">
	<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
		<form action="<%=root%>/prsnfldr/privateprsnfldr/prsnfldrFolder!share.action" method="post">
			<input id="folderId" name="folderId" type="hidden" value="${folderId }"/>
			<input id="isShare" name="model.isShare" type="hidden" value="${ model.isShare}" />
			<%--<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;&nbsp;&nbsp;&nbsp;共享文件夹</td>
				</tr>--%>
				
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td >
							<strong>共享文件夹</strong>
						    </td>
						    
							
							<td align="right">
								<table border="0" align="right" cellpadding="0" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="$('#btnOK').click();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
                                           <td>&nbsp;
												
												</td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="$('#btnCancel').click();">&nbsp;取&nbsp;消&nbsp;</td>
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
			<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
				<tr>
				<td width="25%" height="21" align="center" class="td1">
								<img src="<%=path %>/oa/image/mymail/network.bmp"/>
							</td>
							<td  class="td1">
								<input id="chk_isShare" type="checkbox" checked="checked" name="isShare" value="isShare"/>共享此文件夹
								<input id="chk_isShareAllPeople" type="checkbox"  name="chk_isShareAllPeople" value="1"/>共享给所有人
							</td>
						</tr>
						<tr>
							<td width="25%" height="21" align="right"  valign="top" >
								共享范围：
							</td>
							<td  style="background-color: white;" class="td1">
								<s:textarea cols="30" id="orgusername" name="userName" rows="4" readonly="true"></s:textarea>
								<s:hidden id="orguserid" name="userId"></s:hidden>
								</td>
								</tr>
							<tr>
								<td>
								</td>
								<td align="left">
								<a id="addPerson"class="button" url="<%=root%>/address/addressOrg!tree.action?isShowAllUser=1" href="#">选择人员</a>&nbsp;
							
							</td>
							</tr>
						
						
						<tr>
							<td width="25%" height="21" align="right"class="td1">
								属&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;性：
							</td>
							<td style="background-color: white;">
								<input id="chk_readonly" type="radio" name="folderProp" checked="checked" value="readonly"/>只读
								<input id="chk_readwrite" type="radio" name="folderProp" value="readwrite"/>读写
							</td>
						</tr>
						<tr>
							<td width="25%" height="21" align="right"class="td1">
								创建时间：
							</td>
							<td style="background-color: white;">
								&nbsp;&nbsp;<s:date name="model.folderCreateDatetime" format="yyyy-MM-dd HH:mm:ss"/>
							</td>
						</tr>
						<tr>
							<td colspan="2"  class="td1" align="right">
			        			<input id="btnOK" type="hidden" class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  确定" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			        			<input id="btnCancel" type="hidden" icoPath="<%=root%>/images/ico/quxiao.gif" class="input_bg" value="  取消" />
							</td>
						</tr>
			</table>
			</td>
			</tr>
		</table>
	</form>
</div>	
</body>
</html>