<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp" %>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
	<%@include file="/common/include/meta.jsp" %>
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		
		<title>发送短信</title>
		<style type="text/css">
<!--
body {
	margin:0px;
	height: 100%
}
</style>
		<script>
		String.prototype.trim = function() {
                var strTrim = this.replace(/(^\s*)|(\s*$)/g, "");
                strTrim = strTrim.replace(/^[\s　\t]+|[\s　\t]+$/, ""); 
                return strTrim;
            }
		
		$(document).ready(function() {
			//选择接收人
			$("#addPerson").click(function(){
				var ret=OpenWindow(this.url,"600","400",window);
			});
			
			//清空接收人
			$("#clearPerson").click(function(){
				$("#orgusername").val("");
				$("#orguserid").val("");
			});
		});
		 
		 function save(){
			var ids = $("#orguserid").val();
			if(ids.length==0){
				alert("请选择用户！");
				return;
			}
	 		var url = "<%=path%>/smscontrol/smscontrol!save.action";
	 		if(confirm("增加已选用户具有短信发送权限，确定？")){
				$.ajax({
					type:"post",
					url:url,
					data:{
						userIds:ids
					},
					success:function(data){
						if(data!="" && data!=null){
							alert(data);					
						}else{
							returnValue ="reload";
	  						window.close();
						}
					},
					error:function(data){
						alert("对不起，操作异常"+data);
					}
			    });
	 		}
		 }
		 
		</script>
	</head>
	<base target="_self"/>
	<body class="contentbodymargin" >
	<input type="submit" id="submit" name="submit" value="" style="display: none">
	<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center style="margin: 0px;">
			<s:form action="smscontrol!save.action" theme="simple" name="smsForm" id="smsForm">
				<%--<table border="0" width="100%" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<table border="0" width="100%" cellpadding="2" cellspacing="1"
								align="center">
								<tr>
									<td height="30" colspan="2"
										style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
										<table width="100%" border="0" cellspacing="0" cellpadding="00">
											<tr>
												<td >
												&nbsp;
												</td>
												<td width="227">
													<img src="<%=frameroot%>/images/ico/ico.gif" width="9"
														height="9">&nbsp;
													添加用户权限
												</td>
												<td>
												</td>
												<td width="290">
												</td>
											</tr>
										</table>
									</td>
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
							<td align="left">
							<strong>添加用户权限</strong>
							</td>
					<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;确&nbsp;定&nbsp;</td>
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
									<td>
									<table  border="0" cellpadding="0" cellspacing="1" class="table1" width="100%" >
									<tr>
										<td width="25%" height="25" class="biao_bg1" align="right">
											<span class="wz"><font color=red>*</font> 选择用户：</span>
										</td>
										<td class="td1">
										<s:textarea title="双击选择用户" cols="30" id="orgusername" name="userNames" ondblclick="addPerson.click();"  rows="4" readonly="true"></s:textarea>
										<input type="hidden" id="orguserid" name="userIds" value="${userIds}"></input>
										
										<a id="addPerson" class = "button"url="<%=root%>/address/addressOrg!tree.action" href="#">添加人员</a>&nbsp;
										<a id="clearPerson" class = "button" href="#">清空</a>
										</td>
									</tr>
										<tr>
				                  <td class="table_td"></td>
				                  <td></td>
				                   </tr>
									</table>
									</td>
								</tr>
							
						
					<%--<tr align="center">
						<td>
							<input type="button" class="input_bg" value="确定" onclick="save();"/>
							<input type="button" class="input_bg" value="取消" onclick="window.close();" />
						</td>
					</tr>
				--%>
				
				</table>
			</s:form>
		</DIV>
	</body>

</html>
