<%@ page contentType="text/html; charset=utf-8"%>
<jsp:directive.page import="com.strongit.oa.util.GlobalBaseData"/>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@taglib uri="/tags/web-remind" prefix="remind"%>
<%@ taglib uri="/tags/web-newdate" prefix="strong"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="/common/include/rootPath.jsp"%>

<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>保存委派任务</title>
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/common/common.js" type="text/javascript"></script>
		<style type="text/css">
		
		select{
		border:1px solid #b3bcc3;background-color:#ffffff;
		}
		</style>
		<script language="javascript" type="text/javascript">
		      var i=0;
			function submitForm(){
				i++;
				if(i>1){
					alert("请不要重复提交。");
					return;
				}
			  /*var form=document.getElementById('delegationForm');
			  
			 if(form.dhDeleRealname.value.trim() ==""){
								alert("请选择代理人员!");
				    			return false;
							}
			 if(form.dhDeleProcess.value.trim() ==""){
								alert("请选择代理事项!");
				    			return false;
							}
			 if(form.deStartDate.value.trim() ==""){
								alert("请选择开始时间!");
				    			return false;
							}
 if(form.deRest1.value == "0" && form.deEndDate.value.trim() ==""){
								alert("请选择结束时间!");
				    			return false;
							}
			if(form.dhDeleReason.value.length > 200){
			 					alert("委托事由字数不能大于200!");
			 					return false;
			 }																	
			       form.submit();*/
			    if($("#dhDeleRealname").val() == ""){
			       	alert("请选择委派人员。");
			       	i=0;
			       	return ;
			    }
			    if($("#dhDeleProcess").val() == ""){
			    	i=0;
			    	alert("请选择委派事项。");
			    	return ;
			    }
			    if(document.getElementsByName("model.deRest1")[0].value=="0" && $("#deStartDate").val() == ""){
			    	alert("请选择开始时间。");
			    	i=0;
			    	return ;
			    }
			    if(document.getElementsByName("model.deRest1")[0].value=="0" && $("#deEndDate").val() == ""){
			    	alert("请选择结束时间。");
			    	i=0;
			    	return ;
			    }
			    if($("#dhDeleReason").val().length > 200){
			    	alert("委派事由字数不能大于200。");
			    	i=0;
			    	return ;
			    }
			    if($("#deDeleId").val() == '${userInfo.userId}')  {
			    	alert("委派人员不能选择自己，请重新选择。");
			    	$("#deDeleId").val("");
			    	$("#dhDeleRealname").val("");
			    	i=0;
			    	return ;
			    } 
			    if($("#deEndDate").val() != "" && $("#deStartDate").val() >= $("#deEndDate").val()){
			    	alert("开始时间必须小于结束时间。");
			    	i=0;
			    	return ;
			    }	
			    $.post(scriptroot + "/workflowDelegation/action/processDelegation!checkTaskIsDelegated.action",
			    	  {dhDeleProcess:$("#dhDeleProcess").val()},
			    	  function(ret){
			    		if(ret == "0"){
			    			var remindType = "";
							$("#StrRem").find("input:checkbox:checked").each(function(){
								remindType = remindType + $(this).val() + ",";
							});
							if(remindType!=""){
						    	remindType = remindType.substring(0,remindType.length-1);
							}
							$("#remindType").val(remindType);
			    			$("form").submit();
			    			
			    		}else if(ret == "-1"){
			    			alert("对不起，操作异常。");
			    		}else{
			    			alert(ret);
			    			
			    		}
			    	  });
			    
			}
			
			function cancelSubmit(){
				window.close();	
			}
			
			
			//指定任务处理人
			function setTaskActors(){
					/*var vPageLink = scriptroot + "/workflowDelegation/action/processDelegation!user.action";
					var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:320pt;dialogHeight:360pt;status:no;help:no;scroll:yes;status:0;help:0;scroll:1;");
					if(returnValue!=null&&returnValue!='undefined'&&returnValue!=''){
						var strid = '';
						var strtext = '';
				        var returnits= returnValue.split(",");
				        strid = returnits[0];
				        strtext= returnits[1];

						document.getElementById("deDeleId").value=strid;
						document.getElementById("dhDeleRealname").value=strtext;
					}*/
					
					var ret=OpenWindow("<%=root%>/address/addressOrg!tree.action?elementId=deDeleId&elementName=dhDeleRealname&isNeedSet=no&typewei=weipai","600","400",window);
					if(ret){
						var id = ret[0];
						var name = ret[1];
						if(id.split(",").length>1){
							alert("委派人员只能选择一位。");
							return ;
						}
						$("#deDeleId").val(id);
						$("#dhDeleRealname").val(name);
					}
					
			}
			
			//指定代理事项
			function setProcess(){
					var vPageLink = scriptroot + "/workflowDelegation/action/processDelegation!process.action";
					var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:240pt;dialogHeight:360pt;status:no;help:no;scroll:yes;status:0;help:0;scroll:1;");
					if(returnValue!=null && returnValue!='undefined' && returnValue!=''){
				        var returnits = returnValue.split("|");
						document.getElementById("dhDeleProcess").value=returnValue;
						var sel = document.getElementById("selectProcess");
						while(sel.options.length >0){
							sel.options[sel.options.length-1] = null;
						}
						for(var i= 0; i< returnits.length; i++){
							var name = returnits[i].split(",")[1];
							var op = new Option(name, name);
							sel.options.add(op, sel.options.length);
						}
					}
			}						
			
			function init(){
				var deIsStart;
				if('${model.deIsStart}'==null){
					deIsStart = '0';
				}else{
					deIsStart = '${model.deIsStart}';
				}
				document.getElementById("deIsStart").value = deIsStart;
				if(deIsStart == '0'){
					$("#no").click();
				}else{
					$("#yes").click();
				}
			}
			function alert1(value){
				if(value == "1"){
					$("#deStartDateTR").hide();
					$("#deStartDate").val("");
					$("#deEndDateTR").hide();
					$("#deEndDate").val("");
				}else{
					$("#deStartDateTR").show();
					$("#deEndDateTR").show();
				}
			}
