<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/tags/c.tld" prefix="c"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@include file="/common/include/rootPath.jsp"%>
<html>
	<head>
		<%@include file="/common/include/meta.jsp"%>
		<title>选择人员</title>
		<link href="<%=root%>/workflow/css/windows.css" type="text/css"
			rel="stylesheet">
			<LINK href="<%=frameroot%>/css/properties_windows_add.css" type=text/css rel=stylesheet>
		<script type="text/javascript"
			src="<%=path%>/common/js/jquery/jquery-1.2.6.js"></script>
		<script src="<%=path%>/common/js/upload/jquery.MultiFile.js"
			type="text/javascript" language="javascript"></script>
		<script language='javascript'
			src='<%=path%>/common/js/grid/ChangeWidthTable.js'></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js"
			type="text/javascript" language="javascript"></script>
		<script src="<%=path%>/common/js/common/common.js"
			type="text/javascript"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/service.js"></script>
		<script language="JavaScript"
			src="<%=path%>/common/js/commontab/workservice.js"></script>
        <script src="<%=path%>/common/js/jquery/jquery-1.2.6.js" type="text/javascript"></script>
		<script src="<%=path%>/common/js/upload/jquery.blockUI.js" language="javascript"></script>	
	
		<script language="javascript" type="text/javascript">
		
			/*
				* 添加系统人员
				
				*
			*/
			var geshu2=0;
			var geshu3=0;
			function  addOption(flag, strName, strValue,geshu1){
				var foramulaId=document.getElementById("foramulaId").value;
				var privilsortId=strValue;
				var objSelect;
				if(flag=="org"){
				  objSelect = document.getElementById("tempSendTo_org"); 
				}else if(flag=="post"){
					objSelect = document.getElementById("tempSendTo_group"); 
				}else if(flag=="person"){
					objSelect = document.getElementById("tempSendTo"); 
				}else if(flag=="role"){
					objSelect = document.getElementById("select_role"); 
				}
				if(flag=="person"){
						privilsortId=strValue.substring(1);
					}
				if(privilsortId.length>32){
					alert("privilsortId:"+privilsortId);
					privilsortId=privilsortId.subString(1);
				}
				var privalSort=document.getElementById("privalSort").value;
				if(privalSort=="modelPrival"){
					$.ajax({  type : "post",  
						      url:'<%=path%>/viewmodel/modelPrival!isHasSelect.action',
					          data: {'privilsortId':privilsortId,'foramulaId':foramulaId},
					          async : false, 
					          success:function(data){			              
			                    if(data=="sucess"){
			              	     var aleadyHave = false;
						        for(var i = 0; i<objSelect.length; i++){
						   		   if(objSelect.options[i].value == strValue){
						   			aleadyHave = true;
						   			break;
						   		}
						   }
						     if(aleadyHave == false){
					    	   if(flag!="role"&&geshu1>10&&geshu1!=0){
					    	   show("加载中，请稍等...");}
					      		var  objOption  =  new  Option(strName,strValue);  
					      		objSelect.options.add(objOption, objSelect.options.length);
					      	}

		              }else{
		                if(confirm("当前选择的【"+strName+"】已经设置了其它视图，是否替换该视图？")){
		                	$.ajax({
								type:"post",
								url:"<%=path%>/viewmodel/modelPrival!replace.action",
								data:{
									'privilsortId':privilsortId,'foramulaId':foramulaId			
									},
							    async : false,	
								success:function(data){	
									if(flag!="role"&&geshu1>10&&geshu1!=0){
								    	   show("加载中，请稍等...");}
									var  objOption  =  new  Option(strName,strValue);  
						      		objSelect.options.add(objOption, objSelect.options.length);
						      		},
								error:function(data){
									alert("对不起，操作异常"+data);
								}
							});
		                	
		                }else{
		                	geshu2=geshu2+1;
		                	geshu3=geshu1-geshu2;
		                	var page = document.frames[0];
			                   page.deleteSelectedNodeItem(strValue);
			                   return ;
		                }
		                  
		                
		              }
					          }
			       });
					
					if(objSelect.options.length==geshu3||objSelect.options.length==geshu1){
		      			hidden();
		      		}
				}else if(privalSort=="protalPrival"){
					var objSelect;
					if(flag=="org"){
					  objSelect = document.getElementById("tempSendTo_org"); 
					}else if(flag=="post"){
						objSelect = document.getElementById("tempSendTo_group"); 
					}else if(flag=="person"){
						objSelect = document.getElementById("tempSendTo"); 
					}else if(flag=="role"){
						objSelect = document.getElementById("select_role"); 
					}
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
			  	
			  

			}
			function show(i){
				$.blockUI({ overlayCSS: { backgroundColor: '#B3B3B3'}});
				$.blockUI({ message: '<span class="wz">'+i+'</span>',css:{width:'160px',height:'25px'}});
				
			}
			function hidden(){
				$.unblockUI();
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
			function  removeOption(flag,strName,strValue){ 
				var objSelect;
				if(flag=="org"){
				  objSelect = document.getElementById("tempSendTo_org"); 
				}else if(flag=="post"){
					objSelect = document.getElementById("tempSendTo_group"); 
				}else if(flag=="person"){
					objSelect = document.getElementById("tempSendTo"); 
				}else if(flag=="role"){
					objSelect = document.getElementById("select_role"); 
				}
			
				//var objSelect = document.getElementById("tempSendTo");  
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
			
			//取消选中
			function deleteOptionItem(){
				var objSelectOrg = document.getElementById("tempSendTo_org");
				while(objSelectOrg.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelectOrg.options[objSelectOrg.selectedIndex].value);
					objSelectOrg.remove(objSelectOrg.selectedIndex);

				}	
				
				
				
				var objSelect = document.getElementById("tempSendTo");
				while(objSelect.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelect.options[objSelect.selectedIndex].value);
					objSelect.remove(objSelect.selectedIndex);
				}
				
<%--				var objSelectGroup = document.getElementById("tempSendTo_group");
				if(objSelectGroup.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelectGroup.options[objSelectGroup.selectedIndex].value);
					objSelectGroup.remove(objSelectGroup.selectedIndex);
				}--%>
				
				
				var objSelectRole = document.getElementById("tempSendTo_role");
				while(objSelectRole.selectedIndex >= 0){
					selectsTree.selectedFunc(false,objSelectRole.options[objSelectRole.selectedIndex].value);
					objSelectRole.remove(objSelectRole.selectedIndex);
				}						
			}
			
			//保存首页管理权限
			function submitFunction() {
				var objSelectOrg = document.getElementById("tempSendTo_org");
				var objSelectGroup = document.getElementById("tempSendTo_group");
				var objSelectRole = document.getElementById("tempSendTo_role");
				var objSelect = document.getElementById("tempSendTo");
				
				var foramulaId=document.getElementById("foramulaId").value;
				var ret = "[";
				
				var boo=false;
				
				
				// 机构类型： 1   ，岗位类型： 2   ，角色类型：3    ，人员类型：  4
				
				//获取所有选择的组织结构
				for(var j=0; j<objSelectOrg.length; j++){				    
					boo=true;
					ret =ret+ "{relationId:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privilsort:'1'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privilsortId:'"+objSelectOrg.options[j].value+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"privilsortName:'"+objSelectOrg.options[j].text+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"foramulaId:'"+foramulaId+"'";
				 	ret =ret + "},";
				}		
				
				
				//获取所有选择的岗位
<%--				for(var i = 0;i<objSelectGroup.length;i++){
					boo=true;
					ret =ret+ "{relationId:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privilsort:'2'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privilsortId:'"+objSelectGroup.options[i].value+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"privilsortName:'"+objSelectGroup.options[i].text+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"foramulaId:'"+foramulaId+"'";
				 	ret =ret + "},";
				}--%>
				
				
				
				//获取所有选择的角色
				for(var j=0; j<objSelectRole.length; j++){				    
					boo=true;
					ret =ret+ "{relationId:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privilsort:'3'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privilsortId:'"+objSelectRole.options[j].value+"'";
				 	ret = ret +",";
				 	
				 	
				 	ret = ret +"privilsortName:'"+objSelectRole.options[j].text+"'";
				 	ret = ret +",";
				 	
				 	
				 	ret = ret +"foramulaId:'"+foramulaId+"'";
				 	ret =ret + "},";
				}						
				
				//获取所有选择的系统人员
				for(var j=0; j<objSelect.length; j++){				    
					boo=true;
					ret =ret+ "{relationId:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privilsort:'4'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privilsortId:'"+objSelect.options[j].value+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"privilsortName:'"+objSelect.options[j].text+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"foramulaId:'"+foramulaId+"'";
				 	ret =ret + "},";
					
				}
				
				ret = ret.substring(0,ret.length-1);
				ret = ret + "]";
				if(boo){
					//$("#selectData").val(ret);
					//myTableForm.submit();
					
					//var privalSort=document.getElementById("privalSort").value;
					//var url;
					//if(privalSort=="desktopPortal"){
					//	url="";
					//}else{
					//	url="<%=path%>/viewmodel/modelPrival!save.action";
					//}
					
					 $.post("<%=path%>/viewmodel/modelPrival!save.action",
		             { 'selectData':ret,'foramulaId':foramulaId},
		              function(data){
		              
		              if(data=="sucess"){
		               alert("权限设置成功！");
		               window.returnValue="suc";
		               window.close();
		              }else{
		                 alert("权限设置失败！")
		                 
		                }
		       });
					
					
				}else{
					if(confirm("确认当前首页模块不设置权限！")){
						 $.post('<%=path%>/viewmodel/modelPrival!save.action',
				             { 'selectData':ret,'foramulaId':foramulaId},
				              function(data){
				              
				              if(data=="sucess"){
				               alert("权限设置成功！")
				               window.close();
				              }else{
				                 alert("权限设置失败！")
				                 
				                }
				       });
					}
					
				}
				
			}
			
			
			//保存门户管理权限
			function submitProtal(){
				var objSelectOrg = document.getElementById("tempSendTo_org");
				var objSelectGroup = document.getElementById("tempSendTo_group");
				var objSelectRole = document.getElementById("tempSendTo_role");
				var objSelect = document.getElementById("tempSendTo");
				
				var portalId=document.getElementById("portalId").value;
				var ret = "[";
				
				var boo=false;
				
				
				// 机构类型： 1   ，岗位类型： 2   ，角色类型：3    ，人员类型：  4
				
				//获取所有选择的组织结构
				for(var j=0; j<objSelectOrg.length; j++){				    
					boo=true;
					ret =ret+ "{id:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privalType:'1'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privalId:'"+objSelectOrg.options[j].value+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"privalName:'"+objSelectOrg.options[j].text+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"portalId:'"+portalId+"'";
				 	ret =ret + "},";
				}		
				
				
				
				//获取所有选择的角色
				for(var j=0; j<objSelectRole.length; j++){				    
					boo=true;
					ret =ret+ "{id:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privalType:'3'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privalId:'"+objSelectRole.options[j].value+"'";
				 	ret = ret +",";
				 	
				 	
				 	ret = ret +"privalName:'"+objSelectRole.options[j].text+"'";
				 	ret = ret +",";
				 	
				 	
				 	ret = ret +"portalId:'"+portalId+"'";
				 	ret =ret + "},";
				}						
				
				//获取所有选择的系统人员
				for(var j=0; j<objSelect.length; j++){				    
					boo=true;
					ret =ret+ "{id:''";
				 	ret=ret+ ",";
				 	
				 	ret =ret+ "privalType:'4'";
				 	ret=ret+ ",";
				 	
				 	ret = ret +"privalId:'"+objSelect.options[j].value+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"privalName:'"+objSelect.options[j].text+"'";
				 	ret = ret +",";
				 	
				 	ret = ret +"portalId:'"+portalId+"'";
				 	ret =ret + "},";
					
				}
				
				ret = ret.substring(0,ret.length-1);
				ret = ret + "]";
				if(boo){
					//$("#selectData").val(ret);
					//myTableForm.submit();
					
					//var privalSort=document.getElementById("privalSort").value;
					//var url;
					//if(privalSort=="desktopPortal"){
					//	url="";
					//}else{
					//	url="<%=path%>/desktop/protalPrival!save.action";
					//}
					
					 $.post("<%=path%>/desktop/protalPrival!save.action",
		             { 'selectData':ret,'portalId':portalId},
		              function(data){
		              
		              if(data=="sucess"){
		               alert("权限设置成功！");
		               window.returnValue="suc";
		               window.close();
		              }else{
		                 alert("权限设置失败！")
		                 
		                }
		       });
					
					
				}else{
					if(confirm("确认当前首页模块不设置权限！")){
						 $.post('<%=path%>/viewmodel/modelPrival!save.action',
				             { 'selectData':ret,'foramulaId':foramulaId},
				              function(data){
				              
				              if(data=="sucess"){
				               alert("权限设置成功！")
				               window.close();
				              }else{
				                 alert("权限设置失败！")
				                 
				                }
				       });
					}
					
				}
			}
			
			
			function cancelFunction() {
				window.close();
			}
			function submit1(){
				var portalId=document.getElementById("portalId").value;
				var foramulaId=document.getElementById("foramulaId").value;
				if($.trim(foramulaId)!=null&&""!=$.trim(foramulaId)&&"null"!=$.trim(foramulaId)){
					submitFunction();
				}else{
					submitProtal();
				}
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
			      	document.all.selectsTree.src = "<%=path%>/viewmodel/modelPrival!chooseTree.action?flag=pos";
			    } else if(treeFlag == 'Per'){
			    	//获取具体人员
			      	document.all.selectsTree.src = "<%=path%>/viewmodel/modelPrival!chooseTree.action?flag=per";
			    } else if(treeFlag == 'Org' ){
			    	//获取组织机构
			    	document.all.selectsTree.src = "<%=path%>/viewmodel/modelPrival!chooseTree.action?flag=org";
			    }
			     else if(treeFlag == 'role' ){
			    	//获取组织机构
			    	document.all.selectsTree.src = "<%=path%>/viewmodel/modelPrival!chooseTree.action?flag=role";
			    }
			}
			
			//初始化人员选择
			function initSelect(drusers, drusernames) {
                var druser = drusers.split("|");
                var drusername = drusernames.split("|");
                for (var i=0; i<druser.length; i++) {
                	addOption("person",drusername[i], druser[i],0);
                }
            }
            //初始化角色选择
			function initSelectRole(droles, drolenames) {
                var drole = droles.split("|");
                var drolename = drolenames.split("|");
                for (var i=0; i<drole.length; i++) {
                	addOption("role",drolename[i], drole[i]);
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
                    addOrgOption(dorgname[i], dorg[i],dorg.length);
                }
            }            
            
            //初始化frame中checked选择
            function frameInitChecked(flag){
            	var page = document.frames[0];
            	if(flag == 'modelPrival-orgtree'){
            		var objSelect = document.getElementById("tempSendTo_org");
            		page.initChecked(objSelect);
            	}
            	else if(flag == 'modelPrival-postree'){
            		var objSelect = document.getElementById("tempSendTo_group");
            		page.initChecked(objSelect);
            	}else if(flag == 'modelPrival-roletree'){
            		var objSelect = document.getElementById("tempSendTo_role");
            		page.initChecked(objSelect);
            	}else if(flag == 'modelPrival-pertree'){
                    var objSelect = document.getElementById("tempSendTo");
            		page.initChecked(objSelect);
            	}
            }            
	    </script>
	
	</head>
	<% 
	 	String foramulaId=(String) request.getParameter("foramulaId");
	 	String portalId=(String) request.getParameter("portalId");
	 %>
	<body class="contentbodymargin">
		
		<div id="contentborder" cellpadding="0">
		
			<center>
				<s:form action="/viewmodel/modelPrival!save.action" id="myTableForm" theme="simple">
				<table border="0" cellspacing="0" width="100%" height="100%" cellpadding="0">
					
					<input type="hidden" id="privalSort" name="privalSort" value="${privalSort}">
					<input type="hidden" id="relationId" name="model.relationId" value="${model.relationId}">
					<input type="hidden" id="foramulaId" name="foramulaId" value="<%=foramulaId%>" />
					<input type="hidden" id="portalId" name="portalId" value="<%=portalId%>" />
					<input type="hidden" id="selectData" name="selectData">
		    <tr>
				<td colspan="3" class="table_headtd">
					<table width="100%" border="0" cellspacing="0" cellpadding="00">
						<tr>
							<td class="table_headtd_img" >
								<img src="<%=frameroot%>/images/ico/ico.gif" >&nbsp;
							</td>
							<td align="left">
								<strong>请选择</strong>
							</td>
							<td align="right">
								<table border="0" align="right" cellpadding="00" cellspacing="0">
					                <tr>
					                 	<td width="7"><img src="<%=frameroot%>/images/ch_h_l.gif"/></td>
					                 	<td class="Operation_input" onClick="submit1();">&nbsp;确&nbsp;定&nbsp;</td>
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
						<td style="padding-left:10px;">
						<a  href="#" class="button" onClick="changeTree('Org');">按机构</a>&nbsp;
							
