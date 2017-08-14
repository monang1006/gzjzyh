<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存视图模型</title>
		
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript" language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script type="text/javascript">
			function save(){
				//alert("model.foramulaId="+$("#foramulaId").val()+"&model.foramulaDec="+$("#foramulaDec").val()+"&model.foramulaDefault="+$('input[@name=model.foramulaDefault][@checked]').val());
				if($.trim($("#foramulaDec").val())==''){
					alert("模型描述为必填字段，请填写。");
					$("#foramulaDec").focus();
				}else{
					$.ajax({
						type:"post",
						dataType:"text",
						url:"<%=root%>/viewmodel/viewModel!save.action",
						data:"model.foramulaId="+$("#foramulaId").val()+"&model.foramulaDec="+encodeURIComponent(encodeURIComponent($("#foramulaDec").val()))+"&model.isCreatePage="+$("#isCreatePage").val()+"&model.createTime="+$("#createTime").val()+"&model.foramulaDefault="+$('input[@name=model.foramulaDefault][@checked]').val(),
						success:function(msg){
							if(msg=="true"){
								window.returnValue="true";
								window.close();
							}else{
								alert("信息保存失败，请您从新操作。");
							}
						}
					});
				}
			}
			
			function cancle(){
				window.close();
			}
		</script>
	</head>
	<body class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
			<input id="foramulaId" name="model.foramulaId" type="hidden" value="${model.foramulaId }" >
			<input id="isCreatePage" name="model.isCreatePage" type="hidden" value="${model.isCreatePage }" >
			<input id="createTime" name="model.createTime" type="hidden" value="${model.createTime}" >
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							 <s:if test="model.foramulaId==null">
							   <strong>新建视图模型</strong>
							 </s:if>
							<s:else>
							    <strong>编辑视图模型</strong>
							</s:else>	
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onclick="cancle();">&nbsp;取&nbsp;消&nbsp;</td>
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
						<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table1">
							<tr>
								<td  class="biao_bg1" align="right">
									<span class="wz"><font color=red>*</font>&nbsp;页面模型描述：</span>
								</td>
								<td class="td1" style="padding-left:5px;">
									<input type="text" id="foramulaDec" name="model.foramulaDec" value="${model.foramulaDec }"  maxlength=40 size="40">
								</td>
							</tr>
							<tr>
								<td class="biao_bg1" align="right">
									<span class="wz">是否为默认模型：</span>
								</td>
								<td class="td1" style="padding-left:5px;">
									<s:if test="model.foramulaDefault==1">
										<s:radio id="foramulaDefault" disabled="true" name="model.foramulaDefault" list="#{'0':'非默认模型' , '1':'默认模型' }" listKey="key" listValue="value" />
									</s:if>
									<s:else>
										<s:radio id="foramulaDefault" name="model.foramulaDefault" list="#{'0':'非默认模型' , '1':'默认模型' }" listKey="key" listValue="value" />
									</s:else>
									
								</td>
							</tr>
							<tr>
								<td class="table1_td"></td>
								<td></td>
					       </tr>
						</table>
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>
