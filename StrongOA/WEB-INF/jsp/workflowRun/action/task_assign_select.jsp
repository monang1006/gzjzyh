<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib uri="/tags/c.tld" prefix="c" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;">
		<title>选择任务处理人</title>
		<link href="<%=frameroot%>/css/properties_windows.css" type='text/css'
			rel="stylesheet">
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
				* 添加部门
				
				*
			*/
			function  addGroupOption(strName, strValue){ 
			   var objSelect = document.getElementById("tempSendTo_group"); 
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
			
			/*
				* 是否选择部门
				
				*
			*/
			function  removeGroupOption(strName,strValue){ 
				var objSelect = document.getElementById("tempSendTo_group");  
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
				
			//	var objSelectGroup = document.getElementById("tempSendTo_group");
			//	if(objSelectGroup.selectedIndex >= 0){
			//		selectsTree.selectedFunc(false,objSelectGroup.options[objSelectGroup.selectedIndex].value);
			//		objSelectGroup.remove(objSelectGroup.selectedIndex);
			//	}
			}
			
			function submitFunction() {
				
				var maxActors = "${maxTaskActors}";
				var objSelect = document.getElementById("tempSendTo");
				if(objSelect.length > parseInt(maxActors)){
					alert("允许最大参与人数为" + maxActors + "人，目前选择人数大于" + maxActors + "人，请重新选择！");
					return false;
				}
				
			//	var objSelectGroup = document.getElementById("tempSendTo_group");
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
					retValue = retValue+userId+"|"+userName+",";
				}
				
				//获取所有选择的部门
			//	for(var i = 0;i<objSelectGroup.length;i++){
			//		groupName = objSelectGroup.options[i].text;
			//		groupId = objSelectGroup.options[i].value.substring(1);
			//		retGroupValue = retGroupValue+groupId+","+groupName+"|";
			//	}
				
				//选择了系统人员
				if (retValue.length != 0) {
				  	retValue = retValue.substring(0, (retValue.length-1));
/**				  	if(retGroupValue.length != 0){//选择了部门
						retGroupValue = retGroupValue.substring(0,(retGroupValue.length-1));
						retAllValue = retValue+"$"+retGroupValue;
						type = "1";
					}else{//没有选择部门
						retAllValue = retValue;
						type = "2";
					}**/
				}else{//没有选择系统人员
/**					if(retGroupValue.length != 0){//选择了部门
						retGroupValue = retGroupValue.substring(0,(retGroupValue.length-1));
						retAllValue = retGroupValue;
						type = "3";
					}else{//没有选择部门**/
						retValue = "";
						type = "4";
//					}
				} 
//				window.returnValue = retAllValue+"$"+type;
				window.returnValue = retValue;
				window.close();
			}
			
			function cancelFunction() {
				window.close();
			}
			
			function changeTree(treeFlag) {
			    var objSelect = document.getElementById("tempSendTo");
			    if (treeFlag == 'Default') {
			    	//获取默认设置
			      document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=default&nodeId="+nodeId;
			    } else if(treeFlag == 'Public'){
			    	//获取系统人员
			      document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=user&nodeId="+nodeId+"&taskId="+taskId;	
			    }else if(treeFlag == 'Preactor'){
				    var length = objSelect.length;
				    var zero = 0;
		            for(var i=0; i < length; i++){	                
	            		objSelect.options[zero] = null;
		            }			    
			    	//获取前一步处理人
			      document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=preactor&nodeId="+nodeId;	
			    }else if(treeFlag == 'Other'){
			    	//任意选择其他人
					document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=other&nodeId="+nodeId;			    
			    }
			}
			
			//初始化人员选择
			function initSelect(drusers, drusernames) {
				if(drusers == 'd0'){
					if((drusers != null) && (drusers != "")){
	            		addOption(drusernames, drusers);
	            	}
	            	document.all.tree[1].checked = "true";
			      	document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=default&nodeId="+nodeId;
				}else if(drusers == 'p0'){
					if((drusers != null) && (drusers != "")){
	            		addOption(drusernames, drusers);
	            	}
	            	document.all.tree[2].checked = "true";					
			      	document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=preactor&nodeId="+nodeId;					
				}else{
					if((drusers != null) && (drusers != "")){
		                var druser = drusers.split(",");
		                var drusername = drusernames.split(",");
		                for (var i=0; i<druser.length; i++) {
		                    addOption(drusername[i], druser[i]);
		                }
	                }
	            	document.getElementsByName("tree")[0].checked = "true";	                
			      	document.all.selectsTree.src="<%=root%>/workflowRun/action/runUserSelect!chooseTree.action?setType=user&nodeId="+nodeId+"&taskId="+taskId+"&workflowName=${workflowName}&transitionId=<%=request.getAttribute("transitionId")%>";                
                }
            }
            
            //初始化部门选择
			function initSelectGroup(dgroups, dgroupnames) {
                var dgroup = dgroups.split(",");
                var dgroupname = dgroupnames.split(",");
                for (var i=0; i<dgroup.length; i++) {
                    addGroupOption(dgroupname[i], "c"+dgroup[i]);
                }
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
		<div id="contentborder" cellpadding="0">
			<center>
				<table border="0" cellspacing="0" width="100%" height="90%"
					bordercolordark="#FFFFFF" bordercolorlight="#000000"
					bordercolor="#333300" cellpadding="2">
					<tr>
							<td align="" class="table_headtd">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td class="table_headtd_img" >
											<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
										</td>
										<td align="left">
											<strong>选择处理人</strong>
										</td>
										<td align="right">
											<table border="0" align="right" cellpadding="00" cellspacing="0">
								                <tr>
								                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
								                 	<td class="Operation_input" onClick="submitFunction();">&nbsp;确&nbsp;定&nbsp;</td>
								                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_r.gif"/></td>
							                  		<td width="5"></td>
								                 	<td width="8"><img src="<%=frameroot%>/images/ch_z_l.gif"/></td>
								                 	<td class="Operation_input1" onClick="cancelFunction();">&nbsp;关&nbsp;闭&nbsp;</td>
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
						<td style="padding-left:20px;">
							<input type="radio" name="tree" checked="checked" onclick="changeTree('Public');"
								value="Public" />
							选择人员
<!--							<input type="radio" name="tree" onclick="changeTree('Group');"-->
<!--								 value="Group" />
								 	<input type="radio" name="tree" onclick="changeTree('Default');"
								 value="Default" />
							&nbsp;&nbsp;
							默认设置
-->
							<% if("1".equals(request.getAttribute("isSelectOtherActors"))){ %>
								 	<input type="radio" name="tree" onclick="changeTree('Other');"
								 value="Other" />
							选择其他人
							<%}
							  if("1".equals(request.getAttribute("preActor"))){ %>														
								 	<input type="radio" name="tree" onclick="changeTree('Preactor');"
								 value="Preactor" />
							曾经处理人
							<%} %>							
						</td>
					</tr>
					<tr >
						<td id="treeIframe"  align="center" style="padding-top:10px;">
							<table border="0" width="95%" bordercolor="#FFFFFF"
								cellspacing="0" cellpadding="0" height="100%">
								<tr>
									<td width="45%" align="middle">
										<table border="0" width="98%" height="98%"
											bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%">
													<div id="SelectsDiv" style="display: ">
														<iframe name="selectsTree" id="selectsTree"
															src=""
															frameborder="0" scrolling='no' width='100%' height='100%'
															style="display: "></iframe>
													</div>
												</td>
											</tr>
										</table>
									</td>
									<td width="5%" align="middle">&nbsp;&nbsp;&nbsp;
										<input type="button" value="&lt;&lt;--"
											onclick="deleteOptionItem();" />
									</td>
									<td width="45%" align="middle" valign="top">
										<select name="select" size="100" multiple="multiple" 
												id="tempSendTo" style="width: 170px; height: 440px"></select>
<!--										<select name="select_group" size="100" multiple="multiple"-->
<!--												id="tempSendTo_group" style="width: 180px; height: 170px">-->
<!--											</select>-->
											<script>
														var nodeId = "<%=request.getParameter("nodeId")%>";
														var taskId = "<%=request.getParameter("taskId")%>";
														
														var taskActors = '${taskActors}';
														
														var name = "";
														var value = "";
														
														if(taskActors != null && taskActors != ""){
															var actors = taskActors.split(",");
															
															for(var i = 0; i < actors.length; i++){
																name = name + "," + actors[i].split("|")[1];
																value = value + "," + actors[i].split("|")[0];
															}
															
															name = name.substring(1);
															value = value.substring(1);
														}
														
														initSelect(value, name);														
														
                                                       // var parentWin = window.dialogArguments;
                                                        
                                                      //  var name = parentWin.document.getElementById("u"+nodeId).value;
                                                      //  var value = parentWin.document.getElementById("n"+nodeId).value;
                                                        
                                                        
                                                      //      initSelect(value, name);

                                                    </script>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					
				</table>
			</center>
		</div>
	</body>
</html>