<%--							<input type="radio" name="tree" onclick="changeTree('Pos');"
								value="Pos" />
							按岗位--%>
							<a  href="#" class="button" onClick="changeTree('role')">按角色</a>&nbsp;
							
							<a  href="#" class="button" onClick="changeTree('Per')">按人员</a>&nbsp;
							
							

						</td>
					</tr>
					<tr>
						<td id="treeIframe">
                        <div style="padding-left:5px; padding-top:15px;">
							<table border="0" width="100%" bordercolor="#FFFFFF"
								cellspacing="0" cellpadding="0" height="100%">
								<tr>
									<td width="47%" align="middle">
										<table border="0" width="95%" height="95%"
											bordercolor="#FFFFFF" cellspacing="0" cellpadding="0">
											<tr>
												<td width="100%" height="100%" valign="top">
													<div id="SelectsDiv" >
														<iframe name="selectsTree"
															src="<%=path%>/viewmodel/modelPrival!chooseTree.action?flag=org"
															frameborder="0" scrolling='auto' width='100%' height='100%'>
														</iframe>
													</div>
												</td>
											</tr>
										</table>
									</td>
									<td width="3%" align="middle">
									<a  href="#" class="button" onClick="deleteOptionItem()">&lt;&lt;--</a>
										
									</td>
									<td width="45%" align="middle" valign="top">
										<select name="select_org" size="100" multiple="multiple"
											id="tempSendTo_org" style="width: 180px; height: 175px">
										</select>
