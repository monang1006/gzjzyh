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
	<!--  引用公共样式文件,建议所有样式都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的CSS文件夹下-->
	<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
	<!--  引用公共及自定义js文件,建议js都以文件方式定义在jsp文件外部,通常定义在WebRoot目录下的js文件夹下-->
	<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/validate/formvalidate.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.MultiFile.js" type="text/javascript"></script>
	<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>
	<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>	
		<title>保存JSP列表</title>
		
		<style type="text/css">
		body{
			width:100%;
			margin : 0px;
			height: 100%
		}
		#inHtml{
			CURSOR: default; 
			width: 300px;
			height: 100px;
		}
		#setAllUser{ border:none;}
		</style>
		<script language="javascript">
		var recvuserId = "${msgReceiverIds}";
		$(document).ready(function(){
		
		});
		
		 function save(){
			 var id= $("#id").val();
			 var name= $("#name").val();
			 	if($.trim($("#name").val())==""){
			 		alert("表单名称不能为空。");
			 		return ;
			 	}
			 	if($.trim($("#url").val())==""){
			 		alert("URL配置地址不能为空。");
			 		return ;
			 	}
				document.getElementById("addForm").submit();
				returnValue="reload"
			 }
		
		</script>
	</head>
	<base target="_self"/>
	<body>
	<DIV id=contentborder align=center>
	<s:form action="/eformJspManager/eformJspManager!save.action" name="addForm" id="addForm" method="post" enctype="multipart/form-data">
	<input type="hidden" name="model.id" value="${model.id }">
	<input type="hidden" name="model.createtime" value="${model.createtime }">
	<label id="l_actionMessage" style="display:none;"><s:actionmessage/></label>
	<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
			<table width="100%" border="0" cellspacing="0" cellpadding="00">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
							<script>
											var id = "${model.id}";
											if(id==null|id==""){
												window.document.write("<strong>新建Jsp表单</strong>");
											}else{
												window.document.write("<strong>编辑Jsp表单</strong>");
											}
							</script>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onclick="save();">&nbsp;保&nbsp;存&nbsp;</td>
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
					<table  border="0" cellpadding="0" cellspacing="0" class="table1" width="100%" >
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;表单名称：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="name" name="model.name" type="text"  required="true" style="background-color:#ffffff"
											 size="45" maxlength="30" value="${model.name }">
											 
								&nbsp;
							</td>
						</tr>
						<tr>
							<td class="biao_bg1" align="right">
								<span class="wz"><font color=red>*</font>&nbsp;URL配置地址：&nbsp;</span>
							</td>
							<td class="td1" style="padding-left:5px;">
								&nbsp;<input id="url" name="model.url" type="text"  required="true" style="background-color:#ffffff"
											 size="45" maxlength="45"  value="${model.url }">
											 
								&nbsp;
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
		</s:form>
		</DIV>
	</body>

</html>
