<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@	include file="/common/include/rootPath.jsp"%>
<%@	taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/tags/security" prefix="security"%>
<html>
	<head>
		<title>
			<s:if test="model.pfId==null">
				新建流程模型
			</s:if>
			<s:else>
				编辑流程模型
			</s:else>
		</title>
		<%@include file="/common/include/meta.jsp" %>
		<link rel="stylesheet" type="text/css" href="<%=path%>/workflow/designer/css/style.css">
		<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script language="javascript" src="<%=path%>/workflow/designer/js/webTab/webTab.js"></script>
		<SCRIPT language="javascript" src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></SCRIPT>					
		<base target="_self" />
		
		<style type="text/css">
		
		</style>
	<script type="text/javascript">
	   var tempOwner = "";
	   var tempDesigner = "";
	   var tempStartor = "";
	   
	   function filterString(name){
	    	  name = name.replace(/\%/g, "");
	    	  name = name.replace(/\,/g, "，");
	    	  name = name.replace(/\./g, "．");
	    	  name = name.replace(/\&/g, "");
	    	  name = name.replace(/\@/g, "＠");
	    	  name = name.replace(/\#/g, "＃");
	    	  name = name.replace(/\…/g, "．．．");
	    	  name = name.replace(/\\/g, "＼");
	    	  name = name.replace(/\//g, "／");
	    	  name = name.replace(/\{/g, "〖");
	    	  name = name.replace(/\}/g, "｛");
	    	  name = name.replace(/\{/g, "｝");
	    	  name = name.replace(/\:/g, "∶");
	    	  name = name.replace(/\'/g, "’");
	    	  name = name.replace(/\"/g, "＂");
	    	  name = name.replace(/\;/g, "；");
		   return name;
	   }

       function submitForm(){
    	  var name =  $.trim(document.getElementById("pfName").value);
    	  document.getElementById("pfName").value = filterString(name);
       	  if(name == ""){
       	  	alert("流程名称不能为空。");
       	  	return false;
       	  }
       	  if(name.indexOf("|") != -1){
       	  	alert("流程名称中含有非法字符\"|\"。");
	       	return false;
       	  }
    	  var rest2 =  $.trim(document.getElementById("rest2").value);
    	  document.getElementById("rest2").value = filterString(rest2);

       	  //判断流程名称是否冲突
		  $.ajax({ url: scriptroot + "/workflowDesign/action/processFile!checkProcessfileName.action",
             type:"post",
             dataType:"text",
             data: "id=" + document.getElementById("pfId").value + "&name=" + document.getElementById("pfName").value,
             success:function(msg){ 
                 if(msg == ""){
                 	document.forms[0].submit();
                 }else{
                 	alert(msg);
                 }
             } 
          });       	  
       }
       
       function cancel(){
       	  window.close();
       }
       
       var selectedSection = "owner";
       
       function getInitData(){
       		var returnValue;
       		if(selectedSection == "owner"){
				returnValue = document.getElementById("pfProOwner").value;
			}else if(selectedSection == "designer"){
				returnValue = document.getElementById("pfProDesigner").value;
			}else if(selectedSection == "startor"){
				returnValue = document.getElementById("pfProStartor").value;
			}
			return returnValue == "" ? [] : returnValue.split("|");
       }
       
       function setSelectedData(selectedData){
       		var returnValue = selectedData.join("|");
			
			if(selectedSection == "owner"){
				document.getElementById("pfProOwner").value = returnValue;
				tempOwner = returnValue;
				selectObj = document.getElementById("owner");
			}else if(selectedSection == "designer"){
				document.getElementById("pfProDesigner").value = returnValue;
				tempDesigner = returnValue;
				selectObj = document.getElementById("designer");
			}else if(selectedSection == "startor"){
				document.getElementById("pfProStartor").value = returnValue;
				tempStartor = returnValue;
				selectObj = document.getElementById("startor");
			}
			
			while(selectObj.options.length > 0){
				selectObj.options[0] = null;
			}
			
			/**
			 * 不选择人的情况 
			 */
			if(returnValue != ""){
				for(var i = 0; i < selectedData.length; i++){
					var name = selectedData[i].split(",")[1];
					var op = new Option(name, name);
					selectObj.options.add(op, selectObj.options.length);
				}
			}
       }
       
		//指定人员
		function setTaskActors(flag){
		
			selectedSection = flag;
			var type = "0";
		    if($("#rest1").attr("checked") == true){
		    	type = "1";
		    }
			var vPageLink = scriptroot + "/workflowDesign/action/userSelect!input.action?dispatch=mg&type="+type;
			var returnValue = window.showModalDialog(vPageLink,window,"dialogWidth:720px;dialogHeight:540px;status:no;help:no;scroll:yes;status:0;help:0;");			
		}       

//页面加载时初始化
function initWindow(){
	if("${model.pfId}" != ""){
		var owner = document.getElementById("pfProOwner");
		var designer = document.getElementById("pfProDesigner");
		var startor = document.getElementById("pfProStartor");
//		document.getElementById("pfId").value = "<c:out value="${processfile.pfId}"/>";
//		document.getElementById("pfName").value = "<c:out value="${processfile.pfName}"/>";
		if("${model.pfIsDeploy}" != "0"){
			document.getElementById("pfName").disabled = true;
			document.getElementById("notice").innerHTML = "<font color='red'>流程已部署</font>";
		}
		
//		owner.value = '<c:out value="${processfile.pfProOwner}"/>';
//		designer.value = '<c:out value="${processfile.pfProDesigner}"/>';
//		startor.value = '<c:out value="${processfile.pfProStartor}"/>';
		
		tempOwner = owner.value;
		tempDesigner = designer.value;
		tempStartor = startor.value;		
		
		var ownerSelect = document.getElementById("owner");
		var startorSelect = document.getElementById("startor");
		var designerSelect = document.getElementById("designer");
		if(tempStartor != ""){
			var startorDetails;
			var startors = tempStartor.split("|");
			for(var i=0; i<startors.length; i++){
				startorDetails = startors[i].split(",");
				var flag = startorDetails[0].substring(0,1);
				if(flag == "o"){
					var objOption  =  new  Option(startorDetails[1], startorDetails[1]);  
			      	startorSelect.options.add(objOption, startorSelect.options.length);	
				}else if(flag == "p"){
					var objOption  =  new  Option(startorDetails[1], startorDetails[1]);  
			      	startorSelect.options.add(objOption, startorSelect.options.length);					
				}else if(flag == "u"){
					var objOption  =  new  Option(startorDetails[1], startorDetails[1]);  
			      	startorSelect.options.add(objOption, startorSelect.options.length);					
				}
			}
		}    
		if(tempDesigner != ""){
			var designerDetails;
			var designers = tempDesigner.split("|");
			for(var i=0; i<designers.length; i++){
				designerDetails = designers[i].split(",");
				var flag = designerDetails[0].substring(0,1);
				if(flag == "o"){
					var objOption  =  new  Option(designerDetails[1], designerDetails[1]);  
			      	designerSelect.options.add(objOption, designerSelect.options.length);	
				}else if(flag == "p"){
					var objOption  =  new  Option(designerDetails[1], designerDetails[1]);  
			      	designerSelect.options.add(objOption, designerSelect.options.length);					
				}else if(flag == "u"){
					var objOption  =  new  Option(designerDetails[1], designerDetails[1]);  
			      	designerSelect.options.add(objOption, designerSelect.options.length);					
				}
			}		
		}
		if(tempOwner != ""){
			var ownerDetails;
			var owners = tempOwner.split("|");
			for(var i=0; i<owners.length; i++){
				ownerDetails = owners[i].split(",");
				var flag = ownerDetails[0].substring(0,1);
				if(flag == "o"){
					var objOption  =  new  Option(ownerDetails[1], ownerDetails[1]);  
			      	ownerSelect.options.add(objOption, ownerSelect.options.length);	
				}else if(flag == "p"){
					var objOption  =  new  Option(ownerDetails[1], ownerDetails[1]);  
			      	ownerSelect.options.add(objOption, ownerSelect.options.length);					
				}else if(flag == "u"){
					var objOption  =  new  Option(ownerDetails[1], ownerDetails[1]);  
			      	ownerSelect.options.add(objOption, ownerSelect.options.length);					
				}
			}		
		}
	}
}
</script>
	</head>
	<body onLoad="initWindow()" class=contentbodymargin oncontextmenu="return false;">
		<DIV id=contentborder align=center>
		<form id="meetform" action="<%=path %>/workflowDesign/action/processFile!save.action" method="POST">
				<input type="hidden" name="pfId" id="pfId" value="${model.pfId }" />
				<input type="hidden" name="model.pfProOwner" id="pfProOwner" value="${model.pfProOwner }" />
				<input type="hidden" name="model.pfProDesigner" id="pfProDesigner" value="${model.pfProDesigner }" />
				<input type="hidden" name="model.pfProStartor" id="pfProStartor" value="${model.pfProStartor }" />			
			<table width="100%" class="table_headtd">
				<tr>
			        <td class="table_headtd_img" >
						<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
					</td>
			        <td aligt="left">
			        	<script>
						var id = "${model.pfId}";
						if(id==null|id==""){
							window.document.write("<strong>新建流程模型</strong>");
						}else{
							window.document.write("<strong>编辑流程模型</strong>");
						}
						</script>
			        </td>	
			        <td aligt="right">
			        	<table border="0" align="right" cellpadding="00" cellspacing="0">
			                <tr>
			                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
			                 	<td class="Operation_input" onClick="submitForm();">&nbsp;保&nbsp;存&nbsp;</td>
			                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
		                  		<td width="5"></td>
			                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
			                 	<td class="Operation_input1" onClick="cancel();">&nbsp;取&nbsp;消&nbsp;</td>
			                 	<td width="7"><img src="<%=frameroot%>/images/ch_z_r.gif"/></td>
		                  		<td width="6"></td>
			                </tr>
			            </table>
			        </td>
				</tr>
			</table>	
			<table width="100%" height="10%" border="0" cellpadding="0"
				cellspacing="1" align="center" class="table1">
					<tr>
						<td class="biao_bg1">
							<TABLE border=0 width="100%" height="100%">
								<TR valign=top>
									<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left>
												&nbsp;
												<span id="tabpage1_1">流程名称</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<input type="text" name="model.pfName" id="pfName" value="${model.pfName }" style="border:1px solid #b3bcc3;width:300"
															maxLength="50">
														<span id="notice"></span>
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>&nbsp;
										
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left>
												&nbsp;
												<span id="tabpage1_1">流程别名</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%">
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<input type="text" name="model.rest2" id="rest2" value="${model.rest2 }"  maxLength="50" style="width:300;border:1px solid #b3bcc3;">
														<span id="notice"></span>
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>&nbsp;
										
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left>
												<span id="tabpage2_1">设置管理者</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%">
												<TR valign=top>
													<TD width=0></TD>
													<TD style="position:relative; text-align:right;">
                  <div style="width:300px; border:1px solid #b3bcc3; overflow:hidden;">
														<select name="owner" id="owner" size="5" style="width:304px; margin:-2px;"></select>
                                                        </div>
														&nbsp;&nbsp;
														<a  href="#" style="margin-top:9px;" class="button" onClick="setTaskActors('owner')">选择</a>
														
													</TD>
													<TD></TD>
												</TR>

												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>&nbsp;
										
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left>
												&nbsp;
												<span id="tabpage2_1">设置设计者</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%">
												<TR valign=top>
													<TD width=5></TD>
													<TD style="text-align:right;">
                                                    <div style="width:300px; border:1px solid #b3bcc3; overflow:hidden;">
														<select name="designer" id="designer" size="5"
															style="width:304px;margin:-2px;"></select></div>
														&nbsp;&nbsp;
														<a  href="#" style="margin-top:9px;" class="button" onClick="setTaskActors('designer')">选择</a>
														
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>&nbsp;
										
									</TD>
								</TR>
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left>
												&nbsp;
												<span id="tabpage2_1">设置启动者</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%">
												<TR valign=top>
													<TD width=0></TD>
													<TD style="text-align:right;">
                                                     <div style="width:300px; border:1px solid #b3bcc3; overflow:hidden;">
														<select name="startor" id="startor" size="5"
															style="width:304px;margin:-2;"></select></div>
														&nbsp;&nbsp;
														<a  href="#" style="margin-top:9px;" class="button" onClick="setTaskActors('startor')">选择</a>
														
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>&nbsp;
										
									</TD>
								</TR>
								<security:authorize ifAllGranted="001-0005000200010001">
								<TR valign=top>
									<TD></TD>
									<TD width="100%" valign=top>
										<Fieldset style="border: 1px solid #C0C0C0;">
											<LEGEND align=left>
												&nbsp;
												<span id="tabpage2_1">设置流程类型</span>&nbsp;
											</LEGEND>
											<TABLE border=0 width="100%" height="100%"
												>
												<TR valign=top>
													<TD width=5></TD>
													<TD>
														<s:checkbox id="rest1" name="model.rest1"></s:checkbox>
														<label for="rest1">全局流程</label>
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													</TD>
													<TD></TD>
												</TR>
												<TR height="3">
													<TD></TD>
													<TD></TD>
													<TD></TD>
												</TR>
											</TABLE>
										</Fieldset>
									</TD>
									<TD>&nbsp;
										
									</TD>
								</TR>
								</security:authorize>
								<TR height="100%">
									<TD></TD>
									<TD></TD>
									<TD></TD>
								</TR>
							</TABLE>
						</td>
					</tr>
					<tr>
						<td class="table1_td"></td>
						<td></td>
					</tr>
				</table>
			</form>
			<table width="90%" border="0" cellspacing="0" cellpadding="00">
				<tr>
					<td align="center" valign="middle">
					</td>
				</tr>
			</table>
		</DIV>
	</body>
</html>