<%--										<select name="select_group" size="100" multiple="multiple"
											id="tempSendTo_group" style="width: 180px; height: 130px">
										</select>--%>
										<select name="select_role" size="100" multiple="multiple"
											id="tempSendTo_role" style="width: 180px; height: 175px">
										</select>
										<select name="select" size="100" multiple="multiple"
											id="tempSendTo" style="width: 180px; height: 175px">
										</select>
										<script type="text/javascript">
                                                        var parentWin = window.dialogArguments;
                                                       	
                                                       	var orgOps = "";
                                                       	var orgOpValue = "";
                                                       	var posOps = "";
                                                       	var posOpValue = "";
                                                       	
                                                       	var roleOps="";
                                                       	var roleValue="";
                                                       	
                                                       	var perOps = "";
                                                       	var perOpValue = "";
                                                       	
                                                        var value = "${selectedData}";
                                                        
                                                        //var pageType = "${pageType}";
<%--                                                        if(pageType == "owner"){
                                                        	value = parentWin.tempOwner;
                                                        }else if(pageType == "designer"){
                                                        	value = parentWin.tempDesigner;
                                                        }else if(pageType == "startor"){
                                                        	value = parentWin.tempStartor;
                                                        }--%>
														if(value!=null&&value != ""&&value!="null"){
															var startorDetails;
															var startors = value.split(",");
															for(var i=0; i<startors.length; i++){
																startorDetails = startors[i].split("|");
																var flag = startorDetails[0];
																if(flag == "1"){
																	orgOps = orgOps + startorDetails[1] + "|";
																	orgOpValue = orgOpValue + startorDetails[2] + "|";
																}else if(flag == "2"){
																	
																	posOps = posOps + startorDetails[1] + "|";
																	posOpValue = posOpValue + startorDetails[2] + "|";				
																}else if(flag == "3"){
																	
																	roleOps = roleOps + startorDetails[1] + "|";
																	roleValue = roleValue + startorDetails[2] + "|";
																}else if(flag == "4"){
																	var personId="u"+startorDetails[1];
																	perOps = perOps + personId + "|";
																	perOpValue = perOpValue + startorDetails[2] + "|";
																}
															}
														}
														
														if(orgOps != ""){
															initSelectOrg(orgOps.substring(0,orgOps.length-1), orgOpValue.substring(0,orgOpValue.length-1));
														}
														
														if(posOps != ""){
															initSelectGroup(posOps.substring(0,posOps.length-1), posOpValue.substring(0,posOpValue.length-1));
														}                  
                                                       
                                                       	if(roleOps != ""){
                                                       		initSelectRole(roleOps.substring(0,roleOps.length-1), roleValue.substring(0,roleValue.length-1));
                                                       	}
                                                       	
                                                       if(perOps != ""){
                                                       		initSelect(perOps.substring(0,perOps.length-1), perOpValue.substring(0,perOpValue.length-1));
                                                       	}
                                                    </script>
									</td>
								</tr>
							</table>
                        </div>    
						</td>
					</tr>
				</table>
				</s:form>
			</center>
		</div>
	</body>
</html>
