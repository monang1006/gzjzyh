<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择任务处理人</title>
		<link href="<%=root%>/workflow/css/windows.css" type="text/css"
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
				
				var objSelectGroup = document.getElementById("tempSendTo_group");
				if(objSelectGroup.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelectGroup.options[objSelectGroup.selectedIndex].value);
					objSelectGroup.remove(objSelectGroup.selectedIndex);
				}
			}
			
			function submitFunction() {
				var objSelect = document.getElementById("tempSendTo");
				var objSelectGroup = document.getElementById("tempSendTo_group");
				var userName = "";//人员姓名
				var userId = "";//人员ID
				var groupName = "";//部门名称
				var groupId = "";//部门编号
				var type = "";//标志选择人员的类型（人员+部门，人员，部门，None）
				var retValue = "";//所有选择的人员信息（ID|姓名）
				var retGroupValue = "";//所有选择的部门信息（ID|名称）
				var retAllValue = "";//人员信息和部门信息（retValue+"$"+retGroupValue）
				
				
				//获取所有选择的系统人员
				for(var j=0; j<objSelect.length; j++){				    
					userName =  objSelect.options[j].text;
//					userId  = objSelect.options[j].value.substring(1);//去掉c或d，这里的的value值为c“id”或d“id”
					userId  = objSelect.options[j].value;
//					retValue = retValue+userId+","+userName+"|";
					retValue = retValue+userId+","+userName+"|";
				}
				
				//获取所有选择的部门
				for(var i = 0;i<objSelectGroup.length;i++){
					groupName = objSelectGroup.options[i].text;
//					groupId = objSelectGroup.options[i].value.substring(1);
					groupId = objSelectGroup.options[i].value;
//					retGroupValue = retGroupValue+groupId+","+groupName+"|";
					retGroupValue = retGroupValue+groupId+","+groupName+"|";
				}
				
				if(retValue.length != 0){
					retValue = retValue.substring(0, retValue.length-1);
					retAllValue = retAllValue + retValue + "|";
				}
				if(retGroupValue.length != 0){
					retGroupValue = retGroupValue.substring(0, retGroupValue.length-1);
					retAllValue = retAllValue + retGroupValue + "|";
				}
				if(retAllValue != "" && retAllValue.substring(retAllValue.length-1) == "|"){
					retAllValue = retAllValue.substring(0, retAllValue.length-1);
				}				
				 
				window.returnValue = retAllValue;
				window.close();
			}
			
			function cancelFunction() {
				window.close();
			}
			
			function changeTree(treeFlag) {
			    /*var objSelect = document.getElementById("tempSendTo");
			    
			    var length = objSelect.length
			    var zero = 0;
	            for(var i=0; i < length; i++){	                
            		objSelect.options[zero] = null;
	            }*/

			    if (treeFlag == 'Group') {
			    	//获取所有部门
			      document.all.selectsTree.src="<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=pos";
			    } else if(treeFlag == 'Public'){
			    	//获取系统人员
			      document.all.selectsTree.src="<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=per";
			    }else if(treeFlag == 'Relative'){
			    	document.all.selectsTree.src = "<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=rel";
			    }
			}
			
			//初始化人员选择
			function initSelect(drusers, drusernames) {
                var druser = drusers.split("|");
                var drusername = drusernames.split("|");
                for (var i=0; i<druser.length; i++) {
                    addOption(drusername[i], druser[i]);
                }
            }
            
            
            //初始化部门选择
			function initSelectGroup(dgroups, dgroupnames) {
                var dgroup = dgroups.split("|");
                var dgroupname = dgroupnames.split("|");
                for (var i=0; i<dgroup.length; i++) {
                    addGroupOption(dgroupname[i], dgroup[i]);
                }
            }
            
            //初始化frame中checked选择
            function frameInitChecked(flag){
            	var page = document.frames[0];
            	if(flag == 'node_person_select'){
                    var objSelect = document.getElementById("tempSendTo");
            		page.initChecked(objSelect);
            	}else if(flag == 'node_post_select'){
            		var objSelect = document.getElementById("tempSendTo_group");
            		page.initChecked(objSelect);
            	}
            }            
	    </script>
	</head>
	<body class="contentbodymargin">
		<br>
		<div id="contentborder" cellpadding="0">
			<center>
				<table border="1" cellspacing="0" width="95%" height="90%"
					bordercolordark="#FFFFFF" bordercolorlight="#000000"
					bordercolor="#333300" cellpadding="2">
					<tr>
						<td width="100%">
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td valign='middle'></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td valign='middle'>
										<img src='<%=root%>/images/ico/tb-change.gif'
											align='absmiddle' border="0" alt="">
										&nbsp;
										<strong>选择处理人</strong>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tree" checked="checked"
								onclick="changeTree('Public');" value="Public" />
							人员选择
							<input type="radio" name="tree" onclick="changeTree('Group');"
								value="Group" />
							岗位选择
							<input type="radio" name="tree" onclick="changeTree('Relative');"
								value="Relative" />
							其他选择
						</td>
					</tr>
					<tr>
						<td id="treeIframe">
							<table border="0" width="95%" bordercolor="#FFFFFF"
								cellspacing="0" cellpadding="0" height="100%">
								<tr>
									<td width="45%" align="middle">
										<table border="0" width="95%" height="95%"
											bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%">
													<div id="SelectsDiv" style="display: ">
														<iframe name="selectsTree"
															src="<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=per"
															frameborder="0" scrolling='no' width='100%' height='100%'
															style="display: "></iframe>
													</div>
												</td>
											</tr>
										</table>
									</td>
									<td width="5%" align="middle">
										<input type="button" value="&lt;&lt;--"
											onclick="deleteOptionItem();" />
									</td>
									<td width="45%" align="middle" valign="top">
										<select name="select" size="100" multiple="multiple"
											id="tempSendTo" style="width: 180px; height: 200px"></select>
										<select name="select_group" size="100" multiple="multiple"
											id="tempSendTo_group" style="width: 180px; height: 200px">
										</select>
										<script type="text/javascript">
                                                        var parentWin = window.dialogArguments;
                                                        var value = parentWin.document.getElementsByName("handleActor")[0].value;
                                                       	var posOps = "";
                                                       	var posOpValue = "";
                                                       	var perOps = "";
                                                       	var perOpValue = "";
                                                        
														if(value != ""){
															var startorDetails;
															var startors = value.split("|");
															for(var i=0; i<startors.length; i++){
																startorDetails = startors[i].split(",");
																var flag = startorDetails[0].substring(0,1);
																if(flag == "p"){
																	//若是新的权限系统则应是 posOps + startorDetails[0].substring(1) + "," + startorDetails[1] + "|";
																	posOps = posOps + startorDetails[0] + "|";
																	posOpValue = posOpValue + startorDetails[1] + "|";				
																}else if(flag == "u" || flag == "s"){
																	//若是新的权限系统则应是 perOps + startorDetails[0].substring(1) + "," + startorDetails[1] + "|";
																	perOps = perOps + startorDetails[0] + "|";
																	perOpValue = perOpValue + startorDetails[1] + "|";
																}
															}
														}
														if(posOps != ""){
															initSelectGroup(posOps.substring(0,posOps.length-1), posOpValue.substring(0,posOpValue.length-1));
														}                  
                                                       
                                                       	if(perOps != ""){
                                                       		initSelect(perOps.substring(0,perOps.length-1), perOpValue.substring(0,perOpValue.length-1));
                                                       	}                                                        
                                                    </script>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr height="5%">
						<td>
							<input type="button" class="input_bg" value="确定"
								onclick="submitFunction();" />
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" class="input_bg" value="取消"
								onclick="cancelFunction();" />
						</td>
					</tr>
				</table>
			</center>
		</div>
	</body>
</html>