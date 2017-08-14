<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--  引用标签处-->
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<!--  引用公共文件,以下两个文件是建议包含-->
<%@include file="/common/include/rootPath.jsp"%>
<%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragrma", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css"
			type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/jquery.validate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/validate/formvalidate.js"
			type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>

		<title>发送手机短信</title>
		<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height: 100%
}

.biao_bg1 {
	line-height: 18px;
}

#checkRep {
	border: none;
}

#checknum {
	border: none;
}

#needRep {
	border: none;
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
				$("#orguserid").val("");
			});
			/**
				初始化选择的人员
			*/
			var uName=document.getElementById("userName");
			if(typeof(uName)!="undefined"&&uName.value!=""&&uName.value!=null){
				document.getElementById("orgusername").value=document.getElementById("userName").value;
			}
			iniitNeedRep()
		});
		 var cap_max=200;//可发送的字数
		 function onCharsChange(varField){
		 	 var smsContent = document.getElementById("smsCon");
		 	 var charsmonitor1 = document.getElementById("charsmonitor1");
		 	 var charsmonitor2 = document.getElementById("charsmonitor2");
		     var leftChars = getLeftChars(varField);
		     if ( leftChars >= 0)
		     {
			 	//charsmonitor1.value=cap_max-leftChars;
			 	//charsmonitor2.value=leftChars;
			 	charsmonitor1.innerHTML=cap_max-leftChars;
			 	charsmonitor2.innerHTML=leftChars;
			 	return true;
		     }
		     else
		     {
		     	charsmonitor1.value=cap_max;
		     	charsmonitor2.value="0";
		     	window.alert("短信内容超过字数限制。");
		     	var len = smsContent.value.length + leftChars;
			 	smsContent.value = smsContent.value.substring(0, len);
			 	leftChars = getLeftChars(smsContent);
		     	if ( leftChars >= 0)
		     	{
				charsmonitor1.innerHTML=cap_max-leftChars;
				charsmonitor2.innerHTML=leftChars;
				}
		        return false;
		     }
		 }
		 
		 function getLeftChars(varField){
		    var cap = cap_max;
		    var leftchars = cap - varField.value.length;
		    return (leftchars);
		 }
		 
		 
		 var reg_mobile= /^\d+$/;
		 function saveit(){
		  	 var smsForm = document.getElementById("smsForm");
			 var recvUserIds =document.getElementById("orguserid").value;
			 var imeiCode = document.getElementById("iemiCode").value;
		 	
		   	 if(recvUserIds==null|recvUserIds==""){
	   	 	 	alert("请选择接收人。");
	   	 	 	return false;
		   	 }else{
		   	 	if(recvUserIds.indexOf(",")!=-1){
		   	 		alert("每个IMEI只能匹配一个用户。");
		   	 		return;
		   	 	}
		   	 }
		   	 
		   	 
		   	 if(imeiCode==null|imeiCode==""){
		   	 	alert("请输入IMEI码。");
		   	 	return false;
		   	 }
		   	 
		   	 document.getElementById("userName").value=document.getElementById("orgusername").value;
		   	 smsForm.submit();
		   	 window.returnValue="OK";
		   	 window.close();
		 }
		 
		 function changneedRep(obj){
		 	if(obj.checked){
			 	$("#needRep").val("1");
		 	}else{
			 	$("#needRep").val("0");
		 	}
		 }
		 function iniitNeedRep(){
		 	 if($("#needRep").val()=="1"){
		 	 	$("#needRep").attr("checked",true);
		 	 }else{
		 	 	$("#needRep").attr("checked",false);
		 	 }
		 }
		</script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin">
		<input type="submit" id="submit" name="submit" value=""
			style="display: none">
		<script type="text/javascript"
			src="<%=path%>/common/js/newdate/WdatePicker.js"></script>
		<DIV id=contentborder align=center>
			<s:form action="systemset!saveIMEI.action" theme="simple" name="smsForm"
				id="smsForm">
				<input type="hidden" name="imeiModel.imeiId" id="imei_Id" value="${imeiModel.imeiId}">
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr align="center">
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img">
										<img src="<%=frameroot%>/images/ico/ico.gif">
										&nbsp;

									</td>
									<td>
										<strong>IMEI设置</strong>
									</td>


									<td align="right">
										<table border="0" align="right" cellpadding="0"
											cellspacing="0">
											<tr>
												<td width="7">
													<img src="<%=frameroot%>/images/ch_h_l.gif" />
												</td>
												<td class="Operation_input" onclick="saveit();">
													&nbsp;确&nbsp;定&nbsp;
												</td>
												<td width="7">
													<img src="<%=frameroot%>/images/ch_h_r.gif" />
												</td>
												<td width="5"></td>
												<td>
													&nbsp;
												</td>
												<td width="8">
													<img src="<%=frameroot%>/images/ch_z_l.gif" />
												</td>
												<td class="Operation_input1" onclick="window.close();">
													&nbsp;取&nbsp;消&nbsp;
												</td>
												<td width="7">
													<img src="<%=frameroot%>/images/ch_z_r.gif" />
												</td>
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
							<table border="0" cellpadding="0" cellspacing="1" class="table1"
								width="100%">
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right"
										valign="top">
										<span class="wz"><font color=red>*</font> 选择人员：</span>
									</td>
									<td class="td1">
											<s:textarea title="双击选择接收人" cols="35" id="orgusername"
												name="receiverNames" ondblclick="addPerson.click();"
												rows="4" readonly="true"></s:textarea>
											<input type="hidden" id="userName" name="imeiModel.userName"
												value="${imeiModel.userName}"></input>
											<input type="hidden" id="orguserid" name="imeiModel.userId"
												value="${imeiModel.userId}"></input>
											<input type="hidden" class="input_bg" id="addPerson"
												url="<%=root%>/address/addressOrg!tree.action" value="添加" />
											&nbsp;
								</tr>
								<tr>
									<td>
									</td>
									<td>
										<a id="addPerson" href="#" class="button"
											onclick="$('#addPerson').click()">添加</a>&nbsp;
										<a id="clearPerson" href="#" class="button"
											onclick="$('#clearPerson').click()">清空</a>

									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right"
										valign="top">
										<span class="wz"><font color=red>*</font> IMEI码：</span>
									</td>
									<td class="td1">
										<input type="text" name="imeiModel.iemiCode" id="iemiCode" value="${imeiModel.iemiCode}">
									</td>
								</tr>
								<tr>
									<td width="25%" height="21" class="biao_bg1" align="right">
										<span class="wz">是否开启：</span>
									</td>
									<td class="td1">
										<input type="checkbox" name="imeiModel.isOpen" id="needRep" value="${imeiModel.isOpen}"
											onclick="changneedRep(this);">
									</td>
								</tr>

								<tr>
									<td class="table_td"></td>
									<td></td>
								</tr>
							</table>
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
