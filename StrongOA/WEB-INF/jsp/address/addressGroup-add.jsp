<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<title>保存联系组</title>
		<%@include file="/common/include/meta.jsp" %>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/btn.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
					//添加分组
					$("#btnOK").click(function(){
						var groupName = $.trim($("#groupName").val());
						var groupDesc = $.trim($("#groupDesc").html());
					
						if(groupName == ""){
							alert("联系组名称不能为空。");
							$("#groupName").focus();
							return false;
						}
						if( groupDesc != ""){
							if(groupDesc.length>256){
								$("#groupDesc").html($("#groupDesc").html().substring(0,256))
								alert("描述内容过长，请控制在256个字以内。");
								$("#groupDesc").focus();
								return false;
							}
						}
						$.post("<%=root%>/address/addressGroup!checkIsSysGroup.action",
							{groupName:encodeURI(groupName)},
							function(data){
								if("error" == data){
									alert("对不起，出错了。请与管理员联系。");
								}else if("yes" == data){
									alert("名称与系统分配的组重复，请选择其他的组名。");
								}else if("no" == data){
									var url = $("form").attr("action");
									var id= $("#groupId").val();
									if(id != ""){
										url += "?model.addrGroupId="+id;
									}
									$.ajax({
										type:"post",
										url:url,
										data:{
											"model.addrGroupName":groupName,
											"model.addrGroupRemark":groupDesc
										},
										success:function(data){
											if(data=="exist"){
												alert("对不起，组名已存在。");
											}else{
											    window.returnValue="suc";
											}
											window.close();
										},
										error:function(){
											alert("对不起，操作出错。");
											window.returnValue="fail";
											window.close();
										}
									});
								}
							}
						);
						
					});
					//关闭窗口
					$("#btnCancel").click(function(){window.close();});
			
				
			});
		</script>
	</head>
<base target="_self"/>	  
<body oncontextmenu="return false;">
	<div align="center" style="height:100%;width:100%;overflow: auto;">
		<form action="<%=root %>/address/addressGroup!save.action" method="post">
			<input type=hidden id="groupId" value="${id }"/>
			<table align="center" width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
				<tr >
				<td colspan="3" class="table_headtd">
				<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td class="table_headtd_img" >
									<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										<strong>保存联系组</strong>
									</td>
									<td align="right">
				        				<table border="0" align="right" cellpadding="00" cellspacing="0">
											                <tr>
																<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
											                 	<td class="Operation_input" onclick="$('#btnOK').click();" >&nbsp;保&nbsp;存&nbsp;</td>
											                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
										                  		<td width="5"></td>
										                  		<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
											                 	<td class="Operation_input1" onclick="window.close();">&nbsp;取&nbsp;消&nbsp;</td>
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
				<td width="25%" height="21" class="biao_bg1" align="right"><span class="wz"><font color='red'>*</font>&nbsp;联系组名称：&nbsp;</span></td>
				<td  class="td1">
					<input type="text" id="groupName" value="${model.addrGroupName}" maxlength="7" size="41"/>
					<input type="text" style="display: none;"/><!-- 此隐藏域只是为了防止上个文本框按回车后提交了 -->
				</td>
			</tr>
			<tr>
				<td width="25%" height="21" class="biao_bg1" align="right"><span class="wz">描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述：&nbsp;</span></td>
				<td  class="td1">
					<textarea id="groupDesc" rows="5" cols="32">${model.addrGroupRemark }</textarea>
				</td>
			</tr>
			 <tr>
				<td colspan="2"  class="td1" align="center">
        			<input id="btnOK" type="hidden" class="input_bg" icoPath="<%=root%>/images/ico/queding.gif" value="  保存" />&nbsp;&nbsp;&nbsp;&nbsp;
        			<input id="btnCancel" type="hidden" icoPath="<%=root%>/images/ico/quxiao.gif" class="input_bg" value="  取消" />
				</td>
			</tr>
		</table>
	</form>
	</div>
</body>
</html>