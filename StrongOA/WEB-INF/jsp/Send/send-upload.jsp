<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title>上传正文</title>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/windows.css" type=text/css
			rel=stylesheet>
		<link href="<%=frameroot%>/css/properties_windows.css" type="text/css"
			rel=stylesheet>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/common/js/loadmask/jquery.loadmask.css" />
		<script language="javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/loadmask/jquery.loadmask.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.MultiFile.js"></script>
		<script language="javascript"
			src="<%=path%>/common/js/upload/jquery.blockUI.js"></script>
		<script type="text/javascript">
			//去掉左空格
			function ltrim(s){ 
					return s.replace(/(^\s*)/g, "");
 			} 
 			
 			//去右空格; 
			function rtrim(s){ 
  				return s.replace(/(\s*$)/g, "");
 			} 
			
			//去掉字符串的左右空格
			function trim(s){ 
 				return rtrim(ltrim(s)); 
 			} 
		
			function doSubmit(){
				var attachCount =  $("#MultiFile1_wrap_labels DIV").length;//附件总数
				if(attachCount>1){
					alert("只能上传一个正文!");
					return false;
				}else{
					if(attachCount == 0){
						alert("您没有上传正文,请先上传正文!");
						return false;
						/*
						*if(confirm("您没有上传正文，是否继续提交数据？")){
						*	return true;
						*}else{
						*	return false;
						*}
						*/
					}
				}
				$("body").mask("操作处理中,请稍候...");
				return true;
			}
			
			
			function callback(msg){
				 $("body").unmask();	
				 window.returnValue = msg;
				 window.close();
			}
		</script>
	</head>
	<body class=contentbodymargin>
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<div id=contentborder align=center>
			<fieldset style="width: 94%;">
				<legend>

				</legend>
				<table style="width: 100%;height: 100%">
					<tr>
						<td>
					</tr>
					<tr>
						<td>
							<s:form action="/Send/send!saveInfo.action"
								method="post" enctype="multipart/form-data" target="myIframe">
								<s:hidden id="senddocId" name="sendbean.senddocId"></s:hidden>
								<table width="100%" height="40px" border="0" cellpadding="0"
									cellspacing="1" class="table1">
									<tr>
										<td width="100" class="biao_bg1" align="right">
											<span class="wz">上传正文:&nbsp;&nbsp;</span>
										</td>
										<td class=td1>
											<div style="margin-left: 15px;">
												<input type="file" style="width: 95%;"
													onkeydown="return false;" class="multi" name="uploads" accept="sep"/>
												(
												<span style="color:red">只能上传.sep文件</span>)
											</div>
										</td>
										<td class=td1>
										</td>
									</tr>
								</table>
								<table width="100%" border="0" cellspacing="0" cellpadding="00">
									<tr>
										<td align="center" valign="middle">
											<table width="27%" border="0" cellspacing="0"
												cellpadding="00">
												<tr>
													<td width="34%" height="34">
														&nbsp;
													</td>

													<td width="29%">
														&nbsp;
													</td>

													<td width="29%">
														<input id="save" name="save" onclick=" return doSubmit();"
															type="submit" class="input_bg" value="确定">
													</td>
													<td>
														&nbsp;&nbsp;
													</td>
													<td width="37%">
														<input name="button" type="button"
															onclick="window.close();" class="input_bg" value="关闭">
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</s:form>
						</td>
					</tr>
				</table>

			</fieldset>
		</div>
		<iframe name="myIframe" style="display:none"></iframe>
	</body>
</html>
