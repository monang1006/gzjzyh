<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择人员</title>
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
				* 添加组织机构
				
				*
			*/
			function  addOrgOption(strName, strValue){ 
			   var objSelect = document.getElementById("tempSendTo_org"); 
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
				* 是否选择组织机构
				
				*
			*/
			function  removeOrgOption(strName,strValue){ 
				var objSelect = document.getElementById("tempSendTo_org");  
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
				
				var objSelectOrg = document.getElementById("tempSendTo_org");
				if(objSelectOrg.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelectOrg.options[objSelectOrg.selectedIndex].value);
					objSelectOrg.remove(objSelectOrg.selectedIndex);
				}				
			}
			
			function submitFunction() {
				var objSelect = document.getElementById("tempSendTo");
				var objSelectGroup = document.getElementById("tempSendTo_group");
				var objSelectOrg = document.getElementById("tempSendTo_org");
				var userName = "";//人员姓名
				var userId = "";//人员ID
				var groupName = "";//部门名称
				var groupId = "";//部门编号
				var type = "";//标志选择人员的类型（人员+部门，人员，部门，None）
				var retValue = "";//所有选择的人员信息（ID|姓名）
				var retOrgValue = ""; //所有选择的组织机构（ID|名称）
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
					groupId = objSelectGroup.options[i].value;
					retGroupValue = retGroupValue+groupId+","+groupName+"|";
				}
				
				//获取所有选择的组织结构
				for(var j=0; j<objSelectOrg.length; j++){				    
					orgName =  objSelectOrg.options[j].text;
					orgId  = objSelectOrg.options[j].value;
					retOrgValue = retOrgValue + orgId + "," + orgName+"|";
				}				
				
				if(retOrgValue.length != 0){
					retOrgValue = retOrgValue.substring(0, retOrgValue.length-1);
					retAllValue = retAllValue + retOrgValue + "|";
				}
				if(retGroupValue.length != 0){
					retGroupValue = retGroupValue.substring(0, retGroupValue.length-1);
					retAllValue = retAllValue + retGroupValue + "|";
				}
				if(retValue.length != 0){
					retValue = retValue.substring(0, retValue.length-1);
					retAllValue = retAllValue + retValue;
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

			    if (treeFlag == 'Pos') {
			    	//获取所有部门
			      	document.all.selectsTree.src = "<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=pos";
			    } else if(treeFlag == 'Per'){
			    	//获取具体人员
			      	document.all.selectsTree.src = "<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=per";
			    } else if(treeFlag == 'Org' ){
			    	//获取组织机构
			    	document.all.selectsTree.src = "<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=org";
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
            
            //初始化机构选择
			function initSelectOrg(dorgs, dorgnames) {
                var dorg = dorgs.split("|");
                var dorgname = dorgnames.split("|");
                for (var i=0; i<dorg.length; i++) {
                    addOrgOption(dorgname[i], dorg[i]);
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
            	}else if(flag == 'process_org_select'){
            		var objSelect = document.getElementById("tempSendTo_org");
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
								<tr><td>&nbsp;</td>
									<td valign='middle'>
										<img src='<%=root%>/images/ico/tb-change.gif'
											align='absmiddle' border="0" alt="">
										&nbsp;
										<strong>请选择</strong>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="tree" checked="checked"
								onclick="changeTree('Org');" value="Org" />
							按机构
							<input type="radio" name="tree" onclick="changeTree('Pos');"
								value="Pos" />
							按岗位
							<input type="radio" name="tree" onclick="changeTree('Per');"
								value="Per" />
							按人员

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
															src="<%=path%>/workflowDesign/action/userSelect!chooseTree.action?flag=org"
															frameborder="0" scrolling='no' width='100%' height='100%'
															style="display: ">
														</iframe>
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
										<select name="select_org" size="100" multiple="multiple"
											id="tempSendTo_org" style="width: 180px; height: 130px">
										</select>
										<select name="select_group" size="100" multiple="multiple"
											id="tempSendTo_group" style="width: 180px; height: 130px">
										</select>
										<select name="select" size="100" multiple="multiple"
											id="tempSendTo" style="width: 180px; height: 130px">
										</select>
										<script type="text/javascript">
                                                        var parentWin = window.dialogArguments;
                                                       	
                                                       	var orgOps = "";
                                                       	var orgOpValue = "";
                                                       	var posOps = "";
                                                       	var posOpValue = "";
                                                       	var perOps = "";
                                                       	var perOpValue = "";
                                                       	
                                                        var pageType = "${pageType}";
                                                        var value = "";
                                                        if(pageType == "owner"){
                                                        	value = parentWin.tempOwner;
                                                        }else if(pageType == "designer"){
                                                        	value = parentWin.tempDesigner;
                                                        }else if(pageType == "startor"){
                                                        	value = parentWin.tempStartor;
                                                        }
														if(value != ""){
															var startorDetails;
															var startors = value.split("|");
															for(var i=0; i<startors.length; i++){
																startorDetails = startors[i].split(",");
																var flag = startorDetails[0].substring(0,1);
																if(flag == "o"){
																	orgOps = orgOps + startorDetails[0] + "|";
																	orgOpValue = orgOpValue + startorDetails[1] + "|";
																}else if(flag == "p"){
																	//若是新的权限系统则应是 posOps + startorDetails[0].substring(1) + "," + startorDetails[1] + "|";
																	posOps = posOps + startorDetails[0] + "|";
																	posOpValue = posOpValue + startorDetails[1] + "|";				
																}else if(flag == "u"){
																	//若是新的权限系统则应是 perOps + startorDetails[0].substring(1) + "," + startorDetails[1] + "|";
																	perOps = perOps + startorDetails[0] + "|";
																	perOpValue = perOpValue + startorDetails[1] + "|";
																}
															}
														}
														
														if(orgOps != ""){
															initSelectOrg(orgOps.substring(0,orgOps.length-1), orgOpValue.substring(0,orgOpValue.length-1));
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
