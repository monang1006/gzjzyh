<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage=""%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css" rel=stylesheet />
		<script type="text/javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>工作委托</title>
		<script type="text/javascript">
			function doEntry(obj){
				if(obj.value == "1"){
					//秘书代办
					var Height=600;
					var Width=700;
					var left=(screen.availWidth-10-Width)/2;
					var top=(screen.availHeight-30-Height)/2;
					var Url = "<%=root%>/workflowDelegation/action/processDelegation!input.action?type=add";
					var win = window.open(Url, 'myinfo', 'height=' + Height + ', width=' + Width + ', top='
					+ top + ', left=' + left + ', toolbar=no, '
					+ 'menubar=no, scrollbars=no, resizable=yes,location=no, status=no');
				}
			}
			function saveForm(){
				alert("保存成功！");
				$("#myForm").submit();
			}
		</script>
	</head>

	<body>
		<div style="text-align: center;">
			<s:form id="myForm" action="/myinfo/myInfo!saveWorkEntrust.action" enctype="multipart/form-data">
				<fieldset style="width: 85%;">
					<legend>
						<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9" />
						&nbsp;
						<span class="wz"> 委托设置</span>
					</legend>
					<table width="90%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td>
								<s:radio onclick="doEntry(this);" name="model.mailnum"
									list="#{'0':'允许主办人员代录意见','1':'委托秘书代办','3':'委托主办人员代办','2':'取消代办'}"></s:radio>
							</td>
						</tr>
					</table>
				</fieldset>
				<table width="80%" style="alignment-adjust: central">
					<tr>
						<td style="width: 20%">
							&nbsp;
						</td>
						<td>
							<input type="hidden" id="prsnId" name="prsnId" value="${model.prsnId }" />
							<input id="save" type="button" onclick="saveForm()" class="input_bg" value="保存" />
						</td>
						<td>
							<input id="reset" type="reset" class="input_bg" value="重置" />
						</td>
						<td style="width: 20%">
							&nbsp;
						</td>
					</tr>
				</table>
			</s:form>
		</div>
	</body>
</html>