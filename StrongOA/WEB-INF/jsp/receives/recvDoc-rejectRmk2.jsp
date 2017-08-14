<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@include file="/common/include/meta.jsp"%>

<html>
	<head>
		<title>拒收公文</title>
		<link rel="stylesheet" type="text/css"
			href="<%=frameroot%>/css/properties_windows.css">

		<script src="<%=path%>/oa/js/calendar/spinelz_lib/prototype.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/oa/js/recvdoc/multiFile.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>

		<style type="text/css">
table,tr,td,div {
	margin: 0px;
}
</style>
<%
	String reId = request.getParameter("senddocId");
	
 %>
		<script type="text/javascript">		
	
	//关闭窗口
		function windowclose(){
			window.close(); 
		}
		//清空
		function clearDoc(){ 
		//alert(document.getElementById("senddocId").value);
			$("#docRecvRemark").val("");
			  $("#docRecvRemark").focus();
		//document.getElementById("senddocId").value = ""; 
		} 
         
		//提交
		function save(){ 
			var groupName = $.trim($("#docRecvRemark").val()); 
		    /*
			if(groupName == ""){
				alert("拒收原因不能为空！");
				$("#docRecvRemark").focus();
				return false;
			}
			*/
			if(groupName.length > 2000)
			 {
			  alert("输入的拒收原因长度不能超过2000个字符!");
			  $("#docRecvRemark").focus();
			  return false;
			 }
			document.getElementById("recvState").value = "2";
			document.getElementById("myTable").submit();
		}
		
</script>
	</head>
	<base target="_self">
	<body >
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV align=center>
			<s:form action="/receives/recvDoc!save.action" id="myTable"
				enctype="multipart/form-data" theme="simple">
				<input type="hidden" id="senddocId" name="senddocId"
					value="<%=reId%>" >
				<input type="hidden" id="recvState" name="model.recvState"
					value="${model.recvState}">
			<input type="hidden" id="showClose" name="showClose" value="<%=request.getParameter("showClose")%>">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td height="40"
							style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td width="34" align="center">
										<img src="<%=frameroot%>/images/ico.gif" width="9" height="9">
									</td>
									<td width="300">
										<font color='red'>确定要拒收公文，请填写拒收原因：</font>
									</td>
									<td>
										&nbsp;
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>

				<table border="0" cellpadding="0" cellspacing="1" width="100%" class=biao_bg1
					height="100%">
					<tr>
						<td valign="top" align="left" width="70%">
							<table border="0" cellpadding="0" cellspacing="1" class="table1"
								width="100%">
								<tr>
									<td >
										&nbsp;
									</td> 
									<td class="td1" width="100%" colspan="2" > 
										<s:textarea title="请输入拒收原因" cols="50" id="docRecvRemark"
										name="docRecvRemark" rows="14" readonly="false" value="已拒收！"
										onKeyDown='if (this.value.length>=2000){event.returnValue=false}'></s:textarea>
									</td>
								</tr>
								<tr align="center" class=biao_bg1>
									<td colspan="3" nowrap>
										<input type="button" value="确定" class="input_bg" 
											onclick="save();">
										&nbsp;&nbsp;
										<input type="button" value="清空" class="input_bg"
											onclick="clearDoc();">
										&nbsp;&nbsp;
										<input type="button" value="关闭" class="input_bg"
											onclick="windowclose();">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</s:form>
		</DIV>
	</body>
</html>