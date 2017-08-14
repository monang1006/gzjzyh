<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.strongit.oa.bgt.senddoc.DocManager" />
<jsp:directive.page import="com.strongmvc.service.ServiceLocator" />
<jsp:directive.page import="com.strongit.oa.bo.ToaAttachment" />
<jsp:directive.page import="com.strongit.oa.bgt.model.ToaYjzx" />
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>意见征询反馈</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript" src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript" src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
			function doSubmit(){
				var isSelectFile = false;
			     $("input:file.multi").each(function(){
			     	if($(this).val()!=""){
			     		isSelectFile = true;
			     	}
			     });
			     if(!isSelectFile){
			     	if(!confirm("您没有选择意见征询反馈文件，确定要继续操作码？")){
			     		return ;
			     	}
			     }
			     $("form").submit();
				$("body").mask("操作处理中,请稍候...");
			}
			function callback(msg){
			 $("body").unmask();	
			 if(msg == "true"){
			 	//success
			 	window.returnValue = "ok";
			 	window.close();
			 } else if(msg == "false"){
			 	//error
			 	alert("对不起，系统出现错误，请与管理员联系。");
			 }
			}
			function deldbobj(id){
				ischange = true;
				var obj=document.getElementById(id);
				var value=document.getElementById(id).value;
				obj.value=value+";";
				$("#div"+id).hide();
			}
		</script>
	</head>
	<body class=contentbodymargin>
		<script type="text/javascript" src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id=contentborder align=center>
			<s:form action="/bgt/senddoc/sendDoc!saveYjfk.action" onsubmit="return false;" method="post"
				enctype="multipart/form-data" target="myIframe">
				<s:hidden name="model.id"></s:hidden>
				<s:hidden name="model.instanceId"></s:hidden>
				<fieldset style="width: 90%">
					<legend>
						<span class="wz">意见征询反馈信息 </span>
					</legend>
					<table width="100%" height="80%" border="0" cellpadding="0" cellspacing="1" class="table1">
						<tr>
							<td width="20%" height="21" class="biao_bg1">
								<span class="wz">附件(<font color=red>*</font>)：</span>
							</td>
							<td width="70%" class="td1">
								<input type="file" accept="application/png|jpg" onkeydown="return false;" id="file" name="file" class="multi required"
									style="width: 100%;" />
									(<span style="color:red">只能上传.png和.jpg类型的图片文件</span>)
								<%
									DocManager docManager = (DocManager) ServiceLocator.getService("docManager");
										ToaYjzx model = (ToaYjzx) request.getAttribute("model");
										String id = model.getId();//request.getParameter("model.id");//得到意见征询记录id
										List<ToaAttachment> attachList = docManager.getAttachmentsWithoutContent(id);
										if (attachList != null && !attachList.isEmpty()) {
											for (ToaAttachment attachment : attachList) {
												out.println("<div id=div" + attachment.getAttachId() + ">");
												out.println("[<a onclick=\"deldbobj('" + attachment.getAttachId() + "');\"");
												out.println("href=\"#\">删除</a>]");
												out.println(attachment.getAttachName());
												out.println("</div>");
												out.println("<input value=" + attachment.getAttachId() + " id=" + attachment.getAttachId()
														+ " type=\"hidden\" ");
												out.println(" name=\"dbobj\" />");
											}
										}
								%>
							</td>
						</tr>
					</table>
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td align="center" valign="middle">
								<table width="80%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td width="30%" height="34">
											&nbsp;
										</td>

										<td width="10%">
											<input id="save" name="save" onclick="doSubmit();" type="submit" class="input_bg"
												value="确定">
										</td>
										<td width="20%">
											&nbsp;&nbsp;
										</td>
										<td width="10%">
											<input name="button" type="button" onclick="window.close();" class="input_bg" value="关闭">
										</td>
										<td width="30%">
											&nbsp;&nbsp;
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</fieldset>
			</s:form>
		</div>
		<iframe name="myIframe" style="display: none"></iframe>
	</body>
</html>
