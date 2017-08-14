<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<% 
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
<head>
<%@include file="/common/include/meta.jsp" %>
<title>邮件回复</title>
<link href="<%=frameroot%>/css/windows.css" rel="stylesheet" type="text/css">
<link href="<%=frameroot%>/css/properties_windows.css" rel="stylesheet" type="text/css">
<script language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
<script language="javascript" src="<%=path %>/common/js/upload/jquery.MultiFile.js"></script>
<script type="text/javascript" src="<%=path %>/common/js/common/common.js"></script>
<%--<script language="javascript" src="<%=path %>/common/js/upload/jquery.blockUI.js"></script>
--%><script language="javascript">
	//是否已修改
	
	var ischanged = false;
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
		
		//判断是否修改已读回执
		var ydhzCheck = $("#ydhz").attr("checked");
		$("#ydhz").click(function(){
			if(ydhzCheck!=$("#ydhz").attr("checked")){
				ischanged = true; 
				ydhzCheck = $("#ydhz").attr("checked");
			}
		});
		
		$("#fsxxhref").click(function(){
			if($("#fsxxtr").css("display")=='none'){
				$("#fsxxtr").show();
			}else{
				$("#fsxxtr").hide();
			}
		});
		$("#mailaddress").dblclick(function(){
			
			var accepter = encodeURI(encodeURI($("#mailaddress").val()));
			var csAccepter = encodeURI(encodeURI($("#csinput").val()));
			var msAaccepter = encodeURI(encodeURI($("#msinput").val()));
			
			var url = "<%=root%>/address/addressGroup!chooseperson.action?accepter="+accepter+"&csAccepter="+csAccepter+"&msAaccepter="+msAaccepter;
			OpenWindow(url,"600","350",window);
			
			if(accepter != encodeURI(encodeURI($("#mailaddress").val()))){ ischanged = true; }
			if(csAccepter != encodeURI(encodeURI($("#csinput").val()))){ ischanged = true; }
			if(msAaccepter != encodeURI(encodeURI($("#msinput").val()))){ ischanged = true; }
			
		});
		$("#csinput").dblclick(function(){
			$("#mailaddress").dblclick();
		});
		$("#msinput").dblclick(function(){
			$("#mailaddress").dblclick();
		});
		$("#fsbutton").click(function(){
			
			if($("#fsbutton").attr("disabled")){alert("正在发送，请稍等...");return;}
			
			var oEditor = FCKeditorAPI.GetInstance('content');
            var acontent = oEditor.GetXHTML();
            $("#desc").val(acontent);
            
            var madd = $.trim($("#mailaddress").val());
			if(madd.length==0){
				alert("未选择收件人！");
				$("#mailaddress").focus();
				return;
			}
			if($.trim($("#subject").val()).length==0){
				if(confirm("主题为空，继续发送，确定？")==true){
				}else{
					$("#subject").focus();
					return;
				}
			}
			
			//ajax提交操作
			while (madd.indexOf("，")>0) {
		   	 		madd = madd.replace("，",",");
		   	 	}
		   	$("#mailaddress").val(madd);
			$("#fsbutton").attr("disabled",true);
			document.mailForm.submit();
		});
		
		attHtmlCou = $("#attfile").html();	
	});
	function callback(msg){
		if(msg=="true"){
			$("#ischarge").val("true");
			//alert("邮件发送成功");
			window.close();
		}else if(msg=="lagre"){
			alert("不能上传附件，附件超出最大范围（<%=(com.strongit.oa.util.GlobalBaseData.ATTACH_SIZE/1024) %>KB）！");
			$("#fsbutton").attr("disabled",false);
		}else if(msg=="empty"){
			alert("附件不存在或附件大小为0！");
			$("#fsbutton").attr("disabled",false);
		}else if(msg=="savefalse"){
			alert("邮件已经发送成功！但发件箱保存失败");
			$("#fsbutton").attr("disabled",false);
		}else{
			alert("邮件发送失败！请您确认邮箱设置正确后，再重新发送");
			$("#fsbutton").attr("disabled",false);
		}
	}
	
	function aftersave(msg){
		/**
		if(msg=="true"){
			alert("邮件成功保存至草稿箱!");
		}else{
			alert("邮件保存失败！");
		}*/
	}
	
	var flagCon = "";
	var attHtmlCou = "";
	function saveDraft(){
		
		var oEditor = FCKeditorAPI.GetInstance('content');
	    var acontent = oEditor.GetHTML();
	    	    
	    if(acontent!=flagCon){
	    	ischanged = true;
	    	flagCon = acontent;
	    }
	    
	    if($("#attfile").html()!=attHtmlCou){
	    	ischanged = true;
	    	attHtmlCou = $("#attfile").html();
	    }
	    
	    if(ischanged==false){
			return;
		}
		
		if($("#ischarge").val()=="false"){
			if(confirm("请问您是否要将其保存至草稿箱呢")==true){
				$("#mailForm").attr("action","<%=root %>/mymail/mail!saveDraft.action");
	            $("#desc").val(acontent);
	            ischanged=false;
				document.mailForm.submit();
	        	event.returnValue="您已经提交成功！是否要离开本页面？";
			}else{
				event.returnValue="您是要不保存草稿就离开本页面么？";
			}
		}else{
			//return true;
			
		}
	}
	function chageState(){
		alert("ddddd");
	}
	function charge(){
		if($("#ischarge").val()=="true"){
			return true;
		}else{
			setTimeout("charge()",1000); 
		}
	}
