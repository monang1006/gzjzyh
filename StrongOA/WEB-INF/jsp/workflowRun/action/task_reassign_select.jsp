<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.strongit.oa.util.GlobalBaseData"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>
<%@taglib uri="/tags/web-remind" prefix="remind"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;">
		<title>选择任务处理人</title>
		<link href="<%=root%>/workflow/css/windows.css" type="text/css"
			rel="stylesheet">
		<link href="<%=frameroot%>/css/properties_windows_add.css" type='text/css'
			rel="stylesheet">
	    <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script language="javascript" type="text/javascript">
		
			/*
				* 添加系统人员
				
				*
			*/
			function  addOption(strName, strValue){ 
			   var objSelect = document.getElementById("tempSendTo"); 
			   var aleadyHave = false;
			   for(var i = 0; i<objSelect.length; i++){
			   		if(objSelect.options[i].value == strValue){
			   			aleadyHave = true;
			   			break;
			   		}
			   }
			   
		       if(aleadyHave == false){
		      		var  objOption  =  new  Option(strName,strValue);  
		      		objSelect.options.add(objOption, objSelect.options.length);
		      	}
			}
			
			/*
				* 是否选择系统人员
				
				*
			*/
			function  removeOption(strName,strValue){ 
				var objSelect = document.getElementById("tempSendTo");  
	            for(var i=0; i<objSelect.length; i++){
	            	if(objSelect.options[i].value == strValue){
	            		objSelect.options[i] = null;
	            	}
	            }
			}  
			
			function deleteOptionItem(){
				var objSelect = document.getElementById("tempSendTo");
				if(objSelect.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelect.options[objSelect.selectedIndex].value);
					objSelect.remove(objSelect.selectedIndex);
				}

			}
			
			function submitFunction() {
				
				var maxActors = "${maxTaskActors}";
				var objSelect = document.getElementById("tempSendTo");
				if(objSelect.length == 0){
					alert("请选择指派人员。");
					return ;
				}
				if(objSelect.length > 1){
					alert("任务指派只能选择1个人，请重新选择！");
					//alert("允许最大参与人数为1人，目前选择人数大于1人，请重新选择！");
					return false;
				}
				
				var userName = "";//人员姓名
				var userId = "";//人员ID
				var groupName = "";//部门名称
				var groupId = "";//部门编号
				var type = "";//标志选择人员的类型（人员+部门，人员，部门，None）
				var retValue = "";//所有选择的人员信息（ID|姓名）
				var retGroupValue = "";//所有选择的部门信息（ID|名称）
				var retAllValue;//人员信息和部门信息（retValue+"$"+retGroupValue）
				
				//获取所有选择的系统人员
				for(var j=0; j<objSelect.length; j++){				    
					userName =  objSelect.options[j].text;
					userId  = objSelect.options[j].value;//.substring(1);//去掉c或d，这里的的value值为c“id”或d“id”
					retValue = retValue+ userId + ","+userName+",";
				}
				
				//选择了系统人员
				if (retValue.length != 0) {
				  	retValue = retValue.substring(0, (retValue.length-1));
				}else{//没有选择系统人员
						retValue = "";
						type = "4";
				}
				
				//添加返回信息
				if(retValue != ""){
					var needreturn = document.getElementById("needReturn");
					if(needreturn.checked == true){
						retValue = retValue + ",1";
					}else{
						retValue = retValue + ",0";
					}
				}
				
				var remindType = "";
				$("#StrRem").find("input:checkbox:checked").each(function(){
					remindType = remindType + $(this).val() + ",";
				});
				if(remindType!=""){
			    	remindType = remindType.substring(0,remindType.length-1);
				}
				var returnValue = "{users:'"+retValue+"',remindType:'"+remindType+"'}";
				window.returnValue = returnValue;
				window.close();
			}
			
			function cancelFunction() {
				window.close();
			}
            
            function chosse(){
            	alert("请按联系人选择！");
            }
            
            //初始化frame中checked选择
            function frameInitChecked(flag){
            	var page = document.frames[0];
            	if(flag == 'instance_select'){
                    var objSelect = document.getElementById("tempSendTo");
            		page.initChecked(objSelect);
            	}
            }
            
            
	    </script>
	</head>
	<body class="contentbodymargin">
		
		<div id="contentborder" cellpadding="0" style="overFlow-y:hidden; ">
		
			<center>
			   <table width="100%" height="100%;" border="0" cellpadding="0" cellspacing="0">
				<tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<c:if test="${allowChangeMainActor != \"1\"}">
											<strong>选择任务指派人</strong>
										</c:if>
										<c:if test="${allowChangeMainActor == \"1\"}">
											<strong>选择主办人员</strong>
										</c:if>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="submitFunction();">&nbsp;确&nbsp;定&nbsp;</td>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
				                  		<td width="5"></td>
					                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
					                 	<td class="Operation_input1" onClick="cancelFunction();">&nbsp;取&nbsp;消&nbsp;</td>
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
						<td id="treeIframe">
						 
                        <div style="padding-left:5px; padding-top:15px; ">
							<table border="0" width="100%" bordercolor="#FFFFFF"
								cellspacing="0" cellpadding="0" height="100%">
								<tr height=80%>
									<td width="45%" align="middle" valign="top">
										<table border="0" width="95%" height="450px"
											bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="400px" valign="top">
													<div id="SelectsDiv" >
														<iframe name="selectsTree" id="selectsTree"
															src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?dispatch=reassign&setType=user&nodeId=0&taskId=${taskId}"															frameborder="0" scrolling='no' width='100%' height='100%'
															style="display: "></iframe>
														</iframe>
													</div>
												</td>
											</tr>
										</table>
									</td>
									<td width="5%" align="middle">
									    <a   href="#" class="button" onClick="deleteOptionItem();" >&lt;&lt;--</a><br><br>
										
									</td>
									<td  width="45%" align="middle" valign="top">
										<select name="select" size="100" multiple="multiple" 
												id="tempSendTo" style="width: 220px; height: 460px"></select>
												
										<br/>
										<br/>
										<!-- 标识主办变更 -->
										<c:if test="${allowChangeMainActor != \"1\"}">
										<p style="display: none;">
										</c:if>
										<c:if test="${allowChangeMainActor == \"1\"}">
										<p style="display: none;">
										</c:if>
										<!-- 需要指派返回 -->
										<input type="checkbox" id="needReturncheckbox" name="needReturn" value="1"><label for="needReturncheckbox">需要指派返回请勾选此项</label>
										</p>
										
										
													<script>
														var nodeId = "${nodeId}";
														var taskId = "${taskId}";
                                                    </script>
                                                  </td>
								</tr>
								<tr height=20%><td colspan="3" id="StrRem">
								  <span class="wz">提醒方式：</span>
									<%--<remind:remind msgChecked="checked" isOnlyRemindInfo="true" code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>--%>										
									 <remind:remind  isShowButton="false" isOnlyRemindInfo="true" includeRemind="RTX,SMS" rtxChecked="checked"  code="<%=GlobalBaseData.SMSCODE_WORKFLOW %>"/>
									 </br>
									 </br>
									 </td>
								</tr>
							</table>
							</div>
						</td>
					</tr>
					
				</table>
			</center>
		</div>
	</body>
</html>