</script>
	</head>
	<base target="_self" />
	<body class="contentbodymargin" onload="init()">
<!--		<script type="text/javascript"-->
<!--			src="<%=root%>/common/js/newdate/WdatePicker.js"></script>-->
<script type="text/javascript" src="<%=path%>/common/script/My97DatePicker/WdatePicker.js"></script>
		<div id="contentborder" cellpadding="0">
			<form id="delegationForm"
				action="<%=root%>/workflowDelegation/action/processDelegation!save.action"
				method="POST">
				<input type="hidden" name="deId" value="${model.deId}">
				<input id="remindType" type = "hidden" name="type"/>
				<table width="100%" border="0" cellspacing="0" cellpadding="00">
					<tr>
						<td colspan="3" class="table_headtd">
							<table width="100%" border="0" cellspacing="0" cellpadding="00">
								<tr>
									<td class="table_headtd_img" >
										<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
									</td>
									<td align="left">
										<strong>保存委派任务</strong>
									</td>
									<td align="right">
										<table border="0" align="right" cellpadding="00" cellspacing="0">
											<tr >
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
							                 	<td class="Operation_input" onclick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
						                  		<td width="5"></td>
						                  		<td width="7"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
							                 	<td class="Operation_input1" onclick="cancelSubmit();">&nbsp;关&nbsp;闭&nbsp;</td>
							                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
						                  		<td width="5"></td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<center>
					
					<%--
					<table border="0" width="90%" bordercolor="#FFFFFF" cellspacing="0"
						cellpadding="0">
						<tr>
							<td width="100%">
								<table border="1" cellspacing="0" width="100%"
									bordercolordark="#FFFFFF" bordercolorlight="#000000"
									bordercolor="#333300" cellpadding="2">
									<tr>
										<td width="20%" align="right" class="titleTD" height="20">
											当前用户：
										</td>
										<td width="80%">
											&nbsp; ${userInfo.userName}
										</td>
									</tr>
									<tr>
										<td align="right" class="titleTD" height="20">
											登录帐号：
										</td>
										<td>
											&nbsp; ${userInfo.userLonginName}
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					--%>
					<table border="0" width="100%" cellspacing="0" class="table1" 
						cellpadding="0">
							<tr>
								<td align="right" class="titleTD" height="20">
									<font color="#FF0000">*</font>
									委派人：
								</td>
								<td class="td1">
									&nbsp;
									<input type="text" name="model.dhDeleRealname"
										id="dhDeleRealname" style="width: 210;" readonly="true"
										maxlength="15" />
									<input type="hidden" name="deDeleId" id="deDeleId" />
									<a href="#" class="button" onclick="setTaskActors()">选择</a>&nbsp;
									<font color = '#999999'>委派人只能选择一位</font>	
								</td>
							</tr>
							<tr>
								<td align="right" class="titleTD" valign="top" style="padding-top: 15px;">
									<font color="#FF0000">*</font>
									委派事项：
								</td>
								<td class="td1" style="padding-bottom:5px;">
									&nbsp;
									<table border="0" width="100%">
									<tr><td width="210px">
									<div  style="width: 210px;border:1px solid #b3bcc3;overflow:hidden;margin-bottom: 5px;margin-left: 7px">
									<select id="selectProcess" size="3"  style="width: 214px;border:1px solid #b3bcc3;background-color:#ffffff;margin:-2px"></select>
									</div></td>
									<td align="left"   valign="top">
									<a href="#"  style="margin-left: 4px;" class="button" onclick="setProcess()">选择</a>&nbsp;
										<font color = '#999999'>委派事项可选择多项</font>	
										<input type="hidden" name="dhDeleProcess" id="dhDeleProcess" />
									</td></tr>
									</table>
								</td>
							</tr>
							<br>
							<tr id="deStartDateTR"> 
								<td align="right" class="titleTD" height="20">
									<font color="#FF0000">*</font>
									开始时间：
								</td>
								<td class="td1">
									&nbsp;
									<strong:newdate name="model.deStartDate"
										dateform="yyyy-MM-dd HH:mm:ss" id="deStartDate" width="210"
										skin="whyGreen" isicon="true"></strong:newdate>
								</td>
							</tr>
							<tr id="deEndDateTR">
								<td align="right" class="titleTD" height="20">
									<font color="#FF0000">*</font>
									结束时间：
								</td>
								<td class="td1">
									&nbsp;
									<strong:newdate name="model.deEndDate"
										dateform="yyyy-MM-dd HH:mm:ss" id="deEndDate" width="210"
										skin="whyGreen" isicon="true"></strong:newdate>
								</td>
							</tr>
							<tr>
								<td align="right" class="titleTD" height="20">
									过期设置：
								</td>
								<td class="td1">
									&nbsp;
									<s:select id="deRest1" name="model.deRest1" list="#{'0':'允许过期','1':'永不过期'}" onchange="alert1(this.value);"/>
								</td>
							</tr>
							<tr>
								<td align="right" valign="top" class="titleTD" height="20">
									委派设置：
								</td>
								<td class="td1">
									&nbsp;
									<input type="hidden" name="model.deIsDoingDele"
										id="deIsDoingDele" value="1"><!--
									委派已办任务
									<br />
									&nbsp;
									-->
									
									
									<input type="hidden" name="model.deIsDoingRece"
										id="deIsDoingRece" value="1"><!--
									收回已办任务
									<br />
									&nbsp;
									-->
									
									<input type="checkbox" name="model.deIsEndRece"
										id="deIsEndRece" value="1">
									<!--收回办结任务-->
									收回已办文件
										<font color = '#999999'>委派周期结束之后，处理完的文件回到设置人的文件列表。</font>
										
									<br>
									&nbsp;&nbsp;<font color = '#999999'>委派设置成功后系统默认委派待办文件，文件办理完成后回到本人文件列表。</font>	
								</td>
							</tr>
							<tr>
								<td align="right" class="titleTD" valign="top">
									委派事由：
								</td>
								<td class="td1">
									&nbsp;
									<textarea name="model.dhDeleReason" id="dhDeleReason"
										rows="4" cols="50" max="210" msg="委托事由只能在200个字符之内"></textarea>
								</td>
							</tr>
							<tr>
								<td align="right" class="titleTD">
									委派提醒：
								</td>
								<td id="StrRem" style="vertical-align:midden">
								<span style="height:55px;padding-left:4px"><remind:remind  isShowButton="false" isOnlyRemindInfo="true" includeRemind="RTX,SMS" rtxChecked="checked"  code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/></span>
									<%--<remind:remind msgChecked="checked" isOnlyRemindInfo="true" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>--%>
									  
								</td>
							</tr>
							<tr>
								<td align="right" class="titleTD">
									立即生效设置：
								</td>
								<td class="td1" class="titleTD">
									<script type="text/javascript">
										function ljsx(obj){//立即生效设置
											$("#deIsStart").val(obj.value);
										}
									</script>
									<input id="no" type="radio" name="radio" value="0" onclick="ljsx(this)"/><label for="no">否</label>
									<input id="yes" type="radio" name="radio" value="1" onclick="ljsx(this)"/><label for="yes">是</label>
									<input type="hidden" name="model.deIsStart" id="deIsStart" value="">
								</td>
							</tr>
							<tr>
								<td class="table1_td"></td>
								<td></td>
							</tr>
							<tr>
								<td class="table1_td"></td>
								<td></td>
							</tr>
					</table>
				</center>
			</form>
		</div>
	</body>
</html>