</script>

</head>

<body class="contentbodymargin" onbeforeunload="saveDraft()">
	<input type="hidden" name="ischarge" id="ischarge" value="false">
	<form id="mailForm" name="mailForm" action="<%=root %>/mymail/mail!sendMail.action" method="post" enctype="multipart/form-data" target="myIframe">
	<input type="hidden" name="desc" id="desc"> 
	<div id="con" style="display: none">${myMail.mailCon}</div>
	<div id="contentborder" align="center">
	<table width="100%" height="38px;"
			style="FILTER:progid:DXImageTransform.Microsoft.Gradient(gradientType=0,startColorStr=#ededed,endColorStr=#ffffff);">
			<tr>
				<td>&nbsp;</td>
				<td width="20%">
					<img src="<%=frameroot%>/images/ico/ico.gif" width="9" height="9">&nbsp;
					回复邮件
				</td>
				<td width="80%">
					<table border="0" align="right" cellpadding="00" cellspacing="0">
		                <tr>
		                  <td width="*"></td>
		                  <td><a class="Operation" href="#" onclick="$('#fsbutton').click()"><img src="<%=root%>/images/ico/fasong.gif" width="15" height="15" class="img_s">发送</a></td>
		                  <td width="5"></td>
		                  <td><a class="Operation" href="#" onclick="$('#savedraft').click()"><img src="<%=root%>/images/ico/baocun.gif" width="15" height="15" class="img_s">存草稿</a></td>
		                  <td width="5"></td>
		                  <td><a class="Operation" href="#" onclick="$('#cancle').click()"><img src="<%=root%>/images/ico/guanbi.gif" width="15" height="15" class="img_s">关闭</a></td>
		                  <td width="5"></td>
		                </tr>
		            </table>
				</td>
			</tr>
		</table>
		<table  width="100%" border="0" cellpadding="0" cellspacing="1"  class="table1">
			<tr>
				<td class="biao_bg1" width="15%"><span class="wz">发&nbsp;件&nbsp;人(<font color=red>*</font>)：</span></td>
				<td class="td1" width="35%">
					<select id="sender" name="sender">
						<s:iterator value="#request.list" status="statu" id="mailBox">
							<s:if test="status==0">
								<option class="wz" selected value="<s:property value="#mailBox.mailboxId"/>" >"<s:property value="#mailBox.pop3Account"/>"&lt;<s:property value="#mailBox.mailAddress"/>></option>
							</s:if>
							<s:else>
								<option class="wz" value="<s:property value="#mailBox.mailboxId"/>">"<s:property value="#mailBox.pop3Account"/>"&lt;<s:property value="#mailBox.mailAddress"/>></option>
							</s:else>
						</s:iterator>
						
					</select>
				</td>
				<td class="td1" width="15%">
					
				</td>
				<td class="td1" width="35%" align="left">	
					<table width="100%">
						<tr>
							<td>&nbsp;</td>
							<td>
								<table align="left">
					                <tr>
					                  <td width="*"></td>
					                  <td><input type="button" id="csbutton" class="Operation" style="width: 70px;height: 23px;" value="添加抄送" /></td>
					                  <td width="5"></td>
					                  <td><input type="button" id="msbutton" class="Operation" style="width: 70px;height: 23px;" value="添加密送" /></td>
					                  <td width="5"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%">
					<span class="wz">收&nbsp;件&nbsp;人(<font color=red>*</font>)：</span>
				</td>
				<td colspan="3" class="td1" width="75%">
					<input type="text" name="mailaddress" id="mailaddress" title="多个收件人之间请用英文逗号分隔" style="width:100% " value="${myMail.mailSender }"/>
				</td>
			</tr>
			<tr id=cstr style="display:none;">
				<td class="biao_bg1" width="15%">
					<span class="wz">抄&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;送&nbsp;&nbsp;：</span>
				</td>
				<td colspan="3" class="td1" width="75%">
					<input type="text" name="csinput" id="csinput" style="width:100% "/>
				</td>
			</tr>
			<tr id=mstr style="display:none;">
				<td class="biao_bg1" width="15%">
					<span class="wz">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;送&nbsp;&nbsp;：</span>
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
					<input type="text" name="subject" id="subject" style="width:100% " value="${myMail.mailTitle }" onKeyUp="ischanged = true;"/>
				</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%"><span class="wz">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件&nbsp;&nbsp;：</span></td>
				<td id="attfile" colspan="3" class="td1" width="75%">
					<input type="file" onkeydown="return false;" name="file" class="multi required" style="width: 76%;"/>
				</td>
			</tr>
			<tr>

				<td colspan="4" class="td1" width="100%">
  					<script type="text/javascript" src="<%=path%>/common/js/fckeditor2/fckeditor.js"></script>
					<script type="text/javascript">
						var oFCKeditor = new FCKeditor( 'content' );
						oFCKeditor.BasePath	= '<%=path%>/common/js/fckeditor2/'
						oFCKeditor.ToolbarSet = "FutureDefaultSet" ;
						oFCKeditor.Width = '100%' ;
                        oFCKeditor.Height = '300' ;
						oFCKeditor.Value = document.getElementById("con").innerHTML;
						oFCKeditor.Create() ;
						
						flagCon=oFCKeditor.Value;
					</script>         
				</td>
			</tr>
			<tr style="display: none;">
				<td class="biao_bg1" width="15%"></td>
				<td colspan="3" class="td1" width="75%">
 					<span class="wz"><a id="fsxxhref" href="#">发送选项</a></span>
				</td>
			</tr>
			<tr id=fsxxtr>
				<td class="biao_bg1" width="15%"></td>
				<td colspan="3" class="td1" width="75%"><input type="checkbox" name="ydhz" id="ydhz" value="1">已读回执&nbsp;</td>
			</tr>
			<tr>
				<td class="biao_bg1" width="15%"></td>
				<td colspan="3" class="td1" width="75%">
					 &nbsp;&nbsp;&nbsp;
					 &nbsp;&nbsp;&nbsp;
					
					<table width="100%">
						<tr>
							<td>&nbsp;</td>
							<td width="100%">
								<table align="left">
					                <tr>
					                  <td width="*"></td>
					                  <td><input type="button" name="fsbutton" id="fsbutton" class="Operation" style="width: 40px;height: 23px;" value="发送"/></td>
					                  <td width="5"></td>
					                  <td><input type="button" name="savedraft" id="savedraft" class="Operation" style="width: 52px;height: 23px;" value="存草稿" onclick="saveDraft()"/></td>
					                  <td width="5"></td>
					                  <td><input type="button" name="cancle" id="cancle" class="Operation" style="width: 40px;height: 23px;" value="关闭" onclick="window.close();"></td>
					                  <td width="5"></td>
					                </tr>
					            </table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			
		</table>
					
	</div>
	<iframe name="myIframe" style="display:none"></iframe>
	</form>
	<script>
		$("#mailaddress").focus();	
	</script>
</body>
</html>
