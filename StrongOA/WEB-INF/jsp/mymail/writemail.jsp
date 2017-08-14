<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>邮件发送</title>
<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script language="javascript" src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
<script language="javascript" src="<%=path %>/common/js/upload/jquery.blockUI.js"></script>

<script language="javascript">
	$(document).ready(function(){
		$("#csbutton").click(function(){
			if($("#csbutton").val()=='添加抄送'){
				$("#csbutton").val('删除抄送');
				$("#cstr").show();
			}else{
				$("#csbutton").val('添加抄送');
				$("#cstr").hide();
			};

		});
		$("#msbutton").click(function(){
			if($("#msbutton").val()=='添加密送'){
				$("#msbutton").val('删除密送');
				$("#mstr").show();
			}else{
				$("#msbutton").val('添加密送');
				$("#mstr").hide();
			};
		});
		$("#fsxxhref").click(function(){
			if($("#fsxxtr").css("display")=='none'){
				$("#fsxxtr").show();
			}else{
				$("#fsxxtr").hide();
			}
		});
		$("#mailaddress").dblclick(function(){
			alert("添加收件人");
		});
		$("#csinput").dblclick(function(){
			alert("添加抄送人员");
		});
		$("#msinput").dblclick(function(){
			alert("添加密送人员");
		});
	});
</script>

</head>

<body class="contentbodymargin">
	<div id="contentborder" align="center">
		<table  width="100%" border="0" cellpadding="0" cellspacing="1"  class="table1">
			<tr>
				<td class="biao_bg1" width="15%"><span class="wz">发&nbsp;件&nbsp;人(<font color=red>*</font>)：</span></td>
				<td class="td1" width="35%">
					<select>
						<option class="wz" selected value="1">"yuhz"&lt;yuhz@strongit.com.cn></option>
						<option class="wz" value="2">"yuhzyahoo"&lt;yuhz@yahoo.com.cn></option>
					</select>
				</td>
				<td class="td1" width="15%">
					
				</td>
				<td class="td1" width="35%" align="left">	
					<input type="button" id="csbutton" class="button" value="添加抄送" />&nbsp;&nbsp;<input type="button" id="msbutton" class="button" value="添加密送" />
				</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%">
					<span class="wz">收&nbsp;件&nbsp;人(<font color=red>*</font>)：</span>
				</td>
				<td colspan="3" class="td1" width="75%">
					<input type="text" name="mailaddress" id="mailaddress" style="width:100% "/>
				</td>
			</tr>
			<tr id=cstr style="display:none;">
				<td class="biao_bg1" width="15%">
					<span class="wz">抄&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;送&nbsp;&nbsp;&nbsp;&nbsp;：</span>
				</td>
				<td colspan="3" class="td1" width="75%">
					<input type="text" name="csinput" id="csinput" style="width:100% "/>
				</td>
			</tr>
			<tr id=mstr style="display:none;">
				<td class="biao_bg1" width="15%">
					<span class="wz">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;送&nbsp;&nbsp;&nbsp;&nbsp;：</span>
				</td>
				<td colspan="3" class="td1" width="75%">
					<input type="text" name="msinput" id="msinput" style="width:100% "/>
				</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%">
					<span class="wz">主&nbsp;&nbsp;&nbsp;&nbsp;题(<font color=red>*</font>)：</span>
				</td>
				<td colspan="3" class="td1" width="75%">
					<input type="text" name="subject" id="subject" style="width:100% "/>
				</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%"><span class="wz">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;：</span></td>
				<td colspan="3" class="td1" width="75%">
					<input type="file" class="multi" maxlength="5"/>
				</td>
			</tr>
			<tr>

				<td colspan="4" class="td1" width="100%">
  					<textarea name="content" style="display:none;width=100%"></textarea>
  					<iframe ID="Editor" name="Editor" src="HtmlEditor/index.html?ID=content" frameBorder="0" marginHeight="0" marginWidth="0" scrolling="No" style="height:320px;width:100%"></iframe>
				</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%"></td>
				<td colspan="3" class="td1" width="75%">
 					<span class="wz"><a id="fsxxhref" href="#">发送选项</a></span>
				</td>
			</tr>
			<tr id=fsxxtr style="display:none ">
				<td class="biao_bg1" width="15%"></td>
				<td colspan="3" class="td1" width="75%"><input type="checkbox" name="ydhz" id="ydhz" value="1">已读回执&nbsp;</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%"></td>
				<td colspan="3" class="td1" width="75%"><input type="submit" name="submit" id="submit" class="button" value="发送"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="cancle" id="cancle" class="button" value="取消" onclick="history.go(-1)"></td>
			</tr>
			
		</table>
					
	</div>
</body>
</html>
