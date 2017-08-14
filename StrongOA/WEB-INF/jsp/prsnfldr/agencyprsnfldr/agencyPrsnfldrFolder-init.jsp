<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>机构文件柜-新建文件夹</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/oa/js/prsnfldr/prsnfldr.js" type="text/javascript"></script>
		<script type="text/javascript">
			
		$(document).ready(function(){
			var isTrue='${isTrue}';
			if(isTrue==1){
				returnValue="reload";
				window.close();
			}
		});
		
		//校验文件夹名称是否已经使用
			function checkFolderNameIsUsed(){
				var folderName = $.trim($("#folderName").val());//得到文件夹输入域值，同时去掉空格.
				var parentFolder = $("#parentFolder").val();//当前文件夹所在父文件夹.
				if(folderName == ""){
					alert("请输入文件夹名称。");
					return ;
				}else{
					if(folderName.indexOf("<")!=-1 || folderName.indexOf(">")!=-1){
						alert("文件夹名称中含特殊字符，拒绝输入。");
						return ;
					}
					if(folderName.length>64){
						alert("文件夹名称输入过长。");
						return ;
					}
				}
				$.post("<%=root%>/prsnfldr/agencyprsnfldr/agencyPrsnfldrFolder!checkFolderName.action",
					  {"model.folderName":folderName,"model.folderParentId":parentFolder},
					  function(ret){
					  	if(ret == "1"){
					  		alert("名称已存在。");
					  		return ;
					  	}else if(ret == "0"){
					  		$("#folderName").val(folderName);
					  		$("form").submit();
					  	}else{
					  		alert(ret);//异常
					  	}
					  });
				
			}
		</script>
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;">
<div style="height:100%;overflow: auto;">
	<label id="l_actionMessage" style="display: none;"><s:actionmessage/></label>
		<form action="<%=root%>/prsnfldr/agencyprsnfldr/agencyPrsnfldrFolder!save.action" method="post">
			<input id="parentFolder" name="model.folderParentId" type="hidden" value="${folderId }"/>
			<%--<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
			        <td colspan="2" style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);"><img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;新建文件夹</td>
				</tr>
			--%>
			
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td >
							<strong>新建文件夹</strong>
						    </td>
			
			<td align="right">
								<table border="0" align="right" cellpadding="0" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="checkFolderNameIsUsed();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
                                           <td>&nbsp;
												
												</td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
					<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
				<td width="30%" height="21" class="biao_bg1" align="right"><span class="wz"><font color='red'>*</font> 文件夹名称：</span></td>
				<td  class="td1">
					<input id="folderName" type="text" name="model.folderName" maxLength="32" size="15" style="width: 52%;"/>
				</td>
			<tr>	
			</tr>
			<tr>
				<td width="30%" height="21" class="biao_bg1" align="right"><span class="wz">文件夹位置：</span></td>
				<td  class="td1">
					<input type="radio" name="model.folderSort" checked="checked" value="last">最后<input name="model.folderSort" type="radio" value="first">最前
				</td>
			</tr>
			<tr>
				<td class="table_td"></td>
				<td></td>
				</tr>
			
		</table>
	</td>
	
	</tr>
	
	</table>
	
	</form>
</div>	
</body>
</html>