<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/tags/c.tld" prefix="c"%>
<%@include file="/common/include/rootPath.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择人员</title>
		<link href="<%=frameroot%>/css/properties_windows_add.css" type='text/css'
			rel="stylesheet">
		<script src="<%=path%>/common/js/jquery/jquery-1.2.6.js"
			type="text/javascript"></script>
		<script language="javascript" type="text/javascript">
			window.name="MyModalDialog"; 
			var parentWin = window.dialogArguments;
			var type="";
	        if(parentWin!=undefined){
	        	if(parentWin.document.getElementById("type")!=undefined){
	        		type=parentWin.document.getElementById("type").value;
	        	}
	        }
			// 添加系统人员
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
			
			// 添加部门
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
			
			//是否选择系统人员
			function  removeOption(strName,strValue){ 
				var objSelect = document.getElementById("tempSendTo");  
	            for(var i=0; i<objSelect.length; i++){
	            	if(objSelect.options[i].value == strValue){
	            		objSelect.options[i] = null;
	            	}
	            }
			}  
			
			// 是否选择部门	
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
			}
			
			function submitFunction() {
				var objSelect = document.getElementById("tempSendTo");
				var groupId=document.getElementById("groupId").value;	
				var logo=document.getElementById("logo").value;
				var startSchedules="";
				var userId="${userId}";
				if(logo=="1"){
					startSchedules=document.getElementById("startSchedules").value;
					if(startSchedules==""){
						alert("起始班次不能为空！");
						document.getElementById("startSchedules").focus();
						return;
					}
				}
				//获取所有选择的系统人员
				var personId="";		//人员ID
				var personName = "";	//人员姓名
				var retValue="";
				if(type=="calAttend"){//计算考勤时的选择人员
					for(var j=0; j<objSelect.length; j++){				    
						personId=objSelect.options[j].value;	//人员ID
						personName=objSelect.options[j].text;	//人员姓名
						retValue=retValue+","+personId+"|"+personName;
					}
					if(retValue.length!=0){
						retValue=retValue.substring(1);
					}
					window.returnValue = retValue;
					window.close();
				}else{				//人员排班时的选择人员
					for(var j=0; j<objSelect.length; j++){				    
						personId+=","+objSelect.options[j].value;
					}
					if(personId.length != 0){
						personId=personId.substring(1);
					}
					if(type=="setPersonPrivil"){	//设置用户的人事人员权限
						$.ajax({
							type:"post",
							url:"<%=path%>/attendance/arrange/scheGroup!savePersonPrivil.action",
							data:{
								userId:userId,
								perOps:personId
							},
							success:function(data){	
								alert(data);
								window.close();
							},
							error:function(data){
								alert("对不起，操作异常"+data);
							}
						});
					}else{
						$.ajax({
							type:"post",
							url:"<%=path%>/attendance/arrange/scheGroup!saveArrange1.action",
							data:{
								groupId:groupId,
								perOps:personId,
								startSchedules:startSchedules
							},
							success:function(data){	
								alert("人员排班成功！");		
							},
							error:function(data){
								alert("对不起，操作异常"+data);
							}
						});
					}
				}	
			}
			
			function cancelFunction() {
				window.close();
			}
			
			function changeTree(treeFlag) {
			    if (treeFlag == 'Group') {
			    	//获取所有部门
			      document.all.selectsTree.src="<%=path%>/attendance/arrange/scheGroup!chooseTree.action?forward=org";
			    } else if(treeFlag == 'Public'){
			    	//获取系统人员
			      document.all.selectsTree.src="<%=path%>/attendance/arrange/scheGroup!chooseTree.action?forward=per";
			    }
			}
			
			//初始化人员选择
			function initSelect(user) {
                var druser = user.split(",");
                for (var i=0; i<druser.length; i++) {
                    var drusers=druser[i].split("|");
                    addOption(drusers[1], drusers[0]);
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
            
            function chosse(){
            	alert("请按人员选择！");
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
             function radioOrg(){
			    var audit=OpenWindow("<%=root%>/personnel/personorg/personOrg!radiotree.action",270,320,window);
			    if(audit==undefined||audit==null){
			      	return false;
			    }else{  
	             	$("#orgName").val(audit[1]);	
	             	$("#orgId").val(audit[0]);    
					document.all.myTable.action="<%=root%>/attendance/arrange/scheGroup!arrange.action";
					document.all.myTable.submit(); 
			    }
			}
            function gochange(){
            	document.all.myTable.submit(); 
            }     
	    </script>
	</head>
	<body class="contentbodymargin">

		<div id="contentborder" cellpadding="0">
			<center>
				<s:form id="myTable" theme="simple"
					action="/attendance/arrange/scheGroup!arrange1.action"
					target="MyModalDialog">
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
											<strong>
															<SCRIPT type="text/javascript">
																if(type=="calAttend"){//计算考勤时选择人员
																	document.write("选择人员");
																}else if(type=="setPersonPrivil"){	//设置人事人员权限
																	document.write("选择人员");
																}else{
																	document.write("人员排班");
																}
															</SCRIPT>	 
														</strong>
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
						<s:if test="model.logo!=null&&model.logo==\"1\"">
							<tr>
								<td>
									&nbsp;&nbsp;
									<span class="wz">起始班次(<FONT color="red">*</FONT>)：</span>
									<s:select list="scheduleMap" id="startSchedules"
										name="startSchedules" listKey="key" listValue="value"
										headerKey="" headerValue="请选择起始班次"
										onchange="gochange(this.value)" />
								</td>

							</tr>
						</s:if>
						<tr>
							<td id="treeIframe" align="center">
								<table border="0" width="95%" bordercolor="#FFFFFF"
									cellspacing="0" cellpadding="0" height="100%">
									<tr>
										<td width="48%" align="middle">
											<table border="0" width="98%" height="98%"
												bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
												<tr>
													<td width="100%" height="100%">
														<div id="SelectsDiv" style="display: ">
															<iframe name="selectsTree"
																src="<%=path%>/attendance/arrange/scheGroup!chooseTree.action?orgId=${param.orgId}&forward=per"
																frameborder="0" scrolling='no' width='100%'
																height='100%' style="display: "></iframe>
														</div>
													</td>
												</tr>
											</table>
										</td>
										<td width="5%" align="middle">
											<a href="#" class="button" onClick="deleteOptionItem();">&lt;&lt;--</a>
										</td>
										<td width="42%" align="middle" valign="top">
											<select name="select" size="100" multiple="multiple"
												id="tempSendTo" style="width: 170px; height:440px;"></select>
											<input type="hidden" id="perOps" name="perOps"
												value="${perOps}">
											<input type="hidden" id="groupId" name="groupId"
												value="${groupId}">
											<input type="hidden" id="logo" name="model.logo"
												value="${model.logo}">
											<script type="text/javascript"> 
                                            var perOps=document.getElementById("perOps").value;
                                            if(type=="calAttend"){//如果是计算考勤时选择人员
                                            	perOps=parentWin.document.getElementById("personOpts").value;//获取父窗口的值
                                            }
                                           	if(perOps != ""){
                                           		initSelect(perOps);
                                           	}                                                        
                                        </script>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</s:form>
			</center>
		</div>
	</body>
</html>

